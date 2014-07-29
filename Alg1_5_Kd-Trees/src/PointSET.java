import java.util.LinkedList;
import java.util.List;


/**
 * 
 * @author Tomasz Szerle
 * @version 0.1
 *
 */
public class PointSET {

	private SET<Point2D> points;
	
	/**
	 * construct an empty set of points
	 */
	public PointSET() {
		points = new SET<Point2D>();
	}

	/**
	 * is the set empty?
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return points.isEmpty();
	}

	/**
	 * number of points in the set
	 * 
	 * @return
	 */
	public int size() {
		return points.size();
	}

	/**
	 * add the point p to the set (if it is not already in the set)
	 * 
	 * @param p
	 */
	public void insert(Point2D p) {
		points.add(p);
	}

	/**
	 * does the set contain the point p?
	 * 
	 * @param p
	 * @return
	 */
	public boolean contains(Point2D p) {
		return points.contains(p);
	}

	/**
	 * draw all of the points to standard draw
	 */
	public void draw() {
		for (Point2D point : points) {
			point.draw();
		}
	}

	/**
	 * all points in the set that are inside the rectangle
	 * 
	 * @param rect
	 * @return
	 */
	public Iterable<Point2D> range(RectHV rect) {
		List<Point2D> inRange = new LinkedList<Point2D>();
		for (Point2D point : points) {
			if (rect.contains(point))
				inRange.add(point);
		}
		return inRange;
	}

	/**
	 * a nearest neighbor in the set to p; null if set is empty
	 * 
	 * @param p
	 * @return
	 */
	public Point2D nearest(Point2D p) {
		Point2D minPoint = null;
		double minDistance = Double.POSITIVE_INFINITY;
		
		for (Point2D point : points) {
			double distance = p.distanceTo(point);
			if (distance < minDistance) {
				minDistance = distance;
				minPoint = point;
			}
		}
		return minPoint;
	}
}