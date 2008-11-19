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
import java.util.TimeZone;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * Cannot create a new {@link TimeInterval} with the given parameters.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class IllegalTimeZoneTimeIntervalException extends IllegalTimeIntervalException {

  /*<construction>*/
  //------------------------------------------------------------------

  @MethodContract(
    pre  = @Expression("_iType != null"),
    post = {
      @Expression("value == null"),
      @Expression("begin == _begin"),
      @Expression("end == _end"),
      @Expression("timeZone == _timeZone"),
      @Expression("message == _messageKey"),
      @Expression("cause == _cause")
    }
  )
  public IllegalTimeZoneTimeIntervalException(Class<? extends TimeZoneTimeInterval> tiType, Date begin, Date end, TimeZone timeZone, String messageKey, Throwable cause) {
    super(tiType, begin, end, messageKey, cause);
    $timeZone = klone(timeZone);
  }

  @MethodContract(
    pre  = @Expression("_tzti != null"),
    post = {
      @Expression("value == _tzti"),
      @Expression("valueType == _tzti.class"),
      @Expression("begin == _begin"),
      @Expression("end == _end"),
      @Expression("message == _messageKey"),
      @Expression("cause == _cause")
    }
  )
  public IllegalTimeZoneTimeIntervalException(TimeZoneTimeInterval tzti, Date begin, Date end, TimeZone timeZone, String messageKey, Throwable cause) {
    super(tzti, begin, end, messageKey, cause);
    $timeZone = klone(timeZone);
  }

  /*<construction>*/


  /*<property name="time zone">*/
  //------------------------------------------------------------------

  @Basic
  public final TimeZone getTimeZone() {
    return klone($timeZone);
  }

  private TimeZone $timeZone;

  /*</property>*/

}

