import java.util.Iterator;

/**
 * Singly-linked list implementation of the linked-list data structrue.
 * Iterable interface is used to provide an Iterator for foreach which
 * iterate over all elements in the linked list.
 */
public class LinkedList<T> implements Iterable<T> {
    private Node<T> head = null; // the head of the linked-list
    private Node<T> tail = null; // the tail of the linked-list
    private int n = 0; // the number of element in the linked-list

    /**
     * Gets the size of the linked-list.
     * Returns the number of elements in the linked-list.
     */
    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Adds a new element to the linked-list.
     * @param w the new element to be added.
     */
    public void add(T w) {
        Node<T> cur = new Node<>(w);
        if (head == null) {
            this.head = cur;
        } else {
            tail.next = cur;
        }
        this.tail = cur;
        this.n++;
    }

    /**
     * Provide an iterator which iterates over the elements in the list.
     */
    public Iterator<T> iterator() {
        return new LinkedListIterator<T>(head);
    }

    /**
     * A helper static inner-class node for the linked-list.
     * Contains only one pointer that points to the next element in the list.
     * It also contains the value of the stored element in the node.
     */
    private static class Node<T> {
        public final T value;
        public Node<T> next = null;

        public Node(T value) {
            this.value = value;
        }
    }

    /**
     * An Iterator class for the linked-list.
     * Remove operation is not supported.
     */
    private class LinkedListIterator<T> implements Iterator<T> {
        private Node<T> cur = null;

        public LinkedListIterator(Node<T> firstNode) {
            this.cur = firstNode;
        }

        public boolean hasNext() {
            return cur != null;
        }

        public T next() {
            T v = cur.value;
            this.cur = cur.next;
            return v;
        }

        public void remove(T x) {
            throw new UnsupportedOperationException("operation not supported");
        }
    }
}
