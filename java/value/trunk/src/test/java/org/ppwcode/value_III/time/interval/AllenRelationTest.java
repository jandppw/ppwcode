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
import static org.ppwcode.value_III.time.interval.AllenRelation.or;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AllenRelationTest {

  public final static boolean FULL_TESTS = false;

  public final static int NR_OF_RANDOM_VALUES = 500;

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
//      System.out.println(fullBitPattern(ar) + "  " + ar.hashCode());
//      if (i % 25 == 0) {
//        Thread.sleep(2000);
//      }
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
//        System.out.println("or(" + ar1 + ", " + ar2 + ") == " + result);
        for (AllenRelation br : BASIC_RELATIONS) {
//          System.out.print(br + " ");
          assertTrue(ar1.impliedBy(br) || ar2.impliedBy(br) ? result.impliedBy(br) : true);
          assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) || ar2.impliedBy(br) : true);
        }
//        System.out.println();
      }
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
//        System.out.println("or(" + ar1 + ", " + ar2 + ") == " + result);
        for (AllenRelation br : BASIC_RELATIONS) {
//          System.out.print(br + " ");
          assertTrue(ar1.impliedBy(br) || ar2.impliedBy(br) ? result.impliedBy(br) : true);
          assertTrue(result.impliedBy(br) ? ar1.impliedBy(br) || ar2.impliedBy(br) : true);
        }
        count++;
        float percentage = ((float)count / total) * 100;
        if (count % 100000 == 0) {
          System.out.println("  progress: " + count + " / " + total + " done (" + percentage + "%)");
        }
//        System.out.println();
      }
    }
  }

  @Test
  public void testOr2() {
    AllenRelation result = or(VALUES);
    assertEquals(FULL, result);
  }

  @Test
  public void testAnd() {
    fail("Not yet implemented");
  }

  @Test
  public void testMin() {
    fail("Not yet implemented");
  }

  @Test
  public void testCompose() {
    fail("Not yet implemented");
  }

  @Test
  public void testAllenRelation() {
    fail("Not yet implemented");
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
    fail("Not yet implemented");
  }

  @Test
  public void testIsBasic() {
    fail("Not yet implemented");
  }

  @Test
  public void testUncertainty() {
    fail("Not yet implemented");
  }

  @Test
  public void testConverse() {
    fail("Not yet implemented");
  }

  @Test
  public void testComplement() {
    fail("Not yet implemented");
  }

  @Test
  public void testImpliedBy() {
    fail("Not yet implemented");
  }

  @Test
  public void testImplies() {
    fail("Not yet implemented");
  }

  @Test
  public void testToString() {
    fail("Not yet implemented");
  }

}

