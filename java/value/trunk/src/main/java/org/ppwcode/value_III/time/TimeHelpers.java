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

package org.ppwcode.value_III.time;


import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.pre;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * Static methods for working with dates
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TimeHelpers {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Don't instantiate this class.
   */
  private TimeHelpers() {
    // NOP
  }

  /*</construction>*/



  /**
   * The UTC / UT / GMT time zone.
   */
  @Invars({
    @Expression("UTC.rawOffset == 0"),
    @Expression("UTC.dSTSaving == 0"),
    @Expression("UTC.iD == 'UTC'")
  })
  public final static TimeZone UTC = TimeZone.getTimeZone("UTC");

  /**
   * The epoch, i.e., 1970-1-1, midnight, UTC.
   */
  @Invars({
    @Expression("gregorianCalendar(EPOCH, UTC).get(YEAR) == 1970"),
    @Expression("gregorianCalendar(EPOCH, UTC).get(MONTH) == JAUARY"),
    @Expression("gregorianCalendar(EPOCH, UTC).get(DAY_OF_MONTH) == 1"),
    @Expression("gregorianCalendar(EPOCH, UTC).get(HOUR) == 0"),
    @Expression("gregorianCalendar(EPOCH, UTC).get(MINUTE) == 0"),
    @Expression("gregorianCalendar(EPOCH, UTC).get(SECOND) == 0"),
    @Expression("gregorianCalendar(EPOCH, UTC).get(MILLISECOND) == 0")
  })
  public final static Date EPOCH;
  static {
    GregorianCalendar gc = new GregorianCalendar(1970, 0, 1);
    gc.setTimeZone(UTC);
    EPOCH = gc.getTime();
  }

  /**
   * A Gregorian calendar for a date in a functional, non-imperative way, which is easier to use
   * in contracts.
   */
  @MethodContract(
    pre  = {
      @Expression("_tz != null")
    },
    post = {
      @Expression("_d == null ? result == null"),
      @Expression("_d != null ? result.timeZone == _tz"),
      @Expression("_d != null ? result.time == _d")
    }
  )
  public static GregorianCalendar gregorianCalendar(Date d, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if (d == null) {
      return null;
    }
    GregorianCalendar result = new GregorianCalendar(tz);
    result.setTime(d);
    return result;
  }


  /**
   * {@code d1} is <em>before or equal</em> to {@code d2}.
   * This method also deals with {@code null} dates.
   * {@code null} dates are always earlier.
   */
  @MethodContract(post = @Expression("_d1 == null || (_d2 != null && _d1.before(_d2)) || _d1.equals(_d2)"))
  public static boolean le(Date d1, Date d2) {
    return d1 == null || (d2 != null && d1.before(d2)) || d1.equals(d2);
  }

  /**
   * <p>Whether or not 2 dates fall in the same day, depends on the time zone in which you evaluate the
   *   dates: 0:30 and 1:30 Belgian time on any given date fall in the same day in Pellenberg, but not
   *   in Burwell Village. Whether they fall in the same day in GMT even depends on the fact whether
   *   we are using summer time or winter time in Belgium on the given date.</p>
   * <p>In other Java APIs, a {@code null} time zone is interpreted as the system default time zone,
   *   which is supposed to be the time zone in which the hardware is located. In our experience, this
   *   &quot;default behavior&quot; more often than not leads to extreme misunderstandings and problems.
   *   Furthermore, we believe that UTC as default would be far better, but introducing that here would
   *   be even more confusing.
   *   Therefor we will not accept a {@code null} time zone. Getting the time zone you want and explicitly
   *   stating it makes the developer aware of the issue, and is easy. See
   *   {@link TimeZone#getTimeZone(String)}.</p>
   */
  @MethodContract(
    pre  = @Expression("_tz != null"),
    post = @Expression("(_date1 == null) ? (_date2 == null) : dayDate(_date1, _tz) == dayDate(_date2, _tz)")
  )
  public static boolean sameDay(Date date1, Date date2, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if ((date1 != null) && (date2 != null)) {
      GregorianCalendar cal1 = new GregorianCalendar(tz);
      cal1.setTime(date1);
      GregorianCalendar cal2 = new GregorianCalendar(tz);
      cal2.setTime(date2);
      return fieldEqual(cal1, cal2, Calendar.YEAR) &&
             fieldEqual(cal1, cal2, Calendar.MONTH) &&
             fieldEqual(cal1, cal2, Calendar.DAY_OF_MONTH);
    }
    else {
      return date1 == date2; // they both have to be null
    }
  }

  /**
   * <p>Change <code>date</code> so that it has no time information more accurate then the day level. More
   *   accurate information is set to <code>0</code>. How to do this, depends on the time zone in which you
   *   evaluate the date: 0:30 Belgian time on any given date returns a different result when evaluated in
   *   Pellenberg and Burwell Village. The result in GMT even depends on the fact whether we are using summer
   *   time or winter time in Belgium on the given date.</p>
   * <p>In other Java APIs, a {@code null} time zone is interpreted as the system default time zone,
   *   which is supposed to be the time zone in which the hardware is located. In our experience, this
   *   &quot;default behavior&quot; more often than not leads to extreme misunderstandings and problems.
   *   Furthermore, we believe that UTC as default would be far better, but introducing that here would
   *   be even more confusing.
   *   Therefor we will not accept a {@code null} time zone. Getting the time zone you want and explicitly
   *   stating it makes the developer aware of the issue, and is easy. See
   *   {@link TimeZone#getTimeZone(String)}.</p>
   *
   * @note This was difficult to get right. Don't change without tests!
   */
  @MethodContract(
    pre  = @Expression("_tz != null"),
    post = {
      @Expression("gregorianCalendar(result, _tz).get(YEAR) == gregorianCalendar(date, _tz).get(YEAR)"),
      @Expression("gregorianCalendar(result, _tz).get(MONTH) == gregorianCalendar(date, _tz).get(MONTH)"),
      @Expression("gregorianCalendar(result, _tz).get(DAY_OF_MONTH) == gregorianCalendar(date, _tz).get(DAY_OF_MONTH)"),
      @Expression("gregorianCalendar(result, _tz).get(HOUR_OF_DAY) == 0"),
      @Expression("gregorianCalendar(result, _tz).get(MINUTE) == 0"),
      @Expression("gregorianCalendar(result, _tz).get(SECOND) == 0"),
      @Expression("gregorianCalendar(result, _tz).get(MILLISECOND) == 0")
    }
  )
  public static Date dayDate(Date date, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if (date == null) {
      return null;
    }
    GregorianCalendar cal = new GregorianCalendar(tz);
    cal.clear();
    cal.setTime(date);
    cal.clear(Calendar.HOUR);
    cal.clear(Calendar.HOUR_OF_DAY);
    cal.clear(Calendar.AM_PM);
    cal.clear(Calendar.MINUTE);
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);
    Date result = cal.getTime();
    assert result != date;
    return result;
  }

  @MethodContract(
    pre  = @Expression("_tz != null"),
    post = {
      @Expression("gregorianCalendar(result, _tz).get(YEAR) == 0"),
      @Expression("gregorianCalendar(result, _tz).get(MONTH) == 0"),
      @Expression("gregorianCalendar(result, _tz).get(DAY_OF_MONTH) == 0"),
      @Expression("gregorianCalendar(result, _tz).get(HOUR_OF_DAY) == gregorianCalendar(date, _tz).get(HOUR_OF_DAY)"),
      @Expression("gregorianCalendar(result, _tz).get(MINUTE) == gregorianCalendar(date, _tz).get(MINUTE)"),
      @Expression("gregorianCalendar(result, _tz).get(SECOND) == gregorianCalendar(date, _tz).get(SECOND)"),
      @Expression("gregorianCalendar(result, _tz).get(MILLISECOND) == gregorianCalendar(date, _tz).get(MILLISECOND)")
    }
  )
  public static Date dayTime(Date date, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if (date == null) {
      return null;
    }
    GregorianCalendar cal = new GregorianCalendar(tz);
    cal.clear();
    cal.setTime(date);
    cal.clear(Calendar.ERA);
    cal.clear(Calendar.YEAR);
    cal.clear(Calendar.MONTH);
    cal.clear(Calendar.WEEK_OF_MONTH);
    cal.clear(Calendar.WEEK_OF_YEAR);
    cal.clear(Calendar.DAY_OF_YEAR);
    cal.clear(Calendar.DAY_OF_MONTH);
    cal.clear(Calendar.DAY_OF_WEEK_IN_MONTH);
    cal.clear(Calendar.DAY_OF_WEEK);
    Date result = cal.getTime();
    assert result != date;
    return result;
  }

  @MethodContract(
    pre  = @Expression("_tz != null"),
    post = {@Expression("(_date != null) && (_date == dayDate(_date, _tz))")})
  public static boolean isDayDate(Date date, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if (date == null) {
      return false;
    }
    else {
      GregorianCalendar cal = new GregorianCalendar(tz);
      cal.setTime(date);
      return notSetOr0(cal, Calendar.HOUR) &&
             notSetOr0(cal, Calendar.HOUR_OF_DAY) &&
             notSetOr0(cal, Calendar.MINUTE) &&
             notSetOr0(cal, Calendar.SECOND) &&
             notSetOr0(cal, Calendar.MILLISECOND);
    }
  }

  /**
   * <p>The day date of a point in time in a particular time zone.</p>
   * <p>The Javadoc of {@link java.sql.Date} says:</p>
   * <blockquote>To conform with the definition of SQL <code>DATE</code>, the  millisecond values wrapped
   *   by a <code>java.sql.Date</code> instance must be 'normalized' by setting the  hours, minutes,
   *   seconds, and milliseconds to zero in the particular time zone with which the instance is
   *   associated.</blockquote>
   * <p>This conversion is thus impossible without a time zone.</p>
   * <p>Please take not of the discussion of the sense or nonsense of using {@link java.sql.Date} in the
   *   <a href="package.html#java.sql.Date">package documentation.</a></p>
   */
  @MethodContract(
    pre  = @Expression("_tz != null"),
    post = {
      @Expression("result == dayDate(d, tz)")
    }
  )
  public static java.sql.Date sqlDayDate(Date d, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if (d == null) {
      return null;
    }
    long lResult = dayDate(d, tz).getTime();
    java.sql.Date result = new java.sql.Date(lResult);
    return result;
  }

  /**
   * For a {@link Time}, {@link Time#getTime()} is at the milliseconds after midnight (of any given
   * day). This is independent of a time zone.
   * Normally, this cannot easily be converted into a {@link Date}, or used in a {@link Calendar}.
   */
  @MethodContract(
    pre  = @Expression("_tz != null"),
    post = {
      @Expression("d == null ? result == null"),
      @Expression("d != null ? gregorianCalendar(result, UTC).get(YEAR) == gregorianCalendar(EPOCH, UTC).get(YEAR)"),
      @Expression("d != null ? gregorianCalendar(result, UTC).get(MONTH) == gregorianCalendar(EPOCH, UTC).get(MONTH)"),
      @Expression("d != null ? gregorianCalendar(result, UTC).get(DAY_OF_MONTH) == gregorianCalendar(EPOCH, UTC).get(DAY_OF_MONTH)"),
      @Expression("d != null ? gregorianCalendar(result, UTC).get(HOUR) == gregorianCalendar(d, tz).get(HOUR)"),
      @Expression("d != null ? gregorianCalendar(result, UTC).get(MINUTE) == gregorianCalendar(d, tz).get(MINUTE)"),
      @Expression("d != null ? gregorianCalendar(result, UTC).get(SECOND) == gregorianCalendar(d, tz).get(SECOND)"),
      @Expression("d != null ? gregorianCalendar(result, UTC).get(MILLISECOND) == gregorianCalendar(d, tz).get(MILLISECOND)")
    }
  )
  public static Time sqlTimeOfDay(Date d, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if (d == null) {
      return null;
    }
    GregorianCalendar gc = new GregorianCalendar(tz);
    gc.setTime(d);
    long h = gc.get(Calendar.HOUR_OF_DAY);
    long m = gc.get(Calendar.MINUTE);
    long s = gc.get(Calendar.SECOND);
    long ms = gc.get(Calendar.MILLISECOND);
    long lResult = ms;
    lResult += s * Duration.Unit.SECOND.asMilliseconds();
    lResult += m * Duration.Unit.MINUTE.asMilliseconds();
    lResult += h * Duration.Unit.HOUR.asMilliseconds();
    Time result = new Time(lResult);
    return result;
  }

  /**
   * Return a {@link Date}, i.e., a point in time, that expresses the {@code day}
   * and {@code time} in time zone {@code tz}. The day, which is a {@link java.sql.Date},
   * is not necessarily a day date. That depends on the time zone (see he discussion of
   * the sense or nonsense of using {@link java.sql.Date} in the
   * <a href="package.html#java.sql.Date">package documentation.</a>). The given {@code day}
   * should represent midnight of the intended day in the given time zone {@code tz}.
   * {@code time} must be represented in UTC. It uses the milliseconds since EPOCH
   * as time of day.
   */
  @MethodContract(
    pre  = {
      @Expression("tz != null"),
      @Expression("day != null ? isDayDate(day, tz)"),
      @Expression("time != null ? dayDate(day, UTC) == EPOCH")
    },
    post = {
      @Expression("date == null || time == null ? result == null"),
      @Expression("date != null && time != null ? gregorianCalendar(result, tz).get(YEAR) == gregorianCalendar(day, tz).get(YEAR)"),
      @Expression("date != null && time != null ? gregorianCalendar(result, tz).get(MONTH) == gregorianCalendar(day, tz).get(MONTH)"),
      @Expression("date != null && time != null ? gregorianCalendar(result, tz).get(DAY_OF_MONTH) == gregorianCalendar(day, tz).get(DAY_OF_MONTH)"),
      @Expression("date != null && time != null ? gregorianCalendar(result, tz).get(HOUR) == gregorianCalendar(time, UTC).get(HOUR)"),
      @Expression("date != null && time != null ? gregorianCalendar(result, tz).get(MINUTE) == gregorianCalendar(time, UTC).get(MINUTE)"),
      @Expression("date != null && time != null ? gregorianCalendar(result, tz).get(SECOND) == gregorianCalendar(time, UTC).get(SECOND)"),
      @Expression("date != null && time != null ? gregorianCalendar(result, tz).get(MILLISECOND) == gregorianCalendar(time, UTC).get(MILLISECOND)")
    }
  )
  public static Date compose(Date day, Date time, TimeZone tz) {
    assert preArgumentNotNull(tz, "tz");
    if (day == null || time == null) {
      return null;
    }
    assert pre(isDayDate(day, tz), "isDayDate(day, tz)");
    assert pre(EPOCH.equals(dayDate(time, UTC)), "EPOCH.equals(dayDate(time, UTC)");
    long millies = day.getTime();
    millies += time.getTime();
    Date result = new Date(millies);
    return result;
  }

  private static boolean notSetOr0(Calendar cal, int field) {
    assert preArgumentNotNull(cal, "cal");
    return (! cal.isSet(field)) || (cal.get(field) == 0);
  }

  /**
   * There is no need for a separate time zone: cal1 and cal2 are supposed to
   * be appropriately configured with a time zone each for comparison.
   */
  private static boolean fieldEqual(Calendar cal1, Calendar cal2, int field) {
    assert preArgumentNotNull(cal1, "cal1");
    assert preArgumentNotNull(cal2, "cal2");
    return (! cal1.isSet(field)) ?
             (! cal2.isSet(field)) :
             (cal2.isSet(field) && (cal1.get(field) == cal2.get(field)));
  }

  /**
   * Express the same day date and day time as {@code d} is in the {@code from}
   * {@link TimeZone}, in the {@code to} {@link TimeZone}.
   */
  @MethodContract(
    pre  = {
      @Expression("from != null"),
      @Expression("to != null")
    },
    post = {
      @Expression("d == null ? result == null"),
      @Expression("d != null ? gregoriaCalendar(d, from).get(YEAR) == gregoriaCalendar(d, to).get(YEAR)"),
      @Expression("d != null ? gregoriaCalendar(d, from).get(MONTH) == gregoriaCalendar(d, to).get(MONTH)"),
      @Expression("d != null ? gregoriaCalendar(d, from).get(DAY_OF_MONTH) == gregoriaCalendar(d, to).get(DAY_OF_MONTH)"),
      @Expression("d != null ? gregoriaCalendar(d, from).get(HOUR_OF_DAY) == gregoriaCalendar(d, to).get(HOUR_OF_DAY)"),
      @Expression("d != null ? gregoriaCalendar(d, from).get(MINUTE) == gregoriaCalendar(d, to).get(MINUTE)"),
      @Expression("d != null ? gregoriaCalendar(d, from).get(SECOND) == gregoriaCalendar(d, to).get(SECOND)"),
      @Expression("d != null ? gregoriaCalendar(d, from).get(MILLISECOND) == gregoriaCalendar(d, to).get(MILLISECOND)")
    }
  )
  public static Date move(Date d, TimeZone from, TimeZone to) {
    assert preArgumentNotNull(from, "from");
    assert preArgumentNotNull(to, "to");
    if (d == null) {
      return null;
    }
    GregorianCalendar fromC = new GregorianCalendar(from);
    fromC.setTime(d);
    int year = fromC.get(YEAR);
    int month = fromC.get(MONTH);
    int dayOfMonth = fromC.get(DAY_OF_MONTH);
    int hour = fromC.get(HOUR_OF_DAY);
    int minute = fromC.get(MINUTE);
    int second = fromC.get(SECOND);
    int millisecond = fromC.get(MILLISECOND);
    GregorianCalendar toC = new GregorianCalendar(to);
    toC.set(year, month, dayOfMonth, hour, minute, second);
    toC.set(MILLISECOND, millisecond);
    return toC.getTime();
  }

}
