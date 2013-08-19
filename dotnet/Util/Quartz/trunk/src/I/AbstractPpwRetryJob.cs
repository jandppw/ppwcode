using System;
using System.Linq;

using PPWCode.Util.Quartz.I.Scheduler;

using Quartz;
using Quartz.Spi;

using log4net;

namespace PPWCode.Util.Quartz.I
{
    public abstract class AbstractPpwRetryJob
        : AbstractPpwJob, ISchedulerPlugin 
    {
        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(AbstractPpwRetryJob));

        public const string KeyTimeSpans = @"KeyTimeSpans";
        public const string KeyCurrentId = @"KeyCurrentId";
        public const string KeyMaxId = @"KeyMaxId";

        public abstract void DoAction(JobExecutionContext context, int currentId);
        public abstract void RemoveFaults(JobDataMap jobDataMap);
        public abstract void Schedule(IScheduler scheduler);
        public IPpwScheduler PpwScheduler { get; set; }
        
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
                AggregateException aggregatedExceptions = PpwScheduler.Reschedule(context, e);
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

        #region Implementation of ISchedulerPlugin

        /// <summary>
        /// Called during creation of the <see cref="IScheduler" /> in order to give
        /// the <see cref="ISchedulerPlugin" /> a chance to Initialize.
        /// </summary>
        public void Initialize(string pluginName, IScheduler scheduler)
        {
           Scheduler = scheduler;
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
