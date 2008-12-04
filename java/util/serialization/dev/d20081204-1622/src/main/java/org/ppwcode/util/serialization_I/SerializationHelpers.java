/*<license>
Copyright 2008 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.util.serialization_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.FieldHelpers.fieldValue;
import static org.ppwcode.util.reflect_I.FieldHelpers.fields;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Convenience methods for working overriding serialization..</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class SerializationHelpers {

  /**
   * <p>Create an alternative object for {@code s} to serialize instead of {@code s}.
   *   The alternative object contains all instance variable values of {@code s},
   *   except for those marked {@code transient} or annotated with {@link DoNotSerialize &#64;DoNotSerialize}.</p>
   * <p>Implement a protected method as follows in your hierarchy of classes you want to use this approach:</p>
   * <pre>
   *   protected final Object writeReplace() throws NotSerializableException {
   *     return {@link SerializationHelpers#replace(Serializable) SerializationHelpers.replace(this)};
   *   }
   * </pre>
   * <p>The counterpart method for deserialization is the private method {@code SerializationObject.readResolve()}.</p>
   *
   * <p><strong>This serialization utility also tackles a long standing problem with serialization of <code>ennum</code>s.
   *   This workaround should be removed from this library once the issue is fixed.</strong> See the package documentation
   *   for more information.</p>
   */
  public static Object replace(Serializable s) throws NotSerializableException {
    SerializationObject result = new SerializationObject();
    result.serializedClass = s.getClass();
    List<Field> fields = fields(s.getClass());
    for (Field field : fields) {
      if (! Modifier.isStatic(field.getModifiers()) &&
          ! Modifier.isTransient(field.getModifiers()) &&
          ! field.isAnnotationPresent(DoNotSerialize.class)) {
        SerializationInstanceVariable siv = new SerializationInstanceVariable();
        siv.declaringClass = field.getDeclaringClass();
        siv.name = field.getName();
        try {
          Serializable fieldValueToSerialize = fieldValue(s, field, Serializable.class); // ClassCastExcxeption
          if (Enum.class.isAssignableFrom(field.getType())) {
            /* ENUM SERIALIZATION PATCH
             * This if is a patch for a long standing enum deserialization bug
             * See the package documentation for more information.
             * The enum value is stored as its name() as String
             */
            fieldValueToSerialize = ((Enum<?>)fieldValueToSerialize).name();
          }
          siv.value = fieldValueToSerialize;
        }
        catch (ClassCastException ccExc) {
          Object fv = fieldValue(s, field);
          assert fv != null;
          throw new NotSerializableException(fv.getClass().getName());
        }
        result.instanceVariables.add(siv);
      }
    }
    return result;
  }

}
