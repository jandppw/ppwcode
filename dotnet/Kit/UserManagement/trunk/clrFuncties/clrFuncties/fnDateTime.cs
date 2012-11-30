using System;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using Microsoft.SqlServer.Server;
using System.Globalization;
using System.Threading;
using System.Collections;
using System.Collections.Generic;

public partial class UserDefinedFunctions
{
    /// <summary>
    /// Formateren van een datum
    /// </summary>
    /// <param name="Value"></param>
    /// <param name="Format"></param>
    /// <returns></returns>
    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    [return: SqlFacet(MaxSize = 64)]
    public static SqlString fnDateTime_Format(SqlDateTime dt, [SqlFacet(MaxSize = 64)] SqlString format, [SqlFacet(MaxSize = 20)] SqlString cultureName)
    {
        CultureInfo ci;
        int lcid;

        if (dt.IsNull)
        {
            return SqlString.Null;
        }
        if (cultureName.IsNull)
        {
            ci = new CultureInfo(Thread.CurrentThread.CurrentCulture.Name);
        }
        else
        {
            if (int.TryParse(cultureName.Value, out lcid) == false)
            {
                ci = new CultureInfo(cultureName.Value);
            }
            else
            {
                ci = new CultureInfo(lcid);
            }
        }
        if (format.IsNull)
        {
            return new SqlString(dt.Value.ToString(ci));
        }
        else
        {
            return new SqlString(dt.Value.ToString(format.Value, ci));
        }
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_Create(SqlInt32 year, SqlInt32 month, SqlInt32 day)
    {
        if (day.IsNull || month.IsNull || year.IsNull)
        {
            return SqlDateTime.Null;
        }
        try
        {
            return new SqlDateTime(year.Value, month.Value, day.Value);
        }
        catch
        {
            return SqlDateTime.Null;
        }
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_Merge(SqlDateTime dt, SqlInt32 time)
    {
        if (dt.IsNull || time.IsNull)
        {
            return SqlDateTime.Null;
        }
        try
        {
            return new SqlDateTime(dt.Value.Date.Add(new TimeSpan(time.Value / 100, time.Value % 100, 0)));
        }
        catch (Exception)
        {
            return SqlDateTime.Null;
        }
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_FirstOfMonth(SqlInt32 year, SqlInt32 month)
    {
        return UserDefinedFunctions.fnDateTime_Create(year.Value, month.Value, 1);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_LastOfMonth(SqlInt32 year, SqlInt32 month)
    {
        return UserDefinedFunctions.fnDateTime_Create(year.Value, month.Value, DateTime.DaysInMonth(year.Value, month.Value));
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_FirstOfYear(SqlInt32 year)
    {
        return UserDefinedFunctions.fnDateTime_Create(year.Value, 1, 1);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_LastOfYear(SqlInt32 year)
    {
        return UserDefinedFunctions.fnDateTime_Create(year.Value, 12, 31);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_StripTime(SqlDateTime dt)
    {
        return dt.IsNull ? SqlDateTime.Null : new SqlDateTime(dt.Value.Date);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_StripHour(SqlDateTime dt)
    {
        return dt.IsNull ? SqlDateTime.Null : new SqlDateTime(dt.Value.Year, dt.Value.Month, dt.Value.Day, dt.Value.Hour, 0, 0);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_StripMinute(SqlDateTime dt)
    {
        return dt.IsNull ? SqlDateTime.Null : new SqlDateTime(dt.Value.Year, dt.Value.Month, dt.Value.Day, dt.Value.Hour, dt.Value.Minute, 0);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_Today()
    {
        return new SqlDateTime(DateTime.Today);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_Yesterday()
    {
        return new SqlDateTime(DateTime.Today.AddDays(-1));
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_Tomorrow()
    {
        return new SqlDateTime(DateTime.Today.AddDays(1));
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlInt32 fnDateTime_DaysInMonth(SqlInt32 year, SqlInt32 month)
    {
        if (month.IsNull || year.IsNull)
        {
            return SqlInt32.Null;
        }
        try
        {
            return new SqlInt32(DateTime.DaysInMonth(year.Value, month.Value));
        }
        catch
        {
            return SqlInt32.Null;
        }
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_MaxDate(SqlDateTime dt1, SqlDateTime dt2)
    {
        if (dt1.IsNull || dt2.IsNull)
        {
            return SqlDateTime.Null;
        }
        return new SqlDateTime(dt1.Value < dt2.Value ? dt2.Value : dt1.Value);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_MinDate(SqlDateTime dt1, SqlDateTime dt2)
    {
        if (dt1.IsNull || dt2.IsNull)
        {
            return SqlDateTime.Null;
        }
        return new SqlDateTime(dt1.Value > dt2.Value ? dt2.Value : dt1.Value);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlDateTime fnDateTime_AddWorkingDays(SqlDateTime dt, SqlInt32 nrWorkingDays)
    {
        if (dt.IsNull || nrWorkingDays.IsNull || nrWorkingDays.Value <= 0)
        {
            return SqlDateTime.Null;
        }
        DateTime aDateTime = dt.Value;
        Int32 NrWorkingDays = nrWorkingDays.Value;

        while (NrWorkingDays > 0)
        {
            aDateTime = aDateTime.AddDays(1);
            while (aDateTime.DayOfWeek == DayOfWeek.Saturday || aDateTime.DayOfWeek == DayOfWeek.Sunday)
            {
                aDateTime = aDateTime.AddDays(1);
            }
            NrWorkingDays--;
        }
        return new SqlDateTime(aDateTime);
    }

    [Microsoft.SqlServer.Server.SqlFunction(
        DataAccess = DataAccessKind.None,
        IsDeterministic = true)]
    public static SqlInt32 fnDateTime_WorkingDaysBetween(SqlDateTime startDate, SqlDateTime endDate)
    {
        if (startDate.IsNull || endDate.IsNull)
        {
            return SqlInt32.Null;
        }
        DateTime StartDate = startDate.Value.Date;
        DateTime EndDate = endDate.Value.Date;
        if (StartDate == EndDate)
        {
            return new SqlInt32(0);
        }
        else if (StartDate > EndDate)
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
}