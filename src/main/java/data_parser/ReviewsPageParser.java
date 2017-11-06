package data_parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tuple.MutableTuple;

import java.io.IOException;
import java.util.ArrayList;

public class ReviewsPageParser extends PageParser{
    private String starQuery;
    private String reviewQuery;
    private String textQuery;

    public ReviewsPageParser(String u) throws IOException{
        super(u, "article[class*=pp-review-i]");
        this.starQuery = "span[class*=g-rating-stars-i]";
        this.reviewQuery = "div[class*=pp-review-text]";
        this.textQuery = "div[class*=pp-review-text-i]";
    }

    public MutableTuple parse(){return new MutableTuple();}

    public ArrayList parseComments() throws IOException{
        Elements reviews = this.document.select(this.query);
        ArrayList<MutableTuple> comments = new ArrayList<>();

        int reviewsNumber = reviews.size();
        for (int i = 0; i < reviewsNumber; i++) {
            Element review = reviews.get(i);
            Element star = review.select(this.starQuery).first();
            Element text = review.select(this.reviewQuery).first();

            if (star != null) {
                Elements texts = text.select(this.textQuery);
                Integer commentStar = Integer.parseInt(star.attr("content"));
                String commentText = texts.get(0).text();
                comments.add(new MutableTuple(commentText, commentStar));
            }
        }
        return comments;
    }
}
