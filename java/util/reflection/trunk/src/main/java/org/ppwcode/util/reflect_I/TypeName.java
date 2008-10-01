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


import static java.lang.Character.isLowerCase;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.CloneHelpers.klone;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotEmpty;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.util.LinkedList;
import java.util.List;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Structured parse of a type name, containing the {@link #getPackageName() package name}, the
 *   {@link #getSimpleName simple type name}, and the possible in between {@link #getEnclosingTypeNames()
 *   enclosing types}. The classes encountered are not loaded: this is a pure String operation..</p>
 * <p>This code works with {@link Class#getCanonicalName()} <dfn>canonical names</dfn>}, i.e., the
 *   names of types as they are used in source code, whereas methods in {@link Class} generally work with
 *   {@link Class#getName() <dfn>binary names</dfn>}.</p>
 * <p>The ppwcode exception vernacular is applied.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("packageName != null"),

  @Expression("enclosingTypeNames != null"),
  @Expression("! enclosingTypeNames.contains(null)"),
  @Expression("for (TypeName tn : enclosingTypeNames) {tn.packageName == packageName}"),
  @Expression("for (TypeName tn : enclosingTypeNames) {in enclosing of later}"),
  // MUDO contract for enclosing types; is tested in enclosingRegression (probably not possible to write in logic)
  @Expression("simpleName != null"),
  @Expression("simpleName != EMPTY")
})
public final class TypeName {

  /**
   * <p>The empty String.</p>
   * <p><code>EMPTY = &quot;{@value}&quot;</code></p>
   */
  public final static String EMPTY = "";

  /**
   * <p>The dot.</p>
   * <p><code>DOT = &quot;{@value}&quot;</code></p>
   */
  public final static String DOT = ".";

  /**
   * <p>Grep pattern to find dot.</p>
   * <p><code>DOT_PATTERN = &quot;{@value}&quot;</code></p>
   */
  public final static String DOT_PATTERN = "\\.";


  /*<construction>*/
  //-------------------------------------------------------------------------

  /**
   * <p>Create an instance, from a fully qualified type name, using heuristics to decide which parts
   *   are package name, simple name, and inbetween enclosing types.</p>
   * <p>As {@link #getPackageName() package name} is considered the front part of {@code fqtn}, up until the first
   *   dot-separated part that <em>does not start with a lower case letter</em>. This builds on the pattern that
   *   package names should be all lower case, and type names should start with a capital (leaving room for type
   *   names that start with diacritical marks, as sometimes is used).</p>
   * <p>The {@link #getSimpleName() simple name} is the last part of the {@code fqtn}, after the last dot.</p>
   * <p>Everything in between are {@link #getEnclosingTypeNames() enclosing types}.
   * <p>The package name can be empty for types in the default package, and primitive types.</p>
   *
   * @param fqtn
   *        The fully qualified type name, from which we create this object.
   */
  @MethodContract(
    pre  = @Expression(value = "true", description = "fqtn is a well-formed Java fully qualified type name"),
    post = {
      @Expression("_fqtn.startsWith(packageName)"),
      @Expression("for (String part : packageName.split(DOT_PATTERN)) {" +
                    "part == EMPTY || isLowerCase(part.charAt(0))" +
                  "}"),
      @Expression("packageName != EMPTY ? _fqtn.charAt(packageName.length) == DOT"),
      @Expression("packageName != EMPTY ? ! isLowerCase(_fqtn.charAt(packageName.length + 1))"),

      // MUDO enclosing type names

      @Expression("simplename == _fqtn.split(DOT_PATTERN)[_fqtn.split(DOT_PATTERN).length - 1]")
    }
  )
  public TypeName(String fqtn) {
    preArgumentNotEmpty(fqtn, "fqtn");
    String packageName = EMPTY;
    String simpleName = null;
    $enclosing = new LinkedList<TypeName>();
    if (TypeHelpers.PRIMITIVE_TYPES_BY_NAME.containsKey(fqtn)) {
      // fqtn is the canonical name of a primitive type
      simpleName = fqtn;
      // packageName stays empty
      // enclosing stays empty
    }
    String[] parts = fqtn.split(DOT_PATTERN);
    boolean packageNameDone = false;
    StringBuilder packageNameBuilder = new StringBuilder();
    try {
      for (int i = 0; i < parts.length; i++) {
        if ((! packageNameDone) && isLowerCase(parts[i].charAt(0))) {
          if (i > 0) {
            packageNameBuilder.append(DOT);
          }
          packageNameBuilder.append(parts[i]);
        }
        else {
          // found the start of the type's simple name (their might be nested classes following)
          // we're done with the package name
          packageNameDone = true;
          packageName = packageNameBuilder.toString();
          // the rest are type names, the last is our simple name
          if (i == parts.length - 1) {
            simpleName = parts[i];
          }
          else {
            // enclosing type
            $enclosing.addLast(new TypeName(packageName, klone($enclosing), parts[i]));
          }
        }
      }
    }
    catch (StringIndexOutOfBoundsException exc) {
      unexpectedException(exc);
    }
    $packageName = packageName;
    $simpleName = simpleName;
  }

  @MethodContract(
    pre  = {
      @Expression("_packageName != null"),
      @Expression(value = "true", description = "_packageName is a well-formed package name"),
      @Expression("_simpleName != null"),
      @Expression("_simpleName != EMPTY"),
      @Expression(value = "true", description = "_simpleName is a well-formed type name")
    },
    post = {
      @Expression("packageName == _packageName"),
      @Expression("enclosingTypeNames == _enclosing"),
      @Expression("simpleName == _simpleName")
    }
  )
  public TypeName(String packageName, List<TypeName> enclosing, String simpleName) {
    $packageName = packageName;
    $enclosing = new LinkedList<TypeName>(enclosing);
    $simpleName = simpleName;
  }

  @MethodContract(
    pre  = @Expression("_type != null"),
    post = {
      @Expression("packageName == _type.package != null ? _type.package.name : EMPTY"),
      // ENCLOSING TYPES
      @Expression("simpleName == _type.simpleName")
    }
  )
  public TypeName(Class<?> type) {
    preArgumentNotNull(type);
    $packageName = type.getPackage() != null ? type.getPackage().getName() : EMPTY;
    $simpleName = type.getSimpleName();
    $enclosing = new LinkedList<TypeName>();
    Class<?> enclosing = type.getEnclosingClass();
    while (enclosing != null) {
      $enclosing.addFirst(new TypeName(enclosing));
      enclosing = enclosing.getEnclosingClass();
    }
  }

  /*</construction>*/



  /*<property name="package name">*/
  //-------------------------------------------------------------------------

  @Basic
  public final String getPackageName() {
    return $packageName;
  }

  @Invars(@Expression("$packageName != null"))
  private final String $packageName;

  /*/<property>*/



  /*<property name="simple name">*/
  //-------------------------------------------------------------------------

  @Basic
  public final String getSimpleName() {
    return $simpleName;
  }

  @Invars({
    @Expression("$enclosing != null"),
    @Expression("$enclosing != EMPTY")
  })
  private final String $simpleName;

  /*/<property>*/



  /*<property name="enclosingTypeNames">*/
  //-------------------------------------------------------------------------

  @Basic
  public final List<TypeName> getEnclosingTypeNames() {
    return klone($enclosing);
  }

  @Invars({
    @Expression("$enclosing != null"),
    @Expression("! $enclosing.contains(null)"),
    @Expression("for (TypeName tn : $enclosing) {tn.packageName == packageName}")
  })
  private final LinkedList<TypeName> $enclosing;

  /*/<property>*/




  @Override
  @MethodContract(
    post = @Expression("_other != null && _other instanceof TypeName && packageName == _other.packageName && " +
                       "simpleName == _other.simpleName && enclosingTypeNames == _other.enclosingTypeNames")
  )
  public final boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (! (other instanceof TypeName)) {
      return false;
    }
    TypeName otherTn = (TypeName)other;
    return getPackageName().equals(otherTn.getPackageName()) &&
           getSimpleName().equals(otherTn.getSimpleName()) &&
           getEnclosingTypeNames().equals(otherTn.getEnclosingTypeNames());
   }

  @Override
  public final int hashCode() {
    int result = getPackageName().hashCode() + getSimpleName().hashCode();
    for (TypeName tn : getEnclosingTypeNames()) {
      result += tn.hashCode();
    }
    return result;
  }

  /**
   * The canonical name of the represented type.
   */
  @Override
  public final String toString() {
    StringBuilder result = new StringBuilder();
    result.append(getPackageName());
    if (! getPackageName().equals(EMPTY)) {
      result.append(DOT);
    }
    for (TypeName tn : getEnclosingTypeNames()) {
      result.append(tn.getSimpleName());
      result.append(DOT);
    }
    result.append(getSimpleName());
    return result.toString();
  }

  /**
   * Load the type (class) for which this is the parsed name.
   */
  @MethodContract(
    pre  = @Expression(value = "true", description = "a type with name toString() exists in the class path"),
    post = {
      @Expression("result != null"),
      @Expression("result.canonicalName == toString()")
    }
  )
  public final Class<?> getType() {
    return TypeHelpers.type(toString());
  }

}
