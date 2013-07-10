using System;
using System.Linq;

using Quartz;
using Quartz.Impl;

using log4net;

namespace PPWCode.Util.Quartz.I.Scheduler
{
    public abstract class PpwAbstractScheduler
        : AbstractPpwRetryJob, IPpwScheduler
    {
        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(PpwAbstractScheduler));

        public abstract IRetryJobFault SaveFault(JobDataMap jobDataMap, Exception e);
        public abstract void ActionFailed(JobExecutionContext context, AggregateException ae);

        #region Implementation of IPpwScheduler

        public AggregateException Reschedule(JobExecutionContext context, Exception fault)
        {
            try
            {
                JobDataMap jobDataMap = context.MergedJobDataMap;

                // Save current fault
                IRetryJobFault jobFault = SaveFault(jobDataMap, fault);

                // check for retry
                int maxId = Convert.ToInt32(jobDataMap[KeyMaxId]);
                int currentId = Convert.ToInt32(jobDataMap[KeyCurrentId]);
                if (currentId <= maxId)
                {
                    TimeSpan[] timeSpans = (TimeSpan[])jobDataMap[KeyTimeSpans];
                    ScheduleJob(this, jobDataMap, timeSpans[currentId - 1]);
                    return null;
                }

                // Create an exception that is an aggregation of all occurred exceptions
                AggregateException ae = new AggregateException(jobFault.Message, jobFault.Exceptions);
                try
                {
                    ActionFailed(context, ae);
                }
                catch (Exception e)
                {
                    s_Logger.Error("AbstractRetryJob.ActionFailed", e);
                }
                return ae;
            }
            catch (Exception e)
            {
                s_Logger.Error(@"Internal error: AbstractRetryJob.Reschedule", e);
                throw;
            }
        }
        #endregion

        /// <summary>
        /// Trigger een bestaande job nogmaals.
        /// </summary>
        private static void 
            
            ScheduleJob<T>(T job, JobDataMap jobDataMap, TimeSpan ts)
            where T : AbstractPpwJob
        {
            lock (s_LockObject)
            {
                ISchedulerFactory schedulerFactory = new StdSchedulerFactory();
                IScheduler scheduler = schedulerFactory.GetScheduler();
                ScheduleJobForExecution(scheduler, job, jobDataMap, ts);
            }
        }

        private static void ScheduleJobForExecution(IScheduler scheduler, AbstractPpwJob job, JobDataMap jobDataMap, TimeSpan? ts = null)
        {
            // Determine TriggerName
            Trigger[] triggers = scheduler.GetTriggersOfJob(job.GetType().Name, job.GroupName);
            string triggerName;
            int triggerNr = 0;
            do
            {
                triggerNr++;
                triggerName = string.Format(@"{0}_{1:0000}", job.GetType().Name, triggerNr);
            } while (triggers.Any(t => t.Name == triggerName && t.Group == job.GroupName));

            // trigger
            DateTime startTimeUtc = DateTime.UtcNow + (ts ?? TimeSpan.Zero);
            SimpleTrigger trigger = new SimpleTrigger(triggerName, job.GroupName, job.GetType().Name, job.GroupName, startTimeUtc, null, 0, TimeSpan.Zero)
            {
                JobDataMap = jobDataMap
            };

            // schedule job
            JobDetail jobDetail;
            if (!scheduler.GetJobNames(job.GroupName).Any(n => n == job.GetType().Name))
            {
                jobDetail = new JobDetail(job.GetType().Name, job.GroupName, job.GetType());
                scheduler.ScheduleJob(jobDetail, trigger);
            }
            else
            {
                jobDetail = scheduler.GetJobDetail(job.GetType().Name, job.GroupName);
                scheduler.ScheduleJob(trigger);
            }
            s_Logger.Info(string.Format("Scheduling job {0} at {1} done.", jobDetail, trigger));
        }
    }
}
