/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    private static class Node {
        private boolean isTernimal = false;
        private Node[] children = new Node[26];
    }

    private final Node root; // should not be modified
    private final int[][] dirs = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, 1 }, { 0, -1 },
            { 1, 1 }, { 1, 0 }, { 1, -1 }
    };

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String s : dictionary)
            put(root, s);
    }

    private void put(Node x, String key) {
        for (char c : key.toCharArray()) {
            if (x.children[c - 'A'] == null)
                x.children[c - 'A'] = new Node();
            x = x.children[c - 'A'];
        }
        x.isTernimal = true;
    }

    private boolean get(Node x, String key) {
        for (char c : key.toCharArray()) {
            x = x.children[c - 'A'];
            if (x == null) return false;
        }
        return x.isTernimal;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> set = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                dfs(root, board, i, j, sb, visited, set);
            }
        }

        return set;
    }

    private void dfs(Node x, BoggleBoard board, int i, int j, StringBuilder sb, boolean[][] visited,
                     HashSet<String> set) {
        char c = board.getLetter(i, j);
        Node next = x.children[c - 'A'];
        if (next == null) return;

        // deal with Qu
        if (c == 'Q') {
            next = next.children['U' - 'A'];
            if (next == null) return;
        }

        sb.append(c);
        if (c == 'Q') sb.append('U'); // deal with Qu
        visited[i][j] = true;

        if (sb.length() >= 3 && next.isTernimal)
            set.add(sb.toString());

        for (int[] dir : dirs) {
            int ii = i + dir[0];
            int jj = j + dir[1];
            if (isValidGrid(board, ii, jj) && !visited[ii][jj])
                dfs(next, board, ii, jj, sb, visited, set);
        }
        // backtracking
        visited[i][j] = false;
        sb.deleteCharAt(sb.length() - 1);
        if (c == 'Q') sb.deleteCharAt(sb.length() - 1); // deal with Qu
    }

    private boolean isValidGrid(BoggleBoard board, int row, int col) {
        return col >= 0 && col < board.cols() && row >= 0 && row < board.rows();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!get(root, word)) return 0;
        int length = word.length();
        if (length < 3) return 0;
        else if (length <= 4) return 1;
        else if (length <= 6) return length - 3;
        else if (length == 7) return 5;
        else return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
