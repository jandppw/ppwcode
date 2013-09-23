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

using NUnit.Framework;

using PPWCode.Vernacular.Exceptions.I;

#endregion

namespace PPWCode.Kit.Tasks.API_I.RemoteTest
{
    // ReSharper disable InconsistentNaming
    [TestFixture]
    public class TaskTests : BaseTaskTests
    {
        private void CheckAttributeOccurrences(string key, string value, int expectedCount)
        {
            IDictionary<string, string> searchAttributes = new Dictionary<string, string>
            {
                { key, value }
            };
            FindTasksResult findTasksResult = Svc.FindTasks(string.Empty, searchAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(expectedCount, findTasksResult.NumberOfMatchingTasks);
        }

        [Test]
        public void CreateTask()
        {
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());
        }

        [Test]
        [ExpectedException(typeof(ProgrammingError))]
        public void CreateTaskAndDelete()
        {
            Task task = SaveTask(CreateTasksWithOneAttribute());
            Svc.Delete(task);
        }

        [Test]
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

        [Test]
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

        [Test]
        public void SelectTask_UnknownTaskType_CreatedState_NoAttributes()
        {
            CreateSomeTasksForSearching();
            FindTasksResult findTasksResult = Svc.FindTasks(@"Unknown", null, TaskStateEnum.CREATED);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(0, findTasksResult.NumberOfMatchingTasks);
            Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
        }

        [Test]
        public void SelectTask_TaskType_CreatedState_NoAttributes()
        {
            CreateSomeTasksForSearching();
            FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", null, TaskStateEnum.CREATED);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(3, findTasksResult.NumberOfMatchingTasks);
            Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
        }

        [Test]
        public void SelectTask_TaskType_CreatedAndInProgressState_NoAttributes()
        {
            CreateSomeTasksForSearching();
            FindTasksResult findTasksResult = Svc.FindTasks(@"/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded", null, TaskStateEnum.CREATED | TaskStateEnum.IN_PROGRESS);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(3, findTasksResult.NumberOfMatchingTasks);
            Assert.AreEqual(findTasksResult.NumberOfMatchingTasks, findTasksResult.Tasks.Count);
        }

        [Test]
        public void SelectTask_TaskType_CreatedState_TwoCorrectAttributes()
        {
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

        [Test]
        public void SelectTask_TaskType_CreatedState_ThreeCorrectAttributes()
        {
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

        [Test]
        public void SelectTask_TaskType_CreatedState_FourCorrectAttributes()
        {
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

        [Test]
        public void SelectTask_TaskType_NoState_ThreeCorrectAttributes()
        {
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

        [Test]
        public void UpdateTaskAttributes_ThreeCorrectAttributes_OneMatchingReplaceAttribute()
        {
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
            foreach (Task task in newFindTasksResult.Tasks)
            {
                Assert.AreEqual(task.Attributes["TypeName"], "/PensioB/Sempera/Affiliation");
                Assert.AreEqual(task.Attributes["RetirementPlanName"], "construo-test");
                Assert.AreEqual(task.Attributes["AffiliationID"], "285");
            }
        }

        [Test]
        public void UpdateTaskAttributes_DiffNoOfAttributes_All_OneNewValue()
        {
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());

            IDictionary<string, string> searchAttributes = new Dictionary<string, string>
            {
                { "key1", "value1" }
            };
            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" }
            };
            Svc.UpdateTaskAttributes(new string[0], searchAttributes, replaceAttributes);

            IDictionary<string, string> newSearchAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" }
            };
            FindTasksResult oldFindTasksResult = Svc.FindTasks(string.Empty, searchAttributes, null);
            FindTasksResult newFindTasksResult = Svc.FindTasks(string.Empty, newSearchAttributes, null);
            Assert.IsNotNull(oldFindTasksResult);
            Assert.AreEqual(0, oldFindTasksResult.NumberOfMatchingTasks);
            Assert.IsNotNull(newFindTasksResult);
            Assert.AreEqual(3, newFindTasksResult.NumberOfMatchingTasks);
        }

        [Test]
        public void UpdateTaskAttributes_DiffNoOfAttributes_All_TwoNewValues()
        {
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());

            IDictionary<string, string> searchAttributes = new Dictionary<string, string>
            {
                { "key1", "value1" },
                { "key2", "value2" }
            };
            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" },
                { "key2", "modifiedvalue2" }
            };
            Svc.UpdateTaskAttributes(new string[0], searchAttributes, replaceAttributes);

