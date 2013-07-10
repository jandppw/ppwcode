using System;

using Quartz;

namespace PPWCode.Util.Quartz.I.Scheduler
{
    public interface IPpwScheduler 
    {
        AggregateException Reschedule(JobExecutionContext context, Exception fault);
    }
}
