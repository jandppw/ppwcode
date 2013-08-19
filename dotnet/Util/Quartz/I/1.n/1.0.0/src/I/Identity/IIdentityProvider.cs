using System.Security.Principal;

using Quartz;

namespace PPWCode.Util.Quartz.I.Identity
{
    public interface IIdentityProvider
    {
        WindowsIdentity GetWindowsIdentity(JobExecutionContext rContext);
    }
}
