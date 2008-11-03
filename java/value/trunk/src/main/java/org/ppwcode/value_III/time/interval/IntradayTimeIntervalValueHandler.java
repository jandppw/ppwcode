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
import static org.ppwcode.value_III.time.DateHelpers.compose;
import static org.ppwcode.value_III.time.DateHelpers.isDayDate;
import static org.ppwcode.value_III.time.DateHelpers.sqlDayDate;
import static org.ppwcode.value_III.time.DateHelpers.sqlTimeOfDay;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.deadBranch;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.sql.Time;
import java.sql.Types;
import java.util.Date;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A OpenJPA value handler for {@link DayDateTimeInterval}. Begin and end are stored in 3
 * columns in the database, 1 for the day date as {@code DATE}, 2 for the time of day as
 * {@code TIME}. {@code null} is represented by {@code null} in all 3columns. This is not
 * a problem because both the begin and end being {@code null} is forbidden for
 * {@link IntradayTimeInterval IntradayTimeIntervals}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @mudo generalize with DeterminateIntradayTimeIntervalValueHandler
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class IntradayTimeIntervalValueHandler extends AbstractValueHandler {

  public final Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    return new Column[] {dayColumn(name), timeColumn(name + "_begin"), timeColumn(name + "_end")};
  }

  private Column dayColumn(String name) {
    Column c = new Column();
    c.setName(name + "_day");
    c.setType(Types.DATE);
    c.setJavaType(JavaSQLTypes.SQL_DATE);
    return c;
  }

  private Column timeColumn(String name) {
    Column c = new Column();
    c.setName(name);
    c.setType(Types.TIME);
    c.setJavaType(JavaSQLTypes.TIME);
    return c;
  }

  @Override
  public Object[] toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      IntradayTimeInterval beTi = (IntradayTimeInterval)val;
      if (beTi == null) {
        return new Object[] {null, null, null};
      }
      java.sql.Date day = sqlDayDate(beTi.getDay());
      Time beginTime = sqlTimeOfDay(beTi.getBegin());
      Time endTime = sqlTimeOfDay(beTi.getEnd());
      return new Object[] {day, beginTime, endTime};
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " +
                          IntradayTimeIntervalValueHandler.class.getName() + ", but that can't handle that type");
    }
    return null; // make compiler happy
  }

  @Override
  public Object toObjectValue(ValueMapping vm, Object fromDb) {
    try {
      Object[] dates = (Object[])fromDb;
      java.sql.Date day = (java.sql.Date)dates[0];
      assert day == null || isDayDate(day);
      Time beginTime = (Time)dates[1];
      Time endTime = (Time)dates[2];
      if (day == null) {
        if (beginTime != null || endTime != null) {
          deadBranch("data received from database is not as expected: if the day is null, the times need to be null too");
        }
        return null;
      }
      Date intervalBeginTime = compose(day, beginTime);
      Date intervalEndTime = compose(day, endTime);
      return new IntradayTimeInterval(intervalBeginTime, intervalEndTime);
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected array of 3 values");
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected an array of 3 dates");
    }
    catch (IllegalIntervalException exc) {
      unexpectedException(exc, "data received from database did violate invariants for " + IntradayTimeInterval.class);
    }
    return null; // make compiler happy
  }

}
