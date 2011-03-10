#region Using

using PPWCode.Util.OddsAndEnds.I.Extensions;

using Spring.Context.Support;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    public static class TasksService
    {
        public static ClientTasksDao CreateTaskService()
        {
            ITasksDao tasksDao = ContextRegistry
                .GetContext()
                .GetObject<ITasksDao>("TasksFactory");

            return new ClientTasksDao(tasksDao);
        }
    }
}