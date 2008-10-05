/*<license>
Copyright 2004 - $Date: 2008-10-05 17:38:34 +0200 (Sun, 05 Oct 2008) $ by PeopleWare n.v.

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


import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Supporting methods for working with associations (to-one, to-many).</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-10-05 17:38:34 +0200 (Sun, 05 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2931 $",
         date     = "$Date: 2008-10-05 17:38:34 +0200 (Sun, 05 Oct 2008) $")
public final class AssociationHelpers {

  private AssociationHelpers() {
    // NOP
  }

  /**
   * The direct associated objects of type {@code associatedType} starting from {@code bean}.
   * These are the beans that are properties of {@code bean}, directly (to-one) or via
   * a collection (to-many).
   */
  @MethodContract(
    pre  = {
      @Expression("_bean != null"),
      @Expression("_associatedType != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("for (PropertyDescriptor pd : getPropertyDescriptors(_bean)) {" +
                    "associatedType.isAssignableFrom(pd.propertyType) && propertyValue(_bean, pd.name) != null ? " +
                      "result.contains(propertyValue(_bean, pd.name))" +
                  "}"), // MUDO add toMany
      @Expression("for (_T_ t : result) {" +
                    "exists (PropertyDescriptor pd : getPropertyDescriptors(_bean)) {" +
                      "t == propertyValue(_bean, pd.name)" +
                    "}" +
                  "}") // MUDO add toMany
    }
  )
  public static <_T_> Set<_T_> directAssociatedBeans(_T_ bean, Class<_T_> associatedType) {
    assert preArgumentNotNull(bean, "bean");
    assert preArgumentNotNull(associatedType, "associatedType");
    Set<_T_> result = new HashSet<_T_>();
    PropertyDescriptor[] pds = getPropertyDescriptors(bean);
    for (int i = 0; i < pds.length; i++) {
      PropertyDescriptor pd = pds[i];
      if (associatedType.isAssignableFrom(pd.getPropertyType())) {
        _T_ candidate = propertyValue(bean, pd.getName());
        if (candidate != null) {
          result.add(candidate);
        }
      }
      else if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
        // get the elements, and if they are RousseauBeans, add them
        Set<? extends Object> many = null;
        try {
          @SuppressWarnings("unchecked")
          Set<? extends Object> anyMany = (Set<? extends Object>)getProperty(bean, pd.getName());
          many = anyMany;
        }
        catch (InvocationTargetException exc) {
          /* in a special case, to avoid problems with deserialized objects of a non-initialized
             JPA toMany, we deal with a NullPointerException as if it is a null property */
          if (exc.getCause() instanceof NullPointerException) {
            assert many == null;
            // NOP
          }
          else {
            unexpectedException(exc);
          }
        }
        catch (IllegalAccessException exc) {
          unexpectedException(exc);
        }
        catch (NoSuchMethodException exc) {
          unexpectedException(exc);
        }
        if (many != null) {
          for (Object object : many) {
            if (object != null && associatedType.isInstance(object)) {
              result.add(associatedType.cast(object));
            }
          }
        }
      }
    }
    return result;
  }

  /**
   * All objects of type {@code associatedType} that are reachable from {@code bean}.
   * These are the beans that are simple or multiple properties of {@code bean}. This is applied
   * recursively. {@code bean} itself is also part of the set.
   */
  @MethodContract(
    pre  = {
      @Expression("_bean != null"),
      @Expression("_associatedType != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("{_bean} U directAssociatedSemanticBeans(_bean) U " +
                   "union (_T_ t : directAssociatedSemanticBeans(_bean)) {associatedSemanticBeans(t)}")
    }
  )
  public static <_T_> Set<_T_> associatedBeans(_T_ bean, Class<_T_> associatedType) {
    assert preArgumentNotNull(bean, "bean");
    assert preArgumentNotNull(associatedType, "associatedType");
    LinkedList<_T_> agenda = new LinkedList<_T_>();
    agenda.add(bean);
    int i = 0;
    while (i < agenda.size()) {
      _T_ current = agenda.get(i);
      for (_T_ t : directAssociatedBeans(current, associatedType)) {
        if (! agenda.contains(t)) {
          agenda.add(t);
        }
      }
      i++;
    }
    return new HashSet<_T_>(agenda);
  }

}
