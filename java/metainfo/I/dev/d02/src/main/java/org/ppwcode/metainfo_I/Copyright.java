/*<license>
Copyright 2007 - $Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $ by PeopleWare n.v.

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

package org.ppwcode.metainfo_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Annotation for copyright information. By using this annotation,
 * the copyright information is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATCopyright(&quot;2007 - $Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $, PeopleWare n.v.&quot;)
 * public class ... {
 *  ...
 * }
 * </pre>
 *
 * @author    Jan Dockx
 */
@Copyright("2007 - $Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 857 $",
         date     = "$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Copyright {

  /**
   * The copyright notice. This should start with the period in which the contents
   * (the authored expression) if the file was created (e.g., <var>start year - end year</var>),
   * and the name of the copyright holder.
   */
  String value();

}