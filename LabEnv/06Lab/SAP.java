/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    private final Digraph g;

    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        g = new Digraph(G);
    }

    private class Helper {
        private int length;
        private int ancestor;

        Helper() {
            length = -1;
            ancestor = -1;
        }

        // query key 0 return length; 1 return id
        private void findSAP(int v, int w) {
            if (v < 0 || v >= g.V() || w < 0 || w >= g.V()) throw new IllegalArgumentException();
            if (v == w) {
                length = 0;
                ancestor = w;
                return;
            }
            int[] dist1 = new int[g.V()];
            int[] dist2 = new int[g.V()];
            Arrays.fill(dist1, -1);
            Arrays.fill(dist2, -1);
            // run BFS from v, log every ancestor's distance to v
            Queue<Integer> todo = new Queue<>();
            todo.enqueue(v);
            dist1[v] = 0;
            while (!todo.isEmpty()) {
                int p = todo.dequeue();
                for (int q : g.adj(p)) {
                    if (dist1[q] < 0) { // unmarked (unvisited)
                        dist1[q] = dist1[p] + 1;
                        todo.enqueue(q);
                    }
                }
            }
            int min = Integer.MAX_VALUE;
            // run BFS from w
            dist2[w] = 0;
            todo.enqueue(w);
            while (!todo.isEmpty()) {
                int p = todo.dequeue();
                // find result, dist1[q] >= 0 means first BFS visited
                if (dist1[p] >= 0 && dist1[p] + dist2[p] < min) {
                    min = dist1[p] + dist2[p];
                    ancestor = p;
                }
                for (int q : g.adj(p)) {
                    if (dist2[q] < 0) { // unmarked (unvisited)
                        dist2[q] = dist2[p] + 1;
                        todo.enqueue(q);
                    }
                }
            }
            if (ancestor == -1) length = -1;
            else length = min;
        }

        private void findSAP(Iterable<Integer> v, Iterable<Integer> w) {
            int[] dist1 = new int[g.V()];
            int[] dist2 = new int[g.V()];
            Arrays.fill(dist1, -1);
            Arrays.fill(dist2, -1);
            Queue<Integer> todo = new Queue<>();
            for (int i : v) {
                dist1[i] = 0;
                todo.enqueue(i);
            }
            while (!todo.isEmpty()) {
                int p = todo.dequeue();
                for (int q : g.adj(p)) {
                    if (dist1[q] < 0) { // unmarked (unvisited)
                        dist1[q] = dist1[p] + 1;
                        todo.enqueue(q);
                    }
                }
            }
            int min = Integer.MAX_VALUE;
            // run BFS from w
            for (int i : w) {
                dist2[i] = 0;
                todo.enqueue(i);
            }
            while (!todo.isEmpty()) {
                int p = todo.dequeue();
                // find result, dist1[q] >= 0 means first BFS visited
                if (dist1[p] >= 0 && dist1[p] + dist2[p] < min) {
                    min = dist1[p] + dist2[p];
                    ancestor = p;
                }
                for (int q : g.adj(p)) {
                    if (dist2[q] < 0) { // unmarked (unvisited)
                        dist2[q] = dist2[p] + 1;
                        todo.enqueue(q);
                    }
                }
            }
            if (ancestor == -1) length = -1;
            else length = min;
        }
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Helper helper = new Helper();
        helper.findSAP(v, w);
        return helper.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Helper helper = new Helper();
        helper.findSAP(v, w);
        return helper.ancestor;
    }

    private void validate(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer i : v)
            if (i == null || i < 0 || i >= g.V()) throw new IllegalArgumentException();
        for (Integer i : w)
            if (i == null || i < 0 || i >= g.V()) throw new IllegalArgumentException();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v, w);
        Helper helper = new Helper();
        helper.findSAP(v, w);
        return helper.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v, w);
        Helper helper = new Helper();
        helper.findSAP(v, w);
        return helper.ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
