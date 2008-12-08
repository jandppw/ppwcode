/*<license>
Copyright 2004 - $Date: 2008-11-09 20:52:25 +0100 (Sun, 09 Nov 2008) $ by PeopleWare n.v..

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

package org.ppwcode.value_III.location;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ppwcode.util.test.contract.Contract;
import org.ppwcode.value_III.id11n._Contract_Identifier;


@SuppressWarnings("unchecked")
public class _Contract_PostalCode extends Contract<PostalCode> {

  private static final Object EMPTY = "";

  public _Contract_PostalCode() {
    super(PostalCode.class);
  }

  @Override
  public void assertInvariants(PostalCode pc) {
    super.assertInvariants(pc);
    assertEquals("\n", PostalCode.EOL);
  }

  public void assertLocalizedAddressRepresentation(PostalCode pc, PostalAddress pa, String result) {
    assertNotNull(result);
    assertFalse(EMPTY.equals(result));
    assertTrue(result.contains(pa.getStreetAddress()));
    assertTrue(result.contains(pc.getIdentifier()));
    assertTrue(result.contains(pa.getCity()));
    CountryEditor ce = new CountryEditor();
    ce.setValue(pc.getCountry());
    ce.setDisplayLocale(pa.getLocale());
    assertTrue(result.contains(ce.getLabel()));
  }

  public void assertEqualsObject(PostalCode pc, Object other, boolean result) {
    _Contract_Identifier cId = (_Contract_Identifier)getDirectSuperContracts().get(PostalCode.class);
    cId.assertEqualsObject(pc, other, result);
    if ((other != null) && (other instanceof PostalCode)) {
      boolean expected = pc.getIdentifier().equals(((PostalCode)other).getIdentifier()) &&
                         pc.getCountry().equals(((PostalCode)other).getCountry());
      assertEquals(expected, result);
    }
  }

  public void assertHashCode(PostalCode pc, int result) {
    _Contract_Identifier cId = (_Contract_Identifier)getDirectSuperContracts().get(PostalCode.class);
    cId.assertHashCode(pc, result);
  }

  public void assertToString(PostalCode pc, String result) {
    _Contract_Identifier cId = (_Contract_Identifier)getDirectSuperContracts().get(PostalCode.class);
    cId.assertToString(pc, result);
  }

}

