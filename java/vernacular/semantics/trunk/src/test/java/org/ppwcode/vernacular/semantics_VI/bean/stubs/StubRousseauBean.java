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

package org.ppwcode.vernacular.semantics_VI.bean.stubs;


import java.util.HashSet;
import java.util.Set;

import org.ppwcode.vernacular.semantics_VI.bean.AbstractRousseauBean;
import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;


public class StubRousseauBean extends AbstractRousseauBean implements NumberOfProperties {

  @Override
  public void normalize() {
    normalized = true;
  }

  @Override
  public CompoundPropertyException wildExceptions() {
    return wildExceptions;
  }

  public CompoundPropertyException wildExceptions = new CompoundPropertyException(this, null, null, null);
  {
    wildExceptions.addElementException(new PropertyException(this, "property1", null, null));
    wildExceptions.addElementException(new PropertyException(this, "property4", null, null));
  }

  public boolean normalized;





  private String $property1;

  private StubRousseauBean $property2;

  private int $property3;

  private StubRousseauBeanA $property4;

  private StubRousseauBeanB $property5;

  private Set<StubRousseauBeanA> $propertyA = new HashSet<StubRousseauBeanA>();


  public final String getProperty1() {
    return $property1;
  }


  public final void setProperty1(String property1) {
    $property1 = property1;
  }


  public final StubRousseauBean getProperty2() {
    return $property2;
  }


  public final void setProperty2(StubRousseauBean property2) {
    $property2 = property2;
  }


  public final int getProperty3() {
    return $property3;
  }


  public final void setProperty3(int property3) {
    $property3 = property3;
  }


  public final StubRousseauBeanA getProperty4() {
    return $property4;
  }


  public final void setProperty4(StubRousseauBeanA property4) {
    $property4 = property4;
  }


  public final StubRousseauBeanB getProperty5() {
    return $property5;
  }


  public final void setProperty5(StubRousseauBeanB property5) {
    $property5 = property5;
  }

  public final Set<StubRousseauBeanA> getPropertyA() {
    return $propertyA;
  }

  public final void addPropertyA(StubRousseauBeanA srb) {
    $propertyA.add(srb);
  }

  public int nrOfProperties() {
    return 6;
  }

  public int nrOfSimpleProperties() {
    return nrOfProperties() - 1;
  }

}

