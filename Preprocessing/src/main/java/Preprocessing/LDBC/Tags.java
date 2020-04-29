package Preprocessing.LDBC;

import Preprocessing.Helper;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Tags {
    static Model tagIds;
    static Model tagClassIds;
    static Model tagClassMapping;
    static Model tagClassHierarchy;
    static Model tagRankings;

    public static void main(String[] args) throws Exception {
        loadModels();

        new File("./output/").mkdirs();

        //generateTagClasses();
        //generateTags();
        //generateTagClassHierarchy();
        generatePopularTagByCountry();
    }

    private static void generateTags() throws Exception {
        // 0	1	Hyperflite_K-10_Jawz_Dog_Disc	Hyperflite_K-10_Jawz_Dog_Disc

        var path = "./output/tags.txt";

        var writer = new PrintWriter(path);

        tagIds.forEach(statement -> {
            var uri = statement.getSubject();
            var id = statement.getObject();
            var classes =
                    StreamSupport.stream(tagClassMapping.getStatements(uri, null, null).spliterator(), false)
                            .map(Statement::getObject)
                            .collect(Collectors.toList());

            classes.forEach(cls -> {
                var cls_id = tagClassIds.getStatements((Resource) cls, null, null).iterator().next().getObject();

                var text_id = Helper.getUriLast(uri.stringValue());
                writer.println(String.format("%s\t%s\t%s\t%s", id.stringValue(), cls_id.stringValue(), text_id, text_id));
            });
        });

        writer.close();
    }

    private static void generateTagClassHierarchy() throws Exception {
        // 0	1

        var path = "./output/tagClassHierarchy.txt";

        var writer = new PrintWriter(path);

        tagClassHierarchy.forEach(statement -> {
            var child = statement.getSubject();
            var parent = statement.getObject();

            var child_id = tagClassIds.getStatements(child, null, null).iterator().next().getObject();
            var parent_id = tagClassIds.getStatements((Resource) parent, null, null).iterator().next().getObject();

            writer.println(String.format("%s\t%s\t%s -> %s", child_id.stringValue(), parent_id.stringValue(),
                    Helper.getUriLast(child.stringValue()), Helper.getUriLast(parent.stringValue())));
        });

        writer.close();
    }

    private static void generatePopularTagByCountry() throws Exception {

    }

    private static void generateTagClasses() throws Exception {
        // 0	Social_Commerce	Social_Commerce

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

    private static void loadModels() throws Exception {
        tagIds = Rio.parse(new FileReader("./output/tagIds.ttl"), "", RDFFormat.TURTLE);
        tagClassIds = Rio.parse(new FileReader("./output/tagClasses.ttl"), "", RDFFormat.TURTLE);
        tagClassMapping = Rio.parse(Tags.class.getResourceAsStream("/product_type.ttl"), "", RDFFormat.TURTLE);
        tagClassHierarchy = Rio.parse(Tags.class.getResourceAsStream("/product_type_tree.ttl"), "", RDFFormat.TURTLE);
        tagRankings = Rio.parse(new FileReader("./output/tagRankings.ttl"), "", RDFFormat.TURTLE);
    }
}
