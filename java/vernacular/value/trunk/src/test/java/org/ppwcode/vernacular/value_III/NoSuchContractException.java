/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;

import org.ppwcode.vernacular.exception_II.ProgrammingErrors;



public class NoSuchContractException extends Exception {

  public NoSuchContractException(Class<?> subjectType, Throwable cause) {
    super(cause);
    ProgrammingErrors.preArgumentNotNull(subjectType, "subjectType");
    $subjectType = subjectType;
  }

  public Class<?> getSubjectType() {
    return $subjectType;
  }

  private final Class<?> $subjectType;

}

