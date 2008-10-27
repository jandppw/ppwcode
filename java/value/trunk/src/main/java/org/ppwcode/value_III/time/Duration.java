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
import static org.ppwcode.value_III.time.Duration.Unit.MILLISECOND;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;

import java.text.NumberFormat;

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
    MINUTE      (60 * 1000),

    @Invars(@Expression("HOUR.asMilliseconds() == 60 * 60 * 1000"))
    HOUR        (60 * 60 * 1000),

    /**
     * The duration of one day. Note that calculations with this duration
     * are off if the actual day you are talking about contains a leap second.
     */
    @Invars(@Expression("DAY.asMilliseconds() == 24 * 60 * 60 * 1000"))
    DAY         (24 * 60 * 60 * 1000),

    /**
     * The duration of 7 days. Note that calculations with this duration
     * are off if the actual week you are talking about contains a day with a
     * leap second.
     */
    @Invars(@Expression("WEEK.asMilliseconds() == 7 * 24 * 60 * 60 * 1000"))
    WEEK        (7 * 24 * 60 * 60 * 1000),

    /**
     * The duration of 30 days. Note that calculations with this duration
     * are off if the actual month you are talking about contains a day with a
     * leap second, and clearly do not take into account the difference between
     * months with 28, 29, 30 or 31 days.
     */
    @Invars(@Expression("MONTH.asMilliseconds() == 30 * 24 * 60 * 60 * 1000"))
    MONTH       (30 * 24 * 60 * 60 * 1000),

    /**
     * The duration of 90 days. Note that calculations with this duration
     * are off if the actual quarter you are talking about contains a day with a
     * leap second , and clearly do not take into account the difference between
     * months with 28, 29, 30 or 31 days.
     */
    @Invars(@Expression("QUARTER.asMilliseconds() == 90 * 24 * 60 * 60 * 1000"))
    QUARTER     (3 * 30 * 24 * 60 * 60 * 1000),

    /**
     * The duration of 365 days. Note that calculations with this duration
     * are off if the actual year contains a leap day or a day with a leap second.
     */
    @Invars(@Expression("YEAR.asMilliseconds() == 365 * 24 * 60 * 60 * 1000"))
    YEAR        (365 * 24 * 60 * 60 * 1000),

    /**
     * 10 years. In every decennium, there are 2 leap years. Note that calculations
     * with this duration does not take into account leap seconds, nor non-leap
     * years on century breaks (or exceptions on that on milbreaks).
     */
    @Invars(@Expression("DECENNIUM.asMilliseconds() == ((10 * 365) + 2) * 24 * 60 * 60 * 1000"))
    DECENNIUM   (((10 * 365) + 2) * 24 * 60 * 60 * 1000),

    /**
     * 100 years. In every century, there are 24 leap years (100 / 4, minus the
     * non-leap year on the century break). Note that calculations with this duration
     * does not take into account leap seconds, nor the exception that
     * a century break that is also a millennium break indeed is a leap year.
     */
    @Invars(@Expression("CENTURY.asMilliseconds() == ((100 * 365) + 24) * 24 * 60 * 60 * 1000"))
    CENTURY     (((100 * 365) + 24) * 24 * 60 * 60 * 1000),

    /**
     * 1000 years. In every century, there are 241 leap years (the leap years in a century (24)
     * plus the leap year of the millennium break). Note that calculations with this duration
     * does not take into account leap seconds.
     */
    @Invars(@Expression("MILLENNNIUM.asMilliseconds() == ((1000 * 365) + 241) * 24 * 60 * 60 * 1000"))
    MILLENNIUM   (((1000 * 365) + 241) * 24 * 60 * 60 * 1000);

    private Unit(int milliseconds) {
      $milliseconds = milliseconds;
    }

    /**
     * The factor <code>{@link #MILLISECOND} / this</code>}.
     */
    @Basic
    public int asMilliseconds() {
      return $milliseconds;
    }

    private int $milliseconds;

  }

  @MethodContract(
    pre  = {
      @Expression("d >= 0"),
      @Expression("d != 0 ? unit != null")
    },
    post = @Expression("as(MILLISECOND) == d * unit.asMilliseconds()")
  )
  public Duration(long d, Unit unit) {
    pre(d >= 0, "d >= 0");
    pre(d != 0 ? unit != null : true, "unit != null");
    $millis = (d == 0) ? 0 : d * unit.asMilliseconds();
  }

  @Basic(pre = @Expression("unit != null"),
         invars = @Expression("for (Unit u : Unit.values()) {as(u) >= 0}"))
  public final float as(Unit unit) {
    preArgumentNotNull(unit, "unit");
    return $millis / unit.asMilliseconds();
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
    return nf.format($millis) + " ms";
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

}
