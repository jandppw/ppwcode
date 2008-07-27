/*<license>
Copyright 2008 - $Date: 2008-04-04 00:19:52 +0200 (Fri, 04 Apr 2008) $ by Jan Dockx.

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
import static org.junit.Assert.fail;

import org.junit.Test;


public class TestConstants {



  // constant(String,String)

  @Test
  public void testConstantStringString() {
    fail("Not yet implemented");
  }



  // constant(Class,String)

  public final static String STUB_PUBLIC_CONSTANT = "stub public constant";

  @SuppressWarnings("unused")
  private final static int STUB_PRIVATE_CONSTANT = 789;

  final static double STUB_PACKAGE_CONSTANT = 0.12345;

  protected final static Object STUB_PROTECTED_CONSTANT = new Object();

  @Test
  public void testConstant_Class_String1() throws CannotGetValueException {
    String result = Constants.constant(TestConstants.class, "STUB_PUBLIC_CONSTANT");
    assertEquals(STUB_PUBLIC_CONSTANT, result);
  }

  @Test(expected = CannotGetValueException.class)
  public void testConstant_Class_String2() throws CannotGetValueException {
    Constants.constant(TestConstants.class, "STUB_PRIVATE_CONSTANT");
  }

  @Test(expected = CannotGetValueException.class)
  public void testConstant_Class_String3() throws CannotGetValueException {
    Constants.constant(TestConstants.class, "STUB_PACKAGE_CONSTANT");
  }

  @Test(expected = CannotGetValueException.class)
  public void testConstant_Class_String4() throws CannotGetValueException {
    Constants.constant(TestConstants.class, "STUB_PROTECTED_CONSTANT");
  }

  @Test(expected = CannotGetValueException.class)
  public void testConstant_Class_String5() throws CannotGetValueException {
    Class<?> clazz = null;
    Constants.constant(clazz, "STUB_PROTECTED_CONSTANT");
  }

  @Test(expected = CannotGetValueException.class)
  public void testConstant_Class_String6() throws CannotGetValueException {
    Constants.constant(TestConstants.class, null);
  }

  @Test(expected = CannotGetValueException.class)
  public void testConstant_Class_String7() throws CannotGetValueException {
    Class<?> clazz = null;
    Constants.constant(clazz, null);
  }

  @Test(expected = CannotGetValueException.class)
  public void testConstant_Class_String8() throws CannotGetValueException {
    Constants.constant(TestConstants.class, "KLFJJKJ");
  }

}

