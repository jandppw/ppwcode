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

package org.ppwcode.value_III.time;


import static org.junit.Assert.*;
import static org.ppwcode.util.test.contract.Contract.contractFor;
import static org.ppwcode.value_III.time.Duration.Unit.CENTURY;
import static org.ppwcode.value_III.time.Duration.Unit.DAY;
import static org.ppwcode.value_III.time.Duration.Unit.DECENNIUM;
import static org.ppwcode.value_III.time.Duration.Unit.HOUR;
import static org.ppwcode.value_III.time.Duration.Unit.MILLENNIUM;
import static org.ppwcode.value_III.time.Duration.Unit.MILLISECOND;
import static org.ppwcode.value_III.time.Duration.Unit.MINUTE;
import static org.ppwcode.value_III.time.Duration.Unit.MONTH;
import static org.ppwcode.value_III.time.Duration.Unit.QUARTER;
import static org.ppwcode.value_III.time.Duration.Unit.SECOND;
import static org.ppwcode.value_III.time.Duration.Unit.WEEK;
import static org.ppwcode.value_III.time.Duration.Unit.YEAR;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;
import org.ppwcode.value_III.time.Duration.Unit;
import org.ppwcode.value_III.time.interval.AbstractTimeInterval;
import org.ppwcode.value_III.time.interval.TimeInterval;
import org.ppwcode.value_III.time.interval._Contract_TimeInterval;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.ppwcode.vernacular.value_III._Contract_ImmutableValue;


public class DurationTest {

  private List<AbstractTimeInterval> $subjects;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void demo() {
    NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("nl", "BE"));
    System.out.println("Long MAX = " + nf.format(Long.MAX_VALUE));
    System.out.println("MILLISECOND.asMilliseconds() == " + nf.format(MILLISECOND.asMilliseconds()) + ", max = " + nf.format(MILLISECOND.maxDuration()));
    System.out.println("SECOND.asMilliseconds() == " + nf.format(SECOND.asMilliseconds()) + ", max = " + nf.format(SECOND.maxDuration()));
    System.out.println("MINUTE.asMilliseconds() == " + nf.format(MINUTE.asMilliseconds()) + ", max = " + nf.format(MINUTE.maxDuration()));
    System.out.println("HOUR.asMilliseconds() == " + nf.format(HOUR.asMilliseconds()) + ", max = " + nf.format(HOUR.maxDuration()));
    System.out.println("DAY.asMilliseconds() == " + nf.format(DAY.asMilliseconds()) + ", max = " + nf.format(DAY.maxDuration()));
    System.out.println("WEEK.asMilliseconds() == " + nf.format(WEEK.asMilliseconds()) + ", max = " + nf.format(WEEK.maxDuration()));
    System.out.println("MONTH.asMilliseconds() == " + nf.format(MONTH.asMilliseconds()) + ", max = " + nf.format(MONTH.maxDuration()));
    System.out.println("QUARTER.asMilliseconds() == " + nf.format(QUARTER.asMilliseconds()) + ", max = " + nf.format(QUARTER.maxDuration()));
    System.out.println("YEAR.asMilliseconds() == " + nf.format(YEAR.asMilliseconds()) + ", max = " + nf.format(YEAR.maxDuration()));
    System.out.println("DECENNIUM.asMilliseconds() == " + nf.format(DECENNIUM.asMilliseconds()) + ", max = " + nf.format(DECENNIUM.maxDuration()));
    System.out.println("CENTURY.asMilliseconds() == " + nf.format(CENTURY.asMilliseconds()) + ", max = " + nf.format(CENTURY.maxDuration()));
    System.out.println("MILLENNIUM.asMilliseconds() == " + nf.format(MILLENNIUM.asMilliseconds()) + ", max = " + nf.format(MILLENNIUM.maxDuration()));
    System.out.println("Integer MAX = " + nf.format(Integer.MAX_VALUE));

//    System.out.println("Age of the universe = " + new Duration((long)14E9, YEAR)); too big

