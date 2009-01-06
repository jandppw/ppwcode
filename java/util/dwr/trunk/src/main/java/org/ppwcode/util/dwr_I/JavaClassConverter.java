/*<license>
Copyright 2004 - $Date: 2008-11-16 14:33:57 +0100 (Sun, 16 Nov 2008) $ by PeopleWare n.v..

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

package org.ppwcode.util.dwr_I;

import static org.ppwcode.util.reflect_I.TypeHelpers.type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

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
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;
import org.ppwcode.util.reflect_I.TypeHelpers;

public class JavaClassConverter implements NamedConverter {

	private static final Logger log = Logger.getLogger(JavaClassConverter.class);
	
	private ConverterManager _convertermanager = null;
	
	private String _javascript = null;

	
	public Object convertInbound(Class paramType, InboundVariable iv,	InboundContext inctx) throws MarshallException {

		// We only convert for Class objects, for anything else, we throw exceptions
		if (!paramType.isAssignableFrom(Class.class)) {
			throw new MarshallException(paramType, "JavaClassConverter can only convert Class objects");
		}

		String value = iv.getValue();

		// If the text is null then the whole bean is null
		if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
		{
			return null;
		}

		//we expect an object, so, surrounded by '{' and '}'
		if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START))
		{
			throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
		}

		if (!value.endsWith(ProtocolConstants.INBOUND_MAP_END))
		{
			throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
		}

		//tokenize the incoming key-value pairs
		value = value.substring(1, value.length() - 1);
		Map <String, String> tokens = tokenizeValue(paramType, value);
    
    Class<?> theclass = null;
    String theclassname = null;

    //we search only for the "classname" property, we ignore the rest
    boolean found = false;
    for (Iterator it = tokens.entrySet().iterator(); !found && it.hasNext();)
    {
        Map.Entry entry = (Map.Entry) it.next();
        String key = (String) entry.getKey();
        if (key.equals("classname")) {
        	String val = (String) entry.getValue();
        
        	String[] split = ParseUtil.splitInbound(val);
        	String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
        	String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];

        	InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);
        	theclassname = (String)_convertermanager.convertInbound(String.class, nested, inctx, inctx.getCurrentTypeHintContext());
        	found = true;
        }
    }

    //if found, try to instantiate, otherwise, throw exceptions
    if (found) {
    	try {
    		theclass = type(theclassname);
    	} catch (AssertionError e) {
    		throw new MarshallException(paramType, "JavaClassConvertor: Class not found: \"" + theclassname + "\"");
    	}
    } else {
    	throw new MarshallException(paramType, "JavaClassConvertor: Inbound Object had no \"classname\" property");
    }

    return theclass;
	}

	/**
	 * blatantly copied from BasicObjectConverter
	 */
	private Map<String, String> tokenizeValue (Class<?> paramType, String value) throws MarshallException {
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

  /**
   * untested
   */
	public OutboundVariable convertOutbound(Object data, OutboundContext outctx)	throws MarshallException {

		if (! data.getClass().isAssignableFrom(Class.class)) {
			throw new MarshallException(data.getClass(), "JavaClassConverter cannot marshall other objects than Class Objects");
		}

		Class<?> theclass = (Class<?>)data;

		//Collection of converted children
		Map<String, OutboundVariable> ovs = new TreeMap<String, OutboundVariable>();

		// We need to do this before collecting the children to save recursion
		ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx);
		outctx.put(data, ov);

		OutboundVariable theclassname = _convertermanager.convertOutbound(theclass.getCanonicalName(), outctx);
		ovs.put("classname", theclassname);

		ov.init(ovs, getJavascript());

		return ov;
	}

	public void setConverterManager(ConverterManager config) {
		this._convertermanager = config;
	}


	public String getJavascript()	{
		return _javascript;
	}


	public void setJavascript(String javascript) {
		this._javascript = javascript;
	}
	
	public Map<?, ?> getPropertyMapFromClass(Class type, boolean readRequired,	boolean writeRequired) throws MarshallException {
		if (!type.isAssignableFrom(Class.class)) {
			throw new MarshallException(type, "type is not an instance of java.lang.Class");
		} else {
			Map<String, JavaClassProperty> props = new HashMap<String, JavaClassProperty>();
			props.put("classname", new JavaClassProperty("classname"));
			return props;
		}
	}

	public Map<?, ?> getPropertyMapFromObject(Object example, boolean readRequired,
			boolean writeRequired) throws MarshallException {
		return getPropertyMapFromClass(example.getClass(), readRequired, writeRequired);
	}

	public void setInstanceType(Class instanceType) {
		//NOP
		// is always Class.class
	}

	public Class<?> getInstanceType() {
		return Class.class;
	}

}
