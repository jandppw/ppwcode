/*<license>
Copyright 2008 - $Date$ by PeopleWare n.v.

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

package org.ppwcode.research.json;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.research.jpa.crud.semanticsAlpha.Master;


public class JsonTest {

  private Master $e;

  @Before
  public void prepare() {
    $e = new Master();
    $e.setPersistenceId(444);
    $e.setPersistenceVersion(555);
    $e.setName("An Enterprise Name");
//    System.out.println($e);
  }

  @Test
  public void enterpriseToJson1() {
    /*JSON je =*/ JSONSerializer.toJSON($e);
//    System.out.println(je);
  }

  /**
   * Cycle is in origin of wildExceptions CompoundPropertyException. But we don't want to serialize the wildExceptions anyway. Exclude
   */
  @Test
  public void masterToJson2() {
    JsonConfig cf = new JsonConfig();
    cf.setExcludes(new String[] {"wildExceptions"});
    /*JSON je =*/ JSONSerializer.toJSON($e, cf);
//    System.out.println(je);
  }

  /**
   * Exclude all properties we don't want.
   * This should be generalized with a filter. or a processor for persistent beans in general (that deals with ATToMany relationships).
   */
  @Test
  public void masterToJson3() {
    JsonConfig cf = new JsonConfig();
    cf.setExcludes(new String[] {"wildExceptions", "civilized", "contracts"});
//    JSON je = JSONSerializer.toJSON($e, cf);
//    System.out.println(je);
  }

  // use a ?morpher? ?processor? for the date

  // use a specialized ?morpher? ?processor? for PersistentBeans

  // add a function for lazy loading the contracts

  @Test
  public void dynabean2Json1() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    DynaBean edb = new LazyDynaBean();
    edb.set("persistenceId", 666);
    edb.set("persistenceVersion", 777L);
    edb.set("name", "A Lazy Bean");
    edb.set("address", "Lazy Street 25\n88484 Lazy Town\nLazilia");
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(8888, 11, 31);
    edb.set("terminationDate", gc.getTime());
//    Map<?, ?> edbDesc = PropertyUtils.describe(edb);
//    System.out.println("  dynabean: " + edbDesc);
    /*JSON je =*/ JSONSerializer.toJSON(edb);
//    System.out.println(je);
  }

  @Test
  public void enterprise2DynaBean2Json1() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
    DynaProperty[] props = new DynaProperty[]{
      new DynaProperty("persistenceId", Integer.class),
      new DynaProperty("persistenceVersion", Integer.class),
      new DynaProperty("enterpriseId", String.class),
      new DynaProperty("name", String.class),
      new DynaProperty("address", String.class),
      new DynaProperty("terminationDate", Date.class),
    };
    BasicDynaClass dynaClass = new BasicDynaClass("enterprise", null, props);
    DynaBean edb = dynaClass.newInstance();
    PropertyUtils.copyProperties(edb, $e); // ConversionException
//    Map<?, ?> edbDesc = PropertyUtils.describe(edb);
//    System.out.println("  dynabean: " + edbDesc);
    /*JSON je =*/ JSONSerializer.toJSON(edb);
//    System.out.println(je);
  }

}
