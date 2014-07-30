import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Tomasz Szerle
 * @version 0.1
 *
 */
public class KdTree {

    private static class Node {
        private Node left;
        private Node right;
        private Point2D point;
        private boolean vertical;

        // public Node(Point2D p, boolean v, RectHV rect, Node l, Node r) {
        // left = l;
        // right = r;
        // rectangle = rect;
        // point = p;
        // vertical = v;
        // }

        public Node(Point2D p, boolean v) {
            left = null;
            right = null;
            point = p;
            vertical = v;
        }
    }

    private static final RectHV CONTAINER = new RectHV(0, 0, 1, 1);
    private Node root;
    private int size;
    

    /**
     * construct an empty tree of points
     */
    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * is the tree empty?
     * 
     * @return
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * number of points in the tree
     * 
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * add the point p to the tree (if it is not already in the tree)
     * 
     * @param p
     */
    public void insert(Point2D p) {
        root = insertRecursive(root, p, true);
    }

    /**
     * does the tree contain the point p?
     * 
     * @param p
     * @return
     */
    public boolean contains(Point2D p) {
        return containsRecursive(root, p);
    }

    /**
     * draw all of the points to standard draw
     */
    public void draw() {
        StdDraw.setScale(0, 1);
        drawRecursive(root, CONTAINER);
    }

    /**
     * all points in the set that are inside the rectangle
     * 
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> inRange = new LinkedList<Point2D>();
        rangeRecursive(root, CONTAINER, rect, inRange);
        return inRange;
    }

    /**
     * a nearest neighbor in the tree to p; null if tree is empty
     * 
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p) {
        return nearestRecursive(root, CONTAINER, p, null);
    }

    private Node insertRecursive(Node node, Point2D p, boolean vertical) {
        if (node == null) {
            size++;
            return new Node(p, vertical);
        }
        if (node.point.equals(p))
            return node;

        if (node.vertical) {
            if (p.x() < node.point.x()) {
                node.left = insertRecursive(node.left, p, false);
            } else {
                node.right = insertRecursive(node.right, p, false);

            }
        } else {
            if (p.y() < node.point.y()) {
                node.left = insertRecursive(node.left, p, true);
            } else {
                node.right = insertRecursive(node.right, p, true);
            }
        }
        return node;
    }
    
    private boolean containsRecursive(Node node, Point2D p) {
        if (node == null)
            return false;
        if (node.point.equals(p))
            return true;

        if ((node.vertical && p.x() < node.point.x())
                || (!node.vertical && p.y() < node.point.y()))
            return containsRecursive(node.left, p);
        else
            return containsRecursive(node.right, p);
    }

    private void drawRecursive(Node node, RectHV rect) {
        if (node == null)
            return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        Point2D min, max;
        if (node.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            min = new Point2D(node.point.x(), rect.ymin());
            max = new Point2D(node.point.x(), rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            min = new Point2D(rect.xmin(), node.point.y());
            max = new Point2D(rect.xmax(), node.point.y());
        }
        StdDraw.setPenRadius();
        min.drawTo(max);

        drawRecursive(node.left, getRect(true, node, rect));
        drawRecursive(node.right, getRect(false, node, rect));
    }

    private void rangeRecursive(Node node, RectHV nodeRect, final RectHV rect, final List<Point2D> inRange) {
        if (node == null)
            return;

        if (rect.intersects(nodeRect)) {
            if (rect.contains(node.point))
                inRange.add(node.point);
            rangeRecursive(node.left, getRect(true, node, nodeRect), rect, inRange);
            rangeRecursive(node.right, getRect(false, node, nodeRect), rect, inRange);
        }
    }

    private Point2D nearestRecursive(Node node, RectHV nodeRect, Point2D p, final Point2D currentNearest) {
        if (node == null)
            return currentNearest;

        double distFromCurrentPoint = 0;
        double distFromNewRect = 0;
        Point2D nearest = currentNearest;

        if (nearest != null) {
            distFromCurrentPoint = p.distanceSquaredTo(nearest);
            distFromNewRect = nodeRect.distanceSquaredTo(p);
        }

        if (nearest == null || distFromCurrentPoint > distFromNewRect) {
            final Point2D point = node.point;
            if (nearest == null || distFromCurrentPoint > p.distanceSquaredTo(point))
                nearest = point;

            if ((node.vertical && p.x() < node.point.x())
                    || (!node.vertical && p.y() < node.point.y())) {
                nearest = nearestRecursive(node.left, getRect(true, node, nodeRect), p, nearest);
                nearest = nearestRecursive(node.right, getRect(false, node, nodeRect), p, nearest);
            } else {
                nearest = nearestRecursive(node.right, getRect(false, node, nodeRect), p, nearest);
                nearest = nearestRecursive(node.left, getRect(true, node, nodeRect), p, nearest);
            }
        }
        return nearest;
    }

    private RectHV getRect(boolean left, Node node, RectHV rect) {
        RectHV ret = null;
        if (node.vertical) {
            if (left)
                ret = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
            else
                ret = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            if (left)
                ret = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
            else
                ret = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
        }
        
        return ret;
    }
}