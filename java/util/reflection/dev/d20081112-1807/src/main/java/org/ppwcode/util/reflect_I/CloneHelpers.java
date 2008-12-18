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
import static org.ppwcode.util.reflect_I.MethodHelpers.hasPublicMethod;
import static org.ppwcode.util.reflect_I.MethodHelpers.isPublic;
import static org.ppwcode.util.reflect_I.MethodHelpers.method;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Convenience methods for working with {@code clone()}. Note that there is no type in the Java
 *   API that features {@code clone()} as a public method, and we also cannot retroactively put an
 *   interface above existing API classes. Use these methods if you are interested in the result,
 *   and not in a particular reason why something might have failed. The ppwcode exception vernacular
 *   is applied.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class CloneHelpers {

  private CloneHelpers() {
    // no instances possible
  }

  private static final String CLONE_SIGNATURE = "clone()";

  /**
   * If {@code object} is {@link Cloneable}, return a clone. Otherwise, return {@code object} itself.
   * If {@code object} is {@link Cloneable}, {@code object} must have a public {@code clone()} method.
   */
  @MethodContract(
    pre  = {
      @Expression("_object != null"),
      @Expression("_object instanceof Cloneable ? isKloneable(_object.class)")
    },
    post = @Expression("_object instanceof Cloneable ? _object.clone() : _object")
  )
  public static <_T_> _T_ safeReference(_T_ object) {
    return object == null ? null : (object instanceof Cloneable ? klone(object) : object);
  }

  /**
   * <p>Clone {@code kloneable} if it implements {@link Cloneable} and features a public {@code clone()} method.
   *   In contrast to the standard {@code clone()} method (in most cases) this method is type safe.
   *   If {@code kloneable} does not implement {@link Cloneable} or does not feature a public
   *   {@code clone()} method, this is considered a programming error. This method also can handle {@code null}.</p>
   * <p>
   * <p>The method is called {@code klone} with a &quot;k&quot; to avoid naming conflicts in using classes, where
   *   we would want to work with a static import {@code import static org.ppwcode.util.reflect_I.CloneHelpers.clone;}.
   *   This conflicts with the inherited {@link Object#clone()} method.</p>
   */
  @MethodContract(
    pre  = {
      @Expression("_kloneable != null ? isKloneable(_kloneable.class)")
    },
    post = @Expression("_kloneable != null ? _kloneable.clone() : null")
  )
  public static <_T_> _T_ klone(_T_ kloneable) {
    pre(kloneable != null ? isKloneable(kloneable.getClass()) : true);
    if (kloneable == null) {
      return null;
    }
    Method cm = method(kloneable.getClass(), CLONE_SIGNATURE);
    assert cm != null;
    assert isPublic(cm);
    try {
      Object result = cm.invoke(kloneable);
        /* IllegalAccessException, IllegalArgumentException, InvocationTargetException,
         * NullPointerException, ExceptionInInitializerError */
      @SuppressWarnings("unchecked") _T_ tResult = (_T_)result;
      return tResult;
    }
    catch (final IllegalAccessException exc) {
      unexpectedException(exc, "we only invoke public methods");
    }
    catch (IllegalArgumentException exc) {
      unexpectedException(exc);
    }
    catch (InvocationTargetException exc) {
      unexpectedException(exc, "invoked clone, which cannot throw exceptions");
    }
    catch (NullPointerException exc) {
      unexpectedException(exc);
    }
    catch (ExceptionInInitializerError exc) {
      unexpectedException(exc, "invoked clone, which cannot throw exceptions");
    }
    return null; // keep compiler happy
  }

  @MethodContract(
    post = @Expression("Cloneable.class.isAssignableFrom(type) && hasPublicMethod(type, CLONE_SIGNATURE)")
  )
  public static boolean isKloneable(Class<?> type) {
    return Cloneable.class.isAssignableFrom(type) && hasPublicMethod(type, CLONE_SIGNATURE);
  }

}
