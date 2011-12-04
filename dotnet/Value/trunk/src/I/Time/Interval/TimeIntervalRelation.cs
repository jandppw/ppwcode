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

#endregion

namespace PPWCode.Value.I.Time.Interval
{
    public sealed class TimeIntervalRelation
    {
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

        // ReSharper disable InconsistentNaming
        // with these bit patterns, converse is reverse of 13-bit pattern
        private const int EMPTY_BIT_PATTERN = 0; // 0000000000000
        private const int PRECEDES_BIT_PATTERN = 1; // 0000000000001 p
        private const int MEETS_BIT_PATTERN = 2; // 0000000000010 m
        private const int OVERLAPS_BIT_PATTERN = 4; // 0000000000100 o
        private const int FINISHED_BY_BIT_PATTERN = 8; // 0000000001000 F
        private const int CONTAINS_BIT_PATTERN = 16; // 0000000010000 D
        private const int STARTS_BIT_PATTERN = 32; // 0000000100000 s
        private const int EQUALS_BIT_PATTERN = 64; // 0000001000000 e
        private const int STARTED_BY_BIT_PATTERN = 128; // 0000010000000 S
        private const int DURING_BIT_PATTERN = 256; // 0000100000000 d
        private const int FINISHES_BIT_PATTERN = 512; // 0001000000000 f
        private const int OVERLAPPED_BY_BIT_PATTERN = 1024; // 0010000000000 O
        private const int MET_BY_BIT_PATTERN = 2048; // 0100000000000 M
        private const int PRECEDED_BY_BIT_PATTERN = 4096; // 1000000000000 P
        private const int FULL_BIT_PATTERN = 8191; // 1111111111111 pmoFDseSdfOMP

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
        public static readonly TimeIntervalRelation EQUALS = /* MUDO VALUES[EQUALS_BIT_PATTERN] */ new TimeIntervalRelation(EQUALS_BIT_PATTERN);

        // ReSharper restore InconsistentNaming

        #endregion

        #region Construction

        /// <summary>
        /// There is only 1 private constructor, that constructs the wrapper object
        /// around the bitpattern. This is used exclusively in <see cref="VALUES"/> 
        /// initialization code.
        /// </summary>
        private TimeIntervalRelation(int bitPattern)
        {
            Contract.Requires(bitPattern >= EMPTY_BIT_PATTERN);
            Contract.Requires(bitPattern <= FULL_BIT_PATTERN);
            Contract.Ensures(m_BitPattern == bitPattern);

            m_BitPattern = bitPattern;
        }

        #endregion

        #region Internal representation

        /// <summary>
        /// Only the 13 lowest bits are used. The other (32 - 13 = 19 bits) are 0.
        /// </summary>
        /// <remarks>
        /// Representation invariant (private):
        /// <code>
        /// Contract.Invariant(bitPattern &gt;= EMPTY_BIT_PATTERN);
        /// Contract.Invariant(bitPattern &lt;= FULL_BIT_PATTERN);
        /// </code>
        /// </remarks>
        private readonly int m_BitPattern;

        #endregion

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
        public static TimeIntervalRelation MostCertainTimeIntervalRelation(ITimeInterval i1, ITimeInterval i2)
        {
            // MUDO contract
            throw new NotImplementedException();
        }
    }
}