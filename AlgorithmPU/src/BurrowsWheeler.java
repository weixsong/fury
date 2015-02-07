public class BurrowsWheeler {
	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {
		String str = BinaryStdIn.readString();
		char[] array = str.toCharArray();
		CircularSuffixArray suffix = new CircularSuffixArray(str);
		
		for (int i = 0; i < str.length(); i++) {
			if (suffix.index(i) == 0) {
				BinaryStdOut.write(i);
			}
		}
		
		int R = str.length();
		for (int i = 0; i < str.length(); i++) {
			int index = suffix.index(i);
			BinaryStdOut.write(array[(index - 1 + R) % R]);
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing
	// to standard output
	public static void decode() {
		int first = BinaryStdIn.readInt();
		String str = BinaryStdIn.readString();

		char[] t = str.toCharArray();
		char[] firstColumn = str.toCharArray();
		
		int N = t.length;
		int[] next = new int[N];
		sort(firstColumn, next);
		
		for (int i = 0; i < N; i++) {
			BinaryStdOut.write(firstColumn[first]);
			first = next[first];
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}
	
	private static void sort(char[] arr, int[] next) {
		int R = 256;
		int n = arr.length;
		char[] aux = new char[n];
		
		int[] count = new int[R+1];
        for (int i = 0; i < n; i++)
            count[arr[i] + 1]++;
        
        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];
        
        // move data
        for (int i = 0; i < n; i++) {
        	next[count[arr[i]]] = i;
        	aux[count[arr[i]]++] = arr[i];
        }
        
        // copy back
        for (int i = 0; i < n; i++)
            arr[i] = aux[i];
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-")) {
			BurrowsWheeler.encode();
		} else if (args[0].equals("+")) {
			BurrowsWheeler.decode();
		}
	}
}