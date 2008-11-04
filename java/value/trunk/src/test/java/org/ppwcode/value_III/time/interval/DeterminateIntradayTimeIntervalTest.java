/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.TimeHelpers.dayDate;
import static org.ppwcode.value_III.time.TimeHelpers.le;
import static org.ppwcode.value_III.time.TimeHelpers.sameDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DeterminateIntradayTimeIntervalTest extends AbstractBeginEndTimeIntervalTest {


  @SuppressWarnings("unchecked")
  @Override
  public List<? extends DeterminateIntradayTimeInterval> subjects() {
    return (List<? extends DeterminateIntradayTimeInterval>)$subjects;
  }

  @Override
  @Before
  public void setUp() throws IllegalIntervalException {
    List<DeterminateIntradayTimeInterval> s = new ArrayList<DeterminateIntradayTimeInterval>();
    final Date now = dayDate(new Date());
    DeterminateIntradayTimeInterval subject = new DeterminateIntradayTimeInterval(now, now);
    s.add(subject);
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1, 14, 22, 2);
    final GregorianCalendar gcE = new GregorianCalendar(2000, 0, 1, 22, 34, 12);
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    subject = new DeterminateIntradayTimeInterval(b, e);
    s.add(subject);
    $subjects = s;
  }

  private Date[] $dates;

  @Before
  public void initDates() {
    GregorianCalendar past1 = new GregorianCalendar(1995, 3, 24, 14, 55, 34);
    GregorianCalendar past2 = new GregorianCalendar(1995, 3, 24, 16, 3, 12);
    Date now = new Date();
    GregorianCalendar future1 = new GregorianCalendar(2223, 4, 13, 3, 45, 45);
    GregorianCalendar future2 = new GregorianCalendar(2223, 4, 13, 6, 32, 44);
    $dates = new Date[] {null, past1.getTime(), past2.getTime(), now, future1.getTime(), future2.getTime()};
  }

  @After
  public void deInitDates() {
    $dates = null;
  }

  protected void assertInvariants(DayDateTimeInterval subject) {
    super.assertInvariants(subject);
    assertNotNull(subject.getBegin());
    assertNotNull(subject.getEnd());
    assertTrue(sameDay(subject.getBegin(), subject.getEnd()));
  }

  @Test
  public void testDeterminateIntradayTimeInterval() {
    for (Date d1 : $dates) {
      for (Date d2 : $dates) {
        try {
          DeterminateIntradayTimeInterval subject = new DeterminateIntradayTimeInterval(d1, d2);
          assertEquals(d1, subject.getBegin());
          assertEquals(d2, subject.getEnd());
          assertInvariants(subject);
        }
        catch (IllegalIntervalException exc) {
          assertTrue(d1 == null || d2 == null || ! le(d1, d2) || ! sameDay(d1, d2));
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
          CONTRACT.assertDeterminate(subject, d1, d2, result);
          assertInvariants(subject);
        }
      }
    }
  }

  @Test
  public void testGetDay() {
    for (DeterminateIntradayTimeInterval subject : subjects()) {
      Date result = subject.getDay();
      Date expected = subject.getBegin() != null ? dayDate(subject.getBegin()) :
                        (subject.getEnd() != null ? dayDate(subject.getEnd()) : null);
      assertEquals(expected, result);
      assertInvariants(subject);
    }
  }

}

