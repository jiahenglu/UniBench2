package Preprocessing;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TagIdGenerator {
    public static void main(String[] args) throws Exception {
        var tags = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product.ttl"), "", RDFFormat.TURTLE);
        var classes = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product_type.ttl"), "", RDFFormat.TURTLE);
        var parent_classes = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product_type_tree.ttl"), "", RDFFormat.TURTLE);

        var tags_id = assignIds(tags.subjects().stream().map(Resource::stringValue).collect(Collectors.toSet()));
        var companies_id = assignIds(tags.objects().stream().map(Value::stringValue).collect(Collectors.toSet()));

        var all_classes = new HashSet<>(classes.objects());
        all_classes.addAll(parent_classes.subjects());
        all_classes.addAll(parent_classes.objects());
        var classes_id = assignIds(all_classes.stream().map(Value::stringValue).collect(Collectors.toSet()));

        new File("./output/").mkdir();
        Rio.write(tags_id, new FileWriter("./output/product_id.ttl"), RDFFormat.TURTLE);
        Rio.write(companies_id, new FileWriter("./output/company_id.ttl"), RDFFormat.TURTLE);
        Rio.write(classes_id, new FileWriter("./output/product_type_id.ttl"), RDFFormat.TURTLE);

        //
        // dirLocations.txt to RDF
        //

        var locations = readDicLocations(TagRankingGenerator.class.getResourceAsStream("/dicLocations.txt"));

        var builder = new ModelBuilder();

        locations.entrySet().forEach(pair -> {
            builder.subject("http://dbpedia.org/resource/" + pair.getKey()).add("http://schema.org/identifier", pair.getValue());
        });

        Rio.write(builder.build(), new FileWriter("./output/location_id.ttl"), RDFFormat.TURTLE);
    }

    private static Model assignIds(Set<String> set) {
        // get consistent result
        var sorted = new ArrayList<>(set);
        sorted.sort(Comparator.naturalOrder());

        var builder = new ModelBuilder();

        int id = 0;
        for (var s : sorted) {
            builder.subject(s).add("http://schema.org/identifier", id++);
        }

        return builder.build();
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
