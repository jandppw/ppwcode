/*<license>
Copyright 2004 - $Date: 2008-10-30 00:25:11 +0100 (Thu, 30 Oct 2008) $ by PeopleWare n.v..

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
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>A time interval that requires an explicit time zone. This is needed for those kinds of
 *   time intervals for which, from a semantic standpoint, there is no need for a precise
 *   point in time as begin or end, or for which invariants require evaluation of the precise
 *   begin or end time in the context of a time zone. Note that the time zone is not
 *   a part of the definition of equality or other comparisons.</p>
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date: 2008-10-30 00:25:11 +0100 (Thu, 30 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3384 $",
   date     = "$Date: 2008-10-30 00:25:11 +0100 (Thu, 30 Oct 2008) $")
@Invars({@Expression("timeZone != null")})
public interface TimeZoneTimeInterval extends TimeInterval {

  /**
   * This time interval is interpreted in this time zone.
   */
  @Basic
  TimeZone getTimeZone();

  @MethodContract(post = @Expression("result.timeZone == timeZone"))
  TimeInterval determinate(Date stubBegin, Date stubEnd) throws IllegalTimeIntervalException;

}
