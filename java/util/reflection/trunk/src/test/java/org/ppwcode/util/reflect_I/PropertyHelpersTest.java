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

package org.ppwcode.util.reflect_I;


import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.apache.commons.lang.ArrayUtils.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.PropertyHelpers.EMPTY;
import static org.ppwcode.util.reflect_I.PropertyHelpers.carNestedPropertyName;
import static org.ppwcode.util.reflect_I.PropertyHelpers.cdrNestedPropertyName;
import static org.ppwcode.util.reflect_I.PropertyHelpers.hasProperty;
import static org.ppwcode.util.reflect_I.PropertyHelpers.hasSimpleProperty;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyDescriptor;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyEditor;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyType;
import static org.ppwcode.util.reflect_I.PropertyHelpers.simplePropertyDescriptor;
import static org.ppwcode.util.reflect_I.TypeName.DOT;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.CloneableStubClassA;
import org.ppwcode.util.reflect_I.teststubs.StubClass;
import org.ppwcode.vernacular.exception_III.ApplicationException;
import org.ppwcode.vernacular.exception_III.SemanticException;


public class PropertyHelpersTest {

  public final static Map<String, Class<?>> EXISTING_PROPERTIES_WITH_EDITOR = new HashMap<String, Class<?>>();
  static {
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubRoProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubRoPrivateProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubRoPackageProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubRoProtectedProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubWoProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubWoPrivateProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubWoPackageProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubWoProtectedProperty", CloneableStubClassA.class);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyInt", Integer.TYPE);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyLong", Long.TYPE);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyShort", Short.TYPE);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyByte", Byte.TYPE);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyBoolean", Boolean.TYPE);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyFloat", Float.TYPE);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyDouble", Double.TYPE);
    EXISTING_PROPERTIES_WITH_EDITOR.put("stubPropertyString", String.class);
  }

  public final static Map<String, Class<?>> EXISTING_PROPERTIES_WITHOUT_EDITOR = new HashMap<String, Class<?>>();
  static {
    EXISTING_PROPERTIES_WITHOUT_EDITOR.put("stubPropertyChar", Character.TYPE);
    EXISTING_PROPERTIES_WITHOUT_EDITOR.put("stubPropertyDate", Date.class);
  }

  public final static Map<String, Class<?>> EXISTING_NESTED_PROPERTIES_WITH_EDITOR = new HashMap<String, Class<?>>();
  static {
    EXISTING_NESTED_PROPERTIES_WITH_EDITOR.put("stubProperty", CloneableStubClassA.class);
    EXISTING_NESTED_PROPERTIES_WITH_EDITOR.put("stubProperty.stubProperty.stubProperty", CloneableStubClassA.class);
  }

  public final static Map<String, Class<?>> EXISTING_NESTED_PROPERTIES_WITHOUT_EDITOR = new HashMap<String, Class<?>>();
  static {
    EXISTING_NESTED_PROPERTIES_WITHOUT_EDITOR.put("stubProperty.stubProperty", StubClass.class);
    EXISTING_NESTED_PROPERTIES_WITHOUT_EDITOR.put("stubProperty.stubProperty.stubProperty.stubProperty", StubClass.class);
  }

  public final static Set<String> EXISTING_PROPERTY_NAMES = new HashSet<String>();
  static {
    EXISTING_PROPERTY_NAMES.addAll(EXISTING_PROPERTIES_WITH_EDITOR.keySet());
    EXISTING_PROPERTY_NAMES.addAll(EXISTING_PROPERTIES_WITHOUT_EDITOR.keySet());
  }

  public final static String[] NON_EXISTING_PROPERTY_NAMES = {"doesntExist", "stubPropertyField",
                                                              "stubproperty", "StubProperty", "getStubProperty",
                                                              "setStubProperty", "getStubProperty()",
                                                              "setStubProperty()", "stuBProperty"};

  public final static Set<String> EXISTING_NESTED_PROPERTY_NAMES = new HashSet<String>();
  static {
    EXISTING_NESTED_PROPERTY_NAMES.addAll(EXISTING_NESTED_PROPERTIES_WITH_EDITOR.keySet());
    EXISTING_NESTED_PROPERTY_NAMES.addAll(EXISTING_NESTED_PROPERTIES_WITHOUT_EDITOR.keySet());
  }

  public final static String[] NON_EXISTING_NESTED_PROPERTY_NAMES = {"stubProperty.doesntExist", "doesntExist.stubProperty",
                                                                     "stubProperty.doesntExist.stubProperty",
                                                                     "stubProperty.stubProperty.doesntExist.stubProperty"};

  public final static String[] ILLEGAL_NESTED_PATTERNS = {"", "double..dot", ".startingdot", ".starting.dot",
                                                          "finaldat.", "final.dot.", "..startingdoubledot", "finaldoubledot.."};

