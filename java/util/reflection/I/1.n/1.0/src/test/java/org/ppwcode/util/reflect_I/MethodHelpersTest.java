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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.util.reflect_I.MethodHelpers.constructor;
import static org.ppwcode.util.reflect_I.MethodHelpers.hasMethod;
import static org.ppwcode.util.reflect_I.MethodHelpers.hasPublicMethod;
import static org.ppwcode.util.reflect_I.MethodHelpers.method;
import static org.ppwcode.util.reflect_I.MethodHelpers.methodHelper;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.util.Date;

import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.AbstractSubStubClass;
import org.ppwcode.util.reflect_I.teststubs.AbstractSubSubStubClass;
import org.ppwcode.util.reflect_I.teststubs.AlternateStubClass;
import org.ppwcode.util.reflect_I.teststubs.StubClass;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceAlpha;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceBeta;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceDelta;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceGamma;
import org.ppwcode.util.reflect_I.teststubs.SuperStubClass;
import org.ppwcode.util.reflect_I.teststubs.SuperSuperStubClass;
import org.ppwcode.util.reflect_I.teststubs.SuperSuperStubInterfaceA;


public class MethodHelpersTest {

  @Test
  public void testIsPublic1() {
    Method method = method(StubClass.class, "stubMethod()");
    boolean result = MethodHelpers.isPublic(method);
    assertTrue(result);
  }

  @Test
  public void testIsPublic2() {
    Method method = method(StubClass.class, "stubMethod(long)");
    boolean result = MethodHelpers.isPublic(method);
    assertFalse(result);
  }

  @Test
  public void testIsPublic3() {
    Method method = method(StubClass.class, "stubMethod(boolean)");
    boolean result = MethodHelpers.isPublic(method);
    assertFalse(result);
  }

  @Test
  public void testIsPublic4() {
    Method method = method(StubClass.class, "stubMethod(byte)");
    boolean result = MethodHelpers.isPublic(method);
    assertFalse(result);
  }

  @Test
  public void testIsProtected1() {
    Method method = method(StubClass.class, "stubMethod()");
    boolean result = MethodHelpers.isProtected(method);
    assertFalse(result);
  }

  @Test
  public void testIsProtected2() {
    Method method = method(StubClass.class, "stubMethod(long)");
    boolean result = MethodHelpers.isProtected(method);
    assertFalse(result);
  }

  @Test
  public void testIsProtected3() {
    Method method = method(StubClass.class, "stubMethod(boolean)");
    boolean result = MethodHelpers.isProtected(method);
    assertTrue(result);
  }

  @Test
  public void testIsProtected4() {
    Method method = method(StubClass.class, "stubMethod(byte)");
    boolean result = MethodHelpers.isProtected(method);
    assertFalse(result);
  }

  @Test
  public void testIsPrivate1() {
    Method method = method(StubClass.class, "stubMethod()");
    boolean result = MethodHelpers.isPrivate(method);
    assertFalse(result);
  }

  @Test
  public void testIsPrivate2() {
    Method method = method(StubClass.class, "stubMethod(long)");
    boolean result = MethodHelpers.isPrivate(method);
    assertFalse(result);
  }

  @Test
  public void testIsPrivate3() {
    Method method = method(StubClass.class, "stubMethod(boolean)");
    boolean result = MethodHelpers.isPrivate(method);
    assertFalse(result);
  }

  @Test
  public void testIsPrivate4() {
    Method method = method(StubClass.class, "stubMethod(byte)");
    boolean result = MethodHelpers.isPrivate(method);
    assertTrue(result);
  }

  @Test
  public void testIsPackageAccessible1() {
    Method method = method(StubClass.class, "stubMethod()");
    boolean result = MethodHelpers.isPackageAccessible(method);
    assertFalse(result);
  }

  @Test
  public void testIsPackageAccessible2() {
    Method method = method(StubClass.class, "stubMethod(long)");
    boolean result = MethodHelpers.isPackageAccessible(method);
    assertTrue(result);
  }

  @Test
  public void testIsPackageAccessible3() {
    Method method = method(StubClass.class, "stubMethod(boolean)");
    boolean result = MethodHelpers.isPackageAccessible(method);
    assertFalse(result);
  }

