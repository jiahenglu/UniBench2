package Preprocessing.LDBC;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class generateTagMatrix {
    public static void main(String[] args) throws Exception {
        // <brand> <product> <p> <asin> <price?>

        var rdf = generatePopularTagByCountry.class.getResourceAsStream("/company_product_asin_ranking.tsv");

        var path = "./output/tagMatrix.txt";

        var zipf = new ZipfDistribution(50, 1.5);
        int prev_company = -1;
        int prev_counter = 0;

        var rand = new LogNormalDistribution(0, 0.5);

        var reader = new BufferedReader(new InputStreamReader(rdf));
        reader.readLine(); // skip header

        var writer = new PrintWriter(path);

        var line = "";
        while ((line = reader.readLine()) != null) {
            var segs = line.split("\\t");
            if (segs.length != 6)
                continue;

            int company = Integer.parseInt(segs[3]);
            int product = Integer.parseInt(segs[4]);
            String asin = segs[2].substring(1, segs[2].length() - 1);

            if (company != prev_company) {
                prev_company = company;
                prev_counter = 0;
            }
            double p = zipf.cumulativeProbability(++prev_counter);

            double price = rand.sample() * 50;

            writer.println(String.format("%d %d %1.10f %s %5.2f", company, product, p, asin, price));
        }

        writer.close();
        reader.close();
    }
}
