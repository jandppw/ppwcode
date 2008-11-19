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


import static java.sql.Types.LONGVARCHAR;
import static java.sql.Types.VARCHAR;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.ppwcode.vernacular.value_III.SemanticValueException;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueUserType;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


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
public final class LocalizedStringUserType extends AbstractImmutableValueUserType {

  /*<section name="meta">*/
  //------------------------------------------------------------------

  @Override
  public Class<? extends ImmutableValue> returnedClass() {
    return LocalizedString.class;
  }

  /**
   * Static definition of the 1 LOCALE as VARCHAR and 1 LONGVARCHAR.
   */
  @Invars(@Expression("SQL_TYPES == {VARCHAR, LONGVARCHAR}"))
  private static final int[] SQL_TYPES = {VARCHAR, LONGVARCHAR};

  @MethodContract(post = @Expression("SQL_TYPES"))
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /*</section>*/


  public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
    if (value != null && !returnedClass().isInstance(value)) {
      throw new HibernateException("this user type can only handle values of type "
                                   + returnedClass().getCanonicalName()
                                   + "; "
                                   + value.getClass().getCanonicalName()
                                   + " is not supported");
    }
    else if (value == null) {
      st.setNull(index, VARCHAR);
      st.setNull(index + 1, LONGVARCHAR);
    }
    else {
      LocalizedString ls = (LocalizedString)value;
      Hibernate.LOCALE.nullSafeSet(st, ls.getLocale(), index);
      Hibernate.STRING.nullSafeSet(st, ls.getString(), index + 1);
    }
  }

  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    try {
      Locale lFromDb = (Locale)Hibernate.LOCALE.nullSafeGet(rs, names[0]);
      String sFromDb = (String)Hibernate.STRING.nullSafeGet(rs, names[1]);
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
    catch (SemanticValueException exc) {
      throw new HibernateException("data received from database did violate invariants for " + returnedClass(), exc);
    }
  }

}
