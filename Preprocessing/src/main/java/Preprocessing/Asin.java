package Preprocessing;

import java.util.Random;

public class Asin {
    static Random rand = new Random();

    static char[] alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static char[] all = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String generate() {
        var sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            if (i == 0)
                sb.append(getRandomChar(alphabets));
            else
                sb.append(getRandomChar(all));
        }

        return sb.toString();
    }

    private static char getRandomChar(char[] candidates) {
        return candidates[rand.nextInt(candidates.length)];
    }
}
