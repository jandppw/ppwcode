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
import static org.ppwcode.value_III.time.TimeHelpers.sameDay;

import java.util.Date;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * An effective intra-day time interval, for which begin and end are mandatory to be determinate.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 *
 * @mudo unit test
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("begin != null"),
  @Expression("end != null"),
  @Expression("sameDay(_begin, _end)")
})
public final class DeterminateIntradayTimeInterval extends AbstractBeginEndTimeInterval {

  @MethodContract(
    post = {
      @Expression("begin == _begin"),
      @Expression("end == _end")
    },
    exc  = {
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("! le(_begin, _end")),
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("_begin == null")),
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("_end == null")),
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("sameDay(_begin, _end)"))
    }
  )
  public DeterminateIntradayTimeInterval(Date begin, Date end) throws IllegalTimeIntervalException {
    super(begin, end);
    if (begin == null || end == null) {
      throw new IllegalTimeIntervalException(begin, end, "BEGIN_AND_END_MANDATORY");
    }
    if (! sameDay(begin, end)) {
      throw new IllegalTimeIntervalException(begin, end, "NOT_INSIDE_ONE_DAY");
    }
  }

  public DeterminateIntradayTimeInterval determinate(Date stubBegin, Date stubEnd) {
    return this; // we are determinate ourselfs
  }

  @MethodContract(post = @Expression("begin != null ? dayDate(begin) : end != null ? dayDate(end) : null"))
  public final Date getDay() {
    if (getBegin() != null) {
      return dayDate(getBegin());
    }
    else if (getEnd() != null) {
      return dayDate(getEnd());
    }
    else {
      return null;
    }
  }

}
