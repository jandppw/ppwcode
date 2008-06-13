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

package org.ppwcode.util.reflect_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
//import org.apache.taglibs.standard.lang.jstl.Coercions;
//import org.apache.taglibs.standard.lang.jstl.ELException;
//import org.apache.taglibs.standard.lang.jstl.Logger;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * Convenience methods for working with JavaBeans.
 * These are to be considered merely extensions of methods available
 * in {@link java.beans} and
 * <a href="http://commons.apache.org/beanutils/" target="extern">Apache Jakarta Commons Beanutils</a>,
 * and not a replacement.
 *
 * @note Since the previous version, a number of methods are removed, notably:
 * <dl>
 *   <dt>{@code PropertyDescriptor getPropertyDescriptor(final Class beanClass, final String propertyName)}</dt>
 *   <dd>use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
 *       target="extern">Apache Jakarta Commons Beanutils PropertyUtil.getPropertyDescriptor()</a> instead</dd>
 * </dl>
 *
 * @mudo (jand) most methods are also in Toryt.support.Reflection; consolidate
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1082 $",
         date     = "$Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $")
public class Classes {

  private Classes() {
    // no instances possible
  }

  private static final String PREFIXED_FQCN_PATTERN = "\\."; //$NON-NLS-1$

  private static final String EMPTY = ""; //$NON-NLS-1$

  private static final String DOT = "."; //$NON-NLS-1$

  /**
   * Return a fully qualified class name that is in the same package
   * as <code>fqcn</code>, and has as class name
   * <code>prefix + <var>ClassName</var></code>.
   *
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The fully qualified class name to start from.
   */
  public static String prefixedFqcn(final String prefix,
                                    final String fqcn) {
    String[] parts = fqcn.split(PREFIXED_FQCN_PATTERN);
    String prefixedName = prefix + parts[parts.length - 1];
    String result = EMPTY;
    for (int i = 0; i < parts.length - 1; i++) {
      result = result + parts[i] + DOT;
    }
    result = result + prefixedName;
    return result;
  }

  /**
   * Load the class with name
   * <code>prefixedFqcn(prefix, fqcn)</code>.
   *
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The original fully qualified class name to derive
   *        the prefixed class name from.
   * @throws ClassNotFoundException
   *         true;
   */
  public static Class loadPrefixedClass(final String prefix,
                                        final String fqcn)
      throws ClassNotFoundException {
    return Class.forName(prefixedFqcn(prefix, fqcn));
  }

  /**
   * Instantiate an object of a type
   * <code>prefixedFqcn(prefix, fqcn)</code>.
   *
   * @param cl
   *        The class-loader from which we should create
   *        the bean. If this is null, then the system class-loader
   *        is used.
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The original fully qualified class name to derive
   *        the prefixed class name from.
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public static Object instantiatePrefixed(final ClassLoader cl,
                                           final String prefix,
                                           final String fqcn)
      throws IOException, ClassNotFoundException {
    return java.beans.Beans.instantiate(cl, prefixedFqcn(prefix, fqcn));
  }

}
