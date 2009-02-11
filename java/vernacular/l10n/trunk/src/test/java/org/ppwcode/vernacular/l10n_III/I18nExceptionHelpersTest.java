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

package org.ppwcode.vernacular.l10n_III;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class I18nExceptionHelpersTest {

  //
  //  test I18nExceptionHelpersTest.processElement
  //

  @Test
  public void testProcessElement1() {
    Exception e = new Exception("TEST");
    assertEquals("TEST", I18nExceptionHelpers.processElement("message", e));
  }

  @Test
  public void testProcessElement2() {
    Exception inner = new Exception("INNER");
    Exception outer = new Exception("OUTER", inner);
    assertEquals("OUTER", I18nExceptionHelpers.processElement("message", outer));
    assertEquals("INNER", I18nExceptionHelpers.processElement("cause.message", outer));
  }

  @Test
  public void testProcessElement3() {
    Exception inner = new Exception("INNER");
    Exception inter = new Exception("INTER", inner);
    Exception outer = new Exception("OUTER", inter);
    assertEquals("OUTER", I18nExceptionHelpers.processElement("message", outer));
    assertEquals("INTER", I18nExceptionHelpers.processElement("cause.message", outer));
    assertEquals("INNER", I18nExceptionHelpers.processElement("cause.cause.message", outer));
  }

  @Test
  public void testProcessElement4() {
    Exception inner = new Exception("INNER");
    Exception outer = new Exception("OUTER", inner);
    assertEquals("OUTER", I18nExceptionHelpers.processElement("message", outer));
    assertEquals("INNER", I18nExceptionHelpers.processElement("cause.message", outer));
    assertEquals("null", I18nExceptionHelpers.processElement("cause.cause.message", outer));
  }

}
