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

package org.ppwcode.vernacular.value_III.hibernate2;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.EnumerationValueEditor;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;

/**
 * Support class for Hibernate mapping for
 * {@code EnumerationValue} subtypes.
 * Instance of {@code EnumerationValue} subtypes are mapped
 * to a VARCHAR, that contains the
 * {@link EnumerationValueEditor#getAsText() tag} for the enumeration
 * type value, as defined by the property editor. According to the
 * contract of {@code EnumerationValue} and
 * {@link EnumerationValueEditor}, this is equal to
 * {@code EnumerationValue.toString()}.
 *
 * @annotation
 *    Based on <a href="http://www.hibernate.org/172.html">work by
 *    <em>Christian</em> and <em>sethladd</em></a>.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @idea currently no unit tests; but used in practice a lot in the previous version
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars(@Expression("enumerationValueEditor() != null"))
public class AbstractEnumerationUserType implements UserType {

  /**
   * @post new.getEnumerationValueEditor() == editor;
   */
  protected AbstractEnumerationUserType(final EnumerationValueEditor editor) {
    $editor = editor;
  }

  /**
   * An instance of the enumeration type editor of the enumeration
   * type this UserType works for.
   *
   * @basic
   */
  public final EnumerationValueEditor getEnumerationValueEditor() {
    return $editor;
  }

  private EnumerationValueEditor $editor;

  private static final int[] SQL_TYPES = {Types.VARCHAR};

  /**
   * @return {Types.VARCHAR};
   */
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /**
   * @return getEnumerationValueEditor().getEnumerationValue();
   */
  public final Class<?> returnedClass() {
    return getEnumerationValueEditor().getValueType();
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
   *         EnumerationValue implements ImmutableValue
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
  public final Object nullSafeGet(final ResultSet resultSet,
                                  final String[] names,
                                  final Object owner)
          throws HibernateException, SQLException {
    Object result = null;
    String tag = (String)Hibernate.STRING.nullSafeGet(resultSet, names[0]);
    if (!(resultSet.wasNull())) {
      getEnumerationValueEditor().setAsText(tag);
      result = getEnumerationValueEditor().getValue();
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
   *          && statement.setString(index, getEnumerationValueEditor().getAsText());
   */
  public final void nullSafeSet(final PreparedStatement statement,
                                final Object value,
                                final int index)
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
      getEnumerationValueEditor().setValue(value);
      statement.setString(index,
                          getEnumerationValueEditor().getAsText());
    }
  }

}
