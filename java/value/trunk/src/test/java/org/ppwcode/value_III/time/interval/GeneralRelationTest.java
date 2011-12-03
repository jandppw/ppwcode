/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.value_III.time.interval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.CONCURS_WITH;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.CONTAINS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.DURING;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.EMPTY;
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

  private void show(TimeIntervalRelation gr) {
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
    assertTrue(FULL.impliedBy(EMPTY));
    assertTrue(FULL.impliedBy(PRECEDES));
    assertTrue(FULL.impliedBy(MEETS));
    assertTrue(FULL.impliedBy(OVERLAPS));
    assertTrue(FULL.impliedBy(FINISHED_BY));
    assertTrue(FULL.impliedBy(CONTAINS));
    assertTrue(FULL.impliedBy(STARTS));
    assertTrue(FULL.impliedBy(EQUALS));
    assertTrue(FULL.impliedBy(STARTED_BY));
    assertTrue(FULL.impliedBy(DURING));
    assertTrue(FULL.impliedBy(FINISHES));
    assertTrue(FULL.impliedBy(OVERLAPPED_BY));
    assertTrue(FULL.impliedBy(MET_BY));
    assertTrue(FULL.impliedBy(PRECEDED_BY));
    assertTrue(FULL.impliedBy(FULL));
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
  public void testConverse() {
    for (int i = 0; i < TimeIntervalRelation.NR_OF_RELATIONS; i++) {
      TimeIntervalRelation subject = TimeIntervalRelation.VALUES[i];
      TimeIntervalRelation result = subject.converse();
      assertNotNull(result);
      for (TimeIntervalRelation br : TimeIntervalRelation.BASIC_RELATIONS) {
        TimeIntervalRelation converseBr = br.converse();
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
    for (TimeIntervalRelation br1 : TimeIntervalRelation.BASIC_RELATIONS) {
      for (TimeIntervalRelation br2 : TimeIntervalRelation.BASIC_RELATIONS) {
        TimeIntervalRelation result = TimeIntervalRelation.compose(br1, br2);
        int p1 = Integer.numberOfTrailingZeros(br1.hashCode());
        int p2 = Integer.numberOfTrailingZeros(br2.hashCode());
        TimeIntervalRelation expected = TimeIntervalRelation.BASIC_COMPOSITIONS[p1][p2];
        assertEquals(expected, result);
      }
    }
  }

}

