package org.example.mysqlTest;

import java.sql.*;

/**
 *  参考： https://blog.csdn.net/strivenoend/article/details/77968754
 *  结果未出： 后面再做
 */
public class TestSqlInject {
    public static void main(String[] args) {
        // 后面一行是 新版本的 mysql 有时区 问题
        String connStr = "jdbc:mysql://127.0.0.1:3306/mybatis01?characterEncoding=UTF-8" +
                "&useUnicode=true&useJDBCComplianTimezoneShift=true&useLegacyDateTimeCode=false&serverTimezone=UTC";

        String sql = "select * from user where id = ? and userName = ?";

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(connStr,"root","root");
            System.out.println("数据库连接="+connection);
            // Statement 无法防止 SQL 注入
//            Statement statement = connection.createStatement();
            // PreparedStatement 可以有效防止 SQL 注入，所以生产环境上一定要使用 PreparedStatement , 而不能使用 Statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            // 模拟用户输入正常的值
            //preparedStatement.setString(2,"王五");
            // 测试 sql 注入 （模拟用户输入非法的值）
            preparedStatement.setString(2,"222' or '8'='8");
            // 把最终执行的 sql 语句 打印出来
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("最终执行sql = :"+preparedStatement.toString());
            System.out.println("=============================");
            int col = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()){
                for(int i = 1 ;i<=col; i++){
                    System.out.println(resultSet.getString(i)+"\t");
                    if((i == 2) && (resultSet.getString(i).length()<8)){
                        System.out.println("\t");
                    }
                }
                System.out.println("");
            }
            System.out.println("==========================");
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

/**
 * 正常时 打印结果：
 *
 数据库连接=com.mysql.cj.jdbc.ConnectionImpl@42d3bd8b
 最终执行sql = :com.mysql.cj.jdbc.ClientPreparedStatement: select * from user where id = 1 and userName = '王五'
 =============================
 1
 王五

 null
 2
 null

 ==========================

* 非法输入时，打印结果：
 *
 数据库连接=com.mysql.cj.jdbc.ConnectionImpl@42d3bd8b
 最终执行sql = :com.mysql.cj.jdbc.ClientPreparedStatement: select * from user where id = 1 and userName = '王五'' or ''8''=''8'
 =============================
 ==========================

 2020-5-25 与参考示例 不同，后面再做；
 应该是 ： ... = '222\' OR \'8\'=\'8'

 prepareStatement对象防止sql注入的方式是：把用户非法输入的单引号用\反斜杠做了转义，从而达到了防止sql注入的目的 。


 mysql-connector-java-5.1.31.jar
 你可以仔细研究下PreparedStatement对象是如何防止sql注入的，我自己把最终执行的sql语句打印出来了，看到打印出来的sql语句就明白了，原来是mysql数据库产商，在实现PreparedStatement接口的实现类中的setString(int parameterIndex, String x)函数中做了一些处理，把单引号做了转义(只要用户输入的字符串中有单引号，那mysql数据库产商的setString()这个函数，就会把单引号做转义)

 大家有兴趣可以去网上，下载一份mysql数据库的驱动程序的源代码，看看mysql数据库产商的驱动程序的源代码，去源代码中找到setString(int parameterIndex, String x)函数，看看该函数中是怎么写的，我没有下载mysql数据库产商的驱动程序的源代码，而是把mysql数据库的驱动程序jar包解压了，找到了PreparedStatement.class文件，利用反编译工具，反编译了一下，如下：
 ————————————————
 版权声明：本文为CSDN博主「冷囧囧」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 原文链接：https://blog.csdn.net/czh500/article/details/88202971

 */