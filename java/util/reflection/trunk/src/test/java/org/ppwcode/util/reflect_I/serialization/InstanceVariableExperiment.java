/*<license>
Copyright 2004 - $Date: 2008-07-31 01:17:04 +0200 (Thu, 31 Jul 2008) $ by PeopleWare n.v..

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

package org.ppwcode.util.reflect_I.serialization;


import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Assert;

import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.AbstractSubSubStubClass;
import org.ppwcode.util.reflect_I.teststubs.ExternalizableStub;
import org.ppwcode.util.reflect_I.teststubs.ExternalizableSubStub;
import org.ppwcode.util.reflect_I.teststubs.ExternalizableSubSubStub;


public class InstanceVariableExperiment {

  public final static String INSTANCE_VARIABLE_NAME = "$stubPropertyString";

  @Test
  public void experiment1() {
    Class<?> c = AbstractSubSubStubClass.class;
    while (c != null) {
      System.out.println();
      System.out.println(c);
      System.out.println("--------------------------------------");
      Field[] fs = c.getDeclaredFields();
      for (int i = 0; i < fs.length; i++) {
        Field field = fs[i];
        System.out.println(field);
      }
      c = c.getSuperclass();
    }
  }

  @Test
  public void experiment2() {
    Class<?> c = AbstractSubSubStubClass.class;
    List<Field> result = SerializationHelpers.fields(c);
    System.out.println(result);
  }

  public void filter(List<Field> l) {
    ListIterator<Field> li = l.listIterator();
    while (li.hasNext()) {
      Field f = li.next();
      if (! f.getName().equals(INSTANCE_VARIABLE_NAME)) {
        li.remove();
      }
    }
  }

  @Test
  public void experiment3() {
    Class<?> c = AbstractSubSubStubClass.class;
    List<Field> result = SerializationHelpers.fields(c);
    filter(result);
    System.out.println(result);
  }

  @Test
  public void experiment4() throws IllegalArgumentException, IllegalAccessException {
    AbstractSubSubStubClass subject = new AbstractSubSubStubClass() {

      public void stubMethodEpsilon() {
        // NOP
      }

      public void stubMethodGamma() {
        // NOP
      }

      public void stubMethodDelta() {
        // NOP
      }

      public void stubMethodBeta() {
        // NOP
      }

    };
    Class<?> c = AbstractSubSubStubClass.class;
    List<Field> result = SerializationHelpers.fields(c);
    filter(result);
    for (Field f : result) {
      System.out.println();
      System.out.println("field: " + f);
      System.out.println("field.getName(): " + f.getName());
      System.out.println("field.isAccessible(): " + f.isAccessible());
      System.out.println("field..getAnnotations(): " + f.getAnnotations());
      System.out.println("field.getDeclaringClass(): " + f.getDeclaringClass());
      try {
        System.out.print("field.get(subject): ");
        System.out.println(f.get(subject));
      }
      catch (IllegalAccessException iaExc) {
        System.out.println(iaExc);
      }
    }
  }

  @Test
  public void experiment5() throws IllegalArgumentException, IllegalAccessException {
    AbstractSubSubStubClass subject = new AbstractSubSubStubClass() {

      public void stubMethodEpsilon() {
        // NOP
      }

      public void stubMethodGamma() {
        // NOP
      }

      public void stubMethodDelta() {
        // NOP
      }

      public void stubMethodBeta() {
        // NOP
      }

    };
    Class<?> c = AbstractSubSubStubClass.class;
    List<Field> result = SerializationHelpers.fields(c);
    filter(result);
    for (Field f : result) {
      System.out.println();
      System.out.println("field: " + f);
      System.out.println("field.getName(): " + f.getName());
      System.out.println("field.isAccessible(): " + f.isAccessible());
      System.out.println("field..getAnnotations(): " + f.getAnnotations());
      System.out.println("field.getDeclaringClass(): " + f.getDeclaringClass());
      f.setAccessible(true);
      System.out.println("field.isAccessible(): " + f.isAccessible());
      try {
        System.out.print("field.get(subject): ");
        System.out.println(f.get(subject));
      }
      catch (IllegalAccessException iaExc) {
        System.out.println(iaExc);
      }
    }
  }

  public final static String FILE_NAME = InstanceVariableExperiment.class.getName() + ".ser";

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

