/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v.

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

package org.ppwcode.vernacular.semantics_VI.exception;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.exception_III.ApplicationException;
import org.ppwcode.vernacular.exception_III.CompoundException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Compound property exceptions make it possible for an implementation of a setter, a constructor, or a
 *   validation method of a bean to report all that is wrong with arguments or the bean at once.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("elementsException.size > 1 ? propertyName == null"),
  @Expression("for (PropertyException pExc : allElementExceptions) {! pExc instanceof CompoundPropertyException)")
})
public final class CompoundPropertyException extends PropertyException implements CompoundException<PropertyException> {

  /**
   * The empty String.
   */
  public final static String EMPTY = "";

  /*<construction;>*/
  //------------------------------------------------------------------

//  /**
//   * Only use this to gather exceptions over many objects.
//   *
//   * @param     message
//   *            The message that describes the exceptional circumstance.
//   * @param     cause
//   *            The exception that occurred, causing this exception to be
//   *            thrown, if that is the case.
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_message == null || ! _message.equals(EMPTY)")
//    },
//    post = {
//      @Expression("origin == null"),
//      @Expression("originType == null"),
//      @Expression("propertyName == null"),
//      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
//      @Expression("cause == _cause")
//    }
//  )
//  public CompoundPropertyException(final String message, final Throwable cause) {
//    super(message, cause);
//  }

  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   */
  @MethodContract(
    pre  = {
      @Expression("_origin != null"),
      @Expression("_propertyName != null ? hasProperty(_origin.class, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == _origin"),
      @Expression("originType == _origin.class"),
      @Expression("propertyName == _propertyName"),
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  public CompoundPropertyException(final Object origin, final String propertyName, final String message, final Throwable cause) {
    super(origin, propertyName, message, cause);
  }

//  /**
//   * @param     origin
//   *            The bean that has thrown this exception.
//   * @param     inOriginInitialization
//   *            Set to <code>true</code> if this active
//   *            property is created during the origin initialization;
//   *            if so, an exception will not carry a reference
//   *            to the bean, but only to the bean type.
//   * @param     propertyName
//   *            The name of the property of which the setter has thrown
//   *            this exception because parameter validation failed.
//   * @param     message
//   *            The message that describes the exceptional circumstance.
//   * @param     cause
//   *            The exception that occurred, causing this exception to be
//   *            thrown, if that is the case.
//   *
//   * @since IV 1.0.1/1.0
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_origin != null"),
//      @Expression("_propertyName != null ? hasProperty(_origin.class, _propertyName)"),
//      @Expression("_message == null || ! _message.equals(EMPTY)")
//    },
//    post = {
//      @Expression("inOriginInitialization ? origin == null : origin == _origin"),
//      @Expression("originType == _origin.class"),
//      @Expression("propertyName == _propertyName"),
//      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
//      @Expression("cause == _cause")
//    }
//  )
//  public CompoundPropertyException(final Object origin,
//                                   final boolean inOriginInitialization,
//                                   final String propertyName,
//                                   final String message,
//                                   final Throwable cause) {
//    super(origin, inOriginInitialization, propertyName, message, cause);
//  }

  /**
   * @param     originType
   *            The type of bean that has thrown this exception during initialization.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   *
   * @since IV
   */
  @MethodContract(
    pre  = {
      @Expression("_originType != null"),
      @Expression("_propertyName != null ? hasProperty(_origin.class, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == null"),
      @Expression("originType == _originType"),
      @Expression("propertyName == _propertyName"),
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  public CompoundPropertyException(final Class<?> originType, final String propertyName, final String message, final Throwable cause) {
    super(originType, propertyName, message, cause);
  }

  /*</construction;>*/



  /*<property name="closed">*/
  //------------------------------------------------------------------

  /**
   * To make the exception immutable, it needs to be closed before it is
   * thrown. Element exceptions can only be added to the compound when
   * it is not yet closed.
   */
  @Basic(init = @Expression("false"))
  public boolean isClosed() {
    return $closed;
  }

  @MethodContract(
    post = @Expression("closed"),
    exc = @Throw(
      type = IllegalStateException.class,
      cond = @Expression("'closed")
    )
  )
  public void close() throws IllegalStateException {
    if ($closed) {
      throw new IllegalStateException("can't close twice");
    }
    $closed = true;
    makeImmutable();
  }

  private void makeImmutable() {
    for (Map.Entry<String, Set<PropertyException>> e : $elementExceptionsMap.entrySet()) {
      e.setValue(Collections.unmodifiableSet(e.getValue()));
    }
    $elementExceptionsMap = Collections.unmodifiableMap($elementExceptionsMap);
  }

  private boolean $closed;

  /*</property>*/



  /*<property name="element exceptions">*/
  //------------------------------------------------------------------

  /**
   * A map of sets of exceptions. The key in the map is the name
   * of the property for which the exceptions in the set stored as
   * value for that key occurred. Since the exception is itself a property
   * exception, the property name must match. <code>null</code> is used
   * as the key for general exceptions that can not be attributed
   * to a single exception. If there are multiple entries in the map,
   * the property name of this exception must be <code>null</code>.
   */
  @Basic(
    invars = {
      @Expression("elementExceptionsMap != null"),
      @Expression("! elementExceptionsMap.containsKey(EMPTY)"),
      @Expression("! elementExceptionsMap.containsValue(null)"),
      @Expression("for (Set s : elementExceptionsMap.values) {! s.isEmpty()}"),
      @Expression("for (Set s : elementExceptionsMap.values) {! s.contains(null)}"),
      @Expression("for (Map.Entry e : elementExceptionsMap.entrySet) {for (PropertyException pe : e.value) {pe.propertyName == e.key}}"),
      @Expression("origin != null ? for (Set e : elementExceptionsMap.values) {for (PropertyException pe : s) {pe.origin == origin}}"),
      @Expression("originType != null ? for (Set e : elementExceptionsMap.values) {for (PropertyException pe : s) {pe.originType == originType}}"),
      @Expression("for (Set e : elementExceptionsMap.values) {for (PropertyException pe : s) {! pe instanceof CompoundPropertyException}}"),
      @Expression("propertyName != null ? elementExceptionsMap.size <= 1"),
      @Expression("propertyName != null ? for (String s) {s != propertyName ? ! elementExceptionsMap.containsKey(s)}")
    },
    init = @Expression("elementExceptionsMap.empty")
  )
  public final Map<String, Set<PropertyException>> getElementExceptionsMap() {
    if ($closed) {
      return $elementExceptionsMap; // is made unmodifiable by now
    }
    else {
      return deepImmutableElementExceptionsCopy();
    }
  }

  private Map<String, Set<PropertyException>> deepImmutableElementExceptionsCopy() {
    Map<String, Set<PropertyException>> result = new HashMap<String, Set<PropertyException>>($elementExceptionsMap);
    for (Map.Entry<String, Set<PropertyException>> e : result.entrySet()) {
      Set<PropertyException> pes = e.getValue();
      e.setValue(immutablePESetCopy(pes));
    }
    return Collections.unmodifiableMap(result);
  }

  private Set<PropertyException> immutablePESetCopy(Set<PropertyException> pes) {
    return pes == null ? null : Collections.unmodifiableSet(new HashSet<PropertyException>(pes));
  }

  @MethodContract(post = @Expression("result.equals(elementExceptions[null])"))
  public Set<PropertyException> getGeneralElementExceptions() {
    Set<PropertyException> result = $elementExceptionsMap.get(null);
    return $closed ? result : immutablePESetCopy(result);
  }

  @MethodContract(post = @Expression("union (Set s : elementExceptionsMap.values()"))
  public Set<PropertyException> getElementExceptions() {
    Set<PropertyException> result = new HashSet<PropertyException>();
    for (Set<PropertyException> pes : $elementExceptionsMap.values()) {
      result.addAll(pes);
    }
    return result;
  }

  /**
   * Returns an element exception of this instance. Especially
   * intresting if <code>size == 1</code>, of course.
   * Returns <code>null</code> if <code>size == 0</code>.
   */
  @MethodContract(post = @Expression("result != null ? contains(result"))
  public PropertyException getAnElement() {
    if (isEmpty()) {
      return null;
    }
    else {
      Iterator<Set<PropertyException>> iter1 = $elementExceptionsMap.values().iterator();
      Set<PropertyException> s = iter1.next();
      Iterator<PropertyException> iter2 = s.iterator();
      return iter2.next();
    }
  }

  /**
   * Checks whether this exact property exception is in the compound
   * with reference semantics.
   */
  @MethodContract(
    post = @Expression("_pe != null && elementExceptions[pe.propertyName] != null && " +
                   "exists(PropertyException pe : elementExceptions[pe.propertyName]) {pe.like(_pe)}")
  )
  public final boolean contains(PropertyException pe) {
    if (pe == null) {
      return false;
    }
    Set<PropertyException> pes = $elementExceptionsMap.get(pe.getPropertyName());
    if (pes == null) {
      return false;
    }
    for (PropertyException candidate : pes) {
      if (candidate.like(pe)) {
        return true;
      }
    }
    return false;
  }

  /**
   * There are no element exceptions.
   */
  @MethodContract(post = @Expression("elementExceptionsMap.empty"))
  public final boolean isEmpty() {
    return $elementExceptionsMap.isEmpty();
  }

  /**
   * The total number of exceptions in this compound.
   */
  @MethodContract(
    post = @Expression("sum(Set s : elementExceptionsMap) {s.size})")
  )
  public int getSize() {
    int acc = 0;
    for (Set<PropertyException> s : $elementExceptionsMap.values()) {
      acc += s.size();
    }
    return acc;
  }

  /**
   * @param     pExc
   *            The exception to add as element to the compound.
   */
  @MethodContract(
    post = {
      @Expression("elementExceptionsMap.containsKey(_pExc.propertyName)"),
      @Expression("elementExceptionsMap[_pExc.propertyName].contains(_pExc)"),
      @Expression(value = "! 'closed",
                  description = "since we cannot make true something in the old state, an exception " +
                                "has to be thrown when the exception is closed in the old state")
    },
    exc = {
      @Throw(type = IllegalStateException.class, cond = @Expression("'closed")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("_pExc instanceof CompoundPropertyException")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("_pExc == null")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("propertyName != null && _pExc.propertyName != propertyName")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("origin != null ? _pExc.origin != origin")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("originType != null ? _pExc.originType != originType"))
    }
  )
  public void addElementException(final PropertyException pExc)
      throws IllegalStateException, IllegalArgumentException {
    if (isClosed()) {
      throw new IllegalStateException("cannot add exceptions to compound when closed");
    }
    if (pExc instanceof CompoundPropertyException) {
      throw new IllegalArgumentException("Cannot add a compound property exception. This should be a flat list.");
    }
    if (pExc == null) {
      throw new IllegalArgumentException("Cannot add null exception.");
    }
    if ((getPropertyName() != null) && (!getPropertyName().equals(pExc.getPropertyName()))) {
      throw new IllegalArgumentException("only exceptions for property " + getPropertyName() + " are allowed");
    }
    if ((getOrigin() != null) && (pExc.getOrigin() != getOrigin())) {
      throw new IllegalArgumentException("only exceptions for origin " + getOrigin() + " are allowed");
    }
    if ((getOriginType() != null) && (getOrigin() == null) && (pExc.getOriginType() != getOriginType())) {
      throw new IllegalArgumentException("only exceptions for origin type " + getOriginType() + " are allowed");
    }
    Set<PropertyException> propertySet = $elementExceptionsMap.get(pExc.getPropertyName());
    if (propertySet == null) {
      propertySet = new HashSet<PropertyException>();
      $elementExceptionsMap.put(pExc.getPropertyName(), propertySet);
    }
    propertySet.add(pExc);
  }

  @Invars({
    @Expression("$elementExceptionsMap != null"),
    @Expression("! $elementExceptionsMap.containsKey(EMPTY)"),
    @Expression("! $elementExceptionsMap.containsValue(null)"),
    @Expression("for (Set s : $elementExceptionsMap.values) {! s.isEmpty()}"),
    @Expression("for (Set s : $elementExceptionsMap.values) {! s.contains(null)}"),
    @Expression("for (Set s : $elementExceptionsMap.values) {! s instanceof CompoundPropertyException}"),
    @Expression("for (Map.Entry e : $elementExceptionsMap.entrySet) {for (PropertyException pe : e.value) {pe.propertyName == e.key}}"),
    @Expression("for (Set e : $elementExceptionsMap.values) {for (PropertyException pe : s) {pe.origin == origin}}"),
    @Expression("for (Set e : $elementExceptionsMap.values) {for (PropertyException pe : s) {pe.originType == originType}}"),
    @Expression("propertyName != null ? for (String s) {s != propertyName ? ! $elementExceptionsMap.containsKey(s)}")
  })
  private Map<String, Set<PropertyException>> $elementExceptionsMap = new HashMap<String, Set<PropertyException>>();

  /*</property>*/



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  @Override
  @MethodContract(
    post = @Expression("result ? " +
                         "for (PropertyException otherPe : _other.elementExceptions) {contains(otherPe)} && " +
                         "for (PropertyException Pe : elementExceptions) {_other.contains(Pe)}")
  )
  public boolean like(ApplicationException other) {
    if (! super.like(other)) {
      return false;
    }
    CompoundPropertyException otherCpe = (CompoundPropertyException)other;
    if (getSize() != otherCpe.getSize()) {
      return false;
    }
    for (Set<PropertyException> ourPeSet : $elementExceptionsMap.values()) {
      for (PropertyException ourPe : ourPeSet) {
        if (! otherCpe.contains(ourPe)) {
          return false;
        }
      }
    }
    return true;
  }

  /*</section>*/



  /**
   * This method throws this exception if it is not empty.
   * If this is not empty, this is closed. If the number of element
   * exceptions is larger than 1, this is thrown. If there is exactly
   * 1 element exception, that is thrown instead.
   */
  @MethodContract(
    post = {
      @Expression(
        value = "'empty",
        description = "since we cannot change the old value of empty, we are " +
                      "forced to throw an exception if we are not empty"
      ),
      @Expression("closed")
    },
    exc = {
      @Throw(
        type = CompoundPropertyException.class,
        cond = {
          @Expression("size > 1"),
          @Expression("thrown == this"),
          @Expression("closed")
        }
      ),
      @Throw(
         type = PropertyException.class,
         cond = {
           @Expression("size == 1"),
           @Expression("thrown == anElement"),
           @Expression("closed")
         }
       )
    }
  )
  public final void throwIfNotEmpty() throws PropertyException {
    if (! isClosed()) {
      close();
    }
    if (!isEmpty()) {
      if (getSize() > 1) {
        throw this;
      }
      else {
        throw getAnElement();
      }
    }
  }

}
