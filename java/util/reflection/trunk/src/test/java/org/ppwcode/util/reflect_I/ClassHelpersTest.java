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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.util.reflect_I.ClassHelpers.PRIMITIVE_TYPES;
import static org.ppwcode.util.reflect_I.ClassHelpers.PRIMITIVE_TYPES_MAP;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;
import org.ppwcode.util.reflect_I.StubClass.StubClassA;
import org.ppwcode.util.reflect_I.StubClass.StubClassB;
import org.ppwcode.util.reflect_I.StubClass.StubClassInnerA;
import org.ppwcode.util.reflect_I.StubClass.StubClassInnerB;


public class ClassHelpersTest {

// following in comments: demo methods (which are not really tests)

//  @Test
//  public void demoPrimitiveTypeName() {
//    System.out.println("Boolean.TYPE.getSimpleName(): " + Boolean.TYPE.getSimpleName());
//  }

//  @Test
//  public void demoClassDetails() {
//    // primitive types
//    for (Class<?> pt : ClassHelpers.PRIMITIVE_TYPES) {
//      demoClassDetails(pt);
//    }
//    // classes
//    demoClassDetails(Object.class);
//    demoClassDetails(StubClass.class);
//    demoClassDetails(StubClass.StubClassA.class);
//    demoClassDetails(StubClass.StubClassB.class);
//    demoClassDetails(StubClass.StubClassInnerA.class);
//    demoClassDetails(StubClass.StubClassInnerB.class);
//    // arrays
//    Object array;
//    // primitive types
//    demoClassDetails(boolean[].class);
//    demoClassDetails(byte[].class);
//    demoClassDetails(char[].class);
//    demoClassDetails(short[].class);
//    demoClassDetails(int[].class);
//    demoClassDetails(long[].class);
//    demoClassDetails(float[].class);
//    demoClassDetails(double[].class);
//    // classes
//    demoClassDetails(Object[].class);
//    demoClassDetails(StubClass[].class);
//    demoClassDetails(StubClass.StubClassA[].class);
//    demoClassDetails(StubClass.StubClassB[].class);
//    demoClassDetails(StubClass.StubClassInnerA[].class);
//    demoClassDetails(StubClass.StubClassInnerB[].class);
//    demoClassDetails(Object[][].class);
//    demoClassDetails(Object[][][].class);
//    demoClassDetails(Object[].class);
//  }
//
//  public void demoClassDetails(Class<?> clazz) {
//    System.out.println("is primitive? : " + clazz.isPrimitive());
//    System.out.println("is member class? : " + clazz.isMemberClass());
//    System.out.println("is local class? : " + clazz.isLocalClass());
//    System.out.println("is anonymous class? : " + clazz.isAnonymousClass());
//    System.out.println();
//
//    System.out.println("package : " + clazz.getPackage());
//    System.out.println();
//
//    System.out.println("name : " + clazz.getName());
//    System.out.println("canonical name: " + clazz.getCanonicalName());
//    System.out.println("simple name :" + clazz.getSimpleName());
//    System.out.println("toString :" + clazz.toString());
//    System.out.println();
//
//    System.out.println("is array? : " + clazz.isArray());
//    System.out.println("component type: " + clazz.getComponentType());
//    System.out.println();
//
//    System.out.println();
//  }

//  @Test
//  public void demoArrayClass() throws ClassNotFoundException {
//    System.out.println(Object[].class);
//    System.out.println(Object[][].class);
//    System.out.println(int[][].class);
//    Class<?> result = Class.forName("java.lang.Object");
//    assertEquals(Object.class, result);
//    Method[] ms = StubClass.class.getDeclaredMethods();
//    for (Method method : ms) {
//      System.out.print(method.getName() + ": ");
//      for (Class<?> c : method.getParameterTypes()) {
//        System.out.print(c + ", ");
//      }
//      System.out.println();
//    }
//    result = Class.forName("[Ljava.lang.Object;"); // java.lang.Object[]
//    assertEquals(Object[].class, result);
//    result = Class.forName("[[Ljava.lang.Object;"); // java.lang.Object[][]
//    assertEquals(Object[][].class, result);
//    result = Class.forName("[[I"); // int[][]
//    assertEquals(int[][].class, result);
//  }

  @Test
  public void testPRIMITIVE_TYPES() {
    Class<?>[] expected = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE,
                           Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
    assertEquals(new HashSet<Class<?>>(Arrays.asList(expected)), PRIMITIVE_TYPES);
  }

  @Test
  public void testPRIMITIVE_TYPES_MAP() {
    for (Class<?> pt : PRIMITIVE_TYPES) {
      PRIMITIVE_TYPES_MAP.get(pt.getSimpleName()).equals(pt);
    }
    assertEquals(PRIMITIVE_TYPES, new HashSet<Class<?>>(PRIMITIVE_TYPES_MAP.values()));
  }

  // loadForName

