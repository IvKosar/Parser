package data_parser;

import java.net.*;
import java.io.*;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Parser {
    private static String currentDirectory = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException{
        // create directory to store parsed data
        File dir = new File(Parser.currentDirectory + "/data");
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

    public static void parseReviews(String url) throws IOException{
        String html = readHtmlPage(url);
        Document doc = Jsoup.parse(html);

        Elements nums = doc.select("a[class*=paginator-catalog-l-link]");
        int size = nums.size();
        int commentsCount = (size > 0) ? Integer.parseInt(nums.get(size-1).text()) : 0;

        ArrayList<ArrayList> comments = new ArrayList<ArrayList>();
        for (int i=0; i<commentsCount; i++){
            String pg = url + "page=" + Integer.toString(i+1) + "/";
            ArrayList<ArrayList> comments_part = parseReviewsPage(pg);
            int comments_part_size = comments_part.size();
            for (int j=0; j<comments_part_size; j++){
                comments.add(comments_part.get(j)); // TODO
                }
            }

        // write comments to file
        String filename = "data/" + url.split("/")[4] + ".csv";
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        for (int i=0; i<commentsCount; i++){
            ArrayList<String> comment = comments.get(i); // TODO
            String star = comment.get(0); // TODO
            String text = comment.get(1); // TODO
            writer.println(star + ";" + text);
        }
        writer.close();

        System.out.println(Integer.toString(commentsCount) + " reviews from " + url);
    }

        public static ArrayList parseReviewsPage(String url) throws IOException {
            String html = readHtmlPage(url);
            Document doc = Jsoup.parse(html);
            Elements reviews = doc.select("article[class*=pp-review-i]");
            ArrayList<ArrayList> comments = new ArrayList<ArrayList>();

            int reviewsLen = reviews.size();
            for (int i = 0; i < reviewsLen; i++) {
                Element review = reviews.get(i);
                Element star = review.select("span[class*=g-rating-stars-i]").first();
                Element text = review.select("div[class*=pp-review-text]").first();

                if (star != null) {
                    Elements texts = text.select("div[class*=pp-review-text-i]");
                    String commentStar = star.attr("content");
                    String commentText = texts.get(0).text();
                    ArrayList<String> comment = new ArrayList<String>();
                    comment.add(commentStar);
                    comment.add(commentText);
                    comments.add(comment);
                }
            }
            return comments;
        }
    }
