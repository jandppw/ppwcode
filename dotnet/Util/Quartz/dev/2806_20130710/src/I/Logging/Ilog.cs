using System;
using System.Security.Principal;

using Quartz;

namespace PPWCode.Util.Quartz.I.Logging
{
    public interface ILog
    {
        void LogOnStart(JobExecutionContext context);
        void LogOnFinish(JobExecutionContext context, WindowsIdentity windowsIdentity);
        void LogOnFault(JobExecutionContext context, WindowsIdentity windowsIdentity, Exception exception);
    }
}
