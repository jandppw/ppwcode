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

package org.ppwcode.research.jpa.openjpa.valuehandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.junit.Assert;
import org.junit.Test;
import org.ppwcode.util.reflect_I.InstanceHelpers;
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
  public void testSerializableProperties1() throws SemanticValueException, SQLException {
    displayTest("Serializable properties, null locale, null localized string", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    AnEntitySerializableProperties ae = createAnEntity(emf, null, null, AnEntitySerializableProperties.class);

    saveValidateInDbAndRetrieve(emf, "org_ppwcode_research_jpa_openjpa_valuehandlers_anentityserializableproperties", ae, null, null);
  }

  @Test
  public void testSerializableProperties2() throws SemanticValueException, SQLException {
    displayTest("Serializable properties, effective locale, effective localized string", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    LocalizedString ls = new LocalizedString(new Locale("nl"), "'t es moar een test");
    Locale l = Locale.JAPANESE;
    AnEntitySerializableProperties ae = createAnEntity(emf, ls, l, AnEntitySerializableProperties.class);

    saveValidateInDbAndRetrieve(emf, "org_ppwcode_research_jpa_openjpa_valuehandlers_anentityserializableproperties", ae, ls, l);
  }

  @Test
  public void testValueHandlerProperties1() throws SemanticValueException, SQLException {
    displayTest("Value handler properties, null locale", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    AnEntityValueHandlerProperties ae = createAnEntity(emf, null, null, AnEntityValueHandlerProperties.class);

    saveValidateInDbAndRetrieve(emf, "org_ppwcode_research_jpa_openjpa_valuehandlers_anentityvaluehandlerproperties", ae, null, null);
  }

  private <_T_ extends AnEntity> _T_ createAnEntity(EntityManagerFactory emf, LocalizedString ls, Locale l, Class<_T_> type) {
    _T_ ae = InstanceHelpers.newInstance(type);
    ae.setLocalizedString(ls);
    ae.setLocale(l);
    System.out.println("an entity: " + ae);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(ae);
    tx.commit();
    em.close();
    System.out.println("an entity after create in DB: " + ae);
    return ae;
  }

  private void saveValidateInDbAndRetrieve(EntityManagerFactory emf,
                                           String tableName,
                                           AnEntity ae,
                                           LocalizedString ls,
                                           Locale l) throws SQLException {

    Integer aeId = ae.getPersistenceId();

    EntityManager em = emf.createEntityManager();
    OpenJPAEntityManager kem = OpenJPAPersistence.cast(em);
    Connection conn = (Connection)kem.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);
    ResultSetMetaData rsmd = rs.getMetaData();
    System.out.println("nr of columns: " + rsmd.getColumnCount());
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      System.out.println(i + ": " + rsmd.getColumnName(i) + " " + rsmd.getColumnClassName(i) + " " + rsmd.getColumnTypeName(i));
    }
    assertEquals("PERSISTENCEID", rsmd.getColumnName(1));
    assertEquals("LOCALE", rsmd.getColumnName(2));
    assertEquals("LOCALIZEDSTRING", rsmd.getColumnName(3));
    assertEquals("PERSISTENCEVERSION", rsmd.getColumnName(4));
    boolean foundOne = false;
    while (rs.next()) {
      System.out.println("PERSISTENCEID           : " + rs.getInt("PERSISTENCEID"));
      System.out.println("PERSISTENCEVERSION      : " + rs.getInt("PERSISTENCEVERSION"));
      String columnName = "LOCALIZEDSTRING";
      printVARBINARYColumn(rs, columnName);
      System.out.println("LOCALE                  : " + rs.getObject("LOCALE"));
      Object idFromDb = rs.getObject("PERSISTENCEID");
      if (ae.getPersistenceId().equals(idFromDb)) {
        assertEquals(ae.getPersistenceVersion(), rs.getObject("PERSISTENCEVERSION"));
        assertEquals(jpaDefaultStoredLocaleString(ae), rs.getString("LOCALE"));
        foundOne = true;
      }
      System.out.println();
    }
    assertTrue(foundOne);
    em.close();
    conn.close();

    em = emf.createEntityManager();
    AnEntity fromDb = em.find(ae.getClass(), aeId);
    System.out.println("fromDB: " + fromDb);
    assertNotSame(fromDb, ae);
    assertEquals(ls, fromDb.getLocalizedString());
    assertEquals(l, fromDb.getLocale());
  }

  private void printVARBINARYColumn(ResultSet rs, String columnName) throws SQLException {
    byte[] serializedForm = rs.getBytes(columnName);
    System.out.println(columnName + "         : " + byteArrayRepresentation(serializedForm));
  }

  private String byteArrayRepresentation(byte[] serializedForm) {
    if (serializedForm == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("0x");
    int i = 0;
    while (i < serializedForm.length && i < 10) {
      byte b = serializedForm[i];
      sb.append(Integer.toHexString(b));
      i++;
    }
    if (i < serializedForm.length) {
      sb.append("...");
    }
    return sb.toString();
  }

  private String jpaDefaultStoredLocaleString(AnEntity ae) {
    return ae.getLocale() == null ? null : ae.getLocale().getLanguage() + "_" + ae.getLocale().getCountry() + "_" + ae.getLocale().getVariant();
  }

}
