/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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
import java.util.Map;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>An interface that adds support for i18n for <em>legacy enumeration</em> types.</p>
 * <p>Property editors for enumeration type implement the {@link #getAsText()} and {@link #setAsText(String)} and
 *   {@link #getTags()} methods. These methods process strings used by programmers in some circumstances as the
 *   representation of the enumeration type values. The tags are the <dfn>programmatic String representation</dfn>
 *   of values of the enumeration type. All possible tags are returned by {@link #getTags()}.</p>
 * <p>These programmatic representations of the enumeration type values however are not what we show to end users.
 *   Combo boxes, pop-up menu's radio buttons, and HTML select tags should show an i18n label, but internally should
 *   work with the programmatic representation of the property values.<br />
 *   Normally, a formatter (see {@link java.text.Format} should be used for i18n, but formatters require the
 *   implementation of both a format method for presentation, and a parse method for input. The latter is not
 *   applicable here. Therefor, we bypass formatters, and just extend the {@link java.beans.PropertyEditor} interface
 *   with a method {@link #getLabel()} to return i18n labels for an enumeration type value supported by
 *   {@link #getTags()}.</p>
 * <p>To make the building of combo boxes, pop-up menu's or radio buttons easier, a {@link #getLabelsMap() map} is
 *   offered that contains all tag / label combinations.</p>
 * <p>There are no generics in this class, since we inherit from {@link PropertyEditor}, which is pre-Java 5 and not
 *   retrofitted in the JDK, and since this class is only ment for legacy use of old ppw-value libraries-based
 *   values.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("asText != null ? tags.contains(asText)"),
  @Expression("labelsMap != null"),
  @Expression("! labelsMap.keySet().contains(null)"),
  @Expression("! labelsMap.values().contains(null)")
})
public interface EnumerationValueEditor extends ValueEditor<Object> {


  /**
   * The programmatic String representations of values of {@link #getValueType()}.
   */
  @MethodContract(post = @Expression("labelsMap.keySet().toArray()"))
  String[] getTags();

  @MethodContract(
    post = @Expression(value = "asText == (_tag == EMPTY ? null : _tag)", description = "getValue() is changed so that getAsText() will return _tag"),
    exc  = @Throw(type = IllegalArgumentException.class,
                  cond = @Expression("! tags.contains(tag)"))

  )
  void setAsText(String tag) throws IllegalArgumentException;

  /**
   * Return an i18n label for {@link #getValue()}. This should return a valid label for all entries in
   * {@link #getTags()}. <code>null</code> is returned if no label is found for the value.
   */
  @MethodContract(
    post = @Expression("labelsMap[asText]")
  )
  String getLabel();

  /**
   * A map of programmatic tag / presentation label combinations for all possible values of the enumeration type.
   */
  @Basic
  Map<String, String> getLabelsMap();

}
