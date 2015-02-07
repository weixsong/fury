import java.util.ArrayList;
import java.util.Arrays;

public class Fast {
   public static void main(String[] args) {
	   In in = new In(args[0]);
	   int n = in.readInt();
	   Point[] points = new Point[n];
	   int i = 0;
	   StdDraw.setXscale(0, 32768);
	   StdDraw.setYscale(0, 32768);
	   while (i < n) {
		   int x = in.readInt();
		   int y = in.readInt();
		   points[i] = new Point(x, y);
		   points[i].draw();
		   i++;
	   }
	   Arrays.sort(points);
	   
	   
	   // begin process
	   ArrayList<Point> visitP = new ArrayList<Point>();
	   for (i = 0; i < n; i++) {
		   // copy array
		   Point[] pointCopy = new Point[points.length];
		   for (int j = 0; j < points.length; j++) {
			   pointCopy[j] = points[j];
		   }
		   
		   Point p = pointCopy[i];
		   Arrays.sort(pointCopy, 0, n, p.SLOPE_ORDER);
		   
		   double[] slopes = new double[n];
		   for (int j = 0; j < n; j++) {
			   slopes[j] = p.slopeTo(pointCopy[j]);
		   }
		   
		   
		   for (int j = 0; j < n - 1; ) {
			   int start = j;
			   while (slopes[start] == slopes[j + 1]) {
				   j++;
				   if (j >= n - 1) break;
			   }
			   int end = j;
			   if (end - start >= 2) {
				   // find a line
				   int num = end - start + 2;
				   boolean flag = false;
				   for (int k = start; k <= end; k++) {
					   if (visitP.contains(pointCopy[k])) {
						   flag = true;
					   }
				   }
				   if (flag == true) {
					   break;
				   }
				   
				   Point[] pp = new Point[num];
				   pp[0] = p;
				   
				   for (int k = num - 1; k > 0; k--) {
					   pp[k] = pointCopy[j - ( num - 1 - k)];
				   }
				   Arrays.sort(pp);
				   
				   for (int k = 0; k < num; k++) {
					   System.out.print(pp[k].toString());
					   if (k < num -1) {
						   System.out.print(" -> ");
					   }
				   }
				   System.out.println();
				   pp[0].drawTo(pp[num - 1]);
			   }
			   j++;
		   }
		   visitP.add(p);
	   }
   }
}
