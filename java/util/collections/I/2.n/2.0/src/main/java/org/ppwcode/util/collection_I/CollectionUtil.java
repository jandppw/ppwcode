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

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Utility methods for working with collections.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class CollectionUtil {

  /**
   * @pre s1 != null;
   * @pre s2 != null;
   * @result s1.containsAll(result) && s2.containsAll(s2)
   *         && foreach (E e) {s1.contains(e) && s2.contains(e) ? result.contains(e)};
   */
  public static <E> Set<E> intersection(Set<E> s1, Set<E> s2) {
    assert s1 != null;
    assert s2 != null;
    HashSet<E> intersection = new HashSet<E>(s1);
    intersection.retainAll(s2);
    return intersection;
  }

  /**
   * Based on java.util.Collections.EmptySet.
   */
  private static class EmptySet
      extends AbstractUnmodifiableSet<Object>
      implements Serializable, Set<Object> {

    @Override
    public final Iterator<Object> iterator() {
      return new AbstractUnmodifiableIterator() {
        public boolean hasNext() {
          return false;
        }
        public Object next() {
          throw new NoSuchElementException();
        }
      };
    }

    @Override
    public final int size() {
      return 0;
    }

    @Override
    public final boolean contains(Object obj) {
      return false;
    }

  }

  /**
   * Raw type empty sorted set. Use {@link #emptySortedSet()} for a generic, typed
   * equivalent.
   */
  @SuppressWarnings("unchecked")
  public final static SortedSet EMPTY_SORTED_SET = new EmptySortedSet();

  @SuppressWarnings("unchecked")
  public static <T> SortedSet<T> emptySortedSet() {
    return EMPTY_SORTED_SET;
  }

  private static class EmptySortedSet
      extends EmptySet
      implements Serializable, SortedSet<Object> {

    // Preserves singleton property
    private Object readResolve() {
      return EMPTY_SORTED_SET;
    }

    public Comparator<? super Object> comparator() {
      return null;
    }

    public Object first() throws NoSuchElementException {
      throw new NoSuchElementException();
    }

    public Object last() throws NoSuchElementException {
      throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    public SortedSet<Object> subSet(Object fromElement, Object toElement) {
      return EMPTY_SORTED_SET;
    }

    @SuppressWarnings("unchecked")
    public SortedSet<Object> headSet(Object toElement) {
      return EMPTY_SORTED_SET;
    }

    @SuppressWarnings("unchecked")
    public SortedSet<Object> tailSet(Object fromElement) {
      return EMPTY_SORTED_SET;
    }

  }

  /**
   * Raw type empty ordered set. Use {@link #emptyOrderedSet()} for a generic, typed
   * equivalent.
   */
  private final static EmptyOrderedSet EMPTY_ORDERED_SET_TYPED = new EmptyOrderedSet();

  /**
   * Raw type empty ordered set. Use {@link #emptyOrderedSet()} for a generic, typed
   * equivalent.
   */
  @SuppressWarnings("unchecked")
  public final static OrderedSet EMPTY_ORDERED_SET = EMPTY_ORDERED_SET_TYPED;

  @SuppressWarnings("unchecked")
  public static <T> OrderedSet<T> emptyOrderedSet() {
    return EMPTY_ORDERED_SET;
  }

  /**
   * Based on java.util.Collections.EmptySet.
   */
  private static class EmptyOrderedSet
      extends EmptySet
      implements Serializable, OrderedSet<Object> {

    // Preserves singleton property
    private Object readResolve() {
      return EMPTY_SORTED_SET;
    }

    public boolean add(int index, Object object) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

    public List<? extends Object> asList() {
      return Collections.emptyList();
    }

    public Object get(int index) throws IndexOutOfBoundsException {
      throw new IndexOutOfBoundsException();
    }

    public int indexOf(Object object) {
      return -1;
    }

    public Object remove(int index) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

    @Override
    public EmptyOrderedSet clone() {
      return EMPTY_ORDERED_SET_TYPED;
    }

  }

  public static <T> OrderedSet<T> unmodifiableOrderedSet(OrderedSet<T> ss) throws NullPointerException {
    if (ss == null) {
      throw new NullPointerException();
    }
    return new UnmodifiableOrderedSet<T>(ss);
  }

}

