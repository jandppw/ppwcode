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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;


public class AbstractRousseauBeanTest {

  public static class AbstractRousseauBeanSTUB extends AbstractRousseauBean {

    public AbstractRousseauBeanSTUB(String property1, Date property2, Set<String> property3, int[] property4) {
      $property1 = property1;
      $property2 = property2;
      $property3 = property3;
      $property4 = property4;
    }

    public final String getProperty1() {
      return $property1;
    }

    public final void setProperty1(String property1) {
      $property1 = property1;
    }

    private String $property1;


    public final Date getProperty2() {
      return $property2;
    }

    public final void setProperty2(Date property2) {
      $property2 = property2;
    }

    private Date $property2;

    public final Set<String> getProperty3() {
      return $property3;
    }

    public final void setProperty3(Set<String> property3) {
      $property3 = property3;
    }

    private Set<String> $property3;

    public final int[] getProperty4() {
      return $property4;
    }

    public final void setProperty4(int[] property4) {
      $property4 = property4;
    }

    private int[] $property4;

  }

  public static class AbstractRousseauBeanWILD extends AbstractRousseauBeanSTUB {

    public AbstractRousseauBeanWILD(String property1, Date property2, Set<String> property3, int[] property4) {
      super(property1, property2, property3, property4);
    }

    @Override
    public CompoundPropertyException wildExceptions() {
      CompoundPropertyException cpe = super.wildExceptions();
      cpe.addElementException(new PropertyException(this, "property1", null, null));
      cpe.addElementException(new PropertyException(this, "property1", null, null));
      cpe.addElementException(new PropertyException(this, "property2", null, null));
      cpe.addElementException(new PropertyException(this, "property2", null, null));
      cpe.addElementException(new PropertyException(this, "property3", null, null));
      cpe.addElementException(new PropertyException(this, "property3", null, null));
      return cpe;
    }

  }

  public static class AbstractRousseauBeanNOPROPERTIES extends AbstractRousseauBean {
    // NOP
  }


  private List<AbstractRousseauBean> subjects;

  @Before
  public void setUp() throws Exception {
    subjects = new ArrayList<AbstractRousseauBean>();
    AbstractRousseauBeanSTUB subject = new AbstractRousseauBeanSTUB(null, null, null, null);
    subjects.add(subject);
    Set<String> stringSet = new HashSet<String>();
    stringSet.add("string 1");
    stringSet.add("string 2");
    stringSet.add(null);
    int[] intArray = {5, 6, 4, 8};
    subject = new AbstractRousseauBeanSTUB("PROPERTY 1", null, null, null);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB(null, new Date(), null, null);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB(null, null, stringSet, null);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB(null, null, null, intArray);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB("PROPERTY 1", new Date(), stringSet, intArray);
    subjects.add(subject);
    subject = new AbstractRousseauBeanWILD("PROPERTY 1", new Date(), stringSet, intArray);
    subjects.add(subject);
  }

  @After
  public void tearDown() throws Exception {
    subjects = null;
  }

  public static void assertInvariants(RousseauBean subject) {
    // no own invariants
    RousseauBeanContract.assertInvariants(subject);
  }

  public static void testEquals(AbstractRousseauBean subject, Object other) {
    // execute
    boolean result = subject.equals(other);
    // validate
    RousseauBeanContract.contractEquals(subject, other, result);
    assertInvariants(subject);
  }

  @Test
  public void testEqualsObject() {
    for (AbstractRousseauBean subject : subjects) {
      testEquals(subject, null);
      testEquals(subject, subject);
      testEquals(subject, new Object());
      testEquals(subject, new AbstractRousseauBeanSTUB("hfhfh", null, null, null));
    }
  }

