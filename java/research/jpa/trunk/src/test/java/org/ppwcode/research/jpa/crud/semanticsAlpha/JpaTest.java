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

public class JpaTest {

  static final String PERSISTENCE_UNIT_NAME = "be_hdp_contracts_I_IBMOpenJPA_test";

  public final static String ENTERPRISE_NAME_0 = "HYPOTHESIS-NAME";
  public final static String ENTERPRISE_ADDRESS_0 = "HYPOTHESIS ADDRESS";
  public final static String ENTERPRISE_ENTERPRISE_ID_0 = "0123 456 789";
  public final static Date ENTERPRISE_TERMINATION_DATE_0 = new Date();

  public final static Date CONTRACT_START_DATE_A = new Date();
  public final static Date CONTRACT_TERMINATION_DATE_A = new Date();

  public final static Date CONTRACT_START_DATE_B = new Date();
  public final static Date CONTRACT_TERMINATION_DATE_B = new Date();
  private String $cwdName;
  private String $serFileName;

  private void assertEnterprise0(Integer eId, Master fromDbE) {
    assertEquals(eId, fromDbE.getPersistenceId());
    assertEquals(ENTERPRISE_NAME_0, fromDbE.getName());
    assertEquals(ENTERPRISE_ADDRESS_0, fromDbE.getAddress());
    assertEquals(ENTERPRISE_ENTERPRISE_ID_0, fromDbE.getEnterpriseId());
    assertTrue(sameDay(ENTERPRISE_TERMINATION_DATE_0, fromDbE.getTerminationDate()));
  }

  private Master createEnterprise0() {
    Master e = new Master();
    e.setName(ENTERPRISE_NAME_0);
    e.setAddress(ENTERPRISE_ADDRESS_0);
    e.setEnterpriseId(ENTERPRISE_ENTERPRISE_ID_0);
    e.setTerminationDate(ENTERPRISE_TERMINATION_DATE_0);
    return e;
  }

