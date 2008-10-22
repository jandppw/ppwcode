/*<license>
Copyright 2004 - $Date: 2008-10-19 00:39:45 +0200 (Sun, 19 Oct 2008) $ by PeopleWare n.v..

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

import java.util.Arrays;
import java.util.Date;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.Duration;
import org.ppwcode.vernacular.value_III.ImmutableValue;


/**
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date: 2008-10-19 00:39:45 +0200 (Sun, 19 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3187 $",
         date     = "$Date: 2008-10-19 00:39:45 +0200 (Sun, 19 Oct 2008) $")
public abstract class AbstractTimeInterval implements TimeInterval {

  public final AllenRelation compareTo(TimeInterval other) {
    AllenRelation result = AllenRelation.FULL;


    return result;
  }

}
