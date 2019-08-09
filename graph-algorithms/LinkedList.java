import java.util.Iterator;

/**
 * Singly-linked list implementation of the linked-list data structrue.
 * This implementation only support {@code Integer} type of elements.
 * Iterable interface is used to provide an Iterator for foreach which
 * iterate over all elements in the linked list.
 */
public class LinkedList implements Iterable<Integer> {
    private Node first; // the head of the linked-list
    private int n; // the number of element in the linked-list

    /**
     * Constructor.
     * Initialize an empty linked-list with 0 elements.
     */
    public LinkedList() {
        this.first = null;
        this.n = 0;
    }

    /**
     * Gets the size of the linked-list.
     * Returns the number of elements in the linked-list.
     */
    public int size() {
        return n;
    }

    /**
     * Adds a new element to the linked-list.
     * @param w the new element to be added.
     */
    public void add(int w) {
        Node cur = new Node(w);
        cur.next = first;
        this.first = cur;
        this.n++;
    }

    /**
     * Provide an iterator which iterates over the elements in the list.
     */
    public Iterator<Integer> iterator() {
        return new LinkedListIterator(first);
    }

    /**
     * A helper static inner-class node for the linked-list.
     * Contains only one pointer that points to the next element in the list.
     * It also contains the value of the stored element in the node.
     */
    private static class Node {
        public final int value;
        public Node next = null;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * An Iterator class for the linked-list.
     * Remove operation is not supported.
     */
    private class LinkedListIterator implements Iterator<Integer> {
        private Node cur = null;

        public LinkedListIterator(Node firstNode) {
            this.cur = firstNode;
        }

        public boolean hasNext() {
            return cur != null;
        }

        public Integer next() {
            int v = cur.value;
            this.cur = cur.next;
            return v;
        }

        public void remove(int x) {
            throw new UnsupportedOperationException("operation not supported");
        }
    }
}
