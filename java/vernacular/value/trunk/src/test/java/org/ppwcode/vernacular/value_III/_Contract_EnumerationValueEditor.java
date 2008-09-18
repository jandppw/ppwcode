/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



@SuppressWarnings("unchecked")
public class _Contract_EnumerationValueEditor extends _Contract_<EnumerationValueEditor> {

  public _Contract_EnumerationValueEditor() {
    super(EnumerationValueEditor.class);
  }

  @Override
  public void assertInvariants(EnumerationValueEditor eve) {
    super.assertInvariants(eve);
    assertNotNull(eve.getEnumerationValueType());
    assertTrue(eve.getAsText() != null ? eve.getEnumerationValueType().isInstance(eve.getValue()) : true);
    assertTrue(eve.getEnumerationValueType().isInstance(eve.getValue()) ? eve.getAsText() != null : true);
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
    assertEquals(tag, eve.getAsText());
  }

  public void assertSetAsText_String_Exception(EnumerationValueEditor eve, String tag, IllegalArgumentException exc) {
    assertFalse(contains(eve.getTags(), tag));
  }

  public void assertGetLabel(EnumerationValueEditor eve, String result) {
    assertEquals(eve.getLabelsMap().get(eve.getAsText()), result);
  }

}

