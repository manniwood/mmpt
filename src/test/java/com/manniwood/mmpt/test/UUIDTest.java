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

import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.manniwood.mmpt.test.beans.UUIDBean;

@Test
public class UUIDTest extends TypeTest {

    private static final String TEST_NAME = "foo";
    private static final String TABLE_CREATE_ID = "test.createUUIDTestTable";
    private static final String TEST_UUID_STR = "37a82ee2-114c-4044-ba90-c073bf6d7830";

    public UUIDTest() {
        super(TABLE_CREATE_ID);
    }

    @Test
    public void testUUID() {
        UUIDBean t = new UUIDBean();
        t.setTestId(UUID.fromString(TEST_UUID_STR));
        t.setName(TEST_NAME);
        session.insert("test.insertUUIDTest", t);
        session.commit(true);

        UUIDBean result;
        result = session.selectOne("test.selectUUIDTest", t);
        session.rollback(true);  // just a select; rollback
        Assert.assertEquals(UUID.fromString(TEST_UUID_STR).toString(), result.getTestId().toString(), "Test id needs to be " + TEST_UUID_STR);
        Assert.assertEquals(TEST_NAME, result.getName(), "Test name needs to be " + TEST_NAME);
    }
}