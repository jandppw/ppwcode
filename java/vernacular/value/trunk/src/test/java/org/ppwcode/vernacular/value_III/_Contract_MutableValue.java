/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;



@SuppressWarnings("unchecked")
public class _Contract_MutableValue extends _Contract_<MutableValue> {

  public _Contract_MutableValue() {
    super(MutableValue.class);
  }

  @Override
  public void assertInvariants(MutableValue mv) {
    super.assertInvariants(mv);
  }

  public void assertClone(MutableValue mv, MutableValue result) {
    assertNotSame(mv, result);
    assertEquals(mv, result);
  }

}

