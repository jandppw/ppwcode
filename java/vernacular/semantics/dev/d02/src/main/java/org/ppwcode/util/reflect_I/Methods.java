/*<license>
Copyright 2006 - $Date: 2008/04/03 22:19:23 $ by Jan Dockx.

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


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * <p>Utility methods for reflection. Use these methods if you are
 *   interested in the result of reflection, and not in a particular
 *   reason why some reflective inspection might have failed.</p>
 *
 * @idea (jand) most methods are also in ppw-bean; consolidate
 *
 * @author Jan Dockx
 *
 * @note partial copy from toryt_II_dev
 */
public class Methods {

  private Methods() {
    // NOP
  }

  /**
   * <p>Return the method of class {@code type} with signature {@code signature}.
   *   If something goes wrong, throw a {@link CannotGetMethodException}. In contrast
   *   to {@link Class#getDeclaredMethod(String, Class...)}, {@code findMethod}
   *   throws a simple exception when anything goes wrong (with more detailed reasons as
   *   the {@link Throwable#getCause() cause}). {@code findMethod} returns any method
   *   (not only {@code public} methods as {@link Class#getMethod(String, Class...)}
   *   does), but only methods declared exactly in {@code type}, like
   *   {@link Class#getDeclaredMethod(String, Class...)}, and unlike
   *   {@link Class#getMethod(String, Class...)}.</p>
   *
   * @param type
   *        The class to look for the method in.
   * @param signature
   *        The signature of the method to look for. This is the name of the
   *        method, with the FQCN of the arguments in parenthesis appended, comma
   *        separated. For classes of the package {@code java.lang}, the short class
   *        name may be used.
   *        The return type is not a part of the signature, nor are potential
   *        exception types the method can throw.
   *        Example: {@code "findMethod(java.lang.Class,java.lang.String)"},
   *        which is equivalent to {@code "findMethod ( Class, String )"}.
   * @pre    type != null;
   * @result result != null
   * @result result.getDeclaringClass() == type;
   * @result new MethodSignature(signature).getMethodName().equals(result.getName());
   * @result Arrays.deepEquals(result.getParameterTypes(), new MethodSignature(signature).getParameterTypes());
   * @throws CannotGetMethodException
   *         type == null;
   * @throws CannotGetMethodException
   *         signature == null;
   * @throws CannotGetMethodException
   *         new MethodSignature(signature) throws CannotParseSignatureException;
   * @throws CannotGetMethodException
   *         type.getDeclaredMethod(new MethodSignature(signature).getMethodName(),
   *                                new MethodSignature(signature).getParameterTypes())
   *           throws SecurityException;
   * @throws CannotGetMethodException
   *         type.getDeclaredMethod(new MethodSignature(signature).getMethodName(),
   *                                new MethodSignature(signature).getParameterTypes())
   *           throws NoSuchMethodException);
   */
  public static Method findMethod(Class<?> type, String signature) throws CannotGetMethodException {
    if (type == null) {
      throw new CannotGetMethodException(type, signature,
                                         new NullPointerException("type was null"));
    }
    if (signature == null) {
      throw new CannotGetMethodException(type, signature,
                                         new NullPointerException("signature was null"));
    }
    try {
      MethodSignature sig = new MethodSignature(signature);
      return type.getDeclaredMethod(sig.getMethodName(), sig.getParameterTypes());
    }
    catch (NullPointerException npExc) {
      throw new CannotGetMethodException(type, signature, npExc);
    }
    catch (CannotParseSignatureException cpsExc) {
      throw new CannotGetMethodException(type, signature, cpsExc);
    }
    catch (SecurityException sExc) {
      throw new CannotGetMethodException(type, signature, sExc);
    }
    catch (NoSuchMethodException nsmExc) {
      throw new CannotGetMethodException(type, signature, nsmExc);
    }
  }

  public static Method findMethod(Object o, String signature) throws CannotGetMethodException {
    if (o == null) {
      throw new CannotGetMethodException(null, signature, new NullPointerException("object to get method for was null"));
    }
    return findMethod(o.getClass(), signature);
  }

  public static boolean hasMethod(Class<?> type, String signature) {
    try {
      return type != null && findMethod(type, signature) == null;
    }
    catch (CannotGetMethodException exc) {
      return false;
    }
  }

  public static boolean hasMethod(Object o, String signature) {
    return o != null && hasMethod(o.getClass(), signature);
  }

  public static <_T_> Constructor<_T_> findConstructor(Class<_T_> type, String signature)
      throws CannotGetMethodException {
    try {
      @SuppressWarnings("unchecked") Constructor<_T_>[] constructors = type.getConstructors();
      // unchecked cast because Class.getConstructors return Constructor[] instead of Constructor<T>[]
      for (int i = 0; i < constructors.length; i++) {
        if (constructors[i].toString().indexOf(signature) > -1) {
          return constructors[i];
        }
      }
      throw new CannotGetMethodException(type, signature, null);
    }
    catch (NullPointerException npExc) {
      throw new CannotGetMethodException(type, signature, npExc);
    }
  }

}