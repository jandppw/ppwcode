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

package org.ppwcode.vernacular.resourcebundle;


import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;

import org.ppwcode.vernacular.resourcebundle_II.ResourceBundleLoadStrategy;


public class ResourceBundleLoadStrategyContract {

  // interface, not actual tests, only contract

  public static void assertInvariants(ResourceBundleLoadStrategy subject) {
    // no invariants
  }

  public final static String EMPTY = "";

  public static void loadResourceBundle(ResourceBundleLoadStrategy subject, String basename, ResourceBundle result) {
    // validate
    assertTrue(basename == null ? result == null : true);
    assertTrue(basename.equals(EMPTY) ? result == null : true);
  }

}

