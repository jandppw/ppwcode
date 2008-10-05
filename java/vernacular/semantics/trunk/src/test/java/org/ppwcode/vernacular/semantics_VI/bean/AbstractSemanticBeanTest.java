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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.NumberOfProperties;


public class AbstractSemanticBeanTest {

  public static class AbstractSemanticBeanSTUB extends AbstractSemanticBean implements NumberOfProperties {

    public AbstractSemanticBeanSTUB(String property1, Date property2, Set<String> property3, int[] property4) {
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


    public int nrOfProperties() {
      return 4;
    }

    public int nrOfSimpleProperties() {
      return nrOfProperties() - 2; // property 3 is a set, property 4 is an array
    }

  }

  public static class AbstractSemanticBeanNOPROPERTIES extends AbstractSemanticBean implements NumberOfProperties {

    public int nrOfProperties() {
      return 0;
    }

    public int nrOfSimpleProperties() {
      return nrOfProperties(); // only simple properties in this class
    }

  }


  private List<AbstractSemanticBean> subjects;

  @Before
  public void setUp() throws Exception {
    subjects = new ArrayList<AbstractSemanticBean>();
    AbstractSemanticBeanSTUB subject = new AbstractSemanticBeanSTUB(null, null, null, null);
    subjects.add(subject);
    Set<String> stringSet = new HashSet<String>();
    stringSet.add("string 1");
    stringSet.add("string 2");
    stringSet.add(null);
    int[] intArray = {5, 6, 4, 8};
    subject = new AbstractSemanticBeanSTUB("PROPERTY 1", null, null, null);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB(null, new Date(), null, null);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB(null, null, stringSet, null);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB(null, null, null, intArray);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB("PROPERTY 1", new Date(), stringSet, intArray);
    subjects.add(subject);
    subjects.addAll(RousseauBeanHelpersTest.someRousseauBeans());
  }

  @After
  public void tearDown() throws Exception {
    subjects = null;
  }

  public static void assertInvariants(SemanticBean subject) {
    // no own invariants
    SemanticBeanContract.assertInvariants(subject);
  }

  public static void testEquals(AbstractSemanticBean subject, Object other) {
    // execute
    boolean result = subject.equals(other);
    // validate
    SemanticBeanContract.contractEquals(subject, other, result);
    assertInvariants(subject);
  }

  @Test
  public void testEqualsObject() {
    for (AbstractSemanticBean subject : subjects) {
      testEquals(subject, null);
      testEquals(subject, subject);
      testEquals(subject, new Object());
      testEquals(subject, new AbstractSemanticBeanSTUB("hfhfh", null, null, null));
    }
  }

  public static void testHashCode(AbstractSemanticBean subject) {
    // execute
    int result = subject.hashCode();
    // validate
    SemanticBeanContract.contractHashCode(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testHashCode() {
    for (AbstractSemanticBean subject : subjects) {
      testHashCode(subject);
    }
  }

  public static void testToString(AbstractSemanticBean subject) {
    // execute
    String result = subject.toString();
    System.out.println(result);
    // validate
    SemanticBeanContract.contractToString(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testToString() {
    for (AbstractSemanticBean subject : subjects) {
      testToString(subject);
    }
  }

  @Test
  public void testClone() {
    for (AbstractSemanticBean subject : subjects) {
      try {
        subject.clone();
        fail();
      }
      catch (CloneNotSupportedException cnsExc) {
        // expected
        assertInvariants(subject);
      }
    }
  }

  public static Set<String> testPropertyNamesForToStringA(AbstractSemanticBean subject) {
    Set<String> result = subject.propertyNamesForToString();
    assertNotNull(result);
//    System.out.println(subject.getClass().getCanonicalName());
    assertEquals(((NumberOfProperties)subject).nrOfSimpleProperties(), result.size());
    assertInvariants(subject);
    return result;
  }

  public static Set<String> testPropertyNamesForToStringB(AbstractSemanticBean subject) {
    Set<String> result = testPropertyNamesForToStringA(subject);
    assertTrue(result.contains("property1"));
    assertTrue(result.contains("property2"));
    return result;
  }

  @Test
  public void testPropertyNamesForToString2() {
    AbstractSemanticBean subject = new AbstractSemanticBeanNOPROPERTIES();
    testPropertyNamesForToStringA(subject);
  }

  @Test
  public void testPropertyNamesForToString1() {
    for (AbstractSemanticBean subject : subjects) {
      testPropertyNamesForToStringB(subject);
    }
  }

  public static void testCollectionString(AbstractSemanticBean subject) {
    assertInvariants(subject);
  }

  @Test
  public void testCollectionString() {
    for (AbstractSemanticBean subject : subjects) {
      testCollectionString(subject);
    }
  }

}

