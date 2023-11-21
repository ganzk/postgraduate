package com.track.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    // 读取网页小说，本地执行git命令，将读取的网页小说推送到git上
    public static void main(String[] args) {

        String url = "https://www.plxs.co/book/"; // 小说网页的URL

//        String title = "221045/74917026.html"; // 糖糖
//        String title = "51560/13761662.html"; // 糖糖
        String title = "56239/15912102.html"; // 糖糖

        String title1 = "146397/53690452.html"; // 小茵
        String title2 = "56765/16127470.html"; // 小依

        String title3 = "225164/74606432.html"; // 我的女友
        String title4 = "150878/53822381.html"; // 我的女友
        String title5 = "15447/4920031.html"; // 我的女友

//        testXY(url, title3);

        List<String> bookIds = new ArrayList<>();
        bookIds.add(title);
        bookIds.add(title1);
        bookIds.add(title2);
//        for (String bookId : bookIds){
//            testXY(url, bookId);
//        }


        // m.peblkbbbpd.com/book/25717808/10987846   7778888
        String url1 = "https://m.peblkbbbpd.com/book/"; // 小说网页的URL
        String title_url1 = "25717808/10987846.html"; // 我的女友
        testXY(url1, title_url1);
    }

    public static void test(String url) throws IOException {
        //小说第一章URL,因版权问题，url屏蔽，读者可自行选择
//        String url = "https://www.88yydstxt178.com/12/12372/248375.html";
//        String url = "http://localhost:8090/";
        Document document = Jsoup.connect(url).get();
        Elements bookName = document.select("#j_textWrap .read-container .book-cover-wrap h1");
        //建立文件输出流，保存文本文件，以小说名作为文件名
        FileWriter fw = new FileWriter("d:/test/"+bookName.text()+".txt");
        //通过HttpClients工具类创建一个httpClient对象，该用来发起HTTP请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        while(true) {
            //创建一个“HTTP请求方法为Get”的一个对象
            HttpGet httpGet = new HttpGet(url);
            //通过httpClient发其Http请求，得到Http响应
            CloseableHttpResponse response = httpClient.execute(httpGet);
            //处理响应
            //如果响应状态码是200，表示访问的网页是正常的
            if (response.getStatusLine().getStatusCode() == 200) {
                //拿到响应内容
                HttpEntity entity = response.getEntity();
                //将内容转换成String
                String html = EntityUtils.toString(entity, "UTF-8");
                //由Jsoup解析由HttpClient获取的HTML文档
                document = Jsoup.parse(html);
                //Jsoup通过id、class、标签，解析HTML文档，拿到我们业务上想要的数据
                Elements title = document.select("#j_chapterBox .text-wrap .main-text-wrap .text-head h3 .content-wrap");
                Elements content = document.select("#j_chapterBox .text-wrap .main-text-wrap .read-content p span");

                //解析“下一章”按钮，得到下一章的URL
                Elements next = document.select("#j_chapterNext");
                String href = next.attr("href");
                String prefix = "https:";
                url = prefix + href;
                //如果爬取不到内容，说明本书已爬完最后一章，结束
                if(url.equals("https:")){
                    break;
                }

                fw.write(title.text()+"\r\n");
                for (Element e : content) {
                    fw.write(e.text()+"\r\n");
                }
            }
        }
        fw.close();
    }

    public static void test1(String url) throws IOException {
        //小说第一章URL,因版权问题，url屏蔽，读者可自行选择
        Connection connect = Jsoup.connect(url);
        Document document = connect.get();
        //经F12分析，章节的标题、内容组合选择器如下所示
        Elements title = document.select("#j_chapterBox .text-wrap  .main-text-wrap .text-head h3 .content-wrap");
        Elements content = document.select("#j_chapterBox .text-wrap  .main-text-wrap .read-content p span");
        //利用IO流将小说章节内容保存到本地
        FileWriter fw = new FileWriter("d:/test/abc.txt");
        fw.write(title.text() + "\r\n");
        for (Element e : content) {
            fw.write(e.text() + "\r\n");
        }
        fw.close();
    }

    public static void test2(String url) {

        try {
            // 发起HTTP请求并获取网页内容
            Document doc = Jsoup.connect(url).get();

            // 使用CSS选择器获取小说内容所在的HTML元素
            Elements contentElements = doc.select("#md-header-anchor");
            Elements contentElements1 = doc.select(".is-node");


            // 遍历内容元素并输出文本内容
            for (Element element : contentElements1) {
                Elements p = element.select("p");
                for(Element element1 : p){
                    System.out.println(element1.text());
                }
//                System.out.println(p.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testXY(String url, String title){

        try {

            // https://101.qq.com/#/hero-detail?heroid=2
            // 发起HTTP请求并获取网页内容
            Document doc = Jsoup.connect(url + title).get();

            System.out.println(url + title);

            // 使用CSS选择器获取小说内容所在的HTML元素
            Elements contentElements = doc.select(".panel-body");

            Elements bookName = doc.select("title");
            String[] s1 = bookName.text().split("_");
            String bookStr = "d:/test/"+ s1[1] +".txt";
            File file=new File(bookStr);
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //建立文件输出流，保存文本文件，以小说名作为文件名
            FileWriter fw = new FileWriter(bookStr, true);


            // 遍历内容元素并输出文本内容
            for (Element element : contentElements) {
                List<Node> nodes = element.childNodes();
                for (Node node : nodes){
                    String s = node.outerHtml();
                    if(s.equals("<br>")){

                    } else if (s.lastIndexOf("text-danger") > 0){

                    }else {
                        fw.write(s.replace("&nbsp;", "").replace("... --&gt;&gt;","")+"\r\n");
                    }
                }
            }

            // https://www.plxs.co/book/221045/74917135_2.html
            fw.close();

            //解析“下一章”按钮，得到下一章的URL
            String nextUrl = doc.select("#linkNext").attr("href");
            if(nextUrl.lastIndexOf("https:") > 0){
                return;
            }
            System.out.println(url + title.split("/")[0] + "/" + nextUrl);
            nextUrl = title.split("/")[0] + "/" + nextUrl;
            testXY(url, nextUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
