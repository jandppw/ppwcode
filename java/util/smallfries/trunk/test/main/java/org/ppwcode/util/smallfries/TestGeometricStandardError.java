/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.util.smallfries;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MathUtil.equalPrimitiveValue;
import static org.ppwcode.util.smallfries_I.MathUtil.geometricMean;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.GeometricStandardError;


@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public class TestGeometricStandardError {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private GeometricStandardError $sampleGeometricStandardError = new GeometricStandardError(true);
  private GeometricStandardError $populationGeometricStandardError = new GeometricStandardError(false);
  private GeometricStandardError $sampleGeometricStandardError2 = new GeometricStandardError(true);
  private GeometricStandardError $populationGeometricStandardError2 = new GeometricStandardError(false);

  @Test
  public void constructor1() {
    GeometricStandardError standardError = new GeometricStandardError();
    assertTrue(standardError.isBiasCorrected());
    assertEquals(0L, standardError.getN());
    assertEquals(Double.NaN, standardError.getResult());
  }

  @Test
  public void constructor2() {
    GeometricStandardError standardError1 = new GeometricStandardError(false);
    assertFalse(standardError1.isBiasCorrected());
    assertEquals(0L, standardError1.getN());
    assertEquals(Double.NaN, standardError1.getResult());
    GeometricStandardError standardError2 = new GeometricStandardError(true);
    assertTrue(standardError2.isBiasCorrected());
    assertEquals(0L, standardError2.getN());
    assertEquals(Double.NaN, standardError2.getResult());
  }

  @Test
  public void getResult() {
    double[] values;
    // []
    values = new double[0];
    checkGetResult(values);
    // [1.1]
    values = new double[] {1.1};
    checkGetResult(values);
    // [1.1, 2.2]
    values = new double[] {1.1, 2.2};
    checkGetResult(values);
    // [1.1, 2.2, 3.3]
    values = new double[] {1.1, 2.2, 3.3};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, Double.NaN]
    values = new double[] {1.1, 2.2, 3.3, Double.NaN};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, 0.0]
    values = new double[] {1.1, 2.2, 3.3, 0.0};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
    checkGetResult(values);
  }

  private void checkGetResult(double[] values) {
    $sampleGeometricStandardError.clear();
    $populationGeometricStandardError.clear();
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals((long)values.length, $sampleGeometricStandardError.getN());
    assertEquals((long)values.length, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
  }

  /**
   * @pre   doubles != null;
   */
  private double geometricStandardErrorByHand(boolean sample, double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else if (doubles.length == 1) {
      return 1.0;
    }
    else if (Util.containsNaN(doubles) || Util.containsNegative(doubles)) {
      return Double.NaN;
    }
    else if (Util.containsZero(doubles) || Util.containsInfinity(doubles)) {
      return Double.POSITIVE_INFINITY;
    }
    else {
      double sum = 0.0;
      double geometricMean = geometricMean(doubles);
      for (int i = 0; i < doubles.length; i++) {
        sum += Math.pow(Math.log(doubles[i])-Math.log(geometricMean), 2);
      }
      double x = sample ? 1 : 0;
      double error = Math.exp(Math.sqrt(sum / ((doubles.length - x)*doubles.length)));
      return error;
    }
  }

  /**
   * @pre   doubles != null;
   */
  private double sampleGeometricStandardErrorByHand(double... doubles) {
    return geometricStandardErrorByHand(true, doubles);
  }

  /**
   * @pre   doubles != null;
   */
  private double populationGeometricStandardErrorByHand(double... doubles) {
    return geometricStandardErrorByHand(false, doubles);
  }

  @Test
  public void evaluate() {
    double[] values;
    double[] values2;
    // []
    values = new double[0];
    values2 = new double[] {5.5, 6.6, 7.7, 8.8};
    checkEvaluate(values, values2, 0);
    // [1.1]
    values = new double[] {1.1};
    values2 = new double[] {6.6, 7.7, 1.1, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2]
    values = new double[] {1.1, 2.2};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3]
    values = new double[] {1.1, 2.2, 3.3};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, Double.NaN]
    values = new double[] {1.1, 2.2, 3.3, Double.NaN};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, Double.NaN, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, -5.0, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, 0.0]
    // @remark  In Variance, there is a difference between getResult() and evaluate for values
    //          containing Double.POSITIVE_INFINITY and Double.NEGATIVE_INFINITY.
    //          getResult() --> Infinity
    //          evaluate()  --> NaN
//    values = new double[] {1.1, 2.2, 3.3, 0.0};
//    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, 0.0, 8.8, 9.9};
//    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
//    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
//    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, Double.POSITIVE_INFINITY, 8.8, 9.9};
//    checkEvaluate(values, values2, 2);
  }

  private void checkEvaluate(double[] values, double[] values2, int begin) {
    Mean m = new Mean();
    double mean = m.evaluate(values);
    $sampleGeometricStandardError2.clear();
    $sampleGeometricStandardError2.incrementAll(values);
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError2.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError2.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError2.getResult(), $sampleGeometricStandardError.evaluate(values2, begin, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError2.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError2.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, begin, values.length)));
    $populationGeometricStandardError2.clear();
    $populationGeometricStandardError2.incrementAll(values);
    assertTrue(equalPrimitiveValue($populationGeometricStandardError2.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError2.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError2.getResult(), $populationGeometricStandardError.evaluate(values2, begin, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError2.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError2.getResult(), $populationGeometricStandardError.evaluate(values2, mean, begin, values.length)));
  }

}

