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


import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.pre;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

import java.util.Date;

import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Support for reasoning about relations between time intervals and constraints on time intervals.
 *   <strong>We highly advise to use this class when working with relations between time intervals.
 *   Reasoning about relations between time intervals is treacherously difficult.</strong></p>
 * <p>When working with time intervals, we often want to express constraints (invariants) that limit
 *   acceptable intervals. Expressing this correctly proves extremely difficult in practice. Falling
 *   back to working with isolated begin and end dates, and reasoning about their relations, in
 *   practice proves to be even much more difficult and error prone.</p>
 * <p>This problem was tackled in 1983 by James Allen
 *   (<a href="http://www.cs.brandeis.edu/~cs101a/readings/allen-1983.pdf"><cite>Allen, James F. &quot;Maintaining knowledge
 *   about temporal intervals&quot;; Communications of the ACM 26(11) pages 832-843; November 1983</cite></a>).
 *   A good synopsis of this theory is
 *   <a href="http://www.isr.uci.edu/~alspaugh/foundations/allen.html"><cite>Thomas A. Alspaugh &quot;Allen's interval
 *   algebra&quot;</cite></a>. This class implements this theory, and in this text we give some guidelines
 *   on how to use this class.</p>
 *
 * <h3>Quick overview</h3>
 * <p>Allen found that there are 13 <em>basic relations</em> possible between 2 definite time intervals:</p>
 * <table>
 *   <tr>
 *     <td><code><var>I1</var> {@link #PRECEDES} <var>I2</var></code> </td>
 *     <td><img src="doc-files/AllenRelation-precedes.png" width="296" /></td>
 *     <td><b>p</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #MEETS} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-meets.png" width="296" /></td>
 *     <td><b>m</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #OVERLAPS} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-overlaps.png" width="296" /></td>
 *     <td><b>o</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #FINISHED_BY} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-finishedBy.png" width="296" /></td>
 *     <td><b>F</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #CONTAINS} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-contains.png" width="296" /></td>
 *     <td><b>D</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #STARTS} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-starts.png" width="296" /></td>
 *     <td><b>s</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #EQUALS} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-equals.png" width="296" /></td>
 *     <td><b>e</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #STARTED_BY} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-startedBy.png" width="296" /></td>
 *     <td><b>S</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #DURING} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-during.png" width="296" /></td>
 *     <td><b>d</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #FINISHES} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-finishes.png" width="296" /></td>
 *     <td><b>f</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #OVERLAPPED_BY} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-overlappedBy.png" width="296" /></td>
 *     <td><b>O</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #MET_BY} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-metBy.png" width="296" /></td>
 *     <td><b>M</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>I1</var> {@link #PRECEDED_BY} <var>I2</var></code></td>
 *     <td><img src="doc-files/AllenRelation-precededBy.png" width="296" /></td>
 *     <td><b>P</b></td>
 *   </tr>
 * </table>
 * <ul>
 * </ul>
 * <p>These basic relations can be compared to <code>&lt;</code>, <code>==</code> and <code>></code>
 *   with time instances.</p>
 * <p>When reasoning about the relationship between time intervals however, like when comparing time instances,
 *   we also often employ indeterminate relations, such as
 *   <code><var>I1</var> ({@link #PRECEDES} || {@link #MEETS}) <var>I2</var></code>. This is comparable to
 *   reasoning with <code>&le;</code>, <code>&ge;</code> and <code>!=</code> with time instances.
 *   For time intervals, given 13 basic relations, we get 8192 (== 2<sup>13</sup>) possible <em>general
 *   relations</em>. This includes the {@link #EMPTY empty relationship} for algebraic reasons, and the
 *   {@link #FULL full relationship} (comparable to <code>&lt; || == || &gt;</code> with time instances),
 *   which expresses the maximal uncertainty about the relation between 2 time intervals.</p>
 *
 * <h3>Interval constraints</h3>
 * <p>Time interval relations will most often be used in business code to constrain relations between time intervals.
 *  This is notoriously, treacherously difficult. It is for this reason that you should use code like this,
 *  that at least forces you to think things trough, and tries to offers tools to ease reasoning. The idiom
 *  todo this is explained next.</p>
 * <p>First we need to determine the relation we want to uphold (<code><var>cond</var></code>). E.g., we want
 *  to assert that 2 given intervals <code><var>I1</var></code> and <code><var>I2</var></code> do not concur.
 *  The relationship that expresses this is <code><var>cond</var> == {@link #CONCURS_WITH}.complement()</code>.</p>
 * <p>Next, we want to determine the relationship from <code><var>I1</var></code> to <code><var>I2</var></code>
 *  as precisely as possible. If both <code><var>I1</var></code> and <code><var>I2</var></code> are completely
 *  determined, i.e., neither their begin date nor their end date is {@code null}, the result will be a
 *  {@link #BASIC_RELATIONS basic relation}. Otherwise, the result will be a less certain relation. To determine
 *  this relationship, use {@link #timeIntervalRelation(TimeInterval, TimeInterval)}. See below for dealing
 *  with constrained begin and end dates.</p>
 * <p>The idiom for the assertion we want to express is then:</p>
 * <pre>
 *   timeIntervalRelation(<var>I1</var>, <var>I2</var>).implies(<var>cond</var>)
 * </pre>
 * <p>This is often the form of an invariant. Note that this can fail, on the one hand because the actual
 *   relation is not acceptable, but also because <em>we cannot be 100% sure that the actual relationship
 *   satisfies the condition</em>. In our example, we would have:</p>
 * <pre>
 *   timeIntervalRelation(<var>I1</var>, <var>I2</var>).implies(CONCURS_WITH.complement())
 * </pre>
 * <p>{@link #CONCURS_WITH} being {@code (oFDseSdfO)}, <code>CONCURS_WITH.complement() == (pmMP)</code>.
 *  If the actual relation results in {@code (e)}, e.g., the constraint is clearly not satisfied. If
 *  the actual relation results in {@code (OMP)} for example, it means that it is possible that the relation
 *  is satisfied, but there is also a chance that it is not (if <code><var>I1</var></code> begins before
 *  <code><var>I2</var></code> ends).</p>
 * <p>In code then, we often want to throw an exception to interrupt an algorithm that would violate the
 *   invariant. The idiom for this is usually of the form:</p>
 * <pre>
 *   ...
 *   TimeInterval i1 = ...;
 *   TimeInterval i2 = ...;
 *   TimeIntervalRelation condition = ...;
 *   TimeIntervalRelation actual = timeIntervalRelation(i1, i2);
 *   if (! actual.implies(condition)) {
 *     throw new ....
 *   }
 *   ...
 * </pre>
 * <p>In our example, this would become</p>
 * <pre>
 *   ...
 *   TimeInterval i1 = ...;
 *   TimeInterval i2 = ...;
 *   if (! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())) {
 *     throw new ....
 *   }
 *   ...
 * </pre>
 * <p><strong>Note that in general {@code (! actual.implies(condition))} is <em>not equivalent</em> with
 *   {@code actual.implies(condition.complement())} (see {@link #complement()}).</strong> In our example
 *   this is already clear. If the actual relation results in {@code (e)},
 *   {@code ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())} evaluates to</p>
 * <pre>
 *    ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())
 * == ! (e).implies((pmMP))
 * == ! false
 * == true
 * </pre>
 * <p>and {@code timeIntervalRelation(i1, i2).implies(CONCURS_WITH)} evaluates to</p>
 * <pre>
 *    timeIntervalRelation(i1, i2).implies(CONCURS_WITH)
 * == (e).implies((oFDseSdfO))
 * == true
 * </pre>
 * <p>But in the case where the actual relation results in {@code (OMP)},
 *   {@code ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())} evaluates to</p>
 * <pre>
 *    ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())
 * == ! (OMP).implies((pmMP))
 * == ! false
 * == true
 * </pre>
 * <p>and {@code timeIntervalRelation(i1, i2).implies(CONCURS_WITH)} evaluates to</p>
 * <pre>
 *    timeIntervalRelation(i1, i2).implies(CONCURS_WITH)
 * == (OMP).implies((oFDseSdfO))
 * == <strong>false</strong>
 * </pre>
 * <p>{@code ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())} expresses that [we want to throw an
 *   exception if] <em>it is not guaranteed that <code><var>i1</var></code> and <code><var>i2</var></code> do
 *   not concur</em>. {@code timeIntervalRelation(i1, i2).implies(CONCURS_WITH)} expresses that [we want to throw an
 *   exception if] <em>it is guaranteed that <code><var>i1</var></code> and <code><var>i2</var></code> do
 *   concur</em>. <strong>These 2 phrases are not equivalent.</strong></p>
 *
 * <h3 id="constrainedDates">Reasoning with unknown but constrained begin and end dates</h3>
 * <p>In time intervals, the begin or end end can be {@code null}. The semantics of this is in general that
 *  the begin date, respectively the end date, is unknown. Comparing such an interval with another interval
 *  results in a relatively broad time interval relation, expression an amount of uncertainty.</p>
 * <p>In several use cases however, we do no know a definite begin or end date, but we do know that the
 *  begin or end date have constraints. E.g., consider contracts that have a definite begin date, but are
 *  open ended. The contract interval thus is incompletely known. However, since at the moment of our reasoning
 *  no definite end date is set, we know that the end date is at least later than {@code now}. In comparing
 *  this contract interval with another interval, this constraint can be of use to limit the extend, i.e., the
 *  uncertainty, of the time interval relation. The same applies, e.g., with contracts that will start once payment is
 *  received. Since it is not received yet at the moment of our evaluation, we know that the begin date is at
 *  least later than or equal to {@code now}.</p>
 * <p>In such cases, the interval object <code><var>I</var></code> we are focusing on can be interpreted in
 *  another way. Suppose we are comparing <code><var>I</var></code> with <code><var>Other</var></code>. We are
 *  actually not interested in <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>, but rather in
 *  <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)</code>. Sadly, there is no easy
 *  syntax (or code) to express <code><var>I<sub>constrained</sub></var></code>. What we can express is an
 *  <var>I<sub>determinate</sub></var>, where the border times are filled out in place of the unknown begin
 *  date or end date. <code>timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>)</code>
 *  can be calculated, and will be much less uncertain than
 *  <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>. If now can determine the time interval relation from
 *  <code><var>I<sub>constrained</sub></var></code> to <code><var>I<sub>determinate</sub></var></code>,
 *  we can find <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)</code> as:</p>
 * <pre>
 *   timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>) ==
 *     compose(timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>I<sub>determinate</sub></var>), timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>))
 * </pre>
 * <p>The time interval relation from an interval we are focusing on with constrained semantics to a determinate
 *   interval is a constant that can be determined by reasoning. E.g., for our open ended contract, that lasts
 *   at least longer than today (<code>[I<sub>begin</sub>, &gt; now[</code>, supposing <code>I<sub>begin</sub> &le;
 *   yesterday</code>), we can say that its relation to the determinate interval <code>[I<sub>begin</sub>, now[</code> is
 *   {@code (S)} ({@link #STARTED_BY}). Suppose
 *   <code>timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>) == (o)</code> (say
 *   <code><var>Other</var> == [<var>yesterday</var>, <var>next year</var>[</code>), we can now say
 *   that <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>) == (S).(o) == (oFD)</code>.
 *   The comparison of the indeterminate interval with <code><var>Other</var></code>,
 *   <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>, would have resulted in:</p>
 * <pre>
 *    timeIntervalRelation(<var>I</var>, <var>Other</var>)
 * == timeIntervalRelation([I<sub>begin</sub>, null[, [<var>yesterday</var>, <var>next year</var>[)
 * == (pmoFD)
 * </pre>
 * <p>If you reason directly about <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)</code></p>
 * <pre>
 *    timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)
 * == timeIntervalRelation([I<sub>begin</sub>, &gt; now[, [<var>yesterday</var>, <var>next year</var>[)
 * == (oFD)
 * </pre>
 * <p>you will see that {@code (oFD)} is  indeed the most certain answer.
 * <p>Be aware that in a number of cases, the non-determinate character of <code><var>I</var></code> doesn't matter.
 *   If you suppose in the previous example that
 *   <code>timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>) == (p)</code> (say
 *   <code><var>Other</var> == [<var>next year</var>, Other<sub>end</sub>[</code>),
 *   <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>) == (S).(p) == (pmoFD)</code>.
 *   The comparison of the indeterminate interval with <code><var>Other</var></code>,
 *   <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>, in this case, results in the same time interval relation:</p>
 * <pre>
 *    timeIntervalRelation(<var>I</var>, <var>Other</var>)
 * == timeIntervalRelation([I<sub>begin</sub>, null[, [<var>next year</var>, Other<sub>end</sub>[)
 * == (pmoFD)
 * </pre>
 *
 * <h3>Inference</h3>
 * <p><strong>Be aware that, in general, inference over intervals, also using Allen relations, is NP-complete.</strong>
 *   This means that the time the execution of algorithms will take, is at least difficult to ascertain, and quickly
 *   completely impractical (i.e., with realistic parameters the algorithm would take longer than the universe exists
 *   &mdash; no kidding).</p>
 * <p>There are subsets of the Allen relations for which there exist algorithms that perform much better. These issues
 *   are not implemented here at this time.</p>
 *
 * <h3>About the code</h3>
 * <p>We have chosen to introduce a full-featured type for working with Allen relations, to make encapsulation as
 *   good as possible. This has a slight performance overhead, but we believe that this is worth it, considering the
 *   immense complexity of reasoning about relations between time intervals.</p>
 * <p>Time interval relations are not implemented as a value according to the ppwcode value vernacular, although they do form
 *   an algebra. We presume time interval relations are never shown to end users as values.</p>
 * <p>Time interval relations follow the &quot;8192-fold singleton pattern&quot;. All possible instances are created when this
 *   class is loaded, and it is impossible for a user of the class to create new instances. This means that  reference
 *   equality (&quot;{@code ==}&quot;) can be used to compare time interval relations, Instances are to be obtained
 *   using the constants this class offers, or using the combination methods {@link #or(TimeIntervalRelation...)},
 *   {@link #and(TimeIntervalRelation...)}, {@link #compose(TimeIntervalRelation, TimeIntervalRelation)}, and
 *   {@link #min(TimeIntervalRelation, TimeIntervalRelation)}, and the unary methods {@link #complement()} and {@link #converse()}.
 *   Also, an TimeIntervalRelation can be determined {@link #timeIntervalRelation(TimeInterval, TimeInterval) based on 2 time intervals}.
 *   {@link #VALUES} lists all possible time interval relations.</p>
 * <p>The {@link Object#equals(Object)} is not overridden, because we want to use this type with reference equality.
 *   {@link #hashCode()} is overridden nevertheless, to guarantee a better spread (it also happens to give a peek inside
 *   the encapsulation, for people who know the implementation details).</p>
 * <p>All methods in this class are O(n), i.e., work in constant time, although {@link #compose(TimeIntervalRelation, TimeIntervalRelation)}
 *   takes a significant longer constant time than the other methods.
 */
public final class TimeIntervalRelation {

  /*
   * Implementation note:
   *
   * Time interval relations are implemented as a 13-bit bit pattern, stored in the 13 least significant bits of a 32-bit int.
   * Each of those 13 bits represents a basic relation, being in the general relation ({@code 1}) or not being in the
   * general relation ({@code 0}).
   * The order of the basic relations in the bit pattern is important for some of the algorithms. There is some
   * trickery involved.
   */


  /*<section name="population">*/
  //------------------------------------------------------------------

  /**
   * The total number of possible time interval relations <strong>= {@value}</strong>
   * (i.e., <code>2<sup>13</sup></code>).
   */
  public final static int NR_OF_RELATIONS    = 8192;

  /**
   * All possible time interval relations.
   */
  @Invars({
    @Expression("VALUES != null"),
    @Expression("for (TimeIntervalRelation ar : VALUES) {ar != null}"),
    @Expression("for (int i : 0 .. VALUES.length) {for (int j : i + 1 .. VALUES.length) {VALUES[i] != VALUES[j]}}"),
    @Expression("for (TimeIntervalRelation ar) {VALUES.contains(ar)}")
  })
  public final static TimeIntervalRelation[] VALUES = new TimeIntervalRelation[NR_OF_RELATIONS];
  static {
    for (int i = 0; i < NR_OF_RELATIONS; i++) {
      VALUES[i] = new TimeIntervalRelation(i);
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
   * This empty relation is not a true time interval relation. It does not express a
   * relational condition between intervals. Yet, it is needed for
   * consistencey with some operations on time interval relations.
   * The converse of the empty relation is the empty relation itself.
   */
  @Invars(@Expression("for (TimeIntervalRelation basic : BASIC_RELATIONS) {! EMPTY.impliedBy(basic)}"))
  public final static TimeIntervalRelation EMPTY = VALUES[EMPTY_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>precedes</dfn> an interval <var>I2</var>, i.e., the
   * end of <var>I1</var> is before the begin of <var>I2</var>:</p>
   * <pre>
   *   (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I1.end &lt; I2.begin)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-precedes.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>p</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #PRECEDED_BY}.</p>
   */
  public final static TimeIntervalRelation PRECEDES = VALUES[PRECEDES_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>meets</dfn> an interval <var>I2</var>, i.e., the end
   * of <var>I1</var> is the begin of <var>I2</var>:</p>
   * <pre>
   *   (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I1.end == I2.begin)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-meets.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>m</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #MET_BY}.</p>
   */
  public final static TimeIntervalRelation MEETS = VALUES[MEETS_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>overlaps</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is earlier than the begin of <var>I2</var>, and
   * the end of <var>I1</var> is later than the begin of <var>I2</var> and
   * earlier than the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &lt; I2.begin) &amp;&amp; (I1.end &gt; I2.begin) &amp;&amp; (I1.end &lt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-overlaps.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>o</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #OVERLAPPED_BY}.</p>
   */
  public final static TimeIntervalRelation OVERLAPS = VALUES[OVERLAPS_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>is finished by</dfn> an interval <var>I2</var>, i.e.,
   * the begin of <var>I1</var> is earlier than the begin of <var>I2</var>,
   * and the end of <var>I1</var> is the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &lt; I2.begin) &amp;&amp; (I1.end == I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-finishedBy.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>F</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #FINISHED_BY}.</p>
   */
  public final static TimeIntervalRelation FINISHED_BY = VALUES[FINISHED_BY_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>contains</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is earlier than the begin of <var>I2</var>, and
   * the end of <var>I1</var> is later than the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &lt; I2.begin) &amp;&amp; (I1.end &gt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-contains.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>D</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #DURING}.</p>
   */
  public final static TimeIntervalRelation CONTAINS = VALUES[CONTAINS_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>starts</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is the begin of <var>I2</var>, and the end of
   * <var>I1</var> is earlier than the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin == I2.begin) &amp;&amp; (I1.end &lt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-starts.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>s</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #STARTED_BY}.</p>
   */
  public final static TimeIntervalRelation STARTS = VALUES[STARTS_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>is equal to</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is the begin of <var>I2</var>, and the end of
   * <var>I1</var> is the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin == I2.begin) &amp;&amp; (I1.end == I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-equals.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>e</strong></code>&quot;.</p>
   * <p>The converse of this relation is itself.
   */
  public final static TimeIntervalRelation EQUALS = VALUES[EQUALS_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>is started by</dfn> an interval <var>I2</var>, i.e.,
   * the begin of <var>I1</var> is the begin of <var>I2</var>, and the end of
   * <var>I1</var> is later than the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin == I2.begin) &amp;&amp; (I1.end &gt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-startedBy.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>S</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #STARTS}.</p>
   */
  public final static TimeIntervalRelation STARTED_BY = VALUES[STARTED_BY_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>is during</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is later than the begin of <var>I2</var>, and the
   * end of <var>I1</var> is earlier than the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &gt; I2.begin) &amp;&amp; (I1.end &lt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-during.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>d</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #CONTAINS}.</p>
   */
  public final static TimeIntervalRelation DURING = VALUES[DURING_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>finishes</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is later than the begin of <var>I2</var>, and the
   * end of <var>I1</var> is the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &gt; I2.begin) &amp;&amp; (I1.end == I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-finishes.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>f</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #FINISHES}.</p>
   */
  public final static TimeIntervalRelation FINISHES = VALUES[FINISHES_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>is overlapped by</dfn> an interval <var>I2</var>,
   * i.e., the begin of <var>I1</var> is later than the begin of <var>I2</var>
   * and earlier than the end of <var>I2</var>, and the end of <var>I1</var>
   * is later than the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
   *     (I1.begin &gt; I2.begin) &amp;&amp; (I1.begin &lt; I2.end) &amp;&amp; (I1.end &gt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-overlappedBy.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>O</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #OVERLAPS}.</p>
   */
  public final static TimeIntervalRelation OVERLAPPED_BY = VALUES[OVERLAPPED_BY_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>is met by</dfn> an interval <var>I2</var>, i.e., the
   * begin of <var>I1</var> is the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I2.end != null) &amp;&amp; (I1.begin == I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-metBy.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>M</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #MEETS}.</p>
   */
  public final static TimeIntervalRelation MET_BY = VALUES[MET_BY_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time interval relation that says that an interval
   * <var>I1</var> <dfn>is preceded by</dfn> an interval <var>I2</var>, i.e.,
   * the begin of <var>I1</var> is later than the end of <var>I2</var>:</p>
   * <pre>
   *   (I1.begin != null) &amp;&amp; (I2.end != null) &amp;&amp; (I1.begin &gt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/AllenRelation-precededBy.png">
   * <p>The conventional short representation of this Allen relation is &quot;<code><strong>P</strong></code>&quot;.</p>
   * <p>The converse of this relation is {@link #PRECEDES}.</p>
   */
  public final static TimeIntervalRelation PRECEDED_BY = VALUES[PRECEDED_BY_BIT_PATTERN];

  /**
   * The full time interval relation, which expresses that nothing definite can be
   * said about the relationship between 2 periods.
   * <p>The converse of this relation is the relation itself.
   */
  @Invars(@Expression("FULL == or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY"))
  public final static TimeIntervalRelation FULL = VALUES[FULL_BIT_PATTERN];

  /**
   * The set of all 13 basic time interval relations. That they are presented here in
   * a particular order, is a pleasant side note, but in general not relevant
   * for the user. The list is ordered, first on the first interval beginning
   * before the second (<code><var>I1</var>.begin [&lt;, ==, &gt;]
   * <var>I2</var>.begin</code>) and secondly on the first interval ending
   * before the second (<var><code>I1</code></var><code>.end [&lt;, ==, &gt;]
   * <var>I2</var>.end</code>).
   */
  @Invars({
    @Expression("for (BasicRelation br : BASIC_RELATIONS) {BASIC_RELATIONS[br.basicRelationOrdinal()] == br}"),
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
  public final static TimeIntervalRelation[] BASIC_RELATIONS = {PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS, STARTS,
                                                         EQUALS,
                                                         STARTED_BY, DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY};

  private final static String[] BASIC_CODES = {"p", "m", "o", "F", "D", "s", "e", "S", "d", "f", "O", "M", "P"};

  /*</section>*/



  /*<section name="secondary relations">*/
  //------------------------------------------------------------------

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * and an interval <var>I2</var> are concurrent in some way.
   * Thus, <var>I1</var> does <em>not</em> precede <var>I2</var>, <var>I1</var> does <em>not</em> meet <var>I2</var>,
   * <var>I1</var> is <em>not</em> met be <var>I2</var>, and <var>I1</var> is <em>not</em> preceded by <var>I2</var>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("CONCURS_WITH == or(OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY)"))
  public final static TimeIntervalRelation CONCURS_WITH = or(OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins earlier than an interval <var>I2</var> begins:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.begin &lt; I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_EARLIER == or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS)"))
  public static final TimeIntervalRelation BEGINS_EARLIER = or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * and an interval <var>I2</var> begin at the same time:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.begin == I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGIN_TOGETHER == or(STARTS, EQUALS, STARTED_BY)"))
  public static final TimeIntervalRelation BEGIN_TOGETHER = or(STARTS, EQUALS, STARTED_BY);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins later than an interval <var>I2</var> begins:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.begin &gt; I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_LATER == or(DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY)"))
  public static final TimeIntervalRelation BEGINS_LATER = or(DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins inside an interval <var>I2</var>:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I2.end != null) && (I1.begin &gt; I2.begin) && (I1.begin &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_IN == or(DURING, FINISHES, OVERLAPPED_BY)"))
  public static final TimeIntervalRelation BEGINS_IN = or(DURING, FINISHES, OVERLAPPED_BY);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins earlier and ends earlier than an interval <var>I2</var> begins and ends:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.end != null) && (I2.end != null) && (I1.begin &lt; I2.begin) && (I1.end &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_EARLIER_AND_ENDS_EARLIER == or(PRECEDES, MEETS, OVERLAPS)"))
  public static final TimeIntervalRelation BEGINS_EARLIER_AND_ENDS_EARLIER = or(PRECEDES, MEETS, OVERLAPS);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * begins later and ends later than an interval <var>I2</var> begins and ends:
   * <pre>
   *   (I1.begin != null) && (I2.begin != null) && (I1.end != null) && (I2.end != null) && (I1.begin &gt; I2.begin) && (I1.end &gt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEGINS_LATER_AND_ENDS_LATER == or(OVERLAPPED_BY, MET_BY, PRECEDED_BY)"))
  public static final TimeIntervalRelation BEGINS_LATER_AND_ENDS_LATER = or(OVERLAPPED_BY, MET_BY, PRECEDED_BY);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * ends earlier than an interval <var>I2</var> ends:
   * <pre>
   *   (I1.end != null) && (I2.end != null) && (I1.end &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("ENDS_EARLIER == or(PRECEDES, MEETS, OVERLAPS, STARTS, DURING)"))
  public static final TimeIntervalRelation ENDS_EARLIER = or(PRECEDES, MEETS, OVERLAPS, STARTS, DURING);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * ends inside an interval <var>I2</var>:
   * <pre>
   *   (I1.end != null) && (I2.begin != null) && (I2.end != null) && (I1.end &gt; I2.begin) && (I1.end &lt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("ENDS_IN == or(OVERLAPS, STARTS, DURING)"))
  public static final TimeIntervalRelation ENDS_IN = or(OVERLAPS, STARTS, DURING);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * and an interval <var>I2</var> end at the same time.
   * <pre>
   *   (I1.end != null) && (I2.end != null) && (I1.end == I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("END_TOGETHER == or(FINISHED_BY, EQUALS, FINISHES)"))
  public static final TimeIntervalRelation END_TOGETHER = or(FINISHED_BY, EQUALS, FINISHES);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * ends later than an interval <var>I2</var> ends:
   * <pre>
   *   (I1.end != null) && (I2.end != null) && (I1.end &gt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("ENDS_LATER == or(CONTAINS, STARTED_BY, OVERLAPPED_BY, MET_BY, PRECEDED_BY)"))
  public static final TimeIntervalRelation ENDS_LATER = or(CONTAINS, STARTED_BY, OVERLAPPED_BY, MET_BY, PRECEDED_BY);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * contains the begin of an interval <var>I2</var>:
   * <pre>
   *   (I1.begin != null) && (I1.end != null) && (I2.begin != null) && (I1.begin &lt; I2.begin) && (I1.end &gt; I2.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("CONTAINS_BEGIN == or(OVERLAPS, FINISHED_BY, CONTAINS)"))
  public static final TimeIntervalRelation CONTAINS_BEGIN = or(OVERLAPS, FINISHED_BY, CONTAINS);

  /**
   * A non-basic time interval relation that is often handy to use, which expresses that an interval <var>I1</var>
   * contains the end of an interval <var>I2</var>:
   * <pre>
   *   (I1.begin != null) && (I1.end != null) && (I2.end != null) && (I1.begin &lt; I2.end) && (I1.end &gt; I2.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("CONTAINS_END == or(CONTAINS, STARTED_BY, OVERLAPPED_BY)"))
  public static final TimeIntervalRelation CONTAINS_END = or(CONTAINS, STARTED_BY, OVERLAPPED_BY);

  /*</section>*/



  /*<section name="n-ary operations">*/
  //------------------------------------------------------------------

  /**
   * The main factory method for AllenRelations. Although this is intended to create
   * any disjunction of the basic relations, you can use any relation in the argument
   * list. This is the union of all time interval relations in {@code gr}, when they are considered
   * as sets of basic relations.
   */
  @MethodContract(post = {
    @Expression("for (TimeIntervalRelation br : BASIC_RELATIONS) {exists (TimeIntervalRelation ar : _gr) {ar.impliedBy(br)} ?? result.impliedBy(br)}")
  })
  public static TimeIntervalRelation or(TimeIntervalRelation... gr) {
    int acc = EMPTY_BIT_PATTERN;
    for (TimeIntervalRelation tir : gr) {
      acc |= tir.$bitPattern;
    }
    return VALUES[acc];
  }

  /**
   * The conjunction of the time interval relations in {@code gr}.
   * This is the intersection of all time interval relations in {@code gr}, when they are considered
   * as sets of basic relations.
   */
  @MethodContract(post = {
    @Expression("for (TimeIntervalRelation br : BASIC_RELATIONS) {for (TimeIntervalRelation ar : _gr) {ar.impliedBy(br)} ?? result.impliedBy(br)}")
  })
  public static TimeIntervalRelation and(TimeIntervalRelation... gr) {
    int acc = FULL_BIT_PATTERN;
    for (TimeIntervalRelation tir : gr) {
      acc &= tir.$bitPattern;
    }
    return VALUES[acc];
  }

  /**
   * Remove basic relations in {@code gr2} from {@code gr1}.
   */
  @MethodContract(
    pre  = {
      @Expression("_base != null"),
      @Expression("_term != null")
    },
    post = @Expression("for (TimeIntervalRelation br : BASIC_RELATIONS) {br.implies(result) ?? br.implies(_base) && ! br.implies(_term)}")
  )
  public static TimeIntervalRelation min(TimeIntervalRelation base, TimeIntervalRelation term) {
    assert preArgumentNotNull(base, "base");
    assert preArgumentNotNull(term, "term");
    int xor = base.$bitPattern ^ term.$bitPattern;
    int min = base.$bitPattern & xor;
    return VALUES[min];
  }

  /**
   * This matrix holds the compositions of basic time interval relations. These are part of the given semantics, and cannot be
   * calculated. See {@link #compose(TimeIntervalRelation, TimeIntervalRelation)}.
   */
  public final static TimeIntervalRelation[][] BASIC_COMPOSITIONS =
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
   * <p>Given 3 time intervals <code><var>I1</var></code>, <code><var>I2</var></code> and <code><var>I3</var></code>,
   *   given <code>gr1 == timeIntervalRelation(<var>I1</var>, <var>I2</var>)</code> and <code>gr2 ==
   *   timeIntervalRelation(<var>I2</var>, <var>I3</var>)</code>, <code>compose(gr1, gr2) == timeIntervalRelation(<var>I1</var>,
   *   <var>I3</var>)</code>.</p>
   * <p>Although this method is still, like most other methods in this class, of constant time (<em>O(n)</em>), it
   *   takes a significant longer constant time, namely ~ 13<sup>2</sup>. During unit tests we saw that 100 000 calls
   *   take over a second on a 2.4GHz dual core processor.</p>
   */
  @MethodContract(
    pre  = {
      @Expression("_gr1 != null"),
      @Expression("_gr2 != null")
    },
    post = {
      @Expression("for (TimeIntervalRelation br1 : BASIC_RELATIONS) {for (TimeIntervalRelation br2: BASIC_RELATIONS) {" +
                    "br1.implies(_gr1) && br2.implies(_gr2) ? result.impliedBy(BASIC_COMPOSITIONS[br1.basicRelationOrdinal()][br2.basicRelationOrdinal()])" +
                  "}}")
  })
  public static TimeIntervalRelation compose(TimeIntervalRelation gr1, TimeIntervalRelation gr2) {
    assert preArgumentNotNull(gr1, "gr1");
    assert preArgumentNotNull(gr2, "gr2");
    TimeIntervalRelation acc = EMPTY;
    for (TimeIntervalRelation br1 : BASIC_RELATIONS) {
      if (gr1.impliedBy(br1)) {
        for (TimeIntervalRelation br2 : BASIC_RELATIONS) {
          if (gr2.impliedBy(br2)) {
            acc = or(acc, BASIC_COMPOSITIONS[br1.basicRelationOrdinal()][br2.basicRelationOrdinal()]);
          }
        }
      }
    }
    return acc;
  }

  /**
   * The relation from {@code i1} to {@code i2} with the lowest possible {@link #uncertainty()}.
   * {@code null} as {@link TimeInterval#getBegin()} or {@link TimeInterval#getEnd()} is considered
   * as unknown, and thus is not used to restrict the relation more, leaving it with
   * more {@link #uncertainty()}.
   *
   * @mudo contract
   */
  public static TimeIntervalRelation timeIntervalRelation(TimeInterval i1, TimeInterval i2) {
    Date i1Begin = i1.getBegin();
    Date i1End = i1.getEnd();
    Date i2Begin = i2.getBegin();
    Date i2End = i2.getEnd();
    TimeIntervalRelation result = FULL;
    if (i1Begin != null) {
      if (i2Begin != null) {
        if (i1Begin.before(i2Begin)) {
          result = min(result, BEGINS_EARLIER.complement());
        }
        else if (i1Begin.equals(i2Begin)) {
          result = min(result, BEGIN_TOGETHER.complement());
        }
        else {
          assert i1Begin.after(i2Begin);
          result = min(result, BEGINS_LATER.complement());
        }
      }
      if (i2End != null) {
        if (i1Begin.before(i2End)) { // pmoFDseSdfO, not MP; begins before end
          result = min(result, MET_BY);
          result = min(result, PRECEDED_BY);
        }
        else if (i1Begin.equals(i2End)) {
          if (i1End != null && i2Begin != null && i1End.equals(i2Begin)) {
            assert i1Begin.equals(i1End);
            assert i2Begin.equals(i2End);
            return EQUALS;
          }
          else {
            return MET_BY;
          }
        }
        else {
          assert i1Begin.after(i2End);
          return PRECEDED_BY;
        }
      }
    }
    if (i1End != null) {
      if (i2Begin != null) {
        if (i1End.before(i2Begin)) {
          return PRECEDES;
        }
        else if (i1End.equals(i2Begin)) {
          if (i1Begin != null && i2End != null && i1Begin.equals(i2End)) {
            assert i1Begin.equals(i1End);
            assert i2Begin.equals(i2End);
            return EQUALS;
          }
          return MEETS;
        }
        else {
          assert i1End.after(i2Begin); // not pm, oFDseSdfOMP, ends after begin
          result = min(result, PRECEDES);
          result = min(result, MEETS);
        }
      }
      if (i2End != null) {
        if (i1End.before(i2End)) {
          result = min(result, ENDS_EARLIER.complement());
        }
        else if (i1End.equals(i2End)) {
          result = min(result, END_TOGETHER.complement());
        }
        else {
          assert i1End.after(i2End);
          result = min(result, ENDS_LATER);
        }
      }
    }
    return result;
  }

  /*</section>*/



  /*construction>*/
  //------------------------------------------------------------------

  /**
   * There is only 1 private constructor, that constructs the wrapper object
   * around the bitpattern. This is used exclusively in {@link #VALUES} initialization code.
   */
  @MethodContract(
    pre  = {
      @Expression("_bitPattern >= EMPTY_BIT_PATTERN"),
      @Expression("_bitPattern <= FULL_BIT_PATTERN")
    },
    post = @Expression("$bitpattern == bitPattern")
  )
  private TimeIntervalRelation(int bitPattern) {
    assert pre(bitPattern >= EMPTY_BIT_PATTERN);
    assert pre(bitPattern <= FULL_BIT_PATTERN);
    $bitPattern = bitPattern;
  }

  /*</construction>*/



  /**
   * Only the 13 lowest bits are used. The other (32 - 13 = 19 bits) are 0.
   */
  @Invars({
    @Expression("$bitPattern >= EMPTY_BIT_PATTERN"),
    @Expression("$bitPattern <= FULL_BIT_PATTERN")
  })
  private final int $bitPattern;



  /*<section name="instance operations">*/
  //------------------------------------------------------------------

  /**
   * An ordinal for basic relations.
   */
  @Basic(pre    = @Expression("isBasic()"),
         invars = {@Expression("result >= 0"), @Expression("result < 13")})
  public int basicRelationOrdinal() {
    /*
     * This is the bit position, 0-based, in the 13-bit bit pattern, of the bit
     * representing this as basic relation.
     */
    assert pre(isBasic());
    return Integer.numberOfTrailingZeros($bitPattern);
  }

  /**
   * This is a basic time interval relation.
   */
  @MethodContract(post = @Expression("BASIC_RELATIONS.contains(this)"))
  public final boolean isBasic() {
    return isBasicBitPattern($bitPattern);
  }

  /**
   * A basic relation is expressed by a single bit in the bit pattern.
   */
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

  /**
   * A measure about the uncertainty this time interval relation expresses.
   * This is the fraction of the 13 basic relations that imply this general relation.
   * {@link #FULL} is complete uncertainty, and returns {@code 1}.
   * A basic relation is complete certainty, and returns {@code 0}.
   * The {@link #EMPTY empty} relation has no meaningful uncertainty. This method returns
   * {@link Float#NaN} as value for {@link #EMPTY}.
   */
  @MethodContract(post = {
    @Expression("this != EMPTY ? result == count (TimeIntervalRelation br : BASIC_RELATIONS) {br.implies(this)} - 1) / 12"),
    @Expression("this == EMPTY ? result == Float.NaN")
  })
  public float uncertainty() {
    int count = Integer.bitCount($bitPattern);
    if (count == 0) {
      return Float.NaN;
    }
    count--;
    float result = count / 12.0F;
    return result;
  }

  /**
   * The <dfn>converse</dfn> of an time interval relation from an interval <var>I1</var> to an interval <var>I2</var>
   * is the relation from the interval <var>I2</var> to the interval <var>I1</var>:
   * <pre>
   *   compare(I1, I2).converse() == compare(I2, I1)
   * </pre>
   * The converse of a basic time interval relation is defined. The converse of a general time interval relation <var>A</var>
   * is the disjunction of the converse relations of the basic relations that are implied by <var>A</var>.
   */
  @MethodContract(post = {
    @Expression("converse().converse() == this"),
    @Expression("this == PRECEDES ? converse() == PRECEDED_BY"),
    @Expression("this == MEETS ? converse() == MET_BY"),
    @Expression("this == OVERLAPS ? converse() == OVERLAPPED_BY"),
    @Expression("this == FINISHED_BY ? converse() == FINISHES"),
    @Expression("this == CONTAINS ? converse() == DURING"),
    @Expression("this == STARTS ? converse() == STARTED_BY"),
    @Expression("this == EQUALS ? converse() == EQUALS"),
    @Expression("this == STARTED_BY ? converse() == STARTS"),
    @Expression("this == DURING ? converse() == CONTAINS"),
    @Expression("this == FINISHES ? converse() == FINISHED_BY"),
    @Expression("this == OVERLAPPED_BY ? converse() == OVERLAPS"),
    @Expression("this == MET_BY ? converse() == MEETS"),
    @Expression("this == PRECEDED_BY ? converse() == PRECEDES"),
    @Expression("for (TimeIntervalRelation br : BASIC_RELATIONS) {impliedBy(br) ?? converse().impliedBy(br.converse())}")
  })
  public final TimeIntervalRelation converse() {
    /*
     * Given the current order in which the basic relations occur in the bit pattern,
     * the converse is the reverse bit pattern (read the bit pattern from left to right
     * instead of right to left). We need to add a 19 bit shift to compensate for the fact
     * that we store the 13 bit bitpattern in a 32 bit int.
     */
    int pattern = Integer.reverse($bitPattern);
    pattern >>>= 19; // 32 - 13 = 19
    return VALUES[pattern];
  }

  /**
   * <p>The complement of an time interval relation is the logic negation of the condition the time interval relation expresses.
   *   The complement of a basic time interval relation is the disjunction of all the other basic time interval relations.
   *   The complement of a general time interval relation is the disjunction of all basic time interval relations that are
   *   not implied by the general time interval relation.</p>
   * <p>This method is key to validating semantic constraints on time intervals, using the following idiom:</p>
   * <pre>
   *   ...
   *   TimeInterval i1 = ...;
   *   TimeInterval i2 = ...;
   *   TimeIntervalRelation condition = ...;
   *   TimeIntervalRelation actual = timeIntervalRelation(i1, i2);
   *   if (! actual.implies(condition)) {
   *     throw new ....
   *   }
   *   ...
   * </pre>
   * <p><strong>Be aware that the complement has in general not the same meaning as a logic negation.</strong>
   *   For a basic relation <var>br</var> and a general time interval relation <var>cond</var>, it is true that</p>
   * <p><code><var>br</var>.implies(<var>cond</var>)</code> &hArr;
   *   <code>! <var>br</var>.implies(<var>cond</var>.complement())</code></p>
   * <p><strong>This is however not so for non-basic, and thus general time interval relations</strong>, as the following
   *   counterexample proofs. Suppose a condition is that, for a general relation <var>gr</var>:</p>
   * <pre><var>gr</var>.implies(<var>cond</var>)</pre>
   * <p>Suppose <code><var>gr</var> == (mFO)</code>. Then we can rewrite in the following way:</p>
   * <p>&nbsp;&nbsp;&nbsp;<code><var>gr</var>.implies(<var>cond</var>)</code><br />
   *   &hArr; <code>(mFO).implies(<var>cond</var>)</code><br />
   *   &hArr; <code>(mFO) &sube; <var>cond</var></code><br />
   *   &hArr; <code>(mFO) &sube; <var>cond</var></code><br />
   *   &hArr; <code>(m &isin; <var>cond</var>) && (F &isin; <var>cond</var>) && (O &isin; <var>cond</var>)</code></p>
   * <p>From the definition of the complement, it follows that, for a basic relation <var>br</var> and a general
   *   relation <var>GR</var> as set</p>
   * <p><code>br &isin; GR</code> &hArr; <code>br &notin; GR.complement()</code></p>
   * <p>Thus:</p>
   * <p>&hArr; <code>(m &notin; <var>cond</var>.complement()) && (F &notin; <var>cond</var>.complement()) &&
   *   (O &notin; <var>cond</var>.complement())</code><br />
   *   &hArr; <code>! ((m &isin; <var>cond</var>.complement()) || (F &isin; <var>cond</var>.complement()) ||
   *   (O &isin; <var>cond</var>.complement())</code> (1)</p>
   * <p>While, from the other side:</p>
   * <p>&nbsp;&nbsp;&nbsp;<code>! <var>gr</var>.implies(<var>cond</var>.complement())</code><br />
   *   &hArr; <code>! (mFO).implies(<var>cond</var>.complement())</code><br />
   *   &hArr; <code>! (mFO) &sube; (<var>cond</var>.complement())</code><br />
   *   &hArr; <code>! ((m &isin; <var>cond</var>.complement()) && (F &isin; <var>cond</var>.complement()) &&
   *   (O &isin; <var>cond</var>.complement()))</code> (2)</p>
   * <p>It is clear that (1) is incompatible with (2), except for the case where the initial relation is basic.</p>
   * <p>In the reverse case, for a basic relation <var>br</var> and a general time interval relation <var>actual</var>, nothing
   *   special can be said about the complement of <var>actual</var>, as the following reasoning illustrates:</p>
   * <p>&nbsp;&nbsp;&nbsp;<code><var>actual</var>.implies(<var>br</var>)</code><br />
   *   &hArr;<code><var>actual</var> &sube; <var>br</var></code><br />
   *   &hArr;<code><var>actual</var> &sube; (<var>br</var>)</code><br />
   *   &hArr;<code><var>actual</var> == (<var>br</var>) || <var>actual</var> == &empty;</code><br />
   *   &hArr;<code><var>actual</var>.complement() == (<var>br</var>).complement() || <var>actual</var>.complement() == FULL</code> (3)</p>
   * <p>From the other side:</p>
   * <p>&nbsp;&nbsp;&nbsp;<code>! <var>actual</var>.complement().implies(<var>br</var>)</code><br />
   *   &hArr;<code>! (<var>actual</var>.complement() &sube; <var>br</var>)</code><br />
   *   &hArr;<code>! (<var>actual</var>.complement() &sube; (<var>br</var>))</code><br />
   *   &hArr;<code>! (<var>actual</var>.complement() == (<var>br</var>) || <var>actual</var>.complement() == &empty;)</code><br />
   *   &hArr;<code><var>actual</var>.complement() != (<var>br</var>) && <var>actual</var>.complement() != &empty;</code> (4)</p>
   * <p>It is clear that (3) expresses something completely different then (4), and this effect is obviously even stronger with
   *   non-basic relations.</p>
   * <p>Note that it is exactly this counter-intuitivity that makes reasoning with time intervals so difficult.</p>
   */
  @MethodContract(post = {
    @Expression("for (TimeIntervalRelation br : BASIC_RELATIONS) {" +
                  "(impliedBy(br) ?? ! complement().impliedBy(br)) && (! impliedBy(br) ?? complement().impliedBy(br))" +
                "}")
  })
  public final TimeIntervalRelation complement() {
    /*
     * implemented as the XOR of the FULL bit pattern with this bit pattern;
     * this simply replaces 0 with 1 and 1 with 0.
     */
    int full = FULL.$bitPattern;
    int result = full ^ $bitPattern;
    return VALUES[result];
  }

  /**
   * Is {@code this} implied by {@code gr}? In other words, when considering the relations as a set
   * of basic relations, is {@code this} a superset of {@code gr} (considering equality as also acceptable)?
   */
  @Basic(
    pre = @Expression("_gr != null"),
    invars = {
      @Expression("impliedBy(this)"),
      @Expression("basic ? for (TimeIntervalRelation br : BASIC_RELATIONS) : {br != this ? ! impliedBy(br)}"),
      @Expression("for (TimeIntervalRelation gr) {impliedBy(gr) == for (TimeIntervalRelation br : BASIC_RELATIONS) : {gr.impliedBy(br) ? impliedBy(br)}")
    }
  )
  public final boolean impliedBy(TimeIntervalRelation gr) {
    assert preArgumentNotNull(gr, "gr");
    return ($bitPattern & gr.$bitPattern) == gr.$bitPattern;
  }

  /**
   * Does {@code this} imply {@code gr}? In other words, when considering the relations as a set
   * of basic relations, is {@code this} a subset of {@code gr} (considering equality as also acceptable)?
   */
  @Basic(
    pre = @Expression("_gr != null"),
    invars = @Expression("_gr.impliedBy(this)")
  )
  public final boolean implies(TimeIntervalRelation gr) {
    assert preArgumentNotNull(gr, "gr");
    return (gr.$bitPattern & $bitPattern) == $bitPattern;
  }

  /*</section>*/



  @Override
  public final int hashCode() {
    /*
     * this returns the internal representation of this object: it is a way for people
     * that know about the implementation to see the bit pattern of this time interval relation
     */
    return $bitPattern;
  }

  /**
   * This returns a representation of the Allen relation in the most used short notation (pmoFDseSdfOMP).
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("(");
    if (isBasic()) {
      result.append(BASIC_CODES[basicRelationOrdinal()]);
    }
    else {
      for (int i = 0; i < BASIC_CODES.length; i++) {
        if (impliedBy(BASIC_RELATIONS[i])) {
          result.append(BASIC_CODES[i]);
        }
      }
    }
    result.append(")");
    return result.toString();
  }

}

