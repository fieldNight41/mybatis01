Statement 和 PreparedStatement之间的关系和区别

为主: [https://blog.csdn.net/suwu150/article/details/52745055][https://blog.csdn.net/suwu150/article/details/52745055]

参考: [https://blog.csdn.net/strivenoend/article/details/77968754][https://blog.csdn.net/strivenoend/article/details/77968754]

    关系：PreparedStatement 继承自 Statement ,都是接口；
    区别：PreparedStatement 可以使用占位符，是预编译的，批处理比 Statement 效率高； 可以防止 SQL 注入。
         （参考）使用 Statement 对象。在对数据库只执行一次性存取的时侯，用 Statement 对象进行处理。PreparedStatement 对象的开销比Statement大，对于一次性操作并不会带来额外的好处。
         （参考）statement每次执行sql语句，相关数据库都要执行sql语句的编译，preparedstatement是预编译得,   preparedstatement支持批处理
         
详解：

    1.PreparedStatement 表示预编译的 SQL 语句的对象。
        接口： public interface PreparedStatement extends Statement 之间的继承关系。
        SQL 语句被预编译并存储在 PreparedStatement 对象中。然后可以使用此对象多次高效地执行该语句。
     注：用于设置 IN 参数值的设置方法（setShort、setString 等等）必须指定与其输入的参数的已定义 SQL 类型兼容的类型。
        例如，如果 IN 参数具有 SQL 类型 Integer , 那么应该使用 setInt 方法，问号的位置也是应该注意的，因为第一个问号的位置为1，
        第二个问号的位置为2，以此类推。
        如果需要任意参数类型转换，使用 setObject 方法时应该将目标 SQL 类型作为其参数。
        在以下设置参数的示例中，conn 表示一个活动链接：
            PreparedStatement pstmt = con.preparedStatement("update employees salary = ? where id = ?");
            pstmt.setBigDecimal(1,1533.00);
            pstmt.setInt(2,1102);
            pstmt.execute(); // 注意 提交时这里不能再有 sql 语句，不同于 Statement
演示代码：
***

    2. Statement 用于执行静态 SQL 语句并返回它所生成结果的对象。
        接口：Public interface Statement extends Wrapper
        在默认情况下，同一时间每个 Statement 对象只能打开一个 ResultSet 对象。因此，如果读取一个 ResultSet 对象与另一个交叉，
        则这两个对象必须是由不同的 Statement 生成的。 如果存在某个语句的打开的当前 ResultSet 对象，则 Statement 接口中的所有
        执行方法都会隐式的关闭它。
            如下操作：创建 statement 对象
                Statement statement = connnection.createStatement();
                String sql = "insert into user values('32','提莫','2020-05-24','1','')";
                statement.execute(sql); // 这里提交时应该有 sql 语句，不同于 PreparedStatement
演示代码：                

[https://blog.csdn.net/strivenoend/article/details/77968754]: https://blog.csdn.net/strivenoend/article/details/77968754

[https://blog.csdn.net/suwu150/article/details/52745055]: https://blog.csdn.net/suwu150/article/details/52745055

----

（参考）
    
    Code Fragment 1:
    
    String updateString = "UPDATE COFFEES SET SALES = 75 " + "WHERE COF_NAME LIKE ′Colombian′";
    stmt.executeUpdate(updateString);
    
    Code Fragment 2:
    
    PreparedStatement updateSales = con.prepareStatement("UPDATE COFFEES SET SALES = ? WHERE COF_NAME LIKE ? ");
    updateSales.setInt(1, 75);
    updateSales.setString(2, "Colombian");
    updateSales.executeUpdate();
    
        片断2和片断1的区别在于，后者使用了PreparedStatement对象，而前者是普通的Statement对象。
    PreparedStatement对象不仅包含了SQL语句，而且大多数情况下这个语句已经被预编译过，因而当其执行时，只需DBMS运行SQL语句，而不必先编译。
    当你需要执行Statement对象多次的时候，PreparedStatement对象将会大大降低运行时间，当然也加快了访问数据库的速度。
    这种转换也给你带来很大的便利，不必重复SQL语句的句法，而只需更改其中变量的值，便可重新执行SQL语句。
    选择PreparedStatement对象与否，在于相同句法的SQL语句是否执行了多次，而且两次之间的差别仅仅是变量的不同。
    如果仅仅执行了一次的话，它应该和普通的对象毫无差异，体现不出它预编译的优越性。
 
 三.最重要的一点是极大地提高了安全性.
    
    即使到目前为止,仍有一些人连基本的恶义SQL语法都不知道.
    String sql = "select * from tb_name where name= '"+varname+"' and passwd='"+varpasswd+"'";
    如果我们把[' or '1' = '1]作为varpasswd传入进来.用户名随意,看看会成为什么?
    
    select * from tb_name = '随意' and passwd = '' or '1' = '1';
    因为'1'='1'肯定成立,所以可以任何通过验证.更有甚者:
    把[';drop table tb_name;]作为varpasswd传入进来,则:
    select * from tb_name = '随意' and passwd = '';drop table tb_name;有些数据库是不会让你成功的,但也有很多数据库就可以使这些语句得到执行.
    
    而如果你使用预编译语句.你传入的任何内容就不会和原来的语句发生任何匹配的关系.
    (前提是数据库本身支持预编译,但上前可能没有什么服务端数据库不支持编译了,只有少数的桌面数据库,就是直接文件访问的那些)
    只要全使用预编译语句,你就用不着对传入的数据做任何过虑.而如果使用普通的statement,有可能要对drop,;等做费尽心机的判断和过虑.
    

