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

package org.ppwcode.value_III.legacy;


import junit.framework.TestCase;



public class TestEnterpriseNumber extends TestCase {

//  public static void main(String[] args) {
//    junit.swingui.TestRunner.run(TestEnterpriseNumber.class);
//  }

  public void testEnterpriseNumber_String_String_String_valid1() {
    String left = "0123"; String middle = "456"; String right = "749";
    try {
      new EnterpriseNumber(left, middle, right);
    }
    catch (PropertyException exc) {
      fail("failed with exception: " + exc);
    }
  }

  public void testEnterpriseNumber_String_String_String_valid2() {
    String left = "1222"; String middle = "333"; String right = "424";
    try {
      new EnterpriseNumber(left, middle, right);
    }
    catch (PropertyException exc) {
      fail("failed with exception: " + exc);
    }
  }

  public void testEnterpriseNumber_String_String_String_valid3() {
    String left = "9999"; String middle = "888"; String right = "762";
    try {
      new EnterpriseNumber(left, middle, right);
    }
    catch (PropertyException exc) {
      fail("failed with exception: " + exc);
    }
  }

  public void testEnterpriseNumber_String_String_String_wrong_check() {
    String left = "0123"; String middle = "456"; String right = "750";
    try {
      new EnterpriseNumber(left, middle, right);
      fail("expected an exception, and didn't get one");
    }
    catch (PropertyException exc) {
      // expect an exception
    }
  }

  public void testEnterpriseNumber_String_String_String_wrong_first_pattern() {
    String left = "01234"; String middle = "56"; String right = "789";
    try {
      new EnterpriseNumber(left, middle, right);
      fail("expected an exception, and didn't get one");
    }
    catch (PropertyException exc) {
      // expect an exception
    }
  }

}

