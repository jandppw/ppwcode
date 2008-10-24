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


import static java.lang.Enum.valueOf;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.value_III.EnumHelpers.valuesMap;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Implementations of a number of methods of {@link EnumEditor}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractEnumEditor<_Enum_ extends Enum<_Enum_>> extends AbstractValueEditor<_Enum_>
    implements EnumEditor<_Enum_>, Serializable {

  private static final String EMPTY = "";

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
   */
  @MethodContract(post = @Expression("valuesMap(getEnumType())"))
  public final Map<String, _Enum_> getValuesMap() {
    return valuesMap(getValueType());
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
    _Enum_ originalValue = getValue();
    for (_Enum_ value : getValuesMap().values()) {
      setValue(value);
      result.put(getAsText(), getLabel());
    }
    // restore the original value
    setValue(originalValue);
    return result;
  }

  /*</property>*/




  /*<property name="value">*/
  //------------------------------------------------------------------

  @Override
  @Basic(init = @Expression("null"))
  public final _Enum_ getValue() {
    @SuppressWarnings("unchecked") _Enum_ result = (_Enum_)super.getValue();
    return result;
  }

  /*</property>*/



  /*<property name="asText">*/
  //------------------------------------------------------------------

  @Override
  @MethodContract(post = @Expression("value == null ? null : value.name()"))
  public final String getAsText() {
    String result = (getValue() == null) ? null : getValue().name();
    return result;
  }

  @Override
  @MethodContract(
    post = @Expression("(_text == null || _text == EMPTY) ? " +
                         "value == null : " +
                         "value == valuesMap.get(_text)"),
    exc  = @Throw(type = IllegalArgumentException.class,
                  cond = @Expression("_text != null && _text != EMPTY && valuesMap.get(_text) == null"))
  )
  public final void setAsText(final String text) throws IllegalArgumentException {
    if ((text == null) || EMPTY.equals(text)) {
      setValue(null);
    }
    else {
      try {
        _Enum_ value = valueOf(getValueType(), text);
        // NullPointerException, IllegalArgumentException passes through
        setValue(value);
      }
      catch (NullPointerException npExc) {
        throw new IllegalArgumentException("the enum type cannot be null");
      }
    }
  }

  /*</property>*/

}
