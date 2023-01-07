/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedRandomizedQueue<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
    }

    private Node first;
    private Node last;
    private int n;

    // construct an empty randomized queue
    public LinkedRandomizedQueue() {
        first = null;
        last = null;
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int cnt = StdRandom.uniformInt(n);
        Node p = first;
        Node prevP = null;
        while (cnt-- > 0) {
            prevP = p;
            p = p.next;
        }
        Item item = p.item;
        n--;
        if (prevP == null) first = p.next;
        else prevP.next = p.next;
        if (p == last) last = prevP;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int cnt = StdRandom.uniformInt(n);
        Node p = first;
        while (cnt-- > 0)
            p = p.next;
        return p.item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(first, n);
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Node current;
        private int size;

        public RandomizedQueueIterator(Node first, int n) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            return null;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    public static void main(String[] args) {

    }
}
