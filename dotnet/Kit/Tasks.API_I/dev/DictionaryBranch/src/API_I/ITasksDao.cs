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
    [ServiceContract(Namespace = "http://PPWCode.Kit.Tasks/I/TasksDao")]
    [ServiceKnownType("GetKnownTypes", typeof(TasksDaoHelper))]
    [ExceptionMarshallingBehavior]
    public interface ITasksDao :
        IWcfCrudDao
    {
    }
}