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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.EnumerationValue;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * A class representing a countries. Codes used are standard ISO code which
 * can be found at @link http://ftp.ics.uci.edu/pub/ietf/http/related/iso639.txt
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar     VALUES != null;
 * @invar     VALUES.size() > 0;
 * @invar     ! VALUES.keySet().contains(null);
 * @invar     ! VALUES.values().contains(null);
 * @invar     (forall Object o; VALUES.keySet().contains(o);
 *                t instanceof String);
 * @invar     (forall Object o; VALUES.values().contains(o);
 *                o.getClass() == Country.class);
 * @invar     VALUES.values().contains(this);
 * @invar     this.equals(VALUES.get(toString()));
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class Country extends EnumerationValue {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     discriminator
   *            The string that discriminates this value.
   * @pre       discriminator != null;
   * @pre       discriminator.length > 0;
   * @post      new.toString().equals(discriminator);
   */
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

}
