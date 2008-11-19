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

package org.ppwcode.vernacular.value_III;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.unexpectedException;
import static org.ppwcode.vernacular.resourcebundle_II.ResourceBundleHelpers.value;

import java.util.ResourceBundle;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.resourcebundle_II.DefaultResourceBundleLoadStrategy;
import org.ppwcode.vernacular.resourcebundle_II.KeyNotFoundException;
import org.ppwcode.vernacular.resourcebundle_II.ResourceBundleLoadStrategy;
import org.ppwcode.vernacular.resourcebundle_II.WrongValueTypeException;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>This class provides an implementation for an {@link EnumerationValueEditor} that returns labels for enumeration
 *   type values defined in a resource bundle, normally backed by a properties file.</p>
 * <p>The implementation expects a properties file next to the editor class, with as name
 *   <kbd><var>getEnumerationValueType().getName()</var><var>_locale_identification</var>.properties</kbd>,
 *   The <kbd><var>_locale_identification</var></kbd> should follow the traditional rules (see {@link ResourceBundle}.
 *   In this file, there should be an entry with as key
 *   <code>label.<var>tag</var></code>, where <code><var>tag</var></code>
 *   is the programmatic string representation of an enumeration type value as returned by {@link #getAsText()}, for
 *   each value returned by {@link #getTags()}.</p>
 * <p>The resource bundle is located using the current
 *   {@link #getResourceBundleLoadStrategy() ResourceBundleLoadStrategy}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @mudo UNFINISHED
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class ResourceBundleBasedEnumerationValueEditor<_Value_> extends AbstractEnumerationValueEditor<_Value_> {

  /**
   * The string pre- and postpended to the {@link #getAsText()} String
   * for the label, if no matching entry was found in the resource bundle.
   *
   * <strong>value = {@value}</strong>
   */
  public static final String LABEL_NOT_FOUND_TOKEN = "???";

  /**
   * Return the a label from the {@link #getValueType()} resource bundle, with key
   * {@link #getLabelKey()}. If the resource bundle is not found, or the key is not found in the resource
   * bundle, the String <code>LABEL_NOT_FOUND_TOKEN + getAsText() + LABEL_NOT_FOUND_TOKEN</code>
   * is returned.
   *
   * @mudo contract
   */
  public final String getLabel() {
    String result = null;
    try {
      result = value(getValueType(), new String[] {getLabelKey()}, String.class, getResourceBundleLoadStrategy());
    }
    catch (WrongValueTypeException exc) {
      unexpectedException(exc, "value with key " + getLabelKey() + " in resource bundle for " +
                               getValueType() + " is not of type String");
    }
    catch (KeyNotFoundException exc) {
      result = LABEL_NOT_FOUND_TOKEN + getAsText() + LABEL_NOT_FOUND_TOKEN;
    }
    return result;
  }

  /*<property name="resource bundle load strategy">*/
  //------------------------------------------------------------------

  @Basic(init = @Expression("result instanceof DefaultResourceBundleLoadStrategy"))
  public final ResourceBundleLoadStrategy getResourceBundleLoadStrategy() {
    return $resourceBundleLoadStrategy;
  }

  /**
   * @param     strategy
   *            The new resource bundle load strategy.
   * @post      getResourceBundleLoadStrategy() == strategy;
   */
  @MethodContract(post = @Expression("resourceBundleLoadStrategy == _strategy"))
  public final void setResourceBundleLoadStrategy(final ResourceBundleLoadStrategy strategy) {
    $resourceBundleLoadStrategy = strategy;
  }

  private ResourceBundleLoadStrategy $resourceBundleLoadStrategy = new DefaultResourceBundleLoadStrategy();

  /*</property>*/



  /*<property name="labelKey">*/
  //------------------------------------------------------------------

  @MethodContract(post = @Expression("'label.' + asText"))
  public final String getLabelKey() {
    return "label." + getAsText();
  }

  /*</property>*/

}
