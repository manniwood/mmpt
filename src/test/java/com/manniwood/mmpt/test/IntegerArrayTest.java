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

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.manniwood.mmpt.test.beans.IntegerArrayBean;

@Test
public class IntegerArrayTest {

    private static Logger log = LoggerFactory.getLogger(IntegerArrayTest.class);

    private static final String TEST_NAME = "foo";

    private static final  String MYBATIS_CONF_FILE = "mybatis/config.xml";

    private  SqlSessionFactory sqlSessionFactory = null;


    public IntegerArrayTest() {
        // empty constructor
    }

    @BeforeClass
    protected void initMybatis() {
        log.info("********* initializing sqlSessionFactory with conf file {} ******", MYBATIS_CONF_FILE);
        Reader myBatisConfReader = null;
        try {
            myBatisConfReader = Resources.getResourceAsReader(MYBATIS_CONF_FILE);
        } catch (IOException e) {
            throw new RuntimeException("problem trying to read mybatis config file: ", e);
        }
        if (myBatisConfReader == null) {
            throw new RuntimeException("mybatis conf reader is null");
        }

        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(myBatisConfReader);
        } catch (PersistenceException e) {
            throw new RuntimeException("problem trying to set up database connection: ", e);
        }
        if (sqlSessionFactory == null) {
            throw new RuntimeException("sqlSessionFactory is null");
        }

        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("test.truncateIntArrayTest");
            session.commit(true);
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }

    }

    @Test
    public void testIntArray() {
        IntegerArrayBean t = new IntegerArrayBean();
        Integer[] intArray = new Integer[] { 1, 2, 3 };
        t.setIntegerArray(intArray);
        t.setName(TEST_NAME);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("test.insertIntArray", t);
            session.commit(true);
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }

        IntegerArrayBean result;
        session = sqlSessionFactory.openSession();
        try {
            result = session.selectOne("test.selectIntArray", t);
            session.rollback(true);  // just a select; rollback
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }
        Assert.assertTrue(Arrays.equals(intArray, result.getIntegerArray()), "blah");
        Assert.assertEquals(TEST_NAME, result.getName(), "Test name needs to be " + TEST_NAME);
    }
}