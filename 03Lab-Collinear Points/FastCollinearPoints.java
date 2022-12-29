/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segements;

    public FastCollinearPoints(Point[] points) {
        points = validate(points);
        segements = new ArrayList<>();
        if (points.length < 4) return;
        Point[] aux;
        for (Point p : points) {
            /* Make sure aux maintains natural order.
             * points is already sorted by natural order, we can copy points to aux, which
             * cost O(N), faster than sorting again, which cost O(NlogN)
             * aux = Arrays.copyOf(points, points.length);
             * Or we can modify slopeOrder function: if two points' slope relative to p is the same,
             * then compare their natual order
             * Arrays.sort(aux, p.slopeOrderAndNaturalOrder());
             * Note that this method won't pass API test
             * */
            aux = Arrays.copyOf(points, points.length);
            Arrays.sort(aux, p.slopeOrder());
            Point min = p, max = p;
            for (int j = 1, k = 1; j <= aux.length; j++) {
                if (j == aux.length || Double.compare(p.slopeTo(aux[k]), p.slopeTo(aux[j])) != 0) {
                    /* min equals to anchor p can make sure that segment start from
                     * the smallest point (natural order), so there will be no duplicates
                     * */
                    if (j - k >= 3 && min.compareTo(p) == 0)
                        segements.add(new LineSegment(min, max));
                    k = j;
                    min = p;
                    max = p;
                }
                if (j < aux.length) {
                    if (aux[j].compareTo(min) < 0) min = aux[j];
                    if (aux[j].compareTo(max) > 0) max = aux[j];
                }
            }
        }
    }

    private Point[] validate(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();
        Point[] copy = Arrays.copyOf(points, points.length);
        Arrays.sort(copy);
        for (int i = 0; i + 1 < copy.length; i++) {
            if (copy[i].compareTo(copy[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
        return copy;
    }

    public int numberOfSegments() {
        return segements.size();
    }

    public LineSegment[] segments() {
        return segements.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println("numbers of segments: " + collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
