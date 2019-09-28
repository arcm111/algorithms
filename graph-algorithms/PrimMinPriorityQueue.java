import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Modified min-heap priority queue, adjusted to work with prim's mst algorithm.
 */
public class PrimMinPriorityQueue 
        <T extends VertexInterface, E extends Number> {
    public static final int NIL = -1;
    public final E INFINITY = null; // key is +/-infinity when null
    public int n; // the number of elements in the heap
    public WeightedVertex<T, E>[] heap; // minimum heap
    public int[] heapIndex; // indexes of the original vertices in the heap

    /**
     * Constructor with a list of elements.
     * Initializes the heap size to the number of elements and inserts the
     * elements to the heap while maintaining min-heap property.
     * @param arr an iterable that provides the list of elements.
     * @param size the number of elements in the iterable.
     */
    @SuppressWarnings("unchecked")
    public PrimMinPriorityQueue(Iterable<WeightedVertex<T, E>> items, int n) {
        this.heap = (WeightedVertex<T, E>[]) new WeightedVertex[n + 1];
        this.heapIndex = new int[n];
        buildHeap(items, n);
    }
    
    public PrimMinPriorityQueue(WeightedVertex<T, E>[] items, int size) {
        this(Arrays.asList(items), size);
    }

    /**
     * Converts an array to a min-heap.
     * @param arr an iterable of the elements to add to the heap.
     * @param n the number of the elements in the iterable.
     */
    private void buildHeap(Iterable<WeightedVertex<T, E>> arr, int n) {
        this.n = n;
        Iterator<WeightedVertex<T, E>> iter = arr.iterator();
        for (int i = 1; i <= n; i++) {
            heap[i] = iter.next();
            heapIndex[i - 1] = i;
        }
        for (int i = n / 2; i > 0; i--) {
            minHeapify(i);
        }
    }

    public boolean inQueue(WeightedVertex<T, E> v) {
        return heapIndex[v.getVertex()] != NIL;
    }

    /**
     * Returns the parent's index of another index.
     * @return the index of the parent as integer.
     */
    private int parent(int i) {
        validateVertex(i);
        // or i/2
        return i >> 1;
    }

    /**
     * Returns the left child's index of another index.
     * @return the index of the left child.
     */
    private int left(int i) {
        validateVertex(i);
        // or i*2
        return i << 1;
    }

    /**
     * Returns the right child's index of another index.
     * @return the index of the right child.
     */
    private int right(int i) {
        validateVertex(i);
        // or 2*i + 1
        return (i << 1) | 1;
    }

    /**
     * Checks if the node index is withing acceptable range.
     * The index should be between 1 and n inclusive.
     * @param i the index to be validated.
     * @throws IllegalArgumentException if the index is invalid.
     */
    private void validateVertex(int i) {
        if (i < 1 || i > n) {
            System.out.println("n: " + n);
            throw new IllegalArgumentException("invalid node: " + i);
        }
    }

    /**
     * Swaps two nodes in the heap.
     * @param i the index of the first node.
     * @param j the index of the second node.
     */
    private void switchWeightedVertexs(int i, int j) {
        // switch nodes indexes
        heapIndex[heap[i].getVertex()] = j;
        heapIndex[heap[j].getVertex()] = i;
        // switch nodes
        WeightedVertex<T, E> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Maintains the min-heap-property by letting the value at i float
     * down so that the sub-tree rooted at i obyes the min-heap-property.
     * @param i the root of the sub-tree to fix.
     */
    private void minHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest = i;
        if (l <= n && heap[l].compareTo(heap[i]) == -1) smallest = l;
        if (r <= n && heap[r].compareTo(heap[smallest]) == -1) smallest = r;
        if (smallest != i) {
            switchWeightedVertexs(i, smallest);
            minHeapify(smallest);
        }
    }

    /**
     * Extracts the minimum elment of the heap without violating the
     * min-heap-property.
     * @return the minimum element in the heap.
     * @throws NoSuchElementException if the heap is emepty.
     */
    public WeightedVertex<T, E> extractMin() {
        if (n < 1) {
            throw new NoSuchElementException("heap is empty");
        }
        // update first and last nodes indexes
        heapIndex[heap[n].getVertex()] = 1;
        heapIndex[heap[1].getVertex()] = NIL;
        // replace first element in the heap with the last element,
        // restore min-heap-property, and return the first element.
        WeightedVertex<T, E> min = heap[1];
        heap[1] = heap[n];
        n--;
        if (n > 0) minHeapify(1);
        return min;
    }

    /**
     * Decreases the priority of an element in the queue.
     * @param i the index of the element.
     * @param key the new priority of the element.
     * @throws IllegalArgumentException if the new key is smaller than old key.
     */
    public void decreaseKey(WeightedVertex<T, E> v, E key) {
        // find heap's index of vertex v
        int i = heapIndex[v.getVertex()];
        if (i == NIL) {
            throw new IllegalArgumentException("element is not in the queue");
        } else if (i > n) {
            throw new IllegalArgumentException("Invalid index: " + i);
        } else if (heap[i].key != INFINITY && greater(key, heap[i].key)) {
            System.out.println("Old key: " + v.key);
            System.out.println("New key: " + key);
            throw new IllegalArgumentException("new key should be smaller");
        }
        heap[i].setKey(key);
        while (i > 1 && heap[parent(i)].compareTo(heap[i]) == 1) {
            switchWeightedVertexs(i, parent(i));
            i = parent(i);
        }
    }

    private boolean greater(E a, E b) {
        return compare(a, b) == 1;
    }

    private int compare(E w1, E w2) {
        if (w1 instanceof Float) {
            Float a = Float.valueOf(w1.floatValue());
            Float b = Float.valueOf(w2.floatValue());
            return a.compareTo(b);
        } else if (w1 instanceof Double) {
            Double a = Double.valueOf(w1.doubleValue());
            Double b = Double.valueOf(w2.doubleValue());
            return a.compareTo(b);
        } else if (w1 instanceof Long) {
            Long a = Long.valueOf(w1.longValue());
            Long b = Long.valueOf(w2.longValue());
            return a.compareTo(b);
        } else if (w1 instanceof Short) {
            Short a = Short.valueOf(w1.shortValue());
            Short b = Short.valueOf(w2.shortValue());
            return a.compareTo(b);
        }
        // base case; if none of the cases above applied
        Integer a = Integer.valueOf(w1.intValue());
        Integer b = Integer.valueOf(w2.intValue());
        return a.compareTo(b);
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[vertex]: heap index -> key\n");
        s.append("============================\n");
        for (int i = 0; i < n; i++) {
            WeightedVertex<T, E> v = heap[heapIndex[i]];
            E key = v.key;
            String k;
            if (v.infinity == WeightedVertex.POSITIVE_INFINITY) {
                k = "positive infinity";
            } else if (v.infinity == WeightedVertex.NEGATIVE_INFINITY) {
                k = "negative infinity";
            } else {
                k = key.toString();
            }
            s.append("[" + i + "]: " + heapIndex[i] + " -> " + k + "\n");
        }
        return s.toString();
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        System.out.println("Min-Heap Priority Queue: ");
        Integer[] keys = {7, 2, 8, 9, 10, 1, 3, 6, 4, 5};
        LinkedList<WeightedVertex<Vertex, Integer>> items = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            WeightedVertex<Vertex, Integer> v = 
                    new WeightedVertex<>(new Vertex(i));
            v.parent = WeightedVertex.NIL;
            v.setKey(keys[i]);
            items.add(v);
        }
        PrimMinPriorityQueue<Vertex, Integer> Q = 
                new PrimMinPriorityQueue<>(items, 10);
        System.out.println(Q);
        while (!Q.isEmpty()) {
            System.out.println("Extract min: " + Q.extractMin().key);
        }
    }
}
