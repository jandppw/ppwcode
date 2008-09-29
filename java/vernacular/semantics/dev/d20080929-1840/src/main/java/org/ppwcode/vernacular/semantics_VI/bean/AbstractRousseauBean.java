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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Scope;


/**
 * <p>A partial implementation of the interface {@link RousseauBean}.</p>
 * <p>Subclasses should take care to override {@link #wildExceptions()} diligently,
 *   to add validation concerning properties and civility invariants added in the subclass.</p>
 *
 * @author    nsmeets
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @mudo unit test
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractRousseauBean extends AbstractSemanticBean implements RousseauBean {

  private static final Log LOG = LogFactory.getLog(AbstractRousseauBean.class);

  /**
   * {@inheritDoc}
   *
   * @protected
   * <p><em>The implementation in this class returns an emtpy
   *   CompoundPropertyException.</em> Subclasses should override this method
   *   with the following idiom:</p>
   * <pre>
   * ATOverride
   * ATMethodContract(
   *   post = {
   *     ATExpression(&quot;'someProperty <var>condition</var> ?? result.contains(new <var>XXXPropertyException(...)</var>)&quot;),
   *     ...
   *     ATExpression(&quot;for (PropertyException pe : result.allElementExceptions) {&quot; +
   *                  &quot;pe.like(new <var>XXXPropertyException(...)</var>) || &quot; +
   *                  &quot;...&quot;)
   *   }
   * )
   * public CompoundPropertyException getWildExceptions() {
   *   CompoundPropertyException cpe = super.getWildExceptions();
   *   if (getSomeProperty() <var>condition</var>) {
   *     cpe.addElementException(new <var>XXXPropertyException(...)</var>));
   *   }
   *   ...
   *   return cpe;
   * }
   * </pre>
   */
  @MethodContract(
    post = {
      @Expression(scope = Scope.PROTECTED, value = "result.propertyName == null"),
      @Expression(scope = Scope.PROTECTED, value = "result.elementExceptions.empty")
    }
  )
  public CompoundPropertyException wildExceptions() {
    return new CompoundPropertyException(this, null, null, null);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean civilized() {
    return wildExceptions().isEmpty();
  }

  /**
   * {@inheritDoc}
   *
   * This method logs progress on the debug level.
   */
  public final void checkCivility() throws CompoundPropertyException {
    CompoundPropertyException cpe = wildExceptions();
    if (!cpe.isEmpty()) {
      if (LOG.isDebugEnabled()) { // this if does only logging
        LOG.debug("the wild exceptions were not empty; we will throw the CompoundPropertyException");
        LOG.debug("current RousseauBean is " + toString());
        LOG.debug(cpe.getElementExceptions());
        LOG.debug("ElementExceptions details:");
        for (Map.Entry<String, Set<PropertyException>> entry : cpe.getElementExceptions().entrySet()) {
          Object key = entry.getKey();
          if (key == null) {
            LOG.debug("null as key:");
          }
          else {
            LOG.debug(key + ":");
          }
          for (PropertyException pexc : entry.getValue()) {
            LOG.debug(pexc, pexc);
          }
        }
      }
      cpe.close();
      throw cpe;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @protected
   * The implementation of this method in this class does NOP,
   * as a convenience to most subtypes.
   */
  public void normalize() {
    // NOP
  }

}
