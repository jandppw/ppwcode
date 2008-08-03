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

package org.ppwcode.util.reflect_I;


import static java.lang.Character.isLowerCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.TypeHelpersTest.NON_LOCAL_TYPE_SUBJECTS;
import static org.ppwcode.util.reflect_I.TypeName.DOT_PATTERN;
import static org.ppwcode.util.reflect_I.TypeName.EMPTY;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;


public class TypeNameTest {

  public final static LinkedList<TypeName> EXISTING_SUBJECTS = new LinkedList<TypeName>();
  static {
    for (Class<?> c : NON_LOCAL_TYPE_SUBJECTS) {
      EXISTING_SUBJECTS.add(new TypeName(c));
    }
  }

  public final static LinkedList<String> NON_EXISTING_FQTNS = new LinkedList<String>();
  static {
    NON_EXISTING_FQTNS.add("DefaultPackageClassDoesntExist");
    NON_EXISTING_FQTNS.add("com.mycompany.doesntExist.AndThenAClass");
    NON_EXISTING_FQTNS.add("com.mycompany.doesntExist.AndThenAClass.AndANestedClass");
  }

  public final static LinkedList<TypeName> NON_EXISTING_SUBJECTS = new LinkedList<TypeName>();
  static {
    for (String s : NON_EXISTING_FQTNS) {
      NON_EXISTING_SUBJECTS.add(new TypeName(s));
    }
  }

  public final static LinkedList<TypeName> SUBJECTS = new LinkedList<TypeName>();
  static {
    SUBJECTS.addAll(EXISTING_SUBJECTS);
    SUBJECTS.addAll(NON_EXISTING_SUBJECTS);
  }

  private void validateTypeInvariants(TypeName tn) {
    assertNotNull(tn.getPackageName());
    assertNotNull(tn.getEnclosingTypeNames());
    assertFalse(tn.getEnclosingTypeNames().contains(null));
    for (TypeName enclosing : tn.getEnclosingTypeNames()) {
      assertEquals(tn.getPackageName(), enclosing.getPackageName());
    }
//    System.out.println(tn);
//    System.out.println(tn.getEnclosingTypeNames());
//    System.out.println();
    enclosingRegression(tn.getEnclosingTypeNames());
    assertNotNull(tn.getSimpleName());
    assertFalse(tn.getSimpleName().equals(""));
  }

  private void enclosingRegression(List<TypeName> enclosingTypeNames) {
    for (int i = enclosingTypeNames.size() - 1; i >= 0; i--) {
      TypeName current = enclosingTypeNames.get(i);
      if (i > 0) {
        List<TypeName> previous = enclosingTypeNames.subList(0, i);
//        System.out.println(current);
//        System.out.println(current.getEnclosingTypeNames());
//        System.out.println(previous);
//        System.out.println();
        assertEquals(previous, current.getEnclosingTypeNames());
        enclosingRegression(current.getEnclosingTypeNames());
      }
      else { // i == 0
        assertTrue(current.getEnclosingTypeNames().isEmpty());
      }
    }
  }

  @Test
  public void testHashCode() {
    for (TypeName tn : SUBJECTS) {
      tn.hashCode();
      validateTypeInvariants(tn);
    }
  }

  @Test
  public void testTypeNameString1() {
    for (Class<?> type : NON_LOCAL_TYPE_SUBJECTS) {
      testTypeNameString(type.getCanonicalName());
    }
    for (String s : NON_EXISTING_FQTNS) {
      testTypeNameString(s);
    }
  }

