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

package org.ppwcode.vernacular.l10n_III.resourcebundle;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.ResourceBundle;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * Exception thrown if a applicable {@link ResourceBundle} cannot be found.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("message == null"))
public class ResourceBundleNotFoundException extends ResourceBundleException {

  @MethodContract(post = {
    @Expression("basename == _basename"),
    @Expression("cause == null")
  })
  public ResourceBundleNotFoundException(String basename) {
    this(basename, null);
  }

  @MethodContract(post = {
    @Expression("basename == _basename"),
    @Expression("cause == _cause")
  })
  public ResourceBundleNotFoundException(String basename, Throwable cause) {
    super(cause);
    $basename = basename;
  }


  /* <property name="locale"> */
  //------------------------------------------------------------------

  @Basic
  public final String getBasename() {
    return $basename;
  }

  private String $basename;

  /* </property> */

}

