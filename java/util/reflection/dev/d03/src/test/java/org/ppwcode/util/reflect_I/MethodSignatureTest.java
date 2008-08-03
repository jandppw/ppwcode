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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.AlternateStubClass;
import org.ppwcode.util.reflect_I.teststubs.StubClass;


public class MethodSignatureTest {

  List<Method> methods;

  List<Constructor<?>> constructors;

  List<MethodSignature> subjects;

  @Before
  public void setUp() throws Exception {
    methods = new LinkedList<Method>();
    methods.add(StubClass.class.getDeclaredMethod("stubMethod"));
    methods.add(StubClass.class.getDeclaredMethod("stubMethodWithReturn"));
    methods.add(StubClass.class.getDeclaredMethod("stubMethodWithException"));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Object.class));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", String.class));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Integer.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Class.class));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Integer.TYPE, Boolean.TYPE, Object.class, String.class));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Object.class, Float.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Serializable.class, Float.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Date.class));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Long.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Boolean.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubMethod", Byte.TYPE));
 // MUDO deal with [] array types
//    methods.add(StubClass.class.getDeclaredMethod("stubMethod(Object[])")));
    // static methods
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod"));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethodWithReturn"));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethodWithException"));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Object.class));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", String.class));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Integer.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Class.class));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Integer.TYPE, Boolean.TYPE, Object.class, String.class));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Object.class, Float.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Serializable.class, Float.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Date.class));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Long.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Boolean.TYPE));
    methods.add(StubClass.class.getDeclaredMethod("stubStaticMethod", Byte.TYPE));
    // MUDO deal with [] array types
