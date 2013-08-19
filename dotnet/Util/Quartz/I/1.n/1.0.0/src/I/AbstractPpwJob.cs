using System;
using System.Security.Principal;
using System.Threading;

using PPWCode.Util.Quartz.I.Identity;
using PPWCode.Vernacular.Exceptions.I;

using Quartz;

using log4net;

namespace PPWCode.Util.Quartz.I
{
    public abstract class AbstractPpwJob
        : IStatefulJob
    {
        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(AbstractPpwJob));
        private WindowsIdentity m_WindowsIdentity;

        public abstract string GroupName { get; }
        public static readonly object s_LockObject = new object();
        public abstract IIdentityProvider IdentityProvider { get; }
        public abstract Logging.ILog Log { get; }
        public IScheduler Scheduler { get; set; }

        public abstract void OnExecute(JobExecutionContext context);
        public abstract void FireChildJobs(JobExecutionContext context);
       
        #region Implementation of IJob

        public void Execute(JobExecutionContext context)
        {
            if (m_WindowsIdentity == null)
            {
                try
                {
                    m_WindowsIdentity = IdentityProvider.GetWindowsIdentity(context);
                }
                catch (Exception e)
                {
                    s_Logger.Error("Failed to determine WindowsIdentity, using identity of process", e);
                    m_WindowsIdentity = WindowsIdentity.GetCurrent();
                }
                if (m_WindowsIdentity == null)
                {
                    throw new ProgrammingError("Failed to find out a windowsIdentity");
                }
            }

            // Use the new Principal and save old for restoring later
            IPrincipal previousCurrentPrincipal = Thread.CurrentPrincipal;
            Thread.CurrentPrincipal = new WindowsPrincipal(m_WindowsIdentity);
            try
            {
                try
                {
                    Log.LogOnStart(context);
                    OnExecute(context);
                    Log.LogOnFinish(context, m_WindowsIdentity);
                }
                catch (Exception e)
                {
                    Log.LogOnFault(context, m_WindowsIdentity,  e);
                }
                finally
                {
                   FireChildJobs(context);
                }
            }
            finally
            {
                Thread.CurrentPrincipal = previousCurrentPrincipal;
                if (m_WindowsIdentity != null)
                {
                    m_WindowsIdentity.Dispose();
                    m_WindowsIdentity = null;
                }
            }
        }
        #endregion
    }
}
