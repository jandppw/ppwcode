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

package org.ppwcode.vernacular.resourcebundle_II;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.TypeHelpers.directSuperTypes;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.preArgumentNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;



/**
 * <p>Support methods to work with resource bundles.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class ResourceBundleHelpers {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Cannot instantiate this class. Only use static methods.
   */
  private ResourceBundleHelpers() {
    // NOP
  }

  /*</construction>*/



  /**
   * Find the resource bundle with the same basename as the FQCN of {@code type}.
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_rbls != null")
    },
    post = @Expression("_rbls.loadResourceBundle(_type.getCanonicalName())"),
    exc  = @Throw(type = ResourceBundleNotFoundException.class,
                  cond = @Expression("rbls.loadResourceBundle(type.getCanonicalName()) throws ResourceBundleNotFoundException"))
  )
  public static ResourceBundle typeResourceBundle(Class<?> type, ResourceBundleLoadStrategy rbls) throws ResourceBundleNotFoundException {
    assert preArgumentNotNull(type, "type");
    assert preArgumentNotNull(rbls, "rbls");
    return rbls.loadResourceBundle(type.getCanonicalName());
  }

  /**
   * The value of type {@code valueType} of associated with the first key from {@code keys} that is found in
   * the resource bundle with the same basename as the FQCN of {@code type} or one of its super types.
   *
   * @mudo contract
   * @mudo unit tests
   */
  public static <_T_> _T_ value(Class<?> type, String[] keys, Class<_T_> valueType, ResourceBundleLoadStrategy rbls)
      throws WrongValueTypeException, KeyNotFoundException {
    assert preArgumentNotNull(type, "type");
    assert preArgumentNotNull(keys, "keys");
    assert preArgumentNotNull(valueType, "valueType");
    assert preArgumentNotNull(rbls, "rbls");
    LinkedList<Class<?>> agenda = new LinkedList<Class<?>>();
    agenda.add(type);
    _T_ result = null;
    ListIterator<Class<?>> iter = agenda.listIterator();
    List<ResourceBundle> rbs = new LinkedList<ResourceBundle>();
    while (result == null && iter.hasNext()) {
      Class<?> current = iter.next();
      assert current != null;
      try {
        ResourceBundle rb = typeResourceBundle(current, rbls);
        result = value(rb, keys, valueType); // throws WrongValueTypeException
        rbs.add(rb); // for the final exception of necessary
        return result;
      }
      catch (ResourceBundleNotFoundException exc) {
        // try the super types
        agenda.addAll(directSuperTypes(current)); // concurrent modification
      }
      catch (KeyNotFoundException exc) {
        // try the super types
        agenda.addAll(directSuperTypes(current)); // concurrent modification
      }
    }
    throw new KeyNotFoundException(rbs.toArray(new ResourceBundle[rbs.size()]), keys);
  }

  /**
   * The value of type {@code valueType} associated with the first key in {@code keys} that we find in
   * resource bundle {@code rb}.
   * Throws an exceptions if no such key is found in the resource bundle, or the found value associated with
   * the first key found is of a different type.
   *
   * @mudo unit tests
   */
  @MethodContract(
    pre  = {
      @Expression("_rb != null"),
      @Expression("_keys != null"),
      @Expression("_valueType != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("exists(i : 0 .. _keys.length) {result == value(_rb, _keys[i], _valueType) && " +
                    "for (j : 0 .. i) {value(_rb, _keys[j], _valueType) throws KeyNotFoundException}}")
    },
    exc  = {
      @Throw(type = KeyNotFoundException.class,
             cond = @Expression("for (j : 0 .. _keys.length) {value(_rb, _keys[j], _valueType) throws KeyNotFoundException}")),
      @Throw(type = WrongValueTypeException.class,
             cond = @Expression("exists(i : 0 .. _keys.length) {value(_rb, _keys[i], _valueType) throws WrongValueTypeException && " +
                    "for (j : 0 .. i) {value(_rb, _keys[j], _valueType) throws KeyNotFoundException}}"))
    }
  )
  public static <_T_> _T_ value(ResourceBundle rb, final String[] keys, Class<_T_> valueType) throws WrongValueTypeException, KeyNotFoundException {
    assert preArgumentNotNull(rb, "rb");
    assert preArgumentNotNull(keys, "keys");
    assert preArgumentNotNull(valueType, "valueType");
    _T_ result = null;
    int i = 0;
    while (result == null && i < keys.length) {
      try {
        result = value(rb, keys[i], valueType);
      }
      catch (KeyNotFoundException exc) {
        // NOP search further
      }
    }
    if (result == null) {
      throw new KeyNotFoundException(rb, keys);
    }
    return result;
  }

  /**
   * The value associated with key {@code key} in resource bundle {@code rb} of type {@code valueType}.
   * Throws exceptions if no such key is found in the resource bundle, or the found value associated with
   * the key is of a different type.
   *
   * @mudo unit tests
   */
  @MethodContract(
    pre  = {
      @Expression("_rb != null"),
      @Expression("_valueType != null")
    },
    post = @Expression("_rb.getObject(_key)"),
    exc  = {
      @Throw(type = KeyNotFoundException.class, cond = @Expression("_key == null")),
      @Throw(type = KeyNotFoundException.class, cond = @Expression("_rb.getObject(_key) throws MissingResourceException")),
      @Throw(type = WrongValueTypeException.class, cond = @Expression("! _valueType.instance(_rb.getObject(_key))"))
    }
  )
  public static <_T_> _T_ value(final ResourceBundle rb, final String key, Class<_T_> valueType) throws KeyNotFoundException, WrongValueTypeException {
    assert preArgumentNotNull(rb, "rb");
    assert preArgumentNotNull(valueType, "valueType");
    Object result = null;
    try {
      result = rb.getObject(key);
          /* throws MissingResourceException, ClassCastException */
    }
    catch (NullPointerException npExc) {
      throw new KeyNotFoundException(rb, key, npExc);
    }
    catch (MissingResourceException mrExc) {
      throw new KeyNotFoundException(rb, key, mrExc);
    }
    try {
      return valueType.cast(result);
    }
    catch (ClassCastException ccExc) {
      throw new WrongValueTypeException(rb, key, valueType, result, ccExc);
    }
  }

//  /**
//   * Get the String value associated with <code>key</code> in <code>rb</code>
//   * robustly. If anything goes wrong, return <code>null</code> silently.
//   * In most l10n contexts, this is preferred over getting an exception.
//   * Failures are logged at level WARN.
//   *
//   * @param     rb
//   *            The resource bundle to look in.
//   * @param     key
//   *            The key to look for.
//   * @pre       rb != null;
//   * @pre       key != null && key.length() > 0;
//   * @return    ; <code>null</code> if the resource could not be retrieved
//   */
//  public static String robustStringFromResourceBundle(final ResourceBundle rb,
//                                                      final String key) {
//    assert rb != null : "rb != null;";
//    assert (key != null && key.length() > 0)
//        : "key != null && key.length() > 0";
//    String result = null;
//    try {
//      result = rb.getString(key);
//          /* throws MissingResourceException, ClassCastException */
//      LOG.debug("value for key " + key + " in resource bundle " +
//               rb + " found: " + result);
//    }
//    catch (ClassCastException ccExc) {
//      LOG.warn("value for key " + key + " in resource bundle " +
//               rb + " is not a String; returning null", ccExc);
//    }
//    catch (MissingResourceException mrExc) {
//      LOG.warn("no entry for key " + key + " in resource bundle " +
//               rb + "; returning null", mrExc);
//    }
//    return result;
//  }


}
