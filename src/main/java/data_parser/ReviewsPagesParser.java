package data_parser;

import org.jsoup.select.Elements;
import tuple.MutableTuple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ReviewsPagesParser extends PageParser{
    private class ArrayListToFileWriter{
        public String filename;
        private PrintWriter writer;

        public ArrayListToFileWriter(String f) throws FileNotFoundException, UnsupportedEncodingException {
            filename = f;
            writer = new PrintWriter(filename, "UTF-8");
        }

        public void write(ArrayList data){
            int commentsPartSize = data.size();
            for (int j=0; j<commentsPartSize; j++){
                MutableTuple comment = (MutableTuple)data.get(j);
                writer.println(Integer.toString((Integer)comment.second) + ";" + comment.first);
            }
        }

        public void close(){ writer.close(); }
    }

    public ReviewsPagesParser(String u) throws IOException{
        super(u, "a[class*=paginator-catalog-l-link]");
    }

    public MutableTuple parse() throws IOException{
        Elements reviews = document.select(this.query);
        int size = reviews.size();
        int reviewsNumber = (size > 0) ? Integer.parseInt(reviews.get(size-1).text()) : 0;

        String filename = "data/" + url.split("/")[4] + ".csv";
        ArrayListToFileWriter writer = new ArrayListToFileWriter(filename);
        ArrayList<ArrayList> comments = new ArrayList<ArrayList>();
        for (int i=0; i<reviewsNumber; i++){
            String reviewsPageUrl = this.url + "page=" + Integer.toString(i+1) + "/";
            ReviewsPageParser reviewsPageParser = new ReviewsPageParser(reviewsPageUrl);
            ArrayList<MutableTuple> commentsPart = reviewsPageParser.parseComments();
            writer.write(commentsPart);
        }
        writer.close();

        System.out.println(Integer.toString(reviewsNumber) + " reviews from " + url);

        return new MutableTuple(this.url, reviewsNumber);
    }
}
