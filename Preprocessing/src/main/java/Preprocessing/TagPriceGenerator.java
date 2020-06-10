package Preprocessing;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.FileWriter;
import java.io.IOException;

public class TagPriceGenerator {
    public static void main(String[] args) throws IOException {

        var path = "./output/product_price.ttl";

        var products = Rio.parse(TagPriceGenerator.class.getResourceAsStream("/product.ttl"), "", RDFFormat.TURTLE);

        var rand = new LogNormalDistribution(0, 0.5);

        var builder = new ModelBuilder();

        products.subjects().forEach(subject -> {
            builder.subject(subject).add("http://schema.org/price", rand.sample() * 50);
        });

        Rio.write(builder.build(), new FileWriter(path), RDFFormat.TURTLE);
    }
}
