package Preprocessing.LDBC;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class generatePopularTagByCountry {
    public static void main(String[] args) throws Exception {
        // <country> <company> <p> <class?>

        var rdf = generatePopularTagByCountry.class.getResourceAsStream("/location_company_ranking.tsv");

        var path = "./output/popularTagByCountry.txt";

        var records = new HashMap<Integer, Map<Integer, Double>>();

        var reader = new BufferedReader(new InputStreamReader(rdf));
        reader.readLine(); // skip header

        var line = "";
        while ((line = reader.readLine()) != null) {
            var segs = line.split("\\t");
            if (segs.length != 5)
                continue;

            int country = Integer.parseInt(segs[2]);
            int company = Integer.parseInt(segs[3]);
            double ranking = Double.parseDouble(segs[4]);

            records.computeIfAbsent(country, f -> new HashMap<>());
            records.get(country).put(company, ranking);
        }

        var sorted = sort(records);

        replaceRankingByP(sorted);

        var writer = new PrintWriter(path);
        sorted.forEach(item -> {
            int country = item.getValue0();
            item.getValue1().forEach(tag_p -> {
                int tag = tag_p.getValue0();
                double p = tag_p.getValue1();

                writer.println(String.format("%d %d %1.10f", country, tag, p));
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
