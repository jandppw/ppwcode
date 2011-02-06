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


Microsoft SharePoint
--------------------

Version: Microsoft SharePoint Foundation 2010, version 14.0.4536.1000

You need both
* Microsoft.SharePoint.Client.dll, and
* Microsoft.SharePoint.Client.Runtime.dll

These DLL's are part of the Microsoft SharePoint 2010 distribution.
"Microsoft SharePoint Foundation 2010 installs Microsoft.SharePoint.Client.dll
 and Microsoft.SharePoint.Client.Runtime.dll in
 %ProgramFiles%\Common Files\Microsoft Shared\web server extensions\14\ISAPI
 for easy access in development."

You need to copy those files here for this solution to build.
