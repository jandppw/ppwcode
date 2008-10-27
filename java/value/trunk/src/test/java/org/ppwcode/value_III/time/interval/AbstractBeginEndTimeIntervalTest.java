/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;


import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;


public class AbstractBeginEndTimeIntervalTest extends AbstractTimeIntervalTest {

  private static final class StubAbstractBeginEndTimeInterval extends AbstractBeginEndTimeInterval {

    public StubAbstractBeginEndTimeInterval(Date begin, Date end) throws IllegalIntervalException {
      super(begin, end);
    }

    public TimeInterval determinate(Date stubBegin, Date stubEnd) {
      return null;
    }

  }

  @Override
  @Before
  public void setUp() throws IllegalIntervalException {
    List<AbstractBeginEndTimeInterval> s = new ArrayList<AbstractBeginEndTimeInterval>();
    AbstractBeginEndTimeInterval subject = new StubAbstractBeginEndTimeInterval(null, null);
    s.add(subject);
    final Date now = new Date();
    subject = new StubAbstractBeginEndTimeInterval(now, null);
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


}

