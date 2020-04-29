package Preprocessing;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TagIdGenerator {
    public static void main(String[] args) throws Exception {
        var tags = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product.ttl"), "", RDFFormat.TURTLE);
        var classes = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product_type.ttl"), "", RDFFormat.TURTLE);
        var parent_classes = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product_type_tree.ttl"), "", RDFFormat.TURTLE);

        var tags_id = assignIds(tags.subjects().stream().map(Resource::stringValue).collect(Collectors.toSet()));

        var all_classes = new HashSet<>(classes.objects());
        all_classes.addAll(parent_classes.subjects());
        all_classes.addAll(parent_classes.objects());
        var classes_id = assignIds(all_classes.stream().map(Value::stringValue).collect(Collectors.toSet()));

        new File("./output/").mkdir();
        Rio.write(tags_id, new FileWriter("./output/tagIds.ttl"), RDFFormat.TURTLE);
        Rio.write(classes_id, new FileWriter("./output/tagClasses.ttl"), RDFFormat.TURTLE);
    }

    private static Model assignIds(Set<String> set) {
        var builder = new ModelBuilder();

        int id = 0;
        for (var s : set) {
            builder.subject(s).add("http://schema.org/identifier", id++);
        }

        return builder.build();
    }
}
