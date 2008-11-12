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


import static java.lang.reflect.Modifier.isAbstract;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotEmpty;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.Queue;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Utility methods for method reflection (including constructors). Use these methods if you are
 *   interested in the result of reflection, and not in a particular reason why some reflective
 *   inspection might have failed. The ppwcode exception vernacular is applied.</p>
 * <p>The code in this class assumes that you are interested in methods that are applicable in
 *   the context of the given type. Therefore, we return or take into account all methods of the
 *   given type, and non-private inherited methods. Furthermore, in the retrieval methods, we
 *   assume that you know what you need, so that not finding the method you ask for is
 *   considered a programming error (e.g., typo in the signature). If you want to find out
 *   what the accessibility of a given method is, you can use the boolean inspectors provided
 *   here.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @mudo this MUST be changed to work with array types
 * @idea probably needed to add methods to take a FQN of a constant as a String, for use of static methods
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class MethodHelpers {

  private MethodHelpers() {
    // NOP
  }

  /**
   * The idiom for finding out whether a method {@code m} is public or not,
   * using the standard Java API, is
   * {@link Modifier#isPublic(int) Modifier.isPublic(}{@link Method#getModifiers() m.getModifiers()}{@code )}.
   * This shortens that a bit to {@code MethodHelpers.isPublic(m)}.
   */
  @MethodContract(
    pre  = @Expression("_m != null"),
    post = @Expression("Modifier.isPublic(_m.getModifiers())")
  )
  public static boolean isPublic(Method m) {
    assert preArgumentNotNull(m, "m");
    return Modifier.isPublic(m.getModifiers());
  }

  /**
   * The idiom for finding out whether a method {@code m} is protected or not,
   * using the standard Java API, is
   * {@link Modifier#isPublic(int) Modifier.isProtected(}{@link Method#getModifiers() m.getModifiers()}{@code )}.
   * This shortens that a bit to {@code MethodHelpers.isProtected(m)}.
   */
  @MethodContract(
    pre  = @Expression("_m != null"),
    post = @Expression("Modifier.isProtected(_m.getModifiers())")
  )
  public static boolean isProtected(Method m) {
    assert preArgumentNotNull(m, "m");
    return Modifier.isProtected(m.getModifiers());
  }

  /**
   * The idiom for finding out whether a method {@code m} is private or not,
   * using the standard Java API, is
   * {@link Modifier#isPublic(int) Modifier.isPrivate(}{@link Method#getModifiers() m.getModifiers()}{@code )}.
   * This shortens that a bit to {@code MethodHelpers.isPrivate(m)}.
   */
  @MethodContract(
    pre  = @Expression("_m != null"),
    post = @Expression("Modifier.isPrivate(_m.getModifiers())")
  )
  public static boolean isPrivate(Method m) {
    assert preArgumentNotNull(m, "m");
    return Modifier.isPrivate(m.getModifiers());
  }

  /**
   * The idiom for finding out whether a method {@code m} is package accessible or not,
   * using the standard Java API, is difficult, since there is no modifier for package accessibility.
   * We need to check there there is no accessibility modifier present.
   * This shortens that a bit to {@code MethodHelpers.isPackageAccessible(m)}.
   */
  @MethodContract(
    pre  = @Expression("_m != null"),
    post = @Expression("! Modifier.isPublic(_m.getModifiers()) && " +
                       "! Modifier.isProtected(_m.getModifiers()) && " +
                       "! Modifier.isPrivate(_m.getModifiers())")
  )
  public static boolean isPackageAccessible(Method m) {
    assert preArgumentNotNull(m, "m");
    int mModifiers = m.getModifiers();
    return ! Modifier.isPublic(mModifiers) && ! Modifier.isProtected(mModifiers) && ! Modifier.isPrivate(mModifiers);
  }

  /**
   * The idiom for finding out whether a method {@code m} is private or not,
   * using the standard Java API, is
   * {@link Modifier#isStatic(int) Modifier.isStatic(}{@code m.}{@link Method#getModifiers() getModifiers()}{@code )}.
   * This shortens that a bit to {@code MethodHelpers.isStatic(m)}.
   */
  @MethodContract(
    pre  = @Expression("_m != null"),
    post = @Expression("Modifier.isStatic(_m.getModifiers())")
  )
  public static boolean isStatic(Method m) {
    assert preArgumentNotNull(m, "m");
    return Modifier.isStatic(m.getModifiers());
  }

  /**
   * <p>Return the method of class {@code type} with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrorHelpers}).</p>
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
    catch (SecurityException sExc) {
      unexpectedException(sExc, "not allowed to access " + signature);
    }
    // NoSuchMethodException falls through
    return result;
  }

  /**
   * Package accessible for testing.
   * Look for the method with {@code signature} in {@code type}. If not found, repeat recursively in the superclasses,
   * but only accept non-private methods. If not found, traverse through all super interfaces we encountered,
   * recursively, to find the method.
   */
  static Method inheritedMethodHelper(Class<?> type, String signature) throws NoSuchMethodException {
    assert preArgumentNotNull(type, "type");
    assert preArgumentNotEmpty(signature, "signature");
    Method result = null;
    if (! type.isInterface()) {
      try {
        result = inheritedMethodClassHierarchyHelper(type, signature, true);
      }
      catch (NoSuchMethodException exc) {
        /* could not find method in class hierarchy; so at least, type is an abstract class or an interface, and
         * the method is abstract, if it exists at all; lets search, breath first in the interfaces hierarchy
         */
        if (! isAbstract(type.getModifiers())) {
          throw new NoSuchMethodException("method with signature " + signature + " does not exist in type hierarchy");
        }
        result = inheritedMethodInterfaceHierarchyHelper(type, signature);
      }
    }
    else {
      // it's an interface, go immediately to Start. You will not receive EUR8000.
      result = inheritedMethodInterfaceHierarchyHelper(type, signature);
    }
    return result;
  }

  private static Method inheritedMethodInterfaceHierarchyHelper(Class<?> type, String signature) throws NoSuchMethodException {
    assert preArgumentNotNull(type, "type");
    assert preArgumentNotEmpty(signature, "signature");
    Method result = null;
    Queue<Class<?>> interfaceStack = new LinkedList<Class<?>>();
    interfaceStack.add(type);
    while (! interfaceStack.isEmpty()) {
      Class<?> superInterface = interfaceStack.poll();
      if (superInterface.isInterface()) {
        try {
          result = methodHelper(superInterface, signature);
          // found one; in interfaces, this is surely public; this is it
          return result;
        }
        catch (NoSuchMethodException exc) {
          // try next interface, but first add super interfaces of super interface to the stack
          pushSuperTypes(superInterface, interfaceStack);
        }
      }
      else {
        // it's a class; don't look for the method here, but include its super types
        pushSuperTypes(superInterface, interfaceStack);
      }
    }
    throw new NoSuchMethodException("method with signature " + signature + " does not exist in type hierarchy");
  }

  private static void pushSuperTypes(Class<?> type, Queue<Class<?>> interfaceStack) {
    Class<?> superClass = type.getSuperclass();
    if (superClass != null) {
      interfaceStack.add(superClass);
    }
    Class<?>[] superInterfaces = type.getInterfaces();
    for (Class<?> superInterface : superInterfaces) {
      interfaceStack.add(superInterface);
    }
  }

  private static Method inheritedMethodClassHierarchyHelper(Class<?> type, String signature, boolean allowPrivate) throws NoSuchMethodException {
    assert preArgumentNotNull(type, "type");
    assert preArgumentNotEmpty(signature, "signature");
    assert pre(! type.isInterface());
    Method result = null;
    try {
      result = methodHelper(type, signature);
      if ((! allowPrivate) && isPrivate(result)) {
        // it makes no sense to seek higher in the hierarchy, since methods cannot be made more strict in subtypes
        throw new NoSuchMethodException("method with signature " + signature + " is private in " + type);
      }
    }
    catch (NoSuchMethodException nsmExc) {
      if (type == Object.class) {
        // we cannot go higher, try in interfaces
        throw new NoSuchMethodException("no method with signature " + signature + " found in class hierarchy");
      }
      else {
        result = inheritedMethodClassHierarchyHelper(type.getSuperclass(), signature, false);
        // only allow private in first class of hierarchy
      }
    }
    return result;
  }

  /**
   * <p>Return the method of class {@code type} with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrorHelpers}).</p>
   * <p>We return any matching method found in {@code type}, and if not found there, any non-private
   *   method found in a superclass, searching from this {@code type} towards {@link Object} (if {@code type}
   *   is a class). If not found in any of the superclasses, we start searching the interfaces, of {@code type},
   *   and later the superclasses in the order from {@code type} towards {@link Object}, recursively,
   *   breadfirst.</p>
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
      @Expression("result.declaringClass.isAssignableFrom(_type)"),
      @Expression("result.name == new MethodSignature(_signature).methodName"),
      @Expression("Arrays.deepEquals(result.parameterTypes, new MethodSignature(_signature).parameterTypes"),
      @Expression("result.declaringClass != _type ? ! isPrivate(result)")
    }
  )
  public static Method method(Class<?> type, String signature) {
    Method result = null;
    try {
      result = inheritedMethodHelper(type, signature);
    }
    catch (NoSuchMethodException exc) {
      unexpectedException(exc, "method " + signature + " not found in " + type.getName());
    }
    return result;
  }

  /**
   * <p>Assert whether class {@code type} has a method with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrorHelpers}).</p>
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
      @Expression("exists (Method m) {" +
                    "m.name == new MethodSignature(_signature).methodName && " +
                    "Arrays.deepEquals(m.parameterTypes, new MethodSignature(_signature).parameterTypes && " +
                    "m.declaringClass.isAssignableFrom(_type) && " +
                    "(m.declaringClass != _type ? ! isPrivate(m))" +
                  "}")
    }
  )
  public static boolean hasMethod(Class<?> type, String signature) {
    try {
      return inheritedMethodHelper(type, signature) != null;
    }
    catch (NoSuchMethodException exc) {
      return false;
    }
  }

  /**
   * <p>Assert whether class {@code type} has a public method with signature {@code signature}.
   *   Only methods defined in {@code type} apply: inherited methods do not count.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrorHelpers}).</p>
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
            @Expression("exists (Method m) {" +
                        "m.name == new MethodSignature(_signature).methodName && " +
                        "Arrays.deepEquals(m.parameterTypes, new MethodSignature(_signature).parameterTypes && " +
                        "m.declaringClass.isAssignableFrom(_type) && " +
                        "isPublic(m))" +
                      "}")
    }
  )
  public static boolean hasPublicMethod(Class<?> type, String signature) {
    try {
      return isPublic(inheritedMethodHelper(type, signature));
    }
    catch (NoSuchMethodException exc) {
      return false;
    }
  }

  /**
   * <p>Return the constructor of class {@code type} with signature {@code signature}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrorHelpers}).</p>
   * <p>{@code constructor} returns any method (not only {@code public} methods as
   *   {@link Class#getConstructor(Class...)} does).</p>
   * <p>Note that the name of a constructor in {@link Constructor} is the FQCN
   *   (see {@link Constructor#getName()}), while in our signature it is intended to be
   *   the short, simple name. If no constructor is defined in {@code type}, the default default constructor
   *   is found with the appropriate signature.</p>
   *
   * @param clazz
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
      @Expression("_clazz != null"),
      @Expression("! _clazz.isInterface()"),
      @Expression("_signature != null"),
      @Expression("_signature != EMPTY"),
      @Expression(value = "true", description = "_signature is the signature of an existing method of _clazz")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.declaringClass == _clazz"),
      @Expression("Arrays.deepEquals(result.parameterTypes, new MethodSignature(_signature).parameterTypes")
    }
  )
  public static <_T_> Constructor<_T_> constructor(Class<_T_> clazz, String signature) {
    assert preArgumentNotNull(clazz, "clazz");
    assert preArgumentNotEmpty(signature, "signature");
    Constructor<_T_> result = null;
    try {
      MethodSignature sig = new MethodSignature(signature);
      result = clazz.getDeclaredConstructor(sig.getParameterTypes());
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc);
    }
    catch (SecurityException sExc) {
      unexpectedException(sExc, "not allowed to access " + signature);
    }
    catch (NoSuchMethodException exc) {
      unexpectedException(exc, "constructor " + signature + " not found in " + clazz.getName());
    }
    return result;
  }

  /**
   * <p>Return the constructor of class {@code type} with argument types {@code parameterTypes}.
   *   If something goes wrong, this is considered a programming error (see {@link ProgrammingErrorHelpers}).</p>
   *
   * @param clazz
   *        The class to look for the method in.
   * @param parameterTypes
   *        The argument types in order of the constructor we want.
   */
  @MethodContract(
    pre  = {
      @Expression("_clazz != null"),
      @Expression("! _clazz.isInterface()"),
      @Expression("! _clazz.getDeclaredConstructor(_parameterTypes) throws")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.declaringClass == _clazz"),
      @Expression("Arrays.deepEquals(result.parameterTypes, _parameterTypes")
    }
  )
  public static <_T_> Constructor<_T_> constructor(Class<_T_> clazz, Class<?>... parameterTypes) {
    assert preArgumentNotNull(clazz, "clazz");
    Constructor<_T_> result = null;
    try {
      result = clazz.getDeclaredConstructor(parameterTypes);
    }
    catch (SecurityException sExc) {
      unexpectedException(sExc, "not allowed to access constructor of " + clazz);
    }
    catch (NoSuchMethodException exc) {
      unexpectedException(exc, "constructor with given argument types not found in " + clazz.getName());
    }
    return result;
  }

  // IDEA hasConstructor for consistency, but better not to introduce code we don't need

  /**
   * Get the result of the static method of class {@code clazz} with signature {@code signature}
   * and arguments {@code arguments}: {@code clazz.signature(arguments...)}.
   */
  @MethodContract(
    pre  = {@Expression("hasMethod(clazz, signature)"),
            @Expression("isStatic(method(clazz, signature))")},
    post = @Expression("method(clazz, signature).invoke(null, arguments)")
  )
  public static <_Result_> _Result_ invoke(Class<?> clazz, String signature, Object... arguments) {
    Method m = method(clazz, signature);
    assert isStatic(m);
    Object result = null;
    try {
      result = m.invoke(null, arguments);
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc);
    }
    catch (IllegalAccessException exc) {
      unexpectedException(exc);
    }
    catch (InvocationTargetException exc) {
      unexpectedException(exc);
    }
    catch (ExceptionInInitializerError err) {
      unexpectedException(err);
    }
    catch (NullPointerException exc) {
      unexpectedException(exc, signature + " is a static method");
    }
    @SuppressWarnings("unchecked")
    _Result_ typedResult = (_Result_)result;
    return typedResult;
  }

}
