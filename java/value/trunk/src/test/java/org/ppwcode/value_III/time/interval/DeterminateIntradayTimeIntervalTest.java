/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.TimeHelpers.le;
import static org.ppwcode.value_III.time.TimeHelpers.sameDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;


public class DeterminateIntradayTimeIntervalTest extends AbstractIntradayTimeIntervalTest {


  @SuppressWarnings("unchecked")
  @Override
  public List<? extends DeterminateIntradayTimeInterval> subjects() {
    return (List<? extends DeterminateIntradayTimeInterval>)$subjects;
  }

  @Override
  @Before
  public void setUp() throws IllegalTimeIntervalException {
    List<DeterminateIntradayTimeInterval> s = new ArrayList<DeterminateIntradayTimeInterval>();
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1, 14, 22, 2);
    final GregorianCalendar gcE = new GregorianCalendar(2000, 0, 1, 22, 34, 12);
    final Date now = new Date();
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    for (TimeZone tz : TIMEZONES) {
      DeterminateIntradayTimeInterval subject = new DeterminateIntradayTimeInterval(now, now, tz);
      s.add(subject);
      if (sameDay(b, e, tz)) {
        subject = new DeterminateIntradayTimeInterval(b, e, tz);
        s.add(subject);
      }
    }
    $subjects = s;
  }

  @Override
  protected void assertInvariants(TimeInterval subject) {
    super.assertInvariants(subject);
    assertNotNull(subject.getBegin());
    assertNotNull(subject.getEnd());
  }

  @Override
  @Test
  public void testCONSTRUCTOR() {
    for (Date d1 : $dates) {
      for (Date d2 : $dates) {
        for (TimeZone tz : TIMEZONES) {
          try {
            DeterminateIntradayTimeInterval subject = new DeterminateIntradayTimeInterval(d1, d2, tz);
            assertEquals(d1, subject.getBegin());
            assertEquals(d2, subject.getEnd());
            assertEquals(tz, subject.getTimeZone());
            assertInvariants(subject);
          }
          catch (IllegalTimeIntervalException exc) {
            assertTrue(d1 == null || d2 == null || ! le(d1, d2) || ! sameDay(d1, d2, tz));
          }
        }
      }
    }
  }

  @Test
  public void testDeterminate() {
    for (DeterminateIntradayTimeInterval subject : subjects()) {
      for (Date d1 : $dates) {
        for (Date d2 : $dates) {
          DeterminateIntradayTimeInterval result = subject.determinate(d1, d2);
          TIMEINTERVAL_CONTRACT.assertDeterminate(subject, d1, d2, result);
          assertInvariants(subject);
          System.out.println("subject = " + subject);
          System.out.println("result = " + result);
          System.out.println("subject == result ? " + (subject == result));
          System.out.println("subject.equals(result) ? " + subject.equals(result));
          System.out.println("result.equals(subject) ? " + result.equals(subject));
          assertEquals(subject, result);
        }
      }
    }
  }

}

