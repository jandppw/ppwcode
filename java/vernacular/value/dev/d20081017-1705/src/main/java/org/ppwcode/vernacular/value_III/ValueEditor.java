/*<license>
Copyright 2004 - $Date: 2008-08-31 16:12:37 +0200 (Sun, 31 Aug 2008) $ by PeopleWare n.v..

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

package org.ppwcode.vernacular.value_III;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.beans.PropertyEditor;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;


/**
 * <p>An interface for good value type property editors.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-08-31 16:12:37 +0200 (Sun, 31 Aug 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2511 $",
         date     = "$Date: 2008-08-31 16:12:37 +0200 (Sun, 31 Aug 2008) $")
@Invars({
  @Expression("valueType != null"),
  @Expression("asText != null ?? valueType.isInstance(value)")
})
public interface ValueEditor<_Value_> extends PropertyEditor {


  /**
   * <p>The type we are an editor for. If the editor follows the normal naming scheme,
   *   <code>getClass().toString().equals(getEnumType().getDeclaringClass().toString() + "Editor")</code>.</p>
   * <p>This is not mandatory, but enables the automatic finding of the editor by IDE's and other tools.
   *   Thus, this naming scheme is highly recommended.</p>
   */
  @Basic
  Class<_Value_> getValueType();

}
