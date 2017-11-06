package data_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tuple.MutableTuple;

import java.io.IOException;
import java.util.ArrayList;

public abstract class PageParser {
    protected String url;
    private String html;
    protected Document document;
    protected String query;

    public PageParser(String u, String q) throws IOException {
        this.url = u;
        HtmlPageReader htmlPageReader = new HtmlPageReader(url);
        this.html = htmlPageReader.read();
        this.document = Jsoup.parse(this.html);
        this.query = q;
    }

    public abstract MutableTuple parse() throws IOException;
}

