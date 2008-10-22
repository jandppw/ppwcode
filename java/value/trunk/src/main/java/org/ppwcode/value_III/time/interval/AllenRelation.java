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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public final class AllenRelation {

  public final static int NR_OF_RELATIONS    = 8192;

  // with these bit patterns, converse is reverse of 13-bit pattern
  private final static int EMPTY_BIT_PATTERN          =    0;   // 0000000000000
  private final static int PRECEDES_BIT_PATTERN       =    1;   // 0000000000001 p
  private final static int MEETS_BIT_PATTERN          =    2;   // 0000000000010 m
  private final static int OVERLAPS_BIT_PATTERN       =    4;   // 0000000000100 o
  private final static int FINISHED_BY_BIT_PATTERN    =    8;   // 0000000001000 F
  private final static int CONTAINS_BIT_PATTERN       =   16;   // 0000000010000 D
  private final static int STARTS_BIT_PATTERN         =   32;   // 0000000100000 s
  private final static int EQUALS_BIT_PATTERN         =   64;   // 0000001000000 e
  private final static int STARTED_BY_BIT_PATTERN     =  128;   // 0000010000000 S
  private final static int DURING_BIT_PATTERN         =  256;   // 0000100000000 d
  private final static int FINISHES_BIT_PATTERN       =  512;   // 0001000000000 f
  private final static int OVERLAPPED_BY_BIT_PATTERN  = 1024;   // 0010000000000 O
  private final static int MET_BY_BIT_PATTERN         = 2048;   // 0100000000000 M
  private final static int PRECEDED_BY_BIT_PATTERN    = 4096;   // 1000000000000 P
  private final static int FULL_BIT_PATTERN           = 8191;   // 1111111111111 pmoFDseSdfOMP


  public final static AllenRelation[] VALUES = new AllenRelation[NR_OF_RELATIONS];
  static {
    for (int i = 0; i < NR_OF_RELATIONS; i++) {
      VALUES[i] = new AllenRelation(i);
    }
  }

  public final static AllenRelation EMPTY         = VALUES[EMPTY_BIT_PATTERN];
  public final static AllenRelation PRECEDES      = VALUES[PRECEDES_BIT_PATTERN];
  public final static AllenRelation MEETS         = VALUES[MEETS_BIT_PATTERN];
  public final static AllenRelation OVERLAPS      = VALUES[OVERLAPS_BIT_PATTERN];
  public final static AllenRelation FINISHED_BY   = VALUES[FINISHED_BY_BIT_PATTERN];
  public final static AllenRelation CONTAINS      = VALUES[CONTAINS_BIT_PATTERN];
  public final static AllenRelation STARTS        = VALUES[STARTS_BIT_PATTERN];
  public final static AllenRelation EQUALS        = VALUES[EQUALS_BIT_PATTERN];
  public final static AllenRelation STARTED_BY    = VALUES[STARTED_BY_BIT_PATTERN];
  public final static AllenRelation DURING        = VALUES[DURING_BIT_PATTERN];
  public final static AllenRelation FINISHES      = VALUES[FINISHES_BIT_PATTERN];
  public final static AllenRelation OVERLAPPED_BY = VALUES[OVERLAPPED_BY_BIT_PATTERN];
  public final static AllenRelation MET_BY        = VALUES[MET_BY_BIT_PATTERN];
  public final static AllenRelation PRECEDED_BY   = VALUES[PRECEDED_BY_BIT_PATTERN];
  public final static AllenRelation FULL          = VALUES[FULL_BIT_PATTERN];


  public final static AllenRelation  CONCURS_WITH = or(OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY);

  private static final AllenRelation ENDS_EARLIER                    = or(PRECEDES, MEETS, OVERLAPS, STARTS, DURING);
  private static final AllenRelation BEGINS_EARLIER_AND_ENDS_EARLIER = or(PRECEDES, MEETS, OVERLAPS);
  private static final AllenRelation ENDS_IN                         = or(OVERLAPS, STARTS, DURING);

  private static final AllenRelation BEGINS_EARLIER                  = or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS);
  private static final AllenRelation CONTAINS_BEGIN                  = or(OVERLAPS, FINISHED_BY, CONTAINS);

  private static final AllenRelation ENDS_LATER                      = or(CONTAINS, STARTED_BY, OVERLAPPED_BY, MET_BY, PRECEDED_BY);
  private static final AllenRelation CONTAINS_END                    = or(CONTAINS, STARTED_BY, OVERLAPPED_BY);
  private static final AllenRelation BEGINS_LATER_AND_ENDS_LATER     = or(OVERLAPPED_BY, MET_BY, PRECEDED_BY);

  private static final AllenRelation BEGINS_LATER                    = or(DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY);
  private static final AllenRelation BEGINS_IN                       = or(DURING, FINISHES, OVERLAPPED_BY);

  private static final AllenRelation END_TOGETHER                    = or(FINISHED_BY, EQUALS, FINISHES);
  private static final AllenRelation BEGIN_TOGETHER                  = or(STARTS, EQUALS, STARTED_BY);


  public final static Set<AllenRelation> BASIC_RELATIONS = new HashSet<AllenRelation>(13);
  static {
    BASIC_RELATIONS.add(PRECEDES);
    BASIC_RELATIONS.add(MEETS);
    BASIC_RELATIONS.add(OVERLAPS);
    BASIC_RELATIONS.add(FINISHED_BY);
    BASIC_RELATIONS.add(CONTAINS);
    BASIC_RELATIONS.add(STARTS);
    BASIC_RELATIONS.add(EQUALS);
    BASIC_RELATIONS.add(STARTED_BY);
    BASIC_RELATIONS.add(DURING);
    BASIC_RELATIONS.add(FINISHES);
    BASIC_RELATIONS.add(OVERLAPPED_BY);
    BASIC_RELATIONS.add(MET_BY);
    BASIC_RELATIONS.add(PRECEDED_BY);
}

