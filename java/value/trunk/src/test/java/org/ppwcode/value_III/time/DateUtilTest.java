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
import static org.junit.Assert.*;
import static org.ppwcode.value_III.time.DateUtil.isDayDate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class DateUtilTest {

  @Test
  public void testSameDay1a() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18);
    assertTrue(DateUtil.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1b() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 19);
    assertFalse(DateUtil.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1c() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 10, 18);
    assertFalse(DateUtil.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1d() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2009, 9, 18);
    assertFalse(DateUtil.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay1e() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertTrue(DateUtil.sameDay(c1.getTime(), c2.getTime()));
  }

  @Test
  public void testSameDay2() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertTrue(DateUtil.sameDay(c.getTime(), c.getTime()));
  }

  public void testDayDate(Date date) {
    Date result = DateUtil.dayDate(date);
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

