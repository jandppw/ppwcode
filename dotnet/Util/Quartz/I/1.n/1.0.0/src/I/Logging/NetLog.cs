using System;
using System.Security.Principal;

using Quartz;

using log4net;

namespace PPWCode.Util.Quartz.I.Logging
{
    public class NetLog : ILog
    {
        private static readonly log4net.ILog s_Logger = LogManager.GetLogger(typeof(NetLog));

        #region Implementation of ILog

        public virtual void LogOnStart(JobExecutionContext context)
        {
            s_Logger.Info(string.Format("Starting execution of job {0}.", context));
        }

        public virtual void LogOnFinish(JobExecutionContext context, WindowsIdentity windowsIdentity)
        {
            s_Logger.Info(string.Format("Finished execution of job {0}.", context));
        }

        public virtual void LogOnFault(JobExecutionContext context, WindowsIdentity windowsIdentity,  Exception ex)
        {
            s_Logger.Fatal(string.Format("Failed to execute job {0}.", context), ex);
        }
        #endregion
    }
}
