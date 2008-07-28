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


import static java.lang.reflect.Modifier.isPublic;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.MethodHelpers.hasPublicMethod;
import static org.ppwcode.util.reflect_I.MethodHelpers.methodHelper;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.preArgumentNotNull;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.unexpectedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * Convenience methods for working with {@code clone()}.
 * Note that there is no type in the Java API that features {@code clone()} as a
 * public method, and we also cannot retroactively put an interface above existing
 * API classes.
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
    pre  = @Expression("cloneable instanceof Cloneable ? hasMethod(cloneable, 'clone()') && " +
                       "Modifier.isPublic(method(cloneable, 'clone()').modifiers)"),
    post = @Expression("object instanceof Cloneable ? cloneable.clone() : cloneable")
  )
  public static <_T_> _T_ safeReference(_T_ object) {
    try {
      return object == null ? null : (object instanceof Cloneable ? clone(object) : object);
    }
    catch (CloneNotSupportedException exc) {
      unexpectedException(exc);
      return null; // keep compiler happy
    }
  }

  /**
   * Clone {@code cloneable} if it implements {@link Cloneable}
   * and features a public {@code clone()} method. If {@code cloneable}
   * does not implement {@link Cloneable} or does not feature a public
   * {@code clone()} method, a {@link CloneNotSupportedException} is thrown.
   */
  @MethodContract(
    pre  = @Expression("_cloneable != null"),
    post = @Expression("cloneable.clone()"),
    exc  = {
      @Throw(type = CloneNotSupportedException.class, cond = @Expression("! cloneable instanceof Cloneable")),
      @Throw(type = CloneNotSupportedException.class,
             cond = @Expression("! hasMethod(cloneable, 'clone()') && " +
                                "Modifier.isPublic(method(cloneable, 'clone()').modifiers)"))
    }
  )
  public static <_T_> _T_ clone(_T_ cloneable) throws CloneNotSupportedException {
    preArgumentNotNull(cloneable, "cloneable");
    if (! (cloneable instanceof Cloneable)) {
      throw new CloneNotSupportedException(cloneable + " does not implement Cloneable");
    }
    try {
      Method cm = methodHelper(cloneable.getClass(), CLONE_SIGNATURE); // NoSuchMethodException
      assert cm != null;
      if (! isPublic(cm.getModifiers())) {
        throw new CloneNotSupportedException("Method " + CLONE_SIGNATURE + " of " + cloneable + " is not public");
      }
      Object result = cm.invoke(cloneable);
        /* IllegalAccessException, IllegalArgumentException, InvocationTargetException,
         * NullPointerException, ExceptionInInitializerError */
      @SuppressWarnings("unchecked") _T_ tResult = (_T_)result;
      return tResult;
    }
    catch (final NoSuchMethodException cgmExc) {
      CloneNotSupportedException cnsExc =
          new CloneNotSupportedException("Could not find method " + CLONE_SIGNATURE + " for " + cloneable);
      cnsExc.initCause(cgmExc);
      throw cnsExc;
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
  public static boolean isCloneable(Class<?> type) {
    return Cloneable.class.isAssignableFrom(type) && hasPublicMethod(type, CLONE_SIGNATURE);
  }

}
