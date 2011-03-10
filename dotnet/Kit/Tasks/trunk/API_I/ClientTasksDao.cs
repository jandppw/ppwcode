#region Using

using System.Diagnostics.Contracts;

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

        public ClientTasksDao(ITasksDao taskDao)
            : base(taskDao)
        {
            Contract.Requires(taskDao != null);
            Contract.Ensures(TasksDao == taskDao);

            m_TasksDao = taskDao;
        }

        #endregion

        #region Properties

        private ITasksDao m_TasksDao;

        // ReSharper disable MemberCanBePrivate.Global
        [Pure]
        public ITasksDao TasksDao
        {
            get
            {
                CheckObjectAlreadyDisposed();
                return m_TasksDao;
            }
        }

        // ReSharper restore MemberCanBePrivate.Global

        #endregion

        #region Methods

        /// <inheritdoc cref="ITasksDao.FindTasks"/>
        public FindTasksResult FindTasks(string taskType, string reference, TaskStateEnum? taskState)
        {
            CheckObjectAlreadyDisposed();

            return m_TasksDao.FindTasks(taskType, reference, taskState);
        }

        #endregion

        #region Overrides of ClientCrudDao

        protected override void Cleanup()
        {
            base.Cleanup();
            m_TasksDao = null;
        }

        #endregion
    }
}