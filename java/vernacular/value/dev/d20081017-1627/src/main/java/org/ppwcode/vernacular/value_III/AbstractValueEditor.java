/*<license>
Copyright 2004 - $Date: 2008-09-22 00:31:18 +0200 (Mon, 22 Sep 2008) $ by PeopleWare n.v..

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
import static org.ppwcode.util.reflect_I.TypeHelpers.type;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Implementations of a number of methods for {@link PropertyEditor} classes.</p>
 * <p>There are no generics in this class, since we inherit from {@link PropertyEditor}, which is pre-Java 5 and not
 *   retrofitted in the JDK, and since this class is only ment for legacy use of old ppw-value libraries-based
 *   values.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-09-22 00:31:18 +0200 (Mon, 22 Sep 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2599 $",
         date     = "$Date: 2008-09-22 00:31:18 +0200 (Mon, 22 Sep 2008) $")
public abstract class AbstractValueEditor<_Value_> extends PropertyEditorSupport implements ValueEditor<_Value_>, Serializable {

  /**
   * The value type this is an editor for.
   *
   * @protected
   * <p>This default implementation assumes that we follow the property editor naming scheme, and that
   *   <code>getClass().toString().equals(getValueType().getClass().toString() + "Editor")</code>.</p>
   */
  @MethodContract(post = @Expression("type(getExpectedValueTypeClassName())"))
  public Class<_Value_> getValueType() {
    return type(getExpectedValueTypeClassName());
  }

  @MethodContract(post = @Expression("class.getName().substring(0, class.getName().lastIndexOf('Editor'))"))
  public final String getExpectedValueTypeClassName() {
    String me = getClass().getName();
    return me.substring(0, me.lastIndexOf("Editor"));
  }

}
