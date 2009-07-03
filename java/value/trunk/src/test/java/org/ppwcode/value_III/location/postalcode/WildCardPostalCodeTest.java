package org.ppwcode.value_III.location.postalcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.value_III.id11n.AbstractIdentifierTest;
import org.ppwcode.value_III.id11n.Identifier;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;
import org.ppwcode.value_III.location.Country;
import org.ppwcode.value_III.location.CountryEditor;
import org.ppwcode.value_III.location.PostalAddress;
import org.ppwcode.value_III.location.PostalCode;
import org.ppwcode.vernacular.value_III.ValueException;

public class WildCardPostalCodeTest extends AbstractIdentifierTest {

  @Override
  @Before
  public void setUp() throws Exception {
    List<WildCardPostalCode> s = new ArrayList<WildCardPostalCode>();
    WildCardPostalCode subject = new WildCardPostalCode("2500", new Country()); // Lier, Belgium
    s.add(subject);
    subject = new WildCardPostalCode("10006", Country.VALUES.get("US")); // Manhattan New York, USA
    s.add(subject);
    subject = new WildCardPostalCode("10551", Country.VALUES.get("DE")); // Tiergarten Berlin, Germany
    s.add(subject);
    subject = new WildCardPostalCode("1000", Country.VALUES.get("NL")); // Amsterdam, Netherlands
    s.add(subject);
    subject = new WildCardPostalCode("2920", Country.VALUES.get("LU")); // Luxembourg, Luxembourg
    s.add(subject);
    subject = new WildCardPostalCode("SW1A 1AA", Country.VALUES.get("GB")); // Buckingham Palace London, United Kingdom
    s.add(subject);
    $subjects = s;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<? extends WildCardPostalCode> subjects() {
    return (List<? extends WildCardPostalCode>) $subjects;
  }

  // Cannot use the super directly
  protected void assertInvariants(WildCardPostalCode subject) {
    super.assertInvariants((Identifier) subject);
    // Assert.assertNotNull(subject.getCountry());
  }

  @Test
  public void testWildCardPostalCode1() throws IdentifierWellformednessException {
    String identifier = "5016"; // Mexico City, Mexico
    Country country = Country.VALUES.get("MX");
    WildCardPostalCode subject = new WildCardPostalCode(identifier, country);
    testWildCardPostalCode(subject, identifier, country);
  }

  @Test
  public void testWildCardPostalCode2() throws IdentifierWellformednessException {
    String identifier = "5016"; // Wellington, New Zealand
    Country country = Country.VALUES.get("NZ");
    WildCardPostalCode subject = new WildCardPostalCode(identifier, country);
    testWildCardPostalCode(subject, identifier, country);
  }

  @Test
  public void testWildCardPostalCode3() throws IdentifierWellformednessException {
    String identifier = "MD-2000"; // Chisinau, Moldova
    Country country = Country.VALUES.get("MD");
    WildCardPostalCode subject = new WildCardPostalCode(identifier, country);
    testWildCardPostalCode(subject, identifier, country);
  }

  @Test
  public void testWildCardPostalCode4() throws IdentifierWellformednessException {
    String identifier = "C1420"; // Buenos Aires City, Argentina
    Country country = Country.VALUES.get("AR");
    WildCardPostalCode subject = new WildCardPostalCode(identifier, country);
    testWildCardPostalCode(subject, identifier, country);
  }

  @Test
  public void testWildCardPostalCode5() throws IdentifierWellformednessException {
    String identifier = "BT2328"; // Murra, Burnei
    Country country = Country.VALUES.get("BN");
    WildCardPostalCode subject = new WildCardPostalCode(identifier, country);
    testWildCardPostalCode(subject, identifier, country);
  }

  @Test
  public void testWildCardPostalCode6() throws IdentifierWellformednessException {
    String identifier = "2000"; // Antwerp, Belgium
    Country country = new Country();
    WildCardPostalCode subject = new WildCardPostalCode(identifier, country);
    testWildCardPostalCode(subject, identifier, country);
  }

  private void testWildCardPostalCode(WildCardPostalCode subject, String identifier, Country country) {
    Assert.assertEquals(identifier, subject.getIdentifier());
    Assert.assertEquals(country, subject.getCountry());
    assertInvariants(subject);
  }

  @Test
  public void testLocalizedAddressRepresentation() throws ValueException {
    String identifier = "1060"; // Brussels, Belgium
    Country country = new Country();
    WildCardPostalCode subject = new WildCardPostalCode(identifier, country);
    assertInvariants(subject);
    testLocalizedAddressRepresentation(subject, new PostalAddress(subject, new Locale("NL", "be"), "Brussel",
        "Henri Jasparlaan 128"));
    testLocalizedAddressRepresentation(subject, new PostalAddress(subject, new Locale("FR", "be"), "Bruxelles",
        "Avenue Henri Jaspar 128"));
  }

  private void testLocalizedAddressRepresentation(final WildCardPostalCode subject, final PostalAddress postalAddress) {
    String result = subject.localizedAddressRepresentation(postalAddress);

    CountryEditor ce = new CountryEditor();
    ce.setDisplayLocale(postalAddress.getLocale());
    ce.setValue(subject.getCountry());

    Assert.assertNotNull(result);
    Assert.assertTrue(result != "");
    Assert.assertTrue(result.contains(postalAddress.getStreetAddress()));
    Assert.assertTrue(result.contains(postalAddress.getPostalCode().getIdentifier()));
    Assert.assertTrue(result.contains(postalAddress.getCity()));
    Assert.assertTrue(result.equals(postalAddress.getStreetAddress() + PostalCode.EOL + subject.getIdentifier() + " "
        + postalAddress.getCity() + PostalCode.EOL + ce.getLabel()));
  }
}
