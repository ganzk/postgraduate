package com.track.book;

import com.alibaba.fastjson.JSON;
import com.track.book.domain.Book;
import com.track.book.domain.Crawler;
import com.track.until.ConfUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BookCrawler {

    private static String PATH = "d";
//    private static String PATH = "e";

    ////        String title = "221045/74917026.html"; // 糖糖
    ////        String title = "51560/13761662.html"; // 糖糖
    //        String title = "56239/15912102.html"; // 糖糖
    // 读取网页小说，本地执行git命令，将读取的网页小说推送到git上
    public static void main(String[] args) {

        String date = ConfUtil.readConfigFile("date");
        List<Crawler> crawlers = JSON.parseArray(String.valueOf(JSON.parseObject(date).get("date")), Crawler.class);

        for (Crawler crawler : crawlers){
            if("盘龙".equals(crawler.getName())){
                for (Book book : crawler.getBooks()){
                    System.out.println("盘龙：" + crawler.getUrl() + book.getUrl());
//                    testPanLong(crawler.getUrl(), book.getUrl());
                }
            } else if("笔趣阁".equals(crawler.getName())){
                for (Book book : crawler.getBooks()){
                    System.out.println("笔趣阁：" + crawler.getUrl() + book.getUrl());
//                    testBQG(crawler.getUrl(), book.getUrl());
                }
            }

        }
    }

    // 盘龙
    public static void testPanLong(String url, String title){

        try {

            // https://101.qq.com/#/hero-detail?heroid=2
            // 发起HTTP请求并获取网页内容
            Document doc = Jsoup.connect(url + title).get();

            System.out.println(url + title);

            // 使用CSS选择器获取小说内容所在的HTML元素
            Elements contentElements = doc.select(".panel-body");

            Elements bookName = doc.select("title");
            String[] s1 = bookName.text().split("_");
            String bookStr = PATH + ":/test/"+ s1[1] +".txt";
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
            testPanLong(url, nextUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // 笔趣阁
    public static void testBQG(String url, String title){

        try {

            // https://101.qq.com/#/hero-detail?heroid=2
            // 发起HTTP请求并获取网页内容
            Document doc = Jsoup.connect(url + title).get();

            System.out.println(url + title);

            // 使用CSS选择器获取小说内容所在的HTML元素
            Elements contentElements = doc.select("#booktxt");

            Elements bookName = doc.select("title");
            String[] s1 = bookName.text().split("_");
            String bookStr = "e:/test/"+ s1[0] +".txt";
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
                        fw.write(s.replace("&nbsp;", "")
                                .replace("... --&gt;&gt;","")
                                .replace("<p>","")
                                .replace("</p>","")
                                +"\r\n");
                    }
                }
            }

            // https://www.plxs.co/book/221045/74917135_2.html
            fw.close();

            //解析“下一章”按钮，得到下一章的URL
            String nextUrl = doc.select("#next_url").attr("href");
            if("".equals(nextUrl)){
                return;
            }
            System.out.println(url + nextUrl);
            testBQG(url, nextUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
