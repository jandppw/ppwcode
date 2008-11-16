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
import static org.ppwcode.util.reflect_I.CloneHelpers.klone;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

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
 * General supporting code for time interval implementations that store a begin and and end
 * and need a {@link TimeZone}. The time zone can never be {@code null}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @mudo unit test
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("! (begin == null && end == null)"))
public abstract class AbstractBeginEndTimeZoneTimeInterval extends AbstractBeginEndTimeInterval
    implements TimeZoneTimeInterval {

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
      @Throw(type = IllegalTimeIntervalException.class,
             cond = @Expression("_begin == null && _end == null")),
      @Throw(type = IllegalTimeIntervalException.class,
             cond = @Expression("! le(_begin, _end"))
    }
  )
  protected AbstractBeginEndTimeZoneTimeInterval(Date begin, Date end, TimeZone tz) throws IllegalTimeIntervalException {
    super(begin, end);
    preArgumentNotNull(tz, "tz");
    $timeZone = tz;
  }



  /*<property name="timeZone">*/
  //------------------------------------------------------------------

  public final TimeZone getTimeZone() {
    return klone($timeZone);
  }

  private TimeZone $timeZone;

  /*</property>*/



  @Override
  public String toString() {
    return super.toString() + "time zone(" + getTimeZone().getDisplayName();
  }


}
