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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.AFTER;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.AFTER_BEGIN;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.BASIC_COMPOSITIONS;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.BASIC_RELATIONS;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.BEFORE;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.BEFORE_END;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.BEGINS;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.CONCURS_WITH;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.EMPTY;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.ENDS;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.FULL;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.IN;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.VALUES;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.and;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.compose;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.min;
import static org.ppwcode.value_III.time.interval.TimePointIntervalRelation.or;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class TimePointIntervalRelationTest {

  public final static TimePointIntervalRelation[] values() {
    return VALUES;
  }

  @Test
  public void testVALUES() {
    assertNotNull(VALUES);
    for (int i = 0; i < VALUES.length; i++) {
      TimePointIntervalRelation ar = VALUES[i];
      assertNotNull(ar);
      assertEquals(i, ar.hashCode());
      for (int j = i + 1; j < VALUES.length; j++) {
        assertNotSame(ar, VALUES[j]);
      }
    }
  }

  @SuppressWarnings("unused")
  private String fullBitPattern(TimePointIntervalRelation ar) {
    int bitpattern = ar.hashCode();
    String bitString = Integer.toBinaryString(bitpattern);
    while (bitString.length() < 5) {
      bitString = "0" + bitString;
    }
    return bitString;
  }

  @Test
  public void testEMPTY() {
    for (TimePointIntervalRelation br : BASIC_RELATIONS) {
      assertFalse(EMPTY.impliedBy(br));
    }
  }

  @Test
  public void testBASIC_RELATIONS() {
    for (TimePointIntervalRelation br : BASIC_RELATIONS) {
      assertEquals(br, BASIC_RELATIONS[br.basicRelationOrdinal()]);
    }
    assertEquals(BEFORE, BASIC_RELATIONS[0]);
    assertEquals(BEGINS, BASIC_RELATIONS[1]);
    assertEquals(IN, BASIC_RELATIONS[2]);
    assertEquals(ENDS, BASIC_RELATIONS[3]);
    assertEquals(AFTER, BASIC_RELATIONS[4]);
  }

  @Test
  public void testSecondaryRelations() {
    assertEquals(or(BEGINS, IN), CONCURS_WITH);
    assertEquals(or(IN, ENDS, AFTER), AFTER_BEGIN);
    assertEquals(or(BEFORE, BEGINS, IN), BEFORE_END);
  }

  @Test
  public void testOr0() {
    for (TimePointIntervalRelation ar1 : BASIC_RELATIONS) {
      for (TimePointIntervalRelation ar2 : BASIC_RELATIONS) {
        TimePointIntervalRelation result = or(ar1, ar2);
        validateOr(ar1, ar2, result);
      }
    }
  }

  private void validateOr(TimePointIntervalRelation ar1, TimePointIntervalRelation ar2, TimePointIntervalRelation result) {
    for (TimePointIntervalRelation br : BASIC_RELATIONS) {
      assertTrue(ar1.impliedBy(br) || ar2.impliedBy(br) ? result.impliedBy(br) : true);
      assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) || ar2.impliedBy(br) : true);
    }
  }

  @Test
  public void testOr1() {
    TimePointIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimePointIntervalRelation ar1 : subjects) {
      for (TimePointIntervalRelation ar2 : subjects) {
        TimePointIntervalRelation result = or(ar1, ar2);
        validateOr(ar1, ar2, result);
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 100000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
      }
    }
  }

  @Test
  public void testOr2() {
    TimePointIntervalRelation result = or(VALUES);
    assertEquals(FULL, result);
  }

  @Test
  public void testAnd0() {
    for (TimePointIntervalRelation ar1 : BASIC_RELATIONS) {
      for (TimePointIntervalRelation ar2 : BASIC_RELATIONS) {
        TimePointIntervalRelation result = and(ar1, ar2);
        validateAnd(ar1, ar2, result);
      }
    }
  }

  private void validateAnd(TimePointIntervalRelation ar1, TimePointIntervalRelation ar2, TimePointIntervalRelation result) {
    for (TimePointIntervalRelation br : BASIC_RELATIONS) {
      assertTrue(ar1.impliedBy(br) && ar2.impliedBy(br) ? result.impliedBy(br) : true);
      assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) && ar2.impliedBy(br) : true);
    }
  }

  @Test
  public void testAnd1() {
    TimePointIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimePointIntervalRelation ar1 : subjects) {
      for (TimePointIntervalRelation ar2 : subjects) {
        TimePointIntervalRelation result = and(ar1, ar2);
        validateAnd(ar1, ar2, result);
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 100000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
      }
    }
  }

  @Test
  public void testAnd2() {
    TimePointIntervalRelation result = and(VALUES);
    assertEquals(EMPTY, result);
  }

  @Test
  public void testMin0() {
    for (TimePointIntervalRelation base : BASIC_RELATIONS) {
      for (TimePointIntervalRelation term : BASIC_RELATIONS) {
        TimePointIntervalRelation result = min(base, term);
        validateMin(base, term, result);
      }
    }
  }

  private void validateMin(TimePointIntervalRelation base, TimePointIntervalRelation term, TimePointIntervalRelation result) {
    for (TimePointIntervalRelation br : BASIC_RELATIONS) {
      assertTrue(br.implies(result) ? br.implies(base) && (! br.implies(term)) : true);
      assertTrue(br.implies(base) && (! br.implies(term)) ? br.implies(result) : true);
    }
  }

  @Test
  public void testMin1() {
    TimePointIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimePointIntervalRelation base : subjects) {
      for (TimePointIntervalRelation term : subjects) {
        TimePointIntervalRelation result = min(base, term);
        validateMin(base, term, result);
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 100000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
      }
    }
  }

  @Test
  public void testMin2() {
    TimePointIntervalRelation result = min(FULL, EMPTY);
    assertEquals(FULL, result);
  }

  @Test
  public void testMin3() {
    TimePointIntervalRelation result = min(EMPTY, FULL);
    assertEquals(EMPTY, result);
  }

  @Test
  public void testCompose0() {
    for (TimePointIntervalRelation tpir : BASIC_RELATIONS) {
      for (TimeIntervalRelation ar : TimeIntervalRelation.BASIC_RELATIONS) {
        TimePointIntervalRelation result = compose(tpir, ar);
        validateCompose(tpir, ar, result);
      }
    }
  }

  private void validateCompose(TimePointIntervalRelation tpir, TimeIntervalRelation ar, TimePointIntervalRelation result) {
    for (TimePointIntervalRelation bTpir : BASIC_RELATIONS) {
      for (TimeIntervalRelation bAr : TimeIntervalRelation.BASIC_RELATIONS) {
        assertTrue(bTpir.implies(tpir) && bAr.implies(ar) ?
                     result.impliedBy(BASIC_COMPOSITIONS[bTpir.basicRelationOrdinal()][bAr.basicRelationOrdinal()]) :
                     true);
      }
    }
  }

  @Test
  public void testCompose1() {
    TimePointIntervalRelation[] subjects1 = values();
    TimeIntervalRelation[] subjects2 = TimeIntervalRelation.VALUES;
    long total = subjects1.length * subjects2.length;
    System.out.println("Starting test over " + total + " cases; warning: this method is O(n3)");
    long count = 0;
    for (TimePointIntervalRelation tpir : subjects1) {
      for (TimeIntervalRelation ar : subjects2) {
        TimePointIntervalRelation result = compose(tpir, ar);
        validateCompose(tpir, ar, result);
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 10000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
      }
    }
  }


  @Test
  public void testTimePointIntervalRelation() {
    // MUDO unit test missing
  }

  @Test
  public void testHashCode() {
    for (TimePointIntervalRelation ar : VALUES) {
      int result = ar.hashCode();
      assertEquals(ar, VALUES[result]);
    }
  }

  @Test
  public void testBasicRelationOrdinal() {
    for (TimePointIntervalRelation ar : BASIC_RELATIONS) {
      int result = ar.basicRelationOrdinal();
      assertTrue(result >= 0);
      assertTrue(result < 5);
    }
  }

  @Test
  public void testIsBasic() {
    List<TimePointIntervalRelation> basicRelations = Arrays.asList(BASIC_RELATIONS);
    for (TimePointIntervalRelation ar : VALUES) {
      boolean result = ar.isBasic();
      assertEquals(basicRelations.contains(ar), result);
    }
  }

  @Test
  public void testUncertainty() {
    for (TimePointIntervalRelation ar : VALUES) {
      float result = ar.uncertainty();
//      System.out.println(ar + "  -- " + result);
      if (ar != EMPTY) {
        int count = 0;
        for (TimePointIntervalRelation br : BASIC_RELATIONS) {
          if (br.implies(ar)) {
            count++;
          }
        }
        double expected = (float)(count - 1) / 4;
        assertEquals(expected, result, 1E-5);
      }
      else {
        assertEquals(Float.NaN, result, 0);
      }
    }
  }

  @Test
  public void testComplement1() {
    for (TimePointIntervalRelation ar : VALUES) {
      TimePointIntervalRelation resultA = ar.complement();
      for (TimePointIntervalRelation br : BASIC_RELATIONS) {
        assertTrue(ar.impliedBy(br) ? ! resultA.impliedBy(br) : true);
        assertTrue(! resultA.impliedBy(br) ? ar.impliedBy(br) : true);
        assertTrue(! ar.impliedBy(br) ? resultA.impliedBy(br) : true);
        assertTrue(resultA.impliedBy(br) ? ! ar.impliedBy(br) : true);
      }
      TimePointIntervalRelation resultB = resultA.complement();
      assertEquals(ar, resultB);
    }
  }

  @Test
  public void testComplement2() {
    assertEquals(EMPTY, FULL.complement());
    assertEquals(FULL, EMPTY.complement());
  }

  @Test
  public void testImpliedBy() {
    TimePointIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimePointIntervalRelation subject : values()) {
      assertEquals(true, subject.impliedBy(subject));
      if (subject.isBasic()) {
        for (TimePointIntervalRelation br : BASIC_RELATIONS) {
          assertTrue(br != subject ? ! subject.impliedBy(br) : true);
        }
      }
      for (TimePointIntervalRelation gr : values()) {
        boolean result = subject.impliedBy(gr);
        boolean expected = true;
        for (TimePointIntervalRelation br : BASIC_RELATIONS) {
          if (gr.impliedBy(br) && ! subject.impliedBy(br)) {
            expected = false;
            break;
          }
        }
        assertEquals(expected, result);
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 100000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
      }
    }
  }

  @Test
  public void testImplies() {
    TimePointIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimePointIntervalRelation subject : values()) {
      for (TimePointIntervalRelation gr : values()) {
        boolean result = subject.implies(gr);
        assertEquals(gr.impliedBy(subject), result);
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 100000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
      }
    }
  }

  @Test
  public void testToString() {
    for (TimePointIntervalRelation ar : VALUES) {
      String result = ar.toString();
      assertNotNull(result);
//      System.out.println(result);
    }
  }

}

