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
import static org.ppwcode.value_III.time.TimeHelpers.dayDate;

import java.beans.PropertyEditorManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ppwcode.util.reflect_I.InstanceHelpers;
import org.ppwcode.value_III.ext.java.util.LocaleEditor;
import org.ppwcode.value_III.ext.java.util.TimeZoneEditor;
import org.ppwcode.value_III.localization.LocalizedString;
import org.ppwcode.value_III.time.interval.BeginEndTimeInterval;
import org.ppwcode.value_III.time.interval.DayDateTimeInterval;
import org.ppwcode.value_III.time.interval.DeterminateIntradayTimeInterval;
import org.ppwcode.value_III.time.interval.IllegalTimeIntervalException;
import org.ppwcode.value_III.time.interval.IntradayTimeInterval;
import org.ppwcode.vernacular.value_III.SemanticValueException;

public class ValueHandlersTest {

  private static final String TABLE_NAME_VALUEHANDLERPROPERTIES = "org_ppwcode_research_jpa_openjpa_valuehandlers_anentityvaluehandlerproperties";

  private static final String TABLE_NAME_SERIALIZABLEPROPERTIES = "org_ppwcode_research_jpa_openjpa_valuehandlers_anentityserializableproperties";

  static final String PERSISTENCE_UNIT_NAME = "be_hdp_contracts_I_IBMOpenJPA_test";

  final static Logger LOGGER = Logger.getLogger("ValueHandlersTest");

  private void displayTest(String msg1, String msg2) {
    System.out.println();
    System.out.println();
    System.out.println(msg1);
    System.out.println(msg2);
  }

  @BeforeClass
  public static void registerPropertyEditors() {
    PropertyEditorManager.registerEditor(Locale.class, LocaleEditor.class);
    PropertyEditorManager.registerEditor(TimeZone.class, TimeZoneEditor.class);
  }

