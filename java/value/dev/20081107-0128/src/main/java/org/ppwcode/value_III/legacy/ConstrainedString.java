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

package org.ppwcode.value_III.legacy;


import java.io.Serializable;


/**
 * A Class to represent VARCHAR() objects with constrained checking.
 *
 * @author    Jan Dockx
 * @author    Dimitri Smits
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 *
 * @invar     getMaxLength() >= 0
 * @invar     getConstrainedString().length() <= getMaxLength()
 * @invar     getConstrainedString() <> null
 */
public class ConstrainedString /*extends Delegate*/ implements Serializable {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     delegatingBean
   *            The bean that delegates to this.
   * @param     propertyName
   *            The name of the property for which <code>delegatingBean</code>
   *            uses this.
   * @param     maxLength
   *            The maximum length of the string
   * @pre       delegatingBean != null;
   * @pre       (propertyName != null) && ! propertyName.equals("");
   * @pre       maxLength > 0;
   * @post      new.getDelegatingBean() == delegatingBean;
   * @post      new.getpropertyName().equals(propertyName);
   * @post      new.getString() = "";
   * @post      new.getMaxLength() = maxLength;
   */
  public ConstrainedString(final Object delegatingBean,
                           final String propertyName,
                           final int maxLength) {
    super(/*delegatingBean, propertyName*/);
    assert maxLength > 0 : "maxLength > 0";  //$NON-NLS-1$
    $maxLength = maxLength;
  }

  /**
   * @param     delegatingBean
   *            The bean that delegates to this.
   * @param     propertyName
   *            The name of the property for which <code>delegatingBean</code>
   *            uses this.
   * @param     maxLength
   *            The maximum length of the string
   * @param     stringValue
   *            The initial string for this object.
   * @pre       delegatingBean != null;
   * @pre       (propertyName != null) && ! propertyName.equals("");
   * @pre       maxLength > 0;
   * @pre       stringValue != null;
   * @pre       stringValue.length() <= maxLength;
   * @post      new.getDelegatingBean() == delegatingBean;
   * @post      new.getpropertyName().equals(propertyName);
   * @post      new.getString().equals(stringValue);
   * @post      new.getMaxLength() = maxLength;
   */
  public ConstrainedString(final Object delegatingBean,
                           final String propertyName,
                           final int maxLength,
                           final String stringValue) {
    super(/*delegatingBean, propertyName*/);
    assert maxLength > 0 : "maxLength > 0";  //$NON-NLS-1$
    assert stringValue != null;
    assert stringValue.length() <= maxLength;
    $maxLength = maxLength;
    $string = stringValue;
  }

  /*</construction>*/



  /*<property name="string">*/
  //------------------------------------------------------------------

  /**
   * @param     stringValue
   *            the new value
   * @post      stringValue == null ? new.getString().equals("")
   *                                : new.getString().equals(stringValue);
   * @throws    TooLongException
   *            stringValue.length > getMaxLength();
   */
  public final void setString(final String stringValue)
      throws TooLongException {
    String result = ""; //$NON-NLS-1$
    if (stringValue != null) {
      result = stringValue;
    }
    if (result.length() > getMaxLength()) {
      throw new TooLongException(getDelegatingBean(),
                                 getPropertyName(),
                                 stringValue,
                                 getMaxLength(),
                                 "TOO_LONG", //$NON-NLS-1$
                                 null);
    }
    $string = result;
  }

  private String getPropertyName() {
    // TODO Auto-generated method stub
    return null;
  }

  private Object getDelegatingBean() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @basic
   */
  public final String getString() {
    return $string;
  }

  /**
   * @invar     $string != null;
   * @invar     $string.length < getMaxLength();
   */
  private String $string = ""; //$NON-NLS-1$

  /*</property>*/



  /*<property name="maxLength">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final int getMaxLength() {
    return $maxLength;
  }

  /**
   * @invar     $maxLength >= 0;
   */
  private int $maxLength;

  /*</property>*/



//  /**
//   * @param     other
//   *            Object to compare with
//   * @return    (other != null) && (other instanceof ConstrainedString)
//   *            && (getString().equals(((ConstrainedString)other).getString()))
//   *            && (getMaxLength()
//   *                == ((ConstrainedString)other).getMaxLength());
//   * @post      result ? hashCode() == other.hashCode() : true;
//   */
//  public boolean hasSameValue(final Object other) {
//    return super.hasSameValues(other)
//           && (getString().equals(((ConstrainedString)other).getString()))
//           && (getMaxLength() == ((ConstrainedString)other).getMaxLength());
//  }

  /**
   * Returns a string representation of the object.
   *
   * @return  getString();
   */
  public String toString() {
    return getString();
  }

}
