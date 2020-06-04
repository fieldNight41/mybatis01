**获取java项目目录 根目录**
 
 参考：[https://blog.csdn.net/lacrimarrum/article/details/80050563][https://blog.csdn.net/lacrimarrum/article/details/80050563]
 
     System.out.println(System.getProperty("user.dir")); // D:\MyGitHubProjects\Mybatis-Learn\Mybatis001
     System.out.println(JdomDemo.class.getPackage()); // package org.example.JDomLearn
     System.out.println(JdomDemo.class.getResource("/")); // file:/D:/MyGitHubProjects/Mybatis-Learn/Mybatis001/target/classes/
     System.out.println(JdomDemo.class.getResource("user.dir"));
     System.out.println(JdomDemo.class.getClass().getPackage()); // package java.lang, Java Platform API Specification, version 1.8
           
 
    System.out.println( ProductInfoController.class.getResource("") );
    System.err.println(ProductInfoController.class.getResource("").getPath());
    //输出结果：file:/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/com/apparel/controller/
    //输出结果：/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/com/apparel/controller/
    //获取当前类所在的包路径
 
    //获取class路径
    System.out.println( ProductInfoController.class.getResource("/") );
    System.err.println(ProductInfoController.class.getResource("/").getPath());
    //输出结果：file:/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/
    //输出结果：/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/
 
    System.out.println( ProductInfoController.class.getClassLoader().getResource("") );
    System.err.println(ProductInfoController.class.getClassLoader().getResource("").getPath());
    //输出结果：file:/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/
    //输出结果：/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/
     
 
    //获取class路径
    System.err.println( ProductInfoController.class.getProtectionDomain().getCodeSource().getLocation() );
    System.err.println( ProductInfoController.class.getProtectionDomain().getCodeSource().getLocation().getPath() );
    //输出结果：file:/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/
    //输出结果：/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/
     
    System.err.println( getClass().getProtectionDomain().getCodeSource().getLocation().getPath() );
    //输出结果：/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/
     
     
    //获取项目根目录
    System.err.println(request.getSession().getServletContext().getRealPath("/"));
    //输出结果：D:\Develop\apache-tomcat-7.0.83\webapps\apparelDev\

在得到的class路径中 获取 根目录

    String path = "/D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/WEB-INF/classes/" ;
    if (path.indexOf("WEB-INF") > 0) {    
        path = path.substring(0, path.indexOf("WEB-INF"));     
    }
    System.err.println("path = "+path);
    path = /D:/Develop/apache-tomcat-7.0.83/webapps/apparelDev/

[https://blog.csdn.net/lacrimarrum/article/details/80050563]: https://blog.csdn.net/lacrimarrum/article/details/80050563



**Java中删除一个文件夹下的所有文件(包括子目录内的文件)**

参考： [https://blog.csdn.net/qq_39072607/article/details/90316216][https://blog.csdn.net/qq_39072607/article/details/90316216]

     import java.io.File;
    
    public class Demo8 {
        static int flag = 1;//用来判断文件是否删除成功
        public static void main(String[] args) {
            //删除一个文件夹下的所有文件(包括子目录内的文件)
            File file = new File("D:\\delete");//输入要删除文件目录的绝对路径
            deleteFile(file);
            if (flag == 1){
                System.out.println("文件删除成功！");
            }
        }
        public static void deleteFile(File file){
            //判断文件不为null或文件目录存在
            if (file == null || !file.exists()){
                flag = 0;
                System.out.println("文件删除失败,请检查文件路径是否正确");
                return;
            }
            //取得这个目录下的所有子文件对象
            File[] files = file.listFiles();
            //遍历该目录下的文件对象
            for (File f: files){
                //打印文件名
                String name = file.getName();
                System.out.println(name);
                //判断子目录是否存在子目录,如果是文件则删除
                if (f.isDirectory()){
                    deleteFile(f);
                }else {
                    f.delete();
                }
            }
            //删除空文件夹  for循环已经把上一层节点的目录清空。
            file.delete();
        }
    }

如果想看遍历目录下所有文件(直到根目录)请转链接:
Java如何遍历一个目录下的所有的文件(直到根目录不包括目录)？
[https://blog.csdn.net/qq_39072607/article/details/90314619][https://blog.csdn.net/qq_39072607/article/details/90314619]


[https://blog.csdn.net/qq_39072607/article/details/90316216]: https://blog.csdn.net/qq_39072607/article/details/90316216

[https://blog.csdn.net/qq_39072607/article/details/90314619]: https://blog.csdn.net/qq_39072607/article/details/90314619