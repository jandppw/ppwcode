/*<license>
Copyright 2004 - $Date: 2008-10-27 20:52:26 +0100 (Mon, 27 Oct 2008) $ by PeopleWare n.v..

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

package org.ppwcode.value_III.time.interval.jpa.openjpa;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.value_III.time.DateHelpers.isDayDate;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.JavaTypes;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.interval.BeginEndTimeInterval;
import org.ppwcode.value_III.time.interval.DayDateBeginEndTimeInterval;
import org.ppwcode.value_III.time.interval.IllegalIntervalException;


/**
 * A OpenJPA value handler for {@link DayDateBeginEndTimeInterval}. Begin and end are stored in 2
 * columns in the database as {@code DATE}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date: 2008-10-27 20:52:26 +0100 (Mon, 27 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3326 $",
         date     = "$Date: 2008-10-27 20:52:26 +0100 (Mon, 27 Oct 2008) $")
public final class IntradayBeginEndTimeIntervalValueHandler implements ValueHandler {

  public final Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    return new Column[] {dayColumn(name), timeColumn(name + "_begin"), timeColumn(name + "_end")};
  }

  private Column dayColumn(String name) {
    Column c = new Column();
    c.setName(name + "_day");
    c.setType(Types.DATE);
    c.setJavaType(JavaTypes.DATE);
    return c;
  }

  private Column timeColumn(String name) {
    Column c = new Column();
    c.setName(name);
    c.setType(Types.TIME);
    c.setJavaType(JavaTypes.DATE);
    return c;
  }

  public final boolean isVersionable(ValueMapping vm) {
    return false;
  }

  public final boolean objectValueRequiresLoad(ValueMapping vm) {
    return false;
  }

  public Object getResultArgument(ValueMapping vm) {
    return null;
  }


  public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      IntradayBeginEndTimeInterval beTi = (IntradayBeginEndTimeInterval)val;
      Date day = beTi.getDay();
      Date beginTime = beTi.getBegin();
      Date endTime = beTi.getEnd();
      return new Date[] {day, beginTime, endTime};
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " +
                          IntradayBeginEndTimeIntervalValueHandler.class.getName() + ", but that can't handle that type");
    }
    return null; // make compiler happy
  }

  public Object toObjectValue(ValueMapping vm, Object fromDb) {
    try {
      Object[] dates = (Date[])fromDb;
      Date day = (Date)dates[0];
      assert isDayDate(day);
      Date beginTime = (Date)dates[1];
      beginTime = addTime(day, beginTime);
      Date endTime = (Date)dates[2];
      endTime = addTime(day, endTime);
      return new IntradayBeginEndTimeInterval(beginTime, endTime);
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
    catch (IllegalIntervalException exc) {
      unexpectedException(exc, "data received from database did violate invariants for " + IntradayBeginEndTimeInterval.class);
    }
    return null; // make compiler happy
  }

  private Date addTime(Date day, Date beginTime) {
    long millies = day.getTime();
    millies += beginTime.getTime();
    Date result = new Date(millies);
    return result;
  }

  /**
   * Not used, since {@link #objectValueRequiresLoad(ValueMapping)} returns false.
   */
  public Object toObjectValue(ValueMapping arg0,
                              Object arg1,
                              OpenJPAStateManager arg2,
                              JDBCStore arg3,
                              JDBCFetchConfiguration arg4) throws SQLException {
    return null;
  }

}
