/*<license>
Copyright 2009 - $Date: 2008-10-15 18:22:22 +0200 (Wed, 15 Oct 2008) $ by PeopleWare n.v..

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
package org.ppwcode.vernacular.l10n_III.util;

import java.util.Locale;
import org.ppwcode.vernacular.l10n_III.LocalizedException;
import org.ppwcode.vernacular.l10n_III.resourcebundle.DefaultResourceBundleLoadStrategy;
import org.ppwcode.vernacular.l10n_III.resourcebundle.KeyNotFoundException;
import org.ppwcode.vernacular.l10n_III.resourcebundle.ResourceBundleHelpers;
import org.ppwcode.vernacular.l10n_III.resourcebundle.WrongValueTypeException;

public class SuperException extends Exception implements LocalizedException {

  public SuperException(String message, Throwable cause) {
    super(message, cause);
  }

  public String getMessageTemplate(Locale locale) {
    // use message key to find the right template in the properties files
    String result;
    String messageKey = getMessage();
    DefaultResourceBundleLoadStrategy strategy = new DefaultResourceBundleLoadStrategy();
    strategy.setLocale(locale);
    String[] keys = {messageKey};
    try {
      result = ResourceBundleHelpers.value(getClass(), keys, String.class, strategy);
    } catch (KeyNotFoundException exc) {
      // TODO what to do with exceptions?
      return null;
    } catch (WrongValueTypeException exc) {
      // TODO what to do with exceptions?
      return null;
    }
    return result;
  }
}