  @Test
  public void hypothesis1() {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1 (enterprise without contract, saved with merge)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createEnterprise0();
    assertFalse(em.contains(e));
    Master persistedE = em.merge(e);
    assertFalse(em.contains(e));
    System.out.println("NOTE THAT enterprise IS NOT IN entity manager AFTER MERGE");
    assertTrue(em.contains(persistedE));
    System.out.println("NOTE THAT persisted enterprise IS IN entity manager AFTER MERGE");
    tx.commit();
    assertFalse(em.contains(e));
    System.out.println("NOTE THAT enterprise IS NOT IN entity manager AFTER MERGE");
    assertTrue(em.contains(persistedE));
    System.out.println("NOTE THAT persisted enterprise IS STILL IN entity manager AFTER COMMIT");
    tx = null;
    em = null;

    Integer eId = persistedE.getPersistenceId();
    System.out.println("id of persisted enterprise: " + eId);
    System.out.println("RAM enterprise:\n\t" + e);
    System.out.println("persisted version of RAM enterprise:\n\t" + persistedE);
    assertNotSame(e, persistedE);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em = null;
    assertEnterprise0(eId, fromDbE);
    assertNotSame(persistedE, fromDbE);
    System.out.println("enterprise retrieved from DB:\n\t" + fromDbE);
    System.out.println("$contracts of enterprise retrieved from DB is null?: " + (fromDbE.$contracts == null));
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    assertNull(fromDbE.$contracts); // ok; set is not initialized
    System.out.println("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getContracts());
    System.out.println("IT IS ABSOLUTELY INCREDIBLE THAT THE ABOVE IS NOT NULL; THERE IS MAGIC HERE");
    assertNotNull(fromDbE.getContracts()); // there is no code that makes this possible in Enterprise! This is completely weird!
    assertTrue(fromDbE.getContracts().isEmpty());
  }

  @Test
  public void hypothesis1a() {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1a (enterprise without contract, saved with merge --> we also have primary key)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createEnterprise0();
    assertFalse(em.contains(e));
    System.out.println("RAM enterprise:\n\t" + e);
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    em.persist(e);
    assertTrue(em.contains(e));
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    System.out.println("RAM enterprise, persisted, uncommitted:\n\t" + e);
    tx.commit();
    tx = null;
    assertTrue(em.contains(e));
    System.out.println("NOTE THAT enterprise IS STILL IN entity manager AFTER COMMIT");
    em = null;
    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    System.out.println("RAM enterprise, persisted, committed:\n\t" + e);

    Integer eId = e.getPersistenceId();
    System.out.println("id of persisted enterprise: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    assertFalse(em.contains(e));
    Master fromDbE = em.find(Master.class, eId);
    assertTrue(em.contains(fromDbE));
    tx.commit();
    tx = null;
    assertTrue(em.contains(fromDbE));
    System.out.println("NOTE THAT entity manager STILL CONTAINS RETRIEVED enterprise AFTER COMMIT");
    em = null;
    assertEnterprise0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    assertNull(fromDbE.$contracts); // ok; set is not initialized
    System.out.println("enterprise retrieved from DB:\n\t" + fromDbE);
    System.out.println("$contracts of enterprise retrievede from DB:\n\t" + fromDbE.$contracts);
    assertNull(fromDbE.$contracts);
    System.out.println("contracts of enterprise retrievede from DB:\n\t" + fromDbE.getContracts());
    System.out.println("IT IS ABSOLUTELY INCREDIBLE THAT THE ABOVE IS NOT NULL; THERE IS MAGIC HERE");
    assertNotNull(fromDbE.getContracts());
    assertTrue(fromDbE.getContracts().isEmpty());
  }

  @Test
  public void hypothesis1b() throws IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis1b  (enterprise without contract, saved with persist, serialized and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createEnterprise0();
    System.out.println("RAM enterprise:\n\t" + e);
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    em.persist(e);
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    System.out.println("RAM enterprise, after persist:\n\t" + e);
    tx.commit();
    tx = null;
    em = null;
    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    System.out.println("RAM enterprise, after commit:\n\t" + e);

    Integer eId = e.getPersistenceId();
    System.out.println("id of persisted enterprise: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em = null;
    assertEnterprise0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    System.out.println("enterprise retrieved from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$contracts); // ok; set is not initialized
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    System.out.println("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getContracts());
    assertNotNull(fromDbE.getContracts());
    assertTrue(fromDbE.getContracts().isEmpty());

    Master deserE = serAndDeserEnterprise(e);

    assertEnterprise0(eId, deserE);
    assertNotSame(e, deserE);
    System.out.println("enterprise from ser file:\n\t" + deserE);
    assertNotNull(fromDbE.$contracts);
    System.out.println("$contracts of enterprise from ser file:\n\t" + deserE.$contracts);
    System.out.println("type of $contracts of enterprise from ser file:\n\t" + deserE.$contracts.getClass());
    System.out.println("NOTE THAT AFTER DESERIALIZATION, $contracts IS NOT NULL");
    System.out.println("contracts of enterprise from ser file:\n\t" + deserE.getContracts());
    assertNotNull(deserE.getContracts());
    assertTrue(deserE.getContracts().isEmpty());
  }

  @Test
  public void hypothesis2a() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2a (enterprise with 2 contracts, created using persist, serialized outside transaction and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createEnterprise0();
    Detail slcA = createContractA(e);
    Detail slcB = createContractB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em = null;
    System.out.println("enterprise in DB:\n\t" + e);
    System.out.println("contract A in DB:\n\t" + slcA);
    System.out.println("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    tx.commit();
    tx = null;
    em = null;

    assertEnterprise0(eId, fromDbE);
    System.out.println("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$contracts); // ok; set is not initialized
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    System.out.println("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getContracts());
    assertNotNull(fromDbE.$contracts); // set is initialized
    System.out.println("NOTE THAT THE enterprise IS STILL ATTACHED, EVEN THOUGH THE TRANSACTION IS COMMITTED, AND THE entity manager IS SET TO null");
    assertNotNull(fromDbE.getContracts());
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    System.out.println("type of $contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts.getClass());
    assertFalse(fromDbE.getContracts().isEmpty()); // this is totally unexpected, since the collection is lazy and never touched while attached


    Master deserE = serAndDeserEnterprise(fromDbE);

    assertEnterprise0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("enterprise from ser:\n\t" + deserE);
    assertNotNull(fromDbE.$contracts);
    System.out.println("$contracts of enterprise from ser file:\n\t" + deserE.$contracts);
    System.out.println("type of $contracts of enterprise from ser file:\n\t" + deserE.$contracts.getClass());
    System.out.println("NOTE THAT AFTER DESERIALIZATION, $contracts IS NOT NULL");
    System.out.println("contracts from enterprise from ser:\n\t" + deserE.getContracts());
    assertNotNull(deserE.getContracts());
    assertFalse(deserE.getContracts().isEmpty()); // ok, what goes in serialization comes out
  }

  @Test
  public void hypothesis2b() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2b (enterprise with 2 contracts, created using persist, serialized inside transaction and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createEnterprise0();
    Detail slcA = createContractA(e);
    Detail slcB = createContractB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em = null;
    System.out.println("enterprise in DB:\n\t" + e);
    System.out.println("contract A in DB:\n\t" + slcA);
    System.out.println("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    serEnterprise(fromDbE);
    tx.commit();
    tx = null;
    em = null;
    System.out.println("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$contracts); // ok; set is not initialized
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    System.out.println("contracts of enterprise retrieved from DB:\n\t" + fromDbE.getContracts());
    assertNotNull(fromDbE.$contracts); // set is initialized
    System.out.println("NOTE THAT THE enterprise IS STILL ATTACHED, EVEN THOUGH THE TRANSACTION IS COMMITTED, AND THE entity manager IS SET TO null");
    assertNotNull(fromDbE.getContracts());
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    System.out.println("type of $contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts.getClass());

    Master deserE = deserEnterprise();

    assertEnterprise0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("enterprise from ser:\n\t" + deserE);
    assertNotNull(fromDbE.$contracts);
    System.out.println("$contracts of enterprise from ser file:\n\t" + deserE.$contracts);
    System.out.println("NOTE THAT AFTER DESERIALIZATION, $contracts IS NULL: reason: serialized before toString of collection (even outside transaction)");
    System.out.println("contracts from enterprise from ser:\n\t" + deserE.getContracts());
    assertNull(deserE.getContracts());
  }

  @Test
  public void hypothesis2c() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2c (enterprise with 2 contracts, created using persist, serialized and deserialized, outside trans, with slight touch on contracts)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createEnterprise0();
    Detail slcA = createContractA(e);
    Detail slcB = createContractB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em = null;
    System.out.println("enterprise in DB:\n\t" + e);
    System.out.println("contract A in DB:\n\t" + slcA);
    System.out.println("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    tx.commit();
    tx = null;
    em = null;
    System.out.println("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$contracts); // ok; set is not initialized
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    assertNotNull(fromDbE.getContracts());
    System.out.println("YET SOME MAGIC: $CONTRACTS NOT INITIALIZED AND NULL, BUT GETCONTRACTS NOT NULL");
    System.out.println("THIS MEANS WITHOUT A DOUBT THAT JPA FIDLES WITH THE BODY OF GETCONTRACTS!!!!!!");
    System.out.println("JUST BY CALLING getContracts(), and not $contracts !!!! the collection gets initialized");
    System.out.println("COMPARE THIS TO 2D: SAME CODE, NO GETCONTRACTS");

    Master deserE = serAndDeserEnterprise(fromDbE);

    assertEnterprise0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("enterprise from ser:\n\t" + deserE);
    assertNotNull(fromDbE.$contracts);
    System.out.println("$contracts of enterprise from ser file:\n\t" + deserE.$contracts);
    System.out.println("NOTE THAT AFTER DESERIALIZATION, $contracts IS NOT NULL: reason: slight touch of getContracts!!!!");
    System.out.println("contracts from enterprise from ser:\n\t" + deserE.getContracts());
    assertNotNull(deserE.getContracts());
  }

  @Test
  public void hypothesis2d() throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("hypothesis2d (enterprise with 2 contracts, created using persist, serialized without any init, and deserialized)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createEnterprise0();
    Detail slcA = createContractA(e);
    Detail slcB = createContractB(e);
    em.persist(e);
    em.persist(slcA); // note: persist works in creation, merge does not
    em.persist(slcB);
    tx.commit();
    tx = null;
    em = null;
    System.out.println("enterprise in DB:\n\t" + e);
    System.out.println("contract A in DB:\n\t" + slcA);
    System.out.println("contract B in DB:\n\t" + slcB);

    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.contains(e);
    tx.commit();
    tx = null;
    em = null;
    System.out.println("enterprise from DB:\n\t" + fromDbE);
    assertNull(fromDbE.$contracts); // ok; set is not initialized
    System.out.println("$contracts of enterprise retrieved from DB:\n\t" + fromDbE.$contracts);
    System.out.println("WE NEVER CALLED GETCONTRACTS; NOW SERIALIZING");

    Master deserE = serAndDeserEnterprise(fromDbE);

    assertEnterprise0(eId, deserE);
    assertNotSame(fromDbE, deserE);
    System.out.println("enterprise from ser:\n\t" + deserE);
    assertNull(fromDbE.$contracts);
    System.out.println("$contracts of enterprise from ser file:\n\t" + deserE.$contracts);
    System.out.println("NOTE THAT AFTER DESERIALIZATION, $contracts IS NULL: reason: serialized before toString of collection (even outside transaction)");
    System.out.println("contracts from enterprise from ser:\n\t" + deserE.getContracts());
    assertNull(deserE.getContracts());
  }

  // THE ABOVE MEANS THAT WE NEED TO DO MORE TO MAKE SURE THAT WE DO NOT SEND CONTRACTS OVER THE WIRE

  private Master serAndDeserEnterprise(Master e) throws FileNotFoundException,
  IOException,
  ClassNotFoundException {
    serEnterprise(e);
    Master deserE = deserEnterprise();
    return deserE;
  }

  private Master deserEnterprise() throws FileNotFoundException,
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

  private void serEnterprise(Master e) throws FileNotFoundException, IOException {
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

  private Detail createContractB(Master e) {
    Detail slcB = new Detail();
    slcB.setStartDate(CONTRACT_START_DATE_B);
    slcB.setTerminationDate(CONTRACT_TERMINATION_DATE_B);
    slcB.setEnterprise(e);
    return slcB;
  }

  private Detail createContractA(Master e) {
    Detail slcA = new Detail();
    slcA.setStartDate(CONTRACT_START_DATE_A);
    slcA.setTerminationDate(CONTRACT_TERMINATION_DATE_A);
    slcA.setEnterprise(e);
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
