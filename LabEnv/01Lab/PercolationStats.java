/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONST_K = 1.96;
    private double[] thresholds;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and trials should be larger than 0");
        // perform independent trials on an n-by-n grid
        int trialsTimes = trials;
        thresholds = new double[trialsTimes];
        // start trials
        while (trials-- != 0) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row, col;
                while (true) {
                    row = StdRandom.uniformInt(1, n + 1);
                    col = StdRandom.uniformInt(1, n + 1);
                    if (p.isOpen(row, col)) continue;
                    p.open(row, col);
                    break;
                }
            }
            thresholds[trials] = 1.0 * p.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return thresholds.length == 1 ? Double.NaN : StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONST_K * stddev() / Math.sqrt(1.0 * thresholds.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONST_K * stddev() / Math.sqrt(1.0 * thresholds.length);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
