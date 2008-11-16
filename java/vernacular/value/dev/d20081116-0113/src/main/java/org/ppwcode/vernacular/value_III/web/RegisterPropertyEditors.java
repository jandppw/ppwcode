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

package org.ppwcode.vernacular.value_III.web;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.value_III.ValueHelpers.empty;
import static org.ppwcode.vernacular.value_III.ValueHelpers.registerPropertyEditorPackage;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * This listener is called on startup of a web application, to register packages in the
 * {@link PropertyEditorManager#getEditorSearchPath() property editor search path}.
 * This listener is to be registered with the web application in the
 * <kbd>web.xml</kbd> file. The package names to be added to the
 * {@link PropertyEditorManager#getEditorSearchPath() property editor search path}
 * are to be listed in a comma separated list (whitespace allowed) in an init parameter
 * in the <kbd>web.xml</kbd> file with name {@link #INIT_PARAMETER_NAME}.
 * Operation is logged at debug level.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @todo This class is currently untested formally. A previous version has been used extensively
 *       in production without problems.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class RegisterPropertyEditors implements ServletContextListener {

  private static final Log LOG = LogFactory.getLog(RegisterPropertyEditors.class);

  /**
   * <p>The split pattern for the list of package names in <kbd>web.xml</kbd>.</p>
   *
   * <p><code>SPLIT_PATTERN == &quot;<strong>{@value}</strong>&quot;</code>
   */
  public final static String SPLIT_PATTERN = ",";

  /**
   * <p>A dot.</p>
   *
   * <p><code>DOT == &quot;<strong>{@value}</strong>&quot;</code>
   */
  public final static String DOT = ".";

  /**
   * <p>The postfix to this class's FQCN to complete the {@link #INIT_PARAMETER_NAME}.
   *
   * <p><code>INIT_PARAMETER_NAME_POSTFIX == &quot;<strong>{@value}</strong>&quot;</code>
   */
  public final static String INIT_PARAMETER_NAME_POSTFIX = "PACKAGE_NAMES";

  /**
   * <p>The name of the init parameter in the <kbd>web.xml</kbd> file that holds the
   *   comma-separated list of package names to be added to the
   *   {@link PropertyEditorManager#getEditorSearchPath() property editor search path}.</p>
   *
   * <p><code>INIT_PARAMETER_NAME == &quot;<strong>{@value}</strong>&quot;</code>
   */
  public final static String INIT_PARAMETER_NAME =
      RegisterPropertyEditors.class.getName() + DOT + INIT_PARAMETER_NAME_POSTFIX;

  /**
   * Register this library in the {@link PropertyEditorManager}
   * {@link PropertyEditor} lookup path.
   */
  public void contextInitialized(final ServletContextEvent event) {
    assert preArgumentNotNull(event, "event");
    assert pre(event.getServletContext() != null, "event.getServletContext() != null");
    String initParameterValue = event.getServletContext().getInitParameter(INIT_PARAMETER_NAME);
    if (empty(initParameterValue) && LOG.isDebugEnabled()) {
      LOG.debug("no init parameter \"" + INIT_PARAMETER_NAME +
                "\" found; no package names added to PropertyEditorManager property editor search path");
    }
    else {
      LOG.debug("found init parameter \"" + INIT_PARAMETER_NAME + "\": " + initParameterValue);
      String[] split = initParameterValue.split(SPLIT_PATTERN);
      for (int i = 0; i < split.length; i++) {
        String pn = split[i];
        pn = pn.trim();
        if (! empty(pn)) {
          LOG.debug("adding package name \"" + pn + "\" to PropertyEditorManager property editor search path");
          registerPropertyEditorPackage(pn);
        }
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("PropertyEditorManager property editor search path: " + PropertyEditorManager.getEditorSearchPath());
      }
    }
  }


  /**
   * Notification that the servlet context is about to be shut down.
   * No operation.
   *
   * @post  true;
   */
  public void contextDestroyed(final ServletContextEvent event) {
    // NOP
  }


}
