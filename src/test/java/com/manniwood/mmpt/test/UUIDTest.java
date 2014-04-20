package com.manniwood.mmpt.test;

import java.io.IOException;
import java.io.Reader;
import java.util.UUID;

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

import com.manniwood.mmpt.test.beans.UUIDBean;

@Test
public class UUIDTest {

    private static Logger log = LoggerFactory.getLogger(UUIDTest.class);

    private static final String TEST_NAME = "foo";

    private static final String TEST_UUID_STR = "37a82ee2-114c-4044-ba90-c073bf6d7830";

    private static final  String MYBATIS_CONF_FILE = "mybatis/config.xml";

    private  SqlSessionFactory sqlSessionFactory = null;


    public UUIDTest() {
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
            session.delete("test.truncateTest");
            session.commit(true);
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }

    }

    @Test
    public void testUUID() {
        UUIDBean t = new UUIDBean();
        t.setTestId(UUID.fromString(TEST_UUID_STR));
        t.setName(TEST_NAME);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("test.insertTest", t);
            session.commit(true);
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }

        UUIDBean result;
        session = sqlSessionFactory.openSession();
        try {
            result = session.selectOne("test.selectTest", t);
            session.rollback(true);  // just a select; rollback
        } finally {
            session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
        }
        Assert.assertEquals(UUID.fromString(TEST_UUID_STR).toString(), result.getTestId().toString(), "Test id needs to be " + TEST_UUID_STR);
        Assert.assertEquals(TEST_NAME, result.getName(), "Test name needs to be " + TEST_NAME);
    }
}