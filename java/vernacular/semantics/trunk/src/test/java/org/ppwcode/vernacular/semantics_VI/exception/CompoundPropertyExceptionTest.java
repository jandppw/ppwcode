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

package org.ppwcode.vernacular.semantics_VI.exception;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.vernacular.exception_III.ApplicationException.DEFAULT_MESSAGE_KEY;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyExceptionTest.OriginStub;


public class CompoundPropertyExceptionTest {

  public final static String EMPTY = "";

  private OriginStub originMock;
  private Set<String> propertyNames; // not empty
  private Set<String> messages; // not empty
  private Set<Throwable> throwables;
  private final boolean[] booleans = {true, false};
  private Set<Set<PropertyException>> elementss;
  private Set<CompoundPropertyException> subjects;

  private Set<Object> origins2;
  private Set<Class<?>> originTypes2;
  private Set<String> propertyNames2; // not empty
  private Set<String> messages2; // not empty
  private Set<Throwable> throwables2;

  @Before
  public void setUp() throws Exception {
    originMock = new OriginStub();
    elementss = new HashSet<Set<PropertyException>>();
    // empty set
    Set<PropertyException> elements = new HashSet<PropertyException>();
    elementss.add(elements);
    // one element
    PropertyExceptionTest pet = new PropertyExceptionTest();
    pet.setUp();
    for (PropertyException pe : pet.subjects) {
      elements = new HashSet<PropertyException>();
      elements.add(pe);
      elementss.add(elements);
    }
    pet.tearDown();
    SetterPropertyExceptionTest spet = new SetterPropertyExceptionTest();
    spet.setUp();
    for (SetterPropertyException spe : spet.subjects) {
      elements = new HashSet<PropertyException>();
      elements.add(spe);
      elementss.add(elements);
    }
    spet.tearDown();
    // batch of elements
    elements = new HashSet<PropertyException>();
    pet = new PropertyExceptionTest();
    pet.setUp();
    elements.addAll(pet.subjects);
    pet.tearDown();
    spet = new SetterPropertyExceptionTest();
    spet.setUp();
    elements.addAll(spet.subjects);
    spet.tearDown();
    elementss.add(elements);
    // batch of elements with a compound
    elements = new HashSet<PropertyException>();
    pet = new PropertyExceptionTest();
    pet.setUp();
    elements.addAll(pet.subjects);
    pet.tearDown();
    spet = new SetterPropertyExceptionTest();
    spet.setUp();
    elements.addAll(spet.subjects);
    spet.tearDown();
    elements.add(new CompoundPropertyException(OriginStub.class, null, null, null));
    elementss.add(elements);
    // batch of elements with a compound in a compoound, which is this (loop) NOT DONE YET
    messages = new HashSet<String>();
    messages.add(null);
    messages.add("stub message");
    propertyNames = new HashSet<String>();
    // null not allowed as propertyname
    propertyNames.add("stubProperty");
    throwables = new HashSet<Throwable>();
    throwables.add(null);
    throwables.add(new Throwable());
    subjects = createSubjects();
    origins2 = new HashSet<Object>();
    origins2.add(originMock);
    origins2.add(null);
    origins2.add(new Object());
    originTypes2 = new HashSet<Class<?>>();
    originTypes2.add(OriginStub.class);
    originTypes2.add(null);
    originTypes2.add(Object.class);
    originTypes2.add(PropertyException.class);
    propertyNames2 = new HashSet<String>(propertyNames);
    propertyNames2.add(EMPTY);
    propertyNames2.add("not a property");
    messages2 = new HashSet<String>(messages);
    messages2.add(EMPTY);
    messages2.add("another message");
    throwables2 = new HashSet<Throwable>(throwables);
    throwables2.add(new Exception());
  }

  private Set<CompoundPropertyException> createSubjects() {
    Set<CompoundPropertyException> result = new HashSet<CompoundPropertyException>();
    for (boolean inOriginInitialization : booleans) {
      for (boolean closed : booleans) {
        for (String message : messages) {
          for (String propertyName : propertyNames) {
            for (Throwable t : throwables) {
              for (Set<PropertyException> elements : elementss) {
                CompoundPropertyException subject = new CompoundPropertyException(originMock, inOriginInitialization, propertyName, message, t);
                for (PropertyException element : elements) {
                  try {
                    subject.addElementException(element);
                  }
                  catch (Exception exc) {
                    // NOP
                  }
                }
                if (closed) {
                  subject.close();
                }
                result.add(subject);
              }
            }
          }
        }
      }
    }
    return result;
  }

