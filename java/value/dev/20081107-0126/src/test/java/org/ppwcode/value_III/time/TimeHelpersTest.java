/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time;


import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.JANUARY;
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
import static org.ppwcode.value_III.time.TimeHelpers.EPOCH;
import static org.ppwcode.value_III.time.TimeHelpers.UTC;
import static org.ppwcode.value_III.time.TimeHelpers.compose;
import static org.ppwcode.value_III.time.TimeHelpers.dayDate;
import static org.ppwcode.value_III.time.TimeHelpers.gregorianCalendar;
import static org.ppwcode.value_III.time.TimeHelpers.isDayDate;
import static org.ppwcode.value_III.time.TimeHelpers.le;
import static org.ppwcode.value_III.time.TimeHelpers.move;
import static org.ppwcode.value_III.time.TimeHelpers.sameDay;
import static org.ppwcode.value_III.time.TimeHelpers.sqlDayDate;
import static org.ppwcode.value_III.time.TimeHelpers.sqlTimeOfDay;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;


public class TimeHelpersTest {

  @Test
  public void testUTC() {
    assertEquals(0, UTC.getDSTSavings());
    assertEquals("UTC", UTC.getID());
    assertEquals(0, UTC.getRawOffset());
  }

  @Test
  public void testEPOCH() {
    GregorianCalendar gc = new GregorianCalendar(UTC);
    gc.setTime(EPOCH);
    assertEquals(1970, gc.get(YEAR));
    assertEquals(JANUARY, gc.get(MONTH));
    assertEquals(1, gc.get(DAY_OF_MONTH));
    assertEquals(1, gc.get(DAY_OF_YEAR));
    assertEquals(0, gc.get(HOUR_OF_DAY));
    assertEquals(0, gc.get(HOUR));
    assertEquals(0, gc.get(MINUTE));
    assertEquals(0, gc.get(SECOND));
    assertEquals(0, gc.get(MILLISECOND));
  }

  @Test
  public void testGregorianCalendar1() {
    GregorianCalendar result = TimeHelpers.gregorianCalendar(null, UTC);
    assertNull(result);
  }

  @Test
  public void testGregorianCalendar2() {
    Date now = new Date();
    GregorianCalendar result = TimeHelpers.gregorianCalendar(new Date(), UTC);
    assertNotNull(result);
    assertEquals(UTC, result.getTimeZone());
    assertEquals(now, result.getTime());
  }

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

  public final static TimeZone DEFAULT_TZ = TimeZone.getDefault();

