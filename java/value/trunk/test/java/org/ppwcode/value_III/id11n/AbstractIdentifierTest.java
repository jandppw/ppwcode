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

package org.ppwcode.value_III.id11n;


import static org.ppwcode.util.test.contract.Contract.contractFor;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;


public class AbstractIdentifierTest {

  private static final class StubAbstractIdentifier extends AbstractIdentifier {

    protected StubAbstractIdentifier(String identifier) throws IdentifierWellformednessException {
      super(identifier);
    }

  }

  public static _Contract_Identifier IDENTIFIER_CONTRACT;
  static {
    try {
      IDENTIFIER_CONTRACT = (_Contract_Identifier)contractFor(Identifier.class);
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  public List<? extends AbstractIdentifier> subjects() {
    return $subjects;
  }

  protected List<? extends AbstractIdentifier> $subjects;

  @Before
  public void setUp() throws Exception {
    List<AbstractIdentifier> s = new ArrayList<AbstractIdentifier>();
    AbstractIdentifier subject = new StubAbstractIdentifier("some identifier string");
    s.add(subject);
    $subjects = s;
  }

  @After
  public void tearDown() throws Exception {
    $subjects = null;
  }

  protected void assertInvariants(Identifier subject) {
    IDENTIFIER_CONTRACT.assertInvariants(subject);
  }

  public void testEqualsObject(AbstractIdentifier subject, Object other) {
    boolean result = subject.equals(other);
//    System.out.println(subject + ".equals(" + other + ") == " + result);
    IDENTIFIER_CONTRACT.assertEqualsObject(subject, other, result);
    assertInvariants(subject);
    if (other instanceof Identifier) {
      assertInvariants((Identifier)other);
    }
  }

  @Test
  public void testEqualsObject() {
    for (AbstractIdentifier subject : subjects()) {
      testEqualsObject(subject, null);
      testEqualsObject(subject, new Object());
      for (AbstractIdentifier other : subjects()) {
        testEqualsObject(subject, other);
      }
    }
  }

  @Test
  public void testHashCode() {
    for (AbstractIdentifier subject : subjects()) {
      int result = subject.hashCode();
      IDENTIFIER_CONTRACT.assertHashCode(subject, result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testToString() {
    for (AbstractIdentifier subject : subjects()) {
      String result = subject.toString();
//      System.out.println(result);
      IDENTIFIER_CONTRACT.assertToString(subject, result);
      assertInvariants(subject);
    }
  }

}

