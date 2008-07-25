/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>{@link java.util.Set} implementation tweaked at memory efficiency, with a preference
 *  for empty sets, singletons, and sets that change size slowly. The set does
 *  not accept {@code null} as an element.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class ArraySet<E extends Object> extends AbstractSet<E> implements Cloneable {

  private final static int INITIAL_SIZE = 1;

  private final static int GROWTH = 2;

  @Override
  public final int size() {
    return $dataEnd;
  }

  @Override
  public final boolean isEmpty() {
    return $data == null;
  }

  private class LocalIterator implements Iterator<E> {

    private int $position = 0;

    public final boolean hasNext() {
      return $position < $dataEnd;
    }

    public final E next() throws NoSuchElementException {
      if ($position >= $dataEnd) {
        throw new NoSuchElementException();
      }
      E result = $data[$position];
      $position++;
      return result;
    }

    public final void remove() {
      if ($position <= 0) {
        throw new IllegalStateException();
      }
      $position--; // so we visit the moved element in a moment
      $dataEnd--;
      $data[$position] = $data[$dataEnd];
      $data[$dataEnd] = null;
      if ($dataEnd == 0) {
        // we are empty; throw everything away to save space
        $data = null;
      }
      else {
        shrink(GROWTH);
      }
    }

  }

  @Override
  public final Iterator<E> iterator() {
    return new LocalIterator();
  }

  @Override
  public final void clear() {
    $data = null;
    $dataEnd = 0;
  }

  @Override
  public final boolean contains(Object o) {
    if (o == null) {
      return false;
    }
    if ($data == null) {
      return false;
    }
    for (int i = 0; i < $dataEnd; i++) {
      assert $data[i] != null;
      // assert o != null;
      if (o.equals($data[i])) {
        return true;
      }
      // else; we need to continue looking for the element until we reach the end
    }
    // if we get here, o is not in the set
    return false;
  }

  /**
   * @throws NullPointerException
   *         e == null;
   */
  @Override
  public final boolean add(E e) throws NullPointerException {
    if (e == null) {
      throw new NullPointerException("cannot add null to a ArraySet");
    }
    if ($data == null) {
      @SuppressWarnings("unchecked")
      E[] newArray = (E[])new Object[INITIAL_SIZE];
      $data = newArray;
      assert $dataEnd == 0;
    }
    for (int i = 0; i < $dataEnd; i++) {
      assert $data[i] != null;
      // assert e != null;
      if (e.equals($data[i])) {
        // e is already in the set; we are done, and the set did not change
        return false;
      }
      // else; we need to continue looking for the element until we reach the end
    }
    shrink(1 + GROWTH);
    /* if we get here, we have not found e in the array,
     * and i == $dataEnd;
     * we will add the element at $dataEnd, and grow it,
     * if there is room; if not, we will create a new, bigger array,
     * and copy entries; in any case, we will return true
     */
    if ($dataEnd == $data.length) {
      // the array is full
      @SuppressWarnings("unchecked")
      E[] newArray = (E[])new Object[$data.length + GROWTH];
      System.arraycopy($data, 0, newArray, 0, $dataEnd);
      $data = newArray;
    }
    // there is still room in the array
    $data[$dataEnd] = e;
    $dataEnd++;
    return true;
  }

  @Override
  public final boolean remove(Object o) {
    if (o == null) {
      return false;
    }
    if ($data == null) {
      return false;
    }
    for (int i = 0; i < $dataEnd; i++) {
      // assert o != null;
      if (o.equals($data[i])) {
        // found! remove! we will move the last element here
        $dataEnd--;
        $data[i] = $data[$dataEnd];
        $data[$dataEnd] = null;
        if ($dataEnd == 0) {
          // we are empty; throw everything away to save space
          $data = null;
        }
        else {
          shrink(GROWTH);
        }
        return true;
      }
      // else; we need to continue looking for the element until we reach the end
    }
    // if we get here, o was not one of our elements
    return false;
  }

  private void shrink(int extraRoom) {
    if ($dataEnd + extraRoom < $data.length) {
      // the array is to big
      @SuppressWarnings("unchecked")
      E[] newArray = (E[])new Object[$dataEnd + GROWTH];
      System.arraycopy($data, 0, newArray, 0, $dataEnd);
      $data = newArray;
    }
  }

  @Override
  public final ArraySet<E> clone() {
    ArraySet<E> result = null;
    try {
      @SuppressWarnings("unchecked") ArraySet<E> arraySet = (ArraySet<E>)super.clone();
      result = arraySet;
    }
    catch (CloneNotSupportedException exc) {
      assert false : "CloneNotSupportedException should not happen: " + exc;
    }
    assert result != null;
    @SuppressWarnings("unchecked") E[] es = (E[])new Object[$dataEnd];
    System.arraycopy($data, 0, es, 0, $dataEnd);
    result.$data = es;
    return result;
  }

  /**
   * Data in this set is stored directly in an array, with only very
   * little buffer for growth, to be as memory efficient as possible.
   * When the set is empty, the array is discarded.
   * Initially the array reference is {@code null}, to use as little
   * space as possible. The array elements are used up until $dataEnd - 1,
   * so that $dataEnd is the effective size.
   * After that, data is {@code null}. At meaningful positions,
   * there will never be {@code null}. If the set is empty, and
   * {@code $data == null}, $dataSize is 0.
   *
   * @invar for (int i : 0 .. $dataEnd) {
   *          $data[i] != null;
   *        }
   * @invar for (int i : $dataEnd .. $data.lenght - 1) {
   *          $data[i] == null;
   *        }
   */
  private E[] $data = null;

  /**
   * @invar $data == null ?? $dataEnd == 0;
   */
  private int $dataEnd = 0;

}

