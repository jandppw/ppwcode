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

package org.ppwcode.vernacular.l10n_III.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.l10n_III.LocaleHelpers;
import org.ppwcode.vernacular.l10n_III.LocaleManager;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * This class contains unit tests for HttpRequestLocaleFilter.  The purpose of these
 * tests is to ensure that the filter (in isolation) does what it is designed to do.
 * This is very useful, since it let's us verify that modifications in the filter
 * code still satisfy the requirements.
 * 
 * Note that an integration test with for example a complete Dojo i18n setup would be
 * very useful too.  This is currently not implemented.
 * 
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 */
public class HttpRequestLocaleFilterTest {

  private final static List<Locale> EMPTY_LOCALE_LIST = new ArrayList<Locale>();
  private final static String PKG_NAME = "org.ppwcode.test.HttpRequestLocaleFilter";

  private final static String[] SUPPORTED_LOCALES_1 = { "nl", "fr", "en" };
  private final static String[] SUPPORTED_LOCALES_2 = { "nl", "fr-be", "fr-fr", "fr" };
  private final static String[] PREFERRED_LOCALES_1 = { "nl-be" };
  private final static String[] PREFERRED_LOCALES_2 = { "en", "fr-fr" };

  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {
    LocaleManager.registerSupportedLocales(PKG_NAME, EMPTY_LOCALE_LIST);
  }

  @Test
  public void testLocaleFilter1() throws IOException, ServletException {
    testLocaleFilterHelper(SUPPORTED_LOCALES_1, PREFERRED_LOCALES_1, "nl", null);
  }

  @Test
  public void testLocaleFilter2() throws IOException, ServletException {
    testLocaleFilterHelper(SUPPORTED_LOCALES_1, PREFERRED_LOCALES_1, "nl", "fr-be");
  }

  @Test
  public void testLocaleFilter3() throws IOException, ServletException {
    testLocaleFilterHelper(SUPPORTED_LOCALES_2, PREFERRED_LOCALES_2, "fr-fr", null);
  }

  @Test
  public void testLocaleFilter4() throws IOException, ServletException {
    testLocaleFilterHelper(SUPPORTED_LOCALES_2, PREFERRED_LOCALES_2, "fr-fr", "fr");
  }

  @Test
  public void testLocaleFilter5() throws IOException, ServletException {
    testLocaleFilterHelper(SUPPORTED_LOCALES_2, PREFERRED_LOCALES_2, "fr-fr", "nl-be");
  }


  
  private void testLocaleFilterHelper(String[] supportedLocales, String[] preferredLocales, String bestLocale, String sessionLocale)
          throws IOException, ServletException {

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/index.html");

    for (int i = preferredLocales.length -1; i >= 0; i--) {
      request.addPreferredLocale(LocaleHelpers.constructLocaleFromString(preferredLocales[i]));
    }

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    if (sessionLocale != null) {
      HttpSession session = request.getSession();
      session.setAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE, 
              LocaleHelpers.constructLocaleFromString(sessionLocale));
    }

    LocaleManager.registerSupportedLocales(PKG_NAME, LocaleHelpers.constructListOfLocales(supportedLocales));

    Filter localeFilter = new HttpRequestLocaleFilter();
    localeFilter.doFilter(request, response, chain);

    if (sessionLocale == null) {
      Assert.assertTrue(request.getSession().getAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE).
            equals(LocaleHelpers.constructLocaleFromString(bestLocale)));
    } else {
      Assert.assertTrue(request.getSession().getAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE).
            equals(LocaleHelpers.constructLocaleFromString(sessionLocale)));
    }
  }

}
