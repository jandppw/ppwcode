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

package org.ppwcode.value_III.legacy.hibernate2;


import org.ppwcode.value_III.legacy.Country;
import org.ppwcode.value_III.legacy.CountryEditor;
import org.ppwcode.vernacular.value_III.hibernate2.AbstractEnumerationUserType;


/**
 * Hibernate mapping for {@link Country}. Country ISO codes
 * are stored in a VARCHAR.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @deprecated
 */
@Deprecated
public final class CountryUserType extends AbstractEnumerationUserType {

  /*<section name="Meta Information">*/
  //  ------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /**
   * Create a new {@link CountryUserType}.
   *
   * @post new.getEnumerationValueEditor() instanceof CountryEditor;
   */
  public CountryUserType() {
    super(new CountryEditor());
  }

}
