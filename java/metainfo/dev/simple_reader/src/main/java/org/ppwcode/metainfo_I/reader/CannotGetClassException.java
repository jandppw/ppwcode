/*<license>
Copyright 2008 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.metainfo_I.reader;


import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Signals that a class with {@link #getFullyQualifiedClassName()}
 * could not be loaded. The {@link #getCause() cause} gives more detail.
 *
 * @author Jan Dockx
 */
@SvnInfo(revision = "$Revision: 857 $",
         date     = "$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $")
public class CannotGetClassException extends Exception {

  /**
   * @pre fqcn != null;
   * @post new.getFullyQualifiedClassName().equals(fqcn);
   * @post new.getMessage() == null;
   * @post new.getCause() == cause;
   */
  public CannotGetClassException(String fqcn, Throwable cause) {
    super(cause);
    assert fqcn != null;
    $fqcn = fqcn;
  }

  /**
   * @basic
   */
  public String getFullyQualifiedClassName() {
    return $fqcn;
  }

  /**
   * @invar $fqcn != null;
   */
  private final String $fqcn;


}