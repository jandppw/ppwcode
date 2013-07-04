using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.DirectoryServices;
using System.Linq;
using System.Net.Mail;
using System.Security.Principal;
using System.ServiceModel;
using System.Threading;
using PPWCode.Util.OddsAndEnds.I.ActiveDirectory;
using PPWCode.Vernacular.Exceptions.I;
using Quartz;
using Quartz.Impl;
using Quartz.Spi;
using log4net;

namespace PPWCode.Util.Quartz.I
{
    public abstract class AbstractSecurityJob
        : IStatefulJob,
          ISchedulerPlugin
    {
        #region Fields

        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(AbstractSecurityJob));

        private static readonly object s_LockObject = new object();

        private IScheduler m_Scheduler;
        private WindowsIdentity m_WindowsIdentity;

        public const string KeyUserAccount = @"KeyUserAccount";
        public const string KeyFireChildJobs = @"KeyFireChildJobs";
        public const string KeyAlertOnFinished = @"KeyAlertOnFinished";
        public const string KeyEmail = @"KeyEmail";

        private const string DefaultFromSender = @"";
        private const string From = @"from";
        private const string JobFinished = @"Job Finished";
        private const string DefaultSmtpHostAddress = @"mail.peopleware.be";

        #endregion

        #region Abstract Members

        public abstract string GroupName { get; }
        public abstract void Schedule(IScheduler scheduler);
        public abstract void OnExecute(JobExecutionContext context);
        public abstract IEnumerable<AbstractSecurityJob> ChildJobsToRun { get; }

        #endregion

        #region Private Members

        private WindowsIdentity GetWindowsIdentityFromContext(JobExecutionContext context)
        {
            if (WindowsIdentity == null)
            {
                bool useCurrentWindowsIdentity = GetAppSetting(@"UseCurrentWindowsIdentity", false);
                if (useCurrentWindowsIdentity)
                {
                    m_WindowsIdentity = WindowsIdentity.GetCurrent();
                }
                else
                {
                    string userAccount = null;
                    JobDataMap jobDataMap = context.MergedJobDataMap;
                    if (jobDataMap.Contains(KeyUserAccount))
                    {
                        userAccount = Convert.ToString(jobDataMap[KeyUserAccount]);
                    }
                    if (string.IsNullOrEmpty(userAccount) || userAccount.Trim().Length == 0)
                    {
                        m_WindowsIdentity = WindowsIdentity.GetCurrent();
                    }
                    else
                    {
                        string domainName = AdSearch.GetDomainFromUserAccount(userAccount);
                        AdSearch adSearch = new AdSearch(domainName);
                        ResultPropertyValueCollection property = adSearch.GetProperty(userAccount, @"userprincipalname");
                        string userPrincipalName = null;
                        if (property != null && property.Count == 1)
                        {
                            userPrincipalName = property[0].ToString();
                        }
                        m_WindowsIdentity = userPrincipalName == null
                                                ? WindowsIdentity.GetCurrent()
                                                : new WindowsIdentity(userPrincipalName);
                    }
                }
                Debug.Assert(WindowsIdentity != null && WindowsIdentity.IsAuthenticated);
            }
            return WindowsIdentity;
        }

        private void FireChildJobs(JobExecutionContext context)
        {
            lock (s_LockObject)
            {
                JobDataMap dataMap = context.MergedJobDataMap;
                if (dataMap.Contains(KeyFireChildJobs) && Convert.ToBoolean(dataMap[KeyFireChildJobs]))
                {
                    foreach (AbstractSecurityJob job in ChildJobsToRun)
                    {
                        try
                        {
                            ScheduleJobForExecution(context.Scheduler, job, context.MergedJobDataMap, null);
                        }
                        catch (Exception e)
                        {
                            s_Logger.Error(e.Message, e);
                        }
                    }
                }
            }
        }

        protected virtual void AlertOnFinished(JobExecutionContext context)
        {
           //NOP
        }

        protected virtual void AlertOnFinished(JobExecutionContext context, Exception occurredException)
        {
            //NOP
        }

