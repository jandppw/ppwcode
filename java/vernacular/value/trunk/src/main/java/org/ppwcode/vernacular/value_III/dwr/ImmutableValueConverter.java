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


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.directwebremoting.convert.BaseV20Converter;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.dwrp.ObjectOutboundVariable;
import org.directwebremoting.dwrp.ParseUtil;
import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.reflect_I.InstanceHelpers;

@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class ImmutableValueConverter  extends BaseV20Converter implements NamedConverter {

  private static final Logger log = Logger.getLogger(ImmutableValueConverter.class);

  /* (non-Javadoc)
   * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
   */
  public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
  {
      String value = iv.getValue();

      // If the text is null then the whole bean is null
      if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
      {
          return null;
      }

      if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START))
      {
          throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
      }

      if (!value.endsWith(ProtocolConstants.INBOUND_MAP_END))
      {
          throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
      }

      value = value.substring(1, value.length() - 1);

      // first mark the current object as converted by adding it in the working map -> we need a reference :(
      // next convert the given properties in the given order
      // finally call the constructor with the properties in the given order as parameters
      try
      {
          Object obj = null;
          Map<String, Object> args = new HashMap<String, Object>();

          // we cannot mark the current object as converted yet by adding it in the working map:
          // the reason is that we cannot use a default constructor
          // we will add the object to the working map, after we finished converting it

          // since we are working depth-first, this simply means that we cannot handle circular
          // references, which should not be a problem for the aimed usage

          Map<String, Property> properties = null;
          if (instanceType != null) {
            properties = getPropertyMapFromClass(instanceType, false, true);
          } else {
            properties = getPropertyMapFromClass(paramType, false, true);
          }

          // Loop through the properties passed in
          Map<String, String> tokens = extractInboundTokens(paramType, value);
          for (Iterator<Map.Entry<String, String>> it = tokens.entrySet().iterator(); it.hasNext();)
          {
              Map.Entry<String, String> entry = it.next();
              String key = entry.getKey();
              String val = entry.getValue();

              Property property = properties.get(key);
              if (property == null)
              {
                  log.warn("Missing property to match javascript property: " + key + ".");

                  StringBuffer all = new StringBuffer();
                  for (Iterator<String> pit = properties.keySet().iterator(); pit.hasNext();)
                  {
                      all.append(pit.next());
                      if (pit.hasNext())
                      {
                          all.append(',');
                      }
                  }
                  log.warn("Fields exist for (" + all + ").");
                  continue;
              }

              Class propType = property.getPropertyType();

              String[] split = ParseUtil.splitInbound(val);
              String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
              String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];

              InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);
              TypeHintContext incc = createTypeHintContext(inctx, property);

              Object output = converterManager.convertInbound(propType, nested, inctx, incc);

              // adding in argument map
              args.put(key, output);
          }

          // build argument list for constructor
          Object[] arguments = new Object[fields.size()];
          int i = 0;
          for (String arg : fields) {
            arguments[i] = args.get(arg);
            i++;
          }

          if (instanceType != null) {
            obj = InstanceHelpers.robustNewInstance(instanceType, arguments);
            inctx.addConverted(iv, instanceType, obj);
          }
          else
          {
            obj = InstanceHelpers.robustNewInstance(paramType, arguments);
            inctx.addConverted(iv, paramType, obj);
          }
          return obj;
      }
      catch (MarshallException ex)
      {
          throw ex;
      }
      catch (Exception ex)
      {
          throw new MarshallException(paramType, ex);
      }
  }

  /**
   * {@link #convertInbound(Class, InboundVariable, InboundContext)} needs to
   * create a {@link TypeHintContext} for the {@link Property} it is
   * converting so that the type guessing system can do its work.
   * <p>The method of generating a {@link TypeHintContext} is different for
   * the {@link BeanConverter} and the {@link ObjectConverter}.
   * @param inctx The parent context
   * @param property The property being converted
   * @return The new TypeHintContext
   */
  protected TypeHintContext createTypeHintContext(InboundContext inctx, Property property) {
    return inctx.getCurrentTypeHintContext();
  }


  /* (non-Javadoc)
   * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
   */
  public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
  {
      // Where we collect out converted children
      Map<String, OutboundVariable> ovs = new TreeMap<String, OutboundVariable>();

      // We need to do this before collecing the children to save recurrsion
      ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx);
      outctx.put(data, ov);

      try
      {
          Map<String, Property> properties = getPropertyMapFromObject(data, true, false);
          for (Iterator<Map.Entry<String, Property>> it = properties.entrySet().iterator(); it.hasNext();)
          {
              Map.Entry<String, Property> entry =  it.next();
              String name =  entry.getKey();
              Property property =  entry.getValue();

              Object value = property.getValue(data);
              OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

              ovs.put(name, nested);
          }
      }
      catch (MarshallException ex)
      {
          throw ex;
      }
      catch (Exception ex)
      {
          throw new MarshallException(data.getClass(), ex);
      }

      ov.init(ovs, getJavascript());

      return ov;
  }




  public Map<String, Property> getPropertyMapFromClass(Class type, boolean readRequired,  boolean writeRequired) throws MarshallException {

    try {
      BeanInfo info = Introspector.getBeanInfo(type);
      PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

      Map<String, Property> properties = new HashMap<String, Property>();
      for (int i = 0; i < descriptors.length; i++) {
        PropertyDescriptor descriptor = descriptors[i];
        String name = descriptor.getName();

        // only store properties for names that are also in the fields parameter
        if (fields.contains(name)) {
          properties.put(name, new ImmutableValueProperty(descriptor));
        }
      }
      return properties;

    } catch (IntrospectionException exc) {
      throw new MarshallException(type, exc);
    }
  }

  public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException {
    return getPropertyMapFromClass(example.getClass(), readRequired, writeRequired);
  }


  /**
   * Set a list of properties included from conversion
   * @param includes The space or comma separated list of properties to exclude
   */
  public void setFields(String paramFields)
  {
      if ((paramFields == null) || (paramFields.equals("")))
      {
          throw new IllegalArgumentException("Fields should not be empty.");
      }

      fields = new ArrayList<String>();

      String toSplit = LocalUtil.replace(paramFields, ",", " ");
      StringTokenizer st = new StringTokenizer(toSplit);
      while (st.hasMoreTokens())
      {
          String field = st.nextToken();
          if (field.startsWith("get"))
          {
              log.warn("Fields are based on property names and not method names. '" + field + "' starts with 'get' so it looks like a method name and not a property name.");
          }

          fields.add(field);
      }
  }


  protected List<String> fields;



  /**
   * Loop over all the inputs and extract a Map of key:value pairs
   * @param paramType The type we are converting to
   * @param value The input string
   * @return A Map of the tokens in the string
   * @throws MarshallException If the marshalling fails
   */
  protected Map<String, String> extractInboundTokens(Class paramType, String value) throws MarshallException
  {
      Map<String, String> tokens = new HashMap<String, String>();
      StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_MAP_SEPARATOR);
      int size = st.countTokens();

      for (int i = 0; i < size; i++)
      {
          String token = st.nextToken();
          if (token.trim().length() == 0)
          {
              continue;
          }

          int colonpos = token.indexOf(ProtocolConstants.INBOUND_MAP_ENTRY);
          if (colonpos == -1)
          {
              throw new MarshallException(paramType, Messages.getString("BeanConverter.MissingSeparator", ProtocolConstants.INBOUND_MAP_ENTRY, token));
          }

          String key = token.substring(0, colonpos).trim();
          String val = token.substring(colonpos + 1).trim();
          tokens.put(key, val);
      }

      return tokens;
  }





  public String getJavascript()
  {
      return javascript;
  }

  public void setJavascript(String javascript)
  {
      this.javascript = javascript;
  }

  /**
   * The javascript class name for the converted objects
   */
  protected String javascript;


  public Class getInstanceType()
  {
      return instanceType;
  }

  public void setInstanceType(Class instanceType)
  {
      this.instanceType = instanceType;
  }

  /**
   * A type that allows us to fulfill an interface or subtype requirement
   */
  protected Class instanceType = null;


  public void setConverterManager(ConverterManager converterManager)
  {
      this.converterManager = converterManager;
  }

  public ConverterManager getConverterManager()
  {
      return converterManager;
  }

  /**
   * To forward marshalling requests
   */
  protected ConverterManager converterManager = null;

}
