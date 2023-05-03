/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class MoveToFront {
    private static final int R = 256;
    private static ArrayList<Character> alphabet; // char : index

    private static void reset() {
        alphabet = new ArrayList<>();
        for (char i = 0; i < R; i++) alphabet.add(i);
    }

    private static void update(int i) {
        char c = alphabet.remove(i);
        alphabet.add(0, c);
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        reset();
        while (!BinaryStdIn.isEmpty()) {
            int i = alphabet.indexOf(BinaryStdIn.readChar(8));
            BinaryStdOut.write(i, 8);
            update(i);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        reset();
        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            BinaryStdOut.write(alphabet.get(i), 8);
            update(i);
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
