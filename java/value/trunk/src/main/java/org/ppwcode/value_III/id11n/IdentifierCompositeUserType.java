/*<license>
Copyright 2004 - $Date: 2008-11-27 18:07:26 +0100 (Thu, 27 Nov 2008) $ by PeopleWare n.v..

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

package org.ppwcode.value_III.id11n;

import java.lang.reflect.InvocationTargetException;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.reflect_I.InstanceHelpers;
import org.ppwcode.util.reflect_I.TypeHelpers;
import org.ppwcode.value_III.organization.id11n.OrganizationIdentifier;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueCompositeUserType;

/**
 * <p>Hibernate 3 user type to store and retrieve {@link Identifier} instances.</p>
 * <p>{@link BeEnterpriseNumber} instances are stored in 2 columns of type VARCHAR.
 *   The first contains the FQCN of the actual identifier class to which the instance belongs.
 *   The second column contains the identifier. Both are limited to 255 characters.</p>
 *
 * @author    Ruben Vandeginste
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date: 2008-11-27 18:07:26 +0100 (Thu, 27 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3748 $",
         date     = "$Date: 2008-11-27 18:07:26 +0100 (Thu, 27 Nov 2008) $")
public final class IdentifierCompositeUserType extends AbstractImmutableValueCompositeUserType {

  /**
   * Which type we return is defined by a parameter set on the definition of this user type.
   * This should be a subtype of {@link Identifier}.
   */
  @Override
  public Class<? extends Identifier> returnedClass() {
    return Identifier.class;
  }

  public Type[] getPropertyTypes() {
    return new Type[] { Hibernate.STRING, Hibernate.STRING };
  }

  public final String[] getPropertyNames() {
    return new String[] { "$type", "$identifier" };
  }

  public Object getPropertyValue(Object obj, int prop) {
    try {
      OrganizationIdentifier id = (OrganizationIdentifier) obj;
      if (prop == 0) {
        return id.getClass().getCanonicalName();
      } else if (prop == 1) {
        return id.getIdentifier();
      } else {
        throw new HibernateException("getPropertyValue with wrong index for "+returnedClass());
      }
    } catch (ClassCastException exc) {
      throw new HibernateException("cannot cast component into "+returnedClass(), exc);
    }
  }

  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
  throws HibernateException, SQLException {
    if (value != null && !returnedClass().isInstance(value)) {
      throw new HibernateException("this user type can only handle values of type "
                                   + returnedClass().getCanonicalName()
                                   + "; "
                                   + value.getClass().getCanonicalName()
                                   + " is not supported");
    }
    else if (value == null) {
      Hibernate.STRING.nullSafeSet(st, null, index, session);
      Hibernate.STRING.nullSafeSet(st, null, index + 1, session);
    }
    else {
      OrganizationIdentifier id = (OrganizationIdentifier) value;
      Hibernate.STRING.nullSafeSet(st, id.getClass().getCanonicalName(), index, session);
      Hibernate.STRING.nullSafeSet(st, id.getIdentifier(), index + 1, session);
    }
  }

  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
  throws HibernateException, SQLException {
    Identifier result = null;
    String clazz = null;
    String identifier = null;
    try {
      clazz = (String)Hibernate.STRING.nullSafeGet(rs, names[0], session, owner);
      identifier = (String)Hibernate.STRING.nullSafeGet(rs, names[1], session, owner);
      if (clazz == null && identifier == null) {
        result = null;
      } else {
        Class<? extends Identifier> idType = TypeHelpers.type(clazz);
        result = InstanceHelpers.robustNewInstance(idType, identifier);
      }
      return result;
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (ClassCastException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (InvocationTargetException exc) {
      throw new HibernateException("Creation of identifier of type " + clazz + " failed with an application exception", exc);
    }
  }

}
