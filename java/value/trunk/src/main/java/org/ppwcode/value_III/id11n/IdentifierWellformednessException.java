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

package org.ppwcode.value_III.id11n;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.ValueException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Exception thrown when we detect that an identifier string is not
 *   well-formed according to the rules for a particular identifier scheme.
 *   Since {@link Identifier Identifiers} are immutable objects, and
 *   can only be configured using a constructor, and we may not expose a
 *   partially created identifier object in any way, this exception never
 *   carries an effective violating object. It does however always carry
 *   the particular subclass for which the error occured.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("value == null"))
public class IdentifierWellformednessException extends ValueException {

  @MethodContract(
    pre  = @Expression("_identifierClass != null"),
    post = {
      @Expression("valueType == _identifierClass"),
      @Expression("identifier == _identifier"),
      @Expression("value == null"),
      @Expression("message == _message"),
      @Expression("cause == _cause")
    }
  )
  public IdentifierWellformednessException(Class<? extends Identifier> identifierClass, String identifier,
                                           String message, Throwable cause) {
    super(identifierClass, message, cause);
    $identifier = identifier;
  }



  @Basic
  public final String getIdentifier() {
    return $identifier;
  }

  private final String $identifier;

}
