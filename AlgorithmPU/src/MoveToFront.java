import java.util.HashMap;


public class MoveToFront {
	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	private static class Node {
		public char key;
		public Node next;
		
		public Node(char key, Node next) {
			this.key = key;
			this.next = next;
		}
	}
	
	public static void encode() {
		Node head = new Node((char)0, null);
		for (int i = 255; i >= 0; i--) {
			Node node = new Node((char)i, head.next);
			head.next = node;
		}
		
		char[] chs = BinaryStdIn.readString().toCharArray();
		int index = 0;
		while (index < chs.length) {
			char c = chs[index++];
			Node node = head.next;
			Node pre = head;
			int count = 0;
			while (node != null && node.key != c) {
				count++;
				pre = node;
				node = node.next;
			}
			
			BinaryStdOut.write(count, 8);
			if (pre != head) {
				pre.next = node.next;
				node.next = head.next;
				head.next = node;
			}
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() {
		Node head = new Node((char) 0, null);
		for (int i = 255; i >= 0; i--) {
			Node node = new Node((char) i, head.next);
			head.next = node;
		}
		
		char[] in = BinaryStdIn.readString().toCharArray();
		int index = 0;
		while (index < in.length) {
			int c = (int)in[index++];
			Node node = head.next;
			Node pre = head;
			int idx = 0;
			while (idx < c) {
				idx++;
				pre = node;
				node = node.next;
			}
			BinaryStdOut.write(node.key);
			
			if (pre != head) {
				pre.next = node.next;
				node.next = head.next;
				head.next = node;
			}
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		if (args[0].equals("-")) {
			MoveToFront.encode();
		} else if (args[0].equals("+")) {
			MoveToFront.decode();
		}
	}
}