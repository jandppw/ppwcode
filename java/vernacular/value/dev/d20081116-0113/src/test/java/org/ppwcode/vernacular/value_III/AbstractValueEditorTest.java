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

import static org.ppwcode.util.test.contract.Contract.contractFor;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.unexpectedException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;
import org.ppwcode.vernacular.value_III.stubs.StubAbstractValueEditor;
import org.ppwcode.vernacular.value_III.stubs.StubAbstractValue;


public class AbstractValueEditorTest {

  public static _Contract_AbstractValueEditor CONTRACT;
  static {
    try {
      CONTRACT = (_Contract_AbstractValueEditor)contractFor(AbstractValueEditor.class);
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  AbstractValueEditor<StubAbstractValue>[] $subjects;

  @Before
  public void setUp() throws Exception {
    $subjects = new StubAbstractValueEditor[1];
    $subjects[0] = new StubAbstractValueEditor();
  }

  @After
  public void tearDown() throws Exception {
    $subjects = null;
  }

  @Test
  public void testGetValueType() {
    for (AbstractValueEditor<?> subject : $subjects) {
      Class<?> result = subject.getValueType();
      CONTRACT.assertGetValueType(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testGetExpectedValueTypeClassName() {
    for (AbstractValueEditor<?> subject : $subjects) {
      String result = subject.getExpectedValueTypeClassName();
      CONTRACT.assertGetExpectedValueTypeClassName(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

}

