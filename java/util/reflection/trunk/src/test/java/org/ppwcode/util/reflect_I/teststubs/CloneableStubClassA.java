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

package org.ppwcode.util.reflect_I.teststubs;


import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;


public class CloneableStubClassA implements Cloneable {

  public CloneableStubClassA() {
    // NOP
  }

  public CloneableStubClassA(StubClass sc) {
    $stubProperty = sc;
  }

  @Override
  public final CloneableStubClassA clone() {
    CloneableStubClassA result = null;
    try {
      result = (CloneableStubClassA)super.clone();
    }
    catch (CloneNotSupportedException exc) {
      unexpectedException(exc, "we are Cloneable");
    }
    return result;
  }

  public final static String STUB_PUBLIC_CONSTANT = "stub public constant";

  @SuppressWarnings("unused")
  private final static int STUB_PRIVATE_CONSTANT = 789;

  final static double STUB_PACKAGE_CONSTANT = 0.12345;

  protected final static Object STUB_PROTECTED_CONSTANT = new Object();

  public static Object STUB_NON_FINAL_CONSTANT = new Object();

  public final StubClass getStubProperty() {
    return $stubProperty;
  }

  public final void setStubProperty(StubClass stubProperty) {
    $stubProperty = stubProperty;
  }

  private StubClass $stubProperty; // initializing with a StubClass instance creates an infinite loop

}

