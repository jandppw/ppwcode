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
import static org.ppwcode.util.reflect_I.MethodHelpers.invoke;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * This class contains static convenience methods for working with {@code enums} ({@link Enum}).
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class EnumHelpers {

  /*<construction>*/
  //------------------------------------------------------------------

  private EnumHelpers() {
    // NOP
  }

  /*</construction>*/


  /**
   * A map that contains all the tags, associated with the instance the tag represents.
   */
  public static final <_Enum_ extends Enum<_Enum_>> Map<String, _Enum_> valuesMap(Class<_Enum_> enumType) {
    Map<String, ? extends Enum<?>> values = ENUM_VALUES.get(enumType);
    if (values == null) {
      return initValuesMap(enumType);
    }
    else {
      Map<String, _Enum_> result = new HashMap<String, _Enum_>();
      for (Map.Entry<String, ? extends Enum<?>> e : values.entrySet()) {
        @SuppressWarnings("unchecked") _Enum_ value = (_Enum_)e.getValue();
        result.put(e.getKey(), value);
      }
      return result;
    }
  }

  private static <_Enum_ extends Enum<_Enum_>> Map<String, _Enum_> initValuesMap(Class<_Enum_> enumType) {
    Map<String, _Enum_> result = new HashMap<String, _Enum_>();
    _Enum_[] values = invoke(enumType, "values()");
    for (_Enum_ value : values) {
      result.put(value.name(), value);
    }
    Map<String, _Enum_> safeResult = Collections.unmodifiableMap(result);
    ENUM_VALUES.put(enumType, safeResult);
    return safeResult;
  }

  private static final Map<Class<? extends Enum<?>>, Map<String, ? extends Enum<?>>> ENUM_VALUES =
      new HashMap<Class<? extends Enum<?>>, Map<String, ? extends Enum<?>>>();

}
