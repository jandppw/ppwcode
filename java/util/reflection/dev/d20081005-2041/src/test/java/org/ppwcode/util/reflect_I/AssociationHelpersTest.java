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

package org.ppwcode.util.reflect_I;


import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;
import static org.ppwcode.util.reflect_I.AssociationHelpers.associatedBeans;
import static org.ppwcode.util.reflect_I.AssociationHelpers.directAssociatedBeans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.StubAssociatedBean;
import org.ppwcode.util.reflect_I.teststubs.StubAssociatedBeanA;
import org.ppwcode.util.reflect_I.teststubs.StubAssociatedBeanB;


public class AssociationHelpersTest {

  private Set<? extends StubAssociatedBean> $objects;

  @Before
  public void setUp() throws Exception {
    Set<? extends StubAssociatedBean> objects = someRousseauBeans();
    $objects = objects;
  }

  public static Set<StubAssociatedBean> someRousseauBeans() {
    Set<StubAssociatedBean> rousseauBeans = new HashSet<StubAssociatedBean>();

    StubAssociatedBean srb = new StubAssociatedBean();
    rousseauBeans.add(srb);

    StubAssociatedBean srb1 = new StubAssociatedBean();
    srb = new StubAssociatedBean();
    srb.setProperty2(srb1);
    StubAssociatedBeanA srbA = new StubAssociatedBeanA();
    srb.setProperty4(srbA);
    StubAssociatedBeanB srbB = new StubAssociatedBeanB();
    srb.setProperty5(srbB);
    rousseauBeans.add(srb);

    srb1 = new StubAssociatedBean();
    srb = new StubAssociatedBean();
    srb.setProperty2(srb1);
    srbA = new StubAssociatedBeanA();
    srbA.setProperty2(srb1);
    srbA.setProperty4(srbA);
    StubAssociatedBeanB srbB5 = new StubAssociatedBeanB();
    srbA.setProperty5(srbB5);
    srbA.setProperty6(new StubAssociatedBean());
    srbA.setProperty7(new StubAssociatedBeanA());
    srbA.setPropertyLoop(srb);
    srb.setProperty4(srbA);
    srbB = new StubAssociatedBeanB();
    srb.setProperty5(srbB);
    rousseauBeans.add(srb);

    srb = new StubAssociatedBean();
    for (int i = 0; i < 5; i++) {
      StubAssociatedBeanA srbAQ = new StubAssociatedBeanA();
      srbAQ.setProperty6(srb);
      StubAssociatedBeanB srbBQ = new StubAssociatedBeanB();
      srbBQ.setProperty2(srb);
      srbBQ.setProperty8(srbAQ);
      srbAQ.setProperty5(srbBQ);
      srb.addPropertyA(srbAQ);
    }
    rousseauBeans.add(srb);

    return rousseauBeans;
  }

  @After
  public void tearDown() throws Exception {
    $objects = null;
  }

  @SuppressWarnings("unchecked")
  public void testDirectAssociatedBeans(StubAssociatedBean sb) {
    Set<StubAssociatedBean> result = directAssociatedBeans(sb, StubAssociatedBean.class);
    assertNotNull(result);
    for (PropertyDescriptor pd : getPropertyDescriptors(sb)) {
      if (StubAssociatedBean.class.isAssignableFrom(pd.getPropertyType()) && propertyValue(sb, pd.getName()) != null) {
        assertTrue(result.contains(propertyValue(sb, pd.getName())));
      }
      else if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
        Set<? extends Object> associated = null;
        try {
          associated = (Set<? extends Object>)getProperty(sb, pd.getName());
        }
        catch (InvocationTargetException exc) {
          if (! (exc.getCause() instanceof NullPointerException)) {
            fail();
          }
        }
        catch (IllegalAccessException exc) {
          fail();
        }
        catch (NoSuchMethodException exc) {
          fail();
        }
        if (associated != null) {
          for (Object sbr : associated) {
            if (sbr instanceof StubAssociatedBean) {
              assertTrue(result.contains(sbr));
            }
          }
        }
      }
    }
    for (StubAssociatedBean sbr : result) {
      boolean found = false;
      for (PropertyDescriptor pd : getPropertyDescriptors(sb)) {
        Class<?> pType = pd.getPropertyType();
        Object pValue = propertyValue(sb, pd.getName());
        if ((StubAssociatedBean.class.isAssignableFrom(pType) && pValue == sbr) ||
            (Collection.class.isAssignableFrom(pType) && ((Collection<?>)pValue).contains(sbr))) {
          found = true;
          break;
        }
      }
      if (! found) {
        fail();
      }
    }
  }

  @Test
  public void testDirectAssociatedBeans() {
    for (StubAssociatedBean sb : $objects) {
      testDirectAssociatedBeans(sb);
    }
  }

  public void testAssociatedBeans(StubAssociatedBean sb) {
    Set<StubAssociatedBean> result = associatedBeans(sb, StubAssociatedBean.class);
    assertNotNull(result);
    Set<StubAssociatedBean> expected = new HashSet<StubAssociatedBean>();
    expected.add(sb);
    Set<StubAssociatedBean> daSbs = directAssociatedBeans(sb, StubAssociatedBean.class);
    expected.addAll(daSbs);
    for (StubAssociatedBean sbr : daSbs) {
      expected.addAll(associatedBeans(sbr, StubAssociatedBean.class));
    }
    assertEquals(expected, result);
  }

  @Test
  public void testAssociatedBeans() {
    for (StubAssociatedBean sb : $objects) {
      testAssociatedBeans(sb);
    }
  }

}

