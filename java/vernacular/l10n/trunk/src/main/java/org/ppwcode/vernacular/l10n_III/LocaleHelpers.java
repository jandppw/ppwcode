/*<license>
Copyright 2009 - $Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $ by PeopleWare n.v.

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

package org.ppwcode.vernacular.l10n_III;

import java.util.Enumeration;
import java.util.List;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Locale;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

/**
 * <p>This class provides utility methods for working with locales.</p>
 *
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 */
@Copyright("2009 - $Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4044 $",
         date     = "$Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $")
public final class LocaleHelpers {

  private LocaleHelpers() {
    
  }


  /**
   * This method returns the most preferred locale from the list of supported locales,
   * based on the enumeration of accepted locales.  If none of the accepted locales matches
   * with any of the supported locales, a default locale is returned.  The default locale
   * is the first locale in the list of supported locales.
   *
   * The implementation of the algorithm to determine the most preferred locale is taken
   * from the MyFaces implementation from Apache (calculateLocale in
   * org.apache.myfaces.application.jsp.JspViewHandlerImpl).
   *
   *
   * @param acceptedLocales   An Enumeration of locales in decreasing order of preference.
   * @param supportedLocales  A List of supported locales, with the default locale first.
   * @return
   */
  public static Locale findPreferredLocale(Enumeration<Locale> acceptedLocales, List<Locale> supportedLocales) {

    Locale requestLocale = null;

    // loop over all accepted locales in preferred order
    while (acceptedLocales.hasMoreElements()) {
      requestLocale = acceptedLocales.nextElement();

      // loop over supported locales
      for (Locale supportedLocale : supportedLocales) {
        // higher priority to a language match over an exact match
        // that occures further down (see Jstl Reference 1.0 8.3.1)
        if (requestLocale.getLanguage().equals(supportedLocale.getLanguage())
          && (supportedLocale.getCountry() == null || supportedLocale.getCountry().length() == 0)) {
          return supportedLocale;
        } else if (supportedLocale.equals(requestLocale)) {
          return supportedLocale;
        }
      }
    }

    return supportedLocales.get(0);
  }

}
