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


import static org.junit.Assert.*;
import static org.ppwcode.value_III.time.interval.AllenRelation.BASIC_RELATIONS;
import static org.ppwcode.value_III.time.interval.AllenRelation.BEGINS_EARLIER;
import static org.ppwcode.value_III.time.interval.AllenRelation.BEGINS_EARLIER_AND_ENDS_EARLIER;
import static org.ppwcode.value_III.time.interval.AllenRelation.BEGINS_IN;
import static org.ppwcode.value_III.time.interval.AllenRelation.BEGINS_LATER;
import static org.ppwcode.value_III.time.interval.AllenRelation.BEGINS_LATER_AND_ENDS_LATER;
import static org.ppwcode.value_III.time.interval.AllenRelation.BEGIN_TOGETHER;
import static org.ppwcode.value_III.time.interval.AllenRelation.CONCURS_WITH;
import static org.ppwcode.value_III.time.interval.AllenRelation.CONTAINS;
import static org.ppwcode.value_III.time.interval.AllenRelation.CONTAINS_BEGIN;
import static org.ppwcode.value_III.time.interval.AllenRelation.CONTAINS_END;
import static org.ppwcode.value_III.time.interval.AllenRelation.DURING;
import static org.ppwcode.value_III.time.interval.AllenRelation.EMPTY;
import static org.ppwcode.value_III.time.interval.AllenRelation.ENDS_EARLIER;
import static org.ppwcode.value_III.time.interval.AllenRelation.ENDS_IN;
import static org.ppwcode.value_III.time.interval.AllenRelation.ENDS_LATER;
import static org.ppwcode.value_III.time.interval.AllenRelation.END_TOGETHER;
import static org.ppwcode.value_III.time.interval.AllenRelation.EQUALS;
import static org.ppwcode.value_III.time.interval.AllenRelation.FINISHED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.FINISHES;
import static org.ppwcode.value_III.time.interval.AllenRelation.FULL;
import static org.ppwcode.value_III.time.interval.AllenRelation.MEETS;
import static org.ppwcode.value_III.time.interval.AllenRelation.MET_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.NR_OF_RELATIONS;
import static org.ppwcode.value_III.time.interval.AllenRelation.OVERLAPPED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.OVERLAPS;
import static org.ppwcode.value_III.time.interval.AllenRelation.PRECEDED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.PRECEDES;
import static org.ppwcode.value_III.time.interval.AllenRelation.STARTED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.STARTS;
import static org.ppwcode.value_III.time.interval.AllenRelation.VALUES;
import static org.ppwcode.value_III.time.interval.AllenRelation.and;
import static org.ppwcode.value_III.time.interval.AllenRelation.compose;
import static org.ppwcode.value_III.time.interval.AllenRelation.min;
import static org.ppwcode.value_III.time.interval.AllenRelation.or;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AllenRelationTest {

  public final static boolean FULL_TESTS = false;

  public final static int NR_OF_RANDOM_VALUES = 300;

  public final static AllenRelation[] values() {
    if (FULL_TESTS) {
      return VALUES;
    }
    else {
      AllenRelation[] result = new AllenRelation[NR_OF_RANDOM_VALUES];
      result[0] = EMPTY;
      for (int i = 1; i < (BASIC_RELATIONS.length + 1); i++) {
        result[i] = BASIC_RELATIONS[i - 1];
      }
      result[14] = FULL;
      for (int i = 15; i < NR_OF_RANDOM_VALUES; i++) {
        Random r = new Random();
        int index = r.nextInt(AllenRelation.NR_OF_RELATIONS);
        result[i] = VALUES[index];
      }
      return result;
    }
  }

  @Test
  public void testVALUES() {
    assertNotNull(VALUES);
    for (int i = 0; i < VALUES.length; i++) {
      AllenRelation ar = VALUES[i];
      assertNotNull(ar);
      assertEquals(i, ar.hashCode());
      for (int j = i + 1; j < VALUES.length; j++) {
        assertNotSame(ar, VALUES[j]);
      }
    }
  }

  private String fullBitPattern(AllenRelation ar) {
    int bitpattern = ar.hashCode();
    String bitString = Integer.toBinaryString(bitpattern);
    while (bitString.length() < 13) {
      bitString = "0" + bitString;
    }
    return bitString;
  }

  @Test
  public void testEMPTY() {
    for (AllenRelation br : BASIC_RELATIONS) {
      assertFalse(EMPTY.impliedBy(br));
    }
  }

  @Test
  public void testBASIC_RELATIONS() {
    for (AllenRelation br : BASIC_RELATIONS) {
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
    for (AllenRelation ar1 : BASIC_RELATIONS) {
      for (AllenRelation ar2 : BASIC_RELATIONS) {
        AllenRelation result = or(ar1, ar2);
        validateOr(ar1, ar2, result);
      }
    }
  }

  private void validateOr(AllenRelation ar1, AllenRelation ar2, AllenRelation result) {
    for (AllenRelation br : BASIC_RELATIONS) {
      assertTrue(ar1.impliedBy(br) || ar2.impliedBy(br) ? result.impliedBy(br) : true);
      assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) || ar2.impliedBy(br) : true);
    }
  }

  @Test
  public void testOr1() {
    AllenRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (AllenRelation ar1 : subjects) {
      for (AllenRelation ar2 : subjects) {
        AllenRelation result = or(ar1, ar2);
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
    AllenRelation result = or(VALUES);
    assertEquals(FULL, result);
  }

  @Test
  public void testAnd0() {
    for (AllenRelation ar1 : BASIC_RELATIONS) {
      for (AllenRelation ar2 : BASIC_RELATIONS) {
        AllenRelation result = and(ar1, ar2);
        validateAnd(ar1, ar2, result);
      }
    }
  }

  private void validateAnd(AllenRelation ar1, AllenRelation ar2, AllenRelation result) {
    for (AllenRelation br : BASIC_RELATIONS) {
      assertTrue(ar1.impliedBy(br) && ar2.impliedBy(br) ? result.impliedBy(br) : true);
      assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) && ar2.impliedBy(br) : true);
    }
  }

  @Test
  public void testAnd1() {
    AllenRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (AllenRelation ar1 : subjects) {
      for (AllenRelation ar2 : subjects) {
        AllenRelation result = and(ar1, ar2);
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
    AllenRelation result = and(VALUES);
    assertEquals(EMPTY, result);
  }

  @Test
  public void testMin0() {
    for (AllenRelation base : BASIC_RELATIONS) {
      for (AllenRelation term : BASIC_RELATIONS) {
        AllenRelation result = min(base, term);
        validateMin(base, term, result);
      }
    }
  }

  private void validateMin(AllenRelation base, AllenRelation term, AllenRelation result) {
    for (AllenRelation br : BASIC_RELATIONS) {
      assertTrue(br.implies(result) ? br.implies(base) && (! br.implies(term)) : true);
      assertTrue(br.implies(base) && (! br.implies(term)) ? br.implies(result) : true);
    }
  }

  @Test
  public void testMin1() {
    AllenRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (AllenRelation base : subjects) {
      for (AllenRelation term : subjects) {
        AllenRelation result = min(base, term);
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
    AllenRelation result = min(FULL, EMPTY);
    assertEquals(FULL, result);
  }

  @Test
  public void testMin3() {
    AllenRelation result = min(EMPTY, FULL);
    assertEquals(EMPTY, result);
  }

  @Test
  public void testCompose0() {
    for (AllenRelation gr1 : BASIC_RELATIONS) {
      for (AllenRelation gr2 : BASIC_RELATIONS) {
        AllenRelation result = compose(gr1, gr2);
        validateCompose(gr1, gr2, result);
      }
    }
  }

  private void validateCompose(AllenRelation gr1, AllenRelation gr2, AllenRelation result) {
    for (AllenRelation br1 : BASIC_RELATIONS) {
      for (AllenRelation br2 : BASIC_RELATIONS) {
        assertTrue(br1.implies(gr1) && br2.implies(gr2) ?
                     result.impliedBy(AllenRelation.BASIC_COMPOSITIONS[br1.basicRelationOrdinal()][br2.basicRelationOrdinal()]) :
                     true);
      }
    }
  }

  @Test
  public void testCompose1() {
    AllenRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases; warning: this method is O(n3)");
    long count = 0;
    for (AllenRelation gr1 : subjects) {
      for (AllenRelation gr2 : subjects) {
        AllenRelation result = compose(gr1, gr2);
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
    for (AllenRelation ar : VALUES) {
      int result = ar.hashCode();
      assertEquals(ar, VALUES[result]);
    }
  }

  @Test
  public void testBasicRelationOrdinal() {
    for (AllenRelation ar : BASIC_RELATIONS) {
      int result = ar.basicRelationOrdinal();
      assertTrue(result >= 0);
      assertTrue(result < 13);
    }
  }

  @Test
  public void testIsBasic() {
    List<AllenRelation> basicRelations = Arrays.asList(BASIC_RELATIONS);
    for (AllenRelation ar : VALUES) {
      boolean result = ar.isBasic();
      assertEquals(basicRelations.contains(ar), result);
    }
  }

  @Test
  public void testUncertainty() {
    for (AllenRelation ar : VALUES) {
      float result = ar.uncertainty();
//      System.out.println(ar + "  -- " + result);
      if (ar != EMPTY) {
        int count = 0;
        for (AllenRelation br : BASIC_RELATIONS) {
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
    for (AllenRelation ar : VALUES) {
      AllenRelation resultA = ar.converse();
      for (AllenRelation br : BASIC_RELATIONS) {
        assertTrue(ar.impliedBy(br) ? ar.converse().impliedBy(br.converse()) : true);
        assertTrue(ar.converse().impliedBy(br.converse()) ? ar.impliedBy(br) : true);
      }
      AllenRelation resultB = resultA.converse();
      assertEquals(ar, resultB);
    }
  }

  @Test
  public void testComplement1() {
    for (AllenRelation ar : VALUES) {
      AllenRelation resultA = ar.complement();
      for (AllenRelation br : BASIC_RELATIONS) {
        assertTrue(ar.impliedBy(br) ? ! resultA.impliedBy(br) : true);
        assertTrue(! resultA.impliedBy(br) ? ar.impliedBy(br) : true);
        assertTrue(! ar.impliedBy(br) ? resultA.impliedBy(br) : true);
        assertTrue(resultA.impliedBy(br) ? ! ar.impliedBy(br) : true);
      }
      AllenRelation resultB = resultA.complement();
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
    AllenRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (AllenRelation subject : values()) {
      assertEquals(true, subject.impliedBy(subject));
      if (subject.isBasic()) {
        for (AllenRelation br : BASIC_RELATIONS) {
          assertTrue(br != subject ? ! subject.impliedBy(br) : true);
        }
      }
      for (AllenRelation gr : values()) {
        boolean result = subject.impliedBy(gr);
        boolean expected = true;
        for (AllenRelation br : BASIC_RELATIONS) {
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
    AllenRelation[] subjects = values();
    int subjectsCount = subjects.length;
    long total = subjectsCount * subjectsCount;
    System.out.println("Starting test over " + total + " cases");
    long count = 0;
    for (AllenRelation subject : values()) {
      for (AllenRelation gr : values()) {
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
    for (AllenRelation ar : VALUES) {
      String result = ar.toString();
      assertNotNull(result);
//      System.out.println(result);
    }
  }

}

