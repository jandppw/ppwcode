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
    public struct TimePointIntervalRelation : IEquatable<TimePointIntervalRelation>
    {
        #region Type invariants

        [ContractInvariantMethod]
        private void TypeInvariants()
        {
            // Values
            Contract.Invariant(ClassInitialized ? Values != null : true);
            Contract.Invariant(ClassInitialized ? Values.Length == NrOfRelations : true);
            // Values cannot contain null, because the type is TimePointIntervalRelation[], not
            // TimePointIntervalRelation?[]
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
                "Any instance there ever can be is mentioned in VALUES. There are no other instances.");

            // Basic relations
            Contract.Invariant(ClassInitialized ? BasicRelations != null : true);
            Contract.Invariant(ClassInitialized ? Contract.ForAll(BasicRelations, basic => ! Empty.ImpliedBy(basic)) : true);
            // TODO Contract.Invariant(ClassInitialized ? Full == Or(Before, Begins, In, Ends, After) : true);
            // Contracts sees an assignment here
            Contract.Invariant(ClassInitialized ? Contract.ForAll(BasicRelations, br => BasicRelations[br.BasicRelationalOrdinal] == br) : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[0] == Before : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[1] == Begins : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[2] == In : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[3] == Ends : true);
            Contract.Invariant(ClassInitialized ? BasicRelations[4] == After : true);

            // Secondary relations
            Contract.Invariant(ClassInitialized ? ConcursWith == (Begins | In) : true);
            Contract.Invariant(ClassInitialized ? BeforeEnd == (Before | Begins | In) : true);
            Contract.Invariant(ClassInitialized ? AfterBegin == (In | Ends | After) : true);

            // About relations
            Contract.Invariant(ImpliedBy(this));
            Contract.Invariant(ClassInitialized && IsBasic ? IsNotImpliedByBasicTpirs(this) : true);
            Contract.Invariant(ClassInitialized ? ImplyingMeansEveryBasicTpirThatImpliesAlsoImplies(this) : true);
        }

        /// <summary>
        /// Introduced to technically make invariants possible.
        /// </summary>
        /// <remarks>
        /// Cannot refer to "this" in lambda function if this is a struct.
        /// </remarks>
        [Pure]
        public static bool IsNotImpliedByBasicTpirs(TimePointIntervalRelation tpir)
        {
            Contract.Ensures(Contract.ForAll(BasicRelations, br => br != tpir ? (!tpir.ImpliedBy(br)) : true));

            return BasicRelations.All(br => br != tpir ? (!tpir.ImpliedBy(br)) : true);
        }

        /// <summary>
        /// Introduced to technically make invariants possible.
        /// </summary>
        /// <remarks>
        /// Cannot refer to "this" in lambda function if this is a struct.
        /// </remarks>
        [Pure]
        public static bool ImplyingMeansEveryBasicTpirThatImpliesAlsoImplies(TimePointIntervalRelation tpir)
        {
            Contract.Ensures(Contract.Result<bool>() ==
                             Contract.ForAll(
                                 Values,
                                 v => tpir.ImpliedBy(v) ==
                                      Contract.ForAll(
                                          BasicRelations,
                                          br => v.ImpliedBy(br) ? tpir.ImpliedBy(br) : true)));

            return Values
                .Where(tpir.ImpliedBy)
                .All(v => BasicRelations.Where(v.ImpliedBy).All(tpir.ImpliedBy));
        }

        #endregion

        /*
         * Implementation note:
         *
         * time point-interval relations are implemented as a 5-bit bit pattern, stored in the
         * 5 least significant bits of a 32-bit int. Each of those 5 bits represents a basic relation,
         * being in the general relation (<c>1</c>) or not being in the general relation (<c>0</c>).
         * The order of the basic relations in the bit pattern is important for some of the
         * algorithms. There is some trickery involved.
         */

        #region Population

        /// <summary>
        /// The total number of possible time point-interval relations <strong>= 32</strong>
        /// (i.e., <c>2<sup>5</sup></c>).
        /// </summary>
        public const int NrOfRelations = 32;

        /// <summary>
        /// All possible time point-interval relations.
        /// </summary>
        public static readonly TimePointIntervalRelation[] Values;

        static TimePointIntervalRelation()
        {
            ClassInitialized = false;
            Values = new TimePointIntervalRelation[NrOfRelations];
            for (uint i = 0; i < NrOfRelations; i++)
            {
                Values[i] = new TimePointIntervalRelation(i);
            }
            Empty = Values[EmptyBitPattern];
            Before = Values[BeforeBitPattern];
            Begins = Values[BeginsBitPattern];
            In = Values[InBitPattern];
            Ends = Values[EndsByBitPattern];
            After = Values[AfterBitPattern];
            Full = Values[FullBitPattern];
            BasicRelations = new TimePointIntervalRelation[]
            {
                Before, Begins, In, Ends, After
            };
            ConcursWith = Begins | In;
            BeforeEnd = Or(Before, Begins, In);
            AfterBegin = Or(In, Ends, After);
            BasicCompositions = new TimePointIntervalRelation[][]
            {
                new TimePointIntervalRelation[]
                {
                    Before, Before, Before, Before, Before, Before, Before, Before, BeforeEnd, BeforeEnd, BeforeEnd, BeforeEnd, Full
                },
                new TimePointIntervalRelation[]
                {
                    Before, Before, Before, Before, Before, Begins, Begins, Begins, In, In, In, Ends, After
                },
                new TimePointIntervalRelation[]
                {
                    Before, Before, BeforeEnd, BeforeEnd, Full, In, In, AfterBegin, In, In, AfterBegin, After, After
                },
                new TimePointIntervalRelation[]
                {
                    Before, Begins, In, Ends, After, In, Ends, After, In, Ends, After, After, After
                },
                new TimePointIntervalRelation[]
                {
                    Full, AfterBegin, AfterBegin, After, After, AfterBegin, After, After, AfterBegin, After, After, After, After
                }
            };
            ClassInitialized = true;
        }

        /// <summary>
        /// Ignore. Only here as a technical means to make static type invariants work.
        /// </summary>
        public static readonly bool ClassInitialized;

        #endregion

        #region Basic relations

        private const int EmptyBitPattern = 0; // 00000
        private const int BeforeBitPattern = 1; // 00001 <
        private const int BeginsBitPattern = 2; // 00010 =[<
        private const int InBitPattern = 4; // 00100 ><
        private const int EndsByBitPattern = 8; // 01000 =[>
        private const int AfterBitPattern = 16; // 10000 >
        private const int FullBitPattern = 31; // 11111 < =[< >< =[> >

        /// <summary>
        /// This empty relation is not a true time point-interval relation. It does not express a
        /// relational condition between intervals. Yet, it is needed for
        /// consistencey with some operations on time point-interval relations.
        /// </summary>
        public static readonly TimePointIntervalRelation Empty;

        /// <summary>
        /// <para>A <strong>basic</strong> time point-interval relation that says that a point in time
        /// <var>t</var> <dfn>comes before</dfn> an interval <var>I</var>, i.e., the
        /// <var>t</var> is before the begin of <var>I</var>.</para>
        /// <para>The short representation of this time point-interval relation is
        /// &quot;<code><strong>&lt;</strong></code>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I.Begin != null) &amp;&amp; (t &lt; I.Begin)
        /// </code>
        /// <img style="text-align: center;" src="TimePointIntervalRelation-before.png"/>
        /// </remarks>
        public static readonly TimePointIntervalRelation Before;

        /// <summary>
        /// <para>A <strong>basic</strong> time point-interval relation that says that a point in time
        /// <var>t</var> <dfn>begins</dfn> an interval <var>I</var>, i.e., the
        /// <var>t</var> is the begin of <var>I</var>.</para>
        /// <para>The short representation of this time point-interval relation is
        /// &quot;<code><strong>=[&lt;</strong></code>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I.Begin != null) &amp;&amp; (t == I.Begin)
        /// </code>
        /// <img style="text-align: center;" src="TimePointIntervalRelation-begins.png"/>
        /// </remarks>
        public static readonly TimePointIntervalRelation Begins;

        /// <summary>
        /// <para>A <strong>basic</strong> time point-interval relation that says that a point in time
        /// <var>t</var> <dfn>falls in</dfn> an interval <var>I</var>, i.e., the
        /// <var>t</var> is after the begin of <var>I</var> and before the end of <var>I</var>.</para>
        /// <para>The short representation of this time point-interval relation is
        /// &quot;<code><strong>&gt;&lt;</strong></code>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I.Begin != null) &amp;&amp; (I.End != null) &amp;&amp; (t &gt; I.Begin) &amp;&amp; (t &lt; I.End)
        /// </code>
        /// <img style="text-align: center;" src="TimePointIntervalRelation-in.png"/>
        /// </remarks>
        public static readonly TimePointIntervalRelation In;

        /// <summary>
        /// <para>A <strong>basic</strong> time point-interval relation that says that a point in time
        /// <var>t</var> <dfn>ends</dfn> an interval <var>I</var>, i.e., the
        /// <var>t</var> is the end of <var>I</var>.</para>
        /// <para>The short representation of this time point-interval relation is
        /// &quot;<code><strong>=[&gt;</strong></code>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I.End != null) &amp;&amp; (t == I.End)
        /// </code>
        /// <img style="text-align: center;" src="TimePointIntervalRelation-ends.png"/>
        /// </remarks>
        public static readonly TimePointIntervalRelation Ends;

        /// <summary>
        /// <para>A <strong>basic</strong> time point-interval relation that says that a point in time
        /// <var>t</var> <dfn>comes after</dfn> an interval <var>I</var>, i.e., the
        /// <var>t</var> is after the end of <var>I</var>.</para>
        /// <para>The short representation of this time point-interval relation is
        /// &quot;<code><strong>&gt;</strong></code>&quot;.</para>
        /// </summary>
        /// <remarks>
        /// <code>
        /// (I.End != null) &amp;&amp; (t &gt; I.End)
        /// </code>
        /// <img style="text-align: center;" src="TimePointIntervalRelation-after.png"/>
        /// </remarks>
        public static readonly TimePointIntervalRelation After;

        /// <summary>
        /// The full time point-interval relation, which expresses that nothing definite can be
        /// said about the relationship between a time point and a time interval.
        /// </summary>
        public static readonly TimePointIntervalRelation Full;

        /// <summary>
        /// The set of all 5 basic time point-interval relations. That they are presented here in
        /// a particular order, is a pleasant side note, but in general not relevant
        /// for the user.
        /// </summary>
        public static readonly TimePointIntervalRelation[] BasicRelations;

        private static readonly string[] s_BasicCodes =
            {
                "<", "=[<", "><", "=[>", ">"
            };

        #endregion

        #region Construction

        /// <summary>
        /// There is only 1 private constructor, that constructs the wrapper object
        /// around the bitpattern. This is used exclusively in <see cref="Values"/> 
        /// initialization code, in the <see cref="TimePointIntervalRelation">static
        /// constructor</see>.
        /// </summary>
        private TimePointIntervalRelation(uint bitPattern)
        {
            Contract.Requires(bitPattern >= EmptyBitPattern);
            Contract.Requires(bitPattern <= FullBitPattern);
            // Cannot express this postcondition in a struct for compiler reasons
            // Contract.Ensures(m_BitPattern == bitPattern);

            m_BitPattern = bitPattern;
        }

        #endregion

        #region Representation

        /// <summary>
        /// Only the 5 lowest bits are used. The other (32 - 5 = 27 bits) are 0.
        /// </summary>
        /// <remarks>
        /// Representation Invariants:
        /// <code>
        /// m_BitPattern &gt;= EMPTY_BIT_PATTERN;
        /// m_BitPattern &lt;= FULL_BIT_PATTERN;
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
                                 && obj is TimePointIntervalRelation
                                 && Equals((TimePointIntervalRelation)obj)));

            return obj != null && obj is TimePointIntervalRelation && m_BitPattern == ((TimePointIntervalRelation)obj).m_BitPattern;
        }

        /// <summary>
        /// Basic equality.
        /// </summary>
        [Pure]
        public bool Equals(TimePointIntervalRelation other)
        {
            return m_BitPattern == other.m_BitPattern;
        }

        [Pure]
        public static bool operator ==(TimePointIntervalRelation tpir1, TimePointIntervalRelation tpir2)
        {
            Contract.Ensures(Contract.Result<bool>() == tpir1.Equals(tpir2));

            // tpirN != null: It's a struct. Can't be null.);));)
            return tpir1.m_BitPattern == tpir2.m_BitPattern;
        }

        [Pure]
        public static bool operator !=(TimePointIntervalRelation tpir1, TimePointIntervalRelation tpir2)
        {
            Contract.Ensures(Contract.Result<bool>() == ! (tpir1 == tpir2));

            // tpirN != null: It's a struct. Can't be null.
            return tpir1.m_BitPattern != tpir2.m_BitPattern;
        }

        [Pure]
        public override int GetHashCode()
        {
            return (int)m_BitPattern;
        }

        #endregion

        #region Secondary relations

        /// <summary>
        /// <para>A non-basic time point-interval relation that is often handy to use,
        /// which expresses that a time point <var>t</var> and an interval <var>I</var>
        /// are concurrent in some way.</para>
        /// <para>Thus, <var>t</var> does <em>not</em> come before <var>I</var>,
        /// <var>t</var> is not the end time of <var>I</var>, and <var>t</var> does <em>not</em> 
        /// come after <var>I</var> 
        /// (remember that we define time intervals as right half-open).</para>
        /// </summary>
        public static readonly TimePointIntervalRelation ConcursWith;

        /// <summary>
        /// <para>A non-basic time point-interval relation that is often handy to use,
        /// which expresses that a time point <var>t</var> is earlier than an interval
        /// <var>I</var> ends:</para>
        /// <code>
        /// (I.End != null) &amp;&amp; (t &lt; I.End)
        /// </code>
        /// </summary>
        /// <remarks>
        /// <para>This relation is introduced because it is the possible result of the
        /// <see cref="Compose">composition</see> of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimePointIntervalRelation BeforeEnd;

        /// <summary>
        /// <para>A non-basic time point-interval relation that is often handy to use,
        /// which expresses that a time point <var>t</var> is later than an interval 
        /// <var>I</var> begins:</para>
        /// <code>
        /// (I.Begin != null) &amp;&amp; (t &gt; I.Begin)
        /// </code>
        /// </summary>
        /// <remarks>
        /// <para>This relation is introduced because it is the possible result of the
        /// <see cref="Compose">composition</see> of 2 basic relations.</para>
        /// </remarks>
        public static readonly TimePointIntervalRelation AfterBegin;

        #endregion

        #region N-ary operations

        /// <summary>
        /// <para>The main factory method for <c>TimePointIntervalRelations</c>.</para>
        /// <para>Although this is intended to create any disjunction of the basic relations,
        /// you can use any relation in the argument list.</para>
        /// </summary>
        /// <returns>
        /// <para>This is the union of all time point-interval relations in
        /// <paramref name="tpirs"/>, when they are considered as sets of basic
        /// relations.</para>
        /// </returns>
        /// <remarks>
        /// <para><see cref="op_BitwiseOr">|</see> is a binary operator version of this method.</para>
        /// </remarks>
        [Pure]
        public static TimePointIntervalRelation Or(params TimePointIntervalRelation[] tpirs)
        {
            Contract.Ensures(Contract.ForAll(
                BasicRelations,
                br => (Contract.Exists(tpirs, tpir => tpir.ImpliedBy(br))
                       == Contract.Result<TimePointIntervalRelation>().ImpliedBy(br))));

            uint acc = tpirs.Aggregate<TimePointIntervalRelation, uint>(
                EmptyBitPattern,
                (current, tpir) => current | tpir.m_BitPattern);
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
        public static TimePointIntervalRelation operator |(TimePointIntervalRelation tpir1, TimePointIntervalRelation tpir2)
        {
            /* TODO
             * This contract crashes Contracts. Probably because of the var params of Or.
            Contract.Ensures(Contract.Result<TimePointIntervalRelation>() == Or(tpir1, tpir2));
             */

            return Or(tpir1, tpir2);
        }

        /// <summary>
        /// <para>The conjunction of the time point-interval relations in <paramref name="tpirs"/>.</para>
        /// </summary>
        /// <returns>
        /// <para>This is the intersection of all time point-interval relations in
        /// <paramref name="tpirs"/>, when they are considered as sets of basic relations.</para>
        /// </returns>
        /// <remarks>
        /// <para><see cref="op_BitwiseAnd">&amp;</see> is a binary operator version of this method.</para>
        /// </remarks>
        public static TimePointIntervalRelation And(params TimePointIntervalRelation[] tpirs)
        {
            Contract.Ensures(Contract.ForAll(
                BasicRelations,
                br => (Contract.ForAll(tpirs, tpir => tpir.ImpliedBy(br))
                           ? Contract.Result<TimePointIntervalRelation>().ImpliedBy(br)
                           : true)
                      && Contract.Result<TimePointIntervalRelation>().ImpliedBy(br)
                          ? Contract.ForAll(tpirs, tpir => tpir.ImpliedBy(br))
                          : true));

            uint acc = tpirs.Aggregate<TimePointIntervalRelation, uint>(
                FullBitPattern,
                (current, tpir) => current & tpir.m_BitPattern);
            return Values[acc];
        }

        /// <summary>
        /// <param>Binary operator version of <see cref="And"/>.</param>
        /// <inheritdoc cref="And"/>
        /// </summary>
        /// <returns>
        /// <inheritdoc cref="And"/>
        /// </returns>
        [Pure]
        public static TimePointIntervalRelation operator &(TimePointIntervalRelation tpir1, TimePointIntervalRelation tpir2)
        {
            /* TODO
             * This contract crashes Contracts. Probably because of the var params of And.
            Contract.Ensures(Contract.Result<TimePointIntervalRelation>() == And(tpir1, tpir2));
             */

            return And(tpir1, tpir2);
        }

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
        public static TimePointIntervalRelation Min(TimePointIntervalRelation term1, TimePointIntervalRelation term2)
        {
            Contract.Ensures(Contract.ForAll(
                BasicRelations,
                br => (br.Implies(Contract.Result<TimePointIntervalRelation>())
                           ? br.Implies(term1) && (! br.Implies(term2))
                           : true)
                      && (br.Implies(term1) && (!br.Implies(term2))
                              ? br.Implies(Contract.Result<TimePointIntervalRelation>())
                              : true)));

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
        public static TimePointIntervalRelation operator -(TimePointIntervalRelation term1, TimePointIntervalRelation term2)
        {
            Contract.Ensures(Contract.Result<TimePointIntervalRelation>() == Min(term1, term2));

            return Min(term1, term2);
        }

        /// <summary>
        /// This matrix holds the compositions of basic time point-interval relations with Allen relations.
        /// These are part of the given semantics, and cannot be calculated.
        /// See <see cref="Compose"/>.
        /// </summary>
        public static readonly TimePointIntervalRelation[][] BasicCompositions;

        // MUDO TimeIntervalRelation NIY
        ///// <summary>
        ///// Given a point in time <c><var>t</var></c> and a 2 time intervals <c><var>I1</var></c>,
        ///// <c><var>I2</var></c>, given
        ///// <c>tpir = LeastUncertainTimePointIntervalRelation(<var>t</var>, <var>I1</var>)</c> 
        ///// and <c>tir == LeastUncertainTimeIntervalRelation(<var>I1</var>, <var>I2</var>)</c>,
        ///// <c>compose(tpir, tir) == LeastUncertainTimePointIntervalRelation(<var>t</var>, <var>I2</var>)</c>.
        ///// </summary>
        //public static TimePointIntervalRelation Compose(TimePointIntervalRelation tpir, TimeIntervalRelation tir)
        //{
        //    //@MethodContract(
        //    //  pre  = {
        //    //    @Expression("_tpir != null"),
        //    //    @Expression("_ar != null")
        //    //  },
        //    //  post = {
        //    //    @Expression("for (TimePointIntervalRelation bTpir : BASIC_RELATIONS) {for (TimeIntervalRelation bAr: TimeIntervalRelation.BASIC_RELATIONS) {" +
        //    //                  "bTpir.implies(_tpir) && bAr.implies(_ar) ? result.impliedBy(BASIC_COMPOSITIONS[btPir.basicRelationOrdinal()][bAr.basicRelationOrdinal()])" +
        //    //                "}}")
        //    //})
        //    TimePointIntervalRelation acc = EMPTY;
        //    foreach (TimePointIntervalRelation bTpir in BASIC_RELATIONS)
        //    {
        //        if (tpir.ImpliedBy(bTpir))
        //        {
        //            foreach (TimeIntervalRelation bTir in TimeIntervalRelation.BASIC_RELATIONS)
        //            {
        //                if (tir.ImpliedBy(bTir))
        //                {
        //                    acc |= BASIC_COMPOSITIONS[bTpir.BasicRelationalOrdinal][bTir.BasicRelationalOrdinal];
        //                }
        //            }
        //        }
        //    }
        //    return acc;
        //}

        /// <summary>
        /// The relation of <paramref name="t"/> with <paramref name="i"/> with the lowest
        /// possible <see cref="Uncertainty"/>.
        /// </summary>
        /// <returns>
        /// <c>null</c> as <see cref="ITimeInterval.Begin"/> or <see cref="ITimeInterval.End"/>
        /// is considered as unknown, and thus is not used to restrict the relation more, 
        /// leaving it with more <see cref="Uncertainty"/>.
        /// </returns>
        public static TimePointIntervalRelation LeastUncertainTimePointIntervalRelation(DateTime? t, ITimeInterval i)
        {
            // MUDO contract

            if (t == null)
            {
                return Full;
            }
            DateTime? iBegin = i.Begin;
            DateTime? iEnd = i.End;
            TimePointIntervalRelation result = Full;
            if (iBegin != null)
            {
                if (t < iBegin)
                {
                    return Before;
                }
                else if (t == iBegin)
                {
                    return Begins;
                }
                else
                {
                    Contract.Assume(t > iBegin);
                    result -= Before;
                    result -= Begins;
                }
            }
            if (iEnd != null)
            {
                if (t < iEnd)
                {
                    result -= Ends;
                    result -= After;
                }
                else if (t == iEnd)
                {
                    return Ends;
                }
                else
                {
                    Contract.Assume(t > iEnd);
                    return After;
                }
            }
            return result;
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
                Contract.Ensures(Contract.Result<int>() < 5);

                /*
                 * This is the bit position, 0-based, in the 5-bit bit pattern, of the bit
                 * representing this as basic relation.
                 */
                return m_BitPattern.NumberOfTrailingZeros();
            }
        }

        /// <summary>
        /// This is a basic time point-interval relation.
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
        /// A measure about the uncertainty this time point-interval relation expresses.
        /// </summary>
        /// <return>
        /// <para>This is the fraction of the 5 basic relations that imply this general relation.</para>
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
                                     ? Contract.Result<float>() == (NrOfBasicRelations - 1) / 4.0F
                                     : true);
                Contract.Ensures(this == Empty ? float.IsNaN(Contract.Result<float>()) : true);

                int count = m_BitPattern.BitCount();
                if (count == 0)
                {
                    return float.NaN;
                }
                count--;
                float result = count / 4.0F;
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
        public static int CalcNrOfBasicRelations(TimePointIntervalRelation tpir)
        {
            Contract.Ensures(Contract.Result<int>() == BasicRelations.Count(br => br.Implies(tpir)));

            return BasicRelations.Count(br => br.Implies(tpir));
        }

        /// <summary>
        /// <para>The complement of a time point-interval relation is the logic negation of the condition
        /// the time point-interval relation expresses.</para>
        /// <para>The complement of a basic time point-interval relation is the disjunction of all the
        /// other basic time point-interval relations.</para>
        /// <para>The complement of a general time point-interval relation is the disjunction of all 
        /// basic time point-interval relations that are not implied by the general time point-interval
        /// relation.</para>
        /// </summary>
        /// <remarks>
        /// <para>This method is key to validating semantic constraints on time intervals, using the
        /// following idiom:</para>
        /// <code>
        ///   ...
        ///   DateTime t1 = ...;
        ///   DateTime t2 = ...;
        ///   TimePointIntervalRelation condition = ...;
        ///   TimePointIntervalRelation actual = timePointIntervalRelation(t1, t2);
        ///   if (! actual.Implies(condition)) {
        ///     throw new ....
        ///   }
        ///   ...
        /// </code>
        /// <para><strong>Be aware that the complement has in general not the same meaning as
        /// a logic negation.</strong> For a <em>basic relation</em> <var>br</var> and a 
        /// general time point-interval relation <var>cond</var>, it is true that</para>
        /// <code>
        /// <var>br</var>.Implies(<var>cond</var>) ==>! <var>br</var>.Implies(<var>cond</var>.Complement)
        /// </code>
        /// <para><strong>This is however not so for <em>non-basic, and thus general time 
        /// point-interval relations</em></strong>, as the following counterexample proofs.
        /// Suppose a condition is that, for a general relation <var>gr</var>:
        /// <c><var>gr</var>.Implies(<var>cond</var>)</c></para>
        /// <para>Suppose <c><var>gr</var> == (=[&lt; &gt;&lt;)</c>. Then we can rewrite in
        /// the following way:</para>
        /// <code>
        /// <var>gr</var>.Implies(<var>cond</var>)
        /// ==>(=[&lt; &gt;&lt;).Implies(<var>cond</var>)
        /// ==>(=[&lt; &gt;&lt;) SUBSETOREQUAL <var>cond</var>
        /// ==>(=[&lt; ISIN <var>cond</var>) &amp;&amp; (&gt;&lt; ISIN <var>cond</var>)
        /// </code>
        /// <para>From the definition of the complement, it follows that, for a basic relation
        /// <var>br</var> and a general relation <var>GR</var> as set</para>
        /// <code>
        /// br ISIN GR ==>br NOTIN GR.Complement
        /// </code>
        /// <para>Thus:</para>
        /// <code>
        /// ==>(=[&lt; NOTIN <var>cond</var>.Complement) &amp;&amp; (&gt;&lt; NOTIN <var>cond</var>.Complement)
        /// ==>! ((=[&lt; ISIN <var>cond</var>.Complement) || (&gt;&lt; ISIN <var>cond</var>.Complement) (1)
        /// </code>
        /// <para>While, from the other side:</para>
        /// <code>
        /// ! <var>gr</var>.implies(<var>cond</var>.Complement)
        /// ==>! (=[&lt; &gt;&lt;).Implies(<var>cond</var>.Complement)
        /// ==>! (=[&lt; &gt;&lt;) SUBSETOREQUAL (<var>cond</var>.Complement)
        /// ==>! ((=[&lt; ISIN <var>cond</var>.Complement) &amp;&amp; (&gt;&lt; ISIN <var>cond</var>.Complement) (2)
        /// </code>
        /// <para>It is clear that (1) is incompatible with (2), except for the case where the
        /// initial relation is basic.</para>
        /// <para>In the reverse case, for a basic relation <var>br</var> and a general time
        /// point-interval relation <var>actual</var>, nothing special can be said about the
        /// complement of <var>actual</var>, as the following reasoning illustrates:</para>
        /// <code>
        /// <var>actual</var>.Implies(<var>br</var>)
        /// ==><var>actual</var> SUBSETOREQUAL <var>br</var>
        /// ==><var>actual</var> SUBSETOREQUAL (<var>br</var>)
        /// ==><var>actual</var> == (<var>br</var>) || <var>actual</var> == EMPTYSET
        /// ==><var>actual</var>.Complement == (<var>br</var>).Complement || <var>actual</var>.Complement == FULL (3)
        /// </code>
        /// <para>From the other side:</para>
        /// <code>
        /// ! <var>actual</var>.Complement.Implies(<var>br</var>)
        /// ==>! (<var>actual</var>.Complement SUBSETOREQUAL <var>br</var>)
        /// ==>! (<var>actual</var>.Complement SUBSETOREQUAL (<var>br</var>))
        /// ==>! (<var>actual</var>.Complement == (<var>br</var>) || <var>actual</var>.Complement == EMPTYSET)
        /// ==><var>actual</var>.Complement != (<var>br</var>) &amp;&amp; <var>actual</var>.Complement != EMPTYSET (4)
        /// </code>
        /// <para>It is clear that (3) expresses something completely different then (4), and this 
        /// effect is obviously even stronger with non-basic relations.</para>
        /// <para>Note that it is exactly this counter-intuitivity that makes reasoning with time
        /// intervals so difficult.</para>
        /// <para>The operator version of the complement is <see cref="op_OnesComplement">~</see>.</para>
        /// </remarks>
        public TimePointIntervalRelation Complement
        {
            get
            {
                Contract.Ensures(AreComplementary(this, Contract.Result<TimePointIntervalRelation>()));

                /*
                 * implemented as the XOR of the FULL bit pattern with this bit pattern;
                 * this simply replaces 0 with 1 and 1 with 0.
                 */
                uint result = FullBitPattern ^ m_BitPattern;
                return Values[result];
            }
        }

        /// <summary>
        /// Operator version of the <see cref="Complement"/> of a <c>TimePointIntervalRelation</c>.
        /// <inheritdoc cref="Complement"/>
        /// </summary>
        /// <remarks>
        /// <inheritdoc cref="Complement"/>
        /// </remarks>
        [Pure]
        public static TimePointIntervalRelation operator ~(TimePointIntervalRelation tpir)
        {
            Contract.Ensures(Contract.Result<TimePointIntervalRelation>() == tpir.Complement);

            return tpir.Complement;
        }

        [Pure]
        public static bool AreComplementary(TimePointIntervalRelation tpir1, TimePointIntervalRelation tpir2)
        {
            Contract.Ensures(Contract.Result<bool>() ==
                             Contract.ForAll(
                                 BasicRelations,
                                 br => (tpir1.ImpliedBy(br) ? (!tpir2.ImpliedBy(br)) : tpir2.ImpliedBy(br))
                                       && (tpir2.ImpliedBy(br) ? (!tpir1.ImpliedBy(br)) : tpir1.ImpliedBy(br))));

            return BasicRelations.All(
                br => (tpir1.ImpliedBy(br) ? (!tpir2.ImpliedBy(br)) : tpir2.ImpliedBy(br))
                      && (tpir2.ImpliedBy(br) ? (!tpir1.ImpliedBy(br)) : tpir1.ImpliedBy(br)));
        }

        //public static IEnumerable<TimePointIntervalRelation> BasicRelationsOf(TimePointIntervalRelation tpir)
        //{
        //    Contract.Ensures(Contract.ForAll(
        //        Contract.Result<IEnumerable<TimePointIntervalRelation>>(),
        //        rTpir => rTpir.IsBasic && tpir.ImpliedBy(rTpir)));
        //    Contract.Ensures(Contract.ForAll(
        //        BASIC_RELATIONS,
        //        br => tpir.ImpliedBy(br)
        //            ? Contract.Result<IEnumerable<TimePointIntervalRelation>>().Contains(br)
        //            : true));

        //    return BASIC_RELATIONS.Where(br => tpir.ImpliedBy(br));
        //}

        /// <summary>
        /// <para>Is <c>this</c> implied by <paramref name="tpir"/>?</para>
        /// <para>In other words, when considering the relations as a set of basic relations, is
        /// <c>this</c> a superset of <paramref name="tpir"/>
        /// (considering equality as also acceptable)?</para>
        /// </summary>
        /// <param name="tpir"></param>
        /// <returns></returns>
        [Pure]
        public bool ImpliedBy(TimePointIntervalRelation tpir)
        {
            return (m_BitPattern & tpir.m_BitPattern) == tpir.m_BitPattern;
        }

        /// <summary>
        /// Does <c>this</c> imply <paramref name="tpir"/>? In other words, when considering
        /// the relations as a set of basic relations, is <c>this</c> a subset of
        /// <paramref name="tpir"/> (considering equality as also acceptable)?
        /// </summary>
        [Pure]
        public bool Implies(TimePointIntervalRelation tpir)
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
                bool first = true;
                for (int i = 0; i < s_BasicCodes.Length; i++)
                {
                    if (ImpliedBy(BasicRelations[i]))
                    {
                        if (! first)
                        {
                            result.Append(" ");
                        }
                        result.Append(s_BasicCodes[i]);
                        if (first)
                        {
                            first = false;
                        }
                    }
                }
            }
            result.Append(")");
            return result.ToString();
        }
    }
}