//  @Before
//  public void setUp() throws Exception {
//  }
//
//  @After
//  public void tearDown() throws Exception {
//  }

  @Test
  public void testCarNestedPropertyName1() {
    for (String npn : EXISTING_NESTED_PROPERTY_NAMES) {
      testCarNestedPropertyName(npn);
    }
  }

  @Test
  public void testCarNestedPropertyName2() {
    for (String npn : EXISTING_PROPERTY_NAMES) {
      testCarNestedPropertyName(npn);
    }
  }

  @Test
  public void testCarNestedPropertyName3() {
    for (String npn : ILLEGAL_NESTED_PATTERNS) {
      testCarNestedPropertyName(npn);
    }
  }

  private void testCarNestedPropertyName(String npn) {
    String result = carNestedPropertyName(npn);
    int dotIndex = npn.indexOf(DOT);
    String expected = dotIndex >= 0 ? npn.substring(0, dotIndex) : npn;
    assertEquals(expected, result);
  }

  @Test
  public void testCdrNestedPropertyName1() {
    for (String npn : EXISTING_NESTED_PROPERTY_NAMES) {
      testCdrNestedPropertyName(npn);
    }
  }

  @Test
  public void testCdrNestedPropertyName2() {
    for (String npn : EXISTING_PROPERTY_NAMES) {
      testCdrNestedPropertyName(npn);
    }
  }

  @Test
  public void testCdrNestedPropertyName3() {
    for (String npn : ILLEGAL_NESTED_PATTERNS) {
      testCdrNestedPropertyName(npn);
    }
  }

  private void testCdrNestedPropertyName(String npn) {
    String result = cdrNestedPropertyName(npn);
    int dotIndex = npn.indexOf(DOT);
    String expected = dotIndex >= 0 ? npn.substring(dotIndex + 1, npn.length()) : EMPTY;
    assertEquals(expected, result);
  }

  @Test
  public void testSimplePropertyDescriptor1() {
    Class<?> type = StubClass.class;
    for (String propertyName : EXISTING_PROPERTY_NAMES) {
      PropertyDescriptor result = simplePropertyDescriptor(type, propertyName);
      assertNotNull(result);
      assertTrue(contains(getPropertyDescriptors(type), result));
      assertEquals(propertyName, result.getName());
    }
  }

  @Test
  public void testSimplePropertyDescriptor2() {
    Class<?> type = StubClass.class;
    for (String propertyName : NON_EXISTING_PROPERTY_NAMES) {
      boolean noException = false;
      try {
        simplePropertyDescriptor(type, propertyName);
        noException = true; // can't do fail() here: that throws an AssertionError
      }
      catch (AssertionError aErr) {
        // expected
      }
      if (noException) {
        fail();
      }
    }
  }

  public void testPropertyDescriptor(Class<?> type, String propertyName) {
    PropertyDescriptor result = propertyDescriptor(type, propertyName);
    PropertyDescriptor expected = propertyName.indexOf(".") >= 0 ?
                                  propertyDescriptor(simplePropertyDescriptor(type, carNestedPropertyName(propertyName)).getPropertyType(),
                                                     cdrNestedPropertyName(propertyName)) :
                                  simplePropertyDescriptor(type, propertyName);
    assertEquals(expected, result);
    String[] splitName = propertyName.split(TypeName.DOT_PATTERN);
    String expectedName = splitName[splitName.length - 1];
    assertEquals(expectedName, result.getName());
  }

  @Test
  public void testPropertyDescriptor1() {
    Class<?> type = StubClass.class;
    for (String propertyName : EXISTING_PROPERTY_NAMES) {
      testPropertyDescriptor(type, propertyName);
    }
  }

  @Test
  public void testPropertyDescriptor2() {
    Class<?> type = StubClass.class;
    for (String propertyName : NON_EXISTING_PROPERTY_NAMES) {
      boolean noException = false;
      try {
        propertyDescriptor(type, propertyName);
        noException = true; // can't do fail() here: that throws an AssertionError
      }
      catch (AssertionError aErr) {
        // expected
      }
      if (noException) {
        fail();
      }
    }
  }

  @Test
  public void testPropertyDescriptor3() {
    Class<?> type = StubClass.class;
    for (String propertyName : EXISTING_NESTED_PROPERTY_NAMES) {
      testPropertyDescriptor(type, propertyName);
    }
  }

  @Test
  public void testPropertyDescriptor4() {
    Class<?> type = StubClass.class;
    for (String propertyName : NON_EXISTING_NESTED_PROPERTY_NAMES) {
      boolean noException = false;
      try {
        propertyDescriptor(type, propertyName);
        noException = true; // can't do fail() here: that throws an AssertionError
      }
      catch (AssertionError aErr) {
        // expected
      }
      if (noException) {
        fail();
      }
    }
  }

