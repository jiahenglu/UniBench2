package Preprocessing.LDBC;

import Preprocessing.Helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class generateTagText {
    public static void main(String[] args) throws Exception {
        // <product> <text>

        var rdf = generatePopularTagByCountry.class.getResourceAsStream("/product_comment.tsv");

        var path = "./output/tagText.txt";
        var writer = new PrintWriter(path);

        var reader = new BufferedReader(new InputStreamReader(rdf));
        reader.readLine(); // skip header

        var line = "";
        while ((line = reader.readLine()) != null) {
            var segs = line.split("\\t");
            if (segs.length != 3)
                continue;

            String product = segs[0].substring(1, segs[0].length() - 1);
            int product_id = Integer.parseInt(segs[1]);
            String comment = segs[2].substring(1, segs[2].length() - 4).trim(); // "xxxx"@en

            if (comment.equals(""))
                comment = Helper.getUriLast(product).replace('_', ' ').trim();

            var sb = new StringBuilder(comment);
            int i = sb.length() - 1;
            while (i >= 0) {
                if (sb.charAt(i) == ' ' && i + 1 < sb.length() - 1 && sb.charAt(i + 1) == ' ')
                    sb.deleteCharAt(i + 1);
                else
                    i--;
            }

            writer.println(String.format("%d  %s", product_id, sb.toString()));
        }

        writer.close();
        reader.close();
    }
}
