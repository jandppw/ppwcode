/*<license>
Copyright 2004 - $Date: 2008-08-01 00:01:00 +0200 (vr, 01 aug 2008) $ by PeopleWare n.v.

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

package org.ppwcode.util.resourcebundle;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.ResourceBundle;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * NOT GOOD VERSION; LATEST WORK IS IN SEMANTICS
 */
@Copyright("2004 - $Date: 2008-08-01 00:01:00 +0200 (vr, 01 aug 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1994 $",
         date     = "$Date: 2008-08-01 00:01:00 +0200 (vr, 01 aug 2008) $")
public interface ResourceBundleLoadStrategy {

  /**
   * Try to load the resource bundle with name <code>basename</code>, according to the strategy implemented in this
   * type. If no matching resource bundle can be found with this strategy, this is considered a programming error.
   *
   * @param basename
   *        The basename of the resource bundle that should be loaded.
   */
  @MethodContract(
    pre  = @Expression("resourceBundleExists(basename)"),
    post = {
      @Expression("_basename == null ? result == null"),
      @Expression("_basename == EMPTY ? result == null")
    }
  )
  ResourceBundle loadResourceBundle(final String basename);

}
