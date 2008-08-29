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


import java.io.Serializable;
import java.util.Locale;


/**
 * <p>For i18n, the features provided by {@link Locale}
 *   are used. The returned label is either in the
 *   requested language, if the {@link #getDisplayLocale()}
 *   is set, or in the language of the {@link #getValue()}
 *   displayed locale itself, if the {@link #getDisplayLocale()}
 *   is not set.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class DisplayLocaleBasedEnumerationValueEditor
    extends AbstractEnumerationValueEditor implements Serializable {

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



  /*<property name="display locale">*/
  //------------------------------------------------------------------

  /**
   * This locale is used to determine the i18n {@link #getLabel() label}
   * for a {@link Locale}. It can be <code>null</code>.
   *
   * @basic
   */
  public final Locale getDisplayLocale() {
    return $displayLocale;
  }

  /**
   * @post      new.getDisplayLocale() == displayLocale;
   */
  public final void setDisplayLocale(final Locale displayLocale) {
    $displayLocale = displayLocale;
  }

  private Locale $displayLocale;

  /*</property>*/

}
