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

package org.ppwcode.value_III.organization.id11n.state.be;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.regex.Pattern;

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
import org.toryt.annotations_I.Invars;
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
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@IdentifierIssuingAuthority(name = "MUDO")
@IdentifierSchemeDescription("MUDO")
public final class BeEnterpriseNumber extends AbstractRegexIdentifier implements OrganizationIdentifier {

  /**
   * <p>The Belgian Enterprise Number consists of 10 digits.
   *   The first 8 digits are the <dfn>main number</dfn>. This is the old 7-digit VAT number, preceded
   *   by a {@code 0} for existing organizations that already do have a VAT number. New organizations
   *   get a 8 + 2 digit enterprise number immediately, that starts with {@code 1}.
   *   The last 2 digits are a <dfn>control number</dfn>, which is
   *   <code><var>control number</var> == 97 - <var>main number</var> % 97</code>.</p>
   * <p><code>REGEX_PATTERN_STRING == <strong>{@value}</strong></code></p>
   */
  public final static String REGEX_PATTERN_STRING = "^((0|1)(\\d{7}))(\\d{2})$";

  /**
   * The regex pattern describing a Belgian Enterprise Number.
   */
  @Invars(@Expression("REGEX_PATTERN.pattern() == REGEX_PATTERN_STRING"))
  public final static Pattern REGEX_PATTERN = Pattern.compile(REGEX_PATTERN_STRING);

  /**
   * The constant used in calculating the control number in a Belgian Enterprise Number.
   */
  public final static int CONTROL_CONSTANT = 97;

  /**
   * <p>The prefix used in a Belgian Enterprise Number that is derived from an old existing VAT number.</p>
   */
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
             // MUDO mod 97
    }
  )
  public BeEnterpriseNumber(String identifier) throws IdentifierWellformednessException {
    super(identifier);
    int calculatedControl = CONTROL_CONSTANT - (getMainAsInt() % CONTROL_CONSTANT);
    if (calculatedControl != getControlAsInt()) {
      throw new IdentifierWellformednessException(getClass(), getIdentifier(), "CONTROL_NUMBER", null);
    }
  }

  @MethodContract(post = @Expression("patternGroup(1)"))
  public final String getMain() {
    return patternGroup(1);
  }

  @MethodContract(post = @Expression("Integer.parseInt(main)"))
  public final int getMainAsInt() {
    return Integer.parseInt(getMain());
  }

  @MethodContract(post = @Expression("patternGroup(4)"))
  public final String getControl() {
    return patternGroup(4);
  }

  @MethodContract(post = @Expression("Integer.parseInt(control)"))
  public final int getControlAsInt() {
    return Integer.parseInt(getControl());
  }

  @MethodContract(post = @Expression("patternGroup(2)"))
  public final String getPrefix() {
    return patternGroup(2);
  }

  @MethodContract(post = @Expression("prefix == OLD_VAT_NUMBER_PREFIX"))
  public final boolean isOldVatNumberBased() {
    return getPrefix().equals(OLD_VAT_NUMBER_PREFIX);
  }

  @MethodContract(post = @Expression("patternGroup(3) + control"),
                  exc  = @Throw(type = SemanticValueException.class, cond = @Expression("! isOldVatNumberBased()")))
  public final String getOldVatNumber() throws SemanticValueException {
    if (! isOldVatNumberBased()) {
      throw new SemanticValueException("NOT_OLD_VAT_NUMBER_BASED", null);
    }
    return patternGroup(3) + getControl();
  }

  @Override
  public final String toString() {
    return "BE " + getIdentifier().substring(0, 4) + "." + getIdentifier().substring(4, 7) + "." + getIdentifier().substring(7, 10);
  }

}
