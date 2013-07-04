using System;
using System.Text.RegularExpressions;
using Quartz;
using log4net;

namespace PPWCode.Util.Quartz.I
{
    public abstract class AbstractSecurityCronJob
        : AbstractSecurityJob
    {
        #region Fields

        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(AbstractSecurityCronJob));

        #endregion

        #region Virtual Members

        /// <summary>
        /// Defines a cron-trigger. See http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
        /// </summary>
        protected string ScheduleInfos
        {
            get { return GetAppSetting(@"ScheduleInfo" + Name, string.Empty); }
        }

        #endregion

        #region Overrides of AbstractSecurityJob

        public override sealed void Schedule(IScheduler scheduler)
        {
            // delete old job including trigger
            scheduler.DeleteJob(Name, GroupName);

            if (!string.IsNullOrEmpty(ScheduleInfos))
            {
                string[] scheduleInfos = ScheduleInfos.Split(new[] { '|' }, StringSplitOptions.RemoveEmptyEntries);
                if (scheduleInfos.Length > 0)
                {
                    string jobName = Name;
                    string jobGroup = GroupName;
                    JobDetail jobDetail = new JobDetail(jobName, jobGroup, JobType);
                    for (int i = 0; i < scheduleInfos.Length; i++)
                    {
                        string scheduleInfo = scheduleInfos[i];
                        Regex regex = new Regex(@"^\((?<CronExpression>.+);(?<FireChildJobs>.+)\)$");
                        Match match = regex.Match(scheduleInfo);
                        if (match.Success)
                        {
                            Group cronExpressionGroup = match.Groups[@"CronExpression"];
                            if (cronExpressionGroup.Success)
                            {
                                // Create trigger
                                string triggerName = string.Format(@"{0}_{1:0000}", jobName, i + 1);
                                string triggerGroup = jobGroup;
                                CronTrigger trigger = new CronTrigger(triggerName, triggerGroup, jobName, jobGroup, DateTime.UtcNow, null, cronExpressionGroup.Value);

                                bool fireChildJobs;
                                Group fireChildJobsGroup = match.Groups[@"FireChildJobs"];
                                if (fireChildJobsGroup.Success)
                                {
                                    bool.TryParse(fireChildJobsGroup.Value, out fireChildJobs);
                                }
                                else
                                {
                                    fireChildJobs = false;
                                }
                                trigger.JobDataMap[KeyFireChildJobs] = fireChildJobs;

                                // schedule job
                                if (i == 0)
                                {
                                    scheduler.ScheduleJob(jobDetail, trigger);
                                }
                                else
                                {
                                    scheduler.ScheduleJob(trigger);
                                }
                                s_Logger.Info(string.Format("Scheduling {0} at {1} done.", jobDetail, trigger));
                            }
                        }
                    }
                }
            }
            else
            {
                s_Logger.Info(string.Format(@"No ScheduleInfo found for type {0}!", JobType));
            }
        }

        #endregion
    }
}
