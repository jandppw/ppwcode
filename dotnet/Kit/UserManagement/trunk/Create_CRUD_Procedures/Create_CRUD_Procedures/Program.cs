using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;
using System.IO;

namespace Create_CRUD_Procedures
{

    class Program
    {
        class Permission : IComparable<Permission>
        {
            public string TableName { private get; set; }
            public char Action { private get; set; }
            public int PermissionId { get; private set; }

            public Permission()
            {
            }

            public Permission(int aPermissionI, char aAction, string aTableName)
            {
                TableName = aTableName;
                Action = aAction;
                PermissionId = aPermissionI;
            }

            #region IComparable<Permission> Members

            public int CompareTo(Permission other)
            {
                int result = String.CompareOrdinal(TableName, other.TableName);
                return result == 0 ? Action.CompareTo(other.Action) : result;
            }

            #endregion
        }

        class ProgramData
        {
            private string m_Server;
            private string m_Instance;
            private string m_Catalog;
            private string m_Userid;
            private string m_Password;
            private string m_RelationName;
            private string m_TemplateFile;
            private const int DefaultIndent = 6;
            private List<Permission> m_PermissionList;

            private void ParseArgs(IEnumerable<string> args)
            {
                foreach (string t in args)
                {
                    string optionValue;
                    string arg = t;
                    string option = arg.Substring(0, 2).ToUpper();
                    if (option[0] == '-' && option.Length == 2)
                    {
                        optionValue = arg.Substring(2, arg.Length - 2);
                    }
                    else
                    {
                        optionValue = null;
                    }
                    switch (option)
                    {
                        case "-S":
                            m_Server = optionValue;
                            break;
                        case "-I":
                            m_Instance = optionValue;
                            break;
                        case "-C":
                            m_Catalog = optionValue;
                            break;
                        case "-P":
                            m_Password = optionValue;
                            break;
                        case "-U":
                            m_Userid = optionValue;
                            break;
                        case "-R":
                            m_RelationName = optionValue;
                            break;
                        case "-T":
                            m_TemplateFile = optionValue;
                            break;
                    }
                }
                if (m_Server == null)
                {
                    m_Server = "(local)";
                }
                if (m_Catalog == null)
                {
                    m_Catalog = "UserManagement";
                }
                if (m_TemplateFile == null)
                {
                    m_TemplateFile = "template.sql";
                }
                if (m_RelationName == null)
                {
                    m_RelationName = string.Empty;
                }
            }

            private void CheckTemplateExistence()
            {
                if (!(m_TemplateFile != null && File.Exists(m_TemplateFile)))
                {
                    throw new ApplicationException("Specify an existing template file.");
                }
            }

            private string BuildConnectString()
            {
                SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder
                {
                    DataSource = string.Concat(m_Server, (m_Instance == null) ? string.Empty : string.Concat("\\", m_Instance)),
                    IntegratedSecurity = (m_Userid == null)
                };
                if (!builder.IntegratedSecurity)
                {
                    builder.UserID = m_Userid;
                    builder.Password = m_Password;
                }
                if (m_Catalog != null)
                {
                    builder.InitialCatalog = m_Catalog;
                }
                return builder.ConnectionString;
            }

            private RelationInfo[] GetAllColumnsExceptAudit(InformationSchemaClassesDataContext db, string tableName)
            {
                string[] excludedFieldNames = {
                    "UserCreate",
                    "DateCreated",
                    "UserModified",
                    "DateModified"};
                IEnumerable<RelationInfo> columns =
                    db.RelationInfos
                    .Where(o => o.TABLE_NAME == tableName
                                && o.is_computed == false
                                && !excludedFieldNames.Contains(o.COLUMN_NAME))
                    .OrderBy(o => o.ORDINAL_POSITION)
                    .Select(o => o);
                return columns.ToArray();
            }

            private string BuildCRLFCommaList(int indentSpaces, List<string> ls)
            {
                StringBuilder sb = new StringBuilder(1024);
                int len = ls.Count;
                int i = 0;
                foreach (string s in ls)
                {
                    sb.Append(' ', indentSpaces);
                    if (i == len - 1)
                    {
                        sb.Append(s);
                    }
                    else
                    {
                        sb.AppendLine(string.Concat(s, ','));
                    }
                    i++;
                }
                return sb.ToString();
            }

