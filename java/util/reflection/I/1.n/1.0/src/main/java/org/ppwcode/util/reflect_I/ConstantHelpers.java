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
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.pre;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotEmpty;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Utility methods for constant reflection. Use these methods if you are interested in the result of reflection,
 *   and not in a particular reason why some reflective inspection might have failed. The ppwcode exception vernacular
 *   is applied.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @idea probably needed to add methods to take a FQN of a constant as a String
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class ConstantHelpers {

  private ConstantHelpers() {
    // NOP
  }

  /**
   * Returns the value of the constant (public final static) {@code constantName} in class {@code type}.
   * If that constant doesn't exist, or something else goes wrong, this is considered a programming error.
   * We can work only with public class variables, and we look for these in type {@code type} and super types.
   *
   * @param     type
   *            The type to look in for the constant.
   * @param     constantName
   *            The name of the constant whose value to return.
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("isConstant(_type, _constantName)")
    },
    post = {
      @Expression("_type.getField(_constantName).get(null)")
    }
  )
  public static <_ConstantValue_> _ConstantValue_ constant(final Class<?> type, final String constantName) {
    preArgumentNotNull(type);
    pre(isConstant(type, constantName));
    try {
      Field field = type.getField(constantName); // NoSuchFieldException, NullPointerException, SecurityException
      @SuppressWarnings("unchecked") _ConstantValue_ result =
          (_ConstantValue_)field.get(null); // IllegalAccessException, IllegalArgumentException,
                                            // NullPointerException; cannot happen
                                            // ExceptionInInitializerError
      return result;
      // not checking for static; why should we?
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc);
    }
    catch (NoSuchFieldException nsfExc) {
      unexpectedException(nsfExc);
    }
    catch (SecurityException sExc) {
      unexpectedException(sExc);
    }
    catch (IllegalAccessException iaExc) {
      unexpectedException(iaExc);
    }
    catch (IllegalArgumentException iaExc) {
      unexpectedException(iaExc);
    }
    catch (ExceptionInInitializerError iaExc) {
      unexpectedException(iaExc);
    }
    return null; // make compiler happy
  }

  /**
   * Is {@code constantName} a constant in {@code type} or one of its super types?
   * This means a variable with that name must exist that is static, final and public.
   */
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_constantName != null"),
      @Expression("_constantName != EMPTY")
    },
    post = {
      @Expression("exists (Field f : type.fields) {" +
                    "f.name == _constantName && Modifier.isFinal(f.modifiers) && Modifier.isStatic(f.modifiers)" +
                  "}")
    }
  )
  public static boolean isConstant(Class<?> type, String constantName) {
    preArgumentNotNull(type);
    preArgumentNotEmpty(constantName);
    boolean result = false;
    try {
      Field field = type.getField(constantName); // result is public for sure
      int fMods = field.getModifiers();
      result = Modifier.isFinal(fMods) && Modifier.isStatic(fMods);
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc, "constantName nor type or null here");
    }
    catch (SecurityException exc) {
      unexpectedException(exc);
    }
    catch (NoSuchFieldException exc) {
      result = false;
    }
    return result;
  }

}
