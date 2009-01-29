/*<license>
Copyright 2009 - $Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $ by PeopleWare n.v.

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

package org.ppwcode.vernacular.l10n_III.web;

import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

import org.ppwcode.vernacular.l10n_III.LocaleHelpers;
import org.ppwcode.vernacular.l10n_III.LocaleManager;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

/**
 * This listener is called on startup of a web application to register the locales that are supported
 * by the web application.
 *
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 *
 * @todo this class is a simple listener that registers the locales given in a context parameter
 *     as supported with the LocaleManager.  In the future, the idea is to provide functionality
 *     in either LocaleManager, or an extra class to fetch the supported locales from both remote
 *     session beans, and local configuration files.  We also need a mechanism to update the info
 *     on supported locales in LocaleManager (when e.g. a remote ejb supports a new locale).  For
 *     this, we probably need a mechanism to periodically refresh the information in LocaleManager,
 *     or possibly trigger a refresh with a servlet filter on each request.
 */
@Copyright("2009 - $Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4044 $",
         date     = "$Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $")
public class RegisterSupportedLocalesSimpleListener implements ServletContextListener {

  public final static String SUPPORTED_LOCALES_ATTRIBUTE_NAME = RegisterSupportedLocalesSimpleListener.class.getName() + ".supportedLocales";

  public void contextInitialized(ServletContextEvent sce) {
    String supportedLocalesAttribute = sce.getServletContext().getInitParameter(SUPPORTED_LOCALES_ATTRIBUTE_NAME);
    if (supportedLocalesAttribute != null) {
      String[] locales = supportedLocalesAttribute.split(",");
      List<Locale> supportedLocales = LocaleHelpers.constructListOfLocales(locales);
      LocaleManager.registerSupportedLocales(RegisterSupportedLocalesSimpleListener.class.getName(), supportedLocales);
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {
    // NOP
  }

}
