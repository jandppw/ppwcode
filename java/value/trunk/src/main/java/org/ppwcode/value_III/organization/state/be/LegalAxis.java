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

package org.ppwcode.value_III.organization.state.be;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>The <dfn>legal axis</dfn> is a Belgian concept of social security law.
 *   It defines 4 classes for organizations: A, B, C and D.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public enum LegalAxis {

  /**
   * Legal axis class <dfn>A</dfn> is for organizations with 1000 employees or more.
   */
  A,

  /**
   * Legal axis class <dfn>B</dfn> is for organizations with 200 employees or more, but less than 1000.
   */
  B,

  /**
   * Legal axis class <dfn>C</dfn> is for organizations with 20 employees or more, but less than 200.
   */
  C,

  /**
   * Legal axis class <dfn>D</dfn> is for organizations with less than 20 employees.
   */
  D

}
