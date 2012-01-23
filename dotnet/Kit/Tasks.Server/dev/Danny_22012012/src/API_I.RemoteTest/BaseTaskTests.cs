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

using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Util.OddsAndEnds.I.Extensions;

using Spring.Context.Support;

#endregion

namespace PPWCode.Kit.Tasks.API_I.RemoteTest
{
    [TestClass]
    public abstract class BaseTaskTests
    {
        #region Test Setup

        protected ClientTasksDao Svc { get; private set; }

        [TestInitialize]
        public void Initialize()
        {
            Svc = GetClientTasksDao();
        }

        [TestCleanup]
        public void Cleanup()
        {
            Svc.Dispose();
        }

        #endregion

        #region Protected Helpers

        protected static Task CreateTasksWithOneAttribute()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "taskType/",
            };
            task.AddAttribute(@"key1", @"value1");

            return task;
        }

        protected static Task CreateTasksWithTwoAttributes()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "taskType/",
            };
            task.AddAttribute(@"key1", @"value1");
            task.AddAttribute(@"key2", @"value2");

            return task;
        }

        protected static Task CreateTasksWithThreeAttributes()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded/",
            };
            task.AddAttribute(@"key1", @"value1");
            task.AddAttribute(@"key2", @"value2");
            task.AddAttribute(@"key3", @"value3");

            return task;
        }

        protected IEnumerable<Task> CreateSomeTasksForSearching()
        {
            IList<Task> result = new List<Task>();
            {
                IDictionary<string, string> attributes = new Dictionary<string, string>
                {
                    { "TypeName", "/PensioB/Sempera/Affiliation" },
                    { "RetirementPlanName", "construo" },
                    { "AffiliateSynergyId", "2752" },
                    { "AffiliationID", "107" },
                };
                Task task = new Task
                {
                    State = TaskStateEnum.CREATED,
                    TaskType = "/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded/",
                };
                task.AddAttributes(attributes);
                result.Add(SaveTask(task));
            }
            {
                IDictionary<string, string> attributes = new Dictionary<string, string>
                {
                    { "TypeName", "/PensioB/Sempera/Affiliation" },
                    { "RetirementPlanName", "construo" },
                    { "AffiliateSynergyId", "2787" },
                    { "AffiliationID", "246" },
                };
                Task task = new Task
                {
                    State = TaskStateEnum.CREATED,
                    TaskType = "/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded/",
                };
                task.AddAttributes(attributes);
                result.Add(SaveTask(task));
            }
            {
                IDictionary<string, string> attributes = new Dictionary<string, string>
                {
                    { "TypeName", "/PensioB/Sempera/Affiliation" },
                    { "RetirementPlanName", "construo" },
                    { "AffiliateSynergyId", "6788" },
                    { "AffiliationID", "285" },
                };
                Task task = new Task
                {
                    State = TaskStateEnum.CREATED,
                    TaskType = "/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded/",
                };
                task.AddAttributes(attributes);
                result.Add(SaveTask(task));
            }

            return result;
        }

        protected static bool EqualAttributes(Task t1, Task t2)
        {
            bool result = t1.Attributes.Count == t2.Attributes.Count;
            if (result)
            {
                foreach (var pair in t1.Attributes)
                {
                    string value;
                    result = t2.Attributes.TryGetValue(pair.Key, out value);
                    result &= pair.Value.Equals(value);
                    if (!result)
                    {
                        break;
                    }
                }
            }
            return result;
        }

        protected Task SaveTask(Task task)
        {
            Task createdTask = Svc.Create(task);
            Assert.IsNotNull(createdTask);
            Assert.AreEqual(task.State, createdTask.State);
            Assert.AreEqual(task.TaskType, createdTask.TaskType);
            EqualAttributes(task, createdTask);
            return createdTask;
        }

        #endregion

        #region Private Helpers

        protected static void ClearContentOfTables()
        {
            string connectionString = ConfigurationManager.ConnectionStrings["TasksConnectionString"].ConnectionString;
            Assert.IsFalse(connectionString == null);
            using (SqlConnection con = new SqlConnection(connectionString))
            {
                string[] tblNames = new[]
                {
                    @"dbo.TaskAttributes",
                    @"dbo.Task",
                    @"dbo.AuditLog",
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

        private static ClientTasksDao GetClientTasksDao()
        {
            ClearContentOfTables();

            ITasksDao tasks = ContextRegistry.GetContext().GetObject<ITasksDao>("TasksFactory");
            ClientTasksDao result = new ClientTasksDao(tasks);

            result.FlushAllCaches();
            return result;
        }

        #endregion
    }
}