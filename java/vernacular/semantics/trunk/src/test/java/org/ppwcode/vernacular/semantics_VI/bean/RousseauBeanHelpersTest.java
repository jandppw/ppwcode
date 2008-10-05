/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.semantics_VI.bean;


import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.directUpstreamRousseauBeans;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.normalizeAndCheckCivilityOnUpstreamRousseauBeans;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.upstreamRousseauBeans;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBean;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBeanA;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBeanB;
import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;


public class RousseauBeanHelpersTest {

  private Set<? extends RousseauBean> $rousseauBeans;

  @Before
  public void setUp() throws Exception {
    Set<? extends RousseauBean> rousseauBeans = someRousseauBeans();
    $rousseauBeans = rousseauBeans;
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
    return rousseauBeans;
  }

  @After
  public void tearDown() throws Exception {
    $rousseauBeans = null;
  }

  public void testDirectUpstreamRousseauBeans(RousseauBean rb) {
    Set<RousseauBean> result = directUpstreamRousseauBeans(rb);
    assertNotNull(result);
    for (PropertyDescriptor pd : getPropertyDescriptors(rb)) {
      if (RousseauBean.class.isAssignableFrom(pd.getPropertyType()) && propertyValue(rb, pd.getName()) != null) {
        assertTrue(result.contains(propertyValue(rb, pd.getName())));
      }
    }
    for (RousseauBean rousseauBean : result) {
      boolean found = false;
      for (PropertyDescriptor pd : getPropertyDescriptors(rb)) {
        if (RousseauBean.class.isAssignableFrom(pd.getPropertyType()) && propertyValue(rb, pd.getName()) == rousseauBean) {
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
  public void testDirectUpstreamRousseauBeans() {
    for (RousseauBean rb : $rousseauBeans) {
      testDirectUpstreamRousseauBeans(rb);
    }
  }

  public void testUpstreamRousseauBeans(RousseauBean rb) {
    Set<RousseauBean> result = upstreamRousseauBeans(rb);
    assertNotNull(result);
    Set<RousseauBean> expected = new HashSet<RousseauBean>();
    expected.add(rb);
    Set<RousseauBean> duRbs = directUpstreamRousseauBeans(rb);
    expected.addAll(duRbs);
    for (RousseauBean rbr : duRbs) {
      expected.addAll(upstreamRousseauBeans(rbr));
    }
    assertEquals(expected, result);
  }

  @Test
  public void testUpstreamRousseauBeans() {
    for (RousseauBean rb : $rousseauBeans) {
      testUpstreamRousseauBeans(rb);
    }
  }

  public void testNormalizeAndCheckCivilityOnUpstreamRousseauBeans(RousseauBean rb) {
    CompoundPropertyException result = normalizeAndCheckCivilityOnUpstreamRousseauBeans(rb);
    assertNotNull(result);
    Set<PropertyException> expected = new HashSet<PropertyException>();
    for (RousseauBean rbr : upstreamRousseauBeans(rb)) {
      assertTrue(((StubRousseauBean)rbr).normalized);
      expected.addAll(((StubRousseauBean)rbr).wildExceptions.getAllElementExceptions());
    }
    assertEquals(expected, result.getAllElementExceptions());
  }

  @Test
  public void testNormalizeAndCheckCivilityOnUpstreamRousseauBeans() {
    for (RousseauBean rb : $rousseauBeans) {
      testNormalizeAndCheckCivilityOnUpstreamRousseauBeans(rb);
    }
  }

}

