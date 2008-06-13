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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Annotation to hold the part of the Toryt contract at type level. At this level,
 * the contract only holds type invariants.
 *
 * @mudo ATInherited doesn't work for interfaces. If it doesn't work for interfaces,
 *       and we have to write special code anyway, maybe we should not use @Inherited
 */
@Copyright("2008 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $, Jan Dockx")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1082 $",
         date     = "$Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited // MUDO has no effect for interfaces
public @interface TypeContract {

  /**
   * The list of invariants for the type.
   */
  Expression[] value();

}

