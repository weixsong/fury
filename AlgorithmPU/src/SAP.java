
public class SAP {

	private Digraph digraph;
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null) {
			throw new java.lang.NullPointerException();
		}
		digraph = new Digraph(G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		if (v < 0 || v > this.digraph.V() - 1) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		
		if (w < 0 || w > this.digraph.V() - 1) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		
		BreadthFirstDirectedPaths BFS_V = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths BFS_W = new BreadthFirstDirectedPaths(digraph, w);
		
		int minLength = Integer.MAX_VALUE;
		boolean found = false;
		for (int i = 0; i < digraph.V(); i++) {
			if (BFS_V.hasPathTo(i) && BFS_W.hasPathTo(i)) {
				found = true;
				int sum = BFS_V.distTo(i) + BFS_W.distTo(i);
				if (minLength > sum) {
					minLength = sum;
				}
			}
		}
		
		if (found == false) {
			return -1;
		} else {
			return minLength;
		}
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		if (v < 0 || v > this.digraph.V() - 1) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		
		if (w < 0 || w > this.digraph.V() - 1) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		
		BreadthFirstDirectedPaths BFS_V = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths BFS_W = new BreadthFirstDirectedPaths(digraph, w);
		
		int minLength = Integer.MAX_VALUE;
		int root = -1;
		boolean found = false;
		for (int i = 0; i < digraph.V(); i++) {
			if (BFS_V.hasPathTo(i) && BFS_W.hasPathTo(i)) {
				found = true;
				int sum = BFS_V.distTo(i) + BFS_W.distTo(i);
				if (minLength > sum) {
					minLength = sum;
					root = i;
				}
			}
		}
		
		return root;
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) {
			throw new java.lang.NullPointerException();
		}
		
		BreadthFirstDirectedPaths BFS_V = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths BFS_W = new BreadthFirstDirectedPaths(digraph, w);
		
		int minLength = Integer.MAX_VALUE;
		boolean found = false;
		for (int i = 0; i < digraph.V(); i++) {
			if (BFS_V.hasPathTo(i) && BFS_W.hasPathTo(i)) {
				found = true;
				int sum = BFS_V.distTo(i) + BFS_W.distTo(i);
				if (minLength > sum) {
					minLength = sum;
				}
			}
		}
		
		if (found == false) {
			return -1;
		} else {
			return minLength;
		}
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) {
			throw new java.lang.NullPointerException();
		}
		
		BreadthFirstDirectedPaths BFS_V = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths BFS_W = new BreadthFirstDirectedPaths(digraph, w);
		
		int minLength = Integer.MAX_VALUE;
		boolean found = false;
		int root = -1;
		for (int i = 0; i < digraph.V(); i++) {
			if (BFS_V.hasPathTo(i) && BFS_W.hasPathTo(i)) {
				found = true;
				int sum = BFS_V.distTo(i) + BFS_W.distTo(i);
				if (minLength > sum) {
					minLength = sum;
					root = i;
				}
			}
		}
		
		return root;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}

}
