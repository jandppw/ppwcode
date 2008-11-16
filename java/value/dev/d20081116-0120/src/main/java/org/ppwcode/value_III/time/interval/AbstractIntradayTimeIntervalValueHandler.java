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
import static org.ppwcode.value_III.time.TimeHelpers.UTC;
import static org.ppwcode.value_III.time.TimeHelpers.compose;
import static org.ppwcode.value_III.time.TimeHelpers.isDayDate;
import static org.ppwcode.value_III.time.TimeHelpers.move;
import static org.ppwcode.value_III.time.TimeHelpers.sqlDayDate;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.deadBranch;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.unexpectedException;

import java.sql.Time;
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
 * A OpenJPA value handler for {@link IntradayTimeInterval} and {@link DeterminateIntradayTimeInterval}.
 * Date is stored in 4 columns in the database, 1 for the day date as {@code DATE}, 2 for the time of day as
 * {@code TIME}, and one for the time zone, as {@code VARCHAR}. {@code null} is represented by {@code null}
 * in all 4 columns. This is not a problem because both the begin and end being {@code null} is forbidden for
 * {@link IntradayTimeInterval IntradayTimeIntervals}.
 *
 * Note that the columns cannot be made non-nullable in general here, even for the {@link DeterminateIntradayTimeInterval},
 * since, although the begin and end (and thus date) and the time zone cannot be null, the property of this type
 * as a whole, in general, can still be {@code null}. This value handler than sets all columns to {@code NUL}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractIntradayTimeIntervalValueHandler extends AbstractValueHandler {

  private final TimeZoneValueHandler $timeZoneValueHandler = new TimeZoneValueHandler();

  public final Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    return new Column[] {dayColumn(name), timeColumn(name + "_begin"), timeColumn(name + "_end"),
                         timeZoneColumn(vm, name, io, adapt)};
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

  private Column timeZoneColumn(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    Column c = $timeZoneValueHandler.map(vm, name + "_timezone", io, adapt)[0];
    c.setNotNull(true);
    return c;
  }

  @Override
  public Object[] toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      AbstractIntradayTimeInterval beTi = (AbstractIntradayTimeInterval)val;
      if (beTi == null) {
        return new Object[] {null, null, null, null};
      }
      /*
       * BIG NOTE
       * The day date effectively stored when we try to store d, is the date it is at
       * the default time zone at time d.
       * Thus, if d is midnight of day x in time zone tz, and tz is more to the east than the default time zone,
       * the day date stored is x - 1!!.
       * To fix this, we will correct what we pass in to be midnight of the date we want in the _default time zone_,
       * although that point in time does not correspond most of the time with the point in time we actually want.
       *
       * BIG NOTE
       * The day time effectively stored when we try to store t, is the time of day it is
       * at the default time zone at time t UTC. To get the time in the database it is at time zone
       * beTi.getTimeZone(), we thus need to store a java.sql.Time that expresses that same day time
       * in the default time zone on the day date of EPOCH. If the default time zone is thus west of Greenwich,
       * this is negative milliseconds.
       */
      TimeZone defaultTZ = TimeZone.getDefault();
      // day time, in the default time zone, on 1/1/1970
      Date beginDefaultTZ = move(beTi.getBeginTimeOfDay(), beTi.getTimeZone(), defaultTZ);
      Date endDefaultTZ = move(beTi.getEndTimeOfDay(), beTi.getTimeZone(), defaultTZ);
      /* the same day time, in the default time zone, on 1/1/1970, as a sql time;
       * we copy the milliseconds since epoch UTC into the constructor
       */
      Time beginTime = new Time(beginDefaultTZ.getTime());
      Time endTime = new Time(endDefaultTZ.getTime());
      return new Object[] {midnightDefaultTZ(beTi.getDay(), beTi.getTimeZone()), beginTime, endTime,
                           $timeZoneValueHandler.toDataStoreValue(vm, beTi.getTimeZone(), store)};
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " +
                          AbstractIntradayTimeIntervalValueHandler.class.getName() + ", but that can't handle that type");
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
      java.sql.Date day = (java.sql.Date)data[0];
      Time beginTime = (Time)data[1];
      Time endTime = (Time)data[2];
      TimeZone tz = (TimeZone)$timeZoneValueHandler.toObjectValue(vm, data[3]);
      if (day == null) {
        if (beginTime != null || endTime != null || tz != null) {
          deadBranch("data received from database is not as expected: if the day is null, the times and timezone need to be null too");
        }
        return null;
      }
      /*
       * BIG NOTE
       * What we get here, via OpenJPA, from the database, is a java.sql.Date that represents
       * midnight of the effective day date in the database (DATE), expressed in the DEFAULT TIMEZONE,
       * although we put it in in the timezone tz!
       * So isDayDate(day, tz) are probably NOT true.
       */
      Date correctedDay = midnightTZ(day, tz);
      /*
       * BIG NOTE
       * What we get here, via OpenJPA, from the database, is a java.sql.Time that represents
       * the time of day in the database (TIME), on 1/1/1970, expressed in the DEFAULT TIMEZONE.
       * We will move it to the time zone UTC before composing, while keeping the time of day.
       */
      TimeZone defaultTz = TimeZone.getDefault();
      Date correctedBeginTime = move(beginTime, defaultTz, UTC);
      Date correctedEndTime = move(endTime, defaultTz, UTC);
      Date intervalBeginTime = compose(correctedDay, correctedBeginTime, tz);
      Date intervalEndTime = compose(correctedDay, correctedEndTime, tz);
      return createFreshIntradayTimeInterval(intervalBeginTime, intervalEndTime, tz);
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected array of 3 values");
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected an array of 3 dates");
    }
    catch (IllegalTimeIntervalException exc) {
      unexpectedException(exc, "data received from database did violate invariants for " + IntradayTimeInterval.class);
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

  protected abstract AbstractIntradayTimeInterval createFreshIntradayTimeInterval(Date intervalBeginTime, Date intervalEndTime, TimeZone tz)
      throws IllegalTimeIntervalException;

}
