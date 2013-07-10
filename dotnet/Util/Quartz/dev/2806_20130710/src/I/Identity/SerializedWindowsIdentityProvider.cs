using System;
using System.DirectoryServices;
using System.Security.Principal;

using PPWCode.Util.OddsAndEnds.I.ActiveDirectory;

using Quartz;

namespace PPWCode.Util.Quartz.I.Identity
{
    public class SerializedWindowsIdentityProvider : IIdentityProvider
    {
       private const string KeyUserAccount = @"KeyUserAccount";

       public WindowsIdentity GetWindowsIdentity(JobExecutionContext context)
       {
            string userPrincipalName = null;
            string userAccount = null;
            JobDataMap jobDataMap = context.MergedJobDataMap;
            if (jobDataMap.Contains(KeyUserAccount))
            {
                userAccount = Convert.ToString(jobDataMap[KeyUserAccount]);
            }
            if (String.IsNullOrEmpty(userAccount) || userAccount.Trim().Length == 0)
            {
                return WindowsIdentity.GetCurrent();
            }
            string domainName = AdSearch.GetDomainFromUserAccount(userAccount);
            AdSearch adSearch = new AdSearch(domainName);
            ResultPropertyValueCollection property = adSearch.GetProperty(userAccount, @"userprincipalname");
            if (property != null && property.Count == 1)
            {
                userPrincipalName = property[0].ToString();
            }
            return userPrincipalName == null
                    ? WindowsIdentity.GetCurrent()
                    : new WindowsIdentity(userPrincipalName);
            }
        }
    }
