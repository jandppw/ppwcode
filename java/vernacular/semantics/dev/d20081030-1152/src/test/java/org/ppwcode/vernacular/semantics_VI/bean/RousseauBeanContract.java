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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.vernacular.exception_II.InternalException.DEFAULT_MESSAGE_KEY;

import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;


public class RousseauBeanContract {

  // interface, not actual tests, only contract

  public static void assertInvariants(RousseauBean subject) {
    SemanticBeanContract.assertInvariants(subject);
  }

  public static void contractEquals(RousseauBean subject, Object other, boolean result) {
    // validate
    SemanticBeanContract.contractEquals(subject, other, result);
  }

  public static void contractHashCode(RousseauBean subject, int result) {
    SemanticBeanContract.contractHashCode(subject, result);
  }

  public static void contractToString(RousseauBean subject, String result) {
    SemanticBeanContract.contractToString(subject, result);
  }

  public static void contractGetWildExceptions(RousseauBean subject, CompoundPropertyException result) {
    assertNotNull(result);
    assertFalse(result.isClosed());
    assertEquals(subject, result.getOrigin());
    assertNull(result.getPropertyName());
    assertEquals(DEFAULT_MESSAGE_KEY, result.getMessage());
    assertNull(result.getCause());
  }

  public static void contractIsCivilized(RousseauBean subject, boolean result) {
    assertEquals(subject.wildExceptions().isEmpty(), result);
  }

  public static void contractPostCheckCivility(boolean OLDCivilized, RousseauBean subject) {
    assertTrue(OLDCivilized);
  }

  public static void contractExcCheckCivility(boolean OLDCivilized, RousseauBean subject, CompoundPropertyException thrown) {
    assertFalse(OLDCivilized);
    assertTrue(thrown.like(subject.wildExceptions()));
    assertTrue(thrown.isClosed());
  }

  public static void contractNormalize(RousseauBean subject, boolean result) {
    // NOP
  }

}

