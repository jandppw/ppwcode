using System;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using Microsoft.SqlServer.Server;
using System.Collections;
using System.Collections.Generic;

public partial class UserDefinedFunctions
{

    [SqlFunction(FillRowMethodName = "rhSplitKeys",
     DataAccess = DataAccessKind.None,
     TableDefinition = "F_KEY int")]
    public static IEnumerable fnSplitKeys(SqlString inpStr)
    {
        string locStr = inpStr.IsNull ? string.Empty : inpStr.Value;
        string[] splitStr = locStr.Split(new char[1] { ',' }, StringSplitOptions.RemoveEmptyEntries);
        for (int i = 0; i < splitStr.Length; i++)
        {
            yield return Convert.ToInt32(splitStr[i]);
        }
    }
    public static void rhSplitKeys(
      Object obj, out int F_KEY)
    {
        F_KEY = (int)obj;
    }


    [SqlFunction(FillRowMethodName = "rhSplitStrings",
    DataAccess = DataAccessKind.None,
    TableDefinition = "F_KEY nvarchar(128)")]
    public static IEnumerable fnSplitStrings(SqlString inpStr)
    {
        string locStr = inpStr.IsNull ? string.Empty : inpStr.Value;
        string[] splitStr = locStr.Split(new char[1] { ',' }, StringSplitOptions.RemoveEmptyEntries);
        for (int i = 0; i < splitStr.Length; i++)
        {
            yield return splitStr[i];
        }
    }
    public static void rhSplitStrings(
      Object obj, out string F_KEY)
    {
        F_KEY = (string)obj;
    }
};

