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
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.BASIC_RELATIONS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.BEGINS_EARLIER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.BEGINS_EARLIER_AND_ENDS_EARLIER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.BEGINS_IN;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.BEGINS_LATER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.BEGINS_LATER_AND_ENDS_LATER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.BEGIN_TOGETHER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.CONCURS_WITH;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.CONTAINS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.CONTAINS_BEGIN;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.CONTAINS_END;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.DURING;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.EMPTY;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.ENDS_EARLIER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.ENDS_IN;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.ENDS_LATER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.END_TOGETHER;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.EQUALS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.FINISHED_BY;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.FINISHES;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.FULL;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.MEETS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.MET_BY;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.OVERLAPPED_BY;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.OVERLAPS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.PRECEDED_BY;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.PRECEDES;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.STARTED_BY;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.STARTS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.VALUES;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.and;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.compose;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.min;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.or;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;


public class TimeIntervalRelationTest {

  public final static boolean FULL_TESTS = false;

  public final static int NR_OF_RANDOM_VALUES = 300;

  public final static TimeIntervalRelation[] values() {
    if (FULL_TESTS) {
      return VALUES;
    }
    else {
      TimeIntervalRelation[] result = new TimeIntervalRelation[NR_OF_RANDOM_VALUES];
      result[0] = EMPTY;
      for (int i = 1; i < (BASIC_RELATIONS.length + 1); i++) {
        result[i] = BASIC_RELATIONS[i - 1];
      }
      result[14] = FULL;
      for (int i = 15; i < NR_OF_RANDOM_VALUES; i++) {
        Random r = new Random();
        int index = r.nextInt(TimeIntervalRelation.NR_OF_RELATIONS);
        result[i] = VALUES[index];
      }
      return result;
    }
  }

  @Test
  public void testVALUES() {
    assertNotNull(VALUES);
    for (int i = 0; i < VALUES.length; i++) {
      TimeIntervalRelation ar = VALUES[i];
      assertNotNull(ar);
      assertEquals(i, ar.hashCode());
      for (int j = i + 1; j < VALUES.length; j++) {
        assertNotSame(ar, VALUES[j]);
      }
    }
  }

  @SuppressWarnings("unused")
  private String fullBitPattern(TimeIntervalRelation ar) {
    int bitpattern = ar.hashCode();
    String bitString = Integer.toBinaryString(bitpattern);
    while (bitString.length() < 13) {
      bitString = "0" + bitString;
    }
    return bitString;
  }

  @Test
  public void testEMPTY() {
    for (TimeIntervalRelation br : BASIC_RELATIONS) {
      assertFalse(EMPTY.impliedBy(br));
    }
  }

  @Test
  public void testBASIC_RELATIONS() {
    for (TimeIntervalRelation br : BASIC_RELATIONS) {
      assertEquals(br, BASIC_RELATIONS[br.basicRelationOrdinal()]);
    }
    assertEquals(PRECEDES, BASIC_RELATIONS[0]);
    assertEquals(MEETS, BASIC_RELATIONS[1]);
    assertEquals(OVERLAPS, BASIC_RELATIONS[2]);
    assertEquals(FINISHED_BY, BASIC_RELATIONS[3]);
    assertEquals(CONTAINS, BASIC_RELATIONS[4]);
    assertEquals(STARTS, BASIC_RELATIONS[5]);
    assertEquals(EQUALS, BASIC_RELATIONS[6]);
    assertEquals(STARTED_BY, BASIC_RELATIONS[7]);
    assertEquals(DURING, BASIC_RELATIONS[8]);
    assertEquals(FINISHES, BASIC_RELATIONS[9]);
    assertEquals(OVERLAPPED_BY, BASIC_RELATIONS[10]);
    assertEquals(MET_BY, BASIC_RELATIONS[11]);
    assertEquals(PRECEDED_BY, BASIC_RELATIONS[12]);
  }

