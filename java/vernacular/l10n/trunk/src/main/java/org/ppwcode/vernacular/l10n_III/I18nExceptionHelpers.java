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

import java.text.MessageFormat;
import java.util.Locale;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.exception_III.ProgrammingErrorHelpers;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

/**
 * Class with utility methods for the localization of exceptions.
 *
 * @author    Ruben Vandeginste
 * @author    PeopleWare n.v.
 */
@Copyright("2009 - $Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4044 $",
         date     = "$Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $")
public final class I18nExceptionHelpers {

  private I18nExceptionHelpers() {
    // NOP
  }

  public static String i18nExceptionMessage(LocalizedException exc, Locale locale) {
    assert ProgrammingErrorHelpers.preArgumentNotNull(exc, "exc");
    assert ProgrammingErrorHelpers.preArgumentNotNull(locale, "locale");

    String messageTemplate = exc.getMessageTemplate(locale);
    String message = format(messageTemplate, exc, locale);

    return message;
  }

  public static String format(String template, Object context, Locale locale) {
    String pattern = template;
    MessageFormat format = new MessageFormat(pattern, locale);
    String result = format.format(context);
    return result;
  }

}
