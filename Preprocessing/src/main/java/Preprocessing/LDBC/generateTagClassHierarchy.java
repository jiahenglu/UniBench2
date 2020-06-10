package Preprocessing.LDBC;

import Preprocessing.Helper;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.PrintWriter;

public class generateTagClassHierarchy {
    public static void main(String[] args) throws Exception {
        // 0	1

        var tagClassIds = Rio.parse(generateTagClassHierarchy.class.getResourceAsStream("/product_type_id.ttl"), "", RDFFormat.TURTLE);
        var tagClassHierarchy = Rio.parse(generateTagClassHierarchy.class.getResourceAsStream("/product_type_tree.ttl"), "", RDFFormat.TURTLE);

        var path = "./output/tagClassHierarchy.txt";

        var writer = new PrintWriter(path);

        tagClassHierarchy.forEach(statement -> {
            var child = statement.getSubject();
            var parent = statement.getObject();

            var child_id = tagClassIds.getStatements(child, null, null).iterator().next().getObject();
            var parent_id = tagClassIds.getStatements((Resource) parent, null, null).iterator().next().getObject();

            writer.println(String.format("%s\t%s\t// %s -> %s", child_id.stringValue(), parent_id.stringValue(),
                    Helper.getUriLast(child.stringValue()), Helper.getUriLast(parent.stringValue())));
        });

        writer.close();
    }
}
