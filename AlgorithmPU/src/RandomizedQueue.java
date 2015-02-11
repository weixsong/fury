import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;            // queue elements
    private int N = 0;           // number of elements on queue
    private int first = 0;       // index of first element of queue
    private int last  = 0;       // index of next available slot

	public RandomizedQueue() {
		q = (Item[]) new Object[2];
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}

	public void enqueue(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		if (N == q.length) {
			resize(2 * q.length);
		}
		q[last++] = item;
		// warp-around
		if (last == q.length) {
			last = 0;
		}
		N++;
	}

    private void resize(int size) {
        Item[] temp = (Item[]) new Object[size];
        for (int i = 0; i < N; i++) {
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last  = N;
    }

	public Item dequeue() {
		if (N == 0) {
			throw new NoSuchElementException("Queue underflow");
		}
		int idx = StdRandom.uniform(N);
		idx = (first + idx) % q.length;
		Item ret = q[idx];
		Item temp = q[idx];
		q[idx] = q[first];
		q[first] = temp;
		q[first] = null;
		first++;
		if (first == q.length) {
			first = 0;
		}
		N--;
		if (N == 0) {
			first = last = 0;
		} else if (N > 0 && N == q.length / 4) {
			resize(q.length / 2);
		}
		return ret;
	}

	public Item sample() {
		if (N == 0) {
			throw new NoSuchElementException("Queue underflow");
		}
		int idx = StdRandom.uniform(N);
		idx = (first + idx) % q.length;
		return q[idx];
	}

	@Override
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	private class RandomizedQueueIterator implements Iterator<Item> {
		private int currentSize;
		private int i = 0;
		Item[] r;
		public RandomizedQueueIterator() {
			r = (Item[]) new Object[N];
			
			for (int k = 0; k < N; k++) {
				int id = (first + k) % q.length;
				r[k] = q[id];
			}
			StdRandom.shuffle(r);
			currentSize = N;
		}

        public boolean hasNext() {
        	if (currentSize != N) {
        		throw new ConcurrentModificationException();
        	}
        	return i < N;
        }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) {
            	throw new NoSuchElementException();
            }
            Item item = r[i];
            i++;
            return item;
        }
    }

	public static void main(String[] args) {
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		rq.enqueue("A");
		rq.enqueue("B");
		rq.enqueue("C");
		rq.enqueue("D");
		rq.enqueue("E");
		rq.enqueue("F");
		rq.enqueue("H");
		
		for(String s:rq) {
			System.out.println(s);
		}
		
		/*
		for (int i = 0; i < 4000; i++) {

			Iterator<String> iterator = rq.iterator();
			String s;
			while(true) {
				 s = iterator.next();
				 System.out.print(s);
				 if (s == "H") break;
			}
			System.out.println();
		}
		*/
	}
}
