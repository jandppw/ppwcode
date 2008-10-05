/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.semantics_VI.bean;


import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;
import static org.ppwcode.vernacular.semantics_VI.bean.SemanticBeanHelpers.directAssociatedSemanicBeans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SemanticBeanHelpersTest {

  private Set<? extends SemanticBean> $semanticBeans;

  @Before
  public void setUp() throws Exception {
    $semanticBeans = someSemanticBeans();
  }

  public static Set<? extends SemanticBean> someSemanticBeans() {
    return RousseauBeanHelpersTest.someRousseauBeans();
  }

  @After
  public void tearDown() throws Exception {
    $semanticBeans = null;
  }

  @SuppressWarnings("unchecked")
  public void testDirectAssociatedSemanticBeans(SemanticBean sb) {
    Set<SemanticBean> result = directAssociatedSemanicBeans(sb);
    assertNotNull(result);
    for (PropertyDescriptor pd : getPropertyDescriptors(sb)) {
      if (SemanticBean.class.isAssignableFrom(pd.getPropertyType()) && propertyValue(sb, pd.getName()) != null) {
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
            if (sbr instanceof SemanticBean) {
              assertTrue(result.contains(sbr));
            }
          }
        }
      }
    }
    for (SemanticBean sbr : result) {
      boolean found = false;
      for (PropertyDescriptor pd : getPropertyDescriptors(sb)) {
        Class<?> pType = pd.getPropertyType();
        Object pValue = propertyValue(sb, pd.getName());
        if ((SemanticBean.class.isAssignableFrom(pType) && pValue == sbr) ||
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
  public void testDirectAssociatedSemanticBeans() {
    for (SemanticBean sb : $semanticBeans) {
      testDirectAssociatedSemanticBeans(sb);
    }
  }

}

