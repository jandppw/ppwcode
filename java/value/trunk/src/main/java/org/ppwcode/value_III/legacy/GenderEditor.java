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


import java.util.Map;

import org.ppwcode.vernacular.value_III.ResourceBundleBasedEnumerationValueEditor;


/**
 * A property editor for properties of type {@link Gender}.
 * This editor is chosen automatically when needed, because it is in the
 * same package as the type it is for, with the expected name.
 *
 * <p>Only get and set as text is supported for now. It is all
 *   that is needed for beans used in web application.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public final class GenderEditor extends ResourceBundleBasedEnumerationValueEditor {

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



  /*<property name="valueMap">*/
  //------------------------------------------------------------------

  /**
   *
   */
  public Map getValuesMap() {
    return Gender.VALUES;
  }

}
