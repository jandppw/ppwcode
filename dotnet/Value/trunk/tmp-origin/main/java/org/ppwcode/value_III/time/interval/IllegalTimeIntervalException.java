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

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.ValueException;
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
public class IllegalTimeIntervalException extends ValueException {

  /*<construction>*/
  //------------------------------------------------------------------

  @MethodContract(
    pre  = @Expression("_iType != null"),
    post = {
      @Expression("value == null"),
      @Expression("valueType == _tiType"),
      @Expression("begin == _begin"),
      @Expression("end == _end"),
      @Expression("message == _messageKey"),
      @Expression("cause == _cause")
    }
  )
  public IllegalTimeIntervalException(Class<? extends TimeInterval> tiType, Date begin, Date end, String messageKey, Throwable cause) {
    super(tiType, messageKey, cause);
    $begin = klone(begin);
    $end = klone(end);
  }

  @MethodContract(
    pre  = @Expression("_ti != null"),
    post = {
      @Expression("value == _ti"),
      @Expression("valueType == _ti.class"),
      @Expression("begin == _begin"),
      @Expression("end == _end"),
      @Expression("message == _messageKey"),
      @Expression("cause == _cause")
    }
  )
  public IllegalTimeIntervalException(TimeInterval ti, Date begin, Date end, String messageKey, Throwable cause) {
    super(ti, messageKey, cause);
    $begin = klone(begin);
    $end = klone(end);
  }

  /*</construction>*/



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

}

