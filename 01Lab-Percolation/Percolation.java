/* *****************************************************************************
 *  Name:              Molin Deng
 *  Coursera User ID:  123456
 *  Last modified:     Dec 25, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };
    private WeightedQuickUnionUF uf1; // for full
    private WeightedQuickUnionUF uf2; // for percolate
    private boolean[] sites;
    private int gridSize;
    private int numOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n should be larger than 0");
        gridSize = n;
        uf1 = new WeightedQuickUnionUF(n * n + 1);
        uf2 = new WeightedQuickUnionUF(n * n + 2);
        sites = new boolean[n * n + 2];
        sites[0] = true; // uf1's top and uf2's top
        sites[n * n + 1] = true; // uf2's bottom
        numOfOpenSites = 0;
    }

    // calculate index for row col
    private int getSite(int row, int col) {
        if (row == 0) return 0;
        if (row == gridSize + 1) col = 1;
        return (row - 1) * gridSize + col;
    }

    private void validate(int p, String desc) {
        if (p < 1 || p > gridSize)
            throw new IllegalArgumentException(desc + " index i out of bounds");
    }

    private boolean isInGrid(int row, int col) {
        return row >= 1 && row <= gridSize && col >= 1 && col <= gridSize;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, "row");
        validate(col, "col");
        if (!isOpen(row, col)) {
            numOfOpenSites++;
            sites[getSite(row, col)] = true;
            for (int i = 0; i < dirs.length; i++) {
                int neightborRow = row + dirs[i][0];
                int neightborCol = col + dirs[i][1];
                if (neightborRow == gridSize + 1)
                    uf2.union(getSite(neightborRow, neightborCol), getSite(row, col));
                if (neightborRow == 0 || isInGrid(neightborRow, neightborCol) && isOpen(
                        neightborRow, neightborCol)) {
                    uf1.union(getSite(neightborRow, neightborCol), getSite(row, col));
                    uf2.union(getSite(neightborRow, neightborCol), getSite(row, col));
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, "row");
        validate(col, "col");
        return sites[getSite(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, "row");
        validate(col, "col");
        return isOpen(row, col) && uf1.find(getSite(row, col)) == uf1.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf2.find(gridSize * gridSize + 1) == uf2.find(0);
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            for (int row = 1; row <= n; row++)
                for (int col = 1; col <= n; col++) {
                    if (!p.isOpen(row, col))
                        if (StdRandom.bernoulli(1.0 / (n * n - p.numberOfOpenSites())))
                            p.open(row, col);
                }
        }
        StdOut.println("Percolation threshold is " + p.numberOfOpenSites() / Math.pow(n, 2));
    }
}
