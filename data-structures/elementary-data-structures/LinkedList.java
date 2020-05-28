import java.util.Iterator;
import java.util.Random;

/**
 * Singly-linked list implementation of the linked-list data structrue.
 * Iterable interface is used to provide an Iterator for foreach which
 * iterate over all elements in the linked list.
 */
public class LinkedList<E extends Comparable<E>> implements Iterable<E> {
    private Node<E> head = null; // the head of the linked-list
    private Node<E> tail = null; // the tail of the linked-list
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

    public Node<E> head() {
        return head;
    }

    public Node<E> tail() {
        return tail;
    }

    /**
     * Adds a new element to the tail of the linked-list.
     * @param w the new element to be added.
     */
    public void add(E w) {
        Node<E> cur = new Node<>(w);
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
    public void add(E w, int weight) {
        Node<E> cur = new Node<>(w, weight);
        if (head == null) {
            this.head = cur;
        } else {
            tail.next = cur;
        }
        this.tail = cur;
        this.n++;
    }

    public void addLast(E w) {
        add(w);
    }

    /**
     * Adds a new element to the head of the linked-list.
     * @param w the new element to be added.
     */
    public void addFirst(E w) {
        Node<E> cur = new Node<>(w);
        if (head == null) {
            this.tail = cur;
        }
        cur.next = head;
        this.head = cur;
        this.n++;
    }

    public LinkedList<E> union(LinkedList<E> b) {
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
     * Sorts the elements in the linked list using merge-sort.
     * Running time is <em>O(nlgn)</em>.
     * Space complexity is <em>O(1)</em>.
     */
    public void mergesort() {
        if (size() > 1) {
            this.head = sort(head);
            Node<E> cur = head;
            while (cur.next != null) cur = cur.next;
            this.tail = cur;
        }
    }

    private Node<E> sort(Node<E> node) {
        if (node == null || node.next == null) return node;
        Node<E> mid = middle(node);
        Node<E> right = sort(mid.next);
        mid.next = null;
        Node<E> left = sort(node);
        return merge(left, right);
    }

    private Node<E> merge(Node<E> left, Node<E> right) {
        if (left == null) return right;
        if (right == null) return left;
        Node<E> merged = null;
        if (left.value.compareTo(right.value) <= 0) {
            merged = left;
            merged.next = merge(left.next, right);
        } else {
            merged = right;
            merged.next = merge(left, right.next);
        }
        return merged;
    }

    private Node<E> middle(Node<E> node) {
        Node<E> middle = node;
        while (node.next != null) {
            node = node.next;
            if (node.next != null) {
                node = node.next;
                middle = middle.next;
            }
        }
        return middle;
    }

    /**
     * Provide an iterator which iterates over the elements in the list.
     */
    public Iterator<E> iterator() {
        return new LinkedListIterator<E>(head);
    }

    /**
     * An Iterator class for the linked-list.
     * Remove operation is not supported.
     */
    private class LinkedListIterator<E> implements Iterator<E> {
        private Node<E> cur = null;

        public LinkedListIterator(Node<E> firstNode) {
            this.cur = firstNode;
        }

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public E next() {
            E v = cur.value;
            this.cur = cur.next;
            return v;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("operation not supported");
        }
    }

    /**
     * A helper static inner-class node for the linked-list.
     * Contains only one pointer that points to the next element in the list.
     * It also contains the value of the stored element in the node.
     */
    private static class Node<E> {
        public final E value;
        public final int weight;
        public Node<E> next = null;

        public Node(E value) {
            this.value = value;
            this.weight = -1;
        }

        public Node(E value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<E> iter = iterator();
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

        LinkedList<Integer> c = new LinkedList<>();
        Random rand = new Random();
        for (int i = 0; i < 16; i++) c.add(rand.nextInt(32));
        System.out.println("       c: " + c);
        c.mergesort();
        System.out.println("c sorted: " + c);
        System.out.println("c tail: " + c.tail().value);
    }
}
