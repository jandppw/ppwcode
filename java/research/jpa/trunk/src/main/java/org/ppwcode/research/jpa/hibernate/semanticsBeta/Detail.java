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

package org.ppwcode.research.jpa.hibernate.semanticsBeta;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.persistence_III.jpa.AbstractIntegerIdVersionedPersistentBean;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;

/**
 * Detail
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="org_ppwcode_research_jpa_crud_semanticsalpha_detail")
@Copyright("2008 - $Date: 2008-10-23 11:51:38 +0200 (Thu, 23 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3245 $",
         date     = "$Date: 2008-10-23 11:51:38 +0200 (Thu, 23 Oct 2008) $")
public class Detail extends AbstractIntegerIdVersionedPersistentBean {


  /*<property name="Detail Date">
  -------------------------------------------------------------------------*/

  /**
   * Can be {@code null}.
   */
  public final Date getDate() {
    return $date == null ? null : (Date)$date.clone();
  }

  /**
   * @post equalsWithNull(getDate(), date);
   */
  public final void setDate(Date date) {
    if (date != null) {
      $date = date;
    } else {
      $date = null;
    }
  }

  /**
   * Can be {@code null}.
   */

  @Column(name="date")
  private Date $date;

  /*</property>*/



  /*<property name="Master">
  -------------------------------------------------------------------------*/

  @Basic(init = @Expression("master == null"),
         invars = @Expression("master != null ? master.details.contains(this)"))
  public final Master getMaster() {
    return $master;
  }

  @MethodContract(post = { @Expression("master == _master"),
                           @Expression("'master != null ? ! 'master.details.contains(this)"),
                           @Expression("_master != null ? _master.details.contains(this)")})
  public final void setMaster(Master master) {
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

  @ManyToOne(cascade = {}, optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name="master_fk", nullable = false)
  private Master $master;

  /*</property>*/

}
