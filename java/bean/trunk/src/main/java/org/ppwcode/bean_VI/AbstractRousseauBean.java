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

package org.ppwcode.bean_VI;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A partial implementation of the interface {@link RousseauBean}.
 *
 * @author    nsmeets
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractRousseauBean implements RousseauBean {

  private static final Log LOG = LogFactory.getLog(AbstractRousseauBean.class);

  /**
   * @see  RousseauBean
   *
   * @protected
   * <p><em>The implementation in this class returns an emtpy
   *   CompoundPropertyException
   *   (<code>result.getElementExceptions().isEmpty();</code>).</em>
   *   Subclasses should override this method.</p>
   */
  public CompoundPropertyException getWildExceptions() {
    return new CompoundPropertyException(this, null, null, null);
  }

  /**
   * @see  RousseauBean
   */
  public final boolean isCivilized() {
    return getWildExceptions().isEmpty();
  }

  /**
   * This method does nothing, but will throw the wild exceptions
   * if this bean is not civilized.
   *
   * @see  RousseauBean
   */
  public final void checkCivility() throws CompoundPropertyException {
    CompoundPropertyException cpe = getWildExceptions();
    if (!cpe.isEmpty()) {
      if (LOG.isDebugEnabled()) { // this if does only loggin
        LOG.debug("the wild exceptions were not empty; we will throw the "
            + "CompoundPropertyException"); //$NON-NLS-1$
        LOG.debug("current RousseauBean is " + toString()); //$NON-NLS-1$
        LOG.debug(cpe.getElementExceptions());
        LOG.debug("ElementExceptions details:"); //$NON-NLS-1$
        Iterator iter =  cpe.getElementExceptions().entrySet().iterator();
        while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry)iter.next();
          if (LOG.isDebugEnabled()) {
            Object key = entry.getKey();
            if (key == null) {
              LOG.debug("null as key:");
            }
            else {
              LOG.debug(key + ":");
            }
          }
          Set propertyExceptions = (Set)entry.getValue();
          Iterator exceptions = propertyExceptions.iterator();
          while (exceptions.hasNext()) {
            PropertyException pexc = (PropertyException)exceptions.next();
            LOG.debug(pexc, pexc);
          }
        }
      }
      cpe.close();
      throw cpe;
    }
  }

  /**
   * @see  RousseauBean
   *
   * @protected
   *  The implementation of this method in this class does NOP,
   *  as a convenience to most subtypes.
   */
  public void normalize() {
    // NOP
  }

  /**
   * This instance has the same values as the instance <code>rb</code>.
   * This comparison does not take into account to-many associations,
   * to avoid infinite loops. Comparison is independent of normalization,
   * i.e., it should return the same result when the participants in the
   * comparison are normalized or not. To accomplish that, this method
   * may call {@link #normalize()} itself.
   *
   * @see  RousseauBean
   */
  public boolean hasSameValues(final RousseauBean rb) {
    return (rb != null) && (getClass() == rb.getClass());
  }

  /**
   * @see  RousseauBean
   */
  public final boolean equals(final Object other) {
    return super.equals(other);
  }

  /**
   * @see  RousseauBean
   */
  public final int hashCode() {
    return super.hashCode();
  }

  /**
   * Convenience method for generating the toString
   * of to-many associations.
   *
   * @pre       c != null;
   */
  protected final String collectionString(final Collection c) {
    assert c != null;
    String result = "{"; //$NON-NLS-1$
    for (Iterator i = c.iterator(); i.hasNext();) {
        Object item = i.next();
        result = result + item.getClass().getName()
                     + "@" + item.hashCode(); //$NON-NLS-1$
        if (i.hasNext()) {
          result = result + ", "; //$NON-NLS-1$
        }
    }
    result = result + "}"; //$NON-NLS-1$
    return result;
  }

  /**
   * Returns true when the two given objects are both null, or equal;
   * returns false otherwise.
   *
   * @param   one
   * @param   other
   * @return  (one == null)
   *            ? result == (other == null)
   *            : result == (one.hasSameValues(other));
   */
  public static boolean hasSameValuesWithNull(final RousseauBean one, final RousseauBean other) {
    return (one == null)
            ? (other == null)
            : one.hasSameValues(other);
  }

}
