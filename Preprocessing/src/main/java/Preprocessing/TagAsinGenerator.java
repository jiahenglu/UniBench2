package Preprocessing;

import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.FileWriter;

public class TagAsinGenerator {
    public static void main(String[] args) throws Exception {
        var tags = Rio.parse(TagRankingGenerator.class.getResourceAsStream("/product.ttl"), "", RDFFormat.TURTLE);

        var builder = new ModelBuilder();
        tags.subjects().forEach(sub -> {
            var asin = Asin.generate();

            builder.subject(sub).add("http://schema.org/identifier", asin);
        });

        Rio.write(builder.build(), new FileWriter("./output/product_asin.ttl"), RDFFormat.TURTLE);
    }
}
