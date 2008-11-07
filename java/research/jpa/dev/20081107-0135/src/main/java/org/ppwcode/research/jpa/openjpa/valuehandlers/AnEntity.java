/*<license>
Copyright 2008 - $Date$ by PeopleWare n.v.

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

package org.ppwcode.research.jpa.openjpa.valuehandlers;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Locale;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.localization.LocalizedString;
import org.ppwcode.value_III.time.interval.BeginEndTimeInterval;
import org.ppwcode.value_III.time.interval.DayDateTimeInterval;
import org.ppwcode.value_III.time.interval.DeterminateIntradayTimeInterval;
import org.ppwcode.value_III.time.interval.IntradayTimeInterval;
import org.ppwcode.vernacular.persistence_III.VersionedPersistentBean;

/**
 * AnEnity
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface AnEntity extends VersionedPersistentBean<Integer, Integer> {

  LocalizedString getLocalizedString();

  void setLocalizedString(LocalizedString localizedString);


  LocalizedString getLocalizedString2();

  void setLocalizedString2(LocalizedString localizedString);


  Locale getLocale();

  void setLocale(Locale locale);


  BeginEndTimeInterval getBeginEndTimeInterval();

  void setBeginEndTimeInterval(BeginEndTimeInterval beti);


  DayDateTimeInterval getDayDateTimeInterval();

  void setDayDateTimeInterval(DayDateTimeInterval ddti);


  IntradayTimeInterval getIntradayTimeInterval();

  void setIntradayTimeInterval(IntradayTimeInterval iti);


  DeterminateIntradayTimeInterval getDeterminateIntradayTimeInterval();

  void setDeterminateIntradayTimeInterval(DeterminateIntradayTimeInterval diti);

}
