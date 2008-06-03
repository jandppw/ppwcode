/*<license>
Copyright 2008 - $Date: 2008/04/03 22:19:23 $ by Jan Dockx.

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


package org.ppwcode.metainfo_I.reader;


import static org.junit.Assert.assertEquals;
import static org.ppwcode.metainfo_I.reader._Classes.loadForName;

import org.junit.Test;
import org.ppwcode.metainfo_I.reader.StubClass.StubClassA;
import org.ppwcode.metainfo_I.reader.StubClass.StubClassB;
import org.ppwcode.metainfo_I.reader.StubClass.StubClassInnerA;
import org.ppwcode.metainfo_I.reader.StubClass.StubClassInnerA.StubClassInnerAInner;

public class Test_Classes {



  // loadForName

  @Test
  public void testLoadForName1() throws CannotGetClassException {
    Class<?> result = loadForName("boolean");
    assertEquals(Boolean.TYPE, result);
  }

  @Test
  public void testLoadForName2() throws CannotGetClassException {
    Class<?> result = loadForName("byte");
    assertEquals(Byte.TYPE, result);
  }

  @Test
  public void testLoadForName3() throws CannotGetClassException {
    Class<?> result = loadForName("char");
    assertEquals(Character.TYPE, result);
  }

  @Test
  public void testLoadForName4() throws CannotGetClassException {
    Class<?> result = loadForName("short");
    assertEquals(Short.TYPE, result);
  }

  @Test
  public void testLoadForName5() throws CannotGetClassException {
    Class<?> result = loadForName("int");
    assertEquals(Integer.TYPE, result);
  }

  @Test
  public void testLoadForName6() throws CannotGetClassException {
    Class<?> result = loadForName("long");
    assertEquals(Long.TYPE, result);
  }

  @Test
  public void testLoadForName7() throws CannotGetClassException {
    Class<?> result = loadForName("float");
    assertEquals(Float.TYPE, result);
  }

  @Test
  public void testLoadForName8() throws CannotGetClassException {
    Class<?> result = loadForName("double");
    assertEquals(Double.TYPE, result);
  }

  @Test(expected = CannotGetClassException.class)
  public void testLoadForName9() throws CannotGetClassException {
    loadForName("hjgks");
  }

  @Test
  public void testLoadForName10() throws CannotGetClassException {
    Class<?> result = loadForName("java.lang.String");
    assertEquals(String.class, result);
  }

  @Test
  public void testLoadForName11() throws CannotGetClassException {
    Class<?> result = loadForName("String");
    assertEquals(String.class, result);
  }

  @Test
  public void testLoadForName12() throws CannotGetClassException {
    Class<?> result = loadForName("org.ppwcode.metainfo_I.reader._Classes");
    assertEquals(_Classes.class, result);
  }

  @Test(expected = CannotGetClassException.class)
  public void testLoadForName13() throws CannotGetClassException {
    loadForName("org.toryt.util_I.reflect.Constants ");
  }

  @Test(expected = CannotGetClassException.class)
  public void testLoadForName14() throws CannotGetClassException {
    loadForName("org.toryt.util_I.reflect. Constants");
  }

  @Test(expected = CannotGetClassException.class)
  public void testLoadForName15() throws CannotGetClassException {
    loadForName("org.toryt.util_I.reflect.Deflection");
  }

  @Test(expected = CannotGetClassException.class)
  public void testLoadForName16() throws CannotGetClassException {
    loadForName("org.toryt.util_I.reflect.String");
  }

  // no test 17

  @Test
  public void testLoadForName18() throws CannotGetClassException {
    Class<?> result = loadForName("org.ppwcode.metainfo_I.reader.StubClass.StubClassA");
    assertEquals(StubClassA.class, result);
  }

  @Test
  public void testLoadForName19() throws CannotGetClassException {
    Class<?> result = loadForName("org.ppwcode.metainfo_I.reader.StubClass.StubClassB");
    assertEquals(StubClassB.class, result);
  }

  @Test
  public void testLoadForName20() throws CannotGetClassException {
    Class<?> result = loadForName("org.ppwcode.metainfo_I.reader.StubClass.StubClassInnerA");
    assertEquals(StubClassInnerA.class, result);
  }

  @Test
  public void testLoadForName21() throws CannotGetClassException {
    Class<?> result = loadForName("org.ppwcode.metainfo_I.reader.StubClass.StubClassInnerA.StubClassInnerAInner");
    assertEquals(StubClassInnerAInner.class, result);
  }

}

