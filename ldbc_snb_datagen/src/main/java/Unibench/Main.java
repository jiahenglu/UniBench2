package Unibench;


import java.util.HashMap;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        HashMap mapping = new HashMap();
        Random rand = new Random();
        int NumofProducts = 0;
        int TotalProducts = 9694;
        for (int i = 0; i < 65; i++) {
            NumofProducts = (int) Math.round(inverseF(20, 400, 0.6, rand.nextDouble()));

            if (TotalProducts > NumofProducts) {
                TotalProducts = TotalProducts - NumofProducts;
                mapping.put(i, NumofProducts);
            } else {
                mapping.put(i, TotalProducts);
                break;
            }

        }

        System.out.println(mapping);
        System.out.println("hello world");
    }


    // truncted Power-law distribution between a and b
    public static double inverseF(double a, double b, double c, double u) {
        if (c <= 0.0D) {
            throw new IllegalArgumentException("c <= 0");
        } else if (u >= 0.0D && u <= 1.0D) {
            return u == 0.0D ? a : (u == 1.0D ? b : a + (b - a) * Math.pow(u, 1.0D / c));
        } else {
            throw new IllegalArgumentException("u not in [0, 1]");
        }
    }
}
