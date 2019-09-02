import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Min-heap priority queue implementation using dynamic arrays.
 * <p>Uses dynamic array to store the elements that expands in size when the
 * heap is full and contract when the number of the elements in the heap
 * falls to less than quarter of its capacity. The insert and delete 
 * running time is <em>O(1)</em> amortized and <em>O(n)</em> worst-case. 
 * <p>min-heap-property: for each element i in the heap, parent(i) is smaller.
 * <p>Running time of {@code minHeapify}, {@code insert}, {@code extractMin},
 * {@code decreaseKey} is <em>O(lgn)</em>.
 * Running time of {@code minimum} is <em>O(1)</em>.
 * Running time of {@code buildHeap} is <em>O(n)</em>.
 * Running time of {@code heapSort} is <em>O(nlgn)</em>.
 * <p>{@code minHeapify} moves an element down the heap "sinks" an element to
 * maintain min-heap-property, while {@code decreaseKey} moves an element up
 * the heap "swims".
 */
public class MinPriorityQueue<T extends Comparable<T>> {
    private final static int DEFAULT_CAPACITY = 16; // default initial capacity
    public int initialCapacity; // current initial capacity
    public int capacity; // current capacity of the heap
    public int n; // the number of elements in the heap
    public T[] heap;

    /**
     * Constructor with a specified initial capacity.
     * @param initialCapacity the capacity to initialize the heap size to.
     */
    public MinPriorityQueue(int initialCapacity) {
        init(initialCapacity);
    }

    /**
     * Constructor with no arguments.
     * Initializes the heap size with default initial capacity.
     */
    public MinPriorityQueue() {
        init(DEFAULT_CAPACITY);
    }

    /**
     * Constructor with a list of elements.
     * Initializes the heap size to the number of elements and inserts the
     * elements to the heap while maintaining min-heap property.
     * @param arr an iterable that provides the list of elements.
     * @param size the number of elements in the iterable.
     */
    public MinPriorityQueue(Iterable<T> arr, int size) {
        init(size);
        buildHeap(arr, size);
    }

    /**
     * Helper method used in all constructors.
     * Creates the heap array and sets the value of the capacity.
     * The first element in the heap is left blank, therefore, the capacity
     * is set to the size of the heap plus 1.
     * @param size the size of the heap.
     */
    @SuppressWarnings("unchecked")
    private void init(int size) {
        // The first element in the heap is always left blank, therefore,
        // we allocate n + 1 heap space for the elements.
        this.initialCapacity = size;
        this.capacity = initialCapacity;
        this.heap = (T[]) new Comparable[initialCapacity + 1];
    }

    /**
     * Expands the heap's array to double its current capacity.
     * Allows additional space for the first blank index.
     * Triggered when the the heap is full (n == capacity).
     */
    @SuppressWarnings("unchecked")
    private void expandHeap(){
        int newCapacity = 2 * capacity;
        T[] newHeap = (T[]) new Comparable[newCapacity + 1];
        for (int i = 1; i <= n; i++) newHeap[i] = heap[i];
        this.heap = newHeap;
        this.capacity = newCapacity;
    }

    /**
     * Contracts the heap's array to half its current capacity.
     * Allows additional space for the first blank index.
     * Triggered when number of elements in the heap are less than quarter 
     * of the capacity.
     */
    @SuppressWarnings("unchecked")
    private void contractHeap() {
        int newCapacity = capacity / 2;
        T[] newHeap = (T[]) new Comparable[newCapacity + 1];
        for (int i = 1; i <= n; i++) newHeap[i] = heap[i];
        this.heap = newHeap;
        this.capacity = newCapacity;
    }

    /**
     * Returns the parent's index of another index.
     * The parent index is the floored half of the given index.
     * <em>Floor(i/2)</em>.
     * @return the index of the parent as integer.
     */
    private int parent(int i) {
        validateNode(i);
        // or i/2
        return i >> 1;
    }

    /**
     * Returns the left child's index of another index.
     * @return the index of the left child.
     */
    private int left(int i) {
        validateNode(i);
        // or i*2
        return i << 1;
    }

    /**
     * Returns the right child's index of another index.
     * @return the index of the right child.
     */
    private int right(int i) {
        validateNode(i);
        // or 2*i + 1
        return (i << 1) | 1;
    }

    /**
     * Checks if the node index is withing acceptable range.
     * The index should be between 1 and n inclusive.
     * @param i the index to be validated.
     * @throws IllegalArgumentException if the index is invalid.
     */
    private void validateNode(int i) {
        if (i < 1 || i > n) {
            throw new IllegalArgumentException("invalid node: ["+n+"]:" + i);
        }
    }

