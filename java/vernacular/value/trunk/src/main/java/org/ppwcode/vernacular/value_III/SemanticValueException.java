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


import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;

import org.ppwcode.vernacular.exception_II.SemanticException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * A {@link SemanticException} for value types. This is thrown most often
 * by mutable value types, when a property is set in a way that doesn't agree
 * with the invariants. The exception always carries the type of the value for
 * which the exception occured, and if possible, the value instance.
 * The message of the exception should be, like with any {@code InternalException},
 * a key with which a localized message can be retrieved from a properties file.
 */
public class SemanticValueException extends SemanticException {

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
  public SemanticValueException(Class<? extends Value> valueType, String messageKey, Throwable cause) {
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
  public SemanticValueException(Value value, String messageKey, Throwable cause) {
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

}

