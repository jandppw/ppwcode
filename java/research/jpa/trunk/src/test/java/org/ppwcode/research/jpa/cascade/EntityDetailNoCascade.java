/*<license>
Copyright 2008 - $Date: 2008-11-03 20:53:52 +0100 (Mon, 03 Nov 2008) $ by PeopleWare n.v.

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

package org.ppwcode.research.jpa.cascade;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.persistence_III.jpa.AbstractIntegerIdVersionedPersistentBean;

@Entity
@Table(name="be_hdp_contracts_i_semantics_detail_nocascade")
@Copyright("2008 - $Date: 2008-11-03 20:53:52 +0100 (Mon, 03 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3413 $",
         date     = "$Date: 2008-11-03 20:53:52 +0100 (Mon, 03 Nov 2008) $")
public class EntityDetailNoCascade extends AbstractIntegerIdVersionedPersistentBean {


  public void setDescription(String desc) {
    $description = desc;
  }

  public String getDescription() {
    return $description;
  }

  @Column(name = "description")
  private String $description = "";


  public void setMaster(EntityMasterNoCascade master) {
    if ($master != master) {
      if ($master != null) {
        $master.removeDetail(this);
      }
      $master = master;
      if ($master != null) {
        $master.addDetail(this);
      }
    }
  }

  public EntityMasterNoCascade getMaster() {
    return $master;
  }

  @ManyToOne(cascade={CascadeType.REFRESH} , optional = false)
  @JoinColumn(name="master_fk", nullable = false)
  private EntityMasterNoCascade $master;

}
