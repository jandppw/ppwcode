/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v.

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

package org.ppwcode.vernacular.semantics_VI.bean;


import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.exception_III.CompoundSemanticException;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Supporting methods for working with {@link RousseauBean RousseauBeans}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class RousseauBeanHelpers {

  private RousseauBeanHelpers() {
    // NOP
  }

  /**
   * The upstream {@link RousseauBean RousseauBeans} starting from {@code rb}.
   * These are the beans that are simple properties of {@code rb}. Upstream means
   * in most cases (this is all that is implemented at this time) the beans
   * reachable via a to-one association.
   *
   * @mudo method is probably never used; remove before release
   */
  @MethodContract(
    pre  = @Expression("_rb != null"),
    post = {
      @Expression("result != null"),
      @Expression("for (PropertyDescriptor pd : getPropertyDescriptors(_rb)) {" +
                    "RousseauBean.class.isAssignableFrom(pd.propertyType) && propertyValue(_rb, pd.name) != null ? " +
                      "result.contains(propertyValue(_rb, pd.name))" +
                  "}"),
      @Expression("for (RousseaBean rbr : result) {" +
                    "exists (PropertyDescriptor pd : getPropertyDescriptors(_rb)) {" +
                      "rbr == propertyValue(_rb, pd.name)" +
                    "}" +
                  "}")
    }
  )
  public static Set<RousseauBean> directUpstreamRousseauBeans(RousseauBean rb) {
    assert preArgumentNotNull(rb, "rb");
    Set<RousseauBean> result = new HashSet<RousseauBean>();
    PropertyDescriptor[] pds = getPropertyDescriptors(rb);
    for (int i = 0; i < pds.length; i++) {
      PropertyDescriptor pd = pds[i];
      if (RousseauBean.class.isAssignableFrom(pd.getPropertyType())) {
        RousseauBean upstreamCandidate = propertyValue(rb, pd.getName());
        if (upstreamCandidate != null) {
          result.add(upstreamCandidate);
        }
      }
    }
    return result;
  }

  /**
   * All upstream {@link RousseauBean RousseauBeans} starting from {@code rb}.
   * These are the beans that are simple properties of {@code rb}. Upstream means
   * in most cases (this is all that is implemented at this time) the beans
   * reachable via a to-one association. This is applied recursively.
   * {@code rb} itself is also part of the set.
   *
   * @mudo method is probably never used; remove before release
   */
  @MethodContract(
    pre  = @Expression("_rb != null"),
    post = {
      @Expression("result != null"),
      @Expression("{_rb} U directUpstreamRousseauBeans(_rb) U " +
                   "union (RousseauBean rbr : directUpstreamRousseauBeans(_rb)) {upstreamRousseauBeans(rbr)}")
    }
  )
  public static Set<RousseauBean> upstreamRousseauBeans(RousseauBean rb) {
    assert preArgumentNotNull(rb, "rb");
    LinkedList<RousseauBean> agenda = new LinkedList<RousseauBean>();
    agenda.add(rb);
    int i = 0;
    while (i < agenda.size()) {
      RousseauBean current = agenda.get(i);
      for (RousseauBean rousseauBean : directUpstreamRousseauBeans(current)) {
        if (! agenda.contains(rousseauBean)) {
          agenda.add(rousseauBean);
        }
      }
      i++;
    }
    return new HashSet<RousseauBean>(agenda);
  }

  /**
   * Normalize {@code rb} and all other {@link RousseauBean RousseauBeans} that can be reached
   * from {@code rb} over to-one associations (upstream). At the same time, check the civility
   * and gather all {@link PropertyException PropertyExceptions} that might occur.
   *
   * @mudo method is probably never used; remove before release
   */
  @MethodContract(
    pre  = @Expression("_rb != null"),
    post = {
      @Expression("result != null"),
      @Expression("for (RousseauBean rbr : upstreamRousseauBeans(_rb)) {rbr.normalize()}"),
      @Expression("result.allElementExceptions == union (RousseauBean rbr : upstreamRousseauBeans(_rb)) {rbr.wildExceptions().allElementExceptions}")
    }
  )
  public static CompoundSemanticException normalizeAndCheckCivilityOnUpstreamRousseauBeans(RousseauBean rb) {
    assert preArgumentNotNull(rb, "rb");
    LinkedList<RousseauBean> agenda = new LinkedList<RousseauBean>();
    agenda.add(rb);
    int i = 0;
    CompoundSemanticException cpe = new CompoundSemanticException("UPSTREAM_EXCEPTIONS", null);
    while (i < agenda.size()) {
      RousseauBean current = agenda.get(i);
      current.normalize();
      for (PropertyException pExc : current.wildExceptions().getElementExceptions()) {
        cpe.addElementException(pExc);
      }
      Set<RousseauBean> durbs = directUpstreamRousseauBeans(current);
      for (RousseauBean rousseauBean : durbs) {
        if (! agenda.contains(rousseauBean)) {
          agenda.add(rousseauBean);
        }
      }
      i++;
    }
    return cpe;
  }

  /**
   * Normalize all {@link RousseauBean RousseauBean} in {@code rbs}.
   */
  @MethodContract(
    pre  = @Expression("_rbs != null"),
    post = {
      @Expression("for (RousseauBean rb : _rbs) {rb.normalize()}")
    }
  )
  public static void normalize(Set<? extends RousseauBean> rbs) {
    assert preArgumentNotNull(rbs, "rbs");
    for (RousseauBean rb : rbs) {
      rb.normalize();
    }
  }

  /**
   * Gather all {@link RousseauBean#wildExceptions() wild exceptions} from all {@link RousseauBean RousseauBeans}
   * in {@code rbs}.
   */
  @MethodContract(
    pre  = @Expression("_rbs != null"),
    post = {
      @Expression("result != null"),
      @Expression("result.allElementExceptions == union (RousseauBean rb : _rbs) {rb.wildExceptions().allElementExceptions}")
    }
  )
  public static CompoundSemanticException wildExceptions(Set<? extends RousseauBean> rbs) {
    assert preArgumentNotNull(rbs, "rbs");
    CompoundSemanticException cpe = new CompoundSemanticException("UPSTREAM_EXCEPTIONS", null);
    for (RousseauBean rb : rbs) {
      for (PropertyException pExc : rb.wildExceptions().getElementExceptions()) {
        cpe.addElementException(pExc);
      }
    }
    return cpe;
  }

}
