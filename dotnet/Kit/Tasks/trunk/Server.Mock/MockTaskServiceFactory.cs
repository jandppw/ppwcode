#region Using

using PPWCode.Kit.Tasks.API_I;
using PPWCode.Kit.Tasks.Server.Mock.API_I;

#endregion

namespace PPWCode.Kit.Tasks.Server.Mock
{
    public class MockTaskServiceFactory
    {
        public static ITasksDao CreateTasksInstance()
        {
            return new TasksDaoMock();
        }
    }
}