package Preprocessing.DBpedia;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class SubGraphSplitter {
    public static void main(String[] args) throws Exception {
        var products = Rio.parse(ProductSubGraphMiner.class.getResourceAsStream("/product_id.ttl"), "", RDFFormat.TURTLE)
                .subjects().stream().map(Value::stringValue).toArray(String[]::new);

        var products_hash = new HashSet<>(Arrays.asList(products));

        splitNodes(products_hash);
        splitTriples(products_hash);
    }

    private static void splitNodes(HashSet<String> products_hash) throws IOException {
        var bw_base = new PrintWriter("output/dbpedia/nodes.base.txt");
        var bw_extra = new PrintWriter("output/dbpedia/nodes.extra.txt");

        var i = new AtomicInteger(1);
        var br = new BufferedReader(new FileReader("output/dbpedia/nodes.txt"));

        br.lines().forEach(line -> {
            if (i.incrementAndGet() % 1000 == 0)
                System.out.println(String.format("%d", i.get()));

            if (products_hash.contains(line))
                bw_base.println(line);
            else
                bw_extra.println(line);
        });

        bw_base.close();
        bw_extra.close();
        br.close();
    }

    private static void splitTriples(HashSet<String> products_hash) throws IOException {
        var bw_base = new PrintWriter("output/dbpedia/nodes.properties.base.ttl");
        var bw_extra = new PrintWriter("output/dbpedia/nodes.properties.extra.ttl");

        var i = new AtomicInteger(1);
        var br = new BufferedReader(new FileReader("output/dbpedia/nodes.properties.ttl"));
        br.lines().forEach(line -> {
            if (i.incrementAndGet() % 100000 == 0)
                System.out.println(String.format("%d", i.get()));

            var subject = line.split(" ", 2)[0];
            subject = subject.substring(1, subject.length() - 1); // remove '<' '>'

            if (products_hash.contains(subject))
                bw_base.println(line);
            else
                bw_extra.println(line);
        });

        bw_base.close();
        bw_extra.close();
        br.close();
    }
}
