using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace PPWCode.Util.Quartz
{
    /// <summary>
    /// Interface needed for the AbstractRetryJob
    /// </summary>
    public interface IRetryJobFault
    {
         /// <summary>
        /// Returns the list of exceptions thrown from the previous runs
        /// </summary>
        IEnumerable<Exception> Exceptions { get; }

        /// <summary>
        /// Returns the message, used to report the exceptions on <see cref="Exceptions"/>
        /// </summary>
        string Message { get; }
    }
}
