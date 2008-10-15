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
@Copyright("2004 - $Date: 2008-10-01 23:01:20 +0200 (Wed, 01 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2821 $",
         date     = "$Date: 2008-10-01 23:01:20 +0200 (Wed, 01 Oct 2008) $")
public final class SerializationHelpers {

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
          siv.value = fieldValue(s, field, Serializable.class); // ClassCastExcxeption
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
