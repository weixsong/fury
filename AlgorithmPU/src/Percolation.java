public class Percolation {

	private int N;
	private boolean[] grid;
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF uf2; // used to stop backwash
	private int virtualTop;
	private int virtualBottom;

	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.N = n;
		grid = new boolean[N * N];
		for (int i = 0; i < N * N; i++) {
			grid[i] = false;
		}

		uf = new WeightedQuickUnionUF(N * N + 2);
		uf2 = new WeightedQuickUnionUF(N * N + 1);
		virtualTop = N * N;
		virtualBottom = N * N + 1;
	}

	/**
	 * open a site.
	 * 
	 * @param i
	 * @param j
	 */
	public void open(int i, int j) {
		i = i - 1;
		j = j - 1;
		indiceValidate(i);
		indiceValidate(j);

		int p = xyTo1D(i, j);
		if (this.grid[p] == true) {
			return;
		}

		this.grid[p] = true; // mark open

		if (i > 0) {
			int q = xyTo1D(i - 1, j);
			connect(p, q);
		}
		if (i < N - 1) {
			int q = this.xyTo1D(i + 1, j);
			connect(p, q);
		}
		if (j > 0) {
			int q = this.xyTo1D(i, j - 1);
			connect(p, q);
		}
		if (j < N - 1) {
			int q = this.xyTo1D(i, j + 1);
			connect(p, q);
		}
		
		if (i == 0) {
			uf.union(virtualTop, p);
			uf2.union(virtualTop, p);
		}
		if (i == N - 1) {
			uf.union(p, virtualBottom);
		}
	}
	
	private void connect(int from, int to) {
		if (this.grid[to] == true) {
			uf.union(from, to);
			uf2.union(from, to);
		}
	}

	/**
	 * is site(i, j) open.
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isOpen(int i, int j) {
		i = i - 1;
		j = j - 1;
		indiceValidate(i);
		indiceValidate(j);
		int p = this.xyTo1D(i, j);
		return this.grid[p] == true;
	}

	/**
	 * is site (row i, column j) full
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isFull(int i, int j) {
		i = i - 1;
		j = j - 1;
		indiceValidate(i);
		indiceValidate(j);
		int p = this.xyTo1D(i, j);
		if (this.grid[p] == false) {
			return false;
		}
		int q = xyTo1D(i, j);
		return uf2.connected(virtualTop, q);
	}

	/**
	 * 
	 * @return
	 */
	public boolean percolates() {
		return uf.connected(virtualTop, virtualBottom);
	}

	/**
	 * validate the index
	 * 
	 * @param i
	 */
	private void indiceValidate(int i) {
		if (i < 0 || i >= N) {
			throw new IndexOutOfBoundsException("");
		}
	}

	/**
	 * change 2 dimensional indexes to 1 dimensional index
	 * 
	 * @param x
	 *            input
	 * @param y
	 *            input
	 * @return 1-d index
	 */
	private int xyTo1D(int x, int y) {
		return x * this.N + y;
	}

	public static void main(final String[] args) {
		Percolation percolation = new Percolation(5);
		percolation.open(1, 3);
		percolation.open(2, 3);
		percolation.open(3, 3);
		percolation.open(4, 3);
		percolation.open(5, 3);

		percolation.open(5, 1); // backwash happened

		System.out.println(percolation.percolates());
		System.out.println(percolation.isFull(5, 3));
		System.out.println(percolation.isFull(5, 1));
	}
}
