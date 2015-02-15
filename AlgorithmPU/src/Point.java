import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

	// compare points by slope
	public final Comparator<Point> SLOPE_ORDER = new SLOPE_ORDER();

	private final int x; // x coordinate
	private final int y; // y coordinate

	// create the point (x, y)
	public Point(int x, int y) {
		/* DO NOT MODIFY */
		this.x = x;
		this.y = y;
	}

	// plot this point to standard drawing
	public void draw() {
		/* DO NOT MODIFY */
		StdDraw.point(x, y);
	}

	// draw line between this point and that point to standard drawing
	public void drawTo(Point that) {
		/* DO NOT MODIFY */
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	// slope between this point and that point
	public double slopeTo(Point that) {
		if (that.x == this.x && that.y == this.y) {
			return Double.NEGATIVE_INFINITY;
		}
		if (that.y == this.y) {
			double a = 1.0;
			return (a - a) / a;
		}
		if (that.x == this.x) {
			return Double.POSITIVE_INFINITY;
		}

		return ((double) (that.y - this.y)) / ((double) (that.x - this.x));
	}

	// is this point lexicographically smaller than that one?
	// comparing y-coordinates and breaking ties by x-coordinates
	public int compareTo(Point that) {
		if (this.y < that.y)
			return -1;
		if (this.y > that.y)
			return +1;
		if (this.x < that.x)
			return -1;
		if (this.x > that.x)
			return +1;
		return 0;
	}

	private class SLOPE_ORDER implements Comparator<Point> {
		public int compare(Point p1, Point p2) {
			if (p1 == null || p2 == null) {
				throw new java.lang.NullPointerException();
			}
			double slope1 = slopeTo(p1);
			double slope2 = slopeTo(p2);
			if (slope1 < slope2) {
				return -1;
			} else if (slope1 > slope2) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	// return string representation of this point
	public String toString() {
		/* DO NOT MODIFY */
		return "(" + x + ", " + y + ")";
	}

	// unit test
	public static void main(String[] args) {
		/* YOUR CODE HERE */
		Point p1 = new Point(2, 8);
		Point p2 = new Point(12, 87);
		Point p3 = new Point(2, 17);

		Point[] p = new Point[3];
		p[0] = p3;
		p[1] = p2;
		p[2] = p1;

		Point[] pp = new Point[3];
		for (int i = 0; i < 3; i++) {
			pp[i] = p[i];
		}

		System.out.println();
		Arrays.sort(pp);
		for (int i = 0; i < 3; i++) {
			System.out.print(pp[i]);
		}
		System.out.println();
		
		for (int i = 0; i < 3; i++) {
			System.out.print(p[i]);
		}
	}
}