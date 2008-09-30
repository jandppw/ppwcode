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

package org.ppwcode.vernacular.resourcebundle_II;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * Super type for exceptions thrown by code in this library.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2727 $",
         date     = "$Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $")
public abstract class ResourceBundleException extends Exception {

//  @MethodContract(post = {
//    @Expression("message == null"),
//    @Expression("cause == null")
//  })
//  public ResourceBundleException() {
//    super();
//  }
//
//  @MethodContract(post = {
//    @Expression("message == _message"),
//    @Expression("cause == null")
//  })
//  public ResourceBundleException(String message) {
//    super(message);
//  }

  @MethodContract(post = {
    @Expression("message == null"),
    @Expression("cause == _cause")
  })
  protected ResourceBundleException(Throwable cause) {
    super(cause);
  }

//  @MethodContract(post = {
//    @Expression("message == _message"),
//    @Expression("cause == _cause")
//  })
//  public ResourceBundleException(String message, Throwable cause) {
//    super(message, cause);
//  }

}

