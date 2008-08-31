/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;



public enum StubEnum {

  ENUM_VALUE_1,
  ENUM_VALUE_2,
  ENUM_VALUE_3 {
    @Override
    public String toString() {
      return "enum value 3";
    }
  }

}