  public static void testHashCode(AbstractRousseauBean subject) {
    // execute
    int result = subject.hashCode();
    // validate
    RousseauBeanContract.contractHashCode(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testHashCode() {
    for (AbstractRousseauBean subject : subjects) {
      testHashCode(subject);
    }
  }

  public static void testToString(AbstractRousseauBean subject) {
    // execute
    String result = subject.toString();
    // validate
    RousseauBeanContract.contractToString(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testToString() {
    for (AbstractRousseauBean subject : subjects) {
      testToString(subject);
    }
  }

  @Test
  public void testClone() {
    for (AbstractRousseauBean subject : subjects) {
      try {
        subject.clone();
        fail();
      }
      catch (CloneNotSupportedException cnsExc) {
        assertInvariants(subject);
      }
    }
  }

  public static Set<String> testPropertyNamesForToStringA(AbstractRousseauBean subject, int nrOfProperties) {
    Set<String> result = AbstractSemanticBeanTest.testPropertyNamesForToStringA(subject, nrOfProperties);
    assertFalse(result.contains("wildExceptions"));
    assertFalse(result.contains("civilized"));
    assertInvariants(subject);
    return result;
  }

  public static Set<String> testPropertyNamesForToStringB(AbstractRousseauBean subject, int nrOfProperties) {
    Set<String> result = AbstractSemanticBeanTest.testPropertyNamesForToStringB(subject, nrOfProperties);
    assertFalse(result.contains("wildExceptions"));
    assertFalse(result.contains("civilized"));
    assertInvariants(subject);
    return result;
  }

  @Test
  public void testPropertyNamesForToString2() {
    AbstractRousseauBean subject = new AbstractRousseauBeanNOPROPERTIES();
    testPropertyNamesForToStringA(subject, 0);
  }

  @Test
  public void testPropertyNamesForToString1() {
    for (AbstractRousseauBean subject : subjects) {
      testPropertyNamesForToStringB(subject, 2);
    }
  }

  public static void testCollectionString(AbstractRousseauBean subject) {
    AbstractSemanticBeanTest.testCollectionString(subject);
    assertInvariants(subject);
  }

  @Test
  public void testCollectionString() {
    for (AbstractRousseauBean subject : subjects) {
      testCollectionString(subject);
    }
  }

  public static CompoundPropertyException testGetWildExceptions(AbstractRousseauBean subject) {
    // execute
    CompoundPropertyException result = subject.wildExceptions();
    // validate
    RousseauBeanContract.contractGetWildExceptions(subject, result);
    assertInvariants(subject);
    return result;
  }

  private static void contractPROTECTEDGetWildExceptions(AbstractRousseauBean subject, CompoundPropertyException result) {
    assertNull(result.getPropertyName());
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetWildExceptions() {
    for (AbstractRousseauBean subject : subjects) {
      CompoundPropertyException result = testGetWildExceptions(subject);
      if (subject.getClass() == AbstractRousseauBeanSTUB.class) {
        contractPROTECTEDGetWildExceptions(subject, result);
      }
    }
  }

  public static void testIsCivilized(AbstractRousseauBean subject) {
    // execute
    boolean result = subject.civilized();
    // validate
    RousseauBeanContract.contractIsCivilized(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testIsCivilized() {
    for (AbstractRousseauBean subject : subjects) {
      testIsCivilized(subject);
    }
  }

  public static void testCheckCivility(AbstractRousseauBean subject) {
    boolean OLDcivilized = subject.civilized();
    try {
      subject.checkCivility();
      RousseauBeanContract.contractPostCheckCivility(OLDcivilized, subject);
    }
    catch (CompoundPropertyException thrown) {
      RousseauBeanContract.contractExcCheckCivility(OLDcivilized, subject, thrown);
    }
    assertInvariants(subject);
  }

  @Test
  public void testCheckCivility() {
    for (AbstractRousseauBean subject : subjects) {
      testCheckCivility(subject);
    }
  }

  public static void testNormalize(AbstractRousseauBean subject) {
    // execute
    subject.normalize();
    // validate
    assertInvariants(subject);
  }

  @Test
  public void testNormalize() {
    for (AbstractRousseauBean subject : subjects) {
      testNormalize(subject);
    }
  }

}

