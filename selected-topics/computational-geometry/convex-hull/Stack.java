import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * An implementation of (LIFO) last-in-first-out stack of generic items.
 * It supports {@code push} and {@code pop} operations in constant time.
 * Compatible with foreach by implemention of {@code Iterable} interface.
 */
public class Stack<T> implements Iterable<T> {
    protected Node<T> top = null;
    private int n = 0;

    /**
     * Adds an item to the top of the stack.
     * Running time is <em>O(1)</em>.
     * @param item the element to be added.
     */
    public void push(T item) {
        Node<T> cur = new Node<>(item);
        cur.next = top;
        this.top = cur;
        n++;
    }

    /**
     * Shows the top item without removing it.
     * Running time is <em>O(1)</em>.
     * @throws NoSuchElementException if the stack is empty.
     */
    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("empty stack");
        return top.item;
    }

    /**
     * Removes the top item from the stack.
     * Running time is <em>O(1)</em>.
     * @throws NoSuchElementException if the stack is empty.
     */
    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("empty stack");
        Node<T> tmp = top;
        this.top = top.next;
        n--;
        return tmp.item;
    }

    /**
     * Checks if the stack is empty.
     * The stack is empty if the top is null.
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Returns the number of elements in the stack.
     */
    public int size() {
        return n;
    }

    /**
     * Helper node class used in the stack.
     * Contains a pointer to the next element only.
     */
    protected class Node<T> {
        public final T item;
        public Node<T> next;

        public Node(T item) {
            this.item = item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new StackIterator<T>(this.top);
    }

    /**
     * Iterator for the Stack.
     */
    private class StackIterator<T> implements Iterator<T> {
        private Node<T> firstNode;

        public StackIterator(Node<T> firstNode) {
            this.firstNode = firstNode;
        }

        @Override
        public boolean hasNext() {
            return this.firstNode != null;
        }

        @Override
        public T next() {
            Node<T> cur = firstNode;
            this.firstNode = cur.next;
            return cur.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Unit test.
     */
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 1; i <= 5; i++) stack.push(i);
        for (int x : stack) System.out.println(x);
        System.out.println("top item is: " + stack.peek());
        while (!stack.isEmpty()) {
            int x = stack.pop();
            System.out.println(x);
        }
    }
}