//  methods.add(StubClass.class.getDeclaredMethod("stubMethod(Object[])")));

    constructors = new LinkedList<Constructor<?>>();
    constructors.add(StubClass.class.getDeclaredConstructor());
    constructors.add(StubClass.class.getDeclaredConstructor(Object.class));
    constructors.add(StubClass.class.getDeclaredConstructor(String.class));
    constructors.add(StubClass.class.getDeclaredConstructor(Integer.TYPE));
    constructors.add(StubClass.class.getDeclaredConstructor(Class.class));
    constructors.add(StubClass.class.getDeclaredConstructor(StubClass.class));
    constructors.add(StubClass.class.getDeclaredConstructor(Integer.TYPE, Boolean.TYPE, Object.class, String.class));
    constructors.add(StubClass.class.getDeclaredConstructor(Object.class, Object.class, Float.TYPE));
    constructors.add(StubClass.class.getDeclaredConstructor(Serializable.class, Serializable.class, Float.TYPE));
    constructors.add(StubClass.class.getDeclaredConstructor(Date.class));
    constructors.add(StubClass.class.getDeclaredConstructor(Long.TYPE));
    constructors.add(StubClass.class.getDeclaredConstructor(Boolean.TYPE));
    constructors.add(StubClass.class.getDeclaredConstructor(Byte.TYPE));
    constructors.add(AlternateStubClass.class.getDeclaredConstructor());


    subjects = new LinkedList<MethodSignature>();
    for (Method m : methods) {
      subjects.add(new MethodSignature(m));
    }
    for (Constructor<?> c : constructors) {
      subjects.add(new MethodSignature(c));
    }
    // doesn't exist
    subjects.add(new MethodSignature("noRealMethod()"));
  }

  @After
  public void tearDown() throws Exception {
    constructors = null;
    subjects = null;
    methods = null;
  }

  private void validateInvariants(MethodSignature ms) {
    assertNotNull(ms.getMethodName());
    assertNotSame("", ms.getMethodName());
    assertNotNull(ms.getParameterTypes());
    assertFalse(contains(ms.getParameterTypes(), null));
  }

  private boolean contains(Object[] array, Object something) {
    for (Object object : array) {
      if (object.equals(something)) {
        return true;
      }
    }
    return false;
  }

  @Test
  public void testHashCode() {
    for (MethodSignature ms : subjects) {
      // just call, nothing to test
      ms.hashCode();
      validateInvariants(ms);
    }
  }

  @Test
  public void testToString() {
    for (MethodSignature ms : subjects) {
      // just call, nothing to test
      ms.toString();
      validateInvariants(ms);
    }
  }

  @Test
  public void testEqualsObject() {
    for (MethodSignature ms1 : subjects) {
      for (MethodSignature ms2 : subjects) {
        testEqualsObject(ms1, ms2);
      }
      testEqualsObject(ms1, null);
      testEqualsObject(ms1, new Object());
    }
  }

  private void testEqualsObject(MethodSignature ms1, Object ms2) {
    boolean result = ms1.equals(ms2);
    boolean expected = (ms2 != null) && (ms2 instanceof MethodSignature) &&
      (ms1.getMethodName().equals(((MethodSignature)ms2).getMethodName())) &&
      Arrays.equals(ms1.getParameterTypes(), ((MethodSignature)ms2).getParameterTypes());
    Assert.assertEquals(expected, result);
    validateInvariants(ms1);
    if (ms2 instanceof MethodSignature) {
      validateInvariants((MethodSignature)ms2);
    }
  }

  @Test
  public void testMethodSignatureString() {
    testMethodSignatureString("stubMethod()", "stubMethod", new Class<?>[] {});
    testMethodSignatureString("stubMethod   ()", "stubMethod", new Class<?>[] {});
    testMethodSignatureString("stubMethod  (    )  ", "stubMethod", new Class<?>[] {});
    testMethodSignatureString("stubMethodWithReturn()", "stubMethodWithReturn", new Class<?>[] {});
    testMethodSignatureString("stubMethodWithException()", "stubMethodWithException", new Class<?>[] {});
    testMethodSignatureString("stubMethod(Object)", "stubMethod", new Class<?>[] {Object.class});
    testMethodSignatureString("stubMethod(String)", "stubMethod", new Class<?>[] {String.class});
    testMethodSignatureString("stubMethod (   String)  ", "stubMethod", new Class<?>[] {String.class});
    testMethodSignatureString("stubMethod(int)", "stubMethod", new Class<?>[] {Integer.TYPE});
    testMethodSignatureString("stubMethod(Class)", "stubMethod", new Class<?>[] {Class.class});
    testMethodSignatureString("stubMethod(int, boolean, Object, String)", "stubMethod", new Class<?>[] {Integer.TYPE, Boolean.TYPE, Object.class, String.class});
    testMethodSignatureString("stubMethod(int,boolean,    Object,String)", "stubMethod", new Class<?>[] {Integer.TYPE, Boolean.TYPE, Object.class, String.class});
    testMethodSignatureString("stubMethod(Object, float)", "stubMethod", new Class<?>[] {Object.class, Float.TYPE});
    testMethodSignatureString("stubMethod(java.io.Serializable, float)", "stubMethod", new Class<?>[] {Serializable.class, Float.TYPE});
    testMethodSignatureString("stubMethod(java.util.Date)", "stubMethod", new Class<?>[] {Date.class});
    testMethodSignatureString("stubMethod(long)", "stubMethod", new Class<?>[] {Long.TYPE});
    testMethodSignatureString("stubMethod(boolean)", "stubMethod", new Class<?>[] {Boolean.TYPE});
    testMethodSignatureString("stubMethod(byte)", "stubMethod", new Class<?>[] {Byte.TYPE});
    testMethodSignatureString("doesntExist()", "doesntExist", new Class<?>[] {});
 // MUDO deal with [] array types
//    testMethodSignatureString("stubMethod(Object[])", "stubMethod", new Class<?>[] {});
    testMethodSignatureStringProblem("stubMethod");
    testMethodSignatureStringProblem("stubMethod(");
    testMethodSignatureStringProblem("stubMethod)");
    testMethodSignatureStringProblem("stubMethod(int");
    testMethodSignatureStringProblem("stubMethod(int Object)");
    testMethodSignatureStringProblem("stubMethod(Object,,int)");
  }

  private void testMethodSignatureString(String sig, String methodName, Class<?>[] pTypes) {
    MethodSignature subject = new MethodSignature(sig);
    assertEquals(methodName, subject.getMethodName());
    assertArrayEquals(pTypes, subject.getParameterTypes());
    validateInvariants(subject);
  }

  private void testMethodSignatureStringProblem(String sig) {
    try {
      new MethodSignature(sig);
      fail();
    }
    catch (AssertionError exc) {
      // NOP, normal
    }
  }

  @Test
  public void testMethodSignatureMethod() {
    for (Method m : methods) {
      MethodSignature result = new MethodSignature(m);
      assertEquals(m.getName(), result.getMethodName());
      Assert.assertArrayEquals(m.getParameterTypes(), result.getParameterTypes());
      validateInvariants(result);
    }
  }

  @Test
  public void testMethodSignatureConstructor() {
    for (Constructor<?> c : constructors) {
      MethodSignature result = new MethodSignature(c);
      assertEquals(c.getClass().getSimpleName(), result.getMethodName());
      Assert.assertArrayEquals(c.getParameterTypes(), result.getParameterTypes());
      validateInvariants(result);
    }
  }

  @Test
  public void testGetNumberOfParameters() {
    for (MethodSignature ms : subjects) {
      int result = ms.getNumberOfParameters();
      assertEquals(ms.getParameterTypes().length, result);
      validateInvariants(ms);
    }
  }

  @Test
  public void testGetParameterTypeNames() {
    for (MethodSignature ms : subjects) {
      String[] result = ms.getParameterTypeNames();
      assertNotNull(result);
      assertEquals(ms.getParameterTypes().length, result.length);
      assertFalse(contains(result, null));
      assertFalse(contains(result, ""));
      Class<?>[] paramT = ms.getParameterTypes();
      for (int i = 0; i < result.length; i++) {
        result[i].equals(paramT[i].getCanonicalName());
      }
      validateInvariants(ms);
    }
  }

}

