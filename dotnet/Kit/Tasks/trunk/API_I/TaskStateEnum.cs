#region Using

using System;

#endregion

namespace PPWCode.Kit.Tasks.API_I
{
    /// <summary>
    /// Possible values of <see cref="Task.State"/>.
    /// </summary>
    [Serializable, Flags]
    public enum TaskStateEnum
    {
        /// <summary>
        /// The task is not yet in progress.
        /// </summary>
        CREATED = 1,
        /// <summary>
        /// The task is in progress, but not yet completed.
        /// </summary>
        IN_PROGRESS = 2,
        /// <summary>
        /// The task is completed
        /// </summary>
        COMPLETED = 4,
    }
}