using System;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using Microsoft.SqlServer.Server;
using System.Text;
using System.Threading;
using System.Globalization;

public partial class UserDefinedFunctions
{

    [Microsoft.SqlServer.Server.SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    [return: SqlFacet(MaxSize = 64)]
    public static SqlString fnFormatFloatEx(SqlDouble aFloat, [SqlFacet(MaxSize = 64)] SqlString Format, [SqlFacet(MaxSize = 10)] SqlString CultureName)
    {
        CultureInfo ci;
        int lcid;

        if (aFloat.IsNull)
            return SqlString.Null;
        if (CultureName.IsNull)
        {
            ci = new CultureInfo(Thread.CurrentThread.CurrentCulture.Name);
        }
        else
        {
            if (int.TryParse(CultureName.Value, out lcid) == false)
            {
                ci = new CultureInfo(CultureName.Value);
            }
            else
            {
                ci = new CultureInfo(lcid);
            }
        }
        if (Format.IsNull)
        {
            return new SqlString(aFloat.Value.ToString(ci));
        }
        else
        {
            return new SqlString(aFloat.Value.ToString(Format.Value, ci));
        }
    }

    [Microsoft.SqlServer.Server.SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    [return: SqlFacet(MaxSize = 10)]
    public static SqlString fnGetCultureName4LCID(SqlInt32 LCID)
    {
        if (LCID.IsNull)
        {
            return SqlString.Null;
        }

        string result;
        try
        {
            CultureInfo ci = new CultureInfo(LCID.Value);
            result = ci.Name;
        }
        catch
        {
            result = string.Empty;
        }
        return (result == string.Empty ? SqlString.Null : new SqlString(result));
    }

    private static string GetTimePeriod(int Time, CultureInfo ci)
    {
        return string.Format("{0:00}{1}{2:00}", Time / 100, ci.DateTimeFormat.TimeSeparator, Time % 100);
    }

    [Microsoft.SqlServer.Server.SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    [return: SqlFacet(MaxSize = 32)]
    public static SqlString fnFormatTimeInterval(SqlInt32 BeginTime, SqlInt32 EndTime, SqlInt32 LCID)
    {
        if (BeginTime.IsNull || EndTime.IsNull || LCID.IsNull)
        {
            return SqlString.Null;
        }

        CultureInfo ci = null;
        try
        {
            ci = new CultureInfo(LCID.Value);
        }
        catch
        {
            ci = Thread.CurrentThread.CurrentCulture;
        }
        string result = string.Format("{0} - {1}", GetTimePeriod(BeginTime.Value, ci), GetTimePeriod(EndTime.Value, ci));
        return new SqlString(result);
    }

};

