/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v.

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
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Strategy pattern to load i18n resource bundles.</p>
 * <p>The strategy to find a resource bundle in an i18n context is different in
 *   different deployments environments. E.g., the strategy is different for
 *   general Swing applications, JSTL <code>fmt</code> tags, Struts, JSF, etcetera.</p>
 * <p>Classes that need to access resources based on a {@link java.util.Locale locale}, can use
 *   an instance of this strategy at runtime to have it load a resource bundle for them.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface ResourceBundleLoadStrategy {

  /**
   * Try to load the resource bundle with name <code>basename</code>, according to the strategy
   * implemented in this type. If no matching resource bundle can be found with this strategy,
   * an exception is thrown.
   *
   * @param basename
   *        The basename of the resource bundle that should be loaded.
   */
  @MethodContract(
    post = @Expression("result != null"),
    exc = @Throw(type = ResourceBundleNotFoundException.class,
                 cond = @Expression(value = "_basename == null || _basename == EMPTY || true",
                                    description = "No resource bundle with basename was found with this strategy"))
  )
  ResourceBundle loadResourceBundle(final String basename) throws ResourceBundleNotFoundException;

}
