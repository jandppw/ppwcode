/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.TimeHelpers.dayDate;
import static org.ppwcode.value_III.time.TimeHelpers.isDayDate;
import static org.ppwcode.value_III.time.TimeHelpers.le;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DayDateTimeIntervalTest extends AbstractBeginEndTimeIntervalTest {


  @SuppressWarnings("unchecked")
  @Override
  public List<? extends DayDateTimeInterval> subjects() {
    return (List<? extends DayDateTimeInterval>)$subjects;
  }

  @Override
  @Before
  public void setUp() throws IllegalIntervalException {
    List<DayDateTimeInterval> s = new ArrayList<DayDateTimeInterval>();
    final Date now = dayDate(new Date());
    DayDateTimeInterval subject = new DayDateTimeInterval(now, null);
    s.add(subject);
    subject = new DayDateTimeInterval(null, now);
    s.add(subject);
    subject = new DayDateTimeInterval(now, now);
    s.add(subject);
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1);
    final GregorianCalendar gcE = new GregorianCalendar(2122, 11, 31);
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    subject = new DayDateTimeInterval(b, e);
    s.add(subject);
    $subjects = s;
  }

  private Date[] $dates;

  @Before
  public void initDates() {
    GregorianCalendar past = new GregorianCalendar(1995, 3, 24);
    Date now = dayDate(new Date());
    GregorianCalendar future = new GregorianCalendar(2223, 4, 13);
    $dates = new Date[] {null, past.getTime(), now, future.getTime()};
  }

  @After
  public void deInitDates() {
    $dates = null;
  }

  protected void assertInvariants(DayDateTimeInterval subject) {
    super.assertInvariants(subject);
    assertTrue(subject.getBegin() == null || isDayDate(subject.getBegin()));
    assertTrue(subject.getEnd() == null || isDayDate(subject.getEnd()));
  }

  @Test
  public void testDayDateBeginEndTimeInterval() {
    for (Date d1 : $dates) {
      for (Date d2 : $dates) {
        try {
          DayDateTimeInterval subject = new DayDateTimeInterval(d1, d2);
          assertEquals(d1, subject.getBegin());
          assertEquals(d2, subject.getEnd());
          assertInvariants(subject);
        }
        catch (IllegalIntervalException exc) {
          assertTrue(! le(d1, d2) || (d1 != null && ! isDayDate(d1)) ||
                     (d2 != null && ! isDayDate(d2)) || (d1 == null && d2 == null));
        }
      }
    }
  }

  @Test
  public void testDeterminate() {
    for (DayDateTimeInterval subject : subjects()) {
      for (Date d1 : $dates) {
        for (Date d2 : $dates) {
          try {
            DayDateTimeInterval result = subject.determinate(d1, d2);
            CONTRACT.assertDeterminate(subject, d1, d2, result);
          }
          catch (IllegalIntervalException exc) {
            assertTrue(! le(subject.determinateBegin(d1), subject.determinateEnd(d2)) ||
                       (subject.determinateBegin(d1) != null && ! isDayDate(subject.determinateBegin(d1))) ||
                       (subject.determinateBegin(d2) != null && ! isDayDate(subject.determinateBegin(d2))));
          }
          assertInvariants(subject);
        }
      }
    }
  }

}

