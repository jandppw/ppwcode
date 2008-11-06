/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.TimeHelpers.le;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BeginEndTimeIntervalTest extends AbstractBeginEndTimeIntervalTest {


  @SuppressWarnings("unchecked")
  @Override
  public List<? extends BeginEndTimeInterval> subjects() {
    return (List<? extends BeginEndTimeInterval>)$subjects;
  }

  @Override
  @Before
  public void setUp() throws IllegalTimeIntervalException {
    List<BeginEndTimeInterval> s = new ArrayList<BeginEndTimeInterval>();
    final Date now = new Date();
    BeginEndTimeInterval subject = new BeginEndTimeInterval(now, null);
    s.add(subject);
    subject = new BeginEndTimeInterval(null, now);
    s.add(subject);
    subject = new BeginEndTimeInterval(now, now);
    s.add(subject);
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1);
    final GregorianCalendar gcE = new GregorianCalendar(2122, 11, 31);
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    subject = new BeginEndTimeInterval(b, e);
    s.add(subject);
    $subjects = s;
  }

  private Date[] $dates;

  @Before
  public void initDates() {
    GregorianCalendar past = new GregorianCalendar(1995, 3, 24);
    Date now = new Date();
    GregorianCalendar future = new GregorianCalendar(2223, 4, 13);
    $dates = new Date[] {null, past.getTime(), now, future.getTime()};
  }

  @After
  public void deInitDates() {
    $dates = null;
  }

  protected void assertInvariants(BeginEndTimeInterval subject) {
    super.assertInvariants(subject);
  }

  @Test
  public void testBeginEndTimeInterval() {
    for (Date d1 : $dates) {
      for (Date d2 : $dates) {
        try {
          AbstractBeginEndTimeInterval subject = new BeginEndTimeInterval(d1, d2);
          assertEquals(d1, subject.getBegin());
          assertEquals(d2, subject.getEnd());
          assertInvariants(subject);
        }
        catch (IllegalTimeIntervalException exc) {
          assertTrue(! le(d1, d2) || (d1 == null && d2 == null));
        }
      }
    }
  }

  @Test
  public void testDeterminate() {
    for (BeginEndTimeInterval subject : subjects()) {
      for (Date d1 : $dates) {
        for (Date d2 : $dates) {
          BeginEndTimeInterval result;
          try {
            result = subject.determinate(d1, d2);
            TIMEINTERVAL_CONTRACT.assertDeterminate(subject, d1, d2, result);
          }
          catch (IllegalTimeIntervalException exc) {
            assertTrue(! le(subject.determinateBegin(d1), subject.determinateEnd(d2)));
          }
          assertInvariants(subject);
        }
      }
    }
  }

}

