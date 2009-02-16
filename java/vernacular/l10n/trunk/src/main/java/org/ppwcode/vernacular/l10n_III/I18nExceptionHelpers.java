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

import java.text.CharacterIterator;
import java.text.MessageFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.exception_III.ProgrammingErrorHelpers;
import org.ppwcode.util.reflect_I.PropertyHelpers;

import org.ppwcode.vernacular.l10n_III.resourcebundle.DefaultResourceBundleLoadStrategy;
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

  private static final Log LOG = LogFactory.getLog(I18nExceptionHelpers.class);

  private I18nExceptionHelpers() {
    // NOP
  }

  public static String i18nExceptionMessage(LocalizedException exc, Locale locale) {
    assert preArgumentNotNull(exc, "exc");
    assert preArgumentNotNull(locale, "locale");

    String message = "";
    try {
      LOG.debug("Localized exception message in locale [" + locale + "] for exception [" + exc.toString() + "]");
      String messageTemplate = exc.getMessageTemplate(locale);
      LOG.debug("Template: " + messageTemplate);
      message = format(messageTemplate, exc, locale);
      LOG.debug("Message:  " + message);
    } catch (I18nException i18nExc) {
      LOG.error("Exception: " + i18nExc);
      ProgrammingErrorHelpers.unexpectedException(i18nExc);
    }
    return message;
  }

  public static String format(String template, Object context, Locale locale)
    throws I18nException {
    List<Object> objects = new ArrayList<Object>();
    String pattern = processTemplate(template, context, locale, objects);
    LOG.debug("Pattern: " + pattern);
    MessageFormat form = new MessageFormat(pattern, locale);
    String result = form.format(objects.toArray());
    return result;
  }



  /**
   * Helper method for processTemplate to scan the full pattern, once the beginning of a pattern was found.
   */
  private static String processTemplatePattern(Object context, Locale locale, List<Object> objects,
          CharacterIterator iterator) throws I18nException {
    // previous token was "{", scan up to balanced "}"
    // scan full pattern now
    StringBuffer patternAcc = new StringBuffer(128);
    char token = ' '; // initialise with dummy value
    int balance = 1;
    while ((balance > 0) && (token != CharacterIterator.DONE)) {
      token = iterator.next();
      patternAcc.append(token);
      if (token == '{') {
        balance++;
      } else if (token == '}') {
        balance--;
      }
    }
    // done or bad template ?!?
    if (token == CharacterIterator.DONE) {
      throw new I18nException("Bad template!");
    }
    // remove last "}"
    patternAcc.setLength(patternAcc.length() - 1);
    // prepare pattern, treat the "," for formatting parts
    int comma = patternAcc.indexOf(",");
    String pattern = null;
    String patternPostfix = "";
    if (comma == -1) {
      pattern = patternAcc.toString();
    } else if (comma == 0) {
      throw new I18nException("Bad template!");
    } else {
      pattern = patternAcc.substring(0, comma);
      patternPostfix = patternAcc.substring(comma, patternAcc.length());
    }
    // process pattern
    String processedPattern = processPattern(pattern, context, locale, objects);
    return processedPattern + patternPostfix;
  }


  protected static String processTemplate(String template, Object context, Locale locale, List<Object> objects)
    throws I18nException {
    StringBuffer result = new StringBuffer(1024);
    CharacterIterator iterator = new StringCharacterIterator(template);
    char token = iterator.first();
    while (token != CharacterIterator.DONE) {
      if (token != '{') {
        result.append(token);
      } else {
        result.append("{");
        result.append(processTemplatePattern(context, locale, objects, iterator));
        result.append("}");
      }
      token = iterator.next();
    }
    return result.toString();
  }



  /**
   * Evaluate the given pattern inside the given context, with the given locale and add the object to the list.
   *
   */
  protected static String processPattern(String pattern, Object context, Locale locale, List<Object> list) {
    // first process all elements found inside the pattern and replace the pattern
    // find deepest "{}" pattern first
    String regexp = "^(.*)\\{([^\\{\\}]*)\\}(.*)$";
    Pattern regexpPattern = Pattern.compile(regexp, Pattern.DOTALL);
    String workPattern = pattern;
    Matcher regexpMatcher = regexpPattern.matcher(workPattern);
    while (regexpMatcher.matches()) {
      String before = regexpMatcher.group(1);
      String middle = regexpMatcher.group(2);
      String after = regexpMatcher.group(3);
      workPattern = before + processElementString(middle, context, locale) + after;
      regexpMatcher = regexpPattern.matcher(workPattern);
    }
    // evaluate pattern
    Object value = processElementObject(workPattern, context, locale);
    int position = list.size();
    list.add(value);
    return Integer.toString(position);
  }


  protected static Object processElementObject(String element, Object context, Locale locale) {
    assert preArgumentNotNull(element, "element");
    assert preArgumentNotNull(context, "context");
    assert preArgumentNotNull(locale, "locale");

    Object result = null;
    String[] parts = element.split(":");
    if (parts.length == 1) {
      result = processElementValue(element, context);
    } else {
      result = processElementLabel(parts[0], parts[1], context, locale);
    }
    return result;
  }


  protected static String processElementString(String element, Object context, Locale locale) {
    assert preArgumentNotNull(element, "element");
    assert preArgumentNotNull(context, "context");
    assert preArgumentNotNull(locale, "locale");

    String result = null;
    String[] parts = element.split(":");
    if (parts.length == 1) {
      result = processElementStringValue(element, context);
    } else {
      result = processElementLabel(parts[0], parts[1], context, locale);
    }
    return result;
  }



  protected static String processElementStringValue(String element, Object context) {
    assert preArgumentNotNull(element, "element");
    assert preArgumentNotNull(context, "context");

    String result = null;
    Object value = processElementValue(element, context);
    if (value == null) {
      result = "null";
    } else {
      result = value.toString();
    }
    return result;
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
   * @return The object that corresponds to element and is found in the context.
   */
  protected static Object processElementValue(String element, Object context) {
    assert preArgumentNotNull(element, "element");
    assert preArgumentNotNull(context, "context");

    Object result = null;
    String firstElement = PropertyHelpers.carNestedPropertyName(element);
    String nextElement = PropertyHelpers.cdrNestedPropertyName(element);
    if (nextElement.equals(PropertyHelpers.EMPTY)) {
      result = PropertyHelpers.propertyValue(context, firstElement);
    } else {
      Object newContext = PropertyHelpers.propertyValue(context, firstElement);
      if (newContext == null) {
        result = null;
      } else {
        result = processElementValue(nextElement, newContext);
      }
    }
    return result;
  }


  /**
   * Localized classes provide a localized name for the class itself and every property in the class.
   * This method enables us to fetch the given label for the given element in the given context, and
   * in the given locale.
   * The given element can be a chain of properties.  In order to find the correct label, the chain of
   * properties will be followed with the given context as the root.  The chain will be followed
   * dynamically as long as possible, and statically as soon as a "null" value is encountered in the
   * chain.
   *
   */
  protected static String processElementLabel(String element, String label, Object context, Locale locale) {
    assert preArgumentNotNull(element, "element");
    assert preArgumentNotNull(label, "label");
    assert preArgumentNotNull(context, "context");
    assert preArgumentNotNull(locale, "locale");

    DefaultResourceBundleLoadStrategy strategy = new DefaultResourceBundleLoadStrategy();
    strategy.setLocale(locale);

    // -> as long as not null, follow properties dynamically
    // -> as soon as null, follow statically
    Object newContext = context;
    String property = element;
    Object tmpCtx = null;
    String carProperty = PropertyHelpers.carNestedPropertyName(property);
    String cdrProperty = PropertyHelpers.cdrNestedPropertyName(property);
    while (!cdrProperty.equals(PropertyHelpers.EMPTY)) {
      if (newContext instanceof Class) {
        // follow chain statically
        newContext = PropertyHelpers.propertyType((Class)newContext, carProperty);
        property = cdrProperty;
      } else {
        // follow chain dynamically if possible
        tmpCtx = PropertyHelpers.propertyValue(newContext, carProperty);
        if (tmpCtx != null) {
          // follow dynamically
          newContext = tmpCtx;
        } else {
          // follow statically
          newContext = PropertyHelpers.propertyType(newContext.getClass(), carProperty);
        }
        property = cdrProperty;
      }
      carProperty = PropertyHelpers.carNestedPropertyName(property);
      cdrProperty = PropertyHelpers.cdrNestedPropertyName(property);
    }

    if (newContext instanceof Class) {
      return I18nLabelHelpers.i18nClassGenericLabel(property, (Class)newContext, label, strategy);
    } else {
      return I18nLabelHelpers.i18nInstanceGenericLabel(property, newContext, label, strategy);
    }
  }


}
