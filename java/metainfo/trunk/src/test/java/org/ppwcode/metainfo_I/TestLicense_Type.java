/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
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
    assertNotNull(licenseReader);
    assertTrue(licenseReader.ready());
    BufferedReader r = new BufferedReader(licenseReader);
    String firstLine = r.readLine();
    assertNotNull(firstLine);
    assertTrue(firstLine.length() > 0);
//    while (r.ready()) {
//      System.out.println(r.readLine());
//    }
  }

  @Test
  public void test_getLicense() throws Exception {
    for (Type lt : Type.values()) {
      test_getReader(lt);
    }
  }

}

