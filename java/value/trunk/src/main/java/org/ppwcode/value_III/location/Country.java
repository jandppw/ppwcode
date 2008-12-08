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


import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.EnumerationValue;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * A class representing a countries. Codes used are standard ISO 3166-1 alpha-2 code which can be found at
 * <a href="http://en.wikipedia.org/wiki/ISO_3166-1">http://en.wikipedia.org/wiki/ISO_3166-1</a>.
 * An extra "no country" country is added with a space as discriminator.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("VALUES.values().contains(this)"),
  @Expression("this == VALUES[value]")
})
public final class Country extends EnumerationValue {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     discriminator
   *            The string that discriminates this value. This should be an ISO
   *            country code.
   */
  @MethodContract(
    pre  = {
      @Expression("_discriminator != null"),
      @Expression("_discriminator.length > 0")
    },
    post = @Expression("result.toString().equals(_discriminator)")
  )
  private Country(final String discriminator) {
    super(discriminator);
  }

  /**
   * Enumeration types require a default constructor for serializability. It is ill-advised to use
   * this default constructor yourself. Use the constants instead.
   *
   * @post    new.toString().equals("BE");
   */
  @MethodContract(post = @Expression("value == 'BE'"))
  public Country() {
    this("BE");
  }

  /*</construction>*/



  /**
   * A map containing all possible values for this value type.
   */
  @Invars({
    @Expression("VALUES != null"),
    @Expression("! VALUES.keySet().contains(null)"),
    @Expression("! VALUES.values().contains(null)"),
    @Expression("for (String cc : VALUES.keySet()) {VALUES[cc].value == cc}"),
    @Expression("for (Country c : VALUES.values()) {c != NO_COUNTRY ? Locale.getISOCountries().contains(c.value)}"),
    @Expression("for (String cc : Locale.getISOCountries()) {VALUES[cc] != null}")
  })
  public static final Map<String, Country> VALUES = countriesGenerator();

  private static Map<String, Country> countriesGenerator() {
    Map<String, Country> result = new HashMap<String, Country>();
    result.put(" ", new Country(" "));
    String[] isoCodes = Locale.getISOCountries();
    for (int i = 0; i < isoCodes.length; i++) {
      result.put(isoCodes[i], new Country(isoCodes[i]));
    }
    return Collections.unmodifiableMap(result);
  }


  /**
   * The country object representing no country (N/A). It was important to add this
   * in the context of easy processing of HTML forms.
   */
  @Invars(@Expression("NO_COUNTRY = VALUES[SPACE]"))
  public static Country NO_COUNTRY = VALUES.get(" ");


  private final static Map<Country, Set<Locale>> PRIVATE_COUNTRY_LOCALES = new HashMap<Country, Set<Locale>>();
  static {
    Locale[] ls = Locale.getAvailableLocales();
    for (Locale l : ls) {
      Set<Locale> rs = countryLocaleSet(VALUES.get(l.getCountry()));
      rs.add(l);
    }
    addLocale(VALUES.get("BE"), "de");
    addLocale(VALUES.get("CD"), "fr");
    makeSetsImmutable();
  }

  private static void addLocale(Country c, String isoLanguageCode) {
    Set<Locale> sl = countryLocaleSet(VALUES.get(c.getValue()));
    sl.add(new Locale(isoLanguageCode, c.getValue()));
  }

  private static Set<Locale> countryLocaleSet(Country c) {
    Set<Locale> rs = PRIVATE_COUNTRY_LOCALES.get(c);
    if (rs == null) {
      rs = new HashSet<Locale>();
      PRIVATE_COUNTRY_LOCALES.put(c, rs);
    }
    assert rs != null;
    return rs;
  }

  private static void makeSetsImmutable() {
    for (Map.Entry<Country, Set<Locale>> me : PRIVATE_COUNTRY_LOCALES.entrySet()) {
      me.setValue(unmodifiableSet(me.getValue()));
    }
  }

  /**
   * An (incomplete) list of the locales for a given country.
   *
   * @todo Add more than the locale offered in Java EE. E.g., there is not de for BE!
   *       See e.g. http://www.infoplease.com/ipa/A0855611.html
   */
  public final static Map<Country, Set<Locale>> COUNTRY_LOCALES = unmodifiableMap(PRIVATE_COUNTRY_LOCALES);

  /**
   * The locales in this country.
   */
  @MethodContract(post = @Expression("COUNTRY_LOCALES[this]"))
  public final Set<Locale> getLocales() {
    return COUNTRY_LOCALES.get(this);
  }

}
