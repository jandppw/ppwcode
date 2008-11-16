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

package org.ppwcode.vernacular.value_III.hibernate3;


import static java.sql.Types.VARCHAR;
import static org.hibernate.Hibernate.STRING;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.CloneHelpers.safeReference;
import static org.ppwcode.util.reflect_I.TypeHelpers.type;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.dependency;
import static org.ppwcode.vernacular.exception_III.ProgrammingErrorHelpers.newAssertionError;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.usertype.UserType;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.value_III.Value;
import org.ppwcode.vernacular.value_III.ValueEditor;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>General Hibernate {@link UserType} for any {@link Value} type that has an associated
 *   {@link PropertyEditor}. Instance of the value type are mapped to a VARCHAR, that contains the
 *   {@link PropertyEditor#getAsText() tag} for the value, as defined by the property editor.</p>
 * <p>When instances are constructed with the default constructor, the {@link #returnedClass()}
 *   and {@link #getPropertyEditor()} are {@code null}, and the user type is not
 *   {@link #isOperational() operational}. The instance can be configured then using
 *   {@link #setParameterValues(Properties)}. Here, you can give the fully qualified name of the
 *   type this instance is intended to support. With this name, the type is loaded, and a
 *   {@link PropertyEditor} is sought for the type using the {@link PropertyEditorManager}.
 *   It is an error if the type does not exist or a property editor cannot be found for the type.
 *   Alternatively, you can supply the type for which the instance should work using
 *   {@link #PropertyEditorUserType(Class)}, or you can supply the type and the
 *   property editor using {@link #PropertyEditorUserType(ValueEditor)}.
 *   You could also create a subtype using these constructors to fill out these properties.</p>
 * <p>In a Hibernate context, the easiest use is to define the user type using the {@code param}
 *   tag when definining the property of an entity:</p>
 * <pre>
 * ...
 *   &lt;property name=&quot;<var>propertyName</var>&quot;&gt;
 *     &lt;type name=&quot;org.ppwcode.vernacular.value_III.hibernate3.PropertyEditorUserType&quot;&gt;
 *       &lt;param name=&quot;propertyType&quot;&gt;<var>...property.type...</var>&lt;/param&gt;
 *     &lt;/type&gt;
 *   &lt;/property&gt;
 * ...
 * </pre>
 * <p>Alternatively, you can define the user type for a property type in the general configuration
 *   file.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class PropertyEditorUserType extends AbstractValueUserType {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * An empty instance. Configure using {@link #setParameterValues(Properties)}.
   */
  @MethodContract(post = {
    @Expression("returnedClass == null"),
    @Expression("propertyEditor == null")
  })
  public PropertyEditorUserType() {
    // NOP
  }

  /**
   * An instance aimed to persists instances of {@code valueType}. The appropriate
   * {@link #getPropertyEditor() property editor} is retrieved from the {@link PropertyEditorManager}.
   * If no {@link PropertyEditor} is found, this is an error.
   */
  @MethodContract(post = {
    @Expression("returnedClass == _valueType"),
    @Expression("propertyEditor == PropertyEditorManager.findEditor(_valueType)")
  })
  public PropertyEditorUserType(Class<? extends Value> valueType) {
    $valueType = valueType;
    initPropertyEditor();
  }

  /**
   * An instance aimed to persist instances of a value type using {@code ve}.
   */
  @MethodContract(post = {
    @Expression("returnedClass == _valueType"),
    @Expression("propertyEditor == _pe")
  })
  public PropertyEditorUserType(ValueEditor<? extends Value> ve) {
    $editor = ve;
    $valueType = ve.getValueType();
  }

  /*</construction>*/



  /*<section name="initialization">*/
  //------------------------------------------------------------------

  @MethodContract(post = @Expression("returnedClass() != null && propertyEditor != null"))
  public final boolean isOperational() {
    return $valueType != null && $editor != null;
  }

  /**
   * Set the type of values we will operate for, as a fully qualified name. The name of the
   * type we will operate for needs to be given in a property with key &quot;<code>propertyType</code>&quot;.
   * We also retrieve a {@link #getPropertyEditor() property editor} from the {@link PropertyEditorManager}
   * for that type.
   * If a type with the given name cannot be loaded, or we cannot find a {@link PropertyEditor}
   * for that type, this is an error.
   */
  @MethodContract(post =  {
    @Expression("returnedClass() == type(_params.getProperty('propertyType')"),
    @Expression("propertyEditor == PropertyEditorManager.findEditor(returnedClass())")
  })
  public void setParameterValues(Properties params) {
    String fqcn = params.getProperty("propertyType");
    if (fqcn == null) {
       throw new MappingException("type parameter not specified in Hibernate configuration");
    }
    try {
      $valueType = type(fqcn);
    }
    catch (AssertionError aErr) {  // MUDO booo! type really needs to throw exception
       throw new MappingException("enumClass " + fqcn + " not found", aErr);
    }
    initPropertyEditor();
 }

  /*</section>*/



  /*<property name="value type">*/
  //------------------------------------------------------------------

  /**
   * The class this user type now works.
   */
  @Override
  @Basic
  public Class<? extends Value> returnedClass() {
    return $valueType;
  }

  private Class<? extends Value> $valueType;

  /*</property>*/



  /*<property name="propertyEditor">*/
  //------------------------------------------------------------------

  /**
   * An instance of the value type property editor for the value
   * type this UserType works for.
   */
  @Basic
  public final PropertyEditor getPropertyEditor() {
    return $editor;
  }

  private void initPropertyEditor() {
    assert dependency($valueType, "$valueType");
    $editor = PropertyEditorManager.findEditor($valueType);
    if ($editor == null) {
      throw newAssertionError("no property editor found for type " + $valueType.getCanonicalName() +
                              "; please initialize the PropertyEditorManager for this type, or use the " +
                              "constructor to provide a property editor", null);
    }
  }

  private PropertyEditor $editor;

  /*</property>*/



  /*<section name="meta">*/
  //------------------------------------------------------------------

  /**
   * Static definition of the VARCHAR column we will write in.
   */
  @Invars(@Expression("SQL_TYPES == {VARCHAR}"))
  private static final int[] SQL_TYPES = {VARCHAR};

  @MethodContract(post = @Expression("SQL_TYPES"))
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /*</section>*/



  /**
   * For value types for which we can use a property editor (simple value types),
   * a deep copy is the value itself if the value is immutable, and a klone
   * otherwise. We will return a safe reference, like {@link #replace(Object, Object, Object)}.
   */
  @MethodContract(post = @Expression("safeReference(value)"))
  public final Object deepCopy(final Object value) {
    return safeReference(value);
  }

  /**
   * Write an instance of the mapped class to a prepared statement.
   */
  @MethodContract(
    pre  = @Expression("propertyEditor != null"),
    post = @Expression("STRING.nullSafeSet(statement, getPropertyEditor().setValue(_value); getPropertyEditor().getAsText(), index)"),
    exc  = @Throw(type = HibernateException.class,
                  cond = @Expression("_value != null && ! returnedClass().isInstance(_value)"))
  )
  public final void nullSafeSet(final PreparedStatement statement, final Object value, final int index)
      throws HibernateException, SQLException {
    assert dependency(getPropertyEditor(), "propertyEditor");
    assert dependency(returnedClass(), "returnedClass()");
    if (value != null && ! returnedClass().isInstance(value)) {
      throw new HibernateException("this user type can only handle values of type " + returnedClass().getCanonicalName() +
                                   "; " + value.getClass().getCanonicalName() + " is not supported");
    }
    else {
      try {
        $editor.setValue(value);
        String tag = $editor.getAsText(); //  // IllegalArgumentException
        STRING.nullSafeSet(statement, tag, index);
      }
      catch (IllegalArgumentException iaExc) {
        throw new HibernateException("property editor \"" + $editor + "\" could not transform " + value + " into a String", iaExc);
      }
    }
  }

  /**
   * Retrieve an instance of the mapped class from a JDBC resultset.
   * Implementors should handle possibility of null values.
   */
  public final Object nullSafeGet(final ResultSet resultSet, final String[] names, final Object owner)
      throws HibernateException, SQLException {
    String tag = (String)Hibernate.STRING.nullSafeGet(resultSet, names[0]);
    Object result = null;
    if (tag != null) {
      try {
        $editor.setAsText(tag); // IllegalArgumentException
        result = $editor.getValue();
      }
      catch (IllegalArgumentException iaExc) {
        throw new HibernateException("property editor \"" + $editor + "\" could not transform " + tag + " into an object", iaExc);
      }
    }
    return result;
  }

}
