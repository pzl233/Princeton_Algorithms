import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a set of points in the unit square.
 * It use a brute-force implementation to find
 * a list of points in a given rectangle, and the nearest point of a given point
 * using a red-black BST.
 */
public class PointSET {
    /**
     * Initializes a new set of Point2D
     */
    private SET<Point2D> pointSet;
    public PointSET() {
        pointSet = new SET<>();
    }

    /**
     * Returns true if this set is empty.
     *
     * @return {@code true} if this set is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    /**
     * Returns the number of points in this set.
     * @return number of points in the set
     */
    public int size() {
        return pointSet.size();
    }

    /**
     * Add the point to the set if it is not already in the set
     * @param p the point to be added
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!");
        }
        if (!pointSet.contains(p)) {
            pointSet.add(p);
        }
    }

    /**
     * To determine if the set contain point p
     * @param p the point we want to examine if it is in the set or not.
     * @return {@code true} if the set contain point p;
     *         {@code false} otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!");
        }
        return pointSet.contains(p);
    }


    /**
     *  Draw all points in the set to standard draw
     */
    public void draw() {
        for (Point2D p: pointSet) {
            p.draw();
        }
    }

    /**
     * Find all points that are inside the given rectangle (or on the boundary)
     *
     * @param rect a axis-aligned rectangle we which we want to find points in it.
     * @return a list of points that are inside the given rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Null argument passed!");
        }
        List<Point2D> result = new ArrayList<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Find the nearest point in the to a given point p
     *
     * @param p The point passed to find the nearest point to it.
     * @return a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!");
        }
        double min = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point : pointSet) {
            if (point.distanceSquaredTo(p) < min) {
                min = point.distanceSquaredTo(p);
                nearest = point;
            }
        }
        return nearest;
    }
}
