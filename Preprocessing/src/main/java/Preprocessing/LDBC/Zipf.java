package Preprocessing.LDBC;

public class Zipf {
    private final int n;
    private final double s;
    private double harmonic = 0;

    public Zipf(int n, double s) {
        this.n = n;
        this.s = s;

        for (int i = 1; i <= n; i++)
            this.harmonic += 1d / Math.pow(i, this.s);
    }

    public double pmf(int k) {
        if (k == 0)
            return 0d;
        if (k > n)
            return Double.MIN_VALUE;

        return (1.0d / Math.pow(k, this.s)) / this.harmonic;
    }

    public double cdf(int k) {
        if (k == 0)
            return 0d;
        if (k > n)
            return Double.MIN_VALUE;

        double cdf = 0d;

        for (int i = 1; i <= k; i++)
            cdf += pmf(i);

        return cdf;
    }
}
