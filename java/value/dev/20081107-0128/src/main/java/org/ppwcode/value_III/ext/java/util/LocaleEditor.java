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

package org.ppwcode.value_III.ext.java.util;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.DisplayLocaleBasedEnumerationValueEditor;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>An enumeration type editor for {@link Locale}s.</p>
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
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class LocaleEditor extends DisplayLocaleBasedEnumerationValueEditor<Locale> {

  @Override
  @MethodContract(post = @Expression("Locale.class"))
  public final Class<Locale> getValueType() {
    return Locale.class;
  }

  private static final Map<String, Locale> LOCALE_MAP = new HashMap<String, Locale>();

  static {
    Locale[] locales = Locale.getAvailableLocales();
    for (int i = 0; i < locales.length; i++) {
      Locale l = locales[i];
      LOCALE_MAP.put(l.toString(), l);
    }
    LOCALE_MAP.put(" ", new Locale(" "));
  }

  /**
   * A map containing all available locales ({@link Locale#getAvailableLocales()}).
   */
  @MethodContract( post =
    {
      @Expression("result != null"),
      @Expression("result.size == Locale.getAvailableLocales().lenght"),
      @Expression("for (int i : 0 .. Locale.getAvailableLocales().lenght) {result.containsKey(Locale.getAvailableLocales()[i].toString()}"),
      @Expression("for (int i : 0 .. Locale.getAvailableLocales().lenght) {result.get(Locale.getAvailableLocales()[i].toString()) == Locale.getAvailableLocales()[i]}")
    }
  )
  @Override
  public final Map<String, Locale> getValuesMap() {
    return LOCALE_MAP;
  }

  /**
   * If this is <code>true</code>, the label will only return
   * a label for the language part of the locale. If this is
   * <code>false</code>, the label will return a full label
   * that also displays the language variants.
   */
  @Basic(init = @Expression("false"))
  public final boolean isShortMode() {
    return $shortMode;
  }

  @MethodContract(
    post = @Expression("shortMode = _shortMode")
  )
  public final void setShortMode(final boolean shortMode) {
    $shortMode = shortMode;
  }

  private boolean $shortMode;

  @MethodContract(
    post = {
      @Expression("! shortMode ? value.getDisplayName(displayLocale == null ? value : displayLocale)"),
      @Expression("shortMode ? value.getDisplayLanguage(displayLocale == null ? value : displayLocale)")
    }
  )
  public final String getLabel() {
    String result = "";
    if ((getValue() != null) && (getValue() instanceof Locale)) {
      Locale localeToShow = (Locale)getValue();
      Locale localeInWhichToDisplay = (getDisplayLocale() == null)
                                          ? localeToShow
                                          : getDisplayLocale();
      if ($shortMode) {
        result = localeToShow.getDisplayLanguage(localeInWhichToDisplay);
      }
      else {
        result = localeToShow.getDisplayName(localeInWhichToDisplay);
      }
    }
    return result;
  }

}
