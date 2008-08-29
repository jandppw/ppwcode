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


import java.io.Serializable;


/**
 * <p>
 * A super type for all classes that emulate <em>algebras</em>, i.e.,
 * value types.
 * </p>
 * <p>
 * Value types should override {@link #equals(Object)} and
 * {@link #hashCode()}, so that different objects that represent
 * the same value are considered equal. Value types should override
 * {@link #toString()} to return a textual representation of
 * the value they represent, that does not mention object identity.
 * </p>
 * <p>
 * Value types should be serializable, and thus always should
 * have a public default constructor.
 * </p>
 * <p>
 * Value types should come with a {@link java.beans.PropertyEditor}
 * that supports at least a textual interface.
 * </p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar     (forall MutableValue mv1, mv2; ;
 *              mv1.equals(mv2) ==> mv1.hashCode() == mv2.hashCode());
 *
 * @todo (jand): Check serializability for entire hierarchy
 */
public abstract class Value implements Serializable {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /**
   * The empty string.
   *
   * <strong>= &quot;&quot;</strong>
   */
  public static final String EMPTY = ""; //$NON-NLS-1$

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
   * This method should be overwritten with extra conditions that
   * actually do compare internal values. The following idiom
   * can be used:
   * <pre>
   *     /&#x2A;*
   *      * &#x40;result <var>local conditions</var>;
   *      &#x2A;/
   *     public boolean equals(Object other) {
   *       return super.equals(other)
   *              &amp;&amp; <var>local conditions</var>;
   *     }
   * </pre>
   * Remember that the {@link #hashCode()} must be consistent with
   * <code>equals</code>.
   *
   * @result    result ==> (other != null) && (other.getClass() == getClass());
   */
  @Override
  public boolean equals(final Object other) {
    return (other != null) && (other.getClass() == getClass());
  }

  /**
   * This method is made abstract to enforce subtypes to override
   * this method.
   */
  @Override
  public abstract int hashCode();

  /**
   * This method is made abstract to enforce subtypes to override
   * this method.
   */
  @Override
  public abstract String toString();

}
