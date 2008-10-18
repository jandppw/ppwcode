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


import java.util.Date;


/**
 * An exception type for working with the start and end date of a period.
 *
 * @author    nsmeets
 * @author    Peopleware n.v.
 */
public class InvalidPeriodException extends Exception {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     startDate
   *            The start date to set.
   * @param     endDate
   *            The end date to set.
   * @param     message
   *            The message that describes the exceptional circumstance.
   *
   * @post      new.getStartDate() == startDate;
   * @post      new.getEndDate() == endDate;
   * @post      (message != null)
   *                ? (new.getMessage().equals(message))
   *                : (new.getMessage() == null);
   */
  public InvalidPeriodException(final Date startDate, final Date endDate, final String message) {
    super(message);
    $startDate = startDate;
    $endDate = endDate;
  }

  /*</construction;>*/



  /*<property name="startDate">*/
  //------------------------------------------------------------------

  /**
   * The start date of the period.
   *
   * @basic
   */
  public final Date getStartDate() {
    return $startDate;
  }

  /**
   * @invar     true;
   */
  private Date $startDate;

  /*</property>*/


  /*<property name="endDate">*/
  //------------------------------------------------------------------

  /**
   * The end date of the period.
   *
   * @basic
   */
  public final Date getEndDate() {
    return $endDate;
  }

  /**
   * @invar     true;
   */
  private Date $endDate;

  /*</property>*/


}
