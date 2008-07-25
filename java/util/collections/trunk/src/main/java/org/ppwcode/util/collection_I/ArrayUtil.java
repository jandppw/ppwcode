/*<license>
Copyright 2007 - $Date$ by the authors mentioned below.

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

package org.ppwcode.util.collection_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

/**
 * <p>General array utility methods.</p>
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class ArrayUtil {

  public static <_Base_> _Base_[] subArray(_Base_[] array, int fromInclusive, int toExclusive) {
    assert fromInclusive >= 0;
    assert toExclusive >= 0;
    assert fromInclusive <= toExclusive;
    int lenght = toExclusive - fromInclusive;
    @SuppressWarnings("unchecked") _Base_[] result =
        (_Base_[])Array.newInstance(array.getClass().getComponentType(), lenght);
    System.arraycopy(array, fromInclusive, result, 0, lenght);
    return result;
  }

  /**
   * @pre resultComponentType != null;
   */
  public static <_InBase_, _ResultBase_>
      _ResultBase_[] flatten2(_InBase_[] array,
                                      Class<_ResultBase_> resultComponentType) {
    assert resultComponentType != null;
    LinkedList<_ResultBase_> resultList = new LinkedList<_ResultBase_>();
    flattenHelper(array, resultList);
    @SuppressWarnings("unchecked") _ResultBase_[] result =
        (_ResultBase_[])Array.newInstance(resultComponentType, resultList.size());
    /* unchecked cast because Java API for array construction is not generic */
    resultList.toArray(result);
    return result;
  }

  /**
   * @pre resultComponentType != null;
   */
  public static <_ResultBase_> void flattenHelper(Object[] array,
                                                           LinkedList<_ResultBase_> acc) {
    for (Object firstLevel : array) {
      if ((firstLevel == null) || (! firstLevel.getClass().isArray())) {
        // firstLevel is the final level; it must me of tye _ResultComponentType_
        @SuppressWarnings("unchecked") _ResultBase_ simple = (_ResultBase_)firstLevel;
        // unchecked cast: it is ok, or pre's are violated
        acc.add(simple);
      }
      else {
        // firstLevel is an array; recursive
        flattenHelper((Object[])firstLevel, acc);
      }
    }
  }

  /**
   * {@link java.util.Arrays#asList} returns a dynamic list (changes in the array are seen in the
   * list, and vice versa. This does the same, but returns a new list, uncoupled with
   * the array. The result is unmodifiable.
   *
   * @result result.equals(Arrays.asList(a));
   * @throws NullPointerException
   *         a == null;
   */
  public static <T> List<T> asFreshList(T... a) throws NullPointerException {
    if (a == null) {
      throw new NullPointerException();
    }
    List<T> result = new ArrayList<T>(a.length);
    for (T t : a) {
      result.add(t);
    }
    return Collections.unmodifiableList(result);
  }

}