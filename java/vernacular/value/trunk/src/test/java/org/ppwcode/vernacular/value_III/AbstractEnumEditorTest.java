/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;

import static org.ppwcode.util.test.contract.Contract.contractFor;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.unexpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;
import org.ppwcode.vernacular.value_III.stubs.StubEnum;
import org.ppwcode.vernacular.value_III.stubs.StubEnumEditor;



public class AbstractEnumEditorTest {

  static public _Contract_AbstractEnumEditor<StubEnum> CONTRACT;
  static {
    try {
      @SuppressWarnings("unchecked")
      _Contract_AbstractEnumEditor<StubEnum> contractFor = (_Contract_AbstractEnumEditor<StubEnum>)contractFor(AbstractEnumEditor.class);
      CONTRACT = contractFor;
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  private List<AbstractEnumEditor<StubEnum>> $subjects;

  @Before
  public void setUp() throws Exception {
    $subjects = new ArrayList<AbstractEnumEditor<StubEnum>>();
    $subjects.add(new StubEnumEditor());
    $subjects.get(0).setValue(StubEnum.ENUM_VALUE_1);
    $subjects.add(new StubEnumEditor());
    $subjects.get(1).setValue(StubEnum.ENUM_VALUE_2);
    $subjects.add(new StubEnumEditor());
    $subjects.get(2).setValue(StubEnum.ENUM_VALUE_3);
    $subjects.add(new StubEnumEditor()); // null value
  }

  @After
  public void tearDown() throws Exception {
    $subjects = null;
  }

  @Test
  public void testGetEnumType() {
    for (AbstractEnumEditor<StubEnum> subject : $subjects) {
      Class<?> result = subject.getEnumType();
      CONTRACT.assertGetEnumType(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testGetExpectedEnumTypeClassName() {
    for (AbstractEnumEditor<StubEnum> subject : $subjects) {
      String result = subject.getExpectedEnumTypeClassName();
      CONTRACT.assertGetExpectedEnumTypeClassName(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testGetTags() {
    for (AbstractEnumEditor<StubEnum> subject : $subjects) {
      String[] result = subject.getTags();
      CONTRACT.assertGetTags(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testGetValuesMap() {
    for (AbstractEnumEditor<StubEnum> subject : $subjects) {
      Map<String, StubEnum> result = subject.getValuesMap();
      CONTRACT.assertGetValuesMap(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  // getLabelsMap is basic

  @Test
  public void testGetAsText() {
    for (AbstractEnumEditor<StubEnum> subject : $subjects) {
      String result = subject.getAsText();
      CONTRACT.assertGetAsText(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testSetAsTextString_Nominal() {
    for (AbstractEnumEditor<StubEnum> subject : $subjects) {
      for (StubEnum se : StubEnum.values()) {
        subject.setAsText(se.name());
        CONTRACT.assertSetAsText_String_Nominal(subject, se.name());
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
    for (AbstractEnumEditor<StubEnum> subject : $subjects) {
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

