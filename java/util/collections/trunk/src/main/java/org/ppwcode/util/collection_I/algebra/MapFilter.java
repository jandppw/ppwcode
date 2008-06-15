/*<license>
Copyright 2007 - $Date: 2008-06-15 14:13:34 +0200 (Sun, 15 Jun 2008) $ by PeopleWare n.v..

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

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;

/**
 * An filter criterion for {@link java.util.Map Maps}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @todo Not use in the Beedra code, and no unit test.
 * @see FilteredMap
 * @todo start using ppwcode util collections_N asap
 */
@Copyright("2007 - $Date: 2008-06-15 14:13:34 +0200 (Sun, 15 Jun 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1102 $",
         date     = "$Date: 2008-06-15 14:13:34 +0200 (Sun, 15 Jun 2008) $")
public interface MapFilter<K, V> {

  /**
   * @pre key != null;
   * @pre value != null;
   */
  boolean filter(K key, V value);

}

