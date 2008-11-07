/*<license>
Copyright 2007 - $Date: 2008-11-07 16:59:27 +0100 (Fri, 07 Nov 2008) $ by PeopleWare n.v.

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

package org.ppwcode.value_III.id11n;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Annotation for an {@link Identifier} interface that contains the direct concrete subtypes
 * and the subtype interfaces (known in the library of the interface) .
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2007 - $Date: 2008-11-07 16:59:27 +0100 (Fri, 07 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3471 $",
         date     = "$Date: 2008-11-07 16:59:27 +0100 (Fri, 07 Nov 2008) $")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConcreteIdentifierTypes {

  /**
   * Enumeration of the known direct subtype interfaces of this identifier type.
   */
  Class<?>[] subtypes();

  /**
   * Enumeration of the known direct concrete classes that implement this identifier type.
   */
  Class<?>[] concrete();

}
