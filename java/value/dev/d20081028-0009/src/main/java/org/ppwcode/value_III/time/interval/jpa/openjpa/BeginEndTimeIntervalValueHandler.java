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

package org.ppwcode.value_III.time.interval.jpa.openjpa;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.JavaTypes;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.value_III.time.interval.BeginEndTimeInterval;
import org.ppwcode.value_III.time.interval.IllegalIntervalException;


/**
 * A OpenJPA value handler for {@link BeginEndTimeInterval}. Begin and end are stored in 2
 * columns in the database as {@code TIMESTAMP}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class BeginEndTimeIntervalValueHandler implements ValueHandler {

  public final Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    return new Column[] {timestampColumn(name + "_begin"), timestampColumn(name + "_end")};
  }

  private Column timestampColumn(String name) {
    Column c = new Column();
    c.setName(name);
    c.setType(Types.TIMESTAMP);
    c.setJavaType(JavaTypes.DATE);
    return c;
  }

  public final boolean isVersionable(ValueMapping vm) {
    return false;
  }

  public final boolean objectValueRequiresLoad(ValueMapping vm) {
    return false;
  }

  public Object getResultArgument(ValueMapping vm) {
    return null;
  }


  public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    try {
      BeginEndTimeInterval beTi = (BeginEndTimeInterval)val;
      return new Date[] {beTi.getBegin(), beTi.getEnd()};
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " +
                          BeginEndTimeIntervalValueHandler.class.getName() + ", but that can't handle that type");
    }
    return null; // make compiler happy
  }

  public Object toObjectValue(ValueMapping vm, Object fromDb) {
    try {
      Object[] dates = (Date[])fromDb;
      Date begin = (Date)dates[0];
      Date end = (Date)dates[1];
      return new BeginEndTimeInterval(begin, end);
    }
    catch (NullPointerException exc) {
      unexpectedException(exc, "data received from database is not as expected: we can't deal with null");
    }
    catch (ArrayIndexOutOfBoundsException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected array of 2 values");
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected an array of 2 dates");
    }
    catch (IllegalIntervalException exc) {
      unexpectedException(exc, "data received from database did violate invariants for " + BeginEndTimeInterval.class);
    }
    return null; // make compiler happy
  }

  /**
   * Not used, since {@link #objectValueRequiresLoad(ValueMapping)} returns false.
   */
  public Object toObjectValue(ValueMapping arg0,
                              Object arg1,
                              OpenJPAStateManager arg2,
                              JDBCStore arg3,
                              JDBCFetchConfiguration arg4) throws SQLException {
    return null;
  }

}
