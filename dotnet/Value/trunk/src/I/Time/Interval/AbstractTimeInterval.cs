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
            Contract.Ensures(Contract.Result<bool>() ==
                ((obj == null || GetType() != obj.GetType())
                    ? false
                    : (TimeIntervalRelation.MostCertainTimeIntervalRelation(this, (ITimeInterval)obj) == TimeIntervalRelation.EQUALS)));

            return this.Equals(obj as ITimeInterval);
        }

        /// <summary>
        /// Equals as defined by <see cref="TimeIntervalRelation.TimeIntervalRelation"/>
        /// and <see cref="TimeIntervalRelation.EQUALS"/>.
        /// </summary>
        /// <remarks>
        /// See the
        //  <a href="http://go.microsoft.com/fwlink/?LinkID=85237">full list of guidelines</a>
        //  and also the guidance for <a href="http://go.microsoft.com/fwlink/?LinkId=85238">operator==</a>.
        /// </remarks>
        public bool Equals(ITimeInterval other)
        {
            Contract.Ensures(Contract.Result<bool>() == 
                ((other == null)
                    ? false
                    : (TimeIntervalRelation.MostCertainTimeIntervalRelation(this, other) == TimeIntervalRelation.EQUALS)));

            return (other != null) && GetType().IsInstanceOfType(other)
                   && (TimeIntervalRelation.MostCertainTimeIntervalRelation(this, other)
                       == TimeIntervalRelation.EQUALS);
        }

        /// <summary>
        /// Equality as defined by <see cref="TimeIntervalRelation.TimeIntervalRelation"/>
        /// and <see cref="TimeIntervalRelation.EQUALS"/>.
        /// </summary>
        /// <remarks>
        /// See the
        //  <a href="http://go.microsoft.com/fwlink/?LinkID=85237">full list of guidelines</a>
        //  and also the guidance for <a href="http://go.microsoft.com/fwlink/?LinkId=85238">operator==</a>.
        /// </remarks>
        public static bool operator ==(AbstractTimeInterval ati1, AbstractTimeInterval ati2)
        {
            // NO CONTRACT HERE, TO AVOID INFINITE RECURSION
            //Contract.Ensures(Contract.Result<bool>() == (ati1 == null ? ati2 == null : ati1.Equals(ati2)));

            return SaveIsNull(ati1) ? SaveIsNull(ati2) : ati1.Equals(ati2);
        }

        /// <summary>
        /// Equality as defined by <see cref="TimeIntervalRelation.TimeIntervalRelation"/>
        /// and <see cref="TimeIntervalRelation.EQUALS"/>.
        /// </summary>
        /// <remarks>
        /// See the
        //  <a href="http://go.microsoft.com/fwlink/?LinkID=85237">full list of guidelines</a>
        //  and also the guidance for <a href="http://go.microsoft.com/fwlink/?LinkId=85238">operator==</a>.
        /// </remarks>
        public static bool operator !=(AbstractTimeInterval ati1, AbstractTimeInterval ati2)
        {
            Contract.Ensures(Contract.Result<bool>() == (! (ati1 == ati2)));

            return (SaveIsNull(ati1) && SaveIsNotNull(ati2)) || (SaveIsNotNull(ati1) && (! ati1.Equals(ati2)));
        }

        /// <summary>
        /// Compare to null without using the overridden operator.
        /// </summary>
        private static bool SaveIsNull(AbstractTimeInterval ati)
        {
            object atiAsObject = ati;
            return atiAsObject == null;            
        }

        /// <summary>
        /// Compare to null without using the overridden operator.
        /// </summary>
        private static bool SaveIsNotNull(AbstractTimeInterval ati)
        {
            object atiAsObject = ati;
            return atiAsObject != null;
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