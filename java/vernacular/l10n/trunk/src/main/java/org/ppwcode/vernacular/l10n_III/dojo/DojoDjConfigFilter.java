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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

import org.ppwcode.vernacular.l10n_III.web.HttpRequestLocaleFilter;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

/**
 * <p>This class is a servlet filter that will locate the "djConfig" variable used for
 *   the configuration of Dojo, and that will replace or add a locale option to it.</p>
 *
 * @author Ruben Vandeginste
 * @author PeopleWare n.v.
 */
@Copyright("2009 - $Date: 2009-01-23 17:25:55 +0100 (Fri, 23 Jan 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4029 $",
date = "$Date: 2009-01-23 17:25:55 +0100 (Fri, 23 Jan 2009) $")
public class DojoDjConfigFilter implements Filter {

  private static final Log LOG = LogFactory.getLog(DojoDjConfigFilter.class);
  
  private FilterConfig $filterConfig = null;


  //
  //  Inner classes used for wrapping the ServletResponse and manipulating the body afterwards.
  //

  private class StringServletOutputStream extends ServletOutputStream {

    private StringWriter output;

    public StringServletOutputStream(StringWriter writer) {
      output = writer;
    }

    @Override
    public void write(int b) throws IOException {
      output.write(b);
    }

  }

  private class StringHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private StringWriter output;

    public StringHttpServletResponseWrapper(HttpServletResponse response) {
      super(response);
      output = new StringWriter(65536);
    }

    @Override
    public PrintWriter getWriter() {
      return new PrintWriter(output);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
      return new StringServletOutputStream(output);
    }

    public StringBuffer getStringBuffer() {
      return output.getBuffer();
    }
  }


  //
  //  public methods
  //

  public void init(FilterConfig filterConfig) throws ServletException {
    $filterConfig = filterConfig;
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)
        && (((HttpServletRequest) request).getSession().getAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE) != null)
        && (((HttpServletRequest) request).getSession().getAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE) instanceof Locale)) {
      doDjConfigReplacementFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
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

  private void doDjConfigReplacementFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    // wrap the response before delegating to the rest of the chain
    StringHttpServletResponseWrapper wrapper = new StringHttpServletResponseWrapper(response);

    // execute rest of the chain
    chain.doFilter(request, wrapper);

    // find preferred locale in session scope
    Locale preferredLocale = (Locale) request.getSession().getAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE);

    // replace body
    wrapper.flushBuffer(); // really needed ?
    StringBuffer buffer = wrapper.getStringBuffer();
    PrintWriter out = response.getWriter();
    // only try to replace mime-type "text/html" bodies
    String body = "";
    if (wrapper.getContentType().equals("text/html")) {
      body = replaceDjConfig(buffer, preferredLocale);
    } else {
      body = buffer.toString();
    }
    out.write(body);
    out.close();
    response.setContentLength(body.length());
  }

  // find the djConfig string in the given buffer and configure it to use the given locale
  private String replaceDjConfig(StringBuffer buffer, Locale preferredLocale) {
    StringBuffer result = new StringBuffer(65536);

    // construct regexp
    String regexpFull = "^(.*<script)";                         // up to script tag
    regexpFull += "((\\s+type=\\s*(['\"])text/javascript\\4|";   // type
    regexpFull += "\\s+src=\\s*(['\"]).*?dojo.*?\\5|";    // src
    regexpFull += "\\s+djConfig=\\s*(['\"])(.*?)\\6){3})";         // djConfig
    regexpFull += "(\\s*>\\s*</script>.*)$";                    // closing script tag and afterwards
    LOG.debug("regexp: " + regexpFull);

    Pattern p = Pattern.compile(regexpFull, Pattern.DOTALL);
    Matcher m = p.matcher(buffer);
    // pattern group 1 - before, 2 - djConfig content, 3 - after
    if (m.matches()) {
      LOG.debug("djConfig is: "+m.group(2));
      result.append(m.group(1));
      result.append(replaceDjConfigLocale(m.group(2), preferredLocale));
      LOG.debug("after is: " + m.group(8));
      result.append(m.group(8));
      return result.toString();
    } else {
      LOG.debug("no match");
      return buffer.toString();
    }
  }

  private String replaceDjConfigLocale(String djConfig, Locale preferredLocale) {
    StringBuffer result = new StringBuffer(1024);
    String dojoLocale = DojoLocaleHelpers.localeToString(preferredLocale);

    // construct regexp
    String regexp = "^(.*\\s+djConfig=\\s*)((['\"])(.*?)\\3)(\\s*.*)$";
    LOG.debug("regexp djconfiglocale: " + regexp);

    Pattern p = Pattern.compile(regexp, Pattern.DOTALL);
    Matcher m = p.matcher(djConfig);

    // pattern group 1 - before djConfig, 2 - djConfig content, 3 - after djConfig
    if (m.matches()) {
      String before = m.group(1);
      String quote = m.group(3);
      String config = m.group(4);
      String after = m.group(5);

      String otherQuote = quote.equals("\"") ? "'" : "\"";

      LOG.debug("before " + before);
      LOG.debug("after  " + after);
      LOG.debug("cfg    " + config);
      LOG.debug("quote  " + quote);

      // construct before djConfig content
      result.append(before);
      result.append(quote);

      // filter config itself
      String regexpCfg = "^(.*" + otherQuote + "?locale" + otherQuote + "?\\s*:\\s*" + otherQuote + ")([a-z\\-]*)(" + otherQuote + ".*)$";
      Pattern pCfg = Pattern.compile(regexpCfg, Pattern.DOTALL);
      Matcher mCfg = pCfg.matcher(config);

      if (mCfg.matches()) {
        // found locale in djConfig
        String beforeLocale = mCfg.group(1);
        String locale = mCfg.group(2);
        String afterLocale = mCfg.group(3);

        LOG.debug("before locale: " + beforeLocale);
        LOG.debug("locale: " + locale);
        LOG.debug("after locale: " + afterLocale);

        result.append(beforeLocale);
        result.append(dojoLocale);
        result.append(afterLocale);
      } else {
        // no locale in djConfig: add it
        result.append(config);
        result.append(", locale:");
        result.append(otherQuote);
        result.append(dojoLocale);
        result.append(otherQuote);
      }

      // construct after djConfig content
      result.append(quote);
      result.append(after);
    } else {
      // no match on djConfig, just return the string then
      result.append(djConfig);
    }

    LOG.debug("new djConfig: " + result);
    return result.toString();
  }

}
