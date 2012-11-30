using System;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using Microsoft.SqlServer.Server;

public partial class UserDefinedFunctions {
    [Microsoft.SqlServer.Server.SqlFunction]
    public static SqlInt32 fnMaxInt32(SqlInt32 aNum1, SqlInt32 aNum2)
    {
        if (aNum1.IsNull || aNum2.IsNull)
        {
            return SqlInt32.Null;
        }

        return new SqlInt32(Math.Max(aNum1.Value, aNum2.Value));
    }
};

