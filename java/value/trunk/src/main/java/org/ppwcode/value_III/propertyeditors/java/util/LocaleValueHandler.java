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

package org.ppwcode.value_III.propertyeditors.java.util;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.deadBranch;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.sql.Types;
import java.util.Locale;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.meta.JavaTypes;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers;


/**
 * An OpenJPA {@link ValueHandler} for {@link Locale}. We store the locale as its String
 * representation if a VARCHAR
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$", date = "$Date$")
public class LocaleValueHandler extends AbstractValueHandler {

  public final static String SEPARATOR = "_";

  public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    Column col = new Column();
    col.setName(name);
    col.setType(Types.VARCHAR);
    col.setJavaType(JavaTypes.STRING);
    col.setSize(20);
    return new Column[] {col};
  }

  @Override
  public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      Locale locale = (Locale)val;
      if (locale == null) {
        return null;
      }
      PropertyEditor pe = PropertyEditorManager.findEditor(Locale.class);
      if (pe == null) {
        ProgrammingErrorHelpers.deadBranch("no property editor found for "
                                           + Locale.class
                                           + "; please register a property editor for this type with "
                                           + "PropertyEditorManager.registerEditor(TARGET CLASS, EDITOR CLASS) "
                                           + "or via another mechanism");
      }
      pe.setValue(locale);
      return pe.getAsText();
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle "
                               + val
                               + " with "
                               + getClass().getName()
                               + ", but that can't handle that type");
    }
    return null; // keep compiler happy
  }

  @Override
  public Object toObjectValue(ValueMapping vm, Object val) {
    try {
      String stringVal = (String)val;
      if (stringVal == null) {
        return null;
      }
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
    catch (ClassCastException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected a String");
    }
    return null; // keep compiler happy
  }

}
