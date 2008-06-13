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
import static org.toryt.annotations_I.Support.EMPTY;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * An expression is expressed by a mandatory {@link #value() expression}, written in
 * a given {@link #lang() expression language}.
 * The {@link #scope()} is optional, and expresses which users can see the expression.
 *
 * A {@link #name()} for an expression is optional. A {@link #description()}, containing
 * an explanation in human readable form, is optional. A {@link #note()} is optional.
 *
 *
 * This annotation should never be used directly, but always inside another contract annotation.
 */
@Copyright("2008 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $, Jan Dockx")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1082 $",
         date     = "$Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $")
@Documented
@Retention(RetentionPolicy.RUNTIME)
// Target should be none, always use in wrapper annotation
public @interface Expression {

  /**
   * The mandatory formal expression.
   * It should be written in the language {@link #lang()}.
   */
  String value();

  /**
   * The expression language in which {@link #value()} is written.
   * The default is {@link ElType#TORYT_EL_I the Toryt Expression Language}.
   */
  ElType lang() default ElType.TORYT_EL_I;

  /**
   * The scope of the expression. Default is {@link Scope#PROTECTED}.
   */
  Scope scope() default Scope.PROTECTED;

  /**
   * Optional name for the expression. Use this if it is necessary
   * to be able to refer to this in isolation from somewhere else.
   * The name should be short, but human readable, and may contain spaces
   * or diacriticals.
   */
  String name() default EMPTY;

  /**
   * Optional description in human readable form of the expression.
   * Use this if the formal notation is complex.
   */
  String description() default EMPTY;

  /**
   * Optional remark or note.
   */
  String note() default EMPTY;

}