            CheckAttributeOccurrences("key1", "value1", 1);
            CheckAttributeOccurrences("key2", "value2", 0);
            CheckAttributeOccurrences("key3", "value3", 1);
            CheckAttributeOccurrences("key1", "modifiedvalue1", 2);
            CheckAttributeOccurrences("key2", "modifiedvalue2", 2);

            FindTasksResult findTasksResult = Svc.FindTasks((string)null, replaceAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(2, findTasksResult.NumberOfMatchingTasks);
        }

        [Test]
        public void UpdateTaskAttributes_OneTaskTypesMatchesOne_OneNewValue()
        {
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());

            IDictionary<string, string> searchAttributes = new Dictionary<string, string>
            {
                { "key1", "value1" }
            };
            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" }
            };
            Svc.UpdateTaskAttributes(new[] { "/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded/" }, searchAttributes, replaceAttributes);

            CheckAttributeOccurrences("key1", "value1", 2);
            CheckAttributeOccurrences("key2", "value2", 2);
            CheckAttributeOccurrences("key3", "value3", 1);
            CheckAttributeOccurrences("key1", "modifiedvalue1", 1);

            FindTasksResult findTasksResult = Svc.FindTasks("/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded/", replaceAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(1, findTasksResult.NumberOfMatchingTasks);
        }

        [Test]
        public void UpdateTaskAttributes_OneTaskTypesMatchesTwo_OneNewValue()
        {
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());

            IDictionary<string, string> searchAttributes = new Dictionary<string, string>
            {
                { "key1", "value1" }
            };
            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" }
            };
            Svc.UpdateTaskAttributes(new string[1] { "taskType/" }, searchAttributes, replaceAttributes);

            CheckAttributeOccurrences("key1", "value1", 1);
            CheckAttributeOccurrences("key2", "value2", 2);
            CheckAttributeOccurrences("key3", "value3", 1);
            CheckAttributeOccurrences("key1", "modifiedvalue1", 2);

            FindTasksResult findTasksResult = Svc.FindTasks("taskType/", replaceAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(2, findTasksResult.NumberOfMatchingTasks);
        }

        [Test]
        public void UpdateTaskAttributes_TwoTaskTypesMatchAll_OneNewValue()
        {
            SaveTask(CreateTasksWithOneAttribute());
            SaveTask(CreateTasksWithTwoAttributes());
            SaveTask(CreateTasksWithThreeAttributes());

            IDictionary<string, string> searchAttributes = new Dictionary<string, string>
            {
                { "key1", "value1" }
            };
            IDictionary<string, string> replaceAttributes = new Dictionary<string, string>
            {
                { "key1", "modifiedvalue1" }
            };
            Svc.UpdateTaskAttributes(new[] { "taskType/", "/PensioB/Sempera/Affiliation/ManualCapitalAcquiredVerificationNeeded/" }, searchAttributes, replaceAttributes);

            CheckAttributeOccurrences("key1", "value1", 0);
            CheckAttributeOccurrences("key2", "value2", 2);
            CheckAttributeOccurrences("key3", "value3", 1);
            CheckAttributeOccurrences("key1", "modifiedvalue1", 3);

            FindTasksResult findTasksResult = Svc.FindTasks("taskType/", replaceAttributes, null);
            Assert.IsNotNull(findTasksResult);
            Assert.AreEqual(2, findTasksResult.NumberOfMatchingTasks);
        }
    }

    // ReSharper restore InconsistentNaming
}