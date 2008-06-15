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
import static org.ppwcode.util.reflect_I.Properties.hasProperty;
import static org.toryt.annotations_I.Scope.PROTECTED;

import org.ppwcode.exception_N.SemanticException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Invars;


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
      @Expression("^origin != null"),
      @Expression("^propertyName != null ? hasProperty(^origin.class, ^propertyName)"),
      @Expression("^message == null || ! ^message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == ^origin"),
      @Expression("originType == ^origin.class"),
      @Expression("propertyName == ^propertyName"),
      @Expression("message == ^message"),
      @Expression("cause == ^cause")
    }
  )
  public PropertyException(final Object origin,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    super(message, cause);
    assert origin != null;
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
      @Expression("^origin != null"),
      @Expression("^propertyName != null ? hasProperty(^origin.class, ^propertyName)"),
      @Expression("^message == null || ! ^message.equals(EMPTY)")
    },
    post = {
      @Expression("inOriginInitialization ? origin == null : origin == ^origin"),
      @Expression("originType == ^origin.class"),
      @Expression("propertyName == ^propertyName"),
      @Expression("message == ^message"),
      @Expression("cause == ^cause")
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
      @Expression("^originType != null"),
      @Expression("^propertyName != null ? hasProperty(^origin.class, ^propertyName)"),
      @Expression("^message == null || ! ^message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == null"),
      @Expression("originType == ^originType"),
      @Expression("propertyName == ^propertyName"),
      @Expression("message == ^message"),
      @Expression("cause == ^cause")
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



//  /**
//   * @param     other
//   *            The object to compare the values of this with.
//   */
//  @MethodContract(
//    post = {
//      @Expression("(other != null) && " +
//                  "(hasProperties(other.originType, other.propertyName, other.message, other.cause) && " +
//                  "(origin == other.origin)")
//    }
//  )
//  public boolean hasSameValues(final PropertyException other) {
//    return (other != null)
//             && hasProperties(other.getOriginType(),
//                              other.getPropertyName(),
//                              other.getMessage(),
//                              other.getCause())
//             && getOrigin() == other.getOrigin();
//  }



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  /**
   * @param     origin
   *            The origin to compare the one of this Exception with.
   * @param     propertyName
   *            The  propertyName to compare the one of this Exception with.
   * @param     message
   *            The message to compare the one of this Exception with..
   * @param     cause
   *            The cause to compare the one of this Exception with.
   */
  @MethodContract(
    post = @Expression("(origin == ^origin) && (propertyName == ^propertyName) && (message == ^message) && (cause == ^cause)")
  )
  public final boolean hasProperties(final Object origin,
                                     final String propertyName,
                                     final String message,
                                     final Throwable cause) {
    return (getOrigin() == origin)
              && ((getPropertyName() == null)
                    ? propertyName == null
                    : getPropertyName().equals(propertyName))
              && ((getCause() == null)
                    ? cause == null
                    : getCause().equals(cause))
              && ((getMessage() == null)
                    ? message == null
                    : getMessage().equals(message));
  }

  /**
   * @param     originType
   *            The type of origin to compare the one of this Exception with.
   * @param     propertyName
   *            The  propertyName to compare the one of this Exception with.
   * @param     message
   *            The message to compare the one of this Exception with..
   * @param     cause
   *            The cause to compare the one of this Exception with.
   *
   * @since IV
   */
  @MethodContract(
    post = @Expression("(originType == ^originType) && (propertyName == ^propertyName) && (message == ^message) && (cause == ^cause)")
  )
  public final boolean hasProperties(final Class<?> originType,
                                     final String propertyName,
                                     final String message,
                                     final Throwable cause) {
    return (getOriginType() == originType)
              && ((getPropertyName() == null)
                    ? propertyName == null
                    : getPropertyName().equals(propertyName))
              && ((getCause() == null)
                    ? cause == null
                    : getCause().equals(cause))
              && ((getMessage() == null)
                    ? message == null
                    : getMessage().equals(message));
  }

  /*</section>*/



  /*<section name="reports on">*/
  //------------------------------------------------------------------

  /**
   * Does this exception, in some way, report about
   * an exceptional condition concerning <code>origin</code>,
   * <code>propertyName</code>, with <code>message</code>
   * and <code>cause</code>.
   *
   * This is the non-deterministic version of
   * {@link #hasProperties(Object, String, String, Throwable)}, which
   * can be overwritten by subclasses.
   * The default implementation calls
   * {@link #hasProperties(Object, String, String, Throwable)}.
   */
  @MethodContract(
    post = {
      @Expression("true"),
      @Expression(scope = PROTECTED,
                  value = "hasProperties(^origin, ^propertyName, ^message, ^cause)")
    }
  )
  public boolean reportsOn(final Object origin,
                            final String propertyName,
                            final String message,
                            final Throwable cause) {
    return hasProperties(origin, propertyName, message, cause);
  }

  /**
   * Does this exception, in some way, report about
   * an exceptional condition concerning <code>originType</code>,
   * <code>propertyName</code>, with <code>message</code>
   * and <code>cases</code>.
   *
   * This is the non-deterministic version of
   * {@link #hasProperties(Class, String, String, Throwable)}, which
   * can be overwritten by subclasses.
   * The default implementation calls
   * {@link #hasProperties(Class, String, String, Throwable)}.
   *
   * @since IV
   */
  @MethodContract(
    post = {
      @Expression("true"),
      @Expression(scope = PROTECTED,
                  value = "hasProperties(^originType, ^propertyName, ^message, ^cause)")
    }
  )
  public boolean reportsOn(final Class<?> originType,
                            final String propertyName,
                            final String message,
                            final Throwable cause) {
    return hasProperties(originType, propertyName, message, cause);
  }

  /*</section>*/



//  /*<property name="localizedMessageKeys">*/
//  //------------------------------------------------------------------
//
//  private static final String EMPTY = "";
//  private static final String DOT = ".";
//  private static final String PREFIX = "message.";
//
//  /**
//   * The keys that are tried consecutively are intended for use in
//   * a properties file that comes with the
//   * {@link #getOriginType() origin type} of the exception, with a fall-back
//   * to a properties file that comes with the class of the exception.
//   * The message that is given in the constructor (the
//   * {@link #getMessage()} non-localized message) is intended as the
//   * final discriminating key in a resource bundle.
//   *
//   * <p>The first key, for use in the properties file that comes with
//   *   the {@link #getOriginType() origin type} of the exception, has the form
//   *   <code>getClass().getName() + "." + getPropertyName()
//   *         + "." + getMessage()</code>.
//   *   If the property name is <code>null</code>, the form is
//   *   <code>getClass().getName() + "." + getMessage()</code>. This is
//   *   intended for exceptions that are not bound to a particular
//   *   property. If the message is <code>null</code>, the form of the key
//   *   is <code>getClass().getName() + "." + getPropertyName()</code>
//   *   or <code>getClass().getName()</code>. These forms are intended for
//   *   exceptions of which the type itself is discriminating enough
//   *   for a good exception message. </p>
//   * <p>The second key is intended for use in a properties file that
//   *   comes with the exception class. It is intended for error messages
//   *   that can be written independent of the actual origin or property
//   *   for which they occurred. Such messages should often be considered
//   *   a fall-back.
//   *   The key that should be used in these files is of the form
//   *   <code>"message." + getMessage()</code>. If the message is
//   *   <code>null</code>, no such key is added to the array.</p>
//   * <p>When the {@link #getMessage() message} is used as a key,
//   *   it should be in all caps.</p>
//   *
//   * @result    result != null;
//   * @result    result.length == ((getMessage() != null) ? 2 : 1);
//   * @result    result[0] != null;
//   * @result    (getPropertyName() != null) && (getMessage() != null)
//   *                ==> result[0].equals(getClass().getName()
//   *                                     + "." + getPropertyName()
//   *                                     + "." + getMessage());
//   * @result    (getPropertyName() == null) && (getMessage() != null)
//   *                ==> result[0].equals(getClass().getName()
//   *                                     + "." + getMessage());
//   * @result    (getPropertyName() != null) && (getMessage() == null)
//   *                ==> result[0].equals(getClass().getName()
//   *                                     + "." + getPropertyName());
//   * @result    (getPropertyName() == null) && (getMessage() == null)
//   *                ==> result[0].equals(getClass().getName());
//   * @result    (getMessage() != null)
//   *                ==> (result[1] != null)
//   *                    && result[1].equals("message." + getMessage());
//   */
//  public final String[] getLocalizedMessageKeys() {
//    String[] result = null;
//    String firstKey = getClass().getName()
//                      + (getPropertyName() != null
//                            ? DOT + getPropertyName()
//                            : EMPTY)
//                      + (getMessage() != null ? DOT + getMessage() : EMPTY);
//    String secondKey = getMessage() != null ? PREFIX + getMessage() : null;
//    if (secondKey != null) {
//      result = new String[] {firstKey, secondKey};
//    }
//    else {
//      result = new String[] {firstKey};
//    }
//    return result;
//  }
//
//  /*</property>*/

}
