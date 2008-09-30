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

import static java.util.Collections.unmodifiableList;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * Super type for exceptions that carry a {@link ResourceBundle}
 * and a key.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2727 $",
         date     = "$Date: 2008-09-29 18:21:16 +0200 (Mon, 29 Sep 2008) $")
@Invars(@Expression("message == null"))
public abstract class KeyException extends ResourceBundleException {

  @MethodContract(post = {
    @Expression("resourceBundle == _resourceBundle"),
    @Expression("keys == _key"),
    @Expression("cause == null")
  })
  protected KeyException(ResourceBundle resourceBundle, String... key) {
    this(new ResourceBundle[] {resourceBundle}, key, null);
  }

  @MethodContract(post = {
    @Expression("resourceBundle == _resourceBundle"),
    @Expression("keys == _keys"),
    @Expression("cause == _cause")
  })
  protected KeyException(ResourceBundle resourceBundles[], String[] keys, Throwable cause) {
    super(cause);
    $resourceBundles = unmodifiableList(Arrays.asList(resourceBundles));
    $keys = unmodifiableList(Arrays.asList(keys));
  }

  @MethodContract(post = {
    @Expression("resourceBundle == _resourceBundle"),
    @Expression("keys == {_key}"),
    @Expression("cause == _cause")
  })
  protected KeyException(ResourceBundle resourceBundle, String key, Throwable cause) {
    this(new ResourceBundle[] {resourceBundle}, new String[] {key}, cause);
  }


  /* <property name="resource bundle"> */
  //------------------------------------------------------------------

  @Basic
  public final List<ResourceBundle> getResourceBundles() {
    return $resourceBundles;
  }

  private List<ResourceBundle> $resourceBundles;

  /* </property> */



  /* <property name="key"> */
  //------------------------------------------------------------------

  @Basic
  public final List<String> getKeys() {
    return $keys;
  }

  private List<String> $keys;

  /* </property> */

}

