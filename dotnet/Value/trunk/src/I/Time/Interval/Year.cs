/*<license>
Copyright 2004 - $Date: 2008-12-07 22:15:22 +0100 (Sun, 07 Dec 2008) $ by PeopleWare n.v..

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
    /// <summary>
    /// An actual year, according to the Gregorian calendar.
    /// </summary>
    public sealed class Year : AbstractTimeInterval
    {
        #region Type invariants

        [ContractInvariantMethod]
        private void TypeInvariants()
        {
            Contract.Invariant(Begin == new DateTime(Int, 1, 1));
            Contract.Invariant(End == new DateTime(Int + 1, 1, 1));
            Contract.Invariant(Duration == (IsLeapYear ? DURATION_LEAP_YEAR : DURATION_NON_LEAP_YEAR));
        }

        #endregion

        #region Construction

        /// <summary>
        /// Create a new year instance from an integer representing the year number.
        /// </summary>
        /// <param name="int">The year, as a number, in the Gregorian calendar.
        /// There are no limitations.</param>
        public Year(int @int)
        {
            Contract.Ensures(Int == @int);

            m_Int = @int;
            m_Begin = new DateTime(@int, 1, 1);
            m_End = new DateTime(@int + 1, 1, 1);
        }

        /// <summary>
        /// Create a new year instance, representing the year in which a given
        /// <see cref="DateTime"/> falls.
        /// </summary>
        /// <param name="dt">This date falls in the resulting year instance</param>
        public Year(DateTime dt) : this(dt.Year)
        {
            Contract.Ensures(TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(dt, this).Equals(TimePointIntervalRelation.In));
            
            // NOP
        }

        #endregion

        #region Constants

        public static readonly TimeSpan DURATION_NON_LEAP_YEAR = new TimeSpan(365, 0, 0, 0);
        public static readonly TimeSpan DURATION_LEAP_YEAR = new TimeSpan(366, 0, 0, 0);

        #endregion

        #region Properties

        private readonly int m_Int;

        public int Int
        {
            get { return m_Int; }
        }

        public bool IsLeapYear
        {
            get
            {
                Contract.Ensures(Contract.Result<bool>() == DateTime.IsLeapYear(Int));

                return DateTime.IsLeapYear(m_Int);
            }
        }

        private readonly DateTime m_Begin;

        public override DateTime? Begin
        {
            get { return m_Begin; }
        }

        private readonly DateTime m_End;

        public override DateTime? End
        {
            get { return m_End; }
        }

        public override TimeSpan? Duration
        {
            get { return IsLeapYear ? DURATION_LEAP_YEAR : DURATION_NON_LEAP_YEAR; }
        }

        public override ITimeInterval Determinate(DateTime? stubBegin, DateTime? stubEnd)
        {
            Contract.Ensures((Year) Contract.Result<ITimeInterval>() == this);

            return this;
        }

        #endregion
    }
}