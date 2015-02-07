import java.util.HashMap;
import java.util.TreeSet;


public class WordNet {
	
	private Digraph graph;
	private HashMap<String, TreeSet<Integer>> hash;
	private HashMap<Integer, String> hash_map;
	private int count;
	private DirectedCycle dc;
	private SAP sap;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new java.lang.NullPointerException();
		}
		
		hash = new HashMap<String, TreeSet<Integer>>();
		hash_map = new HashMap<Integer, String>();
		count = 0;
		
		readInSynsets(synsets);
		graph = new Digraph(count);
		readInHypernyms(hypernyms);
		
		dc = new DirectedCycle(graph);
		if (dc.hasCycle()) {
			throw new java.lang.IllegalArgumentException();
		}
		
		sap = new SAP(graph);
	}
	
	private void readInSynsets(String synsets) {
		In in = new In(synsets);
		while(in.hasNextLine()) {
			String line = in.readLine();
			String info[] = line.split(",");
			int id = Integer.parseInt(info[0]);
			String synset = info[1];
			String synset_array[] = synset.split(" ");
			hash_map.put(id, synset);
			for (int i = 0; i < synset_array.length; i++) {
				String value = synset_array[i];
				if (hash.containsKey(value)) {
					hash.get(value).add(id);
				} else {
					TreeSet<Integer> set = new TreeSet<Integer>();
					set.add(id);
					hash.put(value, set);
				}
			}

			count++;
			//System.out.println(id + ":" + synset);
		}
	}
	
	private void readInHypernyms(String hypernyms) {
		int[] outDegree = new int[count];
		for (int i = 0; i < count; i++) {
			outDegree[i] = 0;
		}
		
		In in = new In(hypernyms);
		while(in.hasNextLine()) {
			String line = in.readLine();
			String info[] = line.split(",");
			int start = Integer.parseInt(info[0]);
			for (int i = 1; i < info.length; i++) {
				int to = Integer.parseInt(info[i]);
				graph.addEdge(start, to);
				outDegree[start] = 1;
			}
		}
		
		int rootCount = 0;
		for (int i = 0; i < count; i++) {
			if (outDegree[i] == 0) {
				rootCount++;
			}
		}
		
		if (rootCount > 1) {
			throw new java.lang.IllegalArgumentException();
		}
	}	

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		Stack<String> stack = new Stack<String>();
		for (String key : hash.keySet()) {
			stack.push(key);
		}
		
		return stack;
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) {
			throw new java.lang.NullPointerException();
		}
		
		return hash.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new java.lang.NullPointerException();
		}
		
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new java.lang.IllegalArgumentException();
		}
		
		TreeSet<Integer> source1 = hash.get(nounA);
		TreeSet<Integer> source2 = hash.get(nounB);
		
		//sap = new SAP(graph);
		return sap.length(source1, source2);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new java.lang.NullPointerException();
		}
		
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new java.lang.IllegalArgumentException();
		}
		
		TreeSet<Integer> source1 = hash.get(nounA);
		TreeSet<Integer> source2 = hash.get(nounB);
		
		//sap = new SAP(graph);
		int root = sap.ancestor(source1, source2);
		String res = hash_map.get(root);
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
		TreeSet<Integer> set = new TreeSet<Integer>();
		set.add(5);
		set.add(6);
		System.out.println(set);
	}

}
