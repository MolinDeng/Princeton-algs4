/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private static final int BORADER_ENGERGY = 1000;
    private static final int[] DIR = new int[] { -1, 0, 1 };
    private double[][] energy;
    private int width;
    private int height;
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        energy = new double[width][height];
    }

    // current picture
    public Picture picture() {
        Picture newPic = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newPic.setRGB(i, j, picture.getRGB(i, j));
            }
        }
        return newPic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException();
        if (energy[x][y] != 0.0) return energy[x][y];
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            energy[x][y] = BORADER_ENGERGY;
        else
            energy[x][y] = Math.sqrt(
                    gradient(picture.get(x - 1, y),
                             picture.get(x + 1, y)) +
                            gradient(picture.get(x, y - 1),
                                     picture.get(x, y + 1)));
        return energy[x][y];

    }

    private double gradient(Color a, Color b) {
        return Math.pow(a.getRed() - b.getRed(), 2)
                + Math.pow(a.getGreen() - b.getGreen(), 2)
                + Math.pow(a.getBlue() - b.getBlue(), 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[width];
        // distTo
        double[][] distTo = new double[width][height];
        for (double[] col : distTo)
            Arrays.fill(col, Double.POSITIVE_INFINITY);
        Arrays.fill(distTo[0], 0);
        // edgeTo
        int[][] edgeTo = new int[width][height];

        int endRow = 0;
        double minDist = Double.POSITIVE_INFINITY;

        // iterate follow topological order
        for (int col = 1; col < width; col++) {
            for (int row = 0; row < height; row++) {
                for (int offset : DIR) {
                    int nRow = row + offset;
                    if (nRow < 0 || nRow >= height) continue;
                    // relax
                    if (distTo[col][row] > distTo[col - 1][nRow] + energy(col, row)) {
                        distTo[col][row] = distTo[col - 1][nRow] + energy(col, row);
                        edgeTo[col][row] = nRow;
                    }
                }
                // find the minimum distTo
                if (col == width - 1 && distTo[col][row] < minDist) {
                    minDist = distTo[col][row];
                    endRow = row;
                }
            }
        }
        for (int col = width - 1, row = endRow; col >= 0; row = edgeTo[col][row], col--)
            seam[col] = row;

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height];
        // distTo
        double[][] distTo = new double[height][width];
        for (double[] row : distTo)
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        Arrays.fill(distTo[0], 0);
        // edgeTo
        int[][] edgeTo = new int[height][width];

        int endCol = 0;
        double minDist = Double.POSITIVE_INFINITY;

        // iterate follow topological order
        for (int row = 1; row < height; row++) {
            for (int col = 0; col < width; col++) {
                for (int offset : DIR) {
                    int nCol = col + offset;
                    if (nCol < 0 || nCol >= width) continue;
                    // relax
                    if (distTo[row][col] > distTo[row - 1][nCol] + energy(col, row)) {
                        distTo[row][col] = distTo[row - 1][nCol] + energy(col, row);
                        edgeTo[row][col] = nCol;
                    }
                }
                // find the minimum distTo
                if (row == height - 1 && distTo[row][col] < minDist) {
                    minDist = distTo[row][col];
                    endCol = col;
                }
            }
        }
        for (int row = height - 1, col = endCol; row >= 0; col = edgeTo[row][col], row--)
            seam[row] = col;

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width, height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height - 1; row++) {
                if (row >= seam[col]) {
                    picture.setRGB(col, row, picture.getRGB(col, row + 1));
                    energy[col][row] = energy[col][row + 1];
                }
            }
        }
        // reset energy
        for (int col = 0; col < width; col++) {
            for (int offset : DIR) {
                int row = seam[col] + offset;
                if (row >= 0 && row < height)
                    energy[col][row] = 0;
            }
        }
        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height, width);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {
                if (col >= seam[row]) {
                    picture.setRGB(col, row, picture.getRGB(col + 1, row));
                    energy[col][row] = energy[col + 1][row];
                }
            }
        }
        // reset energy
        for (int row = 0; row < height; row++) {
            for (int offset : DIR) {
                int col = seam[row] + offset;
                if (col >= 0 && col < width)
                    energy[col][row] = 0;
            }
        }
        width--;
    }

    private void validateSeam(int[] seam, int length, int valueLimit) {
        if (seam == null || seam.length != length || valueLimit <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] >= valueLimit) throw new IllegalArgumentException();
        for (int i = 0, j = 1; j < seam.length; i++, j++)
            if (Math.abs(seam[i] - seam[j]) > 1) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

    }
}
