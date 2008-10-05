/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.semantics_VI.bean;


import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;
import static org.ppwcode.vernacular.semantics_VI.bean.AssociationHelpers.associatedBeans;
import static org.ppwcode.vernacular.semantics_VI.bean.AssociationHelpers.directAssociatedBeans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBean;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBeanA;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBeanB;


public class AssociationHelpersTest {

  private Set<? extends StubRousseauBean> $objects;

  @Before
  public void setUp() throws Exception {
    Set<? extends StubRousseauBean> objects = someRousseauBeans();
    $objects = objects;
  }

  public static Set<StubRousseauBean> someRousseauBeans() {
    Set<StubRousseauBean> rousseauBeans = new HashSet<StubRousseauBean>();

    StubRousseauBean srb = new StubRousseauBean();
    rousseauBeans.add(srb);

    StubRousseauBean srb1 = new StubRousseauBean();
    srb = new StubRousseauBean();
    srb.setProperty2(srb1);
    StubRousseauBeanA srbA = new StubRousseauBeanA();
    srb.setProperty4(srbA);
    StubRousseauBeanB srbB = new StubRousseauBeanB();
    srb.setProperty5(srbB);
    rousseauBeans.add(srb);

    srb1 = new StubRousseauBean();
    srb = new StubRousseauBean();
    srb.setProperty2(srb1);
    srbA = new StubRousseauBeanA();
    srbA.setProperty2(srb1);
    srbA.setProperty4(srbA);
    StubRousseauBeanB srbB5 = new StubRousseauBeanB();
    srbA.setProperty5(srbB5);
    srbA.setProperty6(new StubRousseauBean());
    srbA.setProperty7(new StubRousseauBeanA());
    srbA.setPropertyLoop(srb);
    srb.setProperty4(srbA);
    srbB = new StubRousseauBeanB();
    srb.setProperty5(srbB);
    rousseauBeans.add(srb);

    srb = new StubRousseauBean();
    for (int i = 0; i < 5; i++) {
      StubRousseauBeanA srbAQ = new StubRousseauBeanA();
      srbAQ.setProperty6(srb);
      StubRousseauBeanB srbBQ = new StubRousseauBeanB();
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
  public void testDirectAssociatedBeans(StubRousseauBean sb) {
    Set<StubRousseauBean> result = directAssociatedBeans(sb, StubRousseauBean.class);
    assertNotNull(result);
    for (PropertyDescriptor pd : getPropertyDescriptors(sb)) {
      if (StubRousseauBean.class.isAssignableFrom(pd.getPropertyType()) && propertyValue(sb, pd.getName()) != null) {
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
            if (sbr instanceof StubRousseauBean) {
              assertTrue(result.contains(sbr));
            }
          }
        }
      }
    }
    for (StubRousseauBean sbr : result) {
      boolean found = false;
      for (PropertyDescriptor pd : getPropertyDescriptors(sb)) {
        Class<?> pType = pd.getPropertyType();
        Object pValue = propertyValue(sb, pd.getName());
        if ((StubRousseauBean.class.isAssignableFrom(pType) && pValue == sbr) ||
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
    for (StubRousseauBean sb : $objects) {
      testDirectAssociatedBeans(sb);
    }
  }

  public void testAssociatedBeans(StubRousseauBean sb) {
    Set<StubRousseauBean> result = associatedBeans(sb, StubRousseauBean.class);
    assertNotNull(result);
    Set<StubRousseauBean> expected = new HashSet<StubRousseauBean>();
    expected.add(sb);
    Set<StubRousseauBean> daSbs = directAssociatedBeans(sb, StubRousseauBean.class);
    expected.addAll(daSbs);
    for (StubRousseauBean sbr : daSbs) {
      expected.addAll(associatedBeans(sbr, StubRousseauBean.class));
    }
    assertEquals(expected, result);
  }

  @Test
  public void testAssociatedBeans() {
    for (StubRousseauBean sb : $objects) {
      testAssociatedBeans(sb);
    }
  }

}
