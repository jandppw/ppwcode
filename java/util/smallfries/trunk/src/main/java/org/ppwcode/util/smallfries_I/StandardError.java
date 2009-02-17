/*<license>
Copyright 2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $ by PeopleWare n.v..

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

package org.ppwcode.util.smallfries_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.Serializable;

import org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Computes the standard error.
 *   The standard error is the standard deviation divided by the square root
 *   of the number of elements.
 *   This implementation wraps a {@link StandardDeviation} instance.
 *   The <code>isBiasCorrected</code> property of the wrapped {@link StandardDeviation}
 *   instance is exposed, so that this class can be used to
 *   compute both the "sample standard error" (using the sample standard deviation)
 *   or the "population standard deviation" (using the population standard deviation).
 *   See {@link StandardDeviation} for more information.</p>
 * <p>The standard error of an empty set is {@link Double#NaN}.
 *   The standard error of a set containing one element is 0.
 *   If one of the elements is {@link Double#NaN}, then the result is {@link Double#NaN}.
 *   If none of the elements is {@link Double#NaN}, but one of the elements is
 *   {@link Double#POSITIVE_INFINITY} or {@link Double#NEGATIVE_INFINITY}, then the result
 *   is {@link Double#POSITIVE_INFINITY}.</p>
 *
 * @author Nele Smeets
 */
@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public class StandardError extends AbstractStorelessUnivariateStatistic
    implements Serializable {

  private static final long serialVersionUID = 3627337040946565285L;

  /**
   * Constructs a StandardError. Sets the underlying {@link StandardDeviation}
   * instance's <code>isBiasCorrected</code> property to true.
   */
  public StandardError() {
      $standardDeviation = new StandardDeviation();
  }

  /**
   * Contructs a StandardError with the specified value for the
   * <code>isBiasCorrected</code> property.  If this property is set to
   * <code>true</code>, the {@link StandardDeviation} used in computing results will
   * use the bias-corrected, or "sample" formula.  See {@link StandardDeviation} for
   * details.
   *
   * @param isBiasCorrected  whether or not the standard error computation will
   *                         use the bias-corrected formula
   */
  public StandardError(boolean isBiasCorrected) {
    $standardDeviation = new StandardDeviation(isBiasCorrected);
  }

  @Override
  public void clear() {
    $standardDeviation.clear();
  }

  @Override
  public long getN() {
    return $standardDeviation.getN();
  }

  @Override
  public double getResult() {
    return $standardDeviation.getResult() / Math.sqrt($standardDeviation.getN());
  }

  @Override
  public void increment(double d) {
    $standardDeviation.increment(d);
  }

  /*<property name="standardDeviation">*/
  //------------------------------------------------------------------

  private StandardDeviation $standardDeviation;

  /*</property>*/

  /**
   * @return Returns the isBiasCorrected.
   */
  public boolean isBiasCorrected() {
      return $standardDeviation.isBiasCorrected();
  }

  /**
   * @param isBiasCorrected The isBiasCorrected to set.
   */
  public void setBiasCorrected(boolean isBiasCorrected) {
    $standardDeviation.setBiasCorrected(isBiasCorrected);
  }

  /**
   * Returns the standard error of the entries in the input array, or
   * <code>Double.NaN</code> if the array is empty.
   * <p>
   * Returns 0 for a single-value (i.e. length = 1) sample.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @return the standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null
   */
  @Override
  public double evaluate(final double[] values)  {
      return $standardDeviation.evaluate(values) / Math.sqrt(values.length);
  }

  /**
   * Returns the standard error of the entries in the specified portion of
   * the input array, or <code>Double.NaN</code> if the designated subarray
   * is empty.
   * <p>
   * Returns 0 for a single-value (i.e. length = 1) sample.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @param begin index of the first array element to include
   * @param length the number of elements to include
   * @return the standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null or the array index
   *  parameters are not valid
   */
  @Override
  public double evaluate(final double[] values, final int begin, final int length)  {
     return $standardDeviation.evaluate(values, begin, length) / Math.sqrt(length);
  }

  /**
   * Returns the standard error of the entries in the specified portion of
   * the input array, using the precomputed mean value.  Returns
   * <code>Double.NaN</code> if the designated subarray is empty.
   * <p>
   * Returns 0 for a single-value (i.e. length = 1) sample.
   * <p>
   * The formula used assumes that the supplied mean value is the arithmetic
   * mean of the sample data, not a known population parameter.  This method
   * is supplied only to save computation when the mean has already been
   * computed.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @param mean the precomputed mean value
   * @param begin index of the first array element to include
   * @param length the number of elements to include
   * @return the standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null or the array index
   *  parameters are not valid
   */
  public double evaluate(final double[] values, final double mean,
          final int begin, final int length)  {
      return $standardDeviation.evaluate(values, mean, begin, length) / Math.sqrt(length);
  }

  /**
   * Returns the standard error of the entries in the input array, using
   * the precomputed mean value.  Returns
   * <code>Double.NaN</code> if the designated subarray is empty.
   * <p>
   * Returns 0 for a single-value (i.e. length = 1) sample.
   * <p>
   * The formula used assumes that the supplied mean value is the arithmetic
   * mean of the sample data, not a known population parameter.  This method
   * is supplied only to save computation when the mean has already been
   * computed.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @param mean the precomputed mean value
   * @return the standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null
   */
  public double evaluate(final double[] values, final double mean)  {
      return $standardDeviation.evaluate(values, mean) / Math.sqrt(values.length);
  }

}

