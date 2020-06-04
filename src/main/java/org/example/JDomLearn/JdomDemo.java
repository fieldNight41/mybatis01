package org.example.JDomLearn;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * 2020-6-2 ：JDOM 学习
 */
public class JdomDemo {

    // 利用 slf4j 输出调试信息   (或者 用注解  @Slf4j )
    private static Logger LOGGER = LoggerFactory.getLogger(JdomDemo.class);


    public static void main(String[] args) throws Exception {
        JdomDemo demo = new JdomDemo();
        demo.buildXML();
        LOGGER.info("************************ 分割线 ***********************************");
        LOGGER.debug("this is debug message。");
        demo.readXML();
    }

    /**
     * 一、利用 JDOM 组织 xml 文件
     */
    public void buildXML() throws IOException {
        // 1.1 创建根节点
        Element schoolElement = new Element("School");
        // 1.2 创建 Document 对象，将根节点添加到 Document 对象中
        Document xmlFileDocument = new Document(schoolElement);
        // 1.3 设置节点属性  （可以连续运用 setAttribute("attributeName","attributeValue") 来设置属性）
        schoolElement.setAttribute("address","河南")
                .setAttribute("schoolName","河南师范大学");

        // 1.4 需要增加新节点时，
        // 需要 new 一个 Element 对象，再调用父节点的 addContent 方法将新节点增加到文件中；如需要在父节点下增加多歌并列的子节点，字需要调用父节点的 addContent 方法
        Element colegeSWElement = new Element("College").setAttribute("colegeName","生命科学学院");
        schoolElement.addContent(colegeSWElement);
        // 1.5 在创建新节点的时候可以用时通过 setAttribute 来设置节点的属性，或者通过 setText("TextValue") 方法设置节点的值
        Element classesElement = new Element("Class").setAttribute("no","0212");
        classesElement.addContent(new Element("name").setText("提莫"));
        classesElement.addContent(new Element("age").setText("4"));
        classesElement.addContent(new Element("sex").setText("男"));
        classesElement.addContent(new Element("studyTime").setText("2013~2017"));

        Element academicYear1Element = new Element("academicYear1");
        Element academicYear2Element = new Element("academicYear2");
        Element academicYear3Element = new Element("academicYear3");
        Element academicYear4Element = new Element("academicYear4");
        classesElement.addContent(academicYear1Element.setAttribute("year","第一学年"));
        classesElement.addContent(academicYear2Element.setAttribute("year","第二学年"));
        classesElement.addContent(academicYear3Element.setAttribute("year","第三学年"));
        classesElement.addContent(academicYear4Element.setAttribute("year","第四学年"));

        academicYear1Element.addContent(new Element("course").setAttribute("courseName","C语言").setText("92"));
        academicYear1Element.addContent(new Element("course").setAttribute("courseName","计算机基础").setText("88"));
        academicYear1Element.addContent(new Element("course").setAttribute("courseName","高等数学").setText("87"));
        academicYear1Element.addContent(new Element("course").setAttribute("courseName","英语").setText("82"));

        academicYear2Element.addContent(new Element("course").setAttribute("courseName","汇编").setText("90"));
        academicYear2Element.addContent(new Element("course").setAttribute("courseName","离散数学").setText("94"));
        academicYear2Element.addContent(new Element("course").setAttribute("courseName","线性代数").setText("91"));
        academicYear2Element.addContent(new Element("course").setAttribute("courseName","模拟电路").setText("96"));

        academicYear3Element.addContent(new Element("course").setAttribute("courseName","计算机组成原理").setText("88"));
        academicYear3Element.addContent(new Element("course").setAttribute("courseName","操作系统").setText("92"));
        academicYear3Element.addContent(new Element("course").setAttribute("courseName","Java语言").setText("97"));
        academicYear3Element.addContent(new Element("course").setAttribute("courseName","编译原理").setText("84"));

        academicYear4Element.addContent(new Element("course").setAttribute("courseName","C++语言").setText("96"));
        academicYear4Element.addContent(new Element("course").setAttribute("courseName","信息安全").setText("81"));
        academicYear4Element.addContent(new Element("course").setAttribute("courseName","密码学").setText("80"));
        academicYear4Element.addContent(new Element("course").setAttribute("courseName","毕业设计").setText("93"));
        //
        colegeSWElement.addContent(classesElement);
        // 1.6 也可以先添加节点，再设置节点属性
        Element collegeFLElement = new Element("College").setAttribute("collegeName","外国语学院");
        schoolElement.addContent(collegeFLElement);
        collegeFLElement.addContent(new Element("language").setAttribute("kind","English"));
        collegeFLElement.addContent(new Element("language").setAttribute("kind","Japanese"));
        collegeFLElement.addContent(new Element("language").setAttribute("kind","French"));
        collegeFLElement.addContent(new Element("language").setAttribute("kind","German"));

        /**
         * 1.7 创建 Format 对象，用于格式化 XML 字符串，XMLOutputter 默认输出的编码方式是 UTF-8；
             中文乱码： Format  的 setEncoding 设置输出的编码方式为 “GBK” 或者 “GB2312”来解决。
            如果使用 “GBK” ，在 readXML() 方法中读取文件需要转换为 “UTF-8", 否则报异常
         本机测试：好像刚好相反 -- XMLOutputter 用 UTF-8 ；readXML() 也需要转换为 UTF-8 ；都不乱码。
                              XMLOutputter 用GBK，打印会乱码； readXML() 需要转换为 GB2312，就不乱码。
         */
        Format format = Format.getPrettyFormat();
        //设置 xml 文件的缩进为 4 个空格
        format.setIndent("    ");
        XMLOutputter outputter = new XMLOutputter(format.setEncoding("GBK")); // 这里用UTF-8，out不乱码； 但读取时 InputStreamReader 也需要用UTF-8
        /**
         * 利用 Calendar 获取当前距离格林威治标准时间 1970 年 1 月 1 日 00:00:00 的偏移量（毫秒数）
         * 再利用 SimpleDataFormat 格式化时间为 yyyyMMdd 格式
           字母   日期活时间元素     表示      示例
            G    Era              标志符     Text   AD
            y    年               Year      1996; 96
            M    年中的月份         Month     July; Jul ; 07
            w    年中的周数         Number    27
            W    月份中的周数       Number    2
            D    年中的天数         Number    189
            d    月份中的天数       Number    10
            F    月份中的星期       Number    2
            E    星期中的天数       Number    Text  Tuesday; True
            a    Am/pm            标记       Text PM
            H    一天中的小时数(0-23) Number   0
            k    一天中的小时数(1-24) NUmber   24
            K    am/pm中的稀释书(0-11) Number  0
            h    am/pm中的小时数（1-12）Number  12
            m    小时中的分钟数       Number    30
            s    分钟中的秒数         Number    55
            S    毫秒数             Number    978
            z    时区    General time zone     Pacific Standard Time; PST; GMT-08:00
            Z    时区     RFC 822 time zone    -0800
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String date = simpleDateFormat.format(calendar.getTime());
        // 1.8 在程序中可以通过 addContent(index,new Element("***")) 来向 XML 中的某个节点在指定的位置增加子节点
        schoolElement.addContent(0,new Element("fileBuildTime").setText(date));

        /**
         *  1.9 removeChild : 删除第一个子节点名为 College 的节点
                    schoolElement.removeChild("College");
            removeAttribute: 删除节点名为 “school" 的address 属性
                schoolElment.removeAttribute("address");
            获取所有节点名为 "College" 的节点列表
                List child = school.getChildren("Collage");
            删除节点列表中的第一个节点：
                child.remove(0);
            删除所有名为 "College" 的几点
                child.removeAll("College");
            删除叫 "College" 的子元素
                child.removeAll(school.getChildren("College"));
            便捷写法
                school.removeChildren("College");
         */
        LOGGER.info("\n buildXML() 方法组织好的 XML 文件为：\n" +
                "******************************************\n" +
                "outputter.outputString(xmlFileDocument)" +
                "\n ***************************************\n");

        //  通过 removeContent 方法可以将一个节点移动到另一个节点下
        // 1.10 获取节点
        Element studyTime = classesElement.getChild("studyTime");
        // 1.11 将节点移出
        classesElement.removeContent(studyTime);
        // 1.12 将节点重新插入
        schoolElement.addContent(1,studyTime);
        LOGGER.info("应用 removeContent 方法移动节点 “studyTime” 后的效果。");

        outputter.output(xmlFileDocument,System.out);
        /**
         *  1.13 可以通过 outputString 方法将 XML 文件转换成 String 类型
         *      String subString = out.outputString(schooleElemnet.getChildren("Colege"));
         */
        // new 一个文件夹 存放 JDOM 生成的 XML 文件；创建文件可以用 new  (new File("c:/JDOMStudy").createNewFile())

        String proPath = System.getProperty("user.dir"); // D:\MyGitHubProjects\Mybatis-Learn\Mybatis001
        new File(proPath+"\\JdomStudy").mkdir();

        /**
         * 1.14 可以通过 delete 方法删除指定的文件，好像只能删除文件，不能删除文件夹（如果文件夹中包含文件）
         *      要删除整个文件夹可以通过递归得到该文件夹中的文件列表，利用循环删除子文件后再删除问价夹
         *      new File("c:/all.sql").delete();
         *      下面这个方法是列出指定目录小的所有文件和文件夹，JAVA 笔试比较常见的题目
         *      List dirslist = Arrays.ashist(new File("c:/").listFiles());
         */
        /**
         * 利用 XMLOutputter(SAXOutputter、DemoOutputter、JTreeOutputter)
         * 输出组织好的 XML 文件，注意这里不能利用 System.out 输出
         * 也可以用下面这种方式输出到文件
         * FileWriter fr = new FileWriter("c:\\JdomStudy\\MyHUST-"+date+".xml");
         * fr.write(out.outprintString(xmlFile));
         * fr.close(); // 注意这里写完文件后一定要关闭，否则文件不能成功写入
         */
        outputter.output(xmlFileDocument,new FileOutputStream(proPath+"\\JdomStudy\\myHust-"+date+".xml"));

    }


