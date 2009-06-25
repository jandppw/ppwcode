package org.ppwcode.value_III.location.postalcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.value_III.id11n.AbstractIdentifierTest;
import org.ppwcode.value_III.id11n.AbstractRegexIdentifierTest;
import org.ppwcode.value_III.id11n.Identifier;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;
import org.ppwcode.value_III.location.Country;
import org.ppwcode.value_III.location.CountryEditor;
import org.ppwcode.value_III.location.PostalAddress;
import org.ppwcode.value_III.location.PostalCode;
import org.ppwcode.vernacular.value_III.ValueException;

public class WildCardostalCodeTest extends AbstractIdentifierTest {

  @Override
  @Before
  public void setUp() throws Exception {
    List<WildCardPostalCode> s = new ArrayList<WildCardPostalCode>();
    WildCardPostalCode subject = new WildCardPostalCode("2500"); // Lier
    s.add(subject);
    subject = new WildCardPostalCode("4597946");
    s.add(subject);
    subject = new WildCardPostalCode("BE 1060");
    s.add(subject);
    subject = new WildCardPostalCode("Placeholder");
    s.add(subject);
    subject = new WildCardPostalCode("Not known");
    s.add(subject);
    subject = new WildCardPostalCode("London");
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
    Assert.assertEquals(subject.getCountry(), Country.VALUES.get("BE"));
  }

  @Test
  public void testWildCardPostalCode1() throws IdentifierWellformednessException {
    new WildCardPostalCode("0123456789");
  }

  @Test
  public void testWildCardPostalCode2() throws IdentifierWellformednessException {
    String identifier = "2500"; // Lier
    WildCardPostalCode subject = new WildCardPostalCode("2500");
    testWildCardPostalCode(subject, identifier);
  }

  @Test
  public void testWildCardPostalCode2b() throws IdentifierWellformednessException {
    String identifier = "2500a";
    WildCardPostalCode subject = new WildCardPostalCode("2500a");
    testWildCardPostalCode(subject, identifier);
  }

  @Test
  public void testWildCardPostalCode3() throws IdentifierWellformednessException {
    new WildCardPostalCode("BE 2500");
  }

  @Test
  public void testWildCardPostalCode4() throws IdentifierWellformednessException {
    new WildCardPostalCode(" 2500");
  }

  @Test
  public void testWildCardPostalCode5() throws IdentifierWellformednessException {
    new WildCardPostalCode("25.00");
  }

  @Test
  public void testWildCardPostalCode6() throws IdentifierWellformednessException {
    String identifier = "2000";
    WildCardPostalCode subject = new WildCardPostalCode(identifier);
    testWildCardPostalCode(subject, identifier);
  }

  private void testWildCardPostalCode(WildCardPostalCode subject, String identifier) {
    Assert.assertEquals(identifier, subject.getIdentifier());
    assertInvariants(subject);
  }

  @Test
  public void testLocalizedAddressRepresentation() throws ValueException {
    for (WildCardPostalCode subject : subjects()) {
      testLocalizedAddressRepresentation(subject, new PostalAddress(subject, new Locale("NL", "be"), "Lier",
          "Duwijckstraat 17"));
      assertInvariants(subject);
      testLocalizedAddressRepresentation(subject, new PostalAddress(subject, new Locale("FR", "be"), "Bruxelles",
          "Henry Jaspaardlaan 128"));
      assertInvariants(subject);
    }
  }

  private void testLocalizedAddressRepresentation(final WildCardPostalCode subject, final PostalAddress postalAddress) {
    // TODO (dvankeer): Test written based on possible bogus contract
    CountryEditor ce = new CountryEditor();
    ce.setDisplayLocale(postalAddress.getLocale());
    ce.setValue(subject.getCountry());
    Assert.assertTrue(subject.localizedAddressRepresentation(postalAddress).equals(
        postalAddress.getStreetAddress() + PostalCode.EOL + subject.getIdentifier() + " " + postalAddress.getCity()
            + PostalCode.EOL + ce.getLabel()));
  }
}
