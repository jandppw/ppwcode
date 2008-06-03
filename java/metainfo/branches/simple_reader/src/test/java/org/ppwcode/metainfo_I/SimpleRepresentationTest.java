/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.metainfo_I;

import org.junit.Test;


public class SimpleRepresentationTest {

  @Test
  public void testCopyright1() {
    SimpleRepresentation.copyright(SimpleRepresentationTest.class, System.out);
    // nothing to test; we expect a notice of no information
  }

  @Test
  public void testCopyright2() {
    SimpleRepresentation.copyright(License.class, System.out);
    // nothing to test; we expect a copyright notice
  }

  @Test
  public void testLicense1() {
    SimpleRepresentation.license(SimpleRepresentationTest.class, System.out);
    // nothing to test; we expect a notice of no information
  }

  @Test
  public void testLicense2() {
    SimpleRepresentation.license(License.class, System.out);
    // nothing to test; we expect a notice of an apache 2 license
  }

}

