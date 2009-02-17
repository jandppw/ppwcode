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
import static org.ppwcode.util.smallfries_I.MathUtil.arithmeticMean;
import static org.ppwcode.util.smallfries_I.MathUtil.equalPrimitiveValue;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.StandardError;


@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public class TestStandardError {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private StandardError $sampleStandardError = new StandardError(true);
  private StandardError $populationStandardError = new StandardError(false);
  private StandardError $sampleStandardError2 = new StandardError(true);
  private StandardError $populationStandardError2 = new StandardError(false);

  @Test
  public void constructor1() {
    StandardError standardError = new StandardError();
    assertTrue(standardError.isBiasCorrected());
    assertEquals(0L, standardError.getN());
    assertEquals(Double.NaN, standardError.getResult());
  }

  @Test
  public void constructor2() {
    StandardError standardError1 = new StandardError(false);
    assertFalse(standardError1.isBiasCorrected());
    assertEquals(0L, standardError1.getN());
    assertEquals(Double.NaN, standardError1.getResult());
    StandardError standardError2 = new StandardError(true);
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
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, 0.0]
    values = new double[] {1.1, 2.2, 3.3, 0.0};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
    checkGetResult(values);
    // [1.1, 2.2, 3.3, Double.NaN]
    values = new double[] {1.1, 2.2, 3.3, Double.NaN};
    checkGetResult(values);
  }

  private void checkGetResult(double[] values) {
    $sampleStandardError.clear();
    $populationStandardError.clear();
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals((long)values.length, $sampleStandardError.getN());
    assertEquals((long)values.length, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
  }

  /**
   * @pre   doubles != null;
   */
  private double standardErrorByHand(boolean b, double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else if (doubles.length == 1) {
      return 0.0;
    }
    else if (Util.containsNaN(doubles)) {
      return Double.NaN;
    }
    else if (Util.containsInfinity(doubles)) {
      return Double.POSITIVE_INFINITY;
    }
    else {
      double sum = 0.0;
      double mean = arithmeticMean(doubles);
      for (int i = 0; i < doubles.length; i++) {
        sum += Math.pow(doubles[i]-mean, 2);
      }
      double x = b ? 1 : 0; // sample: n - 1, population: n
      double error = Math.sqrt(sum / ((doubles.length - x)*doubles.length));
      return error;
    }
  }

  /**
   * @pre   doubles != null;
   */
  private double sampleStandardErrorByHand(double... doubles) {
    return standardErrorByHand(true, doubles);
  }

  /**
   * @pre   doubles != null;
   */
  private double populationStandardErrorByHand(double... doubles) {
    return standardErrorByHand(false, doubles);
  }

  @Test
  public void evaluate() {
    double[] values;
    double[] values2;
    // []
    values = new double[0];
    values2 = new double[] {5.5, 6.6, 7.7, 8.8};
    checkEvaluate(values, values2, 2);
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
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, -5.0, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, 0.0]
    values = new double[] {1.1, 2.2, 3.3, 0.0};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, 0.0, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
    // @remark  In Variance, there is a difference between getResult() and evaluate for values
    //          containing Double.POSITIVE_INFINITY and Double.NEGATIVE_INFINITY.
    //          getResult() --> Infinity
    //          evaluate()  --> NaN
//    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
//    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, Double.POSITIVE_INFINITY, 8.8, 9.9};
//    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
//    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
//    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY, 8.8, 9.9};
//    checkEvaluate(values, values2, 2);
    // [1.1, 2.2, 3.3, Double.NaN]
    values = new double[] {1.1, 2.2, 3.3, Double.NaN};
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, Double.NaN, 8.8, 9.9};
    checkEvaluate(values, values2, 2);
  }

  private void checkEvaluate(double[] values, double[] values2, int begin) {
    Mean m = new Mean();
    double mean = m.evaluate(values);
    $sampleStandardError2.clear();
    $sampleStandardError2.incrementAll(values);
    assertTrue(equalPrimitiveValue($sampleStandardError2.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError2.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError2.getResult(), $sampleStandardError.evaluate(values2, begin, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError2.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError2.getResult(), $sampleStandardError.evaluate(values2, mean, begin, values.length)));
    $populationStandardError2.clear();
    $populationStandardError2.incrementAll(values);
    assertTrue(equalPrimitiveValue($populationStandardError2.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError2.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError2.getResult(), $populationStandardError.evaluate(values2, begin, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError2.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError2.getResult(), $populationStandardError.evaluate(values2, mean, begin, values.length)));
  }

}

