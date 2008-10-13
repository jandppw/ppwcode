/*<license>
Copyright 2008 - $Date: 2008-09-29 16:35:07 +0200 (Mon, 29 Sep 2008) $ by PeopleWare n.v.

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

package org.ppwcode.research.dynabean;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.research.jpa.crud.semanticsAlpha.Master;




public class DynaBeanExperiment {


  private Master $e;

  @Before
  public void prepare() {
    $e = new Master();
    $e.setPersistenceId(444);
    $e.setPersistenceVersion(555);
    $e.setEnterpriseId("0456-789-123");
    $e.setName("An Enterprise Name");
    $e.setAddress("MyStreet 6\n5546 A City\nBelgium");
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(2222, 1, 1);
    $e.setTerminationDate(gc.getTime());
    System.out.println("bean: " + $e);
  }

  @Test
  public void enterpriseToDynaBean1() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
//    System.out.println("\nbean 2 dynabean");
    DynaProperty[] props = new DynaProperty[]{
      new DynaProperty("id", Long.class),
      new DynaProperty("persistenceVersion", Long.class),
      new DynaProperty("enterpriseId", String.class),
      new DynaProperty("name", String.class),
      new DynaProperty("address", String.class),
      new DynaProperty("terminationDate", Date.class),
    };
    BasicDynaClass dynaClass = new BasicDynaClass("enterprise", null, props);
    DynaBean edb = dynaClass.newInstance();
    PropertyUtils.copyProperties(edb, $e);
//    Map<?, ?> edbDesc = PropertyUtils.describe(edb);
//    System.out.println("  dynabean: " + edbDesc);
//    System.out.println();
  }

  @Test(expected = ConversionException.class)
  public void enterpriseToDynaBean2() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
//    System.out.println("\nbean 2 dynabean");
    DynaProperty[] props = new DynaProperty[]{
      new DynaProperty("id", Long.class),
      new DynaProperty("persistenceVersion", Long.class),
      new DynaProperty("enterpriseId", Date.class), // wrong type
      new DynaProperty("name", String.class),
      new DynaProperty("address", String.class),
      new DynaProperty("terminationDate", Date.class),
    };
    BasicDynaClass dynaClass = new BasicDynaClass("enterprise", null, props);
    DynaBean edb = dynaClass.newInstance();
    PropertyUtils.copyProperties(edb, $e); // ConversionException
  }

  @Test
  public void dynaBeanToEnterprise1() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
//    System.out.println("\ndynabean 2 bean");
    DynaBean edb = new LazyDynaBean();
    edb.set("id", 666L);
    edb.set("persistenceVersion", 777L);
    edb.set("enterpriseId", "9876-734-123");
    edb.set("name", "A Lazy Bean");
    edb.set("address", "Lazy Street 25\n88484 Lazy Town\nLazilia");
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(8888, 11, 31);
    edb.set("terminationDate", gc.getTime());
//    Map<?, ?> edbDesc = PropertyUtils.describe(edb);
//    System.out.println("  dynabean: " + edbDesc);
//    System.out.println("  dynaClass: " + edb.getDynaClass());
    Master e = new Master();
    PropertyUtils.copyProperties(e, edb);
//    System.out.println("  bean: " + e);
//    System.out.println();
  }


  @Test(expected = IllegalArgumentException.class)
  public void dynaBeanToEnterprise2() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
//    System.out.println("\ndynabean 2 bean");
    DynaBean edb = new LazyDynaBean();
    edb.set("id", 666L);
    edb.set("persistenceVersion", 45.45D); // wrong type
    edb.set("enterpriseId", "9876-734-123");
    edb.set("name", "A Lazy Bean");
    edb.set("address", "Lazy Street 25\n88484 Lazy Town\nLazilia");
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(8888, 11, 31);
    edb.set("terminationDate", gc.getTime());
//    Map<?, ?> edbDesc = PropertyUtils.describe(edb);
//    System.out.println("  dynabean: " + edbDesc);
//    System.out.println("  dynaClass: " + edb.getDynaClass());
    Master e = new Master();
    PropertyUtils.copyProperties(e, edb); // IllegalArgumentException, with stupid output on err???
//    System.out.println("  bean: " + e);
//    System.out.println();
  }

}

