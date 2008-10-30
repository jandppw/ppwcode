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

import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;


public class StubRousseauBeanA extends StubRousseauBean {

  private StubRousseauBean $property6;

  private StubRousseauBeanA $property7;

  private StubRousseauBean $propertyLoop;

  public final StubRousseauBean getProperty6() {
    return $property6;
  }

  public final void setProperty6(StubRousseauBean property6) {
    $property6 = property6;
  }

  public final StubRousseauBeanA getProperty7() {
    return $property7;
  }

  public final void setProperty7(StubRousseauBeanA property7) {
    $property7 = property7;
  }

  public final StubRousseauBean getPropertyLoop() {
    return $propertyLoop;
  }

  public final void setPropertyLoop(StubRousseauBean propertyLoop) {
    $propertyLoop = propertyLoop;
  }

  @Override
  public CompoundPropertyException wildExceptions() {
    CompoundPropertyException cpe = super.wildExceptions();
    cpe.addElementException(new PropertyException(this, "property6", null, null));
    return cpe;
  }

  @Override
  public int nrOfProperties() {
    return 8;
  }

}

