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

package org.ppwcode.vernacular.value_III;


import static org.junit.Assert.assertEquals;

import java.beans.PropertyEditorManager;

import org.junit.Test;
import org.ppwcode.vernacular.value_III.stubs.StubEnum;
import org.ppwcode.vernacular.value_III.stubs.StubValue;


public class ValueHelpersTest {

  @Test
  public void testEqn1() {
    Object one = new Object();
    Object other = new Object();
    testEqn(one, other);
  }

  @Test
  public void testEqn2() {
    Object one = new StubValue();
    Object other = new StubValue();
    testEqn(one, other);
  }

  @Test
  public void testEqn3() {
    Object one = new StubValue();
    testEqn(one, one);
  }

  @Test
  public void testEqn4() {
    Object one = StubEnum.ENUM_VALUE_1;
    Object other = StubEnum.ENUM_VALUE_2;
    testEqn(one, other);
  }

  @Test
  public void testEqn5() {
    Object one = StubEnum.ENUM_VALUE_1;
    testEqn(one, one);
  }

  @Test
  public void testEqn6() {
    Object one = new Object();
    testEqn(one, null);
  }

  @Test
  public void testEqn7() {
    Object other = new Object();
    testEqn(null, other);
  }

  @Test
  public void testEqn8() {
    Object one = new StubValue();
    testEqn(one, null);
  }

  @Test
  public void testEqn9() {
    Object other = new StubValue();
    testEqn(null, other);
  }

  @Test
  public void testEqn10() {
    Object one = StubEnum.ENUM_VALUE_2;
    testEqn(one, null);
  }

  @Test
  public void testEqn11() {
    Object other = StubEnum.ENUM_VALUE_3;
    testEqn(null, other);
  }

  @Test
  public void testEqn12() {
    testEqn(null, null);
  }

  private void testEqn(Object one, Object other) {
    boolean expected = one == null ? other == null : one.equals(other);
    boolean result = ValueHelpers.eqn(one, other);
    assertEquals(expected, result);
  }

  private final static String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";

  @Test
  public void testRegisterPropertyEditors() {
    String[] oldEditorSearchPath = PropertyEditorManager.getEditorSearchPath();
    ValueHelpers.registerPropertyEditorPackage(TEST_PACKAGE_NAME);
    String[] editorSearchPath = PropertyEditorManager.getEditorSearchPath();
    assertEquals(oldEditorSearchPath.length + 1, editorSearchPath.length);
    for (int i = 0; i < oldEditorSearchPath.length; i++) {
      assertEquals(oldEditorSearchPath[i], editorSearchPath[i]);
    }
    assertEquals(TEST_PACKAGE_NAME, editorSearchPath[oldEditorSearchPath.length]);
  }

}

