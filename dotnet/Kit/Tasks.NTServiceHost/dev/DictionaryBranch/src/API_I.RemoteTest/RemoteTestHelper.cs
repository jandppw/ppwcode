/*
 * Copyright 2004 - $Date: 2008-11-15 23:58:07 +0100 (za, 15 nov 2008) $ by PeopleWare n.v..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

            ITasksDao tasks = ContextRegistry.GetContext().GetObject<ITasksDao>("TasksDaoFactory");
            ClientTasksDao result = new ClientTasksDao(tasks);

            result.FlushAllCaches();
            return result;
        }
    }
}