/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> treeSet;

    // construct an empty set of points
    public PointSET() {
        treeSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return treeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        treeSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return treeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.01);
        for (Point2D p : treeSet)
            StdDraw.point(p.x(), p.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> arrayList = new ArrayList<>();
        for (Point2D p : treeSet)
            if (rect.contains(p)) arrayList.add(p);
        return arrayList;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double min = Double.POSITIVE_INFINITY;
        Point2D champion = null;
        for (Point2D i : treeSet) {
            double d = p.distanceSquaredTo(i);
            if (d < min) {
                min = d;
                champion = i;
            }
        }
        return champion;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        brute.draw();
    }
}
