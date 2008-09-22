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

package org.ppwcode.vernacular.value_III;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.ppwcode.util.reflect_I.ConstantHelpers;
import org.ppwcode.util.test.contract.Contract;


@SuppressWarnings("unchecked")
public class _Contract_AbstractEnumerationValueEditor extends Contract<AbstractEnumerationValueEditor> {

  public _Contract_AbstractEnumerationValueEditor() {
    super(AbstractEnumerationValueEditor.class);
  }

  @Override
  public void assertInvariants(AbstractEnumerationValueEditor eve) {
    super.assertInvariants(eve);
  }

  public void assertGetLabel(AbstractEnumerationValueEditor eve, String result) {
    _Contract_EnumerationValueEditor eveC = (_Contract_EnumerationValueEditor)getDirectSuperContracts().get(EnumerationValueEditor.class);
    eveC.assertGetLabel(eve, result);
  }




  public void assertGetValueType(AbstractEnumerationValueEditor eve, Class<?> result) {
//    _Contract_EnumerationValueEditor eveC = (_Contract_EnumerationValueEditor)getDirectSuperContracts().get(EnumerationValueEditor.class);
    _Contract_AbstractValueEditor aveC = (_Contract_AbstractValueEditor)getDirectSuperContracts().get(AbstractValueEditor.class);
    aveC.assertGetValueType(eve, result);
  }

  public void assertGetExpectedValueTypeClassName(AbstractEnumerationValueEditor eve, String result) {
//    _Contract_EnumerationValueEditor eveC = (_Contract_EnumerationValueEditor)getDirectSuperContracts().get(EnumerationValueEditor.class);
    _Contract_AbstractValueEditor aveC = (_Contract_AbstractValueEditor)getDirectSuperContracts().get(AbstractValueEditor.class);
    aveC.assertGetExpectedValueTypeClassName(eve, result);
  }

  public void assertGetTags(AbstractEnumerationValueEditor eve, String[] result) {
    _Contract_EnumerationValueEditor eveC = (_Contract_EnumerationValueEditor)getDirectSuperContracts().get(EnumerationValueEditor.class);
    eveC.assertGetTags(eve, result);
    assertArrayEquals(eve.getValuesMap().keySet().toArray(), result);
  }

  public void assertGetValuesMap(AbstractEnumerationValueEditor eve, Map<String, ?> result) {
    assertEquals(ConstantHelpers.constant(eve.getValueType(), "VALUES"), result);
  }

  public void assertGetAsText(AbstractEnumerationValueEditor eve, String result) {
    assertEquals(eve.getValue() == null ? null : eve.getValue().toString(), result);
  }

  public void assertSetAsText_String_Nominal(AbstractEnumerationValueEditor eve, String tag) {
    _Contract_EnumerationValueEditor eveC = (_Contract_EnumerationValueEditor)getDirectSuperContracts().get(EnumerationValueEditor.class);
    eveC.assertSetAsText_String_Nominal(eve, tag);
    assertEquals(tag == null || tag.equals("") ? null : eve.getValuesMap().get(tag), eve.getValue());
  }

  public void assertSetAsText_String_Exception(AbstractEnumerationValueEditor eve, String tag, IllegalArgumentException exc, Object oldValue) {
    _Contract_EnumerationValueEditor eveC = (_Contract_EnumerationValueEditor)getDirectSuperContracts().get(EnumerationValueEditor.class);
    eveC.assertSetAsText_String_Exception(eve, tag, exc);
    assertNotNull(tag);
    assertFalse("".equals(tag));
    assertNull(eve.getValuesMap().get(tag));
    assertEquals(oldValue, eve.getValue());
  }

}

