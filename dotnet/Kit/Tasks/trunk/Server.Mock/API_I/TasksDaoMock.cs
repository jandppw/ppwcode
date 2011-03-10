#region Using

using System;
using System.Collections.Generic;
using System.ServiceModel;
using System.Linq;

using PPWCode.Kit.Tasks.API_I;
using PPWCode.Vernacular.Persistence.I;

#endregion

namespace PPWCode.Kit.Tasks.Server.Mock.API_I
{
    public class TasksDaoMock
        : ITasksDao,
          ICommunicationObject
    {
        /// <summary>
        /// Store tasks in memory
        /// </summary>
        private readonly List<Task> m_Tasks = new List<Task>();

        #region ITasksDao Members

        public FindTasksResult FindTasks(string tasktype, string reference, TaskStateEnum? taskState)
        {
            // NOP
            return null;
        }

        #endregion

        #region ICrudDao Members

        public IPersistentObject DeleteById(string persistentObjectType, long? id)
        {
            // NOP
            return null;
        }

        public void FlushAllCaches()
        {
            // NOP
        }

        public void DoFlush()
        {
            // NOP
        }

        #endregion

        #region IWcfCrudDao Members

        public IPersistentObject Retrieve(string persistentObjectType, long? id)
        {
            // NOP
            return null;
        }

        public ICollection<IPersistentObject> RetrieveAll(string persistentObjectType)
        {
            ICollection<IPersistentObject> result = null;
            if (persistentObjectType == "PPWCode.Kit.Tasks.API_I.Task, PPWCode.Kit.Tasks.API_I")
            {
                result = m_Tasks.Cast<IPersistentObject>().ToArray();
                m_Tasks.Clear();
            }
            return result;
        }

        public IPersistentObject Create(IPersistentObject po)
        {
            Task task = po as Task;

            if (task != null)
            {
                // Make sure that IsTransient contract check will not fail...
                po.PersistenceId = 0;
                m_Tasks.Add(task);
            }
            return task;
        }

        public IPersistentObject Update(IPersistentObject po)
        {
            // NOP
            return null;
        }

        public ICollection<IPersistentObject> UpdateAll(ICollection<IPersistentObject> col)
        {
            // NOP
            return null;
        }

        public IPersistentObject Delete(IPersistentObject po)
        {
            // NOP
            return null;
        }

        public object GetPropertyValue(IPersistentObject po, string propertyName)
        {
            // NOP
            return null;
        }

        public ICollection<IPersistentObject> GetChildren(IPersistentObject po, string propertyName)
        {
            // NOP
            return null;
        }

        #endregion

        #region IDao Members

        public bool IsOperational
        {
            get { return true; }
        }

        #endregion

        #region IDisposable Members

        public void Dispose()
        {
            // NOP
        }

        #endregion

        #region ICommunicationObject Members

#pragma warning disable 67 // Ignore never used warnings

        public void Abort()
        {
            // NOP
        }

        public IAsyncResult BeginClose(TimeSpan timeout, AsyncCallback callback, object state)
        {
            // NOP
            return null;
        }

        public IAsyncResult BeginClose(AsyncCallback callback, object state)
        {
            // NOP
            return null;
        }

        public IAsyncResult BeginOpen(TimeSpan timeout, AsyncCallback callback, object state)
        {
            // NOP
            return null;
        }

        public IAsyncResult BeginOpen(AsyncCallback callback, object state)
        {
            // NOP
            return null;
        }

        public void Close(TimeSpan timeout)
        {
            // NOP
        }

        public void Close()
        {
            // NOP
        }

        public event EventHandler Closed;

        public event EventHandler Closing;

        public void EndClose(IAsyncResult result)
        {
            // NOP
        }

        public void EndOpen(IAsyncResult result)
        {
            // NOP
        }

        public event EventHandler Faulted;

        public void Open(TimeSpan timeout)
        {
            // NOP
        }

        public void Open()
        {
            // NOP
        }

        public event EventHandler Opened;

        public event EventHandler Opening;

        public CommunicationState State
        {
            get { throw new NotImplementedException(); }
        }

#pragma warning restore 67

        #endregion
    }
}