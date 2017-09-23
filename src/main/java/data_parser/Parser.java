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
        System.out.println(links);
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
}
