/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.vernacular.l10n_III.resourcebundle;


import org.ppwcode.vernacular.l10n_III.resourcebundle.DefaultResourceBundleLoadStrategy;
import org.ppwcode.vernacular.l10n_III.resourcebundle.ResourceBundleNotFoundException;
import static java.util.ResourceBundle.getBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DefaultResourceBundleLoadStrategyTest {

  private Set<DefaultResourceBundleLoadStrategy> subjects;

  @Before
  public void setUp() throws Exception {
    subjects = new HashSet<DefaultResourceBundleLoadStrategy>();
    DefaultResourceBundleLoadStrategy subject = new DefaultResourceBundleLoadStrategy();
    subjects.add(subject);
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(Locale.getDefault());
    subjects.add(subject);
    for (Locale locale : Locale.getAvailableLocales()) {
      subject = new DefaultResourceBundleLoadStrategy();
      subject.setLocale(locale);
      subjects.add(subject);
    }
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(new Locale("nl", "BE", "MAC"));
    subjects.add(subject);
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(new Locale("nl", "BE"));
    subjects.add(subject);
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(new Locale("nl"));
    subjects.add(subject);
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(new Locale("nl", "", "MAC"));
    subjects.add(subject);
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(new Locale("", "BE", "MAC"));
    subjects.add(subject);
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(new Locale("", "BE"));
    subjects.add(subject);
    subject = new DefaultResourceBundleLoadStrategy();
    subject.setLocale(new Locale("", "", "MAC"));
    subjects.add(subject);
  }

  @After
  public void tearDown() throws Exception {
    subjects = null;
  }

  public static void assertTypeInvariants(DefaultResourceBundleLoadStrategy subject) {
    _Contract_ResourceBundleLoadStrategy.assertInvariants(subject);
  }

  @Test
  public void testDefaultResourceBundleLoadStrategy() {
    // execute
    DefaultResourceBundleLoadStrategy subject = new DefaultResourceBundleLoadStrategy();
    // validate
    assertNull(subject.getLocale());
    assertTypeInvariants(subject);
  }

  public static void testSetLocale(DefaultResourceBundleLoadStrategy subject, Locale locale) {
    // execute
    subject.setLocale(locale);
    // validate
    assertEquals(locale, subject.getLocale());
    assertTypeInvariants(subject);
  }

  @Test
  public void testSetLocale() {
    for (DefaultResourceBundleLoadStrategy drbls : subjects) {
      for (Locale locale : Locale.getAvailableLocales()) {
        testSetLocale(drbls, locale);
      }
    }
  }

  public final static String EMPTY = "";

  public static void testLoadResourceBundle(DefaultResourceBundleLoadStrategy subject, String basename, boolean shouldExist) {
    // execute
    try {
      ResourceBundle result = subject.loadResourceBundle(basename);
      // validate
      if (shouldExist) {
        _Contract_ResourceBundleLoadStrategy.loadResourceBundle(subject, basename, result);
        ResourceBundle expected = null;
        if (subject.getLocale() != null) {
          expected = getBundle(basename, subject.getLocale());
        }
        else {
          expected = getBundle(basename);
        }
        assertEquals(expected, result);
      }
      else {
        fail();  // should be an exception
      }
    }
    catch (ResourceBundleNotFoundException rbnfExc) {
      if (shouldExist) {
        fail();
      }
      else {
        _Contract_ResourceBundleLoadStrategy.loadResourceBundle(subject, basename, rbnfExc);
        assertTrue(basename == null || EMPTY.equals(basename) ||
                   (subject.getLocale() != null && assertMissingResource(basename, subject.getLocale())) ||
                   (subject.getLocale() == null && assertMissingResource(basename)));
      }
    }
    assertTypeInvariants(subject);
  }

  @Test
  public void testLoadResourceBundleExisting() throws ResourceBundleNotFoundException {
    for (DefaultResourceBundleLoadStrategy drbls : subjects) {
//      System.out.println(drbls);
      testLoadResourceBundle(drbls, EXISTING_BASE_NAME, true);
    }
  }

  @Test
  public void testLoadResourceBundleNonExisting() throws ResourceBundleNotFoundException {
    for (DefaultResourceBundleLoadStrategy drbls : subjects) {
      testLoadResourceBundle(drbls, "somethingelse.that.does.net.exist", false);
    }
  }

  public final static boolean assertMissingResource(String basename, Locale locale) {
    try {
      getBundle(basename, locale);
      return false;
    }
    catch (MissingResourceException mrExc) {
      return true;
    }
  }

  public final static boolean assertMissingResource(String basename) {
    try {
      getBundle(basename);
      return false;
    }
    catch (MissingResourceException mrExc) {
      return true;
    }
  }

  public final static Class<?> EXISTING_TYPE = DefaultResourceBundleLoadStrategy.class;
  public final static String EXISTING_BASE_NAME = EXISTING_TYPE.getCanonicalName();

}

