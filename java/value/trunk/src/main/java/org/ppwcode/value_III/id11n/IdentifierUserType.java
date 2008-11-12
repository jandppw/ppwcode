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

package org.ppwcode.value_III.id11n;


import static java.sql.Types.VARCHAR;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.reflect_I.InstanceHelpers;
import org.ppwcode.util.reflect_I.TypeHelpers;
import org.ppwcode.value_III.organization.id11n.state.be.BeEnterpriseNumber;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueUserType;


/**
 * <p>Hibernate 3 user type to store and retrieve {@link BeEnterpriseNumber} instances.</p>
 * <p>{@link BeEnterpriseNumber} instances are stored in 2 columns of type VARCHAR.
 *   The first contains the FQCN of the actual identifier class to which the instance belongs.
 *   The second column contains the identifier. Both are limited to 255 characters.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date: 2008-11-12 15:03:11 +0100 (Wed, 12 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3500 $",
         date     = "$Date: 2008-11-12 15:03:11 +0100 (Wed, 12 Nov 2008) $")
public final class IdentifierUserType extends AbstractImmutableValueUserType {

//  public void setParameterValues(Properties parameters) {
//    String fqcn = parameters.getProperty("identifierType", Identifier.class.getCanonicalName());
//    $identifierPropertyType = type(fqcn);
//  }
//
//  private Class<? extends Identifier> $identifierPropertyType;

  /**
   * Which type we return is defined by a parameter set on the definition of this user type.
   * This should be a subtype of {@link Identifier}.
   */
  @Override
  public Class<? extends Identifier> returnedClass() {
//    return $identifierPropertyType;
    return Identifier.class;
  }

  public final static int[] SQL_TYPES = {VARCHAR, VARCHAR};

  /**
   * We store the data of {@link Identifier Identifiers} in 2 VARCHAR columns.
   */
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
    if (value == null) {
      st.setNull(index, VARCHAR);
      st.setNull(index + 1, VARCHAR);
    }
    else {
      try {
        Identifier id = (Identifier)value;
        st.setString(index, id.getClass().getCanonicalName());
        st.setString(index + 1, id.getIdentifier());
      }
      catch (ClassCastException ccExc) {
        illegalType(value, ccExc);
      }
    }
  }

  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    Identifier result = null;
    String fqcn = rs.getString(names[0]);
    String id = rs.getString(names[1]);
    if (fqcn == null) {
      assert id == null;
      result = null;
    }
    else {
      try {
        Class<? extends Identifier> idType = TypeHelpers.type(fqcn);
        result = InstanceHelpers.robustNewInstance(idType, id);
      }
      catch (AssertionError aErr) {
        throw new HibernateException("Could not load type " + fqcn + " or could not create instance of that type", aErr.getCause());
      }
      catch (InvocationTargetException exc) {
        throw new HibernateException("Creation of identifier of type " + fqcn + " failed with an application exception", exc);
      }
    }
    return result;
  }

}
