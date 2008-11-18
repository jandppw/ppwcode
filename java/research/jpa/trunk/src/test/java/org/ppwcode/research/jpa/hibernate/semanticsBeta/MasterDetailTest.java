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

package org.ppwcode.research.jpa.hibernate.semanticsBeta;

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

import org.junit.BeforeClass;
import org.junit.Test;

public class MasterDetailTest {

  static final String PERSISTENCE_UNIT_NAME = "test_master_detail";

  final static Logger LOGGER = Logger.getLogger("MasterDetailTest");

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
    //assertNull(e.getPersistenceVersion());
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
    //assertNull(e.getPersistenceVersion());
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
  public void hypothesis6a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("DELETE MASTER WITHOUT DETAILS",
        "hypothesis6a (master without details, created using persist, remove managed master)");
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

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, deserEe.getPersistenceId());
    em.remove(fromDbE);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    assertNull(fromDbE);
    System.out.println("master without details is removed");
}

  @Test
  public void hypothesis6b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("DELETE MASTER WITHOUT DETAILS",
        "hypothesis6b (master without details, created using merge, remove managed master)");
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

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, deserEe.getPersistenceId());
    em.remove(fromDbE);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    assertNull(fromDbE);
    System.out.println("master without details is removed");
  }

  @Test(expected=IllegalArgumentException.class)
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
    em.close();

    Integer eId = e.getPersistenceId();
    assertNotNull(eId);

    System.out.println("EntityManager.remove() will throw an ArgumentException when the argument is a detached object.");
    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.remove(e);
    tx.commit();
    em.close();
    System.out.println("EntityManager DID NOT THROW AN ARGUMENTEXCEPTION");
}

  @Test(expected=IllegalArgumentException.class)
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
    em.close();

    Integer eId = mergedE.getPersistenceId();
    assertNotNull(eId);

    System.out.println("EntityManager.remove() will throw an ArgumentException when the argument is a detached object.");
    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.remove(mergedE);
    tx.commit();
    em.close();
    System.out.println("EntityManager DID NOT THROW AN ARGUMENTEXCEPTION");
  }

  @Test(expected=PersistenceException.class)
  public void hypothesis7a() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer eId = e.getPersistenceId();
    assertNotNull(eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.getDetails();
    tx.commit();
    em.close();

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
    tx.commit();
    em.close();

    Master deserEe = serAndDeserMaster(fromDbE);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    fromDbE = em.find(Master.class, deserEe.getPersistenceId());
    em.remove(fromDbE);
    System.out.println("EntityManager.remove() will throw a PersistenceException because foreign key constraints will be violated.");
    tx.commit();
    em.close();
    System.out.println("EntityManager DID NOT THROW AN PERSISTENCEEXCEPTION");
  }

  @Test
  public void hypothesis7b() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer eId = e.getPersistenceId();
    assertNotNull(eId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbE = em.find(Master.class, eId);
    fromDbE.getDetails();
    tx.commit();
    em.close();

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
    fromDbE = em.find(Master.class, eId);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(slcA);
    Detail deserB = serAndDeserDetail(slcB);
    Master deserE = serAndDeserMaster(fromDbE);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    slcA = em.find(Detail.class, deserA.getPersistenceId());
    slcB = em.find(Detail.class, deserB.getPersistenceId());
    fromDbE = em.find(Master.class, deserE.getPersistenceId());
    em.remove(slcA);
    em.remove(slcB);
    em.remove(fromDbE);
    tx.commit();
    em.close();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail a = em.find(Detail.class, slcA.getPersistenceId());
    Detail b = em.find(Detail.class, slcB.getPersistenceId());
    Master m = em.find(Master.class, fromDbE.getPersistenceId());
    tx.commit();
    em.close();

    assertNull(a);
    assertNull(b);
    assertNull(m);

    System.out.println("Master is removed after removing its details first.");
  }

  @Test
  public void hypothesis8a() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(fromDbA);

    assertDetailA(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster0(e.getPersistenceId(), deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertTrue(deserA.getMaster().$details.size() == 0);
    assertNotNull(deserA.getMaster().getDetails());
    assertTrue(deserA.getMaster().getDetails().size() == 0);

    System.out.println("detail successfully retrieved, with master attached (master.getDetails() is empty set)");
  }

  @Test
  public void hypothesis8b() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.getMaster().getDetails();
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(fromDbA);

    assertDetailA(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster0(e.getPersistenceId(), deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertTrue(deserA.getMaster().$details.size() == 0);
    assertNotNull(deserA.getMaster().getDetails());
    assertTrue(deserA.getMaster().getDetails().size() == 0);

    System.out.println("detail successfully retrieved, with master attached " +
        "(master.getDetails() was touched, but after serialization and deserialization getDetails() " +
        "is an empty set)");
  }

  @Test
  public void hypothesis9a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("MODIFY DETAIL: FIELD CHANGE",
        "hypothesis9a (master with 2 details, created using persist, retrieve, modify" +
        " and merge detail, using managed detail)");
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

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_B);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(fromDbA);

    assertNotNull(deserA);
    assertDetailB(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster0(eId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 1 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 0 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail field change successfull, persistence version incremented with 1");
  }

  @Test
  public void hypothesis9b() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_A);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(fromDbA);

    assertNotNull(deserA);
    assertDetailA(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster0(eId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 0 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 0 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail field change with same value successful, " +
        "persistence version stays the same");
  }

  @Test
  public void hypothesis9c() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    em.close();

    fromDbA.setDate(DETAIL_DATE_B);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copyA = em.merge(fromDbA);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(copyA);

    assertNotNull(deserA);
    assertDetailB(aId, deserA);
    assertNotSame(slcA, deserA);
    assertNotNull(deserA.getMaster());
    assertMaster0(e.getPersistenceId(), deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 1 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 0 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail field change successfull, persistence version incremented with 1");
  }

  @Test
  public void hypothesis9d() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    em.close();

    fromDbA.setDate(DETAIL_DATE_A);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copyA = em.merge(fromDbA);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(copyA);

    assertNotNull(deserA);
    assertDetailA(aId, deserA);
    assertNotSame(slcA, deserA);
    assertNotNull(deserA.getMaster());
    assertMaster0(e.getPersistenceId(), deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 0 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 0 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail field change with same value successfull, " +
        "persistence version stays the same");
  }

  @Test
  public void hypothesis10a() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_B);
    fromDbA.getMaster().setName(MASTER_NAME_1);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(fromDbA);

    assertNotNull(deserA);
    assertDetailB(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster1(eId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 1 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 1 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail field change successfull, master field change successfull, persistence version of both incremented with 1");
  }

  @Test
  public void hypothesis10b() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    fromDbA.setDate(DETAIL_DATE_A);
    fromDbA.getMaster().setName(MASTER_NAME_0);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(fromDbA);

    assertNotNull(deserA);
    assertDetailA(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster0(eId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 0 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 0 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail field and master field change with same value successful, persistence version stays the same for both");
  }

  @Test
  public void hypothesis10c() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    em.close();

    fromDbA.setDate(DETAIL_DATE_B);
    fromDbA.getMaster().setName(MASTER_NAME_1);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copyA = em.merge(fromDbA);
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(copyA);

    assertNotNull(deserA);
    assertDetailB(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster0(eId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 1 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 0 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail field change successful, persistence version incremented with 1, master stays the same");
  }

  @Test
  public void hypothesis10d() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

    Integer aId = slcA.getPersistenceId();
    assertNotNull(aId);
    Integer aVersion = slcA.getPersistenceVersion();
    assertNotNull(aVersion);
    Integer eId = e.getPersistenceId();
    assertNotNull(eId);
    Integer mVersion = e.getPersistenceVersion();
    assertNotNull(mVersion);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail fromDbA = em.find(Detail.class, aId);
    tx.commit();
    em.close();

    fromDbA.setDate(DETAIL_DATE_B);
    fromDbA.getMaster().setName(MASTER_NAME_1);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Detail copyA = em.merge(fromDbA);
    em.merge(fromDbA.getMaster());
    tx.commit();
    em.close();

    Detail deserA = serAndDeserDetail(copyA);

    assertNotNull(deserA);
    assertDetailB(aId, deserA);
    assertNotSame(deserA, fromDbA);
    assertNotNull(deserA.getMaster());
    assertMaster1(eId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(aVersion + 1 == deserA.getPersistenceVersion());
    assertTrue(mVersion + 1 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail and master field change successful, persistence version incremented for both");
  }

  @Test
  public void hypothesis11a() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("CREATE DETAIL AND ATTACH IT TO A MASTER",
        "hypothesis11a (master without details, created using persist, create detail and attach to master, using managed master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    em.close();

    Integer mId = e.getPersistenceId();
    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbM = em.find(Master.class, mId);
    Detail a = createDetailA(fromDbM);
    em.persist(a);
    tx.commit();
    em.close();

    Integer aId = a.getPersistenceId();

    Detail deserA = serAndDeserDetail(a);

    assertNotNull(deserA);
    assertDetailA(aId, deserA);
    assertNotSame(deserA, a);
    assertNotNull(deserA.getMaster());
    assertMaster0(mId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(mVersion + 1 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail created successfully, persistence version of master incremented with 1");
  }

  @Test
  public void hypothesis11b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("CREATE DETAIL AND ATTACH IT TO A MASTER",
        "hypothesis11b (master without details, created using persist, create detail and attach to master (touch master.getDetails()), using detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    tx.commit();
    em.close();

    Integer mId = e.getPersistenceId();
    Integer mVersion = e.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbM = em.find(Master.class, mId);
    fromDbM.getDetails();
    tx.commit();
    em.close();

    Master m = serAndDeserMaster(fromDbM);

    Detail a = createDetailA(m);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.persist(a);
    tx.commit();
    em.close();

    Integer aId = a.getPersistenceId();

    Detail deserA = serAndDeserDetail(a);

    assertNotNull(deserA);
    assertDetailA(aId, deserA);
    assertNotSame(deserA, a);
    assertNotNull(deserA.getMaster());
    assertMaster0(mId, deserA.getMaster());
    assertNotNull(deserA.getMaster().$details);
    assertNotNull(deserA.getMaster().getDetails());

    assertTrue(mVersion + 0 == deserA.getMaster().getPersistenceVersion());

    System.out.println("detail created successfully, persistence version of master stays the same");
  }

  @Test
  public void hypothesis12a() throws FileNotFoundException, IOException, ClassNotFoundException {
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
    em.close();

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
    em.close();

    Integer bId = b.getPersistenceId();
    assertNotNull(bId);

    Detail deserB = serAndDeserDetail(b);
    Detail deserA = serAndDeserDetail(a);

    assertNotNull(deserB);
    assertDetailB(bId, deserB);
    assertNotSame(deserB, a);
    assertNotNull(deserB.getMaster());
    assertMaster0(mId, deserB.getMaster());
    assertNotNull(deserB.getMaster().$details);
    assertNotNull(deserB.getMaster().getDetails());
    assertTrue(deserB.getMaster().getDetails().size()==0);

    assertNotNull(deserA);
    assertDetailA(aId, deserA);
    assertNotSame(deserA, a);
    assertNotNull(deserA.getMaster());
    assertMaster0(mId, deserA.getMaster());
    assertTrue(aVersion + 0 == deserA.getPersistenceVersion());

    assertTrue(mVersion + 1 == deserB.getMaster().getPersistenceVersion());

    em = emf.createEntityManager();
    Master m = em.find(Master.class, mId);
    assertTrue(m.getDetails().size() == 2);
    for (Detail d : m.getDetails()) {
      assertTrue((d.getPersistenceId().compareTo(aId)==0)||
          (d.getPersistenceId().compareTo(bId)==0));
    }
    em.close();

    System.out.println("detail created successfully, attach to master with details, " +
        "persistence version of master incremented with 1, " +
        "persistence version of existing detail stays the same");
  }

  @Test
  public void hypothesis12b() throws FileNotFoundException, IOException, ClassNotFoundException {
    displayTest("CREATE DETAIL AND ATTACH IT TO A MASTER WITH DETAILS",
        "hypothesis12b (master with details, created using persist, " +
        "create detail and attach to master (touch master.getDetails()), using detached master)");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();
    Master e = createMaster0();
    em.persist(e);
    Detail a = createDetailA(e);
    em.persist(a);
    tx.commit();
    em.close();

    Integer mId = e.getPersistenceId();
    Integer mVersion = e.getPersistenceVersion();
    Integer aId = a.getPersistenceId();
    Integer aVersion = a.getPersistenceVersion();

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    Master fromDbM = em.find(Master.class, mId);
    tx.commit();
    em.close();

    Master mr = serAndDeserMaster(fromDbM);

    Detail b = createDetailB(mr);

    em = emf.createEntityManager();
    tx = em.getTransaction();
    tx.begin();
    em.persist(b);
    tx.commit();
    em.close();

    Integer bId = b.getPersistenceId();
    assertNotNull(bId);

    Detail deserB = serAndDeserDetail(b);
    Detail deserA = serAndDeserDetail(a);

    assertNotNull(deserB);
    assertDetailB(bId, deserB);
    assertNotSame(deserB, a);
    assertNotNull(deserB.getMaster());
    assertMaster0(mId, deserB.getMaster());
    assertNotNull(deserB.getMaster().$details);
    assertNotNull(deserB.getMaster().getDetails());
    assertTrue(deserB.getMaster().getDetails().size()==0);

    assertNotNull(deserA);
    assertDetailA(aId, deserA);
    assertNotSame(deserA, a);
    assertNotNull(deserA.getMaster());
    assertMaster0(mId, deserA.getMaster());
    assertTrue(aVersion + 0 == deserA.getPersistenceVersion());

    assertTrue(mVersion + 0 == deserB.getMaster().getPersistenceVersion());

    em = emf.createEntityManager();
    Master m = em.find(Master.class, mId);
    assertTrue(m.getDetails().size() == 2);
    for (Detail d : m.getDetails()) {
      assertTrue((d.getPersistenceId().compareTo(aId)==0)||
          (d.getPersistenceId().compareTo(bId)==0));
    }
    em.close();

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