  @Test
  public void testIsPackageAccessible4() {
    Method method = method(StubClass.class, "stubMethod(byte)");
    boolean result = MethodHelpers.isPackageAccessible(method);
    assertFalse(result);
  }

  @Test
  public void testIsStatic1() {
    Method method = method(StubClass.class, "stubMethod()");
    boolean result = MethodHelpers.isStatic(method);
    assertFalse(result);
  }

  @Test
  public void testIsStatic2() {
    Method method = method(StubClass.class, "stubMethod(long)");
    boolean result = MethodHelpers.isStatic(method);
    assertFalse(result);
  }

  @Test
  public void testIsStatic3() {
    Method method = method(StubClass.class, "stubMethod(boolean)");
    boolean result = MethodHelpers.isStatic(method);
    assertFalse(result);
  }

  @Test
  public void testIsStatic4() {
    Method method = method(StubClass.class, "stubMethod(byte)");
    boolean result = MethodHelpers.isStatic(method);
    assertFalse(result);
  }

  @Test
  public void testIsStatic5() {
    Method method = method(StubClass.class, "stubStaticMethod()");
    boolean result = MethodHelpers.isStatic(method);
    assertTrue(result);
  }

  @Test
  public void testIsStatic6() {
    Method method = method(StubClass.class, "stubStaticMethodWithReturn()");
    boolean result = MethodHelpers.isStatic(method);
    assertTrue(result);
  }

  @Test
  public void testIsStatic7() {
    Method method = method(StubClass.class, "stubStaticMethodWithException()");
    boolean result = MethodHelpers.isStatic(method);
    assertTrue(result);
  }

  @Test
  public void testIsStatic8() {
    Method method = method(StubClass.class, "stubStaticMethod(Object)");
    boolean result = MethodHelpers.isStatic(method);
    assertTrue(result);
  }

