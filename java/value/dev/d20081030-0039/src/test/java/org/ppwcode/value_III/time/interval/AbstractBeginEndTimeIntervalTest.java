/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.DateHelpers.le;
import static org.ppwcode.value_III.time.Duration.delta;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ppwcode.value_III.time.Duration;


public class AbstractBeginEndTimeIntervalTest extends AbstractTimeIntervalTest {

  private static final class StubAbstractBeginEndTimeInterval extends AbstractBeginEndTimeInterval {

    public StubAbstractBeginEndTimeInterval(Date begin, Date end) throws IllegalIntervalException {
      super(begin, end);
    }

    public TimeInterval determinate(Date stubBegin, Date stubEnd) {
      return null;
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<? extends AbstractBeginEndTimeInterval> subjects() {
    return (List<? extends AbstractBeginEndTimeInterval>)$subjects;
  }

  @Override
  @Before
  public void setUp() throws IllegalIntervalException {
    List<AbstractBeginEndTimeInterval> s = new ArrayList<AbstractBeginEndTimeInterval>();
    final Date now = new Date();
    AbstractBeginEndTimeInterval subject = new StubAbstractBeginEndTimeInterval(now, null);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeInterval(null, now);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeInterval(now, now);
    s.add(subject);
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1);
    final GregorianCalendar gcE = new GregorianCalendar(2122, 11, 31);
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    subject = new StubAbstractBeginEndTimeInterval(b, e);
    s.add(subject);
    $subjects = s;
  }

  protected void assertInvariants(AbstractBeginEndTimeInterval subject) {
    super.assertInvariants(subject);
  }

  @Test
  public void testGetDuration() {
    for (AbstractBeginEndTimeInterval subject : subjects()) {
      Duration result = subject.getDuration();
      Duration expected = subject.getBegin() == null || subject.getEnd() == null ? null :
        delta(subject.getBegin(), subject.getEnd());
      assertEquals(expected, result);
      assertInvariants(subject);
    }
  }

  @Test
  public void testAbstractBeginEndTimeInterval() {
    GregorianCalendar past = new GregorianCalendar(1995, 3, 24);
    Date now = new Date();
    GregorianCalendar future = new GregorianCalendar(2223, 4, 13);
    Date[] dates = new Date[] {null, past.getTime(), now, future.getTime()};
    for (Date d1 : dates) {
      for (Date d2 : dates) {
        try {
          AbstractBeginEndTimeInterval subject = new StubAbstractBeginEndTimeInterval(d1, d2);
          assertEquals(d1, subject.getBegin());
          assertEquals(d2, subject.getEnd());
          assertInvariants(subject);
        }
        catch (IllegalIntervalException exc) {
          assertTrue(! le(d1, d2) || (d1 == null && d2 == null));
        }
      }
    }
  }

}