//  public final static AllenRelation PRECEDES_CONVERSE = PRECEDED_BY;
//  public final static AllenRelation MEETS_CONVERSE = MET_BY;
//  public final static AllenRelation OVERLAPS_CONVERSE = OVERLAPPED_BY;
//  public final static AllenRelation FINISHED_BY_CONVERSE = FINISHES;
//  public final static AllenRelation CONTAINS_CONVERSE = DURING;
//  public final static AllenRelation STARTS_CONVERSE = STARTED_BY;
//  public final static AllenRelation EQUALS_CONVERSE = EQUALS;
//  public final static AllenRelation STARTED_BY_CONVERSE = STARTS;
//  public final static AllenRelation DURING_CONVERSE = CONTAINS;
//  public final static AllenRelation FINISHES_CONVERSE = FINISHED_BY;
//  public final static AllenRelation OVERLAPPED_BY_CONVERSE = OVERLAPS;
//  public final static AllenRelation MET_BY_CONVERSE = MEETS;
//  public final static AllenRelation PRECEDED_BY_CONVERSE = PRECEDES;
//
//  public final static AllenRelation[] CONVERSE_VALUES = new AllenRelation[NR_OF_RELATIONS];
//  static {
//    CONVERSE_VALUES[PRECEDES_BIT_PATTERN] = PRECEDES_CONVERSE;
//    CONVERSE_VALUES[MEETS_BIT_PATTERN] = MEETS_CONVERSE;
//    CONVERSE_VALUES[OVERLAPS_BIT_PATTERN] = OVERLAPS_CONVERSE;
//    CONVERSE_VALUES[FINISHED_BY_BIT_PATTERN] = FINISHED_BY_CONVERSE;
//    CONVERSE_VALUES[CONTAINS_BIT_PATTERN] = CONTAINS_CONVERSE;
//    CONVERSE_VALUES[STARTS_BIT_PATTERN] = STARTS_CONVERSE;
//    CONVERSE_VALUES[EQUALS_BIT_PATTERN] = EQUALS_CONVERSE;
//    CONVERSE_VALUES[STARTED_BY_BIT_PATTERN] = STARTED_BY_CONVERSE;
//    CONVERSE_VALUES[DURING_BIT_PATTERN] = DURING_CONVERSE;
//    CONVERSE_VALUES[FINISHES_BIT_PATTERN] = FINISHES_CONVERSE;
//    CONVERSE_VALUES[OVERLAPPED_BY_BIT_PATTERN] = OVERLAPPED_BY_CONVERSE;
//    CONVERSE_VALUES[MET_BY_BIT_PATTERN] = MET_BY_CONVERSE;
//    CONVERSE_VALUES[PRECEDED_BY_BIT_PATTERN] = PRECEDED_BY_CONVERSE;
//    for (int i = 0; i < NR_OF_RELATIONS; i++) {
//      if (! isBasicBitPattern(i)) {
//        int pattern = Integer.reverse(i);
//        pattern >>>= 19; // 32 - 13 = 19
//        CONVERSE_VALUES[i] = VALUES[pattern];
//      }
//    }
//  }


  private final int $bitPattern;

  private AllenRelation(int bitPattern) {
    $bitPattern = bitPattern;
  }

  public static AllenRelation or(AllenRelation... gr) {
    int acc = 0;
    for (AllenRelation allenRelation : gr) {
      acc |= allenRelation.$bitPattern;
    }
    return VALUES[acc];
  }

  public final boolean isBasic() {
    return isBasicBitPattern($bitPattern);
  }

  private static boolean isBasicBitPattern(int bitPattern) {
//    return Integer.bitCount($bitPattern) == 1;
    /* http://graphics.stanford.edu/~seander/bithacks.html
     * Determining if an integer is a power of 2
     * unsigned int v; // we want to see if v is a power of 2
     * bool f;         // the result goes here
     * f = (v & (v - 1)) == 0;
     *
     * Note that 0 is incorrectly considered a power of 2 here. To remedy this, use:
     * f = !(v & (v - 1)) && v;
     */
    return ((bitPattern & (bitPattern - 1)) == 0) && (bitPattern != 0);
  }

