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


import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.vernacular.value_III.ValueHelpers;


/**
 * This listener is called on startup and shut down of a web application, to register
 * this library in the {@link PropertyEditorManager} lookup path.
 * This listener is to be registered with the web application in the
 * <kbd>web.xml</kbd> file.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class RegisterPropertyEditors implements ServletContextListener {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /* </section> */

  private static final Log LOG =
      LogFactory.getLog(RegisterPropertyEditors.class);


  /**
   * Register this library in the {@link PropertyEditorManager}
   * {@link PropertyEditor} lookup path.
   */
  public void contextInitialized(final ServletContextEvent event) {
    LOG.debug("registering ppw-value library in PropertyEditorManager "
              + "PropertyEditor lookup path");
    ValueHelpers.registerPropertyEditors();
    if (LOG.isDebugEnabled()) {
      LOG.debug("PropertyEditorManager PropertyEditor lookup path: "
                + PropertyEditorManager.getEditorSearchPath());
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
