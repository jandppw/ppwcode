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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.util.reflect_I.PropertyHelpers.hasProperty;
import static org.ppwcode.vernacular.exception_III.ApplicationException.DEFAULT_MESSAGE_KEY;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PropertyExceptionTest {

  public static class OriginStub {

    public final Object getStubProperty() {
      return $stubProperty;
    }


    public final void setStubProperty(Object stubProperty) {
      $stubProperty = stubProperty;
    }

    private Object $stubProperty;

  }

  private OriginStub originMock;
  private Set<String> propertyNames; // not empty
  private Set<String> messages; // not empty
  private Set<Throwable> throwables;
  public Set<PropertyException> subjects;

  private Set<Object> origins2;
  private Set<Class<?>> originTypes2;
  private Set<String> propertyNames2; // not empty
  private Set<String> messages2; // not empty
  private Set<Throwable> throwables2;

  @Before
  public void setUp() throws Exception {
    originMock = new OriginStub();
    messages = new HashSet<String>();
    messages.add(null);
    messages.add("stub message");
    propertyNames = new HashSet<String>();
    propertyNames.add(null);
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

  private Set<PropertyException> createSubjects() {
    Set<PropertyException> result = new HashSet<PropertyException>();
    for (String message : messages) {
      for (String propertyName : propertyNames) {
        for (Throwable t : throwables) {
          result.add(new PropertyException(originMock, propertyName, message, t));
          result.add(new PropertyException(originMock.getClass(), propertyName, message, t));
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
    throwables = null;
    subjects = null;
    origins2 = null;
    propertyNames2 = null;
    messages2 = null;
  }

  public final static String EMPTY = "";

  public static void assertTypeInvariants(PropertyException subject) {
    assertTrue(subject.getMessage() == null || ! subject.getMessage().equals(EMPTY));
    assertTrue(subject.getOrigin() != null ? subject.getOriginType() == subject.getOrigin().getClass() : true);
    assertTrue(subject.getPropertyName() != null ? hasProperty(subject.getOriginType(), subject.getPropertyName()) : true);
  }

//  private void testPropertyExceptionStringThrowable(String message, Throwable cause) {
//    // execute
//    PropertyException subject = new PropertyException(message, cause);
//    // validate
//    assertNull(subject.getOrigin());
//    assertNull(subject.getOriginType());
//    assertNull(subject.getPropertyName());
//    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
//    assertEquals(cause, subject.getCause());
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testPropertyExceptionStringThrowable() {
//    for (String message : messages) {
//      for (Throwable t : throwables) {
//        testPropertyExceptionStringThrowable(message, t);
//      }
//    }
//  }

  private void testPropertyExceptionObjectStringStringThrowable(final Object origin,
                                                                final String propertyName,
                                                                final String message,
                                                                final Throwable cause) {
    // execute
    PropertyException subject = new PropertyException(origin, propertyName, message, cause);
    // validate
    assertEquals(origin, subject.getOrigin());
    assertEquals(origin.getClass(), subject.getOriginType());
    assertEquals(propertyName, subject.getPropertyName());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    assertTypeInvariants(subject);
  }

  @Test
  public void testPropertyExceptionObjectStringStringThrowable() {
    for (String message : messages) {
      for (String propertyName : propertyNames) {
        for (Throwable t : throwables) {
          testPropertyExceptionObjectStringStringThrowable(originMock, propertyName, message, t);
        }
      }
    }
  }

//  private void testPropertyExceptionObjectBooleanStringStringThrowable(final Object origin,
//                                                                       final boolean inOriginInitialization,
//                                                                       final String propertyName,
//                                                                       final String message,
//                                                                       final Throwable cause) {
//    // execute
//    PropertyException subject = new PropertyException(origin, inOriginInitialization, propertyName, message, cause);
//    // validate
//    assertTrue(inOriginInitialization ? subject.getOrigin() == null : subject.getOrigin() == origin);
//    assertEquals(origin.getClass(), subject.getOriginType());
//    assertEquals(propertyName, subject.getPropertyName());
//    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
//    assertEquals(cause, subject.getCause());
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testPropertyExceptionObjectQBooleanStringStringThrowable() {
//    for (boolean inOriginInitialization : booleans) {
//      for (String message : messages) {
//        for (String propertyName : propertyNames) {
//          for (Throwable t : throwables) {
//            testPropertyExceptionObjectBooleanStringStringThrowable(originMock, inOriginInitialization, propertyName, message, t);
//          }
//        }
//      }
//    }
//  }

  private void testPropertyExceptionClassOfStringStringThrowable(final Class<?> originType,
                                                                final String propertyName,
                                                                final String message,
                                                                final Throwable cause) {
    // execute
    PropertyException subject = new PropertyException(originType, propertyName, message, cause);
    // validate
    assertNull(subject.getOrigin());
    assertEquals(originType, subject.getOriginType());
    assertEquals(propertyName, subject.getPropertyName());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    assertTypeInvariants(subject);
  }

  @Test
  public void testPropertyExceptionClassOfQStringStringThrowable() {
    for (String message : messages) {
      for (String propertyName : propertyNames) {
        for (Throwable t : throwables) {
          testPropertyExceptionClassOfStringStringThrowable(OriginStub.class, propertyName, message, t);
        }
      }
    }
  }

  private static boolean eqn(String s1, String s2) {
    return s1 == null ? s2 == null : s1.equals(s2);
  }

  public static void testLike(PropertyException subject, PropertyException other) {
    // execute
    boolean result = subject.like(other);
    // validate
    assertTrue(result ? ((other != null) &&
        (other.getClass() == subject.getClass()) &&
        (subject.getOrigin() == other.getOrigin()) &&
        (subject.getOriginType() == other.getOriginType()) &&
        eqn(subject.getPropertyName(), other.getPropertyName()) &&
        eqn(subject.getMessage(), other.getMessage()) &&
        (subject.getCause() == other.getCause())) : true);
    assertTypeInvariants(subject);
  }

  @Test
  public void testLike() {
    for (PropertyException subject : subjects) {
      for (Object origin : origins2) {
        if (origin != null) {
          for (String propertyName : propertyNames2) {
            if ((propertyName == null) || hasProperty(origin.getClass(), propertyName)) {
              for (String message : messages2) {
                if ((message == null) || (! message.equals(""))) {
                  for (Throwable cause : throwables2) {
                    testLike(subject, new PropertyException(origin, propertyName, message, cause));
                  }
                }
              }
            }
          }
        }
      }
      testLike(subject, null);
    }
  }


//  public static void testHasPropertiesObjectStringStringThrowable(PropertyException subject, Object origin, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.hasProperties(origin, propertyName, message, cause);
//    // validate
//    assertEquals(((subject.getOrigin() == origin) && eqn(subject.getPropertyName(), propertyName) &&
//        eqn(subject.getMessage(), message) && (subject.getCause() == cause)), result);
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testHasPropertiesObjectStringStringThrowable() {
//    for (PropertyException subject : subjects) {
//      for (Object origin : origins2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              testHasPropertiesObjectStringStringThrowable(subject, origin, propertyName, message, cause);
//            }
//          }
//        }
//      }
//    }
//  }
//
//  public static void testHasPropertiesClassOfQStringStringThrowable(PropertyException subject, Class<?> originType, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.hasProperties(originType, propertyName, message, cause);
//    // validate
//    assertEquals(((subject.getOriginType() == originType) && eqn(subject.getPropertyName(), propertyName) &&
//        eqn(subject.getMessage(), message) && (subject.getCause() == cause)), result);
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testHasPropertiesClassOfQStringStringThrowable() {
//    for (PropertyException subject : subjects) {
//      for (Class<?> originType : originTypes2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              testHasPropertiesClassOfQStringStringThrowable(subject, originType, propertyName, message, cause);
//            }
//          }
//        }
//      }
//    }
//  }
//
//  public static void testReportsOnObjectStringStringThrowable(PropertyException subject, Object origin, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.reportsOn(origin, propertyName, message, cause);
//    // validate PROTECTED
//    assertEquals(subject.hasProperties(origin, propertyName, message, cause), result);
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testReportsOnObjectStringStringThrowable() {
//    for (PropertyException subject : subjects) {
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
//  public static void testReportsOnClassOfQStringStringThrowable(PropertyException subject, Class<?> originType, String propertyName, String message, Throwable cause) {
//    // execute
//    boolean result = subject.reportsOn(originType, propertyName, message, cause);
//    // validate PROTECTED
//    assertEquals(subject.hasProperties(originType, propertyName, message, cause), result);
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testReportsOnClassOfQStringStringThrowable() {
//    for (PropertyException subject : subjects) {
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

}

