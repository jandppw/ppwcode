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

package org.ppwcode.value_III.location;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.DisplayLocaleBasedEnumerationValueEditor;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * A property editor for properties of type {@link Country}.
 * This editor is chosen automatically when needed, because it is in the
 * same package as the type it is for, with the expected name.
 *
 * <p>Only get and set as text is supported for now. It is all
 *   that is needed for beans used in web application.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class CountryEditor extends DisplayLocaleBasedEnumerationValueEditor<Country> {

  /**
   * @return    Locale.class;
   */
  @MethodContract(post = @Expression("Country.class"))
  public final Class<?> getEnumerationValueType() {
    return Country.class;
  }

  /**
   * A map containing all possible values for the value type
   * {@link Country}.
   *
   * @return  Country.VALUES;
   */
  @MethodContract(post = @Expression("Country.VALUES"))
  @Override
  public final Map<String, Country> getValuesMap() {
    return Country.VALUES;
  }

  /**
   * The name of the country in the language of {@link #getDisplayLocale()}.
   * If there is no display locale, the name of the country in English.
   */
  public final String getLabel() {
    String result = "";
    if ((getValue() != null) && (getValue() instanceof Country)) {
      Country country = (Country)getValue();
      Locale localeToShow = new Locale("en", country.getValue());
            /* language is just a default (null is not accepted);
             * we are only interested in the country setting */
      if (getDisplayLocale() != null) {
        result = localeToShow.getDisplayCountry(getDisplayLocale());

      }
      else {
        Set<Locale> countryLocales = country.getLocales();
        if (countryLocales == null || countryLocales.isEmpty()) {
          result = localeToShow.getDisplayCountry(Locale.US);
        }
        else {
          Iterator<Locale> iter = countryLocales.iterator();
          while (iter.hasNext()) {
            result += localeToShow.getDisplayCountry(iter.next());
            if (iter.hasNext()) {
              result += " / ";
            }
          }
        }
      }
    }
    return result;
  }

}
