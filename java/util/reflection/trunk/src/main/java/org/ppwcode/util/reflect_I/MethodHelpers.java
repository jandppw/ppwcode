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


import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.preArgumentNotEmpty;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.unexpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.ppwcode.vernacular.exception_II.ProgrammingErrors;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Utility methods for method reflection (including constructors). Use these methods if you are
 *   interested in the result of reflection, and not in a particular reason why some reflective
 *   inspection might have failed. The ppwcode exception vernacular is applied.</p>
 *
 * @note To find out what the accessibility of returned methods is, use {@link Modifier#isPublic(int)},
 *       etcetera, on {@link Method#getModifiers() the modifiers} of the result.
 *
 * @author Jan Dockx
 *
 * @mudo this MUST be changed to include inherited methods
 */
public final class MethodHelpers {

  private MethodHelpers() {
    // NOP
  }

  /**
   * <p>Return the method of class {@code type} with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrors}).</p>
   * <p>{@code findMethod} returns any method (not only {@code public} methods as
   *   {@link Class#getMethod(String, Class...)} does), but only methods declared exactly in {@code type},
   *   like {@link Class#getDeclaredMethod(String, Class...)}, and unlike
   *   {@link Class#getMethod(String, Class...)}: inherited methods do not apply.</p>
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
   *
   * @note this method is introduced because there is one exception we need in other methods in this class,
   *       which we cannot eat here: {@link NoSuchMethodException}. See {@link #hasMethod(Class, String)}
   *       and {@link #hasMethod(Object, String)}. The method is only package accessible.
   *
   * @todo This method was first private, now package accessible (see {@link CloneHelpers}). Probably
   *       this method has to become public, with an object and a class variant.
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_signature != null"),
      @Expression("_signature != EMPTY"),
      @Expression(value = "true", description = "_signature is a valid signature")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.declaringClass == _type"),
      @Expression("result.name == new MethodSignature(_signature).methodName"),
      @Expression("Arrays.deepEquqls(result.parameterTypes, new MethodSignature(_signature).parameterTypes")
    },
    exc  = {
      @Throw(type = NoSuchMethodException.class,
             cond = @Expression(value = "true", description = "No method of signature _signature found in _type"))
    }
  )
  static Method methodHelper(Class<?> type, String signature) throws NoSuchMethodException {
    assert preArgumentNotNull(type, "type");
    assert preArgumentNotEmpty(signature, "signature");
    Method result = null;
    try {
      MethodSignature sig = new MethodSignature(signature);
      result = type.getDeclaredMethod(sig.getMethodName(), sig.getParameterTypes());
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc);
    }
    catch (CannotParseSignatureException cpsExc) {
      unexpectedException(cpsExc, signature + " is not a signature");
    }
    catch (SecurityException sExc) {
      unexpectedException(sExc, "not allowed to access " + signature);
    }
    // NoSuchMethodException falls through
    return result;
  }

  /**
   * <p>Return the method of class {@code type} with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrors}).</p>
   * <p>{@code findMethod} returns any method (not only {@code public} methods as
   *   {@link Class#getMethod(String, Class...)} does), but only methods declared exactly in {@code type},
   *   like {@link Class#getDeclaredMethod(String, Class...)}, and unlike
   *   {@link Class#getMethod(String, Class...)}: inherited methods do not apply.</p>
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
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_signature != null"),
      @Expression("_signature != EMPTY"),
      @Expression(value = "true", description = "_signature is the signature of an existing method of _type")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.declaringClass == _type"),
      @Expression("result.name == new MethodSignature(_signature).methodName"),
      @Expression("Arrays.deepEquqls(result.parameterTypes, new MethodSignature(_signature).parameterTypes")
    }
  )
  public static Method method(Class<?> type, String signature) {
    Method result = null;
    try {
      result = methodHelper(type, signature);
    }
    catch (NoSuchMethodException exc) {
      unexpectedException(exc, "method " + signature + " not found in " + type.getName());
    }
    return result;
  }

//  anybody can do o.getClass(); this is stupid in a lib
//  /**
//   * <p>Return the method of the class of {@code o} with signature {@code signature}.
//   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrors}).</p>
//   * <p>{@code findMethod} returns any method (not only {@code public} methods as
//   *   {@link Class#getMethod(String, Class...)} does), but only methods declared exactly in the class of
//   *   {@code o}, like {@link Class#getDeclaredMethod(String, Class...)}, and unlike
//   *   {@link Class#getMethod(String, Class...)}: inherited methods do not apply.</p>
//   *
//   * @param o
//   *        The object whose class to look for the method in.
//   * @param signature
//   *        The signature of the method to look for. This is the name of the
//   *        method, with the FQCN of the arguments in parenthesis appended, comma
//   *        separated. For classes of the package {@code java.lang}, the short class
//   *        name may be used.
//   *        The return type is not a part of the signature, nor are potential
//   *        exception types the method can throw.
//   *        Example: {@code "findMethod(java.lang.Class,java.lang.String)"},
//   *        which is equivalent to {@code "findMethod ( Class, String )"}.
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_o != null"),
//      @Expression("_signature != null"),
//      @Expression("_signature != EMPTY"),
//      @Expression(value = "true", description = "_signature is the signature of an existing method of _o")
//    },
//    post = {
//      @Expression("result != null"),
//      @Expression("result.declaringClass == _o.class"),
//      @Expression("result.name == new MethodSignature(_signature).methodName"),
//      @Expression("Arrays.deepEquqls(result.parameterTypes, new MethodSignature(_signature).parameterTypes")
//    }
//  )
//  public static Method method(Object o, String signature) {
//    preArgumentNotNull(o, "o");
//    return method(o.getClass(), signature);
//  }

  /**
   * <p>Assert whether class {@code type} has a method with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrors}).</p>
   * <p>{@code hasMethod} returns {@code true} on the existence of any method (not only {@code public} methods
   *   as {@link Class#getMethod(String, Class...)} does), but only methods declared exactly in {@code _type},
   *   like {@link Class#getDeclaredMethod(String, Class...)}, and unlike
   *   {@link Class#getMethod(String, Class...)}: inherited methods do not apply.</p>
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
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_signature != null"),
      @Expression("_signature != EMPTY"),
      @Expression(value = "true", description = "_signature is a valid signature")
    },
    post = {
      @Expression("exists (Method m : _type.declaredMethods) {" +
                    "m.name == new MethodSignature(_signature).methodName && " +
                    "Arrays.deepEquqls(m.parameterTypes, new MethodSignature(_signature).parameterTypes" +
                  "}")
    }
  )
  public static boolean hasMethod(Class<?> type, String signature) {
    try {
      return methodHelper(type, signature) != null;
    }
    catch (NoSuchMethodException exc) {
      return false;
    }
  }

  /**
   * <p>Assert whether class {@code type} has a public method with signature {@code signature}.
   *   Only methods defined in {@code type} apply: inherited methods do not count.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrors}).</p>
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
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_signature != null"),
      @Expression("_signature != EMPTY"),
      @Expression(value = "true", description = "_signature is a valid signature")
    },
    post = {
      @Expression("exists (Method m : _type.declaredMethods) {" +
                    "m.name == new MethodSignature(_signature).methodName && " +
                    "Arrays.deepEquqls(m.parameterTypes, new MethodSignature(_signature).parameterTypes && " +
                    "Modifier.isPublic(m.modifiers)" +
                  "}")
    }
  )
  public static boolean hasPublicMethod(Class<?> type, String signature) {
    try {
      Method m = methodHelper(type, signature);
      return m != null && Modifier.isPublic(m.getModifiers());
    }
    catch (NoSuchMethodException exc) {
      return false;
    }
  }

//  anybody can do o.getClass(); this is stupid in an API
//  /**
//   * <p>Assert whether the class of {@code o} has a method with signature {@code signature}.
//   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrors}).</p>
//   * <p>{@code hasMethod} returns {@code true} on the existence of any method (not only {@code public} methods
//   *   as {@link Class#getMethod(String, Class...)} does), but only methods declared exactly in the class of
//   *   {@code _o}, like {@link Class#getDeclaredMethod(String, Class...)}, and unlike
//   *   {@link Class#getMethod(String, Class...)}: inherited methods do not apply.</p>
//   *
//   * @param o
//   *        The class to look for the method in.
//   * @param signature
//   *        The signature of the method to look for. This is the name of the
//   *        method, with the FQCN of the arguments in parenthesis appended, comma
//   *        separated. For classes of the package {@code java.lang}, the short class
//   *        name may be used.
//   *        The return type is not a part of the signature, nor are potential
//   *        exception types the method can throw.
//   *        Example: {@code "findMethod(java.lang.Class,java.lang.String)"},
//   *        which is equivalent to {@code "findMethod ( Class, String )"}.
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_o != null"),
//      @Expression("_signature != null"),
//      @Expression("_signature != EMPTY"),
//      @Expression(value = "true", description = "_signature is a valid signature")
//    },
//    post = {
//      @Expression("exists (Method m : _o.class.getDeclaredMethods()) {" +
//                    "m.name == new MethodSignature(_signature).methodName && " +
//                    "Arrays.deepEquqls(m.parameterTypes, new MethodSignature(_signature).parameterTypes" +
//                  "}")
//    }
//  )
//  public static boolean hasMethod(Object o, String signature) {
//    return o != null && hasMethod(o.getClass(), signature);
//  }

  /**
   * <p>Return the constructor of class {@code type} with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrors}).</p>
   * <p>{@code findMethod} returns any method (not only {@code public} methods as
   *   {@link Class#getMethod(String, Class...)} does), but only methods declared exactly in {@code type},
   *   like {@link Class#getDeclaredMethod(String, Class...)}, and unlike
   *   {@link Class#getMethod(String, Class...)}: inherited methods do not apply.</p>
   * <p>Note that the name of a constructor in {@link Constructor} is the FQCN
   *   (see {@link Constructor#getName()}), while in our signature it is intended to be
   *   the short, simple name.</p>
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
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_signature != null"),
      @Expression("_signature != EMPTY"),
      @Expression(value = "true", description = "_signature is the signature of an existing method of _type")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.declaringClass == _type"),
      @Expression("Arrays.deepEquals(result.parameterTypes, new MethodSignature(_signature).parameterTypes")
    }
  )
  public static <_T_> Constructor<_T_> constructor(Class<_T_> type, String signature) {
    assert preArgumentNotNull(type, "type");
    assert preArgumentNotEmpty(signature, "signature");
    Constructor<_T_> result = null;
    try {
      MethodSignature sig = new MethodSignature(signature);
      result = type.getDeclaredConstructor(sig.getParameterTypes());
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc);
    }
    catch (CannotParseSignatureException cpsExc) {
      unexpectedException(cpsExc, signature + " is not a signature");
    }
    catch (SecurityException sExc) {
      unexpectedException(sExc, "not allowed to access " + signature);
    }
    catch (NoSuchMethodException exc) {
      unexpectedException(exc, "constructor " + signature + " not found in " + type.getName());
    }
    return result;
  }

  // IDEA hasConstructor for consistency, but better not to introduce code we don't need

}
