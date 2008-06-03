/*<license>
Copyright 2008 - $Date: 2008/04/03 22:19:23 $ by Jan Dockx.

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


public class StubClass {

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

}

