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

package org.ppwcode.value_III.legacy;


import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.ppwcode.vernacular.value_III.EnumerationValue;
import org.ppwcode.vernacular.value_III.Value;



/**
 * @author    David Van Keer
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_Role extends _Test_EnumerationValue {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  public static void main(final String[] args) {
    Test.main(new String[] {"be.peopleware.value_II._Test_Role"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_Role__();
    test_VALUES();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final EnumerationValue create_EnumerationValue(final String d) {
    return create_Role();
  }

  protected Role create_Role() {
    return new Role();
  }

  public Set getCases() {
    Set result = new HashSet();
    result.add(new Role());
    result.add(Role.READER);
    result.add(Role.EDITOR);
    result.add(Role.REVIEWER);
    result.add(Role.ADMINISTRATOR);
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Value subject) {
    super.validateTypeInvariants(subject);
    validateVALUETypeInvariants(subject, Role.VALUES);
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected void test_Role__() {
    try {
      Role subject = new Role();
      validate(subject.equals(Role.READER));
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected void test_VALUES() {
   test_VALUES(Role.VALUES, Role.class);
  }

  /*</section>*/

}

