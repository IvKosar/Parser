package data_parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HtmlPageReader {
    private String url;

    public HtmlPageReader(String url){
        this.url = url;
    }
    public String read() throws IOException {
        URL site = new URL(this.url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(site.openStream()));

        String inputLine, content = "";
        while ((inputLine = in.readLine()) != null)
            content += inputLine;
        in.close();

        return content;
    }
}
