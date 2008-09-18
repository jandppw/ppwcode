/*<license>
  Copyright 2008, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.value_III;

import static org.ppwcode.util.reflect_I.InstanceHelpers.newInstance;
import static org.ppwcode.util.reflect_I.TypeHelpers.directSuperTypes;

import java.util.HashSet;
import java.util.Set;

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

  public final Set<_Contract_<? super _Subject_>> getDirectSuperContracts() {
    return $superContracts;
  }

  private final Set<_Contract_<? super _Subject_>> $superContracts;

  private static <_Subject_> Set<_Contract_<? super _Subject_>> initDirectSuperContracts(Class<_Subject_> subjectClass) {
    Set<Class<?>> directSuperTypes = directSuperTypes(subjectClass);
    Set<_Contract_<? super _Subject_>> result = new HashSet<_Contract_<? super _Subject_>>();
    for (Class<?> directSuperType : directSuperTypes) {
      TypeName dstTn = new TypeName(directSuperType);
      TypeName contractTn = new TypeName(dstTn.getPackageName(), dstTn.getEnclosingTypeNames(), "_Contract_" + dstTn.getSimpleName());
      Class<?> superFn = contractTn.getType();
      @SuppressWarnings("unchecked")
      Class<_Contract_<? super _Subject_>> superCn = (Class<_Contract_<? super _Subject_>>)superFn;
      _Contract_<? super _Subject_> superC = newInstance(superCn);
      result.add(superC);
    }
    return result;
  }

  public void assertInvariants(_Subject_ subject) {
    for (_Contract_<? super _Subject_> superContract : getDirectSuperContracts()) {
      superContract.assertInvariants(subject);
    }
  }

}

