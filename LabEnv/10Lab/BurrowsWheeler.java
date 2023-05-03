/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    private final static int R = 256;

    private static int[] keyIndexCount(char[] t) {
        int sz = t.length;
        int[] next = new int[t.length];
        // compute frequency counts
        int[] count = new int[R + 1];
        for (int i = 0; i < sz; i++)
            count[t[i] + 1]++;

        // compute mo: letter r should appear in the range [count[r], count[r+1])
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        // assign next[i]
        for (int i = 0; i < sz; i++)
            next[count[t[i]]++] = i; // t[i]
        return next;
    }

    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(input);
        int n = suffixArray.length();
        char[] t = new char[n];
        int first = 0;
        for (int i = 0; i < n; i++) {
            if (suffixArray.index(i) == 0) first = i;
            t[i] = input.charAt((suffixArray.index(i) - 1 + n) % n);
        }
        BinaryStdOut.write(first);
        for (char c : t) BinaryStdOut.write(c, 8);
        BinaryStdOut.close();
    }


    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int i = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        int[] next = keyIndexCount(t);

        int sz = t.length;
        while (sz-- > 0) {
            BinaryStdOut.write(t[next[i]]);
            i = next[i];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
