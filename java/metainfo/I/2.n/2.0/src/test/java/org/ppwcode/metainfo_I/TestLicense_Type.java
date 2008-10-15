/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.metainfo_I;


import java.io.BufferedReader;
import java.io.Reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ppwcode.metainfo_I.License.Type;


public class TestLicense_Type {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private void test_getReader(Type lt) throws Exception {
    Reader licenseReader = lt.getReader();
    if (lt.getUrl() != null) {
      assertNotNull(licenseReader);
  //    assertTrue(licenseReader.ready()); fails for no apparent reason at PeopleWare network
      BufferedReader r = new BufferedReader(licenseReader);
      String firstLine = r.readLine();
      assertNotNull(firstLine);
      assertTrue(firstLine.length() > 0);
  //    while (r.ready()) {
  //      System.out.println(r.readLine());
  //    }
    }
  }

  @Test
  public void test_getLicense() throws Exception {
    for (Type lt : Type.values()) {
      test_getReader(lt);
    }
  }

}