            private string BuildCRLFUpdateSetList(int indentSpaces, string tableName, List<string> ls)
            {
                StringBuilder sb = new StringBuilder(1024);
                int len = ls.Count;
                int i = 0;
                sb.Append(' ', indentSpaces);
                sb.AppendLine(string.Concat("update [dbo].[", tableName, ']'));
                foreach (string s in ls)
                {
                    if (i == 0)
                    {
                        sb.Append(' ', indentSpaces);
                        sb.AppendLine(i == len - 1 ? string.Concat("   set ", s) : string.Concat("   set ", s, ','));
                    }
                    else
                    {
                        sb.Append(' ', indentSpaces + 7);
                        sb.AppendLine(i == len - 1 ? s : string.Concat(s, ','));
                    }
                    i++;
                }
                return sb.ToString();
            }

            private string BuildCRLFWhereList(int indentSpaces, List<string> ls)
            {
                StringBuilder sb = new StringBuilder(1024);
                int len = ls.Count;
                int i = 0;
                foreach (string s in ls)
                {
                    if (i == 0)
                    {
                        sb.Append(' ', indentSpaces);
                        if (i == len - 1)
                        {
                            sb.Append(string.Concat(" where ", s));
                        }
                        else
                        {
                            sb.AppendLine(string.Concat(" where ", s));
                        }
                    }
                    else
                    {
                        sb.Append(' ', indentSpaces);
                        sb.Append("   and ");
                        if (i == len - 1)
                        {
                            sb.Append(s);
                        }
                        else
                        {
                            sb.AppendLine(s);
                        }
                    }
                    i++;
                }
                return sb.ToString();
            }

            private string CreateInputParameters(IEnumerable<RelationInfo> allColumns, bool returnPKs)
            {
                List<string> ls = new List<string>
                {
                    "@Context xml"
                };
                foreach (RelationInfo c in allColumns)
                {
                    if (returnPKs && c.is_pk == true && c.is_identity)
                    {
                        ls.Add(string.Concat("@", c.COLUMN_NAME, ' ', c.GetDBType(), " OUTPUT"));
                    }
                    else
                    {
                        ls.Add(string.Concat("@", c.COLUMN_NAME, ' ', c.GetDBType()));
                    }
                }
                ls.Add("@errorinfo nvarchar(4000) OUTPUT");
                return BuildCRLFCommaList(4, ls);
            }

            private int GetPermissionId(string tableName, char action)
            {
                int idx = m_PermissionList.BinarySearch(new Permission { TableName = tableName, Action = action });
                return idx < 0 ? -1 : m_PermissionList[idx].PermissionId;
            }

            private string CreateUpdateScript(RelationInfo[] allColumns, string tableName, bool hasAudit, string template)
            {
                IEnumerable<RelationInfo> columns =
                    allColumns
                    .Where(c => c.TABLE_NAME == tableName && c.is_pk == false)
                    .OrderBy(c => c.ORDINAL_POSITION);
                IEnumerable<RelationInfo> pkColumns =
                    allColumns
                    .Where(c => c.TABLE_NAME == tableName && c.is_pk == true)
                    .OrderBy(c => c.PK_ORDINAL_POSITION);

                List<string> ls = columns.Select(c => string.Concat('[', c.COLUMN_NAME, "] = @", c.COLUMN_NAME)).ToList();
                if (!columns.Any())
                {
                    return null;
                }
                if (hasAudit)
                {
                    ls.Add("[UserModified] = @MetaDataUserID");
                    ls.Add("[DateModified] = getdate()");
                }

                List<string> ls2 = pkColumns.Select(c => string.Concat('[', c.COLUMN_NAME, "] = @", c.COLUMN_NAME)).ToList();
                if (!pkColumns.Any())
                {
                    return null;
                }

                string statement;
                int permissionId = GetPermissionId(tableName, 'U');
                if (permissionId >= 0)
                {
                    StringBuilder sb = new StringBuilder(1024);
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine(string.Format("if (dbo.fnCheckSecurity(@Context, @ContextUserID, {0}) = 1)", permissionId));
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine("begin");
                    sb.AppendLine(string.Concat(BuildCRLFUpdateSetList(DefaultIndent + 2, tableName, ls), BuildCRLFWhereList(DefaultIndent + 2, ls2), ';'));
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine("end");
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine("else");
                    sb.Append(' ', DefaultIndent + 2);
                    sb.Append(string.Format("raiserror(50001,16,1,'Update({0})','{1}');", permissionId, tableName));
                    statement = sb.ToString();
                }
                else
                {
                    statement = string.Concat(BuildCRLFUpdateSetList(DefaultIndent, tableName, ls), BuildCRLFWhereList(DefaultIndent, ls2), ';');
                }

                template = template.Replace("<<catalog>>", m_Catalog);
                template = template.Replace("<<today>>", DateTime.Today.ToShortDateString());
                template = template.Replace("<<ProcedureName>>", string.Concat("[dbo].[P_", tableName, "_U]"));
                template = template.Replace("<<InputParams>>", CreateInputParameters(allColumns, false));
                template = template.Replace("<<statement>>", statement);
                return template;
            }

