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

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.manniwood.mmpt.test.beans.IntegerArrayBean;

@Test
public class SmallIntArrayTest extends TypeTest {

    private static final String TABLE_CREATE_ID = "test.createSmallIntArrayTestTable";
    private static Logger log = LoggerFactory.getLogger(IntegerArrayTest.class);

    public SmallIntArrayTest() {
        super(TABLE_CREATE_ID);
    }

    @Test
    public void testIntegerArray() {
        String testName = "basic test";
        IntegerArrayBean t = new IntegerArrayBean();
        Integer[] intArray = new Integer[] { 1, 2, 3 };
        t.setIntegerArray(intArray);
        t.setName(testName);
        session.insert("test.insertSmallIntArray", t);
        session.commit(true);

        IntegerArrayBean result;
        result = session.selectOne("test.selectSmallIntArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getIntegerArray()), "Integer arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testIntegerArrayWithEmbeddedNulls() {
        String testName = "embedded null test";
        IntegerArrayBean t = new IntegerArrayBean();
        Integer[] intArray = new Integer[] { 1, null, 3 };
        t.setIntegerArray(intArray);
        t.setName(testName);
        session.insert("test.insertSmallIntArray", t);
        session.commit(true);

        IntegerArrayBean result;
        result = session.selectOne("test.selectSmallIntArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getIntegerArray()), "Integer arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testIntegerArrayWithTooLargeValue() {
        String testName = "too large value test";
        boolean exceptionWasCaught = false;
        IntegerArrayBean t = new IntegerArrayBean();
        Integer[] intArray = new Integer[] { 1, Integer.MAX_VALUE, 3 };
        t.setIntegerArray(intArray);
        t.setName(testName);
        try {
            session.insert("test.insertSmallIntArray", t);
        } catch (PersistenceException e) {
            log.info("caught expected exception: ", e);
            exceptionWasCaught = true;
        } finally {
            session.rollback(true);
        }

        Assert.assertTrue(exceptionWasCaught, "Integers larger than PostgreSQL smallint need to throw exeption.");
    }

    @Test
    public void testNullIntegerArray() {
        String testName = "null test";
        IntegerArrayBean t = new IntegerArrayBean();
        Integer[] intArray = null;
        t.setIntegerArray(intArray);
        t.setName(testName);
        session.insert("test.insertSmallIntArray", t);
        session.commit(true);

        IntegerArrayBean result;
        result = session.selectOne("test.selectNullSmallIntArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertNull(result.getIntegerArray(), "Integer array must be null.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }
}

