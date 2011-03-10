#region Using

using System.Configuration;
using System.Data.SqlClient;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Util.OddsAndEnds.I.Extensions;

using Spring.Context.Support;

#endregion

namespace PPWCode.Kit.Tasks.API_I.RemoteTest
{
    public static class RemoteTestHelper
    {
        private static void ClearContentOfTables()
        {
            string connectionString = ConfigurationManager.ConnectionStrings["TasksConnectionString"].ConnectionString;
            Assert.IsFalse(connectionString == null);
            using (SqlConnection con = new SqlConnection(connectionString))
            {
                string[] tblNames = new[]
                {
                    "dbo.Task",
                    "dbo.AuditLog",
                };
                con.Open();
                foreach (string tblName in tblNames)
                {
                    using (var cmd = con.CreateCommand())
                    {
                        cmd.CommandText = string.Format("delete from {0}", tblName);
                        cmd.ExecuteNonQuery();
                    }
                }
                con.Close();
            }
        }

        public static ClientTasksDao CreateTaskService()
        {
            ClearContentOfTables();

            ITasksDao tasks = ContextRegistry.GetContext().GetObject<ITasksDao>("TasksFactory");
            ClientTasksDao result = new ClientTasksDao(tasks);

            result.FlushAllCaches();
            return result;
        }
    }
}