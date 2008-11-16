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

import java.util.Map;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>This class defines general modi operandi for <em>legacy, pre-Java 5</em> type-safe enumeration value
 *   types. This code is not deprecated, but still of use, for programmatically defined enumeration value types,
 *   which we cannot define using <code>enum</code>.</p>
 * <p>Values of an enumeration type are immutable. Values of an enumeration type implemented according to this
 *   pattern are not intended to be calculated with. We are interested merely in their existence. This is not an
 *   algebra, but a simple set.</p>
 * <p>There are different possibilities to emulate type-safe enumeration types without the Java 5 {@code enum}
 *   syntax.</p>
 * <p>One popular way was to apply an n-ary singleton-like pattern, where the constructor is kept private, and
 *   the acceptable values are created by the class loader and stored in class variables. When this pattern is
 *   used, it is not necessary to store any value in instance variables, since the mere object identity suffices to
 *   identify the value. Object equality (<code>==</code>) can be used to compare values. This approach however
 *   fails in applications with more than 1 class loader.</p>
 * <p>It is easier to allow many different object to represent the same value. The value is then identified by
 *   the contents of an instance variable. This instance variable is typically an <code>int</code> or a
 *   <code>String</code>. The former is more memory friendly, but the latter is much easier if a String
 *   representation is necessary anyway. This does not preclude working with type-instances as described above.</p>
 * <p>Therefor, this pattern chooses the latter approach. All enumeration type objects carry with them a String
 *   that identifies the value within its type hierarchy. The String should be the String that is to be used as
 *   programmatic representation by an assorted property editor. This is not the label for the value that will
 *   be used to present the value to end users, since this should be i18ned.</p>
 * <p>Most interesting reuse happens in {@link AbstractEnumerationValueEditor}. That class depends on the fact
 *   that the instances of subtypes of this type return a discriminating value through {@link #toString()}, and
 *   that there is a properties file with the same name as the enumeration type that holds i18n labels for all
 *   values of the type. Also, to make implementation of specific EnumerationValueEditors easy to implement, it
 *   subtypes of this class must implement a class variable that holds a {@link Map} of tag / instance associations.
 *   This makes it also sensible to define the actual enumeration type class <code>final</code>. This class contains
 *   type invariants that subclasses must adhere to for a class constant {@link Map} called <code>VALUES</code>.
 *   This is by and large the same approach as used by the Java 5 {@code enum} language feature.</p>
 * <p>Below is an idiom for implementing subclasses:</p>
 * <pre>
 * public final class <var>EnumerationValueName</var> extends EnumerationValue {
 *
 *   /&#x2A;*
 *    * The string that is used internally to identify
 *    * <var>value 1</var>.
 *    *
 *    * <strong>= {&#x40;value}</strong>
 *    &#x2A;/
 *   public static final String <var>VALUE_1</var>_STRING = &quot;<var>DISCRIMINATING_STRING_1</var>&quot;;
 *
 *   &hellip;
 *
 *   /&#x2A;*
 *    * The string that is used internally to identify
 *    * <var>default value</var>.
 *    *
 *    * <strong>= {&#x40;value}</strong>
 *    &#x2A;/
 *   public static final String <var>VALUE_DEFAULT</var>_STRING = &quot;<var>DISCRIMINATING_STRING_DEFAULT</var>&quot;;
 *
 *   /&#x2A;*
 *    * The instance representing <var>value 1</var>.
 *    &#x2A;/
 *   public static final <var>EnumerationValueName</var> <var>VALUE_1</var> =
 *       new <var>EnumerationValueName</var>(<var>VALUE_1</var>_STRING);
 *
 *    &hellip;
 *
 *   /&#x2A;*
 *    * The instance representing <var>default value</var>.
 *    &#x2A;/
 *   public static final <var>EnumerationValueName</var> <var>VALUE_DEFAULT</var> =
 *       new <var>EnumerationValueName</var>(<var>VALUE_DEFAULT</var>_STRING);
 *
 *   public static final Map&lt;String, <var>EnumerationValueName</var>&gt; VALUES = generateValues();
 *
 *   private static Map&lt;String, <var>EnumerationValueName</var>&gt; generateValues() {
 *     Map&lt;String, <var>EnumerationValueName</var>&gt; result = new HashMap&lt;String, <var>EnumerationValueName</var>&gt;();
 *     result.put(<var>VALUE_1</var>.toString(), <var>VALUE_1</var>);
 *     result.put(<var>VALUE_DEFAULT</var>.toString(), <var>VALUE_DEFAULT</var>);
 *     return java.util.Collections.unmodifiableMap(result);
 *   }
 *
 *   /&#x2A;*
 *    * &#x40;param     discriminator
 *    *            The string that discriminates this value.
 *    &#x2A;/
 *   &#x40;MethodContract(
 *     pre  = {
 *       &#x40;Expression(&quot;_discriminator != null&quot;),
 *       &#x40;Expression(&quot;_discriminator.length &gt; 0&quot;)
 *     },
 *     post = &#x40;Expression(&quot;result.toString().equals(_discriminator)&quot;)
 *   )
 *   private <var>EnumerationValueName</var>(final String discriminator) {
 *     super(discriminator);
 *   }
 *
 *   /&#x2A;*
 *    * Enumeration types require a default constructor for serializability.
 *    * It is ill-advised to use this default constructor yourself. Use the constants instead.
 *    &#x2A;/
 *   &#x40;MethodContract(post = &#x40;Expression(&quot;result.equals(<var>VALUE_DEFAULT</var>)&quot;))
 *   public <var>EnumerationValueName</var>() {
 *     this(<var>VALUE_DEFAULT</var>_STRING);
 *   }
 *
 * }
 * </pre>
 * <p>Since uniqueness of instances is not guaranteed,
 *  <strong>comparison should always be done through
 *  {@link #equals(Object)}</strong>. Users
 *   should however still avoid creating new objects, and use
 *   the class constants, or the entries in the <code>VALUES</code>
 *   map instead.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @mudo Contract in test code not worked out, because this was considered legacy, but it turns out we still need it.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("VALUES != null"),
  @Expression("VALUES.size > 0"),
  @Expression("! VALUES.keySet().contains(null)"),
  @Expression("! VALUES.values().contains(null)"),
  @Expression("VALUES.values().contains(this)"),
  @Expression("this.equals(VALUES.get(toString()))")
})
public abstract class EnumerationValue extends AbstractImmutableValue {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     discriminator
   *            The string that discriminates this value.
   */
  @MethodContract(
    pre  = {
      @Expression("_discriminator != null"),
      @Expression("_discriminator.length > 0")
    },
    post = @Expression("result.toString().equals(_discriminator)")
  )
  protected EnumerationValue(final String discriminator) {
    assert discriminator != null;
    assert discriminator.length() > 0;
    $discriminator = discriminator;
  }

  /*</construction>*/



  @MethodContract(post = @Expression("result == toString()"))
  public final String getValue() {
    return $discriminator;
  }

  /**
   * The discriminator, which is also used as the programmatic representation of the value
   * in every context (persistence, transport, ...).
   */
  @Invars(@Expression("$discriminator != null"))
  private String $discriminator;

  @Basic
  @Override
  public final String toString() {
    return $discriminator;
  }



  @Override
  @MethodContract(
    post = @Expression("toString().equals(_other.toString())")
  )
  public final boolean equals(final Object o) {
    return super.equals(o) && ($discriminator.equals(((EnumerationValue)o).$discriminator));
  }

  @Override
  @MethodContract(
    post = @Expression("toString().hashCode()")
  )
  public final int hashCode() {
    return $discriminator.hashCode();
  }

}
