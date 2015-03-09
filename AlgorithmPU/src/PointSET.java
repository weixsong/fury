import java.util.*;

public class PointSET {
	
	private TreeSet<Point2D> set; 
   
    // construct an empty set of points 
	public PointSET() {
	   set = new TreeSet<Point2D>();
	}
	
    // is the set empty? 
	public boolean isEmpty() {
		return set.size() == 0;
	}
	
    // number of points in the set 
	public int size() {
		return set.size();
	}
	
	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			return;
		}
		if (!this.contains(p)) {
			set.add(p);
		}
	}
	
    // does the set contain point p? 
	public boolean contains(Point2D p) {
		return set.contains(p);
	}
	
    // draw all points to standard draw
	public void draw() {
		for (Point2D point : set) {
			point.draw();
		}
//		for (Iterator<Point2D> iter = set.iterator(); iter.hasNext(); ) {
//			iter.next().draw();
//		}
	}
	
    // all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		//Point2D[] points = (Point2D[])set.toArray();
		Queue<Point2D> queue = new Queue<Point2D>();
		for (Point2D point : set) {
			if (rect.contains(point)) {
				queue.enqueue(point);
			}
		}
//		for (Iterator<Point2D> iter = set.iterator(); iter.hasNext(); ) {
//			Point2D p = iter.next();
//			if (rect.contains(p)) {
//				queue.enqueue(p);
//			}
//		}
		
		return queue;
	}
	
    // a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		if (p == null) {
			return null;
		}
		
		if (this.isEmpty()) {
			return null;
		}
		
		Point2D nearest = set.first();
		double shortestDist = Double.MAX_VALUE;
		
		for (Iterator<Point2D> iter = set.iterator(); iter.hasNext(); ) {
			Point2D point = iter.next();
			double distance = p.distanceTo(point);
			
			if (distance < shortestDist) {
				shortestDist = distance;
				nearest = point;
			}
		}
		
		return nearest;
	}

    // unit testing of the methods (optional) 
	public static void main(String[] args) {
		
	}
}
