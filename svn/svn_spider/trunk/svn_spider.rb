#! /usr/bin/env ruby

#   Copyright 2007 - $Date$ by PeopleWare n.v.
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#
#   $Date$
#   $Revision$
#   $HeadURL$

#
# Script to fix stuff in our Subversion repositories
#
#  - setting the correct mime-type
#  - setting svn:keywords
#
# Ruben Vandeginste
#


# change load_path
# $LOAD_PATH << File.join(File.dirname(__FILE__), './lib')

# using subversion library
require "svn/repos"

# subversion repo revision number
revision = "$Rev$"
SVN_SPIDER_REVISION = revision.split[1].strip

# log message
#   this is kept simple on purpose
@@log_msg =  "svn_spider [r#{SVN_SPIDER_REVISION}]\n\n"

# file locations
@@file_locations = {
  "mime_type_registry.txt" => File.join(File.dirname(__FILE__), "mime_type_registry.txt"),
  "config.rb" => File.join(File.dirname(__FILE__), "config.rb")
}

# initialize mime registry
mime_type_registry = Hash.new
File.readlines(@@file_locations["mime_type_registry.txt"]).map{|l| l.split.map{|e| e.strip}}.each{|l| mime_type_registry[l[0]]=l[1]}

# load config + initialization
@@mime_types = {}
@@property_filters = {}
@@repositories = {}
@@credentials = {}
@@working_directory = ""
load @@file_locations["config.rb"]
@@mime_types.each_pair do |key, val|
  mime_type_registry[key] = val
end

# consistency check
# todo

# do the actual checking and fixing for each repository
@@repositories.each_pair do |repo, info|

  begin

    # create a client context
    ctx = Svn::Client::Context.new

    # ssl certificate verification: accept certificates that have been accepted before
    ctx.add_ssl_server_trust_file_provider

    # authentication
    if ((info["auth"] == nil) || (@@credentials[info["auth"]] == nil)) then
      # maybe svn already has the authentication info
      ctx.add_simple_provider
    else
      # username + password for authentication
      ctx.add_simple_prompt_provider(0) do |cred, realm, username, may_save|
        cred.username = @@credentials[info["auth"]]["username"]
        cred.password = @@credentials[info["auth"]]["password"]
      end
    end

    # log message configuration
    @@log = @@log_msg.clone
    @@log << "working root:\n"
    @@log << "  " << info["url"] << "\n\n"
    @@log << "fixes:\n"
    ctx.set_log_msg_func do |items|
      [true, @@log]
    end

    # check out a project, or update it if a working copy is found
    wc_path = File.join @@working_directory,  repo
    if File.exists? wc_path then
      ctx.update wc_path
    else
      ctx.checkout info["url"], wc_path
    end

    # walk the file tree and update property lists
    ctx.list( wc_path, "HEAD", nil, true) do |path, dirent, lock, abs_path|
      # path is path of file relative to wc_path
      # abs_path is location of the root of path
      #        relative to the repository root
      if not dirent.directory? then
        new_props = {}
        # check mime type
        ext = File.extname(path)
        mime = (ext == nil) ? nil : mime_type_registry[ext[1..-1]]
        if ((not ext.empty?) && (mime != nil)) then
          new_props[Svn::Core::PROP_MIME_TYPE] = mime
        end
        # check property filters
        @@property_filters.each_pair do |key, value|
          if path =~ key then
            new_props.merge! value
          end
        end
        # fetch the current property list
        wc_file_path = File.join wc_path, path
        old_props = {}
        if dirent.have_props? then
          props = ctx.proplist wc_file_path
          # should not be empty since we checked that is has properties
          if not props.empty? then
            # only 1 entry since we take one file at a time
            props.each do |pli|
              old_props.merge! pli.props
            end
          end
        end
        # compare property lists and update new_props
        new_props.clone.each_key do |k|
          new_props.delete k if new_props[k] == old_props[k]
        end
        # update the property list in the working copy
        if not new_props.empty? then
          @@log << "  " << path << "\n"
        end
        new_props.each_pair do |key, value|
          ctx.propset key, value, wc_file_path
          # update log
          if (old_props[key] != nil) then
            @@log << "    -" << key << ":"
            @@log << (" " * [(20 - key.size), 1].max)
            @@log << old_props[key] << "\n"
          end
          @@log << "    +" << key << ":"
          @@log << (" " * [(20 - key.size), 1].max)
          @@log << value << "\n"
        end
      end
    end

    # finally commit the changes in the working copy
    ctx.commit wc_path

    # error handling: just log it for now
  rescue Exception => e
    File.open File.join(File.dirname(__FILE__), "spider.log"), "w+" do |f|
      f.write "#{Time.now.getutc} :: error in repo: #{repo}\n"
      f.write "#{Time.now.getutc} :: Exception message: #{e.message}\n"
      f.write "#{Time.now.getutc} :: Exception backtrace: #{e.backtrace.inspect}\n"
    end
  end
end
