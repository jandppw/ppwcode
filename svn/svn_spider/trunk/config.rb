# Sample config file

# working dir for code checkouts
# @@working_directory = "/home/ruben/spider_work_dir"

# credentials
# @@credentials = {
#   "ppwcode" => {
#     "username" => "my_user_name",
#     "password" => "my_password"
#   }
# }

# repositories to check and fix
# @@repositories = {
#   "persistence" => {
#     "url" => "https://ppwcode.googlecode.com/svn/java/vernacular/persistence/trunk",
#     "auth" => "ppwcode"
#   },
#   "metainfo" => {
#     "url" => "https://ppwcode.googlecode.com/svn/java/metainfo/trunk",
#     "auth" => "ppwcode"
#   } 
# }

# mime types (this will override the mime_registry already included)
# @@mime_types = {
#   # exact extension and corresponding mime type
#   # WARNING: mime-type should be "text/..." for non-binary 
#   #   files (otherwise subversion will 'likely' treat it as
#   #   a binary file)
#   "java" => "text/x-java-source",
#   "xml" => "text/xml"
# }

# mapping of file patterns to need-to-set properties
# @@property_filters = {
#   # regular expression for file name
#   # java files
#   /\.java$/ => {
#     Svn::Core::PROP_EOL_STYLE => "native",
#     Svn::Core::PROP_KEYWORDS  => "Date Rev"
#   },
#   # xml files
#   /\.xml$/ => {
#     Svn::Core::PROP_EOL_STYLE => "native",
#     Svn::Core::PROP_KEYWORDS  => "Date Rev Id URL"
#   }
# }
