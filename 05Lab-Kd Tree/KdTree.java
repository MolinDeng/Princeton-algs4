/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int n;

    // construct an empty set of points
    public KdTree() {
        root = null;
        n = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return n;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;
        root = put(root, p, true, 0, 0, 1, 1);
        n++;
    }

    private Node put(Node x, Point2D p, boolean isXPartition, double xmin, double ymin, double xmax,
                     double ymax) {
        if (x == null) return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        int cmp = isXPartition ? Double.compare(p.x(), x.p.x())
                               : Double.compare(p.y(), x.p.y());

        if (cmp < 0) {
            if (isXPartition) xmax = x.p.x();
            else ymax = x.p.y();
            x.left = put(x.left, p, !isXPartition, xmin, ymin, xmax, ymax);
        }
        else {
            if (isXPartition) xmin = x.p.x();
            else ymin = x.p.y();
            x.right = put(x.right, p, !isXPartition, xmin, ymin, xmax, ymax);
        }
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p, true) != null;
    }

    private Point2D get(Node x, Point2D p, boolean isXPartition) {
        if (p == null) throw new IllegalArgumentException();
        if (x == null) return null;
        if (p.compareTo(x.p) == 0) return x.p;
        int cmp = isXPartition ? Double.compare(p.x(), x.p.x())
                               : Double.compare(p.y(), x.p.y());
        if (cmp < 0) return get(x.left, p, !isXPartition);
        else return get(x.right, p, !isXPartition);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    private void draw(Node x, boolean isXPartition) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x.p.x(), x.p.y());
        StdDraw.setPenRadius();
        if (isXPartition) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.left, !isXPartition);
        draw(x.right, !isXPartition);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> arrayList = new ArrayList<>();
        if (!isEmpty()) range(root, rect, arrayList);
        return arrayList;
    }

    private void range(Node nd, RectHV rect, ArrayList<Point2D> arrayList) {
        if (rect.contains(nd.p)) arrayList.add(nd.p);
        if (nd.left != null && rect.intersects(nd.left.rect))
            range(nd.left, rect, arrayList);
        if (nd.right != null && rect.intersects(nd.right.rect))
            range(nd.right, rect, arrayList);
    }

    private static class Champion {
        Point2D p;
        double minD;

        Champion(Point2D pp, double d) {
            p = pp;
            minD = d;
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Champion champion = new Champion(root.p, Double.POSITIVE_INFINITY);
        nearest(p, root, true, champion);
        return champion.p;
    }

    private void nearest(Point2D p, Node nd, boolean flag, Champion champion) {
        double queryD = p.distanceSquaredTo(nd.p);
        if (queryD < champion.minD) {
            champion.p = nd.p;
            champion.minD = queryD;
        }
        boolean leftFirst = (flag ? Double.compare(p.x(), nd.p.x()) :
                             Double.compare(p.y(), nd.p.y())) < 0;
        if (leftFirst) {
            if (nd.left != null && champion.minD > nd.left.rect.distanceSquaredTo(p))
                nearest(p, nd.left, !flag, champion);
            if (nd.right != null && champion.minD > nd.right.rect.distanceSquaredTo(p))
                nearest(p, nd.right, !flag, champion);
        }
        else {
            if (nd.right != null && champion.minD > nd.right.rect.distanceSquaredTo(p))
                nearest(p, nd.right, !flag, champion);
            if (nd.left != null && champion.minD > nd.left.rect.distanceSquaredTo(p))
                nearest(p, nd.left, !flag, champion);
        }

    }

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node left, right;

        Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.left = null;
            this.right = null;
        }
    }


    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(0.81, 0.30);
        Point2D nearest = kdtree.nearest(new Point2D(0.81, 0.30));
        StdDraw.point(nearest.x(), nearest.y());
        StdOut.println(nearest);
    }
}