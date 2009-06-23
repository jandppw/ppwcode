package org.ppwcode.value_III.location;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.ppwcode.value_III.location.postalcode.BePostalCode;
import org.ppwcode.vernacular.value_III.ValueException;

public class PostalAddressTest {

  private static final String EMPTY = "";

  private Set<PostalCode> $postalCodes;

  private Set<Locale> $locales;

  private Set<String> $cities;

  private Set<String> $streetAddresses;

  // TODO (dvankeer): This contructor test is lame, but how do we do this these days?
  @Test
  public void testPostalAddress() throws ValueException {
    $cities = new HashSet<String>();
    $cities.add("Lier");
    $cities.add("Brussels");
    $cities.add("Drongen");

    $streetAddresses = new HashSet<String>();
    $streetAddresses.add("Duwijkstraat 17");
    $streetAddresses.add("Henri Jaspardlaan 128");
    $streetAddresses.add("Industriepark 3");

    $postalCodes = new HashSet<PostalCode>();
    $postalCodes.add(new BePostalCode("2500"));
    $postalCodes.add(new BePostalCode("1060"));
    $postalCodes.add(new BePostalCode("9031"));

    $locales = new HashSet<Locale>();
    $locales.add(new Locale("nl", "be"));
    $locales.add(new Locale("fr", "be"));
    $locales.add(new Locale("de", "be"));

    PostalAddress subject;
    for (PostalCode postalCode : $postalCodes) {
      for (Locale locale : $locales) {
        for (String city : $cities) {
          for (String streetAddress : $streetAddresses) {
            subject = new PostalAddress(postalCode, locale, city, streetAddress);
            assertInvariants(subject);
          }
        }
      }
    }
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress2() throws ValueException {
    PostalAddress subject = new PostalAddress(null, new Locale("nl", "be"), "Lier", "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress3() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), null, "Lier", "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress4a() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), null, "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress4b() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), "", "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress5a() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), "Lier\n",
        "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress5b() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), "Lie\rr",
        "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress5c() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), "Lier\n\r  ",
        "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress5d() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), "Lier\n\r   \n\r ",
        "Duwijkstraat 17");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress6a() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), "Lier", null);
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress6b() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl", "be"), "Lier", "");
    assertInvariants(subject);
  }

  @Test(expected = ValueException.class)
  public void testPostalAdress7() throws ValueException {
    PostalAddress subject = new PostalAddress(new BePostalCode("2500"), new Locale("nl"), "Lier", "Duwijkstraat 17");
    assertInvariants(subject);
  }

  public void assertInvariants(PostalAddress subject) {
    Assert.assertTrue(subject.getPostalCode().getCountry().getLocales().contains(subject.getLocale()));
    Assert.assertTrue(null != subject.getLocale());
    Assert.assertTrue(null != subject.getPostalCode());
    Assert.assertTrue(null != subject.getCity());
    Assert.assertTrue(EMPTY != subject.getCity());
    Assert.assertTrue(!Pattern.compile(".*[\\n\\r]+.*", Pattern.DOTALL).matcher(subject.getCity()).matches());
    Assert.assertTrue(null != subject.getStreetAddress());
    Assert.assertTrue(EMPTY != subject.getStreetAddress());
  }

  // TODO (dvankeer): postalcode localizationString post condition check.

}
