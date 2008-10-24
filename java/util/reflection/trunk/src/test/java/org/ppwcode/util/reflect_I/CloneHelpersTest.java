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
import static org.ppwcode.util.reflect_I.CloneHelpers.isKloneable;
import static org.ppwcode.util.reflect_I.CloneHelpers.klone;
import static org.ppwcode.util.reflect_I.CloneHelpers.safeReference;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.CloneableStubClassA;
import org.ppwcode.util.reflect_I.teststubs.CloneableStubClassB;
import org.ppwcode.util.reflect_I.teststubs.StubClass;


public class CloneHelpersTest {

  @Test
  public void testSafeReference1() {
    CloneableStubClassA stub = new CloneableStubClassA();
    CloneableStubClassA ref = safeReference(stub);
    assertTrue(stub != ref);
  }

  @Test
  public void testSafeReference2() {
    CloneableStubClassB stub = new CloneableStubClassB();
    CloneableStubClassB ref = safeReference(stub);
    assertTrue(stub != ref);
  }

  @Test
  public void testSafeReference3() {
    StubClass stub = new StubClass();
    StubClass ref = safeReference(stub);
    assertTrue(stub == ref);
  }

  @Test
  public void testSafeReference4() {
    Date stub = new Date();
    Date ref = safeReference(stub);
    assertTrue(stub != ref);
    assertEquals(stub, ref);
  }

  @Test
  public void testSafeReference5() {
    Integer stub = new Integer(4);
    Integer ref = safeReference(stub);
    assertTrue(stub == ref);
    assertEquals(stub, ref);
  }

  @Test
  public void testClone_T_1() {
    CloneableStubClassA stub = new CloneableStubClassA();
    CloneableStubClassA klone = klone(stub);
    assertTrue(stub != klone);
  }

  @Test
  public void testClone_T_2() {
    CloneableStubClassB stub = new CloneableStubClassB();
    CloneableStubClassB klone = klone(stub);
    assertTrue(stub != klone);
  }

  @Test(expected = AssertionError.class)
  public void testClone_T_3() {
    StubClass stub = new StubClass();
    klone(stub);
  }

  @Test
  public void testClone_T_4() {
    CloneableStubClassB klone = klone(null);
    Assert.assertNull(klone);
  }

  @Test
  public void testIsCloneable1() {
    boolean result = isKloneable(CloneableStubClassA.class);
    assertTrue(result);
  }

  @Test
  public void testIsCloneable2() {
    boolean result = isKloneable(CloneableStubClassB.class);
    assertTrue(result);
  }

  @Test
  public void testIsCloneable3() {
    boolean result = isKloneable(StubClass.class);
    assertFalse(result);
  }

}

