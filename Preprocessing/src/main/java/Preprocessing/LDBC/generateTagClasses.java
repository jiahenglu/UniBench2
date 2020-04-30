package Preprocessing.LDBC;

import Preprocessing.Helper;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.PrintWriter;

public class generateTagClasses {
    public static void main(String[] args) throws Exception {
        // 0	Social_Commerce	Social_Commerce

        var tagClassIds = Rio.parse(generateTagClasses.class.getResourceAsStream("/product_type_id.ttl"), "", RDFFormat.TURTLE);

        var path = "./output/tagClasses.txt";

        var writer = new PrintWriter(path);

        tagClassIds.forEach(statement -> {
            var uri = statement.getSubject();
            var id = statement.getObject();

            var text_id = Helper.getUriLast(uri.stringValue());

            writer.println(String.format("%s\t%s\t%s", id.stringValue(), text_id, text_id));
        });

        writer.close();
    }
}
