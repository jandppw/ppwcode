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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;
import org.ppwcode.vernacular.value_III.stubs.StubEnumerationValue;
import org.ppwcode.vernacular.value_III.stubs.StubEnumerationValueEditor;


public class AbstractEnumerationValueEditorTest {

  public static _Contract_AbstractEnumerationValueEditor CONTRACT;
  static {
    try {
      CONTRACT = (_Contract_AbstractEnumerationValueEditor)contractFor(AbstractEnumerationValueEditor.class);
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  protected List<AbstractEnumerationValueEditor<StubEnumerationValue>> $subjects;

  @Before
  public void setUp() throws Exception {
    $subjects = new ArrayList<AbstractEnumerationValueEditor<StubEnumerationValue>>(5);
    AbstractEnumerationValueEditor<StubEnumerationValue> aevEd = new StubEnumerationValueEditor();
    aevEd.setValue(StubEnumerationValue.VALUE_1);
    $subjects.add(aevEd);
    aevEd = new StubEnumerationValueEditor();
    aevEd.setValue(StubEnumerationValue.VALUE_2);
    $subjects.add(aevEd);
    aevEd = new StubEnumerationValueEditor();
    aevEd.setValue(StubEnumerationValue.VALUE_3);
    $subjects.add(aevEd);
    aevEd = new StubEnumerationValueEditor();
    aevEd.setValue(StubEnumerationValue.VALUE_DEFAULT);
    $subjects.add(aevEd);
    aevEd = new StubEnumerationValueEditor();
    $subjects.add(aevEd);
  }

  @After
  public void tearDown() throws Exception {
    $subjects = null;
  }

  @Test
  public void testGetEnumerationValueType() {
    for (AbstractEnumerationValueEditor<StubEnumerationValue> subject : $subjects) {
      Class<?> result = subject.getValueType();
      CONTRACT.assertGetValueType(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testGetExpectedEnumerationValueTypeClassName() {
    for (AbstractEnumerationValueEditor<StubEnumerationValue> subject : $subjects) {
      String result = subject.getExpectedValueTypeClassName();
      CONTRACT.assertGetExpectedValueTypeClassName(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testGetTags() {
    for (AbstractEnumerationValueEditor<StubEnumerationValue> subject : $subjects) {
      String[] result = subject.getTags();
      CONTRACT.assertGetTags(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testGetValuesMap() {
    for (AbstractEnumerationValueEditor<StubEnumerationValue> subject : $subjects) {
      Map<String, ?> result = subject.getValuesMap();
      CONTRACT.assertGetValuesMap(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  // getLabelsMap is basic

  @Test
  public void testGetAsText() {
    for (AbstractEnumerationValueEditor<StubEnumerationValue> subject : $subjects) {
      String result = subject.getAsText();
      CONTRACT.assertGetAsText(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testSetAsTextString_Nominal() {
    for (AbstractEnumerationValueEditor<StubEnumerationValue> subject : $subjects) {
      for (String tag : StubEnumerationValue.VALUES.keySet()) {
        subject.setAsText(tag);
        CONTRACT.assertSetAsText_String_Nominal(subject, tag);
        CONTRACT.assertInvariants(subject);
      }
      subject.setAsText(null);
      CONTRACT.assertSetAsText_String_Nominal(subject, null);
      CONTRACT.assertInvariants(subject);
      subject.setAsText("");
      CONTRACT.assertSetAsText_String_Nominal(subject, "");
      CONTRACT.assertInvariants(subject);
    }
  }

  public final static String[] WRONG_TAGS = new String[] {" ", "DOESNTEXIST"};

  @Test
  public void testSetAsTextString_Exception() {
    for (AbstractEnumerationValueEditor<StubEnumerationValue> subject : $subjects) {
      for (String tag : WRONG_TAGS) {
        Object oldValue = subject.getValue();
        try {
          subject.setAsText(tag);
          org.junit.Assert.fail("subject: " + subject + "; tag: " + tag);
        }
        catch (IllegalArgumentException exc) {
          CONTRACT.assertSetAsText_String_Exception(subject, tag, exc, oldValue);
          CONTRACT.assertInvariants(subject);
        }
      }
    }
  }

}

