/*<license>
  Copyright 2008, AristA vzw
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.research.jpa.crud.semanticsAlpha;


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

import org.joda.time.DateMidnight;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.persistence_III.AbstractIntegerIdVersionedPersistentBean;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;

/**
 * MUDO contract hierarchy; this should become ...arista.secondlinecontract
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="be_hdp_contracts_i_secondlinecontract")
@Copyright("2004 - $Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2727 $",
         date     = "$Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $")
public class SecondLineContract extends AbstractIntegerIdVersionedPersistentBean {


  /*<property name="Contract Start Date">
  -------------------------------------------------------------------------*/

  /**
   * If the Start date is {@code null}, it means the enterprise
   * still exists.
   */
  public final Date getStartDate() {
    return $startDate == null ? null : (Date)$startDate.clone();
  }

  /**
   * @post equalsWithNull(getStartDate(), StartDate);
   */
  public final void setStartDate(Date startDate) {
    if (startDate != null) {
      $startDate = (new DateMidnight(startDate)).toDate();
    } else {
      $startDate = null;
    }
  }

  /**
   * Can be {@code null}.
   */

  @Column(name="start_date")
  // TODO Contracts
  private Date $startDate;

  /*</property>*/


  /*<property name="Termination Date">
  -------------------------------------------------------------------------*/

  /**
   * If the termination date is {@code null}, it means the enterprise
   * still exists.
   */
  public final Date getTerminationDate() {
    return $terminationDate == null ? null : (Date)$terminationDate.clone();
  }

  /**
   * @post equalsWithNull(getTerminationDate(), terminationDate);
   */
  public final void setTerminationDate(Date terminationDate) {
    if (terminationDate != null) {
      $terminationDate = (new DateMidnight(terminationDate)).toDate();
    } else {
      $terminationDate = null;
    }
  }

  /**
   * Can be {@code null}.
   */
  @Column(name="termination_date")
  //TODO Contracts
  private Date $terminationDate;

  /*</property>*/


  /*<property name="Enterprise">
  -------------------------------------------------------------------------*/

  @Basic(init = @Expression("enterprise == null"),
         invars = @Expression("enterprise != null ? enterprise.contracts.contains(this)"))
  public final Enterprise getEnterprise() {
    return $enterprise;
  }

  @MethodContract(post = { @Expression("enterprise == _enterprise"),
                           @Expression("'enterprise != null ? ! 'enterprise.contracts.contains(this)"),
                           @Expression("_enterprise != null ? _enterprise.contracts.contains(this)")})
  public final void setEnterprise(Enterprise enterprise) {
    if ($enterprise != enterprise) {
      if ($enterprise != null) {
        $enterprise.removeContract(this);
      }
      $enterprise = enterprise;
      if ($enterprise != null) {
        $enterprise.addContract(this);
      }
    }
  }

  @ManyToOne(cascade = {}, optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name="enterprise_fk", nullable = false)
  private Enterprise $enterprise;

  /*</property>*/

}
