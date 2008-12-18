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

package org.ppwcode.util.serialization_I.teststubsA;


import java.io.Serializable;
import java.util.Date;

import org.ppwcode.vernacular.exception_III.ApplicationException;


public class StubClass extends SuperStubClass {

  public static class StubClassA {

    public StubClassA(String s, int i, float f) {
      //
    }

    public int stubMethod1(String s, int i, float f) {
      return 0;
    }

    public void stubMethod2() {
      return;
    }

  }

  public static class StubClassB extends StubClassA {

    public StubClassB(String s, int i, float f) {
      super(s, i, f);
    }

  }

  public class StubClassInnerA {
    public class StubClassInnerAInner {
      // NOP
    }
  }

  public class StubClassInnerB extends StubClassInnerA {
    // NOP
  }


  public StubClass() {
    $stubProperty = new CloneableStubClassA();
  }

  public StubClass(CloneableStubClassA cscA) {
    $stubProperty = cscA;
  }

  @Override
  @SuppressWarnings("unused")
  public void stubMethod() {
    // NOP
  }

  @SuppressWarnings("unused")
  public double stubMethodWithReturn() {
    return 0.0d;
  }

  @SuppressWarnings("unused")
  public double stubMethodWithException() throws Exception {
    return 0.0d;
  }

  @SuppressWarnings("unused")
  public void stubMethod(Object o) {
    // NOP
  }

  @SuppressWarnings("unused")
  public void stubMethod(String s) {
    // NOP
  }

  @SuppressWarnings("unused")
  public void stubMethod(int i) {
    // NOP
  }

  @SuppressWarnings("unused")
  public void stubMethod(Class<StubClass> stubClass) {
    // NOP
  }

  @SuppressWarnings("unused")
  public void stubMethod(int i, boolean b, Object o, String s) {
    // NOP
  }

  @SuppressWarnings("unused")
  public <_T_> _T_ stubMethod(_T_ t, float f) {
    return null;
  }

  @SuppressWarnings("unused")
  public <_T_ extends Serializable> _T_ stubMethod(_T_ t, float f) {
    return null;
  }

  @SuppressWarnings("unused")
  public void stubMethod(Date d) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static void stubMethod(Object[] os) {
    // NOP
  }

  @SuppressWarnings("unused")
  void stubMethod(long l) {
    // NOP
  }

  @SuppressWarnings("unused")
  protected void stubMethod(boolean b) {
    // NOP
  }

  @SuppressWarnings("unused")
  private void stubMethod(byte b) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod() {
    // NOP
  }

  @SuppressWarnings("unused")
  public static int stubStaticMethodWithReturn() {
    return 0;
  }

