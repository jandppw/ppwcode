/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.util.collection_I;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.collection_I.CollectionUtil.intersection;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestCollectionUtil {

//  @BeforeClass
//  public static void setUpBeforeClass() throws Exception {
//  }
//
//  @AfterClass
//  public static void tearDownAfterClass() throws Exception {
//  }
//
  @Before
  public void setUp() {
    $s1 = new HashSet<String>();
    $s1.add("lala");
    $s1.add("lele");
    $s1.add("lili");
    $s1.add("lolo");
    $s1.add("lulu");
    $s1.add("papa");
    $s1.add("pipi");
    $s1.add("popo");
    $s2 = new TreeSet<String>();
    $s2.add("kaka");
    $s2.add("keke");
    $s2.add("kiki");
    $s2.add("koko");
    $s2.add("kuku");
    $s2.add("papa");
    $s2.add("pipi");
    $s2.add("popo");
    $e1 = Collections.emptySet();
    $e2 = Collections.emptySet();
    $os = new LinkedListOrderedSet<String>();
    $os.add("kaka");
    $os.add("keke");
    $os.add("kiki");
    $os.add("koko");
    $os.add("kuku");
  }

  @After
  public void tearDown() {
    $s1 = null;
    $s2 = null;
    $e1 = null;
    $e2 = null;
    $os = null;
  }

  private Set<String> $s1;
  private Set<String> $s2;
  private Set<String> $e1;
  private Set<String> $e2;
  private OrderedSet<String> $os;

  @Test
  public void testIntersection1() {
    Set<String> i = intersection($e1, $e2);
    assertNotNull(i);
    assertTrue(i.isEmpty());
  }

  @Test
  public void testIntersection2a() {
    Set<String> i = intersection($e1, $s2);
    assertNotNull(i);
    assertTrue(i.isEmpty());
  }

  @Test
  public void testIntersection2b() {
    Set<String> i = intersection($s1, $e2);
    assertNotNull(i);
    assertTrue(i.isEmpty());
  }

  @Test
  public void testIntersection3() {
    Set<String> i = intersection($s1, $s2);
    assertNotNull(i);
    assertTrue(! i.isEmpty());
    assertEquals(3, i.size());
    assertTrue(i.contains("papa"));
    assertTrue(i.contains("pipi"));
    assertTrue(i.contains("popo"));
  }

  @Test
  public void testEmptySortedSet() {
    SortedSet<String> ssstr1 = CollectionUtil.emptySortedSet();
    SortedSet<String> ssstr2 = CollectionUtil.emptySortedSet();
    assertTrue(ssstr1 == ssstr2);
    assertTrue(ssstr1.isEmpty());
    assertEquals(0, ssstr1.size());
    assertTrue(! ssstr1.contains("DODO"));
    assertNull(ssstr1.comparator());
    try {
      ssstr1.first();
      fail();
    }
    catch (NoSuchElementException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      ssstr1.last();
      fail();
    }
    catch (NoSuchElementException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    assertEquals(ssstr1, ssstr1.headSet("dada"));
    assertEquals(ssstr1, ssstr1.tailSet("dodo"));
    assertEquals(ssstr1, ssstr1.subSet("aaaa", "zzzz"));
  }

  @Test
  public void testEmptyOrderedSet() {
    OrderedSet<String> osstr1 = CollectionUtil.emptyOrderedSet();
    OrderedSet<String> osstr2 = CollectionUtil.emptyOrderedSet();
    assertTrue(osstr1 == osstr2);
    assertTrue(osstr1.isEmpty());
    assertEquals(0, osstr1.size());
    assertTrue(! osstr1.contains("DODO"));
    assertEquals(-1, osstr1.indexOf("JOS"));
    assertEquals(-1, osstr1.indexOf(null));
    try {
      osstr1.add(5, "TRIING");
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      osstr1.get(0);
      fail();
    }
    catch (IndexOutOfBoundsException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      osstr1.remove(0);
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    assertEquals(Collections.emptyList(), osstr1.asList());
  }

  @Test
  public void testUnmodifiableOrderedSet1() {
    OrderedSet<String> osstr = CollectionUtil.unmodifiableOrderedSet($os);
    assertNotNull(osstr);
    assertEquals($os, osstr);
    unmodifiable(osstr);
  }

  @Test
  public void testUnmodifiableOrderedSet2() {
    OrderedSet<String> osstr = CollectionUtil.unmodifiableOrderedSet(
          CollectionUtil.<String>emptyOrderedSet());
    assertNotNull(osstr);
    assertTrue(osstr.isEmpty());
    unmodifiable(osstr);
  }

  private void unmodifiable(OrderedSet<String> osstr) {
    try {
      osstr.add("TRIING");
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      osstr.add(5, "TRIING");
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      osstr.remove("TRIING");
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      osstr.remove(5);
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      osstr.addAll($e1);
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      osstr.removeAll($e1);
      fail();
    }
    catch (UnsupportedOperationException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
  }

}

