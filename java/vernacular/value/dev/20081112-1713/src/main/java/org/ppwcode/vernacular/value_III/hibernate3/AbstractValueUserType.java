/*<license>
Copyright 2004 - $Date: 2008-11-12 15:03:11 +0100 (Wed, 12 Nov 2008) $ by PeopleWare n.v..

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

package org.ppwcode.vernacular.value_III.hibernate3;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.usertype.LoggableUserType;
import org.hibernate.usertype.UserType;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.Value;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Hibernate 3 user type support for value types implemented with respect to the ppwcode value vernacular.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date: 2008-11-12 15:03:11 +0100 (Wed, 12 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3500 $",
         date     = "$Date: 2008-11-12 15:03:11 +0100 (Wed, 12 Nov 2008) $")
public abstract class AbstractValueUserType implements UserType, LoggableUserType {

  @Basic
  public abstract Class<? extends Value> returnedClass();

  /**
   * {@link Value#toString()} is intended for debugging and logging
   */
  @MethodContract(post = @Expression("_value == null ? 'null' : value.toString()"))
  public final String toLoggableString(Object value, SessionFactoryImplementor factory) {
    return value == null ? "null" : value.toString();
  }

  /**
   * {@link Value#equals(Object)} is defined for value equality
   */
  @MethodContract(post = @Expression("x == null ? y == null : x.equals(y)"))
  public final boolean equals(Object x, Object y) throws HibernateException {
    return x == null ? y == null : x.equals(y);
  }

  /**
   * {@link Value#hashCode()} is defined for value equality
   */
  @MethodContract(post = @Expression("x == null ? 0 : x.hashCode()"))
  public final int hashCode(Object x) throws HibernateException {
    return x == null ? 0 : x.hashCode();
  }

  /**
   * Values are {@link Serializable}, and thus are cacheable themselves.
   */
  public final Serializable disassemble(Object value) throws HibernateException {
    try {
      Value v = (Value)value;
      return v;
    }
    catch (ClassCastException ccExc) {
      illegalType(value, ccExc);
      return null; // keep compiler happy
    }
  }

  /**
   * Values are {@link Serializable}, and thus are cached themselves.
   */
  @MethodContract(post = @Expression("cached"))
  public final Object assemble(Serializable cached, Object owner) throws HibernateException {
    return cached;
  }

  /**
   * Helper method to throw an appropriate {@link HibernateException} when we are asked
   * to work with an object of a type that we are not equiped for.
   */
  protected final void illegalType(Object value, ClassCastException ccExc) throws HibernateException {
    throw new HibernateException("Value \"" + value + "\" is not of a type this user type can handle. This user " +
        "type only handles instances of " + returnedClass(), ccExc);
  }

}
