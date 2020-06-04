[https://www.php.cn/faq/419041.html][https://www.php.cn/faq/419041.html]

什么是SQL注入攻击

sql注入是一种将 sql 代码添加到输入参数中，传输到 sql 服务器解析并执行的一种攻击方法。
sql注入是如何产生的？

1.web开发人员无法保证所有的输入都已经过滤。
2.攻击者利用发送给sql服务器的输入数据构造可执行代码。

3.数据库未作相应的安全配置（对web应用设置特定的数据库账号，而不使用root或管理员账号，特定数据库账号给予一些简单操作的权限，回收一些类似drop的操作权限）。



sql注入攻击防御



1、使用参数化筛选语句

为了防止SQL注入，用户输入不能直接嵌入到SQL语句中。相反，用户输入必须被过滤或参数化。参数语句使用参数，而不是将用户输入嵌入语句中。在大多数情况下，SQL语句是正确的。然后，用户输入仅限于一个参数。

一般来说，有两种方法可以确保应用程序不易受到SQL注入攻击。一种是使用代码审查，另一种是强制使用参数化语句。强制使用参数化语句意味着在运行时将拒绝嵌入用户输入中的SQL语句。但是，目前对此功能的支持不多。

2、避免使用解释程序，这是黑客用来执行非法命令的手段。

3、防止SQL注入，但也避免一些详细的错误消息，因为黑客可以使用这些消息。标准的输入验证机制用于验证所有输入数据的长度、类型、语句和企业规则。

4、使用专业的漏洞扫描工具。

但是，防范SQL注入攻击是不够的。攻击者现在自动搜索和攻击目标。它的技术甚至可以很容易地应用于其他Web体系结构中的漏洞。企业应该投资于专业的漏洞扫描工具，如著名的Accunetix网络漏洞扫描程序。完美的漏洞扫描器不同于网络扫描器，它专门在网站上查找SQL注入漏洞。最新的漏洞扫描程序可以找到最新发现的漏洞。

5、最后，企业在Web应用程序开发过程的所有阶段执行代码安全检查。首先，安全测试应该在部署Web应用程序之前实现，这比以前更重要、更深远。企业还应在部署后使用漏洞扫描工具和站点监控工具测试网站。

以上就是sql注入攻击如何防御？的详细内容，更多请关注php中文网其它相关文章！

[https://www.php.cn/faq/419041.html]: https://www.php.cn/faq/419041.html




**防止SQL注入的五种方法**

[https://www.cnblogs.com/pressur/p/11226392.html][https://www.cnblogs.com/pressur/p/11226392.html]

一、SQL注入简介
    SQL注入是比较常见的网络攻击方式之一，它不是利用操作系统的BUG来实现攻击，而是针对程序员编程时的疏忽，通过SQL语句，实现无帐号登录，甚至篡改数据库。

二、SQL注入攻击的总体思路

1.寻找到SQL注入的位置

2.判断服务器类型和后台数据库类型

3.针对不通的服务器和数据库特点进行SQL注入攻击

 

三、SQL注入攻击实例

比如在一个登录界面，要求输入用户名和密码：

可以这样输入实现免帐号登录：

用户名： ‘or 1 = 1 –

密 码：

点登陆,如若没有做特殊处理,那么这个非法用户就很得意的登陆进去了.(当然现在的有些语言的数据库API已经处理了这些问题)

这是为什么呢? 下面我们分析一下：

从理论上说，后台认证程序中会有如下的SQL语句：

String sql = "select * from user_table where username=

' "+userName+" ' and password=' "+password+" '";

当输入了上面的用户名和密码，上面的SQL语句变成：

SELECT * FROM user_table WHERE username=

'’or 1 = 1 -- and password='’

分析SQL语句：

条件后面username=”or 1=1 用户名等于 ” 或1=1 那么这个条件一定会成功；

然后后面加两个-，这意味着注释，它将后面的语句注释，让他们不起作用，这样语句永远都能正确执行，用户轻易骗过系统，获取合法身份。

这还是比较温柔的，如果是执行

SELECT * FROM user_table WHERE

username='' ;DROP DATABASE (DB Name) --' and password=''

….其后果可想而知… 

四、应对方法

下面我针对JSP，说一下应对方法：

1.（简单又有效的方法）PreparedStatement

采用预编译语句集，它内置了处理SQL注入的能力，只要使用它的setXXX方法传值即可。

使用好处：

(1).代码的可读性和可维护性.

(2).PreparedStatement尽最大可能提高性能.

(3).最重要的一点是极大地提高了安全性.

原理：

sql注入只对sql语句的准备(编译)过程有破坏作用

而PreparedStatement已经准备好了,执行阶段只是把输入串作为数据处理,

而不再对sql语句进行解析,准备,因此也就避免了sql注入问题. 

2.使用正则表达式过滤传入的参数

要引入的包：

import java.util.regex.*;

正则表达式：

private String CHECKSQL = “^(.+)\\sand\\s(.+)|(.+)\\sor(.+)\\s$”;

判断是否匹配：

Pattern.matches(CHECKSQL,targerStr);

下面是具体的正则表达式：

检测SQL meta-characters的正则表达式 ：

/(\%27)|(\’)|(\-\-)|(\%23)|(#)/ix

修正检测SQL meta-characters的正则表达式 ：/((\%3D)|(=))[^\n]*((\%27)|(\’)|(\-\-)|(\%3B)|(:))/i

典型的SQL 注入攻击的正则表达式 ：/\w*((\%27)|(\’))((\%6F)|o|(\%4F))((\%72)|r|(\%52))/ix

检测SQL注入，UNION查询关键字的正则表达式 ：/((\%27)|(\’))union/ix(\%27)|(\’)

检测MS SQL Server SQL注入攻击的正则表达式：

/exec(\s|\+)+(s|x)p\w+/ix

等等…..

 

3.字符串过滤

比较通用的一个方法：

（||之间的参数可以根据自己程序的需要添加）

public static boolean sql_inj(String str){

String inj_str = "'|and|exec|insert|select|delete|update|

count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";

String inj_stra[] = split(inj_str,"|");

for (int i=0 ; i &lt; inj_stra.length ; i++ ){

if (str.indexOf(inj_stra[i])&gt;=0){

return true;

}

}

return false;

}

 

4.jsp中调用该函数检查是否包函非法字符

 

防止SQL从URL注入：

sql_inj.java代码：

package sql_inj;

import java.net.*;

import java.io.*;

import java.sql.*;

import java.text.*;

import java.lang.String;

public class sql_inj{

public static boolean sql_inj(String str){

String inj_str = "'|and|exec|insert|select|delete|update|

count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";

//这里的东西还可以自己添加

String[] inj_stra=inj_str.split("\\|");

for (int i=0 ; i &lt; inj_stra.length ; i++ ){

if (str.indexOf(inj_stra[i])&gt;=0){

return true;

}

}

return false;

}

}

 

5.JSP页面判断代码：

 

使用javascript在客户端进行不安全字符屏蔽

功能介绍：检查是否含有”‘”,”\\”,”/”

参数说明：要检查的字符串

返回值：0：是1：不是

函数名是

function check(a){

return 1;

fibdn = new Array (”‘” ,”\\”,”/”);

i=fibdn.length;

j=a.length;

for (ii=0; ii＜i; ii++)

{ for (jj=0; jj＜j; jj++)

{ temp1=a.charAt(jj);

temp2=fibdn[ii];

if (tem’; p1==temp2)

{ return 0; }

}

}

return 1;

}

===================================

总的说来，防范一般的SQL注入只要在代码规范上下点功夫就可以了。

凡涉及到执行的SQL中有变量时，用JDBC（或者其他数据持久层）提供的如：PreparedStatement就可以 ，切记不要用拼接字符串的方法就可以了。


来源： https://www.cnblogs.com/pressur/p/11226392.html

[https://www.cnblogs.com/pressur/p/11226392.html]: https://www.cnblogs.com/pressur/p/11226392.html