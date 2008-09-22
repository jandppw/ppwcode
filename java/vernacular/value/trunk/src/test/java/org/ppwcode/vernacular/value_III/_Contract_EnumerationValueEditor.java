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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ppwcode.util.test.contract.Contract;


@SuppressWarnings("unchecked")
public class _Contract_EnumerationValueEditor extends Contract<EnumerationValueEditor> {

  public _Contract_EnumerationValueEditor() {
    super(EnumerationValueEditor.class);
  }

  @Override
  public void assertInvariants(EnumerationValueEditor eve) {
    super.assertInvariants(eve);
    assertTrue(eve.getAsText() != null ? contains(eve.getTags(), eve.getAsText()) : true);
    assertNotNull(eve.getLabelsMap());
    assertFalse(eve.getLabelsMap().keySet().contains(null));
    assertFalse(eve.getLabelsMap().values().contains(null));
  }

  private static boolean contains(Object[] array, Object object) {
    for (int i = 0; i < array.length; i++) {
      if (eqn(array[i], object)) {
        return true;
      }
    }
    return false;
  }

  private static boolean eqn(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  public void assertGetTags(EnumerationValueEditor eve, String[] result) {
    assertArrayEquals(eve.getLabelsMap().keySet().toArray(), result);
  }

  public void assertSetAsText_String_Nominal(EnumerationValueEditor eve, String tag) {
    assertEquals("".equals(tag) ? null : tag, eve.getAsText());
  }

  public void assertSetAsText_String_Exception(EnumerationValueEditor eve, String tag, IllegalArgumentException exc) {
    assertFalse(contains(eve.getTags(), tag));
  }

  public void assertGetLabel(EnumerationValueEditor eve, String result) {
    assertEquals(eve.getLabelsMap().get(eve.getAsText()), result);
  }

}

