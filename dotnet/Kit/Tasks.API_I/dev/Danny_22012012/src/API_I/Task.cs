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
using System.Runtime.Serialization;

using PPWCode.Util.OddsAndEnds.I.Security;
using PPWCode.Vernacular.Exceptions.I;
using PPWCode.Vernacular.Persistence.I;
using PPWCode.Vernacular.Persistence.I.Dao;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// The registration of a task, a &quot;to do&quot; for humans.
    /// </summary>
    /// <remarks>
    /// <para>Tasks are identified by client systems by
    /// a <see cref="TaskType"/> and a <see cref="Reference"/>,
    /// which can later be used to retrieve collections of
    /// <c>Tasks</c>. These values are free text, limited
    /// by good practice.</para>
    /// <para><c>Tasks</c> have a <see cref="State"/>. When
    /// a <c>Task</c> is marked <see cref="TaskStateEnum.IN_PROGRESS"/>,
    /// the back-end automatically stores
    /// <see cref="InProgressSince"/> and <see cref="InProgressBy"/>.
    /// When a <c>Task</c> is marked <see cref="TaskStateEnum.COMPLETED"/>
    /// the back-end automatically stores the
    /// <see cref="CompletedSince"/> and <see cref="CompletedBy"/>.
    /// These properties can not be changed
    /// by clients.</para>
    /// </remarks>
    [PPWSecurityAction(SecurityAction = SecurityActionFlag.RETRIEVE | SecurityActionFlag.CREATE | SecurityActionFlag.UPDATE, Role = Roles.User)]
    [PPWAuditLog(AuditLogAction = PPWAuditLogActionEnum.UPDATE)]
    [Serializable, DataContract(IsReference = true)]
    public class Task :
        AbstractAuditableVersionedPersistentObject
    {
        #region Constructors

        public Task()
        {
            // base contracts
            Contract.Ensures(!PersistenceId.HasValue);
            Contract.Ensures(!PersistenceVersion.HasValue);

            Contract.Ensures(!CreatedAt.HasValue);
            Contract.Ensures(CreatedBy == null);
            Contract.Ensures(!LastModifiedAt.HasValue);
            Contract.Ensures(LastModifiedBy == null);

            // this contract
            Contract.Ensures(!State.HasValue);
            Contract.Ensures(!InProgressSince.HasValue);
            Contract.Ensures(InProgressBy == null);
            Contract.Ensures(!CompletedSince.HasValue);
            Contract.Ensures(CompletedBy == null);
        }

        #endregion

        #region Properties

        [DataMember]
        private TaskStateEnum? m_State;

        /// <summary>
        /// <c>Tasks</c> are <see cref="TaskStateEnum.CREATED"/>,
        /// <see cref="TaskStateEnum.IN_PROGRESS"/> or
        /// <see cref="TaskStateEnum.COMPLETED"/>.
        /// </summary>
        public TaskStateEnum? State
        {
            get
            {
                return m_State;
            }
            set
            {
                if (value != m_State)
                {
                    m_State = value;
                    OnPropertyChanged("State");
                }
                if (m_State.HasValue)
                {
                    switch (m_State.Value)
                    {
                        case TaskStateEnum.CREATED:
                            break;
                        case TaskStateEnum.IN_PROGRESS:
                            InProgressSince = DateTime.Now;
                            InProgressBy = IdentityNameHelper.GetIdentityName();
                            break;
                        case TaskStateEnum.COMPLETED:
                            CompletedSince = DateTime.Now;
                            CompletedBy = IdentityNameHelper.GetIdentityName();
                            break;
                    }
                }
            }
        }

        [DataMember]
        private DateTime? m_InProgressSince;

        /// <summary>
        /// When did work on the <c>Task</c> start?
        /// </summary>
        /// <remarks>
        /// Filled out automatically by the back-end
        /// when the task is marked <see cref="TaskStateEnum.IN_PROGRESS"/>.
        /// </remarks>
        [PPWAuditLogPropertyIgnore]
        public DateTime? InProgressSince
        {
            get
            {
                return m_InProgressSince;
            }
            private set
            {
                if (value != m_InProgressSince)
                {
                    m_InProgressSince = value;
                    OnPropertyChanged("InProgressSince");
                }
            }
        }

        [DataMember]
        private string m_InProgressBy;

        /// <summary>
        /// Who started working on the <c>Task</c>?
        /// </summary>
        /// <remarks>
        /// Filled out automatically by the back-end
        /// when the task is marked <see cref="TaskStateEnum.IN_PROGRESS"/>
        /// with the user name received in the
        /// remote call over WCF.
        /// </remarks>
        [PPWAuditLogPropertyIgnore]
        public string InProgressBy
        {
            get
            {
                return m_InProgressBy;
            }
            private set
            {
                if (value != m_InProgressBy)
                {
                    m_InProgressBy = value;
                    OnPropertyChanged("InProgressBy");
                }
            }
        }

        [DataMember]
        private DateTime? m_CompletedSince;

        /// <summary>
        /// When was the <c>Task</c> completed?
        /// </summary>
        /// <remarks>
        /// Filled out automatically by the back-end
        /// when the task is marked <see cref="TaskStateEnum.COMPLETED"/>.
        /// </remarks>
        [PPWAuditLogPropertyIgnore]
        public DateTime? CompletedSince
        {
            get
            {
                return m_CompletedSince;
            }
            private set
            {
                if (value != m_CompletedSince)
                {
                    m_CompletedSince = value;
                    OnPropertyChanged("CompletedSince");
                }
            }
        }

        [DataMember]
        private string m_CompletedBy;

        /// <summary>
        /// Who completed the <c>Task</c>?
        /// </summary>
        /// <remarks>
        /// Filled out automatically by the back-end
        /// when the task is marked <see cref="TaskStateEnum.COMPLETED"/>,
        /// with the user name received in the
        /// remote call over WCF.
        /// </remarks>
        [PPWAuditLogPropertyIgnore]
        public string CompletedBy
        {
            get
            {
                return m_CompletedBy;
            }
            private set
            {
                if (value != m_CompletedBy)
                {
                    m_CompletedBy = value;
                    OnPropertyChanged("CompletedBy");
                }
            }
        }

        [DataMember]
        private string m_TaskType;

        /// <summary>
        /// A programmatic name defined by the client application,
        /// to distinguish different types of <c>Tasks</c>. Mandatory.
        /// </summary>
        /// <remarks>
        /// The <c>TaskType</c> can be combined with the <see cref="Reference"/>
        /// when retrieving <c>Tasks</c>.
        /// </remarks>
        [PPWAuditLogPropertyIgnore(AuditLogAction = PPWAuditLogActionEnum.ALL)]
        public string TaskType
        {
            get
            {
                return m_TaskType;
            }
            set
            {
                if (State != TaskStateEnum.CREATED)
                {
                    throw new ProgrammingError("TaskType is immutable when state is different from CREATED.");
                }
                if (value != m_TaskType)
                {
                    m_TaskType = value;
                    OnPropertyChanged("TaskType");
                }
            }
        }

        [DataMember]
        private IDictionary<string, string> m_Attributes;

        /// <summary>
        /// A set of attributes defined by the client application,
        /// stored from creation in the <c>Task</c>, to enable
        /// the client to later retrieve relevant collections
        /// of <c>Tasks</c>. Mandatory.
        /// </summary>
        /// <remarks>
        /// <para>This system is used by several different back-end
        /// systems. Client systems must make sure that
        /// these attributes do not collide with the usage of this
        /// </remarks>
        [PPWAuditLogPropertyIgnore(AuditLogAction = PPWAuditLogActionEnum.ALL)]
        public IDictionary<string, string> Attributes
        {
            get
            {
                return new Dictionary<string, string>(m_Attributes);
            }
            private set
            {
                if (value != m_Attributes)
                {
                    m_Attributes = value;
                    OnPropertyChanged("Attributes");
                }
            }
        }

        public void AddAttribute(string key, string value)
        {
            if (State != TaskStateEnum.CREATED)
            {
                throw new ProgrammingError("Attributes are immutable when state is different from CREATED.");
            }

            if (m_Attributes == null)
            {
                m_Attributes = new Dictionary<string, string>();
            }
            m_Attributes[key] = value;
        }

        public void AddAttribute(KeyValuePair<string, string> pair)
        {
            AddAttribute(pair.Key, pair.Value);
        }

        public void AddAttributes(IEnumerable<KeyValuePair<string, string>> pairs)
        {
            foreach (var pair in pairs)
            {
                AddAttribute(pair);
            }
        }

        public void RemoveAttribute(string key)
        {
            if (State != TaskStateEnum.CREATED)
            {
                throw new ProgrammingError("Attributes are immutable when state is different from CREATED.");
            }

            m_Attributes.Remove(key);
        }

        public void RemoveAttribute(KeyValuePair<string, string> pair)
        {
            if (State != TaskStateEnum.CREATED)
            {
                throw new ProgrammingError("Attributes are immutable when state is different from CREATED.");
            }

            m_Attributes.Remove(pair);
        }

        public void RemoveAttributes(IEnumerable<KeyValuePair<string, string>> pairs)
        {
            foreach (var pair in pairs)
            {
                RemoveAttribute(pair);
            }
        }

        #endregion

        #region Private Helper

        /// <summary>
        /// Since <see cref="State"/> is implemented as a bitfield,
        /// it is technically possibly to store multiple states in
        /// the field. In this class this is not allowed.
        /// </summary>
        /// <returns>
        /// <paramref name="taskState"/> is singular.
        /// </returns>
        [Pure]
        public static bool IsSingleTaskState(TaskStateEnum taskState)
        {
            int value = (int)taskState;
            return (value & (value - 1)) == 0;
        }

        #endregion

        #region Civilized

        /// <summary>
        /// A string that can be used, if you wish, as the message to signal that
        /// the property is mandatory, but was not filled out.
        /// </summary>
        public const string MandatoryMessage = "MANDATORY";

        public const string EnumFlagsSingleOption = "ENUM_FLAGS_SINGLE_OPTION";

        /// <summary>
        /// <c>Tasks</c> are civilized if they have a singular <see cref="State"/>
        /// (see <see cref="IsSingleTaskState"/>),
        /// a <see cref="TaskType"/> and a <see cref="Reference"/>.
        /// </summary>
        /// <returns></returns>
        [Pure]
        public override CompoundSemanticException WildExceptions()
        {
#if EXTRA_CONTRACTS
            Contract.Ensures(!State.HasValue ==
                             Contract.Result<CompoundSemanticException>().ContainsElement(new PropertyException(this, "State", ExceptionStrings.Mandatory, null)));
            Contract.Ensures(State.HasValue && !IsSingleTaskState(State.Value) ==
                             Contract.Result<CompoundSemanticException>().ContainsElement(new PropertyException(this, "State", ExceptionStrings.EnumFlagsSingleOption, null)));
            Contract.Ensures(string.IsNullOrEmpty(TaskType) ==
                             Contract.Result<CompoundSemanticException>().ContainsElement(new PropertyException(this, "TaskType", ExceptionStrings.Mandatory, null)));
            Contract.Ensures(string.IsNullOrEmpty(Reference) ==
                             Contract.Result<CompoundSemanticException>().ContainsElement(new PropertyException(this, "Reference", ExceptionStrings.Mandatory, null)));
#endif

            CompoundSemanticException cse = base.WildExceptions();

            if (!State.HasValue)
            {
                cse.AddElement(new PropertyException(this, "State", MandatoryMessage, null));
            }
            if (State.HasValue && !IsSingleTaskState(State.Value))
            {
                cse.AddElement(new PropertyException(this, "State", EnumFlagsSingleOption, null));
            }
            if (string.IsNullOrEmpty(TaskType))
            {
                cse.AddElement(new PropertyException(this, "TaskType", MandatoryMessage, null));
            }

            return cse;
        }

        #endregion
    }
}