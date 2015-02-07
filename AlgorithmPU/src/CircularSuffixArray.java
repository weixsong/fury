public class CircularSuffixArray {
	// circular suffix array of s
	private int N;
	private SuffixArrayX x;

	public CircularSuffixArray(String s) {
		if (s == null) {
			throw new java.lang.NullPointerException();
		}
		
		N = s.length();
		x = new SuffixArrayX(s);
	}
	
	// length of s
	public int length() {
		return N;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i > N - 1) {
			throw new java.lang.IndexOutOfBoundsException();
		}

		return x.index(i);
	}

	private class SuffixArrayX {
		private final char[] text;
		private final int[] index; 
		private final int N; // number of characters in text

		public SuffixArrayX(String text) {
			N = text.length();
			this.text = text.toCharArray();
			this.index = new int[N];
			for (int i = 0; i < N; i++)
				index[i] = i;

			sort(0, N - 1, 0);
		}

		// 3-way string quicksort lo..hi starting at dth character
		private void sort(int lo, int hi, int d) {
			if (hi <= lo) {
				return;
			}
			
			if (d >= N) {
				return;
			}

			int lt = lo, gt = hi;
			char v = text[(index[lo] + d) % N];
			int i = lo + 1;
			while (i <= gt) {
				char t = text[(index[i] + d) % N];
				if (t < v)
					exch(lt++, i++);
				else if (t > v)
					exch(i, gt--);
				else
					i++;
			}

			sort(lo, lt - 1, d);
			if (v >= 0)
				sort(lt, gt, d + 1);
			sort(gt + 1, hi, d);
		}

		// exchange index[i] and index[j]
		private void exch(int i, int j) {
			int swap = index[i];
			index[i] = index[j];
			index[j] = swap;
		}

		public int index(int i) {
			if (i < 0 || i >= N)
				throw new IndexOutOfBoundsException();
			return index[i];
		}
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		String str = "ABRACADABRA!";
		//String str = "CCCAAABBBETTT!";
		CircularSuffixArray suffix = new CircularSuffixArray(str);
		for (int i = 0; i < str.length(); i++) {
			System.out.print(suffix.index(i));
		}
	}
}