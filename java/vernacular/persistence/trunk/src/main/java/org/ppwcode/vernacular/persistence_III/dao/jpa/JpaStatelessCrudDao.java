/*<license>
Copyright 2005 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.vernacular.persistence_III.dao.jpa;


import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.dependency;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.normalize;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.wildExceptions;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.vernacular.exception_II.InternalException;
import org.ppwcode.vernacular.exception_II.SemanticException;
import org.ppwcode.vernacular.persistence_III.IdNotFoundException;
import org.ppwcode.vernacular.persistence_III.PersistentBean;
import org.ppwcode.vernacular.persistence_III.dao.StatelessCrudTransactionDao;
import org.ppwcode.vernacular.semantics_VI.bean.RousseauBean;
import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;

/**
 * @mudo work on transaction annotations
 * @mudo unit tests
 * @mudo catch exceptions and log only external exceptions and programming errors
 */
@Stateless
//@WebService MUDO find maven dependency for javax.jws
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaStatelessCrudDao extends AbstractJpaDao implements StatelessCrudTransactionDao {

  private final static Log _LOG = LogFactory.getLog(JpaStatelessCrudDao.class);

  /*<property name="entity manager">
  -------------------------------------------------------------------------*/

  @Basic
  public final EJBContext getEjbContext() {
    return $ejbContext;
  }

  @MethodContract(
    post = @Expression("ejbContext == _ejbContext")
  )
  public final void setEjbContext(EJBContext manager) {
    $ejbContext = manager;
  }

  @Resource
  private EJBContext $ejbContext;

  /*</property>*/


  @Override
  @MethodContract(post = @Expression("result ? super.isOperational() && (ejbContext != null)"))
  public boolean isOperational() {
    return super.isOperational() && ($ejbContext != null);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  /* only 1 database access, thus SUPPORTS would suffice; yet, to avoid dirty reads, as per JPA recomendation: Required */
  public <_PersistentBean_ extends PersistentBean<?>> Set<_PersistentBean_>
  retrieveAllPersistentBeans(Class<_PersistentBean_> persistentBeanType, boolean retrieveSubClasses) {
    _LOG.debug("Retrieving all records of type \"" + persistentBeanType + "\" ...");
    assert preArgumentNotNull(persistentBeanType, "persistentBeanType");
    assert dependency(getEntityManager(), "entityManager");
    try {
      Query query = null;
      if (retrieveSubClasses) {
        query = getEntityManager().createQuery("FROM " + persistentBeanType.getName());
      }
      else {
        query = getEntityManager().createQuery("FROM " + persistentBeanType.getName() +
                                           " as persistentObject WHERE persistentObject.class = " +
                                           persistentBeanType.getName());
        // MUDO this approach is untested with JPA
      }
      @SuppressWarnings("unchecked")
      List<_PersistentBean_> result = query.getResultList();
      assert result != null;
      Set<_PersistentBean_> setResult = new HashSet<_PersistentBean_>(result);
      _LOG.debug("Retrieval succeeded (" + setResult.size() + " objects retrieved)");
      return setResult;
    }
    catch (IllegalArgumentException iaExc) {
      unexpectedException(iaExc, "query string problem");
    }
    catch (IllegalStateException isExc) {
      unexpectedException(isExc);
    }
    return null; // keep compiler happy
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  /* only 1 database access, thus SUPPORTS would suffice; yet, to avoid dirty reads, as per JPA recomendation: Required */
  public <_Id_ extends Serializable, _PersistentBean_ extends PersistentBean<_Id_>>
  _PersistentBean_ retrievePersistentBean(Class<_PersistentBean_> persistentBeanType, _Id_ id)
      throws IdNotFoundException {
    _LOG.debug("Retrieving record with id = " + id + " of type " + persistentBeanType + " ...");
    assert preArgumentNotNull(persistentBeanType, "persistentBeanType");
    assert preArgumentNotNull(id, "id");
    assert dependency(getEntityManager(), "entityManager");
    _PersistentBean_ result = null;
    try {
      result = getEntityManager().find(persistentBeanType, id);
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc, "" + persistentBeanType + "is not a type the JPA entity manager recognizes");
    }
    if (result == null) {
      _LOG.debug("Record not found");
      throw new IdNotFoundException(persistentBeanType, id);
    }
    else {
      assert id.equals(result.getPersistenceId());
      assert result.getClass() == persistentBeanType;
      if (_LOG.isDebugEnabled()) {
        _LOG.debug("Retrieval succeeded (" + result + ")");
      }
      return result;
    }
  }


  /**
   * The direct associated objects of type {@code associatedType} starting from {@code bean}.
   * These are the beans that are properties of {@code bean}, directly (to-one) or via
   * a collection (to-many).
   */
  @MethodContract(
    pre  = {
      @Expression("_bean != null"),
      @Expression("_associatedType != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("for (PropertyDescriptor pd : getPropertyDescriptors(_bean)) {" +
                    "associatedType.isAssignableFrom(pd.propertyType) && propertyValue(_bean, pd.name) != null ? " +
                      "result.contains(propertyValue(_bean, pd.name))" +
                  "}"), // MUDO add toMany
      @Expression("for (_T_ t : result) {" +
                    "exists (PropertyDescriptor pd : getPropertyDescriptors(_bean)) {" +
                      "t == propertyValue(_bean, pd.name)" +
                    "}" +
                  "}") // MUDO add toMany
    }
  )
  // MUDO replace with AssociationHelpers version in reflection
  private static <_T_> Set<_T_> directAssociatedBeans(_T_ bean, Class<? extends _T_> associatedType) {
    assert preArgumentNotNull(bean, "bean");
    assert preArgumentNotNull(associatedType, "associatedType");
    Set<_T_> result = new HashSet<_T_>();
    PropertyDescriptor[] pds = getPropertyDescriptors(bean);
    for (int i = 0; i < pds.length; i++) {
      PropertyDescriptor pd = pds[i];
      if (associatedType.isAssignableFrom(pd.getPropertyType())) {
        _T_ candidate = propertyValue(bean, pd.getName(), associatedType);
        if (candidate != null) {
          result.add(candidate);
        }
      }
      else if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
        // get the elements, and if they are RousseauBeans, add them
        Set<? extends Object> many = null;
        try {
          @SuppressWarnings("unchecked")
          Set<? extends Object> anyMany = (Set<? extends Object>)getProperty(bean, pd.getName());
          many = anyMany;
        }
        catch (InvocationTargetException exc) {
          /* in a special case, to avoid problems with deserialized objects of a non-initialized
             JPA toMany, we deal with a NullPointerException as if it is a null property */
          if (exc.getCause() instanceof NullPointerException) {
            assert many == null;
            // NOP
          }
          else {
            unexpectedException(exc);
          }
        }
        catch (IllegalAccessException exc) {
          unexpectedException(exc);
        }
        catch (NoSuchMethodException exc) {
          unexpectedException(exc);
        }
        if (many != null) {
          for (Object object : many) {
            if (object != null && associatedType.isInstance(object)) {
              result.add(associatedType.cast(object));
            }
          }
        }
      }
    }
    return result;
  }

  /**
   * All objects of type {@code associatedType} that are reachable from {@code bean}.
   * These are the beans that are simple or multiple properties of {@code bean}. This is applied
   * recursively. {@code bean} itself is also part of the set.
   */
  @MethodContract(
    pre  = {
      @Expression("_bean != null"),
      @Expression("_associatedType != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("{_bean} U directAssociatedSemanticBeans(_bean) U " +
                   "union (_T_ t : directAssociatedSemanticBeans(_bean)) {associatedSemanticBeans(t)}")
    }
  )
  // MUDO replace with AssociationHelpers version in reflection
  private static <_T_> Set<_T_> associatedBeans(_T_ bean, Class<? extends _T_> associatedType) {
    assert preArgumentNotNull(bean, "bean");
    assert preArgumentNotNull(associatedType, "associatedType");
    LinkedList<_T_> agenda = new LinkedList<_T_>();
    agenda.add(bean);
    int i = 0;
    while (i < agenda.size()) {
      _T_ current = agenda.get(i);
      for (_T_ t : directAssociatedBeans(current, associatedType)) {
        if (! agenda.contains(t)) {
          agenda.add(t);
        }
      }
      i++;
    }
    return new HashSet<_T_>(agenda);
  }


  /**
   * The upstream {@link RousseauBean RousseauBeans} starting from {@code rb}.
   * These are the beans that are simple properties of {@code rb}. Upstream means
   * in most cases (this is all that is implemented at this time) the beans
   * reachable via a to-one association.
   *
   * @mudo method is probably never used; remove before release
   */
  @MethodContract(
    pre  = @Expression("_rb != null"),
    post = {
      @Expression("result != null"),
      @Expression("for (PropertyDescriptor pd : getPropertyDescriptors(_rb)) {" +
                    "RousseauBean.class.isAssignableFrom(pd.propertyType) && propertyValue(_rb, pd.name) != null ? " +
                      "result.contains(propertyValue(_rb, pd.name))" +
                  "}"),
      @Expression("for (RousseaBean rbr : result) {" +
                    "exists (PropertyDescriptor pd : getPropertyDescriptors(_rb)) {" +
                      "rbr == propertyValue(_rb, pd.name)" +
                    "}" +
                  "}")
    }
  )
  // MUDO replace with AssociationHelpers version in reflection
  private static <_T_> Set<_T_> directUpstreamBeans(_T_ bean, Class<? extends _T_> associatedType) {
    assert preArgumentNotNull(bean, "rb");
    Set<_T_> result = new HashSet<_T_>();
    PropertyDescriptor[] pds = getPropertyDescriptors(bean);
    for (int i = 0; i < pds.length; i++) {
      PropertyDescriptor pd = pds[i];
      if (associatedType.isAssignableFrom(pd.getPropertyType())) {
        _T_ upstreamCandidate = propertyValue(bean, pd.getName());
        if (upstreamCandidate != null) {
          result.add(upstreamCandidate);
        }
      }
    }
    return result;
  }

  /**
   * All upstream {@link RousseauBean RousseauBeans} starting from {@code rb}.
   * These are the beans that are simple properties of {@code rb}. Upstream means
   * in most cases (this is all that is implemented at this time) the beans
   * reachable via a to-one association. This is applied recursively.
   * {@code rb} itself is also part of the set.
   *
   * @mudo method is probably never used; remove before release
   */
  @MethodContract(
    pre  = @Expression("_rb != null"),
    post = {
      @Expression("result != null"),
      @Expression("{_rb} U directUpstreamRousseauBeans(_rb) U " +
                   "union (RousseauBean rbr : directUpstreamRousseauBeans(_rb)) {upstreamRousseauBeans(rbr)}")
    }
  )
  // MUDO replace with AssociationHelpers version in reflection
  private static <_T_> Set<_T_> upstreamBeans(_T_ bean, Class<? extends _T_> associatedType) {
    assert preArgumentNotNull(bean, "rb");
    LinkedList<_T_> agenda = new LinkedList<_T_>();
    agenda.add(bean);
    int i = 0;
    while (i < agenda.size()) {
      _T_ current = agenda.get(i);
      for (_T_ upstreamBean : directUpstreamBeans(current, associatedType)) {
        if (! agenda.contains(upstreamBean)) {
          agenda.add(upstreamBean);
        }
      }
      i++;
    }
    return new HashSet<_T_>(agenda);
  }

  // MUDO HANDLE SQL EXCEPTIONS

  /**
   * Create or update. Create if ID is null, update if not.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public <_Id_ extends Serializable, _PB_ extends PersistentBean<_Id_>> _PB_ mergePersistentBean(_PB_ pb) throws InternalException {
    _LOG.debug("Merging persistent bean: " + pb);
    if (_LOG.isDebugEnabled()) {
      _LOG.debug("pb.id = " + pb.getPersistenceId() +
                 ((pb.getPersistenceId() == null) ? " == null: persistent bean will be created" :
                                         " != null: persistent bean will be update"));
    }
    assert preArgumentNotNull(pb, "pb");
    assert dependency(getEntityManager(), "enitytManager");
    assert pre(! getEntityManager().contains(pb)); // MUDO contract: this pre is not in the contract!!!
    _PB_ newPb = null;
    /* first we gather all the beans we received as parameter; most often, pb will be detached.
     * if not however, we have a problem: gathering all related beans will load the entire
     * database. see assert pre
     */
    Set<PersistentBean<?>> associatedBeans = associatedBeans(pb, PersistentBean.GENERIC_SUPER_TYPE);
    /* next, we normalize; we do not want to normalize stuff that did not come in as parameter, so we do
     * this before we merge
     */
    normalize(associatedBeans);
    /* now we merge; this isn't committed yet, but we want access to lazy loaded sets when we calculate
     * wild exceptions
     */
    try {
      newPb = getEntityManager().merge(pb); // not committed yet, throws load of exceptions
      /* now all beans in the graph are new; we need to use the variants that are new;
       * we only validate upstream bean; find them
       * MUDO SINCE WE DO NOT VALIDATE DOWNSTREAM, THERE IS CURRENTLY A LOOPHOLE TO GET WILD DATA IN THE DATABASE
       * if you submit a pb with associated beans over a many relationship, in which there is a new bean or a changed
       * bean, and Cascade is on for persist and / or merge, that data will reach the database unchecked;
       * We do not dare to check that now, since, if we do it for all to-many relationships, they will be loaded
       * lazily, and we will load the entire database if we are unlucky
       */
      Set<PersistentBean<?>> newAssociatedBeans = upstreamBeans(pb, PersistentBean.GENERIC_SUPER_TYPE);
      /* we lock all the upstream beans before we do wild exception checks; otherwise, it is possible that somebody else
       * is in the mean time adding or changing another pb to our upstream bean; since we depend on optimistic locking,
       * we need to force that our commit invalidates the other commit; because it happens at the same time, the other
       * change is concurrently doing civilized checks on the parent, based on the old collection of children (without
       * our new child or with the old data of the child we are updating). Some civilized checks might will validate
       * conditions of the child selection as a whole, e.g., that a period in children does not overlap. If we check
       * with the old collection and our new data, and the other transaction also checks with the old collection and
       * its new data, the combination of our new data and the other transactions new data is not validated.
       * We tackle this by inforcing that our commit also updates the version number of the parent, and the other
       * transaction has to apply versioning on our parent to. If we are first, the other transaction is then rolled-back,
       * because we are first. Thus, there is no possibility to add wild data to the database for this case.
       * Getting a write lock is the easiest way to increment the version number for a potentially unchanged object.
       * There is no need to lock pb itself.
       */
      for (PersistentBean<?> upstreamBean : newAssociatedBeans) {
        getEntityManager().lock(upstreamBean, LockModeType.WRITE); // throws load of exceptions
      }
      assert newPb != null;
      /* now, check civility on all new associated beans */
      CompoundPropertyException cpe = wildExceptions(newAssociatedBeans);
      /* if there are exceptions, stop and throw them (but log this first) */
      if (! cpe.isEmpty()) {
        if (_LOG.isDebugEnabled()) {
          _LOG.debug("persistent beans offered for merge are not civilized; rollback", cpe);
        }
        getEjbContext().setRollbackOnly();
        cpe.throwIfNotEmpty();
      }
      else {
        _LOG.debug("merge succeeded; attempting commit and returning new persistent bean: " + newPb);
      }
    }
    // MUDO versioning problem??
    catch (IllegalStateException exc) {
      unexpectedException(exc, "entity manager in illegal state for merge");
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc, "persistent bean not accepted as merge argument by entity manager");
    }
    catch (TransactionRequiredException exc) {
      unexpectedException(exc, "transaction is required!");
    }
    return newPb;
    // now we will get a commit; the exceptions raised here need to be handled still
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public <_Id_ extends Serializable, _PB_ extends PersistentBean<_Id_>> _PB_ deletePersistentBean(_PB_ pb) throws SemanticException {
    _LOG.debug("Deleting persistent bean: " + pb);
    assert preArgumentNotNull(pb, "pb");
    assert dependency(getEntityManager(), "enitytManager");
    try {
      getEntityManager().remove(pb);
      // checking civility here makes no sense
    }
    // MUDO versioning problem??
    catch (IllegalStateException exc) {
      unexpectedException(exc, "entity manager in illegal state for merge");
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc, "persistent bean not accepted as merge argument by entity manager");
    }
    catch (TransactionRequiredException exc) {
      _LOG.error("transaction is required!");
      unexpectedException(exc);
    }
    pb.setPersistenceId(null);
    _LOG.debug("delete succeeded; attempting commit and returning new persistent bean: " + pb);
    return pb;
  }

}
