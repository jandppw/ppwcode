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

using System;
using System.Collections.Generic;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Vernacular.Exceptions.I;

#endregion

namespace PPWCode.Kit.Tasks.API_I.RemoteTest
{
    // ReSharper disable InconsistentNaming
    [TestClass]
    public class TaskTests : BaseTaskTests
    {
        private void CheckAttributeOccurrences(string key, string value, int expectedCount)
        {
            IDictionary<string, string> searchAttributes = new Dictionary<string, string>
            {
                { "key1", "value1" }
            };
            FindTasksResult findTasksResult = Svc.FindTasks(@"", searchAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(expectedCount, findTasksResult.NumberOfMatchingTasks);
            
        }

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
        public void SelectTask_UnknownTaskType_CreatedState_NoAttributes()
        {
            CreateSomeTasksForSearching();
            FindTasksResult findTasksResult = Svc.FindTasks(@"Unknown", null, TaskStateEnum.CREATED);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(0, findTasksResult.NumberOfMatchingTasks);
            Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
        }

        [TestMethod]
        public void SelectTask_TaskType_CreatedState_NoAttributes()
        {
            ClearContentOfTables();
            Svc.FlushAllCaches();
            CreateSomeTasksForSearching();
            FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", null, TaskStateEnum.CREATED);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(3, findTasksResult.NumberOfMatchingTasks);
            Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
        }

        [TestMethod]
        public void SelectTask_TaskType_CreatedAndInProgressState_NoAttributes()
        {
            ClearContentOfTables();
            Svc.FlushAllCaches();
            CreateSomeTasksForSearching();
            FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", null, TaskStateEnum.CREATED | TaskStateEnum.IN_PROGRESS);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(3, findTasksResult.NumberOfMatchingTasks);
            Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
        }

        [TestMethod]
        public void SelectTask_TaskType_CreatedState_TwoCorrectAttributes()
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

        [TestMethod]
        public void SelectTask_TaskType_CreatedState_ThreeCorrectAttributes()
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

        [TestMethod]
        public void SelectTask_TaskType_CreatedState_FourCorrectAttributes()
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

        [TestMethod]
        public void SelectTask_TaskType_NoState_ThreeCorrectAttributes()
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
            FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", searchAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(1, findTasksResult.NumberOfMatchingTasks);
            Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
        }

        [TestMethod]
        [ExpectedException(typeof(ProgrammingError))]
        public void UpdateTaskAttributes_ThreeCorrectAttributes_ZeroReplaceAttributes()
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
            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>();
            Svc.UpdateTaskAttributes(new[] { @"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded" }, searchAttributes, replaceAttributes);
        }

        [TestMethod]
        public void UpdateTaskAttributes_ThreeCorrectAttributes_OneMatchingReplaceAttribute()
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
            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "RetirementPlanName", "construo-test" },
            };
            IDictionary<string, string> newSearchAttributes = new Dictionary<string, string>
            {
                { "TypeName", "/PensioB/Sempera/Affiliation" },
                { "RetirementPlanName", "construo-test" },
                { "AffiliationID", "285" },
            };
            Svc.UpdateTaskAttributes(new[] { @"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded" }, searchAttributes, replaceAttributes);
            FindTasksResult oldFindTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", searchAttributes, null);
            FindTasksResult newFindTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", newSearchAttributes, null);
            Assert.IsNotNull(oldFindTasksResult);
            Assert.AreEqual(0, oldFindTasksResult.NumberOfMatchingTasks);
            Assert.IsNotNull(newFindTasksResult);
            Assert.AreEqual(1, newFindTasksResult.NumberOfMatchingTasks);
            using(IEnumerator<Task> enu = newFindTasksResult.Tasks.GetEnumerator())
            {
                enu.Reset();
                enu.MoveNext();
                Assert.AreEqual(enu.Current.Attributes["TypeName"], "/PensioB/Sempera/Affiliation");
                Assert.AreEqual(enu.Current.Attributes["RetirementPlanName"], "construo-test");
                Assert.AreEqual(enu.Current.Attributes["AffiliationID"], "285");
            }
        }



        [TestMethod]
        public void UpdateTaskAttributes_DiffNoOfAttributes_All_OneNewValue()
        {
            ClearContentOfTables();
            Svc.FlushAllCaches();
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());

            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" }
            };
            Svc.UpdateTaskAttributes(new string[0], null, replaceAttributes);

            IDictionary<string, string> oldSearchAttributes = new Dictionary<string, string>
            {
                { "key1", "value1" }
            };
            IDictionary<string, string> newSearchAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" }
            };
            FindTasksResult oldFindTasksResult = Svc.FindTasks(@"", oldSearchAttributes, null);
            FindTasksResult newFindTasksResult = Svc.FindTasks(@"", newSearchAttributes, null);
            Assert.IsNotNull(oldFindTasksResult);
            Assert.AreEqual(0, oldFindTasksResult.NumberOfMatchingTasks);
            Assert.IsNotNull(newFindTasksResult);
            Assert.AreEqual(3, newFindTasksResult.NumberOfMatchingTasks);
        }

        [TestMethod]
        public void UpdateTaskAttributes_DiffNoOfAttributes_All_TwoNewValues()
        {
            ClearContentOfTables();
            Svc.FlushAllCaches();
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());

            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" },
                { "key2", "modifiedvalue2" }
            };
            Svc.UpdateTaskAttributes(new string[0], null, replaceAttributes);

            CheckAttributeOccurrences("key1", "value1", 1);
            CheckAttributeOccurrences("key2", "value2", 0);
            CheckAttributeOccurrences("key3", "value3", 1);
            CheckAttributeOccurrences("key1", "modifiedvalue1", 2);
            CheckAttributeOccurrences("key2", "modifiedvalue2", 2);

            FindTasksResult findTasksResult = Svc.FindTasks(@"", replaceAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(2, findTasksResult.NumberOfMatchingTasks);
        }
    }

    // ReSharper restore InconsistentNaming
}