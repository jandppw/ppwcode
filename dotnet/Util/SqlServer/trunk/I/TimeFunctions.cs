/*
 * Copyright 2004 - $Date: 2008-11-15 23:58:07 +0100 (za, 15 nov 2008) $ by PeopleWare n.v..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#region Using

using System;
using System.Collections;
using System.Data.SqlTypes;

using Microsoft.SqlServer.Server;

#endregion

// ReSharper disable InconsistentNaming

namespace PPWCode.Util.SqlServer.I
{
    public class TimeFunctions
    {
        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_Create(SqlInt32 aYear, SqlInt32 aMonth, SqlInt32 aDay)
        {
            if (aDay.IsNull || aMonth.IsNull || aYear.IsNull)
            {
                return SqlDateTime.Null;
            }
            try
            {
                return new SqlDateTime(aYear.Value, aMonth.Value, aDay.Value);
            }
            catch
            {
                return SqlDateTime.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_FirstOfMonth(SqlInt32 aYear, SqlInt32 aMonth)
        {
            if (aMonth.IsNull || aYear.IsNull)
            {
                return SqlDateTime.Null;
            }
            try
            {
                return new SqlDateTime(aYear.Value, aMonth.Value, 1);
            }
            catch
            {
                return SqlDateTime.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_LastOfMonth(SqlInt32 aYear, SqlInt32 aMonth)
        {
            if (aMonth.IsNull || aYear.IsNull)
            {
                return SqlDateTime.Null;
            }
            try
            {
                return new SqlDateTime(aYear.Value, aMonth.Value, DateTime.DaysInMonth(aYear.Value, aMonth.Value));
            }
            catch
            {
                return SqlDateTime.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_FirstOfYear(SqlInt32 aYear)
        {
            if (aYear.IsNull)
            {
                return SqlDateTime.Null;
            }
            try
            {
                return new SqlDateTime(aYear.Value, 1, 1);
            }
            catch
            {
                return SqlDateTime.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_LastOfYear(SqlInt32 aYear)
        {
            if (aYear.IsNull)
            {
                return SqlDateTime.Null;
            }
            try
            {
                return new SqlDateTime(aYear.Value, 12, 31);
            }
            catch
            {
                return SqlDateTime.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_StripTime(SqlDateTime aDateTime)
        {
            if (aDateTime.IsNull)
            {
                return SqlDateTime.Null;
            }
            return new SqlDateTime(aDateTime.Value.Date);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_StripHour(SqlDateTime aDateTime)
        {
            if (aDateTime.IsNull)
            {
                return SqlDateTime.Null;
            }
            DateTime dt = aDateTime.Value;
            return new SqlDateTime(dt.Year, dt.Month, dt.Day, dt.Hour, 0, 0);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_StripMinute(SqlDateTime aDateTime)
        {
            if (aDateTime.IsNull)
            {
                return SqlDateTime.Null;
            }
            DateTime dt = aDateTime.Value;
            return new SqlDateTime(dt.Year, dt.Month, dt.Day, dt.Hour, dt.Minute, 0);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_Today()
        {
            return new SqlDateTime(DateTime.Today);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_Yesterday()
        {
            return new SqlDateTime(DateTime.Today.AddDays(-1));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_Tomorrow()
        {
            return new SqlDateTime(DateTime.Today.AddDays(1));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlInt32 fnDateTime_DaysInMonth(SqlInt32 aYear, SqlInt32 aMonth)
        {
            if (aMonth.IsNull || aYear.IsNull)
            {
                return SqlInt32.Null;
            }
            try
            {
                return new SqlInt32(DateTime.DaysInMonth(aYear.Value, aMonth.Value));
            }
            catch
            {
                return SqlInt32.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_MaxDate(SqlDateTime aDateTime1, SqlDateTime aDateTime2)
        {
            if (aDateTime1.IsNull || aDateTime2.IsNull)
            {
                return SqlDateTime.Null;
            }
            return new SqlDateTime(aDateTime1.Value < aDateTime2.Value ? aDateTime2.Value : aDateTime1.Value);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_MinDate(SqlDateTime aDateTime1, SqlDateTime aDateTime2)
        {
            if (aDateTime1.IsNull || aDateTime2.IsNull)
            {
                return SqlDateTime.Null;
            }
            return new SqlDateTime(aDateTime1.Value > aDateTime2.Value ? aDateTime2.Value : aDateTime1.Value);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_AddWorkingDays(SqlDateTime _aDateTime, SqlInt32 _aNrWorkingDays)
        {
            if (_aDateTime.IsNull || _aNrWorkingDays.IsNull || _aNrWorkingDays.Value <= 0)
            {
                return SqlDateTime.Null;
            }
            DateTime aDateTime = _aDateTime.Value;
            Int32 aNrWorkingDays = _aNrWorkingDays.Value;

            while (aNrWorkingDays > 0)
            {
                aDateTime = aDateTime.AddDays(1);
                while (aDateTime.DayOfWeek == DayOfWeek.Saturday || aDateTime.DayOfWeek == DayOfWeek.Sunday)
                {
                    aDateTime = aDateTime.AddDays(1);
                }
                aNrWorkingDays--;
            }
            return new SqlDateTime(aDateTime);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlInt32 fnDateTime_WorkingDaysBetween(SqlDateTime _aStartDate, SqlDateTime _aEndDate)
        {
            if (_aStartDate.IsNull || _aEndDate.IsNull)
            {
                return SqlInt32.Null;
            }
            DateTime StartDate = _aStartDate.Value.Date;
            DateTime EndDate = _aEndDate.Value.Date;
            if (StartDate == EndDate)
            {
                return new SqlInt32(0);
            }
            if (StartDate > EndDate)
            {
                return SqlInt32.Null;
            }
            Int32 Result = 0;
            StartDate = StartDate.AddDays(1);
            while (StartDate < EndDate)
            {
                while (StartDate.DayOfWeek == DayOfWeek.Saturday || StartDate.DayOfWeek == DayOfWeek.Sunday)
                {
                    StartDate = StartDate.AddDays(1);
                }
                if (StartDate < EndDate)
                {
                    StartDate = StartDate.AddDays(1);
                    Result++;
                }
            }
            return new SqlInt32(Result);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_NextWorkingDay(SqlDateTime aDate)
        {
            if (aDate.IsNull)
            {
                return SqlDateTime.Null;
            }
            DateTime d = aDate.Value;
            while (d.DayOfWeek == DayOfWeek.Saturday || d.DayOfWeek == DayOfWeek.Sunday)
            {
                d = d.AddDays(1);
            }
            return new SqlDateTime(d);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_PreviousWorkingDay(SqlDateTime aDate)
        {
            if (aDate.IsNull)
            {
                return SqlDateTime.Null;
            }
            DateTime d = aDate.Value;
            while (d.DayOfWeek == DayOfWeek.Saturday || d.DayOfWeek == DayOfWeek.Sunday)
            {
                d = d.AddDays(-1);
            }
            return new SqlDateTime(d);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlInt32 fnCalcAge(SqlDateTime aBirthDate, SqlDateTime aDate)
        {
            if (aBirthDate.IsNull)
            {
                return SqlInt32.Null;
            }
            DateTime BirthDate = aBirthDate.Value;
            DateTime Date = aDate.IsNull ? DateTime.Today : aDate.Value;
            if (BirthDate > Date)
            {
                return SqlInt32.Null;
            }
            int result = Date.Year - BirthDate.Year;
            if (Date.Month < BirthDate.Month || (Date.Month == BirthDate.Month && Date.Day < BirthDate.Day))
            {
                result--;
            }
            return result;
        }

        private static int dt2Quarter(DateTime dt)
        {
            return (dt.Year * 10) + (((dt.Month - 1) / 3) + 1);
        }

        private static DateTime Quarter2StartDate(int q)
        {
            int Year = q / 10;
            DateTime dt = new DateTime(Year, 1, 1);
            return dt.AddMonths(((q % 10) - 1) * 3);
        }

        private static DateTime Quarter2EndDate(int q)
        {
            int Year = q / 10;
            DateTime dt = new DateTime(Year, 1, 1);
            dt = dt.AddMonths((q % 10) * 3);
            return dt.AddDays(-1);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlInt32 fnQuarter_Quarter(SqlDateTime aQuarter)
        {
            if (aQuarter.IsNull)
            {
                return SqlInt32.Null;
            }
            return new SqlInt32(dt2Quarter(aQuarter.Value));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnDateTime_Quarter(SqlDateTime aQuarter)
        {
            if (aQuarter.IsNull)
            {
                return SqlDateTime.Null;
            }
            DateTime dt = new DateTime(aQuarter.Value.Year, (((aQuarter.Value.Month - 1) / 3) * 3) + 1, 1);
            return new SqlDateTime(dt);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnQuarter_StartDate(SqlInt32 aQuarter)
        {
            if (aQuarter.IsNull)
            {
                return SqlDateTime.Null;
            }
            return new SqlDateTime(Quarter2StartDate(aQuarter.Value));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnQuarter_EndDate(SqlInt32 aQuarter)
        {
            if (aQuarter.IsNull)
            {
                return SqlDateTime.Null;
            }
            return new SqlDateTime(Quarter2EndDate(aQuarter.Value));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnQuarter_EndDateOfYear(SqlInt32 aQuarter)
        {
            if (aQuarter.IsNull)
            {
                return SqlDateTime.Null;
            }
            return new SqlDateTime(new DateTime((aQuarter.Value / 10) + 1, 1, 1).AddDays(-1));
        }

        public static void rhQuarterSplitter(
            Object obj,
            out int Quarter,
            out DateTime StartDate,
            out DateTime EndDate)
        {
            int q = (int)obj;
            Quarter = q;
            StartDate = Quarter2StartDate(q);
            EndDate = Quarter2EndDate(q);
        }

        [SqlFunction(
            FillRowMethodName = "rhQuarterSplitter",
            IsDeterministic = true,
            IsPrecise = true,
            DataAccess = DataAccessKind.None,
            TableDefinition = "[Quarter] int, StartDate date, EndDate date")]
        public static IEnumerable fnQuarters_List(SqlDateTime aStartDate, SqlDateTime aEndDate)
        {
            if (aStartDate.IsNull)
            {
                yield break;
            }
            DateTime StartDate = aStartDate.Value;
            DateTime EndDate = aEndDate.IsNull ? DateTime.Today : aEndDate.Value;

            for (; StartDate <= EndDate;)
            {
                yield return dt2Quarter(StartDate);
                StartDate = StartDate.AddMonths(3);
            }
        }
    }
}

// ReSharper restore InconsistentNaming