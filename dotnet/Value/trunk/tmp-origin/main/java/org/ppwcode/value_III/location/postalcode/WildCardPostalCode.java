package org.ppwcode.value_III.location.postalcode;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.id11n.AbstractIdentifier;
import org.ppwcode.value_III.id11n.IdentifierIssuingAuthority;
import org.ppwcode.value_III.id11n.IdentifierSchemeDescription;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;
import org.ppwcode.value_III.location.Country;
import org.ppwcode.value_III.location.CountryEditor;
import org.ppwcode.value_III.location.PostalAddress;
import org.ppwcode.value_III.location.PostalCode;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;

/**
 * <p>
 * Postal code to be used if no applicable type exists.
 * </p>
 *
 * @author Jan Dockx
 * @author David Van Keer
 * @author PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$", date = "$Date$")
@IdentifierIssuingAuthority(name = "MUDO")
@IdentifierSchemeDescription("MUDO")
public final class WildCardPostalCode extends AbstractIdentifier implements PostalCode {

  @MethodContract(pre = {
    @Expression("_identifier != null"),
    @Expression("_identifier != EMPTY"),
    @Expression("_country != null")
  }, post = {
    @Expression("identifier == _identifier"),
    @Expression("country == _Country")
  })
  public WildCardPostalCode(String identifier, Country country) throws IdentifierWellformednessException {
    super(identifier);
    assert country != null;
    $country = country;
  }

  public Country getCountry() {
    return $country;
  }

  private final Country $country;

  public String localizedAddressRepresentation(PostalAddress postalAddress) {
    CountryEditor ce = new CountryEditor();
    ce.setDisplayLocale(postalAddress.getLocale());
    ce.setValue(getCountry());
    return postalAddress.getStreetAddress() + EOL + getIdentifier() + " " + postalAddress.getCity() + EOL
        + ce.getLabel();
  }

}