/* Although dynamic versions might be interesting, there is no immediate need at the moment.
 * Since a null in the path is a special case, I am furthermore not sure that the ppwcode exception
 * vernacular is appropriate. This would mean we better use the BeanUtils code directly.
 */
//  @Test
//  public void testDynamicPropertyDescriptorObjectString1() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    for (String pn : EXISTING_PROPERTY_NAMES) {
//      StubClass subject = new StubClass();
//      PropertyDescriptor result = dynamicPropertyDescriptor(subject, pn);
//      PropertyDescriptor expected = propertyDescriptor(subject.getClass(), pn);
//      assertEquals(expected, result);
//    }
//  }
//
//  @Test
//  public void testDynamicPropertyDescriptorObjectString2() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    for (String pn : NON_EXISTING_PROPERTY_NAMES) {
//      StubClass subject = new StubClass();
//      boolean noException = false;
//      try {
//        dynamicPropertyDescriptor(subject, pn);
//        noException = true; // can't do fail() here: that throws an AssertionError
//      }
//      catch (AssertionError aErr) {
//        // expected
//      }
//      if (noException) {
//        fail();
//      }
//    }
//  }
//
//  @Test
//  public void testDynamicPropertyDescriptorObjectString3() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    for (String pn : EXISTING_NESTED_PROPERTY_NAMES) {
//      StubClass subject = new StubClass(new CloneableStubClassA(new StubClass(new CloneableStubClassA(new StubClass()))));
//      PropertyDescriptor result = dynamicPropertyDescriptor(subject, pn);
//      PropertyDescriptor expected = PropertyUtils.getPropertyDescriptor(subject, pn);
//      assertEquals(expected, result);
//    }
//  }
//
//  @Test
//  public void testDynamicPropertyDescriptorObjectString3b() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    StubClass subject = new StubClass(new CloneableStubClassA());
//    String threeNested = "stubProperty.stubProperty.stubProperty";
//    boolean noException = false;
//    try {
//      dynamicPropertyDescriptor(subject, threeNested);
//      noException = true;
//    }
//    catch (AssertionError ae) {
//      // normal: there is a null in the path
//    }
//    if (noException) {
//      fail();
//    }
//  }
//
//  @Test
//  public void testDynamicPropertyDescriptorObjectString4() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    for (String pn : NON_EXISTING_NESTED_PROPERTY_NAMES) {
//      StubClass subject = new StubClass(new CloneableStubClassA(new StubClass(new CloneableStubClassA(new StubClass()))));
//      boolean noException = false;
//      try {
//        dynamicPropertyDescriptor(subject, pn);
//        noException = true; // can't do fail() here: that throws an AssertionError
//      }
//      catch (AssertionError aErr) {
//        // expected
//      }
//      if (noException) {
//        fail();
//      }
//    }
//  }

  @Test
  public void testPropertyType() {
    Class<?> type = StubClass.class;
    for (String propertyName : EXISTING_PROPERTY_NAMES) {
      Class<?> result = propertyType(type, propertyName);
      assertEquals(simplePropertyDescriptor(type, propertyName).getPropertyType(), result);
    }
  }

  @Test
  public void testHasSimplePropertyClassOfQString1() {
    Class<?> type = StubClass.class;
    for (String propertyName : EXISTING_PROPERTY_NAMES) {
      boolean result = hasSimpleProperty(type, propertyName);
      assertEquals(true, result);
    }
  }

  @Test
  public void testHasSimplePropertyClassOfQString2() {
    Class<?> type = StubClass.class;
    for (String propertyName : NON_EXISTING_PROPERTY_NAMES) {
      boolean result = hasSimpleProperty(type, propertyName);
      assertEquals(false, result);
    }
  }

  @Test
  public void testHasPropertyClassOfQString1() {
    Class<?> type = StubClass.class;
    for (String propertyName : EXISTING_PROPERTY_NAMES) {
      boolean result = hasProperty(type, propertyName);
      assertEquals(true, result);
    }
  }

  @Test
  public void testHasPropertyClassOfQString2() {
    Class<?> type = StubClass.class;
    for (String propertyName : NON_EXISTING_PROPERTY_NAMES) {
      boolean result = hasProperty(type, propertyName);
      assertEquals(false, result);
    }
  }

  @Test
  public void testHasPropertyClassOfQString3() {
    Class<?> type = StubClass.class;
    for (String propertyName : EXISTING_NESTED_PROPERTY_NAMES) {
      boolean result = hasProperty(type, propertyName);
      assertEquals(true, result);
    }
  }

  @Test
  public void testHasPropertyClassOfQString4() {
    Class<?> type = StubClass.class;
    for (String propertyName : ILLEGAL_NESTED_PATTERNS) {
      boolean result = hasProperty(type, propertyName);
      assertEquals(false, result);
    }
  }

  @Test
  public void testPropertyEditor1() {
    Class<?> type = StubClass.class;
    Map<String, Class<?>> map = new HashMap<String, Class<?>>();
    map.putAll(EXISTING_PROPERTIES_WITH_EDITOR);
    map.putAll(EXISTING_NESTED_PROPERTIES_WITH_EDITOR);
    for (Map.Entry<String, Class<?>> pair : map.entrySet()) {
      PropertyEditor pe = propertyEditor(type, pair.getKey());
      assertNotNull(pe);
      PropertyEditor expected = PropertyEditorManager.findEditor(pair.getValue());
      assertEquals(expected.getClass(), pe.getClass());
    }
  }

  @Test
  public void testPropertyEditor2() {
    Class<?> type = StubClass.class;
    Map<String, Class<?>> map = new HashMap<String, Class<?>>();
    map.putAll(EXISTING_PROPERTIES_WITHOUT_EDITOR);
    map.putAll(EXISTING_NESTED_PROPERTIES_WITHOUT_EDITOR);
    for (Map.Entry<String, Class<?>> pair : map.entrySet()) {
      PropertyEditor expected = PropertyEditorManager.findEditor(pair.getValue());
      assert expected == null;
      boolean noException = false;
      try {
        propertyEditor(type, pair.getKey());
        noException = true;
      }
      catch (AssertionError ae) {
        // normal
      }
      if (noException) {
        fail();
      }
    }
  }

  @Test
  public void testPropertyEditor3() {
    Class<?> type = StubClass.class;
    for (Map.Entry<String, Class<?>> pair : EXISTING_PROPERTIES_WITH_EDITOR.entrySet()) {
      PropertyEditor pe = propertyEditor(type, pair.getKey());
      assertNotNull(pe);
      PropertyEditor expected = PropertyEditorManager.findEditor(pair.getValue());
      assertEquals(expected.getClass(), pe.getClass());
    }
  }

//  @Test
//  public void testHasPropertyEditor() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testPropertyReadMethod() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testHasPropertyReadMethod() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testPropertyWriteMethod() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testHasPropertyWriteMethod() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testPropertyValue() {
//    fail("Not yet implemented");
//  }

  @Test
  public void testSetPropertyValue1() throws ApplicationException {
    StubClass subject = new StubClass();
    Object newValue = new Object();
    PropertyHelpers.setPropertyValue(subject, "ApplicationExceptionProperty", newValue);
    assertEquals(newValue, subject.getApplicationExceptionProperty());
  }

  @Test(expected = SemanticException.class)
  public void testSetPropertyValue2() throws ApplicationException {
    StubClass subject = new StubClass();
    SemanticException newValue = new SemanticException("Semantic Exception message", null);
    PropertyHelpers.setPropertyValue(subject, "ApplicationExceptionProperty", newValue);
  }

}

