**SLF4J 日志：** 
  在 pom.xml 中，当前只引入了  slf4j-nop 

技术概述：采用Sl4j作为日志门面，Log4j2作为日志输出的具体实现；同时结合lombok插件，减少代码的书写。
本项目：   Log4j 、 slf4j-log4j12 （与slf4j保持桥接）   

**问题1：** 只引入 slf4j-api 时，日志不打印，报异常：
    
         SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
         SLF4J: Defaulting to no-operation (NOP) logger implementation 
处理： 
    
    改为引入   slf4j-nop ，OK异常消除。      
 
<br/>  
     
**问题2：** 日志不打印
    
    在 src/mian 下创建 resources/logback.xml 文件，定义日志输出级别等，  仍然不可行 ；
    后又 改为在 src 下创建 src/logback.xml 文件，仍然不可行 ；

参考：普通Java项目中使用Sl4j+Log4j2打印日志 
    [https://www.cnblogs.com/zeng1994/p/fe096e4423deef6b57fdf735b9df5e7f.html][https://www.cnblogs.com/zeng1994/p/fe096e4423deef6b57fdf735b9df5e7f.html]

参考：项目配置使用slf4j
    [https://www.jianshu.com/p/7aa379fa81bd][https://www.jianshu.com/p/7aa379fa81bd]

参考： log4j
    [https://baike.baidu.com/item/log4j/480673?fr=aladdin][https://baike.baidu.com/item/log4j/480673?fr=aladdin]

**解决：**  配置文件名称错误原因

    （1）将log 配置文件名称改为 log4j.properties 或者 log4j.xml 
           如果两个都有，则会默认使用 log4j.xml。 
    （2）引入 log4j-1.2.17.jar log4j日志输出的具体实现 ，
        通过使用Log4j，我们可以控制日志信息输送的目的地是控制台、文件、GUI组件，甚至是套接口服务器、NT的事件记录器、UNIX Syslog守护进程等；
    （3）引入 slf4j-log4j12-1.7.21.jar  用于与slf4j保持桥接（里面自动依赖了 slf4j-api ）
    （4）引入 slf4j-api-1.7.29.jar 
    （5）去掉 不适用的 slf4j-nop-1.7.30。jar，与 Log4j 一起使用会抛异常。       

<br/> 

**问题3：**  使用 log4j.xml 时报错，内容需要修改 ；
    
    log4j:WARN Continuable parsing error 2 and column 53
    log4j:WARN 文档根元素 "configuration" 必须匹配 DOCTYPE 根 "null"。
    log4j:WARN Continuable parsing error 2 and column 53
    log4j:WARN 文档无效: 找不到语法。
    log4j:WARN The <configuration> element has been deprecated.
    log4j:WARN Use the <log4j:configuration> element instead.
    log4j:WARN Unrecognized element contextName
    log4j:WARN Unrecognized element property
    log4j:WARN Unrecognized element property
    log4j:ERROR Could not create an Appender. Reported error follows.
    java.lang.ClassNotFoundException: ch.qos.logback.core.ConsoleAppender


        
**说明：** log4j2 配置文件的读取位置，默认：
        
       (1)非 maven 项目， src 目录下的 log4j2.xml
       (2) Maven 项目，rsources 目录下的 log4j2.xml
        
  
  *****************************************************
  ************************分割线************************  
  
 
JavaWebHttpServlet 普通web项目添加 slf4j+log4j日志打印：
配置文件位于 src下。 
引入：log4j-1.2.17.jar 、slf4j-api-1.7.30.jar 、 slf4j-log4j12-1.7.30.jar
  
    # D:\javaPage\TomcatTwo\apache-tomcat-8.5.15-windows-x64\apache-tomcat-8.5.15\bin\logs
    # log4j.appender.root.File=${scheduleProject}logs/root.log
    log4j.appender.root.File = D:\\MyGitHubProjects\\Mybatis-Learn\\JavaWebHttpServlet\\logs\\javaWebHttpServlet.log
    

[https://www.cnblogs.com/zeng1994/p/fe096e4423deef6b57fdf735b9df5e7f.html]: https://www.cnblogs.com/zeng1994/p/fe096e4423deef6b57fdf735b9df5e7f.html

[https://www.jianshu.com/p/7aa379fa81bd]: https://www.jianshu.com/p/7aa379fa81bd

[https://baike.baidu.com/item/log4j/480673?fr=aladdin]: https://baike.baidu.com/item/log4j/480673?fr=aladdin