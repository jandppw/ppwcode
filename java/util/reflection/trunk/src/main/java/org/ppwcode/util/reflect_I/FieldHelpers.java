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


import static java.util.Collections.reverse;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.deadBranch;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Convenience methods for working with fields.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class FieldHelpers {

  /**
   * Return the field with name {@code fieldName} of class {@code c} or one
   * of its super types. {@link Class#getDeclaredFields()} only returns the fields of
   * this class, and doesn't look in super classes. {@link Class#getFields()} only returns
   * public fields of this class or any super class. This method returns a field with name
   * {@code fieldName}, of any accessibility, of class {@code c}, or, if there is no field
   * with name {@code fieldName} in {@code c}, a field with name {@code fieldName}, of any
   * accessibility, of the superclass of {@code c}, recursively. If no field with name
   * {@code fieldName} is found in the class hierarchy of {@code c}, this is a programming
   * error.
   */
  @MethodContract(
    pre = {
      @Expression("c != null"),
      @Expression("fieldName != null"),
      @Expression("exists (f : fields(c)) {f.getName() == fieldName}")
    },
    post = @Expression("c.getDeclaredField(fieldName) throws NoSuchFieldException ? " +
        "field(c.getSuperClass(), fieldName) : c.getDeclaredField(fieldName)")
  )
  public static Field field(Class<?> c, String fieldName) {
    assert preArgumentNotNull(c, "c");
    assert preArgumentNotNull(fieldName, "fieldName");
    Class<?> current = c;
    while (current != null) {
      try {
        Field result = current.getDeclaredField(fieldName);
        return result;
      }
      catch (NoSuchFieldException nsfExc) {
        current = current.getSuperclass();
      }
    }
    deadBranch("no field with name "+ fieldName + " found");
    return null; // keep compiler happy
  }

  /**
   * The instance fields of class {@code c} and its super classes. The order of the fields is
   * the lexical order in which the fields are defined in the class, from class {@link Object}
   * (which has no fields) down to the class {@code c}.
   *
   * @todo add order to contract
   */
  @MethodContract(
    pre  = @Expression("c != null"),
    post = {
      @Expression("result = filter (Field f : fields(c)) {Modifiers.isStatic(f.modifiers)}")
    }
  )
  public static List<Field> instanceFields(final Class<?> c) {
    assert preArgumentNotNull(c, "c");
    List<Field> result = new ArrayList<Field>();
    Class<?> current = c;
    while (current != null) {
      Field[] fs = current.getDeclaredFields();
      List<Field> fsl = Arrays.asList(fs);
      reverse(fsl);
      for (Field f : fsl) {
        if (! Modifier.isStatic(f.getModifiers())) {
          result.add(f);
        }
      }
      current = current.getSuperclass();
    }
    reverse(result);
    return result;
  }


  /**
   * The fields of class {@code c} and its super classes. The order of the fields is the lexical
   * order in which the fields are defined in the class, from class {@link Object} (which has no
   * fields) down to the class {@code c}.
   *
   * @todo add order to contract
   */
  @MethodContract(
    pre  = @Expression("c != null"),
    post = {
      @Expression("c != Object.class ? result == c.getDeclaredFields() U fields(c.getSuperClass())"),
      @Expression("c == Object.class ? result.isEmpty()")
    }
  )
  public static List<Field> fields(Class<?> c) {
    assert preArgumentNotNull(c, "c");
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
   */
  @MethodContract(
    pre  = {
      @Expression("object != null"),
      @Expression("field != null")
    },
    post = @Expression("field.get(object)")
  )
  public final static Object fieldValue(Object object, Field field) {
    assert preArgumentNotNull(object, "object");
    assert preArgumentNotNull(field, "field");
    return fieldValue(object, field, Object.class);
  }

  /**
   * The value of field {@code field} of object {@code object}, returned with static type {@code expectedType}.
   * The value is read, even on private fields. The security settings must allow this.
   */
  @MethodContract(
    pre  = {
      @Expression("object != null"),
      @Expression("field != null"),
      @Expression("expectedType != null")
    },
    post = @Expression("field.get(object)"),
    exc  = @Throw(type = ClassCastException.class,
                  cond = @Expression("! fieldValue(object, field) instanceof expectedType"))
  )
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
