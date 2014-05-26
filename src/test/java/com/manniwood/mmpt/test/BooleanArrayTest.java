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

import com.manniwood.mmpt.test.beans.BooleanArrayBean;

@Test
public class BooleanArrayTest extends TypeTest {

    private static final String TABLE_CREATE_ID = "test.createBooleanArrayTestTable";

    public BooleanArrayTest() {
        super(TABLE_CREATE_ID);
    }

    @Test
    public void testBooleanArray() {
        String testName = "basic test";
        BooleanArrayBean t = new BooleanArrayBean();
        Boolean[] intArray = new Boolean[] { true, false, true };
        t.setBooleanArray(intArray);
        t.setName(testName);
        session.insert("test.insertBooleanArray", t);
        session.commit(true);

        BooleanArrayBean result;
        result = session.selectOne("test.selectBooleanArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getBooleanArray()), "Boolean arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testBooleanArrayWithEmbeddedNulls() {
        String testName = "embedded null test";
        BooleanArrayBean t = new BooleanArrayBean();
        Boolean[] intArray = new Boolean[] { true, null, false };
        t.setBooleanArray(intArray);
        t.setName(testName);
        session.insert("test.insertBooleanArray", t);
        session.commit(true);

        BooleanArrayBean result;
        result = session.selectOne("test.selectBooleanArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getBooleanArray()), "Boolean arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testNullBooleanArray() {
        String testName = "null test";
        BooleanArrayBean t = new BooleanArrayBean();
        Boolean[] intArray = null;
        t.setBooleanArray(intArray);
        t.setName(testName);
        session.insert("test.insertBooleanArray", t);
        session.commit(true);

        BooleanArrayBean result;
        result = session.selectOne("test.selectNullBooleanArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertNull(result.getBooleanArray(), "Boolean array must be null.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }
}