//  @Override
//  public boolean equals(Object other) {
//    return other != null && other instanceof AllenRelation &&
//     $bitPattern == ((AllenRelation)other).$basicRelationsBitPattern;
//  }

  @Override
  public final int hashCode() {
    return $bitPattern;
  }

  public final boolean implies(AllenRelation gr) {
    return (($bitPattern & gr.$bitPattern) == gr.$bitPattern) ||
           ($bitPattern == 0);
  }

  public final AllenRelation complement() {
    int full = FULL.$bitPattern;
    int result = full ^ $bitPattern;
    return VALUES[result];
  }

  /**
   * compare(p1, p2).converse() == compare(p2, p1)
   */
  public final AllenRelation converse() {
    int pattern = Integer.reverse($bitPattern);
    pattern >>>= 19; // 32 - 13 = 19
    return VALUES[pattern];
  }

  /**
   * Union
   */
  public static AllenRelation or(AllenRelation gr1, AllenRelation gr2) {
    int or = gr1.$bitPattern | gr2.$bitPattern;
    return VALUES[or];
  }

  /**
   * Intersection
   */
  public static AllenRelation and(AllenRelation gr1, AllenRelation gr2) {
    int and = gr1.$bitPattern & gr2.$bitPattern;
    return VALUES[and];
  }

  /**
   * Remove basic relations in {@code gr2} from {@code gr1}.
   */
  public static AllenRelation min(AllenRelation gr1, AllenRelation gr2) {
    int xor = gr1.$bitPattern ^ gr2.$bitPattern;
    int min = gr1.$bitPattern & xor;
    return VALUES[min];
  }


  public final static AllenRelation[][] BASIC_COMPOSITIONS =
    {
      {PRECEDES,PRECEDES,PRECEDES,PRECEDES,PRECEDES,PRECEDES,PRECEDES,PRECEDES,ENDS_EARLIER,ENDS_EARLIER,ENDS_EARLIER,ENDS_EARLIER,FULL},
      {PRECEDES,PRECEDES,PRECEDES,PRECEDES,PRECEDES,MEETS,MEETS,MEETS,ENDS_IN,ENDS_IN,ENDS_IN,END_TOGETHER,ENDS_LATER},
      {PRECEDES,PRECEDES,BEGINS_EARLIER_AND_ENDS_EARLIER,BEGINS_EARLIER_AND_ENDS_EARLIER,BEGINS_EARLIER,OVERLAPS,OVERLAPS,CONTAINS_BEGIN,ENDS_IN,ENDS_IN,CONCURS_WITH,CONTAINS_END,ENDS_LATER},
      {PRECEDES,MEETS,OVERLAPS,FINISHED_BY,CONTAINS,OVERLAPS,FINISHED_BY,CONTAINS,ENDS_IN,END_TOGETHER,CONTAINS_END,CONTAINS_END,ENDS_LATER},
      {BEGINS_EARLIER,CONTAINS_BEGIN,CONTAINS_BEGIN,CONTAINS,CONTAINS,CONTAINS_BEGIN,CONTAINS,CONTAINS,CONCURS_WITH,CONTAINS_END,CONTAINS_END,CONTAINS_END,ENDS_LATER},
      {PRECEDES,PRECEDES,BEGINS_EARLIER_AND_ENDS_EARLIER,BEGINS_EARLIER_AND_ENDS_EARLIER,BEGINS_EARLIER,STARTS,STARTS,BEGIN_TOGETHER,DURING,DURING,BEGINS_IN,MET_BY,PRECEDED_BY},
      {PRECEDES,MEETS,OVERLAPS,FINISHED_BY,CONTAINS,STARTS,EQUALS,STARTED_BY,DURING,FINISHES,OVERLAPPED_BY,MET_BY,PRECEDED_BY},
      {BEGINS_EARLIER,CONTAINS_BEGIN,CONTAINS_BEGIN,CONTAINS,CONTAINS,BEGIN_TOGETHER,STARTED_BY,STARTED_BY,BEGINS_IN,OVERLAPPED_BY,OVERLAPPED_BY,MET_BY,PRECEDED_BY},
      {PRECEDES,PRECEDES,ENDS_EARLIER,ENDS_EARLIER,FULL,DURING,DURING,BEGINS_LATER,DURING,DURING,BEGINS_LATER,PRECEDED_BY,PRECEDED_BY},
      {PRECEDES,MEETS,ENDS_IN,END_TOGETHER,ENDS_LATER,DURING,FINISHES,BEGINS_LATER_AND_ENDS_LATER,DURING,FINISHES,BEGINS_LATER_AND_ENDS_LATER,PRECEDED_BY,PRECEDED_BY},
      {BEGINS_EARLIER,CONTAINS_BEGIN,CONCURS_WITH,CONTAINS_END,ENDS_LATER,BEGINS_IN,OVERLAPPED_BY,BEGINS_LATER_AND_ENDS_LATER,BEGINS_IN,OVERLAPPED_BY,BEGINS_LATER_AND_ENDS_LATER,PRECEDED_BY,PRECEDED_BY},
      {BEGINS_EARLIER,BEGIN_TOGETHER,BEGINS_IN,MET_BY,PRECEDED_BY,BEGINS_IN,MET_BY,PRECEDED_BY,BEGINS_IN,MET_BY,PRECEDED_BY,PRECEDED_BY,PRECEDED_BY},
      {FULL,BEGINS_LATER,BEGINS_LATER,PRECEDED_BY,PRECEDED_BY,BEGINS_LATER,PRECEDED_BY,PRECEDED_BY,BEGINS_LATER,PRECEDED_BY,PRECEDED_BY,PRECEDED_BY,PRECEDED_BY}
    };

  /**
   * O(13^2)
   */
  public static AllenRelation compose(AllenRelation gr1, AllenRelation gr2) {
    AllenRelation acc = EMPTY;
    for (AllenRelation br1 : BASIC_RELATIONS) {
      if (gr1.implies(br1)) {
        int p1 = Integer.numberOfTrailingZeros(br1.hashCode());
        for (AllenRelation br2 : BASIC_RELATIONS) {
          if (gr2.implies(br2)) {
            int p2 = Integer.numberOfTrailingZeros(br2.hashCode());
            acc = or(acc, BASIC_COMPOSITIONS[p1][p2]);
          }
        }
      }
    }
    return acc;
  }

  public static AllenRelation ompare(TimeInterval p1, TimeInterval p2) {
    Date p1Begin = p1.getStartDate();
    Date p1End   = p1.getEndDate();
    Date p2Begin = p2.getStartDate();
    Date p2End   = p2.getEndDate();
    AllenRelation result = FULL;
    if (p1Begin != null) {
      if (p2Begin != null) {
        if (p1Begin.before(p2Begin)) {
          result = min(result, BEGINS_EARLIER.complement());
        }
        else if (p1Begin.equals(p2Begin)) {
          result = min(result, BEGIN_TOGETHER.complement());
        }
        else {
          assert p1Begin.after(p2Begin);
          result = min(result, BEGINS_LATER.complement());
        }
      }
      if (p2End != null) {
        if (p1Begin.before(p2End)) { // pmoFDseSdfO, not MP; begins before end
          result = min(result, MET_BY);
          result = min(result, PRECEDED_BY);
        }
        else if (p1Begin.equals(p2End)) {
          return MET_BY;
        }
        else {
          assert p1Begin.after(p2End);
          return PRECEDED_BY;
        }
      }
    }
    if (p1End != null) {
      if (p2Begin != null) {
        if (p1End.before(p2Begin)) {
          return PRECEDES;
        }
        else if (p1End.equals(p2Begin)) {
          return MEETS;
        }
        else {
          assert p1End.after(p2Begin); // not pm, oFDseSdfOMP, ends after begin
          result = min(result, PRECEDES);
          result = min(result, MEETS);
        }
      }
      if (p2End != null) {
        if (p1End.before(p2End)) {
          result = min(result, ENDS_EARLIER.complement());
        }
        else if (p1End.equals(p2End)) {
          result = min(result, END_TOGETHER.complement());
        }
        else {
          assert p1End.after(p2End);
          result = min(result, ENDS_LATER);
        }
      }
    }
    return result;
  }

}
