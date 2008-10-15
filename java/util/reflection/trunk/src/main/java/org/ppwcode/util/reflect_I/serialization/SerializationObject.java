/*<license>
Copyright 2004 - $Date: 2008-10-05 20:33:16 +0200 (Sun, 05 Oct 2008) $ by PeopleWare n.v..

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


import static org.ppwcode.util.reflect_I.InstanceHelpers.newInstance;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;


public class SerializationObject implements Serializable {

  public Class<?> serializedClass;

  public final Set<SerializationInstanceVariable> instanceVariables = new HashSet<SerializationInstanceVariable>();

  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("##{[");
    sb.append(serializedClass == null ? "unknown class" : serializedClass.getName());
    sb.append(" -- ");
    sb.append(instanceVariables);
    sb.append("]}##");
    return sb.toString();
  }

  private Object readResolve() throws ObjectStreamException {
    Object result = newInstance(serializedClass);
    for (SerializationInstanceVariable siv : instanceVariables) {
      try {
        Field f = siv.declaringClass.getDeclaredField(siv.name);
        boolean oldAccessible = f.isAccessible();
        f.setAccessible(true);
        f.set(result, siv.value);
        f.setAccessible(oldAccessible);
      }
      catch (SecurityException exc) {
        new InvalidObjectException(exc.toString());
      }
      catch (NoSuchFieldException exc) {
        new InvalidObjectException(exc.toString());
      }
      catch (IllegalArgumentException exc) {
        new InvalidObjectException(exc.toString());
      }
      catch (IllegalAccessException exc) {
        unexpectedException(exc);
      }
    }
    return result;
  }

}

