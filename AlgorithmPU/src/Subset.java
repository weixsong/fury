public class Subset {
	public static void main(String[] args){
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		int k = Integer.parseInt(args[0]);
		while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            rq.enqueue(str);
        }
		while(k > 0) {
			String str = rq.dequeue();
			StdOut.println(str);
			k--;
		}
	}
}
