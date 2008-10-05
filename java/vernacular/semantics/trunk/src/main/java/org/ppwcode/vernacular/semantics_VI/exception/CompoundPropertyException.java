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
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Compound property exceptions make it possible for an implementation of a setter, a constructor, or a
 *   validation method of a bean to report all that is wrong with arguments or the bean at once.</p>
 * <p>With single exceptions, a method can report 1 exceptional condition. Often, there are more reasons
 *   possible to reject an actual argument. A traditional implementation tests the conditions one by one,
 *   and throws an exception when the first failure of occurs. This can than be reported to the end user,
 *   but once this mistake is corrected, it often results in the next rejection. This is especially annoying
 *   in web applications, that have a long round trip.</p>
 * <p>Compound exceptions make it possible to implement validation in such a way that all validation is done
 *   every time. A validation failure is stored and remembered until all validation is done, and then collected
 *   in a compound exception, which makes it possible to provide full feedback on validity to the end user in
 *   one pass.</p>
 * <p>Compound property exceptions are build during a certain time, and migth eventually be thrown or not be
 *   thrown. An {@link #isEmpty()} compound property exception should never be thrown. Before a compound property
 *   exception is thrown, it should be {@link #isClosed closed}.</p>
 * <p>Compound property exceptions are meant as a flat list. You should not nest compound property exceptions.
 *   This also avoids cyclic element exceptions.</p>
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
public final class CompoundPropertyException extends PropertyException {

  /**
   * The empty String.
   */
  public final static String EMPTY = "";

  /*<construction;>*/
  //------------------------------------------------------------------

  /**
   * Only use this to gather exceptions over many objects.
   *
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   */
  @MethodContract(
    pre  = {
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == null"),
      @Expression("originType == null"),
      @Expression("propertyName == null"),
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  public CompoundPropertyException(final String message, final Throwable cause) {
    super(message, cause);
  }

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

  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     inOriginInitialization
   *            Set to <code>true</code> if this active
   *            property is created during the origin initialization;
   *            if so, an exception will not carry a reference
   *            to the bean, but only to the bean type.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   *
   * @since IV 1.0.1/1.0
   */
  @MethodContract(
    pre  = {
      @Expression("_origin != null"),
      @Expression("_propertyName != null ? hasProperty(_origin.class, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("inOriginInitialization ? origin == null : origin == _origin"),
      @Expression("originType == _origin.class"),
      @Expression("propertyName == _propertyName"),
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  public CompoundPropertyException(final Object origin,
                                   final boolean inOriginInitialization,
                                   final String propertyName,
                                   final String message,
                                   final Throwable cause) {
    super(origin, inOriginInitialization, propertyName, message, cause);
  }

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

  private boolean $closed;

  /*</property>*/



  /*<property name="element exceptions">*/
  //------------------------------------------------------------------

  /**
   * There are no element exceptions.
   */
  @MethodContract(post = @Expression("elementExceptions.empty"))
  public final boolean isEmpty() {
    return $elementExceptions.isEmpty();
  }

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
      @Expression("elementExceptions != null"),
      @Expression("! elementExceptions.containsKey(EMPTY)"),
      @Expression("! elementExceptions.containsValue(null)"),
      @Expression("for (Set s : elementExceptions.values) {! s.isEmpty()}"),
      @Expression("for (Set s : elementExceptions.values) {! s.contains(null)}"),
      @Expression("for (Map.Entry e : elementExceptions.entrySet) {for (PropertyException pe : e.value) {pe.propertyName == e.key}}"),
      @Expression("origin != null ? for (Set e : elementExceptions.values) {for (PropertyException pe : s) {pe.origin == origin}}"),
      @Expression("originType != null ? for (Set e : elementExceptions.values) {for (PropertyException pe : s) {pe.originType == originType}}"),
      @Expression("for (Set e : elementExceptions.values) {for (PropertyException pe : s) {! pe instanceof CompoundPropertyException}}"),
      @Expression("propertyName != null ? elementExceptions.size <= 1"),
      @Expression("propertyName != null ? for (String s) {s != propertyName ? ! elementExceptions.containsKey(s)}")
    },
    init = @Expression("elementExceptions.empty")
  )
  public final Map<String, Set<PropertyException>> getElementExceptions() {
    if ($closed) {
      return $elementExceptions; // is made unmodifiable by now
    }
    else {
      return deepImmutableElementExceptionsCopy();
    }
  }

  @MethodContract(post = @Expression("result.equals(elementExceptions[null])"))
  public Set<PropertyException> getGeneralElementExceptions() {
    Set<PropertyException> result = $elementExceptions.get(null);
    return $closed ? result : immutablePESetCopy(result);
  }

  @MethodContract(post = @Expression("union (Set s : getElementExceptions().values()"))
  public Set<PropertyException> getAllElementExceptions() {
    Set<PropertyException> result = new HashSet<PropertyException>();
    for (Set<PropertyException> pes : $elementExceptions.values()) {
      result.addAll(pes);
    }
    return result;
  }

  /**
   * @param     pExc
   *            The exception to add as element to the compound.
   */
  @MethodContract(
    post = {
      @Expression("elementExceptions.containsKey(_pExc.propertyName)"),
      @Expression("elementExceptions[_pExc.propertyName].contains(_pExc)"),
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
    Set<PropertyException> propertySet = $elementExceptions.get(pExc.getPropertyName());
    if (propertySet == null) {
      propertySet = new HashSet<PropertyException>();
      $elementExceptions.put(pExc.getPropertyName(), propertySet);
    }
    propertySet.add(pExc);
  }

  private Map<String, Set<PropertyException>> deepImmutableElementExceptionsCopy() {
    Map<String, Set<PropertyException>> result = new HashMap<String, Set<PropertyException>>($elementExceptions);
    for (Map.Entry<String, Set<PropertyException>> e : result.entrySet()) {
      Set<PropertyException> pes = e.getValue();
      e.setValue(immutablePESetCopy(pes));
    }
    return Collections.unmodifiableMap(result);
  }

  private Set<PropertyException> immutablePESetCopy(Set<PropertyException> pes) {
    return pes == null ? null : Collections.unmodifiableSet(new HashSet<PropertyException>(pes));
  }

  private void makeImmutable() {
    for (Map.Entry<String, Set<PropertyException>> e : $elementExceptions.entrySet()) {
      e.setValue(Collections.unmodifiableSet(e.getValue()));
    }
    $elementExceptions = Collections.unmodifiableMap($elementExceptions);
  }

  @Invars({
    @Expression("$elementExceptions != null"),
    @Expression("! $elementExceptions.containsKey(EMPTY)"),
    @Expression("! $elementExceptions.containsValue(null)"),
    @Expression("for (Set s : $elementExceptions.values) {! s.isEmpty()}"),
    @Expression("for (Set s : $elementExceptions.values) {! s.contains(null)}"),
    @Expression("for (Set s : $elementExceptions.values) {! s instanceof CompoundPropertyException}"),
    @Expression("for (Map.Entry e : $elementExceptions.entrySet) {for (PropertyException pe : e.value) {pe.propertyName == e.key}}"),
    @Expression("for (Set e : elementExceptions.values) {for (PropertyException pe : s) {pe.origin == origin}}"),
    @Expression("for (Set e : elementExceptions.values) {for (PropertyException pe : s) {pe.originType == originType}}"),
    @Expression("propertyName != null ? for (String s) {s != propertyName ? ! $elementExceptions.containsKey(s)}")
  })
  private Map<String, Set<PropertyException>> $elementExceptions = new HashMap<String, Set<PropertyException>>();

  /*</property>*/



  /*<property name="size">*/
  //------------------------------------------------------------------

  /**
   * The total number of exceptions in this compound.
   */
  @MethodContract(
    post = @Expression("sum(Set s : elementExceptions) {s.size})")
  )
  public int getSize() {
    int acc = 0;
    for (Set<PropertyException> s : $elementExceptions.values()) {
      acc += s.size();
    }
    return acc;
  }

  /*</property>*/



  /*<property name="an element">*/
  //------------------------------------------------------------------

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
      Iterator<Set<PropertyException>> iter1 = getElementExceptions().values().iterator();
      Set<PropertyException> s = iter1.next();
      Iterator<PropertyException> iter2 = s.iterator();
      return iter2.next();
    }
  }

  /*</property>*/



  /*<section name="contains">*/
  //------------------------------------------------------------------

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
    Set<PropertyException> pes = $elementExceptions.get(pe.getPropertyName());
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

//  /**
//   * Checks whether there is a property exception with these parameters
//   * in this compounds element exceptions.
//   */
//  @MethodContract(
//    post = {
//      @Expression("elementExceptions.containsKey(_propertyName)"),
//      @Expression("exists(PropertyException pe : elementExceptions[_propertyName]) {pe.hasProperties(_origin, _propertyName, _message, _cause}")
//    }
//  )
//  EXCEPTION TYPE ADDED
//  public final boolean contains(final Class<?> exceptionType, final Object origin, final String propertyName, final String message, final Throwable cause) {
//    Set<PropertyException> propertySet = $elementExceptions.get(propertyName);
//    if (propertySet != null) {
//      assert ! propertySet.isEmpty();
//      for (PropertyException candidate : propertySet) {
//        if (candidate.hasProperties(origin, propertyName, message, cause)) {
//          return true;
//        }
//      }
//    }
//    return false;
//  }
//
//
//  /**
//   * Checks whether there is a property exception with these parameters
//   * in this compounds element exceptions.
//   *
//   * @since IV
//   */
//  @MethodContract(
//    post = {
//      @Expression("elementExceptions.containsKey(_propertyName) && " +
//                  "exists(PropertyException pe : elementExceptions[_propertyName]) {pe.hasProperties(_originType, _propertyName, _message, _cause}")
//    }
//  )
//  public final boolean contains(final Class<?> originType, final String propertyName, final String message, final Throwable cause) {
//    Set<PropertyException> propertySet = $elementExceptions.get(propertyName);
//    if (propertySet != null) {
//      assert ! propertySet.isEmpty();
//      for (PropertyException candidate : propertySet) {
//        if (candidate.hasProperties(originType, propertyName, message, cause)) {
//          return true;
//        }
//      }
//    }
//    return false;
//  }
//
//  /*</section>*/
//
//
//
//  /*<section name="reports on">*/
//  //------------------------------------------------------------------
//
//  @MethodContract(post = @Expression("contains(_origin, _propertyName, _message, _cause)"))
//  @Override
//  public final boolean reportsOn(final Object origin, final String propertyName, final String message, final Throwable cause) {
//    return contains(origin, propertyName, message, cause);
//  }
//
//  /**
//   * @since IV
//   */
//  @MethodContract(post = @Expression("contains(_originType, _propertyName, _message, _cause)"))
//  @Override
//  public final boolean reportsOn(final Class<?> originType, final String propertyName, final String message, final Throwable cause) {
//    return contains(originType, propertyName, message, cause);
//  }
//
//  /*</section>*/
//


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

//  /**
//   * @return    (other != null)
//   *                && (getOrigin() == other.getOrigin())
//   *                && ((getPropertyName() == null)
//   *                    ? other.getPropertyName() == null
//   *                    : getPropertyName().equals(other.getPropertyName()))
//   *                && ((getCause() == null)
//   *                    ? other.getCause() == null
//   *                    : getCause().equals(other.getCause()))
//   *                && ((getMessage() == null)
//   *                    ? other.getMessage() == null
//   *                    : getMessage().equals(other.getMessage()));
//   */
//  public boolean hasSameValues(final PropertyException other) {
//    return super.hasSameValues(other)
//            && (other instanceof CompoundPropertyException)
//            && sourceContainsAllInTarget(this,
//                                         (CompoundPropertyException)other)
//            && sourceContainsAllInTarget((CompoundPropertyException)other,
//                                         this);
//  }
//
//  private boolean sourceContainsAllInTarget(
//      final CompoundPropertyException source,
//      final CompoundPropertyException target) {
//    Iterator sets = source.$elementExceptions.entrySet().iterator();
//    while (sets.hasNext()) {
//      Map.Entry entry = (Entry)sets.next();
//      Set otherSet = (Set)target.getElementExceptions().get(entry.getKey());
//      if (otherSet == null) {
//        return false;
//      }
//      Iterator exceptions = ((Set)entry.getValue()).iterator();
//      while (exceptions.hasNext()) {
//        PropertyException pe = (PropertyException)exceptions.next();
//        boolean found = false;
//        Iterator otherExceptions = otherSet.iterator();
//        while (otherExceptions.hasNext() && !found) {
//          PropertyException otherPe = (PropertyException)otherExceptions.next();
//          if (pe.hasSameValues(otherPe)) {
//            found = true;
//          }
//        }
//        if (!found) {
//          return false;
//        }
//      }
//      // if we get here, all exceptions in this entry are matched
//    }
//    // if we get here, all exceptions in all entries are matched
//    return true;
//  }

}
