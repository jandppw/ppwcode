package org.ppwcode.value_III.ext.java.util;

import static org.ppwcode.metainfo_I.License.Type.PROPRIETARY;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.meta.JavaTypes;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

/**
 * The valuehandler will store an {@link InternetAddress} in the database as
 * {@link String}.
 *
 * @author Peter Sinjan
 */
@Copyright("2008 - $Date$, AristA v.z.w.")
@License(PROPRIETARY)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class InternetAddressValueHandler extends AbstractValueHandler {

  private static final long serialVersionUID = -7946528814038849098L;

  /**
   * This function will map take care of the mapping in the database
   */
  public Column[] map(ValueMapping vm, String name, ColumnIO io,
      boolean adapt) {
    Column col = new Column();
    col.setName(name);
    col.setJavaType(JavaTypes.STRING);
    return new Column[]{ col };
  }

  /**
   * @see AbstractValueHandler#toDataStoreValue(ValueMapping, Object, JDBCStore)
   */
  @Override
  public Object toDataStoreValue(ValueMapping vm, Object val,
      JDBCStore store) {
      if (val == null || !(val instanceof InternetAddress))
          return null;
      InternetAddress storeObj = (InternetAddress) val;
      //Store the InternetAddress in the DB as String
      return storeObj.getAddress();
  }

  /**
   * @see AbstractValueHandler#toObjectValue(ValueMapping, Object)
   */
  @Override
  public Object toObjectValue(ValueMapping vm, Object val) {
    if (val == null)
      return null;
    InternetAddress ia = null;
    try {
      //make a new InternetAddress with the retrieved value as address
      ia = new InternetAddress((String) val);
      ia.validate();
    } catch (AddressException e) {
      //NOP
    }
    return ia;
  }

}