        private static void ScheduleJobForExecution(IScheduler scheduler, AbstractSecurityJob job, JobDataMap jobDataMap, TimeSpan? ts)
        {
            // Determine TriggerName
            Trigger[] triggers = scheduler.GetTriggersOfJob(job.Name, job.GroupName);
            string triggerName;
            int triggerNr = 0;
            do
            {
                triggerNr++;
                triggerName = string.Format(@"{0}_{1:0000}", job.Name, triggerNr);
            }
            while (triggers.Any(t => t.Name == triggerName && t.Group == job.GroupName));

            // trigger
            DateTime startTimeUtc = DateTime.UtcNow + (ts ?? TimeSpan.Zero);
            SimpleTrigger trigger = new SimpleTrigger(triggerName, job.GroupName, job.Name, job.GroupName, startTimeUtc, null, 0, TimeSpan.Zero)
            {
                JobDataMap = jobDataMap
            };

            // schedule job
            JobDetail jobDetail;
            if (!scheduler.GetJobNames(job.GroupName).Any(n => n == job.Name))
            {
                jobDetail = new JobDetail(job.Name, job.GroupName, job.GetType());
                scheduler.ScheduleJob(jobDetail, trigger);
            }
            else
            {
                jobDetail = scheduler.GetJobDetail(job.Name, job.GroupName);
                scheduler.ScheduleJob(trigger);
            }
            s_Logger.Info(string.Format("Scheduling job {0} at {1} done.", jobDetail, trigger));
        }

        #endregion

        #region Protected Members

        protected WindowsIdentity WindowsIdentity
        {
            get { return m_WindowsIdentity; }
        }

        protected IScheduler Scheduler
        {
            get { return m_Scheduler; }
        }

        protected static T GetAppSetting<T>(string key, T defaultValue)
            where T : IConvertible
        {
            return ConfigHelper.GetAppSetting(key, defaultValue);
        }

        protected Type JobType
        {
            get { return GetType(); }
        }

        // ReSharper disable UnusedParameter.Local
        protected static string GetSmtpHostAddress(JobExecutionContext context)
        // ReSharper restore UnusedParameter.Local
        {
            return GetAppSetting(@"smtpHostAddress", DefaultSmtpHostAddress);
        }

        protected string GetEmailAddress(JobExecutionContext context)
        {
            string result = GetAppSetting(@"OnJobFinishedOverruleMailTo", (string)null);
            if (string.IsNullOrEmpty(result) || result.Trim().Length == 0)
            {
                JobDataMap jobDataMap = context.MergedJobDataMap;
                if (jobDataMap.Contains(KeyEmail))
                {
                    result = Convert.ToString(jobDataMap[KeyEmail]);
                }
                else
                {
                    WindowsIdentity user = GetWindowsIdentityFromContext(context);
                    AdSearch adSearch = new AdSearch(AdSearch.GetDomainFromUserAccount(user.Name));
                    result = adSearch.FindEmail(AdSearch.GetAccountNameFromUserAccount(user.Name));
                }
            }
            return result;
        }

        protected string GetUserName(JobExecutionContext context)
        {
            string result = GetAppSetting(@"OnJobFinishedOverruleMailTo", (string)null);
            if (string.IsNullOrEmpty(result) || result.Trim().Length == 0)
            {
                JobDataMap jobDataMap = context.MergedJobDataMap;
                if (jobDataMap.Contains(KeyEmail))
                {
                    result = Convert.ToString(jobDataMap[KeyEmail]);
                }
                else
                {
                    WindowsIdentity user = GetWindowsIdentityFromContext(context);
                    AdSearch adSearch = new AdSearch(AdSearch.GetDomainFromUserAccount(user.Name));
                    result = adSearch.FindName(AdSearch.GetAccountNameFromUserAccount(user.Name));
                }
            }
            return result;
        }

