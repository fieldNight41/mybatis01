package org.example.myBatisLearn.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.myBatisLearn.domain.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class MybatisTest2 {

    private static Logger LOGGER = LoggerFactory.getLogger(MybatisTest2.class);

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        // 加载核心配置文件
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        // 读取配置文件内容
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = builder.build(inputStream);
        return sqlSessionFactory;
    }

    @Test
    public void addUser() throws IOException {
        // 创建 sqlsession 对象，开启自动提交事务
        SqlSession sqlSession = this.getSqlSessionFactory().openSession(true);
        // . 调用方法执行
        User user = new User();
        user.setId(4);
        user.setUsername("林诗音");
        user.setBirthday(new Date());
        user.setSex("女");
        user.setAddress("来自大明朝");
        sqlSession.insert("mybatis.addUser",user);
        // 释放资源
        sqlSession.close();
    }

    @Test
    public void deleteUser() throws IOException {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession(true);
        sqlSession.delete("mybatis.deleteUser",4);
        sqlSession.close();
    }

    @Test
    public void updateUser() throws IOException {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession(true);
        // 调用方法执行
        User user = new User();
        user.setId(38);
        user.setUsername("林诗音和小李飞刀");
        user.setSex("1");
        sqlSession.update("mybatis.updateUserById",user);
        sqlSession.close();
    }

    @Test
    public void queryUserById() throws IOException {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        Object user = sqlSession.selectOne("mybatis.queryUserById",4);
        LOGGER.info("lcy--------根据用户id-32-查询用户："+user);
        // 释放资源
        sqlSession.close();
    }

    @Test
    public void queryUserByName1() throws IOException {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        List<Object> userList = sqlSession.selectList("mybatis.queryUserByName1","%小明%"); // `#{username}`
        for (Object object : userList){
            LOGGER.info("lcy-------根据用户名称\"%小明%\"模糊查询："+object);
        }
        // 释放资源
        sqlSession.close();
    }

    @Test
    public void queryUserByName2() throws IOException {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession();
        List<Object> userList = sqlSession.selectList("mybatis.queryUserByName2","小明"); // `%${value}%`
        for(Object object : userList){
            LOGGER.info("lcy-------根据用户名称\"小明\"模糊查询："+object);
        }
        // 释放资源
        sqlSession.close();
    }
}
