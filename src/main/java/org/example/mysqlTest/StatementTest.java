package org.example.mysqlTest;

import java.sql.*;

public class StatementTest {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            // 1. 加载 mysql 驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2. 创建连接数据库的对象
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis01?characterEncoding=UTF-8&useUnicode=true&useJDBCComplianTimezoneShift=true&useLegacyDateTimeCode=false&serverTimezone=UTC","root","root");
            // 这里设置为不自动提交 （）
            Boolean autoCommit = connection.getAutoCommit();// true
            // 如果这里 不加上  设置为 false ; 那么，异常之前的语句都会执行成功，并更新在数据库中（如resultSetFlag）。  只有 这里设置为 false,后主动调用，会触发回滚，都不会保存在数据库。
            connection.setAutoCommit(false);
            // 3. 定义 sql 语句  insert into user values('32','提莫','2020-05-24','1','');     delete from user where id =32
            String sql = "select * from user where id = 32 or id = 38";
            // 4. 创建 Statement 对象
            statement = connection.createStatement();

            // 5. 执行 sql 语句
            Boolean resultSetFlag = statement.execute(sql);
            System.out.println("lcy---------执行结果："+resultSetFlag);// false, 因为没有返回结果集（insert、delete ; 如果是 select 则可能有返回结果集。）

//            sql = "insert into user values('38','提莫2','2020-05-25','1','')";
//            // 这里会报错 : java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '32' for key 'PRIMARY'
//            Boolean resultSetFlag2 = statement.execute(sql);  // （又执行一次） 删除语句可以 ；  插入语句 不可以，因为  主键 id 唯一；
//            System.out.println("lcy---------执行结果2："+resultSetFlag2);

            // 6. 提交 （如果将 setAutoCommit 设为 false ;这里不主动 commit ，数据库里数据就不会被删除。）
            connection.commit();  //  Can't call commit when autocommit=true  （需要在前面设置 为 不自动提交；）
            System.out.println("lcy---------connection.getAutoCommit()："+connection.getAutoCommit()+",autoCommit:"+autoCommit);// false, true
            //connection.setAutoCommit(autoCommit);  // 两个 commit 都可以； 都可以 回滚，也可以保存在数据库。   （）

            // 7.处理结果集
            //resultSet = statement.executeQuery(sql);
            resultSet = statement.getResultSet();

            if(resultSet!=null){
                // 这里 打印 用掉了   next 之后，后面的 while 就 会 少进一次。
                //System.out.println("lcy---------resultSet.next()："+resultSet.next());// true
                while (resultSet.next()){
                    //System.out.println("用户Id：" + resultSet.getInt("id") + ",用户名称：" + resultSet.getString("username"));
                    System.out.println("用户Id：" + resultSet.getInt(1) + ",用户名称：" + resultSet.getString(2));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("lcy---------异常1，执行回滚");
            // commit 可以自动回滚；无需手动
//            try {
//                //  报错 ： Can't call rollback when autocommit=true  （）
//                connection.rollback(); // 事务回滚  （上面，又加了）
//            } catch (SQLException throwables) {
//                System.out.println("lcy---------异常2");
//                throwables.printStackTrace();
//            }
            e.printStackTrace();
        }finally{
            try{
                if(connection!=null){
                    connection.close();
                }
                if(statement!=null) statement.close();
                if(resultSet!=null) resultSet.close();

            } catch (SQLException throwables) {
                System.out.println("lcy---------异常3");
                throwables.printStackTrace();
            }

        }
    }
}