    /**
     * Swaps two nodes in the heap.
     * @param i the index of the first node.
     * @param j the index of the second node.
     */
    private void switchNodes(int i, int j) {
        validateNode(i);
        validateNode(j);
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Maintains the min-heap-property by letting the value at i float
     * down so that the sub-tree rooted at i obyes the min-heap-property.
     * @param i the root of the sub-tree to fix.
     */
    private void minHeapify(int i) {
        validateNode(i);
        int l = left(i);
        int r = right(i);
        int smallest = i;
        if (l <= n && heap[l].compareTo(heap[i]) == -1) smallest = l;
        if (r <= n && heap[r].compareTo(heap[smallest]) == -1) smallest = r;
        if (smallest != i) {
            switchNodes(i, smallest);
            minHeapify(smallest);
        }
    }

    /**
     * Converts an array to a min-heap.
     * @param arr an iterable of the elements to add to the heap.
     * @param n the number of the elements in the iterable.
     */
    private void buildHeap(Iterable<T> arr, int n) {
        this.n = n;
        Iterator<T> iter = arr.iterator();
        for (int i = 1; i <= n; i++) heap[i] = iter.next();
        for (int i = n / 2; i > 0; i--) {
            minHeapify(i);
        }
    }

    /**
     * Reverse sorts the elements in the heap.
     * To sort the elements in ascending order, max-heap need to be
     * used instead.
     */
    public Iterable<T> heapSort() {
        int num = n;
        for (int i = n; i > 1; i--) {
            switchNodes(i, 1);
            n--;
            minHeapify(1);
        }
        this.n = num;
        // Arrays.copyOfRange: endIndex is exclusive
        return Arrays.asList(Arrays.copyOfRange(heap, 1, n + 1));
    }

    /**
     * Gets the minimum element in the heap.
     * @return the minimum element.
     */
    public T minimum() {
        return heap[1];
    }
    
    /**
     * Extracts the minimum elment of the heap without violation the
     * min-heap-property.
     * Contracts the heap array if elements falls below 1/4 of the capacity.
     * @return the minimum element in the heap.
     * @throws NoSuchElementException if the heap is emepty.
     */
    public T extractMin() {
        if (n < 1) {
            throw new NoSuchElementException("heap is empty");
        }
        T min = heap[1];
        heap[1] = heap[n];
        minHeapify(1);
        n--;
        if (capacity > initialCapacity && n < capacity / 4) {
            contractHeap();
        }
        return min;
    }

    /**
     * Decreases the priority of an element in the queue.
     * @param i the index of the element.
     * @param key the new priority of the element.
     * @throws IllegalArgumentException if the new key is smaller than old key.
     */
    public void decreaseKey(int i, T key) {
        validateNode(i);
        if (key.compareTo(heap[i]) == -1) {
            throw new IllegalArgumentException("new key should be smaller");
        }
        // Can be done this way:
        // heap[i] = key;
        // while (i >= 0 && heap[parent(i)].compareTo(heap[i]) == 1) {
        // 	switchNodes(i, parent(i));
        // 	i = parent(i);
        // }
        //
        // Or this way to avoid the three-steps {@code switchNodes} operation:
        while (i > 0 && heap[parent(i)].compareTo(key) == 1) {
            heap[i] = heap[parent(i)];
            i = parent(i);
        }
        heap[i] = key;
    }

    /**
     * Inserts new element into the heap without violating min-heap-property.
     * @param key the priority of the new element.
     */
    public void insertKey(T key) {
        if (n >= capacity) {
            expandHeap();
        }
        n++;
        int i = n;
        while (i > 1 && heap[parent(i)].compareTo(key) == 1) {
            heap[i] = heap[parent(i)];
            i = parent(i);
        }
        heap[i] = key;
    }

    /**
     * Returns the number of the elements in the heap.
     * @return n the size of the heap.
     */
    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        System.out.println("Min-Heap Priority Queue: ");
        Integer[] elements = {7, 2, 8, 9, 10, 1, 3, 6, 4, 5};
        // testing buildHeap, minHeapify, minimum, extractMin:
        MinPriorityQueue<Integer> h = 
                new MinPriorityQueue<>(Arrays.asList(elements), 10);
        System.out.println("Minimum is: " + h.minimum());
        for (int i = 0; i < 10; i++) {
            System.out.println("Extracint min: " + h.extractMin());
        }

        // testing heapSort:
        System.out.println("Heap Sort: ");
        h = new MinPriorityQueue<>(Arrays.asList(elements), 10);
        for (Integer x : h.heapSort()) {
            System.out.print(x + " ");
        }
        System.out.println();

        // testing insertKey, expandHeap, contractHeap:
        System.out.println("Inserting keys: 7 2 8 9 10 1 3 6 4 5");
        h = new MinPriorityQueue<>(5);
        for (int i = 0; i < 10; i++) {
            h.insertKey(elements[i]);
            System.out.print("Insert element [" + elements[i] + "]: ");
            System.out.print("size: " + h.size());
            System.out.println(", capacity [" + h.getCapacity() + "]");
        }
        for (int i = 0; i < 10; i++) {
            System.out.print("Extracint min: " + h.extractMin());
            System.out.print(", size: " + h.size());
            System.out.println(", capacity [" + h.getCapacity() + "]");
        }
    }
}
