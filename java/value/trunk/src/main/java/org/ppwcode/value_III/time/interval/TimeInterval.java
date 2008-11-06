/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.value_III.time.interval;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Date;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.Duration;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>A time interval is a duration between 2 points in time. <strong>Time in general, and intervals in particular,
 *   are treacherously difficult to reason a about. Beware.</strong> Experience shows that falling back to
 *   the begin and end dates as separated dates and reasoning about them does not solve the complexity, and results
 *   in even more difficult reasonings and is even more error prone.</p>
 * <p><em>We strongly suggest that you use {@link TimeIntervalRelation} for reasoning about time intervals.</em></p>
 * <p>For time intervals, we impose a few choices that, in our experience, are good choices.</p>
 *
 * <h3>Interval</h3>
 * <p>We choose for all time intervals to express <em>half, right-open</em> time intervals. A consistent half-open
 *   interval helps in avoiding awkward border conditions. We have chosen to always use half, <em>right</em>-open
 *   intervals for several reasons, stemming from philosophy, the similarity between limitations in physics due to
 *   the finite speed of light and processing and remote communication, the similarity between the impossibility to
 *   express simultaneity of events in physics due to relativity (see
 *   <a href="http://www.amazon.com/About-Time-Einsteins-Unfinished-Revolution/dp/0684818221/ref=sr_1_1?ie=UTF8&amp;s=books&amp;qid=1224448626&amp;sr=1-1">About
 *   Time: Einstein's Unfinished Revolution; <cite>Paul Davies</cite></a>) and the drift of clocks in different
 *   computers in distributed systems, and other half-baked (or half-drunk) reflections. If you bring a bottle, we
 *   can talk about this.<p>
 *
 * <h3>Three properties and incomplete data</h3>
 * <p>A time interval has a {@link #getBegin() begin time}, an {@link #getEnd() end time}, and a
 *   {@link #getDuration() duration}, which are interrelated. The interface does not define which of the 2 of those
 *   3 should be stored, and which should be calculated.</p>
 * <p>The begin date, the end date and the duration can be {@code null}. The semantics of this is to be defined by
 *   the user case by case. We often encounter the use case of open-ended time intervals: an employment spans a time
 *   interval of time, but during most of that time interval, only the begin time is known. In such cases, a
 *   mandatory begin date, and an unknown end date and duration is used, until the end date is decided on. How to deal
 *   with unknown or constrained begin and end times is described in {@link TimeIntervalRelation}.</p>
 * <p>When one basic property is {@code null}, there can be no calculations, and the derived property will be
 *   {@code null} also.</p>
 * <p>{@code TimeInterval TimeIntervals} with all 3 properties {@code null} make no sense
 *   and are prohibited.</p>
 *
 * <h3>Implementations</h3>
 * <p>This package offers many subtypes of {@code TimeInterval}. We believe it is, in this case, more appropriate to
 *   introduce different subtypes for different constraints on time intervals, instead of limiting a general time
 *   interval implementation in the use code. This way, we can add specialized user interfaces, Hibernate user types,
 *   JPA value handlers, etcetera, that are tweaked to specific constraints. E.g., a time interval that uses days as
 *   a time quant, will have a simpler user interface than a time interval that needs a user interface that is precise
 *   to the millisecond.</p>
 * <p>Since this is an {@link ImmutableValue}, implementations should offer a good public constructor, and should
 *   be declared {@code final}.
 *
 * @author Nele Smeets
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
   date     = "$Date$")
@Invars({@Expression("end != null ? le(begin, end)"),
         @Expression("duration = delta(begin, end)"),
         @Expression("! (begin == null && end == null && duration == null)")})
public interface TimeInterval extends ImmutableValue {

  /**
   * This time interval begins at this time, inclusive. This can be {@code null},
   * with semantics to be determined by the user.
   */
  @Basic
  Date getBegin();

  /**
   * This time interval ends at this time, exclusive. This can be {@code null},
   * with semantics to be determined by the user.
   */
  @Basic
  Date getEnd();

  /**
   * The duration of this time interval. This can be {@code null},
   * with semantics to be determined by the user.
   */
  @Basic
  Duration getDuration();

  /**
   * Equals as defined by {@link TimeIntervalRelation#timeIntervalRelation(TimeInterval, TimeInterval)} and {@link TimeIntervalRelation#EQUALS}.
   */
  @MethodContract(post = @Expression("TimeIntervalRelation.allenRelation(this, other) == EQUALS"))
  boolean equals(Object other);

  /**
   * Return a determinate begin. If we don't have one, return {@code stubBegin}.
   */
  @MethodContract(post = @Expression("begin != null ? begin : _stubBegin"))
  Date determinateBegin(Date stubBegin);

  /**
   * Return a determinate end. If we don't have one, return {@code stubEnd}.
   */
  @MethodContract(post = @Expression("end != null ? end : _stubEnd"))
  Date determinateEnd(Date stubEnd);

  /**
   * Return a (more) determinate time interval than this, i.e., replace a {@code null}
   * begin and end by {@code stubBegin} and {@code stubEnd}.
   * This is introduced in support of reasoning with unknown but constrained begin and end dates.
   * See {@link TimeIntervalRelation}, &quot;Reasoning with unknown but constrained begin and end dates&quot;
   * for more information.
   */
  @MethodContract(
    post = {
      @Expression("result != null"),
      @Expression("result.begin == determinateBegin(_stubBegin)"),
      @Expression("result.end == determinateEnd(_stubEnd)")
    },
    exc  = {
      @Throw(type = IllegalTimeIntervalException.class,
             cond = @Expression("! le(determinateBegin(_stubBegin), determinateEnd(_stubEnd))")),
      @Throw(type = IllegalTimeIntervalException.class,
             cond = @Expression("true"))
    }
  )
  TimeInterval determinate(Date stubBegin, Date stubEnd) throws IllegalTimeIntervalException;

}
