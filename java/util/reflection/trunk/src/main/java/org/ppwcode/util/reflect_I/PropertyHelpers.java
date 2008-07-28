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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
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
 * <a href="http://commons.apache.org/beanutils/" target="extern">Apache Jakarta Commons BeanUtils</a>,
 * and not a replacement.
 *
 * @note The methods of the class {@code Beans} of the previous version have been moved here and into {@link ClassHelpers}.
 *       Furthermore, a number of methods are removed, notably:
 * <dl>
 *   <dt>{@code PropertyDescriptor getPropertyDescriptor(final Class beanClass, final String propertyName)}</dt>
 *   <dd>use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
 *       target="extern">Apache Jakarta Commons BeanUtils PropertyUtil.getPropertyDescriptor()</a> instead</dd>
 *   <dt>{@coe setPropertyValueCoerced(final Object bean, final String propertyName, final Object value)}</dt>
 *   <dd>No replacement, except <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
 *       target="extern">Apache Jakarta Commons BeanUtils PropertyUtil.setProperty(Object, String, Object)</a>;
 *       the method was removed because there was a dependency on Apache JSTL EL, which is really too silly in a package like this.</dd>
 *   <dt>{@code void setPropertyValue(final Object bean, final String propertyName, final Object value)}</dt>
 *   <dd>previously deprecated; use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
 *       target="extern">Apache Jakarta Commons beanUtils PropertyUtil.setProperty()</a> instead.</dd>
 *   <dt>{@code Object getPropertyValue(final Object bean, final String propertyName)}</dt>
 *   <dd>previously deprecated; use  <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
 *       target="extern">Apache Jakarta Commons beanUtils PropertyUtil.getProperty()</a> instead.</dd>
 *   <dt>{@code Object getNavigatedPropertyValue(final Object bean, final String propertyExpression)}</dt>
 *   <dd>previously deprecated; use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
 *       target="extern">Apache Jakarta Commons beanUtils PropertyUtil.getProperty()</a> instead.</dd>
 * </dl>
 *
 * @mudo (jand) most methods are also in Toryt.support.Reflection; consolidate
 * @mudo do away with all the different exceptions
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class PropertyHelpers {

  private PropertyHelpers() {
    // no instances possible
  }

  /**
   * Returns the {@link PropertyDescriptor} of the property with
   * name <code>propertyName</code> of <code>beanClass</code>.
   * If no property descriptor is found, {@code null} is returned.
   *
   * @param     beanClass
   *            The bean class to get the property descriptor of
   * @param     propertyName
   *            The programmatic name of the property we want the descriptor for
   *
   * @note In contrast to other methods in this class, the propertyName cannot be
   *       nested or indexed.
   * @note This method was deprecated in version V, and now no longer is. The reason
   *       for its existence is that {@link PropertyUtils} does not feature a method
   *       to retrieve a {@link PropertyDescriptor} based on a {@link Class} argument.
   *       Since the previous version, the semantics have changed! No more exception
   *       is thrown, but {@code null} is returned if no descriptor is found.
   *
   * @idea extend to use BeanUtils nested notation for properties
   */
  @MethodContract(
    pre  = @Expression("beanClass != null"),
    post = {
      @Expression("PropertyUtils.getPropertyDescriptors(^beanClass).contains(result)"),
      @Expression("result.name == ^propertyName")
    }
  )
  public static PropertyDescriptor getPropertyDescriptor(final Class<?> beanClass, final String propertyName) {
    assert beanClass != null;
    PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(beanClass);
      // entries in the array are never null
    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
      assert propertyDescriptor != null;
      if (propertyDescriptor.equals(propertyName)) {
        return propertyDescriptor;
      }
    }
    return null;
  }

  /**
   * Return the type of the property with name <code>propertyName</code>
   * of type <code>type</code>.
   *
   * @pre (type != null) && (propertyName != null) &&
   *        (! propertyName.equals(""));
   * @throws NoSuchMethodException
   *         Type <code>type</code> has no property with name
   *         <code>propertyName</code>.
   *
   * @idea (jand) should have existed in commons beanutils; move to ppw-utils
   */
  public static Class<?> getPropertyType(final Class<?> type, final String propertyName)
      throws NoSuchMethodException {
    return getPropertyDescriptor(type, propertyName).getPropertyType();
  }

  /**
   * Return true if {@code beanClass} has a property with name {@code propertyName}.
   *
   * @note In contrast to other methods in this class, the propertyName cannot be
   *       nested or indexed.
   *
   * @idea extend to use BeanUtils nested notation for properties
   */
  @MethodContract(
    pre  = @Expression("^beanClass != null"),
    post = @Expression("exists(PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(beanClass)) {pd.name == ^propertyName}")
  )
  public static boolean hasProperty(final Class<?> beanClass, final String propertyName) {
    assert beanClass != null;
    PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(beanClass);
    for (PropertyDescriptor prop : props) {
      assert prop.getName() != null;
      if (prop.getName().equals(propertyName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return true if {@code bean} has a property with name {@code propertyName}.
   * {@code propertyName} can be a nested or indexed property name. In that
   * case, the actual dynamic value of the separate steps in the chain
   * is used to go further, and as a result, several exceptions might occur.
   * They are eaten however, and {@code null} is returned.
   *
   * @param propertyName
   *        The name of the property to verify. This can be a nested or indexed
   *        property name.
   */
  @MethodContract(
    pre  = @Expression("^bean != null"),
    post = @Expression("PropertyUtils.getPropertyDescriptor(^bean, ^propertyName) != null")
  )
  public static boolean hasProperty(final Object bean, final String propertyName) {
    assert bean != null;
    try {
      PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, propertyName);
      return pd != null;
    }
    catch (IllegalAccessException iaExc) {
      return false;
    }
    catch (IllegalArgumentException iaExc) {
      return false;
    }
    catch (InvocationTargetException iExc) {
      return false;
    }
    catch (NoSuchMethodException nsmExc) {
      return false;
    }
  }

  /**
   * Returns the {@link PropertyEditor} of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property does not exist, an exception is thrown. If no editor is found,
   * <code>null</code> is returned.
   *
   * @param     beanClass
   *            The bean class to get the property editor of
   * @param     propertyName
   *            The programmatic name of the property we want the editor for
   *
   * @return    PropertyEditor
   *            Returns the {@link PropertyEditor} of the property with
   *            name <code>propertyName</code> of <code>beanClass</code>.
   */
  @MethodContract(
    pre  = @Expression("^beanClass != null"),
    post = @Expression("PropertyUtils.getPropertyDescriptor(^beanClass, ^propertyName"),
    exc  = {
      @Throw(
        type = IntrospectionException.class,
        cond = @Expression("getPropertyDescriptor(^beanClass, ^propertyName) == null")
      ),
      @Throw(
        type = InstantiationException.class,
        cond = @Expression(value = "true", description = "exception when instantiating the property editor")
      ),
      @Throw(
        type = IllegalAccessException.class,
        cond = @Expression(value = "true", description = "exception when instantiating the property editor")
      ),
      @Throw(
        type = ClassCastException.class,
        cond = @Expression(value = "true", description = "exception when instantiating the property editor")
      )
    }
  )
  public static PropertyEditor getPropertyEditorInstance(final Class<?> beanClass, final String propertyName)
      throws IntrospectionException, InstantiationException, IllegalAccessException, ClassCastException {
    assert beanClass != null;
    PropertyDescriptor descriptor = getPropertyDescriptor(beanClass, propertyName);
    if (descriptor == null) {
      throw new IntrospectionException("no descriptor found for property " + propertyName +
                                       " of class " + beanClass);
    }
    Class<?> editorClass = descriptor.getPropertyEditorClass();
    if (editorClass != null) {
      return (PropertyEditor)editorClass.newInstance();
      // InstantiationException, IllegalAccessException, ClassCastException
    }
    else {
      return PropertyEditorManager.findEditor(descriptor.getPropertyType());
    }
  }

  /**
   * Returns the method object of the inspector of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property or such an inspector does not exist, an exception is thrown.
   * This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods.
   *
   * @todo check that This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods, and fix.
   *
   * @param     beanClass
   *            The bean class to get the property read method of
   * @param     propertyName
   *            The programmatic name of the property we want to read
   */
  @MethodContract(
    pre  = @Expression("beanClass != null"),
    post = {
      @Expression("result != null"),
      @Expression("getPropertyDescriptor(beanClass, propertyName).readMethod")
    },
    exc  = {
      @Throw(type = IntrospectionException.class, cond = @Expression(value = "true", description = "Cannot get the BeanInfo of <beanClass.")),
      @Throw(type = NoSuchMethodException.class, cond = @Expression(value = "getPropertyDescriptor(beanClass, propertyName).readMethod == null"))
    }
  )
  public static Method getPropertyReadMethod(final Class<?> beanClass, final String propertyName)
      throws IntrospectionException, NoSuchMethodException {
    assert beanClass != null;
    Method inspector = getPropertyDescriptor(beanClass, propertyName).getReadMethod();
        // this can be null for an read-protected property
    if (inspector == null) {
      throw new NoSuchMethodException("No read method for property " //$NON-NLS-1$
                                      + propertyName);
    }
    return inspector;
  }

  /**
   * Check whether <code>beanClass</code> has a no-arguments getter for
   * <code>propertyName</code>. This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods.
   *
   * @todo check that This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods, and fix.
   *
   * @param     beanClass
   *            The bean class to get the property read method of
   * @param     propertyName
   *            The programmatic name of the property we want to read
   */
  @MethodContract(
    pre  = @Expression("beanClass != null"),
    post = @Expression("getPropertyDescriptor(beanClass, propertyName).readMethod != null"),
    exc  = @Throw(type = IntrospectionException.class, cond = @Expression(value = "true", description = "Cannot get the BeanInfo of <beanClass."))
  )
  public static boolean hasPropertyReadMethod(final Class<?> beanClass, final String propertyName)
      throws IntrospectionException {
    assert beanClass != null;
    return getPropertyDescriptor(beanClass, propertyName).getReadMethod() != null;
  }

  /**
   * Returns the method object of the mutator of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property or such an mutator does not exist, an exception is thrown.
   *
   * @param     beanClass
   *            The bean class to get the property write method of
   * @param     propertyName
   *            The programmatic name of the property we want to write
   */
  @MethodContract(
    pre  = @Expression("beanClass != null"),
    post = {
      @Expression("result != null"),
      @Expression("getPropertyDescriptor(beanClass, propertyName).writeMethod")
    },
    exc  = {
      @Throw(type = IntrospectionException.class, cond = @Expression(value = "true", description = "Cannot get the BeanInfo of <beanClass.")),
      @Throw(type = NoSuchMethodException.class, cond = @Expression(value = "getPropertyDescriptor(beanClass, propertyName).writeMethod == null"))
    }
  )
  public static Method getPropertyWriteMethod(final Class<?> beanClass, final String propertyName)
      throws IntrospectionException, NoSuchMethodException {
    assert beanClass != null;
    Method mutator = getPropertyDescriptor(beanClass, propertyName).getWriteMethod();
        // this can be null for a write-protected property
    if (mutator == null) {
      throw new NoSuchMethodException("No write method for property " //$NON-NLS-1$
                                      + propertyName);
    }
    return mutator;
  }

}
