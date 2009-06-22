package org.ppwcode.value_III.location.postalcode;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.value_III.id11n.AbstractRegexIdentifierTest;
import org.ppwcode.value_III.id11n.Identifier;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;

public class BePostalCodeTest extends AbstractRegexIdentifierTest {

  @Override
  @Before
  public void setUp() throws Exception {
    List<BePostalCode> s = new ArrayList<BePostalCode>();
    BePostalCode subject = new BePostalCode("2500"); // Lier
    s.add(subject);
    subject = new BePostalCode("2000");
    s.add(subject);
    subject = new BePostalCode("1000");
    s.add(subject);
    subject = new BePostalCode("1040");
    s.add(subject);
    subject = new BePostalCode("4000");
    s.add(subject);
    subject = new BePostalCode("6900");
    s.add(subject);
    $subjects = s;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<? extends BePostalCode> subjects() {
    return (List<? extends BePostalCode>) $subjects;
  }

  @Override
  protected void assertInvariants(Identifier subject) {
    super.assertInvariants(subject);
    Assert.assertEquals(BePostalCode.REGEX_PATTERN.pattern(), BePostalCode.REGEX_PATTERN_STRING);
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBePostalCode1() throws IdentifierWellformednessException {
    new BePostalCode("0123456789");
  }

  @Test
  public void testBePostalCode2() throws IdentifierWellformednessException {
    String identifier = "2500"; // Lier
    BePostalCode subject = new BePostalCode("2500");
    testBePostalCode(subject, identifier);
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBePostalCode2b() throws IdentifierWellformednessException {
    String identifier = "2500a";
    BePostalCode subject = new BePostalCode("2500a");
    testBePostalCode(subject, identifier);
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBePostalCode3() throws IdentifierWellformednessException {
    new BePostalCode("BE 2500");
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBePostalCode4() throws IdentifierWellformednessException {
    new BePostalCode(" 2500");
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBePostalCode5() throws IdentifierWellformednessException {
    new BePostalCode("25.00");
  }

  @Test
  public void testBePostalCode6() throws IdentifierWellformednessException {
    String identifier = "2000";
    BePostalCode subject = new BePostalCode(identifier);
    testBePostalCode(subject, identifier);
  }

  private void testBePostalCode(BePostalCode subject, String identifier) {
    Assert.assertEquals(identifier, subject.getIdentifier());
    assertInvariants(subject);
  }

  // @Test
  // TODO (dvankeer): PostalAddresses test cases needed
  // public void testLocalizedAddressRepresentation() {
  //
  // PostalAddress postalAddress = new PostalAddress(this, new Locale("NL", "be"), "Lier", "Duwijckstraat 17");
  // Assert.assertFalse(postalAddress == null);
  // for (BePostalCode subject : subjects()) {
  // String lar = subject.localizedAddressRepresentation(postalAddress);
  // // TODO (dvankeer): Contract tests neede
  // assertInvariants(subject);
  // }
  // }

}
