/*<license>
Copyright 2009 - $Date: 2009-02-12 15:55:44 +0100 (Thu, 12 Feb 2009) $ by PeopleWare n.v..

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

package org.ppwcode.vernacular.l10n_III.dwr;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.convert.ExceptionConverter;
import org.directwebremoting.dwrp.ObjectOutboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.ppwcode.vernacular.l10n_III.LocalizedException;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.l10n_III.I18nExceptionHelpers;
import org.ppwcode.vernacular.l10n_III.web.HttpRequestLocaleFilter;


/**
 * DWR converter for {@link LocalizedException}.
 * The "message" property of an exception is used as a key to find a localized error message, while
 * transforming the Java exception to a Javascript object.
 * The locale used for the localization, is the locale stored in the session scope. 
 *
 * @author    Ruben Vandeginste
 * @author    PeopleWare n.v.
 */
@Copyright("2009 - $Date: 2009-02-12 15:55:44 +0100 (Thu, 12 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4107 $",
date = "$Date: 2009-02-12 15:55:44 +0100 (Thu, 12 Feb 2009) $")
public class LocalizedExceptionConverter extends ExceptionConverter {

  // convertOutBound implementation originally taken from BasicObjectConverter
  @Override
  public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException {
    // Where we collect out converted children
    Map ovs = new TreeMap();

    // We need to do this before collecting the children to save recursion
    ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx);
    outctx.put(data, ov);

    try {
      Map properties = getPropertyMapFromObject(data, true, false);
      for (Iterator it = properties.entrySet().iterator(); it.hasNext();) {
        Map.Entry entry = (Map.Entry) it.next();
        String name = (String) entry.getKey();
        Property property = (Property) entry.getValue();

        Object value = null;
        if (name.equals("message") || name.equals("localizedMessage")) {
          Locale locale = (Locale) WebContextFactory.get().getSession().
                  getAttribute(HttpRequestLocaleFilter.ATTRIBUTE_PREFERRED_LOCALE);
          if (data instanceof LocalizedException) {
            value = I18nExceptionHelpers.i18nExceptionMessage((LocalizedException)data, locale);
            if (value == null) value = "NULL";
          } else {
            throw new MarshallException(data.getClass(), "Not a LocalizedException");
          }
        } else {
          value = property.getValue(data);
        }
        OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

        ovs.put(name, nested);
      }
    } catch (MarshallException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new MarshallException(data.getClass(), ex);
    }

    ov.init(ovs, getJavascript());

    return ov;
  }
}
