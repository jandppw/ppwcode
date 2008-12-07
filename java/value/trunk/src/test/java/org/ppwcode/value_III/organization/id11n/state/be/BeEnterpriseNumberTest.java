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

package org.ppwcode.value_III.organization.id11n.state.be;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.organization.state.be.id11n.BeEnterpriseNumber.CONTROL_CONSTANT;
import static org.ppwcode.value_III.organization.state.be.id11n.BeEnterpriseNumber.REGEX_PATTERN;
import static org.ppwcode.value_III.organization.state.be.id11n.BeEnterpriseNumber.REGEX_PATTERN_STRING;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ppwcode.value_III.id11n.AbstractRegexIdentifierTest;
import org.ppwcode.value_III.id11n.Identifier;
import org.ppwcode.value_III.id11n.IdentifierWellformednessException;
import org.ppwcode.value_III.organization.state.be.id11n.BeEnterpriseNumber;
import org.ppwcode.vernacular.value_III.ValueException;


public class BeEnterpriseNumberTest extends AbstractRegexIdentifierTest {


  @Override
  @Before
  public void setUp() throws Exception {
    List<BeEnterpriseNumber> s = new ArrayList<BeEnterpriseNumber>();
    BeEnterpriseNumber subject = new BeEnterpriseNumber("0453834195"); // PeopleWare
    s.add(subject);
    subject = new BeEnterpriseNumber(fakeEnterpriseNumberString(12345678));
    s.add(subject);
    subject = new BeEnterpriseNumber(fakeEnterpriseNumberString(0));
    s.add(subject);
    subject = new BeEnterpriseNumber(fakeEnterpriseNumberString(1));
    s.add(subject);
    subject = new BeEnterpriseNumber(fakeEnterpriseNumberString(10000000));
    s.add(subject);
    subject = new BeEnterpriseNumber(fakeEnterpriseNumberString(/*0*/7654321));
    s.add(subject);
    $subjects = s;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<? extends BeEnterpriseNumber> subjects() {
    return (List<? extends BeEnterpriseNumber>)$subjects;
  }

  @Override
  protected void assertInvariants(Identifier subject) {
    super.assertInvariants(subject);
    assertEquals(REGEX_PATTERN.pattern(), REGEX_PATTERN_STRING);
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBeEnterpriseNumber1() throws IdentifierWellformednessException {
    new BeEnterpriseNumber("0123456789");
  }

  @Test
  public void testBeEnterpriseNumber2() throws IdentifierWellformednessException {
    String identifier = "0453834195"; // PeopleWare
    BeEnterpriseNumber subject = new BeEnterpriseNumber("0453834195");
    testBeEnterpriseNumber(subject, identifier);
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBeEnterpriseNumber2b() throws IdentifierWellformednessException {
    String identifier = "0453834195"; // PeopleWare
    BeEnterpriseNumber subject = new BeEnterpriseNumber("0453834194");
    testBeEnterpriseNumber(subject, identifier);
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBeEnterpriseNumber3() throws IdentifierWellformednessException {
    new BeEnterpriseNumber("BE 0453834195");
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBeEnterpriseNumber4() throws IdentifierWellformednessException {
    new BeEnterpriseNumber(" 0453834195");
  }

  @Test(expected = IdentifierWellformednessException.class)
  public void testBeEnterpriseNumber5() throws IdentifierWellformednessException {
    new BeEnterpriseNumber("0453.834.195");
  }

  @Test
  public void testBeEnterpriseNumber6() throws IdentifierWellformednessException {
    String identifier = fakeEnterpriseNumberString(12345678);
    BeEnterpriseNumber subject = new BeEnterpriseNumber(identifier);
    testBeEnterpriseNumber(subject, identifier);
  }

  private void testBeEnterpriseNumber(BeEnterpriseNumber subject, String identifier) {
    assertEquals(identifier, subject.getIdentifier());
    assertInvariants(subject);
  }

  private String fakeEnterpriseNumberString(int base) {
    NumberFormat nf = NumberFormat.getIntegerInstance();
    nf.setGroupingUsed(false);
    nf.setMinimumIntegerDigits(8);
    nf.setMaximumIntegerDigits(8);
    String baseString = nf.format(base);
    int mod = base % CONTROL_CONSTANT;
    int control = CONTROL_CONSTANT - mod;
//    System.out.println(control);
    nf.setMinimumIntegerDigits(2);
    nf.setMaximumIntegerDigits(2);
    String controlString = nf.format(control);
    String result = baseString + controlString;
    return result;
  }

  @Test
  public void testGetMain() {
    for (BeEnterpriseNumber subject : subjects()) {
      String result = subject.getMain();
      assertEquals(subject.patternGroup(1), result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testGetMainAsInt() {
    for (BeEnterpriseNumber subject : subjects()) {
      int result = subject.getMainAsInt();
      assertEquals(Integer.parseInt(subject.getMain()), result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testGetControl() {
    for (BeEnterpriseNumber subject : subjects()) {
      String result = subject.getControl();
      assertEquals(subject.patternGroup(4), result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testGetControlAsInt() {
    for (BeEnterpriseNumber subject : subjects()) {
      int result = subject.getControlAsInt();
      assertEquals(Integer.parseInt(subject.getControl()), result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testGetPrefix() {
    for (BeEnterpriseNumber subject : subjects()) {
      String result = subject.getPrefix();
      assertEquals(subject.patternGroup(2), result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testIsOldVatNumberBased() {
    for (BeEnterpriseNumber subject : subjects()) {
      boolean result = subject.isOldVatNumberBased();
      assertEquals(subject.patternGroup(2).equals("0"), result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testGetOldVatNumber() {
    for (BeEnterpriseNumber subject : subjects()) {
      try {
        String result = subject.getOldVatNumber();
        assertEquals(subject.patternGroup(3) + subject.getControl(), result);
      }
      catch (ValueException svExc) {
        assertTrue(! subject.isOldVatNumberBased());
      }
      assertInvariants(subject);
    }
  }

//  @Override
//  @Test
//  public void testToString() {
//    for (BeEnterpriseNumber subject : subjects()) {
//      System.out.println(subject.toString());
//      assertInvariants(subject);
//    }
//  }

}

