import java.util.Arrays;
import java.util.HashSet;

public class Fast {
public static void main(String[] args) {
	   In in = new In(args[0]);
	   int n = in.readInt();
	   Point[] points = new Point[n];
	   Point[] pointsCopy = new Point[n];
	   int i = 0;
	   StdDraw.setXscale(0, 32768);
	   StdDraw.setYscale(0, 32768);
	   while (i < n) {
		   int x = in.readInt();
		   int y = in.readInt();
		   points[i] = new Point(x, y);
		   pointsCopy[i] = new Point(x, y);
		   points[i].draw();
		   i++;
	   }
	   
	   HashSet<String> hash = new HashSet<String>();

	   // begin process
	   for (i = 0; i < n; i++) {
		   // change point i with 0
		   System.arraycopy(pointsCopy, 0, points, 0, n);
		   exch(points, 0, i);
		   Point basePoint = points[0];
		   Arrays.sort(points, 1, n, basePoint.SLOPE_ORDER);
		   
		   double[] slopes = new double[n];
		   for (int j = 0; j < n; j++) {
			   slopes[j] = points[j].slopeTo(basePoint);
		   }
		   
		   for (int j = 1; j < n; ) {
			   double slope = slopes[j];
			   int end = j;
			   while (end + 1 < n && slopes[end + 1] == slope) {
				   end++;
			   }

			   if (end - j >= 2) {
				   // find a line
				   int pointCount = end - j + 2;
				   
				   // shallow copy of points, just copy value type, no reference type
				   // shallow copy of Point type is OK
				   Point[] founds = new Point[pointCount];
				   founds[0] = basePoint;

				   for (int k = 1; k < pointCount; k++) {
					   founds[k] = points[j + k - 1];
				   }
				   Arrays.sort(founds); // this sort do not change the base point position
				   String hashString = founds[0].toString() + founds[pointCount - 1].toString();
				   if (hash.contains(hashString)) {
					   j = end + 1;
					   continue;
				   }
				   hash.add(hashString);
				   for (int k = 0; k < pointCount; k++) {
					   System.out.print(founds[k].toString());
					   if (k < pointCount -1) {
						   System.out.print(" -> ");
					   }
				   }
				   System.out.println();
				   founds[0].drawTo(founds[pointCount - 1]);
			   }
			   j = end + 1;
		   }
	   }
   }
   
   private static void exch(Point[] points, int i, int j) {
	   Point temp = points[i];
	   points[i] = points[j];
	   points[j] = temp;
   }
}