  @After
  public void tearDown() throws Exception {
    originMock = null;
    messages = null;
    propertyNames = null;
    elementss = null;
    throwables = null;
    subjects = null;
    origins2 = null;
    propertyNames2 = null;
    messages2 = null;
  }

  public static void assertTypeInvariants(CompoundPropertyException subject) {
    PropertyExceptionTest.assertTypeInvariants(subject);
    assertTrue(subject.getElementExceptions().size() > 1 ? subject.getPropertyName() == null : true);
    assertNotNull(subject.getElementExceptions());
    assertFalse(subject.getElementExceptions().containsKey(EMPTY));
    assertFalse(subject.getElementExceptions().containsValue(null));
    for (Set<PropertyException> s : subject.getElementExceptions().values()) {
      assertFalse(s.isEmpty());
      assertFalse(s.contains(null));
    }
    for (Map.Entry<String, Set<PropertyException>> e : subject.getElementExceptions().entrySet()) {
      for (PropertyException pe : e.getValue()) {
        eqn(pe.getPropertyName(), e.getKey());
        assertTrue(subject.getOrigin() != null ? subject.getOrigin() == pe.getOrigin() : true);
        assertTrue(subject.getOriginType() != null ? subject.getOriginType() == pe.getOriginType() : true);
        assertFalse(pe instanceof CompoundPropertyException);
      }
    }
    assertTrue(subject.getPropertyName() != null ? subject.getElementExceptions().size() <= 1 : true);
    assertTrue(subject.getPropertyName() != null && subject.getElementExceptions().size() > 0 ?
               subject.getElementExceptions().keySet().contains(subject.getPropertyName()) : true);
  }

  private static boolean eqn(String s1, String s2) {
    return s1 == null ? s2 == null : s1.equals(s2);
  }

  private void testCompoundPropertyExceptionStringThrowable(String message, Throwable cause) {
    // execute
    CompoundPropertyException subject = new CompoundPropertyException(message, cause);
    // validate
    assertNull(subject.getOrigin());
    assertNull(subject.getOriginType());
    assertNull(subject.getPropertyName());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    assertFalse(subject.isClosed());
    assertTrue(subject.getElementExceptions().isEmpty());
    assertTypeInvariants(subject);
  }

  @Test
  public void testCompoundPropertyExceptionStringThrowable() {
    for (String message : messages) {
      for (Throwable t : throwables) {
        testCompoundPropertyExceptionStringThrowable(message, t);
      }
    }
  }

  private void testCompoundPropertyExceptionObjectStringStringThrowable(final Object origin,
                                                                      final String propertyName,
                                                                      final String message,
                                                                      final Throwable cause) {
    // execute
    CompoundPropertyException subject = new CompoundPropertyException(origin, propertyName, message, cause);
    // validate
    assertEquals(origin, subject.getOrigin());
    assertEquals(origin.getClass(), subject.getOriginType());
    assertEquals(propertyName, subject.getPropertyName());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    assertFalse(subject.isClosed());
    assertTrue(subject.getElementExceptions().isEmpty());
    assertTypeInvariants(subject);
  }

  @Test
  public void testCompoundPropertyExceptionObjectStringObjectStringThrowable() {
    for (String message : messages) {
      for (String propertyName : propertyNames) {
        for (Throwable t : throwables) {
          testCompoundPropertyExceptionObjectStringStringThrowable(originMock, propertyName, message, t);
        }
      }
    }
  }

  private void testCompoundPropertyExceptionObjectBooleanStringStringThrowable(final Object origin,
                                                                               final boolean inOriginInitialization,
                                                                               final String propertyName,
                                                                               final String message,
                                                                               final Throwable cause) {
    // execute
    CompoundPropertyException subject = new CompoundPropertyException(origin, inOriginInitialization, propertyName, message, cause);
    // validate
    assertTrue(inOriginInitialization ? subject.getOrigin() == null : subject.getOrigin() == origin);
    assertEquals(origin.getClass(), subject.getOriginType());
    assertEquals(propertyName, subject.getPropertyName());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    assertFalse(subject.isClosed());
    assertTrue(subject.getElementExceptions().isEmpty());
    assertTypeInvariants(subject);
  }

