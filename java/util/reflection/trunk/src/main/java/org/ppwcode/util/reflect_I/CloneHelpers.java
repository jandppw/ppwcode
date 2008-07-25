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

package org.ppwcode.util.reflect_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

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
 * Note that there is no type in the Java API that features the {@code clone()} method.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class CloneHelpers {

  private CloneHelpers() {
    // no instances possible
  }

  private static final String CLONE_SIGNATURE = "clone()";

  /**
   * If {@code object} is {@link Cloneable}, return a clone.
   * Otherwise, return {@code object} itself.
   */
  @MethodContract(
    pre  = @Expression("! cloneable instanceof Cloneable || hasMethod(cloneable, 'clone()')"),
    post = @Expression("object instanceof Cloneable ? cloneable.clone() : cloneable")
  )
  public static <_T_> _T_ safeReference(_T_ object) {
    try {
      return object == null ? null : (object instanceof Cloneable ? clone(object) : object);
    }
    catch (CloneNotSupportedException exc) {
      assert false : "CloneNotSupportedException should not happen: " + exc;
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
    pre  = @Expression("^cloneable != null"),
    post = @Expression("cloneable.clone()"),
    exc  = {
      @Throw(type = CloneNotSupportedException.class, cond = @Expression("! cloneable instanceof Cloneable")),
      @Throw(type = CloneNotSupportedException.class, cond = @Expression("! hasMethod(cloneable, 'clone()')"))
    }
  )
  public static <_T_> _T_ clone(_T_ cloneable) throws CloneNotSupportedException {
    assert cloneable != null;
    if (! (cloneable instanceof Cloneable)) {
      throw new CloneNotSupportedException(cloneable + " does not implement Cloneable");
    }
    try {
      Method cm = MethodHelpers.findMethod(cloneable, CLONE_SIGNATURE); // CannotGetMethodException
      assert cm != null;
      Object result = cm.invoke(cloneable);
        /* IllegalAccessException, IllegalArgumentException, InvocationTargetException,
         * NullPointerException, ExceptionInInitializerError */
      @SuppressWarnings("unchecked") _T_ tResult = (_T_)result;
      return tResult;
    }
    catch (final CannotGetMethodException cgmExc) {
      throw new CloneNotSupportedException("could not find method " + CLONE_SIGNATURE + " for " + cloneable) {
        @Override
        public Throwable getCause() {
          return cgmExc;
        }
      };
    }
    catch (final IllegalAccessException exc) {
      assert false : "cannot happen (findMethod only returns public methods";
    }
    catch (IllegalArgumentException exc) {
      assert false : "cannot happen";
    }
    catch (InvocationTargetException exc) {
      assert false : "cannot happen: clone should never throw an exception";
    }
    catch (NullPointerException exc) {
      assert false : "cannot happen: cloneable is not null";
    }
    catch (ExceptionInInitializerError exc) {
      assert false : "cannot happen: clone should never throw an exception";
    }
    return null; // keep compiler happy
  }

}
