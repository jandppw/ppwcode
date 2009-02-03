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
package org.ppwcode.vernacular.l10n_III.dojo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.l10n_III.LocaleHelpers;
import org.ppwcode.vernacular.l10n_III.util.SimpleServlet;
import org.ppwcode.vernacular.l10n_III.web.HttpRequestLocaleFilter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.PassThroughFilterChain;

/**
 * This class contains unit tests for DojoDjConfigFilter.  The purpose of these tests is
 * to verify that the algorithm used in DojoDjConfigFilter is correct.
 *
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 */
public class DojoDjConfigFilterTest {

  private static final Log LOG = LogFactory.getLog(DojoDjConfigFilterTest.class);

  //
  //  test setups
  //

  public final static String INPUT_1 =
          "<script type=\"text/javascript\"\n" +
          "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
          "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'nl-be'\">\n" +
          "</script>";

  public final static String OUTPUT_1 = "<script type=\"text/javascript\"\n" +
           "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
           "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'fr-be'\">\n" +
           "</script>";

  public final static String INPUT_2 =
          "<script type=\"text/javascript\"\n" +
          "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'nl-be'\"\n" +
          "src=\"js/dojo-1.1.1/dojo/dojo.js\">\n" +
          "</script>";

  public final static String OUTPUT_2 =
           "<script type=\"text/javascript\"\n" +
           "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'fr-be'\"\n" +
           "src=\"js/dojo-1.1.1/dojo/dojo.js\">\n" +
           "</script>";

  public final static String INPUT_3 =
          "<script type='text/javascript'\n" +
          "  djConfig='parseOnLoad: true, modulePaths: {org: \"../../org\", contracts: \"../../../contracts_i18n\", i18n: \"../../../i18n\"}, isDebug: false, locale:\"nl-be\"'\n" +
          "  src=\"js/dojo-1.1.1/dojo/dojo.js\">\n" +
          "</script>";

  public final static String OUTPUT_3 =
          "<script type='text/javascript'\n" +
          "  djConfig='parseOnLoad: true, modulePaths: {org: \"../../org\", contracts: \"../../../contracts_i18n\", i18n: \"../../../i18n\"}, isDebug: false, locale:\"fr-be\"'\n" +
          "  src=\"js/dojo-1.1.1/dojo/dojo.js\">\n" +
          "</script>";

  public final static String INPUT_4 =
          "<script type=\"text/javascript\"\n" +
          "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
          "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'nl'\">\n" +
          "</script>";

  public final static String OUTPUT_4 = "<script type=\"text/javascript\"\n" +
           "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
           "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'fr-be'\">\n" +
           "</script>";

  public final static String INPUT_5 =
          "<script type=\"text/javascript\"\n" +
          "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
          "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:''\">\n" +
          "</script>";

  public final static String OUTPUT_5 = "<script type=\"text/javascript\"\n" +
           "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
           "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'fr-be'\">\n" +
           "</script>";

  public final static String INPUT_6 =
          "<script type=\"text/javascript\"\n" +
          "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
          "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, 'locale':''\">\n" +
          "</script>";

  public final static String OUTPUT_6 = "<script type=\"text/javascript\"\n" +
           "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
           "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, 'locale':'fr-be'\">\n" +
           "</script>";

  public final static String INPUT_7 =
          "<script type=\"text/javascript\"\n" +
          "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
          "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false\">\n" +
          "</script>";

  public final static String OUTPUT_7 = "<script type=\"text/javascript\"\n" +
           "src=\"js/dojo-1.1.1/dojo/dojo.js\"\n" +
           "djConfig=\"parseOnLoad: true, modulePaths: {org: '../../org', contracts: '../../../contracts_i18n', i18n: '../../../i18n'}, isDebug: false, locale:'fr-be'\">\n" +
           "</script>";

  public final static String INPUT_8 =
          "<script type='text/javascript'\n" +
          "  djConfig='parseOnLoad: true, modulePaths: {org: \"../../org\", contracts: \"../../../contracts_i18n\", i18n: \"../../../i18n\"}, isDebug: false'\n" +
          "  src=\"js/dojo-1.1.1/dojo/dojo.js\">\n" +
          "</script>";

  public final static String OUTPUT_8 =
          "<script type='text/javascript'\n" +
          "  djConfig='parseOnLoad: true, modulePaths: {org: \"../../org\", contracts: \"../../../contracts_i18n\", i18n: \"../../../i18n\"}, isDebug: false, locale:\"fr-be\"'\n" +
          "  src=\"js/dojo-1.1.1/dojo/dojo.js\">\n" +
          "</script>";


  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }


  @Test
  public void testDojoDjConfig1() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_1, OUTPUT_1, "fr-be" );
  }

  @Test
  public void testDojoDjConfig2() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_2, OUTPUT_2, "fr-be" );
  }

  @Test
  public void testDojoDjConfig3() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_3, OUTPUT_3, "fr-be" );
  }

  @Test
  public void testDojoDjConfig4() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_4, OUTPUT_4, "fr-be" );
  }

  @Test
  public void testDojoDjConfig5() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_5, OUTPUT_5, "fr-be" );
  }

  @Test
  public void testDojoDjConfig6() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_6, OUTPUT_6, "fr-be" );
  }

  @Test
  public void testDojoDjConfig7() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_7, OUTPUT_7, "fr-be" );
  }

  @Test
  public void testDojoDjConfig8() throws UnsupportedEncodingException, IOException, ServletException {
    testDojoDjConfigFilterHelper("text/html", INPUT_8, OUTPUT_8, "fr-be" );
  }


  private void testDojoDjConfigFilterHelper(String type, String input, String output, String bestLocale)
          throws UnsupportedEncodingException, IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/index.html");
    MockHttpServletResponse response = new MockHttpServletResponse();

    Servlet testServlet = new SimpleServlet(type, input);
    PassThroughFilterChain chain = new PassThroughFilterChain(testServlet);

    HttpSession session = request.getSession();
    session.setAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE,
            LocaleHelpers.constructLocaleFromString(bestLocale));

    Filter djConfigFilter = new DojoDjConfigFilter();
    djConfigFilter.doFilter(request, response, chain);

    LOG.debug("input\n"+input);
    LOG.debug("output\n"+response.getContentAsString());

    Assert.assertEquals(output, response.getContentAsString());
    Assert.assertEquals(output.length(), response.getContentLength());
  }
  
}
