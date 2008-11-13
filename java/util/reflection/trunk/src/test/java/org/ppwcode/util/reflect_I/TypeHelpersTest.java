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
import static org.ppwcode.util.reflect_I.TypeHelpers.PRIMITIVE_TYPES;
import static org.ppwcode.util.reflect_I.TypeHelpers.PRIMITIVE_TYPES_BY_NAME;
import static org.ppwcode.util.reflect_I.TypeHelpers.PRIMITIVE_TYPE_BINARY_NAMES;
import static org.ppwcode.util.reflect_I.TypeHelpers.arrayType;
import static org.ppwcode.util.reflect_I.TypeHelpers.directSuperTypes;
import static org.ppwcode.util.reflect_I.TypeHelpers.distance;
import static org.ppwcode.util.reflect_I.TypeHelpers.isAbstract;
import static org.ppwcode.util.reflect_I.TypeHelpers.isFinal;
import static org.ppwcode.util.reflect_I.TypeHelpers.isInnerType;
import static org.ppwcode.util.reflect_I.TypeHelpers.isPackageAccessible;
import static org.ppwcode.util.reflect_I.TypeHelpers.isPrivate;
import static org.ppwcode.util.reflect_I.TypeHelpers.isProtected;
import static org.ppwcode.util.reflect_I.TypeHelpers.isPublic;
import static org.ppwcode.util.reflect_I.TypeHelpers.isStatic;
import static org.ppwcode.util.reflect_I.TypeHelpers.isTopLevelType;
import static org.ppwcode.util.reflect_I.TypeHelpers.superTypes;
import static org.ppwcode.util.reflect_I.TypeHelpers.type;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.AbstractSubStubClass;
import org.ppwcode.util.reflect_I.teststubs.AbstractSubSubStubClass;
import org.ppwcode.util.reflect_I.teststubs.AlternateStubClass;
import org.ppwcode.util.reflect_I.teststubs.CloneableStubClassA;
import org.ppwcode.util.reflect_I.teststubs.StubClass;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceAlpha;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceBeta;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceDelta;
import org.ppwcode.util.reflect_I.teststubs.StubInterfaceGamma;
import org.ppwcode.util.reflect_I.teststubs.SuperStubClass;
import org.ppwcode.util.reflect_I.teststubs.SuperSuperStubClass;
import org.ppwcode.util.reflect_I.teststubs.SuperSuperStubInterfaceA;
import org.ppwcode.util.reflect_I.teststubs.SuperSuperStubInterfaceB;
import org.ppwcode.util.reflect_I.teststubs.SuperSuperSuperStubInterface;
import org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassA;
import org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassB;
import org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassInnerA;
import org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassInnerB;


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

  public final static LinkedList<Class<?>> NON_LOCAL_TYPE_SUBJECTS = new LinkedList<Class<?>>();
  static {
    NON_LOCAL_TYPE_SUBJECTS.addAll(PRIMITIVE_TYPES);
    NON_LOCAL_TYPE_SUBJECTS.add(String.class);
    NON_LOCAL_TYPE_SUBJECTS.add(ConstantHelpers.class);
    NON_LOCAL_TYPE_SUBJECTS.add(ConstantHelpersTest.class);
    NON_LOCAL_TYPE_SUBJECTS.add(StubClass.class);
    NON_LOCAL_TYPE_SUBJECTS.add(AlternateStubClass.class);
    NON_LOCAL_TYPE_SUBJECTS.add(StubClassA.class);
    NON_LOCAL_TYPE_SUBJECTS.add(StubClassB.class);
    NON_LOCAL_TYPE_SUBJECTS.add(StubClassInnerA.class);
    NON_LOCAL_TYPE_SUBJECTS.add(StubClassInnerA.StubClassInnerAInner.class);
    NON_LOCAL_TYPE_SUBJECTS.add(AbstractSubStubClass.class);
    NON_LOCAL_TYPE_SUBJECTS.add(StubInterfaceAlpha.class);
    NON_LOCAL_TYPE_SUBJECTS.add(CloneableStubClassA.class);
  }


  public final static LinkedList<Class<?>> TYPE_SUBJECTS = new LinkedList<Class<?>>();
  static {
    TYPE_SUBJECTS.addAll(PRIMITIVE_TYPES);
    TYPE_SUBJECTS.addAll(NON_LOCAL_TYPE_SUBJECTS);
    class LocalClass {
      // NOP
    }
    TYPE_SUBJECTS.add(LocalClass.class);
    TYPE_SUBJECTS.add((new StubClassA("", 0, 0) { /* NOP */ }).getClass());
  }



  // is...

  @Test
  public void testIsPublic() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isPublic(type);
      boolean expected = Modifier.isPublic(type.getModifiers());
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsProtected() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isProtected(type);
      boolean expected = Modifier.isProtected(type.getModifiers());
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsPrivate() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isPrivate(type);
      boolean expected = Modifier.isPrivate(type.getModifiers());
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsPackageAccessible() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isPackageAccessible(type);
      int tMods = type.getModifiers();
      boolean expected = (! Modifier.isPublic(tMods)) && (! Modifier.isProtected(tMods)) && (! Modifier.isPrivate(tMods));
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsStatic() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isStatic(type);
      boolean expected = isTopLevelType(type) || Modifier.isStatic(type.getModifiers());
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsAbstract() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isAbstract(type);
      boolean expected = Modifier.isInterface(type.getModifiers()) || Modifier.isAbstract(type.getModifiers());
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsFinal() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isFinal(type);
      boolean expected = Modifier.isFinal(type.getModifiers());
      assertEquals(expected, result);
    }
  }



  // isInnerType(Class)

  @Test
  public void testIsInnerType() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = TypeHelpers.isInnerType(type);
      boolean expected = type.isLocalClass() || type.isAnonymousClass() || (type.isMemberClass() && (! isStatic(type)));
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsInnerType1() {
    assertFalse(isInnerType(ConstantHelpers.class));
  }

  @Test
  public void testIsInnerType2() {
    assertFalse(isInnerType(ConstantHelpers.class));
  }

  @Test
  public void testIsInnerType3() {
    assertFalse(isInnerType(StubClassA.class));
  }

  @Test
  public void testIsInnerType4() {
    assertFalse(isInnerType(StubClassB.class));
  }

  @Test
  public void testIsInnerType5() {
    assertTrue(isInnerType(StubClassInnerA.class));
  }

  @Test
  public void testIsInnerType6() {
    assertTrue(isInnerType(StubClassInnerB.class));
  }

  @Test
  public void testIsInnerType7() {
    class StubClassLocalA {
      // NOP
    }
    assertTrue(isInnerType(StubClassLocalA.class));
  }

  @Test
  public void testIsInnerType8() {
    class StubClassLocalA {
      // NOP
    }
    class StubClassLocalB extends StubClassLocalA {
      // NOP
    }
    assertTrue(isInnerType(StubClassLocalB.class));
  }

  @Test
  public void testIsInnerType9() {
    // anonymous class
    assertTrue(isInnerType((new StubClassA("", 0, 0) { /* NOP */ }).getClass()));
  }



  // isTopLevelClass(Class)

  @Test
  public void testIsTopLevelType() {
    for (Class<?> type : TYPE_SUBJECTS) {
      boolean result = isTopLevelType(type);
      boolean expected = (type.getEnclosingClass() == null);
      assertEquals(expected, result);
    }
  }

  @Test
  public void testIsTopLevelType1() {
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
    String fqtn = "boolean";
    Class<?> result = type(fqtn);
    assertEquals(Boolean.TYPE, result);
    testType(fqtn, result);
  }

  @Test
  public void testType2() {
    String fqtn = "byte";
    Class<?> result = type(fqtn);
    assertEquals(Byte.TYPE, result);
    testType(fqtn, result);
  }

  @Test
  public void testType3() {
    String fqtn = "char";
    Class<?> result = type(fqtn);
    assertEquals(Character.TYPE, result);
    testType(fqtn, result);
  }

  @Test
  public void testType4() {
    String fqtn = "short";
    Class<?> result = type(fqtn);
    assertEquals(Short.TYPE, result);
    testType(fqtn, result);
  }

  @Test
  public void testType5() {
    String fqtn = "int";
    Class<?> result = type(fqtn);
    assertEquals(Integer.TYPE, result);
    testType(fqtn, result);
  }

  @Test
  public void testType6() {
    String fqtn = "long";
    Class<?> result = type(fqtn);
    assertEquals(Long.TYPE, result);
    testType(fqtn, result);
  }

  @Test
  public void testType7() {
    String fqtn = "float";
    Class<?> result = type(fqtn);
    assertEquals(Float.TYPE, result);
    testType(fqtn, result);
  }

  @Test
  public void testType8() {
    String fqtn = "double";
    Class<?> result = type(fqtn);
    assertEquals(Double.TYPE, result);
    testType(fqtn, result);
  }

  @Test(expected = AssertionError.class)
  public void testType9() {
    type("hjgks");
  }

  @Test
  public void testType10() {
    String fqtn = "java.lang.String";
    Class<?> result = type(fqtn);
    assertEquals(String.class, result);
    testType(fqtn, result);
  }

  @Test
  public void testType11() {
    String fqtn = "String";
    Class<?> result = type(fqtn);
    assertEquals(String.class, result);
    testType(fqtn, result);
  }

  @Test
  public void testType12() {
    String fqtn = "org.ppwcode.util.reflect_I.ConstantHelpers";
    Class<?> result = type(fqtn);
    assertEquals(ConstantHelpers.class, result);
    testType(fqtn, result);
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
    String fqtn = "org.ppwcode.util.reflect_I.ConstantHelpersTest";
    Class<?> result = type(fqtn);
    assertEquals(ConstantHelpersTest.class, result);
    testType(fqtn, result);
  }

  @Test
  public void testType18() {
    String fqtn = "org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassA";
    Class<?> result = type(fqtn);
    assertEquals(StubClassA.class, result);
    testType(fqtn, result);
  }

  @Test
  public void testType19() {
    String fqtn = "org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassB";
    Class<?> result = type(fqtn);
    assertEquals(StubClassB.class, result);
    testType(fqtn, result);
  }

  @Test
  public void testType20() {
    String fqtn = "org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassInnerA";
    Class<?> result = type(fqtn);
    assertEquals(StubClassInnerA.class, result);
    testType(fqtn, result);
  }

  @Test
  public void testType21() {
    String fqtn = "org.ppwcode.util.reflect_I.teststubs.StubClass.StubClassInnerA.StubClassInnerAInner";
    Class<?> result = type(fqtn);
    assertEquals(StubClassInnerA.StubClassInnerAInner.class, result);
    testType(fqtn, result);
  }

  public void testType(String fqtn, Class<?> result) {
    assertNotNull(result);
    String expected = fqtn;
    Package p = result.getPackage();
    if (p != null) {
      if (p.getName().equals("java.lang") && (! expected.startsWith("java.lang."))) {
        expected = "java.lang." + expected;
      }
    }
    assertEquals(expected, result.getCanonicalName());
  }



  // arrayClassForName()

  @Test
  public void testArrayType1() {
    Class<ConstantHelpers[]> result = arrayType(ConstantHelpers.class);
    assertEquals(ConstantHelpers[].class, result);
    testArrayType1(ConstantHelpers.class, result);
  }

  @Test
  public void testArrayType2() {
    Class<String[]> result = arrayType(String.class);
    assertEquals(String[].class, result);
    testArrayType1(String.class, result);
  }

  @Test
  public void testArrayType3() {
    Class<String[][]> result = arrayType(String[].class);
    assertEquals(String[][].class, result);
    testArrayType1(String[].class, result);
  }

  @Test
  public void testArrayType4() {
    for (Class<?> pt : PRIMITIVE_TYPES) {
      Class<?> result = arrayType(pt);
      testArrayType1(pt, result);
    }
  }

  public void testArrayType1(Class<?> componentType, Class<?> result) {
    assertNotNull(result);
    assertTrue(result.isArray());
    assertEquals(componentType, result.getComponentType());
  }

  @Test
  public void testDirectSuperTypes() {
    Set<Class<? super AbstractSubSubStubClass>> result = directSuperTypes(AbstractSubSubStubClass.class);
    assertNotNull(result);
    assertEquals(3, result.size());
    assertTrue(result.contains(AbstractSubStubClass.class));
    assertTrue(result.contains(StubInterfaceDelta.class));
    assertTrue(result.contains(StubInterfaceGamma.class));
  }

  @Test
  public void testSuperTypes() {
    Class<AbstractSubSubStubClass> subject = AbstractSubSubStubClass.class;
    Set<Class<? super AbstractSubSubStubClass>> result = superTypes(subject);

    Set<Class<? super AbstractSubSubStubClass>> expected = new HashSet<Class<? super AbstractSubSubStubClass>>();
    Set<Class<? super AbstractSubSubStubClass>> directs = directSuperTypes(subject);
    expected.addAll(directs);
    for (Class<? super AbstractSubSubStubClass> direct : directs) {
      expected.addAll(superTypes(direct));
    }
    assertNotNull(result);
    assertEquals(expected, result);

    assertEquals(12, result.size());
    assertTrue(result.contains(AbstractSubStubClass.class));
    assertTrue(result.contains(StubClass.class));
    assertTrue(result.contains(SuperStubClass.class));
    assertTrue(result.contains(SuperSuperStubClass.class));
    assertTrue(result.contains(Object.class));
    assertTrue(result.contains(SuperSuperSuperStubInterface.class));
    assertTrue(result.contains(SuperSuperStubInterfaceA.class));
    assertTrue(result.contains(SuperSuperStubInterfaceB.class));
    assertTrue(result.contains(StubInterfaceAlpha.class));
    assertTrue(result.contains(StubInterfaceBeta.class));
    assertTrue(result.contains(StubInterfaceDelta.class));
    assertTrue(result.contains(StubInterfaceGamma.class));
  }

  @Test
  public void testDistanceClassClass() {
    assertEquals(0, distance(null, StubClass.class));
    assertEquals(0, distance(StubClass.class, StubClass.class));
    assertEquals(0, distance(SuperSuperStubInterfaceA.class, SuperSuperStubInterfaceA.class));
    assertEquals(Integer.MAX_VALUE, distance(SuperSuperStubInterfaceA.class, SuperSuperStubInterfaceB.class));
    assertEquals(4, distance(AbstractSubSubStubClass.class, SuperSuperStubClass.class));
    assertEquals(5, distance(AbstractSubSubStubClass.class, Object.class));
    assertEquals(Integer.MAX_VALUE, distance(SuperSuperStubClass.class, AbstractSubSubStubClass.class));
    assertEquals(3, distance(AbstractSubSubStubClass.class, SuperSuperStubInterfaceA.class));
    assertEquals(5, distance(AbstractSubSubStubClass.class, SuperSuperSuperStubInterface.class));
    assertEquals(1, distance(AbstractSubSubStubClass.class, StubInterfaceDelta.class));
    assertEquals(2, distance(AbstractSubSubStubClass.class, StubInterfaceBeta.class));
    assertEquals(1, distance(StubInterfaceBeta.class, SuperSuperStubInterfaceA.class));
  }

}

