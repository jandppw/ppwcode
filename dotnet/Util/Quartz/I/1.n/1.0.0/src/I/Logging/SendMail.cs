using System;
using System.Net.Mail;
using System.Security.Principal;

using PPWCode.Util.OddsAndEnds.I.ActiveDirectory;

using Quartz;

using log4net;

namespace PPWCode.Util.Quartz.I.Logging
{
    public class SendMail : NetLog
    {
       private static readonly log4net.ILog s_Logger = LogManager.GetLogger(typeof(SendMail));
        
        public SendMail(
            string from, 
            string subject, 
            string smtpHostAddress)
        {
            From = from;
            Subject = subject;
            SmtpHostAddress = smtpHostAddress;
        }

        private string From { get; set; }
        private string Subject { get; set; }
        private string SmtpHostAddress { get; set; }
        private const string KeyEmail = @"KeyEmail";

        public new virtual void LogOnFinish(JobExecutionContext context, WindowsIdentity windowsIdentity)
        {
            try
            {
                base.LogOnFinish(context, windowsIdentity);
                string email = GetEmailAddress(context, windowsIdentity);
                string body = string.Format(@"Job {0} finished.", GetType().Name);
                SendingMail(context, windowsIdentity, email, From, Subject, body, SmtpHostAddress);
            }
            catch (Exception e)
            {
                s_Logger.Error(e.Message, e);
            }
        }

        public new virtual void LogOnFault(JobExecutionContext context, WindowsIdentity windowsIdentity, Exception exception)
        {
            try
            {
                base.LogOnFault(context, windowsIdentity, exception);
                string email = GetEmailAddress(context, windowsIdentity);
                string body = string.Format(@"Job {1} failed.{0}{2}", Environment.NewLine, GetType().Name, exception);
                SendingMail(context, windowsIdentity, email, From, Subject, body, SmtpHostAddress);
            }
            catch (Exception e)
            {
                s_Logger.Error(@"occurredException", exception);
                s_Logger.Error(e.Message, e);
            }
        }

        private string GetEmailAddress(JobExecutionContext context, WindowsIdentity windowsIdentity)
        {
           JobDataMap jobDataMap = context.MergedJobDataMap;
           string email;
           if (jobDataMap.Contains(KeyEmail))
           {
               email = Convert.ToString(jobDataMap[KeyEmail]);
           }
           else
           {
              AdSearch adSearch = new AdSearch(AdSearch.GetDomainFromUserAccount(windowsIdentity.Name));
              email = adSearch.FindEmail(AdSearch.GetAccountNameFromUserAccount(windowsIdentity.Name));
           }
           return email;
        }

        private void SendingMail(
           JobExecutionContext context,
           WindowsIdentity windowsIdentity,
           string eMailAddress,
           string from,
           string subject,
           string body,
           string smtpHostAddress)
          {
            if (string.IsNullOrEmpty(eMailAddress) && windowsIdentity != null)
            {
                s_Logger.Error(@"SendMail: Unable to determine the e-mail address for " + windowsIdentity.Name);
                return;
            }
            using (MailMessage message = new MailMessage())
            {
                message.To.Add(eMailAddress);
                message.Subject = subject;
                message.From = new MailAddress(from);
                message.Body = body;
                SmtpClient smtp = new SmtpClient(smtpHostAddress);
                smtp.Send(message);
            }
        }
    }
}
