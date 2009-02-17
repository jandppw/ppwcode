/*<license>
Copyright 2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $ by the authors mentioned below.

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

package org.ppwcode.util.collection_I.algebra;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.collection_I.AbstractUnmodifiableSet;
import org.ppwcode.util.smallfries_I.Filter;


/**
 * <p>An unmodifiable {@link Set} wrapper, that only presents
 *   the elements that pass a criterion.</p>
 *
 * @author Jan Dockx
 *
 * @invar getFilter() != null;
 * @invar ! containsValue(null);
 */
@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public class FilteredSet<E> extends AbstractUnmodifiableSet<E> {

  /**
   * @pre wrapped != null;
   * @pre filter != null;
   * @post getFilter() == filter;
   */
  public FilteredSet(Set<E> wrapped, Filter<E> filter) {
    assert wrapped != null;
    assert filter != null;
    $wrapped = wrapped;
    $filter = filter;
  }

  /**
   * @invar $wrapped != null;
   */
  private Set<E> $wrapped;



  /*<property name="filter">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Filter<E> getFilter() {
    return $filter;
  }

  /**
   * @invar $filter != null;
   */
  private final Filter<E> $filter;

  /*</property>*/



  /*<section name="filtering">*/
  //-----------------------------------------------------------------

  @Override
  @SuppressWarnings("unchecked")
  public final boolean contains(Object o) throws ClassCastException {
    return $wrapped.contains(o) &&
           $filter.filter((E)o); // ClassCastException
  }

  @Override
  public final boolean containsAll(Collection<?> c) throws ClassCastException {
    if (! $wrapped.containsAll(c)) {
      return false;
    }
    else {
      @SuppressWarnings("unchecked")
      Collection<E> castC = (Collection<E>)c; // ClassCastException
      for (E e : castC) {
        if (! $filter.filter(e)) {
          return false;
        }
      }
      return true;
    }
  }

  @Override
  public final boolean isEmpty() {
    if ($wrapped.isEmpty()) {
      return true;
    }
    else {
      for (E e : $wrapped) {
        if ($filter.filter(e)) {
          return false;
        }
      }
      return true;
    }
  }

  @Override
  public Iterator<E> iterator() {
    return new AbstractUnmodifiableIterator() {

      Iterator<E> $wrappedIterator = $wrapped.iterator();

      E $next;

      {
        proceed();
      }

      public final boolean hasNext() {
        return $next != null;
      }

      public final E next() {
        E next = $next;
        proceed();
        return next;
      }

      private void proceed() {
        $next = null;
        while (($next == null) && $wrappedIterator.hasNext()) {
          $next = $wrappedIterator.next();
          if (! $filter.filter($next)) {
            $next = null;
          }
        }
      }

    };
  }

  @Override
  public int size() {
    int count = 0;
    for (E e : $wrapped) {
      if ($filter.filter(e)) {
        count++;
      }
    }
    return count;
  }

  @Override
  public Object[] toArray() {
    return toSet().toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return toSet().toArray(a);
  }

  public Set<E> toSet() {
    Set<E> result = new HashSet<E>();
    for (E e : $wrapped) {
      if ($filter.filter(e)) {
        result.add(e);
      }
    }
    return result;
  }

  /*</section>*/

}