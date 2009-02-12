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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Test;

import org.ppwcode.vernacular.l10n_III.util.BeanA;
import org.ppwcode.vernacular.l10n_III.util.ExceptionA;
import static org.junit.Assert.assertEquals;


public class I18nExceptionHelpersTest {

  //
  //  test I18nExceptionHelpersTest.processElementValue
  //

  @Test
  public void testProcessElement1() {
    Exception e = new Exception("TEST");
    assertEquals("TEST", I18nExceptionHelpers.processElementValue("message", e));
  }

  @Test
  public void testProcessElement2() {
    Exception inner = new Exception("INNER");
    Exception outer = new Exception("OUTER", inner);
    assertEquals("OUTER", I18nExceptionHelpers.processElementValue("message", outer));
    assertEquals("INNER", I18nExceptionHelpers.processElementValue("cause.message", outer));
  }

  @Test
  public void testProcessElement3() {
    Exception inner = new Exception("INNER");
    Exception inter = new Exception("INTER", inner);
    Exception outer = new Exception("OUTER", inter);
    assertEquals("OUTER", I18nExceptionHelpers.processElementValue("message", outer));
    assertEquals("INTER", I18nExceptionHelpers.processElementValue("cause.message", outer));
    assertEquals("INNER", I18nExceptionHelpers.processElementValue("cause.cause.message", outer));
  }

  @Test
  public void testProcessElement4() {
    Exception inner = new Exception("INNER");
    Exception outer = new Exception("OUTER", inner);
    assertEquals("OUTER", I18nExceptionHelpers.processElementStringValue("message", outer));
    assertEquals("INNER", I18nExceptionHelpers.processElementStringValue("cause.message", outer));
    assertEquals("null", I18nExceptionHelpers.processElementStringValue("cause.cause.message", outer));
  }


  private String EXP_MESSAGE_1 = "Fout: \"Tekst\" van \"A-Boon\": de waarde \"hello, there!\" wordt niet aanvaard.";
  private String EXP_MESSAGE_2 = "Erreur: \"Texte\" de \"A-Haricot\": la valeur \"hello, there!\" n'est pas acceptable.";
  private String EXP_MESSAGE_3 = "Start Datum (1-6-08) valt na Eind datum (1-1-08).";
  private String EXP_MESSAGE_4 = "Date de debut (01/06/08) is plus tard que Date de fin (01/01/08).";

  @Test
  public void testI18nMessage1() {
    BeanA b = new BeanA();
    b.setString("hello, there!");
    ExceptionA exc = new ExceptionA(b, "string", "DEFAULT", null);
    Locale locale = new Locale("nl");
    String msg = I18nExceptionHelpers.i18nExceptionMessage(exc, locale);
    assertEquals(EXP_MESSAGE_1, msg);
  }

  @Test
  public void testI18nMessage2() {
    BeanA b = new BeanA();
    b.setString("hello, there!");
    ExceptionA exc = new ExceptionA(b, "string", "DEFAULT", null);
    Locale locale = new Locale("fr");
    String msg = I18nExceptionHelpers.i18nExceptionMessage(exc, locale);
    assertEquals(EXP_MESSAGE_2, msg);
  }

  @Test
  public void testI18nMessage3() {
    BeanA b = new BeanA();
    b.setStartDate(getDayDate("01/06/2008"));
    b.setEndDate(getDayDate("01/01/2008"));
    ExceptionA exc = new ExceptionA(b, null, "DATES_NOT_IN_ORDER", null);
    Locale locale = new Locale("nl");
    String msg = I18nExceptionHelpers.i18nExceptionMessage(exc, locale);
    assertEquals(EXP_MESSAGE_3, msg);
  }

  @Test
  public void testI18nMessage4() {
    BeanA b = new BeanA();
    b.setStartDate(getDayDate("01/06/2008"));
    b.setEndDate(getDayDate("01/01/2008"));
    ExceptionA exc = new ExceptionA(b, null, "DATES_NOT_IN_ORDER", null);
    Locale locale = new Locale("fr");
    String msg = I18nExceptionHelpers.i18nExceptionMessage(exc, locale);
    assertEquals(EXP_MESSAGE_4, msg);
  }

//  @Test
//  public void testClass() {
//    BeanA b = new BeanA();
//    ExceptionA exc = new ExceptionA(b, "string", "TYPE", null);
//    Locale locale = new Locale("en");
//    String msg = I18nExceptionHelpers.i18nExceptionMessage(exc, locale);
//    System.out.println("result: " + msg);
//  }


  private static Date getDayDate(String date) {
    SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy");
    try {
      return sdf.parse(date);
    } catch (ParseException exc) {
      Assert.fail();
    }
    return null;
  }

}
