/*<license>
Copyright 2004 - $Date: 2008-10-24 16:52:32 +0200 (Fri, 24 Oct 2008) $ by PeopleWare n.v..

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

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * An actual time interval, that is constructed using a begin and an end date, and has no further
 * restrictions.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 *
 * @mudo unit test
 */
@Copyright("2008 - $Date: 2008-10-24 16:52:32 +0200 (Fri, 24 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3285 $",
         date     = "$Date: 2008-10-24 16:52:32 +0200 (Fri, 24 Oct 2008) $")
public final class BeginEndTimeInterval extends AbstractBeginEndTimeInterval {

  @MethodContract(
    post = {
      @Expression("begin == _begin"),
      @Expression("end == _end")
    },
    exc  = @Throw(type = IllegalIntervalException.class,
                  cond = @Expression("! le(_begin, _end"))
  )
  public BeginEndTimeInterval(Date begin, Date end) throws IllegalIntervalException {
    super(begin, end);
  }

  public BeginEndTimeInterval determinate(Date stubBegin, Date stubEnd) throws IllegalIntervalException {
    return new BeginEndTimeInterval(determinateBegin(stubBegin), determinateEnd(stubEnd));
  }

}
