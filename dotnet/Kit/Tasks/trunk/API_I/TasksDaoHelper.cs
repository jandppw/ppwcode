#region Using

using System;
using System.Collections.Generic;
using System.Reflection;

using PPWCode.Vernacular.Persistence.I.Dao;
using PPWCode.Vernacular.Persistence.I.Dao.NHibernate;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    // TODO documentation
    public static class TasksDaoHelper
    {
        static TasksDaoHelper()
        {
            List<Assembly> assemblies = new List<Assembly>
            {
                typeof(TasksDaoHelper).Assembly,
                typeof(AuditLog).Assembly,
            };
            DaoHelper.RegisterAssembly(typeof(TasksDaoHelper), assemblies);
        }

        public static IEnumerable<Type> GetKnownTypes(ICustomAttributeProvider provider)
        {
            return DaoHelper.GetKnownTypes(typeof(TasksDaoHelper));
        }
    }
}