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

package org.ppwcode.research.jpa.crud.semanticsBeta;

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
import javax.persistence.PersistenceException;

import org.apache.openjpa.persistence.ArgumentException;
import org.junit.BeforeClass;
import org.junit.Test;

public class JpaBTest {

  static final String PERSISTENCE_UNIT_NAME = "be_hdp_contracts_I_IBMOpenJPA_test";
  //static final String PERSISTENCE_UNIT_NAME = "be_hdp_contracts_I";

  final static Logger LOGGER = Logger.getLogger("JpaBTest");

  public final static String MASTER_NAME_0 = "HYPOTHESIS-NAME";
  public final static String MASTER_NAME_1 = "HYPOTHESIS-NEW-NAME";

  public final static Date DETAIL_DATE_A = new Date();
  public final static Date DETAIL_DATE_B = new Date();

  private String $cwdName;
  private String $serFileName;

  @BeforeClass
  public static void updateDateB() {
    DETAIL_DATE_B.setTime(DETAIL_DATE_A.getTime()+9876543);
  }

  private void assertMaster0(Integer eId, Master fromDbE) {
    assertEquals(eId, fromDbE.getPersistenceId());
    assertEquals(MASTER_NAME_0, fromDbE.getName());
  }

  private void assertMaster1(Integer eId, Master fromDbE) {
    assertEquals(eId, fromDbE.getPersistenceId());
    assertEquals(MASTER_NAME_1, fromDbE.getName());
  }

  private void assertDetailA(Integer eId, Detail fromDbE) {
    assertEquals(eId, fromDbE.getPersistenceId());
    assertEquals(DETAIL_DATE_A, fromDbE.getDate());
  }

  private void assertDetailB(Integer eId, Detail fromDbE) {
    assertEquals(eId, fromDbE.getPersistenceId());
    assertEquals(DETAIL_DATE_B, fromDbE.getDate());
  }

  private Master createMaster0() {
    Master e = new Master();
    e.setName(MASTER_NAME_0);
    return e;
  }

  private void displayTest(String msg1, String msg2) {
    System.out.println();
    System.out.println();
    System.out.println(msg1);
    System.out.println(msg2);
  }

  @Test
  public void displayMessage() {
    System.out.println();
    System.out.println();
    System.out.println("TESTS WITH MODIFIED SERIALIZATION");
    System.out.println("=================================");
    System.out.println("Master-Detail uses new serialization methods.  Constraint is that there cannot be any cyclic references");
    System.out.println("in the object graph.  During the tests, detached objects should be serialized and deserialized before");
    System.out.println("any operations or assertions.");
    System.out.println();
    System.out.println();
  }

  @Test
  public void checkDates() {
    assertFalse(DETAIL_DATE_A.equals(DETAIL_DATE_B));
  }

  @Test
  public void hypothesis1a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("RETRIEVE MASTER WITHOUT DETAILS",
        "hypothesis1a (master without detail, saved with merge)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    assertFalse(em.contains(e));
    Master persistedE = em.merge(e);
    assertFalse(em.contains(e));
    assertTrue(em.contains(persistedE));
    tx.commit();
    assertFalse(em.contains(e));
    assertTrue(em.contains(persistedE));
    em.close();

    Integer eId = persistedE.getPersistenceId();
    assertNotSame(e, persistedE);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master m = serAndDeserMaster(fromDbE);

    assertMaster0(eId, m);
    assertNotSame(persistedE, m);
    assertNotNull(m.$details);
    assertTrue(m.$details.size() == 0);
    assertNotNull(m.getDetails());
    assertTrue(m.getDetails().size() == 0);
  }

  @Test
  public void hypothesis1b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("RETRIEVE MASTER WITHOUT DETAILS",
      "hypothesis1b (master without detail, saved with persist)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    assertFalse(em.contains(e));
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    em.persist(e);
    assertTrue(em.contains(e));
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    tx.commit();
    assertTrue(em.contains(e));
    em.close();

    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    assertFalse(em.contains(e));
    Master fromDbE = em.find(Master.class, eId);
    assertTrue(em.contains(fromDbE));
    tx.commit();
    assertTrue(em.contains(fromDbE));
    em.close();

    Master m = serAndDeserMaster(fromDbE);

    assertMaster0(eId, m);
    assertNotSame(e, m);
    assertNotNull(m.$details);
    assertTrue(m.$details.size() == 0);
    assertNotNull(m.getDetails());
    assertTrue(m.getDetails().size() == 0);
  }

