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

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * An effective time interval, that takes a begin date and an end date as constructor parameters,
 * but does not keep the time within a day. Begin and end are mere dates, not times.
 * It is not possible for both the bein and the end date to be {@code null}.
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
  @Expression("begin == null || isDayDate(begin)"),
  @Expression("end == null || isDayDate(end)")
})
public final class DayDateTimeInterval extends AbstractBeginEndTimeInterval {

  @MethodContract(
    post = {
      @Expression("begin == _begin"),
      @Expression("end == _end")
    },
    exc  = {
      @Throw(type = IllegalIntervalException.class, cond = @Expression("_begin == null && _end == null")),
      @Throw(type = IllegalIntervalException.class, cond = @Expression("! le(_begin, _end")),
      @Throw(type = IllegalIntervalException.class, cond = @Expression("begin != null && ! isDayDate(_begin)")),
      @Throw(type = IllegalIntervalException.class, cond = @Expression("end != null && ! isDayDate(_end)"))
    }
  )
  public DayDateTimeInterval(Date begin, Date end) throws IllegalIntervalException {
    super(begin, end);
    if ((begin != null && ! isDayDate(begin)) || (end != null &&! isDayDate(end))) {
      throw new IllegalIntervalException(begin, end, "BEGIN_AND_END_MUST_BE_DAYDATE");
    }
  }

  public DayDateTimeInterval determinate(Date stubBegin, Date stubEnd) throws IllegalIntervalException {
    return new DayDateTimeInterval(determinateBegin(stubBegin), determinateEnd(stubEnd));
  }

}
