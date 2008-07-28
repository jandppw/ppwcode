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

package org.ppwcode.util.reflect_I;


import java.io.Serializable;
import java.util.Date;


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
    return null;
  }

  @SuppressWarnings("unused")
  public static <_T_ extends Serializable> _T_ stubStaticMethod(_T_ t, float f) {
    return null;
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



  public StubClass() {
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

}

