import java.util.Arrays;

public class MaxPriorityQueue<E extends Comparable<E>> {
    private final int length; // the length of the heap array
    private int size; // number of elements in the heap
    private final E[] heap;

    @SuppressWarnings("unchecked")
    public MaxPriorityQueue(int length) {
        this.length = length;
        this.heap = (E[]) new Comparable[length];
    }

    private void maxHeapify(int i) {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException("out of bounds");
        }
        int l = left(i);
        int r = right(i);
        int largest = i;
        if (l < size && heap[l].compareTo(heap[i]) > 0) {
            largest = l;
        }
        if (r < size && heap[r].compareTo(heap[largest]) > 0) {
            largest = r;
        }
        if (largest != i) {
            exchange(i, largest);
            maxHeapify(largest);
        }
    }

    public void insertKey(E key) {
        if (size >= length) {
            throw new IllegalStateException("heap is full");
        }
        heap[size] = key;
        increaseKey(size);
        size++;
    }

    private void increaseKey(int i) {
        while (i > 0 && heap[parent(i)].compareTo(heap[i]) < 0) {
            exchange(i, parent(i));
            i = parent(i);
        }
    }

    public E maximum() {
        return heap[0];
    }

    public E extractMax() {
        if (isEmpty()) {
            throw new IllegalStateException("heap is empty");
        }
        if (size == 1) {
            return heap[--size];
        }
        E max = heap[0];
        heap[0] = heap[size - 1];
        size--;
        maxHeapify(0);
        return max;
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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            builder.append(heap[i]);
            builder.append(", ");
        }
        builder.append(heap[size - 1]);
        builder.append("]");
        return builder.toString();
    }

    public static void main(String[] args) {
        MaxPriorityQueue<Integer> q = new MaxPriorityQueue<>(10);
        for (int a : new int[] {3, 9, 6, 2, 5, 8, 1, 7, 4}) {
            q.insertKey(a);
        }
        System.out.println(q);
        System.out.println("size: " + q.size());
        while (!q.isEmpty()) {
            System.out.println("Extracting maximum: " + q.maximum());
            System.out.print("-[" + q.extractMax() + "]: ");
            System.out.println(q);
        }
    }
}
