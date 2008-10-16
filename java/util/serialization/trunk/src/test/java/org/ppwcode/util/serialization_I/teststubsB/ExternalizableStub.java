/*<license>
Copyright 2008 - $Date: 2008-10-05 20:33:16 +0200 (Sun, 05 Oct 2008) $ by PeopleWare n.v..

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

package org.ppwcode.util.serialization_I.teststubsB;


import static org.ppwcode.util.reflect_I.FieldHelpers.fields;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import org.ppwcode.util.serialization_I.DoNotSerialize;


public class ExternalizableStub extends ExternalizableSuperStub implements Externalizable {

  public final String getProperty1() {
    return $property1;
  }

  public final void setProperty1(String property) {
    $property1 = property;
  }

  private String $property1;



  public final Date getProperty2() {
    return $property2;
  }

  public final void setProperty2(Date property2) {
    $property2 = property2;
  }

  private Date $property2;



  public void writeExternal(ObjectOutput out) throws IOException {
    List<Field> fields = fields(getClass());
    for (Field field : fields) {
      if (! Modifier.isStatic(field.getModifiers()) &&
          ! Modifier.isTransient(field.getModifiers()) &&
          ! field.isAnnotationPresent(DoNotSerialize.class)) {
        field.setAccessible(true);
        try {
          Object value = field.get(this);
          out.writeObject(value);
        }
        catch (IllegalArgumentException exc) {
          throw new IOException("problem getting field value for field " + field + ": " + exc.toString());
        }
        catch (IllegalAccessException exc) {
          throw new IOException("problem getting field value for field " + field + ": " + exc.toString());
        }
      }
    }
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    List<Field> fields = fields(getClass());
    for (Field field : fields) {
      if (! Modifier.isStatic(field.getModifiers()) &&
          ! Modifier.isTransient(field.getModifiers()) &&
          ! field.isAnnotationPresent(DoNotSerialize.class)) {
        try {
          Object value = in.readObject();
          field.setAccessible(true);
          field.set(this, value);
        }
        catch (IllegalArgumentException exc) {
          throw new IOException("problem setting field value for field " + field + ": " + exc.toString());
        }
        catch (IllegalAccessException exc) {
          throw new IOException("problem setting field value for field " + field + ": " + exc.toString());
        }
      }
    }
  }

}

