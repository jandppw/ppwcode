/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v.

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

package org.ppwcode.vernacular.semantics_VI.bean;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>A type that formalizes how to work with JavaBeans that represent
 *   real-world objects (objects with semantic meaning, non-utility objects,
 *   non-value objects).</p>
 * <p>Semantic objects should always be used <em>by reference</em>. For this reason,
 *   the contracts of {@link Object#equals(Object)} and {@link Object#hashCode()},
 *   inherited from {@link Object}, are frozen here.</p>
 * <p>Furthermore, in an ideal world, to keep things under control, there should be
 *   exactly <strong>1</strong> Java object for each relevant real-world object
 *   (abstraction function should be a surjection). Duplicate representatives of
 *   real-world objects make things very hard. Sadly, in very many cicrumstances,
 *   we cannot do without multiple representatives. In any case, to guard
 *   against accidental copy proliferation, semantic objects are non-cloneable.</p>
 * <p>Note that, as a consequence of the above, reference identity nor {@link #equals(Object)}
 *   can detect whether to semantic objects represent the same real-world object
 *   or not. If this is a concern, a semantically relevant separate method, e.g.,
 *   {@code representsSame(_T_ other)} should be added. Furthermore, not that even
 *   if another semantic object has exactly the same properties, it is not necessarily so
 *   that they both represent the same real-world object. Indeed, there <em>is</em>
 *   at least 1 other person in Belgium that has &quot;Jan Dockx&quot; as his name.</p>
 * <p>{@ink #toString()} should only be used for debugging and logging purposes. For
 *   semantic beans, {@ink #toString()} should return a {@link String} that shows
 *   the class name and hash code (like {@link Object#toString()} does, followed
 *   by a comma-separated list of
 *   <code><var>propertyName</var> = <var>propertyValue</var></code> entries.
 *   Experience shows that this is not that hard, but that great care should be taken
 *   that no infinite loop is introduced in the code when navigating over the
 *   object graph. Therefore, the general rule should be that <dfn>upstream</dfn>
 *   reference properties are listed (usually to-one associations), but
 *   <dfn>downstream</dfn> reference properties (usually to-many associations) should
 *   not.</p>
 * <p>{@link AbstractSemanticBean} offers hard implementations that enforce the
 *   above rules. As this interface doesn't really enforce anything through the compiler,
 *   it can be seen as a tagging interface.</p>
 *
 * @note This class is the result of a split of {@link RousseauBean} from the previous
 *       version.
 * @note In a previous version, we had a method {@code hasSameValues(RousseaBean rb)},
 *       meant to express the difference of having the samen values, {@link #equals(Object)}
 *       {@code ==}, and being a representative of the same real-world object.
 *       Yet, in practice it proved that this method was difficult to maintain in all
 *       subtypes, and was actually never used. It is removed in this version.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @since VI
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface SemanticBean {

  /**
   * Override to make final.
   *
   * @see Object
   */
  @MethodContract(post = {@Expression("this == other")})
  boolean equals(final Object other);

  /**
   * Override to make final.
   *
   * @see Object
   */
  int hashCode();

  @MethodContract(
    post = {
      @Expression("result != null"),
      @Expression("result.startsWith(class.name + '@' + Integer.toHexString(hashCode()) + '[')"),
      @Expression("result.endsWith(']')")
    }
  )
  String toString();

}
