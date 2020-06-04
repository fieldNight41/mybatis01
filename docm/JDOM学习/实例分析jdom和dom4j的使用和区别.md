**实例分析jdom和dom4j的使用和区别**

参考：[https://www.cnblogs.com/zhi-hao/p/4016363.html][https://www.cnblogs.com/zhi-hao/p/4016363.html]

对于xml的解析和生成，我们在实际应用中用的比较多的是JDOM和DOM4J，下面通过例子来分析两者的区别
（在这里我就不详细讲解怎么具体解析xml，如果对于xml的解析看不懂的可以先去看下我之前
关于dom跟sax解析xml的随笔 http://www.cnblogs.com/zhi-hao/p/3985720.html ，
其实理解了解析xml的原理，要想学习jdom跟dom4j就比较简单了，jdom跟dom4j只是基于底层api的更高级的封装，
而dom和sax是解析xml的底层接口），我们在使用jdom和dom4j时要导入相应的包，这些包可以到jdom和dom4j的官网去下载。

首先通过jdom来生成xml

    package xmlTest;
    /**
     * @author CIACs
     */
    import java.io.FileOutputStream;
    
    import org.jdom.Document;
    import org.jdom.Element;
    import org.jdom.output.Format;
    import org.jdom.output.XMLOutputter;
    
    public class Jdom {
    
        public static void main(String[] args) throws Exception{
            Document doc = new Document();
            Element root = new Element("root");
            doc.addContent(root);
    
            Element name = new Element("name");
            root.addContent(name);
            root.setAttribute("author","CIACs").setAttribute("url", "http://www.cnblogs.com/zhi-hao/");
            name.addContent("CIACs");
    
            XMLOutputter out = new XMLOutputter();
            Format format = Format.getPrettyFormat();
            format.setIndent("   ");
            out.setFormat(format);
            out.output(doc,new FileOutputStream("jdom.xml"));
    
        }
    
    }

生成的xml

     <?xml version="1.0" encoding="UTF-8"?>
     <root author="CIACs" url="http://www.cnblogs.com/zhi-hao/">
        <name>CIACs</name>
     </root>
 
 这里生成的xml比较简单，这是为了容易理解，简单的理解了，复杂的其实也就会了。
 
 
 接下来通过jdom对xml进行解析
 
    package xmlTest;
    /**
     * @author CIACs
     */
    import java.io.File;
    import java.io.FileOutputStream;
    import java.util.List;
    
    import org.jdom.Attribute;
    import org.jdom.Document;
    import org.jdom.Element;
    import org.jdom.input.SAXBuilder;
    import org.jdom.output.XMLOutputter;
    
    public class Jdom2 {
    
        public static void main(String[] args) throws Exception {
            //通过SAXBuilder解析xml
            SAXBuilder builder = new SAXBuilder();
    
            Document doc = builder.build(new File("jdom.xml"));
    
            Element root = doc.getRootElement();
    
            System.out.println(root.getName());
    
            String name = root.getChild("name").getText();
    
            System.out.println("name: "+name);
    
            List attrs = root.getAttributes();
    
            for(int i = 0; i < attrs.size();i++)
            {
                String attrName;
                String attrValue;
                Attribute attr = (Attribute)attrs.get(i);
                attrName = attr.getName();
                attrValue = attr.getValue();
                System.out.println(attrName+":"+attrValue);
    
            }
            //删除属性url，并保存到jdom2.xml
            root.removeAttribute("url");
    
            XMLOutputter out = new XMLOutputter();
            out.output(doc, new FileOutputStream("jdom2.xml"));
    
    
        }
    }
   
控制台窗口输出的结果：

    root  
    name:CIACs
    authoor:CIACs
    url:http://www.cnblogs.com/zhi-hao/

写入jdom2.xml的内容  

     <?xml version="1.0" encoding="UTF-8"?>
         <root author="CIACs" url="http://www.cnblogs.com/zhi-hao/">
            <name>CIACs</name>
         </root>
 
 
 
**下面是用dom4j来操作xml**

    package xmlTest;
    /**
     * @author CIACs
     */
    import java.io.File;
    import java.io.FileOutputStream;
    import java.util.List;
    
    import javax.xml.parsers.DocumentBuilder;
    import javax.xml.parsers.DocumentBuilderFactory;
    
    import org.dom4j.Attribute;
    import org.dom4j.Document;
    import org.dom4j.DocumentFactory;
    import org.dom4j.DocumentHelper;
    import org.dom4j.Element;
    import org.dom4j.io.DOMReader;
    import org.dom4j.io.OutputFormat;
    import org.dom4j.io.SAXReader;
    import org.dom4j.io.XMLWriter;
    
    public class Dom4j {
        public static void main(String[] args) throws Exception {
            //创建文档跟节点
            Document doc =  DocumentFactory.getInstance().createDocument();
            Element root = DocumentHelper.createElement("root");
            doc.setRootElement(root);
    
            Element name = DocumentHelper.createElement("name");
            name.setText("CIACs");
            name.addAttribute("age", "22");
            root.add(name);
            root.addElement("address").addAttribute("province", "guangdong").addElement("country").setText("guangzhou");
    
            //生成的xml输出到命令行窗口
            OutputFormat format = new OutputFormat("    ",true);
            XMLWriter writer = new XMLWriter(format);
            writer.write(doc);
    
            XMLWriter writer2 = new XMLWriter(new FileOutputStream(new File("dom4j.xml")),format);
            //生成的xml写到内存中
            writer2.write(doc);
    
            System.out.println("-----------通过SAXReader来解析xml文档-----------");
            SAXReader reader = new SAXReader();
            Document doc2 = reader.read(new File("dom4j.xml"));
            Element root2 = doc2.getRootElement();
    
            parse(root2);
    
            System.out.println("---------通过DOMReader来解析xml文档-------------");
    
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            //当一个程序中引用同名不同包的两个Document,其中一个必须要加包名加以区别
            org.w3c.dom.Document doc3 = db.parse(new File("dom4j.xml"));
            DOMReader domReader = new DOMReader();
            //将JAXP的document转换为dom4j的document
            Document doc4 = domReader.read(doc3);
            //获得根元素的名称
            String rootName = doc4.getRootElement().getName();
            System.out.println(rootName);
    
        }
    
        public static void parse(Element element)
        {
            List<Attribute> list = element.attributes();
            if(!list.isEmpty())
            {
            for(Attribute attr:list)
            {
                System.out.println("属性："+attr.getName()+"="+attr.getText());
            }
            }
            List<Element> list2 = element.elements();
            for(int i=0;i<list2.size();i++)
            {
    
                if(list2.get(i).elements().size()>=1)
                {
                System.out.println(list2.get(i).getName());
                parse(list2.get(i));
                }
                else
                {
                    System.out.println(list2.get(i).getName()+": "+list2.get(i).getText());
                }
    
                List<Attribute> list3 = list2.get(i).attributes();
                if(!list3.isEmpty())
                {
                for(Attribute attr:list3)
                {
                    System.out.println("属性："+attr.getName()+"="+attr.getText());
                }
                }
            }
        }
    }
 
输出结果:

    <?xml version="1.0" encoding="UTF-8"?>
            
             <root>
                <name age="22">CIACs</name>
                <address province = "guangdong">
                    <country>guangdong</country>
                </address>
             </root>
     
1、JDOM 
    JDOM的目的是成为 Java 特定文档模型，它简化与 XML 的交互并且比使用 DOM 实现更快。由于是第一个 Java 特定模型，JDOM 一直得到大力推广和促进。正在考虑通过“Java 规范请求 JSR-102”将它最终用作“Java 标准扩展”。从 2000 年初就已经开始了 JDOM 开发。
    JDOM 与 DOM 主要有两方面不同。首先，JDOM 仅使用具体类而不使用接口。这在某些方面简化了 API，但是也限制了灵活性。第二，API 大量使用了 Collections 类，简化了那些已经熟悉这些类的 Java 开发者的使用。
    JDOM 文档声明其目的是“使用 20%（或更少）的精力解决 80%（或更多）Java/XML 问题”（根据学习曲线假定为 20%）。JDOM 对于大多数 Java/XML 应用程序来说当然是有用的，并且大多数开发者发现 API 比 DOM 容易理解得多。JDOM 还包括对程序行为的相当广泛检查以防止用户做任何在 XML 中无意义的事。然而，它仍需要您充分理解 XML 以便做一些超出基本的工作（或者甚至理解某些情况下的错误）。这也许是比学习 DOM 或 JDOM 接口都更有意义的工作。
    JDOM 自身不包含解析器。它通常使用 SAX2 解析器来解析和验证输入 XML 文档（尽管它还可以将以前构造的 DOM 表示作为输入）。它包含一些转换器以将 JDOM 表示输出成 SAX2 事件流、DOM 模型或 XML 文本文档。JDOM 是在 Apache 许可证变体下发布的开放源码。
2、DOM4J 
    虽然 DOM4J 代表了完全独立的开发结果，但最初，它是 JDOM 的一种智能分支。它合并了许多超出基本 XML 文档表示的功能，包括集成的 XPath 支持、XML Schema 支持以及用于大文档或流化文档的基于事件的处理。它还提供了构建文档表示的选项，它通过 DOM4J API 和标准 DOM 接口具有并行访问功能。从 2000 下半年开始，它就一直处于开发之中。
    为支持所有这些功能，DOM4J 使用接口和抽象基本类方法。DOM4J 大量使用了 API 中的 Collections 类，但是在许多情况下，它还提供一些替代方法以允许更好的性能或更直接的编码方法。直接好处是，虽然 DOM4J 付出了更复杂的 API 的代价，但是它提供了比 JDOM 大得多的灵活性。
    在添加灵活性、XPath 集成和对大文档处理的目标时，DOM4J 的目标与 JDOM 是一样的：针对 Java 开发者的易用性和直观操作。它还致力于成为比 JDOM 更完整的解决方案，实现在本质上处理所有 Java/XML 问题的目标。在完成该目标时，它比 JDOM 更少强调防止不正确的应用程序行为。
    DOM4J 是一个非常非常优秀的Java XML API，具有性能优异、功能强大和极端易用使用的特点，同时它也是一个开放源代码的软件。如今你可以看到越来越多的 Java 软件都在使用 DOM4J 来读写 XML，特别值得一提的是连 Sun 的 JAXM 也在用 DOM4J。
3、总述 
    JDOM 在性能测试时表现不佳，在测试 10M 文档时内存溢出。在小文档情况下还值得考虑使用 JDOM。
    总的来说DOM4J是最好的，目前许多开源项目中也大量采用 DOM4J，例如大名鼎鼎的 Hibernate 也用 DOM4J 来读取 XML 配置文件。如果不考虑可移植性，那就采用DOM4J吧！

  
    
[https://www.cnblogs.com/zhi-hao/p/4016363.html]: https://www.cnblogs.com/zhi-hao/p/4016363.html