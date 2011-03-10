#region Using

using System.ServiceModel;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    public class ServiceHostingFactory
    {
        private const string TasksEndpointConfigurationName = "PPWCode.Kit.Tasks.Server.API_I.TasksDao";
        private static readonly object s_TaskChannelStaticLock = new object();
        private static ChannelFactory<ITasksDao> s_TaskChannelFactory;

        public static ITasksDao CreateTasksInstance()
        {
            lock (s_TaskChannelStaticLock)
            {
                if (s_TaskChannelFactory == null)
                {
                    s_TaskChannelFactory = new ChannelFactory<ITasksDao>(TasksEndpointConfigurationName);
                }
                return s_TaskChannelFactory.CreateChannel();
            }
        }
    }
}