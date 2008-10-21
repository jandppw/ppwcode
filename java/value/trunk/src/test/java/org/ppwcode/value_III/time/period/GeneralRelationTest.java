/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.period;

import static org.junit.Assert.*;
import static org.ppwcode.value_III.time.period.GeneralRelation.CONCURS_WITH;
import static org.ppwcode.value_III.time.period.GeneralRelation.CONTAINS;
import static org.ppwcode.value_III.time.period.GeneralRelation.DURING;
import static org.ppwcode.value_III.time.period.GeneralRelation.EMPTY;
import static org.ppwcode.value_III.time.period.GeneralRelation.EQUALS;
import static org.ppwcode.value_III.time.period.GeneralRelation.FINISHED_BY;
import static org.ppwcode.value_III.time.period.GeneralRelation.FINISHES;
import static org.ppwcode.value_III.time.period.GeneralRelation.FULL;
import static org.ppwcode.value_III.time.period.GeneralRelation.MEETS;
import static org.ppwcode.value_III.time.period.GeneralRelation.MET_BY;
import static org.ppwcode.value_III.time.period.GeneralRelation.OVERLAPPED_BY;
import static org.ppwcode.value_III.time.period.GeneralRelation.OVERLAPS;
import static org.ppwcode.value_III.time.period.GeneralRelation.PRECEDED_BY;
import static org.ppwcode.value_III.time.period.GeneralRelation.PRECEDES;
import static org.ppwcode.value_III.time.period.GeneralRelation.STARTED_BY;
import static org.ppwcode.value_III.time.period.GeneralRelation.STARTS;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



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

  private void show(GeneralRelation gr) {
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
    for (int i = 0; i < GeneralRelation.NR_OF_RELATIONS; i++) {
      GeneralRelation subject = GeneralRelation.VALUES[i];
      GeneralRelation result = subject.converse();
      assertNotNull(result);
      for (GeneralRelation br : GeneralRelation.BASIC_RELATIONS) {
        GeneralRelation converseBr = br.converse();
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
    for (GeneralRelation br1 : GeneralRelation.BASIC_RELATIONS) {
      for (GeneralRelation br2 : GeneralRelation.BASIC_RELATIONS) {
        GeneralRelation result = GeneralRelation.compose(br1, br2);
        int p1 = Integer.numberOfTrailingZeros(br1.hashCode());
        int p2 = Integer.numberOfTrailingZeros(br2.hashCode());
        GeneralRelation expected = GeneralRelation.BASIC_COMPOSITIONS[p1][p2];
        assertEquals(expected, result);
      }
    }
  }

}

