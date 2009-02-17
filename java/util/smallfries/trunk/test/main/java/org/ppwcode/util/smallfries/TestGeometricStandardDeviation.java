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

import org.apache.commons.math.stat.descriptive.moment.GeometricMean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.GeometricStandardDeviation;


@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestGeometricStandardDeviation {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private GeometricStandardDeviation $sampleGeometricStandardDeviation = new GeometricStandardDeviation(true);
  private GeometricStandardDeviation $populationGeometricStandardDeviation = new GeometricStandardDeviation(false);
  private GeometricStandardDeviation $sampleGeometricStandardDeviation2 = new GeometricStandardDeviation(true);
  private GeometricStandardDeviation $populationGeometricStandardDeviation2 = new GeometricStandardDeviation(false);

  @Test
  public void constructor1() {
    GeometricStandardDeviation standardDeviation = new GeometricStandardDeviation();
    assertTrue(standardDeviation.isBiasCorrected());
    assertEquals(0L, standardDeviation.getN());
    assertEquals(Double.NaN, standardDeviation.getResult());
  }

  @Test
  public void constructor2() {
    GeometricStandardDeviation standardDeviation1 = new GeometricStandardDeviation(false);
    assertFalse(standardDeviation1.isBiasCorrected());
    assertEquals(0L, standardDeviation1.getN());
    assertEquals(Double.NaN, standardDeviation1.getResult());
    GeometricStandardDeviation standardDeviation2 = new GeometricStandardDeviation(true);
    assertTrue(standardDeviation2.isBiasCorrected());
    assertEquals(0L, standardDeviation2.getN());
    assertEquals(Double.NaN, standardDeviation2.getResult());
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
    $sampleGeometricStandardDeviation.clear();
    $populationGeometricStandardDeviation.clear();
    $sampleGeometricStandardDeviation.incrementAll(values);
    $populationGeometricStandardDeviation.incrementAll(values);
    assertEquals((long)values.length, $sampleGeometricStandardDeviation.getN());
    assertEquals((long)values.length, $populationGeometricStandardDeviation.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardDeviationByHand(values), $sampleGeometricStandardDeviation.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardDeviationByHand(values), $populationGeometricStandardDeviation.getResult()));
  }

  /**
   * @pre   doubles != null;
   */
  private double geometricStandardDeviationByHand(boolean sample, double... doubles) {
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
      double deviation = Math.exp(Math.sqrt(sum / (doubles.length - x)));
      return deviation;
    }
  }

  /**
   * @pre   doubles != null;
   */
  private double sampleGeometricStandardDeviationByHand(double... doubles) {
    return geometricStandardDeviationByHand(true, doubles);
  }

  /**
   * @pre   doubles != null;
   */
  private double populationGeometricStandardDeviationByHand(double... doubles) {
    return geometricStandardDeviationByHand(false, doubles);
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
    GeometricMean gm = new GeometricMean();
    double geomMean = Math.log(gm.evaluate(values));
    $sampleGeometricStandardDeviation2.clear();
    $sampleGeometricStandardDeviation2.incrementAll(values);
    assertTrue(equalPrimitiveValue($sampleGeometricStandardDeviation2.getResult(), $sampleGeometricStandardDeviation.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardDeviation2.getResult(), $sampleGeometricStandardDeviation.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardDeviation2.getResult(), $sampleGeometricStandardDeviation.evaluate(values2, begin, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardDeviation2.getResult(), $sampleGeometricStandardDeviation.evaluate(values, geomMean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardDeviation2.getResult(), $sampleGeometricStandardDeviation.evaluate(values2, geomMean, begin, values.length)));
    $populationGeometricStandardDeviation2.clear();
    $populationGeometricStandardDeviation2.incrementAll(values);
    assertTrue(equalPrimitiveValue($populationGeometricStandardDeviation2.getResult(), $populationGeometricStandardDeviation.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardDeviation2.getResult(), $populationGeometricStandardDeviation.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardDeviation2.getResult(), $populationGeometricStandardDeviation.evaluate(values2, begin, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardDeviation2.getResult(), $populationGeometricStandardDeviation.evaluate(values, geomMean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardDeviation2.getResult(), $populationGeometricStandardDeviation.evaluate(values2, geomMean, begin, values.length)));
  }

}

