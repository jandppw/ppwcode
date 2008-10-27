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

import java.util.Date;
import java.util.GregorianCalendar;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.DateHelpers;
import org.ppwcode.value_III.time.Duration;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * General supporting code for time interval implementations that store a begin and and end, and calculate a duration.
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
public abstract class AbstractBeginEndTimeInterval extends AbstractTimeInterval {

  @MethodContract(
    post = {
      @Expression("begin == _begin"),
      @Expression("end == _end")
    },
    exc  = @Throw(type = IllegalIntervalException.class,
                  cond = @Expression("! le(_begin, _end"))
  )
  protected AbstractBeginEndTimeInterval(Date begin, Date end) throws IllegalIntervalException {
    if (! DateHelpers.le(begin, end)) {
      throw new IllegalIntervalException(begin, end, "NOT_BEGIN_LE_END");
    }
    $begin = klone(begin);
    $end = klone(end);
  }



  /*<property name="begin">*/
  //------------------------------------------------------------------

  @Basic
  public final Date getBegin() {
    return klone($begin);
  }

  private Date $begin;

  /*</property>*/



  /*<property name="end">*/
  //------------------------------------------------------------------

  @Basic
  public final Date getEnd() {
    return klone($end);
  }

  private Date $end;

  /*</property>*/



  /*<property name="duration">*/
  //------------------------------------------------------------------

  /**
   * The duration is the delta between begin and end.
   * This can be {@code null}.
   */
  @MethodContract(post = @Expression("delta(begin, end)"))
  public final Duration getDuration() {
    if ($begin == null || $end == null) {
      return null;
    }
    else {
      GregorianCalendar gcB = new GregorianCalendar();
      gcB.setTime($begin);
      GregorianCalendar gcE = new GregorianCalendar();
      gcE.setTime($end);
      long ms = gcE.getTimeInMillis() - gcB.getTimeInMillis();
      return new Duration(ms, Duration.Unit.MILLISECOND);
    }
  }

  /*</property>*/

}
