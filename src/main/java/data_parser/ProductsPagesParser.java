package data_parser;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import tuple.MutableTuple;

public class ProductsPagesParser extends PageParser {
    private static String pageQuery = "div[class*=g-i-tile-i-title]";

    public ProductsPagesParser() throws IOException {
        super("https://bt.rozetka.com.ua/ua/washing_machines/c80124/filter/", "a[class*=paginator-catalog-l-link]");
    }

    public MutableTuple parse() throws IOException{
        int recursionCounter = 0;
        MutableTuple mostReviewed = helper(this.url, this.query, recursionCounter);
        return mostReviewed;
    }

    private MutableTuple helper(String url, String query, int recursionCounter) throws IOException{
        Elements links = this.document.select(query);
        int elementsNumber;
        if (recursionCounter == 0){
            int linksNumber = links.size();
            elementsNumber = Integer.parseInt(links.get(linksNumber-1).text());
        }
        else{
            elementsNumber = links.size();
        }

        int mostReviewedReviewsCount = 0;
        MutableTuple mostReviewed = new MutableTuple();
        MutableTuple current = new MutableTuple();
        for (int i=0; i<elementsNumber; i++){
            String Url;
            if (recursionCounter == 0){
                Url = this.url + "page=" + Integer.toString(i + 1) + "/";
                mostReviewed = helper(Url, pageQuery, 1);
            }
            else{
                Url = links.get(i).select("a").first().attr("href") + "comments/";
                ReviewsPagesParser reviewsPagesParser = new ReviewsPagesParser(Url);
                current = reviewsPagesParser.parse();
            }
            if ((Integer)current.second > mostReviewedReviewsCount){
                mostReviewedReviewsCount = (Integer) current.second;
                mostReviewed = current;
            }
        }
        return mostReviewed;
    }

    public void generateReport(MutableTuple mostReviewed) throws IOException{
        System.out.println(String.format("Most reviewed product is %s with %d reviews", mostReviewed.first, (Integer)mostReviewed.second));
    }
}
