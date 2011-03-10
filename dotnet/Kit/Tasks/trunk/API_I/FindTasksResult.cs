#region Using

using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Runtime.Serialization;

using PPWCode.Util.OddsAndEnds.I.Extensions;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// When finding tasks, only a limited number of results
    /// are returned (to protect from saturating the network,
    /// and to guarantee client responsiveness).
    /// For a good interface, the <see cref="ITasksDao.FindTasks"/> method returns
    /// the found results, limited, and the number of tasks
    /// that match the criterion.
    /// </summary>
    [Serializable, DataContract(IsReference = true)]
    public class FindTasksResult
    {
        #region Invariant

        [ContractInvariantMethod]
        // ReSharper disable UnusedMember.Local
        private void ObjectInvariant()
            // ReSharper restore UnusedMember.Local
        {
            Contract.Invariant(Tasks != null);
            Contract.Invariant(NumberOfMatchingTasks >= 0);
            // ReSharper disable PossibleNullReferenceException
            Contract.Invariant(NumberOfMatchingTasks >= Tasks.Count);
            // ReSharper restore PossibleNullReferenceException
        }

        #endregion

        #region Constructor

        public FindTasksResult(ICollection<Task> tasks, int numberOfMatchingTasks)
        {
            Contract.Requires(tasks != null);
            Contract.Requires(numberOfMatchingTasks >= 0);
            Contract.Requires(numberOfMatchingTasks >= tasks.Count);
            Contract.Ensures(Tasks.SetEqual(tasks));
            Contract.Ensures(NumberOfMatchingTasks == numberOfMatchingTasks);

            m_Tasks.AddRange(tasks);
            m_NumberOfMatchingTasks = numberOfMatchingTasks;
        }

        #endregion

        #region Properties

        [DataMember]
        private readonly List<Task> m_Tasks = new List<Task>();

        public ICollection<Task> Tasks
        {
            get
            {
                return new List<Task>(m_Tasks);
            }
        }

        [DataMember]
        private readonly int m_NumberOfMatchingTasks;

        public int NumberOfMatchingTasks
        {
            get
            {
                return m_NumberOfMatchingTasks;
            }
        }

        public bool IsAllMatchingTasks()
        {
            Contract.Ensures(Contract.Result<bool>() == (Tasks.Count == NumberOfMatchingTasks));

            int numberOfResults = m_Tasks.Count;
            return numberOfResults == m_NumberOfMatchingTasks;
        }

        #endregion
    }
}