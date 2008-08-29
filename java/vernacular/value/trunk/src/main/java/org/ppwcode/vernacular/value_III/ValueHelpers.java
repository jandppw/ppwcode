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

package org.ppwcode.vernacular.value_III;


import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;


/**
 * This class contains static convenience methods for working
 * with values. This methods do not use the {@link Value}
 * interface, because there are many classes in the outside
 * world that also use these patterns, that do not implement
 * these proprietary interfaces.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public abstract class ValueHelpers {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/



  /*<construction>*/
  //------------------------------------------------------------------

  private ValueHelpers() {
    // NOP
  }

  /*</construction>*/


  /**
   * Is one value {@link Object#equals(java.lang.Object) equal}
   * to another, if <code>null</code> is also allowed as value
   * for a property.
   *
   * @return    (one == null)
   *                ? (other == null)
   *                : one.equals(other);
   *
   * @idea (jand) move to ppw-util or toryt
   */
  public static boolean eqn(final Object one, final Object other) {
    return (one == null) ? (other == null) : one.equals(other);
  }


  /**
   * Assert that <code>p</code> is needed at least to make
   * <code>result</code> <code>true</code>.
   *
   * @param p
   *        A boolean expression that has to be <code>true</code> at least
   *        to make <code>result</code> <code>true</code>.
   * @param result
   *        The result to make <code>true</code>.
   * @return ! p ? ! result;
   */
  public static boolean assertAtLeast(final boolean p, final boolean result) {
    return p || (!result);
  }

  /**
   * Register packages from this library as
   * path for {@link PropertyEditor PropertyEditors} with
   * the {@link PropertyEditorManager}.
   */
  public static void registerPropertyEditors() {
    String[] oldPath = PropertyEditorManager.getEditorSearchPath();
    String[] newPath = new String[oldPath.length + 1];
    System.arraycopy(oldPath, 0, newPath, 0, oldPath.length);
    String currentPackageName = ValueHelpers.class.getPackage().getName();
    newPath[newPath.length - 1] = currentPackageName;
    PropertyEditorManager.setEditorSearchPath(newPath);
  }

}
