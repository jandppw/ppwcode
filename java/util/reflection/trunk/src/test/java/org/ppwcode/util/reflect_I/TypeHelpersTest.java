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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.TypeHelpers.PRIMITIVE_TYPES;
import static org.ppwcode.util.reflect_I.TypeHelpers.PRIMITIVE_TYPES_BY_NAME;
import static org.ppwcode.util.reflect_I.TypeHelpers.PRIMITIVE_TYPE_BINARY_NAMES;
import static org.ppwcode.util.reflect_I.TypeHelpers.arrayType;
import static org.ppwcode.util.reflect_I.TypeHelpers.isInnerType;
import static org.ppwcode.util.reflect_I.TypeHelpers.isTopLevelType;
import static org.ppwcode.util.reflect_I.TypeHelpers.type;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;
import org.ppwcode.util.reflect_I.StubClass.StubClassA;
import org.ppwcode.util.reflect_I.StubClass.StubClassB;
import org.ppwcode.util.reflect_I.StubClass.StubClassInnerA;
import org.ppwcode.util.reflect_I.StubClass.StubClassInnerB;


public class TypeHelpersTest {

// following in comments: demo methods (which are not really tests)

//  @Test
//  public void demoPrimitiveTypeName() {
//    System.out.println("Boolean.TYPE.getSimpleName(): " + Boolean.TYPE.getSimpleName());
//  }

//  @Test
//  public void demoClassDetails() {
//    // primitive types
//    for (Class<?> pt : TypeHelpers.PRIMITIVE_TYPES) {
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
  public void testPRIMITIVE_TYPE_BINARY_NAME() {
    assertEquals(PRIMITIVE_TYPE_BINARY_NAMES.keySet(), PRIMITIVE_TYPES);
  }

  @Test
  public void testPRIMITIVE_TYPES_MAP() {
    for (Class<?> pt : PRIMITIVE_TYPES) {
      PRIMITIVE_TYPES_BY_NAME.get(pt.getSimpleName()).equals(pt);
    }
    assertEquals(PRIMITIVE_TYPES, new HashSet<Class<?>>(PRIMITIVE_TYPES_BY_NAME.values()));
  }


  // isPublic

  // MUDO test
  @Test
  public void testIsPublic() {
    fail("Not yet implemented");
  }



  // isProtected

  // MUDO test
  @Test
  public void testIsProtected() {
    fail("Not yet implemented");
  }



  // isPrivate

  // MUDO test
  @Test
  public void testIsPrivate() {
    fail("Not yet implemented");
  }



  // isPackageAccessible

  // MUDO test
  @Test
  public void testIsPackageAccessible() {
    fail("Not yet implemented");
  }



  // isStatic

  // MUDO test
  @Test
  public void testIsStatic() {
    fail("Not yet implemented");
  }



  // isAbstract

  // MUDO test
  @Test
  public void testIsAbstract() {
    fail("Not yet implemented");
  }



  // isFinal

  // MUDO test
  @Test
  public void testIsFinal() {
    fail("Not yet implemented");
  }



  // isInnerType(Class)

  @Test
  public void testIsInnerType() {
    assertFalse(isInnerType(ConstantHelpers.class));
  }

  @Test
  public void testIsInnerClass2() {
    assertFalse(isInnerType(ConstantHelpers.class));
  }

  @Test
  public void testIsInnerClass3() {
    assertFalse(isInnerType(StubClassA.class));
  }

  @Test
  public void testIsInnerClass4() {
    assertFalse(isInnerType(StubClassB.class));
  }

  @Test
  public void testIsInnerClass5() {
    assertTrue(isInnerType(StubClassInnerA.class));
  }

  @Test
  public void testIsInnerClass6() {
    assertTrue(isInnerType(StubClassInnerB.class));
  }

  @Test
  public void testIsInnerClass7() {
    class StubClassLocalA {
      // NOP
    }
    assertTrue(isInnerType(StubClassLocalA.class));
  }