  @Test
  public void testSecondaryRelations() {
    assertEquals(or(OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY), CONCURS_WITH);
    assertEquals(or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS), BEGINS_EARLIER);
    assertEquals(or(STARTS, EQUALS, STARTED_BY), BEGIN_TOGETHER);
    assertEquals(or(DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY), BEGINS_LATER);
    assertEquals(or(DURING, FINISHES, OVERLAPPED_BY), BEGINS_IN);
    assertEquals(or(PRECEDES, MEETS, OVERLAPS), BEGINS_EARLIER_AND_ENDS_EARLIER);
    assertEquals(or(OVERLAPPED_BY, MET_BY, PRECEDED_BY), BEGINS_LATER_AND_ENDS_LATER);
    assertEquals(or(PRECEDES, MEETS, OVERLAPS, STARTS, DURING), ENDS_EARLIER);
    assertEquals(or(OVERLAPS, STARTS, DURING), ENDS_IN);
    assertEquals(or(FINISHED_BY, EQUALS, FINISHES), END_TOGETHER);
    assertEquals(or(CONTAINS, STARTED_BY, OVERLAPPED_BY, MET_BY, PRECEDED_BY), ENDS_LATER);
    assertEquals(or(OVERLAPS, FINISHED_BY, CONTAINS), CONTAINS_BEGIN);
    assertEquals(or(CONTAINS, STARTED_BY, OVERLAPPED_BY), CONTAINS_END);
  }

  @Test
  public void testOr0() {
    for (TimeIntervalRelation ar1 : BASIC_RELATIONS) {
      for (TimeIntervalRelation ar2 : BASIC_RELATIONS) {
        TimeIntervalRelation result = or(ar1, ar2);
        validateOr(ar1, ar2, result);
      }
    }
  }

  private void validateOr(TimeIntervalRelation ar1, TimeIntervalRelation ar2, TimeIntervalRelation result) {
    for (TimeIntervalRelation br : BASIC_RELATIONS) {
      assertTrue(ar1.impliedBy(br) || ar2.impliedBy(br) ? result.impliedBy(br) : true);
      assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) || ar2.impliedBy(br) : true);
    }
  }

  @Test
  public void testOr1() {
    TimeIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimeIntervalRelation ar1 : subjects) {
      for (TimeIntervalRelation ar2 : subjects) {
        TimeIntervalRelation result = or(ar1, ar2);
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
    TimeIntervalRelation result = or(VALUES);
    assertEquals(FULL, result);
  }

  @Test
  public void testAnd0() {
    for (TimeIntervalRelation ar1 : BASIC_RELATIONS) {
      for (TimeIntervalRelation ar2 : BASIC_RELATIONS) {
        TimeIntervalRelation result = and(ar1, ar2);
        validateAnd(ar1, ar2, result);
      }
    }
  }

  private void validateAnd(TimeIntervalRelation ar1, TimeIntervalRelation ar2, TimeIntervalRelation result) {
    for (TimeIntervalRelation br : BASIC_RELATIONS) {
      assertTrue(ar1.impliedBy(br) && ar2.impliedBy(br) ? result.impliedBy(br) : true);
      assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) && ar2.impliedBy(br) : true);
    }
  }

  @Test
  public void testAnd1() {
    TimeIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimeIntervalRelation ar1 : subjects) {
      for (TimeIntervalRelation ar2 : subjects) {
        TimeIntervalRelation result = and(ar1, ar2);
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
    TimeIntervalRelation result = and(VALUES);
    assertEquals(EMPTY, result);
  }

  @Test
  public void testMin0() {
    for (TimeIntervalRelation base : BASIC_RELATIONS) {
      for (TimeIntervalRelation term : BASIC_RELATIONS) {
        TimeIntervalRelation result = min(base, term);
        validateMin(base, term, result);
      }
    }
  }

  private void validateMin(TimeIntervalRelation base, TimeIntervalRelation term, TimeIntervalRelation result) {
    for (TimeIntervalRelation br : BASIC_RELATIONS) {
      assertTrue(br.implies(result) ? br.implies(base) && (! br.implies(term)) : true);
      assertTrue(br.implies(base) && (! br.implies(term)) ? br.implies(result) : true);
    }
  }

  @Test
  public void testMin1() {
    TimeIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimeIntervalRelation base : subjects) {
      for (TimeIntervalRelation term : subjects) {
        TimeIntervalRelation result = min(base, term);
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
    TimeIntervalRelation result = min(FULL, EMPTY);
    assertEquals(FULL, result);
  }

  @Test
  public void testMin3() {
    TimeIntervalRelation result = min(EMPTY, FULL);
    assertEquals(EMPTY, result);
  }

  @Test
  public void testCompose0() {
    for (TimeIntervalRelation gr1 : BASIC_RELATIONS) {
      for (TimeIntervalRelation gr2 : BASIC_RELATIONS) {
        TimeIntervalRelation result = compose(gr1, gr2);
        validateCompose(gr1, gr2, result);
      }
    }
  }

  private void validateCompose(TimeIntervalRelation gr1, TimeIntervalRelation gr2, TimeIntervalRelation result) {
    for (TimeIntervalRelation br1 : BASIC_RELATIONS) {
      for (TimeIntervalRelation br2 : BASIC_RELATIONS) {
        assertTrue(br1.implies(gr1) && br2.implies(gr2) ?
                     result.impliedBy(TimeIntervalRelation.BASIC_COMPOSITIONS[br1.basicRelationOrdinal()][br2.basicRelationOrdinal()]) :
                     true);
      }
    }
  }

  @Test
  public void testCompose1() {
    TimeIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases; warning: this method is O(n3)");
    long count = 0;
    for (TimeIntervalRelation gr1 : subjects) {
      for (TimeIntervalRelation gr2 : subjects) {
        TimeIntervalRelation result = compose(gr1, gr2);
        validateCompose(gr1, gr2, result);
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 10000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
      }
    }
  }


  @Test
  public void testAllenRelation() {
    // MUDO unit test missing
  }

  @Test
  public void testHashCode() {
    for (TimeIntervalRelation ar : VALUES) {
      int result = ar.hashCode();
      assertEquals(ar, VALUES[result]);
    }
  }

  @Test
  public void testBasicRelationOrdinal() {
    for (TimeIntervalRelation ar : BASIC_RELATIONS) {
      int result = ar.basicRelationOrdinal();
      assertTrue(result >= 0);
      assertTrue(result < 13);
    }
  }

  @Test
  public void testIsBasic() {
    List<TimeIntervalRelation> basicRelations = Arrays.asList(BASIC_RELATIONS);
    for (TimeIntervalRelation ar : VALUES) {
      boolean result = ar.isBasic();
      assertEquals(basicRelations.contains(ar), result);
    }
  }

  @Test
  public void testUncertainty() {
    for (TimeIntervalRelation ar : VALUES) {
      float result = ar.uncertainty();
//      System.out.println(ar + "  -- " + result);
      if (ar != EMPTY) {
        int count = 0;
        for (TimeIntervalRelation br : BASIC_RELATIONS) {
          if (br.implies(ar)) {
            count++;
          }
        }
        double expected = (float)(count - 1) / 12;
        assertEquals(expected, result, 1E-5);
      }
      else {
        assertEquals(Float.NaN, result, 0);
      }
    }
  }

  @Test
  public void testConverse0() {
    assertEquals(PRECEDED_BY, PRECEDES.converse());
    assertEquals(MET_BY, MEETS.converse());
    assertEquals(OVERLAPPED_BY, OVERLAPS.converse());
    assertEquals(FINISHES, FINISHED_BY.converse());
    assertEquals(DURING, CONTAINS.converse());
    assertEquals(STARTED_BY, STARTS.converse());
    assertEquals(EQUALS, EQUALS.converse());
    assertEquals(STARTS, STARTED_BY.converse());
    assertEquals(CONTAINS, DURING.converse());
    assertEquals(FINISHED_BY, FINISHES.converse());
    assertEquals(OVERLAPS, OVERLAPPED_BY.converse());
    assertEquals(MEETS, MET_BY.converse());
    assertEquals(PRECEDES, PRECEDED_BY.converse());
  }

  @Test
  public void testConverse1() {
    for (TimeIntervalRelation ar : VALUES) {
      TimeIntervalRelation resultA = ar.converse();
      for (TimeIntervalRelation br : BASIC_RELATIONS) {
        assertTrue(ar.impliedBy(br) ? ar.converse().impliedBy(br.converse()) : true);
        assertTrue(ar.converse().impliedBy(br.converse()) ? ar.impliedBy(br) : true);
      }
      TimeIntervalRelation resultB = resultA.converse();
      assertEquals(ar, resultB);
    }
  }

  @Test
  public void testComplement1() {
    for (TimeIntervalRelation ar : VALUES) {
      TimeIntervalRelation resultA = ar.complement();
      for (TimeIntervalRelation br : BASIC_RELATIONS) {
        assertTrue(ar.impliedBy(br) ? ! resultA.impliedBy(br) : true);
        assertTrue(! resultA.impliedBy(br) ? ar.impliedBy(br) : true);
        assertTrue(! ar.impliedBy(br) ? resultA.impliedBy(br) : true);
        assertTrue(resultA.impliedBy(br) ? ! ar.impliedBy(br) : true);
      }
      TimeIntervalRelation resultB = resultA.complement();
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
    TimeIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimeIntervalRelation subject : values()) {
      assertEquals(true, subject.impliedBy(subject));
      if (subject.isBasic()) {
        for (TimeIntervalRelation br : BASIC_RELATIONS) {
          assertTrue(br != subject ? ! subject.impliedBy(br) : true);
        }
      }
      for (TimeIntervalRelation gr : values()) {
        boolean result = subject.impliedBy(gr);
        boolean expected = true;
        for (TimeIntervalRelation br : BASIC_RELATIONS) {
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
    TimeIntervalRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (TimeIntervalRelation subject : values()) {
      for (TimeIntervalRelation gr : values()) {
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
    for (TimeIntervalRelation ar : VALUES) {
      String result = ar.toString();
      assertNotNull(result);
//      System.out.println(result);
    }
  }

}

