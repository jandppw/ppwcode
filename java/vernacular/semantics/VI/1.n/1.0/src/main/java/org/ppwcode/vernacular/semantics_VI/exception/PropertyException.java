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
import static org.ppwcode.util.reflect_I.PropertyHelpers.hasProperty;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.exception_III.ApplicationException;
import org.ppwcode.vernacular.exception_III.SemanticException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>{@code PropertyExceptions} are exceptions that carry with them information about the property for which they
 *   occurred. They are usually thrown by a property setter during validation. If the property name is {@code null},
 *   it means that the exception could not be attributed to a specific property of {@link #getOrigin()}.
 *   <em>The <code>origin</code> should not be <code>null</code></em>, except when the exception is thrown during
 *   construction of an object, that could not be completed. In that case, the type should be filled out. Carrying
 *   the reference to the object would expose an incompletely initialized object, as the exception signals a failure
 *   to complete the initialization. <em>The <code>originType</code> should not be <code>null</code></em>.
 *   A {@code PropertyException} reports on an issue with <em>one</em> object. If there is a need to communicate
 *   an issue over more than one issue, use a {@link SemanticException}. Specific property exception subtypes will
 *   make these advises binding in most cases.</p>
 *
 * MUDO move discussion below to l10n package
 *
 * <p>Localized messages are sought in a <kbd>*.properties</kbd> file for {@link #getOriginType() originType} and its
 *   super types. The properties files should be in the directory next to the bean class, with name
 *   <kbd><var>{@link #getOrigin()}<code>.getClass().getName()</code></var>_<var>_locale_identification</var>.properties</kbd>.
 *   Alternatively, messages can be added to a properties file that comes with the exception, with name
 *   <kbd><var>}<code>getClass().getName()</code></var>_<var>_locale_identification</var>.properties</kbd>.</p>
 * <p>The keys for the localized messages in the {@link #getOriginType() originType} properties files have to have the
 *   form
 *   <code><var>this.getClass().getCanonicalName()</var>.<var>{@link #getPropertyName()}</var>.<var>{@link #getMessage()}</var></code>.
 *   If such a key does not exist, we look for a key
 *   <code><var>this.getClass().getCanonicalName()</var>.<var>{@link #getPropertyName()}</var></code>, and next for
 *   <code><var>this.getClass().getCanonicalName()</var>.<var>{@link #getMessage()}</var></code> ,and then for
 *   <code><var>this.getClass().getCanonicalName()</var>. If neither of these keys are found, we look in the properties file
 *   that comes with the exception for an entry with the key
 *   <var>{@link #getMessage()}</var>, and if that is not found, for an entry with key
 *   {@link ApplicationException#DEFAULT_MESSAGE_KEY}.</p>
 * <p>The values in the properties files can use data from exception as parameters in the message. Available are:</p>
 * MUDO This feature is not implemented yet.
 * <ul>
 *   <li>the localized name of the origin type, as <code>{originType}</code>, or <code>{originTypes}</code> for the plural</li>
 *   <li>the localized name of the property as <code>{propertyName}</code></li>
 *   <li>the origin object, as <code>{origin}</code>; this can be used with concatenation to get the values of certain
 *     properties, like <code>{origin.<var>myProperty.myProperty</var>}</code>; if {@code null} is encountered along this chain,
 *     the entire expression evaluates to the string {@code "null"}</li>
 *   <li>any property a subtype of PropertyException might introduce, as <code>{<var>propertyName</var>}</code>.</li>
 * </ul>
 *
 *
 * @note Throwables cannot have generic parameters. Otherwise we would have used this instead of {@link #getOriginType()}.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class PropertyException extends SemanticException {

  /*<construction>*/
  //-------------------------------------------------------------------------

//  /**
//   * Specialized constructor for Compound subtype, without a property name, type
//   * or origin.
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
//  protected PropertyException(final String message, final Throwable cause) {
//    super(message, cause);
//    assert (message == null) || (! message.equals(""));
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
  public PropertyException(Object origin, String propertyName, String message, Throwable cause) {
    super(message, cause);
    assert origin != null;
    assert ! (origin instanceof Class);
    assert (propertyName == null) || hasProperty(origin.getClass(), propertyName);
    $origin = origin;
    $originType = origin.getClass();
    $propertyName = propertyName;
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
//   * @since IV 1.1.0/1.0
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_origin != null"),
//      @Expression("_propertyName != null ? hasProperty(_origin.class, _propertyName)"),
//      @Expression("_message == null || ! _message.equals(EMPTY)")
//    },
//    post = {
//      @Expression("_inOriginInitialization ? origin == null : origin == _origin"),
//      @Expression("originType == _origin.class"),
//      @Expression("propertyName == _propertyName"),
//      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
//      @Expression("cause == _cause")
//    }
//  )
//  public PropertyException(final Object origin,
//                           final boolean inOriginInitialization,
//                           final String propertyName,
//                           final String message,
//                           final Throwable cause) {
//    super(message, cause);
//    assert origin != null;
//    assert (propertyName == null) || hasProperty(origin.getClass(), propertyName);
//    assert (message == null) || (!message.equals(""));
//    if (!inOriginInitialization) {
//      $origin = origin;
//    }
//    $originType = origin.getClass();
//    $propertyName = propertyName;
//  }


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
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  public PropertyException(Class<?> originType, final String propertyName, String message, Throwable cause) {
    super(message, cause);
    assert originType != null;
    assert (propertyName == null) || hasProperty(originType, propertyName);
    assert (message == null) || (!message.equals(""));
    $originType = originType;
    $propertyName = propertyName;
  }

  /*</construction>*/



  /*<property name="originType">*/
  //------------------------------------------------------------------

  /**
   * The type of the bean that has thrown this exception.
   */
  @Basic(invars = @Expression("originType != null"))
  public final Class<?> getOriginType() {
    return $originType;
  }

  private Class<?> $originType;

  /*</property>*/



  /*<property name="origin">*/
  //------------------------------------------------------------------

  /**
   * The bean that has thrown this exception.
   */
  @Basic(
    invars = {
      @Expression("origin != null ? originType == origin.class")
    }
  )
  public final Object getOrigin() {
    return $origin;
  }

  private Object $origin;

  /*</property>*/



  /*<property name="propertyName">*/
  //------------------------------------------------------------------

  /**
   * The name of the property for which this
   */
  @Basic(invars = @Expression("propertyName != null ? hasProperty(originType, propertyName)"))
  public final String getPropertyName() {
    return $propertyName;
  }

  private String $propertyName;

  /*</property>*/



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  @Override
  @MethodContract(
    post = @Expression("result ? (origin == _other.origin) && " +
                                "(originType == _other.originType) && (propertyName == _other.propertyName)")
  )
  public boolean like(ApplicationException other) {
    return super.like(other) &&
           (((PropertyException)other).getOrigin() == getOrigin()) &&
           (((PropertyException)other).getOriginType() == getOriginType()) &&
           eqn(((PropertyException)other).getPropertyName(), getPropertyName());
  }

}