  @Test
  public void testIsInnerClass8() {
    class StubClassLocalA {
      // NOP
    }
    class StubClassLocalB extends StubClassLocalA {
      // NOP
    }
    assertTrue(isInnerType(StubClassLocalB.class));
  }

  @Test
  public void testIsInnerClass9() {
    // anonymous class
    assertTrue(isInnerType((new StubClassA("", 0, 0) { /* NOP */ }).getClass()));
  }



  // isTopLevelClass(Class)

  @Test
  public void testIsTopLevelType() {
    assertTrue(isTopLevelType(ConstantHelpersTest.class));
  }

  @Test
  public void testIsTopLevelType2() {
    assertTrue(isTopLevelType(ConstantHelpers.class));
  }

  @Test
  public void testIsTopLevelType3() {
    assertFalse(isTopLevelType(StubClassA.class));
  }

  @Test
  public void testIsTopLevelType4() {
    assertFalse(isTopLevelType(StubClassB.class));
  }

  @Test
  public void testIsTopLevelType5() {
    assertFalse(isTopLevelType(StubClassInnerA.class));
  }

  @Test
  public void testIsTopLevelType6() {
    assertFalse(isTopLevelType(StubClassInnerB.class));
  }

  @Test
  public void testIsTopLevelType7() {
    class StubClassLocalA {
      // NOP
    }
    assertFalse(isTopLevelType(StubClassLocalA.class));
  }

  @Test
  public void testIsTopLevelType8() {
    class StubClassLocalA {
      // NOP
    }
    class StubClassLocalB extends StubClassLocalA {
      // NOP
    }
    assertFalse(isTopLevelType(StubClassLocalB.class));
  }

  @Test
  public void testIsTopLevelType9() {
    // anonymous class
    assertFalse(isTopLevelType((new StubClassA("", 0, 0) { /* NOP */ }).getClass()));
  }



  // type

  @Test
  public void testType1() {
    String fqn = "boolean";
    Class<?> result = type(fqn);
    assertEquals(Boolean.TYPE, result);
    testType(fqn, result);
  }

  @Test
  public void testType2() {
    String fqn = "byte";
    Class<?> result = type(fqn);
    assertEquals(Byte.TYPE, result);
    testType(fqn, result);
  }

  @Test
  public void testType3() {
    String fqn = "char";
    Class<?> result = type(fqn);
    assertEquals(Character.TYPE, result);
    testType(fqn, result);
  }

  @Test
  public void testType4() {
    String fqn = "short";
    Class<?> result = type(fqn);
    assertEquals(Short.TYPE, result);
    testType(fqn, result);
  }

  @Test
  public void testType5() {
    String fqn = "int";
    Class<?> result = type(fqn);
    assertEquals(Integer.TYPE, result);
    testType(fqn, result);
  }

  @Test
  public void testType6() {
    String fqn = "long";
    Class<?> result = type(fqn);
    assertEquals(Long.TYPE, result);
    testType(fqn, result);
  }

  @Test
  public void testType7() {
    String fqn = "float";
    Class<?> result = type(fqn);
    assertEquals(Float.TYPE, result);
    testType(fqn, result);
  }

  @Test
  public void testType8() {
    String fqn = "double";
    Class<?> result = type(fqn);
    assertEquals(Double.TYPE, result);
    testType(fqn, result);
  }

  @Test(expected = AssertionError.class)
  public void testType9() {
    type("hjgks");
  }

  @Test
  public void testType10() {
    String fqn = "java.lang.String";
    Class<?> result = type(fqn);
    assertEquals(String.class, result);
    testType(fqn, result);
  }

  @Test
  public void testType11() {
    String fqn = "String";
    Class<?> result = type(fqn);
    assertEquals(String.class, result);
    testType(fqn, result);
  }

  @Test
  public void testType12() {
    String fqn = "org.ppwcode.util.reflect_I.ConstantHelpers";
    Class<?> result = type(fqn);
    assertEquals(ConstantHelpers.class, result);
    testType(fqn, result);
  }