    Duration maxDuration = new Duration(Long.MAX_VALUE, MILLISECOND);
    assertInvariants(maxDuration);
  }

  public void assertInvariants(Unit u, long expectedMs) {
//    NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("nl", "BE"));
//    System.out.println("Asserting " + u);
//    System.out.println("  " + u.toString() + ".asMilliseconds() == " + nf.format(u.asMilliseconds()));
//    System.out.println("  expectedMs == " + nf.format(expectedMs));
    assertEquals(expectedMs, u.asMilliseconds());
    assertTrue(u.asMilliseconds() > 0);
  }

  @Test
  public void testUnit() {
//    NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("nl", "BE"));
//    System.out.println("Long MAX = " + nf.format(Long.MAX_VALUE));
    assertInvariants(MILLISECOND, 1);
    assertInvariants(SECOND, 1000L);
    assertInvariants(MINUTE, 60L * SECOND.asMilliseconds());
    assertInvariants(HOUR, 60L * MINUTE.asMilliseconds());
    assertInvariants(DAY, 24L * HOUR.asMilliseconds());
    assertInvariants(WEEK, 7L * DAY.asMilliseconds());
    assertInvariants(MONTH, 30L * DAY.asMilliseconds());
    assertInvariants(QUARTER, 3L * MONTH.asMilliseconds());
    assertInvariants(YEAR, 365L * DAY.asMilliseconds());
    assertInvariants(DECENNIUM, 10L * YEAR.asMilliseconds() + 2L * DAY.asMilliseconds());
    assertInvariants(CENTURY, 100L * YEAR.asMilliseconds() + 24L *DAY.asMilliseconds());
    assertInvariants(MILLENNIUM, 1000L * YEAR.asMilliseconds() + 241L * DAY.asMilliseconds());
  }




  public final static long[] LONGS = {0L, 1L, 10L, 1000L, 3600000L, MILLENNIUM.asMilliseconds(), Long.MAX_VALUE};

  private void assertInvariants(Duration subject) {
//    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("nl", "BE"));
//    System.out.println("  subject = " + subject);
    for (Unit u : Unit.values()) {
//      System.out.println("    as " + u + " = " + nf.format(subject.as(u)));
      assertTrue(subject.as(u) >= 0);
    }
  }

  private void testDurationLongUnit(long l, Unit u) {
    if (u == null || l <= u.maxDuration()) {
      Duration subject = new Duration(l, u);
      long expected = (l == 0 ? 0 : l * u.asMilliseconds());
//      System.out.println(l + " " + u + ": " + subject.as(MILLISECOND) + "  " + expected);
      assertInvariants(subject);
      assertTrue(subject.as(MILLISECOND) == expected);
      assertEquals(expected, subject.as(MILLISECOND), expected / 10E6);
    }
    // else, no Duration possible
  }

  @Test
  public void testDurationLongUnit() {
    testDurationLongUnit(0, null);
    for (long l : LONGS) {
      for (Duration.Unit u : Duration.Unit.values()) {
        testDurationLongUnit(l, u);
      }
    }
  }

  public static _Contract_ImmutableValue CONTRACT;
  static {
    try {
      CONTRACT = (_Contract_ImmutableValue)contractFor(ImmutableValue.class);
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  public void testEqualsObject(Duration subject, Object other) {
    boolean result = subject.equals(other);
    CONTRACT.assertEqualsObject(subject, other, result);
    if (other != null  && other instanceof Duration) {
    boolean expected = (subject.as(MILLISECOND) == ((Duration)other).as(MILLISECOND));
    assertEquals(expected, result);
    CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testEqualsObject() {
    for (long lS : LONGS) {
      for (Duration.Unit uS : Duration.Unit.values()) {
        if (lS < uS.maxDuration()) {
          Duration subject = new Duration(lS, uS);
          testEqualsObject(subject, null);
          testEqualsObject(subject, new Object());
          for (long lO : LONGS) {
            for (Duration.Unit uO : Duration.Unit.values()) {
              if (lO < uO.maxDuration()) {
                Duration other = new Duration(lO, uO);
                testEqualsObject(subject, other);
              }
            }
          }
        }
      }
    }
  }

  @Test
  public void testHashCode() {
    fail("Not yet implemented");
  }

  @Test
  public void testDuration() {
    fail("Not yet implemented");
  }

  @Test
  public void testAs() {
    fail("Not yet implemented");
  }

  @Test
  public void testToString() {
    fail("Not yet implemented");
  }

  @Test
  public void testCompareTo() {
    fail("Not yet implemented");
  }

}