  @Test
  public void testCompoundPropertyExceptionObjectQBooleanStringStringThrowable() {
    for (boolean inOriginInitialization : booleans) {
      for (String message : messages) {
        for (String propertyName : propertyNames) {
          for (Throwable t : throwables) {
            testCompoundPropertyExceptionObjectBooleanStringStringThrowable(originMock, inOriginInitialization, propertyName, message, t);
          }
        }
      }
    }
  }

  private void testCompoundPropertyExceptionClassOfStringStringThrowable(final Class<?> originType,
                                                                         final String propertyName,
                                                                         final String message,
                                                                         final Throwable cause) {
     // execute
    CompoundPropertyException subject = new CompoundPropertyException(originType, propertyName, message, cause);
     // validate
     assertNull(subject.getOrigin());
     assertEquals(originType, subject.getOriginType());
     assertEquals(propertyName, subject.getPropertyName());
     assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
     assertEquals(cause, subject.getCause());
     assertFalse(subject.isClosed());
     assertTrue(subject.getElementExceptions().isEmpty());
     assertTypeInvariants(subject);
   }

  @Test
  public void testCompoundPropertyExceptionClassOfQStringStringThrowable() {
    for (String message : messages) {
      for (String propertyName : propertyNames) {
        for (Throwable t : throwables) {
          testCompoundPropertyExceptionClassOfStringStringThrowable(OriginStub.class, propertyName, message, t);
        }
      }
    }
  }

//  @Test
//  public void testHasPropertiesObjectStringStringThrowable() {
//    for (CompoundPropertyException subject : subjects) {
//      for (Object origin : origins2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              PropertyExceptionTest.testHasPropertiesObjectStringStringThrowable(subject, origin, propertyName, message, cause);
//              assertTypeInvariants(subject);
//            }
//          }
//        }
//      }
//    }
//  }
//
//  @Test
//  public void testHasPropertiesClassOfQStringStringThrowable() {
//    for (CompoundPropertyException subject : subjects) {
//      for (Class<?> originType : originTypes2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              PropertyExceptionTest.testHasPropertiesClassOfQStringStringThrowable(subject, originType, propertyName, message, cause);
//              assertTypeInvariants(subject);
//            }
//          }
//        }
//      }
//    }
//  }

  private static void testClose(CompoundPropertyException subject) {
    boolean oldClosed = subject.isClosed();
    // execute
    try {
      subject.close();
      assertTrue(subject.isClosed());
    }
    catch (IllegalStateException result) {
      assertTrue(oldClosed);
    }
  }

  @Test
  public void testClose() {
    for (CompoundPropertyException subject : subjects) {
      testClose(subject);
    }
  }

  private static void testIsEmpty(CompoundPropertyException subject) {
    // execute
    boolean result = subject.isEmpty();
    // validate
    assertEquals(subject.getElementExceptions().isEmpty(), result);
  }

  @Test
  public void testIsEmpty() {
    for (CompoundPropertyException subject : subjects) {
      testIsEmpty(subject);
    }
  }

  private static void testGetGeneralElementExceptions(CompoundPropertyException subject) {
    // execute
    Set<PropertyException> result = subject.getGeneralElementExceptions();
    // validate
    assertEquals(subject.getElementExceptions().get(null), result);
  }

  @Test
  public void testGetGeneralElementExceptions() {
    for (CompoundPropertyException subject : subjects) {
      testGetGeneralElementExceptions(subject);
    }
  }

  private static void testGetAllElementExceptions(CompoundPropertyException subject) {
    // execute
    Set<PropertyException> result = subject.getAllElementExceptions();
    // validate
    Set<PropertyException> expected = new HashSet<PropertyException>();
    for (Set<PropertyException> s : subject.getElementExceptions().values()) {
      expected.addAll(s);
    }
    assertEquals(expected, result);
  }

