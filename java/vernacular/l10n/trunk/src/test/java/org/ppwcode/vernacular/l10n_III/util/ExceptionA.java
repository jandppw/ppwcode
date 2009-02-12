/*<license>
Copyright 2009 - $Date: 2008-10-15 18:22:22 +0200 (Wed, 15 Oct 2008) $ by PeopleWare n.v..

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

package org.ppwcode.vernacular.l10n_III.util;

public class ExceptionA extends SuperException {

  private Object $origin = null;
  private String $property = null;

  public ExceptionA(Object origin, String property, String message, Throwable cause) {
    super(message, cause);
    $origin = origin;
    $property = property;
  }

  /**
   * @return the $origin
   */
  public Object getOrigin() {
    return $origin;
  }

  /**
   * @return the $property
   */
  public String getProperty() {
    return $property;
  }

  public Object getOriginType() {
    return getOrigin().getClass();
  }

}
