
public class KdTree {
	
	private class Node {
		private Point2D p;      // the point
		private RectHV rect;    // the axis-aligned rectangle corresponding to this node
		private Node lb;        // the left/bottom subtree
		private Node rt;        // the right/top subtree
		
		public Node(Point2D point, RectHV Rect, Node lb, Node rt) {
			N++;
			this.p = point;
			this.rect = Rect;
			this.lb = lb;
			this.rt = rt;
		}
	}
	
	private Node root; // root of BST
	private final static boolean VERTICAL = true;
	private int N;
   
    // construct an empty set of points 
	public KdTree() {
		N = 0;
	}
	
    // is the set empty? 
	public boolean isEmpty() {
		return N == 0;
	}
	
    // number of points in the set 
	public int size() {
		return N;
	}
	
	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		
    	// BST already contains this point
    	if (this.contains(p)) {
    		return;
    	}
		
		root = put(root, p, VERTICAL, 0, 0, 1, 1);
	}
	
    private Node put(Node x, Point2D p, boolean direction, double xmin, double ymin, double xmax, double ymax) {
    	if (x == null) {
    		RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
    		return new Node(p, rect, null, null);
    	}
    	
    	int cmp;
    	if (direction == VERTICAL) {
    		cmp = xOrderComparator(p, x.p);
    		//cmp = Point2D.X_ORDER.compare(p, x.p);
    	} else {
    		cmp = yOrderComparator(p, x.p);
    		//cmp = Point2D.Y_ORDER.compare(p, x.p);
    	}
    	
    	if (cmp < 0) {
    		if (direction == VERTICAL) {
    			xmax = x.p.x();
    		} else {
    			ymax = x.p.y();
    		}
    		x.lb = put(x.lb, p, !direction, xmin, ymin, xmax, ymax);
    	}
    	else {
    		if (direction == VERTICAL) {
    			xmin = x.p.x();
    		} else {
    			ymin = x.p.y();
    		}
    		x.rt = put(x.rt, p, !direction, xmin, ymin, xmax, ymax);
    	}

        return x;
    }
	
    // does the set contain point p? 
	public boolean contains(Point2D p) {
		return get(p) != null;
	}
	
    private Point2D get(Point2D p) {
        return get(root, p, VERTICAL);
    }
    
    private Point2D get(Node x, Point2D key, boolean direction) {
        if (x == null) return null;
        
        // find the point
        if (key.compareTo(x.p) == 0) {
        	return x.p;
        }
        
    	int cmp;
    	if (direction == VERTICAL) {
    		cmp = xOrderComparator(key, x.p);
    		//cmp = Point2D.X_ORDER.compare(key, x.p);
    	} else {
    		cmp = yOrderComparator(key, x.p);
    		//cmp = Point2D.Y_ORDER.compare(key, x.p);
    	}
    	
        if (cmp < 0) {
        	return get(x.lb, key, !direction);
        }
        else {
        	return get(x.rt, key, !direction);
        }
    }
	
    // draw all points to standard draw
	public void draw() {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		drawPoints(root);
		StdDraw.setPenRadius();
		StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);
		drawLines(root, VERTICAL);
	}
	
	private void drawLines(Node x, boolean direction) {
		if (x == null) return;
		
		if (direction == VERTICAL) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
		}
		drawLines(x.lb, !direction);
		drawLines(x.rt, !direction);
	}
	
	private void drawPoints(Node x) {
		if (x == null) return;
		x.p.draw();
		drawPoints(x.lb);
		drawPoints(x.rt);
	}
	
    // all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new java.lang.NullPointerException();
		}
		Queue<Point2D> queue = new Queue<Point2D>();
		
		rangePoint(rect, queue);
		return queue;
	}

	private void rangePoint(RectHV rect, Queue<Point2D> queue) {
		range(root, rect, queue);
	}

	private void range(Node x, RectHV rect, Queue<Point2D> queue) {
		if (x == null) return;
		if (x.rect.intersects(rect)) {
			if (rect.contains(x.p)) {
				queue.enqueue(x.p);
			}

			range(x.lb, rect, queue);
			range(x.rt, rect, queue);
			
		} else {
			return;
		}
	}

    // a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		if (p == null || this.isEmpty()) {
			return null;
		}
		
		Point2D nearestP = root.p;
		double distance = Double.MAX_VALUE;
		return nearest(root, p, nearestP, distance, VERTICAL);
	}
	
	private Point2D nearest(Node x, Point2D p, Point2D nearestP, double distance, boolean direction) {
		if (x == null) {
			return nearestP;
		}
		
		if (x.rect.distanceSquaredTo(p) > distance) {
			return nearestP;
		}
		
		double dist = p.distanceSquaredTo(x.p);
		if (dist < distance) {
			distance = dist;
			nearestP = x.p;
		}
		
    	int cmp;
    	if (direction == VERTICAL) {
    		cmp = xOrderComparator(p, x.p);
    		//cmp = Point2D.X_ORDER.compare(p, x.p);
    	} else {
    		cmp = yOrderComparator(p, x.p);
    		//cmp = Point2D.Y_ORDER.compare(p, x.p);
    	}
		
    	if (cmp < 0) {
    		nearestP = nearest(x.lb, p, nearestP, distance, !direction);
    		distance = p.distanceSquaredTo(nearestP); // recompute the smallest distance
    		nearestP = nearest(x.rt, p, nearestP, distance, !direction);
    	} else {
    		nearestP = nearest(x.rt, p, nearestP, distance, !direction);
    		distance = p.distanceSquaredTo(nearestP); // recompute the smallest distance
    		nearestP = nearest(x.lb, p, nearestP, distance, !direction);
    	}
		
		return nearestP;
	}
	
	private int xOrderComparator(Point2D p, Point2D q) {
        if (p.x() < q.x()) return -1;
        if (p.x() > q.x()) return +1;
        return 0;
	}
	
	private int yOrderComparator(Point2D p, Point2D q) {
        if (p.y() < q.y()) return -1;
        if (p.y() > q.y()) return +1;
        return 0;
	}

    // unit testing of the methods (optional) 
	public static void main(String[] args) {
		KdTree tree = new KdTree();
		tree.insert(new Point2D(0.7, 0.2));
		tree.insert(new Point2D(0.5, 0.4));
		tree.insert(new Point2D(0.2, 0.3));
		tree.insert(new Point2D(0.4, 0.7));
		tree.insert(new Point2D(0.9, 0.6));
		System.out.println(tree.size());
		
		System.out.println(tree.contains(new Point2D(0.1, 0.6)));
		System.out.println(tree.contains(new Point2D(0.9, 0.6)));
		tree.draw();
		
		System.out.println(tree.range(new RectHV(0, 0, 1, 1)));
		System.out.println(tree.nearest(new Point2D(0.9, 0.64)));


		StdDraw.show(0);
		KdTree kdtree = new KdTree();
		while (true) {
			if (StdDraw.mousePressed()) {
				double x = StdDraw.mouseX();
				double y = StdDraw.mouseY();
				System.out.printf("%8.6f %8.6f\n", x, y);
				Point2D p = new Point2D(x, y);
				kdtree.insert(p);
				StdDraw.clear();
				kdtree.draw();
			}
			StdDraw.show(50);
		}
	}
}
