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
using PPWCode.Vernacular.Exceptions.I;

#endregion

namespace PPWCode.Value.I.Time.Interval
{
    /// <summary>
    /// Cannot create a new <see cref="ITimeInterval"/> with the given parameters.
    /// </summary>
    public class IllegalTimeIntervalException : /* MUDO Value */SemanticException
    {
        public IllegalTimeIntervalException(Type tiType, DateTime? begin, DateTime? end, string messageKey, Exception innerException)
            : base( /*tiType, */messageKey, innerException)
        {
            Contract.Requires(tiType != null);
            // Contract.Ensures(Value == null);
            // Contract.Ensures(ValueType == tiType);
            Contract.Ensures(Begin == begin);
            Contract.Ensures(End == end);
            Contract.Ensures(Message == messageKey);
            Contract.Ensures(InnerException == innerException);

            m_Begin = begin;
            m_End = end;
        }

        public IllegalTimeIntervalException(ITimeInterval ti, DateTime? begin, DateTime? end, string messageKey, Exception innerException)
            : base( /*ti,*/ messageKey, innerException)
        {
            Contract.Requires(ti != null);
            // Contract.Ensures(Value == ti);
            // Contract.Ensures(ValueType == ti.getType());
            Contract.Ensures(Begin == begin);
            Contract.Ensures(End == end);
            Contract.Ensures(Message == messageKey);
            Contract.Ensures(InnerException == innerException);

            m_Begin = begin;
            m_End = end;
        }

        private readonly DateTime? m_Begin;

        public DateTime? Begin
        {
            get
            {
                return m_Begin;
            }
        }

        private readonly DateTime? m_End;

        public DateTime? End
        {
            get
            {
                return m_End;
            }
        }

        public override string ToString()
        {
            return base.ToString() + " ("
                   + (Begin != null ? Begin.ToString() : "null") + ", "
                   + (End != null ? End.ToString() : "null") + ")";
        }
    }
}