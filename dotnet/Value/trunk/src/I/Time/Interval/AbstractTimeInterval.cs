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

#endregion

namespace PPWCode.Value.I.Time.Interval
{
    /// <summary>
    /// General supporting code for time interval implementations.
    /// </summary>
    public abstract class AbstractTimeInterval : ITimeInterval /* MUDO , AbstractImmutableValue */
    {
        #region Abstract members of ITimeInterval

        public abstract DateTime? Begin { get; }
        public abstract DateTime? End { get; }
        public abstract TimeSpan? Duration { get; }
        public abstract ITimeInterval Determinate(DateTime? stubBegin, DateTime? stubEnd);

        #endregion

        #region Implemented members of ITimeInterval

        public override sealed bool Equals(object obj)
        {
            // F*CK
            //Contract.Ensures(Contract.Result<bool>() == 
            //    ((obj == null || GetType() != obj.GetType())
            //        ? false
            //        : (TimeIntervalRelation.MostCertainTimeIntervalRelation(this, (ITimeInterval)obj) == TimeIntervalRelation.EQUALS)));

            return (obj != null) && GetType().IsInstanceOfType(obj)
                   && (TimeIntervalRelation.MostCertainTimeIntervalRelation(this, (ITimeInterval)obj)
                       == TimeIntervalRelation.EQUALS);
        }

        public override sealed int GetHashCode()
        {
            int result = 0;
            if (Begin != null)
            {
                result += Begin.GetHashCode();
            }
            if (End != null)
            {
                result += End.GetHashCode();
            }
            /* since we need to be consistent with equals, we can only take into account properties
               that are used by MostCertainTimeIntervalRelation, i.e., begin and end */
            return result;
        }

        public DateTime? DeterminateBegin(DateTime? stubBegin)
        {
            return Begin ?? stubBegin;
        }

        public DateTime? DeterminateEnd(DateTime? stubEnd)
        {
            return End ?? stubEnd;
        }

        public override string ToString()
        {
            return "[" + ToString(Begin) + ", " + ToString(End) + "[\u0394(" + DurationAsString + ")"; // \u0394 is Greek capital delta
        }

        private static string ToString(DateTime? d)
        {
            return d == null ? "|?|" : d.ToString();
        }

        private string DurationAsString
        {
            get
            {
                TimeSpan? ts = Duration;
                return ts == null ? "-?-" : ts.ToString();
            }
        }

        #endregion
    }
}