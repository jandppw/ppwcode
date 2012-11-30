using System.Data.SqlClient;
using System.Data.SqlTypes;
using Microsoft.SqlServer.Server;
using System.Text.RegularExpressions;

public partial class UserDefinedFunctions
{

    [Microsoft.SqlServer.Server.SqlFunction(DataAccess = DataAccessKind.Read, IsDeterministic = true)]
    public static SqlString fnGetErrorMessage(SqlString aErrorMessage, SqlInt32 aLCID, SqlInt32 aDefaultLCID)
    {
        if (aErrorMessage.IsNull)
        {
            return SqlString.Null;
        }
        string errmsg = aErrorMessage.Value;
        string[] criterias = {
            "'(?<pattern>(CK_.+?))'",
            "'(?<pattern>(FK_.+?))'",
            "'(?<pattern>(PK_.+?))'",
            "'(?<pattern>(UQ_.+?))'",

            "\"(?<pattern>(CK_.+?))\"",
            "\"(?<pattern>(FK_.+?))\"",
            "\"(?<pattern>(PK_.+?))\"",
            "\"(?<pattern>(UQ_.+?))\"" 
        };

        Match m = null;
        for (int i = 0; i < criterias.Length; i++)
        {
            m = Regex.Match(errmsg, criterias[i]);
            if (m.Success)
                break;
        }

        if (m.Success)
        {
            string ConstraintName = m.Groups["pattern"].Value;
            SqlString msg = null;
            using (SqlConnection conn = new SqlConnection("context connection=true"))
            {
                SqlCommand command = new SqlCommand(
                               " select dbo.fnXMLGetMessageValue(cm.Description, @lcid, @default_lcid)" +
                               "   from dbo.ConstraintMessage cm" +
                               "  where cm.ConstraintName = @ConstraintName",
                               conn);
                command.Parameters.Add(new SqlParameter("@lcid", aLCID.IsNull ? 1033 : aLCID.Value));
                command.Parameters.Add(new SqlParameter("@default_lcid", aDefaultLCID.IsNull ? 1033 : aDefaultLCID.Value));
                command.Parameters.Add(new SqlParameter("@ConstraintName", ConstraintName));
                conn.Open();
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        msg = reader.GetSqlString(0);
                    }
                }
                conn.Close();
            }
            return (msg.IsNull) ? aErrorMessage : msg;
        }
        else
        {
            return aErrorMessage;
        }

    }

    [Microsoft.SqlServer.Server.SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    public static SqlString fnGetConstraint(SqlString aErrorMessage)
    {
        if (aErrorMessage.IsNull)
        {
            return SqlString.Null;
        }
        string errmsg = aErrorMessage.Value;
        string[] criterias = {
            "'(?<pattern>(CK_.+?))'",
            "'(?<pattern>(FK_.+?))'",
            "'(?<pattern>(PK_.+?))'",
            "'(?<pattern>(UQ_.+?))'",

            "\"(?<pattern>(CK_.+?))\"",
            "\"(?<pattern>(FK_.+?))\"",
            "\"(?<pattern>(PK_.+?))\"",
            "\"(?<pattern>(UQ_.+?))\"" 
        };

        Match m = null;
        for (int i = 0; i < criterias.Length; i++)
        {
            m = Regex.Match(errmsg, criterias[i]);
            if (m.Success)
                break;
        }

        if (m.Success)
        {
            return new SqlString(m.Groups["pattern"].Value);
        }
        else
        {
            return SqlString.Null;
        }
    }
};

