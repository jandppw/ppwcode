/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.util.test.contract;


import static org.ppwcode.util.reflect_I.InstanceHelpers.newInstance;
import static org.ppwcode.util.reflect_I.TypeHelpers.directSuperTypes;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ppwcode.util.reflect_I.TypeName;


public abstract class Contract<_Subject_> {

  protected Contract(Class<_Subject_> subjectClass) {
    assert subjectClass != null;
    $subjectClass = subjectClass;
    $superContracts = initDirectSuperContracts($subjectClass);
  }

  public final Class<_Subject_> getSubjectClass() {
    return $subjectClass;
  }

  private Class<_Subject_> $subjectClass;

  public final Map<Class<? super _Subject_>, Contract<? super _Subject_>> getDirectSuperContracts() {
    return $superContracts;
  }

  private final Map<Class<? super _Subject_>, Contract<? super _Subject_>> $superContracts;

  private static <_Subject_> Map<Class<? super _Subject_>, Contract<? super _Subject_>> initDirectSuperContracts(Class<_Subject_> subjectClass) {
    Set<Class<? super _Subject_>> directSuperTypes = directSuperTypes(subjectClass);
    Map<Class<? super _Subject_>, Contract<? super _Subject_>> result = new HashMap<Class<? super _Subject_>, Contract<? super _Subject_>>();
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

  public final static String CONTRACT_CLASS_NAME_PREFIX = "_Contract_";

  @SuppressWarnings("unchecked")
  public static <_Subject_> Contract<_Subject_> contractFor(Class<_Subject_> type) throws NoSuchContractException {
    preArgumentNotNull(type, "c");
    TypeName typeTn = new TypeName(type);
    TypeName contractTn = new TypeName(typeTn.getPackageName(), typeTn.getEnclosingTypeNames(), CONTRACT_CLASS_NAME_PREFIX + typeTn.getSimpleName());
    Class<?> contractT = null;
    try {
      contractT = contractTn.getType();
    }
    catch (AssertionError aErr) { // MUDO not good; an assertion error should not be caught: does this method need exceptions?
      throw new NoSuchContractException(type, aErr);
    }
    @SuppressWarnings("unchecked")
    Class<Contract<_Subject_>> contractTcast = (Class<Contract<_Subject_>>)contractT;
    Contract<_Subject_> result = newInstance(contractTcast);
    return result;
//    try {
//      return (Contract<_Subject_>)instantiatePrefixed(null, "Contract", type.toString());
//    }
//    catch (AssertionError aErr) { // MUDO not good; an assertion error should not be caught: does this method need exceptions?
//      throw new NoSuchContractException(type, aErr);
//    }
  }

  public void assertInvariants(_Subject_ subject) {
    for (Contract<? super _Subject_> superContract : getDirectSuperContracts().values()) {
      superContract.assertInvariants(subject);
    }
  }

}

