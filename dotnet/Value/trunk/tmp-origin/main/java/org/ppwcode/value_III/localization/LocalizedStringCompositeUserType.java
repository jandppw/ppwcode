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

package org.ppwcode.value_III.localization;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.ppwcode.vernacular.value_III.ValueException;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueCompositeUserType;


/**
 * A Hibernate 3 user type for {@link LocalizedString}. The locale and string
 * are stored in resp. a VARCHAR and LONGVARCHAR column. {@code null} is stored as NULL in both
 * columns. It is not possible that only one column is {@code null}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class LocalizedStringCompositeUserType extends AbstractImmutableValueCompositeUserType
     implements CompositeUserType {

  @Override
  public Class<? extends ImmutableValue> returnedClass() {
    return LocalizedString.class;
  }


  public final Type[] getPropertyTypes() {
    // NOTE: Hibernate.TEXT maps to longvarchar for Hypersonic db,
    //       but gives an error for the Informix db: Informix dialect
    //       does not know how to map this type
    //       Currently using Hibernate.STRING
    return new Type[] { Hibernate.LOCALE, Hibernate.STRING };
  }

  public final String[] getPropertyNames() {
    return new String[] { "$locale", "$text" };
  }

  public Object getPropertyValue(Object obj, int prop) {
    try {
      LocalizedString ls = (LocalizedString) obj;
      if (prop == 0) {
        return ls.getLocale();
      } else if (prop == 1) {
        return ls.getText();
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
      Hibernate.LOCALE.nullSafeSet(st, null, index, session);
      Hibernate.STRING.nullSafeSet(st, null, index + 1, session);
    }
    else {
      LocalizedString ls = (LocalizedString)value;
      Hibernate.LOCALE.nullSafeSet(st, ls.getLocale(), index, session);
      Hibernate.STRING.nullSafeSet(st, ls.getText(), index + 1, session);
    }
  }

  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
  throws HibernateException, SQLException {
    try {
      Locale lFromDb = (Locale)Hibernate.LOCALE.nullSafeGet(rs, names[0], session, owner);
      String sFromDb = (String)Hibernate.STRING.nullSafeGet(rs, names[1], session, owner);
      if (lFromDb == null && sFromDb == null) {
        return null;
      }
      else {
        return new LocalizedString(lFromDb, sFromDb);
      }
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (ClassCastException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (ValueException exc) {
      throw new HibernateException("data received from database did violate invariants for " + returnedClass(), exc);
    }
  }

}
