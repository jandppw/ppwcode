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

package org.ppwcode.util.reflect_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.preArgumentNotEmpty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;

/**
 * Structured parse of a signature String, containing the {@link #getMethodName() method name},
 * the {@link #getParameterTypeNames() canonical name of the parameter types} and the
 * {@link #getParameterTypes() parameter types}.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("methodName != null"),
  @Expression("methodName != EMPTY"),
  @Expression("parameterTypes != null"),
  @Expression("! Arrays.contains(parameterTypes, null)")
})
public final class MethodSignature {

  /*<construction>*/
  //-------------------------------------------------------------------------

  /**
   * @pre signature != null;
   */
  @MethodContract(
    pre  = {
      @Expression("_signature != null"),
      @Expression("_signature != EMPTY"),
      @Expression(value = "true", description = "_signature is a well-formed method signature")
    },
    post = @Expression(value = "true",
                       description = "the method name and types of the resulting object match the given _signature")
  )
  public MethodSignature(String signature) {
    preArgumentNotEmpty(signature, "signature");
    int openingParenthesis = signature.indexOf("(");
    int closingParenthesis = signature.indexOf(")");
    pre((openingParenthesis >= 0) && (closingParenthesis >= 0), "\"" + signature + "\": missing parentheses");
    $methodName = signature.substring(0, openingParenthesis).trim();
    String parameters = signature.substring(openingParenthesis + 1, closingParenthesis);
    assert parameters != null;
    parameters = parameters.trim();
    if (parameters.length() == 0) {
      /* Contract of String.split(): If the expression does not match any part
       * of the input then the resulting array has just one element, namely
       * this string. Split works for >= 1 parameters, but not for 0 arguments. */
      $parameterTypeNames = new String[0];
    }
    else { // > 0 parameters
      $parameterTypeNames = parameters.split(",");
      for (int i = 0; i < $parameterTypeNames.length; i++) {
        assert $parameterTypeNames[i] != null;
        $parameterTypeNames[i] = $parameterTypeNames[i].trim();
      }
    }
    $parameterTypes = new Class<?>[$parameterTypeNames.length];
    for (int i = 0; i < $parameterTypeNames.length; i++) {
      String parameterTypeName = $parameterTypeNames[i];
// MUDO deal with [] array types
      $parameterTypes[i] = TypeHelpers.type(parameterTypeName);
      $parameterTypeNames[i] = $parameterTypes[i].getCanonicalName();
    }
  }

  @MethodContract(
    pre  = @Expression("_method != null"),
    post = {
      @Expression("methodName == _method.name"),
      @Expression("Arrays.equals(parameterTypes, method.parameterTypes")
    }
  )
  public MethodSignature(Method method) {
    assert method != null;
    $methodName = method.getName();
    $parameterTypes = method.getParameterTypes();
    $parameterTypeNames = new String[$parameterTypes.length];
    for (int i = 0; i < $parameterTypes.length; i++) {
      $parameterTypeNames[i] = $parameterTypes[i].getCanonicalName();
    }
  }

  @MethodContract(
    pre  = @Expression("_method != null"),
    post = {
      @Expression("methodName == _method.class.simpleName"),
      @Expression("Arrays.equals(parameterTypes, method.parameterTypes")
    }
  )
  public MethodSignature(Constructor<?> method) {
    assert method != null;
    $methodName = method.getClass().getSimpleName();
    $parameterTypes = method.getParameterTypes();
    $parameterTypeNames = new String[$parameterTypes.length];
    for (int i = 0; i < $parameterTypes.length; i++) {
      $parameterTypeNames[i] = $parameterTypes[i].getCanonicalName();
    }
  }

  /*/<construction>*/



  /*<property name="method name">*/
  //-------------------------------------------------------------------------

  @Basic
  public final String getMethodName() {
    return $methodName;
  }

  /**
   * The name of the method we represent.
   */
  @Invars({
    @Expression("$methodName != null"),
    @Expression("$methodName != EMPTY")
  })
  private final String $methodName;

  /*/<property>*/



  /*<property name="parameter type names">*/
  //-------------------------------------------------------------------------

  @MethodContract(
    post = {
      @Expression("result != null"),
      @Expression("result.length == parameterTypes.length"),
      @Expression("! Arrays.contains(result, null)"),
      @Expression("! Arrays.contains(result, EMPTY)"),
      @Expression("for (int i : 0 .. result.lenght - 1) {result[i] == parameterTypes[i].getCanonicalName}")
    }
  )
  public final String[] getParameterTypeNames() {
    return $parameterTypeNames;
  }

  /**
   * The names of the parameter types, in order.
   */
  @Invars({
    @Expression("$parameterTypeNames != null"),
    @Expression("$parameterTypeNames.length == $parameterTypes.length"),
    @Expression("! Arrays.contains($parameterTypeNames, null)"),
    @Expression("! Arrays.contains($parameterTypeNames, EMPTY)"),
    @Expression("for (int i : 0 .. $parameterTypeNames.lenght - 1) {$parameterTypeNames[i] == $parameterTypes[i].getCanonicalName}")
  })
  private final String[] $parameterTypeNames;

  /*/<property>*/



  /*<property name="parameter types">*/
  //-------------------------------------------------------------------------

  @Basic
  public Class<?>[] getParameterTypes() {
    return $parameterTypes;
  }

  /**
   * The types of the parameters of this method, in order.
   */
  @Invars({
    @Expression("$parameterTypes != null"),
    @Expression("! Arrays.contains($parameterTypes, null)")
  })
  private final Class<?>[] $parameterTypes;

  /*/<property>*/



  /*<property name="number of parameters">*/
  //-------------------------------------------------------------------------

  @MethodContract(
    post = @Expression("parameterTypes.length")
  )
  public final int getNumberOfParameters() {
    return $parameterTypes.length;
  }

  /*/<property>*/


  /**
   * Signature of the represented method in expected format (without argument names).
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer(getMethodName());
    result.append("(");
    for (int i = 0; i < $parameterTypeNames.length; i++) {
      result.append($parameterTypeNames[i]);
      if (i < $parameterTypeNames.length - 1) {
        result.append(",");
      }
    }
    result.append(")");
    return result.toString();
  }

  /**
   * Signatures are considered equal if they are of this type, the {@link #getMethodName()}
   * is exactly the same, and the {@link #getParameterTypes()} are exactly the same, in order.
   */
  @Override
  @MethodContract(
    post = @Expression("(obj != null) && (obj instanceof MethodSignature) && " +
                       "(methodName == other.methodName) && Arrays.equals(parameterTypes, other.parameterTypes)")
  )
  public final boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    else if (! (obj instanceof MethodSignature)) {
      return false;
    }
    else {
      MethodSignature other = (MethodSignature)obj;
      return getMethodName().equals(other.getMethodName()) &&
             Arrays.equals(getParameterTypes(), other.getParameterTypes());
    }
  }

  @Override
  public final int hashCode() {
    int result = getMethodName() == null ? 0 : getMethodName().hashCode();
    for (Class<?> c : getParameterTypes()) {
      result += c.hashCode();
    }
    return result;
  }

}
