/*<license>
Copyright 2004 - $Date: 2008-10-01 23:01:20 +0200 (Wed, 01 Oct 2008) $ by PeopleWare n.v..

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


import static java.util.Collections.reverse;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Convenience methods for working with fields.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-10-01 23:01:20 +0200 (Wed, 01 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2821 $",
         date     = "$Date: 2008-10-01 23:01:20 +0200 (Wed, 01 Oct 2008) $")
public final class FieldHelpers {

  /**
   * THe fields of class {@code c} and its superclasses. The order
   * of the fields is the lexical order in which the fields are defined in the
   * class, from class {@link Object} (which has no fields) down to the
   * class {@code c}.
   *
   * @mudo
   */
  public static List<Field> fields(Class<?> c) {
    List<Field> result = new ArrayList<Field>();
    while (c != null) {
      Field[] fs = c.getDeclaredFields();
      List<Field> fsl = Arrays.asList(fs);
      reverse(fsl);
      result.addAll(fsl);
      c = c.getSuperclass();
    }
    reverse(result);
    return result;
  }

  /**
   * The value of field {@code field} of object {@code object}.
   * The value is read, even on private fields. The security settings must allow this.
   *
   */
  public final static Object fieldValue(Object object, Field field) {
    assert preArgumentNotNull(object, "object");
    assert preArgumentNotNull(field, "field");
    return fieldValue(object, field, Object.class);
  }

  /**
   * The value of field {@code field} of object {@code object}, returned with static type {@code expectedType}.
   * The value is read, even on private fields. The security settings must allow this.
   *
   */
  public final static <_T_> _T_ fieldValue(Object object, Field field, Class<_T_> expectedType) throws ClassCastException {
    assert preArgumentNotNull(object, "object");
    assert preArgumentNotNull(field, "field");
    assert preArgumentNotNull(expectedType, "expectedType");
    boolean oldAccessible = field.isAccessible();
    field.setAccessible(true);
    _T_ result = null;
    try {
      result = expectedType.cast(field.get(object)); // ClassCastException
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc);
    }
    catch (IllegalAccessException exc) {
      unexpectedException(exc);
    }
    field.setAccessible(oldAccessible);
    return result;
  }

}
