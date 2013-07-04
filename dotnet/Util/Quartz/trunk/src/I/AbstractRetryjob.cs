using System;
using System.Collections.Generic;
using System.Linq;
using Quartz;
using log4net;

namespace PPWCode.Util.Quartz.I
{
    public abstract class AbstractRetryjob
        : AbstractSecurityCronJob
    {
        #region Fields

        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(AbstractRetryjob));

        protected const string KeyTimeSpans = @"KeyTimeSpans";
        private const string KeyCurrentId = @"KeyCurrentId";
        private const string KeyMaxId = @"KeyMaxId";

        #endregion

        #region Abstract Members

        protected abstract void DoAction(JobExecutionContext context, int currentId);
        protected abstract IRetryJobFault SaveFault(JobDataMap jobDataMap, Exception e);
        protected abstract void RemoveFaults(JobDataMap jobDataMap);
        protected abstract void ActionFailed(JobExecutionContext context, AggregateException ae);

        #endregion

        #region Overrides AbstractSecurityCronJob

        public override sealed void OnExecute(JobExecutionContext context)
        {
            JobDataMap jobDataMap = context.MergedJobDataMap;
            int currentId = jobDataMap.Contains(KeyCurrentId)
                                ? Convert.ToInt32(jobDataMap[KeyCurrentId]) + 1
                                : 1;

            // Prepare for the first time
            if (currentId == 1)
            {
                if (jobDataMap.Contains(KeyTimeSpans))
                {
                    TimeSpan[] timeSpans = (TimeSpan[])jobDataMap[KeyTimeSpans];
                    timeSpans = timeSpans ?? Enumerable.Empty<TimeSpan>()
                                                 .Where(t => t.Ticks > 0)
                                                 .ToArray();
                    jobDataMap[KeyTimeSpans] = timeSpans;
                    jobDataMap[KeyMaxId] = timeSpans.Length;
                }
                else
                {
                    jobDataMap[KeyMaxId] = 0;
                }
            }
            jobDataMap[KeyCurrentId] = currentId;

            // Execute action on Crm
            Exception savedException = null;
            try
            {
                DoAction(context, currentId);
            }
            catch (Exception e)
            {
                savedException = e;
                AggregateException aggregatedExceptions = Reschedule(context, e);
                if (aggregatedExceptions != null)
                {
                    throw aggregatedExceptions;
                }
            }
            finally
            {
                if (savedException == null)
                {
                    try
                    {
                        RemoveFaults(jobDataMap);
                    }
                    catch (Exception e)
                    {
                        s_Logger.Error(@"Internal error: AbstractRetryJob.OnExecute", e);
                        throw;
                    }
                }
            }
        }

        public override sealed IEnumerable<AbstractSecurityJob> ChildJobsToRun
        {
            get { return (IEnumerable<AbstractSecurityJob>)Enumerable.Empty<AbstractSecurityCronJob>(); }
        }

        #endregion

        #region Private methods

        private AggregateException Reschedule(JobExecutionContext context, Exception fault)
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
    }
}
