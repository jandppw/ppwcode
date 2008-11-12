/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.util.reflect_I;


import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.util.reflect_I.FieldHelpers.field;
import static org.ppwcode.util.reflect_I.FieldHelpers.fieldValue;
import static org.ppwcode.util.reflect_I.FieldHelpers.fields;
import static org.ppwcode.util.reflect_I.FieldHelpers.instanceFields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.ppwcode.util.reflect_I.teststubs.AbstractSubSubStubClass;
import org.ppwcode.util.reflect_I.teststubs.StubClass;


public class FieldHelpersTest {

  public final static String INSTANCE_VARIABLE_NAME = "$stubPropertyString";

//  @Test
//  public void experiment1() {
//    Class<?> c = AbstractSubSubStubClass.class;
//    while (c != null) {
//      System.out.println();
//      System.out.println(c);
//      System.out.println("--------------------------------------");
//      Field[] fs = c.getDeclaredFields();
//      for (int i = 0; i < fs.length; i++) {
//        Field field = fs[i];
//        System.out.println(field);
//      }
//      c = c.getSuperclass();
//    }
//  }

//  @Test
//  public void experiment2() {
//    Class<?> c = AbstractSubSubStubClass.class;
//    List<Field> result = fields(c);
//    System.out.println(result);
//  }

//  public void filter(List<Field> l) {
//    ListIterator<Field> li = l.listIterator();
//    while (li.hasNext()) {
//      Field f = li.next();
//      if (! f.getName().equals(INSTANCE_VARIABLE_NAME)) {
//        li.remove();
//      }
//    }
//  }

//  @Test
//  public void experiment3() {
//    Class<?> c = AbstractSubSubStubClass.class;
//    List<Field> result = fields(c);
//    filter(result);
//    System.out.println(result);
//  }

//  @Test
//  public void experiment4() throws IllegalArgumentException, IllegalAccessException {
//    AbstractSubSubStubClass subject = new AbstractSubSubStubClass() {
//
//      public void stubMethodEpsilon() {
//        // NOP
//      }
//
//      public void stubMethodGamma() {
//        // NOP
//      }
//
//      public void stubMethodDelta() {
//        // NOP
//      }
//
//      public void stubMethodBeta() {
//        // NOP
//      }
//
//    };
//    Class<?> c = AbstractSubSubStubClass.class;
//    List<Field> result = fields(c);
//    filter(result);
//    for (Field f : result) {
//      System.out.println();
//      System.out.println("field: " + f);
//      System.out.println("field.getName(): " + f.getName());
//      System.out.println("field.isAccessible(): " + f.isAccessible());
//      System.out.println("field..getAnnotations(): " + f.getAnnotations());
//      System.out.println("field.getDeclaringClass(): " + f.getDeclaringClass());
//      try {
//        System.out.print("field.get(subject): ");
//        System.out.println(f.get(subject));
//      }
//      catch (IllegalAccessException iaExc) {
//        System.out.println(iaExc);
//      }
//    }
//  }

//  @Test
//  public void experiment5() throws IllegalArgumentException, IllegalAccessException {
//    AbstractSubSubStubClass subject = new AbstractSubSubStubClass() {
//
//      public void stubMethodEpsilon() {
//        // NOP
//      }
//
//      public void stubMethodGamma() {
//        // NOP
//      }
//
//      public void stubMethodDelta() {
//        // NOP
//      }
//
//      public void stubMethodBeta() {
//        // NOP
//      }
//
//    };
//    Class<?> c = AbstractSubSubStubClass.class;
//    List<Field> result = fields(c);
//    filter(result);
//    for (Field f : result) {
//      System.out.println();
//      System.out.println("field: " + f);
//      System.out.println("field.getName(): " + f.getName());
//      System.out.println("field.isAccessible(): " + f.isAccessible());
//      System.out.println("field..getAnnotations(): " + f.getAnnotations());
//      System.out.println("field.getDeclaringClass(): " + f.getDeclaringClass());
//      f.setAccessible(true);
//      System.out.println("field.isAccessible(): " + f.isAccessible());
//      try {
//        System.out.print("field.get(subject): ");
//        System.out.println(f.get(subject));
//      }
//      catch (IllegalAccessException iaExc) {
//        System.out.println(iaExc);
//      }
//    }
//  }

  public class ASSSC extends AbstractSubSubStubClass {

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

  }

  @Test
  public void testField_Class_String() {
    Field result = field(ASSSC.class, INSTANCE_VARIABLE_NAME);
    assertNotNull(result);
    assertEquals(StubClass.class, result.getDeclaringClass());
    assertEquals(String.class, result.getType());
    assertEquals(INSTANCE_VARIABLE_NAME, result.getName());
  }

  @Test
  public void testFields_Class1() {
    List<Field> result = fields(ASSSC.class);
    Field[] directFields = ASSSC.class.getDeclaredFields();
    int splitPoint = result.size() - directFields.length;
    List<Field> endOfResult = result.subList(splitPoint, result.size());
    List<Field> directFieldsList = asList(directFields);
    assertEquals(directFieldsList, endOfResult);
    assertEquals(result.subList(0, splitPoint), fields(ASSSC.class.getSuperclass()));
  }

  @Test
  public void testFields_Class2() {
    List<Field> result = fields(Object.class);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testInstanceFields_Class1() {
    List<Field> result = instanceFields(ASSSC.class);
    List<Field> fields = fields(ASSSC.class);
    List<Field> expected = new ArrayList<Field>(result.size());
    for (Field f : fields) {
      if (! Modifier.isStatic(f.getModifiers())) {
        expected.add(f);
      }
    }
    assertEquals(expected, result);
  }

  @Test
  public void testInstanceFields_Class2() {
    List<Field> result = instanceFields(Object.class);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testFieldValue_Object_Field() {
    ASSSC subject = new ASSSC();
    Field f = field(ASSSC.class, INSTANCE_VARIABLE_NAME);
    Object result = fieldValue(subject, f);
    assertEquals(StubClass.STUB_PROPERTY_STRING_VALUE, result);
  }

  @Test
  public void testFieldValue_Object_Field_Class1() {
    ASSSC subject = new ASSSC();
    Field f = field(ASSSC.class, INSTANCE_VARIABLE_NAME);
    String result = fieldValue(subject, f, String.class);
    assertEquals(StubClass.STUB_PROPERTY_STRING_VALUE, result);
  }

  @Test(expected = ClassCastException.class)
  public void testFieldValue_Object_Field_Class2() {
    ASSSC subject = new ASSSC();
    Field f = field(ASSSC.class, INSTANCE_VARIABLE_NAME);
    fieldValue(subject, f, Integer.class);
  }

}

