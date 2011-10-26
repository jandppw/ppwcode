Dependencies
------------

This package is a package that holds the Microsoft SharePoint Client 
libraries. It is needed for compilation and build of PPWCode.SharePoint.Util
and for building and running all code that depends on the latter.

When possible, a copy of the required files is kept in the src directory
in the repository, so that developers can get started easily. However,
for some dependencies, license issues prohibit us to distribute the
dependencies ourselfs. The developer needs to retrieve them from the
original source himself. In this case, this document describes how to
get the required files.

The required files need to be placed in the src directory. Then the
Microsoft.SharePoint.Client package can be build and deployed to a
private OpenWrap repository. Any other solution that depends on
PPWCode.SharePoint.Util will from that point on be able to resolve
the dependency on Microsoft.SharePoint.Client.

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

You need to copy those files to the src directory for this package to build.
