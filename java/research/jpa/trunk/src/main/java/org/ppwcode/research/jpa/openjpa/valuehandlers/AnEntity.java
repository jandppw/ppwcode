/*<license>
Copyright 2008 - $Date: 2008-10-23 11:51:38 +0200 (Thu, 23 Oct 2008) $ by PeopleWare n.v.

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.localization.LocalizedString;
import org.ppwcode.vernacular.persistence_III.jpa.AbstractIntegerIdVersionedPersistentBean;

/**
 * AnEnity
 */
@Entity
@Table(name="org_ppwcode_research_jpa_openjpa_valuehandlers_anentity")
@Copyright("2008 - $Date: 2008-10-23 11:51:38 +0200 (Thu, 23 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3245 $",
         date     = "$Date: 2008-10-23 11:51:38 +0200 (Thu, 23 Oct 2008) $")
public class AnEntity extends AbstractIntegerIdVersionedPersistentBean {

  public final LocalizedString getLocalizedString() {
    return $localizedString;
  }

  public final void setLocalizedString(LocalizedString localizedString) {
    $localizedString = localizedString;
  }

  @Column(name="localizedstring")
  private LocalizedString $localizedString;

}
