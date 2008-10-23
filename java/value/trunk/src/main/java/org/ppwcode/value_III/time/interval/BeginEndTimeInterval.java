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

import java.util.Date;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.legacy.DayPeriod;
import org.ppwcode.value_III.time.Duration;
import org.ppwcode.vernacular.value_III.AbstractMutableValue;


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
public class BeginEndTimeInterval extends AbstractMutableValue implements TimeInterval {



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Create a new empty period object.
   *
   * @post    getStartDate() == null;
   * @post    getEndDate() == null;
   */
  public BeginEndTimeInterval(Date begin, Date end) {
    // Since we demand of subtypes of MutableValue that they implement
    // {@link java.io.Serializable}, a default constructor is mandatory.
    // NOP
  }

  /**
   * Create a new empty period object.
   *
   * @post    getStartDate() == null;
   * @post    getEndDate() == null;
   */
  public BeginEndTimeInterval() {
    // Since we demand of subtypes of MutableValue that they implement
    // {@link java.io.Serializable}, a default constructor is mandatory.
    // NOP
  }

  /*</construction>*/


  /*<property name="begin">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public Date getBegin() {
    if ($begin == null) {
      return null;
    }
    else {
      return (Date) $begin.clone();
    }
  }

  /**
   * @param     begin
   *            The start date to set for this BeginEndTimeInterval.
   * @post      (startDate == null)
   *               ? new.getStartDate() == null
   *               : new.getStartDate().equals(startDate);
   * @throws    InvalidPeriodException pExc
   *            ( startDate != null
   *                && getEndDate() != null
   *                && !startDate.before(getEndDate())
   *            )
   *              && (startDate == null)
   *                    ? (pExc.getStartDate() == null)
   *                    : (pExc.getStartDate().equals(startDate))
   *              && (getEndDate() == null)
   *                    ? (pExc.getEndDate() == null)
   *                    : (pExc.getEndDate().equals(getEndDate()))
   *              && pExc.getMessage()
   *                    .equals("The given start date is not before the current end date.");
   */
  public void setBegin(final Date begin) throws InvalidPeriodException {
    if (begin != null
            && getEnd() != null
            && !begin.before(getEnd())
    ) {
      InvalidPeriodException ipe = new InvalidPeriodException(
          begin, getEnd(),
          "The given start date is not before the current end date."
      );
      throw ipe;
    }
    $begin = begin;
  }

  private Date $begin;

  /*</property>*/



  /*<property name="end">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public Date getEnd() {
    if ($end == null) {
      return null;
    }
    else {
      return (Date) $end.clone();
    }
  }

  /**
   * @param     end
   *            The end date to set for this BeginEndTimeInterval.
   * @post      (endDate == null)
   *               ? new.getEndDate() == null
   *               : new.getEndDate().equals(endDate);
   * @throws    InvalidPeriodException pExc
   *            ( getStartDate() != null
   *                && endDate != null
   *                && !getStartDate().before(endDate)
   *            )
   *              && (getStartDate() == null)
   *                    ? (pExc.getStartDate() == null)
   *                    : (pExc.getStartDate().equals(getStartDate()))
   *              && (endDate == null)
   *                    ? (pExc.getEndDate() == null)
   *                    : (pExc.getEndDate().equals(endDate))
   *              && pExc.getMessage()
   *                    .equals("The current start date is not before the given end date.");
   */
  public void setEnd(final Date end) throws InvalidPeriodException {
    if (getBegin() != null
          && end != null
          && !getBegin().before(end)
    ) {
      throw new InvalidPeriodException(
          getBegin(), end,
          "The current start date is not before the given end date."
      );
    }
    $end = end;
  }

  private Date $end;

  /*</property>*/

  /**
   * @return  result instanceof BeginEndTimeInterval
   *          &&
   *          (getStartDate() == null)
   *              ? result.getStartDate() == null
   *              : result.getStartDate().equals(getStartDate())
   *          &&
   *          (getEndDate() == null)
   *              ? result.getEndDate() == null
   *              : result.getEndDate().equals(getEndDate());
   */
  public BeginEndTimeInterval clone() {
    BeginEndTimeInterval result = new BeginEndTimeInterval();
    try {
      result.setBegin(getBegin());
      result.setEnd(getEnd());
    }
    catch (InvalidPeriodException PExc) {
      assert false : "InvalidPeriodException on clone cannot happen.";
    }
    return result;
  }

  /**
   * @return  o instanceof BeginEndTimeInterval &&
   *          (getStartDate() == null)
   *             ? o.getStartDate() == null
   *             : getStartDate().equals(o.getStartDate())
   *          &&
   *          (getEndDate() == null)
   *             ? o.getEndDate() == null
   *             : getEndDate().equals(o.getEndDate());
   */
  public boolean equals(final Object o) {
    if (!(o instanceof BeginEndTimeInterval)) {
      return false;
    }
    BeginEndTimeInterval other = (BeginEndTimeInterval) o;
    return
      ((getBegin() == null)
          ? (other.getBegin() == null)
          : (getBegin().equals(other.getBegin()))
      )
      &&
      ((getEnd() == null)
          ? (other.getEnd() == null)
          : (getEnd().equals(other.getEnd()))
      );
  }

