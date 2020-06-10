package Preprocessing.LDBC;

import Preprocessing.Helper;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class generateTags {
    public static void main(String[] args) throws Exception {
        // 0	1	Hyperflite_K-10_Jawz_Dog_Disc	Hyperflite_K-10_Jawz_Dog_Disc

        var productIds = Rio.parse(generateTags.class.getResourceAsStream("/product_id.ttl"), "", RDFFormat.TURTLE);
        var typeIds = Rio.parse(generateTags.class.getResourceAsStream("/product_type_id.ttl"), "", RDFFormat.TURTLE);
        var typeMapping = Rio.parse(generateTags.class.getResourceAsStream("/product_type.ttl"), "", RDFFormat.TURTLE);

        var path = "./output/tags.txt";

        var writer = new PrintWriter(path);

        productIds.forEach(statement -> {
            var uri = statement.getSubject();
            var id = statement.getObject();
            var classes =
                    StreamSupport.stream(typeMapping.getStatements(uri, null, null).spliterator(), false)
                            .map(Statement::getObject)
                            .collect(Collectors.toList());

            classes.forEach(cls -> {
                var cls_id = typeIds.getStatements((Resource) cls, null, null).iterator().next().getObject();

                var text_id = Helper.getUriLast(uri.stringValue());
                writer.println(String.format("%s\t%s\t%s\t%s", id.stringValue(), cls_id.stringValue(), text_id, text_id));
            });
        });

        writer.close();
    }
}
