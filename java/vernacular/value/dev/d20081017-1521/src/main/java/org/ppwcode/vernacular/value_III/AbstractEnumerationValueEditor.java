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
import static org.ppwcode.util.reflect_I.ConstantHelpers.constant;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Scope;
import org.toryt.annotations_I.Throw;


/**
 * <p>Implementations of a number of methods of {@link EnumerationValueEditor}.</p>
 * <p>This implementation depends on the fact that the enumeration type implements
 *   <code>toString()</code> to return the tag for that value, as requested by {@code EnumerationValue}.</p>
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
public abstract class AbstractEnumerationValueEditor extends AbstractValueEditor<Object>
    implements EnumerationValueEditor, Serializable {

  /*<property name="tags">*/
  //------------------------------------------------------------------

  /**
  * If the property value must be one of a set of known tagged values, then
  * this method should return an array of the tag values.
  *
  * @see     PropertyEditorSupport
  */
  @Override
  @MethodContract(post = @Expression("valuesMap.keySet().toArray()"))
  public final String[] getTags() {
    Set<String> tags = getValuesMap().keySet();
    return tags.toArray(new String[tags.size()]);
  }

  /*</property>*/



  /*<property name="valuesMap">*/
  //------------------------------------------------------------------

  /**
   * A map that contains all the tags, associated with the instance
   * the tag represents.
   *
   * @protected
   * It would be wise for the implementation of this method to return
   * a statically constructed Map.
   */
  @MethodContract(post = @Expression(scope = Scope.PROTECTED,
                                     value = "constant(getEnumerationValueType(), 'VALUES')"))
  public Map<String, ?> getValuesMap() {
    return constant(getValueType(), "VALUES");
  }

  /*</property>*/



  /*<property name="labelsMap">*/
  //------------------------------------------------------------------

  /**
   * @protected
   * This implementation builds the labels map based on information in the {@link #getValuesMap()}
   * and uses the method {@link #getLabel()}.
   *
   * @mudo contracts and test
   */
  public final Map<String, String> getLabelsMap() {
    Map<String, String> result = new HashMap<String, String>();
    /* we are going to use this object itself to generate the labels
       let's store the value for a while */
    Iterator<?> iter = getValuesMap().values().iterator();
    Object originalValue = getValue();
    while (iter.hasNext()) {
      setValue(iter.next());
      result.put(getAsText(), getLabel());
    }
    // restore the original value
    setValue(originalValue);
    return result;
  }

  /*</property>*/


  private static final String EMPTY = "";


  /*<property name="asText">*/
  //------------------------------------------------------------------

  /**
   * @note In the previous version, we had code that returned one SPACE when the value
   *       is null. This is strange. We suppose this was introduced at some time, but the
   *       documentation nor the VCS history gives any explanation. This was like since the
   *       first version committed (r137). The name (SPACE) suggests that the reason was
   *       something in HTML in the first version which was used in Struts.
   *       The {@link PropertyEditor#getAsText()} documentation itself is vague about what
   *       to do with a null value. When it turns out in use later that we cannot return
   *       null in {@code getAsText} for some reason, it would be better to return a meaningful
   *       {@code NULL} String or something than a space probably.
   */
  @Override
  @MethodContract(post = @Expression("value == null ? null : value.toString()"))
  public final String getAsText() {
    String result = (getValue() == null) ? null : getValue().toString();
    return result;
  }

  /**
   * @note We also presume, but {@link PropertyEditor#setAsText(String)} is unclear,
   *       that {@code setAstText(null)} should result in a {@code null} value.
   *       To the letter, {@code null} is not a valid tag, and this we would throw
   *       an {@link IllegalArgumentException}. The same applies to the empty string.
   */
  @Override
  @MethodContract(
    post = @Expression("(_text == null || _text == EMPTY) ? " +
                         "value == null : " +
                         "value == valuesMap.get(_text)"),
    exc  = @Throw(type = IllegalArgumentException.class,
                  cond = @Expression("_text != null && _text != EMPTY && valuesMap.get(_text) == null"))
  )
  public final void setAsText(final String text) throws IllegalArgumentException {
    if ((text == null) || text.equals(EMPTY)) {
      setValue(null);
    }
    else {
      Object result = getValuesMap().get(text);
      if (result == null) {
        throw new IllegalArgumentException("\"" + text + "\" was not recognized as a tag");
      }
      setValue(result);
    }
  }

  /*</property>*/

}
