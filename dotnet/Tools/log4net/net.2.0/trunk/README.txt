Dependencies
------------

This directory is intended to be used as svn:external in .net projects.
It contains the definition of a dependency on an external product.

These dependencies are needed for compilation and build, and for running
the code. When you use (parts of) this solutions in other products,
you need the artifacts this solution depends on, recursively, also in
the other product.

When possible, a copy of the required files is kept in this directory
in the repository, so that developers can get started easily. However,
for some dependencies, license issues prohibit us to distribute the
dependencies ourselfs. The developer needs to retrieve them from the
original source himself. In this case, this document describes how to
get the required files.


log4net
-------

Version: 1.2.10, .net 2.0

log4net is an Open Source project, released under the
Apache License v2.

The project home is at
http://logging.apache.org/log4net/

