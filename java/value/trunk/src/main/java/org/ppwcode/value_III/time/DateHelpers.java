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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * Static methods for working with dates
 *
 * @author    Jan Dockx
 * @author    Peopleware NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DateHelpers {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Don't instantiate this class.
   */
  private DateHelpers() {
    // NOP
  }

  /*</construction>*/



  /**
   * {@code d1} is <em>before or equal</em> to {@code d2}.
   * This method also deals with {@code null} dates.
   * {@code null} dates are always earlier.
   */
  @MethodContract(post = @Expression("d1 == null || (d2 != null && d1.before(d2)) || d1.equals(d2)"))
  public static boolean le(Date d1, Date d2) {
    return d1 == null || (d2 != null && d1.before(d2)) || d1.equals(d2);
  }

  @MethodContract(post = @Expression("(date1 == null) ? (date2 == null) : dayDate(date1) == dayDate(date2)"))
  public static boolean sameDay(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      GregorianCalendar cal1 = new GregorianCalendar();
      cal1.setTime(date1);
      GregorianCalendar cal2 = new GregorianCalendar();
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
   * Change <code>date</code> so that it has no time information
   * more accurate then the day level. More accurate information
   * is set to <code>0</code>.
   *
   * @mudo don't use deprecated methods in contract
   * @note This was difficult to get right. Don't change without tests!
   */
  @MethodContract(post = {
    @Expression("_date == null ? result == null"),
    @Expression("_date != null ? result != date"),
    @Expression("_date != null ? result.year == date.year"),
    @Expression("_date != null ? result.month == date.month"),
    @Expression("_date != null ? result.day == date.day"),
    @Expression("_date != null ? result.hours == 0"),
    @Expression("_date != null ? result.minutes == 0"),
    @Expression("_date != null ? result.seconds == 0"),
    @Expression("_date != null ? result.milliseconds == 0")
  })
  public static Date dayDate(Date date) {
    if (date == null) {
      return null;
    }
    GregorianCalendar cal = new GregorianCalendar();
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

  @MethodContract(post = @Expression("(date != null) && date == dayDate(date)"))
  public static boolean isDayDate(Date date) {
    if (date == null) {
      return false;
    }
    else {
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      return notSetOr0(cal, Calendar.HOUR) &&
             notSetOr0(cal, Calendar.HOUR_OF_DAY) &&
             notSetOr0(cal, Calendar.MINUTE) &&
             notSetOr0(cal, Calendar.SECOND) &&
             notSetOr0(cal, Calendar.MILLISECOND);
    }
  }

  private static boolean notSetOr0(Calendar cal, int field) {
    assert cal != null;
    return (! cal.isSet(field)) || (cal.get(field) == 0);
  }

  private static boolean fieldEqual(Calendar cal1, Calendar cal2, int field) {
    assert cal1 != null;
    assert cal2 != null;
    return (! cal1.isSet(field)) ?
             (! cal2.isSet(field)) :
             (cal2.isSet(field) && (cal1.get(field) == cal2.get(field)));
  }

}
