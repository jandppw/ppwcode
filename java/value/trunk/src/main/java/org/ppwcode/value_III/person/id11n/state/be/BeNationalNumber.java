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

package org.ppwcode.value_III.person.id11n.state.be;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.id11n.AbstractRegexIdentifier;
import org.ppwcode.value_III.id11n.IdentifierIssuingAuthority;
import org.ppwcode.value_III.id11n.IdentifierSchemeDescription;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;
import org.ppwcode.value_III.person.id11n.PersonIdentifier;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>The identifier issued by the Belgian state for all civilians
 *   (National Number, rijksregisternummer, numéro national).</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@IdentifierIssuingAuthority(name = "Federal Public Service of the Interior", uri = "http://www.ibz.fgov.be/")
@IdentifierSchemeDescription("http://www.ibz.rrn.fgov.be/fileadmin/user_upload/Registre/nl/instructies/onderrichtingen_080601.pdf")
// starting from page 31
public final class BeNationalNumber extends AbstractRegexIdentifier implements PersonIdentifier {


  public final static String REGEX_PATTERN = "MUDO";

  @MethodContract(
    pre  = {
      @Expression("_identifier != null"),
      @Expression("_identifier != EMPTY")
    },
    post = {
      @Expression("identifier == _identifier")
    },
    exc  = {
      @Throw(type = IdentifierWellformednessException.class,
             cond = @Expression("! regexPattern.matcher(identifier).matches()"))
    }
  )
  public BeNationalNumber(String identifier) throws IdentifierWellformednessException {
    super(identifier);
  }

}
