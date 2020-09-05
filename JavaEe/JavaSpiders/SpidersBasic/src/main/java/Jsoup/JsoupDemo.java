package Jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by ZJ on 2020/8/28
 * comment:
 */
public class JsoupDemo {

    @Test
    public void testJsonUrl() throws Exception{
        //解析url地址
        Document document = Jsoup.parse(new URL("http://www.itcast.cn/"), 1000);

        //获取title的内容
        Element title = document.getElementsByTag("title").first();

        System.out.println(title.text());
    }

    @Test
    public void testJsoupString() throws Exception {
        //读取文件获取
        String html = FileUtils.readFileToString(new File("D:\\Maven\\Studys\\JavaEe\\JavaSpiders\\SpidersBasic\\src\\main\\java\\Jsoup\\jsoup.html"), "UTF-8");

        //    解析字符串
        Document document = Jsoup.parse(html);

        //获取title的内容
        Element title = document.getElementsByTag("title").first();
        System.out.println(title.text());

    }

    @Test
    public void testJsoupHtml() throws Exception {
        //    解析文件
        Document document = Jsoup.parse(new File("D:\\Maven\\Studys\\JavaEe\\JavaSpiders\\SpidersBasic\\src\\main\\java\\Jsoup\\jsoup.html"),"UTF-8");

        //获取title的内容
        Element title = document.getElementsByTag("title").first();
        System.out.println(title.text());
    }

    @Test
    public void testDom() throws IOException {
        //    解析文件
        Document document = Jsoup.parse(new File("D:\\Maven\\Studys\\JavaEe\\JavaSpiders\\SpidersBasic\\src\\main\\java\\Jsoup\\jsoup.html"),"UTF-8");

        //1.    根据id查询元素getElementById
        Element element = document.getElementById("city_bj");

        //2.   根据标签获取元素getElementsByTag
        element = document.getElementsByTag("title").first();

        //3.   根据class获取元素getElementsByClass
        element = document.getElementsByClass("s_name").last();

        //4.   根据属性获取元素getElementsByAttribute
        element = document.getElementsByAttribute("abc").first();
        element = document.getElementsByAttributeValue("class", "city_con").first();

        //获取元素
        Element element1 = document.getElementById("test");

        //1.   从元素中获取id
        String str = element1.id();

        //2.   从元素中获取className
        str = element1.className();

        //3.   从元素中获取属性的值attr
        str = element1.attr("id");

        //4.   从元素中获取所有属性attributes
        str = element1.attributes().toString();

        //5.   从元素中获取文本内容text
        str = element1.text();
    }
}