  @Test
  public void testSerializableProperties1() throws SemanticValueException, SQLException {
    displayTest("Serializable properties, null properties", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    AnEntitySerializableProperties ae = createAnEntity(emf, null, null, null, null, null, null, AnEntitySerializableProperties.class);

    saveValidateInDbAndRetrieve(emf, TABLE_NAME_SERIALIZABLEPROPERTIES, ae);
  }

  @Test
  public void testSerializableProperties2() throws SemanticValueException, SQLException {
    displayTest("Serializable properties, effective properties", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    AnEntitySerializableProperties ae = prepA(emf, AnEntitySerializableProperties.class);

    saveValidateInDbAndRetrieve(emf, TABLE_NAME_SERIALIZABLEPROPERTIES, ae);
  }

  @Test
  public void testValueHandlerProperties1() throws SemanticValueException, SQLException {
    displayTest("Value handler properties, null properties", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    AnEntityValueHandlerProperties ae = createAnEntity(emf, null, null, null, null, null, null, AnEntityValueHandlerProperties.class);

    saveValidateInDbAndRetrieve(emf, TABLE_NAME_VALUEHANDLERPROPERTIES, ae);
  }

  @Test
  public void testValueHandlerProperties2() throws SemanticValueException, SQLException {
    displayTest("Value handler properties, effective properties", "");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    AnEntityValueHandlerProperties ae = prepA(emf, AnEntityValueHandlerProperties.class);

    saveValidateInDbAndRetrieve(emf, TABLE_NAME_VALUEHANDLERPROPERTIES, ae);
  }

  private <_T_ extends AnEntity> _T_ prepA(EntityManagerFactory emf, Class<_T_> type) throws SemanticValueException, IllegalTimeIntervalException {
    LocalizedString ls = new LocalizedString(new Locale("nl"), "'t es moar een test");
    Locale l = Locale.JAPANESE;
    TimeZone tz = TimeZone.getTimeZone("Europe/Moscow");
    GregorianCalendar gc = new GregorianCalendar(1995, 7, 12, 13, 45, 9);
    gc.setTimeZone(tz);
    Date now = new Date();
    BeginEndTimeInterval beti = new BeginEndTimeInterval(gc.getTime(), now);
    // MUDO add double null, left null, right null
    assert tz != null;
    Date pastDd = dayDate(gc.getTime(), tz);
    Date today = dayDate(now, tz);
    DayDateTimeInterval ddti = new DayDateTimeInterval(pastDd, today, tz);
    // MUDO add double null, left null, right null
    tz = TimeZone.getTimeZone("America/Los_Angeles");
    GregorianCalendar gc2 = new GregorianCalendar(1995, 7, 12, 13, 45, 56);
    gc2.setTimeZone(tz);
    GregorianCalendar gc3 = new GregorianCalendar(1995, 7, 12, 23, 4, 56);
    gc3.setTimeZone(tz);
    assert tz != null;
    DeterminateIntradayTimeInterval diti = new DeterminateIntradayTimeInterval(gc2.getTime(), gc3.getTime(), tz);
    tz = TimeZone.getTimeZone("Europe/Moscow");
    GregorianCalendar gc4 = new GregorianCalendar(1995, 7, 12, 13, 45, 56);
    gc4.setTimeZone(tz);
    GregorianCalendar gc5 = new GregorianCalendar(1995, 7, 12, 23, 4, 56);
    gc5.setTimeZone(tz);
    IntradayTimeInterval iti = new IntradayTimeInterval(gc4.getTime(), gc5.getTime(), tz);
    // MUDO add double null, left null, right null
    _T_ ae = createAnEntity(emf, ls, l, beti, ddti, diti, iti, type);
    return ae;
  }

  private <_T_ extends AnEntity> _T_ createAnEntity(EntityManagerFactory emf, LocalizedString ls, Locale l,
                                                    BeginEndTimeInterval beti, DayDateTimeInterval ddti, DeterminateIntradayTimeInterval diti,
                                                    IntradayTimeInterval iti, Class<_T_> type) {
    _T_ ae = InstanceHelpers.newInstance(type);
    ae.setLocalizedString(ls);
    ae.setLocalizedString2(ls);
    ae.setLocale(l);
    ae.setBeginEndTimeInterval(beti);
    ae.setDayDateTimeInterval(ddti);
    ae.setDeterminateIntradayTimeInterval(diti);
    ae.setIntradayTimeInterval(iti);
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
                                           AnEntity ae) throws SQLException {
    Locale aeLocale =  ae.getLocale();
    LocalizedString aeLocalizedString = ae.getLocalizedString();
    Integer aeId = ae.getPersistenceId();
    BeginEndTimeInterval eaBeti = ae.getBeginEndTimeInterval();
    DayDateTimeInterval eaDdti = ae.getDayDateTimeInterval();
    IntradayTimeInterval eaIdti = ae.getIntradayTimeInterval();
    DeterminateIntradayTimeInterval eaDiti = ae.getDeterminateIntradayTimeInterval();

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
    expectedColumns(ae.getClass(), rsmd);
    boolean foundOne = false;
    while (rs.next()) {
      System.out.println("PERSISTENCEID           : " + rs.getInt("PERSISTENCEID"));
      System.out.println("PERSISTENCEVERSION      : " + rs.getInt("PERSISTENCEVERSION"));
      printLocalizedStringFromSql(ae.getClass(), rs);
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
    assertEquals(aeLocalizedString, fromDb.getLocalizedString());
    assertEquals(aeLocalizedString, fromDb.getLocalizedString2());
    assertEquals(aeLocale, fromDb.getLocale());
    assertEquals(eaBeti, fromDb.getBeginEndTimeInterval());
    assertEquals(eaDdti, fromDb.getDayDateTimeInterval());
    assertEquals(eaIdti, fromDb.getIntradayTimeInterval());
    assertEquals(eaDiti, fromDb.getDeterminateIntradayTimeInterval());
  }

  private void printLocalizedStringFromSql(Class<? extends AnEntity> type, ResultSet rs) throws SQLException {
    if (type == AnEntitySerializableProperties.class) {
      printVARBINARYColumn(rs, "LOCALIZEDSTRING");
      printVARBINARYColumn(rs, "LOCALIZEDSTRING2");
      printVARBINARYColumn(rs, "BEGINENDTIMEINTERVAL");
      printVARBINARYColumn(rs, "DAYDATETIMEINTERVAL");
      printVARBINARYColumn(rs, "DETERMINATEINTRADAYTIMEINTERVAL");
      printVARBINARYColumn(rs, "INTRADAYTIMEINTERVAL");
    }
    else if (type == AnEntityValueHandlerProperties.class) {
      System.out.println("$LOCALIZEDSTRING_LOCALE                   : " + rs.getString("$LOCALIZEDSTRING_LOCALE"));
      System.out.println("$LOCALIZEDSTRING_STRING                   : " + rs.getString("$LOCALIZEDSTRING_STRING"));
      System.out.println("LOCALIZEDSTRING2_LOCALE                   : " + rs.getString("LOCALIZEDSTRING2_LOCALE"));
      System.out.println("LOCALIZEDSTRING2_STRING                   : " + rs.getString("LOCALIZEDSTRING2_STRING"));
      System.out.println("$BEGINENDTIMEINTERVAL_BEGIN               : " + rs.getTimestamp("$BEGINENDTIMEINTERVAL_BEGIN"));
      System.out.println("$BEGINENDTIMEINTERVAL_END                 : " + rs.getTimestamp("$BEGINENDTIMEINTERVAL_END"));
      System.out.println("$DAYDATETIMEINTERVAL_BEGIN                : " + rs.getDate("$DAYDATETIMEINTERVAL_BEGIN"));
      System.out.println("$DAYDATETIMEINTERVAL_END                  : " + rs.getDate("$DAYDATETIMEINTERVAL_END"));
      System.out.println("$DAYDATETIMEINTERVAL_TIMEZONE             : " + rs.getString("$DAYDATETIMEINTERVAL_TIMEZONE"));
      System.out.println("$DETERMINATEINTRADAYTIMEINTERVAL_DAY      : " + rs.getDate("$DETERMINATEINTRADAYTIMEINTERVAL_DAY"));
      System.out.println("$DETERMINATEINTRADAYTIMEINTERVAL_BEGIN    : " + rs.getTime("$DETERMINATEINTRADAYTIMEINTERVAL_BEGIN"));
      System.out.println("$DETERMINATEINTRADAYTIMEINTERVAL_END      : " + rs.getTime("$DETERMINATEINTRADAYTIMEINTERVAL_END"));
      System.out.println("$DETERMINATEINTRADAYTIMEINTERVAL_TIMEZONE : " + rs.getString("$DETERMINATEINTRADAYTIMEINTERVAL_TIMEZONE"));
      System.out.println("$INTRADAYTIMEINTERVAL_DAY                 : " + rs.getDate("$INTRADAYTIMEINTERVAL_DAY"));
      System.out.println("$INTRADAYTIMEINTERVAL_BEGIN               : " + rs.getTime("$INTRADAYTIMEINTERVAL_BEGIN"));
      System.out.println("$INTRADAYTIMEINTERVAL_END                 : " + rs.getTime("$INTRADAYTIMEINTERVAL_END"));
      System.out.println("$INTRADAYTIMEINTERVAL_TIMEZONE            : " + rs.getString("$INTRADAYTIMEINTERVAL_TIMEZONE"));
    }
  }

  private void expectedColumns(Class<? extends AnEntity> type, ResultSetMetaData rsmd) throws SQLException {
    if (type == AnEntitySerializableProperties.class) {
      assertEquals("PERSISTENCEID", rsmd.getColumnName(1));
      assertEquals("BEGINENDTIMEINTERVAL", rsmd.getColumnName(2));
      assertEquals("DAYDATETIMEINTERVAL", rsmd.getColumnName(3));
      assertEquals("DETERMINATEINTRADAYTIMEINTERVAL", rsmd.getColumnName(4));
      assertEquals("INTRADAYTIMEINTERVAL", rsmd.getColumnName(5));
      assertEquals("LOCALE", rsmd.getColumnName(6));
      assertEquals("LOCALIZEDSTRING", rsmd.getColumnName(7));
      assertEquals("LOCALIZEDSTRING2", rsmd.getColumnName(8));
      assertEquals("PERSISTENCEVERSION", rsmd.getColumnName(9));
    }
    else if (type == AnEntityValueHandlerProperties.class) {
      int i = 1;
      assertEquals("PERSISTENCEID", rsmd.getColumnName(i++));
      assertEquals("$BEGINENDTIMEINTERVAL_BEGIN", rsmd.getColumnName(i++));
      assertEquals("$BEGINENDTIMEINTERVAL_END", rsmd.getColumnName(i++));
      assertEquals("$DAYDATETIMEINTERVAL_BEGIN", rsmd.getColumnName(i++));
      assertEquals("$DAYDATETIMEINTERVAL_END", rsmd.getColumnName(i++));
      assertEquals("$DAYDATETIMEINTERVAL_TIMEZONE", rsmd.getColumnName(i++));
      assertEquals("$DETERMINATEINTRADAYTIMEINTERVAL_DAY", rsmd.getColumnName(i++));
      assertEquals("$DETERMINATEINTRADAYTIMEINTERVAL_BEGIN", rsmd.getColumnName(i++));
      assertEquals("$DETERMINATEINTRADAYTIMEINTERVAL_END", rsmd.getColumnName(i++));
      assertEquals("$DETERMINATEINTRADAYTIMEINTERVAL_TIMEZONE", rsmd.getColumnName(i++));
      assertEquals("$INTRADAYTIMEINTERVAL_DAY", rsmd.getColumnName(i++));
      assertEquals("$INTRADAYTIMEINTERVAL_BEGIN", rsmd.getColumnName(i++));
      assertEquals("$INTRADAYTIMEINTERVAL_END", rsmd.getColumnName(i++));
      assertEquals("$INTRADAYTIMEINTERVAL_TIMEZONE", rsmd.getColumnName(i++));
      assertEquals("LOCALE", rsmd.getColumnName(i++));
      assertEquals("$LOCALIZEDSTRING_LOCALE", rsmd.getColumnName(i++));
      assertEquals("$LOCALIZEDSTRING_STRING", rsmd.getColumnName(i++));
      assertEquals("LOCALIZEDSTRING2_LOCALE", rsmd.getColumnName(i++));
      assertEquals("LOCALIZEDSTRING2_STRING", rsmd.getColumnName(i++));
      assertEquals("PERSISTENCEVERSION", rsmd.getColumnName(i++));
    }
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
    String result = null;
    Locale l = ae.getLocale();
    if (l == null) {
      result = null;
    }
    else if (ae instanceof AnEntitySerializableProperties) {
      result = l.getLanguage() + "_" + l.getCountry() + "_" + l.getVariant();
    }
    else if (ae instanceof AnEntityValueHandlerProperties) {
      result = l.toString();
    }
    return result;
  }

}
