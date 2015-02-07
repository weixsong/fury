public class BaseballElimination {

	private int N;
	private String[] teams;
	private int win[];
	private int lose[];
	private int remain[];
	private int game[][];
	private int V;
	
	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		N = in.readInt();
		V = 1 + (N - 1) * (N - 2) / 2 + (N) + 1;
		teams = new String[N];
		win = new int[N];
		lose = new int[N];
		remain = new int[N];
		game = new int[N][N];
		
		for (int i = 0; i < N; i++) {
			teams[i] = in.readString();
			win[i] = in.readInt();
			lose[i] = in.readInt();
			remain[i] = in.readInt();
			
			for (int j = 0; j < N; j++) {
				game[i][j] = in.readInt();
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return N;
	}

	// all teams
	public Iterable<String> teams() {
		Queue<String> q = new Queue<String>();
		for (int i = 0; i < N; i++) {
			q.enqueue(teams[i]);
		}
		
		return q;
	}
	
	private int findIndex(String team) {
		int index = -1;
		for (int i = 0; i < N; i++) {
			if (teams[i].equals(team)) {
				index = i;
				break;
			}
		}
		
		if (index == -1) {
			throw new java.lang.IllegalArgumentException();
		}
		
		return index;
	}

	// number of wins for given team
	public int wins(String team) {
		int index = findIndex(team);
		return win[index];
	}

	// number of losses for given team
	public int losses(String team) {
		int index = findIndex(team);
		return lose[index];
	}

	// number of remaining games for given team
	public int remaining(String team) {
		int index = findIndex(team);
		return remain[index];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		int index1 = findIndex(team1);
		int index2 = findIndex(team2);
		return game[index1][index2];
	}
	
	private void constructFlowNetwork(FlowNetwork fn, int index) {
		int s = 0;
		int vertex = 0;
		
		// add links between s and games
		for (int i = 0; i < N; i++) {
			if (i == index) {
				continue; // do not add this team
			}
			
			for (int j = i + 1; j < N; j++) {
				if (j == index) {
					continue;
				}
				
				vertex++;
				int capacity = game[i][j];
				FlowEdge fe = new FlowEdge(s, vertex, capacity);
				fn.addEdge(fe);
			}
		}
		
		int teamIndex = vertex + 1;
		// add link between game and team
		int game_index = 0;
		for (int i = 0; i < N; i++) {
			if (i == index) {
				continue; // do not add this team
			}
			
			for (int j = i + 1; j < N; j++) {
				if (j == index) {
					continue;
				}
				
				game_index++;
				int v1 = teamIndex + i;
				int v2 = teamIndex + j;
				FlowEdge fe1 = new FlowEdge(game_index, v1, Double.POSITIVE_INFINITY);
				FlowEdge fe2 = new FlowEdge(game_index, v2, Double.POSITIVE_INFINITY);
				fn.addEdge(fe1);
				fn.addEdge(fe2);
			}
		}
		
		// add link between team and sink
		int maxWins = win[index] + remain[index];
		int t = V - 1;
		for (int i = 0; i < N; i++) {
			if (i == index) {
				continue;
			}
			
			int v = teamIndex + i;
			int capacity = maxWins - win[i];
			if (capacity < 0) {
				capacity = 0;
			}
			FlowEdge fe = new FlowEdge(v, t, capacity);
			fn.addEdge(fe);
		}
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		//System.out.println("isEliminated");
		// TODO: construct a FlowNetwork
		int index = findIndex(team);
		
		// check if Trivial elimination
		if (isTrivialEliminated(index)) {
			return true;
		}
		

		FlowNetwork fn = new FlowNetwork(V);
		constructFlowNetwork(fn, index);
		
		FordFulkerson ff = new FordFulkerson(fn, 0, V - 1);
		for (FlowEdge fe : fn.adj(0)) {
			//System.out.println( fe.flow() + "/" + fe.capacity());
			if (fe.flow() != fe.capacity()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isTrivialEliminated(int index) {
		int max = Integer.MIN_VALUE;
		
		for (int i = 0; i < N; i++) {
			if (max < win[i]) {
				max = win[i];
			}
		}
		
		if (win[index] + remain[index] < max) {
			return true;
		}
		return false;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		int index = findIndex(team);
		Queue<String> q = new Queue<String>();
		// check if Trivial elimination
		if (isTrivialEliminated(index)) {

			int max = win[index] + remain[index];
			for (int i = 0; i < N; i++) {
				if (max < win[i]) {
					q.enqueue(teams[i]);
				}
			}
			
			if (q.isEmpty()) {
				return null;
			}
			return q;
		}
		
		int V = 1 + (N - 1) * (N - 2) / 2 + (N) + 1;
		FlowNetwork fn = new FlowNetwork(V);
		constructFlowNetwork(fn, index);
		
		FordFulkerson ff = new FordFulkerson(fn, 0, V - 1);
		int teamBegin = (N - 1) * (N - 2) / 2 + 1;
		for (int i = teamBegin; i < teamBegin + N; i++) {
			if (ff.inCut(i)) {
				q.enqueue(teams[i - teamBegin]);
			}
		}
		
		if (q.isEmpty()) {
			return null;
		}
		
		return q;
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
//	    boolean res = division.isEliminated("Atlanta");
//	    StdOut.println(res);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}
