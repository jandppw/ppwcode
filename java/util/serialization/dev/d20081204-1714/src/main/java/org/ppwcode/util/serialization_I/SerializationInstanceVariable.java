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

package org.ppwcode.util.serialization_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.Serializable;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Data about one instance variable, used in an {@link SerializationObject} that replaces
 * another object in serialization.
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class SerializationInstanceVariable implements Serializable {

  /**
   * The exact class that declared this instance variable.
   * It is important to carry this information, since private instance variables
   * can be overloaded in subclasses.
   */
  public Class<?> declaringClass;

  /**
   * The name of the instance variable this represents in {@link #declaringClass}.
   */
  public String name;

  /**
   * The value of the instance variable this represents in the object we are replacing.
   */
  public Serializable value;

  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("+@");
    sb.append(declaringClass == null ? "unknown declaring class" : declaringClass.getName());
    sb.append(" - ");
    sb.append(name == null ? "unknown name" : name);
    sb.append(" == ");
    sb.append(value);
    sb.append("@+");
    return sb.toString();
  }

}

