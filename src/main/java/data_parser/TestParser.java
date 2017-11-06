package data_parser;

import tuple.MutableTuple;

import java.io.IOException;
import java.util.ArrayList;

public class TestParser {
    public static void main(String[] args) throws IOException {
        ProductsPagesParser productsPagesParser = new ProductsPagesParser();
        MutableTuple mostReviewed = productsPagesParser.parse();
        productsPagesParser.generateReport(mostReviewed);
    }
}
