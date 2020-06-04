package org.example.mysqlTest;

import java.sql.*;

public class PreparedStatementTest {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // 1. 加载 mysql 驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2. 创建数据库连接对象
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis01?characterEncoding=UTF-8&useUnicode=true&useJDBCComplianTimezoneShift=true&useLegacyDateTimeCode=false&serverTimezone=UTC","root","root");
            // 3. 定义 sql 语句
            String sql = "select * from user where id = ?";
            // 4. 创建 preparedment 对象，预编译 sql 语句  （也叫JDBC存储过程）
            preparedStatement = connection.prepareStatement(sql);
            // 5. 设置参数值
            preparedStatement.setInt(1,1);
            // 6. 执行，返回结果存于 ResultSet
            Boolean executeFlag = preparedStatement.execute();
            resultSet = preparedStatement.executeQuery();
            System.out.println("lcy---------执行结果："+executeFlag);

            // 7. 处理结果集
            while(resultSet.next()){
                System.out.println("用户Id：" + resultSet.getInt("id") + ",用户名称：" + resultSet.getString("username"));
            }

            // useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            // 8. 关闭资源（释放资源）
            try{
                if(resultSet!=null) {
                    resultSet.close();
                }
                if(preparedStatement!=null){
                    preparedStatement.close();
                }
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
