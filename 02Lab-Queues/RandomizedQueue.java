/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 8;
    private int n;
    private Item[] v;
    private int first;
    private int last;

    // construct an empty randomized queue
    public RandomizedQueue() {
        v = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
        first = 0;
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            copy[i] = v[(first + i) % v.length];
        v = copy;
        first = 0;
        last = n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == v.length) resize(2 * v.length);
        v[last++] = item;
        if (last == v.length) last = 0;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int id = (first + StdRandom.uniformInt(n)) % v.length;
        Item item = v[id];
        v[id] = v[first];
        v[first++] = null;
        n--;
        if (first == v.length) first = 0;
        if (n > 0 && n == v.length / 4) resize(v.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int id = (first + StdRandom.uniformInt(n)) % v.length;
        return v[id];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;
        private int[] idx;

        public RandomizedQueueIterator() {
            i = 0;
            idx = new int[n];
            for (int k = 0; k < n; k++)
                idx[k] = (first + k) % v.length;
            StdRandom.shuffle(idx);
        }

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return v[idx[i++]];
        }
        // public Item next() {
        //     if (!hasNext()) throw new NoSuchElementException();
        //     int myFirst = (first + i) % v.length;
        //     int target = (myFirst + StdRandom.uniformInt(n - i)) % v.length;
        //     Item item = v[target];
        //     v[target] = v[myFirst];
        //     v[myFirst] = item;
        //     i++;
        //     return item;
        // }

        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }
}