    /**
     * 二、利用 JDOM 解析 XML 文件   (通过 SAXBuild)
     */
    public void readXML() throws Exception {

        /**
         * 2.1 利用 Calendar 获取当前距离格林威治标准时间 1970 年 1 月 1 日 00:00:00 的偏移量（毫秒数）
         * 再利用 SimpleDataFormat 格式化时间为 yyyyMMdd 格式
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String date = simpleDateFormat.format(calendar.getTime());

        SAXBuilder saxBuilder = new SAXBuilder();
        // 2.2 从磁盘读取 buildXML() 方法生成的 xml 文件
        String proPath = System.getProperty("user.dir");
        FileInputStream fileInputStream = new FileInputStream(proPath+"\\JdomStudy\\myHust-"+date+".xml");
        /**
         * 2.3 读取文件的时候转换文件的格式为“UTF-8” 或者 “GB2312”，因为在 buildXML() 方法中为了解决中文问题，设置了文件的编码方式为 GBK，
         *  如果再 readXML 中不进行转换，则会出现编码不一致的异常，如果 buildXML() 的编码格式为 GB2312，则在此不需要转换。
         */
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"GB2312"); // UTF-8 中文乱码，除非 写入的时候也用UTF-8
        Document document = saxBuilder.build(inputStreamReader);
        // 格式化输出，解决中文乱码问题
        Format format = Format.getPrettyFormat();
        XMLOutputter outputter = new XMLOutputter(format.setEncoding("GB2312"));// 或者 UTF-8

