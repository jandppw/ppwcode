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
import static org.ppwcode.value_III.time.TimeHelpers.dayDate;
import static org.ppwcode.value_III.time.TimeHelpers.dayTime;
import static org.ppwcode.value_III.time.TimeHelpers.sameDay;

import java.util.Date;
import java.util.TimeZone;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>General code for time intervals that take a begin date and an end date as constructor parameters,
 *   but is restricted to be an intra-day interval, i.e., the begin and end {@link Date} must express
 *   times in the same day. This is dependent on the time zone, which is thus also needed.
 *   The time zone cannot be {@code null}.</p>
 * <p>It is not possible for both the begin and the end date to be {@code null}.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("begin != null && end != null ? sameDay(begin, end, timeZone)")
})
public abstract class AbstractIntradayTimeInterval extends AbstractBeginEndTimeZoneTimeInterval {

  @MethodContract(
    pre  = {
      @Expression("_tz != null")
    },
    post = {
      @Expression("begin == _begin"),
      @Expression("end == _end"),
      @Expression("timeZone == _tz")
    },
    exc  = {
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("_begin == null && _end == null")),
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("! le(_begin, _end")),
      @Throw(type = IllegalTimeZoneTimeIntervalException.class, cond = @Expression("! sameDay(_begin, _end, _tz)"))
    }
  )
  protected AbstractIntradayTimeInterval(Date begin, Date end, TimeZone tz) throws IllegalTimeIntervalException {
    super(begin, end, tz);
    if (begin != null && end != null) {
      if (! sameDay(begin, end, tz)) {
        throw new IllegalTimeZoneTimeIntervalException(getClass(), begin, end, tz, "NOT_INSIDE_ONE_DAY", null);
      }
    }
  }

  /**
   * Midnight in {@link #getTimeZone()} of the day of this time interval.
   */
  @MethodContract(post = @Expression("begin != null ? dayDate(begin) : end != null ? dayDate(end) : null"))
  public final Date getDay() {
    if (getBegin() != null) {
      return dayDate(getBegin(), getTimeZone());
    }
    else if (getEnd() != null) {
      return dayDate(getEnd(), getTimeZone());
    }
    else {
      return null;
    }
  }

  /**
   * The time of day of {@link #getBegin()}, but at the date of epoch (1/1/1970),
   * in the time zone {@link #getTimeZone()}.
   */
  @MethodContract(post = @Expression("dayTime(begin, timeZone"))
  public final Date getBeginTimeOfDay() {
    return dayTime(getBegin(), getTimeZone());
  }

  /**
   * The time of day of {@link #getBegin()}, but at the date of epoch (1/1/1970),
   * in the time zone {@link #getTimeZone()}.
   */
  @MethodContract(post = @Expression("dayTime(end, timeZone"))
  public final Date getEndTimeOfDay() {
    return dayTime(getEnd(), getTimeZone());
  }

}
