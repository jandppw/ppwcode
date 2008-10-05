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

package org.ppwcode.vernacular.semantics_VI.bean;


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
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Supporting methods for working with {@link SemanticBean SemanticBeans}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class SemanticBeanHelpers {

  private SemanticBeanHelpers() {
    // NOP
  }

  /**
   * The direct associated {@link SemanticBean SemanticBeans} starting from {@code sb}.
   * These are the beans that simple properties of {@code sb}, directly (to-one) or via
   * a collection (to-many).
   */
  @MethodContract(
    pre  = @Expression("_sb != null"),
    post = {
      @Expression("result != null"),
      @Expression("for (PropertyDescriptor pd : getPropertyDescriptors(_sb)) {" +
                    "SemanticBean.class.isAssignableFrom(pd.propertyType) && propertyValue(_sb, pd.name) != null ? " +
                      "result.contains(propertyValue(_sb, pd.name))" +
                  "}"), // MUDO add toMany
      @Expression("for (SemanticBean sbr : result) {" +
                    "exists (PropertyDescriptor pd : getPropertyDescriptors(_sb)) {" +
                      "sbr == propertyValue(_sb, pd.name)" +
                    "}" +
                  "}") // MUDO add toMany
    }
  )
  public static Set<SemanticBean> directAssociatedSemanicBeans(SemanticBean sb) {
    assert preArgumentNotNull(sb, "rb");
    Set<SemanticBean> result = new HashSet<SemanticBean>();
    PropertyDescriptor[] pds = getPropertyDescriptors(sb);
    for (int i = 0; i < pds.length; i++) {
      PropertyDescriptor pd = pds[i];
      if (SemanticBean.class.isAssignableFrom(pd.getPropertyType())) {
        SemanticBean candidate = propertyValue(sb, pd.getName());
        if (candidate != null) {
          result.add(candidate);
        }
      }
      else if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
        // get the elements, and if they are RousseauBeans, add them
        Set<? extends Object> many = null;
        try {
          @SuppressWarnings("unchecked")
          Set<? extends Object> anyMany = (Set<? extends Object>)getProperty(sb, pd.getName());
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
            if (object != null && object instanceof SemanticBean) {
              result.add((SemanticBean)object);
            }
          }
        }
      }
    }
    return result;
  }

}
