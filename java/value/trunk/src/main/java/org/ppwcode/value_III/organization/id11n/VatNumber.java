/*<license>
Copyright 2004 - $Date: 2008-11-12 00:55:31 +0100 (Wed, 12 Nov 2008) $ by PeopleWare n.v..

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

package org.ppwcode.value_III.organization.id11n;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>In the EU, many organizations are subject to VAT. They
 *   have to report a VAR number on each invoice or general document.
 *   The form of the VAT number differs between countries. In some
 *   cases, another number is reused.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date: 2008-11-12 00:55:31 +0100 (Wed, 12 Nov 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3496 $",
         date     = "$Date: 2008-11-12 00:55:31 +0100 (Wed, 12 Nov 2008) $")
public interface VatNumber extends OrganizationIdentifier {

  // NOP

}
