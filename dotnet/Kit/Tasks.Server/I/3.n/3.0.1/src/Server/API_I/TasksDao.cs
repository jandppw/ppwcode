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
using System.Diagnostics.Contracts;
using System.Linq;
using System.Reflection;
using System.ServiceModel;
using System.Text;
using System.Transactions;

using NHibernate;

using PPWCode.Kit.Tasks.API_I;
using PPWCode.Vernacular.Persistence.I;
using PPWCode.Vernacular.Persistence.I.Dao;
using PPWCode.Vernacular.Persistence.I.Dao.NHibernate;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Errors;

using log4net;

#endregion

namespace PPWCode.Kit.Tasks.Server.API_I
{
    [NHibernateContext(SessionFactory = @"NHibernateSessionTasksFactory")]
    [NHibernateSerializationServiceBehavior]
    [ErrorLogBehavior]
    [ServiceBehavior(
        InstanceContextMode = InstanceContextMode.PerCall,
        TransactionIsolationLevel = IsolationLevel.ReadCommitted,
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
                { new FactoryKey(typeof(Task), Operation.CREATE), CreateTask },
                { new FactoryKey(typeof(Task), Operation.UPDATE), UpdateTask },
            };
        }

        #endregion

        #region ITasksDao members

        [OperationBehavior(TransactionScopeRequired = true, TransactionAutoComplete = true)]
        public FindTasksResult FindTasks(IEnumerable<string> taskTypes, IDictionary<string, string> searchAttributes, TaskStateEnum? taskState)
        {
            const int MaximumResults = 50;
            const string MethodName = "FindTasks";

            CheckObjectAlreadyDisposed();

            int numberOfMatchingTasks;

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.DebugFormat(
                    "{0}({1},{2},{3})",
                    MethodName,
                    string.Join(@",", taskTypes.ToArray()),
                    searchAttributes,
                    taskState != null
                        ? taskState.ToString()
                        : string.Empty);
            }

