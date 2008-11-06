/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.util.test.contract.Contract.contractFor;
import static org.ppwcode.value_III.time.TimeHelpers.UTC;
import static org.ppwcode.value_III.time.TimeHelpers.le;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;


public class AbstractBeginEndTimeTimeZoneIntervalTest extends AbstractBeginEndTimeIntervalTest {

  private static final class StubAbstractBeginEndTimeZoneTimeInterval extends AbstractBeginEndTimeZoneTimeInterval {

    public StubAbstractBeginEndTimeZoneTimeInterval(Date begin, Date end, TimeZone tz) throws IllegalTimeIntervalException {
      super(begin, end, tz);
    }

    public TimeInterval determinate(Date stubBegin, Date stubEnd) {
      return null;
    }

  }

  public static _Contract_TimeZoneTimeInterval TIMEZONE_TIMEINTERVAL_CONTRACT;
  static {
    try {
      TIMEZONE_TIMEINTERVAL_CONTRACT = (_Contract_TimeZoneTimeInterval)contractFor(TimeZoneTimeInterval.class);
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<? extends AbstractBeginEndTimeZoneTimeInterval> subjects() {
    return (List<? extends AbstractBeginEndTimeZoneTimeInterval>)$subjects;
  }

  public final static TimeZone MOSCOW = TimeZone.getTimeZone("Europe/Moscow");

  public final static TimeZone LA = TimeZone.getTimeZone("America/Los_Angeles");

  public final static TimeZone[] TIMEZONES = new TimeZone[] {TimeZone.getDefault(), UTC, LA, MOSCOW,
                                         TimeZone.getTimeZone("GMT+11"), TimeZone.getTimeZone("GMT+12"),
                                         TimeZone.getTimeZone("GMT-11"), TimeZone.getTimeZone("GMT-12")};
  @Override
  @Before
  public void setUp() throws IllegalTimeIntervalException {
    List<AbstractBeginEndTimeZoneTimeInterval> s = new ArrayList<AbstractBeginEndTimeZoneTimeInterval>();
    final Date now = new Date();
    AbstractBeginEndTimeZoneTimeInterval subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, null, TimeZone.getDefault());
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, null, UTC);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, null, LA);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, null, MOSCOW);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(null, now, TimeZone.getDefault());
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(null, now, UTC);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(null, now, LA);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(null, now, MOSCOW);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, now, TimeZone.getDefault());
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, now, UTC);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, now, LA);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(now, now, MOSCOW);
    s.add(subject);
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1);
    final GregorianCalendar gcE = new GregorianCalendar(2122, 11, 31);
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(b, e, TimeZone.getDefault());
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(b, e, UTC);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(b, e, LA);
    s.add(subject);
    subject = new StubAbstractBeginEndTimeZoneTimeInterval(b, e, MOSCOW);
    s.add(subject);
    $subjects = s;
  }

  protected void assertInvariants(AbstractBeginEndTimeZoneTimeInterval subject) {
    super.assertInvariants(subject);
  }

  @Test
  public void testAbstractBeginEndTimeZoneTimeInterval() {
    GregorianCalendar past = new GregorianCalendar(1995, 3, 24);
    Date now = new Date();
    GregorianCalendar future = new GregorianCalendar(2223, 4, 13);
    Date[] dates = new Date[] {null, past.getTime(), now, future.getTime()};

    for (Date d1 : dates) {
      for (Date d2 : dates) {
        for (TimeZone tz : TIMEZONES) {
          try {
            AbstractBeginEndTimeZoneTimeInterval subject = new StubAbstractBeginEndTimeZoneTimeInterval(d1, d2, tz);
            assertEquals(d1, subject.getBegin());
            assertEquals(d2, subject.getEnd());
            assertEquals(tz, subject.getTimeZone());
            assertInvariants(subject);
          }
          catch (IllegalTimeIntervalException exc) {
            assertTrue(! le(d1, d2) || (d1 == null && d2 == null));
          }
        }
      }
    }
  }

}

