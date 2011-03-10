#region Using

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Vernacular.Exceptions.I;

#endregion

namespace PPWCode.Kit.Tasks.API_I.RemoteTest
{
    [TestClass]
    public class TaskTests
    {
        #region Test Setup

        private ClientTasksDao Svc { get; set; }

        [TestInitialize]
        public void RetirementPlanTestInitialize()
        {
            Svc = RemoteTestHelper.CreateTaskService();
        }

        [TestCleanup]
        public void MyTestCleanup()
        {
            Svc.Dispose();
        }

        #endregion

        [TestMethod]
        public void CreateTask()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "taskType",
                Reference = "123",
            };
            task = Svc.Create(task);
            Assert.IsNotNull(task);
        }

        [TestMethod]
        [ExpectedException(typeof(ProgrammingError))]
        public void CreateTaskAndDelete()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "taskType",
                Reference = "123",
            };
            task = Svc.Create(task);
            Assert.IsNotNull(task);
            Svc.Delete(task);
        }

        [TestMethod]
        public void CreateAndUpdateTask1()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "taskType",
                Reference = "123",
            };
            task = Svc.Create(task);
            Assert.IsNotNull(task);
            Assert.AreEqual(task.State, TaskStateEnum.CREATED);
            Assert.AreEqual(task.TaskType, "taskType/");
            Assert.AreEqual(task.Reference, "123/");
            //
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
        public void CreateAndUpdateTask2()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "taskType",
                Reference = "123",
            };
            task = Svc.Create(task);
            Assert.IsNotNull(task);
            Assert.AreEqual(task.State, TaskStateEnum.CREATED);
            Assert.AreEqual(task.TaskType, "taskType/");
            Assert.AreEqual(task.Reference, "123/");
            //
            {
                task.State = TaskStateEnum.IN_PROGRESS;
                task = Svc.Update(task);
                Assert.IsNotNull(task);
                Assert.AreEqual(task.State, TaskStateEnum.IN_PROGRESS);
            }
            {
                task.TaskType = "taskType2";
                Svc.Update(task);
            }
        }

        [TestMethod]
        public void CreateAndUpdateAndSelectTask()
        {
            Task task = new Task
            {
                State = TaskStateEnum.CREATED,
                TaskType = "taskType",
                Reference = "123",
            };
            task = Svc.Create(task);
            Assert.IsNotNull(task);
            task.State = TaskStateEnum.IN_PROGRESS;
            task = Svc.Update(task);
            Assert.IsNotNull(task);
            Assert.AreEqual(task.State, TaskStateEnum.IN_PROGRESS);
            {
                FindTasksResult result = Svc.FindTasks(null, "1", null);
                Assert.IsNotNull(result);
                Assert.IsNotNull(result.Tasks);
                Assert.AreEqual(result.Tasks.Count, 1);
                Assert.AreEqual(result.NumberOfMatchingTasks, 1);
            }
            {
                FindTasksResult result = Svc.FindTasks("taskTyPe", "1", null);
                Assert.IsNotNull(result);
                Assert.IsNotNull(result.Tasks);
                Assert.AreEqual(result.Tasks.Count, 1);
                Assert.AreEqual(result.NumberOfMatchingTasks, 1);
            }
            {
                FindTasksResult result = Svc.FindTasks("taskTyPe", "1", TaskStateEnum.CREATED | TaskStateEnum.IN_PROGRESS | TaskStateEnum.COMPLETED);
                Assert.IsNotNull(result);
                Assert.IsNotNull(result.Tasks);
                Assert.AreEqual(result.Tasks.Count, 1);
                Assert.AreEqual(result.NumberOfMatchingTasks, 1);
            }
            {
                FindTasksResult result = Svc.FindTasks(null, "0", null);
                Assert.IsNotNull(result);
                Assert.IsNotNull(result.Tasks);
                Assert.AreEqual(result.Tasks.Count, 0);
                Assert.AreEqual(result.NumberOfMatchingTasks, 0);
            }
        }
    }
}