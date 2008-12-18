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
import org.ppwcode.util.serialization_I.teststubsB.Delegate;
import org.ppwcode.util.serialization_I.teststubsB.ReplacementSerializableDiamondBottom;
import org.ppwcode.util.serialization_I.teststubsB.ReplacementSerializableDiamondLeft;
import org.ppwcode.util.serialization_I.teststubsB.ReplacementSerializableDiamondRight;
import org.ppwcode.util.serialization_I.teststubsB.ReplacementSerializableDiamondTop;
import org.ppwcode.util.serialization_I.teststubsB.ReplacementSerializableStub;
import org.ppwcode.util.serialization_I.teststubsB.ReplacementSerializableSubStub;
import org.ppwcode.util.serialization_I.teststubsB.ReplacementSerializableSubSubStub;


public class InstanceVariableReplacementSerializableExperiment {

  public final static String INSTANCE_VARIABLE_NAME = "$stubPropertyString";

  public final static String FILE_NAME = InstanceVariableReplacementSerializableExperiment.class.getName() + ".ser";

  @Test
  public void externalizableTestA() throws IOException, ClassNotFoundException {
    ReplacementSerializableStub subject = new ReplacementSerializableStub();
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
    ReplacementSerializableStub typedResult = (ReplacementSerializableStub)result;
    assertEquals(subject.getProperty1(), typedResult.getProperty1());
    assertEquals(subject.getProperty2(), typedResult.getProperty2());
    assertEquals(subject.getProperty1S(), typedResult.getProperty1S()); // default serializability: fails, because Super is not Serializable
  }

  @Test
  public void externalizableTestB() throws IOException, ClassNotFoundException {
    ReplacementSerializableSubStub subject = new ReplacementSerializableSubStub();
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
    ReplacementSerializableSubStub typedResult = (ReplacementSerializableSubStub)result;
    assertEquals(subject.getProperty1(), typedResult.getProperty1());
    assertEquals(subject.getProperty2(), typedResult.getProperty2());
    assertEquals(subject.getProperty1T(), typedResult.getProperty1T());
    assertEquals(subject.getProperty1S(), typedResult.getProperty1S()); // default serializability: fails, because Super is not Serializable
  }


  @Test
  public void externalizableTestC() throws IOException, ClassNotFoundException {
    ReplacementSerializableSubSubStub subject = new ReplacementSerializableSubSubStub();
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
    ReplacementSerializableSubSubStub typedResult = (ReplacementSerializableSubSubStub)result;
    assertEquals(subject.getProperty1(), typedResult.getProperty1());
    assertEquals(subject.getProperty2(), typedResult.getProperty2());
    assertEquals(subject.getProperty1T(), typedResult.getProperty1T());
    assertEquals(subject.getProperty1S(), typedResult.getProperty1S()); // default serializability: fails, because Super is not Serializable
    assertEquals(ReplacementSerializableSubSubStub.DEFAULT_PROPERTY_1_TT_VALUE, typedResult.getProperty1TT());
  }

  /**
   * Demonstrates the problem with loops discussed in the note to paragaph 3.7 in the serialization
   * specification (http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/input.html#5903).
   *
   * Thus: this serialization protocol fails with loops.
   * Can we fix this ourselfs?
   */
  @Test(expected = ClassCastException.class)
  public void externalizableTestD() throws IOException, ClassNotFoundException {
    File f = null;
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    try {
      ReplacementSerializableSubSubStub subject = new ReplacementSerializableSubSubStub();
      subject.setProperty1S("PROPERTY 1 SUPER");
      subject.setProperty1("PROPERTY 1");
      subject.setProperty2(new Date());
      subject.setProperty1T("PROPERTY 1 SUB");
      subject.setProperty1TT("PROPERTY 1 SUBSUB");

      Delegate d = new Delegate();
      d.setLoop(subject);
      subject.setDelegate(d);

      f = new File(FILE_NAME);
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
      fis = new FileInputStream(f);
      ois = new ObjectInputStream(fis);
      ois.readObject(); // fails !!
    }
    finally {
      ois.close();
      ois = null;
      fis.close();
      fis = null;
      f.delete();
      f = null;
    }
  }


  /**
   * Diamonds are still acyclic graphs. No loops, so no problems.
   */
  @Test
  public void externalizableTestE() throws IOException, ClassNotFoundException {
    ReplacementSerializableDiamondTop top = new ReplacementSerializableDiamondTop();
    ReplacementSerializableDiamondLeft left = new ReplacementSerializableDiamondLeft();
    left.setTop(top);
    ReplacementSerializableDiamondRight right = new ReplacementSerializableDiamondRight();
    right.setTop(top);
    ReplacementSerializableDiamondBottom bottom = new ReplacementSerializableDiamondBottom();
    bottom.setLeft(left);
    bottom.setRight(right);

    File f = new File(FILE_NAME);
    FileOutputStream fos = new FileOutputStream(f);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(bottom);
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
    ReplacementSerializableDiamondBottom typedResult = (ReplacementSerializableDiamondBottom)result;
    assertEquals(typedResult.getLeft().getTop(), typedResult.getRight().getTop());
  }
}

