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

package org.ppwcode.vernacular.value_III;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;

import java.util.Locale;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.exception_III.SemanticException;
import org.ppwcode.vernacular.l10n_III.I18nTemplateException;
import org.ppwcode.vernacular.l10n_III.resourcebundle.DefaultResourceBundleLoadStrategy;
import org.ppwcode.vernacular.l10n_III.resourcebundle.KeyNotFoundException;
import org.ppwcode.vernacular.l10n_III.resourcebundle.ResourceBundleHelpers;
import org.ppwcode.vernacular.l10n_III.resourcebundle.WrongValueTypeException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * A {@link SemanticException} for value types. This is thrown most often
 * by mutable value types, when a property is set in a way that doesn't agree
 * with the invariants. The exception always carries the type of the value for
 * which the exception occured, and if possible, the value instance.
 * The message of the exception should be, like with any {@code ApplicationException},
 * a key with which a localized message that can be retrieved from a properties file.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class ValueException extends SemanticException {

  /*<construction>*/
  //------------------------------------------------------------------

  @MethodContract(
    pre  = @Expression("_valueType != null"),
    post = {
      @Expression("valueType == _valueType"),
      @Expression("value == null"),
      @Expression("message == _messageKey"),
      @Expression("cause == _cause;")
    }
  )
  public ValueException(Class<? extends Value> valueType, String messageKey, Throwable cause) {
    super(messageKey, cause);
    assert preArgumentNotNull(valueType, "valueType");
    $valueType = valueType;
    $value = null;
  }

  @MethodContract(
    pre  = @Expression("_value != null"),
    post = {
      @Expression("value == _value"),
      @Expression("valueType == _value.class"),
      @Expression("message == _messageKey"),
      @Expression("cause == _cause;")
    }
  )
  public ValueException(Value value, String messageKey, Throwable cause) {
    super(messageKey, cause);
    assert preArgumentNotNull(value, "value");
    $value = value;
    $valueType = value.getClass();
  }

  /*</construction>*/



  /*<property name="value type">*/
  //------------------------------------------------------------------

  /**
   * The type of value for which an exception occured. This is thrown
   * by the value, or by code supporting the value, e.g., helper methods
   * or transformation code.
   */
  @Basic(invars = @Expression("valueType != null"))
  public final Class<? extends Value> getValueType() {
    return $valueType;
  }

  @Invars(@Expression("$valueType != null"))
  private final Class<? extends Value> $valueType;

  /*</property>*/



  /*<property name="value type">*/
  //------------------------------------------------------------------

  /**
   * The value instance for which an exception occured. This is thrown
   * by the value, or by code supporting the value, e.g., helper methods
   * or transformation code. This can be {@code null}, e.g., when the exception
   * occured during construction of a value.
   */
  @Basic
  public final Value getValue() {
    return $value;
  }

  private final Value $value;

  /*</property>*/


  /*<section name="getMessageTemplate">*/
  //------------------------------------------------------------------

  private static String DOT = ".";

  /**
   * <p>This method is an implementation of the method defined in the {@code LocalizedException}
   * interface and enables us to give localized error messages using the ppwcode-vernacular-l10n
   * framework.</p>
   *
   * <p>The localized template for the error messages is searched for in the following locations:</p>
   * <ul>
   * <li>The properties file of class {@code valueType} and its supertypes. In this properties-file,
   *     the following keys are searched, in the given order:
   *   <ul>
   *     <li>{this.getClass().getCanonicalName()}.{getMessage()}</li>
   *     <li>{this.getClass().getCanonicalName()}</li>
   *   </ul>
   * </li>
   * <li>The properties file of the exception itself.  This is the same as done in the super class.
   * </li>
   * </ul>
   */
   @Override
  public String getMessageTemplate(Locale locale) throws I18nTemplateException {
    assert preArgumentNotNull(locale, "locale");
    String result = null;

    //  find the correct key in the properties files
    // 1. first check properties-file of class originType and its supertypes
    //   a. key = {this.getClass().getCanonicalName()}.{getMessage()}
    //   b. key = {this.getClass().getCanonicalName()}
    String prefix = getClass().getCanonicalName();
    String[] keys = { prefix + DOT + getMessage(),
                      prefix };
    DefaultResourceBundleLoadStrategy strategy = new DefaultResourceBundleLoadStrategy();
    strategy.setLocale(locale);
    try {
      result = ResourceBundleHelpers.value(getValueType(), keys, String.class, strategy);
    } catch (WrongValueTypeException exc) {
      unexpectedException(exc);
    } catch (KeyNotFoundException exc) {
      // no problem, still calling the getMessageTemplate function of the super class
    }

    // 2. next, check properties-file of the exception itself
    //   a. key = {getMessage()}
    //   b. key = ApplicationException.DEFAULT_MESSAGE_KEY
    // TODO  is (b) actually done and/or needed ?  or should it be considered an error ??
    if (result == null) {
      // super class -> ApplicationException.getMessageTemplate
      result = super.getMessageTemplate(locale);
    }

    return result;
  }

  /*</section>*/


}

