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
        Elements links = this.document.select(this.query);
        int linksNumber = links.size();
        int pagesNumber = Integer.parseInt(links.get(linksNumber-1).text());

        int mostReviewedReviewsCount = 0;
        MutableTuple mostReviewed = new MutableTuple();
        MutableTuple current = new MutableTuple();
        for (int i=0; i<pagesNumber; i++){
            String pageUrl = this.url + "page=" + Integer.toString(i + 1) + "/";
            ProductsPageParser categoryPageParser = new ProductsPageParser(pageUrl);
            current = categoryPageParser.parse();
            if ((Integer)current.second > mostReviewedReviewsCount){
                mostReviewedReviewsCount = (Integer) current.second;
                mostReviewed = current;
            }
        }
        return mostReviewed;
    }

    public void generateReport(MutableTuple mostReviewed) throws IOException{
        System.out.println(String.format("Most reviewed product: %s with %d reviews", mostReviewed.first, (Integer)mostReviewed.second));
    }
}
