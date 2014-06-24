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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.manniwood.mmpt.test.beans.JSONBean;

@Test
public class JSONTest extends TypeTest {

    private static final String TABLE_CREATE_ID = "test.createJSONTestTable";
    private static final String TEST_JSON = "{ \"foo\": \"bar\", \"a\": \"b\" }";

    public JSONTest() {
        super(TABLE_CREATE_ID);
    }

    @Test
    public void testJSON() {
        String testName = "regular test";
        JSONBean t = new JSONBean();
        t.setTestId(TEST_JSON);
        t.setName(testName);
        session.insert("test.insertJSON", t);
        session.commit(true);

        JSONBean result;
        result = session.selectOne("test.selectJSON", t);
        session.rollback(true);  // just a select; rollback
        Assert.assertEquals(TEST_JSON, result.getTestId().toString(), "Test id needs to be " + TEST_JSON);
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testNullJSON() {
        String testName = "null test";
        JSONBean t = new JSONBean();
        t.setTestId(null);
        t.setName(testName);
        session.insert("test.insertJSON", t);
        session.commit(true);

        JSONBean result;
        result = session.selectOne("test.selectNullJSON", t);
        session.rollback(true);  // just a select; rollback
        Assert.assertNull(result.getTestId(), "JSON needs to be null.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }
}