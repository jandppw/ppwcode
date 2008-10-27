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

package org.ppwcode.value_III.time.interval;


import static org.ppwcode.util.test.contract.Contract.contractFor;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.test.contract.NoSuchContractException;
import org.ppwcode.value_III.time.Duration;


public class AbstractTimeIntervalTest {

  private static final class StubAbstractTimeInterval extends AbstractTimeInterval {

    private Date $begin;
    private Date $end;
    private Duration $duration;
    public StubAbstractTimeInterval(Date begin, Date end, Duration duration) {
      $begin = begin;
      $end = end;
      $duration = duration;
    }

    public Date getBegin() {return $begin;}

    public Date getEnd() {return $end;}

    public Duration getDuration() {return $duration;}

    public TimeInterval determinate(Date stubBegin, Date stubEnd) {
      return null;
    }
  }

  public static _Contract_TimeInterval CONTRACT;
  static {
    try {
      CONTRACT = (_Contract_TimeInterval)contractFor(TimeInterval.class);
    }
    catch (NoSuchContractException exc) {
      unexpectedException(exc);
    }
  }

  private List<AbstractTimeInterval> $subjects;

  @Before
  public void setUp() throws Exception {
    $subjects = new ArrayList<AbstractTimeInterval>();
    AbstractTimeInterval subject = new StubAbstractTimeInterval(null, null, null);
    $subjects.add(subject);
    final Date now = new Date();
    subject = new StubAbstractTimeInterval(now, null, null);
    $subjects.add(subject);
    subject = new StubAbstractTimeInterval(null, now, null);
    $subjects.add(subject);
    subject = new StubAbstractTimeInterval(now, null, new Duration(0, null));
    $subjects.add(subject);
    subject = new StubAbstractTimeInterval(now, now, null);
    $subjects.add(subject);
    subject = new StubAbstractTimeInterval(now, now, new Duration(0, null));
    $subjects.add(subject);
    final GregorianCalendar gcB = new GregorianCalendar(2000, 0, 1);
    final GregorianCalendar gcE = new GregorianCalendar(2122, 11, 31);
    final Date b = gcB.getTime();
    final Date e = gcE.getTime();
    subject = new StubAbstractTimeInterval(b, e, null);
    $subjects.add(subject);
    subject = new StubAbstractTimeInterval(b, e, new Duration((gcE.getTimeInMillis() - gcB.getTimeInMillis()), Duration.Unit.MILLISECOND));
    $subjects.add(subject);
  }

  @After
  public void tearDown() throws Exception {
    $subjects = null;
  }

  public void testEqualsObject(AbstractTimeInterval subject, Object other) {
    boolean result = subject.equals(other);
    System.out.println(subject + ".equals(" + other + ") == " + result);
    CONTRACT.assertEqualsObject(subject, other, result);
    CONTRACT.assertInvariants(subject);
    if (other instanceof TimeInterval) {
      CONTRACT.assertInvariants((TimeInterval)other);
    }
  }

  @Test
  public void testEqualsObject() {
    for (AbstractTimeInterval subject : $subjects) {
      testEqualsObject(subject, null);
      testEqualsObject(subject, new Object());
      for (AbstractTimeInterval other : $subjects) {
        testEqualsObject(subject, other);
      }
    }
  }

  @Test
  public void testHashCode() {
    for (AbstractTimeInterval subject : $subjects) {
      int result = subject.hashCode();
      CONTRACT.assertHashCode(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testToString() {
    for (AbstractTimeInterval subject : $subjects) {
      String result = subject.toString();
//      System.out.println(result);
      CONTRACT.assertToString(subject, result);
      CONTRACT.assertInvariants(subject);
    }
  }

  @Test
  public void testDeterminateBeginDate() {
    for (AbstractTimeInterval subject : $subjects) {
      testDeterminateBegin(subject, null);
      testDeterminateBegin(subject, new Date());
    }
  }

  private void testDeterminateBegin(AbstractTimeInterval subject, Date d) {
    Date result = subject.determinateBegin(d);
    CONTRACT.assertDeterminateBegin(subject, d, result);
    CONTRACT.assertInvariants(subject);
  }

  @Test
  public void testDeterminateEndDate() {
    for (AbstractTimeInterval subject : $subjects) {
      testDeterminateEnd(subject, null);
      testDeterminateEnd(subject, new Date());
    }
  }

  private void testDeterminateEnd(AbstractTimeInterval subject, Date d) {
    Date result = subject.determinateEnd(d);
    CONTRACT.assertDeterminateEnd(subject, d, result);
    CONTRACT.assertInvariants(subject);
  }

}

