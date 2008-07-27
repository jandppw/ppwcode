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


import static java.util.Arrays.deepEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ppwcode.util.reflect_I.MethodHelpers.constructor;
import static org.ppwcode.util.reflect_I.MethodHelpers.hasMethod;
import static org.ppwcode.util.reflect_I.MethodHelpers.method;
import static org.ppwcode.util.reflect_I.MethodHelpers.methodHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MethodHelpersTest {

  private StubClass stub;

  @Before
  public void setUp() throws Exception {
    stub = new StubClass();
  }

  @After
  public void tearDown() throws Exception {
    stub = null;
  }

  @Test
  public void testMethodHelper1() throws CannotParseSignatureException, NoSuchMethodException {
    // dynamic method
    testMethodHelper(StubClass.class, "stubMethod()");
    testMethodHelper(StubClass.class, "stubMethodWithReturn()");
    testMethodHelper(StubClass.class, "stubMethodWithException()");
    testMethodHelper(StubClass.class, "stubMethod(Object)");
    testMethodHelper(StubClass.class, "stubMethod(String)");
    testMethodHelper(StubClass.class, "stubMethod(int)");
    testMethodHelper(StubClass.class, "stubMethod(Class)");
    testMethodHelper(StubClass.class, "stubMethod(int, boolean, Object, String)");
    testMethodHelper(StubClass.class, "stubMethod(int,boolean,    Object,String)");
    testMethodHelper(StubClass.class, "stubMethod(Object, float)");
    testMethodHelper(StubClass.class, "stubMethod(java.io.Serializable, float)");
    testMethodHelper(StubClass.class, "stubMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testMethodHelper(StubClass.class, "stubMethod(Object[])");
    // static methods
    testMethodHelper(StubClass.class, "stubStaticMethod()");
    testMethodHelper(StubClass.class, "stubStaticMethodWithReturn()");
    testMethodHelper(StubClass.class, "stubStaticMethodWithException()");
    testMethodHelper(StubClass.class, "stubStaticMethod(Object)");
    testMethodHelper(StubClass.class, "stubStaticMethod(String)");
    testMethodHelper(StubClass.class, "stubStaticMethod(int)");
    testMethodHelper(StubClass.class, "stubStaticMethod(Class)");
    testMethodHelper(StubClass.class, "stubStaticMethod(int, boolean, Object, String)");
    testMethodHelper(StubClass.class, "stubStaticMethod(int,boolean,    Object,String)");
    testMethodHelper(StubClass.class, "stubStaticMethod(Object, float)");
    testMethodHelper(StubClass.class, "stubStaticMethod(java.io.Serializable, float)");
    testMethodHelper(StubClass.class, "stubStaticMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testMethodHelper(StubClass.class, "stubStaticMethod(Object[])");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper2() throws CannotParseSignatureException, NoSuchMethodException {
    methodHelper(StubClass.class, "methodDoesntExist()");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper3() throws CannotParseSignatureException, NoSuchMethodException {
    methodHelper(StubClass.class, "stubMethod(org.ppwcode.util.reflect_I.StubClass)");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper4() throws CannotParseSignatureException, NoSuchMethodException {
    methodHelper(StubClass.class, "stubStaticMethod(org.ppwcode.util.reflect_I.StubClass)");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper5() throws CannotParseSignatureException, NoSuchMethodException {
    methodHelper(StubClass.class, "StubClass(Object, Object, float)");
  }

  public void testMethodHelper(Class<?> type, String signature) throws CannotParseSignatureException, NoSuchMethodException {
    Method result = methodHelper(type, signature);
    assertNotNull(result);
    assertEquals(type, result.getDeclaringClass());
    MethodSignature msig = new MethodSignature(signature);
    assertEquals(msig.getMethodName(), result.getName());
    assertArrayEquals(msig.getParameterTypes(), result.getParameterTypes());
  }

  @Test
  public void testMethodClassOfQString1() throws CannotParseSignatureException {
    // dynamic method
    testMethodClassOfQString(StubClass.class, "stubMethod()");
    testMethodClassOfQString(StubClass.class, "stubMethodWithReturn()");
    testMethodClassOfQString(StubClass.class, "stubMethodWithException()");
    testMethodClassOfQString(StubClass.class, "stubMethod(Object)");
    testMethodClassOfQString(StubClass.class, "stubMethod(String)");
    testMethodClassOfQString(StubClass.class, "stubMethod(int)");
    testMethodClassOfQString(StubClass.class, "stubMethod(Class)");
    testMethodClassOfQString(StubClass.class, "stubMethod(int, boolean, Object, String)");
    testMethodClassOfQString(StubClass.class, "stubMethod(int,boolean,    Object,String)");
    testMethodClassOfQString(StubClass.class, "stubMethod(Object, float)");
    testMethodClassOfQString(StubClass.class, "stubMethod(java.io.Serializable, float)");
    testMethodClassOfQString(StubClass.class, "stubMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testMethodClassOfQString(StubClass.class, "stubMethod(Object[])");
    // static methods
    testMethodClassOfQString(StubClass.class, "stubStaticMethod()");
    testMethodClassOfQString(StubClass.class, "stubStaticMethodWithReturn()");
    testMethodClassOfQString(StubClass.class, "stubStaticMethodWithException()");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(Object)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(String)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(int)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(Class)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(int, boolean, Object, String)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(int,boolean,    Object,String)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(Object, float)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(java.io.Serializable, float)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testMethodClassOfQString(StubClass.class, "stubStaticMethod(Object[])");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString2() throws CannotParseSignatureException {
    method(StubClass.class, "methodDoesntExist()");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString3() throws CannotParseSignatureException {
    method(StubClass.class, "stubMethod(org.ppwcode.util.reflect_I.StubClass)");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString4() throws CannotParseSignatureException {
    method(StubClass.class, "stubStaticMethod(org.ppwcode.util.reflect_I.StubClass)");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString5() throws CannotParseSignatureException {
    method(StubClass.class, "StubClass(Object, Object, float)");
  }

  public void testMethodClassOfQString(Class<?> type, String signature) throws CannotParseSignatureException {
    Method result = method(type, signature);
    assertNotNull(result);
    assertEquals(type, result.getDeclaringClass());
    MethodSignature msig = new MethodSignature(signature);
    assertEquals(msig.getMethodName(), result.getName());
    assertArrayEquals(msig.getParameterTypes(), result.getParameterTypes());
  }

  @Test
  public void testMethodObjectString1() throws CannotParseSignatureException {
    // dynamic method
    testMethodObjectString(stub, "stubMethod()");
    testMethodObjectString(stub, "stubMethodWithReturn()");
    testMethodObjectString(stub, "stubMethodWithException()");
    testMethodObjectString(stub, "stubMethod(Object)");
    testMethodObjectString(stub, "stubMethod(String)");
    testMethodObjectString(stub, "stubMethod(int)");
    testMethodObjectString(stub, "stubMethod(Class)");
    testMethodObjectString(stub, "stubMethod(int, boolean, Object, String)");
    testMethodObjectString(stub, "stubMethod(int,boolean,    Object,String)");
    testMethodObjectString(stub, "stubMethod(Object, float)");
    testMethodObjectString(stub, "stubMethod(java.io.Serializable, float)");
    testMethodObjectString(stub, "stubMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testMethodObjectString(stub, "stubMethod(Object[])");
    // static methods
    testMethodObjectString(stub, "stubStaticMethod()");
    testMethodObjectString(stub, "stubStaticMethodWithReturn()");
    testMethodObjectString(stub, "stubStaticMethodWithException()");
    testMethodObjectString(stub, "stubStaticMethod(Object)");
    testMethodObjectString(stub, "stubStaticMethod(String)");
    testMethodObjectString(stub, "stubStaticMethod(int)");
    testMethodObjectString(stub, "stubStaticMethod(Class)");
    testMethodObjectString(stub, "stubStaticMethod(int, boolean, Object, String)");
    testMethodObjectString(stub, "stubStaticMethod(int,boolean,    Object,String)");
    testMethodObjectString(stub, "stubStaticMethod(Object, float)");
    testMethodObjectString(stub, "stubStaticMethod(java.io.Serializable, float)");
    testMethodObjectString(stub, "stubStaticMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testMethodObjectString(stub, "stubStaticMethod(Object[])");
  }

  @Test(expected = AssertionError.class)
  public void testMethodObjectString2() throws CannotParseSignatureException {
    method(stub, "methodDoesntExist()");
  }

  @Test(expected = AssertionError.class)
  public void testMethodObjectString3() throws CannotParseSignatureException {
    method(stub, "stubMethod(org.ppwcode.util.reflect_I.StubClass)");
  }

  @Test(expected = AssertionError.class)
  public void testMethodObjectString4() throws CannotParseSignatureException {
    method(stub, "stubStaticMethod(org.ppwcode.util.reflect_I.StubClass)");
  }

  @Test(expected = AssertionError.class)
  public void testMethodObjectString5() throws CannotParseSignatureException {
    method(stub, "StubClass(Object, Object, float)");
  }

  public void testMethodObjectString(Object o, String signature) throws CannotParseSignatureException {
    Method result = method(o, signature);
    assertNotNull(result);
    assertEquals(o.getClass(), result.getDeclaringClass());
    MethodSignature msig = new MethodSignature(signature);
    assertEquals(msig.getMethodName(), result.getName());
    assertArrayEquals(msig.getParameterTypes(), result.getParameterTypes());
  }

  @Test
  public void testHasMethodClassOfQString() throws CannotParseSignatureException, SecurityException {
    // dynamic method
    testHasMethodClassOfQString(StubClass.class, "stubMethod()");
    testHasMethodClassOfQString(StubClass.class, "stubMethodWithReturn()");
    testHasMethodClassOfQString(StubClass.class, "stubMethodWithException()");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(Object)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(String)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(int)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(Class)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(int, boolean, Object, String)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(int,boolean,    Object,String)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(Object, float)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(java.io.Serializable, float)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testHasMethodClassOfQString(StubClass.class, "stubMethod(Object[])");
    // static methods
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod()");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethodWithReturn()");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethodWithException()");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(Object)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(String)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(int)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(Class)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(int, boolean, Object, String)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(int,boolean,    Object,String)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(Object, float)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(java.io.Serializable, float)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(Object[])");
    // methods that don't exist
    testHasMethodClassOfQString(StubClass.class, "methodDoesntExist()");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(org.ppwcode.util.reflect_I.StubClass)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(org.ppwcode.util.reflect_I.StubClass)");
    testHasMethodClassOfQString(StubClass.class, "StubClass(Object, Object, float)");
  }

  public void testHasMethodClassOfQString(Class<?> type, String signature) throws CannotParseSignatureException, SecurityException {
    boolean result = hasMethod(type, signature);
    assertEquals(exists(type.getDeclaredMethods(), signature), result);
  }

  @Test
  public void testHasMethodObjectString() throws CannotParseSignatureException, SecurityException {
    // dynamic method
    testHasMethodObjectString(stub, "stubMethod()");
    testHasMethodObjectString(stub, "stubMethodWithReturn()");
    testHasMethodObjectString(stub, "stubMethodWithException()");
    testHasMethodObjectString(stub, "stubMethod(Object)");
    testHasMethodObjectString(stub, "stubMethod(String)");
    testHasMethodObjectString(stub, "stubMethod(int)");
    testHasMethodObjectString(stub, "stubMethod(Class)");
    testHasMethodObjectString(stub, "stubMethod(int, boolean, Object, String)");
    testHasMethodObjectString(stub, "stubMethod(int,boolean,    Object,String)");
    testHasMethodObjectString(stub, "stubMethod(Object, float)");
    testHasMethodObjectString(stub, "stubMethod(java.io.Serializable, float)");
    testHasMethodObjectString(stub, "stubMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testHasMethodObjectString(stub, "stubMethod(Object[])");
    // static methods
    testHasMethodObjectString(stub, "stubStaticMethod()");
    testHasMethodObjectString(stub, "stubStaticMethodWithReturn()");
    testHasMethodObjectString(stub, "stubStaticMethodWithException()");
    testHasMethodObjectString(stub, "stubStaticMethod(Object)");
    testHasMethodObjectString(stub, "stubStaticMethod(String)");
    testHasMethodObjectString(stub, "stubStaticMethod(int)");
    testHasMethodObjectString(stub, "stubStaticMethod(Class)");
    testHasMethodObjectString(stub, "stubStaticMethod(int, boolean, Object, String)");
    testHasMethodObjectString(stub, "stubStaticMethod(int,boolean,    Object,String)");
    testHasMethodObjectString(stub, "stubStaticMethod(Object, float)");
    testHasMethodObjectString(stub, "stubStaticMethod(java.io.Serializable, float)");
    testHasMethodObjectString(stub, "stubStaticMethod(java.util.Date)");
 // MUDO deal with [] array types
//    testHasMethodObjectString(stub, "stubStaticMethod(Object[])");
    // methods that don't exist
    testHasMethodObjectString(stub, "methodDoesntExist()");
    testHasMethodObjectString(stub, "stubMethod(org.ppwcode.util.reflect_I.StubClass)");
    testHasMethodObjectString(stub, "stubStaticMethod(org.ppwcode.util.reflect_I.StubClass)");
    testHasMethodObjectString(stub, "StubClass(Object, Object, float)");
  }

  public void testHasMethodObjectString(Object o, String signature) throws CannotParseSignatureException, SecurityException {
    boolean result = hasMethod(o, signature);
    assertEquals(exists(o.getClass().getDeclaredMethods(), signature), result);
  }

  private Object exists(Method[] declaredMethods, String signature) throws CannotParseSignatureException {
    MethodSignature msig = new MethodSignature(signature);
    for (Method method : declaredMethods) {
      if (method.getName().equals(msig.getMethodName()) &&
          deepEquals(method.getParameterTypes(), msig.getParameterTypes())) {
        return true;
      }
    }
    return false;
  }

  @Test
  public void testConstructor1() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass()");
  }

  @Test
  public void testConstructor2() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(Object)");
  }

  @Test
  public void testConstructor3() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(String)");
  }

  @Test
  public void testConstructor4() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(int)");
  }

  @Test
  public void testConstructor5() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(Class)");
  }

  @Test
  public void testConstructor6() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(org.ppwcode.util.reflect_I.StubClass)");
  }

  @Test
  public void testConstructor7() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(int, boolean, Object, String)");
  }

  @Test
  public void testConstructor8() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(int,boolean,    Object, " +
        "     String)");
  }

  @Test
  public void testConstructor9() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(Object, Object, float)");
  }

  @Test
  public void testConstructor10() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(java.io.Serializable, java.io.Serializable, float)");
  }

  @Test
  public void testConstructor11() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(java.util.Date)");
  }

  //MUDO deal with [] array types
//  @Test
//  public void testConstructor12() throws CannotParseSignatureException {
//    testConstructor(StubClass.class, "StubClass(Object[])");
//  }

  @Test(expected = AssertionError.class)
  public void testConstructor13() throws CannotParseSignatureException {
    testConstructor(StubClass.class, "StubClass(org.ppwcode.util.reflect.StubClass, org.ppwcode.util.reflect.StubClass)");
  }

  public <_T_> void testConstructor(Class<_T_> type, String signature) throws CannotParseSignatureException {
    Constructor<_T_> result = constructor(type, signature);
    assertNotNull(result);
    assertEquals(type, result.getDeclaringClass());
    MethodSignature msig = new MethodSignature(signature);
    assertArrayEquals(msig.getParameterTypes(), result.getParameterTypes());
  }

  @Test
  public void demoArrayClass() throws ClassNotFoundException {
    System.out.println(Object[].class);
    System.out.println(Object[][].class);
    System.out.println(int[][].class);
    Class<?> result = Class.forName("java.lang.Object");
    assertEquals(Object.class, result);
    Method[] ms = StubClass.class.getDeclaredMethods();
    for (Method method : ms) {
      System.out.print(method.getName() + ": ");
      for (Class<?> c : method.getParameterTypes()) {
        System.out.print(c + ", ");
      }
      System.out.println();
    }
    result = Class.forName("[Ljava.lang.Object;"); // java.lang.Object[]
    assertEquals(Object[].class, result);
    result = Class.forName("[[Ljava.lang.Object;"); // java.lang.Object[][]
    assertEquals(Object[][].class, result);
    result = Class.forName("[[I"); // int[][]
    assertEquals(int[][].class, result);
  }

}

