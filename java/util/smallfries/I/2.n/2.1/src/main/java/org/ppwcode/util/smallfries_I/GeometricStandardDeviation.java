/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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
 * <p>Computes the geometric standard deviation.
 *   The geometric standard deviation can be computed using the
 *   arithmetic standard deviation:</p>
 * <ol>
 *   <li>Take the natural logarithm of all values</li>
 *   <li>Compute the standard deviation of the new values</li>
 *   <li>Take the exponential value of the result</li>
 * </ol>
 * <p>This implementation wraps a {@link StandardDeviation} instance.
 *   The <code>isBiasCorrected</code> property of the wrapped {@link StandardDeviation}
 *   instance is exposed, so that this class can be used to
 *   compute both the "sample geometric standard deviation" (using the sample standard deviation)
 *   or the "population geometric standard deviation" (using the population standard deviation).
 *   See {@link StandardDeviation} for more information.</p>
 * <p>The geometric standard deviation of an empty set is {@link Double#NaN}.
 *   The geometric standard deviation of a set containing one element is 1.
 *   If one of the elements is {@link Double#NaN}, then the result is {@link Double#NaN}.
 *   If one of the elements is negative (including {@link Double#NEGATIVE_INFINITY}),
 *   the result is {@link Double#NaN}.
 *   If none of the elements is {@link Double#NaN} or negative, and at least one value is 0,
 *   then the result is {@link Double#POSITIVE_INFINITY}.
 *   If none of the elements is {@link Double#NaN} or negative, and at least one value is
 *   {@link Double#POSITIVE_INFINITY}, the result is {@link Double#POSITIVE_INFINITY}.</p>
 *
 * @author Nele Smeets
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class GeometricStandardDeviation extends AbstractStorelessUnivariateStatistic
    implements Serializable {

  private static final long serialVersionUID = 817986764659095687L;

  /**
   * Constructs a GeometricStandardDeviation.
   * Sets the underlying {@link StandardDeviation}
   * instance's <code>isBiasCorrected</code> property to true.
   */
  public GeometricStandardDeviation() {
      $standardDeviation = new StandardDeviation();
  }

  /**
   * Contructs a GeometricStandardDeviation with the specified value for the
   * <code>isBiasCorrected</code> property.  If this property is set to
   * <code>true</code>, the {@link StandardDeviation} used in computing results will
   * use the bias-corrected, or "sample" formula.  See {@link StandardDeviation} for
   * details.
   *
   * @param isBiasCorrected  whether or not the standard deviation computation will
   *                         use the bias-corrected formula
   */
  public GeometricStandardDeviation(boolean isBiasCorrected) {
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
    return Math.exp($standardDeviation.getResult());
  }

  @Override
  public void increment(double d) {
    $standardDeviation.increment(Math.log(d));
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
   * Returns the geometric standard deviation of the entries in the input array, or
   * <code>Double.NaN</code> if the array is empty.
   * <p>
   * Returns 1 for a single-value (i.e. length = 1) sample.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @return the geometric standard deviation of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null
   */
  @Override
  public double evaluate(final double[] values)  {
    return Math.exp($standardDeviation.evaluate(MathUtil.logTransform(values)));
  }

  /**
   * Returns the geometric standard deviation of the entries in the specified portion of
   * the input array, or <code>Double.NaN</code> if the designated subarray
   * is empty.
   * <p>
   * Returns 1 for a single-value (i.e. length = 1) sample.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @param begin index of the first array element to include
   * @param length the number of elements to include
   * @return the geometric standard deviation of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null or the array index
   *  parameters are not valid
   */
  @Override
  public double evaluate(final double[] values, final int begin, final int length)  {
    return Math.exp($standardDeviation.evaluate(MathUtil.logTransform(values), begin, length));
  }

  /**
   * Returns the geometric standard deviation of the entries in the specified portion of
   * the input array, using the precomputed mean value of the log-transformed
   * sample data.  Returns <code>Double.NaN</code> if the designated subarray is empty.
   * <p>
   * Returns 1 for a single-value (i.e. length = 1) sample.
   * <p>
   * The formula used assumes that the supplied mean value is the arithmetic
   * mean of the log-transformed sample data, not a known population parameter.
   * This method is supplied only to save computation when the mean has already been
   * computed.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @param mean the precomputed mean value of the log-transformed sample data
   * @param begin index of the first array element to include
   * @param length the number of elements to include
   * @return the geometric standard deviation of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null or the array index
   *  parameters are not valid
   */
  public double evaluate(final double[] values, final double mean,
          final int begin, final int length)  {
    return Math.exp($standardDeviation.evaluate(MathUtil.logTransform(values), mean, begin, length));
  }

  /**
   * Returns the geometric standard deviation of the entries in the input array, using
   * the precomputed mean value of the log-transformed sample data.  Returns
   * <code>Double.NaN</code> if the designated subarray is empty.
   * <p>
   * Returns 1 for a single-value (i.e. length = 1) sample.
   * <p>
   * The formula used assumes that the supplied mean value is the arithmetic
   * mean of the log-transformed sample data, not a known population parameter.
   * This method is supplied only to save computation when the mean has already been
   * computed.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @param mean the precomputed mean value of the log-transformed sample data
   * @return the geometric standard deviation of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null
   */
  public double evaluate(final double[] values, final double mean)  {
    return Math.exp($standardDeviation.evaluate(MathUtil.logTransform(values), mean));
  }

}

