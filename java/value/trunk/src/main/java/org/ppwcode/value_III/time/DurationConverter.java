/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ppwcode.value_III.time;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.directwebremoting.convert.BaseV20Converter;
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
import org.directwebremoting.util.Messages;
import org.directwebremoting.util.Logger;

import org.ppwcode.value_III.time.Duration;

/**
 *
 * @author rvdginste
 */
public class DurationConverter extends BaseV20Converter implements NamedConverter {

  private static final Logger log = Logger.getLogger(DurationConverter.class);

  public class DurationProperty implements Property {

    public String getName() {
      return "milliseconds";
    }

    public Class getPropertyType() {
      return long.class;
    }

    public Object getValue(Object bean) throws MarshallException {
      try {
        return ((Duration) bean).asMillisecond();
      } catch (Exception exc) {
        throw new MarshallException(bean.getClass(), exc);
      }
    }

    public void setValue(Object bean, Object value) throws MarshallException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public Method getSetter() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }


  public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException {
    String value = iv.getValue();

    // If the text is null then the whole bean is null
    if (value.trim().equals(ProtocolConstants.INBOUND_NULL)) {
      return null;
    }
    if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START)) {
      throw new MarshallException(paramType, Messages.getString("DurationConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
    }
    if (!value.endsWith(ProtocolConstants.INBOUND_MAP_END)) {
      throw new MarshallException(paramType, Messages.getString("DurationConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
    }

    value = value.substring(1, value.length() - 1);

    try {
      Map<String, Object> args = new HashMap<String, Object>();
      Map<String, Property> properties = null;

      if (instanceType != null) {
        properties = getPropertyMapFromClass(instanceType, false, true);
      } else {
        properties = getPropertyMapFromClass(paramType, false, true);
      }

      // only need one property right now
      Map<String, String> tokens = extractInboundTokens(paramType, value);
      for (Iterator<Map.Entry<String, String>> it = tokens.entrySet().iterator(); it.hasNext();) {
        Map.Entry<String, String> entry = it.next();
        String key = entry.getKey();
        String val = entry.getValue();

        Property property = properties.get(key);
        if (property == null) {
          log.warn("Missing property to match javascript property: " + key + ".");
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

      Object obj = null;
      if (instanceType != null) {
        // obj = InstanceHelpers.newInstance(instanceType, args.get("milliseconds"));
        System.out.println("Milliseconds is : "+args.get("milliseconds"));
        obj = new Duration(((Long) args.get("milliseconds")));
        inctx.addConverted(iv, instanceType, obj);
      } else {
        // obj = InstanceHelpers.newInstance(paramType, args.get("milliseconds"));
        System.out.println("Milliseconds is : "+args.get("milliseconds"));
        obj = new Duration(((Long) args.get("milliseconds")));
        inctx.addConverted(iv, paramType, obj);
      }
      return obj;
    } catch (MarshallException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new MarshallException(paramType, ex);
    }
  }

  public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException {
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

  public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException {
    if (example instanceof Duration) {
      return getPropertyMapFromClass(example.getClass(), readRequired, writeRequired);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public Map<String, Property> getPropertyMapFromClass(Class type, boolean readRequired, boolean writeRequired) throws MarshallException {
    Map<String, Property> properties = new HashMap<String, Property>();
    Property prop = new DurationProperty();
    properties.put("milliseconds", prop);
    return properties;
  }

  public String getJavascript()
  {
      return javascript;
  }

  public void setJavascript(String javascript)
  {
      this.javascript = javascript;
  }

  protected String javascript;


  public Class getInstanceType()
  {
      return instanceType;
  }

  public void setInstanceType(Class instanceType)
  {
      this.instanceType = instanceType;
  }

  protected Class instanceType = null;



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
              throw new MarshallException(paramType, Messages.getString("DurationConverter.MissingSeparator", ProtocolConstants.INBOUND_MAP_ENTRY, token));
          }

          String key = token.substring(0, colonpos).trim();
          String val = token.substring(colonpos + 1).trim();
          tokens.put(key, val);
      }

      return tokens;
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
