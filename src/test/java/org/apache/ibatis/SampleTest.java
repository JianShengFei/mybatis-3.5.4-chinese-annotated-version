package org.apache.ibatis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.autoconstructor.AutoConstructorMapper;
import org.apache.ibatis.autoconstructor.PrimitiveSubject;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.submitted.ognlstatic.StaticClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.util.Set;
import java.util.function.Function;

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

    /**
     * mybatis 内置包下类的扫描工具
     */
    @Test
    public void test02() {
      ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
      resolverUtil.find(new ResolverUtil.IsA(Object.class), "org.apache.ibatis.mapping");
      Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
      for (Class<?> mapperClass : mapperSet) {
        System.out.println(mapperClass);
      }
    }

    /**
     * java8 新特性的用法例子
     */
    @Test
    public void test03() {
      int compute1 = this.compute(5, value -> value * value); // 25
      System.out.println(compute1);

      int compute2 = this.compute(5, value -> value + value); // 10
      System.out.println(compute2);

      int compute3 = this.compute(5, value -> value - 2); // 3
      System.out.println(compute3);

      int compute4 = this.compute(5, Integer::valueOf); // 5
      System.out.println(compute4);
    }

    private int compute(int a, Function<Integer, Integer> function) {
      return function.apply(a);
    }



}