  @Test
  public void testGetAllElementExceptions() {
    for (CompoundPropertyException subject : subjects) {
      testGetAllElementExceptions(subject);
    }
  }

  private static void testAddElementException(CompoundPropertyException subject, PropertyException pExc) {
    boolean oldClosed = subject.isClosed();
    // execute
    try {
      subject.addElementException(pExc);
      assertTrue(subject.getElementExceptions().containsKey(pExc.getPropertyName()));
      assertTrue(subject.getElementExceptions().get(pExc.getPropertyName()).contains(pExc));
      assertFalse(oldClosed);
    }
    catch (IllegalStateException isExc) {
      assertTrue(oldClosed);
    }
    catch (IllegalArgumentException iaExc) {
      assertTrue((pExc instanceof CompoundPropertyException) ||
                 (pExc == null) ||
                 ((subject.getPropertyName() != null) && (! eqn(pExc.getPropertyName(), subject.getPropertyName()))) ||
                 (subject.getOrigin() != null && pExc.getOrigin() != subject.getOrigin()) ||
                 (subject.getOriginType() != null && pExc.getOriginType() != subject.getOriginType()));
    }
  }

  @Test
  public void testAddElementException() throws Exception {
    for (CompoundPropertyException subject : subjects) {
      testAddElementException(subject, null);

      PropertyExceptionTest pet = new PropertyExceptionTest();
      pet.setUp();
      for (PropertyException pe : pet.subjects) {
        testAddElementException(subject, pe);
      }
      pet.tearDown();
      SetterPropertyExceptionTest spet = new SetterPropertyExceptionTest();
      spet.setUp();
      for (SetterPropertyException spe : spet.subjects) {
        testAddElementException(subject, spe);
      }
      spet.tearDown();
      testAddElementException(subject, subject);
      testAddElementException(subject, new CompoundPropertyException(new OriginStub(), "stubProperty", "dada", null));
      testAddElementException(subject, new CompoundPropertyException(new OriginStub(), null, "dada", null));
    }
  }

  private static void testGetSize(CompoundPropertyException subject) {
    // execute
    int result = subject.getSize();
    // validate
    int expected = 0;
    for (Set<?> s : subject.getElementExceptions().values()) {
      expected += s.size();
    }
    assertEquals(expected, result);
  }

  @Test
  public void testGetSize() {
    for (CompoundPropertyException subject : subjects) {
      testGetSize(subject);
    }
  }

  private static void testGetAnElement(CompoundPropertyException subject) {
    // execute
    PropertyException result = subject.getAnElement();
    // validate
    assertTrue(result != null ? subject.contains(result) : true);
  }

  @Test
  public void testGetAnElement() {
    for (CompoundPropertyException subject : subjects) {
      testGetAnElement(subject);
    }
  }

  public static void testContainsPropertyException(CompoundPropertyException subject, PropertyException pe) {
    // execute
    boolean result = subject.contains(pe);
    // validate
    boolean expected = false;
    if ((pe != null) && subject.getElementExceptions().get(pe.getPropertyName()) != null) {
      for (PropertyException cand : subject.getElementExceptions().get(pe.getPropertyName())) {
        if (cand.like(pe)) {
          expected = true;
          break;
        }
      }
    }
    assertEquals(expected, result);
    PropertyExceptionTest.assertTypeInvariants(subject);
    assertTypeInvariants(subject);
  }

