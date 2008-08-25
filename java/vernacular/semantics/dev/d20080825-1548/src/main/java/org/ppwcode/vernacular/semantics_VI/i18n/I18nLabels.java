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

package org.ppwcode.vernacular.semantics_VI.i18n;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyType;
import static org.ppwcode.util.resourcebundle.ResourceBundles.findKeyInTypeProperties;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.resourcebundle.ResourceBundleLoadStrategy;
import org.ppwcode.util.resourcebundle.ResourceBundles;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;


/**
 * <p>Support methods to work with properties files.
 *   There are general methods here, and some methods specific to a
 *   properties file strategy for i18n of bean type labels and bean property
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
 * @author Jan Dockx
 * @todo (jand): test code
 * @idea split the support and bean label properties in different classes;
 *       move the bean property label stuff to ppw-bean
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class I18nLabels {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Cannot instantiate this class. Only use static methods.
   */
  private I18nLabels() {
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
    String result = findKeyInTypeProperties(type, i18nPropertyLabel_keys(property, shortLabel), strategy);
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
  public static final String I18N_PROPERTY_LABEL_KEY_PREFIX = "propertyName.";

  /**
   * <p>Prefix used in property files to discriminate short property
   *  labels.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX = I18N_PROPERTY_LABEL_KEY_PREFIX + "short.";

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
    String result = findKeyInTypeProperties(type,
                                            (plural
                                              ? I18N_PLURAL_TYPE_LABEL_KEYS
                                              : I18N_TYPE_LABEL_KEYS),
                                            strategy);
    return (result != null) ? result : keyNotFound(type.getName());
  }

  /*<property name="localizedMessageKeys">*/
  //------------------------------------------------------------------

  private static final String EMPTY = "";
  private static final String PREFIX = "message";

  /**
   * The keys that are tried consecutively are intended for use in
   * a properties file that comes with the
   * {@link #getOriginType() origin type} of the exception, with a fall-back
   * to a properties file that comes with the class of the exception.
   * The message that is given in the constructor (the
   * {@link #getMessage()} non-localized message) is intended as the
   * final discriminating key in a resource bundle.
   *
   * <p>The first key, for use in the properties file that comes with
   *   the {@link #getOriginType() origin type} of the exception, has the form
   *   <code>getClass().getName() + "." + getPropertyName()
   *         + "." + getMessage()</code>.
   *   If the property name is <code>null</code>, the form is
   *   <code>getClass().getName() + "." + getMessage()</code>. This is
   *   intended for exceptions that are not bound to a particular
   *   property. If the message is <code>null</code>, the form of the key
   *   is <code>getClass().getName() + "." + getPropertyName()</code>
   *   or <code>getClass().getName()</code>. These forms are intended for
   *   exceptions of which the type itself is discriminating enough
   *   for a good exception message. </p>
   * <p>The second key is intended for use in a properties file that
   *   comes with the exception class. It is intended for error messages
   *   that can be written independent of the actual origin or property
   *   for which they occurred. Such messages should often be considered
   *   a fall-back.
   *   The key that should be used in these files is of the form
   *   <code>"message." + getMessage()</code>. If the message is
   *   <code>null</code>, no such key is added to the array.</p>
   * <p>When the {@link #getMessage() message} is used as a key,
   *   it should be in all caps.</p>
   *
   * @result    result != null;
   * @result    result.length == ((getMessage() != null) ? 2 : 1);
   * @result    result[0] != null;
   * @result    (getPropertyName() != null) && (getMessage() != null)
   *                ==> result[0].equals(getClass().getName()
   *                                     + "." + getPropertyName()
   *                                     + "." + getMessage());
   * @result    (getPropertyName() == null) && (getMessage() != null)
   *                ==> result[0].equals(getClass().getName()
   *                                     + "." + getMessage());
   * @result    (getPropertyName() != null) && (getMessage() == null)
   *                ==> result[0].equals(getClass().getName()
   *                                     + "." + getPropertyName());
   * @result    (getPropertyName() == null) && (getMessage() == null)
   *                ==> result[0].equals(getClass().getName());
   * @result    (getMessage() != null)
   *                ==> (result[1] != null)
   *                    && result[1].equals("message." + getMessage());
   */
  private final static String[] getBeanBundleKeys(PropertyException pe) {
    String[] result = null;
//    String className = pe.getClass().getName();
//    String propertyName = pe.getPropertyName();
//    String message = pe.getMessage();

//    className + DOT + propertyName + DOT + message;

    String firstKey = pe.getClass().getName() + (pe.getPropertyName() != null
                            ? DOT + pe.getPropertyName()
                            : EMPTY)
                      + (pe.getMessage() != null ? DOT + pe.getMessage() : EMPTY); // ben prop file
    String secondKey = pe.getMessage() != null ? PREFIX + pe.getMessage() : null; // exception prop file
    if (secondKey != null) {
      result = new String[] {firstKey, secondKey};
    }
    else {
      result = new String[] {firstKey};
    }
    return result;
  }

  private final static String[] getExceptionBundleKeys(PropertyException pe) {
    assert pe != null;
    String[] result = null;
    String message = pe.getMessage();
    if (message == null) {
      String firstKey = PREFIX + DOT + message;
      result = new String[] {firstKey, PREFIX};
    }
    else {
      result = new String[] {PREFIX};
    }
    return result;
  }

  /**
   * Return the a label from the
   * {@link #getLocalizedMessageResourceBundleBasename()} resource
   * bundle with keys {@link #getLocalizedMessageKeys()}, using the
   * resoure bundle load strategy
   * {@link #getLocalizedMessageResourceBundleLoadStrategy()}.
   * The keys are tried in order. The first one that gives a result,
   * is used.
   * If this fails, we try to load a resource with name
   * <code>getClass().getName()</code>, with the same resource
   * bundle load strategy and look up the same keys.
   * If there is no load strategy, or the bundles could not be found,
   * or there is no entry in the bundles with the given keys, the
   * non-localized message is returned.
   */
  public static final String getLocalizedMessage(PropertyException pe, ResourceBundleLoadStrategy rbls) {
    assert pe != null;
    assert rbls != null;
    if (rbls == null) {
      return pe.getMessage();
    }
    String result;
    result = ResourceBundles.findKeyInTypeProperties(pe.getOriginType(), getBeanBundleKeys(pe), rbls);
    if (result == null) {
      result = ResourceBundles.findKeyInTypeProperties(pe.getClass(), getExceptionBundleKeys(pe), rbls);
    }
    if (result == null) {
      result = pe.getMessage();
    }
    return result;
  }
  /*</property>*/


}
