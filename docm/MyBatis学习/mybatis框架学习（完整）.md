# MyBatis 框架学习

参考： [https://blog.csdn.net/wei_li_2015/article/details/80832806][https://blog.csdn.net/wei_li_2015/article/details/80832806]

## 一、从 jdbc 谈起
    参考：https://blog.csdn.net/hellozpc/article/details/80878563
    1. pom.xml 引入 mysql 依赖包
    2. 准备数据：mysql 建表、录入数据
    3. jdbc 基础代码回顾：
    4. jdbc 缺点分析   
        
## 二、MyBatis 介绍
   myBatis 是 Apache 软件基金会下的一个开源项目，前身是 Ibatis 框架。2010年这个项目由apahce软件基金会迁移到 goole code 下，改名 mybatis。
2013年11月又迁移到了 gitHub(https://github.com/mybatis/mybatis-3/releases)。

   myBatis 是一个持久层的框架，是对 JDBC 操作数据库的封装，使开发者只关注业务本身，不需要花费精力去处理加载驱动、创建数据库连接对象、创建 
 statement 对象、参数设置、结果集处理等一系列繁杂的过程代码。
 
   myBatis 通过XML或注解进行配置，将 java 与 sql 语句中的参数自动映射生成最终执行的sql语句，并将sql语句执行结果自动映射成java对象，
 返回给业务层（service）应用。

## 三、MyBatis的入门程序
   需求： 实现用户（user）的增、删、改、查。
   
### 3.1 配置 pom.xml
    加载依赖： mybatis-3.5.4.jar 、mysql-8.0.20.jar、junit-4.12.jar
    加载log4j日志包：slf4j-api-1.7.29 、log4j-1.7.27.jar 、slf4j-log4j12-1.7.21.jar 
    
    说明： 因为 写 Heepservlet 时，引入了 tomcat 依赖包，
         <!--2020-5-25 测试 sessionTest 包中的 SessionInfoServlet 继承 HttpServlet 
           依赖9.0.29时，git提示该版本存在严重的安全漏洞，建议升级到9.0.35或更高版本
           https://github.com/fieldNight41/mybatis01/network/alert/pom.xml/org.apache.tomcat.embed:tomcat-embed-core/open -->
    <dependency>
      <groupId>org.apache.tomcat.embed</groupId>
      <artifactId>tomcat-embed-core</artifactId>
      <version>9.0.35</version>
    </dependency>
    
### 3.2 sqlMapConfig.xml 配置文件，是 mybatis 框架的核心配置文件
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>
        <!--运行环境-->
        <!--default 属性：指定使用哪一个运行环境-->
        <environments default="development">
            <!--id属性：唯一标识 一个运行环境-->
            <environment id="development">
                <!--事务管理配置，type = "JDBC" :mybatis 框架默认使用 jdbc 事务-->
                <transactionManager type="JDBC"></transactionManager>
                <!--数据源配置：type = "POOLED" :mybatis 框架提供的连接池-->
                <dataSource type="POOLED">
                    <property name="driver" value="com.mysql.jdbc.Driver"/>
                    <property name="url" value="jdbc:mysql://127.0.0.1:3306/mybatis01?characterEncoding=UTF-8
                                           &amp;useUnicode=true&amp;useJDBCComplianTimezoneShift=true
                                           &amp;useLegacyDateTimeCode=false&amp;serverTimezone=UTC"/>
                    <property name="username" value="root"/>
                    <property name="password" value="root"/>
                </dataSource>
            </environment>
        </environments>
    
        <!--加载映射文件-->
        <mappers>
            <mapper resource="mybatis/UserMapper.xml"/>
        </mappers>
    </configuration>
    
### 3.3 log4j.properties 日志文件  （未使用这里内容）
    # Global logging configuration
    log4j.rootLogger=DEBUG, stdout
     
    # Console output...
    log4j.appender.stdout=org.apache.log4j.ConsoleAppender
    log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
    log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n

### 3.4 实现类 User.java 文件
     package org.example.myBatisLearn.domain;
     
     import java.util.Date;
     
     public class User {
         private Integer id; // 主键id
         private String username; // 用户名称
         private Date birthday; // 生日
         private String sex; // 性别
         private String address; // 地址
     
         public Integer getId() {
             return id;
         }
         public void setId(Integer id) {
             this.id = id;
         }
         public String getUsername() {
             return username;
         }
         public void setUsername(String username) {
             this.username = username;
         }
         public Date getBirthday() {
             return birthday;
         }
         public void setBirthday(Date birthday) {
             this.birthday = birthday;
         }
         public String getSex() {
             return sex;
         }
         public void setSex(String sex) {
             this.sex = sex;
         }
         public String getAddress() {
             return address;
         }
         public void setAddress(String address) {
             this.address = address;
         }
         @Override
         public String toString() {
             return "User [id=" + id + ", username=" + username + ", birthday=" + birthday + ", sex=" + sex + ", address="
                     + address + "]";
         }
     } 

### 3.5 UserMapper.xml 文件 （映射文件）
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="mybatis">
        <!--新增用户-->
        <insert id="addUser" parameterType="org.example.myBatisLearn.domain.User">
            insert into user (id,username,birthday,sex,address)
            values (#{id},#{username},#{birthday},#{sex},#{address})
        </insert>
    
        <!--根据用户id删除用户-->
        <delete id="deleteUser" parameterType="int">
            delete from user where id = #{id}
        </delete>
    
        <!--根据用户id更新用户-->
        <update id="updateUserById" parameterType="org.example.myBatisLearn.domain.User">
            update user set username=#{username},sex=#{sex} where id = #{id}
        </update>
    
        <!--根据用户id查询-->
        <select id="queryUserById" parameterType="int" resultType="org.example.myBatisLearn.domain.User">
            select id,username,birthday,sex,address from user where id = #{id}
        </select>
    
        <!--方式一：根据用户名称模糊查询用户-->
        <select id="queryUserByName1" parameterType="string" resultType="org.example.myBatisLearn.domain.User">
            select id,username,birthday,sex,address from `user` where username like #{username}
        </select>
    
        <!--方式二：根据用户名称模糊查询用户-->
        <select id="queryUserByName2" parameterType="string" resultType="org.example.myBatisLearn.domain.User">
            select id,username,birthday,sex,address from `user` where username like  '%${value}%'
        </select>
    
    </mapper>

### 3.6 MybatisTest.java 测试类
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
    
    public class MybatisTest {
    
        private static Logger LOGGER = LoggerFactory.getLogger(MybatisTest.class);
    
        public SqlSessionFactory getSqlSessionFactory() throws IOException {
            // 1.加载核心配置文件
            InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
            // 2.读取配置文件的内容
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = builder.build(inputStream);
    
            return sqlSessionFactory;
        }
    
        @Test
        public void addUser() throws IOException {
            // 3. 使用 sqlSessionFactory 对象，创建 SqlSession 对象，开启自动提交事务
            SqlSession sqlSession = this.getSqlSessionFactory().openSession(true);
            // 4. 调用方法执行
            User user = new User();
            user.setId(4);
            user.setUsername("林诗音");
            user.setBirthday(new Date());
            user.setSex("女");
            user.setAddress("来自大明朝");
            sqlSession.insert("mybatis.addUser",user);
            // 事务提交
            // sqlSession.commit();
            // 5. 释放资源
            sqlSession.close();
        }
    
        @Test
        public void deleteUser() throws IOException {
            // 创建 SqlSession 对象
            SqlSession sqlSession = this.getSqlSessionFactory().openSession(true);
            // 调用方法执行
            sqlSession.delete("mybatis.deleteUser",26);
            // 释放资源
            sqlSession.close();
        }
    
        @Test
        public void updateUser() throws IOException {
            // 创建 SqlSession 对象
            SqlSession sqlSession = this.getSqlSessionFactory().openSession(true);
            // 调用方法执行
            User user = new User();
            user.setId(38);
            user.setUsername("林诗音和小李飞刀");
            user.setSex("1");
            sqlSession.update("mybatis.updateUserById",user);
            // 释放资源
            sqlSession.close();
        }
    
        @Test
        public void queryUserById() throws IOException {
            // 创建 SqlSession 对象
            SqlSession sqlSession = this.getSqlSessionFactory().openSession();
            // 调用方法执行
            Object user = sqlSession.selectOne("mybatis.queryUserById",4);
            LOGGER.info("lcy--------根据用户id-32-查询用户："+user);
            // 释放资源
            sqlSession.close();
        }
    
        @Test
        public void queryUserByName1() throws IOException {
            // 创建 SqlSession 对象
            SqlSession sqlSession = this.getSqlSessionFactory().openSession();
            // 调用方法执行
            List<Object> userList = sqlSession.selectList("mybatis.queryUserByName1","%小明%");
            for (Object object : userList){
                LOGGER.info("lcy-------根据用户名称\"%小明%\"模糊查询："+object);
            }
            // 释放资源
            sqlSession.close();
        }
    
        @Test
        public void queryUserByName2() throws IOException {
            // 创建 SqlSession 对象
            SqlSession sqlSession = this.getSqlSessionFactory().openSession();
            // 调用执行
            List<Object> userList = sqlSession.selectList("mybatis.queryUserByName2","小明");
            for(Object object : userList){
                LOGGER.info("lcy-------根据用户名称\"小明\"模糊查询："+object);
            }
            // 释放资源
            sqlSession.close();
        }
    
    }

    

方式一与方式二具体看上面的代码。

在方式一与方式二中，我们使用到了#{}与${}两种方式，name这两种方式用什么区别呢？

## 四、占位符 #{} 与字符串拼接符 ${} 区别
    1. 占位符#{}，相当于jdbc中的问号？，当参数类型传递的是java简单类型的时候，花括号中的内容可以是任意字符串。
    2. 字符串拼接符${}，当参数传递的是java简单类型的时候，花括号中的内容只能是：value
    3. sql语句中使用字符串拼接，可能引起sql注入的问题。但是mybatis框架中字符串拼接符可以放心使用。
        原因是mybatis是后端dao层开发，参数在前端表现层（action）和业务层（service）已经处理好。

## 五、mybatis框架的原理
    mybatis框架的原理

## 六、别名配置与映射文件加载方式
    1. 别名 有两种方式
        <typeAliases>
            <!--自定义别名配置-->
            <typeAlias type = "org.example.myBatisLearn.domain.User" alias = "user" />
            <!--包扫描方式-->
            <package name = "org.example.myBatisLearn.domain.User" />
        </typeAliases>
        
     2. 映射文件加载：两种方法
        <!--加载映射文件-->   
        <mapper resource = "mybatis/UserMapper.xml" />
        <!--包扫描方式-->
        <package name = "org.example.myBatisLearn.domain" />
**注意：包扫描需要将映射文件放在相应的包下面**

## 七、mybatis与hibernate的比较
     相同点：
        都是对 jdbc 的封装，都是持久层框架，都用于 dao 层的开发。
     不同点：
        1. hibernate 对 sql 语句做了封装，提供了 HQL 语句操作数据库，数据库无关性 支持好，在项目需要支持多种数据库的情况下，代码开发量较少，
            sql语句优化困难。
           mybatis 是直接使用 sql 语句操作数据库，不支持数据库无关性，在项目需要支持多种数据库的情况下，代码开发量较多，sql语句优化容易。
        2. hibernate 配置 java 对象与数据库表的对应关系，多表关联配置复杂。
            mybatis 是配置 java 对象与 sql 语句的对应关系，多表关联关系配置简单。 
        3. hibernate 是一个重量级的框架，学习使用门槛高，适合于需求相对稳定，中小型的项目，比如：办公自动化系统（OA）;
            mybatis 是一个轻量级的框架，学习使用门槛低，适合于需求变化频繁，大型的项目，比如：互联网项目。目前企业中mybatis框架使用更多。     


[https://blog.csdn.net/wei_li_2015/article/details/80832806]: https://blog.csdn.net/wei_li_2015/article/details/80832806