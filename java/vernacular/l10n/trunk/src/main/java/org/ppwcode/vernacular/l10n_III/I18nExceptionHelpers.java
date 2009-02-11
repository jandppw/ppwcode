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
import java.util.List;
import java.util.Locale;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.reflect_I.PropertyHelpers;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

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
    assert preArgumentNotNull(exc, "exc");
    assert preArgumentNotNull(locale, "locale");

    String messageTemplate = exc.getMessageTemplate(locale);
    String message = format(messageTemplate, exc, locale);

    return message;
  }

  public static String format(String template, Object context, Locale locale) {
    // The value ({object.property}) for the {object.property:propertyName} of the {object:type} is not acceptable.
    // The value ({enterprise.terminationDate}) for the {enterprise.terminationDate:propertyName} of the {enterprise:type} is not acceptable.
    //
    // The value ({enterprise.{prop}, date}) for the {enterprise.{prop}:propertyName} of the {enterprise:type} is not acceptable.
    //
    String pattern = template;
    MessageFormat format = new MessageFormat(pattern, locale);
    String result = format.format(context);
    return result;
  }


  /**
   * Evaluate the given pattern inside the given context, with the given locale and add the object to the list.
   *
   *
   * @param pattern
   * @param context
   * @param locale
   * @param list
   * @return
   */
  private static String processPattern(String pattern, Object context, Locale locale, List<Object> list) {
    // first process all elements found inside the pattern and replace the pattern
    // next: evaluate the pattern
    return null;
  }



  /**
   * Processes the given element inside the given context.
   * The given element can contain dots, which will be used to follow a chain of elements.
   * The object that is found in the context and that corresponds to the given element is then
   * returned as a String.
   * If any object in the given element chain is "null", then the String "null" is returned.
   * 
   * @param element Must be a field in the given context.
   * @param context
   * @return The String representation of the object that corresponds to element and is found in the context.
   */
  public static String processElement(String element, Object context) {
    assert preArgumentNotNull(element, "element");
    assert preArgumentNotNull(context, "context");

    String result = null;
    String firstElement = PropertyHelpers.carNestedPropertyName(element);
    String nextElement = PropertyHelpers.cdrNestedPropertyName(element);
    if (nextElement.equals(PropertyHelpers.EMPTY)) {
      Object o = PropertyHelpers.propertyValue(context, firstElement);
      if (o == null) {
        result = "null";
      } else {
        result = PropertyHelpers.propertyValue(context, firstElement).toString();
      }
    } else {
      Object newContext = PropertyHelpers.propertyValue(context, firstElement);
      if (newContext == null) {
        result = "null";
      } else {
        result = processElement(nextElement, newContext);
      }
    }
    return result;
  }

}
