/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.util.smallfries;


import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MathUtil.arithmeticMean;
import static org.ppwcode.util.smallfries_I.MathUtil.castToDouble;
import static org.ppwcode.util.smallfries_I.MathUtil.castToFloat;
import static org.ppwcode.util.smallfries_I.MathUtil.castToLong;
import static org.ppwcode.util.smallfries_I.MathUtil.equalPrimitiveValue;
import static org.ppwcode.util.smallfries_I.MathUtil.equalValue;
import static org.ppwcode.util.smallfries_I.MathUtil.geometricMean;
import static org.ppwcode.util.smallfries_I.MathUtil.logTransform;
import static org.ppwcode.util.smallfries_I.MathUtil.populationStandardDeviation;
import static org.ppwcode.util.smallfries_I.MathUtil.populationVariance;
import static org.ppwcode.util.smallfries_I.MathUtil.sampleStandardDeviation;
import static org.ppwcode.util.smallfries_I.MathUtil.sampleVariance;
import static org.ppwcode.util.smallfries_I.MathUtil.ulp;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.MathUtil;


@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public class TestMathUtil {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ds = new LinkedList<Double>();
    ds.add(null);
    ds.add(Double.NEGATIVE_INFINITY);
    ds.add(Double.MIN_VALUE);
    ds.add(Double.valueOf(-7));
    ds.add(Double.valueOf(-1));
    ds.add(Double.valueOf(0));
    ds.add(Double.valueOf(1));
    ds.add(Double.valueOf(1.5d));
    ds.add(Double.valueOf(13d/7d));
    ds.add(Math.E);
    ds.add(Math.PI);
    ds.add(Double.valueOf(7));
    ds.add(Double.MAX_VALUE);
    ds.add(Double.POSITIVE_INFINITY);
    ds.add(Double.NaN);
    fs = new LinkedList<Float>();
    fs.add(null);
    fs.add(Float.NEGATIVE_INFINITY);
    fs.add(Float.MIN_VALUE);
    fs.add(Float.valueOf(-7));
    fs.add(Float.valueOf(-1));
    fs.add(Float.valueOf(0));
    fs.add(Float.valueOf(1));
    fs.add(Float.valueOf(1.5f));
    fs.add(Float.valueOf(13f/7f));
    fs.add((float)Math.E);
    fs.add((float)Math.PI);
    fs.add(Float.valueOf(7));
    fs.add(Float.MAX_VALUE);
    fs.add(Float.POSITIVE_INFINITY);
    fs.add(Float.NaN);
    ls = new LinkedList<Long>();
    ls.add(null);
    ls.add(Long.MIN_VALUE);
    ls.add(Long.valueOf(-7));
    ls.add(Long.valueOf(-1));
    ls.add(Long.valueOf(0));
    ls.add(Long.valueOf(1));
    ls.add(Long.valueOf(7));
    ls.add(Long.MAX_VALUE);
    is = new LinkedList<Integer>();
    is.add(null);
    is.add(Integer.MIN_VALUE);
    is.add(Integer.valueOf(-7));
    is.add(Integer.valueOf(-1));
    is.add(Integer.valueOf(0));
    is.add(Integer.valueOf(1));
    is.add(Integer.valueOf(7));
    is.add(Integer.MAX_VALUE);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    ds = null;
    fs = null;
    ls = null;
    is = null;
  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private static List<Double> ds;
  private static List<Float> fs;
  private static List<Long> ls;
  private static List<Integer> is;

  @Test
  public void testPower() {
    for (Double d : ds) {
      if (d != null) {
        testPower(d, 0);
        testPower(d, 1);
        testPower(d, 2);
        testPower(d, 3);
        testPower(d, 4);
        testPower(d, 7);
        testPower(d, 13);
        testPower(d, 138);
      }
    }
  }

  private void testPower(double d, int e) {
//    System.out.println("d: " + d + "; e: " + e + "; Naive: " + naivePower(d, e) + "; power: " + MathUtil.power(d, e));
    double expected = naivePower(d, e);
    assertTrue(MathUtil.equalPrimitiveValue(expected, MathUtil.power(d, e), Math.ulp(expected) * 32)); // TODO this is a high error rate! why?
  }

  private double naivePower(double d, int e) {
    double result = 1;
    while (e > 0) {
      result *= d;
      e--;
    }
    return result;
  }

  @Test
  public void testEqualValueDoubleDouble() {
    for (Double d1 : ds) {
      for (Double d2 : ds) {
        testEqualValueDoubleNumber(d1, d2, equalValue(d1, d2));
      }
    }
  }

  @Test
  public void testEqualValueDoubleFloat() {
    for (Double d : ds) {
      for (Float f : fs) {
        testEqualValueDoubleNumber(d, f, equalValue(d, f));
      }
    }
  }

  @Test
  public void testEqualValueDoubleLong() {
    for (Double d : ds) {
      for (Long l : ls) {
        testEqualValueDoubleNumber(d, l, equalValue(d, l));
      }
    }
  }

  @Test
  public void testEqualValueDoubleInteger() {
    for (Double d : ds) {
      for (Integer i : is) {
        testEqualValueDoubleNumber(d, i, equalValue(d, i));
      }
    }
  }

  private void testEqualValueDoubleNumber(Double d, Number n, boolean result) {
    if (d == null) {
      assertEquals(n == null, result);
    }
    else if (n == null) {
      assertEquals(false, result);
    }
    else {
      if (d.isNaN()) {
        assertEquals(isNaN(n), result);
      }
      else if (isNaN(n)) {
        assertEquals(false, result);
      }
      else {
        double dValue = d.doubleValue();
        double nValue = n.doubleValue();
        double delta = ! Double.isInfinite(dValue) ?
                         abs(dValue - nValue) :
                         (Double.isInfinite(nValue) && (Math.signum(dValue) == Math.signum(nValue))) ?
                           0 :
                           Double.POSITIVE_INFINITY;
        if (delta <= 2 * ulp(dValue) != result) {
          System.out.println("d = " + d + "; n = " + n + "; result = " + result + "; ulp: " + ulp(dValue) + "; delta: " + delta + "; delta <= ulp(dValue): " + (delta <= ulp(dValue)));
        }
        assertEquals(delta <= 2 * ulp(dValue), result);
      }
    }
  }

  private boolean isNaN(Number n) {
    if (n == null) {
      return false;
    }
    if (n instanceof Double) {
      return ((Double)n).isNaN();
    }
    else if (n instanceof Float) {
      return ((Float)n).isNaN();
    }
    else {
      return false;
    }
  }

  @Test
  public void testEqualValueFloatFloat() {
    for (Float f1 : fs) {
      for (Float f2 : fs) {
        testEqualValueFloatNumber(f1, f2, equalValue(f1, f2));
      }
    }
  }

  @Test
  public void testEqualValueFloatLong() {
    for (Float f : fs) {
      for (Long l : ls) {
        testEqualValueFloatNumber(f, l, equalValue(f, l));
      }
    }
  }

  @Test
  public void testEqualValueFloatInteger() {
    for (Float f : fs) {
      for (Integer i : is) {
        testEqualValueFloatNumber(f, i, equalValue(f, i));
      }
    }
  }

  private void testEqualValueFloatNumber(Float f, Number n, boolean result) {
    if (f == null) {
      assertEquals(n == null, result);
    }
    else if (n == null) {
      assertEquals(false, result);
    }
    else {
      if (f.isNaN()) {
        assertEquals(isNaN(n), result);
      }
      else if (isNaN(n)) {
        assertEquals(false, result);
      }
      else {
        float fValue = f.floatValue();
        float nValue = n.floatValue();
        float delta = ! Float.isInfinite(fValue) ?
                         abs(fValue - nValue) :
                         (Float.isInfinite(nValue) && (Math.signum(fValue) == Math.signum(nValue))) ?
                           0 :
                           Float.POSITIVE_INFINITY;
        if (delta <= 2 * ulp(fValue) != result) {
          System.out.println("f = " + f + "; n = " + n + "; result = " + result + "; ulp: " + ulp(fValue) + "; delta: " + delta + "; delta <= ulp(fValue): " + (delta <= ulp(fValue)));
        }
        assertEquals(delta <= 2 * ulp(fValue), result);
      }
    }
  }

  @Test
  public void testEqualValueLongInteger() {
    for (Long l : ls) {
      for (Integer i : is) {
        testEqualValueLongNumber(l, i, equalValue(l, i));
      }
    }
  }

  private void testEqualValueLongNumber(Long l, Number n, boolean result) {
    if (l == null) {
      assertEquals(n == null, result);
    }
    else if (n == null) {
      assertEquals(false, result);
    }
    else {
      long lValue = l.longValue();
      long nValue = n.longValue();
      assertEquals(lValue == nValue, result);
    }
  }

  @Test
  public void testCastToDoubleFloat() {
    for (Float f : fs) {
      Double result = castToDouble(f);
      assertTrue(equalValue(result, f));
    }
  }

  @Test
  public void testCastToDoubleLong() {
    for (Long l : ls) {
      Double result = castToDouble(l);
      assertTrue(equalValue(result, l));
    }
  }

  @Test
  public void testCastToDoubleInteger() {
    for (Integer i : is) {
      Double result = castToDouble(i);
      assertTrue(equalValue(result, i));
    }
  }

  @Test
  public void testCastToFloatLong() {
    for (Long l : ls) {
      Float result = castToFloat(l);
      assertTrue(equalValue(result, l));
    }
  }

  @Test
  public void testCastToFloatInteger() {
    for (Integer i : is) {
      Float result = castToFloat(i);
      assertTrue(equalValue(result, i));
    }
  }

  @Test
  public void testCastToLong() {
    for (Integer i : is) {
      Long result = castToLong(i);
      assertTrue(equalValue(result, i));
    }
  }

  @Test
  public void testArithmeticMean() {
    double[] values = new double[0];
    assertTrue(equalPrimitiveValue(arithmeticMeanByHand(values), arithmeticMean(values)));
    values = new double[]{1.1};
    assertTrue(equalPrimitiveValue(arithmeticMeanByHand(values), arithmeticMean(values)));
    values = new double[]{1.1, 2.2};
    assertTrue(equalPrimitiveValue(arithmeticMeanByHand(values), arithmeticMean(values)));
    values = new double[]{1.1, 2.2, 3.3};
    assertTrue(equalPrimitiveValue(arithmeticMeanByHand(values), arithmeticMean(values)));
    values = new double[]{1.1, 2.2, 3.3, 4.4};
    assertTrue(equalPrimitiveValue(arithmeticMeanByHand(values), arithmeticMean(values)));
  }

  /**
   * @pre   doubles != null;
   */
  private double arithmeticMeanByHand(double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else {
      double sum = 0.0;
      for (int i = 0; i < doubles.length; i++) {
        sum += doubles[i];
      }
      return sum / doubles.length;
    }
  }

  @Test
  public void testGeometricMean() {
    double[] values = new double[0];
    assertTrue(equalPrimitiveValue(geometricMeanByHand(values), geometricMean(values)));
    values = new double[]{1.1};
    assertTrue(equalPrimitiveValue(geometricMeanByHand(values), geometricMean(values)));
    values = new double[]{1.1, 2.2};
    assertTrue(equalPrimitiveValue(geometricMeanByHand(values), geometricMean(values)));
    values = new double[]{1.1, 2.2, 3.3};
    assertTrue(equalPrimitiveValue(geometricMeanByHand(values), geometricMean(values)));
    values = new double[]{1.1, 2.2, 3.3, 4.4};
    assertTrue(equalPrimitiveValue(geometricMeanByHand(values), geometricMean(values)));
  }

  /**
   * @pre   doubles != null;
   */
  private double geometricMeanByHand(double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else {
      double product = 1.0;
      for (int i = 0; i < doubles.length; i++) {
        product *= doubles[i];
      }
      return Math.pow(product, 1.0 / doubles.length);
    }
  }

  @Test
  public void testSampleVariance() {
    double[] values = new double[0];
    assertTrue(equalPrimitiveValue(sampleVarianceByHand(values), sampleVariance(values)));
    values = new double[]{1.1};
    assertTrue(equalPrimitiveValue(sampleVarianceByHand(values), sampleVariance(values)));
    values = new double[]{1.1, 2.2};
    assertTrue(equalPrimitiveValue(sampleVarianceByHand(values), sampleVariance(values)));
    values = new double[]{1.1, 2.2, 3.3};
    assertTrue(equalPrimitiveValue(sampleVariance(values), sampleVariance(values)));
    values = new double[]{1.1, 2.2, 3.3, 4.4};
    assertTrue(equalPrimitiveValue(sampleVariance(values), sampleVariance(values)));
  }

  /**
   * @pre   doubles != null;
   */
  private double varianceByHand(boolean sample, double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else if (doubles.length == 1) {
      return 0.0;
    }
    else {
      double sum = 0.0;
      double mean = arithmeticMean(doubles);
      for (int i = 0; i < doubles.length; i++) {
        sum += Math.pow(doubles[i]-mean, 2);
      }
      double x = sample ? 1 : 0;
      double variance = sum / (doubles.length - x);
      return variance;
    }
  }

  /**
   * @pre   doubles != null;
   */
  private double sampleVarianceByHand(double... doubles) {
    return varianceByHand(true, doubles);
  }

  @Test
  public void testPopulationVariance() {
    double[] values = new double[0];
    assertTrue(equalPrimitiveValue(populationVarianceByHand(values), populationVariance(values)));
    values = new double[]{1.1};
    assertTrue(equalPrimitiveValue(populationVarianceByHand(values), populationVariance(values)));
    values = new double[]{1.1, 2.2};
    assertTrue(equalPrimitiveValue(populationVarianceByHand(values), populationVariance(values)));
    values = new double[]{1.1, 2.2, 3.3};
    assertTrue(equalPrimitiveValue(populationVarianceByHand(values), populationVariance(values)));
    values = new double[]{1.1, 2.2, 3.3, 4.4};
    assertTrue(equalPrimitiveValue(populationVarianceByHand(values), populationVariance(values)));
  }

  /**
   * @pre   doubles != null;
   */
  private double populationVarianceByHand(double... doubles) {
    return varianceByHand(false, doubles);
  }

  @Test
  public void testSampleStandardDeviation() {
    double[] values = new double[0];
    assertTrue(equalPrimitiveValue(sampleStandardDeviationByHand(values), sampleStandardDeviation(values)));
    values = new double[]{1.1};
    assertTrue(equalPrimitiveValue(sampleStandardDeviationByHand(values), sampleStandardDeviation(values)));
    values = new double[]{1.1, 2.2};
    assertTrue(equalPrimitiveValue(sampleStandardDeviationByHand(values), sampleStandardDeviation(values)));
    values = new double[]{1.1, 2.2, 3.3};
    assertTrue(equalPrimitiveValue(sampleStandardDeviationByHand(values), sampleStandardDeviation(values)));
    values = new double[]{1.1, 2.2, 3.3, 4.4};
    assertTrue(equalPrimitiveValue(sampleStandardDeviationByHand(values), sampleStandardDeviation(values)));
  }

  /**
   * @pre   doubles != null;
   */
  private double standardDeviationByHand(boolean sample, double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else if (doubles.length == 1) {
      return 0.0;
    }
    else {
      double sum = 0.0;
      double mean = arithmeticMean(doubles);
      for (int i = 0; i < doubles.length; i++) {
        sum += Math.pow(doubles[i]-mean, 2);
      }
      double x = sample ? 1 : 0;
      double deviation = Math.sqrt(sum / (doubles.length - x));
      return deviation;
    }
  }

  /**
   * @pre   doubles != null;
   */
  private double sampleStandardDeviationByHand(double... doubles) {
    return standardDeviationByHand(true, doubles);
  }

  @Test
  public void testPopulationStandardDeviation() {
    double[] values = new double[0];
    assertTrue(equalPrimitiveValue(populationStandardDeviationByHand(values), populationStandardDeviation(values)));
    values = new double[]{1.1};
    assertTrue(equalPrimitiveValue(populationStandardDeviationByHand(values), populationStandardDeviation(values)));
    values = new double[]{1.1, 2.2};
    assertTrue(equalPrimitiveValue(populationStandardDeviationByHand(values), populationStandardDeviation(values)));
    values = new double[]{1.1, 2.2, 3.3};
    assertTrue(equalPrimitiveValue(populationStandardDeviationByHand(values), populationStandardDeviation(values)));
    values = new double[]{1.1, 2.2, 3.3, 4.4};
    assertTrue(equalPrimitiveValue(populationStandardDeviationByHand(values), populationStandardDeviation(values)));
  }

  /**
   * @pre   doubles != null;
   */
  private double populationStandardDeviationByHand(double... doubles) {
    return standardDeviationByHand(false, doubles);
  }

  @Test
  public void testLogTransform() {
    double[] values = null;
    assertNull(logTransform(values));
    values = new double[] {1.0, 2.0, 3.0, 4.0};
    double[] logValues = logTransform(values);
    assertNotNull(logValues);
    assertEquals(values.length, logValues.length);
    for (int i = 0; i < logValues.length; i++) {
      assertEquals(Math.log(values[i]), logValues[i]);
    }
  }
}

