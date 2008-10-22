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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * The {@link Object#equals(Object)} is not overridden, because we want to use this
 * type with reference equality. {@link #hashCode()} is overridden nevertheless,
 * to guarantee a better spread (it also happens to give a peek inside the encapsulation,
 * for people who know the implementation details).
 */
public final class AllenRelation {

  /*<section name="population">*/
  //------------------------------------------------------------------

  /**
   * The total number of possible Allen relations <strong>= {@value}</strong>
   * (i.e., <code>2<sup>13</sup></code>).
   */
  public final static int NR_OF_RELATIONS    = 8192;

  /**
   * All possible Allen relations.
   */
  @Invars({
    @Expression("VALUES != null"),
    @Expression("for (AllenRelation ar : VALUES) {ar != null}"),
    @Expression("for (int i : 0 .. VALUES.length) {for (int j : i + 1 .. VALUES.length) {VALUES[i] != VALUES[j]}}"),
    @Expression("for (AllenRelation ar) {VALUES.contains(ar)}")
  })
  public final static AllenRelation[] VALUES = new AllenRelation[NR_OF_RELATIONS];
  static {
    for (int i = 0; i < NR_OF_RELATIONS; i++) {
      VALUES[i] = new AllenRelation(i);
    }
  }

  /*</section>*/


  /*<section name="basic relations">*/
  //------------------------------------------------------------------

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

  /**
   * This empty relation is not a true Allen relation. It does not express a
   * relational condition between intervals. Yet, it is needed for
   * consistencey with some operations on Allen relations.
   */
  @Invars(@Expression("for (AllenRelation basic : BASIC_RELATIONS) {! EMPTY.implies(basic)}"))
  public final static AllenRelation EMPTY         = VALUES[EMPTY_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>precedes</dfn> an interval <var>I2</var>, i.e., the
   * end of <var>I1</var> is before the begin of <var>I2</var>:
   * <pre>
   *   (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I1.end &lt; I2.begin)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-precedes.png">
   */
  public final static AllenRelation PRECEDES      = VALUES[PRECEDES_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>meets</dfn> an interval <var>I2</var>, i.e., the end
   * of <var>I1</var> is the begin of <var>I2</var>:
   * <pre>
   *   (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I1.end == I2.begin)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-meets.png">
   */
  public final static AllenRelation MEETS         = VALUES[MEETS_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>overlaps</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is earlier than the begin of <var>I2</var>, and
   * the end of <var>I1</var> is later than the begin of <var>I2</var> and
   * earlier than the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &lt; I2.begin) &amp;&amp; (I1.end &gt; I2.begin) &amp;&amp; (I1.end &lt; I2.end)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-overlaps.png">
   */
  public final static AllenRelation OVERLAPS      = VALUES[OVERLAPS_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>is finished by</dfn> an interval <var>I2</var>, i.e.,
   * the begin of <var>I1</var> is earlier than the begin of <var>I2</var>,
   * and the end of <var>I1</var> is the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &lt; I2.begin) &amp;&amp; (I1.end == I2.end)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-finishedBy.png">
   */
  public final static AllenRelation FINISHED_BY   = VALUES[FINISHED_BY_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>contains</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is earlier than the begin of <var>I2</var>, and
   * the end of <var>I1</var> is later than the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &lt; I2.begin) &amp;&amp; (I1.end &gt; I2.end)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-contains.png">
   */
  public final static AllenRelation CONTAINS      = VALUES[CONTAINS_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>starts</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is the begin of <var>I2</var>, and the end of
   * <var>I1</var> is earlier than the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin == I2.begin) &amp;&amp; (I1.end &lt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-starts.png">
   */
  public final static AllenRelation STARTS        = VALUES[STARTS_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>is equal to</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is the begin of <var>I2</var>, and the end of
   * <var>I1</var> is the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin == I2.begin) &amp;&amp; (I1.end == I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-equals.png">
   */
  public final static AllenRelation EQUALS        = VALUES[EQUALS_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>is started by</dfn> an interval <var>I2</var>, i.e.,
   * the begin of <var>I1</var> is the begin of <var>I2</var>, and the end of
   * <var>I1</var> is later than the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin == I2.begin) &amp;&amp; (I1.end &gt; I2.end)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-startedBy.png">
   */
  public final static AllenRelation STARTED_BY    = VALUES[STARTED_BY_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>is during</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is later than the begin of <var>I2</var>, and the
   * end of <var>I1</var> is earlier than the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &gt; I2.begin) &amp;&amp; (I1.end &lt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-during.png">
   */
  public final static AllenRelation DURING        = VALUES[DURING_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>finishes</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is later than the begin of <var>I2</var>, and the
   * end of <var>I1</var> is the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &gt; I2.begin) &amp;&amp; (I1.end == I2.end)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-finishes.png">
   */
  public final static AllenRelation FINISHES      = VALUES[FINISHES_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>is overlapped by</dfn> an interval <var>I2</var>,
   * i.e., the begin of <var>I1</var> is later than the begin of <var>I2</var>
   * and earlier than the end of <var>I2</var>, and the end of <var>I1</var>
   * is later than the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &gt; I2.begin) &amp;&amp; (I1.begin &lt; I2.end) &amp;&amp; (I1.end &gt; I2.end)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-overlappedBy.png">
   */
  public final static AllenRelation OVERLAPPED_BY = VALUES[OVERLAPPED_BY_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>is met by</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I2.end != null) &amp;&amp; (I1.begin == I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-metBy.png">
   */
  public final static AllenRelation MET_BY        = VALUES[MET_BY_BIT_PATTERN];

  /**
   * A <strong>basic</strong> Allen relation that says that an interval
   * <var>I1</var> <dfn>is preceded by</dfn> an interval <var>I2</var>, i.e.,
   * the begin of <var>I1</var> is later than the end of <var>I2</var>:
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I2.end != null) &amp;&amp; (I1.begin &gt; I2.end)
   * </pre>
   * <img style="text-align: center;"
   * src="doc-files/AllenRelation-precededBy.png">
   */
  public final static AllenRelation PRECEDED_BY   = VALUES[PRECEDED_BY_BIT_PATTERN];

  /**
   * The full Allen relation, which expresses that nothing definite can be
   * said about the relationship between 2 periods.
   */
  @Invars(@Expression("FULL == or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY"))
  public final static AllenRelation FULL          = VALUES[FULL_BIT_PATTERN];

  /**
   * The set of all 13 basic Allen relations. That they are presented here in
   * a particular order, is a pleasant side note, but in general not relevant
   * for the user. The list is ordered, first on the first interval beginning
   * before the second (<code><var>I1</var>.begin [&lt;, ==, &gt;]
   * <var>I2</var>.begin</code>) and secondly on the first interval ending
   * before the second (<var><code>I1</code></var><code>.end [&lt;, ==, &gt;]
   * <var>I2</var>.end</code>).
   */
  @Invars({
    @Expression("BASIC_RELATIONS[ 0] == PRECEDES"),
    @Expression("BASIC_RELATIONS[ 1] == MEETS"),
    @Expression("BASIC_RELATIONS[ 2] == OVERLAPS"),
    @Expression("BASIC_RELATIONS[ 3] == FINISHED_BY"),
    @Expression("BASIC_RELATIONS[ 4] == CONTAINS"),
    @Expression("BASIC_RELATIONS[ 5] == STARTS"),
    @Expression("BASIC_RELATIONS[ 6] == EQUALS"),
    @Expression("BASIC_RELATIONS[ 7] == STARTED_BY"),
    @Expression("BASIC_RELATIONS[ 8] == DURING"),
    @Expression("BASIC_RELATIONS[ 9] == FINISHES"),
    @Expression("BASIC_RELATIONS[10] == OVERLAPPED_BY"),
    @Expression("BASIC_RELATIONS[11] == MET_BY"),
    @Expression("BASIC_RELATIONS[12] == PRECEDED_BY")
  })
  public final static List<AllenRelation> BASIC_RELATIONS = new ArrayList<AllenRelation>(13);
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

  /*</section>*/



  /*<section name="secondary relations">*/
  //------------------------------------------------------------------

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * and an interval <var>I2</var> are concurrent in some way.
   * Thus, <var>I1</var> does <em>not</em> precede <var>I2</var>, <var>I1</var> does <em>not</em> meet <var>I2</var>,
   * <var>I1</var> is <em>not</em> met be <var>I2</var>, and <var>I1</var> is <em>not</em> preceded by <var>I2</var>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("CONCURS_WITH == or(OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY)"))
  public final static AllenRelation CONCURS_WITH                     = or(OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins earlier than an interval <var>I2</var> begins:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.begin &lt; I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_EARLIER == or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS)"))
  public static final AllenRelation BEGINS_EARLIER                   = or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * and an interval <var>I2</var> begin at the same time:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.begin == I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGIN_TOGETHER == or(STARTS, EQUALS, STARTED_BY)"))
  public static final AllenRelation BEGIN_TOGETHER                   = or(STARTS, EQUALS, STARTED_BY);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins later than an interval <var>I2</var> begins:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.begin &gt; I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_LATER == or(DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY)"))
  public static final AllenRelation BEGINS_LATER                     = or(DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins inside an interval <var>I2</var>:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I2.end != null) && (I1.begin &gt; I2.begin) && (I1.begin &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_IN == or(DURING, FINISHES, OVERLAPPED_BY)"))
  public static final AllenRelation BEGINS_IN                        = or(DURING, FINISHES, OVERLAPPED_BY);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins earlier and ends earlier than an interval <var>I2</var> begins and ends:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.end != null) && (I2.end != null) && (I1.begin &lt; I2.begin) && (I1.end &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_EARLIER_AND_ENDS_EARLIER == or(PRECEDES, MEETS, OVERLAPS)"))
  private static final AllenRelation BEGINS_EARLIER_AND_ENDS_EARLIER = or(PRECEDES, MEETS, OVERLAPS);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins later and ends later than an interval <var>I2</var> begins and ends:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.end != null) && (I2.end != null) && (I1.begin &gt; I2.begin) && (I1.end &gt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_LATER_AND_ENDS_LATER == or(OVERLAPPED_BY, MET_BY, PRECEDED_BY)"))
  public static final AllenRelation BEGINS_LATER_AND_ENDS_LATER      = or(OVERLAPPED_BY, MET_BY, PRECEDED_BY);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * ends earlier than an interval <var>I2</var> ends:
   * <pre>
   *   (I1.end != null) && (I2.end != null) && (I1.end &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("ENDS_EARLIER == or(PRECEDES, MEETS, OVERLAPS, STARTS, DURING)"))
  private static final AllenRelation ENDS_EARLIER                    = or(PRECEDES, MEETS, OVERLAPS, STARTS, DURING);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * ends inside an interval <var>I2</var>:
   * <pre>
   *   (I1.end != null) && (I2.begin != null) && (I2.end != null) && (I1.end &gt; I2.begin) && (I1.end &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("ENDS_IN == or(OVERLAPS, STARTS, DURING)"))
  public static final AllenRelation ENDS_IN                          = or(OVERLAPS, STARTS, DURING);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * and an interval <var>I2</var> end at the same time.
   * <pre>
   *   (I1.end != null) && (I2.end != null) && (I1.end == I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("END_TOGETHER == or(FINISHED_BY, EQUALS, FINISHES)"))
  public static final AllenRelation END_TOGETHER                     = or(FINISHED_BY, EQUALS, FINISHES);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * ends later than an interval <var>I2</var> ends:
   * <pre>
   *   (I1.end != null) && (I2.end != null) && (I1.end &gt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("ENDS_LATER == or(CONTAINS, STARTED_BY, OVERLAPPED_BY, MET_BY, PRECEDED_BY)"))
  public static final AllenRelation ENDS_LATER                       = or(CONTAINS, STARTED_BY, OVERLAPPED_BY, MET_BY, PRECEDED_BY);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * contains the begin of an interval <var>I2</var>:
   * <pre>
   *   (I1.begin != null) && (I1.end != null) && (I2.begin != null) && (I1.begin &lt; I2.begin) && (I1.end &gt; I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("CONTAINS_BEGIN == or(OVERLAPS, FINISHED_BY, CONTAINS)"))
  public static final AllenRelation CONTAINS_BEGIN                   = or(OVERLAPS, FINISHED_BY, CONTAINS);

  /**
   * A non-basic Allen relation that is often handy to use, which expresses that an interval <var>I1</var>
   * contains the end of an interval <var>I2</var>:
   * <pre>
   *   (I1.begin != null) && (I1.end != null) && (I2.end != null) && (I1.begin &lt; I2.end) && (I1.end &gt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("CONTAINS_END == or(CONTAINS, STARTED_BY, OVERLAPPED_BY)"))
  public static final AllenRelation CONTAINS_END                     = or(CONTAINS, STARTED_BY, OVERLAPPED_BY);

  /*</section>*/



  /*<section name="n-ary operations">*/
  //------------------------------------------------------------------

  /**
   * The main factory method for AllenRelations. Although this is intended to create
   * any disjunction of the basic relations, you can use any relation in the argument
   * list.
   */
  @MethodContract(
    post = {
      @Expression("for (AllenRelation br : BASIC_RELATIONS) {exists (AllenRelation ar : gr) {gr.implies(br)} ?? result.implies(br)}")
    }
  )
  public static AllenRelation or(AllenRelation... gr) {
    int acc = 0;
    for (AllenRelation allenRelation : gr) {
      acc |= allenRelation.$bitPattern;
    }
    return VALUES[acc];
  }

  /*</section>*/



  /*construction>*/
  //------------------------------------------------------------------

  /**
   * There is only 1 private constructor, that constructs the wrapper object
   * around the bitpattern. This is used exclusively in {@link #VALUES} initialization code.
   */
  private AllenRelation(int bitPattern) {
    $bitPattern = bitPattern;
  }

  /*</construction>*/

  /**
   * Only the 13 lowest bits are used. The other (32 - 13 = 19 bits) are 0.
   */
  private final int $bitPattern;





  /**
   * This is a basic Allen relation.
   */
  @MethodContract(post = @Expression("BASIC_RELATIONS.contains(this)"))
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




  /*</section>*/



  /*<section name="n-ary operations">*/
  //------------------------------------------------------------------


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
    Date p1Begin = p1.getBegin();
    Date p1End   = p1.getEnd();
    Date p2Begin = p2.getBegin();
    Date p2End   = p2.getEnd();
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

  @Override
  public final int hashCode() {
    /*
     * this returns the internal representation of this object: it is a way for people
     * that know about the implementation to see the bit pattern of this Allen relation
     */
    return $bitPattern;
  }

  private final static String[] BASIC_CODES = {"p", "m", "o", "F", "D", "s", "e", "S", "d", "f", "O", "M", "P"};

  /**
   * This returns a representation of the Allen relation in the most used short notation (pmoFDseSdfOMP).
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("(");
    if (isBasic()) {
      result.append(BASIC_CODES[Integer.numberOfTrailingZeros($bitPattern)]);
    }
    else {
      for (int i = 0; i < BASIC_CODES.length; i++) {
        if (implies(BASIC_RELATIONS.get(i))) {
          result.append(BASIC_CODES[i]);
        }
      }
    }
    result.append(")");
    return result.toString();
  }

}

