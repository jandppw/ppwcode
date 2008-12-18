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
 * <p>An effective time interval, that takes a begin date and an end date as constructor parameters,
 *   but does not keep the time within a day.</p>
 * <p>Begin and end are to be interpreted as mere dates, not times. In Java, we need to use {@link Date}
 *   also when we are not interested in the time part of the point in time (see the discussion on the
 *   sense and nonsense of {@link java.sql.Date} in the <a href="package.html">package documentation</a>).
 *   We choose to express the day date as the {@link Date}, with the time part being midnight. This however
 *   depends on the time zone. In general, this makes sense, since semantically limiting yourself to the
 *   time part of a time is not really possible. Imagine a contract that starts on the 20th of January.
 *   When push comes to shove, if your client is in Tokio, this could mean something completely different
 *   from when your client is in New York, both legally as when you are supposed to start service.</p>
 * <p>Thus, in practice, this class only offers extra validation during construction, that for both
 *   the begin and end, the day time is midnight in the given time zone. For the rest, the class behaves
 *   like a regular time zone. This is the closest we can get to emulate ignoring the concept of day time.</p>
 * <p>It is not possible for both the begin and the end date to be {@code null}.</p>
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("! (begin == null && end == null)"),
  @Expression("begin == null || isDayDate(begin, timeZone)"),
  @Expression("end == null || isDayDate(end, timeZone)")
})
public final class DayDateTimeInterval extends AbstractBeginEndTimeZoneTimeInterval {

  @MethodContract(
    pre  = {
      @Expression("_tz != null")
    },
    post = {
      @Expression("begin == _begin"),
      @Expression("end == _end")
    },
    exc  = {
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("_begin == null && _end == null")),
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("! le(_begin, _end")),
      @Throw(type = IllegalTimeZoneTimeIntervalException.class, cond = @Expression("begin != null && ! isDayDate(_begin, _tz)")),
      @Throw(type = IllegalTimeZoneTimeIntervalException.class, cond = @Expression("end != null && ! isDayDate(_end, _tz)"))
    }
  )
  public DayDateTimeInterval(Date begin, Date end, TimeZone tz) throws IllegalTimeIntervalException {
    super(begin, end, tz);
    if ((begin != null && ! isDayDate(begin, tz)) || (end != null &&! isDayDate(end, tz))) {
      throw new IllegalTimeZoneTimeIntervalException(begin, end, tz, "BEGIN_AND_END_MUST_BE_DAYDATE");
    }
  }

  @MethodContract(
    post = {},
    exc  = {
      @Throw(type = IllegalTimeZoneTimeIntervalException.class,
             cond = @Expression("_stubBegin != null && ! isDayDate(_stubBegin, timeZone")),
      @Throw(type = IllegalTimeZoneTimeIntervalException.class,
             cond = @Expression("_stubEnd != null && ! isDayDate(_stubEnd, timeZone"))
    }
  )
  public DayDateTimeInterval determinate(Date stubBegin, Date stubEnd) throws IllegalTimeIntervalException {
    if ((stubBegin != null && ! isDayDate(stubBegin, getTimeZone())) ||
        (stubEnd != null &&! isDayDate(stubEnd, getTimeZone()))) {
      throw new IllegalTimeZoneTimeIntervalException(stubBegin, stubEnd, getTimeZone(), "BEGIN_AND_END_MUST_BE_DAYDATE");
      // TODO pass this into the exception?
    }
    return new DayDateTimeInterval(determinateBegin(stubBegin), determinateEnd(stubEnd), getTimeZone());
  }

}
