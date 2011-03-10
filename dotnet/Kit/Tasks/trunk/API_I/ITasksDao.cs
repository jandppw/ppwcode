#region Using

using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.ServiceModel;
using System.Transactions;

using PPWCode.Vernacular.Persistence.I;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Errors;

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
        /// with the exact given <paramref name="tasktype"/>,
        /// whose <see cref="Task.Reference"/> starts with <paramref name="reference"/>,
        /// and whose <see cref="Task.State"/> is one of the states included
        /// in <paramref name="taskState"/>.
        /// </summary>
        [OperationContract]
        FindTasksResult FindTasks(string tasktype, string reference, TaskStateEnum? taskState);
    }

    // ReSharper disable InconsistentNaming
    /// <exclude />
    [ContractClassFor(typeof(ITasksDao))]
    public abstract class ITasksDaoContract :
        ITasksDao
    {
        #region Implementation of ITasksDao

        public FindTasksResult FindTasks(string tasktype, string reference, TaskStateEnum? taskState)
        {
            Contract.Requires(Transaction.Current != null);
            Contract.Requires(Transaction.Current.TransactionInformation.DistributedIdentifier == Guid.Empty);
            Contract.Requires(!string.IsNullOrEmpty(reference));
            Contract.Ensures(Contract.Result<FindTasksResult>() != null);

            return default(FindTasksResult);
        }

        #endregion

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

        #region IDao Members

        public abstract bool IsOperational { get; }

        #endregion

        #region IDisposable Members

        public abstract void Dispose();

        #endregion
    }
}