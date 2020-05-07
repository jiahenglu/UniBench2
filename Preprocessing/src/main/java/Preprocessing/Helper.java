package Preprocessing;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.*;

public class Helper {
    public static BufferedReader getCompressedReader(String fileIn) throws FileNotFoundException, CompressorException {
        var fin = new FileInputStream(fileIn);
        var bis = new BufferedInputStream(fin);
        var input = new CompressorStreamFactory().createCompressorInputStream(bis);
        return new BufferedReader(new InputStreamReader(input));
    }

    public static BufferedReader getCompressedReader(InputStream fin) throws CompressorException {
        var bis = new BufferedInputStream(fin);
        var input = new CompressorStreamFactory().createCompressorInputStream(bis);
        return new BufferedReader(new InputStreamReader(input));
    }

    public static String getUriLast(String uri) {
        var segs = uri.split("/");
        return segs[segs.length - 1];
    }

    public static String getTitle(String url) {
        var sb = new StringBuilder(url);
        int i = sb.length() - 1;
        while (i >= 0) {
            if (sb.charAt(i) == ' ' && i + 1 < sb.length() - 1 && sb.charAt(i + 1) == ' ')
                sb.deleteCharAt(i + 1);
            else
                i--;
        }
        return sb.toString();
    }
}
