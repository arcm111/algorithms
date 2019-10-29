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

    public Node<T> head() {
        return head;
    }

    public Node<T> tail() {
        return tail;
    }

    /**
     * Adds a new element to the tail of the linked-list.
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
     * Adds a new weighted element to the tail of the linked-list.
     * @param w the new element to be added.
     */
    public void add(T w, int weight) {
        Node<T> cur = new Node<>(w, weight);
        if (head == null) {
            this.head = cur;
        } else {
            tail.next = cur;
        }
        this.tail = cur;
        this.n++;
    }

    public void addLast(T w) {
        add(w);
    }

    /**
     * Adds a new element to the head of the linked-list.
     * @param w the new element to be added.
     */
    public void addFirst(T w) {
        Node<T> cur = new Node<>(w);
        if (head == null) {
            this.tail = cur;
        }
        cur.next = head;
        this.head = cur;
        this.n++;
    }

    public LinkedList<T> union(LinkedList<T> b) {
        if (this.head == null) {
            this.head = b.head();
            this.tail = b.tail();
        } else {
            this.tail.next = b.head();
            this.tail = b.tail();
        }
        return this;
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
        public final int weight;
        public Node<T> next = null;

        public Node(T value) {
            this.value = value;
            this.weight = -1;
        }

        public Node(T value, int weight) {
            this.value = value;
            this.weight = weight;
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

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public T next() {
            T v = cur.value;
            this.cur = cur.next;
            return v;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("operation not supported");
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (iter.hasNext()) builder.append("->");
        }
        return builder.toString();
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        LinkedList<Integer> a = new LinkedList<>();
        a.add(1);
        a.add(4);
        a.add(5);
        System.out.println("a: " + a);
        LinkedList<Integer> b = new LinkedList<>();
        b.add(8);
        b.add(9);
        b.add(14);
        System.out.println("b: " + b);
        System.out.println("a + b: " + a.union(b));
    }
}
