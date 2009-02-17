/*<license>
Copyright 2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $ by PeopleWare n.v..

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

package org.ppwcode.util.smallfries_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Support for multi-line toString implementation, notably indentation.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public final class MultiLineToStringUtil {

  private MultiLineToStringUtil() {
    // NOP
  }

  public static String EMPTY = "";
  public static String INDENT_1 = "  ";
  public static String INDENT_2 = "    ";
  public static String INDENT_3 = "      ";
  public static String INDENT_4 = "        ";
  public static String INDENT_5 = "          ";

  /**
   * {@code level}-times 2 spaces: the indentation of level {@code level}.
   */
  public static String indent(int level) {
    switch (level) {
      case 0:
        return EMPTY;
      case 1:
        return INDENT_1;
      case 2:
        return INDENT_2;
      case 3:
        return INDENT_3;
      case 4:
        return INDENT_4;
      case 5:
        return INDENT_5;
      default:
        return INDENT_1 + indent(level - 1);
    }
  }

  /**
   * Multi-line version of {@link Object#toString()}, showing the class
   * name and the hash code.
   */
  public static void objectToString(Object o, StringBuffer sb, int level) {
    sb.append(indent(level) + o.getClass().getName() + "\n");
    sb.append(indent(level + 1) + "hash code: " + o.hashCode() + "\n");
  }

}

