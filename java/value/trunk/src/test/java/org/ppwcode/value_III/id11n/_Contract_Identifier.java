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

package org.ppwcode.value_III.id11n;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.ppwcode.value_III.id11n.Identifier.EMPTY;

import org.ppwcode.util.test.contract.Contract;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.ppwcode.vernacular.value_III._Contract_ImmutableValue;


@SuppressWarnings("unchecked")
public class _Contract_Identifier extends Contract<Identifier> {

  public _Contract_Identifier() {
    super(Identifier.class);
  }

  @Override
  public void assertInvariants(Identifier id) {
    super.assertInvariants(id);
    assertNotNull(id.getIdentifier());
    assertFalse(EMPTY.equals(id.getIdentifier()));
  }

  public void assertEqualsObject(Identifier id, Object other, boolean result) {
    _Contract_ImmutableValue cValue = (_Contract_ImmutableValue)getDirectSuperContracts().get(ImmutableValue.class);
    cValue.assertEqualsObject(id, other, result);
    if ((other != null) && (other instanceof Identifier)) {
      boolean expected = id.getIdentifier().equals(((Identifier)other).getIdentifier());
      assertEquals(expected, result);
    }
  }

  public void assertHashCode(Identifier id, int result) {
//    _Contract_ImmutableValue cValue = (_Contract_ImmutableValue)getDirectSuperContracts().get(ImmutableValue.class);
  }

  public void assertToString(Identifier id, String result) {
//    _Contract_ImmutableValue cValue = (_Contract_ImmutableValue)getDirectSuperContracts().get(ImmutableValue.class);
  }

}

