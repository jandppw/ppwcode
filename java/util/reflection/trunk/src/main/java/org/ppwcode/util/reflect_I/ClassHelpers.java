/*<license>
Copyright 2008 - $Date$ by Jan Dockx.

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


import java.beans.Beans;
import java.io.IOException;
import java.lang.reflect.Modifier;


/**
 * <p>Utility methods for reflection. Use these methods if you are
 *   interested in the result of reflection, and not in a particular
 *   reason why some reflective inspection might have failed.</p>
 *
 * <h3 id="onNestedClasses">On nested classes</h3>
 * <p><dfn>Nested types</dfn> are either <dfn>member types</dfn> of
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
 *
 * @idea (jand) most methods are also in ppw-bean; consolidate
 *
 * @note The methods of the class {@code Beans} of the previous version have been moved here and into {@link ResourceBundles}.
 *       Furthermore, a number of methods are removed.
 *
 * @author Jan Dockx
 *
 * @note partial copy from toryt_II_dev
 */
public class ClassHelpers {

  private ClassHelpers() {
    // NOP
  }

  /**
   * {@link Class#forName(String)} that has a simpler exception model, also
   * works for primitive types, and has an embedded &quot;import&quot; for
   * the package {@code java.lang}. This method also handles member types
   * with the dotnotation (where {@link Class#forName(String)} requires
   * &quot;$&quot; separation for member types).
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
   */
  public static Class<?> loadForName(String fqn) throws _CannotGetClassException {
    if (fqn == null) {
      throw new _CannotGetClassException(fqn, new NullPointerException("fqn is null"));
    }
    else if ("boolean".equals(fqn)) {
      return Boolean.TYPE;
    }
    else if ("byte".equals(fqn)) {
      return Byte.TYPE;
    }
    else if ("char".equals(fqn)) {
      return Character.TYPE;
    }
    else if ("short".equals(fqn)) {
      return Short.TYPE;
    }
    else if ("int".equals(fqn)) {
     return Integer.TYPE;
    }
    else if ("long".equals(fqn)) {
      return Long.TYPE;
    }
    else if ("float".equals(fqn)) {
      return Float.TYPE;
    }
    else if ("double".equals(fqn)) {
      return Double.TYPE;
    }
    else {
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