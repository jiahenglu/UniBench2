package Preprocessing.LDBC;

import Preprocessing.Helper;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.javatuples.Pair;

import java.io.*;
import java.util.*;

public class generatePopularTagByCountry {
    public static void main(String[] args) throws Exception {
        // 59 8106 1.00631

        var countryIds = readDicLocations(generatePopularTagByCountry.class.getResourceAsStream("/dicLocations.txt"));
        var tagIds = Rio.parse(generatePopularTagByCountry.class.getResourceAsStream("/product_id.ttl"), "", RDFFormat.TURTLE);
        var tagRankings = Rio.parse(generatePopularTagByCountry.class.getResourceAsStream("/product_ranking.ttl"), "", RDFFormat.TURTLE);
        var tagCountries = Rio.parse(generatePopularTagByCountry.class.getResourceAsStream("/product_location.ttl"), "", RDFFormat.TURTLE);

        var path = "./output/popularTagByCountry.txt";

        var collected = new HashMap<Integer, Map<Integer, Double>>(); // country_id: {[tag_id, ranking]}

        tagCountries.forEach(statement -> {
            var tag = statement.getSubject();
            var country = statement.getObject();

            var ranking_iter = tagRankings.getStatements(tag, null, null).iterator();
            if (!ranking_iter.hasNext())
                return; // this product has no matching QID thus no ranking

            double tag_ranking = Double.parseDouble(ranking_iter.next().getObject().stringValue());

            int tag_id = Integer.parseInt(
                    tagIds.getStatements(tag, null, null).iterator().next().getObject().stringValue());
            int country_id = countryIds.get(Helper.getUriLast(country.stringValue()));

            collected.computeIfAbsent(country_id, f -> new HashMap<>());

            collected.get(country_id).put(tag_id, tag_ranking);
        });

        var sorted = sort(collected); // [country, [tag, ranking]]
        replaceRankingByP(sorted);

        var writer = new PrintWriter(path);
        sorted.forEach(item -> {
            int country = item.getValue0();
            item.getValue1().forEach(tag_p -> {
                int tag = tag_p.getValue0();
                double p = tag_p.getValue1();

                writer.println(String.format("%d %d %f", country, tag, p));
            });
        });
        writer.close();
    }

    private static void replaceRankingByP(List<Pair<Integer, List<Pair<Integer, Double>>>> sorted) {
        sorted.forEach(part -> {
            var zipf = new Zipf(part.getValue1().size(), 1.5d);
            var p = 0d;

            for (int i = 0; i < part.getValue1().size(); i++) {
                p += zipf.pmf(i + 1);

                var pair = part.getValue1().get(i).setAt1(p);
                part.getValue1().set(i, pair);
            }
        });
    }

    // [country, [tag, ranking]]
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

    private static Map<String, Integer> readDicLocations(InputStream resource) throws IOException {
        var map = new HashMap<String, Integer>();

        var br = new BufferedReader(new InputStreamReader(resource));

        String line;
        int id = 0;

        while ((line = br.readLine()) != null)
            map.put(line.split(" ")[1], id++);

        return map;
    }
}
