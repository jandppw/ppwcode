/*<license>
Copyright 2004 - $Date: 2008-06-16 12:36:58 +0200 (Mon, 16 Jun 2008) $ by PeopleWare n.v..

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

package org.ppwcode.bean_VI;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * An implementation enforcing the rules defined in {@link SemanticBean}.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-06-16 12:36:58 +0200 (Mon, 16 Jun 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1177 $",
         date     = "$Date: 2008-06-16 12:36:58 +0200 (Mon, 16 Jun 2008) $")
public abstract class AbstractSemanticBean implements SemanticBean {

  @Override
  public final boolean equals(final Object other) {
    return super.equals(other);
  }

  @Override
  public final int hashCode() {
    return super.hashCode();
  }

  /**
   * Because this method is final, it is impossible to make a subtype
   * Cloneable succesfully.
   */
  @MethodContract(
    post = @Expression("false"),
    exc  = @Throw(type = CloneNotSupportedException.class, cond = @Expression("true"))
  )
  @Override
  protected final Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException("semantic objects may never be cloned");
  }

  @Override
  public final String toString() {
    StringBuilder result = new StringBuilder(super.toString());
    result.append("[");
    propertiesString(result);
    result.append("]");
    return result.toString();
  }

  private void propertiesString(StringBuilder result) {
    Iterator<String> iter = propertyNamesForToString().iterator();
    while (iter.hasNext()) {
      String pname = iter.next();
      result.append(pname);
      result.append(" = ");
      try {
        result.append(PropertyUtils.getProperty(this, pname));
      }
      // exceptions are programming errors
      catch (IllegalAccessException exc) {
        assert false : "IllegalAccessException should not happen: " + exc;
      }
      catch (InvocationTargetException exc) {
        assert false : "InvocationTargetException should not happen: " + exc;
      }
      catch (NoSuchMethodException exc) {
        assert false : "NoSuchMethodException should not happen: " + exc;
      }
      if (iter.hasNext()) {
        result.append(", ");
      }
    }
  }

  /**
   * A list of property names, used to list the
   * <code><var>propertyName</var> = <var>propertyValue</var></code> pairs
   * in {@link #toString()}, in the order of the returned array.
   *
   * The default implementation returns the names of all properties
   * that are not of a type that is a subtype of {@link Collection},
   * or an array, and that is not the {@link #getClass()} method.
   * The order is indeterminate.
   *
   * @mudo contract
   */
  protected Set<String> propertyNamesForToString() {
    PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(this);
    Set<String> result = new HashSet<String>(pds.length);
    for (int i = 0; i < pds.length; i++) {
      Class<?> propertyType = pds[i].getReadMethod().getReturnType();
      if (! Collection.class.isAssignableFrom(propertyType) &&
          ! propertyType.isArray() &&
          ! pds[i].getName().equals("class")) {
        result.add(pds[i].getName());
      }
    }
    return result;
  }

  /**
   * Convenience method for generating the toString of to-many associations.
   *
   * @mudo move somewhere else
   */
  @MethodContract(pre = @Expression("c != null"), post = @Expression("true"))
  protected final static String collectionString(final Collection<?> c) {
    assert c != null;
    StringBuilder result = new StringBuilder("{");
    for (Object item : c) {
      result.append(item.getClass().getName());
      result.append("@");
      result.append(Integer.toHexString(item.hashCode()));
    }
    result.append("}");
    return result.toString();
  }

}
