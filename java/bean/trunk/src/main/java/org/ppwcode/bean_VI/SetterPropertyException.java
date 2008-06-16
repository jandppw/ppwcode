/*<license>
Copyright 2004 - $Date: 2008-06-14 00:35:00 +0200 (Sat, 14 Jun 2008) $ by PeopleWare n.v..

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
import static org.ppwcode.util.reflect_I.CloneUtil.safeReference;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.reflect_I.CloneUtil;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>In many cases, a property exception is needed that reports
 *   the new value that was tried to set, and the old value
 *   of the property. These values can be used to generate
 *   sensible end-user messages of the form <q>Unable to change
 *   {propertyName} for {origin} from {propertyValue} to
 *   {vetoedValue}</q>.</p>
 * <p>This exception is a generalized version of a
 *   {@link PropertyException} that carries that information.
 *   It is a bore to create separate exceptions for each of those
 *   specific cases.</p>
 * <p>It would be nice to use generics for the type of the
 *   property value, but generics are not possible for exceptions.</p>
 * <p>This exception can be used for simple properties of all kinds:
 *   simple properties of reference type, as well as simple properties
 *   of value types, both of mutable types and immutable types. When
 *   the values are put into the exception and when they are returned
 *   out of the exception, we try to clone them. We expect mutable
 *   value types to be cloneable, and immutable value types and
 *   reference types not to be cloneable. This way, the actual value
 *   is guarded from change.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-06-14 00:35:00 +0200 (Sat, 14 Jun 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1124 $",
         date     = "$Date: 2008-06-14 00:35:00 +0200 (Sat, 14 Jun 2008) $")
@Invars(@Expression("propertyName != null"))
public class SetterPropertyException extends PropertyException {

  /*<construction>*/
  //-------------------------------------------------------------------------

  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     vetoedValue
   *            The value that was tried to set on property {@code propertyName},
   *            which is being vetoed by this exception.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   */
  @MethodContract(
    pre  = {
      @Expression("^origin != null"),
      @Expression("^propertyName != null"),
      @Expression("hasProperty(^origin.class, ^propertyName)"),
      @Expression("^message == null || ! ^message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == ^origin"),
      @Expression("originType == ^origin.class"),
      @Expression("propertyName == ^propertyName"),
      @Expression("propertyValue == safeReference(^origin[^propertyName])"),
      @Expression("vetoedValue == safeReference(^vetoedValue)"),
      @Expression("value == ^ value"),
      @Expression("message == ^message"),
      @Expression("cause == ^cause")
    }
  )
  public SetterPropertyException(final Object origin,
                                 final String propertyName,
                                 final Object vetoedValue,
                                 final String message,
                                 final Throwable cause) {
    super(origin, propertyName, message, cause);
    assert propertyName != null;
    $propertyValue = safePropertyValue(origin, propertyName);
    $vetoedValue = safeReference(vetoedValue);
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
      @Expression("^propertyName != null"),
      @Expression("hasProperty(^origin.class, ^propertyName)"),
      @Expression("^message == null || ! ^message.equals(EMPTY)")
    },
    post = {
      @Expression("inOriginInitialization ? origin == null : origin == ^origin"),
      @Expression("originType == ^origin.class"),
      @Expression("propertyName == ^propertyName"),
      @Expression("propertyValue == (inOriginInitialization ? null : safeReference(^origin[^propertyName]))"),
      @Expression("vetoedValue == safeReference(^vetoedValue)"),
      @Expression("message == ^message"),
      @Expression("cause == ^cause")
    }
  )
  public SetterPropertyException(final Object origin,
                                 final boolean inOriginInitialization,
                                 final String propertyName,
                                 final Object vetoedValue,
                                 final String message,
                                 final Throwable cause) {
    super(origin, inOriginInitialization, propertyName, message, cause);
    assert propertyName != null;
    if (! inOriginInitialization) {
      $propertyValue = safePropertyValue(origin, propertyName);
    }
    $vetoedValue = safeReference(vetoedValue);
  }


  /**
   * @param     originType
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     vetoedValue
   *            The value that was tried to set on property {@code propertyName},
   *            which is being vetoed by this exception.
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
      @Expression("^propertyName != null"),
      @Expression("hasProperty(^originType, ^propertyName)"),
      @Expression("^message == null || ! ^message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == null"),
      @Expression("originType == ^originType"),
      @Expression("propertyName == ^propertyName"),
      @Expression("propertyValue == null"),
      @Expression("vetoedValue == safeReference(^vetoedValue)"),
      @Expression("message == ^message"),
      @Expression("cause == ^cause")
    }
  )
  public SetterPropertyException(final Class<?> originType,
                                 final String propertyName,
                                 final Object vetoedValue,
                                 final String message,
                                 final Throwable cause) {
    super(originType, propertyName, message, cause);
    assert propertyName != null;
    $vetoedValue = safeReference(vetoedValue);
  }

  private static Object safePropertyValue(final Object origin, final String propertyName) {
    try {
      return safeReference(PropertyUtils.getSimpleProperty(origin, propertyName));
    }
    catch (IllegalAccessException exc) {
      assert false : "IllegalAccessException should not happen: " + exc + " we already tested that the property exists";
    }
    catch (InvocationTargetException exc) {
      assert false : "InvocationTargetException should not happen: " + exc + " we already tested that the property exists";
    }
    catch (NoSuchMethodException exc) {
      assert false : "NoSuchMethodException should not happen: " + exc + " we already tested that the property exists";
    }
    return null; // make compiler happy
  }

  /*</construction>*/



  /*<property name="property value">*/
  //------------------------------------------------------------------

  /**
   * The value of property {@link #getPropertyName()} of {@link @getOrigin()}
   * at the time this exception occurred. {@code null} is not applicable.
   */
  @Basic(invars = @Expression("origin == null ? propertyValue = null"))
  public final Object getPropertyValue() {
    return CloneUtil.safeReference($propertyValue);
  }

  private Object $propertyValue;

  /*</property>*/



  /*<property name="vetoed value">*/
  //------------------------------------------------------------------

  /**
   * The value that was tried to set on property {@link #getPropertyName()},
   * which was vetoed by this exception.
   */
  @Basic
  public final Object getVetoedValue() {
    return CloneUtil.safeReference($vetoedValue);
  }

  private Object $vetoedValue;

  /*</property>*/

}
