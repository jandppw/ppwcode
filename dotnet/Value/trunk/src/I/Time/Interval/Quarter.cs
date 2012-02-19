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
    /// An actual quarter, according to the Gregorian calendar.
    /// </summary>
    public sealed class Quarter : AbstractTimeInterval
    {
        #region Type invariants

        [ContractInvariantMethod]
        private void TypeInvariants()
        {
            Contract.Invariant(QuarterNumber > 0);
            Contract.Invariant(QuarterNumber <= 4);
            Contract.Invariant(Begin == StartOfQuarter(Year, QuarterNumber));
            Contract.Invariant(End == StartOfNextQuarter(Year, QuarterNumber));
        }

        #endregion

        #region Construction

        /// <summary>
        /// Create a new year instance from an integer representing the year number.
        /// </summary>
        /// <param name="int">The year, as a number, in the Gregorian calendar.
        /// There are no limitations.</param>
        /// <param name="quarterNumber">The number of the quarter in <paramref name="year"/>.</param>
        public Quarter(int year, int quarterNumber)
        {
            Contract.Requires(quarterNumber > 0);
            Contract.Requires(quarterNumber <= 4);
            Contract.Ensures(Year == year);
            Contract.Ensures(QuarterNumber == quarterNumber);

            m_Year = year;
            m_QuarterNumber = quarterNumber;
            m_Begin = StartOfQuarter(year, quarterNumber);
            m_End = StartOfNextQuarter(year, quarterNumber);
        }

        /// <summary>
        /// Create a new quarter instance, representing the quarter in which a given
        /// <see cref="DateTime"/> falls.
        /// </summary>
        /// <param name="dt">This date falls in the resulting year instance</param>
        public Quarter(DateTime dt) : this(dt.Year, (dt.Month + 2) / 3)
        {
            Contract.Ensures(TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(dt, this).Equals(TimePointIntervalRelation.In));
            
            // NOP
        }

        #endregion

        #region Constants

        /// <summary>
        /// The mont in which each quater starts.
        /// The first quarter is <c>QUARTER_BEGIN_MONTH[0]</c>,
        /// the fourth quarter is <c>QUARTER_BEGIN_MONTH[3]</c>
        /// </summary>
        public static readonly int[] QUARTER_BEGIN_MONTH = { 1, 4, 7, 10 };

        /// <summary>
        /// The duration of each quarter, 1 through 4, independent of the year.
        /// For the first quarter, the non-leap-year value is returned.
        /// For leap years, the duration is one day more.
        /// The first quarter is <c>DURATION_OF_QUARTER[0]</c>,
        /// the fourth quarter is <c>DURATION_OF_QUARTER[3]</c>
        /// </summary>
        /// <remarks>
        /// <list type="table">
        ///  <listheader>
        ///    <term>Quarter: Months</term>
        ///    <description>Number of days</description>
        ///  </listheader>
        ///  <item>
        ///    <term>1st quarter (0): Januari, Februari, March</term>
        ///    <description>90 (91) days</description>
        ///  </item>
        ///  <item>
        ///    <term>1st quarter (0): April, May, June</term>
        ///    <description>91 days</description>
        ///  </item>
        ///  <item>
        ///    <term>1st quarter (2): Januari, Februari, March</term>
        ///    <description>92 days</description>
        ///  </item>
        ///  <item>
        ///    <term>4th quarter (3): October, November, December</term>
        ///    <description>92 days</description>
        ///  </item>
        /// </list>
        /// </remarks>
        public static readonly TimeSpan[] DURATION_OF_QUARTER =
        {
            TimeSpan.FromDays(31 + 28 + 31),
            TimeSpan.FromDays(30 + 31 + 30),
            TimeSpan.FromDays(31 + 31 + 30),
            TimeSpan.FromDays(31 + 30 + 31)
        };

        public static readonly TimeSpan ONE_DAY = TimeSpan.FromDays(1);

        /// <summary>
        /// One day more than <c><see cref="DURATION_OF_QUARTER"/>[0]</c>.
        /// </summary>
        public static readonly TimeSpan DURATION_OF_FIRST_QUARTER_IN_LEAP_YEAR = DURATION_OF_QUARTER[0] + ONE_DAY;

        public static TimeIntervalRelation ENCLOSES
            = TimeIntervalRelation.Or(
                TimeIntervalRelation.StartedBy,
                TimeIntervalRelation.Contains,
                TimeIntervalRelation.FinishedBy);

        #endregion

        #region Class methods

        public static DateTime StartOfQuarter(int year, int quarterNumber)
        {
            Contract.Requires(quarterNumber > 0);
            Contract.Requires(quarterNumber <= 4);
            Contract.Ensures(Contract.Result<DateTime>() == new DateTime(year, QUARTER_BEGIN_MONTH[quarterNumber - 1], 1));

            return new DateTime(year, QUARTER_BEGIN_MONTH[quarterNumber - 1], 1);
        }

        public static DateTime StartOfNextQuarter(int year, int quarterNumber)
        {
            Contract.Requires(quarterNumber > 0);
            Contract.Requires(quarterNumber <= 4);
            Contract.Ensures(Contract.Result<DateTime>() == StartOfQuarter(year, quarterNumber).AddMonths(3));

            if (quarterNumber == 4)
            {
                return new DateTime(year + 1, 1, 1);
            }
            else
            {
                return new DateTime(year, QUARTER_BEGIN_MONTH[quarterNumber], 1);                
            }
        }

        #endregion

        #region Properties

        // we store redundant data; it makes little sense to recalculate this stuff a lot with little space loss

        private readonly int m_Year;

        public int Year
        {
            get { return m_Year; }
        }

        private readonly int m_QuarterNumber;

        public int QuarterNumber
        {
            get { return m_QuarterNumber; }
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
            get {
                return (m_QuarterNumber == 1 && DateTime.IsLeapYear(m_Year)
                            ? DURATION_OF_FIRST_QUARTER_IN_LEAP_YEAR
                            : DURATION_OF_QUARTER[m_QuarterNumber - 1]);
            }
        }

        public override ITimeInterval Determinate(DateTime? stubBegin, DateTime? stubEnd)
        {
            Contract.Ensures((Year) Contract.Result<ITimeInterval>() == this);

            return this;
        }

        public Year EnclosingYear
        {
            get
            {
                Contract.Ensures(TimeIntervalRelation.LeastUncertainTimeIntervalRelation(Contract.Result<Year>(), this)
                    .Implies(ENCLOSES));

                return new Year(m_Year);
            }
        }

        #endregion

        public override string ToString()
        {
            return "[" + m_Year + "Q" + m_QuarterNumber  + "[\u0394(quarter)"; // \u0394 is Greek capital delta

        }
    }
}