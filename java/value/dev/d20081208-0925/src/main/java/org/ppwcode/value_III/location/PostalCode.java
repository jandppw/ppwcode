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

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.id11n.Identifier;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Postal codes are identifiers of geographic regions in the context of
 *   {@link PostalAddress postal addresses}. Postal codes are organized per
 *   {@link Country}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface PostalCode extends Identifier {

  /**
   * {@code \n}
   */
  @Invars(@Expression("EOL = '\n'"))
  public final static String EOL = "\n";

  /**
   * The country for which this (type of) postal code is applicable.
   * In most cases, this method is implemented in a concrete class as a constant, returning
   * the same {@link Country} for all instances of that concrete class. A postal code should
   * be bound to a country, but when it is not, {@code getCountry()} returns {@code null}.
   */
  @Basic
  Country getCountry();

  /**
   * An end-user representation of {@code postalAddress},
   * in a format appropriate for the address' location, in the
   * locale of the address.
   */
  @MethodContract(
    pre  = @Expression("_postalAddress != null"),
    post = {
      @Expression("result != null"),
      @Expression("result != EMPTY"),
      @Expression("result.contains(_postalAddress.streetAddress)"),
      @Expression("result.contains(identifier)"),
      @Expression("result.contains(_postalAddress.city)"),
      @Expression("for (CountryEditor ce) {" +
                    "ce.value == _postalAddress.country &&" +
                    "ce.displayLocale = _postalAddress.locale ?" +
                    "result.contains(ce.label)" +
                  "}")
    }
  )
  String localizedAddressRepresentation(PostalAddress postalAddress);

  /**
   * With this extension, it is also necessary to override {@link #hashCode()}.
   */
  @MethodContract(post = @Expression("result ? country = other.country"))
  boolean equals(Object other);

}
