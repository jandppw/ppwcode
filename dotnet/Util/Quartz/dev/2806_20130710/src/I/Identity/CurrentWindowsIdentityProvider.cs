using System.Security.Principal;

using Quartz;

namespace PPWCode.Util.Quartz.I.Identity
{
    public class CurrentWindowsIdentityProvider : IIdentityProvider
    {
        #region Implementation of IIdentityProvider

        public WindowsIdentity GetWindowsIdentity(JobExecutionContext rContext)
        {
            return WindowsIdentity.GetCurrent();
        }

        #endregion
    }
}
