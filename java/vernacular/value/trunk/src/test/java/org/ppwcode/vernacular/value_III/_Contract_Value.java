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

import static org.junit.Assert.assertNotNull;

import org.ppwcode.util.test.contract.Contract;


@SuppressWarnings("unchecked")
public class _Contract_Value extends Contract<Value> {

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
    }
  }

}

