package org.ppwcode.value_III.location.postalcode;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.regex.Pattern;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.id11n.AbstractRegexIdentifier;
import org.ppwcode.value_III.id11n.IdentifierIssuingAuthority;
import org.ppwcode.value_III.id11n.IdentifierSchemeDescription;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;
import org.ppwcode.value_III.location.Country;
import org.ppwcode.value_III.location.CountryEditor;
import org.ppwcode.value_III.location.PostalAddress;
import org.ppwcode.value_III.location.PostalCode;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;

/**
 * <p>
 * Postal codes as used in Belgium
 * </p>
 * 
 * @author Jan Dockx
 * @author PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$", date = "$Date$")
@IdentifierIssuingAuthority(name = "De Post", uri = "http://www.depost.be")
@IdentifierSchemeDescription("http://nl.wikipedia.org/wiki/Postcode")
public final class BePostalCode extends AbstractRegexIdentifier implements PostalCode {

  /**
   * <p>
   * Belgian postal codes are 4 digits.
   * </p>
   * <p>
   * <code>REGEX_PATTERN_STRING == <strong>{@value}</strong></code>
   * </p>
   */
  public final static String REGEX_PATTERN_STRING = "^\\d{4}$";

  /**
   * The regex pattern describing a Belgian Enterprise Number.
   */
  @Invars(@Expression("REGEX_PATTERN.pattern() == REGEX_PATTERN_STRING"))
  public final static Pattern REGEX_PATTERN = Pattern.compile(REGEX_PATTERN_STRING);

  @MethodContract(pre = {
    @Expression("_identifier != null"),
    @Expression("_identifier != EMPTY")
  }, post = {
    @Expression("identifier == _identifier")
  }, exc = {
    @Throw(type = IdentifierWellformednessException.class, cond = @Expression("! regexPattern.matcher(identifier).matches()"))
  })
  public BePostalCode(String identifier) throws IdentifierWellformednessException {
    super(identifier);
  }

  public Country getCountry() {
    return Country.VALUES.get("BE");
  }

  public String localizedAddressRepresentation(PostalAddress postalAddress) {
    CountryEditor ce = new CountryEditor();
    ce.setDisplayLocale(postalAddress.getLocale());
    ce.setValue(getCountry());
    return postalAddress.getStreetAddress() + EOL + getIdentifier() + " " + postalAddress.getCity() + EOL
        + ce.getLabel();
  }

}
