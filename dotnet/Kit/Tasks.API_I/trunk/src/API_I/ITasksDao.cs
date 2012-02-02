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
using System.Diagnostics.Contracts;
using System.ServiceModel;
using System.Transactions;

using PPWCode.Vernacular.Persistence.I;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Errors;

using System.Linq;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// The main remote method API of <c>Tasks</c>.
    /// </summary>
    /// <remarks>
    /// <para>Apart from the CRUD methods, there is only
    /// 1 extra method, to retrieve <c>Tasks</c>.</para>
    /// </remarks>
    [ContractClass(typeof(ITasksDaoContract))]
    [ServiceContract(Namespace = "http://PPWCode.Kit.Tasks/I/TasksDao")]
    [ServiceKnownType("GetKnownTypes", typeof(TasksDaoHelper))]
    [ExceptionMarshallingBehavior]
    public interface ITasksDao :
        IWcfCrudDao
    {
        /// <summary>
        /// Return a <see cref="Task">collection of <c>Tasks</c></see>,
        /// of any of the given <paramref name="taskTypes"/> (or any task if left empty),
        /// whose <see cref="Task.Reference"/> starts with <paramref name="reference"/>,
        /// and whose <see cref="Task.State"/> is one of the states included
        /// in <paramref name="taskState"/>.
        /// </summary>
        [OperationContract]
        [TransactionFlow(TransactionFlowOption.Allowed)]
        FindTasksResult FindTasks(IEnumerable<string> taskTypes, IDictionary<string, string> searchAttributes, TaskStateEnum? taskState);

        /// <summary>
        /// Searches for <c>Tasks</c> of any of the the given <paramref name="taskTypes"/>,
        /// with all of the given attributes matching <paramref name="searchAttributes"/>,
        /// and for all Tasks found, replaces all the given <paramref name="replaceAttributes">.
        /// </summary>
        [OperationContract]
        [TransactionFlow(TransactionFlowOption.Allowed)]
        void UpdateTaskAttributes(IEnumerable<string> taskTypes, IDictionary<string, string> searchAttributes, IDictionary<string, string> replaceAttributes);
    }

    // ReSharper disable InconsistentNaming
    /// <exclude />
    [ContractClassFor(typeof(ITasksDao))]
    public abstract class ITasksDaoContract :
        ITasksDao
    {
        #region Implementation of ITasksDao

        public FindTasksResult FindTasks(IEnumerable<string> taskTypes, IDictionary<string, string> searchAttributes, TaskStateEnum? taskState)
        {
            Contract.Requires(Transaction.Current != null);
            Contract.Requires(taskTypes != null);
            Contract.Ensures(Contract.Result<FindTasksResult>() != null);

            return default(FindTasksResult);
        }

        public void UpdateTaskAttributes(IEnumerable<string> taskTypes, IDictionary<string, string> searchAttributes, IDictionary<string, string> replaceAttributes)
        {
            Contract.Requires(Transaction.Current != null);
            Contract.Requires(taskTypes != null);
            Contract.Requires(replaceAttributes != null);
            Contract.Requires(replaceAttributes.Any());
        }

        #endregion

        #region Implementation of IWcfCrudDao

        public abstract void FlushAllCaches();
        public abstract IPersistentObject DeleteById(string PersistentObjectType, long? Id);

        public abstract IPersistentObject Retrieve(string PersistentObjectType, long? Id);

        public abstract ICollection<IPersistentObject> RetrieveAll(string PersistentObjectType);

        public abstract IPersistentObject Create(IPersistentObject po);

        public abstract IPersistentObject Update(IPersistentObject po);

        public abstract ICollection<IPersistentObject> UpdateAll(ICollection<IPersistentObject> col);

        public abstract IPersistentObject Delete(IPersistentObject po);

        public abstract object GetPropertyValue(IPersistentObject po, string PropertyName);

        public abstract ICollection<IPersistentObject> GetChildren(IPersistentObject po, string PropertyName);

        #endregion

        #region ITasksDao Members

        public abstract bool IsOperational { get; }

        public abstract void Dispose();

        #endregion
    }
}