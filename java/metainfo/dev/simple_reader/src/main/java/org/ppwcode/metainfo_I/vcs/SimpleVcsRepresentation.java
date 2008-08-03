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

package org.ppwcode.metainfo_I.vcs;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.PrintStream;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;


/**
 * Static methods that offer a simple representation of meta
 * annotations found in this package.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 857 $",
         date     = "$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $")
public final class SimpleVcsRepresentation {

  private SimpleVcsRepresentation() {
    // NOP
  }

  /**
   * A simple string representation of the version control information (CVS) of class {@code c}
   * written to {@code out}.
   *
   * @pre c != null;
   * @pre out != null;
   */
  public static void cvsInfo(Class<?> c, PrintStream out) {
    assert c != null;
    assert out != null;
    out.println("CVS: ");
    CvsInfo info = c.getAnnotation(CvsInfo.class);
    if (info == null) {
      out.println("\tunknown");
    }
    else {
      out.println("\trevision: " + info.revision());
      out.println("\tdate: " + info.date());
      out.println("\tstat: " + info.state());
      out.println("\ttag: " + info.tag());
    }
  }

  /**
   * A simple string representation of the version control information (SVN) of class {@code c}
   * written to {@code out}.
   *
   * @pre c != null;
   * @pre out != null;
   */
  public static void svnInfo(Class<?> c, PrintStream out) {
    assert c != null;
    assert out != null;
    out.println("SVN: ");
    SvnInfo info = c.getAnnotation(SvnInfo.class);
    if (info == null) {
      out.println("\tunknown");
    }
    else {
      out.println("\trevision: " + info.revision());
      out.println("\tdate: " + info.date());
    }
  }

  /**
   * A simple string representation of the version control information of class {@code c}
   * written to {@code out}. If either cvs info or svn exists, it is written. If nor
   * cvs info or svn info exists, &quot;unknown&quot; is written.
   *
   * @pre c != null;
   * @pre out != null;
   */
  public static void vcsInfo(Class<?> c, PrintStream out) {
    assert c != null;
    assert out != null;
    SvnInfo svnInfo = c.getAnnotation(SvnInfo.class);
    if (svnInfo != null) {
      svnInfo(c, out);
    }
    CvsInfo cvsInfo = c.getAnnotation(CvsInfo.class);
    if (cvsInfo != null) {
      cvsInfo(c, out);
    }
    if ((svnInfo == null) && (cvsInfo == null)) {
      out.println("VCS: unknown");
    }
  }

}