  public void testTypeNameString(String fqtn) {
    TypeName subject = new TypeName(fqtn);
    assertTrue(fqtn.startsWith(subject.getPackageName()));
    for (String part : subject.getPackageName().split(DOT_PATTERN)) {
      assertTrue(part.equals("") || isLowerCase(part.charAt(0)));
    }
    assertTrue((! subject.getPackageName().equals("")) ? fqtn.charAt(subject.getPackageName().length()) == '.' : true);
    assertTrue((! subject.getPackageName().equals("")) ? ! isLowerCase(fqtn.charAt(subject.getPackageName().length() + 1)) : true);
    // MUDO enclosing types
    String[] fqtnParts = fqtn.split(DOT_PATTERN);
    assertEquals(fqtnParts[fqtnParts.length - 1], subject.getSimpleName());
    validateTypeInvariants(subject);
  }

  @Test
  public void testTypeNameString2() {
    testTypeNameString2("this.is.not....a.GoodThing");
//    testTypeNameString2("this.is.not  . a .GoodThing");
  }

  public void testTypeNameString2(String s) {
    boolean failed = false;
    try {
      new TypeName(s);
      failed = true;
    }
    catch (AssertionError aErr) {
      // expected
    }
    if (failed) fail();
  }

  @Test
  public void testTypeNameStringListOfTypeNameString() {
    for (TypeName tn : SUBJECTS) {
      testTypeNameStringListOfTypeNameString(tn.getPackageName(), tn.getEnclosingTypeNames(), tn.getSimpleName());
    }
  }

  public void testTypeNameStringListOfTypeNameString(String packageName, List<TypeName> enclosing, String simpleName) {
    TypeName subject = new TypeName(packageName, enclosing, simpleName);
    assertEquals(packageName, subject.getPackageName());
    assertEquals(enclosing, subject.getEnclosingTypeNames());
    assertEquals(simpleName, subject.getSimpleName());
    validateTypeInvariants(subject);
  }

  @Test
  public void testTypeNameClassOfQ() {
    for (Class<?> c : NON_LOCAL_TYPE_SUBJECTS) {
      TypeName subject = new TypeName(c);
      assertEquals(c.getPackage() != null ? c.getPackage().getName() : EMPTY, subject.getPackageName());
      // MUDO test enclosing types
      assertEquals(c.getSimpleName(), subject.getSimpleName());
      validateTypeInvariants(subject);
    }
  }

  @Test
  public void testEqualsObject() {
    for (TypeName tn1 : SUBJECTS) {
      for (TypeName tn2 : SUBJECTS) {
        testEqualsObject(tn1, tn2);
      }
      testEqualsObject(tn1, new Object());
      testEqualsObject(tn1, null);
    }
  }

  public void testEqualsObject(TypeName tn, Object other) {
    boolean expected = (other != null) && (other instanceof TypeName) &&
                       tn.getPackageName().equals(((TypeName)other).getPackageName()) &&
                       tn.getSimpleName().equals(((TypeName)other).getSimpleName()) &&
                       tn.getEnclosingTypeNames().equals(((TypeName)other).getEnclosingTypeNames());
    boolean result = tn.equals(other);
    assertEquals(expected, result);
    validateTypeInvariants(tn);
    if (other instanceof TypeName) {
      validateTypeInvariants((TypeName)other);
    }
  }

  @Test
  public void testToString1() {
    for (Class<?> c : NON_LOCAL_TYPE_SUBJECTS) {
      TypeName tn = new TypeName(c);
      String result = tn.toString();
      assertEquals(c.getCanonicalName(), result);
      validateTypeInvariants(tn);
    }
  }

  @Test
  public void testToString2() {
    for (TypeName tn : NON_EXISTING_SUBJECTS) {
      tn.toString();
      validateTypeInvariants(tn);
    }
  }

  @Test
  public void testGetType1() {
    for (Class<?> c : NON_LOCAL_TYPE_SUBJECTS) {
      TypeName tn = new TypeName(c);
      Class<?> result = tn.getType();
      assertEquals(c, result);
      validateTypeInvariants(tn);
    }
  }

  @Test
  public void testGetType2() {
    for (TypeName tn : NON_EXISTING_SUBJECTS) {
      try {
        tn.getType();
        fail();
      }
      catch (AssertionError aErr) {
        // expected
      }
    }
  }

}

