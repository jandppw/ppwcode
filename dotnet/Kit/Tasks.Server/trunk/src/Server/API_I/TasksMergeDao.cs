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
using System.Security.Permissions;
using System.ServiceModel;
using System.Transactions;

using PPWCode.Kit.Tasks.API_I;
using PPWCode.Vernacular.Persistence.I.Dao.NHibernate;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Errors;

using log4net;

#endregion

namespace PPWCode.Kit.Tasks.Server.API_I
{
    [NHibernateContext(SessionFactory = @"NHibernateSessionTasksFactory")]
    [ErrorLogBehavior]
    [ServiceBehavior(
        InstanceContextMode = InstanceContextMode.PerCall,
        TransactionIsolationLevel = IsolationLevel.ReadCommitted,
        UseSynchronizationContext = false)]
    public class TasksMergeDao : ITasksMergeDao
    {
        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(TasksMergeDao));

        #region Properties

        private readonly TasksDao m_TasksDao = new TasksDao();

        private TasksDao TasksDao
        {
            get
            {
                return m_TasksDao;
            }
        }

        #endregion 

        #region Implementation of ITasksMergeDao

        [OperationBehavior(TransactionScopeRequired = true, TransactionAutoComplete = true)]
        [PrincipalPermission(SecurityAction.Demand, Role = Roles.User)]
        public void MergeTasksByReference(string oldReference, string newReference)
        {
            CheckObjectAlreadyDisposed();

            // MUDO: add implementation MergeTasksByReference

            // find all Tasks for which the reference starts with oldReference
            // update those Tasks by replacing the "oldReference" part of the reference
            // with newReference

            // this must be done inside one big transaction, because the rename has to be considered
            // one atomic action... either it succeeds completely, or either the whole rename is rolled back
        }

        #endregion

        #region Implementation of IDao

        public bool IsOperational
        {
            get
            {
                return TasksDao.IsOperational; 
            }
        }

        #endregion

        #region Implementation of IDisposable

        ~TasksMergeDao()
        {
            Cleanup();
        }

        private readonly object m_Locker = new object();
        private bool m_Disposed;

        private bool Disposed
        {
            get
            {
                lock (m_Locker)
                {
                    return m_Disposed;
                }
            }
        }

        public void Dispose()
        {
            lock (m_Locker)
            {
                if (!m_Disposed)
                {
                    try
                    {
                        Cleanup();
                    }
                    // ReSharper disable EmptyGeneralCatchClause
                    catch (Exception)
                    {
                        //NOP
                    }
                    // ReSharper restore EmptyGeneralCatchClause

                    m_Disposed = true;
                    GC.SuppressFinalize(this);
                }
            }
        }

        private void Cleanup()
        {
            s_Logger.Debug("Disposing TasksMergeDao");
            try
            {
                if (TasksDao != null)
                {
                    TasksDao.Dispose();
                }
            }
            catch (Exception e)
            {
                s_Logger.Error(@"Cleanup TasksDao in TasksMergeDao", e);
            }
        }

        private void CheckObjectAlreadyDisposed()
        {
            if (Disposed)
            {
                throw new ObjectDisposedException(GetType().FullName);
            }
        }

        #endregion
    }
}