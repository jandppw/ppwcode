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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.Serializable;
import java.util.Locale;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>For i18n, the features provided by {@link Locale} are used. The returned label is either in the
 *   requested language, if the {@link #getDisplayLocale()} is set, or in the language of the
 *   {@link #getValue()} displayed locale itself, if the {@link #getDisplayLocale()} is not set.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @mudo UNFINISHED
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class DisplayLocaleBasedEnumerationValueEditor
    extends AbstractEnumerationValueEditor implements Serializable {



  /*<property name="display locale">*/
  //------------------------------------------------------------------

  /**
   * This locale is used to determine the i18n {@link #getLabel() label}
   * for a {@link Locale}. It can be <code>null</code>.
   */
  @Basic
  public final Locale getDisplayLocale() {
    return $displayLocale;
  }

  @MethodContract(post = @Expression("displayLocal == _displayLocale"))
  public final void setDisplayLocale(final Locale displayLocale) {
    $displayLocale = displayLocale;
  }

  private Locale $displayLocale;

  /*</property>*/

}
