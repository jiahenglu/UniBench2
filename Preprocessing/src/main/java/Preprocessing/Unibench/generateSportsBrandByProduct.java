package Preprocessing.Unibench;

import Preprocessing.Helper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class generateSportsBrandByProduct {
    public static void main(String[] args) throws Exception {
        // Signia_(sportswear),B00IQANCRO

        var path = "./output/SportsBrandByProduct.csv";

        var rdf = generateSportsBrandByProduct.class.getResourceAsStream("/company_product_asin_ranking.tsv");

        var reader = new BufferedReader(new InputStreamReader(rdf));
        reader.readLine(); // skip header

        var csv = new CSVPrinter(new PrintWriter(path), CSVFormat.DEFAULT);

        var line = "";
        while ((line = reader.readLine()) != null) {
            var segs = line.split("\\t");
            if (segs.length != 6)
                continue;

            String company = Helper.getUriLast(segs[0].substring(1, segs[0].length() - 1));
            String asin = segs[2].substring(1, segs[2].length() - 1);

            csv.printRecord(company, asin);
        }

        csv.close();
        reader.close();
    }
}
