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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.value_III.time.DateHelpers.le;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.EQUALS;
import static org.ppwcode.value_III.time.interval.TimeIntervalRelation.timeIntervalRelation;

import java.util.Date;

import org.ppwcode.util.test.contract.Contract;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.ppwcode.vernacular.value_III._Contract_ImmutableValue;


@SuppressWarnings("unchecked")
public class _Contract_TimeInterval extends Contract<TimeInterval> {

  public _Contract_TimeInterval() {
    super(TimeInterval.class);
  }

  @Override
  public void assertInvariants(TimeInterval ti) {
    super.assertInvariants(ti);
    assertTrue(ti.getEnd() != null ? le(ti.getBegin(), ti.getEnd()) : true);
//    assertEquals(delta(ti.getBegin(), ti.getEnd()), ti.getDuration()); MUDO
    assertFalse(ti.getBegin() == null && ti.getEnd() == null && ti.getDuration() == null);
  }

  public void assertEqualsObject(TimeInterval ti, Object other, boolean result) {
    _Contract_ImmutableValue cValue = (_Contract_ImmutableValue)getDirectSuperContracts().get(ImmutableValue.class);
    cValue.assertEqualsObject(ti, other, result);
    if ((other != null) && (other instanceof TimeInterval)) {
      boolean expected = timeIntervalRelation(ti, (TimeInterval)other) == EQUALS;
      assertEquals(expected, result);
    }
  }

  public void assertHashCode(TimeInterval ti, int result) {
//    _Contract_ImmutableValue cValue = (_Contract_ImmutableValue)getDirectSuperContracts().get(ImmutableValue.class);
  }

  public void assertToString(TimeInterval ti, String result) {
//    _Contract_ImmutableValue cValue = (_Contract_ImmutableValue)getDirectSuperContracts().get(ImmutableValue.class);
  }

  public void assertDeterminateBegin(TimeInterval ti, Date stubBegin, Date result) {
    assertEquals(ti.getBegin() != null ? ti.getBegin() : stubBegin, result);
  }

  public void assertDeterminateEnd(TimeInterval ti, Date stubEnd, Date result) {
    assertEquals(ti.getEnd() != null ? ti.getEnd() : stubEnd, result);
  }

  public void assertDeterminate(TimeInterval subject, Date stubBegin, Date stubEnd, TimeInterval result) {
    assertNotNull(result);
    assertEquals(subject.determinateBegin(stubBegin), result.getBegin());
    assertEquals(subject.determinateEnd(stubEnd), result.getEnd());
  }

}

