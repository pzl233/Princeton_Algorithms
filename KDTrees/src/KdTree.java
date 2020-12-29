import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a set of points in the unit square.
 * It uses a 2d-tree, which is a generalization of a BST to two-dimensional keys to find
 * a list of points in a given rectangle, and the nearest point of a given point.
 * The idea is to build a BST with points in the nodes, using the x- and y-coordinates of the points
 * as keys in strictly alternating sequence.
 */
public class KdTree {
    private Node root;
    private int size;

    /**
     * This class represents each node of the 2d-tree.
     */
    private class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private final boolean isOddLevel;
        private RectHV rect;

        /**
         * Create a node with given point, its left child, its right child, and if it is in odd level or not.
         * @param point the Point2D inside a node
         * @param left the left child, which is "less" than the node.
         * @param right the right child, which is "greater" than the node.
         * @param isOddLevel if the current node is in the odd level, which is used to indicate how to compare current node and its children.
         */
        public Node(Point2D point, Node left, Node right, boolean isOddLevel) {
            this.point = point;
            this.left = left;
            this.right = right;
            this.isOddLevel = isOddLevel;
        }

        /**
         *
         * @param point the Point2D inside a node
         * @param isOddLevel if the current node is in the odd level, which is used to indicate how to compare current node and its children.
         */
        public Node(Point2D point, boolean isOddLevel) {
            this.point = point;
            this.isOddLevel = isOddLevel;
        }

        /**
         * Recursively draw the current node and its child nodes.
         */
        private void draw() {
            if (left != null) {
                left.draw();
            }
            point.draw();
            if (right != null) {
                right.draw();
            }
        }
    }
    /**
     * Initializes the 2D-Tree
     */
    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * Returns true if this 2D-tree is empty.
     *
     * @return {@code true} if this set is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the number of points in this 2D-tree.
     * @return number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * Add the point to the 2D-tree if it is not already in the set
     * @param p the point to be added
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!");
        }
        if (root == null || !contains(p)) {
            root = insertHelper(root, p, true);
            size++;
        }
    }

    /**
     * A helper function to recursively search for the place to insert the point.
     * @param node the node used to locate the right position to insert
     * @param p the point to be inserted
     * @param isOddLevel if the node is in odd level. If it is true, we compare two points by x-coordinates, else by y-coordinates.
     * @return the modified node with new point being inserted.
     */
    private Node insertHelper(Node node, Point2D p, boolean isOddLevel) {
        if (node == null) {
            return new Node(p, isOddLevel);
        }

        if (node.isOddLevel) {
            if (p.x() < node.point.x()) {
                node.left = insertHelper(node.left, p, !isOddLevel);
            } else {
                node.right = insertHelper(node.right, p, !isOddLevel);
            }
        } else {
            if (p.y() < node.point.y()) {
                node.left = insertHelper(node.left, p, !isOddLevel);
            } else {
                node.right = insertHelper(node.right, p, !isOddLevel);
            }
        }
        return node;
    }
    /**
     * To determine if the 2D-tree contain point p
     * @param p the point we want to examine if it is in the set or not.
     * @return {@code true} if the point p is in the 2D-tree.
     *         {@code false} otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!");
        }
        return containsHelper(root, p);
    }

    /**
     * A helper function to recursively find if the point p is in the 2D-tree.
     * @param node the root of the 2D-tree
     * @param p the point need to be found.
     * @return {@code true} if the point p is in the 2D-tree.
     *         {@code false} otherwise
     */
    private boolean containsHelper (Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.point)) {
            return true;
        }
        if (node.isOddLevel && p.x() < node.point.x() ||
                !node.isOddLevel && p.y() < node.point.y()) {
            return containsHelper(node.left, p);
        } else {
            return containsHelper(node.right, p);
        }
    }

    /**
     *  Draw all points in the set to standard draw
     */
    public void draw() {
        if (!isEmpty()) {
            root.draw();
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
        if (!isEmpty()) {
            rangeHelper(root, rect, result);
        }
        return result;
    }

    /**
     * A helper function to find all points contained in a given query rectangle,
     * start at the root and recursively search for points in both subtrees
     * if the query rectangle does not intersect the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     * A subtree is searched only if it might contain a point contained in the query rectangle.
     * @param node the root of the 2D-tree
     * @param rect the given query rectangle
     * @param pointsInRange the list for the point in the rectangle
     */
    private void rangeHelper(Node node, RectHV rect, List<Point2D> pointsInRange) {
        if (node == null) {
            return;
        }
        final boolean searchBoth;

        Point2D p = node.point;
        if (rect.contains(p)) {
            pointsInRange.add(p);
            searchBoth = true;
        } else if (node.isOddLevel && rect.intersects(new RectHV(p.x(), 0.0, p.x(), 1.0)) ||
                !node.isOddLevel && rect.intersects(new RectHV(0.0, p.y(), 1.0, p.y()))) {
            searchBoth = true;
        } else {
            searchBoth = false;
        }

        if (searchBoth) {
            rangeHelper(node.left, rect, pointsInRange);
            rangeHelper(node.right, rect, pointsInRange);
        } else {
            if ((node.isOddLevel && p.x() > rect.xmin()) || (!node.isOddLevel) && p.y() > rect.ymin()) {
                rangeHelper(node.left, rect, pointsInRange);
            } else {
                rangeHelper(node.right, rect, pointsInRange);
            }
        }
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
        if (root == null) {
            return null;
        }
        return nearestHelper(root, p, root).point;
    }

    /**
     * A helper function to find a closest point to a given query point,
     * start at the root and recursively search in both subtrees
     * if the closest point discovered so far is closer than the distance between the query point
     * and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees).
     * That is, search a node only only if it might contain a point that is closer than the best one found so far.
     * @param node the root of the 2D-tree
     * @param p the point used to find its nearest neighbor
     * @param nearest the nearest neighbor of the given point p
     * @return a closest point to a given query point
     */
    private Node nearestHelper(Node node, Point2D p, Node nearest) {
        if (node == null || nearest.point.distanceSquaredTo(p) < node.point.distanceSquaredTo(p)) {
            return nearest;
        }

        if (node.point.distanceSquaredTo(p) < nearest.point.distanceSquaredTo(p)) {
            nearest = node;
        }

        Node left = nearestHelper(node.left, p, nearest);
        if (left.point.distanceSquaredTo(p) < nearest.point.distanceSquaredTo(p)) {
            nearest = left;
        }

        Node right = nearestHelper(node.right, p, nearest);
        if (right.point.distanceSquaredTo(p) < nearest.point.distanceSquaredTo(p)) {
            nearest = right;
        }

        return nearest;
    }
}
