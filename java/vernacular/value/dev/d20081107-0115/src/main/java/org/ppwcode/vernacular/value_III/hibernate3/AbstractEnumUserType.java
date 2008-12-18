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

package org.ppwcode.vernacular.value_III.hibernate3;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.EnumEditor;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;

/**
 * MUDO UNFINISHED!!!!! is ver much like http://www.hibernate.org/272.html Java 5 EnumUserType
 *
 *  <typedef name="suit" class='EnumUserType'>
      <param name="enumClassName">com.company.project.Suit</param>
  </typedef>
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @idea currently no unit tests
 * @mudo UNFINISHED
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("enumerationValueEditor() != null"))
public class AbstractEnumUserType<_Enum_ extends Enum<_Enum_>> implements UserType {

  /**
   * @post new.getEnumEditor() == editor;
   */
  protected AbstractEnumUserType(final EnumEditor<_Enum_> editor) {
    $editor = editor;
  }

  /**
   * An instance of the enumeration type editor of the enumeration
   * type this UserType works for.
   *
   * @basic
   */
  public final EnumEditor<_Enum_> getEnumEditor() {
    return $editor;
  }

  private EnumEditor<_Enum_> $editor;

  private static final int[] SQL_TYPES = {Types.VARCHAR};

  /**
   * @return {Types.VARCHAR};
   */
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /**
   * @return getEnumEditor().getEnum();
   */
  public final Class<?> returnedClass() {
    return getEnumEditor().getValueType();
  }

  /**
   * @return (x == null) ? (y == null) : x.equals(y);
   */
  public final boolean equals(final Object x, final Object y) {
    return (x == null) ? (y == null) : x.equals(y);
  }

  /**
   * @return value;
   */
  public final Object deepCopy(final Object value) {
    return value;
  }

  /**
   * @return false;
   *         Enum is kind of an ImmutableValue
   */
  public final boolean isMutable() {
    return false;
  }

  /**
   * Retrieve an instance of the mapped class from a JDBC resultset.
   * Implementors should handle possibility of null values.
   *
   * @result  resultSet.wasNull()
   *            ==> result == null;
   * @result  !resultSet.wasNull()
   *            ==> result == the object returned by the property editor for
   *                          the string in the field with <code>names[0]</code>.
   * @throws  HibernateException
   *          Hibernate.STRING.nullSafeGet(resultSet, names[0]);
   * @throws  SQLException
   *          Hibernate.STRING.nullSafeGet(resultSet, names[0]);
   * @throws  SQLException
   *          resultSet.wasNull();
   */
  public final Object nullSafeGet(final ResultSet resultSet, final String[] names, final Object owner)
          throws HibernateException, SQLException {
    Object result = null;
    String tag = (String)Hibernate.STRING.nullSafeGet(resultSet, names[0]);
    if (!(resultSet.wasNull())) {
      getEnumEditor().setAsText(tag);
      result = getEnumEditor().getValue();
    }
    return result;
  }

  /**
   * Write an instance of the mapped class to a prepared statement.
   *
   * @post    value == null
   *            ==> the parameter at the given index is set to null
   * @post    value != null
   *            ==> the parameter at the given index is set to the string
   *                representation of the given value as returned by the
   *                enumeration value editor
   *
   * @throws  HibernateException
   *          (value != null)
              && !returnedClass().getName().equals(value.getClass().getName())
   * @throws  SQLException
   *          value == null
   *          && statement.setNull(index, Types.VARCHAR) throws a SQLException;
   * @throws  SQLException
   *          value != null
   *          && statement.setString(index, getEnumEditor().getAsText());
   */
  public final void nullSafeSet(final PreparedStatement statement, final Object value, final int index)
      throws HibernateException, SQLException {
    // make sure the received value is of the right type
    if ((value != null)
          && !returnedClass().getName().equals(value.getClass().getName())) {
      throw new HibernateException("\"" //$NON-NLS-1$
                                   + ((value == null)
                                        ? "null" //$NON-NLS-1$
                                        : value.toString())
                                   + "\" is not a " //$NON-NLS-1$
                               + returnedClass().getName());
    }
    if (value == null) {
      statement.setNull(index, Types.VARCHAR);
    }
    else {
      getEnumEditor().setValue(value);
      statement.setString(index,
                          getEnumEditor().getAsText());
    }
  }

  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    // make sure the received value is of the right type
    if ((cached != null)
        && !returnedClass().getName().equals(cached.getClass().getName())) {
      throw new HibernateException("\"" + ((cached == null) ? "null" : cached.toString())
                                   + "\" is not a " + returnedClass().getName());
    }
    return cached;
  }

  public Serializable disassemble(Object value) throws HibernateException {
    // make sure the received value is of the right type
    if ((value != null)
        && !returnedClass().getName().equals(value.getClass().getName())) {
      throw new HibernateException("\"" + ((value == null) ? "null" : value.toString())
                                   + "\" is not a " + returnedClass().getName());
    }
    return (Serializable)value; // enumeration values are Serializable
  }

  public int hashCode(Object x) throws HibernateException {
    return x.hashCode();
  }

  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    // make sure the received value is of the right type
    if ((original != null)
        && !returnedClass().getName().equals(original.getClass().getName())) {
      throw new HibernateException("\"" + ((original == null) ? "null" : original.toString())
                                   + "\" is not a " + returnedClass().getName());
    }
    return original; // super method doc says this is what to do for immutable types, which we are (enum)
  }

}
