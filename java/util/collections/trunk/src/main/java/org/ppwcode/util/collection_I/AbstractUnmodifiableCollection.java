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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Unmodifiable {@link Collection} general code.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractUnmodifiableCollection<E>
    extends AbstractCollection<E> {

  protected abstract class AbstractUnmodifiableIterator implements Iterator<E> {

    /**
     * @result false;
     * @throws UnsupportedOperationException
     *         true;
     */
    public final void remove() {
      throw new UnsupportedOperationException();
    }

  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean add(E object) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean addAll(Collection<? extends E> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean removeAll(Collection<?> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean retainAll(Collection<?> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final void clear() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean remove(Object object) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

}