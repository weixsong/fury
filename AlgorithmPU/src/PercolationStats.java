
public class PercolationStats {
	private int N;  // size of percolation N * N
	private int T;  // number of tests
	private double[] probablities;
	
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException("invalid arguments");
		}

		this.N = N;
		this.T = T;
		probablities = new double[T];
		int blocks = N * N;

		Percolation percolation = null;
		for (int i = 0; i < this.T; i++) {
			percolation = null;
			percolation = new Percolation(this.N);

			while (!percolation.percolates()) {
				int x = StdRandom.uniform(1, this.N + 1);
				int y = StdRandom.uniform(1, this.N + 1);
				if (!percolation.isOpen(x, y)) {
					percolation.open(x, y);
				}
			}

			int count = 0;
			for (int x = 1; x <= this.N; x++) {
				for (int y = 1; y <= this.N; y++) {
					if (percolation.isOpen(x, y)) {
						count++;
					}
				}
			}

			probablities[i] = (double)count / (double)blocks;
		}
	}

	public double mean() {
		double avg = 0.0;
		for (int i = 0; i < T; i++) {
			avg += probablities[i];
		}
		return avg / T;
	}

	public double stddev() {
		double dev = 0.0;
		double mean = this.mean();
		for (int i = 0; i < this.T; i++) {
			double err = (double)probablities[i] - mean;
			dev += err * err;
		}
		dev = dev / (double)(this.T - 1);
		return Math.sqrt(dev);
	}
	
    // returns lower bound of the 95% confidence interval
	public double confidenceLo() {
		double mean = this.mean();
		double dev = this.stddev();
		return mean - 1.96 * dev / Math.sqrt(this.T);
	}
	
    // returns upper bound of the 95% confidence interval
	public double confidenceHi() {
		double mean = this.mean();
		double dev = this.stddev();
		return mean + 1.96 * dev / Math.sqrt(this.T);
	}
	
	public static void main(String[] args) {
		PercolationStats ps = new PercolationStats(20, 10);
		System.out.println(ps.mean());
		System.out.println(ps.stddev());
	}
}
