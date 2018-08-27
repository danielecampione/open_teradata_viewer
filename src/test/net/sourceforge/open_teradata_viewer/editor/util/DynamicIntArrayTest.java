/*
 * Open Teradata Viewer ( editor util )
 * Copyright (C) 2015, D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package test.net.sourceforge.open_teradata_viewer.editor.util;

import net.sourceforge.open_teradata_viewer.util.DynamicIntArray;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link DynamicIntArray} class.
 *
 * @author D. Campione
 *
 */
public class DynamicIntArrayTest {

    @Test
    public void testZeroArgConstructor() {
        new DynamicIntArray();
    }

    @Test
    public void testIntArgConstructor() {
        new DynamicIntArray(100);
    }

    @Test
    public void testArrayArgConstructor() {
        new DynamicIntArray(new int[] { 0, 1, 2 });
    }

    @Test
    public void testAdd_1ArgOverload() {
        DynamicIntArray array = new DynamicIntArray();
        Assert.assertEquals(0, array.getSize());
        array.add(5);
        Assert.assertEquals(1, array.getSize());
        array.add(5);
        Assert.assertEquals(2, array.getSize());
    }

    @Test
    public void testAdd_2ArgOverload_Array() {
        DynamicIntArray array = new DynamicIntArray();
        Assert.assertEquals(0, array.getSize());

        array.add(0, new int[] { 1, 2 });
        Assert.assertEquals(2, array.getSize());
        Assert.assertEquals(1, array.get(0));
        Assert.assertEquals(2, array.get(1));

        array.add(1, new int[] { 5, 6 });
        Assert.assertEquals(4, array.getSize());
        Assert.assertEquals(1, array.get(0));
        Assert.assertEquals(5, array.get(1));
        Assert.assertEquals(6, array.get(2));
        Assert.assertEquals(2, array.get(3));
    }

    @Test
    public void testAdd_2ArgOverload_2Ints() {
        DynamicIntArray array = new DynamicIntArray();
        Assert.assertEquals(0, array.getSize());

        array.add(0, 5);
        Assert.assertEquals(1, array.getSize());
        Assert.assertEquals(5, array.get(0));

        array.add(0, 4);
        Assert.assertEquals(2, array.getSize());
        Assert.assertEquals(4, array.get(0));
        Assert.assertEquals(5, array.get(1));
    }

    @Test
    public void testClear() {
        DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
        Assert.assertEquals(3, array.getSize());
        array.clear();
        Assert.assertEquals(0, array.getSize());
    }

    @Test
    public void testContains() {
        DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
        Assert.assertFalse(array.contains(0));
        Assert.assertTrue(array.contains(1));
        Assert.assertTrue(array.contains(2));
        Assert.assertTrue(array.contains(3));
        Assert.assertFalse(array.contains(4));
    }

    @Test
    public void testDecrement() {
        DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
        array.decrement(2, 4);
        Assert.assertEquals(7, array.get(0));
        Assert.assertEquals(7, array.get(1));
        Assert.assertEquals(6, array.get(2));
        Assert.assertEquals(6, array.get(3));
        Assert.assertEquals(7, array.get(4));
    }

    @Test
    public void testfill() {
        DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
        for (int i = 0; i < array.getSize(); i++) {
            Assert.assertEquals(7, array.get(i));
        }
        array.fill(3);
        for (int i = 0; i < array.getSize(); i++) {
            Assert.assertEquals(3, array.get(i));
        }
    }
}