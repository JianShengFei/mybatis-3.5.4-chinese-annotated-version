package org.apache.ibatis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.autoconstructor.AutoConstructorMapper;
import org.apache.ibatis.autoconstructor.PrimitiveSubject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jianshengfei
 * @version 1.0.0
 * @ClassName SampleTest.java
 * @Description mybatis test
 * @createTime 2021年08月30日 11:55
 */
public class SampleTest {

    // 会话工厂
    private static SqlSessionFactory sqlSessionFactory;

    // 创建会话
    @BeforeAll
    static void setUp() throws Exception {
      // create a SqlSessionFactory
      try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/autoconstructor/mybatis-config.xml")) {
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
      }

      // populate in-memory database
      BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/autoconstructor/CreateDB.sql");
    }

    @Test
    public void test01() {

      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);
        PrimitiveSubject subject = mapper.getSubject(1);
        System.out.println(JSONUtil.toJsonStr(subject));
        assertNotNull(subject);
      }

    }

}
