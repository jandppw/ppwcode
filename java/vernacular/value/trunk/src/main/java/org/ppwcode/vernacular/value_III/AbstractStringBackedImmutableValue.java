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
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Common code for {@link ImmutableValue} types that have a programmatic representation
 *   that is a String.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @mudo UNFINISHED
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractStringBackedImmutableValue extends AbstractImmutableValue {

  /*<construction>*/
  //------------------------------------------------------------------

  @MethodContract(
    pre  = @Expression("_programmaticRepresentation != null"),
    post = @Expression("toString() == _programmaticRepresentation")
  )
  protected AbstractStringBackedImmutableValue(String programmaticRepresentation) {
    $programmaticRepresentation = programmaticRepresentation;
  }

  /*</construction>*/


  @Basic
  @Override
  public final String toString() {
    return $programmaticRepresentation;
  }

  private final String $programmaticRepresentation;

}
