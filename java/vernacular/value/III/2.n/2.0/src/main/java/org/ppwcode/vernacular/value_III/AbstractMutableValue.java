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
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Support for {@link Value} implementations. It is strongly advised for all implementations of value types
 *   to extend this interface.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractMutableValue extends AbstractValue implements MutableValue {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Mutable value types should have a public default constructor.
   */
  public AbstractMutableValue() {
    // NOP
  }

  /**
   * <p>This method <strong>must</strong> be overwritten with the correct return type.
   *   If needed locally, the override implementation should feature deep copy statements.
   *   The following idiom can be used:</p>
   * <pre>
   *     public Object clone() {
   *       <var>MutableValueType</var> clone = super.clone();
   *       clone.$<var>propertyName1</var> = (<var>PropertyType</var>)$<var>propertyName1</var>.clone();
   *       &hellip;
   *       clone.$<var>propertyNameN</var> = (<var>PropertyType</var>)$<var>propertyNameN</var>.clone();
   *       return clone;
   *     }
   * </pre>
   */
  @MethodContract(
    post = {
      @Expression("result != this"),
      @Expression("result.equals(this)")
    }
  )
  @Override
  public AbstractMutableValue clone() {
    AbstractMutableValue result = null;
    try {
      result = (AbstractMutableValue)super.clone();
    }
    catch (CloneNotSupportedException cnsExc) {
      unexpectedException(cnsExc, "we do implement Cloneable");
    }
    return result;
  }

  /*</construction>*/

}