  @Test
  public void testMethodHelper1() throws NoSuchMethodException {
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
    testMethodHelper(StubClass.class, "stubMethod(long)");
    testMethodHelper(StubClass.class, "stubMethod(boolean)");
    testMethodHelper(StubClass.class, "stubMethod(byte)");
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
    testMethodHelper(StubClass.class, "stubStaticMethod(long)");
    testMethodHelper(StubClass.class, "stubStaticMethod(boolean)");
    testMethodHelper(StubClass.class, "stubStaticMethod(byte)");
 // MUDO deal with [] array types
//    testMethodHelper(StubClass.class, "stubStaticMethod(Object[])");
    // interface
    testMethodHelper(SuperSuperStubInterfaceA.class, "stubMethodC()");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper2() throws NoSuchMethodException {
    methodHelper(StubClass.class, "methodDoesntExist()");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper3() throws NoSuchMethodException {
    methodHelper(StubClass.class, "stubMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper4() throws NoSuchMethodException {
    methodHelper(StubClass.class, "stubStaticMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testMethodHelper5() throws NoSuchMethodException {
    methodHelper(StubClass.class, "StubClass(Object, Object, float)");
  }

  public void testMethodHelper(Class<?> type, String signature) throws NoSuchMethodException {
    Method result = methodHelper(type, signature);
    assertNotNull(result);
    assertEquals(type, result.getDeclaringClass());
    MethodSignature msig = new MethodSignature(signature);
    assertEquals(msig.getMethodName(), result.getName());
    assertArrayEquals(msig.getParameterTypes(), result.getParameterTypes());
  }

  @Test
  public void testInheritedMethodHelper1() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubClass.class, "stubMethod()");
    Method expected = StubClass.class.getDeclaredMethod("stubMethod");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper2() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubClass.class, "toString()");
    Method expected = Object.class.getDeclaredMethod("toString");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper3() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubClass.class, "stubMethodA()");
    Method expected = SuperStubClass.class.getDeclaredMethod("stubMethodA");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper4() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubClass.class, "stubMethodB()");
    Method expected = SuperStubClass.class.getDeclaredMethod("stubMethodB");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper5() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubClass.class, "stubMethodC()");
    Method expected = SuperSuperStubClass.class.getDeclaredMethod("stubMethodC");
    assertEquals(expected, result);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testInheritedMethodHelper6() throws SecurityException, NoSuchMethodException {
    MethodHelpers.inheritedMethodHelper(StubClass.class, "doesntExist()");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testInheritedMethodHelper7() throws SecurityException, NoSuchMethodException {
    MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "doesntExist()");
  }

  @Test(expected = NoSuchMethodException.class)
  public void testInheritedMethodHelper8() throws SecurityException, NoSuchMethodException {
    MethodHelpers.inheritedMethodHelper(StubInterfaceDelta.class, "doesntExist()");
  }

  @Test
  public void testInheritedMethodHelper9() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubInterfaceAlpha.class, "stubMethodAlpha()");
    Method expected = StubInterfaceAlpha.class.getDeclaredMethod("stubMethodAlpha");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper10() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubInterfaceBeta.class, "stubMethodC()");
    Method expected = SuperSuperStubInterfaceA.class.getDeclaredMethod("stubMethodC");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper11() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(StubInterfaceGamma.class, "stubMethodEpsilon()");
    Method expected = StubInterfaceGamma.class.getDeclaredMethod("stubMethodEpsilon");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper12() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "stubMethodAlpha()");
    Method expected = AbstractSubStubClass.class.getDeclaredMethod("stubMethodAlpha");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper13() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "stubMethodBeta()");
    Method expected = StubInterfaceBeta.class.getDeclaredMethod("stubMethodBeta");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper14() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "stubMethodGamma()");
    Method expected = StubInterfaceGamma.class.getDeclaredMethod("stubMethodGamma");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper15() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "stubMethodDelta()");
    Method expected = StubInterfaceDelta.class.getDeclaredMethod("stubMethodDelta");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper16() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "stubMethodEpsilon()");
    Method expected = StubInterfaceGamma.class.getDeclaredMethod("stubMethodEpsilon");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper17() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "stubMethodC()");
    Method expected = SuperSuperStubClass.class.getDeclaredMethod("stubMethodC");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper18() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "stubMethod()");
    Method expected = StubClass.class.getDeclaredMethod("stubMethod");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper19() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "privateStubMethodAleph()");
    Method expected = AbstractSubSubStubClass.class.getDeclaredMethod("privateStubMethodAleph");
    assertEquals(expected, result);
  }

  @Test
  public void testInheritedMethodHelper20a() throws SecurityException, NoSuchMethodException {
    Method result = MethodHelpers.inheritedMethodHelper(SuperStubClass.class, "privateStubMethodBet()");
    Method expected = SuperStubClass.class.getDeclaredMethod("privateStubMethodBet");
    assertEquals(expected, result);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testInheritedMethodHelper20() throws SecurityException, NoSuchMethodException {
    MethodHelpers.inheritedMethodHelper(AbstractSubSubStubClass.class, "privateStubMethodBet()");
  }

  @Test
  public void testMethodClassOfQString1() {
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
    testMethodClassOfQString(StubClass.class, "stubMethod(long)");
    testMethodClassOfQString(StubClass.class, "stubMethod(boolean)");
    testMethodClassOfQString(StubClass.class, "stubMethod(byte)");
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
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(long)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(boolean)");
    testMethodClassOfQString(StubClass.class, "stubStaticMethod(byte)");
 // MUDO deal with [] array types
//    testMethodClassOfQString(StubClass.class, "stubStaticMethod(Object[])");
    // inherited
    testMethodClassOfQString(StubClass.class, "toString()");
    testMethodClassOfQString(StubClass.class, "stubMethodA()");
    testMethodClassOfQString(StubClass.class, "stubMethodB()");
    testMethodClassOfQString(StubClass.class, "stubMethodC()");
    testMethodClassOfQString(StubInterfaceAlpha.class, "stubMethodAlpha()");
    testMethodClassOfQString(StubInterfaceBeta.class, "stubMethodC()");
    testMethodClassOfQString(StubInterfaceGamma.class, "stubMethodEpsilon()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodAlpha()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodBeta()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodGamma()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodDelta()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodEpsilon()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodC()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethod()");
    testMethodClassOfQString(AbstractSubSubStubClass.class, "privateStubMethodAleph()");
    testMethodClassOfQString(SuperStubClass.class, "privateStubMethodBet()");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString2() {
    method(StubClass.class, "methodDoesntExist()");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString3() {
    method(StubClass.class, "stubMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString4() {
    method(StubClass.class, "stubStaticMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString5() {
    method(StubClass.class, "StubClass(Object, Object, float)");
  }

  @Test(expected = AssertionError.class)
  public void testMethodClassOfQString6() {
    method(AbstractSubSubStubClass.class, "privateStubMethodBet()");
  }

  public void testMethodClassOfQString(Class<?> type, String signature) {
    Method result = method(type, signature);
    assertNotNull(result);
    assertTrue(result.getDeclaringClass().isAssignableFrom(type));
    MethodSignature msig = new MethodSignature(signature);
    assertEquals(msig.getMethodName(), result.getName());
    assertArrayEquals(msig.getParameterTypes(), result.getParameterTypes());
    assertTrue(result.getDeclaringClass() != type ? ! MethodHelpers.isPrivate(result) : true);
  }

  @Test
  public void testHasMethodClassOfQString() throws SecurityException {
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
    testHasMethodClassOfQString(StubClass.class, "stubMethod(long)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(boolean)");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(byte)");
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
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(long)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(boolean)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(byte)");
 // MUDO deal with [] array types
//    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(Object[])");
    // methods that don't exist
    testHasMethodClassOfQString(StubClass.class, "methodDoesntExist()");
    testHasMethodClassOfQString(StubClass.class, "stubMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
    testHasMethodClassOfQString(StubClass.class, "stubStaticMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
    testHasMethodClassOfQString(StubClass.class, "StubClass(Object, Object, float)");
    // inherited
    testHasMethodClassOfQString(StubClass.class, "toString()");
    testHasMethodClassOfQString(StubClass.class, "stubMethodA()");
    testHasMethodClassOfQString(StubClass.class, "stubMethodB()");
    testHasMethodClassOfQString(StubClass.class, "stubMethodC()");
    testHasMethodClassOfQString(StubInterfaceAlpha.class, "stubMethodAlpha()");
    testHasMethodClassOfQString(StubInterfaceBeta.class, "stubMethodC()");
    testHasMethodClassOfQString(StubInterfaceGamma.class, "stubMethodEpsilon()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodAlpha()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodBeta()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodGamma()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodDelta()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodEpsilon()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodC()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethod()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "privateStubMethodAleph()");
    testHasMethodClassOfQString(SuperStubClass.class, "privateStubMethodBet()");
    testHasMethodClassOfQString(AbstractSubSubStubClass.class, "privateStubMethodBet()");
  }

  public void testHasMethodClassOfQString(Class<?> type, String signature) {
    boolean result = hasMethod(type, signature);
    try {
      MethodHelpers.inheritedMethodHelper(type, signature);
      assertTrue(result);
    }
    catch (NoSuchMethodException exc) {
      assertFalse(result);
    }
  }

  @Test
  public void testHasPublicMethodClassOfQString() throws SecurityException {
    // dynamic method
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethodWithReturn()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethodWithException()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(Object)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(String)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(int)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(Class)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(int, boolean, Object, String)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(int,boolean,    Object,String)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(Object, float)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(java.io.Serializable, float)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(java.util.Date)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(long)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(boolean)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(byte)");
 // MUDO deal with [] array types
//    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(Object[])");
    // static methods
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethodWithReturn()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethodWithException()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(Object)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(String)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(int)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(Class)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(int, boolean, Object, String)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(int,boolean,    Object,String)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(Object, float)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(java.io.Serializable, float)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(java.util.Date)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(long)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(boolean)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(byte)");
 // MUDO deal with [] array types
//    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(Object[])");
    // methods that don't exist
    testHasPublicMethodClassOfQString(StubClass.class, "methodDoesntExist()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
    testHasPublicMethodClassOfQString(StubClass.class, "stubStaticMethod(org.ppwcode.util.reflect_I.teststubs.StubClass)");
    testHasPublicMethodClassOfQString(StubClass.class, "StubClass(Object, Object, float)");
    // inherited
    testHasPublicMethodClassOfQString(StubClass.class, "toString()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethodA()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethodB()");
    testHasPublicMethodClassOfQString(StubClass.class, "stubMethodC()");
    testHasPublicMethodClassOfQString(StubInterfaceAlpha.class, "stubMethodAlpha()");
    testHasPublicMethodClassOfQString(StubInterfaceBeta.class, "stubMethodC()");
    testHasPublicMethodClassOfQString(StubInterfaceGamma.class, "stubMethodEpsilon()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodAlpha()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodBeta()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodGamma()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodDelta()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodEpsilon()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethodC()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "stubMethod()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "privateStubMethodAleph()");
    testHasPublicMethodClassOfQString(SuperStubClass.class, "privateStubMethodBet()");
    testHasPublicMethodClassOfQString(AbstractSubSubStubClass.class, "privateStubMethodBet()");
  }

  public void testHasPublicMethodClassOfQString(Class<?> type, String signature) {
    boolean result = hasPublicMethod(type, signature);
    try {
      Method m = MethodHelpers.inheritedMethodHelper(type, signature);
      assertEquals(MethodHelpers.isPublic(m), result);
    }
    catch (NoSuchMethodException exc) {
      assertFalse(result);
    }
  }

  @Test
  public void testConstructor1() {
    testConstructor(StubClass.class, "StubClass()");
    testConstructor(StubClass.class, "StubClass(Object)");
    testConstructor(StubClass.class, "StubClass(String)");
    testConstructor(StubClass.class, "StubClass(int)");
    testConstructor(StubClass.class, "StubClass(Class)");
    testConstructor(StubClass.class, "StubClass(org.ppwcode.util.reflect_I.teststubs.StubClass)");
    testConstructor(StubClass.class, "StubClass(int, boolean, Object, String)");
    testConstructor(StubClass.class, "StubClass(int,boolean,    Object, " +
        "     String)");
    testConstructor(StubClass.class, "StubClass(Object, Object, float)");
    testConstructor(StubClass.class, "StubClass(java.io.Serializable, java.io.Serializable, float)");
    testConstructor(StubClass.class, "StubClass(java.util.Date)");
    testConstructor(StubClass.class, "StubClass(long)");
    testConstructor(StubClass.class, "StubClass(boolean)");
    testConstructor(StubClass.class, "StubClass(byte)");
    testConstructor(AlternateStubClass.class, "StubClass()");
  }

  //MUDO deal with [] array types
//  @Test
//  public void testConstructor12() throws _CannotParseSignatureException {
//    testConstructor(StubClass.class, "StubClass(Object[])");
//  }

  @Test(expected = AssertionError.class)
  public void testConstructor13() {
    testConstructor(StubClass.class, "StubClass(org.ppwcode.util.reflect.StubClass, org.ppwcode.util.reflect.StubClass)");
  }

  public <_T_> void testConstructor(Class<_T_> type, String signature) {
    Constructor<_T_> result = constructor(type, signature);
    assertNotNull(result);
    assertEquals(type, result.getDeclaringClass());
    MethodSignature msig = new MethodSignature(signature);
    assertArrayEquals(msig.getParameterTypes(), result.getParameterTypes());
  }

  @Test
  public void testConstructorParameterTypes1() {
    testConstructorParameterTypes(StubClass.class);
    testConstructorParameterTypes(StubClass.class, Object.class);
    testConstructorParameterTypes(StubClass.class, String.class);
    testConstructorParameterTypes(StubClass.class, Integer.TYPE);
    testConstructorParameterTypes(StubClass.class, Class.class);
    testConstructorParameterTypes(StubClass.class, org.ppwcode.util.reflect_I.teststubs.StubClass.class);
    testConstructorParameterTypes(StubClass.class, Integer.TYPE, Boolean.TYPE, Object.class, String.class);
    testConstructorParameterTypes(StubClass.class, Object.class, Object.class, Float.TYPE);
    testConstructorParameterTypes(StubClass.class, Serializable.class, Serializable.class, Float.TYPE);
    testConstructorParameterTypes(StubClass.class, Date.class);
    testConstructorParameterTypes(StubClass.class, Long.TYPE);
    testConstructorParameterTypes(StubClass.class, Boolean.TYPE);
    testConstructorParameterTypes(StubClass.class, Byte.TYPE);
    testConstructorParameterTypes(AlternateStubClass.class);
  }

  //MUDO deal with [] array types
//  @Test
//  public void testConstructor12() throws _CannotParseSignatureException {
//    testConstructor(StubClass.class, "StubClass(Object[])");
//  }

  @Test(expected = AssertionError.class)
  public void testConstructorParameterTypes13() {
    testConstructorParameterTypes(StubClass.class, StringBuffer.class, Date.class);
  }

  public <_T_> void testConstructorParameterTypes(Class<_T_> type, Class<?>... parameterTypes) {
    Constructor<_T_> result = constructor(type, parameterTypes);
    assertNotNull(result);
    assertEquals(type, result.getDeclaringClass());
    assertArrayEquals(parameterTypes, result.getParameterTypes());
  }

  static class X {
    // NOP
  }

  @Test
  public void testConstructorParameterTypesDynamic() throws SecurityException, NoSuchMethodException {
    testConstructorParameterTypesDynamic(StubClass.class.getDeclaredConstructor(Object.class),
                                         StubClass.class, X.class);
    testConstructorParameterTypesDynamic(StubClass.class.getDeclaredConstructor(Object.class),
                                         StubClass.class, Inet4Address.class);
    testConstructorParameterTypesDynamic(StubClass.class.getDeclaredConstructor(Integer.TYPE, Boolean.TYPE, Object.class, String.class),
                                         StubClass.class, Integer.TYPE, Boolean.TYPE, Inet4Address.class, String.class);
    testConstructorParameterTypesDynamic(StubClass.class.getDeclaredConstructor(Object.class, Object.class, Float.TYPE),
                                         StubClass.class, X.class, String.class, Float.TYPE);

    testConstructorParameterTypesDynamic(StubClass.class.getDeclaredConstructor(Object.class, Object.class, Float.TYPE),
                                         StubClass.class, Inet4Address.class, String.class, Float.TYPE);
    testConstructorParameterTypesDynamic(StubClass.class.getDeclaredConstructor(Object.class, Object.class, Float.TYPE),
                                         StubClass.class, String.class, Date.class, Float.TYPE);
    // dub?! Object or Serializable? Dependendent on random in hash set?


    testConstructorParameterTypesDynamic(StubClass.class.getDeclaredConstructor(Date.class),
                                         StubClass.class, java.sql.Date.class);
  }

  public <_T_> void testConstructorParameterTypesDynamic(Constructor<_T_> expected, Class<_T_> type, Class<?>... parameterTypes) {
    Constructor<_T_> result = constructor(type, parameterTypes);
    assertNotNull(result);
    assertEquals(type, result.getDeclaringClass());
//    assertArrayEquals(expected.getParameterTypes(), result.getParameterTypes());
    assertEquals(expected, result);
  }

  @Test
  public void testInvokeClassQStringObject_1() {
    Object result = MethodHelpers.invoke(StubClass.class, "stubStaticMethod()");
    assertNull(result);
  }

  @Test
  public void testInvokeClassQStringObject_2() {
    int result = MethodHelpers.<Integer>invoke(StubClass.class, "stubStaticMethodWithReturn()");
    assertEquals(0, result);
  }

  @Test
  public void testInvokeClassQStringObject_3() {
    int result = MethodHelpers.<Integer>invoke(StubClass.class, "stubStaticMethodWithException()");
    assertEquals(0, result);
  }

  @Test
  public void testInvokeClassQStringObject_4() {
    Object arg = new Object();
    Object result = MethodHelpers.invoke(StubClass.class, "stubStaticMethod(Object, float)", arg, new Float(3.14));
    assertEquals(arg, result);
  }

  @Test
  public void testInvokeClassQStringObject_5() {
    Date arg = new Date();
    Serializable result = MethodHelpers.invoke(StubClass.class, "stubStaticMethod(java.io.Serializable, float)", arg, new Float(3.14));
    assertEquals(arg, result);
  }

  @Test
  public void testInvokeClassQStringObject_6() {
    Date arg = new Date();
    Date result = MethodHelpers.invoke(StubClass.class, "stubStaticMethod(java.io.Serializable, float)", arg, new Float(3.14));
    assertEquals(arg, result);
  }

}