  @Test
  public void testSameDay1a() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18);
    assertTrue(sameDay(c1.getTime(), c2.getTime(), DEFAULT_TZ));
  }

  @Test
  public void testSameDay1b() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 19);
    assertFalse(sameDay(c1.getTime(), c2.getTime(), DEFAULT_TZ));
  }

  @Test
  public void testSameDay1c() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 10, 18);
    assertFalse(sameDay(c1.getTime(), c2.getTime(), DEFAULT_TZ));
  }

  @Test
  public void testSameDay1d() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2009, 9, 18);
    assertFalse(sameDay(c1.getTime(), c2.getTime(), DEFAULT_TZ));
  }

  @Test
  public void testSameDay1e() {
    Calendar c1 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    Calendar c2 = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertTrue(sameDay(c1.getTime(), c2.getTime(), DEFAULT_TZ));
  }

  @Test
  public void testSameDay2() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertTrue(sameDay(c.getTime(), c.getTime(), DEFAULT_TZ));
  }

  public void testDayDate(Date date, TimeZone tz) {
    Date result = dayDate(date, tz);
    if (date == null) {
      assertNull(result);
    }
    else {
      assertNotNull(result);
      assertNotSame(date, result);
      Calendar cd = new GregorianCalendar(tz);
      cd.setTime(date);
      Calendar cr = new GregorianCalendar(tz);
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
  public void testDayDate1() {
    testDayDate(new Date(), DEFAULT_TZ);
  }

  @Test
  public void testDayDate2() {
    testDayDate(new Date(), UTC);
  }

  @Test
  public void testDayDate3() {
    testDayDate(null, DEFAULT_TZ);
  }

  @Test
  public void testDayDate4() {
    testDayDate(dayDate(new Date(), DEFAULT_TZ), DEFAULT_TZ);
  }

  @Test
  public void testDayDate5() {
    testDayDate(dayDate(new Date(), DEFAULT_TZ), UTC);
  }

  @Test
  public void testIsDayDate1() {
    Calendar c = new GregorianCalendar(2008, 9, 18);
    assertTrue(isDayDate(c.getTime(), DEFAULT_TZ));
  }

  @Test
  public void testIsDayDate1b() {
    Calendar c = new GregorianCalendar(2008, 9, 18);
    assertFalse(isDayDate(c.getTime(), UTC)); // you're not in Greenwich, are you?
  }

  @Test
  public void testIsDayDate2() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertFalse(isDayDate(c.getTime(), DEFAULT_TZ));
  }

  @Test
  public void testIsDayDate2b() {
    Calendar c = new GregorianCalendar(2008, 9, 18, 23, 55, 35);
    assertFalse(isDayDate(c.getTime(), UTC));
  }

  @Test
  public void testIsDayDate3() {
    assertFalse(isDayDate(null, UTC));
  }

  @Test
  public void demoSqlDayDate() {
    java.sql.Date today = TimeHelpers.sqlDayDate(new Date(), UTC);
    TimeZone la = TimeZone.getTimeZone("America/Los_Angeles");
    TimeZone moscow = TimeZone.getTimeZone("Europe/Moscow");
//    DateFormat df = DateFormat.getDateInstance(FULL);
//    df.setTimeZone(la);
//    System.out.println(df.format(today));
//    df.setTimeZone(moscow);
//    System.out.println(df.format(today));
    GregorianCalendar gc1 = new GregorianCalendar(la);
    gc1.setTime(today);
    GregorianCalendar gc2 = new GregorianCalendar(moscow);
    gc2.setTime(today);
    assertFalse(gc1.get(DAY_OF_YEAR) == gc2.get(DAY_OF_YEAR));
  }

  @Test
  public void testSqlDqyDate1() {
    testSqlDayDate(new Date(), DEFAULT_TZ);
  }

  @Test
  public void testSqlDqyDate2() {
    testSqlDayDate(new Date(), UTC);
  }

  @Test
  public void testSqlDqyDate3() {
    testSqlDayDate(null, UTC);
  }

  private void testSqlDayDate(Date d, TimeZone tz) {
    java.sql.Date result = sqlDayDate(d, tz);
    assertEquals(dayDate(d, tz), result);
  }

  private void testSqlTimeOfDay(Date d, TimeZone tz) {
    Time result = sqlTimeOfDay(d, tz);
    if (d == null) {
      assertNull(result);
    }
    else {
      assertNotNull(result);
      GregorianCalendar gr = gregorianCalendar(result, UTC);
      GregorianCalendar gd = gregorianCalendar(d, tz);
      assertEquals(1970, gr.get(YEAR));
      assertEquals(JANUARY, gr.get(MONTH));
      assertEquals(1, gr.get(DAY_OF_MONTH));
      assertEquals(gd.get(HOUR), gr.get(HOUR));
      assertEquals(gd.get(MINUTE), gr.get(MINUTE));
      assertEquals(gd.get(SECOND), gr.get(SECOND));
      assertEquals(gd.get(MILLISECOND), gr.get(MILLISECOND));
    }
  }

  @Test
  public void testSqlTimeOfDay1() {
    testSqlTimeOfDay(new Date(), DEFAULT_TZ);
  }

  @Test
  public void testSqlTimeOfDay1b() {
    testSqlTimeOfDay(new Date(), UTC);
  }

  @Test
  public void testSqlTimeOfDay2() {
    testSqlTimeOfDay(EPOCH, DEFAULT_TZ);
  }

  @Test
  public void testSqlTimeOfDay2b() {
    testSqlTimeOfDay(EPOCH, UTC);
  }

  @Test
  public void testSqlTimeOfDay3() {
    testSqlTimeOfDay(null, DEFAULT_TZ);
  }

  private void testCompose(java.sql.Date day, Time time, TimeZone tz) {
    Date result = compose(day, time, tz);
    if (day == null || time == null) {
      assertNull(result);
    }
    else {
      assertNotNull(result);
      GregorianCalendar gd = gregorianCalendar(day, tz);
      GregorianCalendar gt = gregorianCalendar(time, UTC);
      GregorianCalendar gr = gregorianCalendar(result, tz);
      assertEquals(gd.get(YEAR), gr.get(YEAR));
      assertEquals(gd.get(MONTH), gr.get(MONTH));
      assertEquals(gd.get(DAY_OF_MONTH), gr.get(DAY_OF_MONTH));
      assertEquals(gt.get(HOUR), gr.get(HOUR));
      assertEquals(gt.get(MINUTE), gr.get(MINUTE));
      assertEquals(gt.get(SECOND), gr.get(SECOND));
      assertEquals(gt.get(MILLISECOND), gr.get(MILLISECOND));
    }
  }

  @Test
  public void testCompose1() {
    testCompose(sqlDayDate(new Date(), DEFAULT_TZ), sqlTimeOfDay(new Date(), DEFAULT_TZ), DEFAULT_TZ);
  }

  @Test
  public void testCompose1a() {
    testCompose(null, sqlTimeOfDay(new Date(), DEFAULT_TZ), DEFAULT_TZ);
  }

  @Test
  public void testCompose1b() {
    testCompose(sqlDayDate(new Date(), DEFAULT_TZ), null, DEFAULT_TZ);
  }

  @Test
  public void testCompose2() {
    testCompose(sqlDayDate(new Date(), UTC), sqlTimeOfDay(new Date(), DEFAULT_TZ), UTC);
  }

  @Test
  public void testCompose3() {
    testCompose(sqlDayDate(new Date(), DEFAULT_TZ), sqlTimeOfDay(new Date(), UTC), DEFAULT_TZ);
  }

  @Test(expected = AssertionError.class)
  public void testCompose4() {
    testCompose(sqlDayDate(new Date(), UTC), sqlTimeOfDay(new Date(), UTC), DEFAULT_TZ);
  }

  @Test
  public void testCompose5() {
    testCompose(sqlDayDate(EPOCH, DEFAULT_TZ), sqlTimeOfDay(EPOCH, DEFAULT_TZ), DEFAULT_TZ);
  }

  @Test
  public void testCompose6() {
    testCompose(sqlDayDate(EPOCH, UTC), sqlTimeOfDay(EPOCH, UTC), UTC);
  }

  @Test
  public void testCompose7() {
    testCompose(sqlDayDate(EPOCH, UTC), sqlTimeOfDay(EPOCH, DEFAULT_TZ), UTC);
  }

  public void testMove(Date d, TimeZone from, TimeZone to) {
    Date result = move(d, from, to);
    if (d == null) {
      assertNull(result);
    }
    else {
      assertNotNull(result);
      GregorianCalendar fromGc = gregorianCalendar(d, from);
      fromGc.setTime(d);
      GregorianCalendar toGc = gregorianCalendar(d, to);
      toGc.setTime(result);
      assertEquals(fromGc.get(YEAR), toGc.get(YEAR));
      assertEquals(fromGc.get(MONTH), toGc.get(MONTH));
      assertEquals(fromGc.get(DAY_OF_MONTH), toGc.get(DAY_OF_MONTH));
      assertEquals(fromGc.get(HOUR_OF_DAY), toGc.get(HOUR_OF_DAY));
      assertEquals(fromGc.get(MINUTE), toGc.get(MINUTE));
      assertEquals(fromGc.get(SECOND), toGc.get(SECOND));
      assertEquals(fromGc.get(MILLISECOND), toGc.get(MILLISECOND));
    }
  }

  public final static TimeZone[] TIMEZONES =
      new TimeZone[] {UTC, DEFAULT_TZ, TimeZone.getTimeZone("America/Los_Angeles"), TimeZone.getTimeZone("Europe/Moscow")};

  @Test
  public void testMove1() {
    for (TimeZone tzF : TIMEZONES) {
      for (TimeZone tzT : TIMEZONES) {
        testMove(null, tzF, tzT);
        testMove(new Date(), tzF, tzT);
        testMove(dayDate(new Date(), tzF), tzF, tzT);
        testMove(dayDate(new Date(), tzT), tzF, tzT);
      }
    }
  }

}

