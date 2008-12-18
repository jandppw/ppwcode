/*<license>
Copyright 2008 - $Date$ by PeopleWare n.v..

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


import static org.ppwcode.util.reflect_I.FieldHelpers.fields;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
import org.ppwcode.util.serialization_I.teststubsA.AbstractSubSubStubClass;


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
    List<Field> result = fields(c);
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
    List<Field> result = fields(c);
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
    List<Field> result = fields(c);
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
    List<Field> result = fields(c);
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

}

