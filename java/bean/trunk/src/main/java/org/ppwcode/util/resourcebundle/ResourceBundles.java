/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.util.resourcebundle;


import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.ppwcode.i18n_I.ResourceBundleLoadStrategy;


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
public final class ResourceBundles {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Cannot instantiate this class. Only use static methods.
   */
  private ResourceBundles() {
    // NOP
  }

  /*</construction>*/



  /**
   * <p>Return a string from a properties file that has a basename that
   *   matches <code>type</code>, or one of its supertypes.</p>
   * <p>We look for <code>keys</code> in a properties file with the same
   *   name as <code>type.getName()</code>, in order. If no match is
   *   found, or no such properties file exists, we try again with
   *   the super types, in the order they are presented by the reflection
   *   functionality. We look in interfaces first, the superclass if
   *   no match is found in the super interfaces. We search breath-first.</p>
   * <p>If this entire process does not return a valid result, we return
   *   <code>null</code>.</p>
   *
   * @param type
   *        The type to use as starting point for the lookup in associated
   *        properties files.
   * @param keys
   *        The keys to lookup in order.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   */
  public static String findKeyInTypeProperties(final Class<?> type, final String[] keys, final ResourceBundleLoadStrategy strategy) {
    LinkedList<Class<?>> agenda = new LinkedList<Class<?>>();
    agenda.add(type);
    String result = null;
    while ((result == null) && (!agenda.isEmpty())) {
      Class<?> current = agenda.removeFirst();
      result = findKeyWithBasename(current.getName(), keys, strategy);
      if (result == null) {
        Class<?>[] interfaces = current.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
          agenda.add(interfaces[i]);
        }
        Class<?> superClass = current.getSuperclass();
        if (superClass != null) {
          agenda.add(superClass);
        }
      }
    }
    return result; // null if not found
  }

  /**
   * @param     basename
   *            The base name of the resource bundle to look for.
   * @param     keys
   *            The keys to look for in the resource bundle.
   * @param     strategy
   *            The strategy to use to look for a
   *            resource bundle properties file.
   * @pre       key != null && ! key.equals("");
   * @pre       strategy != null
   * @return    ; <code>null</code> if something goes wrong
   */
  public static String findKeyWithBasename(final String basename, final String[] keys, final ResourceBundleLoadStrategy strategy) {
    assert strategy != null;
    assert keys != null && keys.length > 0;
    String result = null;
    if (basename != null && !basename.equals("")) {
      ResourceBundle bundle = strategy.loadResourceBundle(basename);
      if (bundle != null) {
        int i = 0;
        while (result == null && i < keys.length) {
          result = findKeyInResourceBundle(bundle, keys[i]);
          i++;
        }
      }
    }
    return result;
  }

  /**
   * @param     rb
   *            The resource bundle to look in.
   * @param     key
   *            The key to look for.
   * @pre       rb != null;
   * @pre       key != null && key.length() > 0;
   * @return    ; <code>null</code> if the resource could not be retrieved
   */
  public static String findKeyInResourceBundle(final ResourceBundle rb, final String key) {
    assert rb != null;
    assert (key != null && key.length() > 0);
    String result = null;
    try {
      result = rb.getString(key);
          /* throws MissingResourceException, ClassCastException */
    }
    catch (ClassCastException ccExc) {
      // match is not a String, return null
    }
    catch (MissingResourceException mrExc) {
      // key not found, return null
    }
    return result;
  }

}
