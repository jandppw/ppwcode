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

import org.apache.openjpa.persistence.jdbc.Columns;
import org.apache.openjpa.persistence.jdbc.Strategy;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.localization.LocalizedString;
import org.ppwcode.vernacular.persistence_III.jpa.AbstractIntegerIdVersionedPersistentBean;

/**
 * AnEnity
 */
@Entity
@Table(name="org_ppwcode_research_jpa_openjpa_valuehandlers_anentityvaluehandlerproperties")
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class AnEntityValueHandlerProperties extends AbstractIntegerIdVersionedPersistentBean implements AnEntity {

  public final LocalizedString getLocalizedString() {
    return $localizedString;
  }

  public final void setLocalizedString(LocalizedString localizedString) {
    $localizedString = localizedString;
  }

//  @Column(name="localizedstring")

  @Strategy("org.ppwcode.value_III.localization.LocalizedStringValueHandler")
  private LocalizedString $localizedString;




  public final LocalizedString getLocalizedString2() {
    return $localizedString2;
  }

  public final void setLocalizedString2(LocalizedString localizedString) {
    $localizedString2 = localizedString;
  }

  @Columns({
    @Column(name="localizedstring2_locale"),
    @Column(name="localizedstring2_string")
  })
  @Strategy("org.ppwcode.value_III.localization.LocalizedStringValueHandler")
  private LocalizedString $localizedString2;








  public final Locale getLocale() {
    return $locale;
  }

  public final void setLocale(Locale locale) {
    $locale = locale;
  }

  @Column(name="locale")
  @Strategy("org.ppwcode.value_III.propertyeditors.java.util.LocaleValueHandler")
  private Locale $locale;

}
