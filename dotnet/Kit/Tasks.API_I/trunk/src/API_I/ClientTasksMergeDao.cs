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

using System.Diagnostics.Contracts;
using System.Security.Principal;

using PPWCode.Vernacular.Persistence.I.Dao;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// Convenience class for clients that use the remote TasksMerge API.
    /// Clients are advised to use this class or a subclass instaed
    /// of instances of type <see cref="ITasksMergeDao"/> directly.
    /// </summary>
    public class ClientTasksMergeDao : ClientDao
    {
        #region Constructors

        public ClientTasksMergeDao(ITasksMergeDao tasksMergeDao)
            : base(tasksMergeDao)
        {
            Contract.Requires(tasksMergeDao != null);
            Contract.Ensures(TasksMergeDao == tasksMergeDao);
            Contract.Ensures(WindowsIdentity == null);
        }

        public ClientTasksMergeDao(ITasksMergeDao tasksMergeDao, WindowsIdentity windowsIdentity)
            : base(tasksMergeDao, windowsIdentity)
        {
            Contract.Requires(tasksMergeDao != null);
            Contract.Requires(windowsIdentity == null
                              || (windowsIdentity.IsAuthenticated
                                  && (windowsIdentity.ImpersonationLevel == TokenImpersonationLevel.Impersonation
                                      || windowsIdentity.ImpersonationLevel == TokenImpersonationLevel.Delegation)));
            Contract.Ensures(TasksMergeDao == tasksMergeDao);
            Contract.Ensures(WindowsIdentity == windowsIdentity);
        }

        #endregion

        #region Properties

        [Pure]
        public ITasksMergeDao TasksMergeDao
        {
            get
            {
                CheckObjectAlreadyDisposed();

                return (ITasksMergeDao)Obj;
            }
        }

        #endregion

        #region Methods

        public void MergeTasksByReference(string oldReference, string newReference)
        {
            Contract.Requires(!string.IsNullOrEmpty(oldReference));
            Contract.Requires(!string.IsNullOrEmpty(newReference));

            CheckObjectAlreadyDisposed();
            if (WindowsIdentity != null)
            {
                using (WindowsIdentity.Impersonate())
                {
                    TasksMergeDao.MergeTasksByReference(oldReference, newReference);
                }
            }
            else
            {
                TasksMergeDao.MergeTasksByReference(oldReference, newReference);
            }
        }

        #endregion
    }
}