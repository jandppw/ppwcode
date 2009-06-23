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

import java.util.Locale;
import java.util.regex.Pattern;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.AbstractImmutableValue;
import org.ppwcode.vernacular.value_III.ValueException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>General polymorph type for addresses.</p>
 *
 * <h3>Evaluation of historic practices</h3>
 * <p>Addresses are, globally, a difficult data type. Over the years, a lot of approaches are
 *   tried. The approach most often encountered historically, is to create a structured
 *   data type, containing street, number, bus number, postal code, city, state, country
 *   (planet, sun, galaxy, universe, ... see <cite>A Portrait of the Artist as a Young Man,
 *   James Joyce, 1916</cite>). This proves to be a problem in a global context, and does not
 *   offer much benefits.</p>
 * <p>Over different locales, it turns out that the structure differs substantially. The concept
 *   of state is irrelevant in a postal address in most regions outside the USA and Canada.
 *   The street, number, bus number substructure doesn't apply in many cases, and if they do,
 *   there are large local differences in how the are presented, notably in the order in which
 *   they appear.<br />
 *   Structuring addresses is of importance when they are stored in an RDBMS, because it enables
 *   search on the fields. In practice, searching on number and bus number is rarely needed or
 *   sensible. This is also true in the great majority of cases for the street name.<br />
 *   There is thus little to say against having 1 street address field, and giving up its internal
 *   street, number, bus number structure. Finding streets can be tackled with a {@code LIKE %...%}
 *   search.<br />
 *   However, in a sizable amount of cases, another internal structure is encountered in reality.
 *   Large office buildings, e.g., sometimes have a name, and their own internal structure, which
 *   doesn't fit the general structure. This then is sometimes tackled by adding multiple street
 *   address fields, without further internal structure. But that makes searching streets more
 *   difficult.</p>
 * <p>On the other end of the spectrum, addresses can be represented as one unstructured String.
 *   For many applications, this might be sufficient, but it makes searching on country, postal
 *   number or city name very difficult.</p>
 * <p>Apart from searching, a second concern is validation. Although globally the form of addresses
 *   is not standardized, there are standards for most locales. Many existing applications
 *   apply structure, apart from search requirements, for validation reasons. E.g., we want to
 *   validate that a postal code is correctly formatted, and that it is an existing postal code.
 *   We might also want to validate that all fields of an address that are required in your
 *   locale are indeed filled out.<br />
 *   We also often encounter a separate enumeration value / table that holds postal code / city
 *   name pairs. This can then be used to lookup the one given the other, or to validate that a
 *   combination is valid. In practice however, certainly in a global context, this fails. First of
 *   all, the postal code / city name combination is often not one-on-one, and often even not
 *   one-to-many or many-to-one. Secondly, it is difficult to get an authoritative list, even for
 *   your own locale, and furthermore such lists change. If such a lookup table or enumeration
 *   type is installed in an application, it is also required to install a business process to keep
 *   the lists up to date, and we never encountered that in any business. We might also take into
 *   account that postal codes were introduced historically to do away with discussions about the
 *   atomicity of city references in postal addresses. In Belgium, e.g., smaller municipalities
 *   (as a political unit) were merged in fewer, larger municipalities in the last quarter of
 *   the previous century, but still there is no consensus on whether to use the name of the
 *   larger political municipality in a postal address, or rather the name of the smaller entity,
 *   the village. In other locales, sometimes the name of an inner city region, or a district,
 *   or a quarter is used.</p>
 * <p>In the context of validation, it makes sense to evaluate the need for it before we proceed.
 *   We can probably discern 3 use cases of postal addresses in IT systems, with different
 *   requirements in this respect. We believe we can assume that a business is interested in
 *   addresses only to be able to contact business relations (in a large number of cases), and
 *   that thus it is important for the business for the addresses it keeps to correct, or as
 *   correct as possible. In the first use case, it is internal personnel that enters and maintains
 *   the address data. Since it is the business has a large incentive to enter and keep address
 *   data correct, the need for validation is less. We can thus expect that the organization in some
 *   way transfers this incentive to the employees that input and maintain the address data.
 *   We believe the cost of offering full validation in this case, relative to what we can
 *   actually achieve in this respect, is too high to implement this as a backup for lacking
 *   employee motivation. In the second use case, we solicit outside users of our system for
 *   address data. Most often this applies in cases where a large number of correct addresses
 *   have high value for the organization. Gathering correct addresses is probably a business
 *   goal on its own, and we exchange something of value with the user in return for getting
 *   his address. In this case, there is little incentive for the outside user to enter correct
 *   information, and it seems validation is in order. However, as each reader of this text knows
 *   from personal experience, no matter how much validation is thrown onto such an application,
 *   we can always enter a fake address, which might be syntactically correct, if we are not
 *   inclined to share our personal data. In other words, the validation does not help that much
 *   in the business goal. The third use case is similar to the first one. Here, outside users
 *   are asked to enter and maintain their address, but in a case where they themselves have
 *   a need for the address to be correct. Again, here we can rely on the need of the user,
 *   and the help of validation is marginal.<br />
 *   In conclusion, there are good reasons, as always, for data validation. However, the cost
 *   of validation in this context is very high, and the benefits are marginal.</p>
 * <p>Finally we want to mention locales that are multi-lingual. The name of the city, as well
 *   as the name of the street or the name of the building, can exist in different languages.
 *   This certainly makes searching a lot harder, and makes lookup as described above for
 *   postal codes / city name pairs, and maintenance of this lookup data, virtually impossible.</p>
 *
 * <h3>Choices</h3>
 * <p>We understand that the implementation of addresses presented here is incomplete and
 *   insuffucient. Like the rest of this library, this should be considered a given version,
 *   open to amelioration. Users should present problems and modifications for inclusion in a
 *   next version.</p>
 * <p>This version is based on the following assumptions:</p>
 * <ul>
 *   <li>Every postal address is in a definite {@link Country}. Note that this precludes snail mail
 *     to the ISS (duh) and possibly ships in international waters, and might be problematic in
 *     conflict regions.</li>
 *   <li>Every postal address has a definite postal code. We thus assume that every country
 *     has a system of postal codes, and that postal codes are organized per country.</li>
 *   <li>Structure on a higher level than cities, below the country level, are assumed to be
 *     handled by the postal code system of the given country. E.g., in the US, the state is
 *     represented as a 2 letter-code in the postal code.</li>
 *   <li>Further structuring is not cost-effective and not needed.</li>
 * </ul>
 *
 * <h3>Framework</h3>
 * <p>All postal address share this class, which defines the minimal needs for a postal address.
 *   All fields are required. Variations per country are delegated to the dynamic type of the
 *   {@link PostalCode}.</p>
 * <p>Below the level of the postal code or city, the address is free-form. City names are required,
 *   but free form, and there is no programmatic link with the postal code. It is the responsibility
 *   of the end user to enter correct data. Other incentives should be provived to end users to enter
 *   and maintain address data correctly.</p>
 * <p>It is up to the end user to choose one consistent language for city names and street names,
 *   where this is applicable. The end user should be informed that, in choosing the language of names
 *   of cities and streets, data should be entered in the language of the recipient, if possible.
 *   This is a matter of courtesy to the recipient, but functionally we can assume that the postal
 *   workers, who are the actual public at which a postal address is aimed, also will speak that
 *   language. The latter is especially important with city names of large cities, for which
 *   translations exist in other languages.</p>
 * <p>Actual subtypes will also implement a {@link #getLocalizedRepresentation() unified String
 *   representation of the address}, which is localized into a form applicable for the country
 *   in question. This implementation will return a pure String representation, applying end-of-lines
 *   and no other formatting. We are limited to UTF-8 representations in this version.</p>
 *
 * <p>This type and its subtypes should be persisted uniformly, independent of the specific subtypes.
 *   In a relational database, all postal addresses will be stored in 4 columns, in the table of the
 *   entity of which the address is a property value:</p>
 * <ul>
 *   <li>a {@code postal_code} (2 x {@code VARCHAR}) columns, using persistence support available for
 *     {@link PostalCode}; this incorporates the country</li>
 *   <li>a {@code city} ({@code VARCHAR})</li>
 *   <li>a {@code street_address} ({@code VARCHAR}); this might contain end-of-lines</li>
 * </ul>
 * <p>The {@link #getLocalizedRepresentation()} is not persisted.</p>
 * <p>End of lines are always represented as <code>\n</code>. Other representations are not allowed.
 *   End of lines can (and probably must) occur in {@link #getStreetAddress()} and
 *   {@link #getLocalizedRepresentation()}. End of lines are not allowed in {@link #getCity()}.</p>
 * <p>User interfaces should present separate fields for the street address, postal code, city name,
 *   and country. The order in which they are presented can depend dynamically on the choosen
 *   country. The street address should be a multi-line free-text field.
 *   The postal code should be represented in 2 fields, one for the country and one for the code
 *   itself. The latter might apply formatting or validation based on the actual postal code for the
 *   choosen country. The country can be represented as a text field with auto-completion, or a
 *   combo-box or (when a limited numbers of countries is supported), radio buttons. Only known countries
 *   are allowed. The city name should be a single-line free-text field. Lookup links and auto-completion
 *   for postal code and city name are allowed as user assistance, but should not be enforced.</p>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("postalCode.country.locales.contains(locale)"))
public final class PostalAddress extends AbstractImmutableValue {

  // TODO (dvankeer): Don't we need a serial version UID?
  
  @MethodContract(
    post = {
      @Expression("postalCode == _postalCode"),
      @Expression("locale == _locale"),
      @Expression("city == _city"),
      @Expression("streetAddress == _streetAddress")
    },
    exc  = {
      @Throw(type = ValueException.class,
             cond = @Expression("_postalCode == null")),
      @Throw(type = ValueException.class,
             cond = @Expression("_locale == null")),
      @Throw(type = ValueException.class,
             cond = @Expression("_city == null || _city == ''")),
      @Throw(type = ValueException.class,
             cond = @Expression("_city.matches('.*[\\n\\r].*')")),
      @Throw(type = ValueException.class,
             cond = @Expression("_streetAddress == null || _streetAddress == ''")),
      @Throw(type = ValueException.class,
             cond = @Expression("! _postalCode.country.locales.contains(_locale)")),
    }
  )
  public PostalAddress(final PostalCode postalCode, final Locale locale, final String city, final String streetAddress)
      throws ValueException {
    // MUDO compound exception
    if (postalCode == null) {
      throw new ValueException(PostalAddress.class, "POSTALCODE_MANDATORY", null);
    }
    if (locale == null) {
      throw new ValueException(PostalAddress.class, "LOCALE_MANDATORY", null);
    }
    if (city == null || EMPTY.equals(city)) {
      throw new ValueException(PostalAddress.class, "CITY_MANDATORY", null);
    }
    if (Pattern.compile(".*[\\n\\r]+.*", Pattern.DOTALL).matcher(city).matches()) {
      throw new ValueException(PostalAddress.class, "NO_EOL_ALLOWED_IN_CITY", null);
    }
    if (streetAddress == null || EMPTY.equals(streetAddress)) {
      throw new ValueException(PostalAddress.class, "STREETADDRESS_MANDATORY", null);
    }
    if (! postalCode.getCountry().getLocales().contains(locale)) {
      throw new ValueException(PostalAddress.class, "LOCALE_NOT_OF_COUNTRY", null);
    }
    $postalCode = postalCode;
    $locale = locale;
    $city = city;
    $streetAddress = streetAddress;
  }

  /**
   * The locale of this address.
   */
  @Basic(invars = @Expression("locale != null"))
  public Locale getLocale() {
    return $locale;
  }

  private final Locale $locale;

  /**
   * The postal code and the country of this address.
   */
  @Basic(invars = @Expression("postal code != null"))
  public PostalCode getPostalCode() {
    return $postalCode;
  }

  private final PostalCode $postalCode;

  /**
   * The name of the city of this postal address. This should be expressed in the language of
   * the recipient. This should not contain any formatting, nor end of lines, but might contain
   * other separation characters.
   */
  @Basic(invars = {@Expression("cityName != null"), @Expression("cityName != EMPTY"), @Expression("! cityName.contains('\n')")})
  public String getCity() {
    return $city;
  }

  private final String $city;

  /**
   * Any postal address information more specific that the postal code and city.
   * This data should not be formatted by the end user, apart form applying end-of-lines
   * appropriate for the address in the locale and country of the address. End-of-lines
   * should always be expressed as <code>\n</code> (and not as
   * {@link System#getProperty(String) System.getProperty(&quot;line.separator&quot;)}).
   */
  @Basic(invars = {@Expression("streetAddress != null"), @Expression("streetAddress != EMPTY")})
  public String getStreetAddress() {
    return $streetAddress;
  }

  private final String $streetAddress;

  /**
   * A String representation of this postal address, in a form that is applicable for the
   * country at which this particular subtype is aimed. This string should contain no formatting,
   * other than end-of-lines. End-of-lines should be applied according to the formatting rules
   * of the country at which this particular subtype is aimed.
   * End-of-lines should always be expressed as <code>\n</code> (and not as
   * {@link System#getProperty(String) System.getProperty(&quot;line.separator&quot;)}).
   */
  @MethodContract(post = @Expression("getPostalCode().localizedAddressRepresentation(this)"))
  public String getLocalizedRepresentation() {
    return getPostalCode().localizedAddressRepresentation(this);
  }

  // TODO (dvankeer): Don't we need a updated MethodContract as well?
  @Override
  public boolean equals(Object other) {
    return super.equals(other) && getPostalCode().equals(((PostalAddress)other).getPostalCode()) &&
           getCity().equals(((PostalAddress)other).getCity()) &&
           getStreetAddress().equals(((PostalAddress)other).getStreetAddress());
  }

  @Override
  public int hashCode() {
    return getPostalCode().hashCode() + getCity().hashCode() + getStreetAddress().hashCode();
  }

  @Override
  public String toString() {
    return getStreetAddress().replaceAll("\\n", ", ") + "; "  + getCity() + "; "  + getPostalCode().toString();
  }

}
