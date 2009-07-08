package org.ppwcode.value_III.location;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.reflect.InvocationTargetException;
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
import org.ppwcode.vernacular.value_III.hibernate3.AbstractImmutableValueCompositeUserType;

/**
 * <p>
 * Hibernate 3 user type to store and retrieve {@link PostalCode} instances.
 * </p>
 * <p>
 * {@link PostalCode} instances are stored in 3 columns of type VARCHAR. The first contains the FQCN of the actual
 * identifier class to which the instance belongs. The second column contains the identifier. The third column contains
 * the country. All are limited to 255 characters.
 * </p>
 * 
 * @author David Van Keer
 * @author PeopleWare NV
 */
@Copyright("2008 - $Date: 2009-06-25 09:44:06 +0200 (Thu, 25 Jun 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4400 $", date = "$Date: 2009-06-25 09:44:06 +0200 (Thu, 25 Jun 2009) $")
public class PostalCodeCompositeUserType extends AbstractImmutableValueCompositeUserType {

  /**
   * Which type we return is defined by a parameter set on the definition of this user type. This should be a subtype of
   * {@link PostalCode}.
   */
  @Override
  public Class<? extends PostalCode> returnedClass() {
    return PostalCode.class;
  }

  public Type[] getPropertyTypes() {
    return new Type[] {
      Hibernate.CLASS,
      Hibernate.STRING,
      Hibernate.STRING
    };
  }

  public final String[] getPropertyNames() {
    return new String[] {
      "$type",
      "$identifier",
      "$country",
    };
  }

  public Object getPropertyValue(Object obj, int prop) {
    try {
      PostalCode id = (PostalCode) obj;
      if (prop == 0) {
        return id.getClass();
      } else if (prop == 1) {
        return id.getIdentifier();
      } else if (prop == 2) {
        return id.getCountry().getValue();
      } else {
        throw new HibernateException("getPropertyValue with wrong index for " + returnedClass());
      }
    } catch (ClassCastException exc) {
      throw new HibernateException("cannot cast component into " + returnedClass(), exc);
    }
  }

  @SuppressWarnings("unchecked")
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
      throws HibernateException, SQLException {
    PostalCode result = null;
    Class clazz = null;
    String identifier = null;
    String countryKey = null;
    try {
      clazz = (Class) Hibernate.CLASS.nullSafeGet(rs, names[0], session, owner);
      identifier = (String) Hibernate.STRING.nullSafeGet(rs, names[1], session, owner);
      countryKey = (String) Hibernate.STRING.nullSafeGet(rs, names[2], session, owner);
      if (clazz == null && identifier == null && countryKey == null) {
        result = null;
      } else {
        Country country = Country.VALUES.get(countryKey);
        if (countryKey.equalsIgnoreCase("BE")) {
          result = (PostalCode) InstanceHelpers.robustNewInstance(clazz, identifier);
        } else {
          result = (PostalCode) InstanceHelpers.robustNewInstance(clazz, identifier, country);
        }
      }
      return result;
    } catch (ArrayIndexOutOfBoundsException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 3 values", exc);
    } catch (ClassCastException exc) {
      throw new HibernateException("data received from database is not as expected: expected array of 3 values", exc);
    } catch (InvocationTargetException exc) {
      throw new HibernateException("Creation of identifier of type " + PostalAddress.class
          + " failed with an application exception", exc);
    }
  }

  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
      throws HibernateException, SQLException {
    if (value != null && !returnedClass().isInstance(value)) {
      throw new HibernateException("this user type can only handle values of type "
          + returnedClass().getCanonicalName() + "; " + value.getClass().getCanonicalName() + " is not supported");
    } else if (value == null) {
      Hibernate.CLASS.nullSafeSet(st, null, index, session);
      Hibernate.STRING.nullSafeSet(st, null, index + 1, session);
      Hibernate.STRING.nullSafeSet(st, null, index + 2, session);
    } else {
      PostalCode id = (PostalCode) value;
      Hibernate.CLASS.nullSafeSet(st, id.getClass(), index, session);
      Hibernate.STRING.nullSafeSet(st, id.getIdentifier(), index + 1, session);
      Hibernate.STRING.nullSafeSet(st, id.getCountry().getValue(), index + 2, session);
    }
  }

}
