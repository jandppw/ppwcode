/*<license>
Copyright 2009 - $Date: 2008-10-15 18:22:22 +0200 (Wed, 15 Oct 2008) $ by PeopleWare n.v..

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

package org.ppwcode.vernacular.l10n_III;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.junit.Assert;
import org.junit.Test;

public class LocaleHelpersTest {

  private static String[] LANGUAGE_CODES = { "nl", "Nl", "NL", "fr", "de", "en", "EN", "it", "It" };
  private static String[] COUNTRY_CODES = { "BE", "be", "Be", "NL", "GB", "gb", "US", "FR", "DE", "jp", "LU" };
  private static String[] VARIANT_CODES = { "WIN", "MAC", "POSIX" };
  private static String[] DASH_CODES = { "-", "_" };

  private static String[] SUPPORTED_LOCALES_1 = { "nl", "nl-BE", "nl-NL", "fr", "fr-FR", "fr-BE" };
  private static String[] SUPPORTED_LOCALES_2 = { "nl-BE", "nl-NL", "nl", "fr-FR", "fr-BE", "fr" };
  private static String[] SUPPORTED_LOCALES_3 = { "nl-BE", "nl-NL", "fr-FR", "fr-BE" };
  private static String[] SUPPORTED_LOCALES_4 = { "nl-BE", "nl-NL", "fr-FR", "fr-BE", "nl", "fr" };
  private static String[] SUPPORTED_LOCALES_5 = { "nl", "fr", "en" };

  private static String[] ACCEPTED_LOCALES_1 = { "nl", "fr", "en" };
  private static String[] ACCEPTED_LOCALES_2 = { "fr", "fr-BE", "fr-FR" };
  private static String[] ACCEPTED_LOCALES_3 = { "fr-BE", "fr-FR", "fr" };
  private static String[] ACCEPTED_LOCALES_4 = { "nl-US", "fr-FR" };
  private static String[] ACCEPTED_LOCALES_5 = { "en", "en-US" };

  // utility method used in other unit tests
  public static List<Locale> constructListOfLocales(String[] locales) {
    List<Locale> result = new ArrayList<Locale>();
    for (String s : locales) {
      result.add(LocaleHelpers.constructLocaleFromString(s));
    }
    return result;
  }

  // utility method used in other unit tests
  public static Enumeration<Locale> constructEnumerationOfLocales(String[] locales) {
    Vector<Locale> result = new Vector<Locale>(locales.length);
    for (String s : locales) {
      result.add(LocaleHelpers.constructLocaleFromString(s));
    }
    return result.elements();
  }


  //
  // test LocaleHelpers.contructLocale
  //

  private static void testLocaleFromString(String locale, String language, String country, String variant) {
    Locale loc = LocaleHelpers.constructLocaleFromString(locale);
    Assert.assertEquals(language.toLowerCase(), loc.getLanguage().toLowerCase());
    if (country != null) {
      Assert.assertEquals(country.toLowerCase(), loc.getCountry().toLowerCase());
    } else {
      Assert.assertEquals("", loc.getCountry().toLowerCase());
    }
    if (variant != null) {
      Assert.assertEquals(variant.toLowerCase(), loc.getVariant().toLowerCase());
    } else {
      Assert.assertEquals("", loc.getVariant().toLowerCase());
    }
  }
  
  @Test
  public void testConstructLocale() {
    for (String dash : DASH_CODES) {
      for (String language : LANGUAGE_CODES) {
        String strLang = language;

        // test only language
        testLocaleFromString(strLang, language, null, null);

        // test others
        strLang += dash;
        for (String country : COUNTRY_CODES) {
          String strCountry = strLang + country;

          // test only language
          testLocaleFromString(strCountry, language, country, null);

          // test others
          strCountry += dash;
          for (String variant : VARIANT_CODES) {
            String strVariant = strCountry + variant;
            testLocaleFromString(strVariant, language, country, variant);
          }
        }
      }
    }
  }


  //
  // test LocaleHelpers.findPreferredLocale
  //

  private void testHelperFindPreferredLocale(String[] acceptedLocales, String[] supportedLocales, String expectedLocale) {
    Enumeration<Locale> accepted = constructEnumerationOfLocales(acceptedLocales);
    List<Locale> supported = constructListOfLocales(supportedLocales);
    Locale expected = LocaleHelpers.constructLocaleFromString(expectedLocale);
    Assert.assertEquals(expected, LocaleHelpers.findPreferredLocale(accepted, supported));
  }

  @Test
  public void testFindPreferredLocale() {
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_1, SUPPORTED_LOCALES_1, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_2, SUPPORTED_LOCALES_1, "fr");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_3, SUPPORTED_LOCALES_1, "fr");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_4, SUPPORTED_LOCALES_1, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_5, SUPPORTED_LOCALES_1, null);

    testHelperFindPreferredLocale(ACCEPTED_LOCALES_1, SUPPORTED_LOCALES_2, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_2, SUPPORTED_LOCALES_2, "fr");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_3, SUPPORTED_LOCALES_2, "fr-BE");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_4, SUPPORTED_LOCALES_2, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_5, SUPPORTED_LOCALES_2, null);

    testHelperFindPreferredLocale(ACCEPTED_LOCALES_1, SUPPORTED_LOCALES_3, null);
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_2, SUPPORTED_LOCALES_3, "fr-BE");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_3, SUPPORTED_LOCALES_3, "fr-BE");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_4, SUPPORTED_LOCALES_3, "fr-FR");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_5, SUPPORTED_LOCALES_3, null);

    testHelperFindPreferredLocale(ACCEPTED_LOCALES_1, SUPPORTED_LOCALES_4, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_2, SUPPORTED_LOCALES_4, "fr");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_3, SUPPORTED_LOCALES_4, "fr-BE");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_4, SUPPORTED_LOCALES_4, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_5, SUPPORTED_LOCALES_4, null);

    testHelperFindPreferredLocale(ACCEPTED_LOCALES_1, SUPPORTED_LOCALES_5, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_2, SUPPORTED_LOCALES_5, "fr");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_3, SUPPORTED_LOCALES_5, "fr");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_4, SUPPORTED_LOCALES_5, "nl");
    testHelperFindPreferredLocale(ACCEPTED_LOCALES_5, SUPPORTED_LOCALES_5, "en");

  }

}
