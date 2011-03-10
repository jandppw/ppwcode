#region Using

using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Linq;
using System.ServiceModel;
using System.Transactions;

using Iesi.Collections.Generic;

using log4net;

using NHibernate;
using NHibernate.Criterion;

using PPWCode.Kit.Tasks.API_I;
using PPWCode.Vernacular.Persistence.I;
using PPWCode.Vernacular.Persistence.I.Dao;
using PPWCode.Vernacular.Persistence.I.Dao.NHibernate;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Errors;

#endregion

namespace PPWCode.Kit.Tasks.Server.API_I
{
    [NHibernateSerializationBehavior]
    [ErrorLogBehavior]
    [ServiceBehavior(
        InstanceContextMode = InstanceContextMode.PerCall,
        TransactionIsolationLevel = IsolationLevel.ReadCommitted,
        TransactionTimeout = "00:01:00",
        UseSynchronizationContext = false)]
    public class TasksDao :
        NHibernateWcfCrudDao,
        ITasksDao
    {
        #region Fields

        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(TasksDao));

        #endregion

        #region Gateway Table

        protected override Dictionary<FactoryKey, Func<IPersistentObject, IPersistentObject>> GetFactory()
        {
            return new Dictionary<FactoryKey, Func<IPersistentObject, IPersistentObject>>
            {
                // TAsks
                // create
                { new FactoryKey(typeof(Task), Operation.CREATE), CreateTask },
                //
                // update
                { new FactoryKey(typeof(Task), Operation.UPDATE), UpdateTask },
                //
                // delete
            };
        }

        #endregion

        #region Abstract method GetSessionFactory

        protected override ISessionFactory GetSessionFactory()
        {
            return NHibernateSessionTasksFactory.GetSessionFactory();
        }

        #endregion

        #region ITasksDao members

        [OperationBehavior(
            TransactionScopeRequired = true,
            TransactionAutoComplete = true)]
        public FindTasksResult FindTasks(string tasktype, string reference, TaskStateEnum? taskState)
        {
            const int MaximumResults = 50;
            const string MethodName = "FindTasks";

            CheckObjectAlreadyDisposed();

            ICollection<Task> result = new HashedSet<Task>();
            int numberOfMatchingTasks = -1;

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(
                    string.Format(
                        "{0}({1},{2},{3})",
                        MethodName,
                        tasktype ?? string.Empty,
                        reference,
                        taskState.HasValue
                            ? taskState.ToString()
                            : string.Empty));
            }

            try
            {
                ICriteria criteria = BuildFindTasksQuery(tasktype, reference, taskState, MaximumResults);
                result = criteria.List<Task>();
                if (result.Count < MaximumResults)
                {
                    numberOfMatchingTasks = result.Count;
                }
                else
                {
                    criteria = BuildCountTasksQuery(tasktype, reference, taskState);
                    numberOfMatchingTasks = criteria.UniqueResult<int>();
                }
            }
            catch (HibernateException he)
            {
                // Any hibernate exception is an error
                string message = string.Format(
                    "{0}({1},{2},{3})",
                    MethodName,
                    tasktype ?? string.Empty,
                    reference,
                    taskState.HasValue ?
                                           taskState.ToString()
                        : string.Empty);
                s_Logger.Fatal(message, he);
                StatelessCrudDao.TriageException(he, message);
            }

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(
                    string.Format(
                        "{0}({1},{2},{3}) result {4}",
                        MethodName,
                        tasktype ?? string.Empty,
                        reference,
                        taskState.HasValue
                            ? taskState.ToString()
                            : string.Empty, result));
            }

            ICollection<Task> allowedResult = result
                .Where(r => HasSufficientSecurity(r.GetType(), SecurityActionFlag.RETRIEVE))
                .ToList();
            return new FindTasksResult(allowedResult, numberOfMatchingTasks);
        }

        private ICriteria BuildBaseTasksQuery(string tasktype, string reference, TaskStateEnum? taskState)
        {
            if (!reference.EndsWith("%"))
            {
                reference = string.Concat(reference, "%");
            }
            if (!(string.IsNullOrEmpty(tasktype) || tasktype.EndsWith("/")))
            {
                tasktype = string.Concat(tasktype, "/");
            }
            ICriteria criteria = Session
                .CreateCriteria<Task>()
                .Add(Restrictions.Like("Reference", reference));
            if (!string.IsNullOrEmpty(tasktype))
            {
                criteria.Add(Restrictions.Eq("TaskType", tasktype));
            }
            if (taskState.HasValue)
            {
                int value = (int)taskState.Value;
                Junction disjunction = Restrictions.Disjunction();
                int state = 1;
                while (state <= value)
                {
                    if ((state & value) != 0)
                    {
                        disjunction.Add(Restrictions.Eq("State", (TaskStateEnum)state));
                    }
                    state <<= 1;
                }
                criteria.Add(disjunction);
            }
            return criteria;
        }

        private ICriteria BuildFindTasksQuery(string tasktype, string reference, TaskStateEnum? taskState, int maximumResults)
        {
            ICriteria criteria = BuildBaseTasksQuery(tasktype, reference, taskState);
            criteria.AddOrder(Order.Asc("CreatedAt"));
            criteria.SetMaxResults(maximumResults);
            return criteria;
        }

        private ICriteria BuildCountTasksQuery(string tasktype, string reference, TaskStateEnum? taskState)
        {
            ICriteria criteria = BuildBaseTasksQuery(tasktype, reference, taskState);
            criteria.SetProjection(Projections.RowCount());
            return criteria;
        }

        #endregion

        #region Base operations

        /// <summary>
        /// Make sure that a task' ref and tasktype end with '/'
        /// </summary>
        /// <param name="po"></param>
        private static void CheckTask(Task task)
        {
            if (task != null)
            {
                // Make sure that a tasktype always end with a /
                if (!task.TaskType.EndsWith("/"))
                {
                    task.TaskType += "/";
                }

                // Make sure that a ref always end with a /
                if (!task.Reference.EndsWith("/"))
                {
                    task.Reference += "/";
                }
            }
        }

        private IPersistentObject CreateTask(IPersistentObject po)
        {
            Contract.Requires(typeof(Task).IsAssignableFrom(po.GetType()));

            const string MethodName = "CreateTask";

            CheckObjectAlreadyDisposed();

            Task task = po as Task;

            CheckTask(task);

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(string.Format("{0}({1})", MethodName, task));
            }
            return BaseCreate(task);
        }

        private IPersistentObject UpdateTask(IPersistentObject po)
        {
            Contract.Requires(typeof(Task).IsAssignableFrom(po.GetType()));

            const string MethodName = "UpdateTask";

            CheckObjectAlreadyDisposed();

            Task task = po as Task;

            CheckTask(task);

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(string.Format("{0}({1})", MethodName, task));
            }
            return BaseUpdate(task);
        }

        #endregion
    }
}