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

import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>A set that stores elements in {@link WeakReference weak references}.
 *   This means that membership in the set is never a reason not to be garbage
 *   collected.</p>
 * <p>Since this set is used very much in an application we are developing, and
 *   we are having memory issues, this implementation is tweaked to use
 *   as little memory as possible.</p>
 * <p>Cloning a weak hash set makes no sense. Use {@link #strongClone()} instead.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class WeakArraySet<E extends Object> extends AbstractSet<E> {

  private final static int INITIAL_SIZE = 1;

  private final static int GROWTH = 2;

  @Override
  public final int size() {
    optimize();
    return $dataEnd == -1 ? 0 : $dataEnd;
  }

  @Override
  public final boolean isEmpty() {
    optimize();
    return $data == null;
  }

  private class LocalIterator implements Iterator<E> {

    public LocalIterator() {
      optimize();
      if ($data == null) {
        @SuppressWarnings("unchecked")
        E[] newArray = (E[])new Object[0];
        $localData = newArray;
      }
      else {
        @SuppressWarnings("unchecked")
        E[] newArray = (E[])new Object[$dataEnd == -1 ? 0 : $dataEnd];
        int sourceI = 0;
        int targetI = 0;
        while (sourceI < $dataEnd) {
          WeakReference<E> handle = $data[sourceI];
          if (handle != null) {
            E e = handle.get();
            if (e != null) {
              newArray[targetI] = e;
              targetI++;
            }
          }
          sourceI++;
        }
        @SuppressWarnings("unchecked")
        E[] newArray2 = (E[])new Object[targetI];
        System.arraycopy(newArray, 0, newArray2, 0, targetI);
        $localData = newArray2;
      }
    }

    private E[] $localData;
    private int $position = 0;

    public final boolean hasNext() {
      return $position < $localData.length;
    }

    public final E next() throws NoSuchElementException {
      if ($position >= $localData.length) {
        throw new NoSuchElementException();
      }
      E result = $localData[$position];
      $position++;
      return result;
    }

    public final void remove() {
      if ($position <= 0) {
        throw new IllegalStateException();
      }
      WeakArraySet.this.remove($localData[$position -  1]);
    }

  }

  @Override
  public final Iterator<E> iterator() {
    return new LocalIterator();
  }

  @Override
  public final void clear() {
    $data = null;
    $dataEnd = -1;
  }

  private void optimize() {
    if ($data != null) {
      int i = 0;
      while (i < $dataEnd) {
        if (($data[i] == null) || ($data[i].get() == null)) {
          // move from end to here
          $dataEnd--;
          $data[i] = $data[$dataEnd];
          $data[$dataEnd] = null;
          // don't progress, we have to check the new entry at i too (if i was not $dataEnd - 1)
        }
        else {
          // else; we need to progress
          i++;
        }
      }
      // maybe we want to shrink the array
      if ($dataEnd == 0) {
        // we are empty; throw everything away to save space
        $data = null;
        $dataEnd = -1;
      }
      else {
        shrink();
      }
    }
  }

  @Override
  public final boolean contains(Object o) {
    if (o == null) {
      return false;
    }
    if ($data == null) {
      return false;
    }
    int i = 0;
    boolean found = false;
    while ((i < $dataEnd) && (! found)) {
      if (($data[i] == null) || ($data[i].get() == null)) {
        // move from end to here
        $dataEnd--;
        $data[i] = $data[$dataEnd];
        $data[$dataEnd] = null;
        // don't progress, we have to check the new entry at i too (if i was not $dataEnd - 1)
      }
      else {
        assert $data[i] != null;
        assert $data[i].get() != null;
//        assert o != null;
        if (o.equals($data[i].get())) {
          // o is in the set
          found = true;
        }
        // else; we need to continue looking for the element until we reach the end
        i++;
      }
    }
    shrink();
    // if we get here, o is not in the set
    return found;
  }

  /**
   * @throws NullPointerException
   *         e == null;
   */
  @Override
  public final boolean add(E e) throws NullPointerException {
    if (e == null) {
      throw new NullPointerException("cannot add null to a WeakHashSet");
    }
    if ($data == null) {
      @SuppressWarnings("unchecked")
      WeakReference<E>[] newArray = (WeakReference<E>[])new WeakReference<?>[INITIAL_SIZE];
      $data = newArray;
      $dataEnd = 0;
    }
    int i = 0;
    while (i < $dataEnd) {
      if (($data[i] == null) || ($data[i].get() == null)) {
        // move from end to here
        $dataEnd--;
        $data[i] = $data[$dataEnd];
        $data[$dataEnd] = null;
        // don't progress, we have to check the new entry at i too (if i was not $dataEnd - 1)
      }
      else {
        assert $data[i] != null;
        assert $data[i].get() != null;
//        assert e != null;
        if (e.equals($data[i].get())) {
          // e is already in the set; we are done, and the set did not change
          return false;
        }
        // else; we need to continue looking for the element until we reach the end
        i++;
      }
    }
    shrink();
    /* if we get here, we have not found e in the array,
     * and i == $dataEnd, and everything < i is cleaned up;
     * we will add the element at $dataEnd, and grow it,
     * if there is room; if not, we will create a new, bigger array,
     * and copy entries; in any case, we will return true
     */
    if ($dataEnd == $data.length) {
      // the array is full
      @SuppressWarnings("unchecked")
      WeakReference<E>[] newArray = (WeakReference<E>[])new WeakReference<?>[$data.length + GROWTH];
      System.arraycopy($data, 0, newArray, 0, $dataEnd);
      $data = newArray;
    }
    // there is still room in the array
    $data[$dataEnd] = new WeakReference<E>(e);
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
    int i = 0;
    boolean removed = false;
    while (i < $dataEnd) {
      if (($data[i] == null) || ($data[i].get() == null)) {
        // move from end to here
        $dataEnd--;
        $data[i] = $data[$dataEnd];
        $data[$dataEnd] = null;
        // don't progress, we have to check the new entry at i too (if i was not $dataEnd - 1)
      }
      else {
        if (! removed) {
          assert $data[i] != null;
          assert $data[i].get() != null;
  //        assert o != null;
          if (o.equals($data[i].get())) {
            // found! remove! we will move the last element here
            $dataEnd--;
            $data[i] = $data[$dataEnd];
            $data[$dataEnd] = null;
            removed = true; // we will continue cleaning up
          }
          // else; we need to continue looking for the element until we reach the end
        }
        i++;
      }
    }
    if ($dataEnd == 0) {
      // we are empty; throw everything away to save space
      $data = null;
      $dataEnd = -1;
    }
    else {
      shrink();
    }
    return removed;
  }

  private void shrink() {
    if ($dataEnd + 1 + GROWTH < $data.length) {
      // the array is to big
      @SuppressWarnings("unchecked")
      WeakReference<E>[] newArray = (WeakReference<E>[])new WeakReference<?>[$dataEnd + GROWTH];
      System.arraycopy($data, 0, newArray, 0, $dataEnd);
      $data = newArray;
    }
  }

  public final HashSet<E> strongClone() {
    optimize();
    return new HashSet<E>(this);
  }

  /**
   * Data in this set is stored directly in an array, with only very
   * little buffer for growth, to be as memory efficient as possible.
   * A linked list might be a possible solution to, but that would
   * require a small extra object for each entry. This implementation is not
   * time efficient, but memory efficiency is our primary goal.
   * Initially the array reference is {@code null}, to use as little
   * space as possible. The array can contain {@code null} references,
   * and the {@link WeakReference} entries that are effective,
   * might refer to {@code null}.
   * As often as possible, we will push {@code null} references
   * to the end of the array.
   */
  private WeakReference<E>[] $data = null;

  /**
   * @invar $data == null ?? $dataEnd = -1;
   * @invar $dataEnd != -1 ? for (int i : $dataEnd .. $data.lenght - 1) {
   *                           $data[i] == null;
   *                         }
   */
  private int $dataEnd = -1;

}