  @Test
  public void hypothesis1c() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("RETRIEVE MASTER WITHOUT DETAILS",
      "hypothesis1b (master without detail, saved with persist, touch detail)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    assertFalse(em.contains(e));
    assertNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    em.persist(e);
    assertTrue(em.contains(e));
    assertNotNull(e.getPersistenceId());
    assertNull(e.getPersistenceVersion());
    tx.commit();
    assertTrue(em.contains(e));
    em.close();

    assertNotNull(e.getPersistenceId());
    assertNotNull(e.getPersistenceVersion());
    Integer eId = e.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    assertFalse(em.contains(e));
    Master fromDbE = em.find(Master.class, eId);
    assertTrue(em.contains(fromDbE));
    fromDbE.getDetails();
    tx.commit();
    assertTrue(em.contains(fromDbE));
    em.close();

    Master m = serAndDeserMaster(fromDbE);

    assertMaster0(eId, m);
    assertNotSame(e, m);
    assertNotNull(m.$details);
    assertTrue(m.$details.size() == 0);
    assertNotNull(m.getDetails());
    assertTrue(m.getDetails().size() == 0);
  }

  @Test
  public void hypothesis2a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("RETRIEVE MASTER WITH DETAILS",
        "hypothesis2a (master with 2 details, created using persist)");
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
    em.close();

    Integer eId = e.getPersistenceId();

