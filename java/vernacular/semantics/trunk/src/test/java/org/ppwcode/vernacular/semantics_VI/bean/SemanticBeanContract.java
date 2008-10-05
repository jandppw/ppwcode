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

package org.ppwcode.vernacular.semantics_VI.bean;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class SemanticBeanContract {

  // interface, not actual tests, only contract

  public static void assertInvariants(SemanticBean subject) {
    // NOP
  }

  public static void contractEquals(SemanticBean subject, Object other, boolean result) {
    // validate
    assertEquals(subject == other, result);
  }

  public static void contractHashCode(SemanticBean subject, int result) {
    // no validation, any number will do
  }

  public static void contractToString(SemanticBean subject, String result) {
    assertNotNull(result);
    assertTrue(result.startsWith(subject.getClass().getCanonicalName() + "@" + Integer.toHexString(subject.hashCode()) + "["));
    assertTrue(result.endsWith("]"));
  }

}

