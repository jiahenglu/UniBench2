package Preprocessing.DBpedia;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductSubGraphMiner {
    public static void main(String[] args) throws Exception {
        new File("output/dbpedia/").mkdirs();

        //for (int hop = 0; hop <= 2; hop++)
        //   collectNeighbours(hop);

        // sort -o node.0.txt node.0.txt
        // sort -o node.1.txt node.1.txt
        // diff node.0.txt node.1.txt | grep ">" | sed -e 's/> //' > 1.txt

        for (int hop = 0; hop <= 2; hop++)
            collectProperties(hop);
    }

    private static void collectProperties(int hop) throws Exception {

        var lines = Files.readAllLines(Paths.get(String.format("output/dbpedia/node.%d.txt", hop)));

        var writer = new PrintWriter(String.format("output/dbpedia/triple.%d.nt", hop));

        var i = new AtomicInteger(1);
        lines.parallelStream().forEach(node -> {
            if (i.getAndIncrement() % 1000 == 0)
                System.out.println(String.format("%d/%d", i.get(), lines.size()));

            var graph = doCollectProperties("http://localhost:8890/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org", node);
            synchronized (writer) {
                Rio.write(graph, writer, RDFFormat.NTRIPLES);
            }
        });

        writer.close();
    }

    private static Model doCollectProperties(String endpoint, String node) {

        var builder = new ModelBuilder();

        var SparqlRepo = new SPARQLRepository(endpoint);
        SparqlRepo.init();
        var con = SparqlRepo.getConnection();

        var query = String.format("construct { <%s> ?p ?o } where {\n" +
                "    <%s> ?p ?o .\n" +
                "}", node, node);
        var graphQuery = con.prepareGraphQuery(QueryLanguage.SPARQL, query);

        var iter = graphQuery.evaluate();

        synchronized (builder) {
            //var i = 0;
            while (iter.hasNext()) {
                //i += 1
                var stmt = iter.next();
                builder.subject(stmt.getSubject()).add(stmt.getPredicate(), stmt.getObject());
            }
            //println(s"$start: $i items")
        }
        con.close();
        SparqlRepo.shutDown();

        return builder.build();
    }

    private static void collectNeighbours(int hop) throws Exception {
        var products = Rio.parse(ProductSubGraphMiner.class.getResourceAsStream("/product_id.ttl"), "", RDFFormat.TURTLE)
                .subjects().stream().map(Value::stringValue).toArray(String[]::new);

        var neighbours = new ConcurrentHashMap<String, Boolean>();

        var i = new AtomicInteger(1);

        Arrays.stream(products).parallel().forEach(p -> {
            System.out.println(String.format("%d/%d (%s) %s", i.getAndIncrement(), products.length, neighbours.size(), p));

            var collected = doCollectNeighbours("http://localhost:8890/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org", p, hop);

            collected.forEach(c -> neighbours.putIfAbsent(c, Boolean.TRUE));
        });

        var writer = new PrintWriter(String.format("output/dbpedia/node.%d.txt", hop));
        neighbours.keySet().forEach(writer::println);
        writer.close();
    }

    static List<String> doCollectNeighbours(String endpoint, String start, int hop) {
        if (hop < 0)
            throw new IllegalArgumentException("hop must be 0 or greater");

        if (hop == 0)
            return Collections.singletonList(start);

        var SparqlRepo = new SPARQLRepository(endpoint);
        SparqlRepo.init();
        var con = SparqlRepo.getConnection();

        StringBuilder query = new StringBuilder(String.format("PREFIX dbr: <http://dbpedia.org/resource/>\n" +
                        "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "select distinct ?%s where {\n" +
                        "    <%s> ((<>|!<>)|^(<>|!<>)){0,1} ?s .\n" +
                        "    filter(isuri(?s) && strstarts(str(?s), \"http://dbpedia.org/\"))\n"
                , "s".repeat(hop), start));

        if (hop > 1) {
            for (int d = 2; d <= hop; d++) {
                query.append(String.format("    ?%s ((<>|!<>)|^(<>|!<>)){0,1} ?%s .\n" +
                                "    filter(isuri(?%s) && strstarts(str(?%s), \"http://dbpedia.org/\"))\n",
                        "s".repeat(d - 1), "s".repeat(d), "s".repeat(d), "s".repeat(d)
                ));
            }
        }

        query.append("}");

        var tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());

        var result = new ArrayList<String>();
        var iter = tupleQuery.evaluate();
        while (iter.hasNext())
            result.add(iter.next().getValue("s".repeat(hop)).stringValue());

        con.close();
        SparqlRepo.shutDown();

        return result;
    }
}
