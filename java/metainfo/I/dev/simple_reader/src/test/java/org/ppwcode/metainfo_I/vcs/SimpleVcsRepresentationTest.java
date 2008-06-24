/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.metainfo_I.vcs;

import org.junit.Test;


public class SimpleVcsRepresentationTest {

  @Test
  public void testCvsInfo1() {
    SimpleVcsRepresentation.cvsInfo(SimpleVcsRepresentationTest.class, System.out);
    // nothing to test; we expect a notice of no information
  }

  @Test
  public void testCvsInfo2() {
    SimpleVcsRepresentation.cvsInfo(CvsInfo.class, System.out);
    // nothing to test; we expect cvs information
  }

  @Test
  public void testSvnInfo1() {
    SimpleVcsRepresentation.svnInfo(SimpleVcsRepresentationTest.class, System.out);
    // nothing to test; we expect a notice of no information
  }

  @Test
  public void testSvnInfo2() {
    SimpleVcsRepresentation.svnInfo(CvsInfo.class, System.out);
    // nothing to test; we expect svn information
  }


  @Test
  public void testVcsInfo1() {
    SimpleVcsRepresentation.vcsInfo(SimpleVcsRepresentationTest.class, System.out);
    // nothing to test; we expect a notice of no information
  }

  @Test
  public void testVcsInfo2() {
    SimpleVcsRepresentation.vcsInfo(CvsInfo.class, System.out);
    // nothing to test; we expect both svn and cvs information
  }

}

