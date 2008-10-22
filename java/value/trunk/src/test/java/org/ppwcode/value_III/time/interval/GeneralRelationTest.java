/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;

import static org.junit.Assert.*;
import static org.ppwcode.value_III.time.interval.AllenRelation.CONCURS_WITH;
import static org.ppwcode.value_III.time.interval.AllenRelation.CONTAINS;
import static org.ppwcode.value_III.time.interval.AllenRelation.DURING;
import static org.ppwcode.value_III.time.interval.AllenRelation.EMPTY;
import static org.ppwcode.value_III.time.interval.AllenRelation.EQUALS;
import static org.ppwcode.value_III.time.interval.AllenRelation.FINISHED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.FINISHES;
import static org.ppwcode.value_III.time.interval.AllenRelation.FULL;
import static org.ppwcode.value_III.time.interval.AllenRelation.MEETS;
import static org.ppwcode.value_III.time.interval.AllenRelation.MET_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.OVERLAPPED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.OVERLAPS;
import static org.ppwcode.value_III.time.interval.AllenRelation.PRECEDED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.PRECEDES;
import static org.ppwcode.value_III.time.interval.AllenRelation.STARTED_BY;
import static org.ppwcode.value_III.time.interval.AllenRelation.STARTS;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.value_III.time.interval.AllenRelation;



public class GeneralRelationTest {

  @Test
  public void testHashCode() {
    show(EMPTY);
    show(PRECEDES);
    show(MEETS);
    show(OVERLAPS);
    show(FINISHED_BY);
    show(CONTAINS);
    show(STARTS);
    show(EQUALS);
    show(STARTED_BY);
    show(DURING);
    show(FINISHES);
    show(OVERLAPPED_BY);
    show(MET_BY);
    show(PRECEDED_BY);
    show(FULL);
    show(CONCURS_WITH);
  }

  private void show(AllenRelation gr) {
    System.out.println(Integer.toBinaryString(gr.hashCode()) + " (" + gr.hashCode() + ") (position = " + Integer.numberOfTrailingZeros(gr.hashCode()) + ")");
  }

  @Test
  public void testIsBasic() {
    assertFalse(EMPTY.isBasic());
    assertTrue(PRECEDES.isBasic());
    assertTrue(MEETS.isBasic());
    assertTrue(OVERLAPS.isBasic());
    assertTrue(FINISHED_BY.isBasic());
    assertTrue(CONTAINS.isBasic());
    assertTrue(STARTS.isBasic());
    assertTrue(EQUALS.isBasic());
    assertTrue(STARTED_BY.isBasic());
    assertTrue(DURING.isBasic());
    assertTrue(FINISHES.isBasic());
    assertTrue(OVERLAPPED_BY.isBasic());
    assertTrue(MET_BY.isBasic());
    assertTrue(PRECEDED_BY.isBasic());
    assertFalse(FULL.isBasic());
  }

  @Test
  public void testContains() {
    assertTrue(FULL.implies(EMPTY));
    assertTrue(FULL.implies(PRECEDES));
    assertTrue(FULL.implies(MEETS));
    assertTrue(FULL.implies(OVERLAPS));
    assertTrue(FULL.implies(FINISHED_BY));
    assertTrue(FULL.implies(CONTAINS));
    assertTrue(FULL.implies(STARTS));
    assertTrue(FULL.implies(EQUALS));
    assertTrue(FULL.implies(STARTED_BY));
    assertTrue(FULL.implies(DURING));
    assertTrue(FULL.implies(FINISHES));
    assertTrue(FULL.implies(OVERLAPPED_BY));
    assertTrue(FULL.implies(MET_BY));
    assertTrue(FULL.implies(PRECEDED_BY));
    assertTrue(FULL.implies(FULL));
  }

  @Test
  public void testComplement() {
    show(EMPTY.complement());
    show(PRECEDES.complement());
    show(MEETS.complement());
    show(OVERLAPS.complement());
    show(FINISHED_BY.complement());
    show(CONTAINS.complement());
    show(STARTS.complement());
    show(EQUALS.complement());
    show(STARTED_BY.complement());
    show(DURING.complement());
    show(FINISHES.complement());
    show(OVERLAPPED_BY.complement());
    show(MET_BY.complement());
    show(PRECEDED_BY.complement());
    show(FULL.complement());
  }

  @Test
  public void testOr() {
  }

  @Test
  public void testConverse() {
    for (int i = 0; i < AllenRelation.NR_OF_RELATIONS; i++) {
      AllenRelation subject = AllenRelation.VALUES[i];
      AllenRelation result = subject.converse();
      assertNotNull(result);
      for (AllenRelation br : AllenRelation.BASIC_RELATIONS) {
        AllenRelation converseBr = br.converse();
        if (subject.implies(br)) {
          assertTrue(result.implies(converseBr));
        }
        else {
          assertTrue(! result.implies(converseBr));
        }
      }
    }
  }

  @Test
  public void testCompose() {
    for (AllenRelation br1 : AllenRelation.BASIC_RELATIONS) {
      for (AllenRelation br2 : AllenRelation.BASIC_RELATIONS) {
        AllenRelation result = AllenRelation.compose(br1, br2);
        int p1 = Integer.numberOfTrailingZeros(br1.hashCode());
        int p2 = Integer.numberOfTrailingZeros(br2.hashCode());
        AllenRelation expected = AllenRelation.BASIC_COMPOSITIONS[p1][p2];
        assertEquals(expected, result);
      }
    }
  }

}

