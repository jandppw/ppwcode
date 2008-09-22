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
import static org.ppwcode.util.reflect_I.TypeHelpers.type;

import org.ppwcode.util.test.contract.Contract;



@SuppressWarnings("unchecked")
public class _Contract_AbstractValueEditor extends Contract<AbstractValueEditor> {

  public _Contract_AbstractValueEditor() {
    super(AbstractValueEditor.class);
  }

  @Override
  public void assertInvariants(AbstractValueEditor ee) {
    super.assertInvariants(ee);
  }

  public void assertGetValueType(AbstractValueEditor ee, Class<?> result) {
    assertEquals(type(ee.getExpectedValueTypeClassName()), result);
  }

  public void assertGetExpectedValueTypeClassName(AbstractValueEditor ee, String result) {
    assertEquals(ee.getClass().getName().substring(0, ee.getClass().getName().lastIndexOf("Editor")), result);
  }

}

