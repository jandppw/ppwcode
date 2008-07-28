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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.toryt.util_I.reflect.StubClass.StubClassA;
import org.toryt.util_I.reflect.StubClass.StubClassB;
import org.toryt.util_I.reflect.StubClass.StubClassInnerA;
import org.toryt.util_I.reflect.StubClass.StubClassInnerB;


public class TestClasses {



  // loadForName

  @Test
  public void testLoadForName1() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("boolean");
    assertEquals(Boolean.TYPE, result);
  }

  @Test
  public void testLoadForName2() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("byte");
    assertEquals(Byte.TYPE, result);
  }

  @Test
  public void testLoadForName3() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("char");
    assertEquals(Character.TYPE, result);
  }

  @Test
  public void testLoadForName4() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("short");
    assertEquals(Short.TYPE, result);
  }

  @Test
  public void testLoadForName5() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("int");
    assertEquals(Integer.TYPE, result);
  }

  @Test
  public void testLoadForName6() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("long");
    assertEquals(Long.TYPE, result);
  }

  @Test
  public void testLoadForName7() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("float");
    assertEquals(Float.TYPE, result);
  }

  @Test
  public void testLoadForName8() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("double");
    assertEquals(Double.TYPE, result);
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName9() throws _CannotGetClassException {
    Classes.loadForName("hjgks");
  }

  @Test
  public void testLoadForName10() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("java.lang.String");
    assertEquals(String.class, result);
  }

  @Test
  public void testLoadForName11() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("String");
    assertEquals(String.class, result);
  }

  @Test
  public void testLoadForName12() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("org.toryt.util_I.reflect.Constants");
    assertEquals(Constants.class, result);
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName13() throws _CannotGetClassException {
    Classes.loadForName("org.toryt.util_I.reflect.Constants ");
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName14() throws _CannotGetClassException {
    Classes.loadForName("org.toryt.util_I.reflect. Constants");
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName15() throws _CannotGetClassException {
    Classes.loadForName("org.toryt.util_I.reflect.Deflection");
  }

  @Test(expected = _CannotGetClassException.class)
  public void testLoadForName16() throws _CannotGetClassException {
    Classes.loadForName("org.toryt.util_I.reflect.String");
  }

  @Test
  public void testLoadForName17() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("org.toryt.util_I.reflect.TestConstants");
    assertEquals(TestConstants.class, result);
  }

  @Test
  public void testLoadForName18() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("org.toryt.util_I.reflect.StubClass.StubClassA");
    assertEquals(StubClassA.class, result);
  }

  @Test
  public void testLoadForName19() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("org.toryt.util_I.reflect.StubClass.StubClassB");
    assertEquals(StubClassB.class, result);
  }

  @Test
  public void testLoadForName20() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("org.toryt.util_I.reflect.StubClass.StubClassInnerA");
    assertEquals(StubClassInnerA.class, result);
  }

  @Test
  public void testLoadForName21() throws _CannotGetClassException {
    Class<?> result = Classes.loadForName("org.toryt.util_I.reflect.StubClass.StubClassInnerA.StubClassInnerAInner");
    assertEquals(StubClassInnerA.StubClassInnerAInner.class, result);
  }



  // prefixedDqcn

  @Test
  public void testPrefixedFqcn() {
    fail("Not yet implemented");
  }



  // instantiatePrefixed

  @Test
  public void testInstantiatePrefixed() {
    fail("Not yet implemented");
  }



  // isInnerClass(Class)

  @Test
  public void testIsInnerClass1() {
    assertFalse(Classes.isInnerClass(TestConstants.class));
  }

  @Test
  public void testIsInnerClass2() {
    assertFalse(Classes.isInnerClass(Constants.class));
  }

  @Test
  public void testIsInnerClass3() {
    assertFalse(Classes.isInnerClass(StubClassA.class));
  }

  @Test
  public void testIsInnerClass4() {
    assertFalse(Classes.isInnerClass(StubClassB.class));
  }

  @Test
  public void testIsInnerClass5() {
    assertTrue(Classes.isInnerClass(StubClassInnerA.class));
  }

  @Test
  public void testIsInnerClass6() {
    assertTrue(Classes.isInnerClass(StubClassInnerB.class));
  }

  @Test
  public void testIsInnerClass7() {
    class StubClassLocalA {
      // NOP
    }
    assertTrue(Classes.isInnerClass(StubClassLocalA.class));
  }

  @Test
  public void testIsInnerClass8() {
    class StubClassLocalA {
      // NOP
    }
    class StubClassLocalB extends StubClassLocalA {
      // NOP
    }
    assertTrue(Classes.isInnerClass(StubClassLocalB.class));
  }

  @Test
  public void testIsInnerClass9() {
    // anonymous class
    assertTrue(Classes.isInnerClass((new StubClassA("", 0, 0) { /* NOP */ }).getClass()));
  }



  // isTopLevelClass(Class)

  @Test
  public void testIsTopLevelClass1() {
    assertTrue(Classes.isTopLevelClass(TestConstants.class));
  }

  @Test
  public void testIsTopLevelClass2() {
    assertTrue(Classes.isTopLevelClass(Constants.class));
  }

  @Test
  public void testIsTopLevelClass3() {
    assertFalse(Classes.isTopLevelClass(StubClassA.class));
  }

  @Test
  public void testIsTopLevelClass4() {
    assertFalse(Classes.isTopLevelClass(StubClassB.class));
  }

  @Test
  public void testIsTopLevelClass5() {
    assertFalse(Classes.isTopLevelClass(StubClassInnerA.class));
  }

  @Test
  public void testIsTopLevelClass6() {
    assertFalse(Classes.isTopLevelClass(StubClassInnerB.class));
  }

  @Test
  public void testIsTopLevelClass7() {
    class StubClassLocalA {
      // NOP
    }
    assertFalse(Classes.isTopLevelClass(StubClassLocalA.class));
  }

  @Test
  public void testIsTopLevelClass8() {
    class StubClassLocalA {
      // NOP
    }
    class StubClassLocalB extends StubClassLocalA {
      // NOP
    }
    assertFalse(Classes.isTopLevelClass(StubClassLocalB.class));
  }

  @Test
  public void testIsTopLevelClass9() {
    // anonymous class
    assertFalse(Classes.isTopLevelClass((new StubClassA("", 0, 0) { /* NOP */ }).getClass()));
  }



  // arrayClassForName()

  @Test
  public void testArrayClassForName() {
    Class<Constants[]> result = Classes.arrayClassForName(Constants.class);
    assertEquals(Constants[].class, result);
  }

}

