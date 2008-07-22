package org.ppwcode.util.reflect_I;

import java.lang.reflect.Method;

/**
 * Structured parse of a signature String, containing the {@link #getMethodName() method name},
 * the {@link #getParameterTypeNames() canonical name of the parameter types} and the
 * {@link #getParameterTypes() parameter types}.
 *
 * @invar getParameterTypes() != null;
 * @invar noNull(getParameterTypes());
 *
 * @note partial copy from toryt_II_dev
 */
public class MethodSignature {

  /**
   * @pre signature != null;
   */
  public MethodSignature(String signature) throws CannotParseSignatureException {
    assert signature != null;
    int openingParenthesis = signature.indexOf("(");
    int closingParenthesis = signature.indexOf(")");
    if ((openingParenthesis < 0) || (closingParenthesis < 0)) {
      throw new CannotParseSignatureException(signature, "missing parentheses");
    }
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
      try {
        $parameterTypes[i] = ClassHelpers.loadForName(parameterTypeName);
      }
      catch (CannotGetClassException cgcExc) {
        throw new CannotParseSignatureException(signature, cgcExc);
      }
      $parameterTypeNames[i] = $parameterTypes[i].getCanonicalName();
    }
  }

  /**
   * @pre method != null;
   */
  public MethodSignature(Method method) {
    assert method != null;
    $methodName = method.getName();
    $parameterTypes = method.getParameterTypes();
    $parameterTypeNames = new String[$parameterTypes.length];
    for (int i = 0; i < $parameterTypes.length; i++) {
      $parameterTypeNames[i] = $parameterTypes[i].getCanonicalName();
    }
  }

  /**
   * @basic
   */
  public final String getMethodName() {
    return $methodName;
  }

  private final String $methodName;

  /**
   * @result result != null;
   * @result result.length = getNumberOfParameters();
   * @result forall(int i = 0 .. getNumberOfParameters()) {
   *           result[i].equals(getParameterTypes()[i].getCanonicalName())
   *         };
   */
  public final String[] getParameterTypeNames() {
    return $parameterTypeNames;
  }

  private final String[] $parameterTypeNames;

  /**
   * @basic
   */
  public Class<?>[] getParameterTypes() {
    return $parameterTypes;
  }

  private final Class<?>[] $parameterTypes;

  /**
   * @return getParameterTypes().lenght;
   */
  public final int getNumberOfParameters() {
    return $parameterTypes.length;
  }

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

  @Override
  public final boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    else if (! (obj instanceof MethodSignature)) {
      return false;
    }
    else {
      MethodSignature other = (MethodSignature)obj;
      if (! equalsWithNull(getMethodName(), other.getMethodName())) {
        return false;
      }
      else {
        Class<?>[] thisPt = getParameterTypes();
        Class<?>[] otherPt = other.getParameterTypes();
        if (thisPt.length != otherPt.length) {
          return false;
        }
        else {
          for (int i = 0; i < thisPt.length; i++) {
            if (thisPt[i] != otherPt[i]) {
              return false;
            }
          }
          return true;
        }
      }
    }
  }

  private static boolean equalsWithNull(Object one, Object other) {
    if (one == other) {
      return true;
    }
    else if (one == null) {
      assert other != null;
      return false;
    }
    else {
      assert one != null;
      assert other != null;
      return one.equals(other);
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