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
 * <p>Support for reasoning about relations between time points and time intervals, and constraints on those
 *   relationships. <strong>We strongly advise to use this class when working with relations between time
 *   points and time intervals. Reasoning about relations between time points and time intervals is
 *   treacherously difficult.</strong></p>
 * <p>When working with time points and time intervals, we often want to express constraints (invariants)
 *   that limit acceptable combinations. Expressing this correctly proves extremely difficult in practice.
 *   Falling back to working with isolated begin and end dates, and reasoning about their relations with
 *   the time point, in practice proves to be even much more difficult and error prone.</p>
 * <p>This class is developed following the example of {@link TimeIntervalRelation Allen relations}.</p>
 *
 * <h3>Quick overview</h3>
 * <p>We find that there are 5 <em>basic relations</em> possible between a point in time and a definite time
 *   interval:</p>
 * <table>
 *   <tr>
 *     <td><code><var>t</var> {@link #BEFORE} <var>I</var></code></td>
 *     <td><img src="doc-files/TimePointIntervalRelation-before.png" width="296" /></td>
 *     <td><b>&lt;</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>t</var> {@link #BEGINS} <var>I</var></code></td>
 *     <td><img src="doc-files/TimePointIntervalRelation-begins.png" width="296" /></td>
 *     <td><b>=[&lt;</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>t</var> {@link #IN} <var>I</var></code></td>
 *     <td><img src="doc-files/TimePointIntervalRelation-in.png" width="296" /></td>
 *     <td><b>&gt;&lt;</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>t</var> {@link #ENDS} <var>I</var></code></td>
 *     <td><img src="doc-files/TimePointIntervalRelation-ends.png" width="296" /></td>
 *     <td><b>=[&gt;</b></td>
 *   </tr>
 *   <tr>
 *     <td><code><var>t</var> {@link #AFTER} <var>I</var></code></td>
 *     <td><img src="doc-files/TimePointIntervalRelation-after.png" width="296" /></td>
 *     <td><b>&gt;</b></td>
 *   </tr>
 * </table>
 * <p>These basic relations can be compared to <code>&lt;</code>, <code>==</code> and <code>></code>
 *   with time instances.</p>
 * <p>When reasoning about the relationship between time intervals however, like when comparing time instances,
 *   we also often employ indeterminate relations, such as
 *   <code><var>t</var> ({@link #BEFORE} || {@link #BEGINS}) <var>I</var></code>. This is comparable to
 *   reasoning with <code>&le;</code>, <code>&ge;</code> and <code>!=</code> with time instances.
 *   For time intervals, given 5 basic relations, we get 32 (== 2<sup>5</sup>) possible <em>general
 *   relations</em>. This includes the {@link #EMPTY empty relationship} for algebraic reasons, and the
 *   {@link #FULL full relationship} (comparable to <code>&lt; || == || &gt;</code> with time instances),
 *   which expresses the maximal uncertainty about the relation between a point in time and time intervals.</p>
 *
 * <h3>Interval constraints</h3>
 * <p>Time point-interval relations will most often be used in business code to constrain relations between
 *  time points and time intervals.
 *  This is notoriously, treacherously difficult. It is for this reason that you should use code like this,
 *  that at least forces you to think things trough, and tries to offers tools to ease reasoning. The idiom
 *  todo this is explained next.</p>
 * <p>First we need to determine the relation we want to uphold (<code><var>cond</var></code>). E.g., we want
 *  to assert that given a point in time <code><var>t</var></code> does not fall before a time interval
 *  <code><var>I</var></code> is fully started. The relationship that expresses this is
 *  <code><var>cond</var> == {@link #or}({@link #BEFORE}, {@link #BEGINS})</code>.</p>
 * <p>Next, we want to determine the relationship between <code><var>t</var></code> and <code><var>I</var></code>
 *  as precisely as possible. If <code><var>I</var></code> is completely determined, i.e., neither its begin date
 *  nor its end date is {@code null}, the result will be a {@link #BASIC_RELATIONS basic relation}. Otherwise,
 *  the result will be a less certain relation. To determine this relationship, use
 *  {@link #timePointIntervalRelation(Date, TimeInterval)}. See below for dealing with constrained begin and end
 *  dates.</p>
 * <p>The idiom for the assertion we want to express is then:</p>
 * <pre>
 *   timePointIntervalRelation(<var>t</var>, <var>I</var>).implies(<var>cond</var>)
 * </pre>
 * <p>This is often the form of an invariant. Note that this can fail, on the one hand because the actual
 *   relation is not acceptable, but also because <em>we cannot be 100% sure that the actual relationship
 *   satisfies the condition</em>. In our example, we would have:</p>
 * <pre>
 *   timePointIntervalRelation(<var>t</var>, <var>I</var>).implies(or(BEFORE, BEGINS))
 * </pre>
 * <p><code>or(BEFORE, BEGINS) = (&lt;, =[&lt;)</code>. If the actual relation results in {@code (><)}, e.g.,
 *  the constraint is clearly not satisfied. If the actual relation results in {@code (=[< ><)} for example,
 *  it means that it is possible that the relation is satisfied, but there is also a chance that it is not.</p>
 * <p>In code then, we often want to throw an exception to interrupt an algorithm that would violate the
 *   invariant. The idiom for this is usually of the form:</p>
 * <pre>
 *   ...
 *   Date t = ...;
 *   TimeInterval i = ...;
 *   TimePointIntervalRelation condition = ...;
 *   TimePointIntervalRelation actual = timePointIntervalRelation(t, i);
 *   if (! actual.implies(condition)) {
 *     throw new ....
 *   }
 *   ...
 * </pre>
 * <p>In our example, this would become</p>
 * <pre>
 *   ...
 *   Date t = ...;
 *   TimeInterval i = ...;
 *   if (! timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS)) {
 *     throw new ....
 *   }
 *   ...
 * </pre>
 * <p><strong>Note that in general {@code (! actual.implies(condition))} is <em>not equivalent</em> with
 *   {@code actual.implies(condition.complement())} (see {@link #complement()}).</strong> In our example
 *   this is already clear. If the actual relation results in {@code (><)},
 *   {@code ! timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS))} evaluates to</p>
 * <pre>
 *    ! timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS))
 * == ! (&gt;&lt;).implies((&lt; ==[&lt;))
 * == ! false
 * == true
 * </pre>
 * <p>and {@code timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS).complement())} evaluates to</p>
 * <pre>
 *    timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS).complement())
 * == (&gt;&lt;).implies((&lt; &lt[).complement())
 * == (&gt;&lt;).implies((&gt;&lt; =[&gt; &gt;))
 * == true
 * </pre>
 * <p>But in the case where the actual relation results in {@code (=[< ><)},
 *   {@code ! timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS))} evaluates to</p>
 * <pre>
 *    ! timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS))
 * == ! (=[&lt; &gt;&lt;).implies((&lt; ==[&lt;))
 * == ! false
 * == true
 * </pre>
 * <p>and {@code timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS).complement())} evaluates to</p>
 * <pre>
 *    timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS).complement())
 * == (=[&lt; &gt;&lt;).implies((&lt; ==[&lt;).complement())
 * == (=[&lt; &gt;&lt;).implies((&gt;&lt; =[&gt; &gt;))
 * == <strong>false</strong>
 * </pre>
 * <p>{@code ! timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS))} expresses that [we want to throw an
 *   exception if] <em>it is not guaranteed that <code><var>t</var></code> is before <code><var>i</var></code> is
 *   fully started</em>. {@code timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS).complement())} expresses
 *   that [we want to throw an exception if] <em>it is guaranteed that <code><var>t</var></code> is after
 *   <code><var>i</var></code> is fully started</em>. <strong>These 2 phrases are not equivalent.</strong>
 *   Consider a {@code null} begin for <code><var>i</var></code>. When <code><var>t</var></code> is before the
 *   end of <code><var>i</var></code>, we cannot know whether <code><var>t</var></code> is before,
 *   equal to, or after the begin of <code><var>i</var></code>.
 *   With {@code ! timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS))} we will throw an exception,
 *   with {@code timePointIntervalRelation(t, i).implies(or(BEFORE, BEGINS).complement())} we would not.</p>
 *
 *
 * MUDO LOWER NOT FINISHED
 *
 * <h3 id="constrainedDates">Reasoning with unknown but constrained begin and end dates</h3>
 * <p>In time intervals, the begin or end end can be {@code null}. The semantics of this is in general that
 *  the begin date, respectively the end date, is unknown. Comparing such an interval with a point in time
 *  results in a relatively broad time point-interval relation, expressing an amount of uncertainty.</p>
 * <p>In several use cases however, we do not know a definite begin or end date, but we do know that the
 *  begin or end date have constraints. E.g., consider contracts that have a definite begin date, but are
 *  open ended. The contract interval thus is incompletely known. However, since at the moment of our reasoning
 *  no definite end date is set, we know that the end date is at least later than {@code now}. In comparing
 *  this contract interval with another interval, this constraint can be of use to limit the extend, i.e., the
 *  uncertainty, of the time point-interval relation. The same applies, e.g., with contracts that will start once payment is
 *  received. Since it is not received yet at the moment of our evaluation, we know that the begin date is at
 *  least later than or equal to {@code now}.</p>
 * <p>In such cases, the interval object <code><var>I</var></code> we are focusing on can be interpreted in
 *  another way. Suppose we are comparing <code><var>I</var></code> with a point int time <code><var>t</var></code>.
 *  We are actually not interested in <code>timePointIntervalRelation(<var>t</var>, <var>I</var>)</code>, but rather in
 *  <code>timePointIntervalRelation(<var>t</var>, <var>I<sub>constrained</sub></var>)</code>. Sadly, there is no easy
 *  syntax (or code) to express <code><var>I<sub>constrained</sub></var></code>. What we can express is an
 *  <var>I<sub>determinate</sub></var>, where the border times are filled out in place of the unknown begin
 *  date or end date. <code>timePointIntervalRelation(<var>t</var>, <var>I<sub>determinate</sub></var>)</code>
 *  can be calculated, and will be much less uncertain than
 *  <code>timePointIntervalRelation(<var>t</var>, <var>I</var>)</code>. If we now can determine the Allen
 *  relation from <code><var>I<sub>determinate</sub></var></code> to <code><var>I<sub>constrained</sub></var></code> ,
 *  we can find <code>timePointIntervalRelation(<var>t</var>, <var>I<sub>constrained</sub></var>)</code> as:</p>
 * <pre>
 *   timePointIntervalRelation(<var>t</var>, <var>I<sub>constrained</sub></var>) ==
 *     compose(timePointIntervalRelation(<var>t</var>, <var>I<sub>determinate</sub></var>), allenRelation(<var>I<sub>determinate</sub></var>, <var>I<sub>constrained</sub></var>))
 * </pre>
 * <p>The time point-interval relation from an interval we are focusing on with constrained semantics to a determinate
 *   interval is a constant that can be determined by reasoning. E.g., for our open ended contract, that lasts
 *   at least longer than today (<code>[I<sub>begin</sub>, &gt; now[</code>, supposing <code>I<sub>begin</sub> &le;
 *   yesterday</code>), we can say that its relation to the determinate interval <code>[I<sub>begin</sub>, now[</code> is
 *   {@code (s)} ({@link TimeIntervalRelation#STARTS}). Suppose
 *   <code>timePointIntervalRelation(<var>t</var>, <var>I<sub>determinate</sub></var>) == (&gt;&lt;)</code> (say
 *   <code><var>t</var> == <var>yesterday</var></code>), we can now say
 *   that <code>timePointIntervalRelation(<var>t</var>, <var>I<sub>constrained</sub></var>) == (s).(&gt;&tl;) == (&gt;&lt;)</code>.
 *   The comparison of the indeterminate interval with <code><var>t</var></code>,
 *   <code>timePointIntervalRelation(<var>t</var>, <var>I</var>)</code>, would have resulted in:</p>
 * <pre>
 *    timePointIntervalRelation(<var>t</var>, <var>I</var>)
 * == timePointIntervalRelation(<var>yesterday</var>, [I<sub>begin</sub>, null[)
 * == (&gt;&lt; = [&gt; &gt;)
 * </pre>
 * <p>which is indeed the most certain answer.
 * <p>Be aware that in a number of cases, the non-determinate character of <code><var>I</var></code> doesn't matter.
 *   If you suppose in the previous example that
 *   <code>timePointIntervalRelation(<var>t</var>, <var>I<sub>determinate</sub></var>) == (&gt;)</code> (say
 *   <code><var>t</var> == <var>next year</var></code>),
 *   <code>timePointIntervalRelation(<var>t</var>, <var>I<sub>constrained</sub></var>) == (s).(&gt;) == (&gt;&lt; =[&gt; &gt;)</code>.
 *   The comparison of the indeterminate interval with <code><var>t</var></code>,
 *   <code>timePointIntervalRelation(<var>t</var>, <var>I</var>)</code>, in this case, results in the same time point-interval
 *   relation:</p>
 * <pre>
 *    timePointIntervalRelation(<var>t</var>, <var>I</var>)
 * == timePointIntervalRelation(<var>t</var>, [<var>I<sub>begin</sub></var>, null[)
 * == (&gt;&lt; =[&gt; &gt;)
 * </pre>
 *
 * <h3>About the code</h3>
 * <p>We have chosen to introduce a full-featured type for working with time point-interval relations, to make encapsulation as
 *   good as possible. This has a slight performance overhead, but we believe that this is worth it, considering the
 *   immense complexity of reasoning about relations between time intervals.</p>
 * <p>Time point-interval relations are not implemented as a value according to the ppwcode value vernacular, although they do form
 *   an algebra. We presume time point-interval relations are never shown to end users as values.</p>
 * <p>Time point-interval relations follow the &quot;32-fold singleton pattern&quot;. All possible instances are created when this
 *   class is loaded, and it is impossible for a user of the class to create new instances. This means that reference
 *   equality (&quot;{@code ==}&quot;) can be used to compare time point-interval relations, Instances are to be obtained
 *   using the constants this class offers, or using the combination methods {@link #or(TimePointIntervalRelation...)},
 *   {@link #and(TimePointIntervalRelation...)}, {@link #compose(TimePointIntervalRelation, TimeIntervalRelation)}, and
 *   {@link #min(TimePointIntervalRelation, TimePointIntervalRelation)}, and the unary method {@link #complement()}.
 *   Also, an TimePointIntervalRelation can be determined {@link #timePointIntervalRelation(Date, TimeInterval) based on a point
 *   in time and a time interval}.
 *   {@link #VALUES} lists all possible time point-interval relations.</p>
 * <p>The {@link Object#equals(Object)} is not overridden, because we want to use this type with reference equality.
 *   {@link #hashCode()} is overridden nevertheless, to guarantee a better spread (it also happens to give a peek inside
 *   the encapsulation, for people who know the implementation details).</p>
 * <p>All methods in this class are O(n), i.e., work in constant time, although {@link #compose(TimePointIntervalRelation, TimeIntervalRelation)}
 *   takes a significant longer constant time than the other methods.
 */
public final class TimePointIntervalRelation {

  /*
   * Implementation note:
   *
   * time point-interval relations are implemented as a 5-bit bit pattern, stored in the 5 least significant bits of a 32-bit int.
   * Each of those 5 bits represents a basic relation, being in the general relation ({@code 1}) or not being in the
   * general relation ({@code 0}).
   * The order of the basic relations in the bit pattern is important for some of the algorithms. There is some
   * trickery involved.
   */


  /*<section name="population">*/
  //------------------------------------------------------------------

  /**
   * The total number of possible time point-interval relations <strong>= {@value}</strong>
   * (i.e., <code>2<sup>5</sup></code>).
   */
  public final static int NR_OF_RELATIONS    = 32;

  /**
   * All possible time point-interval relations.
   */
  @Invars({
    @Expression("VALUES != null"),
    @Expression("for (TimePointIntervalRelation tir : VALUES) {tir != null}"),
    @Expression("for (int i : 0 .. VALUES.length) {for (int j : i + 1 .. VALUES.length) {VALUES[i] != VALUES[j]}}"),
    @Expression("for (TimePointIntervalRelation tir) {VALUES.contains(tir)}")
  })
  public final static TimePointIntervalRelation[] VALUES = new TimePointIntervalRelation[NR_OF_RELATIONS];
  static {
    for (int i = 0; i < NR_OF_RELATIONS; i++) {
      VALUES[i] = new TimePointIntervalRelation(i);
    }
  }

  /*</section>*/


  /*<section name="basic relations">*/
  //------------------------------------------------------------------

  private final static int EMPTY_BIT_PATTERN   =  0;   // 00000
  private final static int BEFORE_BIT_PATTERN  =  1;   // 00001 <
  private final static int BEGINS_BIT_PATTERN  =  2;   // 00010 =[<
  private final static int IN_BIT_PATTERN      =  4;   // 00100 ><
  private final static int ENDS_BY_BIT_PATTERN =  8;   // 01000 =[>
  private final static int AFTER_BIT_PATTERN   = 16;   // 10000 >
  private final static int FULL_BIT_PATTERN    = 31;   // 11111 < =[< >< =[> >

  /**
   * This empty relation is not a true time point-interval relation. It does not express a
   * relational condition between intervals. Yet, it is needed for
   * consistencey with some operations on time point-interval relations.
   */
  @Invars(@Expression("for (TimePointIntervalRelation basic : BASIC_RELATIONS) {! EMPTY.impliedBy(basic)}"))
  public final static TimePointIntervalRelation EMPTY = VALUES[EMPTY_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time point-interval relation that says that a point in time
   * <var>t</var> <dfn>comes before</dfn> an interval <var>I</var>, i.e., the
   * <var>t</var> is before the begin of <var>I</var>:</p>
   * <pre>
   *   (I.begin != null) &amp;&amp; (t &lt; I2.begin)
   * </pre>
   * <img style="text-align: center;" src="doc-files/TimePointIntervalRelation-before.png">
   * <p>The short representation of this time point-interval relation is
   *   &quot;<code><strong>&lt;</strong></code>&quot;.</p>
   */
  public final static TimePointIntervalRelation BEFORE = VALUES[BEFORE_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time point-interval relation that says that a point in time
   * <var>t</var> <dfn>begins</dfn> an interval <var>I</var>, i.e., the
   * <var>t</var> is the begin of <var>I</var>:</p>
   * <pre>
   *   (I.begin != null) &amp;&amp; (t == I2.begin)
   * </pre>
   * <img style="text-align: center;" src="doc-files/TimePointIntervalRelation-begins.png">
   * <p>The short representation of this time point-interval relation is
   *   &quot;<code><strong>=[&lt;</strong></code>&quot;.</p>
   */
  public final static TimePointIntervalRelation BEGINS = VALUES[BEGINS_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time point-interval relation that says that a point in time
   * <var>t</var> <dfn>falls in</dfn> an interval <var>I</var>, i.e., the
   * <var>t</var> is after the begin of <var>I</var> and before the end of <var>I</var>:</p>
   * <pre>
   *   (I.begin != null) &amp;&amp; (I.end != null) &amp;&amp; (t &gt; I2.begin) &amp;&amp; (t &lt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/TimePointIntervalRelation-in.png">
   * <p>The short representation of this time point-interval relation is
   *   &quot;<code><strong>&gt;&lt;</strong></code>&quot;.</p>
   */
  public final static TimePointIntervalRelation IN = VALUES[IN_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time point-interval relation that says that a point in time
   * <var>t</var> <dfn>ends</dfn> an interval <var>I</var>, i.e., the
   * <var>t</var> is the end of <var>I</var>:</p>
   * <pre>
   *   (I.end != null) &amp;&amp; (t == I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/TimePointIntervalRelation-ends.png">
   * <p>The short representation of this time point-interval relation is
   *   &quot;<code><strong>=[&gt;</strong></code>&quot;.</p>
   */
  public final static TimePointIntervalRelation ENDS = VALUES[ENDS_BY_BIT_PATTERN];

  /**
   * <p>A <strong>basic</strong> time point-interval relation that says that a point in time
   * <var>t</var> <dfn>comes after</dfn> an interval <var>I</var>, i.e., the
   * <var>t</var> is after the end of <var>I</var>:</p>
   * <pre>
   *   (I.end != null) &amp;&amp; (t &gt; I2.end)
   * </pre>
   * <img style="text-align: center;" src="doc-files/TimePointIntervalRelation-after.png">
   * <p>The short representation of this time point-interval relation is
   *   &quot;<code><strong>&gt;</strong></code>&quot;.</p>
   */
  public final static TimePointIntervalRelation AFTER = VALUES[AFTER_BIT_PATTERN];

  /**
   * The full time point-interval relation, which expresses that nothing definite can be
   * said about the relationship between a time point and a time interval.
   */
  @Invars(@Expression("FULL == or(BEFORE, BEGINS, IN, ENDS, AFTER"))
  public final static TimePointIntervalRelation FULL = VALUES[FULL_BIT_PATTERN];

  /**
   * The set of all 5 basic time point-interval relations. That they are presented here in
   * a particular order, is a pleasant side note, but in general not relevant
   * for the user.
   */
  @Invars({
    @Expression("for (BasicRelation br : BASIC_RELATIONS) {BASIC_RELATIONS[br.basicRelationOrdinal()] == br}"),
    @Expression("BASIC_RELATIONS[ 0] == BEFORE"),
    @Expression("BASIC_RELATIONS[ 1] == BEGINS"),
    @Expression("BASIC_RELATIONS[ 2] == IN"),
    @Expression("BASIC_RELATIONS[ 3] == ENDS"),
    @Expression("BASIC_RELATIONS[ 4] == AFTER")
  })
  public final static TimePointIntervalRelation[] BASIC_RELATIONS = {BEFORE, BEGINS, IN, ENDS, AFTER};

  private final static String[] BASIC_CODES = {"<", "=[<", "><", "=[>", ">"};

  /*</section>*/



  /*<section name="secondary relations">*/
  //------------------------------------------------------------------

  /**
   * A non-basic time point-interval relation that is often handy to use, which expresses that a time point <var>t</var>
   * and an interval <var>I</var> are concurrent in some way.
   * Thus, <var>t</var> does <em>not</em> come before <var>I</var>, <var>t</var> is not the end time of <var>I</var>,
   * and <var>t</var> does <em>not</em> come after <var>I</var> (remember that we define time intervals as right half-open).
   */
  @Invars(@Expression("CONCURS_WITH == or(BEGINS, IN)"))
  public final static TimePointIntervalRelation CONCURS_WITH = or(BEGINS, IN);

  /**
   * A non-basic time point-interval relation that is often handy to use, which expresses that a time point <var>t</var>
   * is earlier than an interval <var>I</var> ends:
   * <pre>
   *   (I.end != null) && (t &lt; I.end)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("BEFORE_END == or(BEFORE, BEGINS, IN)"))
  public static final TimePointIntervalRelation BEFORE_END = or(BEFORE, BEGINS, IN);

  /**
   * A non-basic time point-interval relation that is often handy to use, which expresses that a time point <var>t</var>
   * is later than an interval <var>I</var> begins:
   * <pre>
   *   (I.begin != null) && (t &gt; I.begin)
   * </pre>.
   * This relation is introduced because it is the possible result of the composition of 2 basic relations.
   */
  @Invars(@Expression("AFTER_BEGIN == or(IN, ENDS, AFTER)"))
  public static final TimePointIntervalRelation AFTER_BEGIN = or(IN, ENDS, AFTER);

  /*</section>*/



  /*<section name="n-ary operations">*/
  //------------------------------------------------------------------

  /**
   * The main factory method for TimePointIntervalRelations. Although this is intended to create
   * any disjunction of the basic relations, you can use any relation in the argument
   * list. This is the union of all time point-interval relations in {@code gr}, when they are considered
   * as sets of basic relations.
   */
  @MethodContract(post = {
    @Expression("for (TimePointIntervalRelation br : BASIC_RELATIONS) {exists (TimePointIntervalRelation tir : _gr) {tir.impliedBy(br)} ?? result.impliedBy(br)}")
  })
  public static TimePointIntervalRelation or(TimePointIntervalRelation... gr) {
    int acc = EMPTY_BIT_PATTERN;
    for (TimePointIntervalRelation tir : gr) {
      acc |= tir.$bitPattern;
    }
    return VALUES[acc];
  }

  /**
   * The conjunction of the time point-interval relations in {@code gr}.
   * This is the intersection of all time point-interval relations in {@code gr}, when they are considered
   * as sets of basic relations.
   */
  @MethodContract(post = {
    @Expression("for (TimePointIntervalRelation br : BASIC_RELATIONS) {for (TimePointIntervalRelation tir : _gr) {tir.impliedBy(br)} ?? result.impliedBy(br)}")
  })
  public static TimePointIntervalRelation and(TimePointIntervalRelation... gr) {
    int acc = FULL_BIT_PATTERN;
    for (TimePointIntervalRelation tir : gr) {
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
    post = @Expression("for (TimePointIntervalRelation br : BASIC_RELATIONS) {br.implies(result) ?? br.implies(_base) && ! br.implies(_term)}")
  )
  public static TimePointIntervalRelation min(TimePointIntervalRelation base, TimePointIntervalRelation term) {
    assert preArgumentNotNull(base, "base");
    assert preArgumentNotNull(term, "term");
    int xor = base.$bitPattern ^ term.$bitPattern;
    int min = base.$bitPattern & xor;
    return VALUES[min];
  }

  /**
   * This matrix holds the compositions of basic time point-interval relations with Allen relations. These are part
   * of the given semantics, and cannot be calculated. See {@link #compose(TimePointIntervalRelation, TimeIntervalRelation)}.
   */
  public final static TimePointIntervalRelation[][] BASIC_COMPOSITIONS =
    {
      {BEFORE, BEFORE, BEFORE, BEFORE, BEFORE, BEFORE, BEFORE, BEFORE, BEFORE_END, BEFORE_END, BEFORE_END, BEFORE_END, FULL},
      {BEFORE, BEFORE, BEFORE, BEFORE, BEFORE, BEGINS, BEGINS, BEGINS, IN, IN, IN, ENDS, AFTER},
      {BEFORE, BEFORE, BEFORE_END, BEFORE_END, FULL, IN, IN, AFTER_BEGIN, IN, IN, AFTER_BEGIN, AFTER, AFTER},
      {BEFORE, BEGINS, IN, ENDS, AFTER, IN, ENDS, AFTER, IN, ENDS, AFTER, AFTER, AFTER},
      {FULL, AFTER_BEGIN, AFTER_BEGIN, AFTER, AFTER, AFTER_BEGIN, AFTER, AFTER, AFTER_BEGIN, AFTER, AFTER, AFTER, AFTER}
    };

  /**
   * <p>Given a point in time <code><var>t</var></code> and 2 time intervals <code><var>I1</var></code>, <code><var>I2</var></code>,
   *   given <code>tpir = timePointIntervalRelation(<var>t</var>, <var>I1</var>)</code> and
   *   <code>ar == allenRelation(<var>I1</var>, <var>I2</var>)</code>,
   *   <code>compose(tpir, ar) == timePointIntervalRelation(<var>t</var>, <var>I2</var>)</code>.</p>
   */
  @MethodContract(
    pre  = {
      @Expression("_tpir != null"),
      @Expression("_ar != null")
    },
    post = {
      @Expression("for (TimePointIntervalRelation bTpir : BASIC_RELATIONS) {for (TimeIntervalRelation bAr: TimeIntervalRelation.BASIC_RELATIONS) {" +
                    "bTpir.implies(_tpir) && bAr.implies(_ar) ? result.impliedBy(BASIC_COMPOSITIONS[btPir.basicRelationOrdinal()][bAr.basicRelationOrdinal()])" +
                  "}}")
  })
  public static TimePointIntervalRelation compose(TimePointIntervalRelation tpir, TimeIntervalRelation ar) {
    assert preArgumentNotNull(tpir, "tpir");
    assert preArgumentNotNull(ar, "ar");
    TimePointIntervalRelation acc = EMPTY;
    for (TimePointIntervalRelation bTpir : BASIC_RELATIONS) {
      if (tpir.impliedBy(tpir)) {
        for (TimeIntervalRelation bAr : TimeIntervalRelation.BASIC_RELATIONS) {
          if (ar.impliedBy(bAr)) {
            acc = or(acc, BASIC_COMPOSITIONS[bTpir.basicRelationOrdinal()][bAr.basicRelationOrdinal()]);
          }
        }
      }
    }
    return acc;
  }

  /**
   * The relation of {@code t} with {@code i} with the lowest possible {@link #uncertainty()}.
   * {@code null} as {@link TimeInterval#getBegin()} or {@link TimeInterval#getEnd()} is considered
   * as unknown, and thus is not used to restrict the relation more, leaving it with
   * more {@link #uncertainty()}.
   *
   * @mudo contract
   */
  public static TimePointIntervalRelation timePointIntervalRelation(Date t, TimeInterval i) {
    if (t == null) {
      return FULL;
    }
    Date iBegin = i.getBegin();
    Date iEnd = i.getEnd();
    TimePointIntervalRelation result = FULL;
    if (iBegin != null) {
      if (t.before(iBegin)) {
        return BEFORE;
      }
      else if (t.equals(iBegin)) {
        return BEGINS;
      }
      else {
        assert t.after(iBegin);
        result = min(result, BEFORE);
        result = min(result, BEGINS);
      }
    }
    if (iEnd != null) {
      if (t.before(iEnd)) {
        result = min(result, ENDS);
        result = min(result, AFTER);
      }
      else if (t.equals(iEnd)) {
        return ENDS;
      }
      else {
        assert t.after(iEnd);
        return AFTER;
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
  private TimePointIntervalRelation(int bitPattern) {
    assert pre(bitPattern >= EMPTY_BIT_PATTERN);
    assert pre(bitPattern <= FULL_BIT_PATTERN);
    $bitPattern = bitPattern;
  }

  /*</construction>*/



  /**
   * Only the 5 lowest bits are used. The other (32 - 5 = 27 bits) are 0.
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
         invars = {@Expression("result >= 0"), @Expression("result < 5")})
  public int basicRelationOrdinal() {
    /*
     * This is the bit position, 0-based, in the 13-bit bit pattern, of the bit
     * representing this as basic relation.
     */
    assert pre(isBasic());
    return Integer.numberOfTrailingZeros($bitPattern);
  }

  /**
   * This is a basic time point-interval relation.
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
   * A measure about the uncertainty this time point-interval relation expresses.
   * This is the fraction of the 5 basic relations that imply this general relation.
   * {@link #FULL} is complete uncertainty, and returns {@code 1}.
   * A basic relation is complete certainty, and returns {@code 0}.
   * The {@link #EMPTY empty} relation has no meaningful uncertainty. This method returns
   * {@link Float#NaN} as value for {@link #EMPTY}.
   */
  @MethodContract(post = {
    @Expression("this != EMPTY ? result == count (TimePointIntervalRelation br : BASIC_RELATIONS) {br.implies(this)} - 1) / 4"),
    @Expression("this == EMPTY ? result == Float.NaN")
  })
  public float uncertainty() {
    int count = Integer.bitCount($bitPattern);
    if (count == 0) {
      return Float.NaN;
    }
    count--;
    float result = count / 4.0F;
    return result;
  }

  /**
   * <p>The complement of an time point-interval relation is the logic negation of the condition the time point-interval relation expresses.
   *   The complement of a basic time point-interval relation is the disjunction of all the other basic time point-interval relations.
   *   The complement of a general time point-interval relation is the disjunction of all basic time point-interval relations that are
   *   not implied by the general time point-interval relation.</p>
   * <p>This method is key to validating semantic constraints on time intervals, using the following idiom:</p>
   * <pre>
   *   ...
   *   Date t1 = ...;
   *   Date t2 = ...;
   *   TimePointIntervalRelation condition = ...;
   *   TimePointIntervalRelation actual = timePointIntervalRelation(t1, t2);
   *   if (! actual.implies(condition)) {
   *     throw new ....
   *   }
   *   ...
   * </pre>
   * <p><strong>Be aware that the complement has in general not the same meaning as a logic negation.</strong>
   *   For a basic relation <var>br</var> and a general time point-interval relation <var>cond</var>, it is true that</p>
   * <p><code><var>br</var>.implies(<var>cond</var>)</code> &hArr;
   *   <code>! <var>br</var>.implies(<var>cond</var>.complement())</code></p>
   * <p><strong>This is however not so for non-basic, and thus general time point-interval relations</strong>, as the following
   *   counterexample proofs. Suppose a condition is that, for a general relation <var>gr</var>:</p>
   * <pre><var>gr</var>.implies(<var>cond</var>)</pre>
   * <p>Suppose <code><var>gr</var> == (=[&lt; &gt;&lt;)</code>. Then we can rewrite in the following way:</p>
   * <p>&nbsp;&nbsp;&nbsp;<code><var>gr</var>.implies(<var>cond</var>)</code><br />
   *   &hArr; <code>(=[&lt; &gt;&lt;).implies(<var>cond</var>)</code><br />
   *   &hArr; <code>(=[&lt; &gt;&lt;) &sube; <var>cond</var></code><br />
   *   &hArr; <code>(=[&lt; &isin; <var>cond</var>) && (&gt;&lt; &isin; <var>cond</var>)</code></p>
   * <p>From the definition of the complement, it follows that, for a basic relation <var>br</var> and a general
   *   relation <var>GR</var> as set</p>
   * <p><code>br &isin; GR</code> &hArr; <code>br &notin; GR.complement()</code></p>
   * <p>Thus:</p>
   * <p>&hArr; <code>(=[&lt; &notin; <var>cond</var>.complement()) && (&gt;&lt; &notin; <var>cond</var>.complement())</code><br />
   *   &hArr; <code>! ((=[&lt; &isin; <var>cond</var>.complement()) || (&gt;&lt; &isin; <var>cond</var>.complement())</code> (1)</p>
   * <p>While, from the other side:</p>
   * <p>&nbsp;&nbsp;&nbsp;<code>! <var>gr</var>.implies(<var>cond</var>.complement())</code><br />
   *   &hArr; <code>! (=[&lt; &gt;&lt;).implies(<var>cond</var>.complement())</code><br />
   *   &hArr; <code>! (=[&lt; &gt;&lt;) &sube; (<var>cond</var>.complement())</code><br />
   *   &hArr; <code>! ((=[&lt; &isin; <var>cond</var>.complement()) && (&gt;&lt; &isin; <var>cond</var>.complement())</code> (2)</p>
   * <p>It is clear that (1) is incompatible with (2), except for the case where the initial relation is basic.</p>
   * <p>In the reverse case, for a basic relation <var>br</var> and a general time point-interval relation <var>actual</var>, nothing
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
    @Expression("for (TimePointIntervalRelation br : BASIC_RELATIONS) {" +
                  "(impliedBy(br) ?? ! complement().impliedBy(br)) && (! impliedBy(br) ?? complement().impliedBy(br))" +
                "}")
  })
  public final TimePointIntervalRelation complement() {
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
      @Expression("basic ? for (TimePointIntervalRelation br : BASIC_RELATIONS) : {br != this ? ! impliedBy(br)}"),
      @Expression("for (TimePointIntervalRelation gr) {impliedBy(gr) == for (TimePointIntervalRelation br : BASIC_RELATIONS) : {gr.impliedBy(br) ? impliedBy(br)}")
    }
  )
  public final boolean impliedBy(TimePointIntervalRelation gr) {
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
  public final boolean implies(TimePointIntervalRelation gr) {
    assert preArgumentNotNull(gr, "gr");
    return (gr.$bitPattern & $bitPattern) == $bitPattern;
  }

  /*</section>*/



  @Override
  public final int hashCode() {
    /*
     * this returns the internal representation of this object: it is a way for people
     * that know about the implementation to see the bit pattern of this time point-interval relation
     */
    return $bitPattern;
  }

  /**
   * This returns a representation of the time point-interval relation in the most used short notation (&lt; =[&lt; &gt;&lt; =[&gt; &gt;).
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("(");
    if (isBasic()) {
      result.append(BASIC_CODES[basicRelationOrdinal()]);
    }
    else {
      boolean first = true;
      for (int i = 0; i < BASIC_CODES.length; i++) {
        if (impliedBy(BASIC_RELATIONS[i])) {
          result.append(BASIC_CODES[i]);
          if (first) {
            result.append(" ");
            first = false;
          }
        }
      }
    }
    result.append(")");
    return result.toString();
  }

}

