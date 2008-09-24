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
public class _Test_Country extends _Test_EnumerationValue {

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
    Test.main(new String[] {"be.peopleware.value_II._Test_Country"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_Country__();
    test_VALUES();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final EnumerationValue create_EnumerationValue(final String d) {
    return create_Country();
  }

  protected Country create_Country() {
    return new Country();
  }

  public Set getCases() {
    Set result = new HashSet();
    result.add(create_Country());
    result.addAll(Country.VALUES.values());
    return result;
  }

  public Set getSomeCases() {
    Set result = new HashSet();
    result.add(Country.VALUES.get("BE")); //$NON-NLS-1$
    result.add(Country.VALUES.get("US")); //$NON-NLS-1$
    result.add(Country.VALUES.get("FR")); //$NON-NLS-1$
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Value subject) {
    super.validateTypeInvariants(subject);
    validateVALUETypeInvariants(subject, Country.VALUES);
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected void test_Country__() {
    try {
      Country subject = new Country();
      validate(subject.equals(Country.VALUES.get("BE"))); //$NON-NLS-1$
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected void test_VALUES() {
    test_VALUES(Country.VALUES, Country.class);
   }

  /*</section>*/

}

