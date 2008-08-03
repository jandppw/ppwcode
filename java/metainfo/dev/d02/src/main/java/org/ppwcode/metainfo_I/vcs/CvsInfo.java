/*<license>
Copyright 2007 - $Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $ by PeopleWare n.v..

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
 * Annotation for CVS meta-data. By using this annotation,
 * the CVS data about the source revision the compiled code is based on,
 * is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATCvsInfo(revision = &quot;$Revision: 857 $&quot;,
 *           date     = &quot;$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $&quot;,
 *           state    = &quot;$State$&quot;,
 *           tag      = &quot;$Name$&quot;)
 * public class ... {
 *  ...
 *  }
 * </pre>
 *
 * @author    Jan Dockx
 */
@Copyright("2007 - $Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $, PeopleWare n.v.")
@License(APACHE_V2)
@CvsInfo(revision    = "$Revision: 857 $",
         date        = "$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $",
         state       = "$State$",
         tag         = "$Name$")
@SvnInfo(revision = "$Revision: 857 $",
         date     = "$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface CvsInfo {

  /**
   * Source code revision. Fill out with &quot;$Revision: 857 $&quot;
   */
  String revision();

  /**
   * Source code revision. Fill out with &quot;$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $&quot;
   */
  String date();

  /**
   * Source code revision. Fill out with &quot;$State$&quot;
   */
  String state();

  /**
   * Source code revision. Fill out with &quot;$Name$&quot;
   */
  String tag();

}