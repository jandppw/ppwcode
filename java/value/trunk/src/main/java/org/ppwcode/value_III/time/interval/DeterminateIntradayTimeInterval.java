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
 * An effective intra-day time interval, for which begin and end are mandatory to be determinate.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
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
public final class DeterminateIntradayTimeInterval extends AbstractIntradayTimeInterval {

  @SuppressWarnings("unused")
  private DeterminateIntradayTimeInterval() {}
  
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
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("! le(_begin, _end")),
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("_begin == null")),
      @Throw(type = IllegalTimeIntervalException.class, cond = @Expression("_end == null")),
      @Throw(type = IllegalTimeZoneTimeIntervalException.class, cond = @Expression("! sameDay(_begin, _end, _tz)"))
    }
  )
  public DeterminateIntradayTimeInterval(Date begin, Date end, TimeZone tz) throws IllegalTimeIntervalException {
    super(begin, end, tz);
    if (begin == null || end == null) {
      throw new IllegalTimeIntervalException(getClass(), begin, end,"BEGIN_AND_END_MANDATORY", null);
    }
  }

  public DeterminateIntradayTimeInterval determinate(Date stubBegin, Date stubEnd) {
    return this; // we are determinate ourselfs
  }

}
