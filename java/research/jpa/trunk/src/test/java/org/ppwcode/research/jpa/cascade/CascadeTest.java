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

package org.ppwcode.research.jpa.cascade;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class CascadeTest {

  private EntityManager entityManager;
  private EntityTransaction entityTransaction;
  private EntityManagerFactory emf;

  private String punit = "test_cascade";

  private void setUp() {
    emf = Persistence.createEntityManagerFactory(punit);
    entityManager = emf.createEntityManager();
    entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
  }

  private void tearDown() {
    entityTransaction.commit();
    entityManager.close();
    // freeing resources, including connection pools in the case of Hibernate
    emf.close();
  }


  /*
   * No Cascading persist from Master to Detail
   * OpenJPA: this results in a PersistenceException
   * Hibernate: no PersistenceException, but Detail is not persisted
   */
  @Test // (expected = PersistenceException.class)
  public void testNoCascadePersistAllAtOnce() {
    // make master-detail couple and persist master
    setUp();

    EntityMasterNoCascade master = new EntityMasterNoCascade();
    EntityDetailNoCascade detail = new EntityDetailNoCascade();
    detail.setMaster(master);
    entityManager.persist(master);

    tearDown();
  }

  @Test
  public void testNoCascadePersistOneByOne() {
    // make master first and persist immediately
    // make detail next and persist
    setUp();

    EntityMasterNoCascade master = new EntityMasterNoCascade();
    entityManager.persist(master);
    Integer masterId = master.getPersistenceId();

    EntityDetailNoCascade detailA = new EntityDetailNoCascade();
    detailA.setMaster(master);
    entityManager.persist(detailA);
    Integer detailId = detailA.getPersistenceId();

    tearDown();

    // verification
    setUp();

    master = entityManager.find(EntityMasterNoCascade.class, masterId);
    assertEquals(1, master.getDetails().size());
    for (EntityDetailNoCascade d : master.getDetails()) {
      assertEquals(detailId, d.getPersistenceId());
    }

    tearDown();
  }

  @Test
  public void testNoCascadeMergeWithoutNewEntity() {
    // creating master-detail couple
    setUp();

    EntityMasterNoCascade master = new EntityMasterNoCascade();
    master.setDescription("Master version 1");
    entityManager.persist(master);
    Integer masterId = master.getPersistenceId();

    EntityDetailNoCascade detail = new EntityDetailNoCascade();
    detail.setMaster(master);
    detail.setDescription("Detail version 1");
    entityManager.persist(detail);
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // retrieve current persistence version
    // is only filled in after commit of the transaction
    Integer masterVersion = master.getPersistenceVersion();
    Integer detailVersion = detail.getPersistenceVersion();

    // modify detached objects
    assertEquals(master, detail.getMaster());
    detail.setDescription("Detail version 2");
    master.setDescription("Master version 2");

    setUp();

    // get a managed copy from the merge operation
    detail = entityManager.merge(detail);

    tearDown();

    // verification
    setUp();

    master = entityManager.find(EntityMasterNoCascade.class, masterId);
    assertNotSame("Master version 2", master.getDescription());
    assertEquals(masterVersion, master.getPersistenceVersion());
    assertEquals(1, master.getDetails().size());
    for (EntityDetailNoCascade d : master.getDetails()) {
      assertTrue(detailId.equals(d.getPersistenceId()));
      assertEquals("Detail version 2", d.getDescription());
      assertTrue(detailVersion.compareTo(detail.getPersistenceVersion())<0);
    }

    tearDown();
  }

  @Test(expected= IllegalStateException.class)
  public void testNoCascadeMergeWithNewEntity() {
    setUp();

    EntityMasterNoCascade master = new EntityMasterNoCascade();
    master.setDescription("Master version 1");
    entityManager.persist(master);
//    Integer masterId = master.getPersistenceId();

    EntityDetailNoCascade detail = new EntityDetailNoCascade();
    detail.setMaster(master);
    detail.setDescription("Detail version 1");
    entityManager.persist(detail);
//    Integer detailId = detail.getPersistenceId();

    tearDown();

    // modify detached entity: add a new master
    assertEquals(master, detail.getMaster());
    detail.setDescription("Detail version 2");
    EntityMasterNoCascade newMaster = new EntityMasterNoCascade();
    newMaster.setDescription("New Master version 1");
    detail.setMaster(newMaster);

    setUp();

    // get a managed copy from the merge operation
    detail = entityManager.merge(detail);
//    Integer newMasterId = detail.getMaster().getPersistenceId();

    tearDown();
  }

  @Test
  public void testCascadePersistAllAtOnce() {
    // make master-detail couple and persist detail
    // cascade from detail to master
    setUp();

    EntityMasterCascade master = new EntityMasterCascade();
    EntityDetailCascade detail = new EntityDetailCascade();
    detail.setMaster(master);

    // MUDO: master should not need to be persisted, but because of a so-called missing
    // "improvement" in openjpa 1.0.x, the creation of the detail will first insert a
    // null in the foreign key column to master, which results in a non-nullable column
    // constraint violation.
    // An improvement has been added in OpenJPA 1.2.0, to correctly reorder the generated
    // SQL in this case.  I call it a bugfix. [OPENJPA-235]
    entityManager.persist(master);

    entityManager.persist(detail);

    Integer masterId = master.getPersistenceId();
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // verification
    setUp();

    master = entityManager.find(EntityMasterCascade.class, masterId);
    detail = entityManager.find(EntityDetailCascade.class, detailId);
    assertEquals(master, detail.getMaster());
    assertEquals(1, master.getDetails().size());
    for (EntityDetailCascade d : master.getDetails()) {
      assertEquals(detail, d);
    }

    tearDown();
  }

  @Test
  public void testCascadePersistOneByOne() {
    // make master first and persist immediately
    // make detail next and persist
    setUp();

    EntityMasterCascade master = new EntityMasterCascade();
    entityManager.persist(master);
    Integer masterId = master.getPersistenceId();

    EntityDetailCascade detail = new EntityDetailCascade();
    detail.setMaster(master);
    entityManager.persist(detail);
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // verification
    setUp();

    master = entityManager.find(EntityMasterCascade.class, masterId);
    detail = entityManager.find(EntityDetailCascade.class, detailId);
    assertEquals(master, detail.getMaster());
    assertEquals(1, master.getDetails().size());
    for (EntityDetailCascade d : master.getDetails()) {
      assertEquals(detail, d);
    }

    tearDown();
  }

  @Test
  public void testCascadeMergeWithoutNewEntity() {
    setUp();

    EntityMasterCascade master = new EntityMasterCascade();
    master.setDescription("Master version 1");
    entityManager.persist(master);
    Integer masterId = master.getPersistenceId();

    EntityDetailCascade detail = new EntityDetailCascade();
    detail.setMaster(master);
    detail.setDescription("Detail version 1");
    entityManager.persist(detail);
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // retrieve persistence version
    Integer masterVersion = master.getPersistenceVersion();
    Integer detailVersion = detail.getPersistenceVersion();

    // modify detached objects
    assertEquals(master, detail.getMaster());
    detail.setDescription("Detail version 2");
    master.setDescription("Master version 2");

    // merge operation
    setUp();
    detail = entityManager.merge(detail);
    tearDown();

    // verification
    setUp();

    master = entityManager.find(EntityMasterCascade.class, masterId);
    detail = entityManager.find(EntityDetailCascade.class, detailId);
    assertEquals("Master version 2", master.getDescription());
    assertTrue(masterVersion.compareTo(master.getPersistenceVersion())<0);
    assertEquals("Detail version 2", detail.getDescription());
    assertTrue(detailVersion.compareTo(detail.getPersistenceVersion())<0);
    assertEquals(master, detail.getMaster());
    assertEquals(1, master.getDetails().size());
    for (EntityDetailCascade d : master.getDetails()) {
      assertEquals(detail, d);
    }

    tearDown();
  }

  @Test
  public void testCascadeMergeWithNewEntity() {
    setUp();

    EntityMasterCascade master = new EntityMasterCascade();
    master.setDescription("Master version 1");
    entityManager.persist(master);
    Integer masterId = master.getPersistenceId();

    EntityDetailCascade detail = new EntityDetailCascade();
    detail.setMaster(master);
    detail.setDescription("Detail version 1");
    entityManager.persist(detail);
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // retrieve persistence versions
    Integer masterVersion = master.getPersistenceVersion();
    Integer detailVersion = detail.getPersistenceVersion();

    // modify detached entity: add a new master
    assertEquals(master, detail.getMaster());
    detail.setDescription("Detail version 2");
    EntityMasterCascade newMaster = new EntityMasterCascade();
    newMaster.setDescription("New Master version 1");

    detail.setMaster(newMaster);

    setUp();

    // MUDO: this persist is only needed because cascading is not working
    // for a new entity when merge is used
    entityManager.persist(newMaster);

    // get a managed copy from the merge operation
    detail = entityManager.merge(detail);
    Integer newMasterId = detail.getMaster().getPersistenceId();

    tearDown();

    // verification
    setUp();

    master = entityManager.find(EntityMasterCascade.class, masterId);
    detail = entityManager.find(EntityDetailCascade.class, detailId);
    newMaster = entityManager.find(EntityMasterCascade.class, newMasterId);
    assertTrue(masterVersion.compareTo(master.getPersistenceVersion())==0);
    assertTrue(detailVersion.compareTo(detail.getPersistenceVersion())<0);
    assertEquals(newMaster, detail.getMaster());
    assertEquals(1, newMaster.getDetails().size());
    assertEquals(0, master.getDetails().size());
    for (EntityDetailCascade d : newMaster.getDetails()) {
      assertEquals(detail, d);
    }

    tearDown();
  }

  @Test
  public void testCascadePersistFirstMasterLaterDetail() {
    // first create master
    // later add detail and check persistence version of master
    setUp();

    EntityMasterCascade master = new EntityMasterCascade();
    master.setDescription("Master version 1");
    entityManager.persist(master);
    Integer masterId = master.getPersistenceId();

    tearDown();

    // retrieve persistence version
    Integer masterVersion = master.getPersistenceVersion();

    // create detail and add to master
    setUp();

    master = entityManager.find(EntityMasterCascade.class, masterId);
    EntityDetailCascade detail = new EntityDetailCascade();
    detail.setMaster(master);
    detail.setDescription("Detail version 1");
    entityManager.persist(detail);
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // retrieve persistence version
//    Integer detailVersion = detail.getPersistenceVersion();

    // verification
    setUp();

    master = entityManager.find(EntityMasterCascade.class, masterId);
    detail = entityManager.find(EntityDetailCascade.class, detailId);
    assertTrue(masterVersion.compareTo(master.getPersistenceVersion())<0);
    assertEquals(master, detail.getMaster());
    assertEquals(1, master.getDetails().size());
    for (EntityDetailCascade d : master.getDetails()) {
      assertEquals(detail, d);
    }

    tearDown();
  }

  @Test
  public void testCascadeMergeFirstMasterLaterDetailFieldChange() {
    // first create master and detail pair
    // later modify field of detail and merge
    setUp();

    EntityMasterCascade master = new EntityMasterCascade();
    master.setDescription("Master version 1");
    entityManager.persist(master);
    Integer masterId = master.getPersistenceId();

    EntityDetailCascade detail = new EntityDetailCascade();
    detail.setMaster(master);
    detail.setDescription("Detail version 1");
    entityManager.persist(detail);
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // retrieve persistence version
    Integer masterVersion = master.getPersistenceVersion();
    Integer detailVersion = detail.getPersistenceVersion();

    // field change and merge
    setUp();

    detail.setDescription("Detail version 2");
    detail = entityManager.merge(detail);

    tearDown();

    // verification
    setUp();

    master = entityManager.find(EntityMasterCascade.class, masterId);
    detail = entityManager.find(EntityDetailCascade.class, detailId);
    assertTrue(masterVersion.compareTo(master.getPersistenceVersion())==0);
    assertTrue(detailVersion.compareTo(detail.getPersistenceVersion())<0);
    assertEquals(master, detail.getMaster());
    assertEquals(1, master.getDetails().size());
    for (EntityDetailCascade d : master.getDetails()) {
      assertEquals(detail, d);
    }

    tearDown();
  }

  @Test
  public void testCascadeMergeFirstMasterLaterDetailRelationChange() {
    // first create 2 masters and detail
    // later move detail to other master and check persistence versions
    setUp();

    EntityMasterCascade master1 = new EntityMasterCascade();
    master1.setDescription("Master version 1");
    entityManager.persist(master1);
    Integer masterId1 = master1.getPersistenceId();

    EntityMasterCascade master2 = new EntityMasterCascade();
    master2.setDescription("Master version 1");
    entityManager.persist(master2);
    Integer masterId2 = master2.getPersistenceId();

    EntityDetailCascade detail = new EntityDetailCascade();
    detail.setMaster(master1);
    detail.setDescription("Detail version 1");
    entityManager.persist(detail);
    Integer detailId = detail.getPersistenceId();

    tearDown();

    // retrieve persistence version
    Integer masterVersion1 = master1.getPersistenceVersion();
    Integer masterVersion2 = master2.getPersistenceVersion();
    Integer detailVersion = detail.getPersistenceVersion();

    // modification and merge
    setUp();

    detail.setMaster(master2);
    detail = entityManager.merge(detail);
    master1 = entityManager.merge(master1);

    tearDown();

    // verification
    setUp();

    master1 = entityManager.find(EntityMasterCascade.class, masterId1);
    master2 = entityManager.find(EntityMasterCascade.class, masterId2);
    detail = entityManager.find(EntityDetailCascade.class, detailId);
    assertTrue(masterVersion1.compareTo(master1.getPersistenceVersion())<0);
    assertTrue(masterVersion2.compareTo(master2.getPersistenceVersion())<0);
    assertTrue(detailVersion.compareTo(detail.getPersistenceVersion())<0);
    assertEquals(master2, detail.getMaster());
    assertEquals(1, master2.getDetails().size());
    assertEquals(0, master1.getDetails().size());
    for (EntityDetailCascade d : master2.getDetails()) {
      assertEquals(detail, d);
    }

    tearDown();
  }

}
