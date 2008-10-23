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

import java.text.NumberFormat;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.AbstractImmutableValue;


/**
 * A period has a start date and an end date.
 * The start date of a period is strictly before the end date (so a period
 * should not be empty).
 * The start date is included in the interval, the end date is not
 * (right half-open interval).
 *
 * The {@link #compareTo(Object) compare method} compares the
 * {@link #getBegin()}.
 *
 * @author    nsmeets
 * @author    Peopleware NV
 *
 * @invar     (getStartDate() != null && getEndDate() != null)
 *                ? getStartDate().before(getEndDate())
 *                : true;
 *
 * @mudo (jand) must be a value, and move to ppw-value; suggest mutable; (add
 *       normalize method and getWildExceptions ?)
 * @mudo (nsmeets) Normalization does not seem to be a good idea.
 *
 * @see http://en.wikipedia.org/wiki/Allen's_Interval_Algebra Allen's Interval Algebra
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class Duration extends AbstractImmutableValue {

  public static enum Unit {
    MILLISECOND (1),
    SECOND      (1000),
    MINUTE      (60 * 1000),
    HOUR        (60 * 60 * 1000),
    DAY         (24 * 60 * 60 * 1000),
    WEEK        (7 * 24 * 60 * 60 * 1000),
    MONTH       (30 * 24 * 60 * 60 * 1000),
    QUARTER     (3 * 30 * 24 * 60 * 60 * 1000),
    YEAR        (365 * 24 * 60 * 60 * 1000),
    DECENNIUM   (((10 * 365) + 2) * 24 * 60 * 60 * 1000),
    CENTURY     (((100 * 365) + 24) * 24 * 60 * 60 * 1000),
    MILENNIUM   (((1000 * 365) + 241) * 24 * 60 * 60 * 1000);

    Unit(int milliseconds) {
      $milliseconds = milliseconds;
    }

    public int asMilliseconds() {
      return $milliseconds;
    }

    private int $milliseconds;

  }

  public Duration(int d, Unit unit) {
    $millis = d * unit.asMilliseconds();
  }

  public final float as(Unit unit) {
    return $millis / unit.asMilliseconds();
  }

//  public final int asFloor(Unit unit) {
//    return (int)floor($millis / unit.asMilliseconds());
//  }

  private final long $millis;

  @Override
  public boolean equals(Object other) {
    return other != null && other instanceof Duration &&
           $millis == ((Duration)other).as(MILLISECOND);
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

}
