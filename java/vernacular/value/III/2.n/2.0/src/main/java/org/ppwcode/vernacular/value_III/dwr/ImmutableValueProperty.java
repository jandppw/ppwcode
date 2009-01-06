package org.ppwcode.vernacular.value_III.dwr;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;

public class ImmutableValueProperty implements Property {

  private PropertyDescriptor property = null;

  public ImmutableValueProperty(PropertyDescriptor property) {
    this.property = property;
  }

  public String getName() {
    return property.getName();
  }

  public Class getPropertyType() {
    return property.getPropertyType();
  }

  public Method getSetter() {
    return property.getWriteMethod();
  }

  public Object getValue(Object obj) throws MarshallException {
    try {
      Method getter = property.getReadMethod();
      return getter.invoke(obj);
    } catch (Exception exc) {
      throw new MarshallException(obj.getClass(), exc);
    }
  }

  public void setValue(Object obj, Object val) throws MarshallException {
    try {
        Method setter = getSetter();
        setter.invoke(obj, val);
    } catch (Exception exc) {
    throw new MarshallException(obj.getClass(), exc);
    }
  }
}
