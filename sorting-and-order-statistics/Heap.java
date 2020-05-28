import java.util.Arrays;

public abstract class Heap<E extends Comparable<E>> {
    private final int length; // the length of the heap array
    private int size; // number of elements in the heap
    private final E[] heap;

    @SuppressWarnings("unchecked")
    public Heap(int length) {
        this.length = length;
        this.heap = (E[]) new Comparable[length];
    }

    public Heap(E[] items) {
        this.length = items.length;
        this.size = items.length;
        this.heap = items;
        buildHeap();
    }

    /**
     * Builds a heap from an array of elements.
     * Running time is <em>O(n)</em>.
     */
    private void buildHeap() {
        for (int i = size / 2; i >= 0; i--) {
            heapify(i);
        }
    }

    private void heapify(int i) {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException("out of bounds");
        }
        int l = left(i);
        int r = right(i);
        int exchangeItemIndex = i;
        if (l < size && shouldExchange(heap[l], heap[i])) {
            exchangeItemIndex = l;
        }
        if (r < size && shouldExchange(heap[r], heap[exchangeItemIndex])) {
            exchangeItemIndex = r;
        }
        if (exchangeItemIndex != i) {
            exchange(i, exchangeItemIndex);
            heapify(exchangeItemIndex);
        }
    }

    protected abstract boolean shouldExchange(E a, E b);

    /**
     * Heap-sort algorithm.
     * Sorts the elements by swapping the largest elment in the root with the
     * last element in the heap, then it heapifies the new root replacing it 
     * with the second largest element in the heap. At each step it reduces
     * the size of the heap by one so that the largest elements are sorted at
     * the end of the heap unchanged by the heapify operation. It repeat the 
     * process until it reaches the root at which point the heap will be 
     * sorted in ascending order.
     * Runnint time is <em>O(nlgn)</em>.
     */
    public void sort() {
        int originalSize = size;
        // iterate through heap elements from last to second, skipping the
        // first element (root). The root is skipped because it would be the 
        // only element left in the heap at that point and there's no need 
        // to heapify it
        for (int i = size - 1; i >= 1; i--) {
            exchange(i, 0);
            size--;
            heapify(0);
        }
        this.size = originalSize;
    }

    private static int parent(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("invalid index " + i);
        }
        return (i - 1) / 2;
    }

    private static int left(int i) {
        return 2 * i + 1;
    }

    private static int right(int i) {
        return 2 * i + 2;
    }

    private void exchange(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    @Override
    public String toString() {
        return Arrays.toString(heap);
    }
}
