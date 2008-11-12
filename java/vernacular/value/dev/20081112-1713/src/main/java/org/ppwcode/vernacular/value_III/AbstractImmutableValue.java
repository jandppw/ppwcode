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
import org.toryt.annotations_I.Throw;


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
@Invars(@Expression("!Cloneable.class.isAssignableFrom(this.class)"))
public abstract class AbstractImmutableValue extends AbstractValue implements ImmutableValue {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * AbstractValue types should have a public default constructor.
   */
  public AbstractImmutableValue() {
    // NOP
  }

  /*</construction>*/



  /**
   * Final override of clone() method, to make it impossible for subclasses to make the method public.
   */
  @MethodContract(post = @Expression("false"),
                  exc  = @Throw(type = CloneNotSupportedException.class,
                                cond = @Expression("true")))
  @Override
  protected final Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException("AbstractImmutableValue instances should never implement a clone method");
  }

}
