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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.preArgumentNotEmpty;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * This class contains static convenience methods for working with values. This methods do not use the
 * {@link Value} interface, because there are many classes in the outside world that also use these
 * patterns, that do not implement these proprietary interfaces.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class ValueHelpers {

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
   * @idea (jand) move to ppwcode-util?
   */
  @MethodContract(post = @Expression("_one == null ? _other == null : _one.equals(_other)"))
  public static boolean eqn(final Object one, final Object other) {
    return (one == null) ? (other == null) : one.equals(other);
  }

  /**
   * The empty String.
   */
  public final static String EMPTY = "";

  /**
   *
   * @idea (jand) move to ppwcode-util?
   */
  @MethodContract(post = @Expression("s == null || s == EMPTY"))
  public static boolean empty(String s) {
    return s == null || EMPTY.equals(s);
  }

// remove; possibly interesting in Toryt?
//  /**
//   * Assert that <code>p</code> is needed at least to make
//   * <code>result</code> <code>true</code>.
//   *
//   * @param p
//   *        A boolean expression that has to be <code>true</code> at least
//   *        to make <code>result</code> <code>true</code>.
//   * @param result
//   *        The result to make <code>true</code>.
//   * @return ! p ? ! result;
//   */
//  public static boolean assertAtLeast(final boolean p, final boolean result) {
//    return p || (!result);
//  }

  /**
   * {@link PropertyEditor PropertyEditors} are found by the {@link PropertyEditorManager}
   * using several strategies, one of them is looking for a class with a matching name in
   * any of a list of packages. This method adds a package name to that list.
   * There is no test whether {@code packageName} is actually a valid package name.
   */
  @MethodContract(
    pre  = {
      @Expression("_packageName != null"),
      @Expression("_packageName != EMPTY")
    },
    post = {
      @Expression("PropertyEditorManager.getEditorSearchPath().length == PropertyEditorManager'getEditorSearchPath().length + 1"),
      @Expression("for (int i : 0 .. PropertyEditorManager'getEditorSearchPath() - 1) {PropertyEditorManager.getEditorSearchPath()[i] == PropertyEditorManager'getEditorSearchPath()[i]"),
      @Expression("PropertyEditorManager.getEditorSearchPath()[PropertyEditorManager'getEditorSearchPath()] == packageName")
    }
  )
  public static void registerPropertyEditorPackage(String packageName) {
    assert preArgumentNotEmpty(packageName, "packageName");
    String[] oldPath = PropertyEditorManager.getEditorSearchPath();
    String[] newPath = new String[oldPath.length + 1];
    System.arraycopy(oldPath, 0, newPath, 0, oldPath.length);
    newPath[newPath.length - 1] = packageName;
    PropertyEditorManager.setEditorSearchPath(newPath);
  }

// MUDO move to ppwcode value as call to method above
//  /**
//   * Register packages from this library as
//   * path for {@link PropertyEditor PropertyEditors} with
//   * the {@link PropertyEditorManager}.
//   */
//  public static void registerPropertyEditors() {
//    String[] oldPath = PropertyEditorManager.getEditorSearchPath();
//    String[] newPath = new String[oldPath.length + 1];
//    System.arraycopy(oldPath, 0, newPath, 0, oldPath.length);
//    String currentPackageName = ValueHelpers.class.getPackage().getName();
//    newPath[newPath.length - 1] = currentPackageName;
//    PropertyEditorManager.setEditorSearchPath(newPath);
//  }

}
