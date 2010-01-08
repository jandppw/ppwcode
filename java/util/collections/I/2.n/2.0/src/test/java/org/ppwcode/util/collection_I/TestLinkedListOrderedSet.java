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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Iterator;
import java.util.List;

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
public class TestLinkedListOrderedSet {

//  @BeforeClass
//  public static void setUpBeforeClass() throws Exception {
//  }
//
//  @AfterClass
//  public static void tearDownAfterClass() throws Exception {
//  }

  @Before
  public void setUp() throws Exception {
    $empty = new LinkedListOrderedSet<Integer>();
    $llosUp = new LinkedListOrderedSet<Integer>();
    for (int i = 0; i < $size; i++) {
      $llosUp.add(i);
    }
    $llosDown = new LinkedListOrderedSet<Integer>();
    for (int i = $size - 1; i >= 0; i--) {
      $llosDown.add(i);
    }
  }

  @After
  public void tearDown() throws Exception {
    $empty = null;
    $llosUp = null;
    $llosDown = null;
  }

  private int $size = 10;
  private LinkedListOrderedSet<Integer> $empty;
  private LinkedListOrderedSet<Integer> $llosUp;
  private LinkedListOrderedSet<Integer> $llosDown;

  @Test
  public void testSize() {
    assertEquals(0, $empty.size());
    assertEquals($size, $llosUp.size());
    assertEquals($size, $llosDown.size());
  }

