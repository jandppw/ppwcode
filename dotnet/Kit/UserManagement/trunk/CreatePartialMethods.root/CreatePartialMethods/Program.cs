using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Data.SqlClient;

namespace CreatePartialMethods
{

    class ProgramData
    {
        public string Server { get; set; }
        public string Instance { get; set; }
        public string Catalog { get; set; }
        public string Password { get; set; }
        public string UserId { get; set; }
        public string PreFile { get; set; }
        public string PostFile { get; set; }

        private readonly Dictionary<string, string> m_FaultedObjects = new Dictionary<string, string>();

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
                        Server = optionValue;
                        break;
                    case "-I":
                        Instance = optionValue;
                        break;
                    case "-C":
                        Catalog = optionValue;
                        break;
                    case "-P":
                        Password = optionValue;
                        break;
                    case "-U":
                        UserId = optionValue;
                        break;
                    case "-1":
                        PreFile = optionValue;
                        break;
                    case "-2":
                        PostFile = optionValue;
                        break;
                }
            }
            if (Server == null)
            {
                Server = "(local)";
            }
            if (Catalog == null)
            {
                Catalog = "UserManagement";
            }
            if (PreFile == null)
            {
                PreFile = "pre_partialmethods.cs";
            }
            if (PostFile == null)
            {
                PostFile = "post_partialmethods.cs";
            }
        }

        private void CheckFileExistence()
        {
            if (!(PreFile != null && File.Exists(PreFile)))
            {
                throw new ApplicationException("Specify an existing pre partialmethods c# file.");
            }
            if (!(PostFile != null && File.Exists(PostFile)))
            {
                throw new ApplicationException("Specify an existing post partialmethods c# file.");
            }
        }

        private string BuildConnectString()
        {
            SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder
            {
                DataSource = string.Concat(Server, (Instance == null) ? string.Empty : string.Concat("\\", Instance)),
                IntegratedSecurity = (UserId == null)
            };
            if (!builder.IntegratedSecurity)
            {
                builder.UserID = UserId;
                builder.Password = Password;
            }
            if (Catalog != null)
            {
                builder.InitialCatalog = Catalog;
            }
            return builder.ConnectionString;
        }

        private IEnumerable<UIDRoutine> GetRoutines(zetesDataContext db)
        {
            string[] relations = {};

            IEnumerable<UIDRoutine> routines =
                db.UIDRoutines
                .Where(r => !relations.Contains(r.relation))
                .OrderBy(r => r.relation)
                .ThenBy(r => r.PartialMethodName);
            return routines.ToArray();
        }

        private UIDRoutineParameter[] GetRoutineParameters(zetesDataContext db)
        {
            IEnumerable<UIDRoutineParameter> parameters =
                db.UIDRoutineParameters;
            return parameters.ToArray();
        }

        private void DumpFile(string preFile)
        {
            using (StreamReader tr = new StreamReader(preFile))
            {
                while (!tr.EndOfStream)
                {
                    string line = tr.ReadLine();
                    Console.WriteLine(line);
                }
            }
        }

        private void DumpPre()
        {
            DumpFile(PreFile);
        }

        private void DumpPost()
        {
            DumpFile(PostFile);
        }

        private void AddLine(StringBuilder sb, string line)
        {
            sb.Append(' ', 8);
            sb.AppendLine(line);
        }

        private void AddLine2(StringBuilder sb, string line)
        {
            sb.Append(' ', 12);
            sb.AppendLine(line);
        }

        private void AddLine3(StringBuilder sb, string line)
        {
            sb.Append(' ', 20);
            sb.AppendLine(line);
        }

        private void CreatePartialMethods()
        {
            using (zetesDataContext db = new zetesDataContext(BuildConnectString()))
            {
                IEnumerable<UIDRoutine> routines = GetRoutines(db);
                UIDRoutineParameter[] allRoutineParameters = GetRoutineParameters(db);
                foreach (UIDRoutine routine in routines)
                {
                    ChangeFaultedObjects(routine);

                    StringBuilder sb = new StringBuilder(512);
                    UIDRoutineParameter[] routineParameters =
                        allRoutineParameters
                        .Where(p => p.routine_name == routine.routine_name)
                        .OrderBy(p => p.ORDINAL_POSITION)
                        .ToArray();

                    // Header
                    AddLine(sb, string.Format("partial void {0}({1} instance) {{", routine.PartialMethodName, routine.relation));
                    AddLine2(sb, "int rc;");
                    AddLine2(sb, "string errorinfo = null;");
                    foreach (UIDRoutineParameter routineParameter in routineParameters)
                    {
                        if (routineParameter.CS_DECLARATION != null)
                        {
                            AddLine2(sb, routineParameter.CS_DECLARATION);
                        }
                    }
                    sb.AppendLine();
                    AddLine2(sb, string.Format("rc = this.{0}(", routine.routine_name));
                    for (int i = 0; i < routineParameters.Count(); i++)
                    {
                        UIDRoutineParameter routineParameter = routineParameters[i];
                        AddLine3(sb, string.Concat(routineParameter.CS_PARAMETER_DESCR, i == routineParameters.Count() - 1 ? ");" : ","));
                    }
                    foreach (UIDRoutineParameter routineParameter in routineParameters)
                    {
                        if (routineParameter.CS_AFTER_CALL != null)
                        {
                            AddLine2(sb, routineParameter.CS_AFTER_CALL);
                        }
                    }
                    AddLine2(sb, "if (rc != 0) {");
                    AddLine2(sb, "  throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));");
                    AddLine2(sb, "}");
                    AddLine(sb, "}");
 
                    Console.WriteLine(sb.ToString());
                }
            }
        }

        private void ChangeFaultedObjects(UIDRoutine routine)
        {
            // Speciaal geval, Objecten die een naam hebben in de DB die door de DMBL anders hernoemt zal worden
            if (m_FaultedObjects.ContainsKey(routine.relation))
            {
                string value = m_FaultedObjects[routine.relation];
                routine.PartialMethodName = routine.PartialMethodName.Replace(routine.relation, value);
                routine.base_routine = routine.PartialMethodName.Replace(routine.relation, value);
                routine.relation = value; 
            }
        }

        public static void Run(string[] args)
        {
            ProgramData pd = new ProgramData();
            try
            {
                pd.ParseArgs(args);
                pd.CheckFileExistence();
                pd.DumpPre();
                pd.CreatePartialMethods();
                pd.DumpPost();
            }
            catch (Exception exObj)
            {
                Console.WriteLine(exObj);
            }
        }

    }

    class Program
    {
        static void Main(string[] args)
        {
            ProgramData.Run(args);
        }
    }
}
