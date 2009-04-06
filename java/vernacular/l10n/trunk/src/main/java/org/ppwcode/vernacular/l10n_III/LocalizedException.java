/*<license>
Copyright 2009 - $Date: 2009-01-26 14:14:58 +0100 (Mon, 26 Jan 2009) $ by PeopleWare n.v.

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

package org.ppwcode.vernacular.l10n_III;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import java.util.Locale;


/**
 * This interface needs to be implemented for every type of exception that
 * needs to be localized by the ppwcode-vernacular-l10n framework.
 *
 * @author    Ruben Vandeginste
 * @author    PeopleWare n.v.
 */
@Copyright("2009 - $Date: $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: $",
         date     = "$Date: $")
public interface LocalizedException {

  // MUDO contract !!
  String getMessageTemplate(Locale locale) throws I18nTemplateException;

}
