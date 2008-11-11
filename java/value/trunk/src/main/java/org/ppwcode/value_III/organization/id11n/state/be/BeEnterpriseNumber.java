/*<license>
Copyright 2004 - $Date: 2008-11-07 16:59:27 +0100 (Fri, 07 Nov 2008) $ by PeopleWare n.v..

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

package org.ppwcode.value_III.organization.id11n.state.be;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.id11n.AbstractRegexIdentifier;
import org.ppwcode.value_III.id11n.IdentifierIssuingAuthority;
import org.ppwcode.value_III.id11n.IdentifierSchemeDescription;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;
import org.ppwcode.value_III.organization.id11n.OrganizationIdentifier;
import org.ppwcode.vernacular.value_III.SemanticValueException;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>The identifier issued by the Belgian state for all economic
 *   organizations (<span xml-lang="en">Enterprise Number</span>, <span xml-lang="en">VAT Number</span>,
 *   <span xml-lang="fr">Numéro d'Entreprise</span>, <span xml-lang="nl">Ondernemingsnummer</span>,
 *   <span xml-lang="nl">KBO-nummer</span>, <span xml-lang="fr">Numéro de Banque Carrefour d'Entreprise</span>,
 *   <span xml-lang="fr">Numéro BCE</span>).</p>
 * <p>This number is to be used for all economic organizations in (almost) all communication starting
 *   1 Januari 2008.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date: 2008-11-07 16:59:27 +0100 (Fri, 07 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3471 $",
         date     = "$Date: 2008-11-07 16:59:27 +0100 (Fri, 07 Nov 2008) $")
@IdentifierIssuingAuthority(name = "MUDO")
@IdentifierSchemeDescription("MUDO")
public final class BeEnterpriseNumber extends AbstractRegexIdentifier implements OrganizationIdentifier {

  /**
   * The Belgian Enterprise Number consists of 10 digits.
   * The first 8 digits are the <dfn>main number</dfn>. This is the old 7-digit VAT number, preceded
   * by a {@code 0} for existing organizations that already do have a VAT number. New organizations
   * get a 8 + 2 digit enterprise number immediately, that starts with {@code 1}.
   * The last 2 digits are a <dfn>control number</dfn>, which is
   * <code><var>control number</var> == 97 - <var>main number</var> % 97</code>.
   */
  public final static String REGEX_PATTERN = "^((0|1)(\\d{7}))(\\d{2})$";

  public final static String[] GROUP_NAMES = {"mainNumber", "prefix", "oldVatMainNumber", "controlNumber"};

  public final static int CONTROL_CONSTANT = 97;

  public final static String OLD_VAT_NUMBER_PREFIX = "0";

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
  public BeEnterpriseNumber(String identifier) throws IdentifierWellformednessException {
    super(identifier);
    int calculatedControl = CONTROL_CONSTANT - (getMainAsInt() % CONTROL_CONSTANT);
    if (calculatedControl != getControlAsInt()) {
      throw new IdentifierWellformednessException(getClass(), getIdentifier(), "CONTROL_NUMBER", null);
    }
  }

  public final String getMainNumber() {
    return getPatternGroup("mainNumber");
  }

  public final int getMainAsInt() {
    return Integer.parseInt(getMainNumber());
  }

  public final String getControlNumber() {
    return getPatternGroup("controlNumber");
  }

  public final int getControlAsInt() {
    return Integer.parseInt(getControlNumber());
  }

  public final String getPrefixNumber() {
    return getPatternGroup("prefix");
  }

  public final boolean isOldVatNumberBased() {
    return getPrefixNumber().equals(OLD_VAT_NUMBER_PREFIX);
  }

  public final String getOldVatNumber() throws SemanticValueException {
    if (! isOldVatNumberBased()) {
      throw new SemanticValueException("NOT_OLD_VAT_NUMBER_BASED", null);
    }
    return getPatternGroup("oldVatMainNumber") + getControlNumber();
  }

  @Override
  public final String toString() {
    return "BE " + getIdentifier().substring(0, 4) + "." + getIdentifier().substring(4, 7) + "." + getIdentifier().substring(7, 10);
  }

}
