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
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

public class JpaATest {

  final static Logger LOGGER = Logger.getLogger("ValueHandlersTest");

  static final String PERSISTENCE_UNIT_NAME = "test_openjpa_master_detail";

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
    System.out.println("hypothesis1 (master without detail, saved with merge)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    assertFalse(em.contains(e));
    Master persistedE = em.merge(e);
    assertFalse(em.contains(e));
    LOGGER.fine("NOTE THAT enterprise IS NOT IN entity manager AFTER MERGE");
    assertTrue(em.contains(persistedE));
    LOGGER.fine("NOTE THAT persisted enterprise IS IN entity manager AFTER MERGE");
    tx.commit();
    assertFalse(em.contains(e));
    LOGGER.fine("NOTE THAT enterprise IS NOT IN entity manager AFTER MERGE");
    assertTrue(em.contains(persistedE));
    LOGGER.fine("NOTE THAT persisted enterprise IS STILL IN entity manager AFTER COMMIT");
    tx = null;
    em = null;

    Integer eId = persistedE.getPersistenceId();
    LOGGER.fine("id of persisted enterprise: " + eId);
    LOGGER.fine("RAM enterprise:\n\t" + e);
    LOGGER.fine("persisted version of RAM enterprise:\n\t" + persistedE);
    assertNotSame(e, persistedE);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em = null;
    assertMaster0(eId, fromDbE);
    assertNotSame(persistedE, fromDbE);
    LOGGER.fine("enterprise retrieved from DB:\n\t" + fromDbE);
    LOGGER.fine("$contracts of enterprise retrieved from DB is null?: " + (fromDbE.$details == null));
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    assertNull(fromDbE.$details); // ok; set is not initialized
    LOGGER.fine("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getDetails());
    LOGGER.fine("IT IS ABSOLUTELY INCREDIBLE THAT THE ABOVE IS NOT NULL; THERE IS MAGIC HERE");
    assertNotNull(fromDbE.getDetails()); // there is no code that makes this possible in Enterprise! This is completely weird!
    assertTrue(fromDbE.getDetails().isEmpty());
  }

  @Test
  public void hypothesis1a() {
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1a (enterprise without contract, saved with merge --> we also have primary key)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    assertFalse(em.contains(e));
    LOGGER.fine("RAM enterprise:\n\t" + e);
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    em.persist(e);
    assertTrue(em.contains(e));
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    LOGGER.fine("RAM enterprise, persisted, uncommitted:\n\t" + e);
    tx.commit();
    tx = null;
    assertTrue(em.contains(e));
    LOGGER.fine("NOTE THAT enterprise IS STILL IN entity manager AFTER COMMIT");
    em = null;
    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    LOGGER.fine("RAM enterprise, persisted, committed:\n\t" + e);

    Integer eId = e.getPersistenceId();
    LOGGER.fine("id of persisted enterprise: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    assertFalse(em.contains(e));
    Master fromDbE = em.find(Master.class, eId);
    assertTrue(em.contains(fromDbE));
    tx.commit();
    tx = null;
    assertTrue(em.contains(fromDbE));
    LOGGER.fine("NOTE THAT entity manager STILL CONTAINS RETRIEVED enterprise AFTER COMMIT");
    em = null;
    assertMaster0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    LOGGER.fine("enterprise retrieved from DB:\n\t" + fromDbE);
    LOGGER.fine("$contracts of enterprise retrievede from DB:\n\t" + fromDbE.$details);
    assertNull(fromDbE.$details);
    LOGGER.fine("contracts of enterprise retrievede from DB:\n\t" + fromDbE.getDetails());
    LOGGER.fine("IT IS ABSOLUTELY INCREDIBLE THAT THE ABOVE IS NOT NULL; THERE IS MAGIC HERE");
    assertNotNull(fromDbE.getDetails());
    assertTrue(fromDbE.getDetails().isEmpty());
  }

  @Test
  public void hypothesis1b() throws IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1b  (enterprise without contract, saved with persist, serialized and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    LOGGER.fine("RAM enterprise:\n\t" + e);
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    em.persist(e);
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    LOGGER.fine("RAM enterprise, after persist:\n\t" + e);
    tx.commit();
    tx = null;
    em = null;
    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    LOGGER.fine("RAM enterprise, after commit:\n\t" + e);

    Integer eId = e.getPersistenceId();
    LOGGER.fine("id of persisted enterprise: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em = null;
    assertMaster0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    LOGGER.fine("enterprise retrieved from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    LOGGER.fine("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNotNull(fromDbE.getDetails());
    assertTrue(fromDbE.getDetails().isEmpty());

    Master deserE = serAndDeserMaster(e);

    assertMaster0(eId, deserE);
    assertNotSame(e, deserE);
    LOGGER.fine("enterprise from ser file:\n\t" + deserE);
    assertNotNull(fromDbE.$details);
    LOGGER.fine("$contracts of enterprise from ser file:\n\t" + deserE.$details);
    LOGGER.fine("type of $contracts of enterprise from ser file:\n\t" + deserE.$details.getClass());
    LOGGER.fine("NOTE THAT AFTER DESERIALIZATION, $contracts IS NOT NULL");
    LOGGER.fine("contracts of enterprise from ser file:\n\t" + deserE.getDetails());
    assertNotNull(deserE.getDetails());
    assertTrue(deserE.getDetails().isEmpty());
  }

  @Test
  public void hypothesis2a() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2a (enterprise with 2 contracts, created using persist, serialized outside transaction and deserialized)");
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
    em = null;
    LOGGER.fine("enterprise in DB:\n\t" + e);
    LOGGER.fine("contract A in DB:\n\t" + slcA);
    LOGGER.fine("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    tx.commit();
    tx = null;
    em = null;

    assertMaster0(eId, fromDbE);
    LOGGER.fine("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    LOGGER.fine("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNotNull(fromDbE.$details); // set is initialized
    LOGGER.fine("NOTE THAT THE enterprise IS STILL ATTACHED, EVEN THOUGH THE TRANSACTION IS COMMITTED, AND THE entity manager IS SET TO null");
    assertNotNull(fromDbE.getDetails());
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    LOGGER.fine("type of $contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details.getClass());
    assertFalse(fromDbE.getDetails().isEmpty()); // this is totally unexpected, since the collection is lazy and never touched while attached


    Master deserE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    LOGGER.fine("enterprise from ser:\n\t" + deserE);
    assertNotNull(fromDbE.$details);
    LOGGER.fine("$contracts of enterprise from ser file:\n\t" + deserE.$details);
    LOGGER.fine("type of $contracts of enterprise from ser file:\n\t" + deserE.$details.getClass());
    LOGGER.fine("NOTE THAT AFTER DESERIALIZATION, $contracts IS NOT NULL");
    LOGGER.fine("contracts from enterprise from ser:\n\t" + deserE.getDetails());
    assertNotNull(deserE.getDetails());
    assertFalse(deserE.getDetails().isEmpty()); // ok, what goes in serialization comes out
  }

  @Test
  public void hypothesis2b() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2b (enterprise with 2 contracts, created using persist, serialized inside transaction and deserialized)");
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
    em = null;
    LOGGER.fine("enterprise in DB:\n\t" + e);
    LOGGER.fine("contract A in DB:\n\t" + slcA);
    LOGGER.fine("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    serMaster(fromDbE);
    tx.commit();
    tx = null;
    em = null;
    LOGGER.fine("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    LOGGER.fine("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getDetails());
    assertNotNull(fromDbE.$details); // set is initialized
    LOGGER.fine("NOTE THAT THE enterprise IS STILL ATTACHED, EVEN THOUGH THE TRANSACTION IS COMMITTED, AND THE entity manager IS SET TO null");
    assertNotNull(fromDbE.getDetails());
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    LOGGER.fine("type of $contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details.getClass());

    Master deserE = deserMaster();

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    LOGGER.fine("enterprise from ser:\n\t" + deserE);
    assertNotNull(fromDbE.$details);
    LOGGER.fine("$contracts of enterprise from ser file:\n\t" + deserE.$details);
    LOGGER.fine("NOTE THAT AFTER DESERIALIZATION, $contracts IS NULL: reason: serialized before toString of collection (even outside transaction)");
    LOGGER.fine("contracts from enterprise from ser:\n\t" + deserE.getDetails());
    assertNull(deserE.getDetails());
  }

  @Test
  public void hypothesis2c() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2c (enterprise with 2 contracts, created using persist, serialized and deserialized, outside trans, with slight touch on contracts)");
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
    em = null;
    LOGGER.fine("enterprise in DB:\n\t" + e);
    LOGGER.fine("contract A in DB:\n\t" + slcA);
    LOGGER.fine("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    tx.commit();
    tx = null;
    em = null;
    LOGGER.fine("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    assertNotNull(fromDbE.getDetails());
    LOGGER.fine("YET SOME MAGIC: $DETAILS NOT INITIALIZED AND NULL, BUT GETDETAILS NOT NULL");
    LOGGER.fine("THIS MEANS WITHOUT A DOUBT THAT JPA FIDLES WITH THE BODY OF GETDETAILS!!!!!!");
    LOGGER.fine("JUST BY CALLING getContracts(), and not $contracts !!!! the collection gets initialized");
    LOGGER.fine("COMPARE THIS TO 2D: SAME CODE, NO GETDETAILS");

    Master deserE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    LOGGER.fine("enterprise from ser:\n\t" + deserE);
    assertNotNull(fromDbE.$details);
    LOGGER.fine("$contracts of enterprise from ser file:\n\t" + deserE.$details);
    LOGGER.fine("NOTE THAT AFTER DESERIALIZATION, $contracts IS NOT NULL: reason: slight touch of getContracts!!!!");
    LOGGER.fine("contracts from enterprise from ser:\n\t" + deserE.getDetails());
    assertNotNull(deserE.getDetails());
  }

  @Test
  public void hypothesis2d() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2d (enterprise with 2 contracts, created using persist, serialized without any init, and deserialized)");
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
    em = null;
    LOGGER.fine("enterprise in DB:\n\t" + e);
    LOGGER.fine("contract A in DB:\n\t" + slcA);
    LOGGER.fine("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    tx.commit();
    tx = null;
    em = null;
    LOGGER.fine("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$details); // ok; set is not initialized
    LOGGER.fine("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$details);
    LOGGER.fine("WE NEVER CALLED GETDETAILS; NOW SERIALIZING");

    Master deserE = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    LOGGER.fine("enterprise from ser:\n\t" + deserE);
    assertNull(fromDbE.$details);
    LOGGER.fine("$contracts of enterprise from ser file:\n\t" + deserE.$details);
    LOGGER.fine("NOTE THAT AFTER DESERIALIZATION, $contracts IS NULL: reason: serialized before toString of collection (even outside transaction)");
    LOGGER.fine("contracts from enterprise from ser:\n\t" + deserE.getDetails());
    assertNull(deserE.getDetails());
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
    LOGGER.fine($serFileName);
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
