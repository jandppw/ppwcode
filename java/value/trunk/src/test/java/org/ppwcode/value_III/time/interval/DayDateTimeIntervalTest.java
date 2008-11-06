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
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DayDateTimeIntervalTest extends AbstractBeginEndTimeTimeZoneIntervalTest {


  @SuppressWarnings("unchecked")
  @Override
  public List<? extends DayDateTimeInterval> subjects() {
    return (List<? extends DayDateTimeInterval>)$subjects;
  }

  @Override
  @Before
  public void setUp() throws IllegalTimeIntervalException {
    List<DayDateTimeInterval> s = new ArrayList<DayDateTimeInterval>();
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1);
    final GregorianCalendar gcE = new GregorianCalendar(2122, 11, 31);
    for (TimeZone tz : TIMEZONES) {
      final Date now = dayDate(new Date(), tz);
      final Date b = dayDate(gcB.getTime(), tz);
      final Date e = dayDate(gcE.getTime(), tz);
      DayDateTimeInterval subject = new DayDateTimeInterval(now, null, tz);
      s.add(subject);
      subject = new DayDateTimeInterval(null, now, tz);
      s.add(subject);
      subject = new DayDateTimeInterval(now, now, tz);
      s.add(subject);
      subject = new DayDateTimeInterval(b, e, tz);
      s.add(subject);
    }
    $subjects = s;
  }

  private List<Date> $dates = new LinkedList<Date>();

  @Before
  public void initDates() {
    $dates = new LinkedList<Date>();
    $dates.add(null);
    GregorianCalendar past = new GregorianCalendar(1995, 3, 24);
    Date now = new Date();
    GregorianCalendar future = new GregorianCalendar(2223, 4, 13);
    for (TimeZone tz : TIMEZONES) {
      $dates.add(dayDate(past.getTime(), tz));
      $dates.add(dayDate(now, tz));
      $dates.add(dayDate(future.getTime(), tz));
    }
  }

  @After
  public void deInitDates() {
    $dates = null;
  }

  protected void assertInvariants(DayDateTimeInterval subject) {
    super.assertInvariants(subject);
    assertTrue(subject.getBegin() == null || isDayDate(subject.getBegin(), subject.getTimeZone()));
    assertTrue(subject.getEnd() == null || isDayDate(subject.getEnd(), subject.getTimeZone()));
  }

  @Test
  public void testDayDateBeginEndTimeInterval() {
    for (Date d1 : $dates) {
      for (Date d2 : $dates) {
        for (TimeZone tz : TIMEZONES) {
          try {
            DayDateTimeInterval subject = new DayDateTimeInterval(d1, d2, tz);
            assertEquals(d1, subject.getBegin());
            assertEquals(d2, subject.getEnd());
            assertEquals(tz, subject.getTimeZone());
            assertInvariants(subject);
          }
          catch (IllegalTimeIntervalException exc) {
            assertTrue(! le(d1, d2) || (d1 != null && ! isDayDate(d1, tz)) ||
                       (d2 != null && ! isDayDate(d2, tz)) || (d1 == null && d2 == null));
          }
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
            TIMEINTERVAL_CONTRACT.assertDeterminate(subject, d1, d2, result);
          }
          catch (IllegalTimeIntervalException exc) {
            assertTrue(! le(subject.determinateBegin(d1), subject.determinateEnd(d2)) ||
                       (d1 != null && ! isDayDate(d1, subject.getTimeZone())) ||
                       (d2 != null && ! isDayDate(d2, subject.getTimeZone())));
          }
          assertInvariants(subject);
        }
      }
    }
  }

}

