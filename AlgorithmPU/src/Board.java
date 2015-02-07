import java.lang.Math;

public class Board {
	
	private int[][] tiles;
	private int N;

	/*
	 * construct a board from an N-by-N array of blocks,
	 * (where blocks[i][j] = block in row i, column j)
	 */
    public Board(int[][] blocks) {
    	this.N = blocks.length;
    	tiles = new int[N][N];
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			// copy array
    			tiles[i][j] = blocks[i][j];
    		}
    	}
    }

    /*
     * board dimension N
     */
    public int dimension() {
    	return N;
    }
    
    /*
     * number of blocks out of place
     */
    public int hamming() {
    	int count = 0;
    	int sum = N * N;
    	for (int i = 0; i < this.N; i++) {
    		for (int j = 0; j < this.N; j++) {
    			int goal = (i * this.N + j + 1) % sum;
    			if (this.tiles[i][j] != 0 && this.tiles[i][j] != goal) {
    				count++;
    			}
    		}
    	}
    	return count;
    } 
    
    /*
     * sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
    	int count = 0;
    	for (int i = 0; i < this.N; i++) {
    		for (int j = 0; j < this.N; j++) {
    			if (this.tiles[i][j] == 0) {
    				continue;
    			}
    			int number = this.tiles[i][j];
    			int targetRow = (number - 1) / this.N;
    			int targetCol = (number - 1) % this.N;
    			int cost = Math.abs(targetRow - i) + Math.abs(targetCol - j);
    			count += cost;
    		}
    	}
    	return count;
    }
    
    /*
     * is this board the goal board?
     */
    public boolean isGoal() {
    	boolean flag = true;
    	int sum = this.N * this.N;
    	for (int i = 0; i < this.N; i++) {
    		for (int j = 0; j < this.N; j++) {
    			int goal = (i * this.N + j + 1) % sum;
    			if (this.tiles[i][j] != goal) {
    				flag = false;
    				break;
    			}
    		}
    		if (flag == false) {
    			break;
    		}
    	}
    	return flag;
    }
    
    /*
     * a board that is obtained by exchanging two adjacent blocks in the same row
     */
    public Board twin() {
    	Board t = new Board(this.tiles);

    	int row = 0;
    	for (int i = 0; i < N; i++) {
    		boolean full = true;
    		for (int j = 0; j < N; j++) {
    			if (this.tiles[i][j] == 0) {
    				full = false;
    				break;
    			}
    		}
    		if (full == true) {
    			row = i;
    			break;
    		}
    	}
    	int temp = t.tiles[row][0];
    	t.tiles[row][0] = t.tiles[row][1];
    	t.tiles[row][1] = temp;
    	return t;
    }
    
    /*
     * does this board equal y?
     */
    public boolean equals(Object y) {
    	if (y == null) return false;
    	if (y == this) return true;
    	if (y.getClass() != this.getClass()) return false;
    	
    	Board that = (Board)y;
    	if (this.dimension() != that.dimension()) {
    		return false;
    	}
    	
    	for (int i = 0; i < this.N; i++) {
    		for (int j = 0; j < this.N; j++) {
    			if (this.tiles[i][j] != that.tiles[i][j]) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    /*
     * all neighboring boards
     * order is important
     */
    public Iterable<Board> neighbors() {
    	Queue<Board> neighbors = new Queue<Board>();
    	int count = 0;
    	int row = 0;
    	int col = 0;
    	for (int i = 0; i < N; i++) {
    		boolean find = false;
    		for (int j = 0; j < N; j++) {
    			if (this.tiles[i][j] == 0) {
    				row = i;
    				col = j;
    				break;
    			}
    		}
    		if (find == true) {
    			break;
    		}
    	}
    	
    	if (row - 1 >= 0) {
    		Board b = new Board(this.tiles);
    		b.exch(row, col, row - 1, col);
    		neighbors.enqueue(b);
    		count++;
    	}
    	
    	if (row + 1 < this.N) {
    		Board b = new Board(this.tiles);
    		b.exch(row, col, row + 1, col);
    		neighbors.enqueue(b);
    		count++;
    	}
    	
    	if (col - 1 >= 0) {
    		Board b = new Board(this.tiles);
    		b.exch(row, col, row, col - 1);
    		neighbors.enqueue(b);
    		count++;
    	}
    	
    	if (col + 1 < this.N) {
    		Board b = new Board(this.tiles);
    		b.exch(row, col, row, col + 1);
    		neighbors.enqueue(b);
    		count++;
    	}
    	
    	Board[] b = new Board[count];
    	for (int i = 0; i < count; i++) {
    		b[i] = neighbors.dequeue();
    	}
    	
    	Queue<Board> results = new Queue<Board>();
    	for (int i = 0; i < count; i++) {
    		int small = i;
    		for (int j = i + 1; j < count; j++) {
    			if (b[j].manhattan() < b[small].manhattan()) {
    				small = j;
    			}
    		}
    		Board temp = b[i];
    		b[i] = b[small];
    		b[small] = temp;
    	}
    	
    	for (int i = 0; i < count; i++) {
    		results.enqueue(b[i]);
    	}
    	
    	return results;
    }
    
    private void exch(int x1, int y1, int x2, int y2) {
    	int temp = this.tiles[x1][y1];
    	this.tiles[x1][y1] = this.tiles[x2][y2];
    	this.tiles[x2][y2] = temp;
    }
    
    /*
     * string representation of this board (in the output format specified below)
     */
    public String toString() {
    	StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
    	int N = 5;
        int[][] blocks = new int[N][N];
        System.out.println(blocks.length);
    }
}
