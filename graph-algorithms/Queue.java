import java.util.NoSuchElementException;

/**
 * A Queue data structure implementation using doubly-linked-lists.
 * Items enqueued first will be dequeued first (FIFO).
 * {@code enqueue} and {@code dequeue} operations take <em>O(1)</em> constant
 * running time and <em>O(n)</em> space.
 */
public class Queue<T> {
    private Node<T> head = null;
    private Node<T> tail = null;
    private int n = 0;

    /**
     * Helper node class for the queue.
     * It store the elements in doubly-linked nodes.
     */
    private static class Node<T> {
        public T item;
        public Node<T> next = null;

        public Node(T item) {
            this.item = item;
        }
    }

    /**
     * Checks if the queue is empty.
     * The queue is empty if the head of the queue is {@code null}.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Return the number of elements in the queue.
     */
    public int size() {
        return n;
    }

    /**
     * Adds an element to the queue.
     * If the queue is empty, make head and tail point to the new element.
     * Otherwise, add the new element to the tail of the queue.
     * @param item the item to enqueue.
     */
    public void enqueue(T item) {
        Node<T> oldTail = tail; // queue's old tail.
        Node<T> tmp = new Node(item);
        this.tail = tmp;
        if (isEmpty()) { // queue is empty.
            this.head = tail;
        } else { // queue has at least one element.
            oldTail.next = tail;
        }
        n++;
    }

    /**
     * Removes the head from the queue and return its item.
     * If there is only one element in the queue, then make head and tail
     * {@code null}.
     * Otherwise, remove the head and update pointers.
     * @throws IllegalArgumentException if the queue is empty.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        T item = head.item;
        this.head = head.next;
        if (isEmpty()) this.tail = null;
        n--;
        return item;
    }
}
