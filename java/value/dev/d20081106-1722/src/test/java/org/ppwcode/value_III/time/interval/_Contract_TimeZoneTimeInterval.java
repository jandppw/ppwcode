/*<license>
Copyright 2004 - $Date: 2008-11-04 23:11:14 +0100 (Tue, 04 Nov 2008) $ by PeopleWare n.v..

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
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.ppwcode.util.test.contract.Contract;


@SuppressWarnings("unchecked")
public class _Contract_TimeZoneTimeInterval extends Contract<TimeZoneTimeInterval> {

  public _Contract_TimeZoneTimeInterval() {
    super(TimeZoneTimeInterval.class);
  }

  @Override
  public void assertInvariants(TimeZoneTimeInterval ti) {
    super.assertInvariants(ti);
    assertNotNull(ti.getTimeZone());
  }

  public void assertEqualsObject(TimeZoneTimeInterval ti, Object other, boolean result) {
    _Contract_TimeInterval contract = (_Contract_TimeInterval)getDirectSuperContracts().get(TimeInterval.class);
    contract.assertEqualsObject(ti, other, result);
  }

  public void assertHashCode(TimeZoneTimeInterval ti, int result) {
    _Contract_TimeInterval contract = (_Contract_TimeInterval)getDirectSuperContracts().get(TimeInterval.class);
    contract.assertHashCode(ti, result);
  }

  public void assertToString(TimeZoneTimeInterval ti, String result) {
    _Contract_TimeInterval contract = (_Contract_TimeInterval)getDirectSuperContracts().get(TimeInterval.class);
    contract.assertToString(ti, result);
  }

  public void assertDeterminateBegin(TimeZoneTimeInterval ti, Date stubBegin, Date result) {
    _Contract_TimeInterval contract = (_Contract_TimeInterval)getDirectSuperContracts().get(TimeInterval.class);
    contract.assertDeterminateBegin(ti, stubBegin, result);
  }

  public void assertDeterminateEnd(TimeZoneTimeInterval ti, Date stubEnd, Date result) {
    _Contract_TimeInterval contract = (_Contract_TimeInterval)getDirectSuperContracts().get(TimeInterval.class);
    contract.assertDeterminateEnd(ti, stubEnd, result);
  }

  public void assertDeterminate(TimeZoneTimeInterval subject, Date stubBegin, Date stubEnd, TimeInterval result) {
    _Contract_TimeInterval contract = (_Contract_TimeInterval)getDirectSuperContracts().get(TimeInterval.class);
    contract.assertDeterminate(subject, stubBegin, stubEnd, result);
    assertEquals(subject.getTimeZone(), ((TimeZoneTimeInterval)result).getTimeZone());
  }

}

