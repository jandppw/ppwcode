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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

/**
 * <p>This class is used to register locales supported by the application itself
 * and by the applications that it depends on.</p>
 *
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 */
@Copyright("2009 - $Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4044 $",
         date     = "$Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $")
public class LocaleManager {

  private static Hashtable<String,List<Locale>> supportedLocalesPerApplication = new Hashtable<String, List<Locale>>();

  private LocaleManager() {
  }

  /**
   * Register the list of supported locales for a certain application or library in the LocaleManager.
   * 
   * @param application       Name of the application or library for which the list of locales is registered.
   *                          We suggest to use the main package name of the application or library for this purpose.
   * @param supportedLocales  The list of locales to be registered.
   */
  public static void registerSupportedLocales(String application, List<Locale> supportedLocales) {
    List<Locale> copy = copyLocaleList(supportedLocales);
    supportedLocalesPerApplication.put(application, copy);
  }

  public static List<Locale> getSupportedLocales(String application) {
    List<Locale> original = supportedLocalesPerApplication.get(application);
    if (original == null) {
      return null;
    } else {
      return copyLocaleList(original);
    }
  }

  /**
   * Return a list of locales that are supported by all applications or libraries that registered their
   * supported locales with the LocaleManager.  This is thus the intersection of all registered lists
   * of locales.
   * 
   * @return list of locales supported by all dependent applications or libraries
   */
  public static List<Locale> getSupportedLocales() {
    List<Locale> supportedLocales = null;
    // check all registered locales
    for (String app : supportedLocalesPerApplication.keySet()) {
      if (supportedLocales == null) {
        // initialize
        supportedLocales = copyLocaleList(supportedLocalesPerApplication.get(app));
      } else {
        // remove locales not supported by a dependent application or library
        List<Locale> appSupportedLocales = supportedLocalesPerApplication.get(app);
        for (Locale locale : supportedLocales) {
          if (!appSupportedLocales.contains(locale)) {
            supportedLocales.remove(locale);
          }
        }
      }
    }
    return supportedLocales;
  }


  private static List<Locale> copyLocaleList(List<Locale> locales) {
    List<Locale> copy = new ArrayList<Locale>(locales.size());
    for (Locale l : locales) {
      copy.add(l);
    }
    return copy;
  }

}