        protected void SendMail(
            JobExecutionContext context,
            string eMailAddress,
            string from,
            string subject,
            string body,
            string smtpHostAddress)
        {
            if (string.IsNullOrEmpty(eMailAddress))
            {
                WindowsIdentity user = GetWindowsIdentityFromContext(context);
                s_Logger.Error(@"SendMail: Unable to determine the e-mail address for " + user.Name);
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

        #endregion

        #region Public members

        /// <summary>
        /// Start een job onmiddelijk.
        /// </summary>
        /// <typeparam name="T">Type v/d job</typeparam>
        /// <param name="params">Parameters (afhankelijk v/h type v/d job)</param>
        public static void StartJobImmediately<T>(IEnumerable<KeyValuePair<string, object>> @params)
            where T : AbstractSecurityJob, new()
        {
            lock (s_LockObject)
            {
                ISchedulerFactory schedulerFactory = new StdSchedulerFactory();
                IScheduler scheduler = schedulerFactory.GetScheduler();

                JobDataMap jobDataMap = new JobDataMap();
                foreach (KeyValuePair<string, object> param in @params ?? CreateParams(false, true, true))
                {
                    jobDataMap[param.Key] = param.Value;
                }
                ScheduleJobForExecution(scheduler, new T(), jobDataMap, null);
            }
        }

        /// <summary>
        /// Trigger een bestaande job nogmaals.
        /// </summary>
        public static void ScheduleJob<T>(T job, JobDataMap jobDataMap, TimeSpan ts)
            where T : AbstractSecurityJob
        {
            lock (s_LockObject)
            {
                ISchedulerFactory schedulerFactory = new StdSchedulerFactory();
                IScheduler scheduler = schedulerFactory.GetScheduler();
                ScheduleJobForExecution(scheduler, job, jobDataMap, ts);
            }
        }

        /// <summary>
        /// Aanmaken van een default set van parameters, rekening houdend met de WCF ServiceSecurityContext
        /// </summary>
        public static IDictionary<string, object> CreateParams(bool fireChildJobs, bool alertOnFinished, bool useWcfContext)
        {
            WindowsIdentity windowsIdentity = useWcfContext && ServiceSecurityContext.Current != null
                                                  ? ServiceSecurityContext.Current.WindowsIdentity
                                                  : WindowsIdentity.GetCurrent();
            return new Dictionary<string, object>
            {
                { KeyUserAccount, windowsIdentity == null ? null : windowsIdentity.Name },
                { KeyFireChildJobs, fireChildJobs },
                { KeyAlertOnFinished, alertOnFinished }
            };
        }

        public string Name
        {
            get { return JobType.Name; }
        }

        #endregion

        #region Implementation of IJob

        public void Execute(JobExecutionContext context)
        {
            WindowsIdentity windowsIdentity;
            try
            {
                windowsIdentity = GetWindowsIdentityFromContext(context);
            }
            catch (Exception e)
            {
                s_Logger.Error("Failed to determine WindowsIdentity, using identity of process", e);
                windowsIdentity = WindowsIdentity.GetCurrent();
            }
            if (windowsIdentity == null)
            {
                throw new ProgrammingError("Failed to find out a windowsIdentity");
            }

            // Use the new Principal and save old for restoring later
            IPrincipal previousCurrentPrincipal = Thread.CurrentPrincipal;
            Thread.CurrentPrincipal = new WindowsPrincipal(windowsIdentity);
            try
            {
                Exception savedException = null;
                try
                {
                    s_Logger.Info(string.Format("Starting execution of job {0}.", context));
                    OnExecute(context);
                    s_Logger.Info(string.Format("Finished execution of job {0}.", context));
                }
                catch (Exception e)
                {
                    savedException = e;
                    s_Logger.Fatal(string.Format("Failed to execute job {0}.", context), savedException);
                }
                finally
                {
                    if (savedException == null)
                    {
                        AlertOnFinished(context);
                    }
                    else
                    {
                        AlertOnFinished(context, savedException);
                    }
                    FireChildJobs(context);
                }
            }
            finally
            {
                Thread.CurrentPrincipal = previousCurrentPrincipal;
                if (WindowsIdentity != null)
                {
                    WindowsIdentity.Dispose();
                    m_WindowsIdentity = null;
                }
            }
        }

        #endregion

        #region Implementation of ISchedulerPlugin

        /// <summary>
        /// Called during creation of the <see cref="IScheduler" /> in order to give
        /// the <see cref="ISchedulerPlugin" /> a chance to Initialize.
        /// </summary>
        public void Initialize(string pluginName, IScheduler scheduler)
        {
            m_Scheduler = scheduler;
        }

        /// <summary>
        /// Called when the associated <see cref="IScheduler" /> is started, in order
        /// to let the plug-in know it can now make calls into the scheduler if it
        /// needs to.
        /// </summary>
        public virtual void Start()
        {
            lock (s_LockObject)
            {
                Schedule(Scheduler);
            }
        }

        /// <summary>
        /// Schedules the job that checks whether any new scanned documents arrived
        /// and processes those as needed.  The method first removes the quartz 
        /// job and trigger if they already existed. Next, a new job and trigger is 
        /// created and put in place.
        /// </summary>
        public virtual void Shutdown()
        {
        }

        #endregion
    }
}
