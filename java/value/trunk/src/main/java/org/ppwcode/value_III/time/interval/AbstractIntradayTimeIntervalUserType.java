/*<license>
Copyright 2004 - $Date: 2008-11-16 15:45:00 +0100 (Sun, 16 Nov 2008) $ by PeopleWare n.v..

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


import static java.sql.Types.DATE;
import static java.sql.Types.TIME;
import static java.sql.Types.VARCHAR;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.value_III.time.TimeHelpers.compose;
import static org.ppwcode.value_III.time.TimeHelpers.nullSafeSetDayDate;
import static org.ppwcode.value_III.time.TimeHelpers.nullSafeSetDayTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimeZone;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.TimeHelpers;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueUserType;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * A Hibernate 3 value handler for {@link IntradayTimeInterval} and {@link DeterminateIntradayTimeInterval}.
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
@Copyright("2008 - $Date: 2008-11-16 15:45:00 +0100 (Sun, 16 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3659 $",
         date     = "$Date: 2008-11-16 15:45:00 +0100 (Sun, 16 Nov 2008) $")
public abstract class AbstractIntradayTimeIntervalUserType extends AbstractImmutableValueUserType {

  /*<section name="meta">*/
  //------------------------------------------------------------------

  /**
   * Static definition of the 1 TIMEZONE and 2 TIMESTAMP columns we will write in.
   */
  @Invars(@Expression("SQL_TYPES == {DATE, TIME, TIME, VARCHAR}"))
  private static final int[] SQL_TYPES = {DATE, TIME, TIME, VARCHAR};

  @MethodContract(post = @Expression("SQL_TYPES"))
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /*</section>*/



  public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException,
                                                                        SQLException {
    if (value != null && !returnedClass().isInstance(value)) {
      throw new HibernateException("this user type can only handle values of type "
                                   + returnedClass().getCanonicalName()
                                   + "; "
                                   + value.getClass().getCanonicalName()
                                   + " is not supported");
    }
    else if (value == null) {
      st.setNull(index, DATE);
      st.setNull(index + 1, TIME);
      st.setNull(index + 2, TIME);
      st.setNull(index + 3, VARCHAR);
    }
    else {
      /*
       * BIG NOTE The day date effectively stored when we try to store d, is the date it is at the
       * default time zone at time d. Thus, if d is midnight of day x in time zone tz, and tz is
       * more to the east than the default time zone, the day date stored is x - 1!!. To fix this,
       * we use the SQL methods that take a timezone as extra parameters.
       *
       * BIG NOTE The day time
       * effectively stored when we try to store t, is the time of day it is at the default time
       * zone at time t UTC. To get the time in the database, we use the SQL methods that take a
       * time zone as extra parameter.
       */
      IntradayTimeInterval idti = (IntradayTimeInterval)value;
      TimeZone tz = idti.getTimeZone();
      nullSafeSetDayDate(st, idti.getDay(), tz, index);
      nullSafeSetDayTime(st, idti.getBeginTimeOfDay(), tz, index + 1);
      nullSafeSetDayTime(st, idti.getEndTimeOfDay(), tz, index + 2);
      Hibernate.TIMEZONE.nullSafeSet(st, tz, index + 3);
    }
  }

  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    try {
      /*
       * BIG NOTE
       * The day date effectively stored when we try to store d, is the date it is at
       * the default time zone at time d.
       * Thus, if d is midnight of day x in time zone tz, and tz is more to the east than the default time zone,
       * the day date stored is x - 1!!.
       * To fix this, we use the SQL methods that require an explicit time zone.
       *
       * BIG NOTE The day time
       * effectively stored when we try to store t, is the time of day it is at the default time
       * zone at time t UTC. To get the time in the database, we use the SQL methods that take a
       * time zone as extra parameter.
       */
      TimeZone tz = (TimeZone)Hibernate.TIMEZONE.nullSafeGet(rs, names[3]);
      Date day = TimeHelpers.nullSafeGetDayDate(rs, names[0], tz);
      Date beginTime = TimeHelpers.nullSafeGetDayTime(rs, names[1], tz);
      Date endTime = TimeHelpers.nullSafeGetDayTime(rs, names[2], tz);
      if (tz == null && day == null && beginTime == null && endTime == null) {
        return null;
      }
      Date intervalBeginTime = compose(day, beginTime, tz);
      Date intervalEndTime = compose(day, endTime, tz);
      return createFreshIntradayTimeInterval(intervalBeginTime, intervalEndTime, tz);
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (ClassCastException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (IllegalTimeIntervalException exc) {
      throw new HibernateException("data received from database did violate invariants for " + BeginEndTimeInterval.class, exc);
    }
  }

  protected abstract AbstractIntradayTimeInterval createFreshIntradayTimeInterval(Date intervalBeginTime, Date intervalEndTime, TimeZone tz)
      throws IllegalTimeIntervalException;

}
