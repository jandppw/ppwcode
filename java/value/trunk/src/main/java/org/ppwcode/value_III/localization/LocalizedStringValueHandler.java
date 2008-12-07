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

package org.ppwcode.value_III.localization;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;

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
import org.ppwcode.value_III.ext.java.util.LocaleValueHandler;
import org.ppwcode.vernacular.value_III.ValueException;


/**
 * A OpenJPA value handler for {@link LocalizedString}. The locale and string
 * are stored in 2 separate VARCHAR columns. {@code null} is stored as NULL in both
 * columns. It is not possible that only one column is {@code null}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class LocalizedStringValueHandler extends AbstractValueHandler implements ValueHandler {

  public static final String EMPTY = "";

  private final LocaleValueHandler $localeValueHandler = new LocaleValueHandler();

  public final Column[] map(ValueMapping vm, String propertyName, ColumnIO io, boolean adapt) {
    return new Column[] {localeColumn(vm, propertyName, io, adapt), stringColumn(propertyName)};
  }


  private Column localeColumn(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    return $localeValueHandler.map(vm, name + "_locale", io, adapt)[0];
  }

  private Column stringColumn(String name) {
    Column c = new Column();
    c.setName(name + "_string");
    c.setType(Types.LONGVARCHAR);
    c.setJavaType(JavaTypes.STRING);
    return c;
  }

  @Override
  public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      LocalizedString ls = (LocalizedString)val;
      if (val == null) {
        return new Object[] {null, null};
      }
      Locale l = ls.getLocale();
      assert l != null;
      return new Object[] {$localeValueHandler.toDataStoreValue(vm, l, store), ls.getText()};
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " +
                          LocalizedStringValueHandler.class.getName() + ", but that can't handle that type");
    }
    return null; // make compiler happy
  }

  @Override
  public Object toObjectValue(ValueMapping vm, Object fromDb) {
    try {
      Object[] data = (Object[])fromDb;
      String lString = (String)data[0];
      Locale locale = (Locale)$localeValueHandler.toObjectValue(vm, lString);
      String string = (String)data[1];
      if (locale == null && string == null) {
        return null;
      }
      else {
        return new LocalizedString(locale, string);
      }
    }
    catch (NullPointerException exc) {
      unexpectedException(exc, "data received from database is not as expected: we can't deal with null");
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected array of 2 values");
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected an array of 3 dates");
    }
    catch (ValueException exc) {
      unexpectedException(exc, "data received from database did violate invariants for " + LocalizedString.class);
    }
    return null; // make compiler happy
  }

}
