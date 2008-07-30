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

import java.beans.Beans;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;


/**
 * <p>Utility methods for type reflection . Use these methods if you are interested in the result of reflection,
 *   and not in a particular reason why some reflective inspection might have failed. The ppwcode exception
 *   vernacular is applied.</p>
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
 *   <p>Member class declarations (§8.5) describe nested classes that are members
 *     of the surrounding class. Member classes may be static, in which case they
 *     have no access to the instance variables of the surrounding class; or they
 *     may be inner classes (§8.1.3).<p>
 *   <p>Member interface declarations (§8.5) describe nested interfaces that are
 *     members of the surrounding class.</p>
 *   <p>[...]</p>
 *   <p>Inner classes include local (§14.3), anonymous (§15.9.5) and
 *     non-static member classes (§8.5).</p>
 *   <p>[...]</p>
 *   <p>Member interfaces (§8.5) are always implicitly static so they are never
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
 *   <p>A local class is a nested class (§8) that is not a member of any class and
 *     that has a name. All local classes are inner classes (§8.1.3). Every local
 *     class declaration statement is immediately contained by a block.</p>
 *   <cite><a href="http://java.sun.com/docs/books/jls/third_edition/html/statements.html">Java Language Specification, Chapter 14</a></cite>
 * </blockquote>
 * <blockquote>
 *   <p>An anonymous class is always an inner class (§8.1.3); it is never static
 *     (§8.1.1, §8.5.2).</p>
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
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class ClassHelpers {

  private ClassHelpers() {
    // NOP
  }

  /**
   * All Java primitive types.
   */
  @Invars(
    @Expression("PRIMITIVE_TYPES == " +
      "Set{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE}")
  )
  private final static Set<Class<?>> PRIMITIVE_TYPES_INTERNAL = new HashSet<Class<?>>(8);
  static {
    PRIMITIVE_TYPES_INTERNAL.add(Boolean.TYPE);
    PRIMITIVE_TYPES_INTERNAL.add(Byte.TYPE);
    PRIMITIVE_TYPES_INTERNAL.add(Character.TYPE);
    PRIMITIVE_TYPES_INTERNAL.add(Short.TYPE);
    PRIMITIVE_TYPES_INTERNAL.add(Integer.TYPE);
    PRIMITIVE_TYPES_INTERNAL.add(Long.TYPE);
    PRIMITIVE_TYPES_INTERNAL.add(Float.TYPE);
    PRIMITIVE_TYPES_INTERNAL.add(Double.TYPE);
  }

  /**
   * All Java primitive types.
   */
  @Invars(
    @Expression("PRIMITIVE_TYPES == " +
      "Set{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE}")
  )
  public final static Set<Class<?>> PRIMITIVE_TYPES = Collections.unmodifiableSet(PRIMITIVE_TYPES_INTERNAL);

  /**
   * A map of all primitive types, with their simple name as key.
   */
  @Invars({
    @Expression("for (Class<?> primitiveType : PRIMITIVE_TYPES_MAP_INTERNAL) {" +
                  "PRIMITIVE_TYPES_MAP_INTERNAL[primitiveType.simpleName] == primitiveType" +
                 "}"),
    @Expression("PRIMITIVE_TYPES_MAP_INTERNAL.values == PRIMITIVE_TYPES")
  })
  private final static Map<String, Class<?>> PRIMITIVE_TYPES_MAP_INTERNAL = new HashMap<String, Class<?>>(8);
  static {
    for (Class<?> primitiveType : PRIMITIVE_TYPES) {
      PRIMITIVE_TYPES_MAP_INTERNAL.put(primitiveType.getSimpleName(), primitiveType);
    }
  }

  /**
   * A map of all primitive types, with their simple name as key.
   */
  @Invars({
    @Expression("for (Class<?> primitiveType : PRIMITIVE_TYPES) {" +
                  "PRIMITIVE_TYPES_MAP[primitiveType.simpleName] == primitiveType" +
                 "}"),
    @Expression("PRIMITIVE_TYPES.values == PRIMITIVE_TYPES")
  })
  public final static Map<String, Class<?>> PRIMITIVE_TYPES_MAP = Collections.unmodifiableMap(PRIMITIVE_TYPES_MAP_INTERNAL);

  /**
   * <p>{@link Class#forName(String)} with a simpler exception model.</p>
   * <p>This method also works for primitive types, and has an embedded &quot;import&quot; for the package
   *   {@code java.lang}.</p>
   * <p>This method handles member types with the dot notation (where {@link Class#forName(String)} requires
   *   <dfn>binary</dfn> &quot;$&quot; separation for member types).</p>
   *
   * @result result != null
   * @result "boolean".equals(fqn) ?? result == Boolean.TYPE;
   * @result "byte".equals(fqn) ?? result == Byte.TYPE;
   * @result "char".equals(fqn) ?? result == Character.TYPE;
   * @result "short".equals(fqn) ?? result == Short.TYPE;
   * @result "int".equals(fqn) ?? result == Integer.TYPE;
   * @result "long".equals(fqn) ?? result == Long.TYPE;
   * @result "float".equals(fqn) ?? result == Float.TYPE;
   * @result "double".equals(fqn) ?? result == Double.TYPE;
   * @result (! "boolean".equals(fqn)) && (! "byte".equals(fqn)) &&
   *           (! "char".equals(fqn)) && (! "short".equals(fqn)) &&
   *           (! "int".equals(fqn)) && (! "long".equals(fqn)) &&
   *           (! "float".equals(fqn)) && (! "double".equals(fqn)) ?
   *         (result = Class.forName(fqn)) || (result == Class.forName("java.lang." + fqn);
   * @throws _CannotGetClassException
   *         fqn == null;
   * @throws _CannotGetClassException
   *         (! "boolean".equals(fqn)) && (! "byte".equals(fqn)) &&
   *           (! "char".equals(fqn)) && (! "short".equals(fqn)) &&
   *           (! "int".equals(fqn)) && (! "long".equals(fqn)) &&
   *           (! "float".equals(fqn)) && (! "double".equals(fqn)) ?
   *         Class.forName(fqn) throws && Class.forName("java.lang." + fqn) throws;
   *
   * @mudo This method should also support array type with the &quot;[]&quot; notation.
   */
  public static Class<?> loadForName(String fqn) throws _CannotGetClassException {
    if (fqn == null) {
      throw new _CannotGetClassException(fqn, new NullPointerException("fqn is null"));
    }
    Class<?> primitiveType = PRIMITIVE_TYPES_MAP.get(fqn);
    if (primitiveType != null) {
      return primitiveType;
    }
    try {
      try {
        return Class.forName(fqn);
      }
      catch (ClassNotFoundException cnfExc) {
        if (! fqn.contains(".")) {
          // there are no member classes in java.lang, are there?
          try {
            return Class.forName("java.lang." + fqn);
          }
          catch (ClassNotFoundException cnfExc2) {
            throw new _CannotGetClassException(fqn, cnfExc2);
          }
        }
        else { // let's try for member classes
          // from right to left, replace "." with "$"
          String[] names = fqn.split("\\."); // regex
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
              return Class.forName(tryThis);
            }
            catch (ClassNotFoundException cnfExc2) {
              // NOP; try with i--
            }
          }
          // if we get here, we finally give up
          throw new _CannotGetClassException(fqn, null);
        }
      }
    }
    catch (LinkageError lErr) {
      // also catches ExceptionInInitializerError
      throw new _CannotGetClassException(fqn, lErr);
    }
  }

  /**
   * Return a fully qualified class name that is in the same package
   * as <code>fqcn</code>, and has as class name
   * <code>prefix + <var>ClassName</var></code>.
   *
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The fully qualified class name to start from.
   * @throws NullPointerException
   *         (prefix == null) || (fqcn == null);
   */
  public static String prefixedFqcn(final String prefix,
                                    final String fqcn)
  throws NullPointerException {
    String[] parts = fqcn.split(ClassHelpers.PREFIXED_FQCN_PATTERN);
    String prefixedName = prefix + parts[parts.length - 1];
    String result = ClassHelpers.EMPTY;
    for (int i =0; i < parts.length - 1; i++) {
      result = result + parts[i] + ClassHelpers.DOT;
    }
    result = result + prefixedName;
    return result;
  }

  private final static String PREFIXED_FQCN_PATTERN = "\\.";

  private final static String EMPTY = "";

  private final static String DOT = ".";

  /**
   * Load the class with name
   * <code>prefixedFqcn(prefix, fqcn)</code>.
   *
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The original fully qualified class name to derive
   *        the prefixed class name from.
   * @throws ClassNotFoundException
   *         true;
   */
  public static Class<?> loadPrefixedClass(final String prefix,
                                           final String fqcn)
      throws ClassNotFoundException {
    return Class.forName(prefixedFqcn(prefix, fqcn));
  }

  /**
   * Instantiate an object of a type
   * <code>prefixedFqcn(prefix, fqcn)</code>.
   *
   * @param cl
   *        The class-loader from which we should create
   *        the bean. If this is null, then the system class-loader
   *        is used.
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The original fully qualified class name to derive
   *        the prefixed class name from.
   * @throws _CannotCreateInstanceException
   */
  public static Object instantiatePrefixed(ClassLoader cl,
                                           final String prefix,
                                           final String fqcn)
  throws _CannotCreateInstanceException {
    try {
      String prefixedFqcn = prefixedFqcn(prefix, fqcn);
      try {
        Object result = Beans.instantiate(cl, prefixedFqcn);
        return result;
      }
      catch (ClassNotFoundException cnfExc) {
        throw new _CannotCreateInstanceException(prefixedFqcn, cnfExc);
      }
      catch (IOException ioExc) {
        throw new _CannotCreateInstanceException(prefixedFqcn, ioExc);
      }
    }
    catch (NullPointerException npExc) {
      throw new _CannotCreateInstanceException(prefix + "/" + fqcn, npExc);
    }
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
   *
   * @pre type != null;
   * @return type.isLocalClass() || type.isAnonymousClass() ||
   *         (type.isMemberClass() && (! Modifier.isStatic(type.getModifiers())));
   */
  public static boolean isInnerClass(Class<?> type) {
    assert type != null;
    return type.isLocalClass() ||
    type.isAnonymousClass() ||
    (type.isMemberClass() && (! Modifier.isStatic(type.getModifiers())));
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
   *
   * @pre type != null;
   * @return getEnclosingClass() == null;
   */
  public static boolean isTopLevelClass(Class<?> type) {
    assert type != null;
    return type.getEnclosingClass() == null;
  }

  /**
   * Introduced to keep compiler happy in getting array type, while
   * discarding impossible exceptions.
   *
   * @pre componentType != null;
   * @return Class.forName(componentType.getName() + "[]");
   */
  public static <_ArrayBase_> Class<_ArrayBase_[]> arrayClassForName(Class<_ArrayBase_> componentType) {
    assert componentType != null;
    String arrayFqcn = "[L" + componentType.getName() + ";";
    try {
      @SuppressWarnings("unchecked") Class<_ArrayBase_[]> result =
          (Class<_ArrayBase_[]>)Class.forName(arrayFqcn);
      return result;
    }
    /* exceptions cannot happen, since componentType was already
       laoded before this call */
    catch (ExceptionInInitializerError eiiErr) {
      assert false : "cannot happen";
    }
    catch (LinkageError lErr) {
      assert false : "cannot happen";
    }
    catch (ClassNotFoundException cnfExc) {
      assert false : "cannot happen";
    }
    return null;
  }

}