  /**
   * extra space
   */
  @Test(expected = AssertionError.class)
  public void testType13() {
    type("org.ppwcode.util.reflect_I.ConstantHelpers ");
  }

  /**
   * extra space
   */
  @Test(expected = AssertionError.class)
  public void testType14() {
    type("org.ppwcode.util.reflect_I. ConstantHelpers");
  }

  @Test(expected = AssertionError.class)
  public void testType15() {
    type("org.ppwcode.util.reflect_I.Deflection");
  }

  @Test(expected = AssertionError.class)
  public void testType16() {
    type("org.ppwcode.util.reflect_I.String");
  }

  @Test
  public void testType17() {
    String fqn = "org.ppwcode.util.reflect_I.ConstantHelpersTest";
    Class<?> result = type(fqn);
    assertEquals(ConstantHelpersTest.class, result);
    testType(fqn, result);
  }

  @Test
  public void testType18() {
    String fqn = "org.ppwcode.util.reflect_I.StubClass.StubClassA";
    Class<?> result = type(fqn);
    assertEquals(StubClassA.class, result);
    testType(fqn, result);
  }

  @Test
  public void testType19() {
    String fqn = "org.ppwcode.util.reflect_I.StubClass.StubClassB";
    Class<?> result = type(fqn);
    assertEquals(StubClassB.class, result);
    testType(fqn, result);
  }

  @Test
  public void testType20() {
    String fqn = "org.ppwcode.util.reflect_I.StubClass.StubClassInnerA";
    Class<?> result = type(fqn);
    assertEquals(StubClassInnerA.class, result);
    testType(fqn, result);
  }

  @Test
  public void testType21() {
    String fqn = "org.ppwcode.util.reflect_I.StubClass.StubClassInnerA.StubClassInnerAInner";
    Class<?> result = type(fqn);
    assertEquals(StubClassInnerA.StubClassInnerAInner.class, result);
    testType(fqn, result);
  }

  public void testType(String fqn, Class<?> result) {
    assertNotNull(result);
    String expected = fqn;
    Package p = result.getPackage();
    if (p != null) {
      if (p.getName().equals("java.lang") && (! expected.startsWith("java.lang."))) {
        expected = "java.lang." + expected;
      }
    }
    assertEquals(expected, result.getCanonicalName());
  }



  // prefixedFqcn

  // MUDO test
  @Test
  public void testPrefixedFqcn() {
    fail("Not yet implemented");
  }



  // instantiatePrefixedClass

  // MUDO test
  @Test
  public void testInstantiatePrefixedClass() {
    fail("Not yet implemented");
  }



  // instantiatePrefixed

  // MUDO test
  @Test
  public void testInstantiatePrefixed() {
    fail("Not yet implemented");
  }



  // arrayClassForName()

  @Test
  public void testArrayClassForName1() {
    Class<ConstantHelpers[]> result = arrayType(ConstantHelpers.class);
    assertEquals(ConstantHelpers[].class, result);
    testArrayClassForName1(ConstantHelpers.class, result);
  }

  @Test
  public void testArrayClassForName2() {
    Class<String[]> result = arrayType(String.class);
    assertEquals(String[].class, result);
    testArrayClassForName1(String.class, result);
  }

  @Test
  public void testArrayClassForName3() {
    Class<String[][]> result = arrayType(String[].class);
    assertEquals(String[][].class, result);
    testArrayClassForName1(String[].class, result);
  }

  @Test
  public void testArrayClassForName4() {
    for (Class<?> pt : PRIMITIVE_TYPES) {
      Class<?> result = arrayType(pt);
      testArrayClassForName1(pt, result);
    }
  }

  public void testArrayClassForName1(Class<?> componentType, Class<?> result) {
    assertNotNull(result);
    assertTrue(result.isArray());
    assertEquals(componentType, result.getComponentType());
  }

}

