/*<license>
Copyright 2008 - $Date$ by PeopleWare n.v.

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

package org.ppwcode.research.jpa.openjpa.valuehandlers;

import static org.junit.Assert.assertEquals;
import static org.ppwcode.value_III.time.Duration.Unit.HOUR;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.ppwcode.value_III.time.Duration;
import org.ppwcode.value_III.time.interval.BeginEndTimeInterval;
import org.ppwcode.value_III.time.interval.IllegalTimeIntervalException;
import org.ppwcode.value_III.time.interval.TimeInterval;

public class TimeZoneTest {

  @Test
  public void testTimeZoneId() {
    System.out.println("ALL AVAILABLE");
    System.out.println("-------------");
    String[] atzs = TimeZone.getAvailableIDs();
    for (String string : atzs) {
      System.out.println(string);
    }
    for (int offset = -12; offset <= +12; offset++) {
      System.out.println();
      System.out.println("AVAILABLE FOR RAW GMT" + (offset < 0 ? offset : "+" + offset));
      System.out.println("--------------------------");
      atzs = TimeZone.getAvailableIDs((int)(offset * HOUR.asMilliseconds()));
      for (String string : atzs) {
        System.out.println(string);
      }
    }
    System.out.println();
    System.out.println();
    System.out.println();
  }

  @Test
  public void testOurTimeZone() {
    TimeZone tz = TimeZone.getTimeZone("Europe/Brussels");
    System.out.println(tz.getDisplayName(false, TimeZone.SHORT, new Locale("nl", "BE")));
    System.out.println(tz.getDisplayName(false, TimeZone.LONG, new Locale("nl", "BE")));
    System.out.println(tz.getDisplayName(true, TimeZone.SHORT, new Locale("nl", "BE")));
    System.out.println(tz.getDisplayName(true, TimeZone.LONG, new Locale("nl", "BE")));
    System.out.println("Are we using DST? : " + tz.useDaylightTime());
    System.out.println("Timezone id: " + tz.getID());
  }


  @Test
  public void testTimeZoneOffset() throws IllegalTimeIntervalException {
    System.out.println();
    System.out.println();
    System.out.println();
    TimeZone tz = TimeZone.getTimeZone("Europe/Brussels");
    GregorianCalendar gcSummertime = new GregorianCalendar(2008, 9, 25, 21, 17, 0);
    GregorianCalendar gcWintertime = new GregorianCalendar(2008, 9, 26, 21, 17, 0);
    System.out.println("Offset in summertime: " + tz.getOffset(gcSummertime.getTimeInMillis()) / HOUR.asMilliseconds() + "h");
    System.out.println("Offset in wintertime: " + tz.getOffset(gcWintertime.getTimeInMillis()) / HOUR.asMilliseconds() + "h");
    System.out.println("Raw offset: " + tz.getRawOffset() / HOUR.asMilliseconds() + "h");
    System.out.println("Current DST savings: " + tz.getDSTSavings() / HOUR.asMilliseconds() + "h");
    System.out.println("In summer time at " + gcSummertime.getTime() + "? : " + tz.inDaylightTime(gcSummertime.getTime()));
    System.out.println("In summer time at " + gcWintertime.getTime() + "? : " + tz.inDaylightTime(gcWintertime.getTime()));

    TimeInterval ti = new BeginEndTimeInterval(gcSummertime.getTime(), gcWintertime.getTime());
    System.out.println(ti);
    Duration d = ti.getDuration();
    System.out.println("hours: " + d.as(HOUR));
  }

  @Test
  public void testDateWithTimeZone1() {
    System.out.println();
    System.out.println();
    System.out.println();
    Date d = new Date();
    GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT-9"));
    gc.setTime(d);
    Date d2 = gc.getTime();
    System.out.println("original date: " + d);
    System.out.println("calendar: " + gc);
    System.out.println("calendar: " + gc.getTimeZone());
    System.out.println("transfered date: " + d2);
    assertEquals(d, d2);
  }

  @Test
  public void testDateWithTimeZone2() {
    System.out.println();
    System.out.println();
    System.out.println();
    GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT-9"));
    System.out.println("calendar: " + gc);
    Date d = gc.getTime();
    System.out.println("date from calendar: " + d);
    GregorianCalendar gc2 = new GregorianCalendar(TimeZone.getTimeZone("GMT+5"));
    gc2.setTime(d);
    System.out.println("calendar 2: " + gc2);
    System.out.println("date of calendar 2: " + gc2.getTime());
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
    df.setCalendar(new GregorianCalendar());
    df.setTimeZone(TimeZone.getTimeZone("GMT-9"));
    System.out.println(df.format(gc.getTime()));
    df.setTimeZone(TimeZone.getTimeZone("GMT-5"));
    System.out.println(df.format(gc2.getTime()));
  }

  @Test
  public void testMillis() {
    System.out.println();
    System.out.println();
    System.out.println();
    Date now = new GregorianCalendar(2008, Calendar.NOVEMBER, 3, 16, 55, 33).getTime();
    System.out.println("now = " + now);
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(now);
    gc.setTimeZone(TimeZone.getTimeZone("UTC"));
    long millis = now.getTime();
    System.out.println("now millis= " + millis);

    long years = millis / Duration.Unit.YEAR.asMilliseconds();
    long restYears = millis % Duration.Unit.YEAR.asMilliseconds();
    System.out.println("years = " + years + (" (now years passed since epoch - 1970 = " + (gc.get(Calendar.YEAR) -  1970) + ")"));
    assertEquals(years, gc.get(Calendar.YEAR) - 1970);
    System.out.println("rest years = " + restYears);

    long leapDaysSince1970 = (years + 1) / 4;
    long leapDayMillis = leapDaysSince1970 * Duration.Unit.DAY.asMilliseconds();
    // 1972 1976 1980 1984 1988 1992 1996 2000 2004 (2008 not yet complete, so this leay day is in the days, not the years)
    System.out.println("leap days since 1970 = " + leapDaysSince1970 + " (= " + leapDayMillis + " ms)");
    long correctedRestYears = restYears - leapDayMillis;
    System.out.println("corrected rest years = " + correctedRestYears);

    long days = correctedRestYears / Duration.Unit.DAY.asMilliseconds();
    long restDays = correctedRestYears % Duration.Unit.DAY.asMilliseconds();
    System.out.println("days = " + days + (" (now days of years passed since epoch = " + (gc.get(Calendar.DAY_OF_YEAR) - 1) + ")"));
    System.out.println(31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 2);
    assertEquals(days, gc.get(Calendar.DAY_OF_YEAR)- 1);
    System.out.println("rest days = " + restDays);

    long hours = restDays / Duration.Unit.HOUR.asMilliseconds();
    long restHours = restDays % Duration.Unit.HOUR.asMilliseconds();
    System.out.println("hours = " + hours + (" (now hours passed since epoch = " + gc.get(Calendar.HOUR_OF_DAY) + ")"));
    assertEquals(hours, gc.get(Calendar.HOUR_OF_DAY));
    System.out.println("rest hours = " + restHours);

    long minutes = restHours / Duration.Unit.MINUTE.asMilliseconds();
    long restMinutes = restHours % Duration.Unit.MINUTE.asMilliseconds();
    System.out.println("minutes = " + minutes + (" (now minutes passed since epoch= " + gc.get(Calendar.MINUTE) + ")"));
    assertEquals(minutes, gc.get(Calendar.MINUTE));
    System.out.println("rest minutes = " + restMinutes);

    long seconds = restMinutes / Duration.Unit.SECOND.asMilliseconds();
    long restSeconds = restMinutes % Duration.Unit.SECOND.asMilliseconds();
    System.out.println("seconds = " + seconds + (" (now seconds passed since epoch = " + gc.get(Calendar.SECOND) + ")"));
    assertEquals(seconds, gc.get(Calendar.SECOND));
    System.out.println("rest seconds = " + restSeconds);
  }

  @Test
  public void testSqlDate() {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("This test shows clearly that java.sql.Date _itself_ does _nothing_ to force a date. " +
                       "Javadoc says a \"driver\" will set intraday time to 0, but this class does nothing " +
                       "special.");
    System.out.println("------------------------------------------------------------------------------------");
    TimeZone tz = TimeZone.getTimeZone("America/Chicago");
    GregorianCalendar gcNow = new GregorianCalendar(2008, Calendar.NOVEMBER, 3, 16, 55, 33);
    System.out.println("now = " + gcNow.getTime());
    System.out.println("now = " + gcNow);
    gcNow.setTimeZone(tz);
    System.out.println("now = " + gcNow);
    System.out.println(gcNow.getTime().getTime());
    gcNow.clear(Calendar.HOUR);
    gcNow.clear(Calendar.HOUR_OF_DAY);
    gcNow.clear(Calendar.AM_PM);
    gcNow.clear(Calendar.MINUTE);
    gcNow.clear(Calendar.SECOND);
    gcNow.clear(Calendar.MILLISECOND);
    long millis = gcNow.getTime().getTime();
    System.out.println(millis);
    System.out.println();
    java.sql.Date sqlDate = new java.sql.Date(millis);
    System.out.println(sqlDate);
    System.out.println(sqlDate.getTime());
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(sqlDate);
    System.out.println(gc.get(Calendar.YEAR) + " " + gc.get(Calendar.MONTH) + " " + gc.get(Calendar.DAY_OF_MONTH) + " " +
                       gc.get(Calendar.HOUR_OF_DAY) + " " + gc.get(Calendar.MINUTE) + " " + gc.get(Calendar.SECOND) + " " +
                       gc.get(Calendar.MILLISECOND));
    gc.setTimeZone(tz);
    System.out.println(gc.get(Calendar.YEAR) + " " + gc.get(Calendar.MONTH) + " " + gc.get(Calendar.DAY_OF_MONTH) + " " +
                       gc.get(Calendar.HOUR_OF_DAY) + " " + gc.get(Calendar.MINUTE) + " " + gc.get(Calendar.SECOND) + " " +
                       gc.get(Calendar.MILLISECOND));

    System.out.println();
    sqlDate = new java.sql.Date(millis + 7);
    System.out.println(sqlDate);
    System.out.println(sqlDate.getTime());
    gc = new GregorianCalendar();
    gc.setTime(sqlDate);
    System.out.println(gc.get(Calendar.YEAR) + " " + gc.get(Calendar.MONTH) + " " + gc.get(Calendar.DAY_OF_MONTH) + " " +
                       gc.get(Calendar.HOUR_OF_DAY) + " " + gc.get(Calendar.MINUTE) + " " + gc.get(Calendar.SECOND) + " " +
                       gc.get(Calendar.MILLISECOND));
    gc.setTimeZone(tz);
    System.out.println(gc.get(Calendar.YEAR) + " " + gc.get(Calendar.MONTH) + " " + gc.get(Calendar.DAY_OF_MONTH) + " " +
                       gc.get(Calendar.HOUR_OF_DAY) + " " + gc.get(Calendar.MINUTE) + " " + gc.get(Calendar.SECOND) + " " +
                       gc.get(Calendar.MILLISECOND));

    System.out.println();
    sqlDate = new java.sql.Date(millis + 1234567890);
    System.out.println(sqlDate);
    System.out.println(sqlDate.getTime());
    gc = new GregorianCalendar();
    gc.setTime(sqlDate);
    System.out.println(gc.get(Calendar.YEAR) + " " + gc.get(Calendar.MONTH) + " " + gc.get(Calendar.DAY_OF_MONTH) + " " +
                       gc.get(Calendar.HOUR_OF_DAY) + " " + gc.get(Calendar.MINUTE) + " " + gc.get(Calendar.SECOND) + " " +
                       gc.get(Calendar.MILLISECOND));
    gc.setTimeZone(tz);
    System.out.println(gc.get(Calendar.YEAR) + " " + gc.get(Calendar.MONTH) + " " + gc.get(Calendar.DAY_OF_MONTH) + " " +
                       gc.get(Calendar.HOUR_OF_DAY) + " " + gc.get(Calendar.MINUTE) + " " + gc.get(Calendar.SECOND) + " " +
                       gc.get(Calendar.MILLISECOND));
  }

}
