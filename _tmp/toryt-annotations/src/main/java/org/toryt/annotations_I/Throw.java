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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * The part of a method contract that expresses that a {@link Throwable} can be thrown.
 * {@link #type()} is the type of expression that might be thrown.
 * {@link #cond()} is the condition that has to be satisfied before the exception
 * might be thrown.
 *
 * This annotation should never be used directly, but always inside a method contract
 * annotation.
 */
@Copyright("2008 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $, Jan Dockx")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1082 $",
         date     = "$Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $")
@Documented
@Retention(RetentionPolicy.RUNTIME)
// Target should be none, always use in wrapper annotation
public @interface Throw {

  /**
   * The mandatory type of the throwable this annotation expresses
   * about.
   */
  Class<?> type();

  /**
   * The expressions under which the exception is allowed to be thrown.
   * All expressions need to be true (and). Some of these expressions
   * will enforce state in the thrown expression.
   */
  Expression[] cond();

}

