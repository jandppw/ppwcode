/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;



@SuppressWarnings("unchecked")
public class _Contract_ImmutableValue extends _Contract_<ImmutableValue> {

  public _Contract_ImmutableValue() {
    super(ImmutableValue.class);
  }

  @Override
  public void assertInvariants(ImmutableValue v) {
    super.assertInvariants(v);
    org.junit.Assert.assertTrue(!Cloneable.class.isAssignableFrom(v.getClass()));
  }

}

