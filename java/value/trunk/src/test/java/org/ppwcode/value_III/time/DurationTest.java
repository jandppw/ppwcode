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
import static org.ppwcode.value_III.time.Duration.Unit.MILLISECOND;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;
import org.ppwcode.value_III.time.Duration.Unit;
import org.ppwcode.value_III.time.interval.AbstractTimeInterval;
import org.ppwcode.value_III.time.interval.TimeInterval;
import org.ppwcode.value_III.time.interval._Contract_TimeInterval;


public class DurationTest {

  private List<AbstractTimeInterval> $subjects;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  public final static long[] LONGS = {0, 1, 10, 1000, 3600000, ((1000 * 365) + 241) * 24 * 60 * 60 * 1000, Long.MAX_VALUE};

  private void assertInvariants(Duration subject) {
    for (Unit u : Unit.values()) {
      assertTrue(subject.as(u) >= 0);
    }
  }

  private void testDurationLongUnit(long l, Unit u) {
    Duration subject = new Duration(l, u);
    long expected = (l == 0 ? 0 : l * u.asMilliseconds());
//    if (expected < 0) {
//      System.out.println("problem");
//    }
    System.out.println(subject.as(MILLISECOND) + "  " + expected);
    assertTrue(subject.as(MILLISECOND) == expected);
    assertEquals(expected, subject.as(MILLISECOND), 1E-5);
    assertInvariants(subject);
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
  public void testEqualsObject() {
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

