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
import static org.ppwcode.value_III.time.TimeHelpers.le;
import static org.ppwcode.value_III.time.Duration.delta;

import java.util.Date;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.Duration;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * General supporting code for time interval implementations that store a begin and and end,
 * and calculate a duration. In this case, it is impossible for both the begin and end date
 * to be {@code null}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("! (begin == null && end == null)"))
public abstract class AbstractBeginEndTimeInterval extends AbstractTimeInterval {

  // MUDO this is absolutely not the way to go; this way all validation is circumvented
  protected AbstractBeginEndTimeInterval() {}

  @MethodContract(
    post = {
      @Expression("begin == _begin"),
      @Expression("end == _end")
    },
    exc  = {
      @Throw(type = IllegalTimeIntervalException.class,
             cond = @Expression("_begin == null && _end == null")),
      @Throw(type = IllegalTimeIntervalException.class,
             cond = @Expression("! le(_begin, _end"))
    }
  )
  protected AbstractBeginEndTimeInterval(Date begin, Date end) throws IllegalTimeIntervalException {
    if (begin == null && end == null) {
      throw new IllegalTimeIntervalException(getClass(), begin, end, "NOT_BEGIN_AND_END_NULL", null);
    }
    if (begin != null && end != null && ! le(begin, end)) {
      throw new IllegalTimeIntervalException(getClass(), begin, end, "NOT_BEGIN_LE_END", null);
    }
    this.begin = klone(begin);
    this.end = klone(end);
  }



  /*<property name="begin">*/
  //------------------------------------------------------------------

  @Basic
  public final Date getBegin() {
    return klone(begin);
  }

  // MUDO this needs a $; if the $ must be removed, something else is wrong
  private Date begin;

  /*</property>*/



  /*<property name="end">*/
  //------------------------------------------------------------------

  @Basic
  public final Date getEnd() {
    return klone(end);
  }

  // MUDO this needs a $; if the $ must be removed, something else is wrong
  private Date end;

  /*</property>*/



  /*<property name="duration">*/
  //------------------------------------------------------------------

  /**
   * The duration is the delta between begin and end.
   * This can be {@code null}.
   */
  @MethodContract(post = @Expression("begin == null || end == null ? null : delta(begin, end)"))
  public final Duration getDuration() {
    if (begin == null || end == null) {
      return null;
    }
    else {
      return delta(end, begin);
    }
  }

  /*</property>*/

}
