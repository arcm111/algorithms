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
        if (left.key.compareTo(right.key) <= 0) {
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

    public int iterativeInsertionSortRuntime = 0;
    /**
     * Sorts nodes using insertion-sort iteratively.
     * Runing time complexity <em>O(n^2)</em>.
     * Best case running time is <em>O(n)</em> when the list is reverse sorted.
     * Space complexity is <em>O(1)</em>.
     */
    public void insertionSort() {
        Node<E> sorted = null;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.next = null;
            sorted = insertSorted(sorted, cur);
            cur = next;
        }
        this.head = sorted;
        this.tail = getTail(sorted);
    }

    /**
     * Inserts a node in its place in a sorted linkedlist.
     * Insert <b>after</b> nodes with equal keys to maintain stability.
     * @param head the head of a sorted linkedlist
     * @param node the node to be inserted
     * @return the head of the new sorted linkedlist
     */
    private Node<E> insertSorted(Node<E> head, Node<E> node) {
        if (head == null || head.key.compareTo(node.key) > 0) {
            iterativeInsertionSortRuntime++;
            node.next = head;
            return node;
        }
        Node<E> parent = null;
        Node<E> cur = head;
        while (cur != null && cur.key.compareTo(node.key) <= 0) {
            iterativeInsertionSortRuntime++;
            parent = cur;
            cur = cur.next;
        }
        node.next = parent.next;
        parent.next = node;
        return head;
    }

    public int recursiveInsertionSortRuntime = 0;
    /**
     * Sorts nodes using insertion-sort retursively.
     * Runing time complexity <em>O(n^2)</em>.
     * Best case running time is <em>O(n)</em> when the list is already sorted.
     * Space complexity is <em>O(n)</em> allocated for the recursive call stack.
     */
    public void insertionSortRecursive() {
        if (isEmpty()) {
            throw new IllegalStateException("Linkedlist is empty!");
        }
        Node<E> sorted = insertionSortRecursive(head);
        this.head = sorted;
        this.tail = getTail(sorted);
    }

    private Node<E> insertionSortRecursive(Node<E> head) {
        if (head.next == null) return head;
        Node<E> next = head.next;
        head.next = null;
        Node<E> sorted = insertionSortRecursive(next);
        return insertSortedRecursive(sorted, head);
    }

    /**
     * Inserts a node in its place in a sorted linkedlist.
     * Insert <b>before</b> nodes with equal keys to maintain stability.
     * @param head the head of a sorted linkedlist
     * @param node the node to be inserted
     * @return the head of the new sorted linkedlist
     */
    private Node<E> insertSortedRecursive(Node<E> head, Node<E> node) {
        if (head.key.compareTo(node.key) >= 0) {
            recursiveInsertionSortRuntime++;
            node.next = head;
            return node;
        }
        Node<E> parent = null;
        Node<E> cur = head;
        while (cur != null && cur.key.compareTo(node.key) < 0) {
            recursiveInsertionSortRuntime++;
            parent = cur;
            cur = cur.next;
        }
        node.next = parent.next;
        parent.next = node;
        return head;
    }

    /**
     * Finds the last element (tail) of a node.
     * @param node the node to find its tail
     * @return the tail
     */
    private Node<E> getTail(Node<E> node) {
        Node<E> last = null;
        for (Node<E> cur = node; cur != null; cur = cur.next) {
            last = cur;
        }
        return last;
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
            E v = cur.key;
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
     * It also contains the key of the stored element in the node.
     */
    public static class Node<E> {
        public final E key;
        public final int weight;
        public Node<E> next = null;

        public Node(E key) {
            this.key = key;
            this.weight = -1;
        }

        public Node(E key, int weight) {
            this.key = key;
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
        System.out.println("c tail: " + c.tail().key);

        LinkedList<Integer> d = new LinkedList<>();
        for (int i = 0; i < 16; i++) d.add(rand.nextInt(32));
        System.out.println("       d: " + d);
        d.insertionSort();
        System.out.println("d sorted: " + d);
        System.out.println("d tail: " + d.tail().key);

        LinkedList<Integer> e = new LinkedList<>();
        for (int i = 0; i < 16; i++) e.add(rand.nextInt(32));
        System.out.println("       e: " + e);
        e.insertionSortRecursive();
        System.out.println("e sorted: " + e);
        System.out.println("e tail: " + e.tail().key);

        System.out.println("Insertion-sort runtime on a sorted list:");
        LinkedList<Integer> f = new LinkedList<>();
        LinkedList<Integer> g = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            f.add(i);
            g.add(i);
        }
        System.out.println("f: " + f);
        System.out.println("g: " + g);
        f.insertionSort();
        g.insertionSortRecursive();
        System.out.println("f sorted (iteratively): " + f);
        System.out.println("g sorted (recursively): " + g);
        System.out.println("f runtime: " + f.iterativeInsertionSortRuntime);
        System.out.println("g runtime: " + g.recursiveInsertionSortRuntime);

        System.out.println("Insertion-sort runtime on a reverse sorted list:");
        f = new LinkedList<>();
        g = new LinkedList<>();
        for (int i = 15; i >= 0; i--) {
            f.add(i);
            g.add(i);
        }
        System.out.println("f: " + f);
        System.out.println("g: " + g);
        f.insertionSort();
        g.insertionSortRecursive();
        System.out.println("f sorted (iteratively): " + f);
        System.out.println("g sorted (recursively): " + g);
        System.out.println("f runtime: " + f.iterativeInsertionSortRuntime);
        System.out.println("g runtime: " + g.recursiveInsertionSortRuntime);
    }
}
