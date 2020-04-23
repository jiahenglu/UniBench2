package Unibench;

/**
 * Created by chzhang on 06/03/2017.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MetaDataParcer {
    public static HashMap<String, Boolean> tagList = new HashMap<String, Boolean>();
    private static String tag;

    public static void initialize(File fs) throws IOException {
        FileReader fr = new FileReader(fs);
        BufferedReader buff = new BufferedReader(fr);
        while ((tag = buff.readLine()) != null) {
            if (tag != " ") {
                tagList.put(tag, false);
            }
        }
    }
}