  @Test
  public void testLoadForName1() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("boolean");
    assertEquals(Boolean.TYPE, result);
  }

  @Test
  public void testLoadForName2() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("byte");
    assertEquals(Byte.TYPE, result);
  }

  @Test
  public void testLoadForName3() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("char");
    assertEquals(Character.TYPE, result);
  }

  @Test
  public void testLoadForName4() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("short");
    assertEquals(Short.TYPE, result);
  }

  @Test
  public void testLoadForName5() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("int");
    assertEquals(Integer.TYPE, result);
  }

  @Test
  public void testLoadForName6() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("long");
    assertEquals(Long.TYPE, result);
  }

  @Test
  public void testLoadForName7() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("float");
    assertEquals(Float.TYPE, result);
  }

  @Test
  public void testLoadForName8() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("double");
    assertEquals(Double.TYPE, result);
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName9() throws _CannotGetClassException {
    ClassHelpers.loadForName("hjgks");
  }

  @Test
  public void testLoadForName10() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("java.lang.String");
    assertEquals(String.class, result);
  }

  @Test
  public void testLoadForName11() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("String");
    assertEquals(String.class, result);
  }

  @Test
  public void testLoadForName12() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("org.ppwcode.util.reflect_I.ConstantHelpers");
    assertEquals(ConstantHelpers.class, result);
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName13() throws _CannotGetClassException {
    ClassHelpers.loadForName("org.ppwcode.util.reflect_I.Constants ");
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName14() throws _CannotGetClassException {
    ClassHelpers.loadForName("org.ppwcode.util.reflect_I. Constants");
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName15() throws _CannotGetClassException {
    ClassHelpers.loadForName("org.ppwcode.util.reflect_I.Deflection");
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName16() throws _CannotGetClassException {
    ClassHelpers.loadForName("org.ppwcode.util.reflect_I.String");
  }

  @Test
  public void testLoadForName17() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("org.ppwcode.util.reflect_I.ConstantHelpersTest");
    assertEquals(ConstantHelpersTest.class, result);
  }

  @Test
  public void testLoadForName18() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("org.ppwcode.util.reflect_I.StubClass.StubClassA");
    assertEquals(StubClassA.class, result);
  }

  @Test
  public void testLoadForName19() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("org.ppwcode.util.reflect_I.StubClass.StubClassB");
    assertEquals(StubClassB.class, result);
  }

  @Test
  public void testLoadForName20() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("org.ppwcode.util.reflect_I.StubClass.StubClassInnerA");
    assertEquals(StubClassInnerA.class, result);
  }

  @Test
  public void testLoadForName21() throws _CannotGetClassException {
    Class<?> result = ClassHelpers.loadForName("org.ppwcode.util.reflect_I.StubClass.StubClassInnerA.StubClassInnerAInner");
    assertEquals(StubClassInnerA.StubClassInnerAInner.class, result);
  }



//  // prefixedDqcn
//
//  // MUDO test
//  @Test
//  public void testPrefixedFqcn() {
//    fail("Not yet implemented");
//  }
//
//
//
//  // instantiatePrefixed
//
//  // MUDO test
//  @Test
//  public void testInstantiatePrefixed() {
//    fail("Not yet implemented");
//  }



  // isInnerClass(Class)

  @Test
  public void testIsInnerClass1() {
    assertFalse(ClassHelpers.isInnerType(ConstantHelpers.class));
  }

  @Test
  public void testIsInnerClass2() {
    assertFalse(ClassHelpers.isInnerType(ConstantHelpers.class));
  }

  @Test
  public void testIsInnerClass3() {
    assertFalse(ClassHelpers.isInnerType(StubClassA.class));
  }

  @Test
  public void testIsInnerClass4() {
    assertFalse(ClassHelpers.isInnerType(StubClassB.class));
  }

  @Test
  public void testIsInnerClass5() {
    assertTrue(ClassHelpers.isInnerType(StubClassInnerA.class));
  }

  @Test
  public void testIsInnerClass6() {
    assertTrue(ClassHelpers.isInnerType(StubClassInnerB.class));
  }

  @Test
  public void testIsInnerClass7() {
    class StubClassLocalA {
      // NOP
    }
    assertTrue(ClassHelpers.isInnerType(StubClassLocalA.class));
  }

  @Test
  public void testIsInnerClass8() {
    class StubClassLocalA {
      // NOP
    }
    class StubClassLocalB extends StubClassLocalA {
      // NOP
    }
    assertTrue(ClassHelpers.isInnerType(StubClassLocalB.class));
  }

  @Test
  public void testIsInnerClass9() {
    // anonymous class
    assertTrue(ClassHelpers.isInnerType((new StubClassA("", 0, 0) { /* NOP */ }).getClass()));
  }



  // isTopLevelClass(Class)

  @Test
  public void testIsTopLevelClass1() {
    assertTrue(ClassHelpers.isTopLevelType(ConstantHelpersTest.class));
  }

  @Test
  public void testIsTopLevelClass2() {
    assertTrue(ClassHelpers.isTopLevelType(ConstantHelpers.class));
  }

  @Test
  public void testIsTopLevelClass3() {
    assertFalse(ClassHelpers.isTopLevelType(StubClassA.class));
  }

  @Test
  public void testIsTopLevelClass4() {
    assertFalse(ClassHelpers.isTopLevelType(StubClassB.class));
  }

  @Test
  public void testIsTopLevelClass5() {
    assertFalse(ClassHelpers.isTopLevelType(StubClassInnerA.class));
  }

  @Test
  public void testIsTopLevelClass6() {
    assertFalse(ClassHelpers.isTopLevelType(StubClassInnerB.class));
  }

  @Test
  public void testIsTopLevelClass7() {
    class StubClassLocalA {
      // NOP
    }
    assertFalse(ClassHelpers.isTopLevelType(StubClassLocalA.class));
  }

  @Test
  public void testIsTopLevelClass8() {
    class StubClassLocalA {
      // NOP
    }
    class StubClassLocalB extends StubClassLocalA {
      // NOP
    }
    assertFalse(ClassHelpers.isTopLevelType(StubClassLocalB.class));
  }

  @Test
  public void testIsTopLevelClass9() {
    // anonymous class
    assertFalse(ClassHelpers.isTopLevelType((new StubClassA("", 0, 0) { /* NOP */ }).getClass()));
  }



  // arrayClassForName()

  @Test
  public void testArrayClassForName() {
    Class<ConstantHelpers[]> result = ClassHelpers.arrayClassForName(ConstantHelpers.class);
    assertEquals(ConstantHelpers[].class, result);
  }

}

