/*<license>
Copyright 2011 - $Date: 2008-11-06 15:27:53 +0100 (Thu, 06 Nov 2008) $ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

#region Using

using System;
using System.Diagnostics.Contracts;

#endregion

namespace PPWCode.Value.I.Time.Interval
{
    /// <summary>
    ///  General supporting code for time interval implementations that store a begin and and end,
    ///  and calculate a duration. In this case, it is impossible for both the begin and end date
    ///  to be <c>null</c>.
    /// </summary>
    public abstract class AbstractBeginEndTimeInterval : AbstractTimeInterval
    {
        //        @Invars(@Expression("! (begin == null && end == null)"))

        #region Construction

        protected AbstractBeginEndTimeInterval(DateTime? begin, DateTime? end)
        {
            Contract.Ensures(Begin == begin);
            Contract.Ensures(End == end);
            Contract.EnsuresOnThrow<IllegalTimeIntervalException>(begin == null && end == null || begin > end);

            if (begin == null && end == null)
            {
                throw new IllegalTimeIntervalException(GetType(), null, null, "NOT_BEGIN_AND_END_NULL", null);
            }
            if (begin != null && end != null && (begin.Value > end.Value))
            {
                throw new IllegalTimeIntervalException(GetType(), begin, end, "NOT_BEGIN_LE_END", null);
            }
            m_Begin = begin;
            m_End = end;
        }

        #endregion

        #region Properties

        private DateTime? m_Begin;

        public override sealed DateTime? Begin
        {
            get
            {
                return m_Begin;
            }
        }

        private DateTime? m_End;

        public override sealed DateTime? End
        {
            get
            {
                return m_End;
            }
        }

        /// <summary>
        /// The duration is the delta between begin and end.
        /// This can be <c>null</c>.
        /// </summary>
        public override sealed TimeSpan? Duration
        {
            get
            {
                Contract.Ensures(Contract.Result<TimeSpan?>() == (Begin == null || End == null ? null : End - Begin));
                if (m_Begin == null || m_End == null)
                {
                    return null;
                }
                return m_End.Value - m_Begin.Value;
            }
        }

        #endregion
    }
}