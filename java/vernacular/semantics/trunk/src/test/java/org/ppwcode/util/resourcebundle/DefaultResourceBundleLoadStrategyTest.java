/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.util.resourcebundle;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.resourcebundle.DefaultResourceBundleLoadStrategy;


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
    ResourceBundleLoadStrategyContract.assertInvariants(subject);
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
    ResourceBundle result = subject.loadResourceBundle(basename);
    // validate
    assertTrue(basename == null || basename.equals(EMPTY) ? result == null : true);
    if (subject.getLocale() != null) {
      try {
        ResourceBundle expected = ResourceBundle.getBundle(basename, subject.getLocale());
        assertEquals(expected, result);
      }
      catch (MissingResourceException mrExc) {
        assertNull(result);
      }
    }
    if (subject.getLocale() == null) {
      try {
        ResourceBundle expected = ResourceBundle.getBundle(basename);
        assertEquals(expected, result);
      }
      catch (MissingResourceException mrExc) {
        assertNull(result);
      }
    }
    if (shouldExist) {
      assertNotNull(result);
    }
    else {
      assertNull(result);
    }
    assertTypeInvariants(subject);
  }

  @Test
  public void testLoadResourceBundleExisting() {
    for (DefaultResourceBundleLoadStrategy drbls : subjects) {
      testLoadResourceBundle(drbls, "org.ppwcode.i18n_I.DefaultResourceBundleLoadStrategy", true);
    }
  }

  @Test
  public void testLoadResourceBundleNonExisting() {
    for (DefaultResourceBundleLoadStrategy drbls : subjects) {
      testLoadResourceBundle(drbls, "somethingelse.that.does.net.exist", false);
    }
  }

}

