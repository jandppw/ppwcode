/*<license>
Copyright 2008 - $Date$ by Jan Dockx.

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

package org.toryt.annotations_I;


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
 * The annotated method is a basic inspector. Inside this annotation, we
 * can set or describe initial values for the property that is expressed by
 * the basic inspector. This extends all constructor postconditions (also that of
 * an implicit constructor). We can also add type invariants: instead of mentioning
 * type invariants on the type level using {@link Invars}, for invariants that
 * are only concerned with 1 property, it often is clearer for the human reader to
 * find them with the property it is concerned with.
 */
@Copyright("2008 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $, Jan Dockx")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1082 $",
         date     = "$Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Basic {

  /**
   * Type invariants that only concern the property this basic inspector
   * represents.
   */
  Expression[] invars() default {};

  /**
   * The initial value for the property of which this basic inspector
   * is the representative. Several expression can be used to non-deterministically
   * describe facts about the initial value if necessary.
   */
  Expression[] init() default {};

}

