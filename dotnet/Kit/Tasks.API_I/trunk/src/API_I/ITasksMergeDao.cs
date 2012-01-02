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
using System.Diagnostics.Contracts;
using System.ServiceModel;
using System.Transactions;

using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Errors;
using PPWCode.Vernacular.Persistence.I.Dao;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// The remote method API of Tasks for anything
    /// that is related to merging <c>Tasks</c>.
    /// </summary>
    [ContractClass(typeof(ITasksMergeDaoContract))]
    [ServiceContract(Namespace = "http://PPWCode.Kit.Tasks/I/TasksMergeDao")]
    [ServiceKnownType("GetKnownTypes", typeof(TasksDaoHelper))]
    [ExceptionMarshallingBehavior]
    public interface ITasksMergeDao : IDao
    {
        [OperationContract]
        [TransactionFlow(TransactionFlowOption.Allowed)]
        void MergeTasksByReference(string oldReference, string newReference);
    }

    /// <exclude />
    [ContractClassFor(typeof(ITasksMergeDao))]
    // ReSharper disable InconsistentNaming
    public abstract class ITasksMergeDaoContract :
        // ReSharper restore InconsistentNaming
        ITasksMergeDao
    {
        #region Implementation of ITasksMergeDao

        public void MergeTasksByReference(string oldReference, string newReference)
        {
            Contract.Requires(Transaction.Current != null);
            Contract.Requires(!string.IsNullOrEmpty(oldReference));
            Contract.Requires(!string.IsNullOrEmpty(newReference));
            Contract.Requires(IsOperational);
        }

        #endregion

        #region Implementation of IDao

        public bool IsOperational
        {
            get
            {
                throw new NotImplementedException();
            }
        }

        #endregion

        #region Implementation of IDisposable

        public void Dispose()
        {
        }

        #endregion
    }
}