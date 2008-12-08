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

package org.ppwcode.value_III.time;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.value_III.time.Duration.Unit.MILLENNIUM;
import static org.ppwcode.value_III.time.Duration.Unit.CENTURY;
import static org.ppwcode.value_III.time.Duration.Unit.YEAR;
import static org.ppwcode.value_III.time.Duration.Unit.MONTH;
import static org.ppwcode.value_III.time.Duration.Unit.WEEK;
import static org.ppwcode.value_III.time.Duration.Unit.DAY;
import static org.ppwcode.value_III.time.Duration.Unit.HOUR;
import static org.ppwcode.value_III.time.Duration.Unit.MINUTE;
import static org.ppwcode.value_III.time.Duration.Unit.SECOND;
import static org.ppwcode.value_III.time.Duration.Unit.MILLISECOND;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.pre;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.AbstractImmutableValue;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * A duration of time. The smallest unit we can work with are <em>ms</em>.
 *
 * @note This class is "under design". This is a first version, but we lack use cases to
 *       validate better implementations. If you have better code, please submit it.
 *       The current maximal duration is approx. 292 471 200 years. The age of the universe
 *       is approx. 14 000 000 000 years.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class Duration extends AbstractImmutableValue implements Comparable<Duration> {

  public static enum Unit {

    /**
     * The smallest duration unit we support.
     * (There are ways to support more units. Please implement them and submit your patch.)
     */
    MILLISECOND (1),

    @Invars(@Expression("SECOND.asMilliseconds() == 1000"))
    SECOND      (1000),

    @Invars(@Expression("MINUTE.asMilliseconds() == 60 * 1000"))
    MINUTE      (60L * 1000L),

    @Invars(@Expression("HOUR.asMilliseconds() == 60 * 60 * 1000"))
    HOUR        (60L * 60L * 1000L),

    /**
     * The duration of one day. Note that calculations with this duration
     * are off if the actual day you are talking about contains a leap second.
     */
    @Invars(@Expression("DAY.asMilliseconds() == 24 * 60 * 60 * 1000"))
    DAY         (24L * 60L * 60L * 1000L),

    /**
     * The duration of 7 days. Note that calculations with this duration
     * are off if the actual week you are talking about contains a day with a
     * leap second.
     */
    @Invars(@Expression("WEEK.asMilliseconds() == 7 * 24 * 60 * 60 * 1000"))
    WEEK        (7L * 24L * 60L * 60L * 1000L),

    /**
     * The duration of 30 days. Note that calculations with this duration
     * are off if the actual month you are talking about contains a day with a
     * leap second, and clearly do not take into account the difference between
     * months with 28, 29, 30 or 31 days.
     */
    @Invars(@Expression("MONTH.asMilliseconds() == 30 * 24 * 60 * 60 * 1000"))
    MONTH       (30L * 24L * 60L * 60L * 1000L),

    /**
     * The duration of 90 days. Note that calculations with this duration
     * are off if the actual quarter you are talking about contains a day with a
     * leap second , and clearly do not take into account the difference between
     * months with 28, 29, 30 or 31 days.
     */
    @Invars(@Expression("QUARTER.asMilliseconds() == 90 * 24 * 60 * 60 * 1000"))
    QUARTER     (3L * 30L * 24L * 60L * 60L * 1000L),

    /**
     * The duration of 365 days. Note that calculations with this duration
     * are off if the actual year contains a leap day or a day with a leap second.
     */
    @Invars(@Expression("YEAR.asMilliseconds() == 365 * 24 * 60 * 60 * 1000"))
    YEAR        (365L * 24L * 60L * 60L * 1000L),

    /**
     * 10 years. In every decennium, there are 2 leap years. Note that calculations
     * with this duration does not take into account leap seconds, nor non-leap
     * years on century breaks (or exceptions on that on milbreaks).
     */
    @Invars(@Expression("DECENNIUM.asMilliseconds() == ((10 * 365) + 2) * 24 * 60 * 60 * 1000"))
    DECENNIUM   (((10L * 365L) + 2L) * 24L * 60L * 60L * 1000L),

    /**
     * 100 years. In every century, there are 24 leap years (100 / 4, minus the
     * non-leap year on the century break). Note that calculations with this duration
     * does not take into account leap seconds, nor the exception that
     * a century break that is of a year divisible by 400 indeed is a leap year.
     */
    @Invars(@Expression("CENTURY.asMilliseconds() == ((100 * 365) + 24) * 24 * 60 * 60 * 1000"))
    CENTURY     (((100L * 365L) + 24L) * 24L * 60L * 60L * 1000L),

    /**
     * 1000 years. In every century, there are 241 leap years (the leap years in a century (24)
     * plus the 2 leap years of years divisible by 400). Note that calculations with this duration
     * does not take into account leap seconds, nor occasions where the millennium is divisible by
     * 400 year.
     */
    @Invars(@Expression("MILLENNNIUM.asMilliseconds() == ((1000 * 365) + 241) * 24 * 60 * 60 * 1000"))
    MILLENNIUM   (((1000L * 365L) + 242L) * 24L * 60L * 60L * 1000L);

    private Unit(long milliseconds) {
      $milliseconds = milliseconds;
      $maxDuration = Long.MAX_VALUE / milliseconds;
    }

    /**
     * The factor <code>{@link #MILLISECOND} / this</code>}.
     */
    @Basic(invars = @Expression("asMilliseconds() > 0"))
    public long asMilliseconds() {
      return $milliseconds;
    }

    private long $milliseconds;

    /**
     * The maximum duration expressed in this unit.
     */
    @Basic(invars = @Expression("maxDuration() == Long.MAX_VALUE / asMilliseconds()"))
    public long maxDuration() {
      return $maxDuration;
    }

    private long $maxDuration;

  }



  /**
   * The maximal duration supported. This is 9 223 372 036 854 776 000 milliseconds, which is
   * approx. 292 471 200 years.
   */
  public final static Duration MAX_VALUE = new Duration(Long.MAX_VALUE, MILLISECOND);

  @MethodContract(
    pre  = {
      @Expression("d >= 0"),
      @Expression("d != 0 ? unit != null"),
      @Expression("d != 0 ? d <= unit.maxDuration()")
    },
    post = @Expression("as(MILLISECOND) == d * unit.asMilliseconds()")
  )
  public Duration(long d, Unit unit) {
    assert pre(d >= 0, "d >= 0");
    assert pre(d != 0 ? unit != null : true, "unit != null");
    pre(d != 0 ? d <= unit.maxDuration() : true); // this is deliberately not put after an assert; this we want to validate ALWAYS
    $millis = (d == 0) ? 0 : d * unit.asMilliseconds();
  }

  @MethodContract(post = @Expression("as(MILLISECOND)"))
  public final long asMillisecond() {
    return $millis;
  }

  /**
   * This duration expressed in the unit {@code unit}. Expect accurary to 5 decimals.
   */
  @Basic(pre = @Expression("unit != null"),
         invars = @Expression("for (Unit u : Unit.values()) {as(u) >= 0}"))
  public final float as(Unit unit) {
    preArgumentNotNull(unit, "unit");
//    System.out.println("      unit = " + unit.asMilliseconds() + "ms");
    return ((float)$millis) / unit.asMilliseconds();
  }

  /**
   * This duration expressed in the unit {@code unit}, truncated to an integer.
   */
  @Basic(pre = @Expression("unit != null"),
         invars = @Expression("for (Unit u : Unit.values()) {as(u) >= 0}"))
  public final long asTruncated(Unit unit) {
    preArgumentNotNull(unit, "unit");
    return (long)Math.floor(as(unit));
  }

  @Invars(@Expression("$millis >= 0"))
  private final long $millis;

  @Override
  @MethodContract(post = @Expression("as(MILLESECOND) == ((Duration)other).as(MILLISECOND)"))
  public boolean equals(Object other) {
    return super.equals(other) && $millis == ((Duration)other).as(MILLISECOND);
  }

  @Override
  public int hashCode() {
    return (int)$millis;
  }

  @Override
  public String toString() {
    NumberFormat nf = NumberFormat.getIntegerInstance();
    return nf.format($millis) + "ms";
  }

  /**
   * @todo move to a formatter-like class
   */
  public String toRoughString() {
    NumberFormat nf = NumberFormat.getIntegerInstance();
    if (asTruncated(MILLENNIUM) > 0) {
      return nf.format(asTruncated(MILLENNIUM)) + " millennia";
    }
    else if (asTruncated(CENTURY) > 0) {
      return nf.format(asTruncated(CENTURY)) + " centuries";
    }
    else if (asTruncated(YEAR) > 0) {
      return nf.format(asTruncated(YEAR)) + " years";
    }
    else if (asTruncated(MONTH) > 0) {
      return nf.format(asTruncated(MONTH)) + " months";
    }
    else if (asTruncated(WEEK) > 0) {
      return nf.format(asTruncated(WEEK)) + " weeks";
    }
    else if (asTruncated(DAY) > 0) {
      return nf.format(asTruncated(DAY)) + " days";
    }
    else if (asTruncated(HOUR) > 0) {
      return nf.format(asTruncated(HOUR)) + "h";
    }
    else if (asTruncated(MINUTE) > 0) {
      return nf.format(asTruncated(MINUTE)) + " minutes";
    }
    else if (asTruncated(SECOND) > 0) {
      return nf.format(asTruncated(SECOND)) + "s";
    }
    else {
      return nf.format(as(MILLISECOND)) + "ms";
    }
  }

  @MethodContract(post = {
    @Expression("o == null || as(MILLESECOND) < o.as(MILLISECOND) ? -1 : (as(MILLISECOND) == o.as(MILLISECOND) ? 0 : +1)")
  })
  public int compareTo(Duration o) {
    if (o == null) {
      return -1;
    }
    else {
      long other = o.$millis;
      if ($millis < other) {
        return -1;
      }
      else if ($millis == other) {
        return 0;
      }
      else {
        return 1;
      }
    }
  }

  /**
   * The sum of the durations in {@code d}.
   */
  @MethodContract(
    pre  = {
      @Expression("for (int i = 0 .. d.length) {d[i] != null}"),
      @Expression("sum (Duration aD : d) {d.asMillisecond()} <= Long.MAX_VALUE")
    },
    post = {
      @Expression("result != null"),
      @Expression("d.length == 0 ? result.asMillisecond() == 0"),
      @Expression("d.length != 0 ? result.asMillisecond() == sum(d[1..d.length]) + d[0].asMillisecond()")
    }
  )
  public static Duration sum(Duration... d) {
    preSum(d);
    long result = 0;
    for (Duration aD : d) {
      result += aD.asMillisecond();
    }
    return new Duration(result, MILLISECOND);
  }

  private static void preSum(Duration[] d) {
    if (d.length > 1) {
      List<Duration> dList = Arrays.asList(d);
      List<Duration> dCdrList = dList.subList(1, dList.size());
      Duration[] dCdr = dCdrList.toArray(new Duration[dCdrList.size()]);
      pre(sum(dCdr).asMillisecond() <= Long.MAX_VALUE - d[0].asMillisecond());
    }
  }

  /**
   * The difference between 2 durations. Order is unimportant.
   */
  @MethodContract(
    pre  = {
      @Expression("d1 != null"),
      @Expression("d2 != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.asMillisecond() == Math.abs(d1.asMillisecond() - d2.asMillisecond())")
    }
  )
  public static Duration delta(Duration d1, Duration d2) {
    assert preArgumentNotNull(d1, "d1");
    assert preArgumentNotNull(d2, "d2");
    long result = Math.abs(d1.asMillisecond() - d2.asMillisecond());
    return new Duration(result, MILLISECOND);
  }

  /**
   * The time difference between 2 dates. Order is unimportant.
   */
  @MethodContract(
    pre  = {
      @Expression("d1 != null"),
      @Expression("d2 != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.asMillisecond() == Math.abs(d1.getTime() - d2.getTime())")
    }
  )
  public static Duration delta(Date d1, Date d2) {
    assert preArgumentNotNull(d1, "d1");
    assert preArgumentNotNull(d2, "d2");
    long result = Math.abs(d1.getTime() - d2.getTime());
    return new Duration(result, MILLISECOND);
  }

  /**
   * A duration {@code n} times as long as this duration.
   */
  @MethodContract(
    pre  = {
      @Expression("n >= 0"),
      @Expression("n * asMillisecond() <= Long.MAX_VALUE")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.asMilliseconde() == asMillisecond() * n")
    }
  )
  public Duration times(int n) {
    pre(n >= 0);
    pre(n == 0 || $millis <= Long.MAX_VALUE / n);
    return new Duration($millis * n, MILLISECOND);
  }


  /**
   * A duration that is the {@code n}th part as long as this duration.
   */
  @MethodContract(
    pre  = {
      @Expression("n > 0")
    },
    post = {
      @Expression("result != null"),
      @Expression("result.asMilliseconde() == asMillisecond() / n")
    }
  )
  public Duration div(int n) {
    pre(n > 0);
    return new Duration($millis / n, MILLISECOND);
  }

}
