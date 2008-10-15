/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.util.reflect_I.serialization.teststubs;

import java.io.Serializable;



public class Delegate implements Serializable {

  public final ReplacementSerializableSuperStub getLoop() {
    return $loop;
  }

  public final void setLoop(ReplacementSerializableSuperStub loop) {
    $loop = loop;
  }

  private ReplacementSerializableSuperStub $loop;

}

