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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.util.reflect_I.PropertyHelpers.hasProperty;
import static org.ppwcode.vernacular.exception_III.ApplicationException.DEFAULT_MESSAGE_KEY;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyExceptionTest.OriginStub;


public class SetterPropertyExceptionTest {

  public final static String EMPTY = "";

  private Set<OriginStub> origins;
  private Set<String> propertyNames; // not empty
  private Set<Object> propertyValues;
  private Set<Object> vetoedValues;
  private Set<String> messages; // not empty
  private Set<Throwable> throwables;
  private final boolean[] booleans = {true, false};
  public Set<SetterPropertyException> subjects;

  private Set<Object> origins2;
  private Set<Class<?>> originTypes2;
  private Set<String> propertyNames2; // not empty
  private Set<String> messages2; // not empty
  private Set<Throwable> throwables2;

  @Before
  public void setUp() throws Exception {
//    originMock = new OriginStub();
    messages = new HashSet<String>();
    messages.add(null);
    messages.add("stub message");
    propertyNames = new HashSet<String>();
    // null not allowed as propertyname
    propertyNames.add("stubProperty");
    propertyValues = new HashSet<Object>();
    propertyValues.add(null);
    propertyValues.add(new Object());
    propertyValues.add(new String());
    propertyValues.add(new Date());
    vetoedValues = new HashSet<Object>();
    vetoedValues.add(null);
    vetoedValues.add(new Object());
    vetoedValues.add(new String());
    vetoedValues.add(new Date());
    throwables = new HashSet<Throwable>();
    throwables.add(null);
    throwables.add(new Throwable());
    origins = new HashSet<OriginStub>();
    for (Object propertyValue : propertyValues) {
      OriginStub origin = new OriginStub();
      origin.setStubProperty(propertyValue);
      origins.add(origin);
    }
    subjects = createSubjects();
    origins2 = new HashSet<Object>(origins);
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

  private Set<SetterPropertyException> createSubjects() {
    Set<SetterPropertyException> result = new HashSet<SetterPropertyException>();
    for (boolean inOriginInitialization : booleans) {
      for (String message : messages) {
        for (String propertyName : propertyNames) {
          for (Object origin : origins) {
            for (Throwable t : throwables) {
              for (Object vetoedValue : vetoedValues) {
                result.add(new SetterPropertyException(origin, inOriginInitialization, propertyName, vetoedValue, message, t));
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
    origins = null;
    messages = null;
    propertyNames = null;
    propertyValues = null;
    throwables = null;
    subjects = null;
    origins2 = null;
    propertyNames2 = null;
    messages2 = null;
  }

  public static void assertTypeInvariants(SetterPropertyException subject) {
    PropertyExceptionTest.assertTypeInvariants(subject);
    assertNotNull(subject.getPropertyName());
    assertTrue(subject.getOrigin() == null ? subject.getPropertyValue() == null : true);
  }

  private void testSetterPropertyExceptionObjectStringObjectStringThrowable(final Object origin,
                                                                      final String propertyName,
                                                                      final Object vetoedValue,
                                                                      final String message,
                                                                      final Throwable cause)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    // execute
    SetterPropertyException subject = new SetterPropertyException(origin, propertyName, vetoedValue, message, cause);
    // validate
    assertEquals(origin, subject.getOrigin());
    assertEquals(origin.getClass(), subject.getOriginType());
    assertEquals(propertyName, subject.getPropertyName());
    assertEquals(PropertyUtils.getProperty(origin, propertyName), subject.getPropertyValue());
    assertEquals(vetoedValue, subject.getVetoedValue());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    PropertyExceptionTest.assertTypeInvariants(subject);
    assertTypeInvariants(subject);
  }

  @Test
  public void testSetterPropertyExceptionObjectStringObjectStringThrowable()
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    for (Object origin : origins) {
      for (String message : messages) {
        for (String propertyName : propertyNames) {
          for (Object vetoedValue : vetoedValues) {
            for (Throwable t : throwables) {
              testSetterPropertyExceptionObjectStringObjectStringThrowable(origin, propertyName, vetoedValue, message, t);
            }
          }
        }
      }
    }
  }

  private void testSetterPropertyExceptionObjectBooleanStringObjectStringThrowable(final Object origin,
                                                                                   final boolean inOriginInitialization,
                                                                                   final String propertyName,
                                                                                   final Object vetoedValue,
                                                                                   final String message,
                                                                                   final Throwable cause)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    // execute
    SetterPropertyException subject = new SetterPropertyException(origin, inOriginInitialization, propertyName, vetoedValue, message, cause);
    // validate
    assertTrue(inOriginInitialization ? subject.getOrigin() == null : subject.getOrigin() == origin);
    assertEquals(origin.getClass(), subject.getOriginType());
    assertEquals(propertyName, subject.getPropertyName());
    assertEquals(inOriginInitialization ? null : PropertyUtils.getProperty(origin, propertyName),
                 subject.getPropertyValue());
    assertEquals(vetoedValue, subject.getVetoedValue());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    PropertyExceptionTest.assertTypeInvariants(subject);
    assertTypeInvariants(subject);
  }

  @Test
  public void testSetterPropertyExceptionObjectBooleanStringObjectStringThrowable()
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    for (boolean inOriginInitialization : booleans) {
      for (Object origin : origins) {
        for (String message : messages) {
          for (String propertyName : propertyNames) {
            for (Object vetoedValue : vetoedValues) {
              for (Throwable t : throwables) {
                testSetterPropertyExceptionObjectBooleanStringObjectStringThrowable(origin, inOriginInitialization, propertyName, vetoedValue, message, t);
              }
            }
          }
        }
      }
    }
  }

//  private void testSetterPropertyExceptionClassOfQStringObjectStringThrowable(final Class<?> originType,
//                                                                              final String propertyName,
//                                                                              final Object vetoedValue,
//                                                                              final String message,
//                                                                              final Throwable cause) {
//    // execute
//    SetterPropertyException subject = new SetterPropertyException(originType, propertyName, vetoedValue, message, cause);
//    // validate
//    assertNull(subject.getOrigin());
//    assertEquals(originType, subject.getOriginType());
//    assertEquals(propertyName, subject.getPropertyName());
//    assertNull(subject.getPropertyValue());
//    assertEquals(vetoedValue, subject.getVetoedValue());
//    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
//    assertEquals(cause, subject.getCause());
//    PropertyExceptionTest.assertTypeInvariants(subject);
//    assertTypeInvariants(subject);
//  }

//  Method no longer exists
//  @Test
//  public void testSetterPropertyExceptionClassOfQStringObjectStringThrowable() {
//    for (String message : messages) {
//      for (String propertyName : propertyNames) {
//        for (Object vetoedValue : vetoedValues) {
//          for (Throwable t : throwables) {
//            testSetterPropertyExceptionClassOfQStringObjectStringThrowable(OriginStub.class, propertyName, vetoedValue, message, t);
//          }
//        }
//      }
//    }
//  }

  public static void testLike(SetterPropertyException subject, PropertyException other) {
    // execute
    boolean result = subject.like(other);
    // validate
    ValuePropertyExceptionTest.testLike(subject, other);
    assertTrue(result ? eqn(subject.getVetoedValue(), ((SetterPropertyException)other).getVetoedValue()) : true);
    assertTypeInvariants(subject);
  }

  private static boolean eqn(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  @Test
  public void testLike() {
    for (SetterPropertyException subject : subjects) {
      for (Object origin : origins2) {
        if (origin != null) {
          for (String propertyName : propertyNames2) {
            if ((propertyName == null) || hasProperty(origin.getClass(), propertyName)) {
              for (String message : messages2) {
                if ((message == null) || (! message.equals(""))) {
                  for (Throwable cause : throwables2) {
                    testLike(subject, new PropertyException(origin, propertyName, message, cause));
                    testLike(subject, new ValuePropertyException(origin, propertyName, message, cause));
                    for (Object vetoedValue : vetoedValues) {
                      testLike(subject, new SetterPropertyException(origin, propertyName, vetoedValue, message, cause));
                    }
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

//  @Test
//  public void testHasPropertiesObjectStringStringThrowable() {
//    for (SetterPropertyException subject : subjects) {
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
//    for (SetterPropertyException subject : subjects) {
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
//
//  @Test
//  public void testReportsOnObjectStringStringThrowable() {
//    for (SetterPropertyException subject : subjects) {
//      for (Object origin : origins2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              PropertyExceptionTest.testReportsOnObjectStringStringThrowable(subject, origin, propertyName, message, cause);
//              assertTypeInvariants(subject);
//            }
//          }
//        }
//      }
//    }
//  }
//
//  @Test
//  public void testReportsOnClassOfQStringStringThrowable() {
//    for (SetterPropertyException subject : subjects) {
//      for (Class<?> originType : originTypes2) {
//        for (String propertyName : propertyNames2) {
//          for (String message : messages2) {
//            for (Throwable cause : throwables2) {
//              PropertyExceptionTest.testReportsOnClassOfQStringStringThrowable(subject, originType, propertyName, message, cause);
//              assertTypeInvariants(subject);
//            }
//          }
//        }
//      }
//    }
//  }

}

