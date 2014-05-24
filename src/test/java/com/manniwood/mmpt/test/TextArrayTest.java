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

import com.manniwood.mmpt.test.beans.StringArrayBean;

@Test
public class TextArrayTest extends TypeTest {

    private static final String TABLE_CREATE_ID = "test.createTextArrayTestTable";

    public TextArrayTest() {
        super(TABLE_CREATE_ID);
    }

    @Test
    public void testStringArray() {
        String testName = "basic test";
        StringArrayBean t = new StringArrayBean();
        String[] intArray = new String[] { "one", "two", "three" };
        t.setStringArray(intArray);
        t.setName(testName);
        session.insert("test.insertTextArray", t);
        session.commit(true);

        StringArrayBean result;
        result = session.selectOne("test.selectTextArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getStringArray()), "String arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testStringArrayWithEmbeddedNulls() {
        String testName = "embedded null test";
        StringArrayBean t = new StringArrayBean();
        String[] intArray = new String[] { "one", null, "three" };
        t.setStringArray(intArray);
        t.setName(testName);
        session.insert("test.insertTextArray", t);
        session.commit(true);

        StringArrayBean result;
        result = session.selectOne("test.selectTextArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertTrue(Arrays.equals(intArray, result.getStringArray()), "String arrays need to match.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }

    @Test
    public void testNullStringArray() {
        String testName = "null test";
        StringArrayBean t = new StringArrayBean();
        String[] intArray = null;
        t.setStringArray(intArray);
        t.setName(testName);
        session.insert("test.insertTextArray", t);
        session.commit(true);

        StringArrayBean result;
        result = session.selectOne("test.selectNullTextArray", t);
        session.rollback(true);  // just a select; rollback

        Assert.assertNull(result.getStringArray(), "String array must be null.");
        Assert.assertEquals(testName, result.getName(), "Test name needs to be " + testName);
    }
}

