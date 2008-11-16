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

package org.ppwcode.value_III.ext.java.util;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.deadBranch;

import java.util.Locale;

import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.openjpa.AbstractEnumerationValueValueHandler;


/**
 * An OpenJPA {@link ValueHandler} for {@link Locale}. We store the locale as its String
 * representation in a VARCHAR
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$", date = "$Date$")
public class LocaleValueHandler extends AbstractEnumerationValueValueHandler {

  public LocaleValueHandler() {
    super(Locale.class, 20);
  }

  public final static String SEPARATOR = "_";

  /**
   * Since not all locales possible are in the values map of the {@link LocaleEditor},
   * we should recreate what we need ourselves.
   */
  @Override
  public Object toObjectValue(ValueMapping vm, Object val) {
    try {
      return super.toObjectValue(vm, val);
    }
    catch (IllegalArgumentException iaExc) {
      assert val instanceof String;
      String stringVal = (String)val;
      String[] valParts = stringVal.split(SEPARATOR);
      switch (valParts.length) {
        case 1:
          return new Locale(valParts[0]);
        case 2:
          return new Locale(valParts[0], valParts[1]);
        case 3:
          return new Locale(valParts[0], valParts[1], valParts[2]);
        default:
          deadBranch("somebody hacked the database: locale string contains more than 3 parts");
      }
    }
    return null; // keep compiler happy
  }

}
