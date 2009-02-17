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
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.collection_I.AbstractUnmodifiableCollection;
import org.ppwcode.util.smallfries_I.Filter;


/**
 * <p>An unmodifiable {@link Map} wrapper, that only presents
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
public class FilteredMap<K, V> implements Map<K, V> {

  /**
   * @pre wrapped != null;
   * @pre filter != null;
   * @pre ! wrapped.containsValue(null);
   * @post getFilter() == filter;
   */
  public FilteredMap(Map<K, V> wrapped, MapFilter<K, V> filter) {
    assert wrapped != null;
    assert filter != null;
    $wrapped = wrapped;
    $filter = filter;
    $entrySet = new FilteredSet<Map.Entry<K, V>>($wrapped.entrySet(), $entrySetFilter);
  }

  /**
   * @invar $wrapped != null;
   */
  private Map<K, V> $wrapped;



  /*<property name="filter">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final MapFilter<K, V> getFilter() {
    return $filter;
  }

  /**
   * @invar $filter != null;
   */
  private final MapFilter<K, V> $filter;

  /*</property>*/



  /*<section name="filtering">*/
  //-----------------------------------------------------------------

  public V get(Object key) {
    try {
      @SuppressWarnings("unchecked")
      K kKey = (K)key; // ClassCastException
      if (key == null) {
        return null;
      }
      V value = $wrapped.get(key);
      if (value == null) {
        return null;
      }
      if ($filter.filter(kKey, value)) {
        return value;
      }
      else {
        return null;
      }
    }
    catch (ClassCastException ccExc) {
      return null; // key is not an instance of K
    }
  }

  public final Set<Map.Entry<K, V>> entrySet() {
    return $entrySet;
  }

  private final Filter<Map.Entry<K, V>> $entrySetFilter = new Filter<Entry<K,V>>() {

    public boolean filter(java.util.Map.Entry<K, V> entry) {
      return $filter.filter(entry.getKey(), entry.getValue());
    }

  };

  private final Set<Map.Entry<K, V>> $entrySet;

  public final boolean isEmpty() {
    return $entrySet.isEmpty(); // entry set isEmpty is performant
  }

  public int size() {
    return $entrySet.size(); // entry set size is performant
  }

  private abstract class KeyValueCollection<E, C extends Collection<E>> extends AbstractUnmodifiableCollection<E> {

    protected abstract E getThing(Map.Entry<K, V> entry);

    protected abstract C createCollection();

    // containsAll: default implementation

    @Override
    public final boolean isEmpty() {
      return FilteredMap.this.isEmpty();
    }

    @Override
    public final int size() {
      return FilteredMap.this.size();
    }

    public C toCollection() {
      C result = createCollection();
      for (Map.Entry<K, V> entry : $wrapped.entrySet()) {
        if ($filter.filter(entry.getKey(), entry.getValue())) {
          result.add(getThing(entry));
        }
      }
      return result;
    }

    @Override
    public Object[] toArray() {
      return toCollection().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
      return toCollection().toArray(a);
    }

    @Override
    public final Iterator<E> iterator() {
      return new AbstractUnmodifiableIterator() {

        Iterator<Map.Entry<K, V>> $wrappedIterator = $wrapped.entrySet().iterator();

        Map.Entry<K, V> $next;

        {
          proceed();
        }

        public final boolean hasNext() {
          return $next != null;
        }

        public final E next() {
          E next = getThing($next);
          proceed();
          return next;
        }


        private void proceed() {
          $next = null;
          while (($next == null) && $wrappedIterator.hasNext()) {
            $next = $wrappedIterator.next();
            if (! $filter.filter($next.getKey(), $next.getValue())) {
              $next = null;
            }
          }
        }

      };
    }

  }

  private class KeySet extends KeyValueCollection<K, Set<K>> implements Set<K> {

    @Override
    protected K getThing(java.util.Map.Entry<K, V> entry) {
      return entry.getKey();
    }

    @Override
    protected final Set<K> createCollection() {
      return new HashSet<K>();
    }

    @Override
    public final boolean contains(Object o) throws ClassCastException {
      return containsKey(o);
    }

  }

  private Set<K> $keySet = new KeySet();

  public Set<K> keySet() {
    return $keySet;
  }

  private class ValueCollection extends KeyValueCollection<V, Collection<V>> {

    @Override
    protected V getThing(java.util.Map.Entry<K, V> entry) {
      return entry.getValue();
    }

    /* don't override implementation of contains value:
     * this is the most efficient possible, and other methods
     * depend on it
     */
    @Override
    public final boolean contains(Object o) throws ClassCastException {
      return super.contains(o);
    }

    @Override
    protected Collection<V> createCollection() {
      return new LinkedList<V>();
    }

  }

  private final Collection<V> $valueCollection = new ValueCollection();

  public Collection<V> values() {
    return $valueCollection;
  }

  public final boolean containsKey(Object key) {
    return get(key) != null;
  }

  public final boolean containsValue(Object value) {
    return $valueCollection.contains(value);
  }

  /*</section>*/



  /*<section name="modifications (unsupported)">*/
  //-----------------------------------------------------------------

  /**
   * @post false
   * @throws UnsupportedOperationException
   */
  public final void clear() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @post false
   * @throws UnsupportedOperationException
   */
  public final V put(K key, V value) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @post false
   * @throws UnsupportedOperationException
   */
  public final void putAll(Map<? extends K, ? extends V> t) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @post false
   * @throws UnsupportedOperationException
   */
  public final V remove(Object key) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /*</section>*/

}