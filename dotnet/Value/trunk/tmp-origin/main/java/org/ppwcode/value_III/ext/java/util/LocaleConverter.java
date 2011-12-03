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

package org.ppwcode.value_III.ext.java.util;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

import java.beans.PropertyEditor;
import java.util.Locale;

import org.directwebremoting.convert.BaseV20Converter;
import org.directwebremoting.dwrp.SimpleOutboundVariable;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>For {@link Locale}, we cannot use the standard {@code LocaleConverter}
 *   from the value vernacular, because there is a difference in programmatic representation.
 *   The seperator between the parts of the programmatic representation in Java is the
 *   underscore. In Dojo however, the hyphen is expected.
 *   See <a href="http://dojotoolkit.org/book/dojo-book-0-9/part-3-programmatic-dijit-and-dojo/i18n/globalization-guidelines/locale-and-resou">the Dojo book</a>.</p>
 * <p>This converter also uses the {@link LocaleEditor}, but replaces undercores with
 *   hyphens and vice-versa.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class LocaleConverter extends BaseV20Converter {

  @SuppressWarnings("unchecked")
  public Object convertInbound(Class paramType, InboundVariable data, InboundContext inctx) throws MarshallException {
    preArgumentNotNull(paramType, "paramType");
    PropertyEditor pe = new LocaleEditor();
    if (pe == null) {
      throw new MarshallException(paramType, "no property editor found for this type");
    }
    String jsRepresentation = data.getValue();
    String javaRepresentation = jsRepresentation.replaceAll("-", "_");
    pe.setAsText(javaRepresentation);
    return pe.getValue();
  }

  public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException {
    if (data == null) {
      return null;
    }
    else {
      PropertyEditor pe = new LocaleEditor();
      pe.setValue(data);
      String javaRepresentation = pe.getAsText();
      String jsRepresentation = javaRepresentation.replaceAll("_", "-");
      OutboundVariable result = new SimpleOutboundVariable("\"" + jsRepresentation + "\"", outctx, true);
      return result;
    }
  }

}
