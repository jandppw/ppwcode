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

package org.ppwcode.vernacular.l10n_III.dojo;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.reflect_I.TypeHelpers;
import org.ppwcode.vernacular.l10n_III.resourcebundle.DefaultResourceBundleLoadStrategy;
import org.ppwcode.vernacular.l10n_III.I18nLabelHelpers;

/**
 * Servlet that emulates Dojo Localization bundles
 *
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 */
@Copyright("2009 - $Date: 2009-01-23 17:25:55 +0100 (Fri, 23 Jan 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4029 $",
         date     = "$Date: 2009-01-23 17:25:55 +0100 (Fri, 23 Jan 2009) $")
public class DojoI18nServlet extends HttpServlet {

  private Locale defaultLocale = null;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try {
      defaultLocale = Locale.getDefault();
      String loc = config.getInitParameter("defaultLocale");
      if (loc != null) {
        defaultLocale = constructLocale(loc);
      }
    } catch (Throwable e) { 
      e.printStackTrace();
    }
  }

  public void doService(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    try {

      /*
      System.out.println("Preferred locale:" + request.getLocale());
      System.out.println("Accepted locales:");
      
      Enumeration locs = request.getLocales();
      while (locs.hasMoreElements()) {
        System.out.println("   - "+ locs.nextElement());
      }
       */

      response.setContentType("text/javascript");
      PrintWriter out = response.getWriter();

      // request path that we need
      String i18nUrl = request.getPathInfo();
      // split path into package name, class name and localization
      Pattern p = Pattern.compile("^/(.*)/nls/(([^/]+)/)?([^/]+)\\.js$");
      Matcher m = p.matcher(i18nUrl);

      // initialize parts
      String fullName = null;
      String pkgName = null;
      String locName = null;
      String clsName = null;

      // find the needed parts in the url
      if (m.find()) {
        fullName = m.group(0);
        pkgName = m.group(1);
        pkgName = pkgName.replace('/', '.');
        locName = m.group(3);
        clsName = m.group(4);
      }

      // generate output
      if (fullName != null) {
        Locale loc = constructLocale(locName);
        HashMap<String, String> translations = getTranslations(pkgName+"."+clsName, loc);
        String localizationBundle = buildDojoLocalizationBundle(translations);
        out.println(localizationBundle);
      } else {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

    } catch (Throwable e) {
      e.printStackTrace();
    }

  }


  private Locale constructLocale(String str) {
    if (str == null) {
      return defaultLocale;
    }
    // 0 - language
    // 1 - country
    // 2 - variant
    String[] parts = str.split("-");
    if (parts.length == 1) {
      parts = str.split("_");
    }
    switch (parts.length) {
      case 1:
        return new Locale(parts[0]);
      case 2:
        return new Locale(parts[0], parts[1].toUpperCase());
      default:
        return new Locale(parts[0], parts[1].toUpperCase(), parts[2]);
    }
  }


  private HashMap<String, String> getTranslations(String fqcn, Locale loc) {
    HashMap<String, String> translations = new HashMap<String, String>();
    System.out.println("Request started");

    Class type = TypeHelpers.type(fqcn);

    DefaultResourceBundleLoadStrategy strategy = new DefaultResourceBundleLoadStrategy();
    strategy.setLocale(loc);

    String typeNameSingle = I18nLabelHelpers.i18nTypeLabel(type, false, strategy);
    String typeNamePlural = I18nLabelHelpers.i18nTypeLabel(type, true, strategy);

    if (typeNameSingle != null)
      translations.put(I18nLabelHelpers.I18N_TYPE_LABEL_KEY, typeNameSingle);
    if (typeNamePlural != null)
      translations.put(I18nLabelHelpers.I18N_PLURAL_TYPE_LABEL_KEY, typeNamePlural);

    System.out.println("Entity name: " + typeNameSingle);
    System.out.println("Entity name plural: " + typeNamePlural);

    PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(type);
    for (PropertyDescriptor prop : props) {
      String propLabelLong = I18nLabelHelpers.i18nPropertyLabel(prop.getName(), type, false, strategy);
      String propLabelShort = I18nLabelHelpers.i18nPropertyLabel(prop.getName(), type, true, strategy);

      if (propLabelLong != null)
        translations.put(I18nLabelHelpers.I18N_PROPERTY_LABEL_KEY_PREFIX + prop.getName(), propLabelLong);
      if (propLabelShort != null)
        translations.put(I18nLabelHelpers.I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX + prop.getName(), propLabelShort);

      System.out.println("Property: " + prop.getName());
      System.out.println("Property name: " + propLabelLong);
      System.out.println("Property name short: " + propLabelShort);
    }
    System.out.println("Request finished");
    return translations;
  }

  private String buildDojoLocalizationBundle(HashMap<String, String> translations) {
    StringBuilder builder = new StringBuilder(1024);
    builder.append("({ \n");
    for (String key : translations.keySet()) {
      builder.append("  ");
      builder.append(key.replace('.', '_'));
      builder.append(": \"");
      builder.append(translations.get(key));
      builder.append("\",");
    }
    builder.append("origin: \"DojoI18nServlet\"");
    builder.append("}) \n");
    return builder.toString();
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doService(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doService(request, response);
  }
}
