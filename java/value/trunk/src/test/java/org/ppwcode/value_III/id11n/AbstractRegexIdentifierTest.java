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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.util.reflect_I.ConstantHelpers.constant;
import static org.ppwcode.util.reflect_I.ConstantHelpers.isConstant;
import static org.ppwcode.value_III.id11n.AbstractRegexIdentifier.REGEX_PATTERN_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;


public class AbstractRegexIdentifierTest extends AbstractIdentifierTest {

  public static class StubAbstractRegexIdentifier1 extends AbstractRegexIdentifier {

    public final static Pattern REGEX_PATTERN = Pattern.compile("^\\d{5}$");

    public StubAbstractRegexIdentifier1(String identifier) throws IdentifierWellformednessException {
      super(identifier);
    }

  }

  public static class StubAbstractRegexIdentifier2 extends AbstractRegexIdentifier {

    public final static Pattern REGEX_PATTERN = Pattern.compile("^([ab]{5}(\\d{2}))([xyz]+)$");

    public StubAbstractRegexIdentifier2(String identifier) throws IdentifierWellformednessException {
      super(identifier);
    }

  }

  @Override
  @Before
  public void setUp() throws Exception {
    List<AbstractRegexIdentifier> s = new ArrayList<AbstractRegexIdentifier>();
    AbstractRegexIdentifier subject = new StubAbstractRegexIdentifier1("12345");
    s.add(subject);
    subject = new StubAbstractRegexIdentifier2("aaaaa64z");
    s.add(subject);
    $subjects = s;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<? extends AbstractRegexIdentifier> subjects() {
    return (List<? extends AbstractRegexIdentifier>)$subjects;
  }

  @Override
  protected void assertInvariants(Identifier subject) {
    super.assertInvariants(subject);
    assertTrue(isConstant(subject.getClass(), REGEX_PATTERN_NAME));
    assertEquals("REGEX_PATTERN", REGEX_PATTERN_NAME);
  }

  @Test(expected  = IdentifierWellformednessException.class)
  public void testAbstractRegexIdentifier1() throws IdentifierWellformednessException {
    new StubAbstractRegexIdentifier1("some identifier string");
  }

  @Test(expected  = IdentifierWellformednessException.class)
  public void testAbstractRegexIdentifier2() throws IdentifierWellformednessException {
    new StubAbstractRegexIdentifier1("12");
  }

  @Test(expected  = IdentifierWellformednessException.class)
  public void testAbstractRegexIdentifier3() throws IdentifierWellformednessException {
    new StubAbstractRegexIdentifier1("123456");
  }

  @Test
  public void testAbstractRegexIdentifier4() throws IdentifierWellformednessException {
    new StubAbstractRegexIdentifier1("12345");
  }

  @Test
  public void testAbstractRegexIdentifier5() throws IdentifierWellformednessException {
    new StubAbstractRegexIdentifier2("ababa44y");
  }

  @Test
  public void testGetRegexPattern() {
    for (AbstractRegexIdentifier subject : subjects()) {
      Pattern result = subject.getRegexPattern();
      assertEquals(constant(subject.getClass(), REGEX_PATTERN_NAME), result);
      assertInvariants(subject);
    }
  }

  public void testPatternGroup(AbstractRegexIdentifier subject, int i) {
    String result = subject.patternGroup(i);
//    System.out.println(subject.getIdentifier() + " [" + subject.getRegexPattern() + "](" + i + ") == " + result);
    Matcher m = subject.getRegexPattern().matcher(subject.getIdentifier());
    m.matches();
    String expected = m.group(i);
    assertEquals(expected, result);
    assertInvariants(subject);
  }

  @Test
  public void testPatternGroup() {
    for (AbstractRegexIdentifier subject : subjects()) {
      Matcher m = subject.getRegexPattern().matcher(subject.getIdentifier());
      m.matches();
      for (int i = 0; i <= m.groupCount(); i++) {
        testPatternGroup(subject, i);
      }
    }
  }

}

