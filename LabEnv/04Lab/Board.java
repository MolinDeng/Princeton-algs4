/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private static final int[][] DIR = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
    private final int n;
    private final int[][] tiles;
    private final int hammingD;
    private final int manhattanD;
    private final int emptyPosX;
    private final int emptyPosY;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        this.tiles = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++)
            this.tiles[i] = Arrays.copyOf(tiles[i], tiles.length);
        n = tiles.length;
        // calculate hamming distance and manhattan distance
        int h = 0, m = 0, px = 0, py = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    px = i;
                    py = j;
                    continue;
                }
                // if the tile is in the right position i * n + j + 1 = val
                int val = tiles[i][j] - 1;
                int man = Math.abs(val / n - i) + Math.abs(val % n - j);
                if (man > 0) h++;
                m += man;
            }
        }
        manhattanD = m;
        hammingD = h;
        emptyPosX = px;
        emptyPosY = py;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingD;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanD;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingD == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    private boolean isSwapable(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    private int[][] copyTiles() {
        int[][] newTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newTiles[i][j] = tiles[i][j];
            }
        }
        return newTiles;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> p = new ArrayList<>();
        int swapi, swapj;
        for (int[] dir : DIR) {
            swapi = emptyPosX + dir[0];
            swapj = emptyPosY + dir[1];
            if (isSwapable(swapi, swapj)) {
                int[][] newTiles = copyTiles();
                newTiles[emptyPosX][emptyPosY] = newTiles[swapi][swapj];
                newTiles[swapi][swapj] = 0;
                p.add(new Board(newTiles));
            }
        }
        return p;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newTiles = copyTiles();
        int[] indices = new int[4];
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != emptyPosX || j != emptyPosY) {
                    indices[k++] = i;
                    indices[k++] = j;
                    if (k == indices.length) break;
                }
            }
            if (k == indices.length) break;
        }
        int t = newTiles[indices[0]][indices[1]];
        newTiles[indices[0]][indices[1]] = newTiles[indices[2]][indices[3]];
        newTiles[indices[2]][indices[3]] = t;
        return new Board(newTiles);
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Board inital2 = new Board(tiles);
        StdOut.println(initial.equals(inital2));
        StdOut.println(initial.equals(initial.twin()));

        StdOut.println(initial);
        StdOut.println("hanmming dis: " + initial.hamming());
        StdOut.println("manhattan dis: " + initial.manhattan());
        StdOut.println("Twin: ");
        StdOut.println(initial.twin());
        StdOut.println("Neighbors: ");
        ArrayList<Board> ns = (ArrayList<Board>) initial.neighbors();
        for (Board b : ns) StdOut.println(b);
    }
}
