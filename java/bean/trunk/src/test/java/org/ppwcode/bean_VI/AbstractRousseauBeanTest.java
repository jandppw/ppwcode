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

package org.ppwcode.bean_VI;


import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


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

  public static void testPropertyNamesForToString1(AbstractRousseauBean subject) {
    AbstractSemanticBeanTest.testPropertyNamesForToString1(subject);
    assertInvariants(subject);
  }

  @Test
  public void testPropertyNamesForToString1() {
    for (AbstractRousseauBean subject : subjects) {
      testPropertyNamesForToString1(subject);
    }
  }

  public static void testPropertyNamesForToString2(AbstractRousseauBean subject) {
    AbstractSemanticBeanTest.testPropertyNamesForToString1(subject);
    assertInvariants(subject);
  }

  @Test
  public void testPropertyNamesForToString2() {
    AbstractRousseauBean subject = new AbstractRousseauBeanNOPROPERTIES();
    testPropertyNamesForToString2(subject);
  }

  @Test
  public void testGetWildExceptions() {
    fail("Not yet implemented");
  }

  @Test
  public void testIsCivilized() {
    fail("Not yet implemented");
  }

  @Test
  public void testCheckCivility() {
    fail("Not yet implemented");
  }

  @Test
  public void testNormalize() {
    fail("Not yet implemented");
  }

}

