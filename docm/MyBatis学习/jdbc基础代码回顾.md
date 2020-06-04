# JDBC 基础代码回顾

参考： [https://blog.csdn.net/hellozpc/article/details/80878563][https://blog.csdn.net/hellozpc/article/details/80878563]

## 一、从 jdbc 谈起
    1. pom.xml 引入 mysql 依赖包
    2. 准备数据：mysql 建表、录入数据
    3. jdbc 基础代码回顾：
    4. jdbc 缺点分析  
    
    package org.example.mysqlTest;
    
    import java.sql.*;
    
    public class JdbcTest {
        public static void main(String[] args) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
    
            try {
                // 1. 加载 mysql 驱动
                Class.forName("com.mysql.jdbc.Driver");
                // 2. 创建数据库的连接对象   "jdbc:mysql://127.0.0.1:3306/liwei", "root", "root"
                connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis01?useUnicode=true" +
                        "&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true" +
                        "&useLegacyDatetimeCode=false&serverTimezone=UTC","root","root");
                // 3. 定义 sql 语句  （这里是测试）
                String sql = "select * from user where id = ?";
                // 4. 创建 statement 对象 （预编译的 sql语句的对象 ： SQL 语句被预编译并存储在 PreparedStatement 对象中。然后可以使用此对象多次高效地执行该语句）
                preparedStatement = connection.prepareStatement(sql);
                // 5. 设置参数 （第一个问号 ? 为1，第二个？为2 以此类推； 设置 IN 参数值的设置方法（setShort、setString 等等）必须指定与输入参数的已定义 SQL 类型兼容的类型）
                preparedStatement.setInt(1,1); // id 的 参数类型为 int
                // 6. 执行 ， 返回的结果集 存于 ResultSet
                resultSet = preparedStatement.executeQuery();
                // 7. 处理结果集
                while(resultSet.next()){
                    System.out.println("用户Id：" + resultSet.getInt("id") + ",用户名称：" + resultSet.getString("username"));
                }
    
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                // 异常，必要时 这里主动
                throwables.printStackTrace();
            }finally {
                // 8.释放资源
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
    
    /**
     *  1. com.mysql.jdbc.SocketFactory 查一查 是什么
     *
     * 6、总结jdbc开发出现的问题
     *
     * 频繁创建数据库连接对象、释放、容易造成系统资源浪费，影响系统性能。企业项目中可以使用连接池解决这个问题，但是使用Jdbc需要自己实现连接池。mybatis框架已经提供连接池。
     * sql语句定义、参数设置、结果集处理存在硬编码。企业项目中sql语句变化的可能性较大，一旦发生变化，需要修改java代码，系统需要重新编译，重新发布。不好维护。
     * 结果集处理存在重复代码，处理麻烦。如果可以映射成为java对象会比较方便
     * ————————————————
     * 版权声明：本文为CSDN博主「缓缓而行」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/wei_li_2015/article/details/80832806
     *
     * 2. 启动之后，报异常
     * Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'.
     *  The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
     * java.sql.SQLException: The server time zone value '�й���׼ʱ��' is unrecognized or represents more than one time zone.
     * You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
     * 解释：出现了mysql的无效连接属性异常：服务器时区值无法辨认或表示多个时区，如果你想使用时区支持，你必须通过服务器时区配置属性来配置服务器或JDBC驱动从而使用更具体的时区值。
     * 原因：因为MySQL版本过高，MySQL升级到8.0及以上，添加了许多新特性，安全性也得到提升。当然操作时也增加了些繁琐，需要考虑到的时区问题便是其中之一。
     * 解决：
     *      a.url 部分加上 serverTimezone=UTC
     *      b.url 部分加上 serverTimezone=Asia/Shanghai
     *      c.或者在url路径的问号后面加上:useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC,也可以解决问题.
     */


[https://blog.csdn.net/hellozpc/article/details/80878563]: https://blog.csdn.net/hellozpc/article/details/80878563