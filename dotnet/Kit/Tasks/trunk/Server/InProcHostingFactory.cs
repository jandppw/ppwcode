#region Using

using PPWCode.Kit.Tasks.API_I;
using PPWCode.Kit.Tasks.Server.API_I;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Hosting;

#endregion

namespace PPWCode.Kit.Tasks.Server
{
    public class InProcHostingFactory
    {
        public static ITasksDao CreateTasksInstance()
        {
            return InProcFactory.CreateInstance<TasksDao, ITasksDao>();
        }
    }
}