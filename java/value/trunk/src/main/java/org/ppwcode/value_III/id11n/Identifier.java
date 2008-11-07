/*<license>
Copyright 2004 - $Date: 2008-10-28 20:52:03 +0100 (Tue, 28 Oct 2008) $ by PeopleWare n.v..

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

package org.ppwcode.value_III.id11n;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.ImmutableValue;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>An identifier that identifies an object or concept in the real world.
 *   Identifier types have an applicability domain. For each applicability domain
 *   there is a separate interface, forming a multiple inheritance graph. Use the
 *   appropriate subtype as polymorph static type for your property.</p>
 * <p>Identifiers are an alpha-numeric String. Subtypes and subclasses add constraints
 *   to the format and content of that String. The string cannot be {@code null}
 *   nor {@code empty}.</p>
 * <p>Subclasses are either abstract or final. Concrete final subclasses always offer
 *   a constructor that takes the programmatic representation of the identifier as
 *   parameter. Concrete final subclasses always feature an annotation of type
 *   {@link IdentifierSchemeDescription} that contains the URL of a formal description of
 *   the implemented identification scheme. Concrete final subclasses always feature an annotation
 *   of type {@link IdentifierIssuingAuthority} that contains the name and possibly an URI that
 *   identifies the issuing authority.</p>
 *
 * @author    Jan Dockx
 * @author    Peopleware NV
 */
@Copyright("2008 - $Date: 2008-10-28 20:52:03 +0100 (Tue, 28 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 3356 $",
         date     = "$Date: 2008-10-28 20:52:03 +0100 (Tue, 28 Oct 2008) $")
public interface Identifier extends ImmutableValue {

  /**
   * The empty String.
   */
  public final static String EMPTY = "";

  /**
   * The programmatic representation of the the identifier.
   */
  @Basic(
    invars = {
      @Expression("identifier != null"),
      @Expression("identifier != EMPTY")
    }
  )
  String getIdentifier();

  @MethodContract(post = @Expression("identifier == other.identifier"))
  boolean equals(Object other);

  @MethodContract(post = @Expression("class.canonicalName + ':' + identifier"))
  String toString();

}
