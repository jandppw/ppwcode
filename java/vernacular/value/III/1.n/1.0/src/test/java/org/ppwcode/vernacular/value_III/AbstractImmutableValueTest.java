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


import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;
import static org.ppwcode.util.test.contract.Contract.contractFor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;
import org.ppwcode.vernacular.value_III.stubs.StubAbstractImmutableValue;
import org.ppwcode.vernacular.value_III.stubs.StubAbstractValue;


public class AbstractImmutableValueTest {

  public static _Contract_ImmutableValue CONTRACT;
  static {
    try {
      CONTRACT = (_Contract_ImmutableValue)contractFor(ImmutableValue.class);
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  private StubAbstractImmutableValue $subject;

  @Before
  public void setUp() throws Exception {
    $subject = new StubAbstractImmutableValue();
  }

  @After
  public void tearDown() throws Exception {
    $subject = null;
  }

  @Test
  public void testImmutableValue() {
    AbstractImmutableValue subject = new StubAbstractImmutableValue();
    CONTRACT.assertInvariants(subject);
  }

  @Test
  public void testEqualsObject1() {
    Object other = new Object();
    testEquals_Object(other);
  }

  @Test
  public void testEqualsObject2() {
    Object other = null;
    testEquals_Object(other);
  }

  @Test
  public void testEqualsObject3() {
    Object other = new StubAbstractValue();
    testEquals_Object(other);
  }

  @Test
  public void testEqualsObject3a() {
    Object other = new StubAbstractImmutableValue();
    testEquals_Object(other);
  }

  @Test
  public void testEqualsObject4() {
    Object other = $subject;
    testEquals_Object(other);
  }

  private void testEquals_Object(Object other) {
    boolean result = $subject.equals(other);
    CONTRACT.assertEqualsObject($subject, other, result);
    CONTRACT.assertInvariants($subject);
  }


}

