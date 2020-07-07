package Preprocessing.Unibench;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Preprocessing.Helper;
import Preprocessing.LDBC.generatePopularTagByCountry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.javatuples.Pair;

public class generateMetadataProduct {
    public static void main(String[] args) throws Exception {
        // asin|productId|brand|title|price|categories|feedback|imgUrl|description

        var path = "./output/metadata_product.csv";

        var rdf = generatePopularTagByCountry.class.getResourceAsStream("/company_product_asin_price_ranking.tsv");

        var reader = new BufferedReader(new InputStreamReader(rdf));
        reader.readLine(); // skip header

        var writer = new CSVPrinter(new FileWriter(path), CSVFormat.DEFAULT
                .withDelimiter('|')
                .withHeader("asin", "productId", "brand", "title", "price", "categories", "feedback", "imgUrl", "description"));

        var line = "";
        while ((line = reader.readLine()) != null) {
            var segs = line.split("\\t");
            if (segs.length != 7)
                continue;

            String product = Helper.getTitle(Helper.getUriLast(segs[1].substring(1, segs[1].length() - 1)));
            String asin = segs[2].substring(1, segs[2].length() - 1);
            int brand = Integer.parseInt(segs[3]);
            int productId = Integer.parseInt(segs[4]);
            double price = Double.parseDouble(segs[5]);

            var feedback = "[" + generateFeedbacks().stream()
                    .map(fb -> String.format("'%d.0,%s'", fb.getValue0(), fb.getValue1()))
                    .collect(Collectors.joining(", ")) + "]";

            writer.printRecord(asin, productId, brand, product, String.format("%5.2f", price), "[['category']]",
                    feedback, "http://example.com/img.jpg", "description");
        }

        writer.close();
    }

    static IntegerDistribution randU = new UniformIntegerDistribution(5, 20);
    static IntegerDistribution randZ = new ZipfDistribution(5, 2);

    private static List<Pair<Integer, String>> generateFeedbacks() {
        int num = randU.sample();

        var result = new ArrayList<Pair<Integer, String>>(num);
        for (int i = 0; i < num; i++) {
            result.add(new Pair<>(6 - randZ.sample(), "feedback"));
        }

        return result;
    }
}
