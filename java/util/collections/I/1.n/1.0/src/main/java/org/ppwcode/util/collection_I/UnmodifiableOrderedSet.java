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

import java.util.Iterator;
import java.util.List;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Unmodifiable {@link OrderedSet} backed by an {@link OrderedSet}.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class UnmodifiableOrderedSet<E>
    extends AbstractUnmodifiableSet<E>
    implements OrderedSet<E> {

  public UnmodifiableOrderedSet(OrderedSet<? extends E> os) {
    $backingOs = os;
  }

  @Override
  public UnmodifiableOrderedSet<E> clone() {
    try {
      @SuppressWarnings("unchecked")
      UnmodifiableOrderedSet<E> result = (UnmodifiableOrderedSet<E>)super.clone();
      result.$backingOs = $backingOs.clone();
      return result;
    }
    catch (CloneNotSupportedException exc) {
      assert false : "CloneNotSupportedException should not happen: " + exc;
      return null; // keep compiler happy
    }
  }

  /**
   * Can't be final for clone.
   *
   * @invar $backingOs != null;
   */
  private OrderedSet<? extends E> $backingOs;

  /**
   * @basic
   */
  @Override
  public final int size() {
    return $backingOs.size();
  }

  /**
   * @throws IndexOutOfBoundsException
   *         index < 0;
   * @throws IndexOutOfBoundsException
   *         index >= size();
   */
  public final E get(int index) throws IndexOutOfBoundsException {
    return $backingOs.get(index); // IndexOutOfBoundsException
  }

  /**
   * @result result >= 0 ?? get(result).equals(object);
   * @result result == -1 ?? ! contains(object);
   */
  public final int indexOf(Object object) {
    return $backingOs.indexOf(object);
  }

  @Override
  public final Iterator<E> iterator() {
    return new AbstractUnmodifiableIterator() {

      private Iterator<? extends E> iter = $backingOs.iterator();

      public boolean hasNext() {
        return iter.hasNext();
      }

      public E next() {
        return iter.next();
      }

    };
  }

  /**
   * @result result != null;
   * @result equals(result);
   */
  public final List<? extends E> asList() {
    return $backingOs.asList();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  public final boolean add(int index, E object) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  public final E remove(int index) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public final String toString() {
    return $backingOs.toString();
  }

}