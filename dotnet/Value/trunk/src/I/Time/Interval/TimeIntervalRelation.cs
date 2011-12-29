/*<license>
Copyright 2011 - $Date: 2008-11-06 15:27:53 +0100 (Thu, 06 Nov 2008) $ by PeopleWare n.v..

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

#region Using

using System;
using System.Diagnostics.Contracts;
using System.Linq;
using System.Text;

#endregion

namespace PPWCode.Value.I.Time.Interval
{
    /// <summary>
    //* <p>Support for reasoning about relations between time intervals and constraints on time intervals.
    //*   <strong>We highly advise to use this class when working with relations between time intervals.
    //*   Reasoning about relations between time intervals is treacherously difficult.</strong></p>
    //* <p>When working with time intervals, we often want to express constraints (invariants) that limit
    //*   acceptable intervals. Expressing this correctly proves extremely difficult in practice. Falling
    //*   back to working with isolated begin and end dates, and reasoning about their relations, in
    //*   practice proves to be even much more difficult and error prone.</p>
    //* <p>This problem was tackled in 1983 by James Allen
    //*   (<a href="http://www.cs.brandeis.edu/~cs101a/readings/allen-1983.pdf"><cite>Allen, James F. &quot;Maintaining knowledge
    //*   about temporal intervals&quot;; Communications of the ACM 26(11) pages 832-843; November 1983</cite></a>).
    //*   A good synopsis of this theory is
    //*   <a href="http://www.isr.uci.edu/~alspaugh/foundations/allen.html"><cite>Thomas A. Alspaugh &quot;Allen's interval
    //*   algebra&quot;</cite></a>. This class implements this theory, and in this text we give some guidelines
    //*   on how to use this class.</p>
    //*
    //* <h3>Quick overview</h3>
    //* <p>Allen found that there are 13 <em>basic relations</em> possible between 2 definite time intervals:</p>
    //* <table>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #PRECEDES} <var>I2</var></code> </td>
    //*     <td><img src="doc-files/AllenRelation-precedes.png" width="296" /></td>
    //*     <td><b>p</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #MEETS} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-meets.png" width="296" /></td>
    //*     <td><b>m</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #OVERLAPS} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-overlaps.png" width="296" /></td>
    //*     <td><b>o</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #FINISHED_BY} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-finishedBy.png" width="296" /></td>
    //*     <td><b>F</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #CONTAINS} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-contains.png" width="296" /></td>
    //*     <td><b>D</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #STARTS} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-starts.png" width="296" /></td>
    //*     <td><b>s</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #EQUALS} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-equals.png" width="296" /></td>
    //*     <td><b>e</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #STARTED_BY} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-startedBy.png" width="296" /></td>
    //*     <td><b>S</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #DURING} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-during.png" width="296" /></td>
    //*     <td><b>d</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #FINISHES} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-finishes.png" width="296" /></td>
    //*     <td><b>f</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #OVERLAPPED_BY} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-overlappedBy.png" width="296" /></td>
    //*     <td><b>O</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #MET_BY} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-metBy.png" width="296" /></td>
    //*     <td><b>M</b></td>
    //*   </tr>
    //*   <tr>
    //*     <td><code><var>I1</var> {@link #PRECEDED_BY} <var>I2</var></code></td>
    //*     <td><img src="doc-files/AllenRelation-precededBy.png" width="296" /></td>
    //*     <td><b>P</b></td>
    //*   </tr>
    //* </table>
    //* <ul>
    //* </ul>
    //* <p>These basic relations can be compared to <code>&lt;</code>, <code>==</code> and <code>></code>
    //*   with time instances.</p>
    //* <p>When reasoning about the relationship between time intervals however, like when comparing time instances,
    //*   we also often employ indeterminate relations, such as
    //*   <code><var>I1</var> ({@link #PRECEDES} || {@link #MEETS}) <var>I2</var></code>. This is comparable to
    //*   reasoning with <code>&le;</code>, <code>&ge;</code> and <code>!=</code> with time instances.
    //*   For time intervals, given 13 basic relations, we get 8192 (== 2<sup>13</sup>) possible <em>general
    //*   relations</em>. This includes the {@link #EMPTY empty relationship} for algebraic reasons, and the
    //*   {@link #FULL full relationship} (comparable to <code>&lt; || == || &gt;</code> with time instances),
    //*   which expresses the maximal uncertainty about the relation between 2 time intervals.</p>
    //*
    //* <h3>Interval constraints</h3>
    //* <p>Time interval relations will most often be used in business code to constrain relations between time intervals.
    //*  This is notoriously, treacherously difficult. It is for this reason that you should use code like this,
    //*  that at least forces you to think things trough, and tries to offers tools to ease reasoning. The idiom
    //*  todo this is explained next.</p>
    //* <p>First we need to determine the relation we want to uphold (<code><var>cond</var></code>). E.g., we want
    //*  to assert that 2 given intervals <code><var>I1</var></code> and <code><var>I2</var></code> do not concur.
    //*  The relationship that expresses this is <code><var>cond</var> == {@link #CONCURS_WITH}.complement()</code>.</p>
    //* <p>Next, we want to determine the relationship from <code><var>I1</var></code> to <code><var>I2</var></code>
    //*  as precisely as possible. If both <code><var>I1</var></code> and <code><var>I2</var></code> are completely
    //*  determined, i.e., neither their begin date nor their end date is {@code null}, the result will be a
    //*  {@link #BASIC_RELATIONS basic relation}. Otherwise, the result will be a less certain relation. To determine
    //*  this relationship, use {@link #timeIntervalRelation(TimeInterval, TimeInterval)}. See below for dealing
    //*  with constrained begin and end dates.</p>
    //* <p>The idiom for the assertion we want to express is then:</p>
    //* <pre>
    //*   timeIntervalRelation(<var>I1</var>, <var>I2</var>).implies(<var>cond</var>)
    //* </pre>
    //* <p>This is often the form of an invariant. Note that this can fail, on the one hand because the actual
    //*   relation is not acceptable, but also because <em>we cannot be 100% sure that the actual relationship
    //*   satisfies the condition</em>. In our example, we would have:</p>
    //* <pre>
    //*   timeIntervalRelation(<var>I1</var>, <var>I2</var>).implies(CONCURS_WITH.complement())
    //* </pre>
    //* <p>{@link #CONCURS_WITH} being {@code (oFDseSdfO)}, <code>CONCURS_WITH.complement() == (pmMP)</code>.
    //*  If the actual relation results in {@code (e)}, e.g., the constraint is clearly not satisfied. If
    //*  the actual relation results in {@code (OMP)} for example, it means that it is possible that the relation
    //*  is satisfied, but there is also a chance that it is not (if <code><var>I1</var></code> begins before
    //*  <code><var>I2</var></code> ends).</p>
    //* <p>In code then, we often want to throw an exception to interrupt an algorithm that would violate the
    //*   invariant. The idiom for this is usually of the form:</p>
    //* <pre>
    //*   ...
    //*   TimeInterval i1 = ...;
    //*   TimeInterval i2 = ...;
    //*   TimeIntervalRelation condition = ...;
    //*   TimeIntervalRelation actual = timeIntervalRelation(i1, i2);
    //*   if (! actual.implies(condition)) {
    //*     throw new ....
    //*   }
    //*   ...
    //* </pre>
    //* <p>In our example, this would become</p>
    //* <pre>
    //*   ...
    //*   TimeInterval i1 = ...;
    //*   TimeInterval i2 = ...;
    //*   if (! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())) {
    //*     throw new ....
    //*   }
    //*   ...
    //* </pre>
    //* <p><strong>Note that in general {@code (! actual.implies(condition))} is <em>not equivalent</em> with
    //*   {@code actual.implies(condition.complement())} (see {@link #complement()}).</strong> In our example
    //*   this is already clear. If the actual relation results in {@code (e)},
    //*   {@code ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())} evaluates to</p>
    //* <pre>
    //*    ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())
    //* == ! (e).implies((pmMP))
    //* == ! false
    //* == true
    //* </pre>
    //* <p>and {@code timeIntervalRelation(i1, i2).implies(CONCURS_WITH)} evaluates to</p>
    //* <pre>
    //*    timeIntervalRelation(i1, i2).implies(CONCURS_WITH)
    //* == (e).implies((oFDseSdfO))
    //* == true
    //* </pre>
    //* <p>But in the case where the actual relation results in {@code (OMP)},
    //*   {@code ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())} evaluates to</p>
    //* <pre>
    //*    ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())
    //* == ! (OMP).implies((pmMP))
    //* == ! false
    //* == true
    //* </pre>
    //* <p>and {@code timeIntervalRelation(i1, i2).implies(CONCURS_WITH)} evaluates to</p>
    //* <pre>
    //*    timeIntervalRelation(i1, i2).implies(CONCURS_WITH)
    //* == (OMP).implies((oFDseSdfO))
    //* == <strong>false</strong>
    //* </pre>
    //* <p>{@code ! timeIntervalRelation(i1, i2).implies(CONCURS_WITH.complement())} expresses that [we want to throw an
    //*   exception if] <em>it is not guaranteed that <code><var>i1</var></code> and <code><var>i2</var></code> do
    //*   not concur</em>. {@code timeIntervalRelation(i1, i2).implies(CONCURS_WITH)} expresses that [we want to throw an
    //*   exception if] <em>it is guaranteed that <code><var>i1</var></code> and <code><var>i2</var></code> do
    //*   concur</em>. <strong>These 2 phrases are not equivalent.</strong></p>
    //*
    //* <h3 id="constrainedDates">Reasoning with unknown but constrained begin and end dates</h3>
    //* <p>In time intervals, the begin or end end can be {@code null}. The semantics of this is in general that
    //*  the begin date, respectively the end date, is unknown. Comparing such an interval with another interval
    //*  results in a relatively broad time interval relation, expression an amount of uncertainty.</p>
    //* <p>In several use cases however, we do no know a definite begin or end date, but we do know that the
    //*  begin or end date have constraints. E.g., consider contracts that have a definite begin date, but are
    //*  open ended. The contract interval thus is incompletely known. However, since at the moment of our reasoning
    //*  no definite end date is set, we know that the end date is at least later than {@code now}. In comparing
    //*  this contract interval with another interval, this constraint can be of use to limit the extend, i.e., the
    //*  uncertainty, of the time interval relation. The same applies, e.g., with contracts that will start once payment is
    //*  received. Since it is not received yet at the moment of our evaluation, we know that the begin date is at
    //*  least later than or equal to {@code now}.</p>
    //* <p>In such cases, the interval object <code><var>I</var></code> we are focusing on can be interpreted in
    //*  another way. Suppose we are comparing <code><var>I</var></code> with <code><var>Other</var></code>. We are
    //*  actually not interested in <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>, but rather in
    //*  <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)</code>. Sadly, there is no easy
    //*  syntax (or code) to express <code><var>I<sub>constrained</sub></var></code>. What we can express is an
    //*  <var>I<sub>determinate</sub></var>, where the border times are filled out in place of the unknown begin
    //*  date or end date. <code>timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>)</code>
    //*  can be calculated, and will be much less uncertain than
    //*  <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>. If now can determine the time interval relation from
    //*  <code><var>I<sub>constrained</sub></var></code> to <code><var>I<sub>determinate</sub></var></code>,
    //*  we can find <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)</code> as:</p>
    //* <pre>
    //*   timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>) ==
    //*     compose(timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>I<sub>determinate</sub></var>), timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>))
    //* </pre>
    //* <p>The time interval relation from an interval we are focusing on with constrained semantics to a determinate
    //*   interval is a constant that can be determined by reasoning. E.g., for our open ended contract, that lasts
    //*   at least longer than today (<code>[I<sub>begin</sub>, &gt; now[</code>, supposing <code>I<sub>begin</sub> &le;
    //*   yesterday</code>), we can say that its relation to the determinate interval <code>[I<sub>begin</sub>, now[</code> is
    //*   {@code (S)} ({@link #STARTED_BY}). Suppose
    //*   <code>timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>) == (o)</code> (say
    //*   <code><var>Other</var> == [<var>yesterday</var>, <var>next year</var>[</code>), we can now say
    //*   that <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>) == (S).(o) == (oFD)</code>.
    //*   The comparison of the indeterminate interval with <code><var>Other</var></code>,
    //*   <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>, would have resulted in:</p>
    //* <pre>
    //*    timeIntervalRelation(<var>I</var>, <var>Other</var>)
    //* == timeIntervalRelation([I<sub>begin</sub>, null[, [<var>yesterday</var>, <var>next year</var>[)
    //* == (pmoFD)
    //* </pre>
    //* <p>If you reason directly about <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)</code></p>
    //* <pre>
    //*    timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>)
    //* == timeIntervalRelation([I<sub>begin</sub>, &gt; now[, [<var>yesterday</var>, <var>next year</var>[)
    //* == (oFD)
    //* </pre>
    //* <p>you will see that {@code (oFD)} is  indeed the most certain answer.
    //* <p>Be aware that in a number of cases, the non-determinate character of <code><var>I</var></code> doesn't matter.
    //*   If you suppose in the previous example that
    //*   <code>timeIntervalRelation(<var>I<sub>determinate</sub></var>, <var>Other</var>) == (p)</code> (say
    //*   <code><var>Other</var> == [<var>next year</var>, Other<sub>end</sub>[</code>),
    //*   <code>timeIntervalRelation(<var>I<sub>constrained</sub></var>, <var>Other</var>) == (S).(p) == (pmoFD)</code>.
    //*   The comparison of the indeterminate interval with <code><var>Other</var></code>,
    //*   <code>timeIntervalRelation(<var>I</var>, <var>Other</var>)</code>, in this case, results in the same time interval relation:</p>
    //* <pre>
    //*    timeIntervalRelation(<var>I</var>, <var>Other</var>)
    //* == timeIntervalRelation([I<sub>begin</sub>, null[, [<var>next year</var>, Other<sub>end</sub>[)
    //* == (pmoFD)
    //* </pre>
    //*
    //* <h3>Inference</h3>
    //* <p><strong>Be aware that, in general, inference over intervals, also using Allen relations, is NP-complete.</strong>
    //*   This means that the time the execution of algorithms will take, is at least difficult to ascertain, and quickly
    //*   completely impractical (i.e., with realistic parameters the algorithm would take longer than the universe exists
    //*   &mdash; no kidding).</p>
    //* <p>There are subsets of the Allen relations for which there exist algorithms that perform much better. These issues
    //*   are not implemented here at this time.</p>
    //*
    //* <h3>About the code</h3>
    //* <p>We have chosen to introduce a full-featured type for working with Allen relations, to make encapsulation as
    //*   good as possible. This has a slight performance overhead, but we believe that this is worth it, considering the
    //*   immense complexity of reasoning about relations between time intervals.</p>
    //* <p>Time interval relations are not implemented as a value according to the ppwcode value vernacular, although they do form
    //*   an algebra. We presume time interval relations are never shown to end users as values.</p>
    //* <p>Time interval relations follow the &quot;8192-fold singleton pattern&quot;. All possible instances are created when this
    //*   class is loaded, and it is impossible for a user of the class to create new instances. This means that  reference
    //*   equality (&quot;{@code ==}&quot;) can be used to compare time interval relations, Instances are to be obtained
    //*   using the constants this class offers, or using the combination methods {@link #or(TimeIntervalRelation...)},
    //*   {@link #and(TimeIntervalRelation...)}, {@link #compose(TimeIntervalRelation, TimeIntervalRelation)}, and
    //*   {@link #min(TimeIntervalRelation, TimeIntervalRelation)}, and the unary methods {@link #complement()} and {@link #converse()}.
    //*   Also, an TimeIntervalRelation can be determined {@link #timeIntervalRelation(TimeInterval, TimeInterval) based on 2 time intervals}.
    //*   {@link #VALUES} lists all possible time interval relations.</p>
    //* <p>The {@link Object#equals(Object)} is not overridden, because we want to use this type with reference equality.
    //*   {@link #hashCode()} is overridden nevertheless, to guarantee a better spread (it also happens to give a peek inside
    //*   the encapsulation, for people who know the implementation details).</p>
    //* <p>All methods in this class are O(n), i.e., work in constant time, although {@link #compose(TimeIntervalRelation, TimeIntervalRelation)}
    //*   takes a significant longer constant time than the other methods.
    /// </summary>
    public struct TimeIntervalRelation : IEquatable<TimeIntervalRelation>
    {
        #region Type invariants

        [ContractInvariantMethod]
        private void TypeInvariants()
        {
            // Values
            Contract.Invariant(ClassInitialized ? Values != null : true);
            Contract.Invariant(ClassInitialized ? Values.Length == NrOfRelations : true);
            // Values cannot contain null, because the type is TimeIntervalRelation[], not
            // TimeIntervalRelation?[]
            Contract.Invariant(
                ClassInitialized
                    ? Contract.ForAll(
                        0,
                        NrOfRelations - 1,
                        i => Contract.ForAll(i + 1, NrOfRelations - 1, j => Values[i] != Values[j]))
                    : true,
                "No duplicate entries.");
            Contract.Invariant(
                ClassInitialized ? Values.Contains(this) : true,
                "Any instance there ever can be is mentioned in Values. There are no other instances.");

            // Basic relations
            Contract.Invariant(ClassInitialized ? BasicRelations != null : true);
            Contract.Invariant(ClassInitialized ? Contract.ForAll(BasicRelations, basic => ! Empty.ImpliedBy(basic)) : true);
            // MUDO Contract.Invariant(ClassInitialized ? FULL == or(PRECEDES, MEETS, OVERLAPS, FINISHED_BY, CONTAINS, STARTS, EQUALS, STARTED_BY, DURING, FINISHES, OVERLAPPED_BY, MET_BY, PRECEDED_BY) : true);
            Contract.Invariant(ClassInitialized ? Contract.ForAll(BasicRelations, br => BasicRelations[br.BasicRelationalOrdinal] == br) : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[0] == Precedes : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[1] == Meets : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[2] == Overlaps : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[3] == FinishedBy : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[4] == Contains : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[5] == Starts : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[6] == EQUALS : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[7] == StartedBy : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[8] == During : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[9] == Finishes : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[10] == OverlappedBy : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[11] == MetBy : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[12] == PrecededBy : true);

            // Secondary relations
            Contract.Invariant(ClassInitialized ? ConcursWith == (Overlaps | FinishedBy | Contains | Starts | EQUALS | StartedBy | During | Finishes | OverlappedBy) : true);
            Contract.Invariant(ClassInitialized ? BeginsEarlier == (Precedes | Meets | Overlaps | FinishedBy | Contains) : true);
            Contract.Invariant(ClassInitialized ? BeginTogether == (Starts | EQUALS | StartedBy) : true);
            Contract.Invariant(ClassInitialized ? BeginsLater == (During | Finishes | OverlappedBy | MetBy | PrecededBy) : true);
            Contract.Invariant(ClassInitialized ? BeginsIn == (During | Finishes | OverlappedBy) : true);
            Contract.Invariant(ClassInitialized ? BeginsEarlierAndEndsEarlier == (Precedes | Meets | Overlaps) : true);
            Contract.Invariant(ClassInitialized ? BeginsLaterAndEndsLater == (OverlappedBy | MetBy | PrecededBy) : true);
            Contract.Invariant(ClassInitialized ? EndsEarlier == (Precedes | Meets | Overlaps | Starts | During) : true);
            Contract.Invariant(ClassInitialized ? EndsIn == (Overlaps | Starts | During) : true);
            Contract.Invariant(ClassInitialized ? EndTogether == (FinishedBy | EQUALS | Finishes) : true);
            Contract.Invariant(ClassInitialized ? EndsLater == (Contains | StartedBy | OverlappedBy | MetBy | PrecededBy) : true);
            Contract.Invariant(ClassInitialized ? ContainsBegin == (Overlaps | FinishedBy | Contains) : true);
            Contract.Invariant(ClassInitialized ? ContainsEnd == (Contains | StartedBy | OverlappedBy) : true);

            // About relations
            Contract.Invariant(ImpliedBy(this));
            Contract.Invariant(ClassInitialized && IsBasic ? IsNotImpliedByBasicTirs(this) : true);
            Contract.Invariant(ClassInitialized ? ImplyingMeansEveryBasicTirThatImpliesAlsoImplies(this) : true);
        }

        /// <summary>
        /// Introduced to technically make invariants possible.
        /// </summary>
        /// <remarks>
        /// Cannot refer to "this" in lambda function if this is a struct.
        /// </remarks>
        [Pure]
        public static bool IsNotImpliedByBasicTirs(TimeIntervalRelation tir)
        {
            Contract.Ensures(Contract.ForAll(BasicRelations, br => br != tir ? (!tir.ImpliedBy(br)) : true));

            return BasicRelations.All(br => br != tir ? (!tir.ImpliedBy(br)) : true);
        }

        /// <summary>
        /// Introduced to technically make invariants possible.
        /// </summary>
        /// <remarks>
        /// Cannot refer to "this" in lambda function if this is a struct.
        /// </remarks>
        [Pure]
        public static bool ImplyingMeansEveryBasicTirThatImpliesAlsoImplies(TimeIntervalRelation tir)
        {
            Contract.Ensures(Contract.Result<bool>() ==
                             Contract.ForAll(
                                 Values,
                                 v => tir.ImpliedBy(v) ==
                                      Contract.ForAll(
                                          BasicRelations,
                                          br => v.ImpliedBy(br) ? tir.ImpliedBy(br) : true)));

            return Values
                .Where(tir.ImpliedBy)
                .All(v => BasicRelations.Where(v.ImpliedBy).All(tir.ImpliedBy));
        }

        #endregion

        /*
         * Implementation note:
         *
         * Time interval relations are implemented as a 13-bit bit pattern, stored in the 13 least significant bits of a 32-bit int.
         * Each of those 13 bits represents a basic relation, being in the general relation (<c>1</c>}) or not being in the
         * general relation (<c>0</c>).
         * The order of the basic relations in the bit pattern is important for some of the algorithms. There is some
         * trickery involved.
         */

        #region Population

        /// <summary>
        /// The total number of possible time point-interval relations <strong>= 8192</strong>
        /// (i.e., <c>2<sup>13</sup></c>).
        /// </summary>
        public const int NrOfRelations = 8192;

        /// <summary>
        /// All possible time interval relations.
        /// </summary>
        public static readonly TimeIntervalRelation[] Values;

        static TimeIntervalRelation()
        {
            ClassInitialized = false;
            Values = new TimeIntervalRelation[NrOfRelations];
            for (uint i = 0; i < NrOfRelations; i++)
            {
                Values[i] = new TimeIntervalRelation(i);
            }
            Empty = Values[EmptyBitPattern];
            Precedes = Values[PrecedesBitPattern];
            Meets = Values[MeetsBitPattern];
            Overlaps = Values[OverlapsBitPattern];
            FinishedBy = Values[FinishedByBitPattern];
            Contains = Values[ContainsBitPattern];
            Starts = Values[StartsBitPattern];
            EQUALS = Values[EqualsBitPattern];
            StartedBy = Values[StartedByBitPattern];
            During = Values[DuringBitPattern];
            Finishes = Values[FinishesBitPattern];
            OverlappedBy = Values[OverlappedByBitPattern];
            MetBy = Values[MetByBitPattern];
            PrecededBy = Values[PrecededByBitPattern];
            Full = Values[FullBitPattern];
            BasicRelations = new TimeIntervalRelation[]
            {
                Precedes, Meets, Overlaps, FinishedBy, Contains, Starts,
                EQUALS,
                StartedBy, During, Finishes, OverlappedBy, MetBy, PrecededBy
            };
            ConcursWith
                = Or(Overlaps, FinishedBy, Contains, Starts, EQUALS, StartedBy, During, Finishes, OverlappedBy);
            BeginsEarlier = Or(Precedes, Meets, Overlaps, FinishedBy, Contains);
            BeginTogether = Or(Starts, EQUALS, StartedBy);
            BeginsLater = Or(During, Finishes, OverlappedBy, MetBy, PrecededBy);
            BeginsIn = Or(During, Finishes, OverlappedBy);
            BeginsEarlierAndEndsEarlier = Or(Precedes, Meets, Overlaps);
            BeginsLaterAndEndsLater = Or(OverlappedBy, MetBy, PrecededBy);
            EndsEarlier = Or(Precedes, Meets, Overlaps, Starts, During);
            EndsIn = Or(Overlaps, Starts, During);
            EndTogether = Or(FinishedBy, EQUALS, Finishes);
            EndsLater = Or(Contains, StartedBy, OverlappedBy, MetBy, PrecededBy);
            ContainsBegin = Or(Overlaps, FinishedBy, Contains);
            ContainsEnd = Or(Contains, StartedBy, OverlappedBy);
            //BasicCompositions = new TimePointIntervalRelation[][]
            //{
            //    new TimePointIntervalRelation[]
            //    {
            //        Before, Before, Before, Before, Before, Before, Before, Before, BeforeEnd, BeforeEnd, BeforeEnd, BeforeEnd, Full
            //    },
            //    new TimePointIntervalRelation[]
            //    {
            //        Before, Before, Before, Before, Before, Begins, Begins, Begins, In, In, In, Ends, After
            //    },
            //    new TimePointIntervalRelation[]
            //    {
            //        Before, Before, BeforeEnd, BeforeEnd, Full, In, In, AfterBegin, In, In, AfterBegin, After, After
            //    },
            //    new TimePointIntervalRelation[]
            //    {
            //        Before, Begins, In, Ends, After, In, Ends, After, In, Ends, After, After, After
            //    },
            //    new TimePointIntervalRelation[]
            //    {
            //        Full, AfterBegin, AfterBegin, After, After, AfterBegin, After, After, AfterBegin, After, After, After, After
            //    }
            //};
            ClassInitialized = true;
        }

        /// <summary>
        /// Ignore. Only here as a technical means to make static type invariants work.
        /// </summary>
        public static readonly bool ClassInitialized;

        #endregion

        #region Basic relations

        // with these bit patterns, converse is reverse of 13-bit pattern
        private const int EmptyBitPattern = 0; // 0000000000000 -
        private const int PrecedesBitPattern = 1; // 0000000000001 p
        private const int MeetsBitPattern = 2; // 0000000000010 m
        private const int OverlapsBitPattern = 4; // 0000000000100 o
        private const int FinishedByBitPattern = 8; // 0000000001000 F
        private const int ContainsBitPattern = 16; // 0000000010000 D
        private const int StartsBitPattern = 32; // 0000000100000 s
        private const int EqualsBitPattern = 64; // 0000001000000 e
        private const int StartedByBitPattern = 128; // 0000010000000 S
        private const int DuringBitPattern = 256; // 0000100000000 d
        private const int FinishesBitPattern = 512; // 0001000000000 f
        private const int OverlappedByBitPattern = 1024; // 0010000000000 O
        private const int MetByBitPattern = 2048; // 0100000000000 M
        private const int PrecededByBitPattern = 4096; // 1000000000000 P
        private const int FullBitPattern = 8191; // 1111111111111 pmoFDseSdfOMP

        /// <summary>
        /// This empty relation is not a true time interval relation. It does not express a
        /// relational condition between intervals. Yet, it is needed for
        /// consistencey with some operations on time interval relations.
        /// The converse of the empty relation is the empty relation itself.
        /// </summary>
        public static readonly TimeIntervalRelation Empty;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>precedes</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.End"/> of <c>I1</c> is before the <see cref="ITimeInterval.Begin"/> 
        /// of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>p</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.End != null) &amp;&amp; (I2.Begin != null) &amp;&amp; (I1.End &lt; I2.Begin)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-precedes.png"/>
        /// <para>The converse of this relation is <see cref="PrecededBy"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation Precedes;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>meets</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.End"/> of <c>I1</c> is the <see cref="ITimeInterval.Begin"/> 
        /// of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>m</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.End != null) &amp;&amp; (I2.Begin != null) &amp;&amp; (I1.End == I2.Begin)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-meets.png"/>
        /// <para>The converse of this relation is <see cref="MetBy"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation Meets;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>overlaps</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is earlier than the <see cref="ITimeInterval.Begin"/> 
        /// of <c>I2</c>, and the <see cref="ITimeInterval.End"/> of <c>I1</c> is later
        /// than the <see cref="ITimeInterval.Begin"/> of <c>I2</c> and earlier than the
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>o</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I1.End != null) &amp;&amp; (I2.Begin != null)
        /// &amp;&amp; (I2.End != null) &amp;&amp; (I1.Begin &lt; I2.Begin)
        /// &amp;&amp; (I1.End &gt; I2.Begin) &amp;&amp; (I1.End &lt; I2.End)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-overlaps.png"/>
        /// <para>The converse of this relation is <see cref="OverlappedBy"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation Overlaps;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>is finished by</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is earlier than the
        /// <see cref="ITimeInterval.Begin"/> of <c>I2</c>, and the <see cref="ITimeInterval.End"/>
        /// of <c>I1</c> is the <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>F</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I1.End != null) &amp;&amp; (I2.Begin != null)
        /// &amp;&amp; (I2.End != null) &amp;&amp; (I1.Begin &lt; I2.Begin)
        /// &amp;&amp; (I1.End == I2.End)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-finishedBy.png"/>
        /// <para>The converse of this relation is <see cref="Finishes"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation FinishedBy;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>contains</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is earlier than the
        /// <see cref="ITimeInterval.Begin"/> of <c>I2</c>, and the
        /// <see cref="ITimeInterval.End"/>
        /// of <c>I1</c> is later than the <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>D</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I1.End != null) &amp;&amp; (I2.Begin != null)
        /// &amp;&amp; (I2.End != null) &amp;&amp; (I1.Begin &lt; I2.Begin)
        /// &amp;&amp; (I1.End &gt; I2.End)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-contains.png"/>
        /// <para>The converse of this relation is <see cref="During"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation Contains;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>starts</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is the
        /// <see cref="ITimeInterval.Begin"/> of <c>I2</c>, and the
        /// <see cref="ITimeInterval.End"/>
        /// of <c>I1</c> is earlier than the <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>s</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I1.End != null) &amp;&amp; (I2.Begin != null)
        /// &amp;&amp; (I2.End != null) &amp;&amp; (I1.Begin == I2.Begin)
        /// &amp;&amp; (I1.End &lt; I2.End)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-starts.png"/>
        /// <para>The converse of this relation is <see cref="StartedBy"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation Starts;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>is equal to</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is the <see cref="ITimeInterval.Begin"/> 
        /// of <c>I2</c>, and the <see cref="ITimeInterval.End"/> of
        /// <c>I1</c> is the <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>e</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp; (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
        /// (I1.begin == I2.begin) &amp;&amp; (I1.end == I2.end)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-equals.png"/>
        /// <para>The converse of this relation is itself.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation EQUALS;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>is started by</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is the 
        /// <see cref="ITimeInterval.Begin"/> of <c>I2</c>, 
        /// and the <see cref="ITimeInterval.End"/> of <c>I1</c> is later than the 
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>S</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp;
        /// (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
        /// (I1.begin == I2.begin) &amp;&amp; (I1.end &gt; I2.end)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-startedBy.png"/>
        /// <para>The converse of this relation is <see cref="Starts"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation StartedBy;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>is during</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is later than the 
        /// <see cref="ITimeInterval.Begin"/> of <c>I2</c>, 
        /// and the <see cref="ITimeInterval.End"/> of <c>I1</c> is earlier than the 
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>d</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp;
        /// (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
        /// (I1.begin &gt; I2.begin) &amp;&amp; (I1.end &lt; I2.end)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-during.png"/>
        /// <para>The converse of this relation is <see cref="Contains"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation During;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>finishes</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is later than the 
        /// <see cref="ITimeInterval.Begin"/> of <c>I2</c>, 
        /// and the <see cref="ITimeInterval.End"/> of <c>I1</c> is the 
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>f</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp;
        /// (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
        /// (I1.begin &gt; I2.begin) &amp;&amp; (I1.end == I2.end)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-finishes.png"/>
        /// <para>The converse of this relation is <see cref="FinishedBy"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation Finishes;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>is overlapped by</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is later than the 
        /// <see cref="ITimeInterval.Begin"/> of <c>I2</c> and earlier than the
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>, 
        /// and the <see cref="ITimeInterval.End"/> of <c>I1</c> is later than the 
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>O</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.begin != null) &amp;&amp; (I1.end != null) &amp;&amp;
        /// (I2.begin != null) &amp;&amp; (I2.end != null) &amp;&amp;
        /// (I1.begin &gt; I2.begin) &amp;&amp; (I1.Begin &lt; I2.End) &amp;&amp; (I1.End &gt; I2.End)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-overlappedBy.png"/>
        /// <para>The converse of this relation is <see cref="Overlaps"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation OverlappedBy;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>is met by</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is the 
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>M</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.End != null) &amp;&amp; (I1.Begin == I2.End)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-metBy.png"/>
        /// <para>The converse of this relation is <see cref="Meets"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation MetBy;

        /// <summary>
        /// A <strong>basic</strong> time interval relation that says that an interval
        /// <c>I1</c> <dfn>is preceded by</dfn> an interval <c>I2</c>, i.e., the
        /// <see cref="ITimeInterval.Begin"/> of <c>I1</c> is later than the 
        /// <see cref="ITimeInterval.End"/> of <c>I2</c>.
        /// <para>The conventional short representation of this Allen relation 
        /// is &quot;<c><strong>P</strong></c>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.End != null) &amp;&amp; (I1.Begin &gt; I2.End)
        /// </code>
        /// <img style="text-align: center;" src="AllenRelation-precededBy.png"/>
        /// <para>The converse of this relation is <see cref="Precedes"/>.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation PrecededBy;

        /// <summary>
        /// The full time interval relation, which expresses that nothing definite can be
        /// said about the relationship between 2 periods.
        /// </summary>
        /// <remarks>
        /// The converse of this relation is the relation itself.
        /// </remarks>
        public static readonly TimeIntervalRelation Full;

        /// <summary>
        /// The set of all 13 basic time point-interval relations. That they are presented here in
        /// a particular order is not relevant
        /// for the user.
        /// </summary>
        /// <remarks>
        /// That they are presented here in a particular order, is a pleasant side note, but in
        /// general not relevant for the user. The list is ordered, first on the first interval
        /// beginning before the second (<c>I1.<see cref="ITimeInterval.Begin"/> [&lt;, ==, &gt;]
        /// I2.<see cref="ITimeInterval.Begin"/></c>) and secondly on the first interval ending
        /// before the second (<c>I1.<see cref="ITimeInterval.End"/> [&lt;, ==, &gt;]
        /// I2.<see cref="ITimeInterval.End"/></c>).
        /// </remarks>
        public static readonly TimeIntervalRelation[] BasicRelations;

        private static readonly string[] s_BasicCodes =
        {
            "p", "m", "o", "F", "D", "s", "e", "S", "d", "f", "O", "M", "P"
        };

        #endregion

        #region Secondary relations

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> and an interval <c>I2</c> 
        /// are concurrent in some way.
        /// </summary>
        /// <remarks>
        /// Thus, <c>I1</c> does <em>not</em> precede <c>I2</c>, 
        /// <c>I1</c> does <em>not</em> meet <c>I2</c>,
        /// <c>I1</c> is <em>not</em> met be <c>I2</c>, 
        /// and <c>I1</c> is <em>not</em> preceded by <c>I2</c>.
        /// This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.
        /// </remarks>
        public static readonly TimeIntervalRelation ConcursWith;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> begins earlier than
        /// an interval <c>I2</c> begins.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.Begin != null) &amp;&amp; (I1.Begin &lt; I2.Begin)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation BeginsEarlier;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> and an interval <c>I2</c>
        /// begin at the same time.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.Begin != null) &amp;&amp; (I1.Begin == I2.Begin)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation BeginTogether;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> begins later than
        /// an interval <c>I2</c>.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.Begin != null) &amp;&amp; (I1.Begin &gt; I2.Begin)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation BeginsLater;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> begins inside
        /// an interval <c>I2</c>.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.Begin != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.Begin &gt; I2.Begin) &amp;&amp; (I1.Begin &lt; I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation BeginsIn;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> begins earlier and ends earlier
        /// an interval <c>I2</c> begins and ends.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.Begin != null)
        /// &amp;&amp; (I1.End != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.Begin &lt; I2.Begin) &amp;&amp; (I1.End &lt; I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation BeginsEarlierAndEndsEarlier;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> begins later and ends later than
        /// an interval <c>I2</c> begins and ends.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I2.Begin != null)
        /// &amp;&amp; (I1.End != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.Begin &gt; I2.Begin) &amp;&amp; (I1.End &gt; I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation BeginsLaterAndEndsLater;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> ends earlier than
        /// an interval <c>I2</c> ends.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.End != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.End &lt; I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation EndsEarlier;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> ends inside
        /// an interval <c>I2</c>.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.End != null) &amp;&amp; (I2.Begin != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.End &gt; I2.Begin) &amp;&amp; (I1.End &lt; I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation EndsIn;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> and an interval <c>I2</c>
        /// end at the same time.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.End != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.End == I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation EndTogether;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> ends later than an 
        /// interval <c>I2</c> ends.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.End != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.End &gt; I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation EndsLater;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> contains the begin of
        /// an interval <c>I2</c>.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I1.End != null) &amp;&amp; (I2.Begin != null)
        /// &amp;&amp; (I1.Begin &lt; I2.Begin) &amp;&amp; (I1.End &gt; I2.Begin)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation ContainsBegin;

        /// <summary>
        /// A non-basic time interval relation that is often handy to use,
        /// which expresses that an interval <c>I1</c> contains the end of
        /// an interval <c>I2</c>.
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I1.Begin != null) &amp;&amp; (I1.End != null) &amp;&amp; (I2.End != null)
        /// &amp;&amp; (I1.Begin &lt; I2.End) &amp;&amp; (I1.End &gt; I2.End)
        /// </code>
        /// <para>This relation is introduced because it is the possible result of
        /// the composition of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimeIntervalRelation ContainsEnd;

        #endregion

        #region N-ary operations

        /// <summary>
        /// <para>The main factory method for AllenRelations.</para>
        /// <para>Although this is intended to create any disjunction of the basic relations,
        /// you can use any relation in the argument list.</para>
        /// </summary>
        /// <returns>
        /// <para>This is the union of all time interval relations in
        /// <paramref name="tirs"/>, when they are considered as sets of basic
        /// relations.</para>
        /// </returns>
        /// <remarks>
        /// <para><see cref="op_BitwiseOr">|</see> is a binary operator version 
        /// of this method.</para>
        /// </remarks>
        [Pure]
        public static TimeIntervalRelation Or(params TimeIntervalRelation[] tirs)
        {
            Contract.Ensures(Contract.ForAll(
                BasicRelations,
                br => Contract.Exists(tirs, tir => tir.ImpliedBy(br))
                      == Contract.Result<TimeIntervalRelation>().ImpliedBy(br)));

            uint acc = tirs.Aggregate<TimeIntervalRelation, uint>(
                EmptyBitPattern,
                (current, tir) => current | tir.m_BitPattern);
            return Values[acc];
        }

        /// <summary>
        /// <param>Binary operator version of <see cref="Or"/>.</param>
        /// <inheritdoc cref="Or"/>
        /// </summary>
        /// <returns>
        /// <inheritdoc cref="Or"/>
        /// </returns>
        [Pure]
        public static TimeIntervalRelation operator |(TimeIntervalRelation tir1, TimeIntervalRelation tir2)
        {
            /* TODO
             * This contract crashes Contracts. Probably because of the var params of Or.
            Contract.Ensures(Contract.Result<TimeIntervalRelation>() == Or(tir1, tir2));
             */

            return Or(tir1, tir2);
        }

  //**
  // * The conjunction of the time interval relations in {@code gr}.
  // * This is the intersection of all time interval relations in {@code gr}, when they are considered
  // * as sets of basic relations.
  // */
  //@MethodContract(post = {
  //  @Expression("for (TimeIntervalRelation br : BASIC_RELATIONS) {for (TimeIntervalRelation ar : _gr) {ar.impliedBy(br)} ?? result.impliedBy(br)}")
  //})
  //public static TimeIntervalRelation and(TimeIntervalRelation... gr) {
  //  int acc = FULL_BIT_PATTERN;
  //  for (TimeIntervalRelation tir : gr) {
  //    acc &= tir.$bitPattern;
  //  }
  //  return VALUES[acc];
  //}

        /// <summary>
        /// <para>Remove basic relations in <paramref name="term2"/> from
        /// <paramref name="term1"/>.</para>
        /// </summary>
        /// <returns>
        /// <para>This is the difference between the time point-interval relations in
        /// <paramref name="term1"/> and <paramref name="term2"/>, 
        /// when they are considered as sets of basic relations.</para>
        /// </returns>
        /// <remarks>
        /// <para><see cref="op_Subtraction">-</see> is a operator version of this method.</para>
        /// </remarks>
        [Pure]
        public static TimeIntervalRelation Min(TimeIntervalRelation term1, TimeIntervalRelation term2)
        {
            Contract.Ensures(Contract.ForAll(
                BasicRelations,
                br => (br.Implies(Contract.Result<TimeIntervalRelation>())
                    == (br.Implies(term1) && (! br.Implies(term2))))));

            uint xor = term1.m_BitPattern ^ term2.m_BitPattern;
            uint min = term1.m_BitPattern & xor;
            return Values[min];
        }

        /// <summary>
        /// <param>Operator version of <see cref="Min"/>.</param>
        /// <inheritdoc cref="Min"/>
        /// </summary>
        /// <returns>
        /// <inheritdoc cref="Min"/>
        /// </returns>
        [Pure]
        public static TimeIntervalRelation operator -(TimeIntervalRelation term1, TimeIntervalRelation term2)
        {
            Contract.Ensures(Contract.Result<TimeIntervalRelation>() == Min(term1, term2));

            return Min(term1, term2);
        }

        /// <summary>
        /// The relation from <paramref name="i1"/> to <paramref name="i2"/> with the lowest possible
        /// <see cref="Uncertainty"/>.
        /// </summary>
        /// <returns>
        /// <c>null</c> as <see cref="ITimeInterval.Begin"/> or <see cref="ITimeInterval.End"/>
        /// is considered as unknown, and thus is not used to restrict the relation more, leaving
        /// it with more <see cref="Uncertainty"/>.
        /// </returns>
        [Pure]
        public static TimeIntervalRelation LeastUncertainTimeIntervalRelation(ITimeInterval i1, ITimeInterval i2)
        {
            // MUDO contract
            TimeIntervalRelation result = Full;
            //if (i1.Begin != null) {
            //  if (i2.Begin != null) {
            //    if (i1.Begin < i2.Begin) {
            //      result = Min(result, BeginsEarlier.complement());
            //    }
            //    else if (i1.Begin == i2.Begin) {
            //      result = Min(result, BeginTogether.complement());
            //    }
            //    else {
            //        Contract.Assume(i1.Begin > i2.Begin);
            //      result = Min(result, BeginsLater.complement());
            //    }
            //  }
            //  if (i2.End != null) {
            //    if (i1.Begin< i2.End)) { // pmoFDseSdfO, not MP; begins before end
            //      result = Min(result, MetBy);
            //      result = Min(result, PrecededBy);
            //    }
            //    else if (i1.Begin == i2.End) {
            //      if (i1.End != null && i2.Begin != null && i1.End == i2.Begin)
            //      {
            //          Contract.Assume(i1.Begin == i1.End);
            //          Contract.Assume(i2.Begin == i2.End);
            //        return EQUALS;
            //      }
            //      else {
            //        return MetBy;
            //      }
            //    }
            //    else
            //    {
            //        Contract.Assume(i1.Begin > i2.End);
            //      return PrecededBy;
            //    }
            //  }
            //}
            //if (i1.End != null) {
            //  if (i2.Begin != null) {
            //    if (i1.End < i2.Begin) {
            //      return Precedes;
            //    }
            //    else if (i1.End == i2.Begin) {
            //      if (i1.Begin != null && i2.End != null && i1.Begin == i2.End) {
            //          Contract.Assume(i1.Begin == i1.End);
            //          Contract.Assume(i2.Begin == i2.End);
            //        return EQUALS;
            //      }
            //      return Meets;
            //    }
            //    else {
            //        Contract.Assume(i1.End > i2.Begin) // not pm, oFDseSdfOMP, ends after begin
            //      result = Min(result, Precedes);
            //      result = Min(result, Meets);
            //    }
            //  }
            //  if (i2.End != null) {
            //    if (i1.End < i2.End) {
            //      result = Min(result, EndsEarlier.complement());
            //    }
            //    else if (i1.End == i2.End) {
            //      result = Min(result, EndTogether.complement());
            //    }
            //    else {
            //      Contract.Assume(i1.End > i2.End);
            //      result = Min(result, EndsLater);
            //    }
            //  }
            //}
            return result;
        }

        #endregion

        #region Construction

        /// <summary>
        /// There is only 1 private constructor, that constructs the wrapper object
        /// around the bitpattern. This is used exclusively in <see cref="Values"/> 
        /// initialization code, in the <see cref="TimeIntervalRelation">static
        /// constructor</see>.
        /// </summary>
        private TimeIntervalRelation(uint bitPattern)
        {
            Contract.Requires(bitPattern >= EmptyBitPattern);
            Contract.Requires(bitPattern <= FullBitPattern);
            // Cannot express this postcondition in a struct for compiler reasons
            // Contract.Ensures(m_BitPattern == bitPattern);

            m_BitPattern = bitPattern;
        }

        #endregion

        #region Internal representation

        /// <summary>
        /// Only the 13 lowest bits are used. The other (32 - 13 = 19 bits) are 0.
        /// </summary>
        /// <remarks>
        /// Representation invariants (private):
        /// <code>
        /// Contract.Invariant(bitPattern &gt;= EMPTY_BIT_PATTERN);
        /// Contract.Invariant(bitPattern &lt;= FullBitPattern);
        /// </code>
        /// </remarks>
        private readonly uint m_BitPattern;

        #endregion

        #region Equality

        [Pure]
        public override bool Equals(object obj)
        {
            Contract.Ensures(Contract.Result<bool>()
                             == (obj != null
                                 && obj is TimeIntervalRelation
                                 && Equals((TimeIntervalRelation)obj)));

            return obj != null && obj is TimeIntervalRelation && m_BitPattern == ((TimeIntervalRelation)obj).m_BitPattern;
        }

        /// <summary>
        /// Basic equality.
        /// </summary>
        [Pure]
        public bool Equals(TimeIntervalRelation other)
        {
            return m_BitPattern == other.m_BitPattern;
        }

        [Pure]
        public static bool operator ==(TimeIntervalRelation tir1, TimeIntervalRelation tir2)
        {
            Contract.Ensures(Contract.Result<bool>() == tir1.Equals(tir2));

            // tpirN != null: It's a struct. Can't be null.);));)
            return tir1.m_BitPattern == tir2.m_BitPattern;
        }

        [Pure]
        public static bool operator !=(TimeIntervalRelation tir1, TimeIntervalRelation tir2)
        {
            Contract.Ensures(Contract.Result<bool>() == !(tir1 == tir2));

            // tpirN != null: It's a struct. Can't be null.
            return tir1.m_BitPattern != tir2.m_BitPattern;
        }

        [Pure]
        public override int GetHashCode()
        {
            return (int)m_BitPattern;
        }

        #endregion

        #region Instance operations

        /// <summary>
        /// An ordinal for basic relations.
        /// </summary>
        public int BasicRelationalOrdinal
        {
            get
            {
                Contract.Requires(IsBasic);
                Contract.Ensures(Contract.Result<int>() >= 0);
                Contract.Ensures(Contract.Result<int>() < 13);

                /*
                 * This is the bit position, 0-based, in the 5-bit bit pattern, of the bit
                 * representing this as basic relation.
                 */
                return m_BitPattern.NumberOfTrailingZeros();
            }
        }

        /// <summary>
        /// This is a basic time interval relation.
        /// </summary>
        public bool IsBasic
        {
            get
            {
                Contract.Ensures(Contract.Result<bool>() == BasicRelations.Contains(this));

                return m_BitPattern.IsBasicBitPattern();
            }
        }

        /// <summary>
        /// A measure about the uncertainty this time interval relation expresses.
        /// </summary>
        /// <return>
        /// <para>This is the fraction of the 13 basic relations that imply this general relation.</para>
        /// <para><see cref="Full"/> is complete uncertainty, and returns <c>1</c>.</para>
        /// <para>A <see cref="IsBasic">basic relation</see> is complete certainty,
        /// and returns <c>0</c>.</para>
        /// <para>The <see cref="Empty"/> relation has no meaningful uncertainty.
        /// This method returns <see cref="Float.NaN"/> as value for <see cref="Empty"/>.</para>
        /// </return>
        public float Uncertainty
        {
            get
            {
                Contract.Ensures(this != Empty
                                     ? Math.Abs(Contract.Result<float>() - ((NrOfBasicRelations - 1) / 12.0F)) <= 0.1e-6F
                                     : true);
                Contract.Ensures(this == Empty ? float.IsNaN(Contract.Result<float>()) : true);

                int count = m_BitPattern.BitCount();
                if (count == 0)
                {
                    return float.NaN;
                }
                count--;
                float result = count / 12.0F;
                return result;
            }
        }

        public int NrOfBasicRelations
        {
            get
            {
                Contract.Ensures(Contract.Result<int>() == CalcNrOfBasicRelations(this));

                return CalcNrOfBasicRelations(this);
            }
        }

        /// <summary>
        /// Introduced to technically make postcondition possible.
        /// </summary>
        /// <remarks>
        /// Cannot refer to "this" in lambda function if this is a struct.
        /// </remarks>
        [Pure]
        public static int CalcNrOfBasicRelations(TimeIntervalRelation tpir)
        {
            Contract.Ensures(Contract.Result<int>() == BasicRelations.Count(br => br.Implies(tpir)));

            return BasicRelations.Count(br => br.Implies(tpir));
        }

        /// <summary>
        /// <para>Is <c>this</c> implied by <paramref name="tpir"/>?</para>
        /// <para>In other words, when considering the relations as a set of basic relations, is
        /// <c>this</c> a superset of <paramref name="tpir"/>
        /// (considering equality as also acceptable)?</para>
        /// </summary>
        /// <param name="tpir"></param>
        /// <returns></returns>
        [Pure]
        public bool ImpliedBy(TimeIntervalRelation tpir)
        {
            return (m_BitPattern & tpir.m_BitPattern) == tpir.m_BitPattern;
        }

        /// <summary>
        /// Does <c>this</c> imply <paramref name="tpir"/>? In other words, when considering
        /// the relations as a set of basic relations, is <c>this</c> a subset of
        /// <paramref name="tpir"/> (considering equality as also acceptable)?
        /// </summary>
        [Pure]
        public bool Implies(TimeIntervalRelation tpir)
        {
            Contract.Ensures(Contract.Result<bool>() == tpir.ImpliedBy(this));

            return (tpir.m_BitPattern & m_BitPattern) == m_BitPattern;
        }

        #endregion

        /// <summary>
        /// This returns a representation of the time point-interval
        /// relation in the most used short notation
        /// (&lt; =[&lt; &gt;&lt; =[&gt; &gt;).
        /// </summary>
        [Pure]
        public override string ToString()
        {
            StringBuilder result = new StringBuilder();
            result.Append("(");
            if (IsBasic)
            {
                result.Append(s_BasicCodes[BasicRelationalOrdinal]);
            }
            else
            {
                for (int i = 0; i < s_BasicCodes.Length; i++)
                {
                    if (ImpliedBy(BasicRelations[i]))
                    {
                        result.Append(s_BasicCodes[i]);
                    }
                }
            }
            result.Append(")");
            return result.ToString();
        }
    }
}