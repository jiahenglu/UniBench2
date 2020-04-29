package Preprocessing;

import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TagRankingGenerator {
    public static void main(String[] args) throws Exception {
        var mapping = readRankings();
        System.out.println(mapping.size() + " mappings");

        var builder = new ModelBuilder();

        var model = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product_qid.ttl"), "", RDFFormat.TURTLE);
        model.forEach(triple -> {
            if (!triple.getPredicate().stringValue().equals("http://schema.org/about"))
                return;

            var qid = Integer.parseInt(triple.getObject().stringValue().substring(32)); // http://www.wikidata.org/entity/Q6668180
            if (!mapping.containsKey(qid)) {
                System.out.println("Missing rank for Q" + qid);
                return;
            }

            builder.subject(triple.getSubject()).add("http://schema.org/ratingValue", mapping.get(qid));
        });

        new File("./output/").mkdir();
        Rio.write(builder.build(), new FileWriter("./output/tagRankings.ttl"), RDFFormat.TURTLE);
    }

    private static Map<Integer, Double> readRankings() throws IOException {
        var ranks = new HashMap<Integer, Double>();

        var reader = new BufferedReader(
                new InputStreamReader(TagRankingGenerator.class.getResourceAsStream("/2020-04-16.allwiki.links.rank")));
        reader.lines().forEach(l -> {
            var seg = l.split("\\t");
            ranks.put(Integer.parseInt(seg[0]), Double.parseDouble(seg[1]));
        });
        reader.close();

        return ranks;
    }
}
