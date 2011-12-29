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
    /// <para>A time interval is a duration between 2 points in time.
    /// <strong>Time in general, and intervals in particular, are treacherously difficult to reason
    /// about. Beware.</strong></para>
    /// </summary>
    /// <remarks>
    /// <para><strong>Time in general, and intervals in particular, are treacherously difficult to reason
    /// about. Beware.</strong> Experience shows that falling back to the begin and end dates as 
    /// separated dates and reasoning about them does not solve the complexity, and results in even
    /// more difficult reasonings and is even more error prone.</para>
    /// <para><em>We strongly suggest that you use <see cref="TimeIntervalRelation"/> for reasoning 
    /// about time intervals.</em></para>
    /// <para>For time intervals, we impose a few choices that, in our experience, are good
    /// choices.</para>
    /// 
    /// <h3>Interval</h3>
    /// <para>We choose for all time intervals to express <em>half, right-open</em> time intervals.
    /// A consistent half-open interval helps in avoiding awkward border conditions. We have chosen
    /// to always use half, <em>right</em>-open intervals for several reasons, stemming from philosophy,
    /// the similarity between limitations in physics due to the finite speed of light and processing and remote
    /// communication, the similarity between the impossibility to express simultaneity of events in physics due to
    /// relativity (see
    /// <a href="http://www.amazon.com/About-Time-Einsteins-Unfinished-Revolution/dp/0684818221/ref=sr_1_1?ie=UTF8&amp;s=books&amp;qid=1224448626&amp;sr=1-1">About
    /// Time: Einstein's Unfinished Revolution; <cite>Paul Davies</cite></a>) and the drift of clocks in different
    /// computers in distributed systems, and other half-baked (or half-drunk) reflections. If you bring a bottle,
    /// we can talk about this.</para>
    /// 
    /// <h3>Three properties and incomplete data</h3>
    /// <para>A time interval has a <see cref="Begin">begin time</see>, an <see cref="End">end time</see>
    /// <see cref="Duration">duration</see>, which are interrelated. The interface does not define which
    /// 2 of those 3 should be stored, and which should be calculated.</para>
    /// <para>The begin date, the end date and the duration can be <c>null</c>. The semantics of this is 
    /// to be defined by the user case by case. We often encounter the use case of open-ended time intervals:
    /// an employment spans a time interval of time, but during most of that time interval, only the begin
    /// time is known. In such cases, a mandatory begin date, and an unknown end date and duration is used,
    /// until the end date is decided on. How to deal with unknown or constrained begin and end times is 
    /// described in <see cref="TimeIntervalRelation"/>.</para>
    /// <para>When one basic property is <c>null</c>, there can be no calculations, and the derived 
    /// property will be <c>null</c> also.</para>
    /// <para><c>ITimeIntervals</c> with all 3 properties <c>null</c> make no sense and are
    /// prohibited.</para>
    /// 
    /// <h3>Implementations</h3>
    /// <para>This namespace offers many subtypes of <c>ITimeInterval</c>. We believe it is, in this case, 
    /// more appropriate to introduce different subtypes for different constraints on time intervals,
    /// instead of limiting a general time interval implementation in the use code. This way, we can add
    /// specialized user interfaces, Hibernate user types, etcetera, that are tweaked to specific constraints. 
    /// E.g., a time interval that uses days as a time quant, will have a simpler user interface than a time 
    /// interval that needs a user interface that is precise to the millisecond.</para>
    /// <para>Since this is an <see cref="MUDOIImmutableValue"/>, implementations should offer a good public
    /// constructor, and should be declared <c>sealed</c>.</para>
    /// 
    /// <h3>Struct or class</h3>
    /// <para>Time intervals are designed as class instances, because we require inheritance,
    /// and we cannot have a sensible 0-element. Why the latter is important, see
    /// <a href="http://msdn.microsoft.com/en-us/library/ms229031(v=VS.90).aspx">Structure Design</a>.</para>
    /// 
    /// <h3>Equality and comparison</h3>
    /// MUDO
    /// </remarks>
    [ContractClass(typeof(ITimeIntervalContract))]
    public interface ITimeInterval : IEquatable<ITimeInterval> /* MUDO extends IImmutableValue */
    {
        /// <summary>
        /// This time interval begins at this time, inclusive. This can be <c>null</c>,
        /// with semantics to be determined by the user.
        /// </summary>
        DateTime? Begin { get; }

        /// <summary>
        /// This time interval ends at this time, exclusive. This can be <c>null</c>,
        /// with semantics to be determined by the user.
        /// </summary>
        DateTime? End { get; }

        /// <summary>
        /// The duration of this time interval. This can be <c>null</c>,
        /// with semantics to be determined by the user.
        /// </summary>
        TimeSpan? Duration { get; }

        /// <summary>
        /// Equals as defined by <see cref="TimeIntervalRelation.TimeIntervalRelation"/>
        /// and <see cref="TimeIntervalRelation.EQUALS"/>.
        /// </summary>
        /// <remarks>
        /// <para>Note that this definition also applies to <see cref="IEquatable.Equals"/>,
        /// <c>operator ==</c> and <c>operator !=</c>, which should also be overriden.</para>
        /// <para>See the
        /// <a href="http://go.microsoft.com/fwlink/?LinkID=85237">full list of guidelines</a>
        /// and also the guidance for
        /// <a href="http://go.microsoft.com/fwlink/?LinkId=85238">operator==</a>.</para>
        /// </remarks>
        bool Equals(object obj);

        /// <summary>
        /// Override to be consistent with <see cref="Equals"/>.
        /// </summary>
        int GetHashCode();

        /// <summary>
        /// Return a determinate begin. If we don't have one, return <paramref name="stubBegin"/>.
        /// </summary>
        [Pure]
        DateTime? DeterminateBegin(DateTime? stubBegin);

        /// <summary>
        /// Return a determinate end. If we don't have one, return <paramref name="stubEnd"/>.
        /// </summary>
        [Pure]
        DateTime? DeterminateEnd(DateTime? stubEnd);

        /// <summary>
        /// Return a (more) determinate time interval than this, i.e., replace a <c>null</c>
        /// begin and end by <paramref name="stubBegin"/> and <paramref name="stubEnd"/>.
        /// This is introduced in support of reasoning with unknown but constrained begin and end dates.
        /// See <see cref="TimeIntervalRelation"/>, &quot;Reasoning with unknown but constrained
        /// begin and end dates&quot; for more information.
        /// </summary>
        [Pure]
        ITimeInterval Determinate(DateTime? stubBegin, DateTime? stubEnd);
    }


    // ReSharper disable InconsistentNaming
    /// <exclude />
    [ContractClassFor(typeof(ITimeInterval))]
    public abstract class ITimeIntervalContract : ITimeInterval
    {
        #region Type invariants

        [ContractInvariantMethod]
        private void TypeInvariants()
        {
            Contract.Invariant(! (Begin == null && End == null && Duration == null));
            Contract.Invariant(Begin != null && End != null ? Duration != null : true);
            Contract.Invariant(Begin != null && Duration != null ? End != null : true);
            Contract.Invariant(End != null && Duration != null ? Begin != null : true);
            Contract.Invariant(Begin != null && End != null ? Begin <= End : true);
            Contract.Invariant(Duration != null ? TimeSpan.Zero <= Duration : true);
            Contract.Invariant((Begin != null && End != null && Duration != null) ? Duration == End - Begin : true);
        }

        #endregion

        #region Implementation of IPerson

        public DateTime? Begin
        {
            get
            {
                return default(DateTime);
            }
        }

        public DateTime? End
        {
            get
            {
                return default(DateTime);
            }
        }

        public TimeSpan? Duration
        {
            get
            {
                return default(TimeSpan);
            }
        }

        public bool Equals(ITimeInterval other)
        {
            // F*CK
            //Contract.Ensures(Contract.Result<bool>() == 
            //    ((other == null)
            //        ? false
            //        : (TimeIntervalRelation.LeastUncertainTimeIntervalRelation(this, other) == TimeIntervalRelation.EQUALS)));

            return false;
        }

        public override bool Equals(object obj)
        {
            // F*CK
            //Contract.Ensures(Contract.Result<bool>() ==
            //    ((obj == null || GetType() != obj.GetType())
            //        ? false
            //        : (TimeIntervalRelation.LeastUncertainTimeIntervalRelation(this, (ITimeInterval)obj) == TimeIntervalRelation.EQUALS)));

            return false;
        }

        public override int GetHashCode()
        {
            return 0;
        }

        public DateTime? DeterminateBegin(DateTime? stubBegin)
        {
            Contract.Ensures(Contract.Result<DateTime?>() == (Begin ?? stubBegin));

            return null;
        }

        public DateTime? DeterminateEnd(DateTime? stubEnd)
        {
            Contract.Ensures(Contract.Result<DateTime?>() == (End ?? stubEnd));

            return null;
        }

        public ITimeInterval Determinate(DateTime? stubBegin, DateTime? stubEnd)
        {
            Contract.Ensures(Contract.Result<ITimeInterval>() != null);
            Contract.Ensures((Contract.Result<ITimeInterval>().GetType() == GetType()));
            Contract.Ensures((Contract.Result<ITimeInterval>().Begin == DeterminateBegin(stubBegin)));
            Contract.Ensures(Contract.Result<ITimeInterval>().End == DeterminateEnd(stubEnd));

            Contract.EnsuresOnThrow<IllegalTimeIntervalException>(DeterminateBegin(stubBegin) > DeterminateEnd(stubEnd));
            Contract.EnsuresOnThrow<IllegalTimeIntervalException>(true);

            return null;
        }

        #endregion
    }
}