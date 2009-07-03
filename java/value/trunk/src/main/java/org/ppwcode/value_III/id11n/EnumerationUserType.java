package org.ppwcode.value_III.id11n;

import static java.sql.Types.VARCHAR;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.reflect_I.InstanceHelpers;
import org.ppwcode.vernacular.value_III.EnumerationValue;
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueUserType;

/**
 * <p>
 * Hibernate 3 user type to store and retrieve {@link EnumerationValue} instances.
 * </p>
 * <p>
 * {@link EnumerationValue} instances are stored in 2 columns of type VARCHAR. The first contains the FQCN of the actual
 * identifier class to which the instance belongs. The second column contains the identifier. Both are limited to 255
 * characters.
 * </p>
 * 
 * @author David Van Keer
 * @author PeopleWare NV
 */
@Copyright("2008 - $Date: 2008-11-27 18:07:26 +0100 (Thu, 27 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3748 $", date = "$Date: 2008-11-27 18:07:26 +0100 (Thu, 27 Nov 2008) $")
public class EnumerationUserType extends AbstractImmutableValueUserType {

  /**
   * Which type we return is defined by a parameter set on the definition of this user type. This should be a subtype of
   * {@link EnumerationValue}.
   */
  @Override
  public Class<? extends EnumerationValue> returnedClass() {
    return EnumerationValue.class;
  }

  public final static int[] SQL_TYPES = {
    VARCHAR,
    VARCHAR
  };

  /**
   * We store the data of {@link EnumerationValue EnumerationValues} in 2 VARCHAR columns.
   */
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
    Hibernate.CLASS.set(st, value, index);
    Hibernate.STRING.set(st, value, index + 1);
  }

  @SuppressWarnings("unchecked")
  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    EnumerationValue result = null;
    Class<? extends EnumerationValue> clazz = (Class<? extends EnumerationValue>) Hibernate.CLASS.get(rs, names[0]);
    String discriminator = (String) Hibernate.STRING.get(rs, names[1]);
    try {
      result = InstanceHelpers.robustNewInstance(clazz, discriminator);
    } catch (AssertionError aErr) {
      throw new HibernateException("Could not load type " + clazz + " or could not create instance of that type", aErr
          .getCause());
    } catch (InvocationTargetException exc) {
      throw new HibernateException(
          "Creation of enumeration of type " + clazz + " failed with an application exception", exc);
    }

    return result;
  }

}
