/*<license>
Copyright 2008 - $Date: 2008-07-31 01:17:04 +0200 (Thu, 31 Jul 2008) $ by PeopleWare n.v..

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

package org.ppwcode.util.serialization_I;


import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.junit.Test;
import org.ppwcode.util.serialization_I.teststubsB.ExternalizableStub;
import org.ppwcode.util.serialization_I.teststubsB.ExternalizableSubStub;
import org.ppwcode.util.serialization_I.teststubsB.ExternalizableSubSubStub;


public class InstanceVariableExternalizableExperiment {

  public final static String INSTANCE_VARIABLE_NAME = "$stubPropertyString";

  public final static String FILE_NAME = InstanceVariableExternalizableExperiment.class.getName() + ".ser";

  @Test
  public void externalizableTestA() throws IOException, ClassNotFoundException {
    ExternalizableStub subject = new ExternalizableStub();
    subject.setProperty1S("PROPERTY 1 SUPER");
    subject.setProperty1("PROPERTY 1");
    subject.setProperty2(new Date());

    File f = new File(FILE_NAME);
    FileOutputStream fos = new FileOutputStream(f);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(subject);
    oos.flush();
    oos.close();
    oos = null;
    fos.close();
    fos.flush();
    fos = null;
    f = null;

    f = new File(FILE_NAME);
    FileInputStream fis = new FileInputStream(f);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Object result = ois.readObject();
    ois.close();
    ois = null;
    fis.close();
    fis = null;
    f.delete();
    f = null;

    System.out.println(result);
    ExternalizableStub typedResult = (ExternalizableStub)result;
    assertEquals(subject.getProperty1S(), typedResult.getProperty1S());
    assertEquals(subject.getProperty1(), typedResult.getProperty1());
    assertEquals(subject.getProperty2(), typedResult.getProperty2());
  }

  @Test
  public void externalizableTestB() throws IOException, ClassNotFoundException {
    ExternalizableSubStub subject = new ExternalizableSubStub();
    subject.setProperty1S("PROPERTY 1 SUPER");
    subject.setProperty1("PROPERTY 1");
    subject.setProperty2(new Date());
    subject.setProperty1T("PROPERTY 1 SUB");

    File f = new File(FILE_NAME);
    FileOutputStream fos = new FileOutputStream(f);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(subject);
    oos.flush();
    oos.close();
    oos = null;
    fos.close();
    fos.flush();
    fos = null;
    f = null;

    f = new File(FILE_NAME);
    FileInputStream fis = new FileInputStream(f);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Object result = ois.readObject();
    ois.close();
    ois = null;
    fis.close();
    fis = null;
    f.delete();
    f = null;

    System.out.println(result);
    ExternalizableSubStub typedResult = (ExternalizableSubStub)result;
    assertEquals(subject.getProperty1S(), typedResult.getProperty1S());
    assertEquals(subject.getProperty1(), typedResult.getProperty1());
    assertEquals(subject.getProperty2(), typedResult.getProperty2());
    assertEquals(subject.getProperty1T(), typedResult.getProperty1T());
  }


  @Test
  public void externalizableTestC() throws IOException, ClassNotFoundException {
    ExternalizableSubSubStub subject = new ExternalizableSubSubStub();
    subject.setProperty1S("PROPERTY 1 SUPER");
    subject.setProperty1("PROPERTY 1");
    subject.setProperty2(new Date());
    subject.setProperty1T("PROPERTY 1 SUB");
    subject.setProperty1TT("PROPERTY 1 SUBSUB");

    File f = new File(FILE_NAME);
    FileOutputStream fos = new FileOutputStream(f);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(subject);
    oos.flush();
    oos.close();
    oos = null;
    fos.close();
    fos.flush();
    fos = null;
    f = null;

    f = new File(FILE_NAME);
    FileInputStream fis = new FileInputStream(f);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Object result = ois.readObject();
    ois.close();
    ois = null;
    fis.close();
    fis = null;
    f.delete();
    f = null;

    System.out.println(result);
    ExternalizableSubSubStub typedResult = (ExternalizableSubSubStub)result;
    assertEquals(subject.getProperty1S(), typedResult.getProperty1S());
    assertEquals(subject.getProperty1(), typedResult.getProperty1());
    assertEquals(subject.getProperty2(), typedResult.getProperty2());
    assertEquals(subject.getProperty1T(), typedResult.getProperty1T());
    assertEquals(ExternalizableSubSubStub.DEFAULT_PROPERTY_1_TT_VALUE, typedResult.getProperty1TT());
  }


}

