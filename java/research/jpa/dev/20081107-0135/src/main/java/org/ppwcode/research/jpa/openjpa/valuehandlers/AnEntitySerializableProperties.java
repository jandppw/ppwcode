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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.localization.LocalizedString;
import org.ppwcode.value_III.time.interval.BeginEndTimeInterval;
import org.ppwcode.value_III.time.interval.DayDateTimeInterval;
import org.ppwcode.value_III.time.interval.DeterminateIntradayTimeInterval;
import org.ppwcode.value_III.time.interval.IntradayTimeInterval;
import org.ppwcode.vernacular.persistence_III.jpa.AbstractIntegerIdVersionedPersistentBean;

/**
 * AnEnity
 */
@Entity
@Table(name="org_ppwcode_research_jpa_openjpa_valuehandlers_anentityserializableproperties")
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class AnEntitySerializableProperties extends AbstractIntegerIdVersionedPersistentBean implements AnEntity {

  public final LocalizedString getLocalizedString() {
    return $localizedString;
  }

  public final void setLocalizedString(LocalizedString localizedString) {
    $localizedString = localizedString;
  }

  @Column(name="localizedstring")
  private LocalizedString $localizedString;





  public final LocalizedString getLocalizedString2() {
    return $localizedString2;
  }

  public final void setLocalizedString2(LocalizedString localizedString) {
    $localizedString2 = localizedString;
  }

  @Column(name="localizedstring2")
  private LocalizedString $localizedString2;





  public final Locale getLocale() {
    return $locale;
  }

  public final void setLocale(Locale locale) {
    $locale = locale;
  }

  @Column(name="locale")
  private Locale $locale;





  public final BeginEndTimeInterval getBeginEndTimeInterval() {
    return $beginEndTimeInterval;
  }

  public final void setBeginEndTimeInterval(BeginEndTimeInterval beti) {
    $beginEndTimeInterval = beti;
  }

  @Column(name = "beginEndTimeInterval")
  private BeginEndTimeInterval $beginEndTimeInterval;





  public final DayDateTimeInterval getDayDateTimeInterval() {
    return $dayDateTimeInterval;
  }

  public final void setDayDateTimeInterval(DayDateTimeInterval ddti) {
    $dayDateTimeInterval = ddti;
  }

  @Column(name = "dayDateTimeInterval")
  private DayDateTimeInterval $dayDateTimeInterval;





  public final IntradayTimeInterval getIntradayTimeInterval() {
    return $intradayTimeInterval;
  }

  public final void setIntradayTimeInterval(IntradayTimeInterval iti) {
    $intradayTimeInterval = iti;
  }

  @Column(name = "intradayTimeInterval")
  private IntradayTimeInterval $intradayTimeInterval;





  public final DeterminateIntradayTimeInterval getDeterminateIntradayTimeInterval() {
    return $determinateIntradayTimeInterval;
  }

  public final void setDeterminateIntradayTimeInterval(DeterminateIntradayTimeInterval diti) {
    $determinateIntradayTimeInterval = diti;
  }

  @Column(name = "determinateIntradayTimeInterval")
  private DeterminateIntradayTimeInterval $determinateIntradayTimeInterval;

}
