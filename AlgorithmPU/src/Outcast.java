
public class Outcast {
	
	private WordNet wn;
	
	public Outcast(WordNet wordnet) {
		wn = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		if (nouns == null) {
			throw new java.lang.NullPointerException();
		}
		
		for (int i = 0; i < nouns.length; i++) {
			if (!wn.isNoun(nouns[i])) {
				throw new java.lang.IllegalArgumentException();
			}
		}
		
		int sumDist = Integer.MIN_VALUE;
		String res = "";
		for (int i = 0; i < nouns.length; i++) {
			int dist = 0;
			for (int j = 0; j < nouns.length; j++) {
				dist += wn.distance(nouns[i], nouns[j]);
			}
			
			if (sumDist < dist) {
				sumDist = dist;
				res = nouns[i];
			}
		}
		
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}

}
