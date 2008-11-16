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
import static java.sql.Types.VARCHAR;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.value_III.time.TimeHelpers.isDayDate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueUserType;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * A Hibernate 3 value handler for {@link DayDateTimeInterval}. Begin and end are stored in 2
 * columns in the database as {@code DATE}, and the time zone is stored using Hibernate support.
 * {@code null} is represented by {@code null} in all 3 columns. This is not a problem because both
 * the begin and end being {@code null} is forbidden for {@link DayDateTimeInterval DayDateTimeIntervals}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date: 2008-11-16 15:45:00 +0100 (Sun, 16 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3659 $",
         date     = "$Date: 2008-11-16 15:45:00 +0100 (Sun, 16 Nov 2008) $")
public final class DayDateTimeIntervalUserType extends AbstractImmutableValueUserType {

  /*<section name="meta">*/
  //------------------------------------------------------------------

  @Override
  public Class<? extends ImmutableValue> returnedClass() {
    return DayDateTimeInterval.class;
  }

  /**
   * Static definition of the 1 TIMEZONE and 2 TIMESTAMP columns we will write in.
   */
  @Invars(@Expression("SQL_TYPES == {VARCHAR, DATE, DATE}"))
  private static final int[] SQL_TYPES = {VARCHAR, DATE, DATE};

  @MethodContract(post = @Expression("SQL_TYPES"))
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /*</section>*/



  public void nullSafeSet(PreparedStatement st, Object value, int index)
      throws HibernateException, SQLException {
    if (value != null && !returnedClass().isInstance(value)) {
      throw new HibernateException("this user type can only handle values of type "
                                   + returnedClass().getCanonicalName()
                                   + "; "
                                   + value.getClass().getCanonicalName()
                                   + " is not supported");
    }
    else if (value == null) {
      st.setNull(index, VARCHAR);
      st.setNull(index + 1, DATE);
      st.setNull(index + 2, DATE);
    }
    else {
      /*
       * BIG NOTE
       * The day date effectively stored when we try to store d, is the date it is at
       * the default time zone at time d.
       * Thus, if d is midnight of day x in time zone tz, and tz is more to the east than the default time zone,
       * the day date stored is x - 1!!.
       * To fix this, we use the SQL methods that take a timezone as extra parameters.
       */
      DayDateTimeInterval dtti = (DayDateTimeInterval)value;
      TimeZone tz = dtti.getTimeZone();
      nullSafeSet(st, dtti.getBegin(), tz, index);
      nullSafeSet(st, dtti.getEnd(), tz, index + 1);
      Hibernate.TIMEZONE.nullSafeSet(st, dtti.getTimeZone(), index + 2);
    }
  }

  private static void nullSafeSet(PreparedStatement st, Date d, TimeZone tz, int index) throws SQLException {
    if (d == null) {
      st.setNull(index, DATE);
    }
    else {
      assert isDayDate(d, tz);
      java.sql.Date dd = new java.sql.Date(d.getTime());
      GregorianCalendar cal = new GregorianCalendar(tz);
      st.setDate(index, dd, cal);
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
       */
      TimeZone tz = (TimeZone)Hibernate.TIMEZONE.nullSafeGet(rs, names[2]);
      Date begin = nullSafeGet(rs, names[0], tz);
      Date end = nullSafeGet(rs, names[1], tz);
      if (tz == null && begin == null && end == null) {
        return null;
      }
      return new DayDateTimeInterval(begin, end, tz);
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

  private Date nullSafeGet(ResultSet rs, String columnName, TimeZone tz) throws SQLException {
    java.sql.Date fromDb = null;
    if (tz != null) {
      GregorianCalendar cal = new GregorianCalendar(tz);
      fromDb = rs.getDate(columnName, cal);
    }
    else {
      fromDb = rs.getDate(columnName);
    }
    if (fromDb == null || rs.wasNull()) {
      return null;
    }
    else {
      return fromDb;
    }
  }

}
