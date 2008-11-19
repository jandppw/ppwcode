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

package org.ppwcode.value_III.time.interval;


import static java.sql.Types.TIMESTAMP;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueUserType;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * A Hibernate 3 {@code UserType} for {@link BeginEndTimeInterval}. Begin and end are stored in 2
 * columns in the database as {@code TIMESTAMP}. {@code null} is represented by {@code null}
 * in both columns. This is not a problem because both the begin and end being {@code null}
 * is forbidden for {@link BeginEndTimeInterval BeginEndTimeIntervals}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class BeginEndTimeIntervalUserType extends AbstractImmutableValueUserType {

  /*<section name="meta">*/
  //------------------------------------------------------------------

  @Override
  public Class<? extends ImmutableValue> returnedClass() {
    return BeginEndTimeInterval.class;
  }

  /**
   * Static definition of the 2 TIMESTAMP columns we will write in.
   */
  @Invars(@Expression("SQL_TYPES == {TIMESTAMP, TIMESTAMP}"))
  private static final int[] SQL_TYPES = {TIMESTAMP, TIMESTAMP};

  @MethodContract(post = @Expression("SQL_TYPES"))
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /*</section>*/



  public void nullSafeSet(PreparedStatement st, Object value, int index)
      throws HibernateException, SQLException {
    if (value != null && ! returnedClass().isInstance(value)) {
      throw new HibernateException("this user type can only handle values of type " + returnedClass().getCanonicalName() +
                                   "; " + value.getClass().getCanonicalName() + " is not supported");
    }
    else if (value == null) {
      st.setNull(index, TIMESTAMP);
      st.setNull(index + 1, TIMESTAMP);
    }
    else {
      BeginEndTimeInterval beti = (BeginEndTimeInterval)value;
      Hibernate.TIMESTAMP.nullSafeSet(st, beti.getBegin(), index);
      Hibernate.TIMESTAMP.nullSafeSet(st, beti.getEnd(), index + 1);
    }
  }

  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    try {
      Date begin = (Date)Hibernate.TIMESTAMP.nullSafeGet(rs, names[0]);
      Date end = (Date)Hibernate.TIMESTAMP.nullSafeGet(rs, names[1]);
      if (begin == null && end == null) {
        return null;
      }
      return new BeginEndTimeInterval(begin, end);
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (ClassCastException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 2 values", exc);
    }
    catch (IllegalTimeIntervalException exc) {
      throw new HibernateException("data received from database did violate invariants for " + BeginEndTimeInterval.class, exc);
    }
  }

}
