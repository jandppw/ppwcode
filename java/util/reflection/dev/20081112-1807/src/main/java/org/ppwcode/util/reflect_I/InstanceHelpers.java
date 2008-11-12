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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.MethodHelpers.constructor;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.newAssertionError;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Utility methods for instance reflection. Use these methods if you are interested in the result of reflection,
 *   and not in a particular reason why some reflective inspection might have failed. The ppwcode exception
 *   vernacular is applied.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class InstanceHelpers {

  private InstanceHelpers() {
    // NOP
  }

//  // MUDO tests and contracts
//  public static <_Class_> _Class_ newInstance(Class<_Class_> clazz) {
//    preArgumentNotNull(clazz, "clazz");
//    _Class_ result = null;
//    try {
//      result = clazz.newInstance();
//    }
//    catch (InstantiationException exc) {
//      unexpectedException(exc, "trying to instantiage " + clazz + " with default constructor");
//    }
//    catch (IllegalAccessException exc) {
//      unexpectedException(exc, "trying to instantiage " + clazz + " with default constructor");
//    }
//    catch (ExceptionInInitializerError err) {
//      unexpectedException(err, "trying to instantiage " + clazz + " with default constructor");
//    }
//    catch (SecurityException exc) {
//      unexpectedException(exc, "trying to instantiage " + clazz + " with default constructor");
//    }
//    return result;
//  }
//
//  // MUDO tests and contracts
//  public static <_Class_> _Class_ robustNewInstance(Class<_Class_> clazz) {
//    preArgumentNotNull(clazz, "clazz");
//    _Class_ result = null;
//    try {
//      result = clazz.newInstance();
//    }
//    catch (InstantiationException exc) {
//      unexpectedException(exc, "trying to instantiage " + clazz + " with default constructor");
//    }
//    catch (IllegalAccessException exc) {
//      unexpectedException(exc, "trying to instantiage " + clazz + " with default constructor");
//    }
//    catch (ExceptionInInitializerError err) {
//      unexpectedException(err, "trying to instantiage " + clazz + " with default constructor");
//    }
//    catch (SecurityException exc) {
//      unexpectedException(exc, "trying to instantiage " + clazz + " with default constructor");
//    }
//    return result;
//  }

  /**
   * Does not work currently with null argument. Try heuristic, based on number of arguments, etc...
   *
   * @throws InvocationTargetException The constructor throws an exception
   *
   * @mudo contract and unit test
   */
  public static <_Class_> _Class_ robustNewInstance(Class<_Class_> clazz, Object... arguments) throws InvocationTargetException {
    preArgumentNotNull(clazz, "clazz");
    Class<?>[] parameterTypes = new Class<?>[arguments.length];
    for (int i = 0; i < arguments.length; i++) {
      if (arguments[i] == null) {
        throw newAssertionError("MUDO cannot derive type from null; this has to be fixed in a next version", null);
      }
      parameterTypes[i] = arguments[i].getClass();
    }
    Constructor<_Class_> c = constructor(clazz, parameterTypes);
    _Class_ result = null;
    try {
      result = c.newInstance(arguments);
    }
    catch (IllegalAccessException exc) {
      unexpectedException(exc, "trying to instantiage " + clazz + " with constructor with arguments " + Arrays.deepToString(arguments));
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc, "trying to instantiage " + clazz + " with constructor with arguments " + Arrays.deepToString(arguments));
    }
    catch (InstantiationException exc) {
      unexpectedException(exc, "trying to instantiage " + clazz + " with constructor with arguments " + Arrays.deepToString(arguments));
    }
    catch (ExceptionInInitializerError err) {
      unexpectedException(err, "trying to instantiage " + clazz + " with default constructor");
    }
    return result;
  }

  /**
   * Does not work currently with null argument. Try heuristic, based on number of arguments, etc...
   *
   * @mudo contract and unit test
   */
  public static <_Class_> _Class_ newInstance(Class<_Class_> clazz, Object... arguments) {
    try {
      return robustNewInstance(clazz, arguments);
    }
    catch (InvocationTargetException exc) {
      unexpectedException(exc, "trying to instantiage " + clazz + " with constructor with arguments " + Arrays.deepToString(arguments));
      return null; // keep compiler happy
    }
  }

}