            private string BuildDelete(int indentSpaces, string tableName, List<string> ls)
            {
                StringBuilder sb = new StringBuilder(1024);
                sb.Append(' ', indentSpaces);
                sb.AppendLine("delete");
                sb.Append(' ', indentSpaces);
                sb.AppendLine(string.Concat("  from [dbo].[", tableName, ']'));
                sb.Append(BuildCRLFWhereList(indentSpaces, ls));
                return sb.ToString();
            }

            private string CreateDeleteScript(IEnumerable<RelationInfo> allColumns, string tableName, string template)
            {
                IEnumerable<RelationInfo> pkColumns =
                    allColumns
                    .Where(c => c.TABLE_NAME == tableName && c.is_pk == true)
                    .OrderBy(c => c.PK_ORDINAL_POSITION);

                List<string> ls = pkColumns.Select(c => string.Concat('[', c.COLUMN_NAME, "] = @", c.COLUMN_NAME)).ToList();
                if (!pkColumns.Any())
                {
                    return null;
                }

                string statement;
                int permissionId = GetPermissionId(tableName, 'D');
                if (permissionId >= 0)
                {
                    StringBuilder sb = new StringBuilder(1024);
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine(string.Format("if (dbo.fnCheckSecurity(@Context, @ContextUserID, {0}) = 1)", permissionId));
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine("begin");
                    sb.AppendLine(string.Concat(BuildDelete(DefaultIndent + 2, tableName, ls), ';'));
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine("end");
                    sb.Append(' ', DefaultIndent);
                    sb.AppendLine("else");
                    sb.Append(' ', DefaultIndent + 2);
                    sb.Append(string.Format("raiserror(50001,16,1,'Delete({0})','{1}');", permissionId, tableName));
                    statement = sb.ToString();
                }
                else
                {
                    statement = string.Concat(BuildDelete(DefaultIndent, tableName, ls), ';');
                }

                template = template.Replace("<<catalog>>", m_Catalog);
                template = template.Replace("<<today>>", DateTime.Today.ToShortDateString());
                template = template.Replace("<<ProcedureName>>", string.Concat("[dbo].[P_", tableName, "_D]"));
                template = template.Replace("<<InputParams>>", CreateInputParameters(pkColumns.ToArray(), false));
                template = template.Replace("<<statement>>", statement);
                return template;
            }