  @SuppressWarnings("unused")
  public static int stubStaticMethodWithException() throws Exception {
    return 0;
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod(Object o) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod(String s) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod(int i) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod(Class<StubClass> stubClass) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod(int i, boolean b, Object o, String s) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static <_T_> _T_ stubStaticMethod(_T_ t, float f) {
    return t;
  }

  @SuppressWarnings("unused")
  public static <_T_ extends Serializable> _T_ stubStaticMethod(_T_ t, float f) {
    return t;
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod(Date d) {
    // NOP
  }

  @SuppressWarnings("unused")
  public static void stubStaticMethod(Object[] os) {
    // NOP
  }

  @SuppressWarnings("unused")
  void stubStaticMethod(long l) {
    // NOP
  }

  @SuppressWarnings("unused")
  protected void stubStaticMethod(boolean b) {
    // NOP
  }

  @SuppressWarnings("unused")
  private void stubStaticMethod(byte b) {
    // NOP
  }



  public StubClass(Object o) {
    // NOP
  }

  public StubClass(String s) {
    // NOP
  }


  public StubClass(int i) {
    // NOP
  }

  public StubClass(Class<StubClass> stubClass) {
    // NOP
  }

  public StubClass(StubClass s) {
    // NOP
  }

  public StubClass(int i, boolean b, Object o, String s) {
    // NOP
  }

  public <_T_> StubClass(_T_ t1, _T_ t2, float f) {
    // NOP
  }


  public <_T_ extends Serializable> StubClass(_T_ t1, Serializable t2, float f) {
    // NOP
  }

  public StubClass(Date d) throws Exception {
    // NOP
  }

  public StubClass(Object[] os) throws Exception {
    // NOP
  }

  StubClass(long l) throws Exception {
    // NOP
  }

  protected StubClass(boolean b) throws Exception {
    // NOP
  }

  @SuppressWarnings("unused")
  private StubClass(byte b) throws Exception {
    // NOP
  }



  public final CloneableStubClassA getStubProperty() {
    return $stubProperty.clone();
  }

  public final void setStubProperty(CloneableStubClassA stubProperty) {
    $stubProperty = stubProperty.clone();
  }

  private CloneableStubClassA $stubProperty;



  public CloneableStubClassA stubPropertyField = new CloneableStubClassA();



  public final CloneableStubClassA getStubRoProperty() {
    return $stubRoProperty.clone();
  }

  private CloneableStubClassA $stubRoProperty = new CloneableStubClassA();



  public final CloneableStubClassA getStubRoPrivateProperty() {
    return $stubRoPrivateProperty.clone();
  }

  @SuppressWarnings("unused")
  private final void setStubRoPrivateProperty(CloneableStubClassA stubRoPrivateProperty) {
    $stubRoPrivateProperty = stubRoPrivateProperty.clone();
  }

  private CloneableStubClassA $stubRoPrivateProperty = new CloneableStubClassA();



  public final CloneableStubClassA getStubRoPackageProperty() {
    return $stubRoPackageProperty.clone();
  }

  final void setStubRoPackageProperty(CloneableStubClassA stubRoPackageProperty) {
    $stubRoPackageProperty = stubRoPackageProperty.clone();
  }

  private CloneableStubClassA $stubRoPackageProperty = new CloneableStubClassA();




  public final CloneableStubClassA getStubRoProtectedProperty() {
    return $stubRoProtectedProperty.clone();
  }

  protected final void setStubRoProtectedProperty(CloneableStubClassA stubRoProtectedProperty) {
    $stubRoProtectedProperty = stubRoProtectedProperty.clone();
  }

  private CloneableStubClassA $stubRoProtectedProperty = new CloneableStubClassA();



  public final void setStubWoProperty(CloneableStubClassA stubWoProperty) {
    $stubWoProperty = stubWoProperty.clone();
  }

  @SuppressWarnings("unused")
  private CloneableStubClassA $stubWoProperty = new CloneableStubClassA();



  @SuppressWarnings("unused")
  private final CloneableStubClassA getStubWoPrivateProperty() {
    return $stubWoPrivateProperty.clone();
  }

  public final void setStubWoPrivateProperty(CloneableStubClassA stubWoPrivateProperty) {
    $stubWoPrivateProperty = stubWoPrivateProperty.clone();
  }

  private CloneableStubClassA $stubWoPrivateProperty = new CloneableStubClassA();



  final CloneableStubClassA getStubWoPackageProperty() {
    return $stubWoPackageProperty.clone();
  }

  public final void setStubWoPackageProperty(CloneableStubClassA stubWoPackageProperty) {
    $stubWoPackageProperty = stubWoPackageProperty.clone();
  }

  private CloneableStubClassA $stubWoPackageProperty = new CloneableStubClassA();



  protected final CloneableStubClassA getStubWoProtectedProperty() {
    return $stubWoProtectedProperty.clone();
  }

  public final void setStubWoProtectedProperty(CloneableStubClassA stubWoProtectedProperty) {
    $stubWoProtectedProperty = stubWoProtectedProperty.clone();
  }

  private CloneableStubClassA $stubWoProtectedProperty = new CloneableStubClassA();



  public final int getStubPropertyInt() {
    return $stubPropertyInt;
  }

  public final void setStubPropertyInt(int stubPropertyInt) {
    $stubPropertyInt = stubPropertyInt;
  }

  private int $stubPropertyInt = 7;



  public final long getStubPropertyLong() {
    return $stubPropertyLong;
  }

  public final void setStubPropertyLong(long stubPropertyLong) {
    $stubPropertyLong = stubPropertyLong;
  }

  private long $stubPropertyLong = 7L;



  public final char getStubPropertyChar() {
    return $stubPropertyChar;
  }

  public final void setStubPropertyChar(char stubPropertyChar) {
    $stubPropertyChar = stubPropertyChar;
  }

  private char $stubPropertyChar = 'c';



  public final short getStubPropertyShort() {
    return $stubPropertyShort;
  }

  public final void setStubPropertyShort(short stubPropertyShort) {
    $stubPropertyShort = stubPropertyShort;
  }

  private short $stubPropertyShort = 7;



  public final byte getStubPropertyByte() {
    return $stubPropertyByte;
  }

  public final void setStubPropertyByte(byte stubPropertyByte) {
    $stubPropertyByte = stubPropertyByte;
  }

  private byte $stubPropertyByte = 7;



  public final boolean getStubPropertyBoolean() {
    return $stubPropertyBoolean;
  }

  public final void setStubPropertyBoolean(boolean stubPropertyBoolean) {
    $stubPropertyBoolean = stubPropertyBoolean;
  }

  private boolean $stubPropertyBoolean = true;



  public final float getStubPropertyFloat() {
    return $stubPropertyFloat;
  }

  public final void setStubPropertyFloat(float stubPropertyFloat) {
    $stubPropertyFloat = stubPropertyFloat;
  }

  private float $stubPropertyFloat = 7.7F;



  public final double getStubPropertyDouble() {
    return $stubPropertyDouble;
  }

  public final void setStubPropertyDouble(double stubPropertyDouble) {
    $stubPropertyDouble = stubPropertyDouble;
  }

  private double $stubPropertyDouble = 7.7D;



  public final String getStubPropertyString() {
    return $stubPropertyString;
  }

  public final void setStubPropertyString(String stubPropertyString) {
    $stubPropertyString = stubPropertyString;
  }

  private String $stubPropertyString = STUB_PROPERTY_STRING_VALUE;



  public final Date getStubPropertyDate() {
    return $stubPropertyDate;
  }

  public final void setStubPropertyDate(Date stubPropertyDate) {
    $stubPropertyDate = stubPropertyDate;
  }

  private Date $stubPropertyDate = new Date();




  public final Object getInternalExceptionProperty() {
    return $internalExceptionProperty;
  }

  public final void setInternalExceptionProperty(Object o) throws ApplicationException {
    if (o instanceof ApplicationException) {
      throw (ApplicationException)o;
    }
    $internalExceptionProperty = o;
  }

  private Object $internalExceptionProperty;

  public final static String STUB_PROPERTY_STRING_VALUE = "String property stub";

}

