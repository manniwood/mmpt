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
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.manniwood.mmpt.test.beans.UUIDArrayBean;

@Test
public class UUIDArrayTest extends TypeTest {

    private static final String TABLE_CREATE_ID = "test.createUUIDArrayTestTable";

    public UUIDArrayTest() {
        super(TABLE_CREATE_ID);
    }

    @Test
    public void testUUIDArray() {
        String testName = "basic test";
        UUIDArrayBean t = new UUIDArrayBean();
        UUID[] uuidArray = new UUID[] {
                UUID.fromString("00000000-0000-4000-8000-000000000001"),
                UUID.fromString("00000000-0000-4000-8000-000000000002"),
                UUID.fromString("00000000-0000-4000-8000-000000000003") };
        t.setUUIDArray(uuidArray);
        t.setName(testName);
        session.insert("test.insertUUIDArray", t);
        session.commit(true);

        UUIDArrayBean result;
        result = session.selectOne("test.selectUUIDArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(uuidArray, result.getUUIDArray()), "UUID arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testUUIDArrayWithEmbeddedNulls() {
        String testName = "embedded null test";
        UUIDArrayBean t = new UUIDArrayBean();
        UUID[] uuidArray = new UUID[] {
                UUID.fromString("00000000-0000-4000-8000-000000000001"),
                null,
                UUID.fromString("00000000-0000-4000-8000-000000000003") };
        t.setUUIDArray(uuidArray);
        t.setName(testName);
        session.insert("test.insertUUIDArray", t);
        session.commit(true);

        UUIDArrayBean result;
        result = session.selectOne("test.selectUUIDArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(uuidArray, result.getUUIDArray()), "UUID arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testNullUUIDArray() {
        String testName = "null test";
        UUIDArrayBean t = new UUIDArrayBean();
        UUID[] uuidArray = null;
        t.setUUIDArray(uuidArray);
        t.setName(testName);
        session.insert("test.insertUUIDArray", t);
        session.commit(true);

        UUIDArrayBean result;
        result = session.selectOne("test.selectNullUUIDArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertNull(result.getUUIDArray(), "UUID array must be null.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }
}

