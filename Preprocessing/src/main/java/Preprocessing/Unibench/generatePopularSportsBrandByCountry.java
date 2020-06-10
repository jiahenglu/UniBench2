package Preprocessing.Unibench;

import Preprocessing.Helper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class generatePopularSportsBrandByCountry {

    public static void main(String[] args) throws Exception {
        // 0,Signia_(sportswear),Argentina,0.5,Sports

        var path = "./output/PopularSportsBrandByCountry.csv";

        // read company types
        var types = generatePopularSportsBrandByCountry.class.getResourceAsStream("/company_type.tsv");
        var type_map = new HashMap<String, Set<String>>();
        {
            var reader = new BufferedReader(new InputStreamReader(types));
            reader.readLine(); // skip header

            var line = "";
            while ((line = reader.readLine()) != null) {
                var segs = line.split("\\t");
                if (segs.length != 2)
                    continue;

                String company = Helper.getUriLast(segs[0].substring(1, segs[0].length() - 1));
                String type = Helper.getUriLast(segs[1].substring(1, segs[1].length() - 1));

                type_map.putIfAbsent(company, new HashSet<>());
                type_map.get(company).add(type);
            }
            reader.close();
        }

        //

        var rdf = generatePopularSportsBrandByCountry.class.getResourceAsStream("/location_company_ranking.tsv");

        var records = new HashMap<Integer, Map<Integer, Double>>();

        var country_map = new HashMap<Integer, String>();
        var company_map = new HashMap<Integer, String>();
        {
            var reader = new BufferedReader(new InputStreamReader(rdf));
            reader.readLine(); // skip header

            var line = "";
            while ((line = reader.readLine()) != null) {
                var segs = line.split("\\t");
                if (segs.length != 5)
                    continue;

                int country_id = Integer.parseInt(segs[2]);
                int company_id = Integer.parseInt(segs[3]);
                double ranking = Double.parseDouble(segs[4]);

                String country = Helper.getUriLast(segs[0].substring(1, segs[0].length() - 1));
                String company = Helper.getUriLast(segs[1].substring(1, segs[1].length() - 1));
                country_map.putIfAbsent(country_id, country);
                company_map.putIfAbsent(company_id, company);

                records.computeIfAbsent(country_id, f -> new HashMap<>());
                records.get(country_id).put(company_id, ranking);
            }
            reader.close();
        }

        var sorted = sort(records);

        replaceRankingByP(sorted);

        var writer = new CSVPrinter(new PrintWriter(path), CSVFormat.DEFAULT);
        sorted.forEach(item -> {
            String country = country_map.get(item.getValue0());
            item.getValue1().forEach(tag_p -> {
                int company_id = tag_p.getValue0();
                String company = company_map.get(tag_p.getValue0());
                double p = tag_p.getValue1();

                try {
                    for (var type : type_map.get(company)) {
                        writer.printRecord(company_id, company, country, p, type);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        writer.close();
    }

    private static List<Pair<Integer, List<Pair<Integer, Double>>>> sort(Map<Integer, Map<Integer, Double>> map) {
        var result = new ArrayList<Pair<Integer, List<Pair<Integer, Double>>>>();

        map.forEach((country, tags) -> {
            var crt = new ArrayList<Pair<Integer, Double>>();

            tags.forEach((tag, rank) -> crt.add(new Pair<>(tag, rank)));

            crt.sort((a, b) -> {
                if (a.getValue1().equals(b.getValue1())) return 0;
                return a.getValue1() > b.getValue1() ? -1 : 1;
            });

            result.add(new Pair<>(country, crt));
        });

        result.sort(Comparator.comparingInt(Pair::getValue0));

        return result;
    }

    private static final IntegerDistribution zipf = new ZipfDistribution(50, 1.5);

    private static void replaceRankingByP(List<Pair<Integer, List<Pair<Integer, Double>>>> sorted) {
        sorted.forEach(part -> {
            for (int i = 0; i < part.getValue1().size(); i++) {
                var pair = part.getValue1().get(i).setAt1(zipf.cumulativeProbability(i + 1));
                part.getValue1().set(i, pair);
            }
        });
    }
}
