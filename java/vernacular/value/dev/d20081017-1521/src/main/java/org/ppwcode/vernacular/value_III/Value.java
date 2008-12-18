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

import java.io.Serializable;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>A super type for all classes that emulate <em>algebras</em>, i.e., value types.</p>
 * <p>Value types <strong>must</strong> override {@link #equals(Object)} and {@link #hashCode()}, so that
 *   different objects that represent the same value are considered equal. Value types <strong>must</strong>
 *   override {@link #toString()} to return a textual representation of the value they represent, that does
 *   not mention object identity. Value types <strong>must</strong> be {@link Serializable}, and thus always
 *   should have a public default constructor.</p>
 * <p>According to this vernacular, value types <em>should</em> come with a {@link java.beans.PropertyEditor}
 *   that supports at least a textual interface. Supporting classes in this library offer support for Hibernate
 *   {@ink org.hibernate.usertype.UserType} classes, JSF {@link javax.faces.convert.Converter} classes,
 *   and the likes for other technologies, that are based on JavaBeans {@link java.beans.PropertyEditor}
 *   classes. JavaBeans {@link java.beans.PropertyEditor} classes are used as the basis for all these
 *   conversions, because they are the oldest conversion technology, and part of the basic Java API.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression(value = "for (Value v1) {for (Value v2) {" +
                              "v1.equals(mv2) ? v1.hashCode() == v2.hashCode()}" +
                            "}",
                    note = "just a formalisation of contract described in English in java.lang.Object"))
public abstract class Value implements Serializable {

  /**
   * The empty string.
   *
   * <strong>= &quot;&quot;</strong>
   */
  public static final String EMPTY = "";



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Value types should have a public default constructor.
   */
  public Value() {
    // NOP
  }

  /*</construction>*/



  /**
   * This method should be overwritten by actual value types with extra conditions that actually do compare internal
   * values. The following idiom can be used:
   * <pre>
   *     &#x40;MethodContract(post = &#x40;Expression(&quot;<var>local conditions</var>&quot))
   *     public boolean equals(Object other) {
   *       return super.equals(other) &amp;&amp; <var>local conditions</var>;
   *     }
   * </pre>
   * Remember that the {@link #hashCode()} must be consistent with <code>equals</code>.
   */
  @MethodContract(post = @Expression("result ? ((other != null) && (other.class == this.class))"))
  @Override
  public boolean equals(final Object other) {
    return (other != null) && (other.getClass() == getClass());
  }

  /**
   * This method is made abstract to enforce subtypes to override this method.
   */
  @Override
  public abstract int hashCode();

  /**
   * This method is made abstract to enforce subtypes to override this method.
   */
  @Override
  public abstract String toString();

}