  @Test
  public void testContainsPropertyException() {
    for (CompoundPropertyException subject : subjects) {
      testContainsPropertyException(subject, null);
      testContainsPropertyException(subject, new PropertyException(OriginStub.class, "stubProperty", null, null));
      testContainsPropertyException(subject, new PropertyException(OriginStub.class, null, null, null));
      testContainsPropertyException(subject, new PropertyException(new OriginStub(), "stubProperty", null, null));
      testContainsPropertyException(subject, new PropertyException(new OriginStub(), null, null, null));
      for (Set<PropertyException> pes : subject.getElementExceptions().values()) {
        for (PropertyException pe : pes) {
          testContainsPropertyException(subject, pe);
        }
      }
    }
  }

//  public static void testContainsObjectStringStringThrowable(CompoundPropertyException subject, Object origin, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.contains(origin, propertyName, message, cause);
//    // validate
//    boolean expected = subject.getElementExceptions().containsKey(propertyName) &&
//                       existsIn(origin, propertyName, message, cause, subject.getElementExceptions().get(propertyName));
//    assertEquals(expected, result);
//    PropertyExceptionTest.assertTypeInvariants(subject);
//    assertTypeInvariants(subject);
//  }
//
//  private static boolean existsIn(Object origin, String propertyName, String message, Throwable cause, Set<PropertyException> set) {
//    for (PropertyException pe : set) {
//      if (pe.hasProperties(origin, propertyName, message, cause)) {
//        return true;
//      }
//    }
//    return false;
//  }
//
//  @Test
//  public void testContainsObjectStringStringThrowable() {
//    for (CompoundPropertyException subject : subjects) {
//      for (Object origin : origins2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              testContainsObjectStringStringThrowable(subject, origin, propertyName, message, cause);
//            }
//          }
//        }
//      }
//    }
//  }
//
//  public static void testContainsClassOfQStringStringThrowable(CompoundPropertyException subject, Class<?> originType, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.contains(originType, propertyName, message, cause);
//    // validate
//    boolean expected = subject.getElementExceptions().containsKey(propertyName) &&
//                       existsIn(originType, propertyName, message, cause, subject.getElementExceptions().get(propertyName));
//    assertEquals(expected, result);
//    PropertyExceptionTest.assertTypeInvariants(subject);
//    assertTypeInvariants(subject);
//  }
//
//  private static boolean existsIn(Class<?> originType, String propertyName, String message, Throwable cause, Set<PropertyException> set) {
//    for (PropertyException pe : set) {
//      if (pe.hasProperties(originType, propertyName, message, cause)) {
//        return true;
//      }
//    }
//    return false;
//  }
//
//  @Test
//  public void testContainsClassOfQStringStringThrowable() {
//    for (CompoundPropertyException subject : subjects) {
//      for (Class<?> originType : originTypes2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              testContainsClassOfQStringStringThrowable(subject, originType, propertyName, message, cause);
//            }
//          }
//        }
//      }
//    }
//  }
//
//  public static void testReportsOnObjectStringStringThrowable(CompoundPropertyException subject, Object origin, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.reportsOn(origin, propertyName, message, cause);
//    // validate
//    assertEquals(subject.contains(origin, propertyName, message, cause), result);
//    PropertyExceptionTest.assertTypeInvariants(subject);
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testReportsOnObjectStringStringThrowable() {
//    for (CompoundPropertyException subject : subjects) {
//      for (Object origin : origins2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              testReportsOnObjectStringStringThrowable(subject, origin, propertyName, message, cause);
//            }
//          }
//        }
//      }
//    }
//  }
//
//  public static void testReportsOnClassOfQStringStringThrowable(CompoundPropertyException subject, Class<?> originType, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.reportsOn(originType, propertyName, message, cause);
//    // validate
//    assertEquals(subject.contains(originType, propertyName, message, cause), result);
//    PropertyExceptionTest.assertTypeInvariants(subject);
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testReportsOnClassOfQStringStringThrowable() {
//    for (CompoundPropertyException subject : subjects) {
//      for (Class<?> originType : originTypes2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              testReportsOnClassOfQStringStringThrowable(subject, originType, propertyName, message, cause);
//            }
//          }
//        }
//      }
//    }
//  }

  private static void testThrowIfNotEmpty(CompoundPropertyException subject) {
    boolean oldEmpty = subject.isEmpty();
    // execute
    try {
      subject.throwIfNotEmpty();
      // validate not thrown
      assertTrue(oldEmpty);
      assertTrue(subject.isClosed());
    }
    catch (CompoundPropertyException result) {
      // validate thrown
      assertTrue(subject.getSize() > 1);
      assertEquals(subject, result);
      assertTrue(subject.isClosed());
    }
    catch (PropertyException result) {
      // validate thrown
      assertTrue(subject.getSize() == 1);
      assertEquals(subject.getAnElement(), result);
      assertTrue(subject.isClosed());
    }
  }

  @Test
  public void testThrowIfNotEmpty() {
    for (CompoundPropertyException subject : subjects) {
      testThrowIfNotEmpty(subject);
    }
  }

}

