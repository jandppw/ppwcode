/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.bean_VI;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.PropertyHelpers.hasProperty;

import org.ppwcode.exception_N.SemanticException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>PropertyExceptions are exceptions that carry with them information
 *   about the property for which they occurred. They are usually thrown
 *   by a property setter during validation. If the property name is
 *   <code>null</code>, it means that the exception could not be
 *   attributed to a specific property of {@link #getOrigin()}.
 *   <em>The <code>origin</code> should not be <code>null</code></em>,
 *   except when the exception is thrown during construction of an
 *   object, that could not be completed. In that case, the type
 *   should be filled out. Carrying the reference to the object would
 *   expose an incompletely initialized object, as the exception
 *   signals a failure to complete the initialization.</p>
 * <p>Localized messages are sougth
 *   in a <kbd>*.properties</kbd> file for the class of the origin. The
 *   properties files should be in the directory next to the bean class, with
 *   name <kbd><var>{@link #getOrigin()}<code>.getClass().getName()</code></var>
 *   <var>_locale_identification</var>.properties</kbd>. Alternatively,
 *   messages can be added to a properties file that comes with the
 *   exception.</p>
 * <p>The keys for the localized messages have to have the form
 *   <code><var>this.getClass().getName()</var>.
 *         <var>{@link #getPropertyName()}</var>.
 *         <var>{@link #getMessage()}</var></code>.
 *   See {@link #getLocalizedMessageKeys()}.</p>
 *
 * @note Throwables cannot have generic parameters. Otherwise we would have used
 *       this instead of {@link #getOriginType()}.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @idea (jand): Check that property name is truly a property of the origin
 * @mudo extends SemanticException extends InternalException
 * @mudo i18n
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(
  @Expression("message == null || ! message.equals(EMPTY)")
)
public class PropertyException extends SemanticException {

  /*<construction>*/
  //-------------------------------------------------------------------------

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
      @Expression("message == _message"),
      @Expression("cause == _cause")
    }
  )
  public PropertyException(final Object origin,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    super(message, cause);
    assert origin != null;
    assert ! (origin instanceof Class);
    assert (propertyName == null) || hasProperty(origin.getClass(), propertyName);
    assert (message == null) || (! message.equals(""));
    $origin = origin;
    $originType = origin.getClass();
    $propertyName = propertyName;
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
   * @since IV 1.1.0/1.0
   */
  @MethodContract(
    pre  = {
      @Expression("_origin != null"),
      @Expression("_propertyName != null ? hasProperty(_origin.class, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("_inOriginInitialization ? origin == null : origin == _origin"),
      @Expression("originType == _origin.class"),
      @Expression("propertyName == _propertyName"),
      @Expression("message == _message"),
      @Expression("cause == _cause")
    }
  )
  public PropertyException(final Object origin,
                           final boolean inOriginInitialization,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    super(message, cause);
    assert origin != null;
    assert (propertyName == null) || hasProperty(origin.getClass(), propertyName);
    assert (message == null) || (!message.equals(""));
    if (!inOriginInitialization) {
      $origin = origin;
    }
    $originType = origin.getClass();
    $propertyName = propertyName;
  }


  /**
   * @param     originType
   *            The bean that has thrown this exception.
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
      @Expression("_propertyName != null ? hasProperty(_originType, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == null"),
      @Expression("originType == _originType"),
      @Expression("propertyName == _propertyName"),
      @Expression("message == _message"),
      @Expression("cause == _cause")
    }
  )
  public PropertyException(final Class<?> originType,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    super(message, cause);
    assert originType != null;
    assert (propertyName == null) || hasProperty(originType, propertyName);
    assert (message == null) || (!message.equals(""));
    $originType = originType;
    $propertyName = propertyName;
  }

  /*</construction>*/



  /*<property name="origin">*/
  //------------------------------------------------------------------

  /**
   * The bean that has thrown this exception.
   */
  @Basic(
    invars = {
      @Expression("originType != null"),
      @Expression("origin != null ? originType == origin.class")
    }
  )
  public final Object getOrigin() {
    return $origin;
  }

  private Object $origin;

  /*</property>*/



  /*<property name="originType">*/
  //------------------------------------------------------------------

  /**
   * The type of the bean that has thrown this exception.
   */
  @Basic(
    invars = @Expression("originType != null")
  )
  public final Class<?> getOriginType() {
    return $originType;
  }

  private Class<?> $originType;

  /*</property>*/



  /*<property name="propertyName">*/
  //------------------------------------------------------------------

  /**
   * The name of the property for which this
   */
  @Basic(
    invars = @Expression("propertyName != null ? hasProperty(originType, propertyName)")
  )
  public final String getPropertyName() {
    return $propertyName;
  }

  private String $propertyName;

  /*</property>*/



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  /**
   * Compare {@code other} to this: is other of the the exact same
   * type and does other have the exact same properties.
   *
   * This method is an alternative to {@link #equals(Object)}, which
   * we cannot override, because we need to keep reference semantics
   * for exceptions.
   *
   * This method is introduced mainly for use in contracts of methods
   * that throw property exceptions, and in unit tests for those
   * methods.
   *
   * This method must be overridden in every subclass that adds a property
   * to include that property in the comparison.
   *
   * @note methods was formerly called {@code hasSameValues}, and now replaces
   *       {@code hasSameValues}, 2 {@code contains} methods and 2 {@code reportsOn}
   *       methods, which in practice did not fulfill their promise.
   *
   * @since VI
   */
  @MethodContract(
    post = @Expression("result ? (_other != null) && (_other.class = class) && " +
                       "(origin == _other.origin) && (originType == _other.originType) && " +
                       "(propertyName == _other.propertyName) && (message == _other.message) && " +
                       "(cause == _other.cause)")
  )
  public boolean like(PropertyException other) {
    return (other != null) && (other.getClass() == getClass()) &&
           (other.getOrigin() == getOrigin()) &&
           (other.getOriginType() == getOriginType()) &&
           eqn(other.getPropertyName(), getPropertyName()) &&
           eqn(other.getMessage(), getMessage()) &&
           (other.getCause() == getCause());
  }

  protected final boolean eqn(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

}
