#region Using

using System.Data.SqlTypes;
using System.Xml;

using Microsoft.SqlServer.Server;

#endregion

public partial class UserDefinedFunctions
{
    [SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    public static SqlString fnXMLGetMessageValue(SqlXml doc, SqlInt32 lcid, SqlInt32 defaultLcid)
    {
        SqlString defaultValue = SqlString.Null;

        if (doc.IsNull)
        {
            return SqlString.Null;
        }

        using (XmlReader xr = doc.CreateReader())
        {
            while (xr.ReadToFollowing("message"))
            {
                int localLcid;
                if (xr.HasAttributes && int.TryParse(xr.GetAttribute("lcid"), out localLcid))
                {
                    if ((lcid == localLcid) && (!xr.IsEmptyElement))
                    {
                        xr.Read();
                        if (xr.NodeType == XmlNodeType.Text || xr.NodeType == XmlNodeType.Whitespace) //Message element contains text
                        {
                            return new SqlString(xr.Value);
                        }
                        return xr.NodeType == XmlNodeType.EndElement ? string.Empty : SqlString.Null;
                    }
                    if ((defaultLcid == localLcid) && (!xr.IsEmptyElement))
                    {
                        xr.Read();
                        if (xr.NodeType == XmlNodeType.Text || xr.NodeType == XmlNodeType.Whitespace) //Message element contains text
                        {
                            defaultValue = xr.Value;
                        }
                        else if (xr.NodeType == XmlNodeType.EndElement) //Message element contains no text
                        {
                            defaultValue = string.Empty;
                        }
                        else
                        {
                            defaultValue = SqlString.Null;
                        }
                    }
                }
            }
        }
        return defaultValue;
    }

    [SqlFunction(DataAccess = DataAccessKind.None, IsDeterministic = true)]
    public static SqlString fnXMLGetContextValue(SqlXml context, SqlString param)
    {
        if (context.IsNull || param.IsNull)
        {
            return SqlString.Null;
        }
        using (XmlReader xr = context.CreateReader())
        {
            while (xr.ReadToFollowing("contextparam"))
            {
                //                if(true) return xr.GetAttribute("key").Equals(Param.Value).ToString();
                if (xr.HasAttributes && xr.GetAttribute("key") == param.Value)
                {
                    xr.Read();
                    if (xr.NodeType == XmlNodeType.Text || xr.NodeType == XmlNodeType.Whitespace) //Message element contains text
                    {
                        return new SqlString(xr.Value);
                    }
                    return xr.NodeType == XmlNodeType.EndElement ? string.Empty : SqlString.Null;
                }
            }
        }
        return SqlString.Null;
    }
}