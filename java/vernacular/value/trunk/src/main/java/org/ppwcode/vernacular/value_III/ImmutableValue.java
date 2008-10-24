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

package org.ppwcode.vernacular.value_III;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>This interface defines the general modus operandi for immutable value types.</p>
 * <p>When a immutable value type is used for a bean property, the following idiom is often used:</p>
 * <pre>
 *
 *     &hellip;
 *
 *     /*&lt;property name=&quot;<var>propertyName</var>&quot;&gt;&#x2A;/
 *     //------------------------------------------------------------------
 *
 *     &#x40;Basic
 *     public <var>ImmutableValueType</var> get<var>PropertyName</var>() {
 *       return $<var>propertyName</var>;
 *     }
 *
 *     &#x40;MethodContract(post = &#x40;Expression(&quot;<var>propertyName</var> = <var>_propertyName</var>&quot;))
 *     public void set<var>PropertyName</var>(<var>ImmutableValueType</var> <var>propertyName</var>) {
 *       $<var>propertyName</var> = <var>propertyName</var>;
 *     }
 *
 *     private <var>ImmutableValueType</var> $<var>propertyName</var>;
 *
 *     /&#x2A;&lt;/property&gt;&#x2A;/
 *
 *     &hellip;
 *
 * </pre>
 * <p>Potentially with the addition of validation and type invariants. Note that values are not cloned or copied when
 *   they are set or returned.</p>
 * <p>The actual value of an immutable value can only be defined in the constructor. A default constructor is necessary
 *   for serializability. It should create a default value. Since there is no need to make copies of immutable values,
 *   there is no need for cloning functionality. Thus, an immutable type <strong>may not</strong> implement
 *   {@link java.lang.Cloneable}. A copy constructor can be handy, but is often never used.
 *   The {@link #equals(Object)} and {@link Object#hashCode()} method <strong>must</strong> be overridden.</p>
 * <p>Actual immutable value types <strong>must</strong> be declared final. This is the only way immutability can be
 *   enforced (otherwise, a subclass might always introduce mutators). As a result, if there is a copy constructor, the
 *   cloning functionality will also not be missed. Because immutable value types must be final, if you have a type
 *   hierarchy (e.g., a general immutable value {@code A} defined as an interface or an abstract class, with concrete
 *   subtypes {@code A1}, {@code A2} and {@code A3} that are declared final) for some reason, properties also
 *   <strong>must</strong> be defined statically of one of the final types ({@code A1}, {@code A2} or {@code A3})
 *   and <strong>not</strong> be of a more general type ({@code A}). If you would define a property of the more
 *   general type, which is not final itself, anybody could define a new sub type ({@code A<sub>hack</sub>}) that
 *   still is mutable, and use it as a property value. Type hierarchies may still be interesting for outside
 *   code reuse. As a result, {@link #equals(Object)} can only be affirmative when the other object is of the
 *   exact same type.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("!Cloneable.class.isAssignableFrom(this.class)"))
public interface ImmutableValue extends Value {

  /**
   * {@inheritDoc}
   * Since immutable values have to be declared final,
   * This method should be overwritten by actual value types with extra conditions that actually do compare internal
   * values. The following idiom can be used:
   * <pre>
   *     &#x40;MethodContract(post = &#x40;Expression(&quot;<var>local conditions</var>&quot))
   *     public boolean equals(Object other) {
   *       return super.equals(other) &amp;&amp; <var>local conditions</var>;
   *     }
   * </pre>
   * Remember that the {@link Object#hashCode()} must be consistent with <code>equals</code>.
   */
  @MethodContract(post = @Expression("result ? this.class.isInstance(other)"))
  boolean equals(final Object other);

}
