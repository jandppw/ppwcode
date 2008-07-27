/*<license>
Copyright 2006 - $Date: 2008-04-04 00:19:52 +0200 (Fri, 04 Apr 2008) $ by Jan Dockx.

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


import java.lang.reflect.Field;

import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>Utility methods for reflection. Use these methods if you are
 *   interested in the result of reflection, and not in a particular
 *   reason why some reflective inspection might have failed.</p>
 *
 * @idea (jand) most methods are also in ppw-bean; consolidate
 *
 * @author Jan Dockx
 *
 * @todo move to ppwcode util reflection
 */
@CvsInfo(revision = "$Revision: 315 $",
         date     = "$Date: 2008-04-04 00:19:52 +0200 (Fri, 04 Apr 2008) $",
         state    = "$State$",
         tag      = "$Name$")
public class ConstantHelpers {

  private ConstantHelpers() {
    // NOP
  }

  /**
   * Returns the constant(public final static) with the given fully qualified
   * name.
   *
   * @param     fqClassName
   *            The fully qualified class name of the type to look in
   *            for the constant.
   * @param     constantName
   *            The name of the constant whose value to return.
   * @return    Object
   *            The value of the field named <code>constantName</code>
   *            in class <code>fqClassName</code>.
   * @throws    CannotGetClassException
   *            Could not load class <code>fqClassName</code>.
   * @throws    CannotGetValueException
   *            Error retrieving value.
   */
  public static <_ConstantValue_> _ConstantValue_ constant(final String fqClassName,
                                                           final String constantName)
      throws CannotGetClassException, CannotGetValueException {
    Class<?> clazz = Classes.loadForName(fqClassName);
    return constant(clazz, constantName);
  }

  /**
   * Returns the constant (public final static) with the given fully qualified
   * name. A simple exception is returned if something goes wrong. We can work
   * only with public class variables, and we look for these in class {@code clazz}
   * and super classes.
   *
   * @param     clazz
   *            The type to look in for the constant.
   * @param     constantName
   *            The name of the constant whose value to return.
   * @result    clazz.getField(constantName).get(null);
   *            The value of the field named <code>constantName</code>
   *            in class <code>clazz</code>.
   * @throws    CannotGetValueException
   *            clazz == null;
   * @throws    CannotGetValueException
   *            constantName == null;
   * @throws    CannotGetValueException
   *            clazz.getField(constantName) throws NoSuchFieldException;
   * @throws    CannotGetValueException
   *            clazz.getField(constantName) throws SecurityException;
   * @throws    CannotGetValueException
   *            clazz.getField(constantName).get(null) throws IllegalAccessException;
   * @throws    CannotGetValueException
   *            clazz.getField(constantName).get(null) throws IllegalArgumentException;
   *            constantName is not a static field
   */
  public static <_ConstantValue_> _ConstantValue_ constant(final Class<?> clazz,
                                                           final String constantName)
      throws CannotGetValueException {
    try {
      Field field = clazz.getField(constantName); // NoSuchFieldException
                                                  // NullPointerException
                                                  // SecurityException
      @SuppressWarnings("unchecked") _ConstantValue_ result =
          (_ConstantValue_)field.get(null); // IllegalAccessException
                                            // IllegalArgumentException
                                            // NullPointerException; cannot happen
                                            // ExceptionInInitializerError
      return result;
      // not checking for static; why should we?
    }
    catch (NullPointerException npExc) {
      throw new CannotGetValueException(clazz, constantName, npExc);
    }
    catch (NoSuchFieldException nsfExc) {
      throw new CannotGetValueException(clazz, constantName, nsfExc);
    }
    catch (SecurityException sExc) {
      throw new CannotGetValueException(clazz, constantName, sExc);
    }
    catch (IllegalAccessException iaExc) {
      throw new CannotGetValueException(clazz, constantName, iaExc);
    }
    catch (IllegalArgumentException iaExc) {
      throw new CannotGetValueException(clazz, constantName, iaExc);
    }
    catch (ExceptionInInitializerError iaExc) {
      assert false : "Should not happen";
      return null; // make compiler happy
    }
  }

}
