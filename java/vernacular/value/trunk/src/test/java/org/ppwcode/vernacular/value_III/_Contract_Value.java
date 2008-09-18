/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SuppressWarnings("unchecked")
public class _Contract_Value extends _Contract_<Value> {

  public _Contract_Value() {
    super(Value.class);
  }

  @Override
  public void assertInvariants(Value v) {
    super.assertInvariants(v);
    /* consistency of hashcode with equals: cannot easily be tested;
     * we just call hashCode here, to make sure it doesn't crash
     */
    v.hashCode();
    // call toString here, to make sure it doesn't crash
    v.toString();
  }

  public void assertEqualsObject(Value v, Object other, boolean result) {
    if (result) {
      assertNotNull(other);
      assertEquals(v.getClass(), other.getClass());
    }
  }

}

