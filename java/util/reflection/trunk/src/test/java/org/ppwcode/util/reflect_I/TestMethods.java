/*<license>
Copyright 2008 - $Date$ by Jan Dockx.

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


package org.toryt.util_I.reflect;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.toryt.util_I.reflect.StubClass.StubClassA;
import org.toryt.util_I.reflect.StubClass.StubClassB;


public class TestMethods {

  @SuppressWarnings("unused")
  private static void stubMethod() {
    // NOP
  }



  // methodKind(Method)

  @Test
  public void testMethodKind1() throws CannotGetMethodException {
    Method method = Methods.method(Methods.class, "methodKind(java.lang.reflect.Method)");
    MethodKind result = Methods.methodKind(method);
    assertEquals(MethodKind.CLASS_INSPECTOR, result);
  }

  @Test
  public void testMethodKind2() throws CannotGetMethodException {
    Method method = Methods.method(TestMethods.class, "stubMethod()");
    MethodKind result = Methods.methodKind(method);
    assertEquals(MethodKind.CLASS_MUTATOR, result);
  }

  @Test
  public void testMethodKind3() throws CannotGetMethodException {
    Method method = Methods.method(StubClassA.class, "stubMethod1(String,int,float)");
    MethodKind result = Methods.methodKind(method);
    assertEquals(MethodKind.INSTANCE_INSPECTOR, result);
  }

  @Test
  public void testMethodKind4() throws CannotGetMethodException {
    Method method = Methods.method(StubClassA.class, "stubMethod2()");
    MethodKind result = Methods.methodKind(method);
    assertEquals(MethodKind.INSTANCE_MUTATOR, result);
  }



  // findMethod

  @Test
  public void testFindMethod1() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = "stubMethod1(java.lang.String,int,float)";
    testFindMethod(clazz, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod2() throws CannotGetMethodException {
    Class<?> clazz = StubClassB.class;
    String signature = "stubMethod1(java.lang.String,int,float)";
    Methods.method(clazz, signature);
  }

  @Test
  public void testFindMethod3() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = "stubMethod1(String,int,float)";
    testFindMethod(clazz, signature);
  }

  @Test
  public void testFindMethod4() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = " stubMethod1 ( String, int, float ) ";
    testFindMethod(clazz, signature);
  }

  @Test
  public void testFindMethod5() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = " stubMethod1 ( java.lang.String, int, float ) ";
    testFindMethod(clazz, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod6() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = "doesnExist(java.lang.String,int,float) ";
    Methods.method(clazz, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod7() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    // missing closing bracket
    String signature = "stubMethod1(java.lang.String,int,float";
    Methods.method(clazz, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod8() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    // java.util.String is not an existing class; the signature is not valid
    String signature = "stubMethod1(java.util.String,int,float)";
    Methods.method(clazz, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod9() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    // nonExistentParameterType is not an existing class; the signature is not valid
    String signature = "stubMethod1(String,int,nonExistentParameterType)";
    Methods.method(clazz, signature);
  }

  @Test
  public void testFindMethod10() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = "stubMethod2()";
    testFindMethod(clazz, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod11() throws CannotGetMethodException {
    Class<?> clazz = StubClassB.class;
    String signature = "stubMethod2()";
    Methods.method(clazz, signature);
  }

  @Test
  public void testFindMethod12() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = " stubMethod2() ";
    testFindMethod(clazz, signature);
  }

  @Test
  public void testFindMethod13() throws CannotGetMethodException {
    Class<?> clazz = StubClassA.class;
    String signature = " stubMethod2(  )";
    testFindMethod(clazz, signature);
  }

  @Test
  public void testFindMethod14() throws CannotGetMethodException {
    Class<?> clazz = TestMethods.class;
    String signature = "stubMethod()";
    testFindMethod(clazz, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod15() throws CannotGetMethodException {
    String signature = "stubMethod1(java.lang.String,int,float)";
    Methods.method(null, signature);
  }

  @Test(expected = CannotGetMethodException.class)
  public void testFindMethod16() throws CannotGetMethodException {
    Methods.method(StubClassA.class, null);
  }

  private void testFindMethod(Class<?> clazz, String signature) throws CannotGetMethodException {
    Method result = Methods.method(clazz, signature);
    assertTrue(result.getDeclaringClass() == clazz);
    try {
      MethodSignature sig = new MethodSignature(signature);
      assertEquals(sig.getMethodName(), result.getName());
      assertTrue(Arrays.deepEquals(result.getParameterTypes(), sig.getParameterTypes()));
    }
    catch (CannotParseSignatureException exc) {
      assert false;
    }
  }



  // findConstructor

  @Test
  public void testFindConstructor() {
    fail("Not yet implemented");
  }

}

