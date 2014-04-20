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

import com.manniwood.mmpt.test.beans.IntArrayBean;
import com.manniwood.mmpt.typehandlers.IntegerArrayWrapper;

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
        IntArrayBean t = new IntArrayBean();
        Integer[] intArray = new Integer[] { 1, 2, 3 };
        IntegerArrayWrapper wrapper = new IntegerArrayWrapper(intArray);
        t.setIntegerArrayWrapper(wrapper);
        t.setName(TEST_NAME);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("test.insertIntArray", t);
            session.commit(true);
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }

        IntArrayBean result;
        session = sqlSessionFactory.openSession();
        try {
            result = session.selectOne("test.selectIntArray", t);
            session.rollback(true);  // just a select; rollback
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }
        Assert.assertTrue(Arrays.equals(intArray, result.getIntegerArrayWrapper().getIntegerArray()), "blah");
        Assert.assertEquals(TEST_NAME, result.getName(), "Test name needs to be " + TEST_NAME);
    }
}