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
import org.toryt.annotations_I.MethodContract;


/**
 * <p>This interface defines the general modus operandi for mutable value types.</p>
 * <p>When a mutable value type is used for a bean property, the following idiom is often used:</p>
 * <pre>
 *
 *     &hellip;
 *
 *     /*&lt;property name=&quot;<var>propertyName</var>&quot;&gt;&#x2A;/
 *     //------------------------------------------------------------------
 *
 *     &#x40;Basic
 *     public <var>ImmutableValueType</var> get<var>PropertyName</var>() {
 *       return (<var>ImmutableValueType</var>)$<var>propertyName</var>.clone();
 *     }
 *
 *     &#x40;MethodContract(post = &#x40;Expression(&quot;<var>propertyName</var> = <var>_propertyName</var>&quot;))
 *     public void set<var>PropertyName</var>(<var>ImmutableValueType</var> <var>propertyName</var>) {
 *       $<var>propertyName</var> = (<var>ImmutableValueType</var>)<var>propertyName</var>.clone();
 *     }
 *
 *     private <var>ImmutableValueType</var> $<var>propertyName</var>;
 *
 *     /&#x2A;&lt;/property&gt;&#x2A;/
 *
 *     &hellip;
 *
 * </pre>
 * <p>Potentially with the addition of validation and type invariants. Note that values are cloned (or copied) when
 *   they are set or returned to protect encapsulation.</p>
 * <p>For this reason, mutable value types <strong>must</strong> implement {@link Cloneable}. Since the properties of
 *   the value are mutable, it is enough to have a default constructor that sets default values internally. Since we
 *   demand of subtypes that they implement {@link java.io.Serializable}, a default constructor is mandatory.</p>
 * <p>Like with any value type, {@link #equals(Object)} and {@link #hashCode()} <strong>must</strong> be overridden
 *   appropriately.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface MutableValue extends Value, Cloneable {

  @MethodContract(
    post = {
      @Expression("result != this"),
      @Expression("result.equals(this)")
    }
  )
  MutableValue clone();

}
