/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;


import static org.ppwcode.util.reflect_I.InstanceHelpers.newInstance;
import static org.ppwcode.util.reflect_I.TypeHelpers.directSuperTypes;
import static org.ppwcode.util.reflect_I.TypeHelpers.instantiatePrefixed;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrors.preArgumentNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ppwcode.util.reflect_I.TypeHelpers;
import org.ppwcode.util.reflect_I.TypeName;


public abstract class _Contract_<_Subject_> {

  protected _Contract_(Class<_Subject_> subjectClass) {
    assert subjectClass != null;
    $subjectClass = subjectClass;
    $superContracts = initDirectSuperContracts($subjectClass);
  }

  public final Class<_Subject_> getSubjectClass() {
    return $subjectClass;
  }

  private Class<_Subject_> $subjectClass;

  public final Map<Class<? super _Subject_>, _Contract_<? super _Subject_>> getDirectSuperContracts() {
    return $superContracts;
  }

  private final Map<Class<? super _Subject_>, _Contract_<? super _Subject_>> $superContracts;

  private static <_Subject_> Map<Class<? super _Subject_>, _Contract_<? super _Subject_>> initDirectSuperContracts(Class<_Subject_> subjectClass) {
    Set<Class<? super _Subject_>> directSuperTypes = directSuperTypes(subjectClass);
    Map<Class<? super _Subject_>, _Contract_<? super _Subject_>> result = new HashMap<Class<? super _Subject_>, _Contract_<? super _Subject_>>();
    for (Class<? super _Subject_> directSuperType : directSuperTypes) {
      try {
        result.put(directSuperType, contractFor(directSuperType));
      }
      catch (NoSuchContractException nscExc) {
        // NOP
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <_Subject_> _Contract_<_Subject_> contractFor(Class<_Subject_> type) throws NoSuchContractException {
    preArgumentNotNull(type, "c");
    TypeName typeTn = new TypeName(type);
    TypeName contractTn = new TypeName(typeTn.getPackageName(), typeTn.getEnclosingTypeNames(), "_Contract_" + typeTn.getSimpleName());
    Class<?> contractT = null;
    try {
      contractT = contractTn.getType();
    }
    catch (AssertionError aErr) { // MUDO not good; an assertion error should not be caught: does this method need exceptions?
      throw new NoSuchContractException(type, aErr);
    }
    @SuppressWarnings("unchecked")
    Class<_Contract_<_Subject_>> contractTcast = (Class<_Contract_<_Subject_>>)contractT;
    _Contract_<_Subject_> result = newInstance(contractTcast);
    return result;
//    try {
//      return (_Contract_<_Subject_>)instantiatePrefixed(null, "_Contract_", type.toString());
//    }
//    catch (AssertionError aErr) { // MUDO not good; an assertion error should not be caught: does this method need exceptions?
//      throw new NoSuchContractException(type, aErr);
//    }
  }

  public void assertInvariants(_Subject_ subject) {
    for (_Contract_<? super _Subject_> superContract : getDirectSuperContracts().values()) {
      superContract.assertInvariants(subject);
    }
  }

}

