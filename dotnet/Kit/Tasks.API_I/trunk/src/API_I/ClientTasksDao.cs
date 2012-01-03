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
using System.Security.Principal;

using PPWCode.Vernacular.Persistence.I.Dao;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// Convenience class for clients that use the remote Tasks API.
    /// Clients are advised to use a subclass of this class instead
    /// of instances of type <see cref="ITasksDao"/> directly.
    /// </summary>
    /// <remarks>
    /// <para>The remote Actua API, expressed in <see cref="ITasksDao"/>
    /// offers all functionality needed to work with the Tasks server.
    /// Instances of this class wrap around an instance of <see cref="ITasksDao"/>
    /// to offer that functionality in even easier ways to client users.
    /// This obviously only contains code that is applicable to all or most
    /// clients. (Actually, this class would better be put in a separate
    /// assembly, away from the API assembly, since it is irrelevant to the server,
    /// but that would be overdoing it a bit).</para>
    /// <para>Clients are advised not to use this class directly, but to create
    /// a subclass, where client specific helper methods for communication with
    /// the Tasks back-end can be added.</para>
    /// <para>Contracts of these methods do not define ensures clauses
    /// formally completely. A part is only described in comments.
    /// In most cases, execution of the ensures clauses would mean another call to the server,
    /// and that is not workable.</para>
    /// </remarks>
    public class ClientTasksDao :
        ClientCrudDao
    {
        #region Invariant

        [ContractInvariantMethod]
        // ReSharper disable UnusedMember.Local
        private void ObjectInvariant()
        {
            Contract.Invariant(TasksDao != null);
        }

        // ReSharper restore UnusedMember.Local

        #endregion

        #region Constructors

        public ClientTasksDao(ITasksDao tasksDao)
            : base(tasksDao)
        {
            Contract.Requires(tasksDao != null);
            Contract.Ensures(TasksDao == tasksDao);
            Contract.Ensures(WindowsIdentity == null);
        }

        public ClientTasksDao(ITasksDao tasksDao, WindowsIdentity windowsIdentity)
            : base(tasksDao, windowsIdentity)
        {
            Contract.Requires(tasksDao != null);
            Contract.Requires(windowsIdentity == null
                              || (windowsIdentity.IsAuthenticated
                                  && (windowsIdentity.ImpersonationLevel == TokenImpersonationLevel.Impersonation
                                      || windowsIdentity.ImpersonationLevel == TokenImpersonationLevel.Delegation)));
            Contract.Ensures(TasksDao == tasksDao);
            Contract.Ensures(WindowsIdentity == windowsIdentity);
        }

        #endregion

        #region Properties

        [Pure]
        public ITasksDao TasksDao
        {
            get
            {
                CheckObjectAlreadyDisposed();

                return (ITasksDao)Obj;
            }
        }

        #endregion

        #region Methods

        /// <inheritdoc cref="ITasksDao.FindTasks"/>
        [Obsolete("FindTasks method is moved to ClientTasks")]
        public FindTasksResult FindTasks(string taskType, string reference, TaskStateEnum? taskState)
        {
            Contract.Requires(!string.IsNullOrEmpty(reference));
            Contract.Ensures(Contract.Result<FindTasksResult>() != null);

            CheckObjectAlreadyDisposed();
            if (WindowsIdentity != null)
            {
                using (WindowsIdentity.Impersonate())
                {
                    return TasksDao.FindTasks(taskType, reference, taskState);
                }
            }
            return TasksDao.FindTasks(taskType, reference, taskState);
        }

        #endregion
    }
}