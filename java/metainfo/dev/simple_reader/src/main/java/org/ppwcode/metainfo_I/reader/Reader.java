/*<license>
Copyright 2008 - $Date$ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.ppwcode.metainfo_I.reader;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.SimpleRepresentation;
import org.ppwcode.metainfo_I.vcs.SimpleVcsRepresentation;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Simple command line program to show meta information for a file.
 * Arguments are a list of FQCN's.
 */
@Copyright("2007 - $Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 857 $",
         date     = "$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $")
public class Reader {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("need at least 1 FQCN as argument");
      System.exit(-1);
    }
    for (String fqcn : args) {
      try {
        Class<?> c = _Classes.loadForName(fqcn);
        System.out.println(c.toString());
        SimpleRepresentation.copyright(c, System.out);
        SimpleRepresentation.license(c, System.out);
        SimpleVcsRepresentation.vcsInfo(c, System.out);
      }
      catch (CannotGetClassException exc) {
        System.err.println(fqcn + " is not a valid fully qualified class name");
        System.err.println();
      }
      System.out.println();
    }
  }

}

