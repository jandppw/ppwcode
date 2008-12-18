/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.DateHelpers.isDayDate;
import static org.ppwcode.value_III.time.DateHelpers.le;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;



public class DateHelpersTest {


  public void testLe(Date d1, Date d2) {
    boolean result = le(d1, d2);
    boolean expected = d1 == null || (d2 != null && d1.before(d2)) || d1.equals(d2);
    assertEquals(expected, result);
  }

  @Test
  public void testLe1a() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18);
    testLe(c1.getTime(), c2.getTime());
  }

  @Test
  public void testLe1b() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 19);
    testLe(c1.getTime(), c2.getTime());
  }

  @Test
  public void testLe1c() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 10, 18);
    testLe(c1.getTime(), c2.getTime());
  }

  @Test
  public void testLe1d() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2009, 9, 18);
    testLe(c1.getTime(), c2.getTime());
  }

  @Test
  public void testLe1e() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    testLe(c1.getTime(), c2.getTime());
  }

  @Test
  public void testLe2() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    testLe(c.getTime(), c.getTime());
  }

  @Test
  public void testLe3() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    testLe(c.getTime(), null);
  }

  @Test
  public void testLe4() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    testLe(null, c.getTime());
  }

  @Test
  public void testLe5() {
    testLe(null, null);
  }

  @Test
  public void testSameDay1a() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18);
    assertTrue(DateHelpers.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1b() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 19);
    assertFalse(DateHelpers.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1c() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 10, 18);
    assertFalse(DateHelpers.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1d() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2009, 9, 18);
    assertFalse(DateHelpers.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1e() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertTrue(DateHelpers.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay2() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertTrue(DateHelpers.sameDay(c.getTime(), c.getTime()));
  }

  public void testDayDate(Date date) {
    Date result = DateHelpers.dayDate(date);
    if (date == null) {
      assertNull(result);
    }
    else {
      assertNotNull(result);
      assertNotSame(date, result);
      Calendar cd = new GregorianCalendar();
      cd.setTime(date);
      Calendar cr = new GregorianCalendar();
      cr.setTime(result);
      assertEquals(cd.get(YEAR), cr.get(YEAR));
      assertEquals(cd.get(MONTH), cr.get(MONTH));
      assertEquals(cd.get(DAY_OF_MONTH), cr.get(DAY_OF_MONTH));
      assertEquals(0, cr.get(HOUR_OF_DAY));
      assertEquals(0, cr.get(MINUTE));
      assertEquals(0, cr.get(SECOND));
      assertEquals(0, cr.get(MILLISECOND));
    }
  }

  @Test
  public void testDayDate() {
    testDayDate(new Date());
  }

  @Test
  public void testIsDayDate1() {
    Calendar c = new GregorianCalendar(2008, 9, 18);
    assertTrue(isDayDate(c.getTime()));
  }

  @Test
  public void testIsDayDate2() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertFalse(isDayDate(c.getTime()));
  }

}

