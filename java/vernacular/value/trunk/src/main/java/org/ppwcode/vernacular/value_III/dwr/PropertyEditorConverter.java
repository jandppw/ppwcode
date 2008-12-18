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

package org.ppwcode.vernacular.value_III.dwr;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.directwebremoting.convert.BaseV20Converter;
import org.directwebremoting.dwrp.SimpleOutboundVariable;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.Value;


/**
 * <p>General DWR {@link Converter} for any {@link Value} type that has an associated
 *   {@link PropertyEditor}. Since {@link #convertInbound(Class, InboundVariable, InboundContext)}
 *   carries the target value type, and {@link #convertOutbound(Object, OutboundContext)}
 *   carries the object we need to convert, there is no need to specify the exact type for
 *   which we work. This converter type can be used <em>as is</em> for any value type
 *   that has a {@link PropertyEditor} defined in the {@link PropertyEditorManager},
 *   or that can be found via the appropriate naming scheme.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class PropertyEditorConverter extends BaseV20Converter {

  private final String ENCODING = "UTF-8";

//  /*<construction>*/
//  //------------------------------------------------------------------
//
//  /**
//   * An instance aimed to persist instances of a value type using {@code ve}.
//   */
//  @MethodContract(
//    pre  = @Expression("_ve != null"),
//    post = {
//      @Expression("valyeType == _valueType"),
//      @Expression("propertyEditor == _pe")
//    }
//  )
//  public PropertyEditorConverter(ValueEditor<? extends Value> ve) {
//    preArgumentNotNull(ve, "ve");
//    $editor = ve;
//    $valueType = ve.getValueType();
//  }
//
//  /*</construction>*/
//
//
//
//  /*<property name="value type">*/
//  //------------------------------------------------------------------
//
//  /**
//   * The class this user type now works.
//   */
//  @Basic(invars = @Expression("valueType"))
//  public Class<? extends Value> getValueType() {
//    return $valueType;
//  }
//
//  private Class<? extends Value> $valueType;
//
//  /*</property>*/
//
//
//
//  /*<property name="propertyEditor">*/
//  //------------------------------------------------------------------
//
//  /**
//   * An instance of the value type property editor for the value
//   * type this UserType works for.
//   */
//  @Basic(invars = @Expression("propertyEditor != null"))
//  public final PropertyEditor getPropertyEditor() {
//    return $editor;
//  }
//
//  private PropertyEditor $editor;
//
//  /*</property>*/



  /**
   * Attempt to coerce the data from a string to an Object.
   * If anything goes wrong with inbound conversion then we generally throw
   * an exception because we are converting data from the untrusted internet
   * so we take the assumption that anything wrong is someone hacking.
   * @param paramType The type to convert to
   * @param data The data to convert
   * @param inctx The map of data that we are working on
   * @return The convered data, or null if the conversion was not possible
   * @throws MarshallException If the conversion failed for some reason
   */
  @SuppressWarnings("unchecked")
  public Object convertInbound(Class paramType, InboundVariable data, InboundContext inctx) throws MarshallException {
    preArgumentNotNull(paramType, "paramType");
    PropertyEditor pe = PropertyEditorManager.findEditor(paramType);
    if (pe == null) {
      throw new MarshallException(paramType, "no property editor found for this type");
    }
    //When DWR converts the javascript object to a java object, the reference string will contain
    //URL Encoded values. When a TimeZone is parsed, the encoded value (eg. Europe%2FBrussels) will be
    //used. This String will not be found in the converter map of TimeZone. In order to parse the
    //object in a correct way, we first have to decode the String.
    try {
      pe.setAsText(URLDecoder.decode(data.getValue(), ENCODING));
    }
    catch (UnsupportedEncodingException e) {
      throw new MarshallException(paramType, e);
    }
    return pe.getValue();
  }

  public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException {
    if (data == null) {
      return null;
    }
    else {
      PropertyEditor pe = PropertyEditorManager.findEditor(data.getClass());
      pe.setValue(data);
      OutboundVariable result = new SimpleOutboundVariable("\"" + pe.getAsText() + "\"", outctx, true);
      return result;
    }
  }

}
