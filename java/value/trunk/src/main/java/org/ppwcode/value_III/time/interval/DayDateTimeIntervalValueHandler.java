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
import static org.ppwcode.value_III.time.TimeHelpers.isDayDate;
import static org.ppwcode.value_III.time.TimeHelpers.move;
import static org.ppwcode.value_III.time.TimeHelpers.sqlDayDate;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.sql.Types;
import java.util.Date;
import java.util.TimeZone;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.ext.java.util.TimeZoneValueHandler;


/**
 * A OpenJPA value handler for {@link DayDateTimeInterval}. Begin and end are stored in 2
 * columns in the database as {@code DATE}, and the time zone is stored using the
 * {@link TimeZoneValueHandler}. {@code null} is represented by {@code null}
 * in all 3 columns. This is not a problem because both the begin and end being {@code null}
 * is forbidden for {@link DayDateTimeInterval DayDateTimeIntervals}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class DayDateTimeIntervalValueHandler extends AbstractValueHandler {

  private final TimeZoneValueHandler $timeZoneValueHandler = new TimeZoneValueHandler();

  public final Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    return new Column[] {dateColumn(name + "_begin"), dateColumn(name + "_end"),
                         $timeZoneValueHandler.map(vm, name + "_timezone", io, false)[0]};
  }

  private Column dateColumn(String name) {
    Column c = new Column();
    c.setName(name);
    c.setType(Types.DATE);
    c.setJavaType(JavaSQLTypes.SQL_DATE);
    return c;
  }

  @Override
  public Object[] toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      DayDateTimeInterval beTi = (DayDateTimeInterval)val;
      if (beTi == null) {
        return new Object[] {null, null, null};
      }
      /*
       * BIG NOTE
       * The day date effectively stored when we try to store d, is the date it is at
       * the default time zone at time d.
       * Thus, if d is midnight of day x in time zone tz, and tz is more to the east than the default time zone,
       * the day date stored is x - 1!!.
       * To fix this, we will correct what we pass in to be midnight of the date we want in the _default time zone_,
       * although that point in time does not correspond most of the time with the point in time we actually want.
       */
      return new Object[] {midnightDefaultTZ(beTi.getBegin(), beTi.getTimeZone()),
                           midnightDefaultTZ(beTi.getEnd(), beTi.getTimeZone()),
                           $timeZoneValueHandler.toDataStoreValue(vm, beTi.getTimeZone(), store)};
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " +
                          DayDateTimeIntervalValueHandler.class.getName() + ", but that can't handle that type");
    }
    return null; // make compiler happy
  }

  private static java.sql.Date midnightDefaultTZ(Date d, TimeZone tz) {
    assert isDayDate(d, tz);
    TimeZone defaultTz = TimeZone.getDefault();
    Date moved = move(d, tz, defaultTz);
    assert isDayDate(moved, defaultTz);
    return sqlDayDate(moved, defaultTz);
  }

  @Override
  public Object toObjectValue(ValueMapping vm, Object fromDb) {
    try {
      Object[] data = (Object[])fromDb;
      Date begin = (Date)data[0];
      Date end = (Date)data[1];
      TimeZone tz = (TimeZone)$timeZoneValueHandler.toObjectValue(vm, data[2]);
      if (begin == null && end == null) {
        return null;
      }
      /*
       * BIG NOTE
       * What we get here, via OpenJPA, from the database, is a java.sql.Date that represents
       * midnight of the effective day date in the database (DATE), expressed in the DEFAULT TIMEZONE,
       * although we put it in in the timezone tz!
       * So isDayDate(begin, tz) and isDayDate(end, tz) are probably NOT true.
       */
      return new DayDateTimeInterval(midnightTZ(begin, tz), midnightTZ(end, tz), tz);
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

  private Date midnightTZ(Date d, TimeZone tz) {
    TimeZone defaultTz = TimeZone.getDefault();
    assert isDayDate(d, defaultTz);
    Date moved = move(d, defaultTz, tz);
    assert isDayDate(moved, tz);
    return moved;
  }

}
