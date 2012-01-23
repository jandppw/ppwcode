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

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Vernacular.Exceptions.I;

#endregion

namespace PPWCode.Kit.Tasks.API_I.RemoteTest
{
    [TestClass]
    public class TaskTests : BaseTaskTests
    {
        [TestMethod]
        public void CreateTask()
        {
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());
        }

        [TestMethod]
        [ExpectedException(typeof(ProgrammingError))]
        public void CreateTaskAndDelete()
        {
            Task task = SaveTask(CreateTasksWithOneAttribute());
            Svc.Delete(task);
        }

        [TestMethod]
        public void CreateAndUpdateStates()
        {
            Task task = SaveTask(CreateTasksWithOneAttribute());
            {
                task.State = TaskStateEnum.IN_PROGRESS;
                task = Svc.Update(task);
                Assert.IsNotNull(task);
                Assert.AreEqual(task.State, TaskStateEnum.IN_PROGRESS);
            }
            {
                task.State = TaskStateEnum.COMPLETED;
                task = Svc.Update(task);
                Assert.IsNotNull(task);
                Assert.AreEqual(task.State, TaskStateEnum.COMPLETED);
            }
        }

        [TestMethod]
        [ExpectedException(typeof(ProgrammingError))]
        public void CreateAndUpdateStateThenTaskType()
        {
            Task task = SaveTask(CreateTasksWithOneAttribute());
            {
                task.State = TaskStateEnum.IN_PROGRESS;
                task = Svc.Update(task);
                Assert.IsNotNull(task);
                Assert.AreEqual(task.State, TaskStateEnum.IN_PROGRESS);
            }
            {
                task.TaskType = "taskType2/";
                Svc.Update(task);
            }
        }

        [TestMethod]
        public void CreateAndUpdateAndSelectTask()
        {
            {
                CreateSomeTasksForSearching();
                FindTasksResult findTasksResult = Svc.FindTasks(@"Unknown", null, TaskStateEnum.CREATED);
                Assert.IsNotNull(findTasksResult);
                Assert.AreEqual(0, findTasksResult.NumberOfMatchingTasks);
                Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
            }
            {
                ClearContentOfTables();
                Svc.FlushAllCaches();
                CreateSomeTasksForSearching();
                FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", null, TaskStateEnum.CREATED);
                Assert.IsNotNull(findTasksResult);
                Assert.AreEqual(3, findTasksResult.NumberOfMatchingTasks);
                Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
            }
            {
                ClearContentOfTables();
                Svc.FlushAllCaches();
                CreateSomeTasksForSearching();
                FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", null, TaskStateEnum.CREATED | TaskStateEnum.IN_PROGRESS);
                Assert.IsNotNull(findTasksResult);
                Assert.AreEqual(3, findTasksResult.NumberOfMatchingTasks);
                Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
            }
            {
                ClearContentOfTables();
                Svc.FlushAllCaches();
                CreateSomeTasksForSearching();
                IDictionary<string, string> searchAttributes = new Dictionary<string, string>
                {
                    { "TypeName", "/PensioB/Sempera/Affiliation" },
                    { "RetirementPlanName", "construo" },
                };
                FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", searchAttributes, TaskStateEnum.CREATED);
                Assert.IsNotNull(findTasksResult);
                Assert.AreEqual(3, findTasksResult.NumberOfMatchingTasks);
                Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
            }
            {
                ClearContentOfTables();
                Svc.FlushAllCaches();
                CreateSomeTasksForSearching();
                IDictionary<string, string> searchAttributes = new Dictionary<string, string>
                {
                    { "TypeName", "/PensioB/Sempera/Affiliation" },
                    { "RetirementPlanName", "construo" },
                    { "AffiliateSynergyID", "6788" },
                    { "AffiliationID", "285" },
                };
                FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", searchAttributes, TaskStateEnum.CREATED);
                Assert.IsNotNull(findTasksResult);
                Assert.AreEqual(1, findTasksResult.NumberOfMatchingTasks);
                Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
            }
            {
                ClearContentOfTables();
                Svc.FlushAllCaches();
                CreateSomeTasksForSearching();
                IDictionary<string, string> searchAttributes = new Dictionary<string, string>
                {
                    { "TypeName", "/PensioB/Sempera/Affiliation" },
                    { "RetirementPlanName", "construo" },
                    { "AffiliateSynergyID", "6788" },
                };
                FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", searchAttributes, TaskStateEnum.CREATED);
                Assert.IsNotNull(findTasksResult);
                Assert.AreEqual(1, findTasksResult.NumberOfMatchingTasks);
                Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
            }
            {
                ClearContentOfTables();
                Svc.FlushAllCaches();
                CreateSomeTasksForSearching();
                IDictionary<string, string> searchAttributes = new Dictionary<string, string>
                {
                    { "TypeName", "/PensioB/Sempera/Affiliation" },
                    { "RetirementPlanName", "construo" },
                    { "AffiliationID", "285" },
                };
                FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", searchAttributes, TaskStateEnum.CREATED);
                Assert.IsNotNull(findTasksResult);
                Assert.AreEqual(1, findTasksResult.NumberOfMatchingTasks);
                Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
            }
        }
    }
}