/*
The MIT License (MIT)

Copyright (c) 2014 Manni Wood

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.manniwood.mmpt.test;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.manniwood.mmpt.test.beans.LongArrayBean;

@Test
public class BigIntArrayTest extends TypeTest {

    private static final String TABLE_CREATE_ID = "test.createBigIntArrayTestTable";

    public BigIntArrayTest() {
        super(TABLE_CREATE_ID);
    }

    @Test
    public void testLongArray() {
        String testName = "basic test";
        LongArrayBean t = new LongArrayBean();
        Long[] intArray = new Long[] { 1L, 2L, 3L };
        t.setLongArray(intArray);
        t.setName(testName);
        session.insert("test.insertBigIntArray", t);
        session.commit(true);

        LongArrayBean result;
        result = session.selectOne("test.selectBigIntArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getLongArray()), "Long arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testLongArrayWithEmbeddedNulls() {
        String testName = "embedded null test";
        LongArrayBean t = new LongArrayBean();
        Long[] intArray = new Long[] { 1L, null, 3L };
        t.setLongArray(intArray);
        t.setName(testName);
        session.insert("test.insertBigIntArray", t);
        session.commit(true);

        LongArrayBean result;
        result = session.selectOne("test.selectBigIntArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getLongArray()), "Long arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testNullLongArray() {
        String testName = "null test";
        LongArrayBean t = new LongArrayBean();
        Long[] intArray = null;
        t.setLongArray(intArray);
        t.setName(testName);
        session.insert("test.insertBigIntArray", t);
        session.commit(true);

        LongArrayBean result;
        result = session.selectOne("test.selectNullBigIntArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertNull(result.getLongArray(), "Long array must be null.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }
}

