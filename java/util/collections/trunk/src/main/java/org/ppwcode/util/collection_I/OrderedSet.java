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

import java.util.List;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

/**
 * <p>Set that keeps a user defined order (in contrast to a sorted set,
 *   where the order is defined algorithmically by a comparator). Elements
 *   have a position in an {@code OrderedSet}.</p>
 * <p>Based on <a href="http://svn.apache.org/viewvc/jakarta/commons/proper/collections/branches/collections_jdk5_branch/src/java/org/apache/commons/collections/set/AbstractSerializableSetDecorator.java?view=markup">Apache Jakarta Commons SVN repository d.d. 2007/3/12</a>,
 *   by Stephen Colebourne and Henning P. Schmiedehausen.</p>
 *
 * @note This cannot be a {@link List}, sadly, for the dubious reason that the
 *       {@link List} contract says that {@link List#add(Object)} always returns
 *       {@code true}, and the {@link java.util.Collection} contract says that
 *       {@link java.util.Collection#add(Object)} returns {@code true} if the collection has
 *       changed. In a list, every {@code add} has a change effect (the element is
 *       added at the end of the list, possibly as a duplicate). In a list with
 *       set behavior, if the element is already in the list, it would move. This
 *       is also a change, except when the element to be added is already at the end:
 *       then nothing changes (no extra element and no move), so we need to return
 *       {@code false}. Arguably, the contract of {@code List} is too strong. The
 *       same reasoning applies to {@link #add(int, Object)}, {ATlink #addAll(int, java.util.Collection)} // TODO change this Javadoc when implemented
 *       and {@link #addAll(java.util.Collection)}.
 *
 * @note A method {@code boolean addAll(int index, Collection<? extends E> coll)}
 *       has no meaningful semantics in an ordered set or set list. Suppose we take
 *       the ordered set {1, 2, 3}, and we ask to add {1, 2, 3, 4, 5, 6} at index 3?
 *       The result would have to something like {x, x, x, 1, 2, 3, 4, 5, 6}, but we
 *       can't set any elements at the x's. Throwing an {@link IndexOutOfBoundsException}
 *       here is not the correct semantics either, or very difficult for the user
 *       to understand.
 *
 * @note Implementing classes should implement {@link #equals(Object)} so that it takes
 *       the order into account.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface OrderedSet<E> extends Set<E>, Cloneable {

    /**
     * Gets an unmodifiable view of the order of the Set.
     *
     * @return an unmodifiable list view
     */
    List<? extends E> asList();

    E get(int index);

    int indexOf(Object object);

    boolean add(int index, E object);

/*    boolean addAll(int index, Collection<? extends E> coll);
 * TODO skipped for now, but should be implemented
 */

    E remove(int index);

    OrderedSet<E> clone();

}