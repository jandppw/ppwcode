/*<license>
Copyright 2004 - $Date: 2008-12-07 22:15:22 +0100 (Sun, 07 Dec 2008) $ by PeopleWare n.v..

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
    /// An actual time interval, that is constructed using a begin and an end date,
    /// and has no further restrictions. It is not possible for both the begin and
    /// the end date to be <c>null</c>.
    /// </summary>
    public sealed class BeginEndTimeInterval : AbstractBeginEndTimeInterval
    {
        public BeginEndTimeInterval(DateTime? begin, DateTime? end)
            : base(begin, end)
        {
            Contract.Ensures(Begin == begin);
            Contract.Ensures(End == end);
            Contract.EnsuresOnThrow<IllegalTimeIntervalException>(
                begin == null && end == null
                || begin != null && end != null && begin.Value > end.Value);

            // NOP
        }

        public override ITimeInterval Determinate(DateTime? stubBegin, DateTime? stubEnd)
        {
            return DeterminateBeginEndTimeInterval(stubBegin, stubEnd);
        }

        public BeginEndTimeInterval DeterminateBeginEndTimeInterval(DateTime? stubBegin, DateTime? stubEnd)
        {
            Contract.Ensures(Contract.Result<BeginEndTimeInterval>() != null);
            Contract.Ensures((Contract.Result<BeginEndTimeInterval>().Begin == DeterminateBegin(stubBegin)));
            Contract.Ensures(Contract.Result<BeginEndTimeInterval>().End == DeterminateEnd(stubEnd));

            Contract.EnsuresOnThrow<IllegalTimeIntervalException>(DeterminateBegin(stubBegin) > DeterminateEnd(stubEnd));

            return new BeginEndTimeInterval(DeterminateBegin(stubBegin), DeterminateEnd(stubEnd));
        }
    }
}