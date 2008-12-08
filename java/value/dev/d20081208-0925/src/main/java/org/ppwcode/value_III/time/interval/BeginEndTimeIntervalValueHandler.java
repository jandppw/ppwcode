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

package org.ppwcode.value_III.time.interval;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;

import java.sql.Types;
import java.util.Date;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.meta.JavaTypes;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A OpenJPA value handler for {@link BeginEndTimeInterval}. Begin and end are stored in 2
 * columns in the database as {@code TIMESTAMP}. {@code null} is represented by {@code null}
 * in both columns. This is not a problem because both the begin and end being {@code null}
 * is forbidden for {@link BeginEndTimeInterval BeginEndTimeIntervals}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class BeginEndTimeIntervalValueHandler extends AbstractValueHandler {

  public final Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    return new Column[] {timestampColumn(name + "_begin"), timestampColumn(name + "_end")};
  }

  private Column timestampColumn(String name) {
    Column c = new Column();
    c.setName(name);
    c.setType(Types.TIMESTAMP);
    c.setJavaType(JavaTypes.DATE);
    return c;
  }

  @Override
  public Object[] toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      BeginEndTimeInterval beTi = (BeginEndTimeInterval)val;
      if (beTi == null) {
        return new Object[] {null, null};
      }
      return new Object[] {beTi.getBegin(), beTi.getEnd()};
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " +
                          BeginEndTimeIntervalValueHandler.class.getName() + ", but that can't handle that type");
    }
    return null; // make compiler happy
  }

  @Override
  public Object toObjectValue(ValueMapping vm, Object fromDb) {
    try {
      Object[] dates = (Object[])fromDb;
      Date begin = (Date)dates[0];
      Date end = (Date)dates[1];
      if (begin == null && end == null) {
        return null;
      }
      return new BeginEndTimeInterval(begin, end);
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected array of 2 values");
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected an array of 2 dates");
    }
    catch (IllegalTimeIntervalException exc) {
      unexpectedException(exc, "data received from database did violate invariants for " + BeginEndTimeInterval.class);
    }
    return null; // make compiler happy
  }

}
