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
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotEmpty;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.AbstractImmutableValue;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>General implementation of some methods required by an {@link Identifier}.</p>
 *
 * @author    Jan Dockx
 * @author    Peopleware NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractIdentifier extends AbstractImmutableValue implements Identifier {

  @MethodContract(
    pre  = {
      @Expression("_identifier != null"),
      @Expression("_identifier != EMPTY")
    },
    post = {
      @Expression("identifier == _identifier")
    },
    exc  = {
      @Throw(type = IdentifierWellformednessException.class, cond = @Expression("true"))
    }
  )
  protected AbstractIdentifier(String identifier) throws IdentifierWellformednessException {
    assert preArgumentNotEmpty(identifier, "identifier");
    $identifier = identifier;
  }



  public final String getIdentifier() {
    return $identifier;
  }

  @Invars({
    @Expression("$identifier != null"),
    @Expression("$identifier != EMPTY")
  })
  private final String $identifier;



  @Override
  public boolean equals(Object other) {
    return super.equals(other) && getIdentifier().equals(((Identifier)other).getIdentifier());
  }

  @Override
  public final int hashCode() {
    return $identifier.hashCode();
  }

  @Override
  public String toString() {
    return getClass().getCanonicalName() + ":" + getIdentifier();
  }

}
