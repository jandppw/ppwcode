using System.Collections.Generic;
using System.Reflection;

using NHibernate;

using PPWCode.Kit.Tasks.API_I;
using PPWCode.Vernacular.Persistence.I.Dao.NHibernate;

namespace PPWCode.Kit.Tasks.Server.API_I
{
    public static class NHibernateSessionTasksFactory
    {
        private const string TasksConnectionString = "TasksConnectionString";

        static NHibernateSessionTasksFactory()
        {
            List<Assembly> assemblies = new List<Assembly>
            {
                typeof(ClientTasksDao).Assembly,
            };
            NHibernateSessionFactory.CreateSessionFactory(TasksConnectionString, null, new HashSet<Assembly>(assemblies));
        }

        public static ISessionFactory GetSessionFactory()
        {
            return NHibernateSessionFactory.GetSessionFactory(TasksConnectionString);
        }
    }
}
