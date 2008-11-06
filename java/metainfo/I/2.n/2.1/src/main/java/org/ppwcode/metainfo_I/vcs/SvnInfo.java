/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.metainfo_I.vcs;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;


/**
 * Annotation for Subversion meta-data. By using this annotation,
 * the Subversion data about the source revision the compiled code is based on,
 * is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATSvnInfo(revision = &quot;$Revision$&quot;,
 *           date     = &quot;$Date$&quot;)
 * public class ... {
 *  ...
 * }
 * </pre>
 *
 * @author    Jan Dockx
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface SvnInfo {

  /**
   * Source code revision. Fill out with &quot;$Revision$&quot;
   */
  String revision();

  /**
   * Source code revision. Fill out with &quot;$Date$&quot;
   */
  String date();

}
