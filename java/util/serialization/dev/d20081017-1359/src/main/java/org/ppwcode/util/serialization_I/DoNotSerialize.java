/*<license>
Copyright 2008 - $Date: 2008-07-26 00:36:28 +0200 (Sat, 26 Jul 2008) $ by PeopleWare n.v.

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

package org.ppwcode.util.serialization_I;


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
 * Annotation for serilization helpers. The property annotated with this annotation
 * should not be serialized (on deserialization, the initial value after construction
 * with the default constructor is kept).
 *
 * Usage pattern:
 * <pre>
 *   &#64;DoNotSerialize
 *   public final <var>PropertyType</var> get<var>PropertyName</var>() {
 *     ...
 *   }
 * </pre>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2008 - $Date: 2008-07-26 00:36:28 +0200 (Sat, 26 Jul 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1860 $",
         date     = "$Date: 2008-07-26 00:36:28 +0200 (Sat, 26 Jul 2008) $")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DoNotSerialize {

  // NOP

}
