/*<license>
Copyright 2004 - $Date: 2008-10-19 00:39:45 +0200 (Sun, 19 Oct 2008) $ by PeopleWare n.v..

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

package org.ppwcode.value_III.time.period;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Date;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.Duration;
import org.ppwcode.vernacular.value_III.ImmutableValue;


/**
 * <p>A period is a time interval between 2 points in time.</p>
 * <p>Time in general, and periods in particular, are treacherously difficult. For periods, we impose a few choices
 *   that, in our experience, are good choices.</p>
 *
 * <h3>Interval</h3>
 * <p>We choose for all periods to express <em>half, right-open</em> time intervals. A consistent half-open interval
 *   helps in avoiding awkward border conditions. We have chosen to always use half, <em>right</em>-open intervals
 *   for several reasons, stemming from philosophy, the similarity between limitations in physics due to the finite
 *   speed of light and processing and remote communication, the similarity between the impossibility to express
 *   simultaneity of events in physics due to relativity (see
 *   <a href="http://www.amazon.com/About-Time-Einsteins-Unfinished-Revolution/dp/0684818221/ref=sr_1_1?ie=UTF8&s=books&qid=1224448626&sr=1-1">About
 *   Time: Einstein's Unfinished Revolution; <cite>Paul Davies</cite></a>) and the drift of clocks in different
 *   computers in distributed systems, and other half-baked (or half-drunk) reflections. If you bring a bottle, we can
 *   talk about this.</p>
 *
 * <h3>Three properties and incomplete data</h3>
 * <p>A period has a start time, and end time, and a duration, wich are interrelated. The interface does not define
 *   which of the 2 of those 3 should be stored, and which should be calculated.</p>
 * <p>The start date, the end date and the duration can be {@code null}. The semantics of this is to be defined by
 *   the user case by case. When one basic property is {@code null}, there can be no calculations, and the derived
 *   property will be {@code null} also. We often encounter the use case of open-ended periods: an employment spans
 *   a period of time, but during most of that period, only the start time is known. In such cases, a mandatory
 *   start date, and an unknown end date and duration is used, until the end date is decided on.</p>
 *
 * <h3>Algebra</h3>
 * <p>For reasoning with periods, it is important to understand
 *   <a href="http://en.wikipedia.org/wiki/Allen's_Interval_Algebra">Allen's Interval Algebra</a>. The {@code Period}
 *   type offers methods for each of the 13 relationships of this algebra.</p>
 *
 * <h3>Implementations</h3>
 * <p>This package offers many subtypes of {@code Period}. We believe it is, in this case, more appropriate to
 *   introduce different subtypes for different constraints on periods, instead of limiting a general period
 *   implementation in the use code. This way, we can add specialized user interfaces, that are tweaked to specific
 *   constraints. E.g., a period that uses days as a time quant, will have a simpler user interface than a period
 *   that needs a user interface that is precise to the millisecond.</p>
 *
 * @author    Nele Smeets
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar     (getStartDate() != null && getEndDate() != null)
 *                ? getStartDate().before(getEndDate())
 *                : true;
 */
@Copyright("2008 - $Date: 2008-10-19 00:39:45 +0200 (Sun, 19 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3187 $",
         date     = "$Date: 2008-10-19 00:39:45 +0200 (Sun, 19 Oct 2008) $")
public interface Period extends ImmutableValue {

  Date getStartDate();

  Date getEndDate();

  Duration getDuration();

}
