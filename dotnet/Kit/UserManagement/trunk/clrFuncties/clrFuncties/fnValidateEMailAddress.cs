using System;
using System.Data.SqlTypes;
using System.Text;
using System.Text.RegularExpressions;

using Microsoft.SqlServer.Server;

public partial class UserDefinedFunctions
{
    private static readonly Regex EmailRegex = new Regex(
        @"^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]" +
        @"{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))" +
        @"([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$");

    [SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    public static SqlBoolean fnValidateEmailAddress([SqlFacet(MaxSize = 320)] SqlString emailAddress)
    {
        if (emailAddress.IsNull)
        {
            return SqlBoolean.False;
        }

        return EmailRegex.IsMatch(emailAddress.Value);
    }

    [SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    public static SqlBoolean fnValidateEmailAddresses(SqlString emailAddresses)
    {
        if (emailAddresses.IsNull)
        {
            return SqlBoolean.False;
        }
        string[] splitStr = emailAddresses.Value.Split(new[] { ',', ';' }, StringSplitOptions.RemoveEmptyEntries);
        foreach (string s in splitStr)
        {
            if (!EmailRegex.IsMatch(s.Trim()))
            {
                return SqlBoolean.False;
            }
        }
        return SqlBoolean.True;
    }

    [SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    public static SqlString fnFormatEmailAddresses(SqlString emailAddresses)
    {
        if (!fnValidateEmailAddresses(emailAddresses))
        {
            return SqlString.Null;
        }
        string[] splitStr = emailAddresses.Value.Split(new[] { ',', ';' }, StringSplitOptions.RemoveEmptyEntries);
        StringBuilder sb = new StringBuilder(512);
        bool firstItem = true;
        foreach (string s in splitStr)
        {
            if (firstItem)
            {
                firstItem = false;
            }
            else
            {
                sb.Append(',');
            }
            sb.Append(s.Trim());
        }
        return new SqlString(sb.ToString());
    }
};