  @Test
  public void testGet() {
    for (int i = 0; i < $size; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    for (int i = $size - 1; i >= 0; i--) {
      assertEquals($size - 1 - i, $llosDown.get(i).intValue());
    }
    try {
      $llosUp.get(-1);
      fail();
    }
    catch (IndexOutOfBoundsException ioobExc) {
      // expected
    }
    catch (Throwable t) {
      fail();
    }
    try {
      $llosUp.get($size);
      fail();
    }
    catch (IndexOutOfBoundsException ioobExc) {
      // expected
    }
    catch (Throwable t) {
      fail();
    }
  }

  @Test
  public void testIndexOf() {
    for (int i = 0; i < $size; i++) {
      assertEquals(i, $llosUp.indexOf(i));
    }
    for (int i = $size - 1; i >= 0; i--) {
      assertEquals($size - 1 - i, $llosDown.indexOf(i));
    }
    assertEquals(-1, $empty.indexOf(11));
    assertEquals(-1, $llosUp.indexOf(11));
    assertEquals(-1, $llosDown.indexOf(11));
  }

  @Test
  public void testIterator1() {
    Iterator<Integer> iter = $empty.iterator();
    assertFalse(iter.hasNext());
  }

  @Test
  public void testIterator2() {
    Iterator<Integer> iter = $llosUp.iterator();
    int counter = 0;
    while (iter.hasNext()) {
      assertEquals(counter, iter.next().intValue());
      counter++;
    }
  }

  @Test
  public void testIterator3() {
    Iterator<Integer> iter = $llosUp.iterator();
    while (iter.hasNext()) {
      iter.next();
      iter.remove();
    }
    assertTrue($llosUp.isEmpty());
  }

  @Test
  public void testAsList() {
    assertTrue($empty.asList().isEmpty());
    List<Integer> l = $llosUp.asList();
    for (int i = 0; i < $size; i++) {
      assertEquals(i, l.get(i).intValue());
    }
    l = $llosDown.asList();
    for (int i = $size - 1; i >= 0; i--) {
      assertEquals($size - 1 - i, l.get(i).intValue());
    }
  }

  @Test
  public void testAddE22() {
    boolean result = $llosUp.add(22);
    assertTrue(result);
    assertEquals($size + 1, $llosUp.size());
    assertTrue($llosUp.contains(22));
    for (int i = 0; i < $size; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    assertEquals(10, $llosUp.indexOf(22));
    assertEquals(22, $llosUp.get(10).intValue());
  }

  @Test
  public void testAddE5() {
    boolean result = $llosUp.add(5);
    assertTrue(result);
    assertEquals($size, $llosUp.size());
    assertTrue($llosUp.contains(5));
    for (int i = 0; i < 5; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    for (int i = 5; i < $size - 2; i++) {
      assertEquals(i + 1, $llosUp.get(i).intValue());
    }
    assertEquals(9, $llosUp.indexOf(5));
    assertEquals(5, $llosUp.get(9).intValue());
  }

  @Test
  public void testAddE0() {
    boolean result = $llosUp.add(0);
    assertTrue(result);
    assertEquals($size, $llosUp.size());
    assertTrue($llosUp.contains(0));
    for (int i = 0; i < $size - 2; i++) {
      assertEquals(i + 1, $llosUp.get(i).intValue());
    }
    assertEquals(9, $llosUp.indexOf(0));
    assertEquals(0, $llosUp.get(9).intValue());
  }

  @Test
  public void testAddIntE5_22() {
    testAddIntE22(5);
  }

  @Test
  public void testAddIntE5_7() {
    testAddIntE7before(5);
  }

  @Test
  public void testAddIntE7_5() {
    testAddIntE5after(7);
  }

  @Test
  public void testAddIntE5_5() {
    testAddIntEii(5);
  }

  @Test
  public void testAddIntE0_22() {
    testAddIntE22(0);
  }

  @Test
  public void testAddIntE0_7() {
    testAddIntE7before(0);
  }

  @Test
  public void testAddIntE0_0() {
    testAddIntEii(0);
  }

  @Test
  public void testAddIntE9_22() {
    testAddIntE22(9);
  }

  @Test
  public void testAddIntE9_5() {
    testAddIntE5after(9);
  }

  @Test
  public void testAddIntE9_9() {
    testAddIntEii(9);
  }


  private void testAddIntE22(int index) {
    boolean result = $llosUp.add(index, 22);
    assertTrue(result);
    assertEquals($size + 1, $llosUp.size());
    assertTrue($llosUp.contains(22));
    for (int i = 0; i < index; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    assertEquals(22, $llosUp.get(index).intValue());
    for (int i = index + 1; i <= $size; i++) {
      assertEquals(i - 1, $llosUp.get(i).intValue());
    }
    assertEquals(index, $llosUp.indexOf(22));
  }

  private void testAddIntE7before(int index) {
    boolean result = $llosUp.add(index, 7);
    assertTrue(result);
    assertEquals($size, $llosUp.size());
    assertTrue($llosUp.contains(7));
    for (int i = 0; i < index; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    assertEquals(7, $llosUp.get(index).intValue());
    for (int i = index + 1; i <= 7; i++) {
      assertEquals(i - 1, $llosUp.get(i).intValue());
    }
    for (int i = 8; i < $size; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    assertEquals(index, $llosUp.indexOf(7));
  }

  private void testAddIntE5after(int index) {
    boolean result = $llosUp.add(index, 5);
    assertTrue(result);
    assertEquals($size, $llosUp.size());
    assertTrue($llosUp.contains(index));
    for (int i = 0; i < 5; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    for (int i = 5; i <= index - 1; i++) {
      assertEquals(i + 1, $llosUp.get(i).intValue());
    }
    assertEquals(5, $llosUp.get(index).intValue());
    for (int i = index + 1; i < $size; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    assertEquals(index, $llosUp.indexOf(5));
  }

  private void testAddIntEii(int index) {
    boolean result = $llosUp.add(index, index);
    assertFalse(result);
    assertEquals($size, $llosUp.size());
    assertTrue($llosUp.contains(index));
    for (int i = 0; i < $size; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
  }

//  @Test
//  public void testAddAllIntCollectionOfQextendsE5() {
//    testAddAllIntCollectionOfQextendsE(5);
//  }
//
//  @Test
//  public void testAddAllIntCollectionOfQextendsE0() {
//    testAddAllIntCollectionOfQextendsE(0);
//  }
//
//  @Test
//  public void testAddAllIntCollectionOfQextendsE9() {
//    testAddAllIntCollectionOfQextendsE(9);
//  }
//
//  private void testAddAllIntCollectionOfQextendsE(int index) {
//    boolean result = $llosUp.addAll(index, $llosDown);
//    assertTrue(result);
//    assertEquals($size * 2, $llosUp.size());
//    for (int i = 0; i < index; i++) {
//      assertEquals(i, $llosUp.get(i));
//    }
//    for (int i = index; i < index + $size - 1; i++) {
//      assertEquals($size - (i - index) - 1, $llosUp.get(i));
//    }
//    for (int i = index + $size; i < (2 * $size) - 1; i++) {
//      assertEquals(i + $size, $llosUp.get(i));
//    }
//  }

  @Test
  public void testRemoveInt5() {
    testRemoveInt(5);
  }

  @Test
  public void testRemoveInt0() {
    testRemoveInt(0);
  }

  @Test
  public void testRemoveInt9() {
    testRemoveInt(9);
  }

  private void testRemoveInt(int index) {
    Integer result = $llosUp.remove(index);
    assertEquals($size - 1, $llosUp.size());
    assertEquals(index, result.intValue());
    for (int i = 0; i < index; i++) {
      assertEquals(i, $llosUp.get(i).intValue());
    }
    for (int i = index; i < $size - 1; i++) {
      assertEquals(i + 1, $llosUp.get(i).intValue());
    }
  }

}

