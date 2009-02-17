/*<license>
Copyright 2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $ by the authors mentioned below.

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

package org.ppwcode.util.smallfries;


import static org.junit.Assert.assertEquals;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.ComparisonUtil;


@Copyright("2007 - $Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1107 $",
         date     = "$Date: 2009-02-17 14:18:20 +0100 (Tue, 17 Feb 2009) $")
public class TestComparisonUtil {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void equalsWithNull_String_1() {
    equalsWithNull(null, null);
  }

  @Test
  public void equalsWithNull_String_2() {
    equalsWithNull(null, "");
  }

  @Test
  public void equalsWithNull_String_3() {
    equalsWithNull(null, "Test");
  }

  @Test
  public void equalsWithNull_String_4() {
    equalsWithNull("", null);
  }

  @Test
  public void equalsWithNull_String_5() {
    equalsWithNull(null, "Test");
  }

  @Test
  public void equalsWithNull_String_6() {
    equalsWithNull("", "");
  }

  @Test
  public void equalsWithNull_String_7() {
    equalsWithNull("", "Test");
  }

  @Test
  public void equalsWithNull_String_8() {
    equalsWithNull("Test", "Test");
  }

  @Test
  public void equalsWithNull_String_9() {
    equalsWithNull("Test", "");
  }

  @Test
  public void equalsWithNull_String_10() {
    String t = "Test";
    equalsWithNull(t, t);
  }

  @Test
  public void equalsWithNull_String_11() {
    String t = "Test";
    equalsWithNull(t, "Test");
  }

  @Test
  public void equalsWithNull_String_12() {
    String t = "Test";
    equalsWithNull("Test", t);
  }

  @Test
  public void equalsWithNull_Date_2() {
    equalsWithNull(null, new Date());
  }

  @Test
  public void equalsWithNull_Date_4() {
    equalsWithNull(new Date(), null);
  }

  @Test
  public void equalsWithNull_Date_5() {
    Date d = new Date();
    equalsWithNull(d, d);
  }

  @Test
  public void equalsWithNull_Date_6() {
    equalsWithNull(new Date(), new Date());
  }

  @Test
  public void equalsWithNull_Date_7() {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(new Date());
    gc.add(Calendar.DATE, -1);
    Date yesterday = gc.getTime();
    equalsWithNull(new Date(), yesterday);
  }

  @Test
  public void equalsWithNull_Date_8() {
    Date d = new Date();
    equalsWithNull(d, d.clone());
    /* it is remarkable that the compiler eats this;
     * well, not really: in this type inference, _Value_ --> Object!
     */
  }

  @Test
  public void equalsWithNull_Date_9() {
    Date d = new Date();
    equalsWithNull(d.clone(), d);
    /* it is remarkable that the compiler eats this;
     * well, not really: in this type inference, _Value_ --> Object!
     */
  }

  @Test
  public void equalsWithNull_Date_10() {
    Date d = new Date();
    equalsWithNull(d, (Date)d.clone());
  }

  @Test
  public void equalsWithNull_Date_11() {
    Date d = new Date();
    equalsWithNull((Date)d.clone(), d);
  }

  @Test
  public void equalsWithNull_Bean_2() {
    equalsWithNull(null, new Object());
  }

  @Test
  public void equalsWithNull_Bean_4() {
    equalsWithNull(new Object(), null);
  }

  @Test
  public void equalsWithNull_Bean_6() {
    Object o = new Object();
    equalsWithNull(o, o);
  }

  @Test
  public void equalsWithNull_Bean_7() {
    equalsWithNull(new Object(), new Object());
  }

  private <_Value_> void equalsWithNull(_Value_ a, _Value_ b) {
    boolean result = ComparisonUtil.equalsWithNull(a, b);
    assertEquals(a == b ? true : result, result);
    assertEquals(a != b && a == null ? false : result, result);
    assertEquals(a != b && b == null ? false : result, result);
    assertEquals(a != b && a != null && b != null ? a.equals(b) : result, result);
  }

}

