import java.util.HashMap;
import java.util.Map;


public class BoggleSolver {
//	private TST<Integer> tst;
//	private TST<Integer> finded;
	
	private TrieST<Integer> tst;
	private TrieST<Integer> finded;

	// Initializes the data structure using the given array of strings as the
	// dictionary.
	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		//tst = new TST<Integer>();
		tst = new TrieST<Integer>();
		for (String str : dictionary) {
			tst.put(str, 1);
		}
	}

	// Returns the set of all valid words in the given Boggle board, as an
	// Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		Queue<String> queue = new Queue<String>();
		//finded = new TST<Integer>();
		finded = new TrieST<Integer>();

		// DFS for all beginner
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				Queue<String> words = findWords(board, i, j);
				while (!words.isEmpty()) {
					String word = words.dequeue();
					queue.enqueue(word);
				}
			}
		}

		return queue;
	}

	private Queue<String> findWords(BoggleBoard board, int i, int j) {
		boolean[][] visited = new boolean[board.rows()][board.cols()];
		visited[i][j] = true;
		char letter = board.getLetter(i, j);
		String prefix = "";
		if (letter == 'Q') {
			prefix += "QU";
		} else {
			prefix += letter;
		}

		Queue<String> find = new Queue<String>();
		DFS(board, i, j, prefix, visited, find);
		return find;
	}
	
	private void visit(int i, int j, BoggleBoard board, boolean[][] visited, Queue<String> find, String prefix) {
		visited[i][j] = true;
		char letter = board.getLetter(i, j);
		String str = prefix;
		if (letter == 'Q') {
			str += "QU";
		} else {
			str += letter;
		}
		DFS(board, i, j, str, visited, find);
		visited[i][j] = false;
	}

	private void DFS(BoggleBoard board, int i, int j, String prefix,
			boolean[][] visited, Queue<String> find) {
		if (prefix.length() >= 3) {
			boolean res = tst.hasWords(prefix);
			if (!res) {
				return;
			}
		}

		if (prefix.length() >= 3) {
			if (tst.contains(prefix)) {
				if (!finded.contains(prefix)) {
					find.enqueue(prefix);
					finded.put(prefix, 1);
				}
			}
		}

		if (i + 1 < board.rows() && !visited[i + 1][j]) {
			visit(i + 1, j, board, visited, find, prefix);
		}

		if (j + 1 < board.cols() && !visited[i][j + 1]) {
			visit(i, j + 1, board, visited, find, prefix);
		}

		if (i - 1 >= 0 && !visited[i - 1][j]) {
			visit(i - 1, j, board, visited, find, prefix);
		}

		if (j - 1 >= 0 && !visited[i][j - 1]) {
			visit(i, j - 1, board, visited, find, prefix);
		}

		if (i - 1 >= 0 && j - 1 >= 0 && !visited[i - 1][j - 1]) {
			visit(i - 1, j - 1, board, visited, find, prefix);
		}

		if (i + 1 < board.rows() && j - 1 >= 0 && !visited[i + 1][j - 1]) {
			visit(i + 1, j - 1, board, visited, find, prefix);
		}

		if (i + 1 < board.rows() && j + 1 < board.cols()
				&& !visited[i + 1][j + 1]) {
			visit(i + 1, j + 1, board, visited, find, prefix);
		}

		if (i - 1 >= 0 && j + 1 < board.cols() && !visited[i - 1][j + 1]) {
			visit(i - 1, j + 1, board, visited, find, prefix);
		}
	}

	// Returns the score of the given word if it is in the dictionary, zero
	// otherwise.
	// (You can assume the word contains only the uppercase letters A through
	// Z.)
	public int scoreOf(String word) {
		if (tst.contains(word)) {
			int length = word.length();
			if (length <= 2) {
				return 0;
			}
			if (length <= 4) {
				return 1;
			}
			if (length <= 5) {
				return 2;
			}
			if (length <= 6) {
				return 3;
			}
			if (length <= 7) {
				return 5;
			}
			return 11;
		} else {
			return 0;
		}
	}

	private class TST<Value> {
		private int N; // size
		private Node root; // root of TST

		private class Node {
			private char c; // character
			private Node left, mid, right; // left, middle, and right subtries
			private Value val; // value associated with string
		}

		/**************************************************************
		 * Is string key in the symbol table?
		 **************************************************************/
		public boolean contains(String key) {
			return get(key) != null;
		}

		public Value get(String key) {
			if (key == null)
				throw new NullPointerException();
			if (key.length() == 0)
				throw new IllegalArgumentException("key must have length >= 1");
			Node x = get(root, key, 0);
			if (x == null)
				return null;
			return x.val;
		}

		// return subtrie corresponding to given key
		private Node get(Node x, String key, int d) {
			if (key == null)
				throw new NullPointerException();
			if (key.length() == 0)
				throw new IllegalArgumentException("key must have length >= 1");
			if (x == null)
				return null;
			char c = key.charAt(d);
			if (c < x.c)
				return get(x.left, key, d);
			else if (c > x.c)
				return get(x.right, key, d);
			else if (d < key.length() - 1)
				return get(x.mid, key, d + 1);
			else
				return x;
		}

		/**************************************************************
		 * Insert string s into the symbol table.
		 **************************************************************/
		public void put(String s, Value val) {
			if (!contains(s))
				N++;
			root = put(root, s, val, 0);
		}

		private Node put(Node x, String s, Value val, int d) {
			char c = s.charAt(d);
			if (x == null) {
				x = new Node();
				x.c = c;
			}
			if (c < x.c)
				x.left = put(x.left, s, val, d);
			else if (c > x.c)
				x.right = put(x.right, s, val, d);
			else if (d < s.length() - 1)
				x.mid = put(x.mid, s, val, d + 1);
			else
				x.val = val;
			return x;
		}
		
		public boolean hasWords(String prefix) {
			Node x = get(root, prefix, 0);
			if (x == null)
				return false;
			if (x.val != null)
				return true;
			
			return collect2(x.mid, prefix);
		}
		
		private boolean collect2(Node x, String prefix) {
			if (x == null) {
				return false;
			}
			return true;
		}

		// all keys starting with given prefix
		public Iterable<String> prefixMatch(String prefix) {
			Queue<String> queue = new Queue<String>();
			Node x = get(root, prefix, 0);
			if (x == null)
				return queue;
			if (x.val != null)
				queue.enqueue(prefix);
			collect(x.mid, prefix, queue);
			return queue;
		}

		// all keys in subtrie rooted at x with given prefix
		private void collect(Node x, String prefix, Queue<String> queue) {
			if (x == null)
				return;
			collect(x.left, prefix, queue);
			if (x.val != null)
				queue.enqueue(prefix + x.c);
			collect(x.mid, prefix + x.c, queue);
			collect(x.right, prefix, queue);
		}
	}
	
	private static final int R = 26;        // extended ASCII
    // R-way trie node
    private class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

	private class TrieST<Value> {
	    private Node root;      // root of trie
	    private int N;          // number of keys in trie

	    public TrieST() {
	    }

	    public Value get(String key) {
	        Node x = get(root, key, 0);
	        if (x == null) return null;
	        return (Value) x.val;
	    }

	    public boolean contains(String key) {
	        return get(key) != null;
	    }

	    private Node get(Node x, String key, int d) {
	        if (x == null) return null;
	        if (d == key.length()) return x;
	        int c = key.charAt(d);
	        c = c - 65;
	        return get(x.next[c], key, d+1);
	    }

	    public void put(String key, Value val) {
	        root = put(root, key, val, 0);
	    }

	    private Node put(Node x, String key, Value val, int d) {
	        if (x == null) x = new Node();
	        if (d == key.length()) {
	            if (x.val == null) N++;
	            x.val = val;
	            return x;
	        }
	        int c = key.charAt(d);
	        c = c - 65;
	        x.next[c] = put(x.next[c], key, val, d+1);
	        return x;
	    }
	    
	    public boolean hasWords(String prefix) {
	    	Node x = get(root, prefix, 0);
	    	if (x == null) {
	    		return false;
	    	}  else {
	    		return true;
	    	}
	    }

//	    public Iterable<String> keysWithPrefix(String prefix) {
//	        Queue<String> results = new Queue<String>();
//	        Node x = get(root, prefix, 0);
//	        collect(x, new StringBuilder(prefix), results);
//	        return results;
//	    }
//
//	    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
//	        if (x == null) return;
//	        if (x.val != null) results.enqueue(prefix.toString());
//	        for (char c = 0; c < R; c++) {
//	            prefix.append(c);
//	            collect(x.next[c], prefix, results);
//	            prefix.deleteCharAt(prefix.length() - 1);
//	        }
//	    }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args[1]);
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
	}

}
