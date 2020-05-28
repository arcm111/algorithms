import java.util.Arrays;

public class MinPriorityQueue<E extends Comparable<E>> {
    private final int length; // the length of the heap array
    private int size; // number of elements in the heap
    private final E[] heap;

    @SuppressWarnings("unchecked")
    public MinPriorityQueue(int length) {
        this.length = length;
        this.heap = (E[]) new Comparable[length];
    }

    private void minHeapify(int i) {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException("out of bounds");
        }
        int l = left(i);
        int r = right(i);
        int smallest = i;
        if (l < size && heap[l].compareTo(heap[i]) < 0) {
            smallest = l;
        }
        if (r < size && heap[r].compareTo(heap[smallest]) < 0) {
            smallest = r;
        }
        if (smallest != i) {
            exchange(i, smallest);
            minHeapify(smallest);
        }
    }

    public void insertKey(E key) {
        if (size >= length) {
            throw new IllegalStateException("heap is full");
        }
        heap[size] = key;
        decreaseKey(size);
        size++;
    }

    private void decreaseKey(int i) {
        while (i > 0 && heap[parent(i)].compareTo(heap[i]) > 0) {
            exchange(i, parent(i));
            i = parent(i);
        }
    }

    public E minimum() {
        return heap[0];
    }

    public E extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("heap is empty");
        }
        if (size == 1) {
            return heap[--size];
        }
        E min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        minHeapify(0);
        return min;
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
        MinPriorityQueue<Integer> q = new MinPriorityQueue<>(10);
        for (int a : new int[] {3, 9, 6, 2, 5, 8, 1, 7, 4}) {
            q.insertKey(a);
        }
        System.out.println(q);
        System.out.println("size: " + q.size());
        while (!q.isEmpty()) {
            System.out.println("Extracting minimum: " + q.minimum());
            System.out.print("-[" + q.extractMin() + "]: ");
            System.out.println(q);
        }
    }
}
