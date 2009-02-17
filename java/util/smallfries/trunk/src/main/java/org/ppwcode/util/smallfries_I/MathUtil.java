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


import static java.lang.Math.signum;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math.stat.descriptive.moment.FirstMoment;
import org.apache.commons.math.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;
import org.apache.commons.math.stat.descriptive.summary.Product;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * General math utility methods, missing from {@link Math}.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public final class MathUtil {

  private MathUtil() {
    // NOP
  }

  /**
   * Power of {@code double}, with exponent a positive integer.
   * We believe this iterative multiplication is faster than
   * {@code Math#power(double)} in this case
   *
   * @pre exponent >=0;
   */
  public static double power(double base, int exponent) {
    assert exponent >= 0;
    switch (exponent) {
      case 0:
        return 1;
      case 1:
        return base;
      case 2:
        return base * base;
      case 3:
        return base * base * base;
      default:
        if (base == Double.POSITIVE_INFINITY) {
          return base;
        }
        if (base == Double.NEGATIVE_INFINITY) {
          if (odd(exponent)) {
            return base;
          }
          else {
            return Double.POSITIVE_INFINITY;
          }
        }
        assert exponent > 3;
        double result = 1;
        int halfExponent = exponent / 2;
        assert halfExponent >= 2;
        double halfResult = power(base, halfExponent);
        result = halfResult * halfResult;
        if (odd(exponent)) {
          result *= base;
        }
        return result;
    }
  }

  /**
   * {@code i} is odd. For negative numbers, it still means that
   * the modulus is {@code 0}.
   *
   * @return i%2 != 0;
   */
  public static boolean odd(int i) {
    return i % 2 != 0;
  }

  /**
   * NaN's are equal. Serious doubles are compared, using {@code Math.ulp(d1) * 2}.
   */
  public static boolean equalPrimitiveValue(double d1, double d2) {
    return equalValue(d1, d2, Math.ulp(d1) * 2);
  }

  /**
   * NaN's are equal. Serious doubles are compared, taking
   * into account the given error.
   */
  public static boolean equalPrimitiveValue(double d1, double d2, double error) {
    if (Double.isNaN(d1)) {
      return Double.isNaN(d2);
    }
    else if (Double.isNaN(d2)) {
      return false;
    }
    else {
      if (Double.isInfinite(d1)) {
        if (Double.isInfinite(d2) && signum(d1) == signum(d2)) {
          return true;
        }
        else {
          return false;
        }
      }
      else {
        double delta = Math.abs(d1 - d2);
        return delta <= error;
      }
    }
  }

  /**
   * NaN's are equal.
   */
  public static boolean equalValue(Number n1, Number n2) {
    if (n1 instanceof Double) {
      return equalValue((Double)n1, n2);
    }
    else if (n1 instanceof Float) {
      return equalValue((Float)n1, n2);
    }
    else if (n1 instanceof Long) {
      return equalValue((Long)n1, n2);
    }
    else {
      if (n1 == null) {
        return n2 == null;
      }
      else if (n2 == null) {
        return false;
      }
      else {
        return n1.equals(n2);
      }
    }
  }

  /**
   * NaN's are equal. Serious doubles are compared, taking
   * into account the given error.
   */
  public static boolean equalValue(Double d, Number n, double error) {
    if (d == null) {
      return n == null;
    }
    else if (n == null) {
      return false;
    }
    else {
//      assert d != null;
//      assert n != null;
      return equalPrimitiveValue(d.doubleValue(), n.doubleValue(), error);
    }
  }

  /**
   * NaN's are equal. Serious doubles are compared, taking
   * into account the {@link Math#ulp(double)}.
   */
  public static boolean equalValue(Double d, Number n) {
    double ulp2 = 0;
    if (d != null &&
        n != null &&
        !d.isNaN() &&
        !isNaN(n) &&
        !Double.isInfinite(d.doubleValue())) {
      double dv = d.doubleValue();
      ulp2 = 2 * ulp(dv);
    }
    return equalValue(d, n, ulp2);
  }

  public static double ulp(double d) {
    return Double.isInfinite(d) ? 0 : Math.ulp(d);
  }

  public static double ulp(float d) {
    return Double.isInfinite(d) ? 0 : Math.ulp(d);
  }

  public static boolean isNaN(Number n) {
    if (n == null) {
      return false;
    }
    else if (n instanceof Double) {
      return ((Double)n).isNaN();
    }
    else if (n instanceof Float) {
      return ((Float)n).isNaN();
    }
    else {
      return false;
    }
  }

  /**
   * NaN's are equal. Serious floats are compared, taking
   * into account the {@link Math#ulp(float)}.
   */
  public static boolean equalValue(Float f, Number n) {
    if (f == null) {
      return n == null;
    }
    else if (n == null) {
      return false;
    }
    else {
//      assert f1 != null;
//      assert f2 != null;
      if (f.isNaN()) {
        return isNaN(n);
      }
      else if (isNaN(n)) {
        return false;
      }
      else {
        float fv = f.floatValue();
        float nv = n.floatValue();
        if (Float.isInfinite(fv)) {
          if (Float.isInfinite(nv) && signum(fv) == signum(nv)) {
            return true;
          }
          else {
            return false;
          }
        }
        else {
          float delta = Math.abs(fv - nv);
          return delta <= 2 * ulp(fv);
        }
      }
    }
  }

  public static boolean equalValue(BigInteger bi, Number n) {
    return (bi == null) ?
             (n == null) :
             (n != null) && (bi.compareTo(BigInteger.valueOf(n.longValue())) == 0);
  }

  public static boolean equalValue(Long l, Number n) {
    return (l == null) ?
             (n == null) :
             (n != null) && (l.longValue() == n.longValue());
  }

  public static boolean equalValue(Boolean b1, Boolean b2) {
    if (b1 == null) {
      return b2 == null;
    }
    else {
      return b1.equals(b2);
    }
  }

  /**
   * @result equalValue(result, floatObject);
   */
  public static BigDecimal castToBigDecimal(Double doubleObject) {
    if (doubleObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(doubleObject);
    }
  }

  /**
   * @result equalValue(result, floatObject);
   */
  public static BigDecimal castToBigDecimal(Float floatObject) {
    if (floatObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(floatObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static BigDecimal castToBigDecimal(BigInteger biObject) {
    if (biObject == null) {
      return null;
    }
    else {
      return new BigDecimal(biObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static BigDecimal castToBigDecimal(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static BigDecimal castToBigDecimal(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, floatObject);
   */
  public static Double castToDouble(Float floatObject) {
    if (floatObject == null) {
      return null;
    }
    else {
      return Double.valueOf(floatObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static Double castToDouble(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return Double.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static Double castToDouble(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return Double.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static Float castToFloat(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return Float.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   *
   * @mudo is this a range problem?
   */
  public static Float castToFloat(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return Float.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static BigInteger castToBigInteger(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return BigInteger.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static BigInteger castToBigInteger(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return BigInteger.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static Long castToLong(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return Long.valueOf(integerObject);
    }
  }

  /**
   * @pre  doubles != null;
   */
  public static double sum(double... doubles) {
    assert doubles != null;
    Sum sum = new Sum();
    return sum.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double product(double... doubles) {
    assert doubles != null;
    Product product = new Product();
    return product.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double max(double... doubles) {
    assert doubles != null;
    Max max = new Max();
    return max.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double min(double... doubles) {
    assert doubles != null;
    Min min = new Min();
    return min.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double arithmeticMean(double... doubles) {
    assert doubles != null;
    FirstMoment fm = new FirstMoment();
    return fm.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double geometricMean(double... doubles) {
    assert doubles != null;
    GeometricMean gm = new GeometricMean();
    return gm.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double sampleVariance(double... doubles) {
    assert doubles != null;
    Variance v = new Variance(true);
    return v.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double populationVariance(double... doubles) {
    assert doubles != null;
    Variance v = new Variance(false);
    return v.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double sampleStandardDeviation(double... doubles) {
    assert doubles != null;
    StandardDeviation sd = new StandardDeviation(true);
    return sd.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double populationStandardDeviation(double... doubles) {
    assert doubles != null;
    StandardDeviation sd = new StandardDeviation(false);
    return sd.evaluate(doubles);
  }

  /**
   * @pre  doubles != null;
   */
  public static double sampleStandardError(double... doubles) {
    assert doubles != null;
    StandardError se = new StandardError(true);
    double standardError = se.evaluate(doubles);
    return standardError;
  }

  /**
   * @pre  doubles != null;
   */
  public static double populationStandardError(double... doubles) {
    assert doubles != null;
    StandardError se = new StandardError(false);
    double standardError = se.evaluate(doubles);
    return standardError;
  }

  /**
   * @pre  doubles != null;
   */
  public static double sampleGeometricStandardDeviation(double... doubles) {
    assert doubles != null;
    GeometricStandardDeviation gsd = new GeometricStandardDeviation(true);
    gsd.incrementAll(doubles);
    double standardDeviation = gsd.getResult();
    return standardDeviation;
  }

  /**
   * @pre  doubles != null;
   */
  public static double populationGeometricStandardDeviation(double... doubles) {
    assert doubles != null;
    GeometricStandardDeviation gsd = new GeometricStandardDeviation(false);
    gsd.incrementAll(doubles);
    double standardDeviation = gsd.getResult();
    return standardDeviation;
  }
  /**
   * @pre  doubles != null;
   */
  public static double sampleGeometricStandardError(double... doubles) {
    assert doubles != null;
    GeometricStandardError gse = new GeometricStandardError(true);
    gse.incrementAll(doubles);
    double standardError = gse.getResult();
    return standardError;
  }

  /**
   * @pre  doubles != null;
   */
  public static double populationGeometricStandardError(double... doubles) {
    assert doubles != null;
    GeometricStandardError gse = new GeometricStandardError(false);
    gse.incrementAll(doubles);
    double standardError = gse.getResult();
    return standardError;
  }

  /**
   * Return a new array, containing the log-transformed values of the given
   * values.
   *
   * @param   values  The array to transform.
   * @return  values == null ==> result == null;
   * @result  values != null ==> result != null;
   * @result  values != null ==> result.length == values.length;
   * @result  values != null ==>
   *             (forAll int i; 0 <= i < values.length; result[i]=Math.log(values[i]));
   */
  public static double[] logTransform(double[] values) {
    if (values == null) {
      return null;
    }
    double[] logValues = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      logValues[i] = Math.log(values[i]);
    }
    return logValues;
  }

  /**
   * Returns the log with the given base of the given argument.
   * We use the following formula: loga(x) = ln(x) / ln(a).
   */
  public static double log(double base, double argument) {
    return Math.log(argument) / Math.log(base);
  }
}
