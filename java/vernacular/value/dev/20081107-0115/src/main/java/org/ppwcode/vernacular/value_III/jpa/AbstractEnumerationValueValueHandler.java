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

package org.ppwcode.vernacular.value_III.jpa;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.deadBranch;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.sql.Types;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.meta.JavaTypes;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.EnumerationValue;


/**
 * An OpenJPA {@link ValueHandler} for concrete {@link EnumerationValue} types, for which a
 * {@link PropertyEditor} has been defined.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$", date = "$Date$")
public abstract class AbstractEnumerationValueValueHandler extends AbstractValueHandler {

  /*<construction>*/
  //------------------------------------------------------------------

  protected AbstractEnumerationValueValueHandler(Class<?> valueType, int columnSize) {
    $valueType =  valueType;
    $columnSize = columnSize;
  }

  /*</construction>*/



  /*<property name="value type">*/
  //------------------------------------------------------------------

  public final Class<?> getValueType() {
    return $valueType;
  }

  private final Class<?> $valueType;
  /*</property>*/



  /*<property name="column size">*/
  //------------------------------------------------------------------

  public final int getColumnSize() {
    return $columnSize;
  }

  private final int $columnSize;

  /*</property>*/



  /*<property name="property editor">*/
  //------------------------------------------------------------------

  public final PropertyEditor getPropertyEditor() {
    PropertyEditor pe = PropertyEditorManager.findEditor(getValueType());
    if (pe == null) {
      deadBranch("no property editor found for " + getValueType() +
                 "; please register a property editor for this type with " +
                 "PropertyEditorManager.registerEditor(TARGET CLASS, EDITOR CLASS) " +
                 "or via another mechanism");
    }
    return pe;
  }

  /*</property>*/



  /*<section name="value handler mapping definition">*/
  //------------------------------------------------------------------

  public final Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
    Column col = new Column();
    col.setName(name);
    col.setType(Types.VARCHAR);
    col.setJavaType(JavaTypes.STRING);
    col.setSize($columnSize);
    return new Column[] {col};
  }

  /*</section>*/


  /*<section name="storage and retrieval using a property editor">*/
  //------------------------------------------------------------------

  @Override
  public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
    if (val == null) {
      return null;
    }
    try {
      Object typedVal = getValueType().cast(val);
      PropertyEditor pe = getPropertyEditor();
      pe.setValue(typedVal);
      return pe.getAsText();
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "trying to handle " + val + " with " + getClass().getName() +
                               ", but that can't handle that type; this value handler only " +
                               "handles instance of " + getValueType());
    }
    return null; // keep compiler happy
  }

  @Override
  public Object toObjectValue(ValueMapping vm, Object val) throws IllegalArgumentException {
    try {
      String stringVal = (String)val;
      if (stringVal == null) {
        return null;
      }
      PropertyEditor pe = getPropertyEditor();
      pe.setAsText(stringVal);
      return pe.getValue();
    }
    catch (ClassCastException exc) {
      unexpectedException(exc, "data received from database is not as expected: expected a String");
    }
    return null; // keep compiler happy
  }

  /*</section>*/

}
