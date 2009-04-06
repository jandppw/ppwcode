/*<license>
Copyright 2009 - $Date: 2009-02-12 15:55:44 +0100 (Thu, 12 Feb 2009) $ by PeopleWare n.v..

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

import java.util.Locale;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;

@Copyright("2009 - $Date: 2009-02-12 15:55:44 +0100 (Thu, 12 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 4107 $",
         date     = "$Date: 2009-02-12 15:55:44 +0100 (Thu, 12 Feb 2009) $")
public class I18nTemplateException extends I18nException {

  /*<construction>*/
  //------------------------------------------------------------------

  @MethodContract(
    post = {
      @Expression("message == _message"),
      @Expression("cause == null"),
      @Expression("template = _template"),
      @Expression("context == null"),
      @Expression("locale = null"),
    }
  )
  public I18nTemplateException(String message, String template) {
    super(message);
    $template = template;
  }

  @MethodContract(
    post = {
      @Expression("message == _message"),
      @Expression("cause == _cause"),
      @Expression("template = _template"),
      @Expression("context == null"),
      @Expression("locale = null"),
    }
  )
  public I18nTemplateException(String message, String template, Throwable cause) {
    super(message, cause);
    $template = template;
  }

  @MethodContract(
    post = {
      @Expression("message == _message"),
      @Expression("cause == _cause"),
      @Expression("template = _template"),
      @Expression("context == _context"),
      @Expression("locale = _locale"),
    }
  )
  public I18nTemplateException(String message, String template, Object context, Locale locale, Throwable cause) {
    super(message, cause);
    $template = template;
    $context = context;
    $locale = locale;
  }

  /*</construction>*/



  /*<property name="template">*/
  //------------------------------------------------------------------

  @Basic
  public final String getTemplate() {
    return $template;
  }

  private String $template = "";

  /*</property>*/



  /*<property name="context">*/
  //------------------------------------------------------------------

  @Basic
  public final Object getContext() {
    return $context;
  }

  private Object $context = null;

  /*</property>*/



  /*<property name="locale">*/
  //------------------------------------------------------------------

  @Basic
  public final Locale getLocale() {
    return $locale;
  }

  private Locale $locale = null;

  /*</property>*/

}
