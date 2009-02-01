/*<license>
Copyright 2009 - $Date: 2009-01-23 17:25:55 +0100 (Fri, 23 Jan 2009) $ by PeopleWare n.v..

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

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.l10n_III.LocaleHelpers;
import org.ppwcode.vernacular.l10n_III.LocaleManager;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

/**
 * <p>This class is a servlet filter that will calculate the preferred locale
 * based on the locales supported by the application and the locales accepted
 * by the client.  This preferred locale will then be stored in an attribute
 * in session scope.</p>
 *
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 */
@Copyright("2009 - $Date: 2009-01-23 17:25:55 +0100 (Fri, 23 Jan 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4029 $",
         date     = "$Date: 2009-01-23 17:25:55 +0100 (Fri, 23 Jan 2009) $")
public class HttpRequestLocaleFilter implements Filter {

  private static final Log LOG = LogFactory.getLog(HttpRequestLocaleFilter.class);

  // attribute name used to store preferred locale in session scope
  public static final String ATTRIBUTE_PREFERRED_LOCALE = HttpRequestLocaleFilter.class.getName() + ".locale";

  private FilterConfig $filterConfig = null;

  public void init(FilterConfig filterConfig) throws ServletException {
    $filterConfig = filterConfig;
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      doRequestLocaleFilter((HttpServletRequest) request, response, chain);
    } else {
      chain.doFilter(request, response);
    }
  }

  public void destroy() {
    $filterConfig = null;
  }

  //
  //  private methods
  //

  private void doRequestLocaleFilter(HttpServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    HttpSession session = request.getSession();

    if (session.getAttribute(ATTRIBUTE_PREFERRED_LOCALE) == null) {
      Enumeration acceptedLocales = request.getLocales();
      List<Locale> supportedLocales = LocaleManager.getSupportedLocales();

      @SuppressWarnings("unchecked") // request.getLocales does not use generics
      Locale bestLocale = LocaleHelpers.findPreferredLocale(acceptedLocales, supportedLocales);

      if (LOG.isDebugEnabled()) {
        String info = "accepted:  ";
        Enumeration test = request.getLocales();
        while (test.hasMoreElements()) {
          info += test.nextElement() + " ";
        }
        LOG.debug(info);
        LOG.debug("supported: " + supportedLocales);
        LOG.debug("preferred: " + bestLocale);
      }

      session.setAttribute(ATTRIBUTE_PREFERRED_LOCALE, bestLocale);
    }
    chain.doFilter(request, response);
  }

}
