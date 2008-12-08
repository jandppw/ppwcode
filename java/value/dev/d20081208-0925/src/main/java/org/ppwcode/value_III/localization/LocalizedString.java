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

package org.ppwcode.value_III.localization;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Arrays;
import java.util.Locale;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.AbstractImmutableValue;
import org.ppwcode.vernacular.value_III.ValueException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * A String that carries with it the locale of the text it contains.
 * This is handy in a number of circumstances, where texts must
 * be stored in multiple, or possibly different, languages.
 *
 * @author    Jan Dockx
 * @author    Peopleware NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class LocalizedString extends AbstractImmutableValue {


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * A new localized string.
   * The locale is mandatory (as is the String), and must be meaningful.
   * At least the language needs to be an existing language. The string
   * is mandatory, and can also not be empty. If the semantics requires
   * something like this, you should use a {@code null LocalizedString},
   * not a {@code LocalizedString} with a {@null} or an empty string.
   * @throws ValueException
   */
  @MethodContract(
    pre  = {
      @Expression("_locale != null"),
      @Expression("Locale.getISOLanguages().contains(_locale.getLanguage())"),
      @Expression("_string != null"),
      @Expression("_string != EMPTY")
    },
    post = {
      @Expression("locale == _locale"),
      @Expression("string == _string")
    },
    exc  = {
      @Throw(type = LocaleException.class,
             cond = @Expression("_locale == null")),
      @Throw(type = LocaleException.class,
             cond = @Expression("! Locale.getISOLanguages().contains(_locale.getLanguate())")),
      @Throw(type = ValueException.class,
             cond = @Expression("_string == null")),
      @Throw(type = ValueException.class,
             cond = @Expression("_string == EMPTY"))
    }
  )
  public LocalizedString(Locale locale, String text) throws LocaleException, ValueException {
    if (locale == null) {
      throw new LocaleException(getClass(), locale, "LOCALE_IS_NULL", null);
    }
    if (! Arrays.asList(Locale.getISOLanguages()).contains(locale.getLanguage())) {
      throw new LocaleException(getClass(), locale, "UNKNOWN_LANGUAGE", null);
    }
    if (text== null) {
      throw new ValueException(getClass(), "STRING_IS_NULL", null);
    }
    if (text.equals(EMPTY)) {
      throw new ValueException(getClass(), "STRING_IS_EMPTY", null);
    }
    $locale = locale;
    $text = text;
  }

  /*</construction>*/



  /*<property name="locale">*/
  //------------------------------------------------------------------

  @Basic(invars = {
    @Expression("locale != null"),
    @Expression("Locale.getISOLanguages().contains(locale.getLanguage())")
  })
  public final Locale getLocale() {
    return $locale;
  }

  private final Locale $locale;

  /*</property>*/



  /*<property name="locale">*/
  //------------------------------------------------------------------

  @Basic(invars = {
    @Expression("text != null"),
    @Expression("text != EMPTY")
  })
  public final String getText() {
    return $text;
  }

  private final String $text;

  /*</property>*/



  @MethodContract(post = @Expression("locale == other.locale) && text == other.text)"))
  @Override
  public final boolean equals(Object other) {
    return super.equals(other) && $locale.equals(((LocalizedString)other).getLocale()) &&
            $text.equals(((LocalizedString)other).getText());
  }

  @Override
  public final int hashCode() {
    return $locale.hashCode() + $text.hashCode();
  }

  @Override
  public final String toString() {
    return "[" + $locale.toString() + "]" + $text;
  }

}
