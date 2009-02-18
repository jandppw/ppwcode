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
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Computes the geometric standard error.
 *   The geometric standard error can be computed using the
 *   arithmetic standard error:</p>
 * <ol>
 *   <li>Take the natural logarithm of all values</li>
 *   <li>Compute the standard error of the new values</li>
 *   <li>Take the exponential value of the result</li>
 * </ol>
 * <p>This implementation wraps a {@link StandardError} instance.
 *   The <code>isBiasCorrected</code> property of the wrapped {@link StandardError}
 *   instance is exposed, so that this class can be used to
 *   compute both the "sample geometric standard error" (using the sample standard error)
 *   or the "population geometric standard deviation" (using the population standard error).
 *   See {@link StandardError} for more information.</p>
 * <p>The geometric standard error of an empty set is {@link Double#NaN}.
 *   The geometric standard error of a set containing one element is 1.
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
public class GeometricStandardError extends AbstractStorelessUnivariateStatistic
    implements Serializable {

  private static final long serialVersionUID = -7832903942699783021L;

  /**
   * Constructs a GeometricStandardError.
   * Sets the underlying {@link StandardError}
   * instance's <code>isBiasCorrected</code> property to true.
   */
  public GeometricStandardError() {
      $standardError = new StandardError();
  }

  /**
   * Contructs a GeometricStandardError with the specified value for the
   * <code>isBiasCorrected</code> property.  If this property is set to
   * <code>true</code>, the {@link StandardError} used in computing results will
   * use the bias-corrected, or "sample" formula.  See {@link StandardError} for
   * details.
   *
   * @param isBiasCorrected  whether or not the standard error computation will
   *                         use the bias-corrected formula
   */
  public GeometricStandardError(boolean isBiasCorrected) {
    $standardError = new StandardError(isBiasCorrected);
  }

  @Override
  public void clear() {
    $standardError.clear();
  }

  @Override
  public long getN() {
    return $standardError.getN();
  }

  @Override
  public double getResult() {
    return Math.exp($standardError.getResult());
  }

  @Override
  public void increment(double d) {
    $standardError.increment(Math.log(d));
  }

  /*<property name="standardError">*/
  //------------------------------------------------------------------

  private StandardError $standardError;

  /*</property>*/

  /**
   * @return Returns the isBiasCorrected.
   */
  public boolean isBiasCorrected() {
      return $standardError.isBiasCorrected();
  }

  /**
   * @param isBiasCorrected The isBiasCorrected to set.
   */
  public void setBiasCorrected(boolean isBiasCorrected) {
    $standardError.setBiasCorrected(isBiasCorrected);
  }

  /**
   * Returns the geometric standard error of the entries in the input array, or
   * <code>Double.NaN</code> if the array is empty.
   * <p>
   * Returns 1 for a single-value (i.e. length = 1) sample.
   * <p>
   * Throws <code>IllegalArgumentException</code> if the array is null.
   * <p>
   * Does not change the internal state of the statistic.
   *
   * @param values the input array
   * @return the geometric standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null
   */
  @Override
  public double evaluate(final double[] values)  {
    return Math.exp($standardError.evaluate(MathUtil.logTransform(values)));
  }

  /**
   * Returns the geometric standard error of the entries in the specified portion of
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
   * @return the geometric standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null or the array index
   *  parameters are not valid
   */
  @Override
  public double evaluate(final double[] values, final int begin, final int length)  {
    return Math.exp($standardError.evaluate(MathUtil.logTransform(values), begin, length));
  }

  /**
   * Returns the geometric standard error of the entries in the specified portion of
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
   * @return the geometric standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null or the array index
   *  parameters are not valid
   */
  public double evaluate(final double[] values, final double mean,
          final int begin, final int length)  {
    return Math.exp($standardError.evaluate(MathUtil.logTransform(values), mean, begin, length));
  }

  /**
   * Returns the geometric standard error of the entries in the input array, using
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
   * @return the geometric standard error of the values or Double.NaN if length = 0
   * @throws IllegalArgumentException if the array is null
   */
  public double evaluate(final double[] values, final double mean)  {
    return Math.exp($standardError.evaluate(MathUtil.logTransform(values), mean));
  }

}

