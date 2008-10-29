/*<license>
Copyright 2008 - $Date: 2008-10-23 11:51:38 +0200 (Thu, 23 Oct 2008) $ by PeopleWare n.v.

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

package org.ppwcode.research.jpa.openjpa.valuehandlers;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.junit.Test;
import org.ppwcode.value_III.localization.LocalizedString;
import org.ppwcode.vernacular.value_III.SemanticValueException;

public class ValueHandlersTest {

  static final String PERSISTENCE_UNIT_NAME = "be_hdp_contracts_I_IBMOpenJPA_test";
  //static final String PERSISTENCE_UNIT_NAME = "be_hdp_contracts_I";

  final static Logger LOGGER = Logger.getLogger("ValueHandlersTest");

  private void displayTest(String msg1, String msg2) {
    System.out.println();
    System.out.println();
    System.out.println(msg1);
    System.out.println(msg2);
  }

  @Test
  public void valueHandlers1() throws SemanticValueException, SQLException {
    displayTest("Value handlers", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    AnEntity ae = new AnEntity();
    LocalizedString ls = new LocalizedString(new Locale("nl"), "'t es moar een test");
    ae.setLocalizedString(ls);
    System.out.println("an entity: " + ae);

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(ae);
    tx.commit();
    em.close();
    System.out.println("an entity after create in DB: " + ae);

    Integer aeId = ae.getPersistenceId();

    em = emf.createEntityManager();
    AnEntity fromDb = em.find(AnEntity.class, aeId);
    System.out.println("fromDB: " + fromDb);

    OpenJPAEntityManager kem = OpenJPAPersistence.cast(em);
    Connection conn = (Connection)kem.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("SELECT * FROM org_ppwcode_research_jpa_openjpa_valuehandlers_anentity");
    ResultSetMetaData rsmd = rs.getMetaData();
    System.out.println("nr of columns: " + rsmd.getColumnCount());
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      System.out.println(i + ": " + rsmd.getColumnName(i) + " " + rsmd.getColumnClassName(i) + " " + rsmd.getColumnTypeName(i));
    }
    assertEquals("PERSISTENCEID", rsmd.getColumnName(1));
    assertEquals("LOCALIZEDSTRING", rsmd.getColumnName(2));
    assertEquals("PERSISTENCEVERSION", rsmd.getColumnName(3));
    while (rs.next()) {
      System.out.println("PERSISTENCEID           : " + rs.getInt("PERSISTENCEID"));
      System.out.println("PERSISTENCEVERSION      : " + rs.getInt("PERSISTENCEVERSION"));
      System.out.println("LOCALIZEDSTRING         : " + rs.getObject("LOCALIZEDSTRING"));
      assertEquals(ae.getPersistenceId(), rs.getObject("PERSISTENCEID"));
      assertEquals(ae.getPersistenceVersion(), rs.getObject("PERSISTENCEVERSION"));
    }
  }

}
