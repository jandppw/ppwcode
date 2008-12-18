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
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>An abstract class that implements a {@link PropertyEditor} completely for
 *   {@link AbstractStringBackedImmutableValue} based value types.</p>
 * <p>To use this, simply create an empty class next to your value type that extends this class.</p>
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
public abstract class StringBackedImmutableValueEditor extends AbstractValueEditor<StringBackedImmutableValueEditor>
    implements Serializable {


  public final static String EMPTY = "";


  /*<property name="asText">*/
  //------------------------------------------------------------------

  @Override
  @MethodContract(post = @Expression("value == null ? null : value.toString()"))
  public final String getAsText() {
    String result = (getValue() == null) ? null : getValue().toString();
    return result;
  }

  /**
   * @note We also presume, but {@link PropertyEditor#setAsText(String)} is unclear,
   *       that {@code setAstText(null)} should result in a {@code null} value.
   *       To the letter, {@code null} is not a valid tag, and this we would throw
   *       an {@link IllegalArgumentException}. The same applies to the empty string.
   */
  @Override
  @MethodContract(
    post = @Expression("(_text == null || _text == EMPTY) ? " +
                         "value == null : " +
                         "value == _Value_(_text)"),
    exc  = @Throw(type = IllegalArgumentException.class,
                  cond = @Expression("_text != null && _text != EMPTY && ! _Value_.isValid(_text_"))
  )
  public final void setAsText(final String text) throws IllegalArgumentException {
    if ((text == null) || text.equals(EMPTY)) {
      setValue(null);
    }
    else {
      StringBackedImmutableValueEditor value = null;
      try {
        Constructor<? extends StringBackedImmutableValueEditor> c = getValueType().getDeclaredConstructor(String.class);
        value = c.newInstance(text);
      }
      catch (InstantiationException exc) {
        // text is not a good programmatic representation of a value of type getValueType();
        throw new IllegalArgumentException(exc);
      }
      catch (IllegalArgumentException exc) {
        unexpectedException(exc);
      }
      catch (IllegalAccessException exc) {
        unexpectedException(exc);
      }
      catch (InvocationTargetException exc) {
        unexpectedException(exc);
      }
      catch (ExceptionInInitializerError err) {
        unexpectedException(err);
      }
      catch (SecurityException exc) {
        unexpectedException(exc);
      }
      catch (NoSuchMethodException exc) {
        unexpectedException(exc);
      }
      setValue(value);
    }
  }

  /*</property>*/

}
