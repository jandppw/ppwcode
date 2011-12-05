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
            // VALUES
            Contract.Invariant(ClassInitialized ? VALUES != null : true);
            Contract.Invariant(ClassInitialized ? VALUES.Length == NR_OF_RELATIONS : true);
            Contract.Invariant(
                ClassInitialized
                    ? Contract.ForAll(
                        0,
                        NR_OF_RELATIONS - 1,
                        i => Contract.ForAll(i + 1, NR_OF_RELATIONS - 1, j => VALUES[i] != VALUES[j]))
                    : true,
                "No duplicate entries.");
            Contract.Invariant(
                ClassInitialized ? VALUES.Contains(this) : true,
                "Any instance there ever can be is mentioned in VALUES. There are no other instances.");

            // Basic relations
            Contract.Invariant(ClassInitialized ? BASIC_RELATIONS != null : true);
            // MUDO Contract.Invariant(ClassInitialized ? Contract.ForAll(BASIC_RELATIONS, basic => ! EMPTY.impliedBy(basic)) : true);

            // MUDO Contract.Invariant(ClassInitialized ? FULL == or(BEFORE, BEGINS, IN, ENDS, AFTER) : true);
            // MUDO Contract.Invariant(ClassInitialized ? Contract.ForAll(BASIC_RELATIONS, br => BASIC_RELATIONS[br.BasicRelationOrdinal] == br) : true);
            Contract.Invariant(ClassInitialized ? BASIC_RELATIONS[0] == BEFORE : true);
            Contract.Invariant(ClassInitialized ? BASIC_RELATIONS[1] == BEGINS : true);
            Contract.Invariant(ClassInitialized ? BASIC_RELATIONS[2] == IN : true);
            Contract.Invariant(ClassInitialized ? BASIC_RELATIONS[3] == ENDS : true);
            Contract.Invariant(ClassInitialized ? BASIC_RELATIONS[4] == AFTER : true);

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
            Contract.Ensures(Contract.ForAll(BASIC_RELATIONS, br => br != tpir ? (!tpir.ImpliedBy(br)) : true));

            return BASIC_RELATIONS.All(br => br != tpir ? (!tpir.ImpliedBy(br)) : true);
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
                                 VALUES,
                                 v => tpir.ImpliedBy(v) ==
                                      Contract.ForAll(
                                          BASIC_RELATIONS,
                                          br => v.ImpliedBy(br) ? tpir.ImpliedBy(br) : true)));

            return VALUES
                .Where(tpir.ImpliedBy)
                .All(v => BASIC_RELATIONS.Where(v.ImpliedBy).All(tpir.ImpliedBy));
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
        public const int NR_OF_RELATIONS = 32;

        /// <summary>
        /// All possible time point-interval relations.
        /// </summary>
        public static readonly TimePointIntervalRelation[] VALUES;

        static TimePointIntervalRelation()
        {
            ClassInitialized = false;
            VALUES = new TimePointIntervalRelation[NR_OF_RELATIONS];
            for (uint i = 0; i < NR_OF_RELATIONS; i++)
            {
                VALUES[i] = new TimePointIntervalRelation(i);
            }
            EMPTY = VALUES[EMPTY_BIT_PATTERN];
            BEFORE = VALUES[BEFORE_BIT_PATTERN];
            BEGINS = VALUES[BEGINS_BIT_PATTERN];
            IN = VALUES[IN_BIT_PATTERN];
            ENDS = VALUES[ENDS_BY_BIT_PATTERN];
            AFTER = VALUES[AFTER_BIT_PATTERN];
            FULL = VALUES[FULL_BIT_PATTERN];
            BASIC_RELATIONS = new TimePointIntervalRelation[]
            {
                BEFORE, BEGINS, IN, ENDS, AFTER
            };
            ClassInitialized = true;
        }

        /// <summary>
        /// Ignore. Only here as a technical means to make static type invariants work.
        /// </summary>
        public static readonly bool ClassInitialized;

        #endregion

        #region Basic relations

        private const int EMPTY_BIT_PATTERN = 0; // 00000
        private const int BEFORE_BIT_PATTERN = 1; // 00001 <
        private const int BEGINS_BIT_PATTERN = 2; // 00010 =[<
        private const int IN_BIT_PATTERN = 4; // 00100 ><
        private const int ENDS_BY_BIT_PATTERN = 8; // 01000 =[>
        private const int AFTER_BIT_PATTERN = 16; // 10000 >
        private const int FULL_BIT_PATTERN = 31; // 11111 < =[< >< =[> >

        /// <summary>
        /// This empty relation is not a true time point-interval relation. It does not express a
        /// relational condition between intervals. Yet, it is needed for
        /// consistencey with some operations on time point-interval relations.
        /// </summary>
        public static readonly TimePointIntervalRelation EMPTY;

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
        public static readonly TimePointIntervalRelation BEFORE;

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
        public static readonly TimePointIntervalRelation BEGINS;

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
        public static readonly TimePointIntervalRelation IN;

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
        public static readonly TimePointIntervalRelation ENDS;

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
        public static readonly TimePointIntervalRelation AFTER;

        /// <summary>
        /// The full time point-interval relation, which expresses that nothing definite can be
        /// said about the relationship between a time point and a time interval.
        /// </summary>
        public static readonly TimePointIntervalRelation FULL;

        /// <summary>
        /// The set of all 5 basic time point-interval relations. That they are presented here in
        /// a particular order, is a pleasant side note, but in general not relevant
        /// for the user.
        /// </summary>
        public static readonly TimePointIntervalRelation[] BASIC_RELATIONS;

        private static readonly string[] BASIC_CODES =
            {
                "<", "=[<", "><", "=[>", ">"
            };

        #endregion

        #region Construction

        /// <summary>
        /// There is only 1 private constructor, that constructs the wrapper object
        /// around the bitpattern. This is used exclusively in <see cref="VALUES"/> 
        /// initialization code, in the <see cref="TimePointIntervalRelation">static
        /// constructor</see>.
        /// </summary>
        /// <param name="bitPattern"></param>
        private TimePointIntervalRelation(uint bitPattern)
        {
            Contract.Requires(bitPattern >= EMPTY_BIT_PATTERN);
            Contract.Requires(bitPattern <= FULL_BIT_PATTERN);
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

        #region Instance operations

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
                return NumberOfTrailingZeros(m_BitPattern);
            }
        }

        /// <summary>
        /// Based on Java, Integer.numberOfTrailingZeros.
        /// </summary>
        /// <remarks>
        /// (Based on Java documentation):
        /// Returns the number of zero bits following the lowest-order ("rightmost")
        /// one-bit in the representation of the specified
        /// <c>uint</c> value.  Returns 32 if the specified value has no
        /// one-bits in its representation, in other words if it is
        /// equal to zero.
        /// </remarks>
        private static int NumberOfTrailingZeros(uint i)
        {
            uint y;
            if (i == 0)
            {
                return 32;
            }
            uint n = 31;
            y = i << 16;
            if (y != 0)
            {
                n = n - 16;
                i = y;
            }
            y = i << 8;
            if (y != 0)
            {
                n = n - 8;
                i = y;
            }
            y = i << 4;
            if (y != 0)
            {
                n = n - 4;
                i = y;
            }
            y = i << 2;
            if (y != 0)
            {
                n = n - 2;
                i = y;
            }
            return (int)(n - ((i << 1) >> 31));
        }

        /// <summary>
        /// This is a basic time point-interval relation.
        /// </summary>
        public bool IsBasic
        {
            get
            {
                Contract.Ensures(Contract.Result<bool>() == BASIC_RELATIONS.Contains(this));

                return IsBasicBitPattern(m_BitPattern);
            }
        }

        /// <summary>
        /// A basic relation is expressed by a single bit in the bit pattern.
        /// </summary>
        private static bool IsBasicBitPattern(uint bitPattern)
        {
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
                result.Append(BASIC_CODES[BasicRelationalOrdinal]);
            }
            else
            {
                bool first = true;
                for (int i = 0; i < BASIC_CODES.Length; i++)
                {
                    if (ImpliedBy(BASIC_RELATIONS[i]))
                    {
                        if (! first)
                        {
                            result.Append(" ");
                        }
                        result.Append(BASIC_CODES[i]);
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