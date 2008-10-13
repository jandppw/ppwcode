/*<license>
  Copyright 2008, AristA vzw
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.research.jpa.crud.semanticsAlpha;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateMidnight;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.persistence_III.AbstractIntegerIdVersionedPersistentBean;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * An entity with a Belgian enterprise id for Belgian entities, and something similar
 * for possible foreign entities. These are the entities with which we enter into
 * a contract for delivering services, and to which we send invoices.
 */
@NamedQueries({
  @NamedQuery(name  = "countEnterprisesBySearchString",
              query = "SELECT COUNT(e) FROM Enterprise AS e WHERE e.$enterpriseId LIKE :ss OR e.$name LIKE :ss OR e.$address LIKE :ss"),
      // MUDO actually, what we should do is LIKE word1 AND LIKE word2 AND LIKE word3 AND ....
  @NamedQuery(name  = "countEnterprisesBySearchStringWithoutInactive",
              query = "SELECT COUNT(e) FROM Enterprise AS e WHERE (e.$enterpriseId LIKE :ss OR e.$name LIKE :ss OR e.$address LIKE :ss) AND e.$terminationDate IS NULL"),
      // MUDO actually, what we should do is LIKE word1 AND LIKE word2 AND LIKE word3 AND ....
  @NamedQuery(name  = "findEnterprisesBySearchString",
              query = "SELECT e FROM Enterprise AS e WHERE e.$enterpriseId LIKE :ss OR e.$name LIKE :ss OR e.$address LIKE :ss"),
  @NamedQuery(name  = "findEnterprisesBySearchStringWithoutInactive",
              query = "SELECT e FROM Enterprise AS e WHERE (e.$enterpriseId LIKE :ss OR e.$name LIKE :ss OR e.$address LIKE :ss) AND e.$terminationDate IS NULL"),
  @NamedQuery(name  = "findEnterprisesByEnterpriseId",
              query = "SELECT e FROM Enterprise AS e WHERE e.$enterpriseId LIKE :oid")
})
@Entity
@Table(name="be_hdp_contracts_i_semantics_enterprise")
@Copyright("2004 - $Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2727 $",
         date     = "$Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $")
public class Master extends AbstractIntegerIdVersionedPersistentBean {

  /*<property name="enterprise id">
  -------------------------------------------------------------------------*/

  /**
   * Can be {@code null}.
   */
  public final String getEnterpriseId() {
    return $enterpriseId;
  }

  public final void setEnterpriseId(String enterpriseId) {
    $enterpriseId = enterpriseId;
  }

  /**
   * @mudo make it multiple EnterpriseIdentifier
   */
  @Column(name="enterprise_id")
  private String $enterpriseId;

  /*</property>*/



  /*<property name="name">
  -------------------------------------------------------------------------*/

  /**
   * Can be {@code null}.
   */
  public final String getName() {
    return $name;
  }

  public final void setName(String s) {
    $name = s;
  }

  /**
   * @mudo make it multiple I15dString
   */
  @Column(name="name")
  private String $name;

  /*</property>*/



  /*<property name="address">
  -------------------------------------------------------------------------*/

  /**
   * Can be {@code null}.
   */
  public final String getAddress() {
    return $address;
  }

  public final void setAddress(String address) {
    $address = address;
  }

  /**
   * @mudo make it Address
   */
  @Column(name="address")
  private String $address;

  /*</property>*/



  /*<property name="termination date">
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
  @MethodContract(post = { @Expression("terminationDate.getYear().equals(_terminationdate.getYear())"),
                           @Expression("terminationDate.getMonth().equals(_terminationDate.getMonth())"),
                           @Expression("terminationDate.getDay().equals(_terminationDate.getDay())")} )
  public final void setTerminationDate(Date terminationDate) {
    if (terminationDate != null) {
      $terminationDate = (new DateMidnight(terminationDate)).toDate();
    } else {
      $terminationDate =  null;
    }
  }

  /**
   * Can be {@code null}.
   */
  @Column(name="termination_date")
  @Temporal(TemporalType.TIMESTAMP)
  @Invars( @Expression("$terminationDate != null ? { $terminationDate.getHours() == 0" +
      "                                              && $terminationDate.getMinutes() == 0" +
      "                                              && $terminationDate.getSeconds() == 0" +
      "                                              && $terminationDate.getTime % 1000 == 0}"))
  private Date $terminationDate;

  /*</property>*/



  /*<property name="contracts">
  -------------------------------------------------------------------------*/

  @MethodContract(pre  = {@Expression("_c != null"),
                          @Expression("_c.enterprise == this")},
                  post = @Expression("contracts.contains(_c)"))
  final void addContract(Detail c) {
    assert preArgumentNotNull(c, "c");
    $contracts.add(c);
  }

  @MethodContract(post = @Expression("! contracts.contains(_c)"))
  final void removeContract(Detail c) {
    $contracts.remove(c);
  }

  @Basic(init   = @Expression("contracts.isEmpty()"),
         invars = {@Expression("contracts != null"),
                   @Expression("! contracts.contains(null)"),
                   @Expression("for (Contract c : contracts) { c.enterprise == this }")})
  final public Set<Detail> getContracts() {
    return $contracts == null ? null : new HashSet<Detail>($contracts);
  }

  /**
   * @note When this is used in a JPA context, this is replaced by a reference to a proxy set to enable lazy loading.
   *       When the set is accessed while the object is attached to an entity manager, this proxy is filled, and from
   *       then on behaves like a regular set.
   *       When an object is detached however before the set is accessed, the set proxy wil remain uninitialized. What
   *       we know for sure is, that when we serialize and deserialize an object of this class, that the resulting
   *       object in that case has a null $contracts. In other words, we cannot enforce the invariant that $contracts != null.
   *       There are 2 possible solutions to this problem. Either we check everywhere where we use $contracts, and create
   *       an empty hash set lazily, or we change the contract of getContracts(), and we say that getContracts() can indeed
   *       return null (and that the other invariants only apply when getContracts != null). For the time being, the latter
   *       seems the safer approach. The first approach will work in all use cases we can imagine, except in the following:
   *       when a user requests all contracts with a given criterium, the  result will be the union of a subset of the
   *       contracts of a few enterprises. What is important is that, for a given enterprise, not all its contracts have to
   *       be in the result. At the time of the request in the entity manager scope, $contracts will not have been initialized.
   *       If we do not access it, it will not be initialized when we serialize the collection of found contracts,
   *       and on deserialization, $contracts will be null, and the bidirectional association is not upheld: contracts point
   *       to an enterprise, and it doesn't point back. If we create an empty set in this case, the user will see objects
   *       for which the type invariants are violated. In the second option, we clear in the contract that getContracts()
   *       might be null (and the user has no control over that), and that if this is so, the rules do not apply.
   *       This is the approach that we will follow.
   * @mudo probably, but contracts not changed yet; the code is change to avoid a NullPointerException in getContracts(),
   *       but not anywhere else
   */
  @OneToMany(mappedBy = "$enterprise", cascade = {}, fetch=FetchType.LAZY)
  @Invars({@Expression("$contracts != null"),
           @Expression("! $contracts.contains(null)"),
           @Expression("for (Contract c : $contracts) { c.enterprise == this }")})
  // hibernate and JPA do not allow final for the set.
  Set<Detail> $contracts = new HashSet<Detail>(); // package accessible for some tests
  // set is null after deserialization if not initialized before serialization

  /*</property>*/

}