        // 2.4 获取根目录
        Element rootElement = document.getRootElement();
        // 2.5 获取第一个子节点 fileBuildTime();
        Element fileBuildTime = rootElement.getChild("fileBuildTime");
        LOGGER.info("第一个子节点 fileBuileTime 为："+outputter.outputString(fileBuildTime));
        LOGGER.info("第一个子节点 fileBuileTime 的内容为："+fileBuildTime.getText());

        // 2.6 通过 XPath  获取任意路径下的 course 元素
        XPath courseXpath = XPath.newInstance("//course");
        //  返回所有的 course 元素
        List courseList = courseXpath.selectNodes(document);
        LOGGER.info(proPath+"\\JdomStudy\\myHust-"+date+".xml 文件中含有*"+courseList.size()+"*个\"course\"节点\n\n这些节点的内容分别是：");
        // 循环处理所有的 course 元素
        Iterator courseIterator = courseList.iterator();
        while (courseIterator.hasNext()){
            Element courseElement = (Element) courseIterator.next();
            // 通过 getParentElement() 方法可以获得本节点的父节点信息
            LOGGER.info(courseElement.getParentElement().getAttribute("year").getValue().toString()+
                    "的学习成绩："+courseElement.getAttribute("courseName").getValue().toString()+
                    ":"+courseElement.getText());

            // 也可以通过 XPath 指定路径获取节点信息
            XPath path = XPath.newInstance("//College/Class/name");
            List textList = path.selectNodes(document);
            Iterator nameIterator = textList.iterator();
            while (nameIterator.hasNext()){
                Element nameElement = (Element) nameIterator.next();
                LOGGER.info("本程序的作者是："+nameElement.getTextTrim());
                // 我们也可以通过 set*** 方法来设置其他属性或者节点的值
                nameElement.setText("IT搬砖");
                LOGGER.info("修改作者后本程序的作者是："+nameElement.getTextTrim());
            }
        }

    }
}
