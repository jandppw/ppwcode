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

package org.ppwcode.metainfo_I;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.PrintStream;

import org.ppwcode.metainfo_I.vcs.SvnInfo;


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
public final class SimpleRepresentation {

  private SimpleRepresentation() {
    // NOP
  }

  /**
   * A simple string representation of the copyright of class {@code c}
   * written to {@code out}.
   *
   * @pre c != null;
   * @pre out != null;
   */
  public static void copyright(Class<?> c, PrintStream out) {
    assert c != null;
    assert out != null;
    out.print("copyright ");
    Copyright copyright = c.getAnnotation(Copyright.class);
    if (copyright == null) {
      out.println("unknown");
    }
    else {
      out.println(copyright.value());
    }
  }

  /**
   * A simple string representation of the license of class {@code c}
   * written to {@code out}.
   *
   * @pre c != null;
   * @pre out != null;
   */
  public static void license(Class<?> c, PrintStream out) {
    assert c != null;
    assert out != null;
    out.print("license: ");
    License license = c.getAnnotation(License.class);
    if (license == null) {
      out.println("unknown");
    }
    else {
      out.println(license.value().name() + " (" + license.value().getUrl() + ")");
    }
  }

}

