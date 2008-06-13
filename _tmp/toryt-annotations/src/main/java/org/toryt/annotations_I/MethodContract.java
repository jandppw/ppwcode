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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Annotation to express a method contract. A method contract features expressions
 * for {@link #pre() preconditions}, {@link #post() postconditions}, and
 * MUDO stuff for exceptions.
 *
 * @note It doesn't make sense to make this annotation ATInherited, because that
 *       doesn't work for methods.
 */
@Copyright("2008 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $, Jan Dockx")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1082 $",
         date     = "$Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface MethodContract {

  /**
   * The list of preconditions for the type.
   */
  Expression[] pre() default {};

  /**
   * The list of postconditions for the type.
   */
  Expression[] post();

  /**
   * The list of exceptions that might be thrown, with the conditions
   * under which they might be thrown.
   */
  Throw[] exc() default {};

}