            private string CreateInsertScript(RelationInfo[] allColumns, string tableName, bool hasAudit, string template)
            {
                IEnumerable<RelationInfo> columns =
                    allColumns
                    .Where(c => c.TABLE_NAME == tableName && c.is_identity == false)
                    .OrderBy(c => c.ORDINAL_POSITION);
                RelationInfo identityColumn =
                    allColumns.SingleOrDefault(c => c.TABLE_NAME == tableName && c.is_identity);

                List<string> ls1 = new List<string>();
                List<string> ls2 = new List<string>();
                foreach (RelationInfo c in columns)
                {
                    ls1.Add(string.Concat('[', c.COLUMN_NAME, ']'));
                    ls2.Add(string.Concat('@', c.COLUMN_NAME));
                }
                if (hasAudit)
                {
                    ls1.Add("[UserCreate]");
                    ls1.Add("[UserModified]");

                    ls2.Add("@ContextUserID");
                    ls2.Add("@MetaDataUserID");
                }

                StringBuilder sb = new StringBuilder(1024);
                int permissionId = GetPermissionId(tableName, 'C');
                int indent = (permissionId >= 0) ? DefaultIndent + 2 : DefaultIndent;
                sb.Append(' ', indent);
                sb.AppendLine(string.Concat("insert into [dbo].[", tableName, "] ("));
                sb.AppendLine(string.Concat(BuildCRLFCommaList(indent + 2, ls1), ')'));
                sb.Append(' ', indent);
                sb.AppendLine("values (");
                sb.Append(string.Concat(BuildCRLFCommaList(indent + 2, ls2), ");"));
                if (identityColumn != null)
                {
                    string strIdentity = identityColumn.COLUMN_NAME;
                    sb.AppendLine();
                    sb.Append(' ', indent);
                    sb.Append(string.Concat("set @", strIdentity, " = scope_identity();"));
                }

                string statement;
                if (permissionId >= 0)
                {
                    StringBuilder sb2 = new StringBuilder(1024);
                    sb2.Append(' ', DefaultIndent);
                    sb2.AppendLine(string.Format("if (dbo.fnCheckSecurity(@Context, @ContextUserID, {0}) = 1)", permissionId));
                    sb2.Append(' ', DefaultIndent);
                    sb2.AppendLine("begin");
                    sb2.AppendLine(sb.ToString());
                    sb2.Append(' ', DefaultIndent);
                    sb2.AppendLine("end");
                    sb2.Append(' ', DefaultIndent);
                    sb2.AppendLine("else");
                    sb2.Append(' ', DefaultIndent + 2);
                    sb2.Append(string.Format("raiserror(50001,16,1,'Insert({0})','{1}');", permissionId, tableName));
                    statement = sb2.ToString();
                }
                else
                {
                    statement = sb.ToString();
                }

                template = template.Replace("<<catalog>>", m_Catalog);
                template = template.Replace("<<today>>", DateTime.Today.ToShortDateString());
                template = template.Replace("<<ProcedureName>>", string.Concat("[dbo].[P_", tableName, "_I]"));
                template = template.Replace("<<InputParams>>", CreateInputParameters(allColumns, true));
                template = template.Replace("<<statement>>", statement);

                return template;
            }

            private void CreateCrudScripts()
            {
                string[] excludedRelations = { };
                string[] excludedTables4Permission = 
                {
                    "Language", 
                    "Menu", 
                    "MenuPermission", 
                    "Page",
                    "PagePermission",
                    "Permission"                     
                };


                using (InformationSchemaClassesDataContext db = new InformationSchemaClassesDataContext(BuildConnectString()))
                {
                    var tables =
                        db.RelationInfos
                        .Where(r => r.TABLE_NAME.StartsWith(m_RelationName)
                                    && r.TABLE_TYPE == "BASE TABLE"
                                    && !excludedRelations.Contains(r.TABLE_NAME))
                        .Select(r => new { r.TABLE_NAME, r.TABLE_TYPE, r.has_audit })
                        .Distinct();
                    m_PermissionList =
                        db.Permissions
                        .Where(p => !excludedTables4Permission.Contains(p.Name))
                        .Select(p => new Permission(p.PermissionId, p.Action, p.Name))
                        .ToList();
                    m_PermissionList.Sort();

                    string template;
                    using (StreamReader tr = new StreamReader(m_TemplateFile))
                    {
                        template = tr.ReadToEnd();
                    }

                    Console.WriteLine(string.Format("use [{0}]", m_Catalog));
                    Console.WriteLine("go");
                    Console.WriteLine(string.Empty);
                    Console.WriteLine(string.Empty);

                    foreach (var t in tables)
                    {
                        RelationInfo[] allColumns = GetAllColumnsExceptAudit(db, t.TABLE_NAME);

                        string script = CreateUpdateScript(allColumns, t.TABLE_NAME, t.has_audit.Value, template);
                        if (script != null)
                            Console.WriteLine(script);

                        script = CreateDeleteScript(allColumns, t.TABLE_NAME, template);
                        if (script != null)
                            Console.WriteLine(script);

                        script = CreateInsertScript(allColumns, t.TABLE_NAME, t.has_audit.Value, template);
                        if (script != null)
                            Console.WriteLine(script);

                    }
                }
            }

            public static void Run(IEnumerable<string> args)
            {
                ProgramData pd = new ProgramData();
                try
                {
                    pd.ParseArgs(args);
                    pd.CheckTemplateExistence();
                    pd.CreateCrudScripts();
                }
                catch (Exception exObj)
                {
                    Console.WriteLine(exObj);
                }
            }
        }

        static void Main(string[] args)
        {
            ProgramData.Run(args);
        }
    }
}