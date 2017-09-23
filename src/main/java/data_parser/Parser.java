package data_parser;

import java.net.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Parser {
    public static void main(String[] args) throws IOException{
        // create directory to store parsed data
        String cur_dir = System.getProperty("user.dir");
        File dir = new File(cur_dir + "/data");
        dir.mkdir();

        // read entire html page from url and save it to string
        String url = "https://bt.rozetka.com.ua/ua/washing_machines/c80124/filter/";
        String html = readHtmlPage(url);

        // create parser object
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[class*=paginator-catalog-l-link]");
        int len = links.size();
        int num = Integer.parseInt(links.get(len-1).text());
        for (int i=0; i<num; i++){
            String pg = url + "page=" + Integer.toString(i + 1) + "/";
            parseCategoryPage(pg);
        }

        //int pages_num = links[-1].;


    }

    public static String readHtmlPage(String url) throws IOException{
        URL site = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(site.openStream()));

        String inputLine, content = "";
        while ((inputLine = in.readLine()) != null)
            content += inputLine;
        in.close();

        return content;
    }

    public static void parseCategoryPage(String url)throws IOException{
        String html = readHtmlPage(url);
        Document doc = Jsoup.parse(html);

        Elements products = doc.select("div[class*=g-i-tile-i-title]");
        int size = products.size();
        for (int i=0;i<size;i++){
            String linkHref = products.get(i).select("a").first().attr("href");
            parseReviews(linkHref + "comments/");
        }
    }

    public static void parseReviews(String url){
        String html = readHtmlPage(url);
        Document doc = Jsoup.parse(html);


    }
}