            // coalesce if nescessary
            searchAttributes = searchAttributes ?? new Dictionary<string, string>();
            taskTypes = taskTypes ?? Enumerable.Empty<string>();
            IList<Task> result;
            try
            {
                IQuery query = BuildFindTasksQuery(taskTypes, searchAttributes, taskState, MaximumResults);
                result = query.List<Task>();
                if (result.Count < MaximumResults)
                {
                    numberOfMatchingTasks = result.Count;
                }
                else
                {
                    query = BuildCountTasksQuery(taskTypes, searchAttributes, taskState);
                    numberOfMatchingTasks = (int)query.UniqueResult<long>();
                }
            }
            catch (HibernateException he)
            {
                // Any hibernate exception is an error
                string message = string.Format(
                    "{0}({1},{2},{3})",
                    MethodName,
                    string.Join(@",", taskTypes.ToArray()),
                    searchAttributes,
                    taskState != null
                        ? taskState.ToString()
                        : string.Empty);
                s_Logger.Fatal(message, he);
                StatelessCrudDao.TriageException(he, message);
                // Line needed to keep resharper happy :) 
                // triageException should give an exception back instead of throwing it already
                throw new Exception();
            }

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.DebugFormat(
                    "{0}({1},{2},{3}) result {4}",
                    MethodName,
                    string.Join(@",", taskTypes.ToArray()),
                    searchAttributes,
                    taskState.HasValue
                        ? taskState.ToString()
                        : string.Empty, result);
            }

            ICollection<Task> allowedResult = result
                .Where(r => HasSufficientSecurity(r.GetType(), SecurityActionFlag.RETRIEVE))
                .ToArray();
            return new FindTasksResult(allowedResult, numberOfMatchingTasks);
        }

        [OperationBehavior(TransactionScopeRequired = true, TransactionAutoComplete = true)]
        public void UpdateTaskAttributes(IEnumerable<string> taskTypes, IDictionary<string, string> searchAttributes, IDictionary<string, string> replaceAttributes)
        {
            const string MethodName = "UpdateTaskAttributes";

            CheckObjectAlreadyDisposed();

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(
                    string.Format(
                        "{0}({1},{2},{3})",
                        MethodName,
                        string.Join(@",", taskTypes.ToArray()),
                        searchAttributes,
                        replaceAttributes));
            }

            // coalesce if nescessary
            searchAttributes = searchAttributes ?? new Dictionary<string, string>();
            taskTypes = taskTypes ?? Enumerable.Empty<string>();
            try
            {
                IQuery query = BuildFindTasksQuery(taskTypes, searchAttributes, null, null);
                IList<Task> tasks = query.List<Task>();
                foreach (Task task in tasks)
                {
                    //check that all keys exist, skip task if not so
                    if (replaceAttributes.Keys.All(k => task.Attributes.ContainsKey(k)))
                    {
                        //replace the values
                        foreach (string key in replaceAttributes.Keys)
                        {
                            task.AddAttribute(key, replaceAttributes[key]);
                        }
                    }
                    else
                    {
                        // log info
                        s_Logger.InfoFormat(
                            "Skipped task because at least one replacement attribute key could not be found Task=({0},{1},{2},{3})",
                            task,
                            string.Join(@",", taskTypes.ToArray()),
                            searchAttributes,
                            replaceAttributes);
                    }
                }
            }
            catch (HibernateException he)
            {
                // Any hibernate exception is an error
                string message = string.Format(
                    "{0}({1},{2},{3})",
                    MethodName,
                    string.Join(@",", taskTypes.ToArray()),
                    searchAttributes,
                    replaceAttributes);
                s_Logger.Fatal(message, he);
                StatelessCrudDao.TriageException(he, message);
                // Line needed to keep resharper happy :) 
                // triageException should give an exception back instead of throwing it already
                throw new Exception();
            }

            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(
                    string.Format(
                        "{0}({1},{2},{3}) ended succussfully",
                        MethodName,
                        string.Join(@",", taskTypes.ToArray()),
                        searchAttributes,
                        replaceAttributes));
            }
        }

        private static TaskQuery BuildBaseTasksQuery(
            IEnumerable<string> taskTypes,
            IEnumerable<KeyValuePair<string, string>> searchAttributes,
            TaskStateEnum? taskState,
            string optionalOrderbyClause)
        {
            Contract.Requires(searchAttributes != null);
            Contract.Ensures(Contract.Result<TaskQuery>() != null);

            // Start with base query
            StringBuilder queryString = new StringBuilder(@"from Task t", 2048);
            List<string> wherePredicates = new List<string>();
            IDictionary<string, object> parameters = new Dictionary<string, object>();

            // Handle searchAttributes
            {
                int i = 0;
                foreach (KeyValuePair<string, string> pair in searchAttributes)
                {
                    queryString.AppendFormat(@" join t.Attributes a{0}", i);
                    wherePredicates.Add(string.Format(@"index(a{0}) = :AttributeName{0} and a{0} = :AttributeValue{0}", i));
                    parameters.Add(@"AttributeName" + i, pair.Key);
                    parameters.Add(@"AttributeValue" + i, pair.Value);
                    i++;
                }
            }

            // Handle taskTypes
            {
                StringBuilder orTasks = new StringBuilder(512);
                int i = 0;
                foreach (string taskType in taskTypes.Where(taskType => !string.IsNullOrEmpty(taskType)))
                {
                    orTasks.Append(i == 0 ? "(" : " or ");
                    orTasks.Append(@"t.TaskType = :TaskType" + i);
                    parameters.Add(@"TaskType" + i, taskType.EndsWith("/") ? taskType : string.Concat(taskType, "/"));
                    i++;
                }
                if (i > 0)
                {
                    orTasks.Append(')');
                    wherePredicates.Add(orTasks.ToString());
                }
            }

            // Handle taskState
            if (taskState != null)
            {
                int value = (int)taskState.Value;
                StringBuilder taskStatePredicate = new StringBuilder(64);
                bool firstStatePredicate = true;
                int state = 1;
                int i = 0;
                while (state <= value)
                {
                    if ((state & value) != 0)
                    {
                        if (firstStatePredicate)
                        {
                            taskStatePredicate.AppendFormat(@"(t.State = :State{0}", i);
                            firstStatePredicate = false;
                        }
                        else
                        {
                            taskStatePredicate.AppendFormat(@" or t.State = :State{0}", i);
                        }
                        parameters.Add(@"State" + i, (TaskStateEnum)state);
                    }
                    state <<= 1;
                    i++;
                }
                if (!firstStatePredicate)
                {
                    taskStatePredicate.Append(')');
                    wherePredicates.Add(taskStatePredicate.ToString());
                }
            }

            // join query with where predicates
            bool firstPredicate = true;
            foreach (string predicate in wherePredicates)
            {
                if (firstPredicate)
                {
                    queryString.AppendFormat(@" where {0}", predicate);
                    firstPredicate = false;
                }
                else
                {
                    queryString.AppendFormat(@" and {0}", predicate);
                }
            }

            // Append 'order by' clause if specified
            if (!string.IsNullOrEmpty(optionalOrderbyClause))
            {
                queryString.AppendFormat(@" order by {0}", optionalOrderbyClause);
            }

            return new TaskQuery(queryString.ToString(), parameters);
        }

        private IQuery BuildFindTasksQuery(
            IEnumerable<string> taskTypes,
            IEnumerable<KeyValuePair<string, string>> searchAttributes,
            TaskStateEnum? taskState,
            int? maximumResults)
        {
            Contract.Requires(searchAttributes != null);
            Contract.Ensures(Contract.Result<IQuery>() != null);

            TaskQuery taskQuery = BuildBaseTasksQuery(taskTypes, searchAttributes, taskState, @"t.CreatedAt asc");
            IQuery query = Session.CreateQuery(taskQuery.QueryString);
            foreach (KeyValuePair<string, object> parameter in taskQuery.Parameters)
            {
                query.SetParameter(parameter.Key, parameter.Value);
            }
            if (maximumResults != null)
            {
                query.SetMaxResults(maximumResults.Value);
            }
            return query;
        }

        private IQuery BuildCountTasksQuery(
            IEnumerable<string> taskTypes,
            IEnumerable<KeyValuePair<string, string>> searchAttributes,
            TaskStateEnum? taskState)
        {
            Contract.Requires(searchAttributes != null);
            Contract.Ensures(Contract.Result<IQuery>() != null);

            TaskQuery taskQuery = BuildBaseTasksQuery(taskTypes, searchAttributes, taskState, null);
            IQuery query = Session.CreateQuery(@"select count(*) " + taskQuery.QueryString);
            foreach (KeyValuePair<string, object> parameter in taskQuery.Parameters)
            {
                query.SetParameter(parameter.Key, parameter.Value);
            }
            return query;
        }

        #endregion

        #region Base operations

        /// <summary>
        /// Make sure that a task' ref and tasktype end with '/'
        /// </summary>
        /// <param name="po"></param>
        private static void CheckTask(Task task)
        {
            // Make sure that a tasktype always end with a /
            if (task != null && !task.TaskType.EndsWith("/"))
            {
                task.TaskType += "/";
            }
        }

        private IPersistentObject CreateTask(IPersistentObject po)
        {
            Contract.Requires(po != null);
            Contract.Requires(po is Task);
            Contract.Ensures(Contract.Result<IPersistentObject>() != null);

            CheckObjectAlreadyDisposed();
            Task task = po as Task;
            CheckTask(task);
            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(string.Format("{0}({1})", MethodBase.GetCurrentMethod().Name, task));
            }
            return BaseCreate(task);
        }

        private IPersistentObject UpdateTask(IPersistentObject po)
        {
            Contract.Requires(po != null);
            Contract.Requires(po is Task);
            Contract.Ensures(Contract.Result<IPersistentObject>() != null);

            CheckObjectAlreadyDisposed();
            Task task = po as Task;
            CheckTask(task);
            if (s_Logger.IsDebugEnabled)
            {
                s_Logger.Debug(string.Format("{0}({1})", MethodBase.GetCurrentMethod().Name, task));
            }
            return BaseUpdate(task);
        }

        #endregion
    }
}