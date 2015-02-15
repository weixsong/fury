import java.util.Arrays;

public class Brute {
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
		   //System.out.println(points[i].toString());
		   points[i].draw();
		   i++;
	   }
	   
	   for (i = 0; i < n - 3; i++) {
		   for (int j = i + 1; j < n - 2; j++) {
			   double slope_ij = points[j].slopeTo(points[i]);
			   
			   for (int k = j + 1; k < n - 1; k++) {
				   double slope_ik = points[k].slopeTo(points[i]);
				   if (slope_ij != slope_ik) continue;
				   
				   for (int m = k + 1; m < n; m++) {
					   double slope_im = points[m].slopeTo(points[i]);
					   if (slope_im == slope_ij) {
						   Point[] p = new Point[4];
						   p[0] = points[i];
						   p[1] = points[j];
						   p[2] = points[k];
						   p[3] = points[m];
						   Arrays.sort(p);
						   // find a line with 4 points
						   System.out.print(p[0].toString());
						   System.out.print(" -> ");
						   System.out.print(p[1].toString());
						   System.out.print(" -> ");
						   System.out.print(p[2].toString());
						   System.out.print(" -> ");
						   System.out.print(p[3].toString());
						   System.out.println();
						   
						   p[3].drawTo(p[0]);
					   }
				   }
			   }
		   }
	   }
   }
}
