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
using System.Security.Principal;

using PPWCode.Vernacular.Persistence.I.Dao;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// Convenience class for clients that use the remote TasksMerge API.
    /// Clients are advised to use this class or a subclass instaed
    /// of instances of type <see cref="ITasks"/> directly.
    /// </summary>
    public class ClientTasks : ClientDao
    {
        #region Constructors

        public ClientTasks(ITasks tasks)
            : base(tasks)
        {
            Contract.Requires(tasks != null);
            Contract.Ensures(Tasks == tasks);
            Contract.Ensures(WindowsIdentity == null);
        }

        public ClientTasks(ITasks tasks, WindowsIdentity windowsIdentity)
            : base(tasks, windowsIdentity)
        {
            Contract.Requires(tasks != null);
            Contract.Requires(windowsIdentity == null
                              || (windowsIdentity.IsAuthenticated
                                  && (windowsIdentity.ImpersonationLevel == TokenImpersonationLevel.Impersonation
                                      || windowsIdentity.ImpersonationLevel == TokenImpersonationLevel.Delegation)));
            Contract.Ensures(Tasks == tasks);
            Contract.Ensures(WindowsIdentity == windowsIdentity);
        }

        #endregion

        #region Properties

        [Pure]
        public ITasks Tasks
        {
            get
            {
                CheckObjectAlreadyDisposed();

                return (ITasks)Obj;
            }
        }

        #endregion

        #region Methods

        /// <inheritdoc cref="ITasks.FindTasks"/>
        public FindTasksResult FindTasks(string taskType, IDictionary<string,string> searchAttributes, TaskStateEnum? taskState)
        {
            Contract.Requires(searchAttributes != null);
            Contract.Requires(searchAttributes.Count > 0);
            Contract.Ensures(Contract.Result<FindTasksResult>() != null);

            CheckObjectAlreadyDisposed();
            if (WindowsIdentity != null)
            {
                using (WindowsIdentity.Impersonate())
                {
                    return Tasks.FindTasks(taskType, searchAttributes, taskState);
                }
            }
            return Tasks.FindTasks(taskType, searchAttributes, taskState);
        }

        public void MergeTasksByReference(string oldReference, string newReference)
        {
            Contract.Requires(!string.IsNullOrEmpty(oldReference));
            Contract.Requires(!string.IsNullOrEmpty(newReference));

            CheckObjectAlreadyDisposed();
            if (WindowsIdentity != null)
            {
                using (WindowsIdentity.Impersonate())
                {
                    Tasks.MergeTasksByReference(oldReference, newReference);
                }
            }
            else
            {
                Tasks.MergeTasksByReference(oldReference, newReference);
            }
        }

        #endregion
    }
}