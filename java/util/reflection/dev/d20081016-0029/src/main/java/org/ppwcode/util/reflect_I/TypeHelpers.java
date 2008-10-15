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
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotEmpty;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.beans.Beans;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Utility methods for type reflection. Use these methods if you are interested in the result of reflection,
 *   and not in a particular reason why some reflective inspection might have failed. The ppwcode exception
 *   vernacular is applied.</p>
 * <p>In general, this code works with {@link Class#getCanonicalName()} <dfn>canonical names</dfn>}, i.e., the
 *   names of types as they are used in source code, whereas methods in {@link Class} generally work with
 *   {@link Class#getName() <dfn>binary names</dfn>}.</p>
 *
 * <h3 id="onNestedClasses">On nested classes</h3>
 * <p>On close inspection, the terminology on <dfn>nested classes</dfn> turns out to be
 *   somewhat contrived. Terms used are <dfn>top level types</dfn>, <dfn>nested types</dfn>, <dfn>member types</dfn>,
 *   <dfn>local classes</dfn>, <dfn>anonymous classes</dfn>, <dfn>inner classes</dfn>
 *   and <dfn>static classes</dfn>. These terms don't however relate as, at least we, would expect.</p>
 * <h4>According to the Java Language Specification</h4>
 * <p>According to the <cite><a href="http://java.sun.com/docs/books/jls/third_edition/html/">Java Language Specification</a></cite>,
 *   <dfn>Nested types</dfn> are either <dfn>member types</dfn> of
 *   an enclosing class, or <dfn>local classes</dfn> or <dfn>anonymous
 *   classes</dfn>, i.e., classes defined inside an enclosing method or
 *   code block, resp. with or without a name. Types defined inside an
 *   enclosing block can never be interfaces. Nested interfaces can only
 *   be member types.</p>
 * <p><dfn>Inner classes</dfn> are nested classes (member, local or anonymous)
 *   that have an <dfn>enclosing instance</dfn> or an <dfn>enclosing block</dfn>,
 *   i.e., there is a context in which variables, parameters or methods can be
 *   defined, which can be referenced from within the inner class. <dfn>static
 *   classes</dfn> are either member classes that are defined to be static or
 *   <dfn>top level classes</dfn>.</p>
 * <p>These definitions come from the following sources:</p>
 * <blockquote>
 *   <p>A nested class is any class whose declaration occurs within the body of
 *     another class or interface. A top level class is a class that is not a
 *     nested class.</p>
 *   <p>[...]</p>
 *   <p>Member class declarations (paragraph 8.5) describe nested classes that are members
 *     of the surrounding class. Member classes may be static, in which case they
 *     have no access to the instance variables of the surrounding class; or they
 *     may be inner classes (paragraph 8.1.3).<p>
 *   <p>Member interface declarations (paragraph 8.5) describe nested interfaces that are
 *     members of the surrounding class.</p>
 *   <p>[...]</p>
 *   <p>Inner classes include local (paragraph 14.3), anonymous (paragraph 15.9.5) and
 *     non-static member classes (paragraph 8.5).</p>
 *   <p>[...]</p>
 *   <p>Member interfaces (paragraph 8.5) are always implicitly static so they are never
 *     considered to be inner classes.</p>
 *   <p>[...]</p>
 *   <p>Nested enum types are implicitly static.</p>
 *   <cite><a href="http://java.sun.com/docs/books/jls/third_edition/html/classes.html">Java Language Specification, Chapter 5</a></cite>
 * </blockquote>
 * <blockquote>
 *   <p>A nested interface is any interface whose declaration occurs within the
 *   body of another class or interface. A top-level interface is an interface that
 *   is not a nested interface.</p>
 *   <cite><a href="http://java.sun.com/docs/books/jls/third_edition/html/interfaces.html">Java Language Specification, Chapter 9</a></cite>
 * </blockquote>
 * <blockquote>
 *   <p>A local class is a nested class (paragraph 8) that is not a member of any class and
 *     that has a name. All local classes are inner classes (paragraph 8.1.3). Every local
 *     class declaration statement is immediately contained by a block.</p>
 *   <cite><a href="http://java.sun.com/docs/books/jls/third_edition/html/statements.html">Java Language Specification, Chapter 14</a></cite>
 * </blockquote>
 * <blockquote>
 *   <p>An anonymous class is always an inner class (paragraph 8.1.3); it is never static
 *     (paragraph 8.1.1, paragraph 8.5.2).</p>
 *   <cite><a href="http://java.sun.com/docs/books/jls/third_edition/html/expressions.html">Java Language Specification, Chapter 15</a></cite>
 * </blockquote>
 * <p>The following table is a synopsis of the relation of the terms concerning top level and nested types:</p>
 * <table>
 *   <tr>
 *     <td></td>
 *     <th rowspan="3">top level type</th>
 *     <th colspan="3">nested type</th>
 *   </tr>
 *   <tr>
 *     <td></td>
 *     <th rowspan="2">member type</th>
 *     <th colspan="2"><em>type in a code block</em></th>
 *   </tr>
 *   <tr>
 *     <td></td>
 *     <th>local type</th>
 *     <th>anonymous type</th>
 *   </tr>
 *   <tr>
 *     <th>inner type</th>
 *     <td align="center">-</td>
 *     <td align="center">x</td>
 *     <td align="center">x</td>
 *     <td align="center">x</td>
 *   </tr>
 *   <tr>
 *     <th>static types</th>
 *     <td align="center">x</td>
 *     <td align="center">x</td>
 *     <td align="center">-</td>
 *     <td align="center">-</td>
 *   </tr>
 * </table>
 * <h4>In the context of this class</h4>
 * <p>In the context of the code in this class, <dfn>types in a code block</dfn> are considered irrelevant:
 *   whether or not the type is a local type or an anonyouns class, there is no practical need to
 *   load the types in outside code in the type of programs this libary addresses.</p>
 * <p>So, for all practical purposes, we can speak in this class about:</p>
 * <ul>
 *   <li><dfn>top level types</dfn></li>,
 *   <li><dfn>nested types</dfn in general
 *     <ul>
 *       <li><dfn>inner types</dfn>, <dfn>non-static nested types</dfn> or <dfn>dynamic nested types</dfn>
 *         (nested classes of which the instances have an outer instance)</li>
 *       <li><dfn>static nested types</dfn> (nested classes of which the instances do not have an outer
 *         instance), and finally</li>
 *       <li><dfn>enclosing types</dfn>, i.e., the types in which nested types are nested, and the
 *         <dfn>immediately enclosing type</dfn>, i.e., the type in which a nested type is nested directly,
 *         without further enclosing types inbetween.</li>
 *     </ul>
 *   </li>
 * </ul>
 * <p>Note that {@link Class} has methods to decided whether a type is a
 *   {@link Class#isMemberClass() <dfn>member class</dfn>}, a {@link Class#isLocalClass() <dfn>local class</dfn>}
 *   or an {@link Class#isAnonymousClass() <dfn>anonymous class</dfn>}, but not whether it is an <dfn>inner type<dfn>
 *   or a <dfn>static nested type</dfn>, or a <dfn>top level type</dfn> or <dfn>nested type</dfn>. Methods to test
 *   that are provide here.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TypeHelpers {

  private TypeHelpers() {
    // NOP
  }

  /**
   * A map of the binary names of all primitive types.
   */
  @Invars(
    @Expression("PRIMITIVE_TYPES.values == " +
      "Set{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE}")
  )
  private final static Map<Class<?>, String> PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE = new HashMap<Class<?>, String>(8);
  static {
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Boolean.TYPE, "Z");
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Byte.TYPE, "B");
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Character.TYPE, "C");
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Double.TYPE, "D");
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Float.TYPE, "F");
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Integer.TYPE, "I");
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Long.TYPE, "J");
    PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.put(Short.TYPE, "S");
  }

  /**
   * A map of the binary names of all primitive types.
   */
  @Invars(
    @Expression("PRIMITIVE_TYPE_BINARY_NAMES.keySet == PRIMITIVE_TYPES")
  )
  public final static Map<Class<?>, String> PRIMITIVE_TYPE_BINARY_NAMES =
      Collections.unmodifiableMap(PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE);

  /**
   * All Java primitive types.
   */
  @Invars(
    @Expression("PRIMITIVE_TYPES == " +
      "Set{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE}")
  )
  public final static Set<Class<?>> PRIMITIVE_TYPES = Collections.unmodifiableSet(PRIMITIVE_TYPE_BINARY_NAMES_PRIVATE.keySet());

  /**
   * A map of all primitive types, with their simple name as key.
   */
  @Invars({
    @Expression("for (Class<?> primitiveType : PRIMITIVE_TYPES_BY_NAME_INTERNAL) {" +
                  "PRIMITIVE_TYPES_BY_NAME_INTERNAL[primitiveType.simpleName] == primitiveType" +
                 "}"),
    @Expression("PRIMITIVE_TYPES_BY_NAME_INTERNAL.values == PRIMITIVE_TYPES")
  })
  private final static Map<String, Class<?>> PRIMITIVE_TYPES_BY_NAME_INTERNAL = new HashMap<String, Class<?>>(8);
  static {
    for (Class<?> primitiveType : PRIMITIVE_TYPES) {
      PRIMITIVE_TYPES_BY_NAME_INTERNAL.put(primitiveType.getSimpleName(), primitiveType);
    }
  }

  /**
   * A map of all primitive types, with their simple name as key.
   */
  @Invars({
    @Expression("for (Class<?> primitiveType : PRIMITIVE_TYPES) {" +
                  "PRIMITIVE_TYPES_BY_NAME[primitiveType.simpleName] == primitiveType" +
                 "}"),
    @Expression("PRIMITIVE_TYPES.values == PRIMITIVE_TYPES")
  })
  public final static Map<String, Class<?>> PRIMITIVE_TYPES_BY_NAME = Collections.unmodifiableMap(PRIMITIVE_TYPES_BY_NAME_INTERNAL);

  /**
   * The idiom for finding out whether a type {@code t} is public or not,
   * using the standard Java API, is
   * {@link Modifier#isPublic(int) Modifier.isPublic(}{@link Method#getModifiers() t.getModifiers()}{@code )}.
   * This shortens that a bit to {@code MethodHelpers.isPublic(t)}.
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("Modifier.isPublic(_t.getModifiers())")
  )
  public static boolean isPublic(Class<?> t) {
    assert preArgumentNotNull(t, "method");
    return Modifier.isPublic(t.getModifiers());
  }

  /**
   * The idiom for finding out whether a type {@code t} is protected or not,
   * using the standard Java API, is
   * {@link Modifier#isProtected(int) Modifier.isProtected(}{@link Class#getModifiers() t.getModifiers()}{@code )}.
   * This shortens that a bit to {@code TypeHelpers.isProtected(t)}.
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("Modifier.isProtected(_t.getModifiers())")
  )
  public static boolean isProtected(Class<?> t) {
    assert preArgumentNotNull(t, "t");
    return Modifier.isProtected(t.getModifiers());
  }

  /**
   * The idiom for finding out whether a type {@code t} is private or not,
   * using the standard Java API, is
   * {@link Modifier#isPrivate(int) Modifier.isPrivate(}{@link Class#getModifiers() t.getModifiers()}{@code )}.
   * This shortens that a bit to {@code TypeHelpers.isPrivate(t)}.
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("Modifier.isPrivate(_t.getModifiers())")
  )
  public static boolean isPrivate(Class<?> t) {
    assert preArgumentNotNull(t, "t");
    return Modifier.isPrivate(t.getModifiers());
  }

  /**
   * The idiom for finding out whether a type {@code t} is package accessible or not,
   * using the standard Java API, is difficult, since there is no modifier for package accessibility.
   * We need to check there there is no accessibility modifier present.
   * This shortens that a bit to {@code TypeHelpers.isPackageAccessible(t)}.
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("! Modifier.isPublic(_t.getModifiers()) && " +
                       "! Modifier.isProtected(_t.getModifiers()) && " +
                       "! Modifier.isPrivate(_t.getModifiers())")
  )
  public static boolean isPackageAccessible(Class<?> t) {
    assert preArgumentNotNull(t, "t");
    int tModifiers = t.getModifiers();
    return ! Modifier.isPublic(tModifiers) && ! Modifier.isProtected(tModifiers) && ! Modifier.isPrivate(tModifiers);
  }

  /**
   * The idiom for finding out whether a type {@code t} is static (top level types always are) or not,
   * using the standard Java API, is
   * {@link Modifier#isStatic(int) Modifier.isStatic(}{@link Class#getModifiers() t.getModifiers()}{@code )} for nested
   * types.
   * This shortens that a bit to {@code TypeHelpers.isStatic(t)}.
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("isTopLevelClass(_t) || Modifier.isStatic(_t.getModifiers())")
  )
  public static boolean isStatic(Class<?> t) {
    assert preArgumentNotNull(t, "t");
    return isTopLevelType(t) || Modifier.isStatic(t.getModifiers());
  }

  /**
   * The idiom for finding out whether a type {@code t} is abstract (interfaces always are) or not,
   * using the standard Java API, is
   * {@link Modifier#isAbstract(int) Modifier.isAbstract(}{@link Class#getModifiers() t.getModifiers()}{@code )}
   * for non-interface types.
   * This shortens that a bit to {@code TypeHelpers.isAbstract(t)}.
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("Modifier.isInterface(_t.getModifiers()) || Modifier.isAbstract(_t.getModifiers())")
  )
  public static boolean isAbstract(Class<?> t) {
    assert preArgumentNotNull(t, "t");
    return t.isInterface() || Modifier.isAbstract(t.getModifiers());
  }

  /**
   * The idiom for finding out whether a type {@code t} is final (interfaces never are) or not,
   * using the standard Java API, is
   * {@link Modifier#isFinal(int) Modifier.isFinal(}{@link Class#getModifiers() t.getModifiers()}{@code )}.
   * This shortens that a bit to {@code TypeHelpers.isFinal(t)}.
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("Modifier.isFinal(_t.getModifiers())")
  )
  public static boolean isFinal(Class<?> t) {
    assert preArgumentNotNull(t, "t");
    return Modifier.isFinal(t.getModifiers());
  }

  /**
   * <p>Is {@code type} an <dfn>inner class</dfn> or not?</p>
   * <p>The type {@link Class} has methods to find out whether the class is an
   *   {@link Class#isAnonymousClass() <dfn>anonymous class</dfn>} or not,
   *   is a {@link Class#isLocalClass() <dfn>local class</dfn>} or not, and
   *   whether it is a {@link Class#isMemberClass() <dfn>member class</dfn>}
   *   or not. It lacks however methods to know whether the class is an
   *   <dfn>inner class</dfn> or not.</p>
   * <p>For a discussion, see <a href="#onNestedClasses">On nested classes</a>
   *   above.</p>
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("_t.isLocalClass() || _t.isAnonymousClass() || " +
                       "(_t.isMemberClass() && (! isStatic(_t)))")
  )
  public static boolean isInnerType(Class<?> t) {
    assert t != null;
    return t.isLocalClass() || t.isAnonymousClass() || (t.isMemberClass() && (! Modifier.isStatic(t.getModifiers())));
  }

  /**
   * <p>Is {@code type} a top level class or not?</p>
   * <p>The type {@link Class} has methods to find out whether the class is an
   *   {@link Class#isAnonymousClass() <dfn>anonymous class</dfn>} or not,
   *   is a {@link Class#isLocalClass() <dfn>local class</dfn>} or not, and
   *   whether it is a {@link Class#isMemberClass() <dfn>member class</dfn>}
   *   or not. It lacks however methods to know whether the class is a
   *   <dfn>top level class</dfn> or a nested class.</p>
   * <p>For a discussion, see <a href="#onNestedClasses">On nested classes</a>
   *   above.</p>
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("_t.enclosingClass == null")
  )
  public static boolean isTopLevelType(Class<?> type) {
    assert type != null;
    return type.getEnclosingClass() == null;
  }

  /**
   * <p>{@link Class#forName(String)} with a simpler exception model.</p>
   * <p>This method also works for primitive types, and has an embedded &quot;import&quot; for the package
   *   {@code java.lang}.</p>
   * <p>This method handles member types with the dot notation (where {@link Class#forName(String)} requires
   *   <dfn>binary</dfn> &quot;$&quot; separation for member types).</p>
   *
   * @mudo This method should also support array type with the &quot;[]&quot; notation.
   * @mudo We need a method like this, that does it best, but doesn't fail if such a class does not exist
   */
  @MethodContract(
    pre  = {
      @Expression("fqtn != null && fqtn != EMPTY"),
      @Expression(value = "true", description = "type with canonical name fqtn exists in the classpath")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.canonicalName == _fqtn")
    }
  )
  public static <_Class_> Class<_Class_> type(String fqtn) {
    preArgumentNotEmpty(fqtn, "fqtn");
    Class<?> result = PRIMITIVE_TYPES_BY_NAME.get(fqtn);
    if (result == null) {
      try {
        try {
          result = Class.forName(fqtn);
        }
        catch (ClassNotFoundException cnfExc) {
          if (! fqtn.contains(".")) {
            // there are no member classes in java.lang, are there?
            try {
              result = Class.forName("java.lang." + fqtn);
            }
            catch (ClassNotFoundException cnfExc2) {
              unexpectedException(cnfExc2, "Since the name of the type we look for (\"" + fqtn + "\") does not contain " +
                  "a dot, and was not found in the unnamed packagewe, we assumed the class you intend to load should be " +
                  "sought in java.lang, but we didn't find it their either.");
            }
          }
          else { // let's try for member classes
            // from right to left, replace "." with "$"
            String[] names = fqtn.split("\\."); // regex
            for (int i = names.length - 2; i >= 0; i--) {
              StringBuffer build = new StringBuffer();
              for (int j = 0; j < names.length; j++) {
                build.append(names[j]);
                if (j < names.length - 1) {
                  build.append((j < i) ? "." : "$");
                }
              }
              String tryThis = build.toString();
              try {
                result = Class.forName(tryThis);
                break;
              }
              catch (ClassNotFoundException cnfExc2) {
                // NOP; try with i--
              }
            }
            if (result == null) { // if we get here without a result, we finally give up
              throw new AssertionError("cannot find type with canonical name \"" + fqtn + "\"");
            }
          }
        }
      }
      catch (LinkageError lErr) {
        unexpectedException(lErr);
      }
    }
    try {
      @SuppressWarnings("unchecked")
      Class<_Class_> typedResult = (Class<_Class_>)result;
      return typedResult;
    }
    catch (ClassCastException ccExc) {
      unexpectedException(ccExc, "returned class is not of expected type");
      return null; // keep compiler happy
    }
  }

  /**
   * Instantiate an object of a type
   * <code>prefixedFqcn(prefix, fqtn)</code>.
   *
   * @param cl
   *        The class-loader from which we should create
   *        the bean. If this is null, then the system class-loader
   *        is used.
   * @param prefix
   *        The prefix to add before the type name.
   * @param fqtn
   *        The original fully qualified type name to derive
   *        the prefixed type name from.
   *
   * @mudo this method needs to be moved; split into just an exception vernacular "instantiate" method; we probably no longer need this than
   */
  public static Object instantiatePrefixed(ClassLoader cl, final String prefix, final String fqtn) {
    preArgumentNotNull(prefix, "prefix");
    preArgumentNotEmpty(fqtn, "fqtn");
    TypeName n = new TypeName(fqtn);
    TypeName prefixedN = new TypeName(n.getPackageName(), n.getEnclosingTypeNames(), prefix + n.getSimpleName());
    String prefixedFqcn = prefixedN.toString();
    Object result = null;
    try {
      result = Beans.instantiate(cl, prefixedFqcn);
    }
    catch (ClassNotFoundException cnfExc) {
      unexpectedException(cnfExc, "class \"" + prefixedFqcn + "\" not found in classpath");
    }
    catch (IOException ioExc) {
      unexpectedException(ioExc);
    }
    return result;
  }

  /**
   * Introduced to keep compiler happy in getting array type, while
   * discarding impossible exceptions.
   *
   * @pre componentType != null;
   * @return Class.forName(componentType.getName() + "[]");
   */
  @MethodContract(
    pre  = @Expression("_componentType != null"),
    post = {
      @Expression("result != null"),
      @Expression("result.isArray()"),
      @Expression("result.componentType == _componentType")
    }
  )
  public static <_ArrayComponentType_> Class<_ArrayComponentType_[]> arrayType(Class<_ArrayComponentType_> componentType) {
    preArgumentNotNull(componentType, "componentType");
    int dimensions = 1;
    Class<?> ultimateComponentType = componentType;
    while (ultimateComponentType.isArray()) {
      dimensions++;
      ultimateComponentType = ultimateComponentType.getComponentType();
    }
    // base name
    String arrayFqcn = PRIMITIVE_TYPE_BINARY_NAMES.get(ultimateComponentType);
    if (arrayFqcn == null) {
      // object type
      arrayFqcn = "L" + ultimateComponentType.getName() + ";";
    }
    // add dimensions
    for (int i = 0; i < dimensions; i++) {
      arrayFqcn = "[" + arrayFqcn;
    }
    try {
      @SuppressWarnings("unchecked")
      Class<_ArrayComponentType_[]> result = (Class<_ArrayComponentType_[]>)Class.forName(arrayFqcn);
      return result;
    }
    /* exceptions cannot happen, since componentType was already
       laoded before this call */
    catch (ExceptionInInitializerError eiiErr) {
      unexpectedException(eiiErr, "componentType was already loaded before this call");
    }
    catch (LinkageError lErr) {
      unexpectedException(lErr, "componentType was already loaded before this call");
    }
    catch (ClassNotFoundException cnfExc) {
      unexpectedException(cnfExc, "componentType was already loaded before this call");
    }
    return null;
  }

  @MethodContract(pre  = {@Expression("_type != null"),
                          @Expression("! _type.isArray()")},
                  post = @Expression("result == _type.interfaces U (_type.superClass == null ? {} : {_type.superClass})"))
  public static <_Type_> Set<Class<? super _Type_>> directSuperTypes(Class<_Type_> type) {
    preArgumentNotNull(type, "type");
    pre(! type.isArray());
    HashSet<Class<? super _Type_>> result = new HashSet<Class<? super _Type_>>();
    Class<? super _Type_> superClass = type.getSuperclass(); // null with primitive types and Object, Object with arrays, interface
    if (superClass != null) {
      result.add(superClass);
    }
    Class<?>[] superInterfacesRaw = type.getInterfaces();
    @SuppressWarnings("unchecked")
    Class<? super _Type_>[] superInterfaces = (Class<? super _Type_>[])superInterfacesRaw;
    for (Class<? super _Type_> i : superInterfaces) {
      result.add(i);
    }
    return result;
  }


  @MethodContract(pre  = {@Expression("_type != null"),
                          @Expression("! _type.isArray()")},
                  post = @Expression("result == directSuperTypes(_type) U union(Class<?> superType : directSuperType(_type)) {superTypes(superType)}"))
  public static <_Type_> Set<Class<? super _Type_>> superTypes(Class<_Type_> type) {
    preArgumentNotNull(type, "type");
    pre(! type.isArray());
    LinkedList<Class<? super _Type_>> result = new LinkedList<Class<? super _Type_>>();
    result.add(type);
    for (int i = 0; i < result.size(); i++) {
      Class<? super _Type_> t = result.get(i);
      Set<? extends Class<? super _Type_>> tSupers = directSuperTypes(t);
      for (Class<? super _Type_> tSuper : tSupers) {
        if (! result.contains(tSuper)) {
          result.add(tSuper);
        }
      }
    }
    return new HashSet<Class<? super _Type_>>(result.subList(1, result.size()));
  }


}