  /**
   * @return  ( (getStartDate() == null)
   *               ? 0
   *               : getStartDate().hashCode()
   *          )
   *          +
   *          ( (getEndDate() == null)
   *               ? 0
   *               : getEndDate().hashCode()
   *          );
   */
  public int hashCode() {
    return
      ((getBegin() == null)
         ? 0
         : getBegin().hashCode()
      )
      +
      ((getEnd() == null)
         ? 0
         : getEnd().hashCode()
      );
  }

  /**
   * @return  ((getStartDate() == null) ? "???" : getStartDate().toString())
   *          +
   *          " - "
   *          +
   *          ((getEndDate() == null) ? "???" : getEndDate().toString());
   */
  public String toString() {
    String start = (getBegin() == null) ? "???" : getBegin().toString();
    String end = (getEnd() == null) ? "???" : getEnd().toString();
    return start + " - " + end;
  }

  /**
   * @return  ( getStartDate() != null &&
   *            getEndDate() != null
   *          )
   *             ? (  (  getEndDate().getTime()
   *                     -
   *                     getStartDate().getTime()
   *                  )
   *                  /
   *                  (24*60*60*1000)
   *               )
   *             : -1;
   *
   * @deprecated You probably want to use {@link DayPeriod}.
   */
  public long getNbDaysInPeriod() {
    Date startDate = getBegin();
    Date endDate = getEnd();
    if (startDate != null && endDate != null) {
        long differenceInMillis =
          endDate.getTime() - startDate.getTime();
        return differenceInMillis / (24 * 60 * 60 * 1000);
    }
    return -1;
  }

  /**
   * @return  ((getStartDate() != null) && (getEndDate() != null)) ? -1 : getNbDaysInPeriod() + 1;
   * @deprecated Use new class {@link DayPeriod}.
   */
  public long getNbDaysInPeriodInclusive() {
    long ndip = getNbDaysInPeriod();
    return (ndip == -1) ? -1 : ndip + 1;
  }

  /**
   * Compares this object with the specified object for order.
   *
   * @result  getStartDate() == null && ((BeginEndTimeInterval)o).getStartDate() == null
   *            ==> result == 0;
   * @result  getStartDate() == null && ((BeginEndTimeInterval)o).getStartDate() != null
   *            ==> result == -1;
   * @result  getStartDate() != null && ((BeginEndTimeInterval)o).getStartDate() == null
   *            ==> result == 1;
   * @result  getStartDate() != null && ((BeginEndTimeInterval)o).getStartDate() != null
   *            ==> getStartDate().compareTo(((BeginEndTimeInterval)o).getStartDate());
   */
  public int compareTo(final Object o) {
    BeginEndTimeInterval p = (BeginEndTimeInterval)o; // ClassCastException ok
    if (getBegin() == null) {
      if (p.getBegin() == null) { // NullPointerException ok
        return 0;
      }
      else {
        return -1;
      }
    }
    else if (p.getBegin() == null) {
      return 1;
    }
    else {
      return getBegin().compareTo(p.getBegin());
    }
  }

  /**
   * <code>date</code> is in this period, inclusive:
   * <code>date in [getStartDate(), getEndDate()]</code>.
   *
   * @return (date == null) && (getStartDate() == null) && (getEndDate() == null) &&
   *            (! date.before(getStartDate())) && (! date.after(getEndDate()));
   * @deprecated Use new class {@link DayPeriod}.
   */
  public final boolean containsInclusive(final Date date) {
    if ((date == null) || (getBegin() == null) || (getEnd() == null)) {
      return false;
    }
    else {
      return (!date.before(getBegin())) && (!date.after(getEnd()));
    }
  }


  /**
   * <code>date</code> is in this period, half-inclusive:
   * <code>date in [getStartDate(), getEndDate()[</code>.
   *
   * @return contains(date);
   * @deprecated User {@link #contains(Date)} instead.
   */
  public final boolean containsLeftInclusive(final Date date) {
    if ((date == null) || (getBegin() == null) || (getEnd() == null)) {
      return false;
    }
    else {
      return (!date.before(getBegin())) && date.before(getEnd());
    }
  }

  /**
   * <code>date</code> is in this period, half-inclusive:
   * <code>date in [getStartDate(), getEndDate()[</code>.
   *
   * @return (date == null) && (getStartDate() == null) && (getEndDate() == null) &&
   *            (! date.before(getStartDate())) && date.before(getEndDate());
   */
  public boolean contains(final Date date) {
    return containsInclusive(date);

  }

  public AllenRelation compareTo(TimeInterval other) {
    // TODO Auto-generated method stub
    return null;
  }

  public Duration getDuration() {
    // TODO Auto-generated method stub
    return null;
  }

}