    assertMaster0(eId, e);
    assertNotNull(e.$details);
    assertTrue(e.$details.size()==2);
    assertNotNull(e.getDetails());
    assertTrue(e.getDetails().size()==2);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    assertFalse(em.contains(e));
    Master fromDbE = em.find(Master.class, eId);
    assertTrue(em.contains(fromDbE));
    tx.commit();
    assertTrue(em.contains(fromDbE));
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
  }

  @Test
  public void hypothesis2b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("RETRIEVE MASTER WITH DETAILS",
        "hypothesis2b (master with 2 details, created using persist, details touched)");
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
    em.close();

    Integer eId = e.getPersistenceId();

    assertMaster0(eId, e);
    assertNotNull(e.$details);
    assertTrue(e.$details.size()==2);
    assertNotNull(e.getDetails());
    assertTrue(e.getDetails().size()==2);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    assertFalse(em.contains(e));
    Master fromDbE = em.find(Master.class, eId);
    assertTrue(em.contains(fromDbE));
    fromDbE.getDetails();
    tx.commit();
    assertTrue(em.contains(fromDbE));
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
  }


  @Test
  public void hypothesis3a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITHOUT DETAILS: FIELD CHANGE",
        "hypothesis3a (master without detail, created with PERSIST, field change on managed master in transaction)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.setName(MASTER_NAME_1);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster1(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 1 == deserEe.getPersistenceVersion());

    System.out.println("master without details successfully modified and persistence version incremented");
  }

  @Test
  public void hypothesis3b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITHOUT DETAILS: FIELD CHANGE",
        "hypothesis3b (master without detail, created with PERSIST, field change on managed master in transaction)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.setName(MASTER_NAME_0);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 0 == deserEe.getPersistenceVersion());

    System.out.println("master without details successfully modified and persistence version incremented");
  }

  @Test
  public void hypothesis3c() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITHOUT DETAILS: FIELD CHANGE",
        "hypothesis3c (master without detail, created with PERSIST, field change on detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    e.setName(MASTER_NAME_1);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.merge(e);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster1(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 1 == deserEe.getPersistenceVersion());

    System.out.println("master without details successfully modified in detached state and persistence version incremented");
  }

  @Test
  public void hypothesis3d() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITHOUT DETAILS: FIELD CHANGE",
        "hypothesis3d (master without detail, created with PERSIST, field change with same value on detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    e.setName(MASTER_NAME_0);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.merge(e);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 0 == deserEe.getPersistenceVersion());

    System.out.println("master without details successfully modified with same value in detached state and persistence version stays the same");
  }

  @Test
  public void hypothesis4a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITH DETAILS: FIELD CHANGE",
        "hypothesis4a (master with 2 details, created using persist, field change on managed master in transaction)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA);
    em.persist(slcB);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.setName(MASTER_NAME_1);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster1(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 1 == deserEe.getPersistenceVersion());

    System.out.println("master with details successfully modified and persistence version incremented");
  }


  @Test
  public void hypothesis4b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITH DETAILS: FIELD CHANGE",
        "hypothesis4b (master with 2 details, created using persist, field change to same value on managed master in transaction)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA);
    em.persist(slcB);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.setName(MASTER_NAME_0);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 0 == deserEe.getPersistenceVersion());

    System.out.println("master with details successfully modified and persistence version incremented");
  }

  @Test
  public void hypothesis4c() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITH DETAILS: FIELD CHANGE",
    "hypothesis4c (master with 2 details, created using persist, field change on detached master, master merged)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA);
    em.persist(slcB);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    e.setName(MASTER_NAME_1);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.merge(e);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster1(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 1 == deserEe.getPersistenceVersion());

    System.out.println("master with details successfully modified and persistence version incremented");
  }

  @Test
  public void hypothesis4d() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITH DETAILS: FIELD CHANGE",
    "hypothesis4d (master with 2 details, created using persist, field change to same value on detached master, master merged)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA);
    em.persist(slcB);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    e.setName(MASTER_NAME_0);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.merge(e);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 0 == deserEe.getPersistenceVersion());

    System.out.println("master with details remains the same and persistence version not incremented");
  }

  @Test
  public void hypothesis4e() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MASTER WITH DETAILS: FIELD CHANGE",
        "hypothesis4e (master with 2 details, created using persist, field change on managed master in transaction, details touched)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Detail slcA = createDetailA(e);
    Detail slcB = createDetailB(e);
    em.persist(e);
    em.persist(slcA);
    em.persist(slcB);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    Integer eVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.setName(MASTER_NAME_1);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    fromDbE.getDetails();
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster1(eId, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);
    assertTrue(eVersion + 1 == deserEe.getPersistenceVersion());

    System.out.println("master with details successfully modified and persistence version incremented");
  }


  @Test
  public void hypothesis5a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("CREATE NEW MASTER",
        "hypothesis5a (master without details, created using persist)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    em.close();

    Integer eId = e.getPersistenceId();
    assertNotNull(eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(fromDbE, deserEe);
    assertNotSame(e, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);

    System.out.println("master without details successfully created with persist");
  }

  @Test
  public void hypothesis5b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("CREATE NEW MASTER",
        "hypothesis5b (master without details, created using merge)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Master mergedE = em.merge(e);
    tx.commit();
    em.close();

    Integer eId = mergedE.getPersistenceId();
    assertNotNull(eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    assertMaster0(eId, deserEe);
    assertNotSame(fromDbE, deserEe);
    assertNotSame(mergedE, deserEe);
    assertNotNull(deserEe.$details);
    assertTrue(deserEe.$details.size() == 0);
    assertNotNull(deserEe.getDetails());
    assertTrue(deserEe.getDetails().size() == 0);

    System.out.println("master without details successfully created with merge");
  }

  @Test
  public void hypothesis6a() {
    displayTest("DELETE MASTER WITHOUT DETAILS",
        "hypothesis6a (master without details, created using persist, remove managed master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer eId = e.getPersistenceId();
    assertNotNull(eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.remove(fromDbE);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNull(fromDbE);
    System.out.println("master without details is removed");
}

  @Test
  public void hypothesis6b() {
    displayTest("DELETE MASTER WITHOUT DETAILS",
        "hypothesis6b (master without details, created using merge, remove managed master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Master mergedE = em.merge(e);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer eId = mergedE.getPersistenceId();
    assertNotNull(eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    em.remove(fromDbE);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNull(fromDbE);
    System.out.println("master without details is removed");
  }

  @Test(expected=ArgumentException.class)
  public void hypothesis6c() {
    displayTest("DELETE MASTER WITHOUT DETAILS",
        "hypothesis6c (master without details, created using persist, remove detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer eId = e.getPersistenceId();
    assertNotNull(eId);

    System.out.println("EntityManager.remove() will throw an ArgumentException when the argument is a detached object.");
    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.remove(e);
    tx.commit();
    System.out.println("EntityManager DID NOT THROW AN ARGUMENTEXCEPTION");
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNull(fromDbE);
    System.out.println("master is removed");
}

  @Test(expected=ArgumentException.class)
  public void hypothesis6d() {
    displayTest("DELETE MASTER WITHOUT DETAILS",
        "hypothesis6d (master without details, created using merge, remove detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    Master mergedE = em.merge(e);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer eId = mergedE.getPersistenceId();
    assertNotNull(eId);

    System.out.println("EntityManager.remove() will throw an ArgumentException when the argument is a detached object.");
    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.remove(mergedE);
    tx.commit();
    System.out.println("EntityManager DID NOT THROW AN ARGUMENTEXCEPTION");
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNull(fromDbE);
    System.out.println("master is removed");
  }

  @Test(expected=PersistenceException.class)
  public void hypothesis7a() {
    displayTest("DELETE MASTER WITH DETAILS",
        "hypothesis7a (master with 2 details, created using persist, remove managed master in transaction)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer eId = e.getPersistenceId();
    LOGGER.fine("id of PERSISTed master: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.getDetails();
    LOGGER.fine("details : "+fromDbE.getDetails());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertMaster0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    assertTrue(fromDbE.getDetails().size()==2);
    for (Detail d : fromDbE.getDetails()) {
      assertTrue((d.getPersistenceId().compareTo(slcA.getPersistenceId())==0)||
          (d.getPersistenceId().compareTo(slcB.getPersistenceId())==0));
    }

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    em.remove(fromDbE);
    System.out.println("EntityManager.remove() will throw a PersistenceException because foreign key constraints will be violated.");
    tx.commit();
    System.out.println("EntityManager DID NOT THROW AN PERSISTENCEEXCEPTION");
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNull(fromDbE);
    System.out.println("master is removed");

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    System.out.println(slcA.getPersistenceId());
    System.out.println(slcB.getPersistenceId());
    Detail a = em.find(Detail.class, slcA.getPersistenceId());
    Detail b = em.find(Detail.class, slcB.getPersistenceId());
    LOGGER.fine("detail A after remove:\n\t" + a);
    LOGGER.fine("detail B after remove:\n\t" + b);
    System.out.println("detail A after remove:\n\t" + a);
    System.out.println("detail B after remove:\n\t" + b);

    assertNull(a);
    assertNull(b);

    tx.commit();
    tx = null;
    em.close();
    em = null;
  }

  @Test
  public void hypothesis7b() {
    displayTest("DELETE MASTER WITH DETAILS",
        "hypothesis7b (master with 2 details, created using persist, first remove details then remove managed master in transaction)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer eId = e.getPersistenceId();
    LOGGER.fine("id of PERSISTed master: " + eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.getDetails();
    LOGGER.fine("details : "+fromDbE.getDetails());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertMaster0(eId, fromDbE);
    assertNotSame(e, fromDbE);
    assertTrue(fromDbE.getDetails().size()==2);
    for (Detail d : fromDbE.getDetails()) {
      assertTrue((d.getPersistenceId().compareTo(slcA.getPersistenceId())==0)||
          (d.getPersistenceId().compareTo(slcB.getPersistenceId())==0));
    }

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    slcA = em.find(Detail.class, slcA.getPersistenceId());
    slcB = em.find(Detail.class, slcB.getPersistenceId());
    em.remove(slcA);
    em.remove(slcB);
    fromDbE = em.find(Master.class, eId);
    em.remove(fromDbE);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNull(fromDbE);
    System.out.println("master is removed");

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail a = em.find(Detail.class, slcA.getPersistenceId());
    Detail b = em.find(Detail.class, slcB.getPersistenceId());

    assertNull(a);
    assertNull(b);

    tx.commit();
    tx = null;
    em.close();
    em = null;
  }

  @Test
  public void hypothesis8a() {
    displayTest("RETRIEVE DETAIL",
        "hypothesis8a (master with 2 details, created using persist, retrieve detail)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    System.out.println("detail successfully retrieved, with master attached (master.getDetails() is null)");
  }

  @Test
  public void hypothesis8b() {
    displayTest("RETRIEVE DETAIL",
        "hypothesis8b (master with 2 details, created using persist, retrieve detail, touch master.getDetails())");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.getMaster().getDetails();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNotNull(fromDbA.getMaster().$details);
    assertNotNull(fromDbA.getMaster().getDetails());
    assertTrue(fromDbA.getMaster().getDetails().size()==2);
    for (Detail d : fromDbA.getMaster().getDetails()) {
      assertTrue((d.getPersistenceId().compareTo(slcA.getPersistenceId())==0)||
          (d.getPersistenceId().compareTo(slcB.getPersistenceId())==0));
    }

    System.out.println("detail successfully retrieved, with master attached " +
        "(master.getDetails() was touched and contains correct details)");
  }

  @Test
  public void hypothesis8c() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("RETRIEVE DETAIL",
        "hypothesis8c (master with 2 details, created using persist, retrieve detail, serialize and deserialize)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    Detail deserDA = serAndDeserDetail(fromDbA);
    assertNotNull(deserDA);
    assertDetailA(aId, deserDA);
    assertNotSame(slcA, deserDA);
    assertNotNull(deserDA.getMaster());
    assertMaster0(e.getPersistenceId(), deserDA.getMaster());
    assertNull(deserDA.getMaster().$details);
    assertNull(deserDA.getMaster().getDetails());

    System.out.println("detail successfully retrieved, with master attached (master.getDetails() is null," +
        " serialization succeeded)");
  }

  @Test
  public void hypothesis8d() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("RETRIEVE DETAIL",
        "hypothesis8d (master with 2 details, created using persist, retrieve detail, touch master.getDetails()," +
        " serialize and deserialize)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.getMaster().getDetails();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNotNull(fromDbA.getMaster().$details);
    assertNotNull(fromDbA.getMaster().getDetails());
    assertTrue(fromDbA.getMaster().getDetails().size()==2);
    for (Detail d : fromDbA.getMaster().getDetails()) {
      assertTrue((d.getPersistenceId().compareTo(slcA.getPersistenceId())==0)||
          (d.getPersistenceId().compareTo(slcB.getPersistenceId())==0));
    }

    Detail deserDA = serAndDeserDetail(fromDbA);
    assertNotNull(deserDA);
    assertDetailA(aId, deserDA);
    assertNotSame(slcA, deserDA);
    assertNotNull(deserDA.getMaster());
    assertMaster0(e.getPersistenceId(), deserDA.getMaster());
    assertNotNull(deserDA.getMaster().$details);
    assertNotNull(deserDA.getMaster().getDetails());
    assertTrue(deserDA.getMaster().getDetails().size()==2);
    for (Detail d : deserDA.getMaster().getDetails()) {
      assertTrue((d.getPersistenceId().compareTo(slcA.getPersistenceId())==0)||
          (d.getPersistenceId().compareTo(slcB.getPersistenceId())==0));
    }

    System.out.println("detail successfully retrieved, with master attached (master.getDetails() was touched and" +
        " contains correct details, serialization succeeded)");
  }

  @Test
  public void hypothesis9a() {
    displayTest("MODIFY DETAIL: FIELD CHANGE",
        "hypothesis9a (master with 2 details, created using persist, retrieve, modify and merge detail, using managed detail)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_B);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailB(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(aVersion + 1 == fromDbA.getPersistenceVersion());
    assertTrue(mVersion.equals(fromDbA.getMaster().getPersistenceVersion()));

    System.out.println("detail field change successfull, persistence version incremented with 1");
  }

  @Test
  public void hypothesis9b() {
    displayTest("MODIFY DETAIL: FIELD CHANGE",
        "hypothesis9b (master with 2 details, created using persist, retrieve, modify and merge detail," +
        " using managed detail, modify with same value)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_A);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(aVersion.compareTo(fromDbA.getPersistenceVersion())==0);
    assertTrue(mVersion.equals(fromDbA.getMaster().getPersistenceVersion()));

    System.out.println("detail field change with same value successful, persistence version stays the same");
  }

  @Test
  public void hypothesis9c() {
    displayTest("MODIFY DETAIL: FIELD CHANGE",
        "hypothesis9c (master with 2 details, created using persist, retrieve, modify detached detail, merge detail back)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    fromDbA.setDate(DETAIL_DATE_B);
    assertNotNull(fromDbA.getMaster().getName());
    System.out.println(" NOTE: DETACHED DETAIL HAS A DETACHED, COMPLETE, MASTER");

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail blah = em.merge(fromDbA);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNull(blah.getMaster().getName());
    System.out.println(" NOTE: DETACHED DETAIL AFTER MERGE HAS A DETACHED, INCOMPLETE, MASTER: MASTER.GETNAME() == NULL");
    System.out.println(" -> ! NOTE THAT IF CASCADETYPE.MERGE FROM CHILD TO MASTER IS ACTIVATED, THE MASTER IS COMPLETE !");

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copy = em.find(Detail.class, blah.getPersistenceId());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(copy.getMaster().getName());
    System.out.println(" NOTE: DETACHED DETAIL AFTER FIND IN NEW TRANSACTION HAS A DETACHED, COMPLETE, MASTER");

    assertNotNull(copy);
    assertDetailB(aId, copy);
    assertNotSame(slcA, copy);
    assertNotNull(copy.getMaster());
    assertMaster0(e.getPersistenceId(), copy.getMaster());
    assertNull(copy.getMaster().$details);
    assertNull(copy.getMaster().getDetails());

    assertTrue(aVersion + 1 == copy.getPersistenceVersion());
    assertTrue(mVersion.equals(copy.getMaster().getPersistenceVersion()));

    System.out.println("detail field change successfull, persistence version incremented with 1");
  }

  @Test
  public void hypothesis9d() {
    displayTest("MODIFY DETAIL: FIELD CHANGE",
        "hypothesis9d (master with 2 details, created using persist, retrieve, modify detached detail, merge detail back)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    fromDbA.setDate(DETAIL_DATE_B);
    assertNotNull(fromDbA.getMaster().getName());
    System.out.println(" NOTE: DETACHED DETAIL HAS A DETACHED, COMPLETE, MASTER");

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail blah = em.merge(fromDbA);
    blah.getMaster().getName();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(blah.getMaster().getName());
    System.out.println(" NOTE: DETACHED DETAIL AFTER MERGE AND DETAIL.GETMASTER().GETNAME() HAS A DETACHED, COMPLETE, MASTER");

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copy = em.find(Detail.class, blah.getPersistenceId());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(copy.getMaster().getName());
    System.out.println(" NOTE: DETACHED DETAIL AFTER FIND IN NEW TRANSACTION HAS A DETACHED, COMPLETE, MASTER");

    assertNotNull(copy);
    assertDetailB(aId, copy);
    assertNotSame(slcA, copy);
    assertNotNull(copy.getMaster());
    assertMaster0(e.getPersistenceId(), copy.getMaster());
    assertNull(copy.getMaster().$details);
    assertNull(copy.getMaster().getDetails());

    assertTrue(aVersion + 1 == copy.getPersistenceVersion());
    assertTrue(mVersion.equals(copy.getMaster().getPersistenceVersion()));

    System.out.println("detail field change successfull, persistence version incremented with 1");
  }

  @Test
  public void hypothesis9e() {
    displayTest("MODIFY DETAIL: FIELD CHANGE",
        "hypothesis9e (master with 2 details, created using persist, retrieve, modify detached detail," +
        "merge detail back, modify with same value)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    fromDbA.setDate(DETAIL_DATE_A);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.merge(fromDbA);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copy = em.find(Detail.class, slcA.getPersistenceId());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(copy);
    assertDetailA(aId, copy);
    assertNotSame(slcA, copy);
    assertNotNull(copy.getMaster());
    assertMaster0(e.getPersistenceId(), copy.getMaster());
    assertNull(copy.getMaster().$details);
    assertNull(copy.getMaster().getDetails());

    assertTrue(aVersion.compareTo(copy.getPersistenceVersion())==0);
    assertTrue(mVersion.equals(copy.getMaster().getPersistenceVersion()));

    System.out.println("detail field change with same value successful, persistence version stays the same");
  }

  @Test
  public void hypothesis10a() {
    displayTest("MODIFY DETAIL: FIELD CHANGE, MODIFY MASTER: FIELD CHANGE",
        "hypothesis10a (master with 2 details, created using persist, retrieve, modify both detail and master, " +
        "merge detail, using managed detail and master)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_B);
    fromDbA.getMaster().setName(MASTER_NAME_1);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailB(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster1(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(aVersion + 1 == fromDbA.getPersistenceVersion());
    assertTrue(mVersion + 1 == fromDbA.getMaster().getPersistenceVersion());

    System.out.println("detail field change successfull, master field change successfull, persistence version of both incremented with 1");
  }

  @Test
  public void hypothesis10b() {
    displayTest("MODIFY DETAIL: FIELD CHANGE, MODIFY MASTER: FIELD CHANGE",
        "hypothesis10b (master with 2 details, created using persist, retrieve, modify both detail and master," +
        " merge detail, using managed detail, modify with same value)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_A);
    fromDbA.getMaster().setName(MASTER_NAME_0);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotSame(slcA, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(aVersion.equals(fromDbA.getPersistenceVersion()));
    assertTrue(mVersion.equals(fromDbA.getMaster().getPersistenceVersion()));

    System.out.println("detail field and master field change with same value successful, persistence version stays the same for both");
  }

  @Test
  public void hypothesis10c() {
    displayTest("MODIFY DETAIL: FIELD CHANGE, MODIFY MASTER: FIELD CHANGE",
        "hypothesis10c (master with 2 details, created using persist, retrieve, modify detached detail and master, merge detail back)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    fromDbA.setDate(DETAIL_DATE_B);
    fromDbA.getMaster().setName(MASTER_NAME_1);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail blah = em.merge(fromDbA);
    fromDbA.getMaster().getName();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copy = em.find(Detail.class, blah.getPersistenceId());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(copy);
    assertDetailB(aId, copy);
    assertNotSame(slcA, copy);
    assertNotNull(copy.getMaster());
    assertMaster0(e.getPersistenceId(), copy.getMaster());
    assertNull(copy.getMaster().$details);
    assertNull(copy.getMaster().getDetails());

    assertTrue(aVersion + 1 == copy.getPersistenceVersion());
    assertTrue(mVersion + 0 == copy.getMaster().getPersistenceVersion());

    System.out.println("detail field change successful, persistence version incremented with 1, master stays the same");
  }

  @Test
  public void hypothesis10d() {
    displayTest("MODIFY DETAIL: FIELD CHANGE, MODIFY MASTER: FIELD CHANGE",
        "hypothesis10d (master with 2 details, created using persist, retrieve, modify detached detail and master," +
        "merge detail and merge master)");
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
    LOGGER.fine("master after persist:\n\t" + e);
    LOGGER.fine("detail A after persist:\n\t" + slcA);
    LOGGER.fine("detail B after persist:\n\t" + slcB);

    Integer aId = slcA.getPersistenceId();
    LOGGER.fine("id of PERSISTed detail A: " + aId);
    Integer aVersion = slcA.getPersistenceVersion();
    LOGGER.fine("version of PERSISTed detail A: " + aVersion);

    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    fromDbA.setDate(DETAIL_DATE_B);
    fromDbA.getMaster().setName(MASTER_NAME_1);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.merge(fromDbA);
    em.merge(fromDbA.getMaster());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copy = em.find(Detail.class, slcA.getPersistenceId());
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(copy);
    assertDetailB(aId, copy);
    assertNotSame(slcA, copy);
    assertNotNull(copy.getMaster());
    assertMaster1(e.getPersistenceId(), copy.getMaster());
    assertNull(copy.getMaster().$details);
    assertNull(copy.getMaster().getDetails());

    assertTrue(aVersion + 1 == copy.getPersistenceVersion());
    assertTrue(mVersion + 1 == copy.getMaster().getPersistenceVersion());

    System.out.println("detail and master field change successful, persistence version incremented for both");
  }

  @Test
  public void hypothesis11a() {
    displayTest("CREATE DETAIL AND ATTACH IT TO A MASTER",
        "hypothesis11a (master without details, created using persist, create detail and attach to master, using managed master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    LOGGER.fine("master after persist:\n\t" + e);

    Integer mId = e.getPersistenceId();
    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbM = em.find(Master.class, mId);
    Detail a = createDetailA(fromDbM);
    em.persist(a);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer aId = a.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(mVersion + 1 == fromDbA.getMaster().getPersistenceVersion());

    System.out.println("detail created successfully, persistence version of master incremented with 1");
  }

  @Test
  public void hypothesis11b() {
    displayTest("CREATE DETAIL AND ATTACH IT TO A MASTER",
        "hypothesis11b (master without details, created using persist, create detail and attach to master (touch master.getDetails()), using detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    LOGGER.fine("master after persist:\n\t" + e);

    Integer mId = e.getPersistenceId();
    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbM = em.find(Master.class, mId);
    fromDbM.getDetails();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Detail a = createDetailA(fromDbM);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.persist(a);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer aId = a.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(mVersion + 0 == fromDbA.getMaster().getPersistenceVersion());

    System.out.println("detail created successfully, persistence version of master stays the same");
  }

  @Test
  public void hypothesis12a() {
    displayTest("CREATE DETAIL AND ATTACH IT TO A MASTER WITH DETAILS",
        "hypothesis12a (master with details, created using persist, create detail and attach to master, using managed master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    Detail a = createDetailA(e);
    em.persist(a);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    LOGGER.fine("master after persist:\n\t" + e);

    Integer mId = e.getPersistenceId();
    Integer mVersion = e.getPersistenceVersion();
    Integer aId = a.getPersistenceId();
    Integer aVersion = a.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbM = em.find(Master.class, mId);
    Detail b = createDetailB(fromDbM);
    em.persist(b);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer bId = b.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbB = em.find(Detail.class, bId);
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbB);
    assertDetailB(bId, fromDbB);
    assertNotNull(fromDbB.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbB.getMaster());
    assertNull(fromDbB.getMaster().$details);
    assertNull(fromDbB.getMaster().getDetails());

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(aVersion + 0 == fromDbA.getPersistenceVersion());
    assertTrue(mVersion + 1 == fromDbB.getMaster().getPersistenceVersion());

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master m = em.find(Master.class, e.getPersistenceId());
    m.getDetails();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertTrue(m.getDetails().size() == 2);

    System.out.println("detail created successfully, attach to master with detials, persistence version of master incremented with 1, " +
        "persistence version of existing detail stays the same");
  }

  @Test
  public void hypothesis12b() {
    displayTest("CREATE DETAIL AND ATTACH IT TO A MASTER WITH DETAILS",
        "hypothesis12b (master with details, created using persist, create detail and attach to master (touch master.getDetails()), using detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    Detail a = createDetailA(e);
    em.persist(a);
    tx.commit();
    tx = null;
    em.close();
    em = null;
    LOGGER.fine("master after persist:\n\t" + e);

    Integer mId = e.getPersistenceId();
    Integer mVersion = e.getPersistenceVersion();
    Integer aId = a.getPersistenceId();
    Integer aVersion = a.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbM = em.find(Master.class, mId);
    fromDbM.getDetails();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Detail b = createDetailB(fromDbM);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.persist(b);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    Integer bId = b.getPersistenceId();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbB = em.find(Detail.class, bId);
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertNotNull(fromDbB);
    assertDetailB(bId, fromDbB);
    assertNotNull(fromDbB.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbB.getMaster());
    assertNull(fromDbB.getMaster().$details);
    assertNull(fromDbB.getMaster().getDetails());

    assertNotNull(fromDbA);
    assertDetailA(aId, fromDbA);
    assertNotNull(fromDbA.getMaster());
    assertMaster0(e.getPersistenceId(), fromDbA.getMaster());
    assertNull(fromDbA.getMaster().$details);
    assertNull(fromDbA.getMaster().getDetails());

    assertTrue(aVersion + 0 == fromDbA.getPersistenceVersion());
    assertTrue(mVersion + 0 == fromDbA.getMaster().getPersistenceVersion());

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master m = em.find(Master.class, e.getPersistenceId());
    m.getDetails();
    tx.commit();
    tx = null;
    em.close();
    em = null;

    assertTrue(m.getDetails().size() == 2);

    System.out.println("detail created successfully, persistence version of master stays the same, " +
        "persistence version of existing detail stays the same");
  }



  // THE ABOVE MEANS THAT WE NEED TO DO MORE TO MAKE SURE THAT WE DO NOT SEND DETAILS OVER THE WIRE

  // HELPER CODE

  private Detail serAndDeserDetail(Detail e) throws FileNotFoundException,
  IOException,
  ClassNotFoundException {
    serDetail(e);
    Detail deserE = deserDetail();
    return deserE;
  }

  private Detail deserDetail() throws FileNotFoundException,
                                      IOException,
                                      ClassNotFoundException {
    File iSerFile = new File($serFileName);
    FileInputStream fis = new FileInputStream(iSerFile);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Detail deserE = (Detail)ois.readObject();
    ois.close();
    ois = null;
    fis.close();
    fis = null;
    iSerFile.delete();
    iSerFile = null;
    return deserE;
  }

  private void serDetail(Detail e) throws FileNotFoundException, IOException {
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
