public class Solver {

	private class Node implements Comparable<Node> {
		public Board board = null;
		public Node pre = null;
		public int manhattan = 0;
		public int moves = 0;

		public Node(Board b, Node parent) {
			board = b;
			manhattan = b.manhattan();
			moves = parent == null ? 0 : parent.moves + 1;
			pre = parent;
		}

		@Override
		public int compareTo(Node that) {
			if ((this.manhattan + this.moves) > (that.manhattan + that.moves)) {
				return 1;
			} else if ((this.manhattan + this.moves) < (that.manhattan + that.moves)) {
				return -1;
			} else {
				if (this.manhattan > that.manhattan) {
					return 1;
				} else if (this.manhattan < that.manhattan) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	private MinPQ<Node> pq = null;
	private MinPQ<Node> pq2 = null;

	private Node current = null;
	private Node current2 = null;

	private boolean isSolvable;

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		pq = new MinPQ<Node>();
		pq2 = new MinPQ<Node>();

		isSolvable = true;

		Node node = new Node(initial, null);
		pq.insert(node);

		Board twin = initial.twin();
		Node node2 = new Node(twin, null);
		pq2.insert(node2);

		while (!pq.isEmpty() && !pq2.isEmpty()) {
			current = pq.delMin();
			current2 = pq2.delMin();

			// test if solvable
			if (current.board.isGoal()) {
				break;
			} else if (current2.board.isGoal()) {
				isSolvable = false;
				break;
			}

			// add neighbors to PQ
			for (Board b : current.board.neighbors()) {
				Node n = new Node(b, current);
				if (current.pre == null) {
					pq.insert(n);
				} else if (!current.pre.board.equals(b)) {
					pq.insert(n);
				}
			}

			for (Board b : current2.board.neighbors()) {
				Node n = new Node(b, current2);
				if (current2.pre == null) {
					pq2.insert(n);
				} else if (!current2.pre.board.equals(b)) {
					pq2.insert(n);
				}
			}
		}
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return isSolvable;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if (!isSolvable()) {
			return -1;
		}
		return current.moves;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		if (!isSolvable()) {
			return null;
		}
		Stack<Board> solution = new Stack<Board>();
		for (Node node = current; node != null; node = node.pre) {
			solution.push(node.board);
		}
		return solution;
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {
		// create initial board from file
		System.out.println(args[0]);
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
