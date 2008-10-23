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
import static org.ppwcode.util.reflect_I.InstanceHelpers.newInstance;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * This type is used by {@link SerializationHelpers#replace(Serializable)} to replace
 * instances of other classes in serialization.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class SerializationObject implements Serializable {

  /**
   * The class of the original object this object is replacing in serialization.
   */
  public Class<?> serializedClass;

  /**
   * Data about the instance variables of the object we are replacing.
   */
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

  /**
   * This method, on deserialization of an object of this type, will replace this
   * object by what is returned by this method, i.e., an object of type {@link #serializedClass},
   * with all instance variables set according to {@link #instanceVariables}.
   *
   * This method must remain private to avoid unchecked access to the instance
   * variables of all objects. See the reasoning about the public methods
   * of {@code Externalizable} in the serialization specification.
   * (A lame excuse, since anybody can access private instance variables,
   * but hey).
   */
  private Object readResolve() throws InvalidObjectException {
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
        throw new InvalidObjectException(exc.toString());
      }
      catch (NoSuchFieldException exc) {
        throw new InvalidObjectException(exc.toString());
      }
      catch (IllegalArgumentException exc) {
        throw new InvalidObjectException(exc.toString());
      }
      catch (IllegalAccessException exc) {
        unexpectedException(exc);
      }
    }
    return result;
  }

}

