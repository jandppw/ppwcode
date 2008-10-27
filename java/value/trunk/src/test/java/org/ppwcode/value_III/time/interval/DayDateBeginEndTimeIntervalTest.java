/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.DateHelpers.dayDate;
import static org.ppwcode.value_III.time.DateHelpers.isDayDate;
import static org.ppwcode.value_III.time.DateHelpers.le;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DayDateBeginEndTimeIntervalTest extends AbstractBeginEndTimeIntervalTest {


  @SuppressWarnings("unchecked")
  @Override
  public List<? extends DayDateBeginEndTimeInterval> subjects() {
    return (List<? extends DayDateBeginEndTimeInterval>)$subjects;
  }

  @Override
  @Before
  public void setUp() throws IllegalIntervalException {
    List<DayDateBeginEndTimeInterval> s = new ArrayList<DayDateBeginEndTimeInterval>();
    DayDateBeginEndTimeInterval subject = new DayDateBeginEndTimeInterval(null, null);
    s.add(subject);
    final Date now = dayDate(new Date());
    subject = new DayDateBeginEndTimeInterval(now, null);
    s.add(subject);
    subject = new DayDateBeginEndTimeInterval(null, now);
    s.add(subject);
    subject = new DayDateBeginEndTimeInterval(now, now);
    s.add(subject);
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1);
    final GregorianCalendar gcE = new GregorianCalendar(2122, 11, 31);
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    subject = new DayDateBeginEndTimeInterval(b, e);
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

  @Test
  public void testDayDateBeginEndTimeInterval() {
    for (Date d1 : $dates) {
      for (Date d2 : $dates) {
        try {
          DayDateBeginEndTimeInterval subject = new DayDateBeginEndTimeInterval(d1, d2);
          assertEquals(d1, subject.getBegin());
          assertEquals(d2, subject.getEnd());
        }
        catch (IllegalIntervalException exc) {
          assertTrue(! le(d1, d2) || (d1 != null && ! isDayDate(d1)) || (d2 != null && ! isDayDate(d2)));
        }
      }
    }
  }

  @Test
  public void testDeterminate() {
    for (DayDateBeginEndTimeInterval subject : subjects()) {
      for (Date d1 : $dates) {
        for (Date d2 : $dates) {
          try {
            DayDateBeginEndTimeInterval result = subject.determinate(d1, d2);
            CONTRACT.assertDeterminate(subject, d1, d2, result, null);
          }
          catch (IllegalIntervalException exc) {
            CONTRACT.assertDeterminate(subject, d1, d2, null, exc);
          }
          CONTRACT.assertInvariants(subject);
        }
      }
    }
  }

}

