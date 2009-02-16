/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v.

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


import org.ppwcode.util.reflect_I.PropertyHelpers;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyType;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;
import static org.ppwcode.vernacular.l10n_III.resourcebundle.ResourceBundleHelpers.value;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.l10n_III.resourcebundle.KeyNotFoundException;
import org.ppwcode.vernacular.l10n_III.resourcebundle.ResourceBundleLoadStrategy;
import org.ppwcode.vernacular.l10n_III.resourcebundle.WrongValueTypeException;


/**
 * <p><strong>This class is unfinished, and should not be considered part of this release.</strong></p>
 *
 * <p>Support methods to work with properties files. There are general methods here, and some
 *   methods specific to a properties file strategy for i18n of bean type labels and bean property
 *   labels, and general bean related text.</p>
 * <p>In general, we look for the i18n strings in a properties file that has
 *   the same fully qualified name as the bean class whose labels we are
 *   interested in. If a match is not found, we search for the labels
 *   through the supertypes of the bean class.</p>
 * <p>{@link #i18nPropertyLabel(String, Class, boolean, ResourceBundleLoadStrategy)}
 *   returns the full or the short label of a JavaBean property, using a
 *   specific resource bundle load strategy, based on the <code>Class</code>
 *   of the JavaBean.
 *   {@link #i18nInstancePropertyLabel(String, Object, boolean, ResourceBundleLoadStrategy)}
 *   does the same, based on an instance of a JavaBean.<br/>
 *   {@link #i18nTypeLabel(Class, boolean, ResourceBundleLoadStrategy)} returns
 *   the full or short type label of a JavaBean, using a specific resource bundle
 *   load strategy, based on the <code>Class</code>
 *   of the JavaBean.</p>
 * <p>The other public methods are support methods to retrieve properties
 *   file entries.</p>
 *
 * @note Since the previous version, the order of the arguments of some methods have changed.
 *
 * @author Jan Dockx
 * @todo (jand): test code
 * @idea split the support and bean label properties in different classes;
 *       move the bean property label stuff to ppw-bean
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class I18nLabelHelpers {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Cannot instantiate this class. Only use static methods.
   */
  private I18nLabelHelpers() {
    // NOP
  }

  /*</construction>*/



  /**
   * <p>Token used in return values to signal properties that
   *  are not found.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String NOT_FOUND_TOKEN = "???";

  /**
   * <p>Token used in return values to separate type names
   *  from property names.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String PROPERTY_SEPARATOR_TOKEN = "#";

  private static final char DOT = '.';

  /**
   * <p>Return a label for a property with name <code>property</code>
   *   of a type <code>type</code>.</p>
   * <p>We look for a property with key
   *   <code>propertyName.<var>property</var></code> or
   *   <code>propertyName.short.<var>property</var></code>
   *   in a properties file with the name of <code>type</code>,
   *   and its supertypes. We search bottom up, and return the
   *   first match. If no match is found, we return the String
   *   <code>&quot;???&quot;<var>property</var>&quot;???&quot;</code>.
   *   If the short label does not exist in a given properties file,
   *   we look for the normal label, and vice versa, before
   *   we try the next type.</p>
   * <p>Property names can be nested, as described in
   *   <a href="http://jakarta.apache.org/commons/beanutils/commons-beanutils-1.7.0/docs/api/"><code>org.apache.commons.beanutils.PropertyUtilsBean</code></a>.
   *   This means that, if <code>property</code> contains '.' separators, we
   *   look up the type of the portion before the dot, and then call
   *   <code>findKeyInTypeProperties(<var>beforeDotType</var>, <var>propertyAfterDot</var>, shortLabel, strategy)</code>.
   *   If one of the properties in this path does not exist, we return <code>null</code>.</p>
   *
   * @param property
   *        The name of the property to return an i18n'ed label for.
   * @param type
   *        The type of bean that owns the property. This is the
   *        start of our lookup.
   * @param shortLabel
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   *
   * @mudo better exception type
   */
  public static String i18nPropertyLabel(final String property, final Class<?> type, final boolean shortLabel, final ResourceBundleLoadStrategy strategy)
      throws IllegalArgumentException {
    if ((property == null) || (property.length() <= 0) || (type == null)) {
      throw new IllegalArgumentException("parameters must be effective");
    }
    int dotPosition = property.indexOf(DOT);
    if (dotPosition >= 0) {
      String preDot = property.substring(0, dotPosition);
      String postDot = property.substring(dotPosition + 1);
      Class<?> preType = propertyType(type, preDot);
      return i18nPropertyLabel(postDot, preType, shortLabel, strategy);
    }
    String result = null;
    try {
      result = value(type, i18nPropertyLabel_keys(property, shortLabel), String.class, strategy);
    }
    catch (KeyNotFoundException exc) {
      return null;
    }
    catch (WrongValueTypeException exc) {
      unexpectedException(exc);
    }
    return (result != null)
            ? result
            : keyNotFound(property + PROPERTY_SEPARATOR_TOKEN + type.getName());
  }

  /**
   * <p>Return a label for a property with name <code>property</code>
   *   of a type <code>instance.getClass()</code>.</p>
   *
   * @see #i18nPropertyLabel(String, Class, boolean, ResourceBundleLoadStrategy)
   *
   * @param property
   *        The name of the property to return an i18n'ed label for.
   * @param instance
   *        The object that owns the property. The class of this object
   *        is the start of our lookup.
   * @param shortLabel
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   */
  public static String i18nInstancePropertyLabel(
      final String property,
      final Object instance,
      final boolean shortLabel,
      final ResourceBundleLoadStrategy strategy) {
    return i18nPropertyLabel(property,
                             instance.getClass(),
                             shortLabel,
                             strategy);
  }


  /**
   * <p>Prefix used in property files to discriminate property
   *  labels.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_PROPERTY_LABEL_KEY = "propertyName";
  public static final String I18N_PROPERTY_LABEL_KEY_PREFIX = I18N_PROPERTY_LABEL_KEY + DOT;

  /**
   * <p>Prefix used in property files to discriminate short property
   *  labels.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_SHORT_PROPERTY_LABEL_KEY = I18N_PROPERTY_LABEL_KEY_PREFIX + "short";
  public static final String I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX = I18N_SHORT_PROPERTY_LABEL_KEY + DOT;

  /**
   * @pre property != null;
   * @pre property.length() > 0;
   */
  private static String[] i18nPropertyLabel_keys(final String property, final boolean shortLabel) {
    assert property != null;
    assert property.length() > 0;
    String key = I18N_PROPERTY_LABEL_KEY_PREFIX + property;
    String shortKey = I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX + property;
    return shortLabel ? new String[] {shortKey, key} : new String[] {key, shortKey};
  }

  /**
   * @pre key != null;
   * @pre key.length() > 0;
   * @pre type != null;
   */
  private static String keyNotFound(final String key) {
    assert key != null;
    assert key.length() > 0;
    return NOT_FOUND_TOKEN
            + key
            + NOT_FOUND_TOKEN;
  }

  /**
   * <p>Key used in property files to discriminate the type
   *  label.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_TYPE_LABEL_KEY = "type";

  /**
   * <p>Key used in property files to discriminate the
   *  plural type label.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_PLURAL_TYPE_LABEL_KEY =
    I18N_TYPE_LABEL_KEY + ".plural";

  private static String[] I18N_TYPE_LABEL_KEYS =
    new String[] {I18N_TYPE_LABEL_KEY};

  private static String[] I18N_PLURAL_TYPE_LABEL_KEYS =
    new String[] {I18N_PLURAL_TYPE_LABEL_KEY};

  /**
   * <p>Return a label for a type <code>type</code>.</p>
   * <p>We look for a property with key
   *   <code>type</code> or <code>type.plural</code>
   *   in a properties file with the name of <code>type</code>,
   *   and its supertypes. We search bottom up, and return the
   *   first match. If no match is found, we return the String
   *   <code>&quot;???&quot;<var>className</var>&quot;???&quot;</code>.</p>
   *
   * @param type
   *        The type of bean that owns the property. This is the
   *        start of our lookup.
   * @param plural
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *          type == null;
   */
  public static String i18nTypeLabel(final Class<?> type, final boolean plural, final ResourceBundleLoadStrategy strategy) {
    if (type == null) {
      throw new IllegalArgumentException("type must be effective");
    }
    String result = null;
    try {
      result = value(type, (plural ? I18N_PLURAL_TYPE_LABEL_KEYS : I18N_TYPE_LABEL_KEYS), String.class, strategy);
    }
    catch (WrongValueTypeException exc) {
      unexpectedException(exc);
    }
    catch (KeyNotFoundException exc) {
      return null;
    }
    return (result != null) ? result : keyNotFound(type.getName());
  }


  /**
   * <p>Return a label of type <code>type</code>, for the given property of
   * the given instance.</p>
   *
   * <p>Note that the <code>property</code> cannot be a "chained" property.  Note also
   * that we use the dynamic type of the <code>property</code> to determine the label,
   * instead of the static type of the property as defined in the class
   * definition of <code>instance</code>.</p>
   *
   * @param property
   *        The property of the instance, for which we want the label.
   * @param instance
   *        The bean that owns the property.
   * @param type
   *        The type of the label that we are asking for. This type must be one of the following:
   *        <code>I18N_PROPERTY_LABEL_KEY</code>, <code>I18N_SHORT_PROPERTY_LABEL_KEY</code>,
   *        <code>I18N_TYPE_LABEL_KEY</code> and <code>I18N_PLURAL_TYPE_LABEL_KEY</code>.
   * @param strategy
   *        The strategy used to find the resource bundle properties file.
   * @return
   */
  public static String i18nInstanceGenericLabel(
          final String property,
          final Object instance,
          final String type,
          final ResourceBundleLoadStrategy strategy) {
    String result = null;
    
    if (type.equals(I18N_PROPERTY_LABEL_KEY)) {
      result = i18nInstancePropertyLabel(property, instance, false, strategy);
    } else if (type.equals(I18N_SHORT_PROPERTY_LABEL_KEY)) {
      result = i18nInstancePropertyLabel(property, instance, true, strategy);
    } else if (type.equals(I18N_TYPE_LABEL_KEY)) {
      Object value = PropertyHelpers.propertyValue(instance, property);
      result = i18nTypeLabel(value.getClass(), false, strategy);
    } else if (type.equals(I18N_PLURAL_TYPE_LABEL_KEY)) {
      Object value = PropertyHelpers.propertyValue(instance, property);
      result = i18nTypeLabel(value.getClass(), true, strategy);
    } else {
      // TODO  other error ??  return null ??
      throw new IllegalArgumentException("This type of label is not supported.");
    }
    return result;
  }

  public static String i18nClassGenericLabel(
          final String property,
          final Class instance,
          final String type,
          final ResourceBundleLoadStrategy strategy) {
    String result = null;

    if (type.equals(I18N_PROPERTY_LABEL_KEY)) {
      result = i18nPropertyLabel(property, instance, false, strategy);
    } else if (type.equals(I18N_SHORT_PROPERTY_LABEL_KEY)) {
      result = i18nPropertyLabel(property, instance, true, strategy);
    } else if (type.equals(I18N_TYPE_LABEL_KEY)) {
      Object propertyClass = PropertyHelpers.propertyType(instance, property);
      result = i18nTypeLabel((Class)propertyClass, false, strategy);
    } else if (type.equals(I18N_PLURAL_TYPE_LABEL_KEY)) {
      Object propertyClass = PropertyHelpers.propertyType(instance, property);
      result = i18nTypeLabel((Class)propertyClass, true, strategy);
    } else {
      // TODO  other error ??  return null ??
      throw new IllegalArgumentException("This type of label is not supported.");
    }
    return result;
  }


}
