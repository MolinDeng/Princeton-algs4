/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    private static char[] init() {
        char[] a = new char[R];
        for (char i = 0; i < R; i++) a[i] = i;
        return a;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] index2Char = init();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);

            int idx = 0;
            for (; idx < R; idx++)
                if (index2Char[idx] == c) break;
            for (int i = idx; i > 0; i--)
                index2Char[i] = index2Char[i - 1];
            index2Char[0] = c;

            BinaryStdOut.write(idx, 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] index2Char = init();
        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readChar();
            char c = index2Char[idx];
            for (int i = idx; i > 0; i--)
                index2Char[i] = index2Char[i - 1];
            index2Char[0] = c;

            BinaryStdOut.write(c, 8);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }
}
