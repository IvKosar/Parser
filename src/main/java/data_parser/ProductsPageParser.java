package data_parser;

import org.jsoup.select.Elements;
import tuple.MutableTuple;

import java.io.IOException;
import java.util.ArrayList;

public class ProductsPageParser extends PageParser{
    public ProductsPageParser(String u) throws IOException{
        super(u, "div[class*=g-i-tile-i-title]");
    }

    public MutableTuple parse() throws IOException {
        Elements products = document.select(this.query);
        int size = products.size();

        int mostReviewedReviewsCount = 0;
        MutableTuple mostReviewed = new MutableTuple();
        MutableTuple current = new MutableTuple();
        for (int i=0;i<size;i++){
            String reviewsUrl = products.get(i).select("a").first().attr("href") + "comments/";
            ReviewsPagesParser reviewsPagesParser = new ReviewsPagesParser(reviewsUrl);
            current = reviewsPagesParser.parse();
            if ((Integer)current.second > mostReviewedReviewsCount) {
                mostReviewedReviewsCount = (Integer)current.second;
                mostReviewed = current;
            }
        }
        return mostReviewed;
    }
}
