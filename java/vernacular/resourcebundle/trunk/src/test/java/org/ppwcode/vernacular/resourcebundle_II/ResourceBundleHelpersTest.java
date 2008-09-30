/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.resourcebundle_II;

import static java.util.ResourceBundle.getBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.ppwcode.vernacular.resourcebundle_II.ResourceBundleHelpers.value;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NOTATIONDatatypeValidator;



public class ResourceBundleHelpersTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testTypeResourceBundle() {
    fail("Not yet implemented");
  }

  @Test
  public void testValueClassOfQStringArrayClassOf_T_ResourceBundleLoadStrategy() {
    fail("Not yet implemented");
  }

  @Test
  public void testValueResourceBundleStringArrayClassOf_T_() {
    fail("Not yet implemented");
  }

  @Test
  public void testValueResourceBundleStringClassOf_T_1() throws KeyNotFoundException, WrongValueTypeException {
    String result = value(EXISTING_RESOURCE_BUNDLE, EXISTING_KEY, String.class);
    assertEquals(EXISTING_KEY_VALUE, result);
  }

  @Test(expected = WrongValueTypeException.class)
  public void testValueResourceBundleStringClassOf_T_2() throws KeyNotFoundException, WrongValueTypeException {
    @SuppressWarnings("unused")
    Integer result = value(EXISTING_RESOURCE_BUNDLE, EXISTING_KEY, Integer.class);
    fail();
  }

  @Test(expected = KeyNotFoundException.class)
  public void testValueResourceBundleStringClassOf_T_3() throws KeyNotFoundException, WrongValueTypeException {
    value(EXISTING_RESOURCE_BUNDLE, NON_EXISTING_KEY, String.class);
    fail();
  }

  @Test
  public void testValueResourceBundleStringClassOf_T_4() throws KeyNotFoundException, WrongValueTypeException {
    String result = value(EXISTING_RESOURCE_BUNDLE, EMPTY_KEY, String.class);
    assertEquals(EMPTY_KEY_VALUE, result);
  }

  public final static Locale EXISTING_LOCALE = new Locale("nl", "BE");
  public final static ResourceBundle EXISTING_RESOURCE_BUNDLE = getBundle(DefaultResourceBundleLoadStrategyTest.EXISTING_BASE_NAME, EXISTING_LOCALE);
  public final static String EXISTING_KEY = "entry";
  public final static String EXISTING_KEY_VALUE = "bogus";
  public final static String NON_EXISTING_KEY = "notanentry";
  public final static String EMPTY_KEY = "emptyEntry";
  public final static String EMPTY_KEY_VALUE = "";

}

