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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.EQUALS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.timeIntervalRelation;

import java.util.Date;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.Duration;
import org.ppwcode.vernacular.value_III.AbstractImmutableValue;


/**
 * General supporting code for time interval implementations.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTimeInterval extends AbstractImmutableValue implements TimeInterval {

  @Override
  public final boolean equals(Object other) {
//    if (other != null && other instanceof TimeInterval && getBegin() != null &&
//        getEnd() != null && ((TimeInterval)other).getBegin() != null &&
//        ((TimeInterval)other).getEnd() != null) {
//      GregorianCalendar gcB1 = new GregorianCalendar();
//      gcB1.setTime(getBegin());
//      GregorianCalendar gcE1 = new GregorianCalendar();
//      gcE1.setTime(getEnd());
//      GregorianCalendar gcB2 = new GregorianCalendar();
//      gcB2.setTime(((TimeInterval)other).getBegin());
//      GregorianCalendar gcE2 = new GregorianCalendar();
//      gcE2.setTime(((TimeInterval)other).getEnd());
//      if ((gcB1.get(Calendar.YEAR) == 2000)  && (gcE1.get(Calendar.YEAR) == 2122) &&
//          (gcB2.get(Calendar.YEAR) == 2000) && (gcE2.get(Calendar.YEAR) == 2122) &&
//          ((TimeInterval)other).getDuration() != null) {
//        System.out.println("IT'S A HIT");
//      }
//    }
    /* we cannot call super.equals(other) in this case; that checks whether
     * other has the same class as this. In this case that will never happen,
     * since we are abstract. Furthermore we want to compare all TimeIntervals
     * to each other, via the interface.
     */
    return (other != null) && (getClass().isInstance(other)) && timeIntervalRelation(this, (TimeInterval)other) == EQUALS;
  }

  @Override
  public final int hashCode() {
    int result = 0;
    if (getBegin() != null) {
      result += getBegin().hashCode();
    }
    if (getEnd() != null) {
      result += getEnd().hashCode();
    }
    /* since we need to be consistent with equals, we can only take into account properties
       that are used by allenRelation, i.e., begin and end */
    return result;
  }

  @Override
  public String toString() {
    return "[" + toString(getBegin()) + ", " + toString(getEnd()) + "[\u0394(" + toStringDuration() + ")"; // \u0394 is Greek capital delta
  }

  private static String toString(Date d) {
    return d == null ? "|?|" : d.toString();
  }

  private String toStringDuration() {
    Duration d = getDuration();
    return d == null ? "-?-" : d.toRoughString();
  }

  public final Date determinateBegin(Date stubBegin) {
    return getBegin() != null ? getBegin() : stubBegin;
  }

  public final Date determinateEnd(Date stubEnd) {
    return getEnd() != null ? getEnd() : stubEnd;
  }

}
