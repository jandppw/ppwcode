/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;




public class StubEnumerationValueEditor extends AbstractEnumerationValueEditor {

  public static final String LABEL_DECORATION = "++LABEL++";

  public String getLabel() {
    return LABEL_DECORATION + getAsText() + LABEL_DECORATION;
  }

}

