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
import java.util.TimeZone;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.DisplayLocaleBasedEnumerationValueEditor;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>An enumeration type editor for {@link TimeZone TimeZones}.</p>
 * <p>For i18n, the features provided by {@link TimeZone} are used. The returned label is in the
 *   requested language, if the {@link #getDisplayLocale()} is set, or in the default locale
 *   of it is not set.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TimeZoneEditor extends DisplayLocaleBasedEnumerationValueEditor<TimeZone> {

  @Override
  @MethodContract(post = @Expression("TimeZone.class"))
  public final Class<TimeZone> getValueType() {
    return TimeZone.class;
  }

  private static final Map<String, TimeZone> TIMEZONE_MAP = new HashMap<String, TimeZone>();

  static {
    String[] timeZones = TimeZone.getAvailableIDs();
    for (int i = 0; i < timeZones.length; i++) {
      String id = timeZones[i];
      TIMEZONE_MAP.put(id, TimeZone.getTimeZone(id));
    }
  }

  /**
   * A map containing all available TimeZones ({@link TimeZone#getAvailableIDs()}).
   */
  @MethodContract( post =
    {
      @Expression("result != null"),
      @Expression("result.size == TimeZone.getAvailableIDs().lenght"),
      @Expression("for (int i : 0 .. TimeZone.getAvailableIDs().lenght) {result.containsKey(TimeZone.getAvailableIDs()[i]}"),
      @Expression("for (int i : 0 .. TimeZone.getAvailableIDs().lenght) {result.get(TimeZone.getAvailableIDs()[i]) == TimeZone.getTimeZone(getAvailableIDs()[i])}")
    }
  )
  @Override
  public final Map<String, TimeZone> getValuesMap() {
    return TIMEZONE_MAP;
  }



  /**
   * If this is <code>true</code>, the label will return the time zone
   * in a short format, and in a long format otherwise.
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
    if ((getValue() == null) || (! (getValue() instanceof TimeZone))) {
      return null;
    }
    else {
      TimeZone timeZoneToShow = (TimeZone)getValue();
      Locale localeInWhichToDisplay = (getDisplayLocale() == null) ? Locale.getDefault() : getDisplayLocale();
      if ($shortMode) {
        return timeZoneToShow.getDisplayName(false, TimeZone.SHORT, localeInWhichToDisplay);
      }
      else {
        return timeZoneToShow.getDisplayName(false, TimeZone.LONG, localeInWhichToDisplay);
      }
    }
  }

}
