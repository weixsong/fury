import java.awt.Color;


public class SeamCarver {
	
	private int color[][];
	private int column;
	private int row;
	
	private final int BOARD_ENERGE = 255 * 255 + 255 * 255 + 255 * 255;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		column = picture.width();
		row = picture.height();
		color = new int[row][column];

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				color[i][j] = picture.get(j, i).getRGB();
			}
		}
	}

	// current picture
	public Picture picture() {
		Picture p = new Picture(column, row);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				Color c = new Color(color[i][j]);
				p.set(j, i, c);
			}
		}
		
		return p;
	}

	// width of current picture
	public int width() {
		return column;
	}

	// height of current picture
	public int height() {
		return row;
	}

	/* energy of pixel at column x and row y
	 * x, column, width
	 * y, row, height
	 */
	public double energy(int x, int y) {
		if (x < 0 || x > width() - 1) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		
		if (y < 0 || y > height() - 1) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		
		// border
		if ((x == 0 || x == width() - 1) || (y == 0 || y == height() - 1)) {
			return this.BOARD_ENERGE;
		}
		
		return energy_x(y, x) + energy_y(y, x);
	}
	
	// row x and column y
	private double energy_x(int x, int y) {
		Color x1 = new Color(color[x + 1][y]);
		Color x2 = new Color(color[x - 1][y]);
		double rx = Math.abs(x1.getRed() - x2.getRed());
		double gx = Math.abs(x1.getGreen() - x2.getGreen());
		double bx = Math.abs(x1.getBlue() - x2.getBlue());
		
		return rx * rx + gx * gx + bx * bx;
	}
	
	// row x and column y
	private double energy_y(int x, int y) {
		Color y1 = new Color(color[x][y - 1]);
		Color y2 = new Color(color[x][y + 1]);
		double ry = Math.abs(y1.getRed() - y2.getRed());
		double gy = Math.abs(y1.getGreen() - y2.getGreen());
		double by = Math.abs(y1.getBlue() - y2.getBlue());
		
		return ry * ry + gy * gy + by * by;
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		transpose();
		int[] res = findVerticalSeam();
		transpose();
		return res;
	}
	
	private void transpose() {

		int temp2 = column;
		column = row;
		row = temp2;
		
		int[][] temp = new int[row][column];
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				temp[i][j] = color[j][i];
			}
		}
		
		color = temp;
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		int virtual_top = width() * height();
		int virtual_bottom = width() * height() + 1;
		int V = width() * height() + 2;
		
		double[] distTo;         // distTo[v] = distance  of shortest s->v path
	    DirectedEdge[] edgeTo;   // edgeTo[v] = last edge on shortest s->v path
		
		double[][] pixelEnergy = new double[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				pixelEnergy[i][j] = this.energy(j, i);
			}
		}
		
	    distTo = new double[V];
        edgeTo = new DirectedEdge[V];
        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[virtual_top] = 0;
		
		// relax the virtual top
		for (int j = 0; j < width(); j++) {
			int w = this.convertXY2one(0, j);
			DirectedEdge e = new DirectedEdge(virtual_top, w, 0);
			relax(e, distTo, edgeTo);
		}
		
		// relax the matrix
		for (int i = 0; i < height(); i++) {
			for (int j = 0; j < width(); j++) {
				// relax all 3 possibilities (i, j)
				int v = this.convertXY2one(i, j);
				if (i + 1 < height() && j - 1 >= 0) {
					int w = this.convertXY2one(i + 1, j - 1);
					DirectedEdge e = new DirectedEdge(v, w, pixelEnergy[i + 1][j - 1]);
					relax(e, distTo, edgeTo);
				}
				
				if (i + 1 < height()) {
					int w = this.convertXY2one(i + 1, j);
					DirectedEdge e = new DirectedEdge(v, w, pixelEnergy[i + 1][j]);
					relax(e, distTo, edgeTo);
				}
				
				if (i + 1 < height() && j + 1 < width()) {
					int w = this.convertXY2one(i + 1, j + 1);
					DirectedEdge e = new DirectedEdge(v, w, pixelEnergy[i + 1][j + 1]);
					relax(e, distTo, edgeTo);
				}
			}
		}
		
		// relax the virtual bottom
		for (int j = 0; j < column; j++) {
			int w = virtual_bottom;
			int v = this.convertXY2one(height() - 1, j);
			DirectedEdge e = new DirectedEdge(v, w, 0);
			relax(e, distTo, edgeTo);
		}
	    
		int[] res = new int[height()];
		Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[virtual_bottom]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        //path.pop(); // pop the path of virtual top
        
        int i = 0;
        while(!path.isEmpty()) {
        	if (i >= height()) {
        		break;
        	}
        	DirectedEdge e = path.pop();
        	int w = e.to();
        	// convert to x,y
        	int y = w % width();
        	res[i++] = y;
        }
		
		return res;
	}
	
	private int convertXY2one(int x, int y) {
		return x * width() + y;
	}
	
	// relax edge e
    private void relax(DirectedEdge e, double[] distTo, DirectedEdge[] edgeTo) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }       
    }

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null) {
			throw new java.lang.NullPointerException();
		}
		
		if (width() <=1 ) {
			throw new java.lang.IllegalArgumentException();
		}
		
		sanityCheck_horizontal(seam);

		for (int i = 0; i < column; i++) {
			for (int j = seam[i]; j < height() - 1; j++) {
				color[j][i] = color[j + 1][i];
			}
		}
		
		row--;
	}
	
	private void sanityCheck_horizontal(int[] seam) {
		if (seam.length != width()) {
			throw new java.lang.IllegalArgumentException();
		}
		
		for (int i = 0; i < seam.length - 2; i++) {
			if (seam[i] < 0 || seam[i] > height() - 1) {
				throw new java.lang.IllegalArgumentException();
			}
			
			int j = i + 1;
			if ((Math.abs(seam[j] - seam[i])) > 1) {
				throw new java.lang.IllegalArgumentException();
			}
		}
		
		if (seam[seam.length - 1] < 0 || seam[seam.length - 1] > height() - 1) {
			throw new java.lang.IllegalArgumentException();
		}
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null) {
			throw new java.lang.NullPointerException();
		}
		
		if (width() <= 1) {
			throw new java.lang.IllegalArgumentException();
		}
		
		sanityCheck_vertical(seam);

		for (int i = 0; i < height(); i++) {
			// shift
			for (int j = seam[i]; j < width() - 1; j++) {
				color[i][j] = color[i][j + 1];
			}
		}

		column--;
	}
	
	private void sanityCheck_vertical(int[] seam) {
		if (seam.length != height()) {
			throw new java.lang.IllegalArgumentException();
		}
		
		for (int i = 0; i < seam.length - 2; i++) {
			if (seam[i] < 0 || seam[i] > width() - 1) {
				throw new java.lang.IllegalArgumentException();
			}
			
			int j = i + 1;
			if ((Math.abs(seam[j] - seam[i])) > 1) {
				throw new java.lang.IllegalArgumentException();
			}
		}
		
		if (seam[seam.length - 1] < 0 || seam[seam.length - 1] > width() - 1) {
			throw new java.lang.IllegalArgumentException();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//        Picture inputImg = new Picture(args[0]);
//        System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
//        inputImg.show();        
//        SeamCarver sc = new SeamCarver(inputImg);
//        
//        System.out.printf("Displaying energy calculated for each pixel.\n");
//        SCUtility.showEnergy(sc);
	}

}
