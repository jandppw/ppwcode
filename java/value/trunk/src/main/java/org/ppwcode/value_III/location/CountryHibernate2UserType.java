/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.value_III.location;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.hibernate2.AbstractEnumerationUserType;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * Hibernate mapping for {@link Country}. Country ISO codes
 * are stored in a VARCHAR.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class CountryHibernate2UserType extends AbstractEnumerationUserType<Country> {

  /**
   * Create a new {@link CountryHibernate2UserType}.
   *
   * @post new.getEnumerationValueEditor() instanceof CountryEditor;
   */
  @MethodContract(post = @Expression("enumerationValueEditor instanceof CountryEditor"))
  public CountryHibernate2UserType() {
    super(new CountryEditor());
  }

}
