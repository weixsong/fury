import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private Node first;
	private Node last;
	private int N;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

	public Deque() {
		first = null;
		last = null;
		N = 0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return N;
	}

	public void addFirst(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		Node node = new Node();
		node.item = item;
		node.next = first;
		node.prev = null;
		
		if (first == null) {
			first = node;
			last = node;
		} else {
			first.prev = node;
			first = node;
		}
		N++;
	}

	public void addLast(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		Node node = new Node();
		node.item = item;
		node.next = null;
		node.prev = last;
		if (last == null) {
			last = node;
			first = node;
		} else {
			last.next = node;
			last = node;
		}
		N++;
	}

	public Item removeFirst() {
		if (first == null) {
			throw new NoSuchElementException();
		}
		Node n = first;
		Item item = n.item;
		N--;
		if (N == 0) {
			first = last = null;
		} else {
			first = first.next;
			first.prev = null;
		}
		n.prev = null; // sw
		n.next = null; // sw
		//n = null;
		return item;
	}
	public Item removeLast() {
		if (last == null) {
			throw new NoSuchElementException();
		}
		Node n = last;
		Item item = n.item;
		N--;
		if (N == 0) {
			first = last = null;
		} else {
			last = last.prev;
			last.next = null;
		}
		n.prev = null; // sw
		n.next = null; // sw
		//n = null;
		return item;
	}

	@Override
	public Iterator<Item> iterator() {
		return new DequeIterator(first);
	}

	private class DequeIterator implements Iterator<Item> {
        private Node current;

    	public DequeIterator(Node first) {
    		current = first;
    	}

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        private int num = N;
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int num2 = N;
            if (num != num2) {
            	throw new ConcurrentModificationException();
            }
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }

	public static void main(String[] args) {
		Deque<String> dq = new Deque<String>();
		dq.addFirst("sw1");
		//dq.removeLast();
		dq.addLast("sw2");
		dq.removeFirst();
		dq.addFirst("sw3");
		for (String s : dq) {
			System.out.println(s);
		}
	}
}
