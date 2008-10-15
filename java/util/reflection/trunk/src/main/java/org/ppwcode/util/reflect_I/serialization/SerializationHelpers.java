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

package org.ppwcode.util.reflect_I.serialization;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers;


/**
 * <p>Convenience methods for working overriding serialization..</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-10-01 23:01:20 +0200 (Wed, 01 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2821 $",
         date     = "$Date: 2008-10-01 23:01:20 +0200 (Wed, 01 Oct 2008) $")
public final class SerializationHelpers {

  public static List<Field> fields(Class<?> c) {
    List<Field> result = new ArrayList<Field>();
    while (c != null) {
      Field[] fs = c.getDeclaredFields();
      result.addAll(Arrays.asList(fs));
      c = c.getSuperclass();
    }
    return result;
  }

  public void f(Object obj) {
    preArgumentNotNull(obj, "obj");
    // serialize from top to bottom
    List<Class<?>> classes = new ArrayList<Class<?>>();
    Class<?> current = obj.getClass();
    while (current !=  null) {
      classes.add(current);
      current = current.getSuperclass();
    }
    ListIterator<Class<?>> iter = classes.listIterator(classes.size());
    while (iter.hasPrevious()) {
      writeObjectSlice(obj, iter.previous());
    }
  }

  private void writeObjectSlice(Object obj, Class<?> clazz) {
    Field[] fields = clazz.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (! field.isAnnotationPresent(DoNotSerialize.class)) {
        writeValue(obj, field);
      }
    }
  }

  private void writeValue(Object obj, Field field) {
    field.setAccessible(true);
    try {
      Object value = field.get(obj);
      ////
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc);
    }
    catch (IllegalAccessException exc) {
      unexpectedException(exc);
    }
  }




}
