/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k == 0) return;
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int cnt = 0;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            cnt++;
            if (queue.size() == k) {
                if (StdRandom.bernoulli(1.0 * k / cnt)) {
                    queue.dequeue();
                    queue.enqueue(s);
                }
            }
            else
                queue.enqueue(s);
        }
        while (k-- > 0) StdOut.println(queue.dequeue());
    }
}
