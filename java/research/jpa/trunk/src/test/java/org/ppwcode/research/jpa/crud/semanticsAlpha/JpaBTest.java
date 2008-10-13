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

package org.ppwcode.research.jpa.crud.semanticsAlpha;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;
import org.ppwcode.research.jpa.crud.semanticsAlpha.Master;
import org.ppwcode.research.jpa.crud.semanticsAlpha.Detail;

public class JpaBTest {

  static final String PERSISTENCE_UNIT_NAME = "be_hdp_contracts_I_IBMOpenJPA_test";

  public final static String MASTER_NAME_0 = "HYPOTHESIS-NAME";

  public final static Date DETAIL_DATE_A = new Date();

  public final static Date DETAIL_DATE_B = new Date();

  private String $cwdName;
  private String $serFileName;

  private void assertMaster0(Integer eId, Master fromDbE) {
    assertEquals(eId, fromDbE.getPersistenceId());
    assertEquals(MASTER_NAME_0, fromDbE.getName());
  }

  private Master createMaster0() {
    Master e = new Master();
    e.setName(MASTER_NAME_0);
    return e;
  }

  @Test
  public void hypothesis1() {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1 (master without detail, saved with MERGE)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    assertFalse(em.contains(e));
    Master persistedE = em.merge(e);
    assertFalse(em.contains(e));
    System.out.println("NOTE THAT original master IS NOT IN entity manager AFTER MERGE");
    assertTrue(em.contains(persistedE));
    System.out.println("NOTE THAT MERGEd master IS IN entity manager AFTER MERGE");
    tx.commit();
    assertFalse(em.contains(e));
    System.out.println("NOTE THAT original master IS NOT IN entity manager AFTER COMMIT");
    assertTrue(em.contains(persistedE));
    System.out.println("NOTE THAT MERGEd master IS STILL IN entity manager AFTER COMMIT");
    em.close();
    tx = null;
    em = null;

    Integer eId = persistedE.getPersistenceId();
    System.out.println("id of MERGEd master: " + eId);
    System.out.println("original RAM master:\n\t" + e);
    System.out.println("MERGEd version of original RAM master:\n\t" + persistedE);
    assertNotSame(e, persistedE);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();
    tx = null;
    em = null;
    assertMaster0(eId, fromDbE);
    assertNotSame(persistedE, fromDbE);
    System.out.println("master retrieved from DB:\n\t" + fromDbE);
    System.out.println("$details of master retrieved from DB is null?: " + (fromDbE.$details == null));
    System.out.println("$details of master retrieved from DB:\n\t" + fromDbE.$details);
    assertNull(fromDbE.$details); // ok; set is not initialized
    System.out.println("details of master retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNull(fromDbE.getDetails());
    System.out.println("BOTH ARE NULL AS EXPECTED: LAZY LOADING DOES NOT WORK ON DETACHED OBJECTS");
  }

  @Test
  public void hypothesis1a() {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1a (master without detail, saved with PERSIST --> we also have primary key)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    assertFalse(em.contains(e));
    System.out.println("RAM master:\n\t" + e);
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    // using persist here
    em.persist(e);
    assertTrue(em.contains(e));
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    System.out.println("RAM master, persisted, uncommitted:\n\t" + e);
    tx.commit();
    tx = null;
    assertTrue(em.contains(e));
    System.out.println("NOTE THAT master IS STILL IN entity manager AFTER COMMIT");
    System.out.println("RAM master, persisted, committed:\n\t" + e);
    em.close();
    em = null;
    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    System.out.println("RAM master, persisted, committed, detached:\n\t" + e);

    Integer eId = e.getPersistenceId();
    System.out.println("id of persisted master: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    assertFalse(em.contains(e));
    Master fromDbE = em.find(Master.class, eId);
    assertTrue(em.contains(fromDbE));
    tx.commit();
    tx = null;
    assertTrue(em.contains(fromDbE));
    System.out.println("NOTE THAT entity manager STILL CONTAINS RETRIEVED master AFTER COMMIT");
    em.close();
    em = null;
    assertMaster0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    System.out.println("master retrieved from DB:\n\t" + fromDbE);
    System.out.println("$details of master retrieved from DB:\n\t" + fromDbE.$details);
    assertNull(fromDbE.$details);
    System.out.println("details of master retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNull(fromDbE.getDetails());
    System.out.println("BOTH ARE NULL AS EXPECTED: LAZY LOADING DOES NOT WORK ON DETACHED OBJECTS");
  }

  @Test
  public void hypothesis1b() throws IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1b  (master without detail, saved with PERSIST, serialized and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    System.out.println("original RAM master:\n\t" + e);
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    assertNotNull(e.$details);
    System.out.println("NOTE THAT $details for original RAM master is NOT NULL: "+e.$details);
    assertNotNull(e.getDetails());
    System.out.println("NOTE THAT details for original RAM master is NOT NULL: "+e.getDetails());
    em.persist(e);
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    System.out.println("original RAM master, after PERSIST:\n\t" + e);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    System.out.println("original RAM master, after commit and detach:\n\t" + e);

    Integer eId = e.getPersistenceId();
    System.out.println("id of persisted master: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    assertMaster0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    System.out.println("master retrieved from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    System.out.println("$details of master retrieved from DB:\n\t" + fromDbE.$details);
    assertNull(fromDbE.getDetails());
    System.out.println("details of master retrieved from DB:\n\t" + fromDbE.getDetails());

    // serialization and deserialization of original master
    Master deserE = serAndDeserMaster(e);

    assertMaster0(eId, deserE);
    assertNotSame(e, deserE);
    System.out.println("original master from ser file:\n\t" + deserE);
    assertNotNull(deserE.$details);
    System.out.println("$details of original master from ser file:\n\t" + deserE.$details);
    System.out.println("type of $details of original master from ser file:\n\t" + deserE.$details.getClass());
    System.out.println("NOTE THAT AFTER SERIALIZATION+DESERIALIZATION, $details IS NOT NULL");
    assertNotNull(deserE.getDetails());
    System.out.println("details of master from ser file:\n\t" + deserE.getDetails());
    assertTrue(deserE.getDetails().isEmpty());

    // serialization and deserialization of master from db
    Master deserFromDbE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserFromDbE);
    assertNotSame(e, deserFromDbE);
    System.out.println("master from db from ser file:\n\t" + deserFromDbE);
    assertNull(deserFromDbE.$details);
    System.out.println("$details of master from db from ser file:\n\t" + deserFromDbE.$details);
    System.out.println("NOTE THAT AFTER SERIALIZATION+DESERIALIZATION, $details IS NULL");
    assertNull(deserFromDbE.getDetails());
    System.out.println("details of master from db from ser file:\n\t" + deserFromDbE.getDetails());
  }

  @Test
  public void hypothesis2a() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2a (master with 2 details, created using persist, serialized outside transaction and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master after persist:\n\t" + e);
    System.out.println("detail A after persist:\n\t" + slcA);
    System.out.println("detail B after persist:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    assertMaster0(eId, e);
    assertNotNull(e.$details); // ok; set is not initialized
    System.out.println("$details of master after persist:\n\t" + e.$details);
    System.out.println("details of master after persist:\n\t" + e.getDetails());
    assertNotNull(e.$details); // set is not initialized: working with detached objects
    assertNotNull(e.getDetails()); // set is not initialized: working with detached objects
    System.out.println("master after persist: keeps the details set during persist");

    Master deserEe = serAndDeserMaster(e);

    assertMaster0(eId, deserEe);
    assertNotSame(e, deserEe);
    System.out.println("master from original ser:\n\t" + deserEe);
    assertNotNull(deserEe.$details);
    System.out.println("$details of master from original ser file:\n\t" + deserEe.$details);
    System.out.println("NOTE THAT AFTER SERIALIZATION+DESERIALIZATION, $details IS NOT NULL");
    System.out.println("details from master from original ser:\n\t" + deserEe.getDetails());
    assertNotNull(deserEe.$details);
    assertNotNull(deserEe.getDetails());

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    assertFalse(em.contains(e));
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertMaster0(eId, fromDbE);
    System.out.println("master retrieved from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    System.out.println("$details of master retrieved from DB:\n\t" + fromDbE.$details);
    System.out.println("details of master retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNull(fromDbE.$details); // set is not initialized: working with detached objects
    assertNull(fromDbE.getDetails()); // set is not initialized: working with detached objects
    System.out.println("master retrieved from DB: lazy loading of attached details");

    Master deserE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("master from ser:\n\t" + deserE);
    assertNull(deserE.$details);
    System.out.println("$details of master from ser file:\n\t" + deserE.$details);
    System.out.println("NOTE THAT AFTER SERIALIZATION+DESERIALIZATION, $details IS STILL NULL");
    System.out.println("details from master from ser:\n\t" + deserE.getDetails());
    assertNull(deserE.$details);
    assertNull(deserE.getDetails());
  }

  @Test
  public void hypothesis2b() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2b (master with 2 details, created using persist, serialized inside transaction and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master after persist:\n\t" + e);
    System.out.println("detail A after persist:\n\t" + slcA);
    System.out.println("detail B after persist:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    assertFalse(em.contains(e));
    serMaster(fromDbE);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    System.out.println("$details of master retrieved from DB:\n\t" + fromDbE.$details);
    System.out.println("details of master retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNull(fromDbE.$details);
    assertNull(fromDbE.getDetails());

    Master deserE = deserMaster();

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("master from ser:\n\t" + deserE);
    assertNull(deserE.$details);
    System.out.println("$details of master from ser file:\n\t" + deserE.$details);
    System.out.println("NOTE THAT AFTER SERIALIZATION+DESERIALIZATION, $details IS STILL NULL");
    System.out.println("details from master from ser:\n\t" + deserE.getDetails());
    assertNull(deserE.getDetails());
  }

  @Test
  public void hypothesis2c() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2c (master with 2 details, created using persist, serialized and deserialized outside transaction)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master after persist:\n\t" + e);
    System.out.println("detail A after persist:\n\t" + slcA);
    System.out.println("detail B after persist:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    assertFalse(em.contains(e));
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    System.out.println("$details of master retrieved from DB:\n\t" + fromDbE.$details);
    System.out.println("details of master retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNull(fromDbE.$details);
    assertNull(fromDbE.getDetails());

    Master deserE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("master from ser:\n\t" + deserE);
    assertNull(deserE.$details);
    System.out.println("$details of master from ser file:\n\t" + deserE.$details);
    System.out.println("NOTE THAT AFTER SERIALIZATION+DESERIALIZATION, $details IS STILL NULL");
    System.out.println("details from master from ser:\n\t" + deserE.getDetails());
    assertNull(deserE.getDetails());
  }

  @Test
  public void hypothesis2d() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2d (master with 2 details, created using persist, details touched, serialized and deserialized outside transaction)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master after persist:\n\t" + e);
    System.out.println("detail A after persist:\n\t" + slcA);
    System.out.println("detail B after persist:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    assertFalse(em.contains(e));
    fromDbE.getDetails();
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master from DB:\n\t" + fromDbE);
    assertNotNull(fromDbE.$details); // set should be initialized
    System.out.println("$details of master retrieved from DB:\n\t" + fromDbE.$details);
    assertNotNull(fromDbE.getDetails());
    System.out.println("details of master retrieved from DB:\n\t" + fromDbE.getDetails());

    Master deserE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("master from ser:\n\t" + deserE);
    assertNotNull(deserE.$details);
    System.out.println("$details of master from ser file:\n\t" + deserE.$details);
    assertNotNull(deserE.getDetails());
    System.out.println("details from master from ser:\n\t" + deserE.getDetails());
  }

  @Test
  public void hypothesis2e() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2e (master with 2 details, created using persist, touching details on managed entities)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    System.out.println("master after persist:\n\t" + e);
    System.out.println("detail A after persist:\n\t" + slcA);
    System.out.println("detail B after persist:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    assertFalse(em.contains(e));
    tx.commit();
    tx = null;

    System.out.println("managed master from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    System.out.println("$details of managed master:\n\t" + fromDbE.$details);
    assertNotNull(fromDbE.getDetails());
    System.out.println("details of managed master:\n\t" + fromDbE.getDetails());
    assertNotNull(fromDbE.$details);
    System.out.println("$details of managed master:\n\t" + fromDbE.$details);
    System.out.println("BEFORE: $DETAILS IS NOT INITIALIZED AND NULL");
    System.out.println("GETDETAILS: GETDETAILS IS INITIALIZED AND NOT NULL");
    System.out.println("AFTER: $DETAILS IS INITIALIZED AND NOT NULL");
    System.out.println("THIS MEANS WITHOUT A DOUBT THAT JPA FIDLES WITH THE BODY OF GETDETAILS!!!!!!");
    System.out.println("JUST BY CALLING getDetails(), and not $details !!!! the collection gets initialized");

    em.close();
    em = null;

    Master deserE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("master from ser:\n\t" + deserE);
    assertNotNull(deserE.$details);
    System.out.println("$details of master from ser file:\n\t" + deserE.$details);
    System.out.println("NOTE THAT AFTER SERIALIZATION+DESERIALIZATION, $details IS NOT NULL: reason: touch of getDetails!!!!");
    System.out.println("details from master from ser:\n\t" + deserE.getDetails());
    assertNotNull(deserE.getDetails());
  }







  // THE ABOVE MEANS THAT WE NEED TO DO MORE TO MAKE SURE THAT WE DO NOT SEND DETAILS OVER THE WIRE

  private Master serAndDeserMaster(Master e) throws FileNotFoundException,
  IOException,
  ClassNotFoundException {
    serMaster(e);
    Master deserE = deserMaster();
    return deserE;
  }

  private Master deserMaster() throws FileNotFoundException,
                                      IOException,
                                      ClassNotFoundException {
    File iSerFile = new File($serFileName);
    FileInputStream fis = new FileInputStream(iSerFile);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Master deserE = (Master)ois.readObject();
    ois.close();
    ois = null;
    fis.close();
    fis = null;
    iSerFile.delete();
    iSerFile = null;
    return deserE;
  }

  private void serMaster(Master e) throws FileNotFoundException, IOException {
    $cwdName = System.getProperty("user.dir");
    $serFileName = $cwdName + "/hypothesis.ser";
    System.out.println($serFileName);
    File oSerFile = new File($serFileName);
    FileOutputStream fos = new FileOutputStream(oSerFile);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(e);
    oos.flush();
    oos.close();
    oos = null;
    fos.flush();
    fos.close();
    fos = null;
    oSerFile = null;
  }

  private Detail createDetailB(Master e) {
    Detail slcB = new Detail();
    slcB.setDate(DETAIL_DATE_B);
    slcB.setMaster(e);
    return slcB;
  }

  private Detail createDetailA(Master e) {
    Detail slcA = new Detail();
    slcA.setDate(DETAIL_DATE_A);
    slcA.setMaster(e);
    return slcA;
  }

  /**
   * @return (date1 == null) ? (date2 == null) : dayDate(date1).equals(dayDate(date2));
   */
  public static boolean sameDay(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      GregorianCalendar cal1 = new GregorianCalendar();
      cal1.setTime(date1);
      GregorianCalendar cal2 = new GregorianCalendar();
      cal2.setTime(date2);
      return fieldEqual(cal1, cal2, Calendar.YEAR) &&
             fieldEqual(cal1, cal2, Calendar.MONTH) &&
             fieldEqual(cal1, cal2, Calendar.DAY_OF_MONTH);
    }
    else {
      return date1 == date2; // they both have to be null
    }
  }

  private static boolean fieldEqual(Calendar cal1, Calendar cal2, int field) {
    assert cal1 != null;
    assert cal2 != null;
    return (! cal1.isSet(field)) ?
             (! cal2.isSet(field)) :
             (cal2.isSet(field) && (cal1.get(field) == cal2.get(field)));
  }

}
