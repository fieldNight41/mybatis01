
**关于HttpServlet和Servlet以及doPost和doGet关系**

参考： [https://www.cnblogs.com/yefengCrazy/p/5636710.html][https://www.cnblogs.com/yefengCrazy/p/5636710.html]

Servlet的框架是由两个Java包组成:javax.servlet和javax.servlet.http. 在javax.servlet包中定义了所有的Servlet类都必须实现或扩展的的通用接口和类.在javax.servlet.http包中定义了采用HTTP通信协议的HttpServlet类.

Servlet的框架的核心是javax.servlet.Servlet接口,所有的Servlet都必须实现这一接口.在Servlet接口中定义了5个方法,其中有3个方法代表了Servlet的声明周期:

init方法,负责初始化Servlet对象
service方法,负责相应客户的请求
destory方法,当Servlet对象退出声明周期时,负责释放占有的资源

当Web容器接收到某个Servlet请求时,Servlet把请求封装成一个HttpServletRequest对象,然后把对象传给Servlet的对应的服务方法.

     HTTP的请求方式包括DELETE,GET,OPTIONS,POST,PUT和TRACE,在HttpServlet类中分别提供了相应的服务方法,它们是,doDelete(),doGet(),doOptions(),doPost(), doPut()和doTrace(). 

HttpServlet的功能  

HttpServlet首先必须读取Http请求的内容。Servlet容器负责创建HttpServlet对象，并把Http请求直接封装到HttpServlet对象中，大大简化了HttpServlet解析请求数据的工作量。HttpServlet容器响应Web客户请求流程如下：

1）Web客户向Servlet容器发出Http请求；

2）Servlet容器解析Web客户的Http请求；

3）Servlet容器创建一个HttpRequest对象，在这个对象中封装Http请求信息；

4）Servlet容器创建一个HttpResponse对象；

5）Servlet容器调用HttpServlet的service方法，把HttpRequest和HttpResponse对象作为service方法的参数传给HttpServlet对象；

6）HttpServlet调用HttpRequest的有关方法，获取HTTP请求信息；

7）HttpServlet调用HttpResponse的有关方法，生成响应数据；

8）Servlet容器把HttpServlet的响应结果传给Web客户。

二、创建HttpServlet的步骤——“四部曲”

1）扩展HttpServlet抽象类；

2）覆盖HttpServlet的部分方法，如覆盖doGet()或doPost()方法；

3）获取HTTP请求信息。通过HttpServletRequest对象来检索HTML表单所提交的数据或URL上的查询字符串；

4）生成HTTP响应结果。通过HttpServletResponse对象生成响应结果，它有一个getWriter()方法，该方法返回一个PrintWriter对象。

举个例子如下：

package mypack;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
public class HelloServlet extends HttpServlet//第一步：扩展HttpServlet抽象类 
{
 //第二步：覆盖doGet()方法
 public void doGet(HttpServletRequest request,
  HttpServletResponse response)throws IOException,ServletException{
  //第三步：获取HTTP请求中的参数信息
  String clientName=request.getParameter("clientName");
  if(clientName!=null)
   clientName=new String(clientName.getBytes("ISO-8859-1"),"GB2312");
  else
   clientName="我的朋友";

  //第四步：生成HTTP响应结果
  PrintWriter out;
  String title="HelloServlet";
  String heading1="HelloServlet的doGet方法的输出：";
  //set content type
  response.setContentType("text/html;charset=GB2312");
  //write html page
  out=response.getWriter();
  out.print("<HTML><HEAD><TITLE>"+title+"</TITLE>");
  out.print("</HEAD><BODY>");
  out.print(heading1);
  out.println("<h1><p>"+clientName+":您好</h1>");
  out.print("</BODY></HTML>");

  out.close();
 }
}

在web.xml中添加

<servlet>
   <servlet-name>HelloServlet</servlet-name>
   <servlet-class>mypack.HelloServlet</servlet-class>
  </servlet>
  <servlet-mapping>
   <servlet-name>HelloServlet</servlet-name>
   <url-pattern>/hello</url-pattern>
</servlet-mapping>

通过URL访问HelloServlet:

注意：

实现service方法。 

    Servlet的主要功能是接受从浏览器发送过来的HTTP请求（request），并返回HTTP响应（response）。这个工作是在service方法中完成的。service方法包括从request对象获得客户端数据和向response对象创建输出。 

    如果一个Servlet从javax.servlet.http.HttpServlet继承，实现了doPost或doGet方法，那么这个Servlet只能对POST或GET做出响应。如果开发人员想处理所有类型的请求（request），只要简单地实现service方法即可（但假如选择实现service方法，则不必实现doPost或doGet方法，除非在service方法的开始调用super.service()）。



[https://www.cnblogs.com/yefengCrazy/p/5636710.html]: https://www.cnblogs.com/yefengCrazy/p/5636710.html